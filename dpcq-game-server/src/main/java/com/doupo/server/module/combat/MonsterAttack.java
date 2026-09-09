package com.doupo.server.module.combat;

import com.doupo.protocol.DamageTypeVo;

/**
 * 一次怪物普攻的结算结果。
 *
 * <p>由 {@link CombatSession} 产出，{@link CombatProtocolWriter} 消费后翻译成
 * {@code UseSkillResp} 与 {@code SkillActionResp} 两条下推协议。
 */
public final class MonsterAttack {

    private final long monsterSceneUnitId;
    private final long targetSceneUnitId;
    private final long skillId;
    private final long damageActionId;
    private final long damageSkillId;
    private final DamageTypeVo damageType;
    private final int special;
    private final boolean delayedImpact;
    private final boolean resetRage;
    private final float timeRatio;
    private final double damage;
    private final double targetHpAfterDamage;
    private final double monsterRage;
    private final float monsterX;
    private final float monsterY;
    private final float monsterZ;
    private final float targetX;
    private final float targetY;
    private final float targetZ;

    MonsterAttack(
            CombatUnit monster,
            CombatUnit target,
            long skillId,
            long damageActionId,
            long damageSkillId,
            DamageTypeVo damageType,
            int special,
            boolean delayedImpact,
            boolean resetRage,
            float timeRatio,
            double damage,
            double monsterRage) {

        this.monsterSceneUnitId = monster.getSceneUnitId();
        this.targetSceneUnitId = target.getSceneUnitId();
        this.skillId = skillId;
        this.damageActionId = damageActionId;
        this.damageSkillId = damageSkillId;
        this.damageType = damageType;
        this.special = special;
        this.delayedImpact = delayedImpact;
        this.resetRage = resetRage;
        this.timeRatio = timeRatio;
        this.damage = damage;
        this.targetHpAfterDamage = target.getCurrentHp();
        this.monsterRage = monsterRage;
        this.monsterX = monster.getX();
        this.monsterY = monster.getY();
        this.monsterZ = monster.getZ();
        this.targetX = target.getX();
        this.targetY = target.getY();
        this.targetZ = target.getZ();
    }

    public long getMonsterSceneUnitId() {
        return monsterSceneUnitId;
    }

    public long getTargetSceneUnitId() {
        return targetSceneUnitId;
    }

    public long getSkillId() {
        return skillId;
    }

    public long getDamageActionId() {
        return damageActionId;
    }

    public long getDamageSkillId() {
        return damageSkillId;
    }

    public DamageTypeVo getDamageType() {
        return damageType;
    }

    public int getSpecial() {
        return special;
    }

    public boolean isDelayedImpact() {
        return delayedImpact;
    }

    public boolean isResetRage() {
        return resetRage;
    }

    public float getTimeRatio() {
        return timeRatio;
    }

    public double getDamage() {
        return damage;
    }

    /** 客户端血条需要的扣血后绝对值。 */
    public double getTargetHpAfterDamage() {
        return targetHpAfterDamage;
    }

    /** 怪物怒气，对应抓包里随普攻递增的属性 103111。 */
    public double getMonsterRage() {
        return monsterRage;
    }

    public float getMonsterX() {
        return monsterX;
    }

    public float getMonsterY() {
        return monsterY;
    }

    public float getMonsterZ() {
        return monsterZ;
    }

    public float getTargetX() {
        return targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public float getTargetZ() {
        return targetZ;
    }
}
