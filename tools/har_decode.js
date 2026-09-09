"use strict";

const fs = require("fs");
const path = require("path");

const PROTO_ROOT = String.raw`D:\doudi-resources\client-analysis\decompiled-gameplay-runtime`;

const PRIMITIVES = {
  string: "string",
  int: "int32",
  uint: "uint32",
  short: "int32",
  ushort: "uint32",
  byte: "int32",
  sbyte: "int32",
  bytes: "bytes",
  long: "int64",
  ulong: "uint64",
  bool: "bool",
  float: "float",
  double: "double",
};

const MEMBER_RE = /\[ProtoMember\((\d+)([^)]*)\)\]/g;
const NAME_ARG_RE = /Name\s*=\s*"([^"]*)"/;
const PACKED_ARG_RE = /IsPacked\s*=\s*true/;
const PROP_RE =
  /public\s+(?:static\s+)?(?:readonly\s+)?(?:List<(?<list_inner>\w+)>\s+(?<list_name>\w+)|byte\[\]\s+(?<bytes_name>\w+)|(?<ty>\w+)\s+(?<name>\w+))/;
const CLASS_RE = /public\s+(?:sealed\s+)?(?:partial\s+)?class\s+(\w+)/g;
const ENUM_RE = /public\s+enum\s+(\w+)/g;
const ENUM_VALUE_RE = /^\s*(\w+)\s*(?:=\s*(-?\d+))?\s*,?/;
const REGISTER_RE = /RegisterProtocolIdAndType\((\d+),\s*typeof\((\w+)\)\)/g;

function camelCase(name) {
  const parts = name.split("_").filter(Boolean);
  return parts
    .map((part, i) => (i === 0 ? part[0].toLowerCase() + part.slice(1) : part[0].toUpperCase() + part.slice(1)))
    .join("");
}

function jsonFieldName(nameAttr, propName) {
  if (nameAttr) return camelCase(nameAttr);
  if (!propName) return propName;
  return propName[0].toLowerCase() + propName.slice(1);
}

function shouldPrefixEnum(names) {
  return names.length > 0 && names.every((n) => /^[A-Z][A-Z0-9]*$/.test(n));
}

function matchingBrace(text, openIdx) {
  let depth = 0;
  for (let i = openIdx; i < text.length; i++) {
    const ch = text[i];
    if (ch === "{") depth++;
    else if (ch === "}") {
      depth--;
      if (depth === 0) return i;
    } else if (ch === '"') {
      i++;
      while (i < text.length) {
        if (text[i] === "\\") {
          i += 2;
          continue;
        }
        if (text[i] === '"') break;
        i++;
      }
    }
  }
  return text.length - 1;
}

function walkCsFiles(dir, out = []) {
  for (const ent of fs.readdirSync(dir, { withFileTypes: true })) {
    const p = path.join(dir, ent.name);
    if (ent.isDirectory()) walkCsFiles(p, out);
    else if (ent.name.endsWith(".cs")) out.push(p);
  }
  return out;
}

class Schema {
  constructor(protoRoot = PROTO_ROOT) {
    this.protoRoot = protoRoot;
    this.messages = new Map();
    this.enums = new Map();
    this.enumPrefix = new Map();
    this.protocolNames = new Map();
  }

  load() {
    for (const file of walkCsFiles(this.protoRoot)) {
      const text = fs.readFileSync(file, "utf8");
      if (!text.includes("[ProtoContract]") && !text.includes("[ProtoMember")) continue;
      this.parseFile(text);
    }
    const defineText = fs.readFileSync(path.join(this.protoRoot, "SGEngine.GameplayCustom", "ProtocolDefine.cs"), "utf8");
    for (const m of defineText.matchAll(REGISTER_RE)) {
      this.protocolNames.set(Number(m[1]), m[2]);
    }
  }

  parseFile(text) {
    const hits = [];
    for (const m of text.matchAll(CLASS_RE)) hits.push({ kind: "class", name: m[1], index: m.index, end: m.index + m[0].length });
    for (const m of text.matchAll(ENUM_RE)) hits.push({ kind: "enum", name: m[1], index: m.index, end: m.index + m[0].length });
    hits.sort((a, b) => a.index - b.index);
    for (const hit of hits) {
      const brace = text.indexOf("{", hit.end);
      if (brace < 0) continue;
      const end = matchingBrace(text, brace);
      const body = text.slice(brace + 1, end);
      if (hit.kind === "enum") this.parseEnum(hit.name, body);
      else this.parseMessage(hit.name, body);
    }
  }

  parseEnum(name, body) {
    const mapping = new Map();
    let auto = 0;
    for (const line of body.split(/\r?\n/)) {
      const stripped = line.split("//", 1)[0].trim();
      if (!stripped || stripped.startsWith("[")) continue;
      const m = stripped.match(ENUM_VALUE_RE);
      if (!m) continue;
      const ident = m[1];
      if (ident === "get" || ident === "set") continue;
      if (m[2] != null) auto = Number(m[2]);
      mapping.set(auto, ident);
      auto += 1;
    }
    if (mapping.size) {
      this.enums.set(name, mapping);
      this.enumPrefix.set(name, shouldPrefixEnum([...mapping.values()]));
    }
  }

  parseMessage(name, body) {
    const fields = new Map();
    MEMBER_RE.lastIndex = 0;
    let m;
    while ((m = MEMBER_RE.exec(body))) {
      const num = Number(m[1]);
      const args = m[2] || "";
      const nameAttr = (args.match(NAME_ARG_RE) || [])[1];
      const packed = PACKED_ARG_RE.test(args);
      const rest = body.slice(m.index + m[0].length, m.index + m[0].length + 400);
      const pm = rest.match(PROP_RE);
      if (!pm || !pm.groups) continue;
      const repeated = Boolean(pm.groups.list_inner) || packed;
      const ty = pm.groups.bytes_name ? "bytes" : pm.groups.list_inner || pm.groups.ty;
      const prop = pm.groups.list_name || pm.groups.bytes_name || pm.groups.name;
      fields.set(num, {
        name: jsonFieldName(nameAttr, prop),
        type: ty,
        repeated,
        packed,
      });
    }
    this.messages.set(name, fields);
  }

  formatEnum(enumName, value) {
    const mapping = this.enums.get(enumName);
    if (!mapping) return value;
    const ident = mapping.get(value);
    if (ident == null) return value;
    if (this.enumPrefix.get(enumName)) return `${enumName}_${ident}`;
    return ident;
  }
}

function decodeVarint(buf, i) {
  let shift = 0;
  let n = 0n;
  while (true) {
    if (i >= buf.length) throw new Error("truncated varint");
    const b = buf[i++];
    n |= BigInt(b & 0x7f) << BigInt(shift);
    if (b < 0x80) return [n, i];
    shift += 7;
    if (shift > 70) throw new Error("varint too long");
  }
}

function toSigned64(n) {
  const big = typeof n === "bigint" ? n : BigInt(n);
  if (big >= 1n << 63n) return big - (1n << 64n);
  return big;
}

function toSigned32(n) {
  let v = Number(toSigned64(n));
  v |= 0;
  return v;
}

function decodeKey(buf, i) {
  const [key, ni] = decodeVarint(buf, i);
  const k = Number(key);
  return [k >>> 3, k & 7, ni];
}

function skipField(buf, i, wire) {
  if (wire === 0) {
    const r = decodeVarint(buf, i);
    return r[1];
  }
  if (wire === 1) return i + 8;
  if (wire === 2) {
    const [ln, ni] = decodeVarint(buf, i);
    return ni + Number(ln);
  }
  if (wire === 5) return i + 4;
  if (wire === 3) {
    while (true) {
      const [fn, w, ni] = decodeKey(buf, i);
      i = ni;
      if (w === 4 || fn === 0) return i;
      i = skipField(buf, i, w);
    }
  }
  throw new Error(`unknown wire type ${wire}`);
}

function readPackedVarints(buf) {
  const out = [];
  let i = 0;
  while (i < buf.length) {
    const [n, ni] = decodeVarint(buf, i);
    out.push(n);
    i = ni;
  }
  return out;
}

class Decoder {
  constructor(schema) {
    this.schema = schema;
  }

  decodeMessage(typeName, buf) {
    const fields = this.schema.messages.get(typeName);
    if (!fields) return this.decodeUnknown(buf);
    const result = {};
    let i = 0;
    while (i < buf.length) {
      const [fieldNo, wire, ni] = decodeKey(buf, i);
      i = ni;
      const spec = fields.get(fieldNo);
      if (!spec) {
        i = skipField(buf, i, wire);
        continue;
      }
      const [value, nj] = this.readValue(spec, wire, buf, i);
      i = nj;
      const key = spec.name;
      if (spec.repeated) {
        if (!Array.isArray(result[key])) result[key] = [];
        if (Array.isArray(value)) result[key].push(...value);
        else result[key].push(value);
      } else {
        result[key] = value;
      }
    }
    return result;
  }

  readValue(spec, wire, buf, i) {
    const ty = spec.type;
    const kind = this.kind(ty);
    if (wire === 2) {
      const [lnBig, ni] = decodeVarint(buf, i);
      i = ni;
      const ln = Number(lnBig);
      const chunk = buf.subarray(i, i + ln);
      i += ln;
      if (kind === "string") return [chunk.toString("utf8"), i];
      if (kind === "bytes") return [chunk.toString("hex"), i];
      if (kind === "message") return [this.decodeMessage(ty, chunk), i];
      if (
        spec.repeated ||
        spec.packed ||
        ["int32", "int64", "uint32", "uint64", "bool", "enum"].includes(kind)
      ) {
        return [readPackedVarints(chunk).map((n) => this.convertScalar(kind, ty, n)), i];
      }
      if (kind === "float" && ln === 4) return [buf.readFloatLE(i - ln), i];
      if (kind === "double" && ln === 8) return [buf.readDoubleLE(i - ln), i];
      return [chunk.toString("hex"), i];
    }
    if (wire === 0) {
      const [n, ni] = decodeVarint(buf, i);
      return [this.convertScalar(kind, ty, n), ni];
    }
    if (wire === 1) {
      if (kind === "double") return [buf.readDoubleLE(i), i + 8];
      if (kind === "int64") return [buf.readBigInt64LE(i).toString(), i + 8];
      return [buf.readBigUInt64LE(i).toString(), i + 8];
    }
    if (wire === 5) {
      if (kind === "float") return [buf.readFloatLE(i), i + 4];
      return [buf.readInt32LE(i), i + 4];
    }
    return [null, skipField(buf, i, wire)];
  }

  kind(ty) {
    if (PRIMITIVES[ty]) return PRIMITIVES[ty];
    if (this.schema.enums.has(ty)) return "enum";
    return "message";
  }

  convertScalar(kind, ty, n) {
    if (kind === "bool") return n !== 0n && n !== 0;
    if (kind === "enum") return this.schema.formatEnum(ty, toSigned32(n));
    if (kind === "int32") return toSigned32(n);
    if (kind === "int64") return toSigned64(n).toString();
    if (kind === "uint64") return (typeof n === "bigint" ? n : BigInt(n)).toString();
    return Number(n);
  }

  decodeUnknown(buf) {
    const result = {};
    let i = 0;
    while (i < buf.length) {
      const [fieldNo, wire, ni] = decodeKey(buf, i);
      i = ni;
      const key = String(fieldNo);
      let val;
      if (wire === 0) {
        const [n, nj] = decodeVarint(buf, i);
        i = nj;
        val = Number(toSigned64(n));
      } else if (wire === 1) {
        val = buf.readDoubleLE(i);
        i += 8;
      } else if (wire === 2) {
        const [ln, nj] = decodeVarint(buf, i);
        i = nj;
        const chunk = buf.subarray(i, i + Number(ln));
        i += Number(ln);
        const text = chunk.toString("utf8");
        val = /[\x00-\x08]/.test(text) ? chunk.toString("hex") : text;
      } else if (wire === 5) {
        val = buf.readFloatLE(i);
        i += 4;
      } else {
        i = skipField(buf, i, wire);
        continue;
      }
      if (Object.prototype.hasOwnProperty.call(result, key)) {
        if (!Array.isArray(result[key])) result[key] = [result[key]];
        result[key].push(val);
      } else result[key] = val;
    }
    return result;
  }
}

function extractWsMessages(har) {
  const out = [];
  for (const entry of har.log?.entries || []) {
    for (const key of ["_webSocketMessages", "_websocketMessages", "webSocketMessages"]) {
      if (Array.isArray(entry[key])) out.push(...entry[key]);
    }
  }
  return out;
}

function framePayload(msg) {
  const payload = msg.payload;
  if (payload && typeof payload === "object") {
    if (typeof payload.buffer === "string" && payload.buffer) {
      return [Buffer.from(payload.buffer, "base64"), null];
    }
    if (Array.isArray(payload.data)) return [Buffer.from(payload.data), null];
    if (typeof payload.data === "string" && payload.encoding === "base64") {
      return [Buffer.from(payload.data, "base64"), null];
    }
    if (payload.type === "Buffer" && Array.isArray(payload.data)) {
      return [Buffer.from(payload.data), null];
    }
    if (payload.type === 4) return [null, "WebSocket ping"];
    if (payload.type === 5) return [null, "WebSocket pong"];
    if (!payload.buffer) return [null, "缺少 payload.buffer (非 Reqable 格式帧)"];
  }
  const opcode = msg.opcode ?? 2;
  if (opcode === 1 && typeof msg.data === "string") return [null, "text frame"];
  if (typeof msg.data === "string" && msg.data) {
    try {
      return [Buffer.from(msg.data, "base64"), null];
    } catch {
      return [null, "base64 解码失败"];
    }
  }
  return [null, "缺少 payload.buffer (非 Reqable 格式帧)"];
}

function isoTime(value, fallback) {
  if (value == null) return fallback || new Date().toISOString();
  if (typeof value === "string") {
    if (value.includes("T") || value.endsWith("Z")) return value;
    const n = Number(value);
    if (!Number.isNaN(n)) value = n;
    else return value;
  }
  if (typeof value === "number") {
    let n = value;
    if (n > 1e14) n /= 1000;
    else if (n < 1e11) n *= 1000;
    return new Date(n).toISOString();
  }
  return String(value);
}

function parseFrame(payload, direction, authEnabled) {
  if (payload.length < 8) throw new Error("frame too short");
  const lenField = payload.readUInt32BE(0);
  const protocolId = payload.readUInt32BE(4);
  const hasChecksum = direction === "send" && authEnabled && payload.length >= 12;
  let checksum = null;
  let offset = 8;
  if (hasChecksum) {
    checksum = payload.readInt32BE(8);
    offset = 12;
  }
  const body = payload.subarray(offset);
  return {
    protocolId,
    protocolIdHex: "0x" + protocolId.toString(16),
    lenField,
    hasChecksum,
    checksum,
    bodyLen: body.length,
    body,
  };
}

function directionOf(msg) {
  if (msg.flow === 0 || msg.flow === "0") return "send";
  if (msg.flow === 1 || msg.flow === "1") return "recv";
  const t = String(msg.type || msg.direction || "").toLowerCase();
  if (t === "send" || t === "sent") return "send";
  if (t === "receive" || t === "recv" || t === "received") return "recv";
  return t || "recv";
}

function decodeHar(harPath, outPath) {
  const schema = new Schema();
  schema.load();
  const decoder = new Decoder(schema);
  const har = JSON.parse(fs.readFileSync(harPath, "utf8"));
  const wsMsgs = extractWsMessages(har);
  const started = har.log?.entries?.[0]?.startedDateTime;
  const messages = [];
  const stats = { totalFrames: 0, send: 0, recv: 0, ok: 0, unknown: 0, error: 0, nonbinary: 0 };
  let authEnabled = false;

  for (let idx = 0; idx < wsMsgs.length; idx++) {
    const msg = wsMsgs[idx];
    stats.totalFrames++;
    const direction = directionOf(msg);
    stats[direction] = (stats[direction] || 0) + 1;
    const rec = {
      idx,
      time: isoTime(msg.timestamp ?? msg.time, started),
      direction,
    };
    const [payload, err] = framePayload(msg);
    const opcode = msg.opcode;
    if (!payload || (opcode != null && opcode !== 2)) {
      rec.status = "nonbinary";
      rec.error = err || "非二进制帧";
      rec.decoded = null;
      stats.nonbinary++;
      messages.push(rec);
      continue;
    }
    let frame;
    try {
      frame = parseFrame(payload, direction, authEnabled);
    } catch (exc) {
      rec.status = "error";
      rec.error = String(exc.message || exc);
      rec.decoded = null;
      stats.error++;
      messages.push(rec);
      continue;
    }
    const protocolId = frame.protocolId;
    rec.protocolId = protocolId;
    rec.protocolIdHex = frame.protocolIdHex;
    rec.lenField = frame.lenField;
    rec.hasChecksum = frame.hasChecksum;
    rec.checksum = frame.checksum;
    rec.bodyLen = frame.bodyLen;
    const name = schema.protocolNames.get(protocolId);
    if (name) {
      rec.name = name;
      const ordered = {
        idx: rec.idx,
        time: rec.time,
        direction: rec.direction,
        name: rec.name,
        protocolId: rec.protocolId,
        protocolIdHex: rec.protocolIdHex,
        lenField: rec.lenField,
        hasChecksum: rec.hasChecksum,
        checksum: rec.checksum,
        bodyLen: rec.bodyLen,
      };
      try {
        ordered.status = "ok";
        ordered.decoded = decoder.decodeMessage(name, frame.body);
        stats.ok++;
      } catch (exc) {
        ordered.status = "error";
        ordered.error = String(exc.message || exc);
        ordered.decoded = null;
        stats.error++;
      }
      if (protocolId === 50052) authEnabled = true;
      messages.push(ordered);
    } else {
      rec.status = "unknown";
      rec.error = `未知协议 ID ${protocolId}`;
      rec.decoded = null;
      stats.unknown++;
      if (protocolId === 50052) authEnabled = true;
      messages.push(rec);
    }
  }

  const result = {
    fileName: path.basename(harPath),
    parsedAt: new Date().toISOString(),
    stats,
    messages,
  };
  fs.writeFileSync(outPath, JSON.stringify(result, null, 2), "utf8");
  return { stats, sample: messages.slice(0, 3), protocolCount: schema.protocolNames.size };
}

function inspectHar(harPath) {
  const har = JSON.parse(fs.readFileSync(harPath, "utf8"));
  const msgs = extractWsMessages(har);
  console.log(`entries=${har.log?.entries?.length || 0} ws=${msgs.length}`);
  if (msgs[0]) {
    console.log("first_keys", Object.keys(msgs[0]));
    const sample = { ...msgs[0] };
    if (typeof sample.data === "string" && sample.data.length > 80) sample.data = sample.data.slice(0, 80) + "...";
    if (sample.payload && typeof sample.payload.buffer === "string") {
      sample.payload = { ...sample.payload, buffer: sample.payload.buffer.slice(0, 80) + "..." };
    }
    console.log("first_msg", JSON.stringify(sample).slice(0, 1500));
  }
}

module.exports = {
  Schema,
  Decoder,
  parseFrame,
  framePayload,
  extractWsMessages,
  directionOf,
  isoTime,
  decodeHar,
};

if (require.main === module) {
  const harPath = process.argv[2] || String.raw`c:\Users\ppp\Desktop\imlj2-cn-gamewx.lansors.com_2026_09_07_19_03_22.har`;
  const outPath = process.argv[3] || harPath.replace(/\.har$/i, ".json");
  inspectHar(harPath);
  const { stats, sample, protocolCount } = decodeHar(harPath, outPath);
  console.log("protocols", protocolCount);
  console.log("wrote", outPath);
  console.log("stats", JSON.stringify(stats));
  console.log("sample", JSON.stringify(sample, null, 2).slice(0, 4000));
}
