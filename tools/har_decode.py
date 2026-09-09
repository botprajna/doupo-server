#!/usr/bin/env python3
"""Decode a Reqable/Chrome HAR of Doudi WebSocket frames into named JSON."""

from __future__ import annotations

import json
import os
import re
import struct
import sys
from datetime import datetime, timezone
from pathlib import Path
from typing import Any, Dict, List, Optional, Tuple

PROTO_ROOT = Path(r"D:\doudi-resources\client-analysis\decompiled-gameplay-runtime")
PROTOCOL_DEFINE = PROTO_ROOT / "SGEngine.GameplayCustom" / "ProtocolDefine.cs"

PRIMITIVES = {
    "string": "string",
    "int": "int32",
    "uint": "uint32",
    "short": "int32",
    "ushort": "uint32",
    "byte": "int32",
    "sbyte": "int32",
    "long": "int64",
    "ulong": "uint64",
    "bool": "bool",
    "float": "float",
    "double": "double",
}

MEMBER_RE = re.compile(r"\[ProtoMember\((?P<num>\d+)(?P<args>[^)]*)\)\]")
NAME_ARG_RE = re.compile(r'Name\s*=\s*"([^"]*)"')
PACKED_ARG_RE = re.compile(r"IsPacked\s*=\s*true")
PROP_RE = re.compile(
    r"public\s+(?:static\s+)?(?:readonly\s+)?"
    r"(?:List<(?P<list_inner>\w+)>\s+(?P<list_name>\w+)"
    r"|(?P<ty>\w+)\s+(?P<name>\w+))"
)
CLASS_RE = re.compile(r"public\s+(?:sealed\s+)?(?:partial\s+)?class\s+(\w+)")
ENUM_RE = re.compile(r"public\s+enum\s+(\w+)")
ENUM_VALUE_RE = re.compile(r"^\s*(\w+)\s*(?:=\s*(-?\d+))?\s*,?")
REGISTER_RE = re.compile(
    r"RegisterProtocolIdAndType\((\d+),\s*typeof\((\w+)\)\)"
)


def camel_case(name: str) -> str:
    parts = name.split("_")
    out = []
    for i, part in enumerate(parts):
        if not part:
            continue
        if i == 0:
            out.append(part[:1].lower() + part[1:])
        else:
            out.append(part[:1].upper() + part[1:])
    return "".join(out) if out else name


def json_field_name(name_attr: Optional[str], prop_name: str) -> str:
    if name_attr:
        return camel_case(name_attr)
    if not prop_name:
        return prop_name
    return prop_name[:1].lower() + prop_name[1:]


def should_prefix_enum(names: List[str]) -> bool:
    return bool(names) and all(re.fullmatch(r"[A-Z][A-Z0-9]*", n) for n in names)


class Schema:
    def __init__(self) -> None:
        self.messages: Dict[str, Dict[int, dict]] = {}
        self.enums: Dict[str, Dict[int, str]] = {}
        self.enum_prefix: Dict[str, bool] = {}
        self.protocol_names: Dict[int, str] = {}

    def load(self) -> None:
        self._load_csharp()
        text = PROTOCOL_DEFINE.read_text(encoding="utf-8", errors="ignore")
        for m in REGISTER_RE.finditer(text):
            self.protocol_names[int(m.group(1))] = m.group(2)

    def _load_csharp(self) -> None:
        for path in PROTO_ROOT.rglob("*.cs"):
            try:
                text = path.read_text(encoding="utf-8", errors="ignore")
            except OSError:
                continue
            if "[ProtoContract]" not in text and "[ProtoMember" not in text:
                continue
            self._parse_file(text)

    def _parse_file(self, text: str) -> None:
        i = 0
        n = len(text)
        while i < n:
            cls = CLASS_RE.search(text, i)
            enm = ENUM_RE.search(text, i)
            if cls and enm:
                nxt = cls if cls.start() < enm.start() else enm
            else:
                nxt = cls or enm
            if not nxt:
                break
            if nxt.re is ENUM_RE:
                name = nxt.group(1)
                brace = text.find("{", nxt.end())
                if brace < 0:
                    i = nxt.end()
                    continue
                end = _matching_brace(text, brace)
                self._parse_enum(name, text[brace + 1 : end])
                i = end + 1
            else:
                name = nxt.group(1)
                brace = text.find("{", nxt.end())
                if brace < 0:
                    i = nxt.end()
                    continue
                end = _matching_brace(text, brace)
                self._parse_message(name, text[brace + 1 : end])
                i = end + 1

    def _parse_enum(self, name: str, body: str) -> None:
        mapping: Dict[int, str] = {}
        auto = 0
        for line in body.splitlines():
            stripped = line.split("//", 1)[0].strip()
            if not stripped or stripped.startswith("["):
                continue
            m = ENUM_VALUE_RE.match(stripped)
            if not m:
                continue
            ident = m.group(1)
            if ident in ("get", "set"):
                continue
            if m.group(2) is not None:
                auto = int(m.group(2))
            mapping[auto] = ident
            auto += 1
        if mapping:
            self.enums[name] = mapping
            self.enum_prefix[name] = should_prefix_enum(list(mapping.values()))

    def _parse_message(self, name: str, body: str) -> None:
        fields: Dict[int, dict] = {}
        for m in MEMBER_RE.finditer(body):
            num = int(m.group("num"))
            args = m.group("args") or ""
            name_attr = None
            nm = NAME_ARG_RE.search(args)
            if nm:
                name_attr = nm.group(1)
            packed = bool(PACKED_ARG_RE.search(args))
            rest = body[m.end() : m.end() + 400]
            pm = PROP_RE.search(rest)
            if not pm:
                continue
            if pm.group("list_inner"):
                ty = pm.group("list_inner")
                prop = pm.group("list_name")
                repeated = True
            else:
                ty = pm.group("ty")
                prop = pm.group("name")
                repeated = False
            fields[num] = {
                "name": json_field_name(name_attr, prop),
                "type": ty,
                "repeated": repeated or packed,
                "packed": packed,
            }
        if fields:
            self.messages[name] = fields

    def format_enum(self, enum_name: str, value: int) -> Any:
        mapping = self.enums.get(enum_name)
        if not mapping:
            return value
        ident = mapping.get(value)
        if ident is None:
            return value
        if self.enum_prefix.get(enum_name):
            return f"{enum_name}_{ident}"
        return ident


def _matching_brace(text: str, open_idx: int) -> int:
    depth = 0
    i = open_idx
    n = len(text)
    while i < n:
        ch = text[i]
        if ch == "{":
            depth += 1
        elif ch == "}":
            depth -= 1
            if depth == 0:
                return i
        elif ch == '"':
            i += 1
            while i < n:
                if text[i] == "\\":
                    i += 2
                    continue
                if text[i] == '"':
                    break
                i += 1
        i += 1
    return n - 1


def decode_varint(buf: bytes, i: int) -> Tuple[int, int]:
    shift = 0
    n = 0
    while True:
        if i >= len(buf):
            raise ValueError("truncated varint")
        b = buf[i]
        i += 1
        n |= (b & 0x7F) << shift
        if b < 0x80:
            return n, i
        shift += 7
        if shift > 70:
            raise ValueError("varint too long")


def to_signed64(n: int) -> int:
    if n >= 1 << 63:
        return n - (1 << 64)
    return n


def to_signed32(n: int) -> int:
    n = to_signed64(n)
    n &= 0xFFFFFFFF
    if n >= 0x80000000:
        return n - 0x100000000
    return n


def decode_key(buf: bytes, i: int) -> Tuple[int, int, int]:
    key, i = decode_varint(buf, i)
    return key >> 3, key & 7, i


def skip_field(buf: bytes, i: int, wire: int) -> int:
    if wire == 0:
        _, i = decode_varint(buf, i)
        return i
    if wire == 1:
        return i + 8
    if wire == 2:
        ln, i = decode_varint(buf, i)
        return i + ln
    if wire == 5:
        return i + 4
    if wire == 3:
        while True:
            fn, w, i = decode_key(buf, i)
            if w == 4 or fn == 0:
                return i
            i = skip_field(buf, i, w)
    raise ValueError(f"unknown wire type {wire}")


def read_packed_varints(buf: bytes) -> List[int]:
    out = []
    i = 0
    while i < len(buf):
        n, i = decode_varint(buf, i)
        out.append(n)
    return out


class Decoder:
    def __init__(self, schema: Schema) -> None:
        self.schema = schema

    def decode_message(self, type_name: str, buf: bytes) -> dict:
        fields = self.schema.messages.get(type_name)
        if fields is None:
            return self._decode_unknown_message(buf)
        result: dict = {}
        i = 0
        n = len(buf)
        while i < n:
            field_no, wire, i = decode_key(buf, i)
            spec = fields.get(field_no)
            if spec is None:
                i = skip_field(buf, i, wire)
                continue
            value, i = self._read_value(spec, wire, buf, i)
            key = spec["name"]
            if spec["repeated"]:
                lst = result.setdefault(key, [])
                if isinstance(value, list):
                    lst.extend(value)
                else:
                    lst.append(value)
            else:
                result[key] = value
        return result

    def _read_value(self, spec: dict, wire: int, buf: bytes, i: int):
        ty = spec["type"]
        kind = self._kind(ty)
        if wire == 2:
            ln, i = decode_varint(buf, i)
            chunk = buf[i : i + ln]
            i += ln
            if kind == "string":
                return chunk.decode("utf-8", errors="replace"), i
            if kind == "bytes":
                return chunk.hex(), i
            if kind == "message":
                return self.decode_message(ty, chunk), i
            if spec["repeated"] or spec["packed"] or kind in (
                "int32",
                "int64",
                "uint32",
                "uint64",
                "bool",
                "enum",
            ):
                nums = read_packed_varints(chunk)
                return [self._convert_scalar(kind, ty, n) for n in nums], i
            if kind == "float" and ln == 4:
                return struct.unpack("<f", chunk)[0], i
            if kind == "double" and ln == 8:
                return struct.unpack("<d", chunk)[0], i
            return chunk.hex(), i
        if wire == 0:
            n, i = decode_varint(buf, i)
            return self._convert_scalar(kind, ty, n), i
        if wire == 1:
            chunk = buf[i : i + 8]
            i += 8
            if kind == "double":
                return struct.unpack("<d", chunk)[0], i
            if kind == "int64":
                return str(struct.unpack("<q", chunk)[0]), i
            return str(struct.unpack("<Q", chunk)[0]), i
        if wire == 5:
            chunk = buf[i : i + 4]
            i += 4
            if kind == "float":
                return struct.unpack("<f", chunk)[0], i
            return struct.unpack("<i", chunk)[0], i
        i = skip_field(buf, i, wire)
        return None, i

    def _kind(self, ty: str) -> str:
        if ty in PRIMITIVES:
            return PRIMITIVES[ty]
        if ty in self.schema.enums:
            return "enum"
        if ty in self.schema.messages:
            return "message"
        return "message"

    def _convert_scalar(self, kind: str, ty: str, n: int):
        if kind == "bool":
            return bool(n)
        if kind == "enum":
            return self.schema.format_enum(ty, to_signed32(n))
        if kind == "int32":
            return to_signed32(n)
        if kind == "int64":
            return str(to_signed64(n))
        if kind == "uint64":
            return str(n)
        return n

    def _decode_unknown_message(self, buf: bytes) -> dict:
        result: dict = {}
        i = 0
        n = len(buf)
        while i < n:
            field_no, wire, i = decode_key(buf, i)
            key = str(field_no)
            if wire == 0:
                val, i = decode_varint(buf, i)
                val = to_signed64(val)
            elif wire == 1:
                val = struct.unpack("<d", buf[i : i + 8])[0]
                i += 8
            elif wire == 2:
                ln, i = decode_varint(buf, i)
                chunk = buf[i : i + ln]
                i += ln
                try:
                    val = chunk.decode("utf-8")
                    if any(ord(c) < 9 for c in val):
                        val = chunk.hex()
                except UnicodeDecodeError:
                    val = chunk.hex()
            elif wire == 5:
                val = struct.unpack("<f", buf[i : i + 4])[0]
                i += 4
            else:
                i = skip_field(buf, i, wire)
                continue
            if key in result:
                prev = result[key]
                if not isinstance(prev, list):
                    prev = [prev]
                    result[key] = prev
                prev.append(val)
            else:
                result[key] = val
        return result


def extract_ws_messages(har: dict) -> List[dict]:
    out = []
    for entry in har.get("log", {}).get("entries", []):
        for key in ("_webSocketMessages", "_websocketMessages", "webSocketMessages"):
            msgs = entry.get(key)
            if msgs:
                out.extend(msgs)
    return out


def frame_payload(msg: dict) -> Tuple[Optional[bytes], Optional[str]]:
    payload = msg.get("payload")
    if isinstance(payload, dict):
        buf = payload.get("buffer")
        if isinstance(buf, str) and buf:
            import base64

            return base64.b64decode(buf), None
        data = payload.get("data")
        if isinstance(data, list):
            return bytes(data), None
        if isinstance(data, str) and payload.get("encoding") == "base64":
            import base64

            return base64.b64decode(data), None
        if payload.get("type") == "Buffer" and isinstance(payload.get("data"), list):
            return bytes(payload["data"]), None
        if not buf:
            return None, "缺少 payload.buffer (非 Reqable 格式帧)"
    data = msg.get("data")
    opcode = msg.get("opcode", 2)
    if opcode in (1,) and isinstance(data, str):
        return None, "text frame"
    if isinstance(data, str) and data:
        import base64

        try:
            return base64.b64decode(data), None
        except Exception:
            return None, "base64 解码失败"
    return None, "缺少 payload.buffer (非 Reqable 格式帧)"


def iso_time(value: Any, fallback: Optional[str] = None) -> str:
    if value is None:
        return fallback or datetime.now(timezone.utc).strftime("%Y-%m-%dT%H:%M:%S.%f")[:-3] + "Z"
    if isinstance(value, str):
        if value.endswith("Z") or "T" in value:
            return value
        try:
            value = float(value)
        except ValueError:
            return value
    if isinstance(value, (int, float)):
        n = float(value)
        if n > 1e14:
            n /= 1e6
        elif n > 1e12:
            n /= 1e3
        elif n > 1e11:
            n /= 1e3
        dt = datetime.fromtimestamp(n, tz=timezone.utc)
        return dt.strftime("%Y-%m-%dT%H:%M:%S.%f")[:-3] + "Z"
    return str(value)


def parse_frame(payload: bytes, direction: str, auth_enabled: bool) -> dict:
    if len(payload) < 8:
        raise ValueError("frame too short")
    len_field = int.from_bytes(payload[0:4], "big")
    protocol_id = int.from_bytes(payload[4:8], "big")
    has_checksum = direction == "send" and auth_enabled and len(payload) >= 12
    checksum = None
    offset = 8
    if has_checksum:
        checksum = int.from_bytes(payload[8:12], "big")
        if checksum >= 1 << 31:
            checksum -= 1 << 32
        offset = 12
    body = payload[offset:]
    return {
        "protocolId": protocol_id,
        "protocolIdHex": hex(protocol_id),
        "lenField": len_field,
        "hasChecksum": has_checksum,
        "checksum": checksum,
        "bodyLen": len(body),
        "body": body,
    }


def direction_of(msg: dict) -> str:
    t = str(msg.get("type") or msg.get("direction") or "").lower()
    if t in ("send", "sent"):
        return "send"
    if t in ("receive", "recv", "received"):
        return "recv"
    return t or "recv"


def decode_har(har_path: Path, out_path: Path) -> dict:
    schema = Schema()
    schema.load()
    decoder = Decoder(schema)
    har = json.loads(har_path.read_text(encoding="utf-8"))
    ws_msgs = extract_ws_messages(har)
    started = None
    if har.get("log", {}).get("entries"):
        started = har["log"]["entries"][0].get("startedDateTime")

    messages = []
    stats = {
        "totalFrames": 0,
        "send": 0,
        "recv": 0,
        "ok": 0,
        "unknown": 0,
        "error": 0,
        "nonbinary": 0,
    }
    auth_enabled = False

    for idx, msg in enumerate(ws_msgs):
        stats["totalFrames"] += 1
        direction = direction_of(msg)
        stats[direction] = stats.get(direction, 0) + 1
        rec: dict = {
            "idx": idx,
            "time": iso_time(msg.get("time"), started),
            "direction": direction,
        }
        payload, err = frame_payload(msg)
        opcode = msg.get("opcode")
        if payload is None or (opcode not in (None, 2) and opcode != 2):
            rec["status"] = "nonbinary"
            rec["error"] = err or "非二进制帧"
            rec["decoded"] = None
            stats["nonbinary"] += 1
            messages.append(rec)
            continue
        try:
            frame = parse_frame(payload, direction, auth_enabled)
        except Exception as exc:
            rec["status"] = "error"
            rec["error"] = str(exc)
            rec["decoded"] = None
            stats["error"] += 1
            messages.append(rec)
            continue

        protocol_id = frame["protocolId"]
        rec.update(
            {
                "protocolId": protocol_id,
                "protocolIdHex": frame["protocolIdHex"],
                "lenField": frame["lenField"],
                "hasChecksum": frame["hasChecksum"],
                "checksum": frame["checksum"],
                "bodyLen": frame["bodyLen"],
            }
        )
        name = schema.protocol_names.get(protocol_id)
        if name:
            rec["name"] = name
            # Keep name next to direction like the original dump.
            rec = {
                k: rec[k]
                for k in (
                    "idx",
                    "time",
                    "direction",
                    "name",
                    "protocolId",
                    "protocolIdHex",
                    "lenField",
                    "hasChecksum",
                    "checksum",
                    "bodyLen",
                )
                if k in rec
            }
            try:
                decoded = decoder.decode_message(name, frame["body"])
                rec["status"] = "ok"
                rec["decoded"] = decoded
                stats["ok"] += 1
            except Exception as exc:
                rec["status"] = "error"
                rec["error"] = str(exc)
                rec["decoded"] = None
                stats["error"] += 1
            if protocol_id == 50052:
                auth_enabled = True
        else:
            rec["status"] = "unknown"
            rec["error"] = f"未知协议 ID {protocol_id}"
            rec["decoded"] = None
            stats["unknown"] += 1
            if protocol_id == 50052:
                auth_enabled = True

        messages.append(rec)

    result = {
        "fileName": har_path.name,
        "parsedAt": datetime.now(timezone.utc).strftime("%Y-%m-%dT%H:%M:%S.%f")[:-3] + "Z",
        "stats": stats,
        "messages": messages,
    }
    out_path.write_text(
        json.dumps(result, ensure_ascii=False, indent=2),
        encoding="utf-8",
    )
    return stats


def inspect_har(har_path: Path) -> None:
    har = json.loads(har_path.read_text(encoding="utf-8"))
    msgs = extract_ws_messages(har)
    print(f"entries={len(har.get('log', {}).get('entries', []))} ws={len(msgs)}")
    if msgs:
        sample = dict(msgs[0])
        if "data" in sample and isinstance(sample["data"], str) and len(sample["data"]) > 80:
            sample["data"] = sample["data"][:80] + "..."
        payload = sample.get("payload")
        if isinstance(payload, dict) and isinstance(payload.get("buffer"), str):
            payload = dict(payload)
            payload["buffer"] = payload["buffer"][:80] + "..."
            sample["payload"] = payload
        print("first_keys", sorted(msgs[0].keys()))
        print("first_msg", json.dumps(sample, ensure_ascii=False)[:1500])


if __name__ == "__main__":
    har_path = Path(
        sys.argv[1]
        if len(sys.argv) > 1
        else r"c:\Users\ppp\Desktop\imlj2-cn-gamewx.lansors.com_2026_09_07_19_03_22.har"
    )
    out_path = Path(
        sys.argv[2]
        if len(sys.argv) > 2
        else har_path.with_suffix(".json")
    )
    inspect_har(har_path)
    stats = decode_har(har_path, out_path)
    print("wrote", out_path)
    print("stats", json.dumps(stats, ensure_ascii=False))
