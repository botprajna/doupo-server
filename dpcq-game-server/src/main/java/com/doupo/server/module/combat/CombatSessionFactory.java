package com.doupo.server.module.combat;

import com.doupo.protocol.AttributeVO;
import com.doupo.protocol.SceneUnitVo;

import java.util.List;

/**
 * 从服务端已经下发给客户端的 {@link SceneUnitVo} 提取战斗属性，
 * 构建 {@link CombatSession} 所需的 {@link CombatUnit}。
 *
 * <p>属性来源与客户端读到的完全一致，避免服务端另设一套数值导致两边血条不一致。
 * 数值都取自 {@code fightInfoVo.attributeList}（见 {@code SceneHandler} 构建快照时
 * 写出的 101001/103001/103011）。
 */
public final class CombatSessionFactory {

    private CombatSessionFactory() {
    }

    /**
     * 构建玩家战斗单位。
     *
     * @param playerVo 服务端下发的玩家 {@link SceneUnitVo}
     */
    public static CombatUnit playerUnit(SceneUnitVo playerVo) {
        if (playerVo == null) {
            throw new IllegalArgumentException("playerVo must not be null");
        }

        return unitFromSceneVo(playerVo);
    }

    /**
     * 构建怪物战斗单位。
     *
     * @param monsterVo 服务端下发的怪物 {@link SceneUnitVo}
     */
    public static CombatUnit monsterUnit(SceneUnitVo monsterVo) {
        if (monsterVo == null) {
            throw new IllegalArgumentException("monsterVo must not be null");
        }

        return unitFromSceneVo(monsterVo);
    }

    private static CombatUnit unitFromSceneVo(SceneUnitVo sceneVo) {
        long id = sceneVo.getBaseInfoVo().getId();
        float x = sceneVo.getBaseInfoVo().getX();
        float y = sceneVo.getBaseInfoVo().getY();
        float z = sceneVo.getBaseInfoVo().getZ();

        List<AttributeVO> attributes =
                sceneVo.getFightInfoVo().getAttributeListList();

        double attack = findAttribute(attributes, CombatUnit.ATTR_ATTACK);
        double maxHp = findAttribute(attributes, CombatUnit.ATTR_MAX_HP);
        double currentHp = findAttribute(
                attributes, CombatUnit.ATTR_CURRENT_HP);

        /*
         * 若存在当前血量属性则以其为初始血量；否则用最大血量。
         * 服务端下发时通常两者相同，防御性地兜底。
         */
        double initialHp = currentHp > 0 ? currentHp : maxHp;

        CombatUnit unit = new CombatUnit(id, attack, initialHp, x, y, z);
        unit.setDir(sceneVo.getBaseInfoVo().getDir());
        if (sceneVo.hasSceneMonsterVo()) {
            unit.setTemplateId(sceneVo.getSceneMonsterVo().getTemplateId());
            unit.setMonsterId(sceneVo.getSceneMonsterVo().getMonsterId());
        }
        return unit;
    }

    private static double findAttribute(
            List<AttributeVO> attributes,
            int type) {
        for (AttributeVO attribute : attributes) {
            if (attribute.getType() == type) {
                return attribute.getValue();
            }
        }
        return 0;
    }
}
