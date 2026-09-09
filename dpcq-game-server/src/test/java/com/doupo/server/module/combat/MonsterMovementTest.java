package com.doupo.server.module.combat;

import com.doupo.protocol.MoveResp;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MonsterMovementTest {

    private static CombatSession session(int chapter) {
        CombatSession session = new CombatSession(chapter,
                new CombatUnit(1L, 100, 100000, 0, 0, 0));
        CombatUnit rear = new CombatUnit(3L, 287.5, 3000, 0, 0, 16);
        rear.setTemplateId(1000603L);
        session.addMonster(rear);
        return session;
    }

    @Test
    public void rearMonsterWalksBeforeAttackingInEachOfTheThreeWaves() {
        for (int chapter = 10200101; chapter <= 10200103; chapter++) {
            CombatSession session = session(chapter);
            assertTrue(session.advanceMonsterMovement(0L).isEmpty());
            session.alertMonster(3L);
            MoveResp start = session.advanceMonsterMovement(100L).get(0);
            assertEquals(3L, start.getMove().getId());
            assertEquals(1, start.getMove().getType());
            assertEquals(16f, start.getMove().getCurZ(), 0.0001f);
            assertEquals(5f, start.getMove().getAngularSpeed(), 0.0001f);
            assertEquals(16f, session.getMonster(3L).getZ(), 0.0001f);
            assertTrue(session.tick(100L).isEmpty());
            MoveResp moving = session.advanceMonsterMovement(166L).get(0);
            assertEquals(15.67f, session.getMonster(3L).getZ(), 0.0001f);
            assertEquals(session.getMonster(3L).getZ(), moving.getMove().getCurZ(), 0.0001f);
            assertTrue(session.tick(166L).isEmpty());
            boolean stopped = false;
            long time;
            for (time = 232L; time <= 4060L; time += 66L) {
                List<MoveResp> moves = session.advanceMonsterMovement(time);
                stopped = moves.stream().anyMatch(move -> move.getMove().getType() == 4);
                if (stopped) {
                    MoveResp stop = moves.get(0);
                    assertEquals(stop.getMove().getCurZ(), stop.getMove().getZ(), 0.0001f);
                    break;
                }
                assertTrue("no damage before reaching player", session.tick(time).isEmpty());
            }
            assertTrue("must stop in range", stopped);
            assertFalse(session.needsChase(3L));
            MonsterAttack attack = session.tick(time).get(0);
            assertEquals(220310110101L, attack.getSkillId());
            assertEquals(26d, attack.getDamage(), 0d);
            assertEquals(session.getMonster(3L).getZ(), attack.getMonsterZ(), 0f);
            assertTrue(session.tick(time + 66L).isEmpty());
            assertEquals(220310210101L, session.tick(time + 1000L).get(0).getSkillId());
        }
    }

    @Test
    public void repeatedWarningsDoNotResetPositionOrCooldown() {
        CombatSession session = session(10200101);
        session.alertMonster(3L);
        session.advanceMonsterMovement(0);
        session.advanceMonsterMovement(66);
        session.alertMonster(3L);
        session.advanceMonsterMovement(132);
        assertEquals(15.34f, session.getMonster(3L).getZ(), 0.0001f);
        session.advanceMonsterMovement(3000);
        assertEquals(1, session.tick(3000).size());
        session.alertMonster(3L);
        assertTrue(session.tick(3066).isEmpty());
    }

    @Test
    public void movementReplansFromServerPositionWhenPlayerMoves() {
        CombatSession session = session(10200101);
        session.alertMonster(3L);
        session.advanceMonsterMovement(0);
        session.updatePlayerPosition(10, 0, 0);
        MoveResp replanned = session.advanceMonsterMovement(66).get(0);
        assertEquals(15.67f, replanned.getMove().getCurZ(), 0.0001f);
        assertTrue(replanned.getMove().getX() > 0f);
        session.advanceMonsterMovement(132);
        assertTrue(session.getMonster(3L).getX() > 0f);
        assertTrue(session.getMonster(3L).distanceTo(session.getPlayer()) > 5d);
        assertTrue(session.tick(132).isEmpty());
    }

    @Test
    public void pausedMovementStopsAndResumesWithoutCatchingUpPausedTime() {
        CombatSession session = session(10200101);
        session.alertMonster(3L);
        session.advanceMonsterMovement(0);
        session.advanceMonsterMovement(66);
        float z = session.getMonster(3L).getZ();
        session.setPaused(true);
        assertEquals(4, session.advanceMonsterMovement(132).get(0).getMove().getType());
        assertTrue(session.advanceMonsterMovement(50000).isEmpty());
        assertTrue(session.tick(50000).isEmpty());
        assertEquals(z, session.getMonster(3L).getZ(), 0f);
        session.setPaused(false);
        assertEquals(1, session.advanceMonsterMovement(60000).get(0).getMove().getType());
        assertEquals(z, session.getMonster(3L).getZ(), 0f);
        session.advanceMonsterMovement(60066);
        assertEquals(z - 0.33f, session.getMonster(3L).getZ(), 0.0001f);
    }

    @Test
    public void deadMonstersAndEndedSessionsCannotContinueMovingOrAttacking() {
        CombatSession session = session(10200101);
        session.alertMonster(3L);
        session.advanceMonsterMovement(0);
        session.markMonsterDead(3L);
        assertTrue(session.advanceMonsterMovement(1000).isEmpty());
        assertTrue(session.tick(1000).isEmpty());
        assertEquals(16f, session.getMonster(3L).getZ(), 0f);

        session = session(10200101);
        session.alertMonster(3L);
        session.advanceMonsterMovement(0);
        session.deactivate();
        assertEquals(4, session.advanceMonsterMovement(1000).get(0).getMove().getType());
        assertTrue(session.advanceMonsterMovement(2000).isEmpty());
        assertTrue(session.tick(2000).isEmpty());
    }

    @Test
    public void newWaveDoesNotInheritOldWaveAlertOrMovement() {
        CombatSession previous = session(10200101);
        previous.alertMonster(3L);
        previous.advanceMonsterMovement(0);
        CombatSession next = session(10200102);
        previous.deactivate();
        assertTrue(next.advanceMonsterMovement(10000).isEmpty());
        assertEquals(16f, next.getMonster(3L).getZ(), 0f);
        next.alertMonster(3L);
        assertEquals(1, next.advanceMonsterMovement(10066).get(0).getMove().getType());
        assertEquals(16f, next.getMonster(3L).getZ(), 0f);
    }

    @Test
    public void mountainMovementDoesNotChangeEarlierChaptersOrBossAndReplayCadence() {
        for (int chapter : new int[] {10100101, 10200105, 10200205, 10200305, 10200405, 10200505, 10300101, 10300105}) {
            CombatSession session = session(chapter);
            session.alertMonster(3L);
            assertTrue(session.advanceMonsterMovement(0).isEmpty());
            assertTrue(session.advanceMonsterMovement(5000).isEmpty());
            assertEquals(16f, session.getMonster(3L).getZ(), 0f);
            assertEquals(1000L, session.getTickIntervalMillis());
        }
        assertEquals(66L, session(10200101).getTickIntervalMillis());
    }
}
