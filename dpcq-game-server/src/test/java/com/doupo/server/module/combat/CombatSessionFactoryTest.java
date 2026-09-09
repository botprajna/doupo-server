package com.doupo.server.module.combat;

import static org.junit.Assert.assertEquals;

import com.doupo.protocol.AttributeVO;
import com.doupo.protocol.SceneFightUnitInfoVo;
import com.doupo.protocol.SceneUnitBaseInfoVo;
import com.doupo.protocol.SceneUnitVo;

import org.junit.Test;

public class CombatSessionFactoryTest {

    private static SceneUnitVo unitWithAttributes(
            long id, float x, float y, float z,
            double attack, double maxHp, double currentHp) {

        SceneFightUnitInfoVo.Builder fightInfo = SceneFightUnitInfoVo.newBuilder()
                .addAttributeList(attribute(CombatUnit.ATTR_ATTACK, attack))
                .addAttributeList(attribute(CombatUnit.ATTR_MAX_HP, maxHp))
                .addAttributeList(attribute(CombatUnit.ATTR_CURRENT_HP, currentHp));

        return SceneUnitVo.newBuilder()
                .setBaseInfoVo(SceneUnitBaseInfoVo.newBuilder()
                        .setId(id)
                        .setX(x)
                        .setY(y)
                        .setZ(z))
                .setFightInfoVo(fightInfo)
                .build();
    }

    private static AttributeVO attribute(int type, double value) {
        return AttributeVO.newBuilder().setType(type).setValue(value).build();
    }

    @Test
    public void extractsAttackAndHpFromSceneUnitVo() {
        SceneUnitVo monsterVo = unitWithAttributes(
                101L, 1, 2, 3, 220, 460, 460);

        CombatUnit unit = CombatSessionFactory.monsterUnit(monsterVo);

        assertEquals(101L, unit.getSceneUnitId());
        assertEquals(220, unit.getAttack(), 0.0001);
        assertEquals(460, unit.getMaxHp(), 0.0001);
        assertEquals(460, unit.getCurrentHp(), 0.0001);
        assertEquals(1, unit.getX(), 0.0001);
        assertEquals(2, unit.getY(), 0.0001);
        assertEquals(3, unit.getZ(), 0.0001);
    }

    @Test
    public void fallsBackToMaxHpWhenCurrentHpAttributeIsMissing() {
        SceneUnitVo playerVo = SceneUnitVo.newBuilder()
                .setBaseInfoVo(SceneUnitBaseInfoVo.newBuilder().setId(1L))
                .setFightInfoVo(SceneFightUnitInfoVo.newBuilder()
                        .addAttributeList(attribute(
                                CombatUnit.ATTR_ATTACK, 481.8))
                        .addAttributeList(attribute(
                                CombatUnit.ATTR_MAX_HP, 12613.2)))
                .build();

        CombatUnit unit = CombatSessionFactory.playerUnit(playerVo);

        assertEquals(12613.2, unit.getCurrentHp(), 0.0001);
    }
}
