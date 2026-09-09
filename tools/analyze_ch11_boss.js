"use strict";

const fs = require("fs");
const j = JSON.parse(fs.readFileSync("D:\\抓包数据\\ch11-boss-battlelog.json", "utf8"));
const BOSS = "770167535477719408";
const PLAYER = "767192978761449840";

console.log("--- spawn 50804 ---");
for (const r of j.timeline.filter((x) => x.packetId === 50804)) {
  for (const u of r.units || []) {
    const keys = Object.keys(u.attrs || {});
    console.log(
      JSON.stringify({
        t: r.time,
        id: u.id,
        template: u.templateId,
        monsterId: u.monsterId,
        heroJob: u.heroJob,
        camp: u.campId,
        attrCount: keys.length,
        hp: u.attrs["103011"] || u.attrs["103001"],
        atk: u.attrs["101001"] || u.attrs["102001"] || u.attrs["101011"],
        sample: keys.slice(0, 20).map((k) => k + ":" + u.attrs[k]),
      })
    );
  }
}

console.log("\n--- 50762 UseSkill order ---");
for (const r of j.timeline.filter((x) => x.packetId === 50762)) {
  const side =
    r.attackId === BOSS ? "BOSS" : r.attackId === PLAYER ? "PLAYER" : "OTHER";
  console.log(r.time, side, r.skillId);
}

const hits = [];
for (const r of j.timeline.filter((x) => x.packetId === 50763)) {
  for (const a of r.actions || []) {
    if (a.damage == null) continue;
    hits.push({
      t: r.time,
      skill: String(a.skillId || r.skillId),
      dmg: Number(a.damage),
      type: a.damageType,
      atk: a.attackId,
      tgt: a.targetId,
      die: !!a.die,
    });
  }
}
console.log("\n--- hits", hits.length, "---");
const by = {};
let pD = 0;
let bD = 0;
for (const h of hits) {
  if (!by[h.skill]) by[h.skill] = { n: 0, sum: 0, vals: [] };
  by[h.skill].n++;
  by[h.skill].sum += h.dmg;
  by[h.skill].vals.push(h.dmg);
  if (h.atk === PLAYER) pD += h.dmg;
  if (h.atk === BOSS) bD += h.dmg;
}
console.log("playerSum", pD, "bossSum", bD);
console.log(JSON.stringify(j.fightStatistics, null, 2));
for (const [k, v] of Object.entries(by)) {
  console.log(k, "n=" + v.n, "sum=" + v.sum, "vals=" + v.vals.join(","));
}

console.log("\n--- 103011 HP ---");
for (const r of j.timeline) {
  const lists = [];
  if (r.attr) lists.push(r.attr);
  for (const a of r.actions || []) if (a.attr) lists.push(a.attr);
  for (const at of lists) {
    const hp = (at.list || []).find((x) => x.type === 103011);
    if (hp) console.log(r.time, "tgt", at.targetId, hp.value);
  }
}
