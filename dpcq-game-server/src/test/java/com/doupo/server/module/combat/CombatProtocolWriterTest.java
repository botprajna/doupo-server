package com.doupo.server.module.combat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.doupo.protocol.ActionVo;
import com.doupo.protocol.DamageTypeVo;
import com.doupo.protocol.SkillActionResp;
import com.doupo.protocol.UseSkillResp;
import com.google.protobuf.GeneratedMessageV3;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CombatProtocolWriterTest {

    private static final class RecordedMessage {
        private final int protocolId;
        private final GeneratedMessageV3 message;

        private RecordedMessage(int protocolId, GeneratedMessageV3 message) {
            this.protocolId = protocolId;
            this.message = message;
        }
    }

    @Test
    public void writesUseSkillRespThenSkillActionRespInOrder() {
        CombatUnit monster = new CombatUnit(101L, 220, 460, 1, 0, 2);
        CombatUnit player = new CombatUnit(1L, 100, 12613.2, 0, 0, 0);
        CombatSession session = new CombatSession(10100101, player);
        session.addMonster(monster);

        MonsterAttack attack = session.tick(0L).get(0);
        List<RecordedMessage> recorded = new ArrayList<>();

        CombatProtocolWriter.writeMonsterAttack(
                attack,
                (protocolId, message) -> recorded.add(
                        new RecordedMessage(protocolId, message)));

        assertEquals(2, recorded.size());
        assertEquals(50762, recorded.get(0).protocolId);
        assertTrue(recorded.get(0).message instanceof UseSkillResp);
        assertEquals(50763, recorded.get(1).protocolId);
        assertTrue(recorded.get(1).message instanceof SkillActionResp);
    }

    @Test
    public void useSkillRespCarriesAttackerAndTargetIdentity() {
        CombatUnit monster = new CombatUnit(101L, 220, 460, 1, 0, 2);
        CombatUnit player = new CombatUnit(1L, 100, 12613.2, 0, 0, 0);
        CombatSession session = new CombatSession(10100101, player);
        session.addMonster(monster);

        MonsterAttack attack = session.tick(0L).get(0);
        UseSkillResp[] captured = new UseSkillResp[1];

        CombatProtocolWriter.writeMonsterAttack(
                attack,
                (protocolId, message) -> {
                    if (protocolId == 50762) {
                        captured[0] = (UseSkillResp) message;
                    }
                });

        assertEquals(101L, captured[0].getAttackId());
        assertEquals(1L, captured[0].getTargetId());
        assertEquals(
                CombatSession.MONSTER_NORMAL_ATTACK_SKILLS[0],
                captured[0].getSkillId());
        assertTrue(captured[0].getServerAdvance());
    }

    @Test
    public void skillActionRespContainsRageDamageThenPlayerHpInThatOrder() {
        CombatUnit monster = new CombatUnit(101L, 220, 460, 1, 0, 2);
        CombatUnit player = new CombatUnit(1L, 100, 12613.2, 0, 0, 0);
        CombatSession session = new CombatSession(10100101, player);
        session.addMonster(monster);

        MonsterAttack attack = session.tick(0L).get(0);
        SkillActionResp[] captured = new SkillActionResp[1];

        CombatProtocolWriter.writeMonsterAttack(
                attack,
                (protocolId, message) -> {
                    if (protocolId == 50763) {
                        captured[0] = (SkillActionResp) message;
                    }
                });

        SkillActionResp response = captured[0];
        assertEquals(3, response.getActionListCount());

        ActionVo rage = response.getActionList(0);
        assertTrue(rage.hasAttributeActionVo());
        assertFalse(rage.hasDamageVo());
        assertEquals(101L, rage.getAttributeActionVo().getAttackId());
        assertEquals(101L, rage.getAttributeActionVo().getTargetId());
        assertEquals(103111, rage.getAttributeActionVo().getAttrList(0).getType());

        ActionVo damage = response.getActionList(1);
        assertTrue(damage.hasDamageVo());
        assertEquals(220.0, damage.getDamageVo().getDamage(), 0.0001);
        assertEquals(DamageTypeVo.GENERAL_SKILL, damage.getDamageVo().getType());
        assertEquals(101L, damage.getDamageVo().getAttackId());
        assertEquals(1L, damage.getDamageVo().getTargetId());

        ActionVo playerHp = response.getActionList(2);
        assertTrue(playerHp.hasAttributeActionVo());
        assertEquals(1L, playerHp.getAttributeActionVo().getTargetId());
        assertEquals(
                CombatUnit.ATTR_CURRENT_HP,
                playerHp.getAttributeActionVo().getAttrList(0).getType());
        assertEquals(
                12393.2,
                playerHp.getAttributeActionVo().getAttrList(0).getValue(),
                0.0001);
    }

    @Test
    public void sixthBossRageSkillWritesCapturedImpactPacket() {
        CombatUnit monster = new CombatUnit(101L, 2300, 17000, 0, 0, 0);
        CombatUnit player = new CombatUnit(1L, 1189.07668, 30991.35, 0, 0, 0);
        CombatSession session = new CombatSession(10200105, player);
        session.addMonster(monster);

        MonsterAttack attack = null;
        for (int i = 0; i < 7; i++) {
            attack = session.tick(
                    i * CombatSession.BOSS_ATTACK_INTERVAL_MILLIS).get(0);
        }
        List<RecordedMessage> recorded = new ArrayList<>();
        CombatProtocolWriter.writeMonsterAttack(
                attack,
                (protocolId, message) -> recorded.add(
                        new RecordedMessage(protocolId, message)));

        assertEquals(3, recorded.size());
        UseSkillResp use = (UseSkillResp) recorded.get(0).message;
        assertEquals(51320110101L, use.getSkillId());
        assertEquals(1.333f, use.getTimeRatio(), 0.0001f);

        SkillActionResp start =
                (SkillActionResp) recorded.get(1).message;
        assertEquals(2, start.getActionListCount());
        assertEquals(0,
                start.getActionList(0).getAttributeActionVo()
                        .getAttrList(0).getValue(),
                0.0001);
        assertEquals(9,
                start.getActionList(1).getAttributeActionVo()
                        .getAttrList(0).getValue(),
                0.0001);

        SkillActionResp impact =
                (SkillActionResp) recorded.get(2).message;
        assertEquals(0, impact.getSkillId());
        assertEquals(8431,
                impact.getActionList(0).getDamageVo().getDamage(),
                0.0001);
        assertEquals(5080101L,
                impact.getActionList(0).getDamageVo().getActionId());
        assertEquals(51320110101L,
                impact.getActionList(0).getDamageVo().getSkillId());
        assertEquals(DamageTypeVo.FIGHT_SKILL,
                impact.getActionList(0).getDamageVo().getType());
        assertEquals(0,
                impact.getActionList(1).getAttributeActionVo()
                        .getAttackId());
    }
}
