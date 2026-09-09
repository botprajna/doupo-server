package com.doupo.server.module.scene;

import com.doupo.protocol.*;
import com.doupo.server.module.combat.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;

public class LaterMonsterMovementTest {
    @Test
    public void activeSkillOpeningAlertsAllSixInEveryLaterWaveAndLoop() {
        for (int stage = 2; stage <= 5; stage++) {
            SceneHandler h = new SceneHandler();
            Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000009700L + stage);
            try {
                for (int wave : new int[]{1, 2, 3, 1}) {
                    int chapter = 10200000 + stage * 100 + wave;
                    h.enterGuidanceMainMapMonster(c, GuidanceMainMapMonsterEnterReq.newBuilder()
                            .setChapterId(chapter).setY(7).setZ(100 + wave * 40).build());
                    CombatSession session = CombatSessionRegistry.get(c.id);
                    assertEquals(chapter, session.getChapterId());
                    c.writes.clear();
                    float z = session.getBattleCenterZ() - 7;
                    h.useGuidanceMainMapSkill(c, GuidanceMainMapUseSkillReq.newBuilder()
                            .setSkillId(10150210100L).setTargetId(h.waveUnitIdAt(c.id, 0))
                            .setY(7).setZ(z).setTy(7).setTz(z).build());
                    Set<Long> warned = new HashSet<>();
                    c.writes.stream().filter(WarningResp.class::isInstance).map(WarningResp.class::cast)
                            .forEach(w -> warned.add(w.getId()));
                    assertEquals("all six alerted in " + chapter, 6, warned.size());
                    List<MoveResp> moves = session.advanceMonsterMovement(0);
                    assertEquals("all six pursue in " + chapter, 6, moves.size());
                    for (MoveResp m : moves) assertTrue(warned.contains(m.getMove().getId()));
                    for (CombatUnit m : session.getMonsters()) h.killGuidanceMonster(c,
                            GuidanceKillMonsterReq.newBuilder().setMonsterId(m.getSceneUnitId()).build());
                    h.endMainMapFight(c, MainMapEndFightReq.newBuilder().setMainMapChapterId(chapter).build());
                }
            } finally { CombatSessionRegistry.clear(c.id); }
        }
    }

    @Test
    public void laterRearMonsterApproachesStopsAndAttacksInAllTwelveWaves() {
        for (int stage = 2; stage <= 5; stage++) for (int wave = 1; wave <= 3; wave++) {
            int chapter = 10200000 + stage * 100 + wave;
            int kind = stage == 3 ? 13 : 121002;
            CombatSession s = rearSession(chapter, kind, 16);
            s.alertMonster(2);
            assertFalse("warning must enable movement " + chapter, s.advanceMonsterMovement(0).isEmpty());
            assertTrue(s.tick(0).isEmpty());
            boolean attacked = false;
            for (long t = 66; t < 5000; t += 66) {
                List<MoveResp> moves = s.advanceMonsterMovement(t);
                List<MonsterAttack> attacks = s.tick(t);
                if (attacks.isEmpty()) continue;
                assertTrue(moves.stream().anyMatch(m -> m.getMove().getType() == 4));
                assertEquals(kind == 13 ? 220310110101L : 40310310101L, attacks.get(0).getSkillId());
                assertTrue(s.getPlayer().getCurrentHp() < s.getPlayer().getMaxHp());
                assertTrue("movement ticks must not accelerate attacks", s.tick(t + 66).isEmpty());
                attacked = true;
                break;
            }
            assertTrue("must attack before timeout " + chapter, attacked);
        }
    }

    @Test
    public void rearUsesItsOwnSkillAndProjectileActionInsteadOfGenericMelee() {
        for (int kind : new int[]{13, 121002}) {
            CombatSession s = rearSession(10200301, kind, 0);
            s.alertMonster(2);
            MonsterAttack attack = s.tick(0).get(0);
            assertEquals(kind == 13 ? 220310110101L : 40310310101L, attack.getSkillId());
            assertEquals(kind == 13 ? 2200101L : 5123601L, attack.getDamageActionId());
            assertEquals(kind == 13 ? 128 : 0, attack.getSpecial());
            assertTrue(attack.isDelayedImpact());
        }
    }

    @Test
    public void correctCastRadiusAndAlertGateApplyToBothRearKinds() {
        for (int kind : new int[]{13, 121002}) {
            float radius = kind == 13 ? 5f : 6f;
            CombatSession s = rearSession(10200301, kind, radius);
            assertTrue("not alerted yet", s.tick(0).isEmpty());
            s.alertMonster(2);
            assertFalse(s.needsChase(2));
            assertEquals(1, s.tick(0).size());
            s.getMonster(2).moveTo(0, 0, radius + 0.01f);
            assertTrue(s.needsChase(2));
            assertTrue(s.tick(1000).isEmpty());
        }
    }

    @Test
    public void bothRearFamiliesMatchRawCapturedCastAndImpactProtocol() throws Exception {
        for (int kind : new int[]{13, 121002}) {
            CombatSession s = rearSession(10200301, kind, 0);
            s.alertMonster(2);
            MonsterAttack attack = s.tick(0).get(0);
            List<com.google.protobuf.GeneratedMessageV3> writes = new ArrayList<>();
            CombatProtocolWriter.writeMonsterAttack(attack, (id, message) -> writes.add(message));
            assertEquals(3, writes.size());
            UseSkillResp capturedUse = UseSkillResp.parseFrom(fixture(kind == 13 ? "5513-50762.bin" : "2372-50762.bin"));
            UseSkillResp use = (UseSkillResp) writes.get(0);
            assertEquals(capturedUse.getSkillId(), use.getSkillId());
            assertEquals(capturedUse.getTimeRatio(), use.getTimeRatio(), 0.000001f);
            assertEquals(2, use.getAttackId());
            assertEquals(1, use.getTargetId());
            assertTrue(use.getServerAdvance());
            SkillActionResp capturedHit = SkillActionResp.parseFrom(fixture(kind == 13 ? "5554-50763.bin" : "2403-50763.bin"));
            // 只替换测试玩家/怪物身份及本场伤害/血量；技能、动作、special、结构全部对原始包。
            ActionVo damage = capturedHit.getActionList(0).toBuilder().setDamageVo(
                    capturedHit.getActionList(0).getDamageVo().toBuilder().setAttackId(2).setTargetId(1)
                            .setDamage(attack.getDamage())).build();
            ActionVo hp = capturedHit.getActionList(1).toBuilder().setAttributeActionVo(
                    capturedHit.getActionList(1).getAttributeActionVo().toBuilder().setTargetId(1)
                            .setAttrList(0, AttributeVO.newBuilder().setType(103011)
                                    .setValue(attack.getTargetHpAfterDamage()))).build();
            assertEquals(capturedHit.toBuilder().setActionList(0, damage).setActionList(1, hp).build(), writes.get(2));
        }
    }

    @Test
    public void skillSelectionAndCastBoundariesMatchSixNineLubanProfiles() throws Exception {
        com.fasterxml.jackson.databind.JsonNode config = new com.fasterxml.jackson.databind.ObjectMapper()
                .readTree(fixture("skill-ranges.json"));
        assertEquals("6.9.263", config.path("version").asText());
        for (com.fasterxml.jackson.databind.JsonNode profile : config.path("evidence")) {
            int kind = profile.path("monsterId").asInt();
            float radius = (float) profile.path("skills").path(0).path("radius").asDouble();
            CombatSession s = rearSession(10200401, kind, radius);
            s.alertMonster(2);
            for (int i = 0; i < 2; i++) {
                assertFalse(s.needsChase(2));
                assertEquals(profile.path("skills").path(i).path("id").asLong(), s.tick(i * 1000).get(0).getSkillId());
            }
            s.getMonster(2).moveTo(0, 0, radius + 0.01f);
            assertTrue(s.needsChase(2));
            assertTrue(s.tick(2000).isEmpty());
        }
    }

    private static byte[] fixture(String name) throws Exception {
        try (java.io.InputStream in = LaterMonsterMovementTest.class.getResourceAsStream("/later-monsters/" + name)) {
            assertNotNull(name, in);
            return com.google.protobuf.ByteString.readFrom(in).toByteArray();
        }
    }

    @Test
    public void laterRearPauseDeathAndRepeatedAlertDoNotResetMovementOrCooldown() {
        CombatSession s = rearSession(10200403, 121002, 16);
        s.alertMonster(2);
        s.advanceMonsterMovement(0);
        s.advanceMonsterMovement(66);
        float z = s.getMonster(2).getZ();
        s.setPaused(true);
        assertEquals(4, s.advanceMonsterMovement(132).get(0).getMove().getType());
        s.advanceMonsterMovement(100000);
        assertEquals(z, s.getMonster(2).getZ(), 0f);
        s.setPaused(false);
        s.advanceMonsterMovement(100066);
        assertEquals(z, s.getMonster(2).getZ(), 0f);
        s.advanceMonsterMovement(103066);
        assertEquals(1, s.tick(103066).size());
        s.alertMonster(2);
        assertTrue(s.tick(103132).isEmpty());
        assertEquals(40310410101L, s.tick(104066).get(0).getSkillId());
        s.markMonsterDead(2);
        assertTrue(s.advanceMonsterMovement(105066).isEmpty());
        assertTrue(s.tick(105066).isEmpty());
    }

    @Test
    public void realTickWritesRearStopThenSkillThenImpactAndRejectsOldWaveTick() {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000009710L);
        try {
            h.enterGuidanceMainMapMonster(c, GuidanceMainMapMonsterEnterReq.newBuilder()
                    .setChapterId(10200401).setZ(100).build());
            CombatSession s = CombatSessionRegistry.get(c.id);
            s.updatePlayerPosition(0, 0, 100);
            long rear = h.waveUnitIdAt(c.id, 4);
            s.alertMonster(rear);
            s.advanceMonsterMovement(System.currentTimeMillis() - 10000);
            c.writes.clear();
            CombatTickProcessor.onTick(null, new CombatTick(c, s));
            int stop = -1, skill = -1;
            for (int i = 0; i < c.writes.size(); i++) {
                Object message = c.writes.get(i);
                if (message instanceof MoveResp && ((MoveResp) message).getMove().getId() == rear
                        && ((MoveResp) message).getMove().getType() == 4) stop = i;
                if (message instanceof UseSkillResp && ((UseSkillResp) message).getAttackId() == rear) skill = i;
            }
            assertTrue(stop >= 0 && skill > stop);
            assertEquals(40310310101L, ((UseSkillResp) c.writes.get(skill)).getSkillId());
            SkillActionResp impact = (SkillActionResp) c.writes.get(skill + 2);
            assertEquals(0, impact.getSkillId());
            assertEquals(5123601L, impact.getActionList(0).getDamageVo().getActionId());
            assertEquals(s.getPlayer().getSceneUnitId(), impact.getActionList(0).getDamageVo().getTargetId());
            assertTrue(s.getPlayer().getCurrentHp() < s.getPlayer().getMaxHp());
            c.writes.clear();
            CombatTickProcessor.onTick(null, new CombatTick(c, rearSession(10200403, 121002, 16)));
            assertTrue("old session must not move or attack current wave", c.writes.isEmpty());
        } finally { CombatSessionRegistry.clear(c.id); }
    }

    private static CombatSession rearSession(int chapter, int kind, float z) {
        CombatSession s = new CombatSession(chapter, new CombatUnit(1, 100, 100000, 0, 0, 0));
        SceneUnitVo rear = SceneUnitVo.newBuilder()
                .setBaseInfoVo(SceneUnitBaseInfoVo.newBuilder().setId(2).setZ(z))
                .setSceneMonsterVo(SceneMonsterVo.newBuilder().setMonsterId(kind).setTemplateId(1000803))
                .setFightInfoVo(SceneFightUnitInfoVo.newBuilder()
                        .addAttributeList(AttributeVO.newBuilder().setType(101001).setValue(862.5))
                        .addAttributeList(AttributeVO.newBuilder().setType(103001).setValue(5000))).build();
        s.addMonster(CombatSessionFactory.monsterUnit(rear));
        return s;
    }
}
