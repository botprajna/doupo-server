"use strict";

const fs = require("fs");
const path = require("path");
const { Schema, Decoder } = require("./har_decode.js");

const dir =
  process.argv[2] ||
  String.raw`D:\doudi-resources\doupo-server\dpcq-game-server\src\main\resources\wutan-battlelog`;
const schema = new Schema();
schema.load();
const decoder = new Decoder(schema);
const id = process.argv[3] || "10300101";
const data = fs.readFileSync(path.join(dir, id + ".bin"));
const vo = decoder.decodeMessage("BattleLogVO", data);
const entries = vo.entryList || [];
let skills = {};
let firstMonster = null;
let packetCounts = {};
for (const e of entries) {
  for (const item of e.itemList || []) {
    packetCounts[item.packetId] = (packetCounts[item.packetId] || 0) + 1;
    if (item.packetId === 50804 && item.data && !firstMonster) {
      const vis = decoder.decodeMessage(
        "BattleLogUpdateVisibleResp",
        Buffer.from(item.data, "hex")
      );
      const u = (vis.sceneUpdateVisibleResp?.visibleList || [])[0];
      if (u?.sceneMonsterVo) {
        firstMonster = {
          time: e.time,
          template: u.sceneMonsterVo.templateId,
          monsterId: u.sceneMonsterVo.monsterId,
          x: u.baseInfoVo?.x,
          z: u.baseInfoVo?.z,
        };
      }
    }
    if ((item.packetId === 50762 || item.packetId === 50763) && item.data) {
      const name = item.packetId === 50762 ? "UseSkillResp" : "SkillActionResp";
      const d = decoder.decodeMessage(name, Buffer.from(item.data, "hex"));
      const sid = String(d.skillId || 0);
      skills[sid] = (skills[sid] || 0) + 1;
    }
  }
}
console.log("entries", entries.length, "packets", packetCounts);
console.log("firstMonster", firstMonster);
console.log("skills", skills);
