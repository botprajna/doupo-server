package com.doupo.server.module.combat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class CombatSessionTest {

    @Test
    public void ninthBossLethalHitKeepsOneHpWithoutClippingDisplayedDamage() {
        CombatUnit player = new CombatUnit(1L, 100, 100, 0, 0, 0);
        CombatSession session = new CombatSession(10200405, player);
        session.addMonster(monster(101L));

        MonsterAttack attack = session.tick(0L).get(0);

        assertEquals(4336, attack.getDamage(), 0.0001);
        assertEquals(1, attack.getTargetHpAfterDamage(), 0.0001);
        assertTrue(session.isActive());
        assertTrue(session.tryBeginNinthBossGuide());
        assertTrue(session.isPaused());
        assertFalse(session.tryBeginNinthBossGuide());
    }

    @Test
    public void ninthBossGuideIsNotAnAttackCountOrTimeTrigger() {
        CombatSession session = new CombatSession(10200405,
                new CombatUnit(1L, 100, 1000000, 0, 0, 0));
        session.addMonster(monster(101L));
        for (int i = 0; i < 20; i++) {
            session.tick(i * 2000L);
            assertFalse(session.tryBeginNinthBossGuide());
        }
    }

    @Test
    public void ninthBossGuideDoesNotStartAfterFightStopped() {
        CombatSession session = new CombatSession(10200405,
                new CombatUnit(1L, 100, 1, 0, 0, 0));
        session.addMonster(monster(101L));
        session.markMonsterDead(101L);
        assertFalse(session.tryBeginNinthBossGuide());
    }

    private static CombatUnit player() {
        return new CombatUnit(1L, 100, 12613.2, 0, 0, 0);
    }

    private static CombatUnit monster(long id) {
        return new CombatUnit(id, 220, 460, 0, 0, 0);
    }

    @Test
    public void doesNotAttackBeforeIntervalElapses() {
        CombatSession session = new CombatSession(10100101, player());
        session.addMonster(monster(101L));

        List<MonsterAttack> firstTick = session.tick(0L);
        List<MonsterAttack> secondTick = session.tick(500L);

        assertEquals(1, firstTick.size());
        assertTrue(secondTick.isEmpty());
    }

    @Test
    public void attacksAgainAfterIntervalElapses() {
        CombatSession session = new CombatSession(10100101, player());
        session.addMonster(monster(101L));

        session.tick(0L);
        List<MonsterAttack> nextAttack = session.tick(
                CombatSession.ATTACK_INTERVAL_MILLIS);

        assertEquals(1, nextAttack.size());
    }

    @Test
    public void alternatesBetweenTheTwoNormalAttackSkills() {
        CombatSession session = new CombatSession(10100101, player());
        session.addMonster(monster(101L));

        long first = session.tick(0L).get(0).getSkillId();
        long second = session.tick(
                CombatSession.ATTACK_INTERVAL_MILLIS)
                .get(0)
                .getSkillId();

        assertEquals(CombatSession.MONSTER_NORMAL_ATTACK_SKILLS[0], first);
        assertEquals(CombatSession.MONSTER_NORMAL_ATTACK_SKILLS[1], second);
    }

    @Test
    public void reducesPlayerHpByMonsterAttackAndReportsAbsoluteValue() {
        CombatUnit player = player();
        CombatSession session = new CombatSession(10100101, player);
        session.addMonster(monster(101L));

        MonsterAttack attack = session.tick(0L).get(0);

        assertEquals(220.0, attack.getDamage(), 0.0001);
        assertEquals(12393.2, attack.getTargetHpAfterDamage(), 0.0001);
        assertEquals(12393.2, player.getCurrentHp(), 0.0001);
    }

    @Test
    public void deadMonsterStopsAttacking() {
        CombatSession session = new CombatSession(10100101, player());
        session.addMonster(monster(101L));

        boolean marked = session.markMonsterDead(101L);
        List<MonsterAttack> attacks = session.tick(0L);

        assertTrue(marked);
        assertTrue(attacks.isEmpty());
    }

    @Test
    public void markingAnAlreadyDeadMonsterReturnsFalse() {
        CombatSession session = new CombatSession(10100101, player());
        session.addMonster(monster(101L));

        session.markMonsterDead(101L);
        boolean secondMark = session.markMonsterDead(101L);

        assertFalse(secondMark);
    }

    @Test
    public void sessionBecomesInactiveWhenAllMonstersAreDead() {
        CombatSession session = new CombatSession(10100101, player());
        session.addMonster(monster(101L));

        session.markMonsterDead(101L);

        assertFalse(session.isActive());
        assertTrue(session.tick(0L).isEmpty());
    }

    @Test
    public void sessionBecomesInactiveWhenPlayerDies() {
        CombatUnit player = new CombatUnit(1L, 100, 50, 0, 0, 0);
        CombatSession session = new CombatSession(10100101, player);
        session.addMonster(monster(101L));

        session.tick(0L);

        assertFalse(player.isAlive());
        assertFalse(session.isActive());
    }

    @Test
    public void monsterOutOfRangeDoesNotAttack() {
        CombatUnit player = new CombatUnit(1L, 100, 12613.2, 0, 0, 0);
        CombatSession session = new CombatSession(10100101, player);
        session.addMonster(new CombatUnit(101L, 220, 460, 50, 0, 0));

        List<MonsterAttack> attacks = session.tick(0L);

        assertTrue(attacks.isEmpty());
    }

    /**
     * 第一小关四只怪沿 z 轴每 15 个单位一只。玩家贴身打第一只时，第二只怪
     * 必须仍在攻击范围外，否则它会在原地对空气挥手（实测出现过的回归）。
     */
    @Test
    public void adjacentMonsterFifteenUnitsAwayStaysOutOfRange() {
        CombatUnit player = new CombatUnit(1L, 100, 12613.2, 0, 0, 2.5f);
        CombatSession session = new CombatSession(10100101, player);
        CombatUnit engaged = new CombatUnit(101L, 220, 460, 0, 0, 5.2f);
        CombatUnit nextInLine = new CombatUnit(102L, 220, 460, 0, 0, 20.2f);
        session.addMonster(engaged);
        session.addMonster(nextInLine);

        List<MonsterAttack> attacks = session.tick(0L);

        assertEquals(1, attacks.size());
        assertEquals(
                engaged.getSceneUnitId(),
                attacks.get(0).getMonsterSceneUnitId());
    }

    @Test
    public void updatePlayerPositionAffectsRangeChecks() {
        CombatUnit player = new CombatUnit(1L, 100, 12613.2, 0, 0, 0);
        CombatSession session = new CombatSession(10100101, player);
        session.addMonster(new CombatUnit(101L, 220, 460, 0, 0, 20));

        assertTrue(session.tick(0L).isEmpty());

        session.updatePlayerPosition(0, 0, 19);

        assertEquals(1, session.tick(0L).size());
    }

    @Test
    public void deactivateStopsFurtherAttacks() {
        CombatSession session = new CombatSession(10100101, player());
        session.addMonster(monster(101L));

        session.deactivate();

        assertFalse(session.isActive());
        assertTrue(session.tick(0L).isEmpty());
    }

    @Test
    public void thirdBossUsesCapturedSkillsAndDamageSequence() {
        CombatUnit player = new CombatUnit(1L, 809.2535, 21137, 0, 0, 0);
        CombatSession session = new CombatSession(10100305, player);
        session.addMonster(new CombatUnit(101L, 800, 12000, 0, 0, 0));

        double[] damages = {491, 491, 491, 343, 491, 491};
        long[] skills = {
                51410110101L, 51410210101L, 51410110101L,
                51410210101L, 51410110101L, 51410210101L
        };
        for (int i = 0; i < damages.length; i++) {
            MonsterAttack attack = session.tick(
                    i * CombatSession.BOSS_ATTACK_INTERVAL_MILLIS).get(0);
            assertEquals(damages[i], attack.getDamage(), 0.0001);
            assertEquals(skills[i], attack.getSkillId());
        }
        // 序列播完后循环：第 7 次攻击回到序列第 0 个（技能/伤害整体循环），而非停止。
        MonsterAttack looped = session.tick(
                damages.length
                        * CombatSession.BOSS_ATTACK_INTERVAL_MILLIS).get(0);
        assertEquals(damages[0], looped.getDamage(), 0.0001);
        assertEquals(skills[0], looped.getSkillId());
        assertEquals(18339 - 491, player.getCurrentHp(), 0.0001);
    }

    @Test
    public void fourthBossDoesNotUseRawAttackAsDamage() {
        CombatUnit player = new CombatUnit(1L, 865.007, 22874.5, 0, 0, 0);
        CombatSession session = new CombatSession(10100405, player);
        session.addMonster(new CombatUnit(101L, 1800, 16000, 0, 0, 0));

        double[] damages = {1368, 1368, 1044, 1044, 1044, 549, 549, 1044};
        for (int i = 0; i < damages.length; i++) {
            MonsterAttack attack = session.tick(
                    i * CombatSession.BOSS_ATTACK_INTERVAL_MILLIS).get(0);
            assertEquals(damages[i], attack.getDamage(), 0.0001);
            assertEquals(51310110101L, attack.getSkillId());
        }
        assertEquals(14864.5, player.getCurrentHp(), 0.0001);
    }

    @Test
    public void sixthChapterMeleeAndRangedDamageMatchCapture() {
        CombatUnit player = new CombatUnit(
                1L, 1189.07668, 30991.35, 0, 0, 0);
        CombatSession session = new CombatSession(10200101, player);
        session.addMonster(new CombatUnit(101L, 275, 3450, 0, 0, 0));
        session.addMonster(new CombatUnit(102L, 287.5, 3000, 0, 0, 0));

        List<MonsterAttack> attacks = session.tick(0L);

        assertEquals(2, attacks.size());
        assertEquals(25, attacks.get(0).getDamage(), 0.0001);
        assertEquals(201110110101L, attacks.get(0).getSkillId());
        assertEquals(26, attacks.get(1).getDamage(), 0.0001);
        assertEquals(220310110101L, attacks.get(1).getSkillId());
        assertTrue(attacks.get(1).isDelayedImpact());
        assertEquals(30940.35, player.getCurrentHp(), 0.0001);
    }

    @Test
    public void sixthBossIncludesCapturedRageSkillDamage() {
        CombatUnit player = new CombatUnit(
                1L, 1189.07668, 30991.35, 0, 0, 0);
        CombatSession session = new CombatSession(10200105, player);
        session.addMonster(new CombatUnit(101L, 2300, 17000, 0, 0, 0));

        double[] damages = {1686, 1686, 1686, 1686, 1180, 1686, 8431, 1686};
        MonsterAttack attack = null;
        for (int i = 0; i < damages.length; i++) {
            attack = session.tick(
                    i * CombatSession.BOSS_ATTACK_INTERVAL_MILLIS).get(0);
            assertEquals(damages[i], attack.getDamage(), 0.0001);
        }
        assertEquals(51310110101L, attack.getSkillId());
        assertEquals(11264.35, player.getCurrentHp(), 0.0001);
    }

    /** 第六关 Boss 攻击序列（8 个）播完后必须循环，而不是停止攻击。 */
    @Test
    public void sixthBossKeepsAttackingAfterSequenceEnds() {
        CombatUnit player = new CombatUnit(
                1L, 1189.07668, 30991.35, 0, 0, 0);
        CombatSession session = new CombatSession(10200105, player);
        session.addMonster(new CombatUnit(101L, 2300, 17000, 0, 0, 0));

        // 前 8 次 = 抓包序列。
        for (int i = 0; i < 8; i++) {
            session.tick(
                    i * CombatSession.BOSS_ATTACK_INTERVAL_MILLIS).get(0);
        }
        // 第 9 次（seq=8）应循环回序列第 0 个定义。
        MonsterAttack looped = session.tick(
                8 * CombatSession.BOSS_ATTACK_INTERVAL_MILLIS).get(0);
        assertEquals(1686, looped.getDamage(), 0.0001);
        assertEquals(51310110101L, looped.getSkillId());
    }

    /** 第九关 Boss 使用抓包技能序列，播完后仍继续攻击。 */
    @Test
    public void ninthBossUsesCapturedSequenceAndKeepsAttacking() {
        CombatUnit player = new CombatUnit(
                1L, 1846.54656, 47938.8, 0, 0, 0);
        CombatSession session = new CombatSession(10200405, player);
        session.addMonster(new CombatUnit(101L, 20000, 380000, 0, 0, 0));

        double[] damages = {4336, 17345, 4336, 1865};
        long[] skills = {
                50810210101L, 50810110101L,
                50810210101L, 50810210101L
        };
        for (int i = 0; i < damages.length; i++) {
            MonsterAttack attack = session.tick(
                    i * CombatSession.BOSS_ATTACK_INTERVAL_MILLIS).get(0);
            assertEquals(damages[i], attack.getDamage(), 0.0001);
            assertEquals(skills[i], attack.getSkillId());
        }

        MonsterAttack looped = session.tick(
                damages.length
                        * CombatSession.BOSS_ATTACK_INTERVAL_MILLIS).get(0);
        assertEquals(4336, looped.getDamage(), 0.0001);
        assertEquals(50810210101L, looped.getSkillId());
    }

    /** 引导暂停期间不推进攻击，恢复后 Boss 继续原序列。 */
    @Test
    public void pausedBossResumesWithoutLosingAttackSequence() {
        CombatUnit player = new CombatUnit(
                1L, 1846.54656, 47938.8, 0, 0, 0);
        CombatSession session = new CombatSession(10200405, player);
        session.addMonster(new CombatUnit(101L, 20000, 380000, 0, 0, 0));

        session.setPaused(true);
        assertTrue(session.tick(0).isEmpty());
        session.setPaused(false);

        MonsterAttack attack = session.tick(10_000).get(0);
        assertEquals(4336, attack.getDamage(), 0.0001);
        assertEquals(50810210101L, attack.getSkillId());
    }

    /** 第十关 Boss 使用抓包技能序列，播完后仍继续攻击。 */
    @Test
    public void tenthBossUsesCapturedSequenceAndKeepsAttacking() {
        CombatUnit player = new CombatUnit(
                1L, 2093.88816, 54483.8, 0, 0, 0);
        CombatSession session = new CombatSession(10200505, player);
        session.addMonster(new CombatUnit(101L, 3000, 45000, 0, 0, 0));

        double[] damages = {
                1362, 1362, 1946, 1362, 1362, 1946, 7783, 1946, 1362
        };
        long[] skills = {
                50210110101L, 50210210101L, 50210110101L,
                50210210101L, 50210110101L, 50210210101L,
                50220110101L, 50210110101L, 50210210101L
        };
        for (int i = 0; i < damages.length; i++) {
            MonsterAttack attack = session.tick(
                    i * CombatSession.BOSS_ATTACK_INTERVAL_MILLIS).get(0);
            assertEquals(damages[i], attack.getDamage(), 0.0001);
            assertEquals(skills[i], attack.getSkillId());
        }

        MonsterAttack looped = session.tick(
                damages.length
                        * CombatSession.BOSS_ATTACK_INTERVAL_MILLIS).get(0);
        assertEquals(1362, looped.getDamage(), 0.0001);
        assertEquals(50210110101L, looped.getSkillId());
    }
}
