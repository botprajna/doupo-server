package com.doupo.server.module.combat;

/**
 * 一个战斗单位在服务端的内存状态。
 *
 * <p>本服目前不持久化战斗中的血量：一场引导战结束后单位即失效，
 * 因此这里只保存推进战斗所必需的最小状态。
 *
 * <p>数值来源全部是服务端进入场景时已经下发给客户端的
 * {@code SceneUnitVo.fightInfoVo.attributeList}，不额外引入配置表数值。
 */
public final class CombatUnit {

    /** 攻击力属性。 */
    public static final int ATTR_ATTACK = 101001;

    /** 当前血量属性；客户端血条读的就是这个值。 */
    public static final int ATTR_CURRENT_HP = 103011;

    /** 最大血量属性。 */
    public static final int ATTR_MAX_HP = 103001;

    private final long sceneUnitId;
    private final double attack;
    private final double maxHp;

    private double currentHp;
    private float x;
    private float y;
    private float z;
    private float dir;
    private long templateId;
    private int monsterId;

    public CombatUnit(
            long sceneUnitId,
            double attack,
            double maxHp,
            float x,
            float y,
            float z) {

        if (sceneUnitId == 0) {
            throw new IllegalArgumentException(
                    "sceneUnitId must not be zero");
        }
        if (maxHp <= 0) {
            throw new IllegalArgumentException(
                    "maxHp must be positive: " + maxHp);
        }

        this.sceneUnitId = sceneUnitId;
        this.attack = Math.max(0, attack);
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long getSceneUnitId() {
        return sceneUnitId;
    }

    public double getAttack() {
        return attack;
    }

    public double getMaxHp() {
        return maxHp;
    }

    public double getCurrentHp() {
        return currentHp;
    }

    public boolean isAlive() {
        return currentHp > 0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void moveTo(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getDir() {
        return dir;
    }

    public void setDir(float dir) {
        this.dir = dir;
    }

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    /** SceneMonsterVo.monsterId 对应 MonsterSkillConfig.Id，不是波次模板 ID。 */
    public int getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(int monsterId) {
        this.monsterId = monsterId;
    }

    /**
     * 扣血并返回实际造成的伤害。
     *
     * <p>血量下限为 0，因此实际伤害可能小于请求的伤害。客户端需要的是扣血
     * 之后的绝对值（见 {@link CombatProtocolWriter}），由调用方读
     * {@link #getCurrentHp()} 取得。
     *
     * @param damage 期望造成的伤害；非正数不产生任何效果
     * @return 实际扣掉的血量
     */
    public double applyDamage(double damage) {
        return applyDamage(damage, 0);
    }

    double applyDamage(double damage, double minimumHp) {
        if (damage <= 0 || !isAlive()) {
            return 0;
        }

        double applied = Math.min(damage, Math.max(0, currentHp - minimumHp));
        currentHp -= applied;
        return applied;
    }

    public void restoreFullHealth() {
        currentHp = maxHp;
    }

    public double distanceTo(CombatUnit other) {
        double dx = x - other.x;
        double dy = y - other.y;
        double dz = z - other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
