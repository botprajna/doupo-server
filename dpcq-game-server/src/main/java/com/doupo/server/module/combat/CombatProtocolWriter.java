package com.doupo.server.module.combat;

import com.doupo.protocol.ActionVo;
import com.doupo.protocol.AttributeActionVO;
import com.doupo.protocol.AttributeVO;
import com.doupo.protocol.DamageTypeVo;
import com.doupo.protocol.DamageVO;
import com.doupo.protocol.SkillActionResp;
import com.doupo.protocol.UseSkillResp;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 把 {@link MonsterAttack} 翻译成官服抓包中怪物普攻的下推协议序列。
 *
 * <p>每次伤害下推 <b>两条</b> 协议：
 * <ol>
 *   <li>{@code UseSkillResp(50762)} —— 服务端确认怪物释放技能；</li>
 *   <li>{@code SkillActionResp(50763)} —— 技能动作，含三个
 *       {@link ActionVo}：怪物怒气、伤害、玩家扣血绝对值。</li>
 * </ol>
 *
 * <p>结构对照抓包 {@code 第一次创角色.json} idx 833 一帧（怪物攻击玩家）：
 * <pre>
 *   UseSkillResp:  attackId=怪物, targetId=玩家, skillId=201110110101, serverAdvance=true
 *   SkillActionResp: skillId=同上, actionList=[
 *     {actionVoTypeId=18, attributeActionVO{attrList=[{103111, 怒气}], attackId=怪物, targetId=怪物}},
 *     {actionVoTypeId=1,  damageVO{damage, type=GENERAL_SKILL, attackId=怪物, targetId=玩家}},
 *     {actionVoTypeId=18, attributeActionVO{attrList=[{103011, 扣血后绝对值}], attackId=怪物, targetId=玩家}},
 *   ]
 * </pre>
 *
 * <p>注意：客户端 {@code FightResultFactory.CreateFightResultList} 只按子消息是
 * 否存在来分发，不读 {@code actionVoTypeId}；该字段仅为与官服抓包保持一致。
 */
public final class CombatProtocolWriter {

    private CombatProtocolWriter() {
    }

    /**
     * 把一次怪物普攻翻译成两条协议并交给 writer 下推。
     *
     * @param writer 接受一条协议号的回调
     */
    public static void writeMonsterAttack(
            MonsterAttack attack,
            ProtocolWriter writer) {

        writer.write(50762, buildUseSkillResp(attack));
        if (attack.isDelayedImpact()) {
            writer.write(50763, buildImpactStartResp(attack));
            writer.write(50763, buildImpactResp(attack));
        } else {
            writer.write(50763, buildSkillActionResp(attack));
        }
    }

    private static UseSkillResp buildUseSkillResp(MonsterAttack attack) {
        return UseSkillResp.newBuilder()
                .setAttackId(attack.getMonsterSceneUnitId())
                .setSkillId(attack.getSkillId())
                .setTargetId(attack.getTargetSceneUnitId())
                .setCurX(attack.getMonsterX())
                .setCurY(attack.getMonsterY())
                .setCurZ(attack.getMonsterZ())
                .setTargetX(attack.getTargetX())
                .setTargetY(attack.getTargetY())
                .setTargetZ(attack.getTargetZ())
                .setAngle(180)
                .setStartType(0)
                .setServerAdvance(true)
                .setSkillNo(ThreadLocalRandom.current().nextInt())
                .setFailCode(0)
                .setTimeRatio(attack.getTimeRatio())
                .setPlaySkillBlackMask(false)
                .build();
    }

    private static SkillActionResp buildImpactStartResp(
            MonsterAttack attack) {
        SkillActionResp.Builder response = SkillActionResp.newBuilder()
                .setSkillId(attack.getSkillId());
        if (attack.isResetRage()) {
            response.addActionList(buildMonsterRageAction(attack, 0));
        }
        response.addActionList(buildMonsterRageAction(
                attack,
                attack.getMonsterRage()));
        return response.build();
    }

    private static SkillActionResp buildImpactResp(MonsterAttack attack) {
        return SkillActionResp.newBuilder()
                .setSkillId(0)
                .addActionList(buildDamageAction(attack))
                .addActionList(buildPlayerHpAction(attack, true))
                .build();
    }

    private static SkillActionResp buildSkillActionResp(MonsterAttack attack) {
        SkillActionResp.Builder response = SkillActionResp.newBuilder()
                .setSkillId(attack.getSkillId());

        response.addActionList(buildMonsterRageAction(attack));
        response.addActionList(buildDamageAction(attack));
        response.addActionList(buildPlayerHpAction(attack));

        return response.build();
    }

    /** 怪物自身怒气变化（103111 递增）。 */
    private static ActionVo buildMonsterRageAction(MonsterAttack attack) {
        return buildMonsterRageAction(attack, attack.getMonsterRage());
    }

    private static ActionVo buildMonsterRageAction(
            MonsterAttack attack,
            double rage) {
        long monsterId = attack.getMonsterSceneUnitId();

        return ActionVo.newBuilder()
                .setActionVoTypeId(18)
                .setAttributeActionVo(AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103111, rage))
                        .setAttackId(monsterId)
                        .setTargetId(monsterId)
                        .build())
                .build();
    }

    /** 伤害（damageVO）。 */
    private static ActionVo buildDamageAction(MonsterAttack attack) {
        return ActionVo.newBuilder()
                .setActionVoTypeId(1)
                .setDamageVo(DamageVO.newBuilder()
                        .setDamage(attack.getDamage())
                        .setSpecial(attack.getSpecial())
                        .setActionId(attack.getDamageActionId())
                        .setType(attack.getDamageType())
                        .setSkillId(attack.getDamageSkillId())
                        .setAttackId(attack.getMonsterSceneUnitId())
                        .setTargetId(attack.getTargetSceneUnitId())
                        .build())
                .build();
    }

    /** 玩家扣血后绝对值（103011）。 */
    private static ActionVo buildPlayerHpAction(MonsterAttack attack) {
        return buildPlayerHpAction(attack, false);
    }

    private static ActionVo buildPlayerHpAction(
            MonsterAttack attack,
            boolean impact) {
        return ActionVo.newBuilder()
                .setActionVoTypeId(18)
                .setAttributeActionVo(AttributeActionVO.newBuilder()
                        .addAttrList(attribute(
                                CombatUnit.ATTR_CURRENT_HP,
                                attack.getTargetHpAfterDamage()))
                        .setAttackId(
                                impact
                                        ? 0
                                        : attack.getMonsterSceneUnitId())
                        .setTargetId(attack.getTargetSceneUnitId())
                        .build())
                .build();
    }

    private static AttributeVO attribute(int type, double value) {
        return AttributeVO.newBuilder()
                .setType(type)
                .setValue(value)
                .build();
    }

    /**
     * 协议下推回调，供 {@link CombatProtocolWriter} 与具体的写渠道解耦，
     * 便于在测试里拦截断言。
     */
    public interface ProtocolWriter {

        /**
         * 下推一条协议。
         *
         * @param protocolId 协议号
         * @param message    序列化后的消息体
         */
        void write(int protocolId, com.google.protobuf.GeneratedMessageV3 message);
    }
}
