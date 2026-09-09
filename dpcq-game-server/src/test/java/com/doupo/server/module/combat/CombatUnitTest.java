package com.doupo.server.module.combat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CombatUnitTest {

    @Test
    public void startsAtMaxHpAndIsAlive() {
        CombatUnit unit = new CombatUnit(1L, 100, 500, 0, 0, 0);

        assertEquals(500, unit.getCurrentHp(), 0.0001);
        assertTrue(unit.isAlive());
    }

    @Test
    public void applyDamageClampsAtZero() {
        CombatUnit unit = new CombatUnit(1L, 100, 50, 0, 0, 0);

        double applied = unit.applyDamage(80);

        assertEquals(50, applied, 0.0001);
        assertEquals(0, unit.getCurrentHp(), 0.0001);
        assertFalse(unit.isAlive());
    }

    @Test
    public void deadUnitTakesNoFurtherDamage() {
        CombatUnit unit = new CombatUnit(1L, 100, 50, 0, 0, 0);
        unit.applyDamage(50);

        double secondHit = unit.applyDamage(20);

        assertEquals(0, secondHit, 0.0001);
    }

    @Test
    public void nonPositiveDamageIsIgnored() {
        CombatUnit unit = new CombatUnit(1L, 100, 50, 0, 0, 0);

        assertEquals(0, unit.applyDamage(0), 0.0001);
        assertEquals(0, unit.applyDamage(-5), 0.0001);
        assertEquals(50, unit.getCurrentHp(), 0.0001);
    }

    @Test
    public void distanceToUsesEuclideanDistance() {
        CombatUnit a = new CombatUnit(1L, 0, 100, 0, 0, 0);
        CombatUnit b = new CombatUnit(2L, 0, 100, 3, 0, 4);

        assertEquals(5.0, a.distanceTo(b), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsZeroSceneUnitId() {
        new CombatUnit(0L, 100, 50, 0, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNonPositiveMaxHp() {
        new CombatUnit(1L, 100, 0, 0, 0, 0);
    }
}
