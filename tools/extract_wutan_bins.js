"use strict";

const fs = require("fs");
const path = require("path");
const {
  Schema,
  Decoder,
  parseFrame,
  framePayload,
  extractWsMessages,
  directionOf,
} = require("./har_decode.js");

const HAR =
  process.argv[2] ||
  String.raw`c:\Users\ppp\Desktop\imlj2-cn-gamewx.lansors.com_2026_09_07_19_03_22.har`;
const OUT_DIR =
  process.argv[3] ||
  String.raw`D:\doudi-resources\doupo-server\dpcq-game-server\src\main\resources\wutan-battlelog`;

const CHAPTERS = process.argv[4] ? process.argv[4].split(',').map(Number) : [
  10300101, 10300102, 10300103, 10300105, 10300201, 10300202, 10300203,
  10300205, 10300301, 10300302, 10300303, 10300305, 10300401, 10300402,
  10300403, 10300405,
];

function hexToBuf(hex) {
  return Buffer.from(hex || "", "hex");
}

function main() {
  const schema = new Schema();
  schema.load();
  const decoder = new Decoder(schema);
  const har = JSON.parse(fs.readFileSync(HAR, "utf8"));
  const msgs = extractWsMessages(har);
  fs.mkdirSync(OUT_DIR, { recursive: true });

  const lastStart = new Map();
  const saved = new Set();
  let authEnabled = false;
  for (let idx = 0; idx < msgs.length; idx++) {
    const direction = directionOf(msgs[idx]);
    const [payload] = framePayload(msgs[idx]);
    if (!payload) continue;
    let frame;
    try {
      frame = parseFrame(payload, direction, authEnabled);
    } catch {
      continue;
    }
    if (frame.protocolId === 50052) {
      authEnabled = true;
    }
    if (frame.protocolId === 61962 || frame.protocolId === 75252) {
      try {
        const req = decoder.decodeMessage(frame.protocolId === 61962
          ? "MainMapStartFightReq" : "FireTowerChallengeReq", frame.body);
        lastStart.set(req.mainMapChapterId || 0, {
          idx,
          x: req.x,
          y: req.y,
          z: req.z,
        });
      } catch (err) {
        // ignore malformed start-fight frames
      }
      continue;
    }
    if (frame.protocolId !== 61971 && frame.protocolId !== 75253) continue;
    let decoded;
    try {
      decoded = decoder.decodeMessage(frame.protocolId === 61971
        ? "MainMapStartFightResp" : "FireTowerChallengeResp", frame.body);
    } catch (err) {
      continue;
    }
    const chapterId = decoded.mainMapChapterId || decoded.configId;
    if (!CHAPTERS.includes(chapterId) || saved.has(chapterId)) continue;
    const log = decoded.battleLog || {};
    if (!log.data) {
      console.log("skip", chapterId, "no data bytes");
      continue;
    }
    const data = hexToBuf(log.data);
    const origin = lastStart.get(frame.protocolId === 61971 ? chapterId : 0)
      || { x: 0, y: 0, z: 0, idx: -1 };
    const stats = log.fightStatisticsResp || {};
    const lines = [
      "chapterId=" + chapterId,
      "fightIdx=" + idx,
      "originIdx=" + origin.idx,
      "originX=" + (origin.x || 0),
      "originY=" + (origin.y || 0),
      "originZ=" + (origin.z || 0),
      "speed=" + (log.speed || 1.1),
      "startTime=" + Number(log.startTime || 66),
      "win=" + (!!stats.win),
      "dataBytes=" + data.length,
    ];
    function addSide(prefix, rows) {
      lines.push(prefix + ".count=" + rows.length);
      rows.forEach((row, i) => {
        lines.push(
          prefix +
            "." +
            i +
            "=" +
            [
              row.essenceFireId || 0,
              row.heroJobId || 0,
              row.monsterId || 0,
              row.level || 0,
              row.damage || 0,
              row.takeDamage || 0,
              row.cure || 0,
            ].join(",")
        );
      });
    }
    addSide("winner", stats.winner || []);
    addSide("loser", stats.loser || []);
    fs.writeFileSync(path.join(OUT_DIR, chapterId + ".bin"), data);
    fs.writeFileSync(
      path.join(OUT_DIR, chapterId + ".meta"),
      lines.join("\n") + "\n"
    );
    saved.add(chapterId);
    console.log(
      "saved",
      chapterId,
      "idx",
      idx,
      "bytes",
      data.length,
      "origin",
      origin.x,
      origin.z,
      "skills later in java"
    );
  }
  for (const id of CHAPTERS) {
    if (!saved.has(id)) console.log("MISSING", id);
  }
}

main();
