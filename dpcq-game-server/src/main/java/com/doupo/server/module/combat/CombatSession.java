package com.doupo.server.module.combat;

import com.doupo.protocol.DamageTypeVo;
import com.doupo.protocol.FightMoveVo;
import com.doupo.protocol.MoveResp;
import com.doupo.protocol.SceneUnitVo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一名玩家在一场引导战中的服务端战斗状态。
 *
 * <p>本类是纯逻辑：不依赖 Akka、Netty 或协议类，时间由调用方以毫秒时间戳传入，
 * 因此可以在单元测试里确定性地推进。协议下推由 {@link CombatProtocolWriter} 负责。
 *
 * <p>线程模型：一个会话只会被它所属玩家的 Actor 访问，因此内部不加锁。
 * 定时推进也是通过投递消息回同一个 Actor 完成的，不存在并发访问。
 *
 * <p>行为参照官服抓包 {@code 第一次创角色.json}（idx 832-895）：怪物以约 1 秒的
 * 间隔轮流使用两个普攻技能攻击玩家，服务端下推 {@code UseSkillResp} 与
 * {@code SkillActionResp}，其中玩家血量是扣血后的绝对值。
 */
public final class CombatSession {

    private static final Logger LOGGER = LoggerFactory.getLogger(CombatSession.class);

    /**
     * 怪物普攻技能，取自官服抓包中怪物 {@code UseSkillResp.skillId}，
     * 两个技能交替使用。
     */
    static final long[] MONSTER_NORMAL_ATTACK_SKILLS = {
            201110110101L,
            201110210101L
    };

    /** 普攻间隔，抓包中相邻两次怪物攻击相差约 0.86 ~ 1.0 秒。 */
    static final long ATTACK_INTERVAL_MILLIS = 1000L;

    /** 三、四、六关 Boss 抓包中相邻起手约为 1.125 秒。 */
    static final long BOSS_ATTACK_INTERVAL_MILLIS = 1125L;

    /** 怪物怒气每次普攻的增量，对应抓包里 103111 的递增步长。 */
    static final double RAGE_PER_ATTACK = 9;

    /**
     * 怪物进入攻击距离的阈值。
     *
     * <p>取值依据实测：第一小关四只怪沿 z 轴每 15 个单位放一只
     * （见 {@code guidanceMonsterSnapshots}），因此阈值必须显著小于 15，
     * 否则玩家打第一只怪时，15 个单位外的第二只怪也会被判定进入攻击范围，
     * 在原地对着空气挥手。
     *
     * <p>官服抓包里怪物攻击玩家时两者坐标几乎重合（距离≈ 0），说明这是贴身
     * 近战。取 5 既覆盖贴身交战的抖动，又与相邻怪物的 15 拉开足够间距。
     */
    static final double ATTACK_RANGE = 5.0;

    /** 怪物追击速度，与抓包 {@code ChaseStartResp.speed} 同量级。 */
    static final float CHASE_SPEED = 35.5f;

    // 第六关抓包 idx 2010-2076：MoveResp 每约 66ms 推进 0.33 单位，
    // angularSpeed=5；客户端 ComponentMove.OnBattleMove 将该字段当移动速度。
    private static final long MONSTER_MOVE_INTERVAL_MILLIS = 66L;
    private static final float MONSTER_MOVE_SPEED = 5f;

    private final int chapterId;
    private final CombatUnit player;
    private final Map<Long, MonsterState> monsters = new LinkedHashMap<>();

    private boolean active = true;
    private boolean paused;
    private boolean playerAttackStarted;
    private boolean ninthBossGuideStarted;
    private boolean ninthBossGuidePending;
    private long ninthBossGuideReadyAtMillis;
    private SceneUnitVo ninthBossPlayerSnapshot;
    /** 玩家怒气（放主动技能 10150510101 的条）：攻击命中 +2、挨打 +18、上限 100（抓包 idx 2008-2054）。 */
    private int mp = 0;

    /**
     * 本波进场战斗点 Z（客户端 {@code GuidanceMainMapMonsterEnterReq.z}）。
     *
     * <p>第三波地面点 104.6 后面 10 单位就是跳台（114.7）。官服会先让远怪走近，
     * 玩家追击只落到 109.9；没有走位包时必须拿这个 Z 把追击截在跳点前。
     */
    private float battleCenterZ;

    public int getMp() {
        return mp;
    }

    /** 增加/扣除怒气，钳制到 [0, 100]。返回最新怒气。 */
    public int addMp(int delta) {
        mp = Math.max(0, Math.min(100, mp + delta));
        return mp;
    }

    private static final AttackDefinition[] THIRD_BOSS_ATTACKS = {
            normal(51410110101L, 40210110101L, 491, 39, 1.167f),
            normal(51410210101L, 40210110101L, 491, 48, 1.167f),
            normal(51410110101L, 40210110101L, 491, 57, 1.167f),
            normal(51410210101L, 40210110101L, 343, 66, 1.167f),
            normal(51410110101L, 40210110101L, 491, 75, 1.167f),
            normal(51410210101L, 40210110101L, 491, 84, 1.167f)
    };

    private static final AttackDefinition[] FOURTH_BOSS_ATTACKS = {
            normal(51310110101L, 51310110101L, 1368, 39, 1.333f),
            normal(51310110101L, 51310110101L, 1368, 48, 1.333f),
            normal(51310110101L, 51310110101L, 1044, 57, 1.333f),
            normal(51310110101L, 51310110101L, 1044, 66, 1.333f),
            normal(51310110101L, 51310110101L, 1044, 75, 1.333f),
            normal(51310110101L, 51310110101L, 549, 84, 1.333f),
            normal(51310110101L, 51310110101L, 549, 93, 1.333f),
            normal(51310110101L, 51310110101L, 1044, 100, 1.333f)
    };

    private static final AttackDefinition[] SIXTH_BOSS_ATTACKS = {
            normal(51310110101L, 51310110101L, 1686, 39, 1.333f),
            normal(51310110101L, 51310110101L, 1686, 56, 1.333f),
            normal(51310110101L, 51310110101L, 1686, 65, 1.333f),
            normal(51310110101L, 51310110101L, 1686, 82, 1.333f),
            normal(51310110101L, 51310110101L, 1180, 93, 1.333f),
            normal(51310110101L, 51310110101L, 1686, 100, 1.333f),
            impact(51320110101L, 5080101L, 8431, 9, 1.333f, true),
            normal(51310110101L, 51310110101L, 1686, 22, 1.333f)
    };

    /** 第七关 Boss：技能 50210110101/50210210101，伤害 1110/777，抓包 idx 6326-6426。 */
    private static final AttackDefinition[] SEVENTH_BOSS_ATTACKS = {
            normal(50210110101L, 50210110101L, 1110, 30, 1.333f),
            normal(50210210101L, 50210210101L, 1110, 30, 1.333f),
            normal(50210110101L, 50210110101L, 1110, 30, 1.333f),
            normal(50210210101L, 50210210101L, 1110, 30, 1.333f),
            normal(50210110101L, 50210110101L, 777, 30, 1.333f),
            normal(50210210101L, 50210210101L, 777, 30, 1.333f)
    };

    /** 第八关 Boss：技能 51410110101/51410210101，伤害 801/1145，抓包。 */
    private static final AttackDefinition[] EIGHTH_BOSS_ATTACKS = {
            normal(51410110101L, 51410110101L, 801, 30, 1.333f),
            normal(51410210101L, 51410210101L, 801, 30, 1.333f),
            normal(51410110101L, 51410110101L, 801, 30, 1.333f),
            normal(51410210101L, 51410210101L, 1145, 30, 1.333f),
            normal(51410110101L, 51410110101L, 1145, 30, 1.333f),
            normal(51410210101L, 51410210101L, 1145, 30, 1.333f)
    };

    /** 第九关 Boss：技能与单段伤害取自抓包 idx 8110-8230。 */
    private static final AttackDefinition[] NINTH_BOSS_ATTACKS = {
            normal(50810210101L, 50810210101L, 4336, 39, 1.333f),
            normal(50810110101L, 50810110101L, 17345, 52, 1.333f),
            normal(50810210101L, 50810210101L, 4336, 69, 1.333f),
            normal(50810210101L, 50810210101L, 1865, 88, 1.333f)
    };

    /** 第十关 Boss：技能与单段伤害取自抓包 idx 9352-9520。 */
    private static final AttackDefinition[] TENTH_BOSS_ATTACKS = {
            normal(50210110101L, 50210110101L, 1362, 39, 1.567f),
            normal(50210210101L, 50210210101L, 1362, 52, 1.567f),
            normal(50210110101L, 50210110101L, 1946, 63, 1.567f),
            normal(50210210101L, 50210210101L, 1362, 74, 1.567f),
            normal(50210110101L, 50210110101L, 1362, 89, 1.567f),
            normal(50210210101L, 50210210101L, 1946, 100, 1.567f),
            impact(50220110101L, 50220110102L, 7783, 9, 1.567f, true),
            normal(50210110101L, 50210110101L, 1946, 20, 1.567f),
            normal(50210210101L, 50210210101L, 1362, 35, 1.567f)
    };

    public CombatSession(int chapterId, CombatUnit player) {
        if (player == null) {
            throw new IllegalArgumentException("player must not be null");
        }

        this.chapterId = chapterId;
        this.player = player;
    }

    public int getChapterId() {
        return chapterId;
    }

    public CombatUnit getPlayer() {
        return player;
    }

    /**
     * 战斗是否仍在推进。玩家死亡、怪物全灭或战斗被显式结束后为 {@code false}。
     */
    public boolean isActive() {
        return active
                && player.isAlive()
                && hasAliveMonster();
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /** 抓包 idx 8204→8209：第九关首次濒死才触发药老教学。 */
    public boolean tryBeginNinthBossGuide() {
        return tryBeginNinthBossGuide(System.currentTimeMillis());
    }

    public boolean tryBeginNinthBossGuide(long nowMillis) {
        if (!active || chapterId != 10200405 || ninthBossGuideStarted
                || player.getCurrentHp() != 1 || !hasAliveMonster()
                || (ninthBossGuidePending && nowMillis < ninthBossGuideReadyAtMillis)) {
            return false;
        }
        ninthBossGuidePending = false;
        ninthBossGuideStarted = true;
        paused = true;
        return true;
    }

    public boolean isNinthBossGuideStarted() {
        return ninthBossGuideStarted;
    }

    public void setNinthBossPlayerSnapshot(SceneUnitVo snapshot) {
        ninthBossPlayerSnapshot = snapshot;
    }

    public SceneUnitVo getNinthBossPlayerSnapshot() {
        return ninthBossPlayerSnapshot;
    }

    public boolean isPaused() {
        return paused || ninthBossGuidePending;
    }

    public void addMonster(CombatUnit monster) {
        if (monster == null) {
            throw new IllegalArgumentException("monster must not be null");
        }

        monsters.put(
                monster.getSceneUnitId(),
                new MonsterState(monster));
    }

    public Collection<CombatUnit> getMonsters() {
        List<CombatUnit> result = new ArrayList<>(monsters.size());
        for (MonsterState state : monsters.values()) {
            result.add(state.unit);
        }
        return result;
    }

    public CombatUnit getMonster(long sceneUnitId) {
        MonsterState state = monsters.get(sceneUnitId);
        return state == null ? null : state.unit;
    }

    public boolean hasAliveMonster() {
        for (MonsterState state : monsters.values()) {
            if (state.unit.isAlive()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 用客户端上报的玩家坐标更新玩家位置。
     *
     * <p>玩家在场景里会主动走位贴近怪物（客户端自动寻路），官服抓包显示玩家与
     * 正在交战的怪是贴身的（距离≈ 0）。服务端若不跟踪玩家位置，就会一直按
     * 玩家进场时的初始坐标算距离，导致怪物永远超出攻击范围。因此每次收到
     * {@code 61974} 进场或 {@code 61998} 技能上报时，把玩家的最新坐标同步进来。
     */
    public void updatePlayerPosition(float x, float y, float z) {
        player.moveTo(x, y, z);
    }

    public void setBattleCenterZ(float battleCenterZ) {
        this.battleCenterZ = battleCenterZ;
    }

    public float getBattleCenterZ() {
        return battleCenterZ;
    }

    /** @return 当前波是否是玩家第一次出手。 */
    public boolean markPlayerAttackStarted() {
        if (playerAttackStarted) {
            return false;
        }
        playerAttackStarted = true;
        return true;
    }

    /**
     * 标记一只怪物已死亡。
     *
     * <p>击杀判定仍然由客户端上报（{@code 61964} / {@code 61997}），服务端在这里
     * 同步内存状态，使该怪物停止攻击。
     *
     * @return 如果这次调用确实让一只存活的怪物转为死亡则返回 {@code true}
     */
    public boolean markMonsterDead(long sceneUnitId) {
        MonsterState state = monsters.get(sceneUnitId);

        if (state == null || !state.unit.isAlive()) {
            return false;
        }

        state.unit.applyDamage(state.unit.getCurrentHp());
        return true;
    }

    /** 显式结束战斗，例如客户端发来 {@code MainMapEndFightReq}。 */
    public void deactivate() {
        active = false;
    }

    /**
     * 推进一次战斗。
     *
     * <p>对每只存活且已进入攻击距离的怪物，若普攻冷却已到则结算一次伤害。
     * 返回的列表保持怪物加入顺序，便于测试断言与协议下推顺序稳定。
     *
     * @param nowMillis 当前时间戳，毫秒
     * @return 本次推进产生的所有怪物普攻；没有攻击时返回空列表
     */
    public List<MonsterAttack> tick(long nowMillis) {
        if (!isActive() || isPaused()) {
            return java.util.Collections.emptyList();
        }

        List<MonsterAttack> attacks = new ArrayList<>();

        for (MonsterState state : monsters.values()) {
            if (!state.unit.isAlive()) {
                continue;
            }
            if (!player.isAlive()) {
                break;
            }
            if (hasMonsterMovement() && chapterId >= 10200201 && !state.alerted) {
                continue;
            }

            double distance = state.unit.distanceTo(player);
            if (distance > attackRange(state.unit)) {
                LOGGER.debug(
                        "Monster out of attack range: unit={}, distance={}",
                        state.unit.getSceneUnitId(), distance);
                continue;
            }
            if (!state.isAttackReady(nowMillis)) {
                LOGGER.debug(
                        "Monster attack on cooldown: unit={}, nextAt={}, now={}",
                        state.unit.getSceneUnitId(),
                        state.nextAttackAtMillis, nowMillis);
                continue;
            }

            AttackDefinition definition = attackDefinition(
                    state.unit,
                    state.attackCount);
            if (definition == null) {
                continue;
            }
            MonsterAttack attack = state.attack(
                    player,
                    nowMillis,
                    definition,
                    attackIntervalMillis(),
                    chapterId == 10200405);
            LOGGER.info(
                    "Monster attack: chapter={}, unit={}, skill={}, damage={}, rage={}, seq={}",
                    chapterId,
                    state.unit.getSceneUnitId(),
                    definition.skillId,
                    definition.damage,
                    definition.rage,
                    state.attackCount);
            attacks.add(attack);
            if (chapterId == 10200405 && !ninthBossGuideStarted && player.getCurrentHp() == 1) {
                // 6.9.263 Skill/Skills：508102101的rs=3467ms，508101101的rs=1100ms。
                // 按UseSkillResp.timeRatio缩放，等旧动画的命中结算完成后才能发教学回血。
                // 抓包8170→8207也在攻击后摇时恢复；不能与致命50763在同一tick恢复。
                long recoveryMillis = definition.skillId == 50810210101L ? 3467L : 1100L;
                ninthBossGuideReadyAtMillis = nowMillis + (long) Math.ceil(recoveryMillis / (double) definition.timeRatio);
                ninthBossGuidePending = true;
                LOGGER.info("Ninth boss teaching pending: unit={}, skill={}, hp=1, readyAt={}",
                        player.getSceneUnitId(), definition.skillId, ninthBossGuideReadyAtMillis);
                break;
            }
        }

        return attacks;
    }

    /**
     * 怪物是否需要先靠近玩家才能攻击。
     *
     * <p>最小实现只回答“要不要追”，真正的寻路留给后续迭代。
     */
    public boolean needsChase(long monsterSceneUnitId) {
        MonsterState state = monsters.get(monsterSceneUnitId);

        return state != null
                && state.unit.isAlive()
                && state.unit.distanceTo(player) > attackRange(state.unit);
    }

    /** 魔兽山脉第 6～10 关三波共用实时战斗；Boss 与乌坦城战报不走此移动链路。 */
    private boolean hasMonsterMovement() {
        int wave = chapterId % 100;
        return chapterId >= 10200101 && chapterId <= 10200503 && wave >= 1 && wave <= 3;
    }

    private double attackRange(CombatUnit monster) {
        if (hasMonsterMovement() && chapterId >= 10200201) {
            // 6.9.263 SkillFightConfig.CastRadius：121001 的普攻 3，121002 的普攻 6，13 的普攻 5。
            if (monster.getMonsterId() == 121001) return 3d;
            if (monster.getMonsterId() == 121002) return 6d;
        }
        return ATTACK_RANGE;
    }

    public long getTickIntervalMillis() {
        return hasMonsterMovement() ? MONSTER_MOVE_INTERVAL_MILLIS : ATTACK_INTERVAL_MILLIS;
    }

    /** 与 50793 同时激活本波对象；重复提示不能重置移动和攻击冷却。 */
    public void alertMonster(long sceneUnitId) {
        MonsterState state = monsters.get(sceneUnitId);
        if (hasMonsterMovement() && state != null && state.unit.isAlive()) {
            state.alerted = true;
        }
    }

    /**
     * 先推进上帧已下发的移动，再按玩家最新位置下发跑动/停步；调用后才结算攻击。
     * 仅在当前波的直线战斗区内走近，不实现跨场景或跨跳台寻路。
     */
    public List<MoveResp> advanceMonsterMovement(long nowMillis) {
        if (!hasMonsterMovement()) {
            return java.util.Collections.emptyList();
        }
        List<MoveResp> moves = new ArrayList<>();
        boolean canMove = isActive() && !paused;
        for (MonsterState state : monsters.values()) {
            CombatUnit unit = state.unit;
            long elapsed = state.moving ? Math.max(0L, nowMillis - state.lastMoveAtMillis) : 0L;
            state.lastMoveAtMillis = nowMillis;
            if (!unit.isAlive() || !state.alerted) {
                state.moving = false;
                continue;
            }
            if (!canMove) {
                if (state.moving) {
                    moves.add(monsterMove(unit, unit.getX(), unit.getY(), unit.getZ(), 4));
                    state.moving = false;
                }
                continue;
            }
            if (state.moving) {
                double dx = state.moveX - unit.getX();
                double dy = state.moveY - unit.getY();
                double dz = state.moveZ - unit.getZ();
                double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                double step = MONSTER_MOVE_SPEED * (elapsed / 1000d);
                double ratio = distance > 0d ? Math.min(1d, step / distance) : 0d;
                unit.moveTo((float) (unit.getX() + dx * ratio),
                        (float) (unit.getY() + dy * ratio),
                        (float) (unit.getZ() + dz * ratio));
            }
            if (!needsChase(unit.getSceneUnitId())) {
                if (state.moving) {
                    moves.add(monsterMove(unit, unit.getX(), unit.getY(), unit.getZ(), 4));
                    state.moving = false;
                }
                continue;
            }
            // 落点略在范围内以避免浮点舍入卡在边界，不放大攻击距离。
            double ratio = 1d - (attackRange(unit) - 0.001d) / unit.distanceTo(player);
            state.moveX = (float) (unit.getX() + (player.getX() - unit.getX()) * ratio);
            state.moveY = (float) (unit.getY() + (player.getY() - unit.getY()) * ratio);
            state.moveZ = (float) (unit.getZ() + (player.getZ() - unit.getZ()) * ratio);
            state.moving = true;
            moves.add(monsterMove(unit, state.moveX, state.moveY, state.moveZ, 1));
        }
        return moves;
    }

    private static MoveResp monsterMove(CombatUnit unit, float x, float y, float z, int type) {
        if (type == 1) {
            float angle = (float) Math.toDegrees(Math.atan2(x - unit.getX(), z - unit.getZ()));
            unit.setDir(angle < 0f ? angle + 360f : angle);
        }
        return MoveResp.newBuilder().setMove(FightMoveVo.newBuilder()
                .setId(unit.getSceneUnitId()).setType(type).setAngle(unit.getDir())
                .setX(x).setY(y).setZ(z).setAngularSpeed(MONSTER_MOVE_SPEED)
                .setCurX(unit.getX()).setCurY(unit.getY()).setCurZ(unit.getZ())).build();
    }

    private long attackIntervalMillis() {
        switch (chapterId) {
            case 10100305:
            case 10100405:
            case 10200105:
            case 10200205:
            case 10200305:
            case 10200405:
            case 10200505:
                return BOSS_ATTACK_INTERVAL_MILLIS;
            default:
                return ATTACK_INTERVAL_MILLIS;
        }
    }

    private AttackDefinition attackDefinition(
            CombatUnit monster,
            int attackCount) {
        if (hasMonsterMovement() && chapterId >= 10200201) {
            double rage = RAGE_PER_ATTACK * (attackCount + 1);
            // MonsterSkillConfig 13 / 121002；第二次抓包 idx 5513/5554、2372/2403。
            // 沿用当前战斗伤害值，本次只纠正技能与飞行物命中协议，不重写数值公式。
            if (monster.getMonsterId() == 13) {
                long skillId = attackCount % 2 == 0 ? 220310110101L : 220310210101L;
                return impact(skillId, 2200101L, monster.getAttack(), rage, 1.11f, false);
            }
            if (monster.getMonsterId() == 121002) {
                long skillId = attackCount % 2 == 0 ? 40310310101L : 40310410101L;
                return new AttackDefinition(skillId, 5123601L, skillId,
                        DamageTypeVo.GENERAL_SKILL, 0, monster.getAttack(), rage,
                        1.24839f, true, false);
            }
            if (monster.getMonsterId() == 121001) {
                long skillId = MONSTER_NORMAL_ATTACK_SKILLS[attackCount % MONSTER_NORMAL_ATTACK_SKILLS.length];
                return normal(skillId, skillId, monster.getAttack(), rage, 1.17f);
            }
        }
        switch (chapterId) {
            case 10100305:
                return step(THIRD_BOSS_ATTACKS, attackCount);
            case 10100405:
                return step(FOURTH_BOSS_ATTACKS, attackCount);
            case 10200101:
            case 10200102:
            case 10200103:
                if (monster.getAttack() == 287.5) {
                    long skillId = MONSTER_NORMAL_ATTACK_SKILLS[
                            attackCount % MONSTER_NORMAL_ATTACK_SKILLS.length]
                            + 19200000000L;
                    return impact(
                            skillId,
                            2200101L,
                            26,
                            RAGE_PER_ATTACK * (attackCount + 1),
                            1.11f,
                            false);
                }
                long skillId = MONSTER_NORMAL_ATTACK_SKILLS[
                        attackCount % MONSTER_NORMAL_ATTACK_SKILLS.length];
                return normal(
                        skillId,
                        skillId,
                        25,
                        RAGE_PER_ATTACK * (attackCount + 1),
                        1.17f);
            case 10200105:
                return step(SIXTH_BOSS_ATTACKS, attackCount);
            case 10200205:
                return step(SEVENTH_BOSS_ATTACKS, attackCount);
            case 10200305:
                return step(EIGHTH_BOSS_ATTACKS, attackCount);
            case 10200405:
                return step(NINTH_BOSS_ATTACKS, attackCount);
            case 10200505:
                return step(TENTH_BOSS_ATTACKS, attackCount);
            default:
                long defaultSkillId = MONSTER_NORMAL_ATTACK_SKILLS[
                        attackCount % MONSTER_NORMAL_ATTACK_SKILLS.length];
                return normal(
                        defaultSkillId,
                        defaultSkillId,
                        monster.getAttack(),
                        RAGE_PER_ATTACK * (attackCount + 1),
                        1.0f);
        }
    }

    /**
     * 取第 {@code attackCount} 次攻击的定义。
     *
     * <p>抓包里的攻击序列只覆盖 Boss 被击杀前的那几招，不表示上限。官服 Boss 会
     * 一直攻击直到玩家或 Boss 死亡，因此序列播完后从第 0 个定义循环播放（技能、
     * 伤害、怒气、间隔整体循环），而不是返回 {@code null} 停止攻击。
     */
    private static AttackDefinition step(
            AttackDefinition[] definitions,
            int attackCount) {
        if (definitions.length == 0) {
            return null;
        }
        return definitions[attackCount % definitions.length];
    }

    private static AttackDefinition normal(
            long skillId,
            long actionId,
            double damage,
            double rage,
            float timeRatio) {
        return new AttackDefinition(
                skillId,
                actionId,
                skillId,
                DamageTypeVo.GENERAL_SKILL,
                0,
                damage,
                rage,
                timeRatio,
                false,
                false);
    }

    private static AttackDefinition impact(
            long skillId,
            long actionId,
            double damage,
            double rage,
            float timeRatio,
            boolean resetRage) {
        return new AttackDefinition(
                skillId,
                actionId,
                skillId,
                resetRage
                        ? DamageTypeVo.FIGHT_SKILL
                        : DamageTypeVo.GENERAL_SKILL,
                resetRage ? 0 : 128,
                damage,
                rage,
                timeRatio,
                true,
                resetRage);
    }

    /** 单只怪物的攻击节奏与技能轮换状态。 */
    private static final class MonsterState {

        private final CombatUnit unit;
        private long nextAttackAtMillis;
        private int attackCount;
        private boolean alerted;
        private boolean moving;
        private long lastMoveAtMillis;
        private float moveX;
        private float moveY;
        private float moveZ;

        private MonsterState(CombatUnit unit) {
            this.unit = unit;
        }

        private boolean isAttackReady(long nowMillis) {
            return nowMillis >= nextAttackAtMillis;
        }

        private MonsterAttack attack(
                CombatUnit target,
                long nowMillis,
                AttackDefinition definition,
                long intervalMillis,
                boolean protectPlayer) {
            attackCount++;
            nextAttackAtMillis = nowMillis + intervalMillis;

            // MainStageConfig 10200405.RoleBlood=1，抓包最后一击仍飘4336但血量为1。
            double damage = target.applyDamage(definition.damage, protectPlayer ? 1 : 0);
            if (protectPlayer) {
                damage = definition.damage;
            }

            return new MonsterAttack(
                    unit,
                    target,
                    definition.skillId,
                    definition.actionId,
                    definition.damageSkillId,
                    definition.damageType,
                    definition.special,
                    definition.delayedImpact,
                    definition.resetRage,
                    definition.timeRatio,
                    damage,
                    definition.rage);
        }
    }

    private static final class AttackDefinition {

        private final long skillId;
        private final long actionId;
        private final long damageSkillId;
        private final DamageTypeVo damageType;
        private final int special;
        private final double damage;
        private final double rage;
        private final float timeRatio;
        private final boolean delayedImpact;
        private final boolean resetRage;

        private AttackDefinition(
                long skillId,
                long actionId,
                long damageSkillId,
                DamageTypeVo damageType,
                int special,
                double damage,
                double rage,
                float timeRatio,
                boolean delayedImpact,
                boolean resetRage) {
            this.skillId = skillId;
            this.actionId = actionId;
            this.damageSkillId = damageSkillId;
            this.damageType = damageType;
            this.special = special;
            this.damage = damage;
            this.rage = rage;
            this.timeRatio = timeRatio;
            this.delayedImpact = delayedImpact;
            this.resetRage = resetRage;
        }
    }
}
