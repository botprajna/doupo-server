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
const OUT =
  process.argv[3] ||
  String.raw`D:\抓包数据\ch11-boss-battlelog.json`;

const PACKET_TYPES = {
  50756: "SceneUpdateVisibleResp",
  50757: "SceneForgetVisibleResp",
  50762: "UseSkillResp",
  50763: "SkillActionResp",
  50781: "FightResultResp",
  50798: "MpResp",
  50801: "AttributeActionVO",
  50804: "BattleLogUpdateVisibleResp",
};

function hexToBuf(hex) {
  if (!hex) return Buffer.alloc(0);
  return Buffer.from(hex, "hex");
}

function summarizeAction(action) {
  const out = { actionVoTypeId: action.actionVoTypeId };
  if (action.damageVO) {
    out.damage = action.damageVO.damage;
    out.damageType = action.damageVO.type;
    out.skillId = action.damageVO.skillId;
    out.skillBaseId = action.damageVO.skillBaseId;
    out.attackId = action.damageVO.attackId;
    out.targetId = action.damageVO.targetId;
    out.special = action.damageVO.special;
  }
  if (action.dieVO) {
    out.die = {
      monsterTemplateId: action.dieVO.monsterTemplateId,
      attackId: action.dieVO.attackId,
      targetId: action.dieVO.targetId,
    };
  }
  if (action.attributeActionVO) {
    out.attr = {
      attackId: action.attributeActionVO.attackId,
      targetId: action.attributeActionVO.targetId,
      list: (action.attributeActionVO.attrList || []).map((a) => ({
        type: a.type,
        value: a.value,
      })),
    };
  }
  if (action.cureVO) out.cure = action.cureVO;
  if (action.skillDamageVO) out.skillDamageVO = action.skillDamageVO;
  if (action.mixDamageVO) out.mixDamageVO = action.mixDamageVO;
  return out;
}

function pickUnit(visible) {
  const units = visible?.sceneUpdateVisibleResp?.visibleList || [];
    return units.map((u) => {
    const base = u.baseInfoVo || {};
    const monster = u.sceneMonsterVo || {};
    const fight = u.fightInfoVo || {};
    const hero = u.heroVo || {};
    const attrs = {};
    for (const a of fight.attributeList || []) {
      attrs[String(a.type)] = a.value;
    }
    return {
      id: base.id,
      templateId: monster.templateId,
      monsterId: monster.monsterId,
      heroJob: hero.job,
      campId: fight.campId,
      x: base.x,
      y: base.y,
      z: base.z,
      attrs,
    };
  });
}

function decodeLog(decoder, schema, dataHex) {
  const vo = decoder.decodeMessage("BattleLogVO", hexToBuf(dataHex));
  const entries = vo.entryList || [];
  const timeline = [];
  const skillCasts = [];
  const packetCounts = {};
  for (const entry of entries) {
    const time = Number(entry.time || 0);
    for (const item of entry.itemList || []) {
      const packetId = item.packetId;
      packetCounts[packetId] = (packetCounts[packetId] || 0) + 1;
      const typeName = PACKET_TYPES[packetId] || schema.protocolNames.get(packetId);
      let decoded = null;
      if (typeName && item.data) {
        decoded = decoder.decodeMessage(typeName, hexToBuf(item.data));
      }
      const row = { time, packetId, name: typeName || String(packetId) };
      if (packetId === 50763 && decoded) {
        row.skillId = decoded.skillId;
        row.actions = (decoded.actionList || []).map(summarizeAction);
        const damages = row.actions
          .filter((a) => a.damage != null)
          .map((a) => ({
            damage: Number(a.damage),
            type: a.damageType,
            skillId: String(a.skillId || decoded.skillId || 0),
            attackId: a.attackId,
            targetId: a.targetId,
          }));
        skillCasts.push({
          time,
          skillId: String(
            decoded.skillId && decoded.skillId !== "0"
              ? decoded.skillId
              : damages[0]?.skillId || decoded.skillId || 0
          ),
          damages: damages.map((d) => d.damage),
          hits: damages,
          die: row.actions.some((a) => a.die),
        });
      } else if (packetId === 50762 && decoded) {
        row.skillId = decoded.skillId;
        row.attackId = decoded.attackId;
        row.targetId = decoded.targetId;
        skillCasts.push({
          time,
          skillId: String(decoded.skillId),
          useSkill: true,
        });
      } else if (packetId === 50804 && decoded) {
        row.units = pickUnit(decoded);
      } else if (packetId === 50801 && decoded) {
        row.attr = {
          attackId: decoded.attackId,
          targetId: decoded.targetId,
          list: (decoded.attrList || []).map((a) => ({
            type: a.type,
            value: a.value,
          })),
        };
      } else if (decoded) {
        row.decoded = decoded;
      }
      timeline.push(row);
    }
  }
  const skillCounts = {};
  for (const c of skillCasts) {
    skillCounts[c.skillId] = (skillCounts[c.skillId] || 0) + 1;
  }
  return {
    entryCount: entries.length,
    packetCounts,
    skillCounts,
    skillCasts,
    timeline,
  };
}

function main() {
  const schema = new Schema();
  schema.load();
  const decoder = new Decoder(schema);
  const har = JSON.parse(fs.readFileSync(HAR, "utf8"));
  const msgs = extractWsMessages(har);
  const want = new Set([8676, 8688, 9006]);
  // collect every 61971
  const fights = [];
  for (let idx = 0; idx < msgs.length; idx++) {
    const direction = directionOf(msgs[idx]);
    const [payload] = framePayload(msgs[idx]);
    if (!payload) continue;
    let frame;
    try {
      frame = parseFrame(payload, direction, false);
    } catch {
      continue;
    }
    if (frame.protocolId !== 61971) continue;
    const decoded = decoder.decodeMessage("MainMapStartFightResp", frame.body);
    const chapterId = decoded.mainMapChapterId;
    const log = decoded.battleLog || {};
    const parsed = decodeLog(decoder, schema, log.data);
    const stats = log.fightStatisticsResp || {};
    fights.push({
      idx,
      time: msgs[idx].time,
      chapterId,
      bodyLen: frame.bodyLen,
      battleLogId: log.battleLogId,
      type: log.type,
      fin: log.fin,
      speed: log.speed,
      end: decoded.end,
      result: decoded.result,
      startTime: log.startTime,
      preBattleInfo: log.preBattleInfo,
      fightStatistics: stats,
      entryCount: parsed.entryCount,
      packetCounts: parsed.packetCounts,
      skillCounts: parsed.skillCounts,
      skillCasts: parsed.skillCasts,
      timeline: want.has(idx) || chapterId === 10300105 ? parsed.timeline : undefined,
      timelinePreview:
        chapterId === 10300105
          ? undefined
          : parsed.timeline.filter(
              (r) => r.packetId === 50763 || r.packetId === 50762 || r.die
            ).length,
    });
  }

  const boss = fights.filter((f) => f.chapterId === 10300105);
  const waves = fights.filter((f) => [10300101, 10300102, 10300103].includes(f.chapterId));
  const summary = {
    sourceHar: path.basename(HAR),
    note:
      "61971.battleLog.data 已按 BattleLogVO.entryList 解开。JSON 里原先的 data.battleLogId 数组是 byte[] 被误当成 FightStatisticsResp 的残片，不能当完整战报。",
    bossCount: boss.length,
    waveCount: waves.length,
    allFightChapters: fights.map((f) => ({
      idx: f.idx,
      chapterId: f.chapterId,
      bodyLen: f.bodyLen,
      fin: f.fin,
      end: f.end,
      win: f.fightStatistics?.win,
      skillCounts: f.skillCounts,
      packetCounts: f.packetCounts,
      winner: f.fightStatistics?.winner,
      loser: f.fightStatistics?.loser,
    })),
    ch11Boss: boss[0]
      ? {
          idx: boss[0].idx,
          chapterId: boss[0].chapterId,
          bodyLen: boss[0].bodyLen,
          battleLogId: boss[0].battleLogId,
          fin: boss[0].fin,
          end: boss[0].end,
          speed: boss[0].speed,
          startTime: boss[0].startTime,
          preBattleInfo: boss[0].preBattleInfo,
          fightStatistics: boss[0].fightStatistics,
          entryCount: boss[0].entryCount,
          packetCounts: boss[0].packetCounts,
          skillCounts: boss[0].skillCounts,
          skillCasts: boss[0].skillCasts,
        }
      : null,
  };

  const out = { summary, fights: fights.filter((f) => f.chapterId === 10300105 || want.has(f.idx)) };
  // keep full boss timeline only in dedicated dump
  if (boss[0]) {
    fs.writeFileSync(
      OUT,
      JSON.stringify(
        {
          ...summary.ch11Boss,
          timeline: boss[0].timeline,
        },
        null,
        2
      ),
      "utf8"
    );
  }
  const summaryPath = OUT.replace(/\.json$/i, "-summary.json");
  fs.writeFileSync(summaryPath, JSON.stringify(summary, null, 2), "utf8");
  console.log("fights", fights.length);
  for (const f of fights) {
    console.log(
      `idx=${f.idx} ch=${f.chapterId} body=${f.bodyLen} entries=${f.entryCount} skills=${JSON.stringify(f.skillCounts)} pkts=${JSON.stringify(f.packetCounts)} win=${f.fightStatistics?.win}`
    );
  }
  console.log("wrote", OUT);
  console.log("wrote", summaryPath);
}

main();
