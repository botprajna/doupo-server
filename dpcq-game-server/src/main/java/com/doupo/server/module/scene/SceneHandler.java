package com.doupo.server.module.scene;

import com.doupo.protocol.AlchemyNewMakeCostItemNumUpdateResp;
import com.doupo.protocol.AlchemyNewMakeReq;
import com.doupo.protocol.AlchemyNewMakeResp;
import com.doupo.protocol.AlchemyNewMakeVo;
import com.doupo.protocol.AlchemyNewMergeResp;
import com.doupo.protocol.AlchemyNewMergeVo;
import com.doupo.protocol.AlchemyNewExpChangeResp;
import com.doupo.protocol.AlchemyNewLevelUpgradeResp;
import com.doupo.protocol.AlchemyNewTakingReq;
import com.doupo.protocol.AlchemyNewTakingResp;
import com.doupo.protocol.AnimationStateSkillSlotUpdateResp;
import com.doupo.protocol.AnimationStateSkillSlotUpdateVo;
import com.doupo.protocol.AnimationStateSkillSlotVo;
import com.doupo.protocol.BattlePassInfoResp;
import com.doupo.protocol.ChangeSceneFinishAReq;
import com.doupo.protocol.ChangeSceneFinishBReq;
import com.doupo.protocol.ChangeSceneReq;
import com.doupo.protocol.ChangeSceneResp;
import com.doupo.protocol.ChaseStartResp;
import com.doupo.protocol.ChaseStopResp;
import com.doupo.protocol.CrisisEventFreeRewardReq;
import com.doupo.protocol.CrisisEventFreeRewardResp;
import com.doupo.protocol.CrisisEventInfoResp;
import com.doupo.protocol.CrisisEventRewardReq;
import com.doupo.protocol.CrisisEventRewardResp;
import com.doupo.protocol.CrisisEventUpdateResp;
import com.doupo.protocol.CrisisEventVo;
import com.doupo.protocol.CrossHeroShortInfo;
import com.doupo.protocol.AttributeVO;
import com.doupo.protocol.AttributeActionVO;
import com.doupo.protocol.ActionVo;
import com.doupo.protocol.CurrencyItemVo;
import com.doupo.protocol.DamageTypeVo;
import com.doupo.protocol.DamageVO;
import com.doupo.protocol.DieVO;
import com.doupo.protocol.FightSkillUpdateResp;
import com.doupo.protocol.FightResultResp;
import com.doupo.protocol.FireTargetFindReq;
import com.doupo.protocol.FireTargetFindResp;
import com.doupo.protocol.FireTargetInfoResp;
import com.doupo.protocol.FireTargetRewardReq;
import com.doupo.protocol.FireTargetRewardResp;
import com.doupo.protocol.FireTargetRoleUnlockReq;
import com.doupo.protocol.FireTargetRoleUnlockResp;
import com.doupo.protocol.GuidanceKillMonsterReq;
import com.doupo.protocol.GuidanceMainMapResetReq;
import com.doupo.protocol.GuidanceMainMapStartFightReq;
import com.doupo.protocol.GuidanceMainMapUseSkillReq;
import com.doupo.protocol.GuidanceMainMapMonsterEnterReq;
import com.doupo.protocol.GuidanceMainMapPauseReq;
import com.doupo.protocol.GuidanceOpenAutoSkillReq;
import com.doupo.protocol.GuidanceUseSkillReq;
import com.doupo.protocol.HeroFightForceResp;
import com.doupo.protocol.HeroFightForceVo;
import com.doupo.protocol.HeroLevelBreakReq;
import com.doupo.protocol.HeroLevelBreakResp;
import com.doupo.protocol.HeroLevelUpgradeReq;
import com.doupo.protocol.HeroLevelUpgradeResp;
import com.doupo.protocol.HeroRoadRewardResp;
import com.doupo.protocol.HeroLevelVo;
import com.doupo.protocol.HeroShortInfoUpdateResp;
import com.doupo.protocol.HeroShortInfoVo;
import com.doupo.protocol.HeroOriginNodeStatReq;
import com.doupo.protocol.HeroOriginNodeStatResp;
import com.doupo.protocol.HeroOriginNodeStatVo;
import com.doupo.protocol.HeroUnlockIndexesResp;
import com.doupo.protocol.HeroUnlockReq;
import com.doupo.protocol.HeroUnlockResp;
import com.doupo.protocol.StatShowNodeVo;
import com.doupo.protocol.HeroSkinPart;
import com.doupo.protocol.HeroSkillActResp;
import com.doupo.protocol.HeroSkillActReq;
import com.doupo.protocol.HeroSkillBaseUpdateResp;
import com.doupo.protocol.MpResp;
import com.doupo.protocol.HeroSkillSchemaUpdateResp;
import com.doupo.protocol.HeroSkillSchemaUpdateReq;
import com.doupo.protocol.HeroSkillSchemaVo;
import com.doupo.protocol.HeroSkillSlotUnlockReq;
import com.doupo.protocol.HeroSkillSlotUnlockResp;
import com.doupo.protocol.HeroSkillSlotUpdateResp;
import com.doupo.protocol.HeroSkillSlotVo;
import com.doupo.protocol.HeroSkillElementUpdateResp;
import com.doupo.protocol.HeroSkillJobElementUpdateResp;
import com.doupo.protocol.HeroJobSkillElementVo;
import com.doupo.protocol.ActiveSkillChangeResp;
import com.doupo.protocol.BaoBuStartResp;
import com.doupo.protocol.FightSchemaHeroInfoVo;
import com.doupo.protocol.FightSchemaInfo;
import com.doupo.protocol.FightSchemaInfoResp;
import com.doupo.protocol.FightSchemaVo;
import com.doupo.protocol.LongAndIntegerPairEntry;
import com.doupo.protocol.HeroSkillSkillUpdateResp;
import com.doupo.protocol.HeroSkillStarUpReq;
import com.doupo.protocol.HeroSkillStarTotalMaxHisUpdateResp;
import com.doupo.protocol.AllHeroLevelInfoResp;
import com.doupo.protocol.HeroStepUpgradeReq;
import com.doupo.protocol.HeroStepUpgradeResp;
import com.doupo.protocol.RealmInfoReq;
import com.doupo.protocol.RealmInfoResp;
import com.doupo.protocol.HeroStatUpdateResp;
import com.doupo.protocol.HeroStatVo;
import com.doupo.protocol.MainEquipBagAddResp;
import com.doupo.protocol.MainEquipBagReduceResp;
import com.doupo.protocol.MainEquipDressReq;
import com.doupo.protocol.MainEquipHeroVO;
import com.doupo.protocol.MainEquipInfoResp;
import com.doupo.protocol.MainEquipRandomAttrVO;
import com.doupo.protocol.MainEquipPositionInfoResp;
import com.doupo.protocol.MainEquipSlotVO;
import com.doupo.protocol.MainEquipVO;
import com.doupo.protocol.MainMapChapterInfoResp;
import com.doupo.protocol.MainMapEndFightReq;
import com.doupo.protocol.MainMapKillMonsterReq;
import com.doupo.protocol.MainMapPassChapterUpdateResp;
import com.doupo.protocol.MainMapStartFightReq;
import com.doupo.protocol.MainMapStartFightResp;
import com.doupo.protocol.MainMapTakeHangUpRewardReq;
import com.doupo.protocol.MainMapTakeHangUpRewardResp;
import com.doupo.protocol.LotteryDrawReq;
import com.doupo.protocol.LotteryDrawResp;
import com.doupo.protocol.LotteryInfoResp;
import com.doupo.protocol.LotteryPoolUpReq;
import com.doupo.protocol.LotteryPoolUpResp;
import com.doupo.protocol.LotteryType;
import com.doupo.protocol.MedicineCauldronHistoryConsumeReq;
import com.doupo.protocol.MedicineCauldronHistoryConsumeResp;
import com.doupo.protocol.MedicineCauldronInfoResp;
import com.doupo.protocol.NewFightSkillLotteryVo;
import com.doupo.protocol.MonsterRewardVo;
import com.doupo.protocol.IntegerAndIntegerPairEntry;
import com.doupo.protocol.IntegerAndLongPairEntry;
import com.doupo.protocol.ModuleNewOpenResp;
import com.doupo.protocol.PackInfoResp;
import com.doupo.protocol.PackItemVo;
import com.doupo.protocol.PackUpdateResp;
import com.doupo.protocol.PackUpdateVo;
import com.doupo.protocol.PlayerCumulateLoginDaysResp;
import com.doupo.protocol.PlayerGuideSaveReq;
import com.doupo.protocol.PlayerFightForceCheckPointChangeResp;
import com.doupo.protocol.PlayerFightForceResp;
import com.doupo.protocol.PlayerHeroSkillVo;
import com.doupo.protocol.PlayerSkillVo;
import com.doupo.protocol.PurseUpdateResp;
import com.doupo.protocol.ReputationInitResp;
import com.doupo.protocol.ReputationLvOnTaskRewardUpdateResp;
import com.doupo.protocol.ReputationLvUpReq;
import com.doupo.protocol.ReputationLvUpResp;
import com.doupo.protocol.ReputationRangeVo;
import com.doupo.protocol.RewardItemVo;
import com.doupo.protocol.RewardResp;
import com.doupo.protocol.RealFirstChargeDayRewardVo;
import com.doupo.protocol.RealFirstChargeInfoResp;
import com.doupo.protocol.RealFirstChargeUpdateResp;
import com.doupo.protocol.SceneCommonSkillVO;
import com.doupo.protocol.SceneForgetVisibleResp;
import com.doupo.protocol.SceneFightUnitInfoVo;
import com.doupo.protocol.SceneHeroVo;
import com.doupo.protocol.SceneMonsterVo;
import com.doupo.protocol.SceneUnitBaseInfoVo;
import com.doupo.protocol.SceneUnitVo;
import com.doupo.protocol.SceneUpdateVisibleResp;
import com.doupo.protocol.ServerVoUpdateType;
import com.doupo.protocol.SkillContainerVO;
import com.doupo.protocol.SkillActionResp;
import com.doupo.protocol.SkillVo;
import com.doupo.protocol.StatVo;
import com.doupo.protocol.StoryReadFinishReq;
import com.doupo.protocol.StoryReadFinishResp;
import com.doupo.protocol.StringAndDoublePairEntry;
import com.doupo.protocol.StrongStatueInfoResp;
import com.doupo.protocol.SyncNonSceneHeroVoUpdateResp;
import com.doupo.protocol.TaskPhase;
import com.doupo.protocol.TaskRewardReq;
import com.doupo.protocol.TaskUniqueKey;
import com.doupo.protocol.TaskUpdateResp;
import com.doupo.protocol.TaskVo;
import com.doupo.protocol.UpdateItem;
import com.doupo.protocol.UseSkillResp;
import com.doupo.protocol.VectorVo;
import com.doupo.protocol.WarningResp;
import com.doupo.server.foundation.GameServerClock;
import com.doupo.server.module.combat.CombatScheduler;
import com.doupo.server.module.combat.CombatSession;
import com.doupo.server.module.combat.CombatSessionFactory;
import com.doupo.server.module.combat.CombatSessionRegistry;
import com.doupo.server.module.combat.CombatUnit;
import com.google.protobuf.ByteString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SceneHandler {

    private final ConcurrentMap<Long, Chapter17Progress> laterProgress = new ConcurrentHashMap<>();

    private Chapter17Progress later(long playerId) {
        return laterProgress.computeIfAbsent(playerId, ignored -> new Chapter17Progress());
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(SceneHandler.class);

    /** 开服日，未注入时按第 1 天（单测 / 新服）。 */
    private int serverOpenDay = 1;
    private GameServerClock serverClock = GameServerClock.dayOne();
    /** 已领过 200012、首充模块已对客户端打开。 */
    private final ConcurrentMap<Long, Boolean> firstChargeUnlocked =
            new ConcurrentHashMap<Long, Boolean>();
    /** 已下发给客户端的最高首充档 chargeId。 */
    private final ConcurrentMap<Long, Integer> firstChargePushedMax =
            new ConcurrentHashMap<Long, Integer>();

    private final ConcurrentMap<Long, GuidanceState> guidanceStates = new ConcurrentHashMap<>();
    /** 玩家当前正在打的那一波怪物实例。 */
    private final ConcurrentMap<Long, WaveInstance> currentWaves =
            new ConcurrentHashMap<>();
    /** 已按抓包 idx 1974 预加载、玩家还没走进去的下一波实例。 */
    private final ConcurrentMap<Long, WaveInstance> preloadedWaves =
            new ConcurrentHashMap<>();
    /** 怪物场景单位 ID 的号段游标，见 {@link #allocateWaveUnitIdBase}。 */
    private final ConcurrentMap<Long, Integer> waveUnitIdSlots =
            new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Integer> alchemyMakeTimes =
            new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Integer> newFightSkillLotteryDrawTimes =
            new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Integer> newFightSkillLotteryStage =
            new ConcurrentHashMap<>();
    /** 纳戒能量（100200）当前值，内存模拟；奖励 +N、抽取一次扣所需。 */
    private final ConcurrentMap<Long, Integer> newFightSkillEnergy =
            new ConcurrentHashMap<>();
    /** 主线奖励的 10060 堆叠数量（抓包 idx 9134 起 itemIndex=26）。 */
    private final ConcurrentMap<Long, Integer> item10060Counts =
            new ConcurrentHashMap<>();
    /** 待领取的首次挂机斗铠（200029 后 2 件，对应抓包 idx 9064/9096）。 */
    private final ConcurrentMap<Long, List<MainEquipVO>> pendingHangUpEquips =
            new ConcurrentHashMap<>();
    /** 已领取斗铠与已穿戴槽位；与其余引导状态一样由玩家 Actor 串行修改。 */
    private final ConcurrentMap<Long, MainEquipState> mainEquipStates =
            new ConcurrentHashMap<>();
    /** 斗师声望当前等级，开名望后为 1。 */
    private final ConcurrentMap<Long, Integer> reputationLevels =
            new ConcurrentHashMap<>();
    /** 当前等待一次纳戒抽取完成的主线任务（200017/200053/200023）。 */
    private final ConcurrentMap<Long, Integer> pendingLotteryMainTasks =
            new ConcurrentHashMap<>();
    /** 已接取、等待真实事件完成的主线任务。 */
    private final ConcurrentMap<Long, Integer> acceptedMainTasks =
            new ConcurrentHashMap<>();
    /** 已通关的主线章节（用于 mainChapterPass AutoFinish）。 */
    private final ConcurrentMap<Long, Set<Integer>> passedMainChapters =
            new ConcurrentHashMap<>();
    /** 已领取危机事件 20010 奖励的玩家。 */
    private final Set<Long> crisisEvent20010Rewarded =
            ConcurrentHashMap.newKeySet();
    private final Set<Long> secondHeroChapterTasksSent =
            ConcurrentHashMap.newKeySet();
    /** 当前纳戒能量堆叠所在背包槽；扣到 0 后删除，下次获得时使用新槽。 */
    private final ConcurrentMap<Long, Integer> newFightSkillEnergyItemIndexes =
            new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Long> newFightSkillEnergyCreateTimes =
            new ConcurrentHashMap<>();
    /** 家族测试每日免费奖励的已领取天数。 */
    private final ConcurrentMap<Long, Set<Integer>> crisisEventFreeRewardDays =
            new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Long> crisisEventGold =
            new ConcurrentHashMap<>();

    /** 纳戒单抽消耗（lotterytypeconfig LotteryType=12 Consume=100200×10）。 */
    private static final int NEW_FIGHT_SKILL_DRAW_COST = 10;
    /** 纳戒十连消耗（lotterytypeconfig LotteryType=12 TenConsume=100200×100）。 */
    private static final int NEW_FIGHT_SKILL_TEN_COST = 100;
    /** crisisfreerewardconfig：开服第 1~14 天均奖励 currency 2 ×10。 */
    private static final int CRISIS_EVENT_FREE_REWARD_MIN_DAY = 1;
    private static final int CRISIS_EVENT_FREE_REWARD_MAX_DAY = 14;
    private static final long CRISIS_EVENT_FREE_REWARD_GOLD = 10;
    /** 官服抓包中解锁家族测试时的 currency 2 余额。 */
    private static final long CRISIS_EVENT_UNLOCK_GOLD = 25;
    /** 纳戒升级后抽卡池（抓包 idx 6086/6462/6944/7341/7444/8733）。 */
    private static final int[] NEW_FIGHT_SKILL_POOL = {
            80130010, 80140000, 80130100, 80128010,
            80128000, 80130000, 100053, 201010
    };
    /** PackUpdateResp.operationType = HERO_SKILL_ACT_COST。 */
    private static final int HERO_SKILL_ACT_COST = 75004;
    /** 薰儿解锁节点，FireTargetNodeConfig Id=22 / Param=1。 */
    private static final int FIRE_TARGET_XUNER_NODE = 22;
    private static final int FIRE_TARGET_FAMILY_NODE = 1;
    private static final int SECOND_HERO_INDEX = 1;
    private final ConcurrentMap<Long, Long> alchemyExpPools =
            new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Integer> alchemyTakingCounts =
            new ConcurrentHashMap<>();
    /** 灵液库存、产物与分类计数；由玩家 Actor 串行访问。 */
    private final ConcurrentMap<Long, AlchemyState> alchemyStates =
            new ConcurrentHashMap<>();

    private static final class AlchemyState {
        private final Map<Integer, UpdateItem> materials = new HashMap<>();
        private final Map<Long, AlchemyNewMakeVo> products = new HashMap<>();
        private final Map<Long, Integer> productKinds = new HashMap<>();
        private final Map<Integer, Long> dailyCosts = new java.util.TreeMap<>();
        private final Map<Integer, Integer> takingCounts = new java.util.TreeMap<>();
        private java.time.LocalDate costDay;
    }
    /** 下一个丹药 id（官服连续 0..22）。 */
    private final ConcurrentMap<Long, Integer> nextAlchemyId =
            new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Integer> alchemyLevels =
            new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Integer> heroLevels =
            new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Integer> heroStages =
            new ConcurrentHashMap<>();
    /** 已激活斗技当前星级。 */
    private final ConcurrentMap<Long, ConcurrentMap<Integer, Integer>> heroSkillStars =
            new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Integer> skillStarMaxHis =
            new ConcurrentHashMap<>();
    /** 刚激活、客户端立刻拿同一张碎片去升星时的一次抵扣。 */
    private final ConcurrentMap<Long, Set<Integer>> pendingSkillStarCredits =
            new ConcurrentHashMap<>();
    private final Set<Long> sixthRealmTaskPlayers =
            ConcurrentHashMap.newKeySet();
    /** 主线 200012（到达魔兽山脉第6关）本局已推送 FINISHED。 */
    private final Set<Long> finishedArriveChapter6 =
            ConcurrentHashMap.newKeySet();
    /** 已解锁本关 Boss 的玩家 → 循环第一波章节 ID（第三波打完置位，进 Boss 时清除）。 */
    private final ConcurrentMap<Long, Integer> bossUnlockedWave =
            new ConcurrentHashMap<>();
    /** 纳戒抽卡已激活的技能 baseId（抽到 80 开头道具时激活道具+1 技能，去重）。 */
    private final ConcurrentMap<Long, Set<Long>> activatedNewFightSkills =
            new ConcurrentHashMap<>();
    /** 已学会的斗技（含任务/危机事件激活），抽到对应道具时不再重复激活。 */
    private final ConcurrentMap<Long, Set<Long>> learnedNewFightSkills =
            new ConcurrentHashMap<>();
    /** 纳戒道具堆叠：playerId → itemIndex → stack。 */
    private final ConcurrentMap<Long, ConcurrentMap<Integer, LotteryItemStack>>
            lotteryItemStacks = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Integer> lotteryNextItemIndex =
            new ConcurrentHashMap<>();
    /** 当前装配方案，替换时回显抓包 idx 1963/5859 的 schema。 */
    private final ConcurrentMap<Long, SkillSchemaState> heroSkillSchemas =
            new ConcurrentHashMap<>();
    private final Set<Long> secondHeroUnlocked =
            ConcurrentHashMap.newKeySet();
    private static final Set<Long> THIRD_SKILL_STAR_PLAYERS =
            ConcurrentHashMap.newKeySet();
    private static final Set<Long> FIRST_SKILL_STAR_PLAYERS =
            ConcurrentHashMap.newKeySet();
    /**
     * 方案里已装配焰分噬浪尺 80128001（第 9 关给的技能，可装进任意已解锁血脉槽）。
     * 八段解锁的血脉槽 3（3001）本身是空槽，装什么由玩家选。
     */
    private static final Set<Long> YANFEN_SLOT_PLAYERS =
            ConcurrentHashMap.newKeySet();
    private static final int YANFEN_SKILL_BASE_ID = 80128001;
    private static final long YANFEN_FIGHT_SKILL_ID = 10150310100L;
    private static final long SECOND_FIGHT_SKILL_ID = 10150210100L;
    private static final int SECOND_SKILL_BASE_ID = 80130001;
    private static final int CLAW_SKILL_BASE_ID = 80128011;
    private static final long CLAW_FIGHT_SKILL_ID = 10150410100L;
    private static final int SKILL_ELEMENT_FIRE = 2;
    /**
     * 突破/斗技/家族测试给出的攻击与满血。
     *
     * <p>每关的基线值写在 {@link #playerAttack} 和 {@link #restoreHp} 里，
     * 但玩家在关卡中途拿到的加成不能被下一波进场时的基线值抹掉——官服抓包
     * idx 5542 五段突破后攻击从 1189.077 涨到 1433.883，第七关后面几波进场
     * 下发的都是涨过的值。
     */
    private final ConcurrentMap<Long, Double> grantedAttack =
            new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Double> grantedMaxHp =
            new ConcurrentHashMap<>();
    /** 最近一次下发的玩家战力，供 50459 回包。登录抓包 idx 93 为 10599。 */
    private final ConcurrentMap<Long, Double> playerFightForce =
            new ConcurrentHashMap<>();
    private static final int STAT_SHOW_NODE_NEW_EQUIP = 3;
    private static final double DEFAULT_HERO_POWER = 10599;
    /** 血脉槽3。NewSkillLocationConfig BaseId=3001，ConditionHeroLevel=6（八段）。 */
    private static final int THIRD_SKILL_SLOT_ID = 3001;
    private static final int THIRD_SKILL_SLOT_UNLOCK_LEVEL = 6;
    /** HUD 包裹 BagView。moduleopenconfig 3101，UNLOCK_HERO_LEVEL Level=6。 */
    private static final int BAG_MODULE_ID = 3101;
    /** 背包合成页。moduleopenconfig 3102，与 3101 同时开。 */
    private static final int BAG_COMPOSE_MODULE_ID = 3102;
    /** 问卷调查。moduleopenconfig 3701，八段同开。 */
    private static final int QUESTIONNAIRE_MODULE_ID = 3701;
    /** 飞升。moduleopenconfig 4203，九段才开。 */
    private static final int ASCEND_MODULE_ID = 4203;
    /** 万兽鼎。moduleopenconfig Id=1622，TASK_REWARD 200026。 */
    private static final int MEDICINE_CAULDRON_MODULE_ID = 1622;
    /** 名望系统。moduleopenconfig Id=10，TASK_REWARD 200029，ViewId=ReputationView。 */
    private static final int REPUTATION_MODULE_ID = 10;
    /** 挂机奖励。moduleopenconfig Id=23011，TASK_REWARD 200029。 */
    private static final int HANG_UP_REWARD_MODULE_ID = 23011;
    /** 斗铠。moduleopenconfig Id=4308，TASK_REWARD 200029。 */
    private static final int MAIN_EQUIP_MODULE_ID = 4308;
    /** 主线 200030/200033/200034/200035 奖励 10060 的背包槽。 */
    private static final int ITEM_10060_INDEX = 26;
    /** 挂机领取 RewardResp.operationType，官服 MAIN_STAGE_PASS_HANGUP_REWARD。 */
    private static final int HANG_UP_REWARD_OPERATION = 61952;
    /** 声望升级 PackUpdate.operationType。 */
    private static final int REPUTATION_LV_UP_OPERATION = 53701;
    /** 斗师 1 级升 2 级消耗 10060×700，见 reputationconfig Id=1001。 */
    private static final int DOUSHI_LV1_UPGRADE_COST = 700;
    /** 已用 75015/75003 解锁的血脉槽，避免重复推空槽。 */
    private final ConcurrentMap<Long, Set<Integer>> unlockedHeroSkillSlots =
            new ConcurrentHashMap<>();
    /** 已下发 3101 背包模块，避免 50852 重复推。 */
    private final Set<Long> bagModulesOpened = ConcurrentHashMap.newKeySet();

    /** 当前本地客户端初始化回到第一关，相关临时状态必须同步回到初始值。 */
    @org.springframework.beans.factory.annotation.Value("${game.test.chapter9-start:false}")
    private boolean chapter9TestEnabled;
    private final Set<Long> chapter9TestPlayers = ConcurrentHashMap.newKeySet();

    public void resetTutorialPlayer(long playerId) {
        chapter9TestPlayers.remove(playerId);
        CombatSessionRegistry.clear(playerId);
        guidanceStates.remove(playerId);
        currentWaves.remove(playerId);
        preloadedWaves.remove(playerId);
        waveUnitIdSlots.remove(playerId);
        alchemyMakeTimes.remove(playerId);
        alchemyExpPools.remove(playerId);
        alchemyTakingCounts.remove(playerId);
        alchemyStates.remove(playerId);
        nextAlchemyId.remove(playerId);
        alchemyLevels.remove(playerId);
        heroLevels.remove(playerId);
        heroStages.remove(playerId);
        heroSkillStars.remove(playerId);
        skillStarMaxHis.remove(playerId);
        pendingSkillStarCredits.remove(playerId);
        newFightSkillEnergy.remove(playerId);
        newFightSkillEnergyItemIndexes.remove(playerId);
        newFightSkillEnergyCreateTimes.remove(playerId);
        newFightSkillLotteryDrawTimes.remove(playerId);
        newFightSkillLotteryStage.remove(playerId);
        activatedNewFightSkills.remove(playerId);
        learnedNewFightSkills.remove(playerId);
        lotteryItemStacks.remove(playerId);
        lotteryNextItemIndex.remove(playerId);
        heroSkillSchemas.remove(playerId);
        secondHeroUnlocked.remove(playerId);
        pendingLotteryMainTasks.remove(playerId);
        item10060Counts.remove(playerId);
        pendingHangUpEquips.remove(playerId);
        mainEquipStates.remove(playerId);
        reputationLevels.remove(playerId);
        laterProgress.remove(playerId);
        acceptedMainTasks.remove(playerId);
        passedMainChapters.remove(playerId);
        sixthRealmTaskPlayers.remove(playerId);
        finishedArriveChapter6.remove(playerId);
        bossUnlockedWave.remove(playerId);
        crisisEvent20010Rewarded.remove(playerId);
        secondHeroChapterTasksSent.remove(playerId);
        crisisEventFreeRewardDays.remove(playerId);
        crisisEventGold.remove(playerId);
        firstChargeUnlocked.remove(playerId);
        firstChargePushedMax.remove(playerId);
        THIRD_SKILL_STAR_PLAYERS.remove(playerId);
        FIRST_SKILL_STAR_PLAYERS.remove(playerId);
        YANFEN_SLOT_PLAYERS.remove(playerId);
        grantedAttack.remove(playerId);
        grantedMaxHp.remove(playerId);
        playerFightForce.remove(playerId);
        unlockedHeroSkillSlots.remove(playerId);
        bagModulesOpened.remove(playerId);
    }

    @Autowired(required = false)
    void setGameServerClock(GameServerClock clock) {
        if (clock != null) {
            this.serverClock = clock;
            this.serverOpenDay = clock.openDay();
        }
    }

    /** 单测切换开服日：1=只有 6 元首充，≥2=补 30/98。 */
    void setServerOpenDay(int openDay) {
        this.serverOpenDay = Math.max(1, openDay);
    }

    /** 记下一次属性提升，之后进场下发的攻击与满血不会再退回关卡基线。 */
    private void grantPlayerStats(
            long playerId,
            double attack,
            double maxHp) {
        grantedAttack.merge(playerId, attack, Math::max);
        grantedMaxHp.merge(playerId, maxHp, Math::max);
    }

    private Set<Long> activatedSkills(long playerId) {
        return activatedNewFightSkills.computeIfAbsent(
                playerId, ignored -> ConcurrentHashMap.newKeySet());
    }

    private Set<Long> learnedSkills(long playerId) {
        return learnedNewFightSkills.computeIfAbsent(
                playerId, ignored -> ConcurrentHashMap.newKeySet());
    }

    private ConcurrentMap<Integer, LotteryItemStack> lotteryStacks(long playerId) {
        return lotteryItemStacks.computeIfAbsent(
                playerId, ignored -> new ConcurrentHashMap<>());
    }

    private Set<Integer> pendingStarCredits(long playerId) {
        return pendingSkillStarCredits.computeIfAbsent(
                playerId, ignored -> ConcurrentHashMap.newKeySet());
    }

    private int skillStarOf(long playerId, int skillId) {
        ConcurrentMap<Integer, Integer> stars = heroSkillStars.get(playerId);
        if (stars == null) {
            return 1;
        }
        return stars.getOrDefault(skillId, 1);
    }

    private void noteLearnedSkill(
            long playerId, int skillId, int star, int maxHis) {
        learnedSkills(playerId).add((long) skillId);
        heroSkillStars
                .computeIfAbsent(playerId, ignored -> new ConcurrentHashMap<>())
                .put(skillId, star);
        skillStarMaxHis.merge(playerId, maxHis, Math::max);
    }

    /**
     * 境界任务按 {@code playerlevelconfig} 同步：八段=200021，一星斗者=200025。
     * 七段 200013 仍由五段突破包 {@link #writeFifthLevelBreakUpdates} 下发。
     */
    private void syncHeroLevelTasks(IPlayerContext context) {
        int level = heroLevels.getOrDefault(context.getId(), 2);
        if (level >= 6) {
            tryFinishAcceptedMainTask(context, 200021, 1);
            tryOpenEighthRealmBagModules(context);
        }
        if (level >= 8) {
            tryFinishAcceptedMainTask(context, 200025, 1);
        }
        if (level >= 9) {
            tryFinishAcceptedMainTask(context, 200028, 1);
        }
        refreshChapter17Tasks(context);
    }

    /** Explicit, reversible test start; called once after reset and before PlayerInitEnd. */
    public boolean initializeChapter9Test(IPlayerContext context) {
        if (!chapter9TestEnabled) return false;
        long id = context.getId();
        if (!chapter9TestPlayers.add(id)) return true;
        long now = System.currentTimeMillis();
        heroLevels.put(id, Chapter9TestCheckpoint.LEVEL);
        heroStages.put(id, 1);
        playerFightForce.put(id, (double) Chapter9TestCheckpoint.POWER);
        grantPlayerStats(id, Chapter9TestCheckpoint.ATTACK, Chapter9TestCheckpoint.HP);
        FIRST_SKILL_STAR_PLAYERS.add(id);
        THIRD_SKILL_STAR_PLAYERS.add(id);
        finishedArriveChapter6.add(id);
        sixthRealmTaskPlayers.add(id);
        crisisEvent20010Rewarded.add(id);
        for (int boss : Chapter9TestCheckpoint.PASSED_BOSSES) markChapterPassed(id, boss);
        guidanceStates.put(id, new GuidanceState(Chapter9TestCheckpoint.CHAPTER));

        alchemyExpPools.put(id, 10L); // idx 6710, after the level-7 upgrade
        alchemyLevels.put(id, 1);
        alchemyMakeTimes.put(id, 4);
        alchemyTakingCounts.put(id, 16);
        nextAlchemyId.put(id, 17);
        AlchemyState alchemy = new AlchemyState();
        alchemy.costDay = alchemyNow().atZone(java.time.ZoneId.of("Asia/Shanghai")).toLocalDate();
        alchemy.dailyCosts.put(101, 4L);
        alchemy.takingCounts.put(1, 1);
        alchemy.takingCounts.put(1101, 16);
        alchemyStates.put(id, alchemy);
        context.write(75060, AlchemyNewExpChangeResp.newBuilder().setExp(10).setLevel(1).build(), 0);
        writeAlchemyCosts(context, alchemy);
        writeAllHeroLevelInfo(context);

        ModuleNewOpenResp.Builder modules = ModuleNewOpenResp.newBuilder();
        for (int module : Chapter9TestCheckpoint.MODULES) modules.addOpens(module);
        context.write(50852, modules.build(), 0);
        HeroSkillSkillUpdateResp.Builder learned = HeroSkillSkillUpdateResp.newBuilder();
        for (int skill : Chapter9TestCheckpoint.SKILLS) {
            activatedSkills(id).add((long) skill);
            noteLearnedSkill(id, skill, 2, 12);
            learned.addPlayerSkillUpdates(PlayerSkillVo.newBuilder().setBaseId(skill)
                    .addHeroSkills(PlayerHeroSkillVo.newBuilder().setHeroIdx(-1)
                            .setHeroSkill(SkillVo.newBuilder().setBaseId(skill).setStar(2))));
        }
        context.write(75046, learned.build(), 0);
        persistSkillSchema(id, HeroSkillSchemaUpdateReq.newBuilder().setHeroIndex(0)
                .addSkills(intPair(2001, 80128011)).addSkills(intPair(1001, 100001)).build());
        HeroSkillSchemaVo.Builder schema = HeroSkillSchemaVo.newBuilder().setId(1)
                .setActiveTime(schemaActiveTime(id)).setActive(true)
                .addAllSlot2SkillBaseIds(heroSkillSchemas.get(id).slots);
        context.write(75001, com.doupo.protocol.HeroSkillInfoResp.newBuilder()
                .addHeroVoList(com.doupo.protocol.HeroSkillVo.newBuilder().setHeroIndex(0)
                        .setCurSchemaId(1).setSkillStarTotalMaxHis(12)
                        .addSlots(HeroSkillSlotVo.newBuilder().setId(1001).setAnyOnSkill(true))
                        .addSlots(HeroSkillSlotVo.newBuilder().setId(2001).setAnyOnSkill(true))
                        .addSlots(HeroSkillSlotVo.newBuilder().setId(3001))
                        .addSchemas(schema)
                        .addSchemas(HeroSkillSchemaVo.newBuilder().setId(2).setActiveTime(now / 1000 + 2).setActive(true))
                        .addSchemas(HeroSkillSchemaVo.newBuilder().setId(3))).build(), 0);
        unlockedHeroSkillSlots.computeIfAbsent(id, ignored -> ConcurrentHashMap.newKeySet()).add(3001);
        context.write(52351, PlayerFightForceResp.newBuilder().setPlayerFightForce(Chapter9TestCheckpoint.POWER).build(), 0);
        context.write(75151, HeroFightForceResp.newBuilder().addHeroVoList(HeroFightForceVo.newBuilder()
                .setHeroIndex(0).setFightForce(Chapter9TestCheckpoint.POWER)).build(), 0);
        context.write(50455, HeroStatUpdateResp.newBuilder().setHeroVo(HeroStatVo.newBuilder()
                .setHeroIndex(0).addStats(stat(101001, Chapter9TestCheckpoint.ATTACK))
                .addStats(stat(102001, Chapter9TestCheckpoint.DEFENSE))
                .addStats(stat(103001, Chapter9TestCheckpoint.HP))
                .addStats(stat(107002, 64)).addStats(stat(104001, 100))
                .addStats(stat(105001, 1)).addStats(stat(106001, 1))).build(), 0);

        PackUpdateVo.Builder items = PackUpdateVo.newBuilder().setPackType(1)
                .addUpdateItems(changeNewFightSkillEnergy(context, 29, 11, now)); // idx 7324
        for (int[] item : Chapter9TestCheckpoint.ITEMS) {
            LotteryItemStack stack = new LotteryItemStack(item[0], item[1], item[2], id * 1000 + item[0], now);
            lotteryStacks(id).put(item[0], stack);
            items.addUpdateItems(lotteryUpdateItem(stack));
        }
        lotteryNextItemIndex.put(id, 20);
        context.write(50401, PackInfoResp.newBuilder().addPacks(items).build(), 0);
        newFightSkillLotteryDrawTimes.put(id, 12);
        newFightSkillLotteryStage.put(id, 2);
        context.write(77354, buildUpgradedLotteryInfo(12, 2).toBuilder()
                .setNewFightSkillLotteryVo(NewFightSkillLotteryVo.newBuilder().setShowStage(2)
                        .addShowStage2DrawCountInfoList(intPair(1, 5))
                        .addShowStage2DrawCountInfoList(intPair(2, 7))).build(), 0);
        acceptNextMainTask(context, 200023); // idx 7328, ninth-boss task follows the draw
        context.write(61951, MainMapChapterInfoResp.newBuilder()
                .setMainMapChapterId(Chapter9TestCheckpoint.CHAPTER).setHasReward(true)
                .setStageTime(12).setLastStageTime(10).setLoseBackId(10200301)
                .setHistoryTopId(10200305).build(), 0);
        com.doupo.protocol.PlayerGuideSaveResp.Builder guides = com.doupo.protocol.PlayerGuideSaveResp.newBuilder();
        for (int[] guide : Chapter9TestCheckpoint.GUIDES) guides.addGuideGroupAndIdPairList(intPair(guide[0], guide[1]));
        context.write(50367, guides.build(), 0);
        LOGGER.warn("Chapter 9 TEST checkpoint initialized: player={}, chapter={}, level={}, hp={}, attack={}",
                id, Chapter9TestCheckpoint.CHAPTER, Chapter9TestCheckpoint.LEVEL,
                Chapter9TestCheckpoint.HP, Chapter9TestCheckpoint.ATTACK);
        return true;
    }

    private static IntegerAndIntegerPairEntry intPair(int key, int value) {
        return IntegerAndIntegerPairEntry.newBuilder().setKey(key).setValue(value).build();
    }

    public static ChangeSceneResp initialScene() {
        return buildSceneResponse(2, 1);
    }

    /** 家族测试右下角每日免费宝箱。 */
    @PlayerCmd
    public void claimCrisisEventFreeReward(
            IPlayerContext context,
            CrisisEventFreeRewardReq request) {
        int openDay = request.getOpenDay();
        if (openDay < CRISIS_EVENT_FREE_REWARD_MIN_DAY
                || openDay > CRISIS_EVENT_FREE_REWARD_MAX_DAY) {
            LOGGER.warn("Reject crisis event free reward: playerId={}, openDay={}",
                    context.getId(), openDay);
            return;
        }

        Set<Integer> claimedDays = crisisEventFreeRewardDays.computeIfAbsent(
                context.getId(), ignored -> ConcurrentHashMap.newKeySet());
        if (claimedDays.add(openDay)) {
            long gold = crisisEventGold.merge(
                    context.getId(),
                    CRISIS_EVENT_UNLOCK_GOLD + CRISIS_EVENT_FREE_REWARD_GOLD,
                    (current, ignored) -> current + CRISIS_EVENT_FREE_REWARD_GOLD);
            context.write(
                    50651,
                    PurseUpdateResp.newBuilder()
                            .addItems(CurrencyItemVo.newBuilder()
                                    .setType(2)
                                    .setValue(gold))
                            .build(),
                    0);
            context.write(
                    50406,
                    RewardResp.newBuilder()
                            .setOperationType(76702)
                            .addRewardItemVos(RewardItemVo.newBuilder()
                                    .setItemKey(2)
                                    .setAmount(CRISIS_EVENT_FREE_REWARD_GOLD)
                                    .setGiveFlag(false))
                            .build(),
                    0);
            LOGGER.info("Grant crisis event free reward: playerId={}, openDay={}, gold={}",
                    context.getId(), openDay, gold);
        } else {
            LOGGER.info("Ignore duplicate crisis event free reward: playerId={}, openDay={}",
                    context.getId(), openDay);
        }

        context.write(
                76706,
                CrisisEventFreeRewardResp.newBuilder()
                        .setOpenDay(openDay)
                        .build(),
                0);
    }

    /**
     * 剧情读完回包，对应抓包 idx 5765/5768。
     *
     * <p>客户端播完危机剧情 20010 后会先发 51156；不回 51157 会一直锁在剧情里，
     * 人停住、技能特效也不播。
     */
    @PlayerCmd
    public void finishStoryRead(
            IPlayerContext context,
            StoryReadFinishReq request) {
        context.write(
                51157,
                StoryReadFinishResp.newBuilder()
                        .setFinishStoryId(request.getFinishStoryId())
                        .build(),
                0);
        LOGGER.info("Story read finished: player={}, story={}",
                context.getId(), request.getFinishStoryId());
    }

    /** 家族试炼危机事件领奖，对应抓包 idx 5767-5778，eventId=20010。 */
    @PlayerCmd
    public void claimCrisisEventReward(
            IPlayerContext context,
            CrisisEventRewardReq request) {
        if (request.getEventId() != 20010
                || !crisisEvent20010Rewarded.add(context.getId())) {
            return;
        }

        long now = System.currentTimeMillis();
        int itemIndex = 14;
        context.write(
                51157,
                StoryReadFinishResp.newBuilder()
                        .setFinishStoryId(20010)
                        .build(),
                0);
        context.write(
                50402,
                packUpdate(
                        76701,
                        UpdateItem.newBuilder()
                                .setItemIndex(itemIndex)
                                .setPackItem(PackItemVo.newBuilder()
                                        .setObjectId(
                                                context.getId() * 1000 + itemIndex)
                                        .setKey(90000999)
                                        .setSize(1)
                                        .setCreateTime(now)
                                        .setLastGainTime(now))
                                .build()),
                0);
        context.write(
                50406,
                RewardResp.newBuilder()
                        .setOperationType(76701)
                        .addRewardItemVos(RewardItemVo.newBuilder()
                                .setItemKey(90000999)
                                .setAmount(1)
                                .setGiveFlag(false))
                        .build(),
                0);
        context.write(
                76704,
                CrisisEventRewardResp.newBuilder()
                        .setEventId(20010)
                        .build(),
                0);
        context.write(
                50402,
                packUpdate(
                        75004,
                        UpdateItem.newBuilder()
                                .setItemIndex(itemIndex)
                                .build()),
                0);
        SkillVo skill = SkillVo.newBuilder()
                .setBaseId(100001)
                .setStar(2)
                .setActStar(0)
                .setAwaken(false)
                .setSelElement(0)
                .build();
        context.write(
                75011,
                HeroSkillActResp.newBuilder()
                        .setHeroIndex(0)
                        .setShowTips(true)
                        .setPlayerSkill(PlayerSkillVo.newBuilder()
                                .setBaseId(100001)
                                .addHeroSkills(PlayerHeroSkillVo.newBuilder()
                                        .setHeroIdx(-1)
                                        .setHeroSkill(skill)))
                        .build(),
                0);
        context.write(
                75049,
                HeroSkillStarTotalMaxHisUpdateResp.newBuilder()
                        .setHeroIndex(0)
                        .setSkillStarTotalMaxHis(9)
                        .build(),
                0);
        context.write(
                75039,
                HeroSkillBaseUpdateResp.newBuilder()
                        .addBaseIds(100001)
                        .build(),
                0);
        context.write(
                76702,
                CrisisEventUpdateResp.newBuilder()
                        .addUpdates(CrisisEventVo.newBuilder()
                                .setId(20018)
                                .setStartMillis(now)
                                .setReward(false))
                        .build(),
                0);
        noteLearnedSkill(context.getId(), 100001, 2, 9);
        noteQualitySkill(context, 100001);
        tryFinishAcceptedMainTask(context, 200014, 1);
        writeSecondHeroChapterTasks(context);
    }

    /**
     * 苍穹绘卷 Typeset=2 章节页会按任务 Vo 刷列表。缺 30201/30202 时
     * CrisisEventChapterTaskItem 空指针，界面看起来像点不开。
     * 官服创角 TaskUpdate 就带这些危机事件任务。
     */
    public void writeSecondHeroChapterTasks(IPlayerContext context) {
        if (!secondHeroChapterTasksSent.add(context.getId())) {
            return;
        }
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(30201, TaskPhase.PROGRESS, 0))
                        .addTaskVos(task(30202, TaskPhase.PROGRESS, 0))
                        .addTaskVos(task(30301, TaskPhase.PROGRESS, 0))
                        .addTaskVos(task(30302, TaskPhase.PROGRESS, 1))
                        .addTaskVos(task(30401, TaskPhase.PROGRESS, 0))
                        .addTaskVos(task(30402, TaskPhase.PROGRESS, 1))
                        .build(),
                0);
    }

    /** 斗帝之路搜寻。家族试炼领奖后进入薰儿节点 22。 */
    @PlayerCmd
    public void findFireTarget(
            IPlayerContext context,
            FireTargetFindReq request) {
        int nodeId = fireTargetNode(context.getId());
        context.write(
                75653,
                FireTargetFindResp.newBuilder()
                        .setNodeId(nodeId)
                        .build(),
                0);
    }

    @PlayerCmd
    public void claimFireTargetReward(
            IPlayerContext context,
            FireTargetRewardReq request) {
        context.write(
                75658,
                FireTargetRewardResp.newBuilder()
                        .setNodeId(fireTargetNode(context.getId()))
                        .build(),
                0);
    }

    /** 薰儿节点解锁第二角色槽，客户端随后发 HeroJobCreateReq heroIndex=1。 */
    @PlayerCmd
    public void unlockFireTargetRole(
            IPlayerContext context,
            FireTargetRoleUnlockReq request) {
        unlockSecondHero(context);
    }

    @PlayerCmd
    public void unlockHero(
            IPlayerContext context,
            HeroUnlockReq request) {
        if (request.getUnlockHeroIndex() == SECOND_HERO_INDEX) {
            unlockSecondHero(context);
        }
    }

    /**
     * 斗技界面节点战力。第 9 关引导 10043 打开斗技时会发，
     * 不回 50459 会卡在药老演示焰分噬浪尺的对白上。对应抓包 idx 8225-8226。
     */
    @PlayerCmd
    public void queryHeroOriginNodeStat(
            IPlayerContext context,
            HeroOriginNodeStatReq request) {
        int node = request.getStatShowNode() == 0
                ? STAT_SHOW_NODE_NEW_EQUIP
                : request.getStatShowNode();
        context.write(
                50459,
                HeroOriginNodeStatResp.newBuilder()
                        .setHeroVo(HeroOriginNodeStatVo.newBuilder()
                                .setHeroIndex(request.getHeroIndex())
                                .setStatShowNodeVo(StatShowNodeVo.newBuilder()
                                        .setStatShowNode(node)
                                        .setFightForce(0))
                                .setHeroPower(currentFightForce(context.getId()))
                                .setHeroNodePower(0))
                        .build(),
                0);
        if (heroLevels.getOrDefault(context.getId(), 2)
                >= THIRD_SKILL_SLOT_UNLOCK_LEVEL) {
            pushThirdSkillSlotUnlock(context, request.getHeroIndex());
        }
    }

    /**
     * 点锁定血脉槽。官服创角抓包用 75015 推 3001，点格子则走 75002/75003。
     * 客户端已按 ConditionHeroLevel=6 拦截；重启后内存等级可能落后于客户端。
     */
    @PlayerCmd
    public void unlockHeroSkillSlot(
            IPlayerContext context,
            HeroSkillSlotUnlockReq request) {
        int slotId = request.getSlotId();
        if (slotId != THIRD_SKILL_SLOT_ID) {
            return;
        }
        unlockedSlots(context.getId()).add(slotId);
        context.write(
                75003,
                HeroSkillSlotUnlockResp.newBuilder()
                        .setHeroIndex(request.getHeroIndex())
                        .setSlot(emptySkillSlot(slotId))
                        .build(),
                0);
    }

    private double currentFightForce(long playerId) {
        return playerFightForce.getOrDefault(playerId, DEFAULT_HERO_POWER);
    }

    private void noteFightForce(long playerId, double force) {
        playerFightForce.put(playerId, force);
    }

    private Set<Integer> unlockedSlots(long playerId) {
        return unlockedHeroSkillSlots.computeIfAbsent(
                playerId, ignored -> ConcurrentHashMap.newKeySet());
    }

    private static HeroSkillSlotVo emptySkillSlot(int slotId) {
        return HeroSkillSlotVo.newBuilder()
                .setId(slotId)
                .setLv(0)
                .setMasterLv(0)
                .setAnyOnSkill(false)
                .build();
    }

    /**
     * 解锁血脉槽3。官服抓包 idx 6717：75015，id=3001，anyOnSkill=false。
     */
    private void pushThirdSkillSlotUnlock(
            IPlayerContext context,
            int heroIndex) {
        if (!unlockedSlots(context.getId()).add(THIRD_SKILL_SLOT_ID)) {
            return;
        }
        context.write(
                75015,
                HeroSkillSlotUpdateResp.newBuilder()
                        .setHeroIndex(heroIndex)
                        .addSlots(emptySkillSlot(THIRD_SKILL_SLOT_ID))
                        .build(),
                0);
    }

    private int fireTargetNode(long playerId) {
        return crisisEvent20010Rewarded.contains(playerId)
                ? FIRE_TARGET_XUNER_NODE
                : FIRE_TARGET_FAMILY_NODE;
    }

    private void writeFireTargetInfo(
            IPlayerContext context,
            int nodeId,
            boolean win,
            boolean unlocked) {
        context.write(
                75651,
                FireTargetInfoResp.newBuilder()
                        .setNodeId(nodeId)
                        .setWin(win)
                        .setUnlocked(unlocked)
                        .build(),
                0);
    }

    private void unlockSecondHero(IPlayerContext context) {
        if (!crisisEvent20010Rewarded.contains(context.getId())) {
            return;
        }
        secondHeroUnlocked.add(context.getId());
        writeFireTargetInfo(context, FIRE_TARGET_XUNER_NODE, true, true);
        context.write(
                75662,
                FireTargetRoleUnlockResp.newBuilder()
                        .setNodeId(FIRE_TARGET_XUNER_NODE)
                        .setWin(true)
                        .setUnlocked(true)
                        .build(),
                0);
        context.write(
                50453,
                HeroUnlockResp.newBuilder()
                        .setUnlockHeroIndex(SECOND_HERO_INDEX)
                        .build(),
                0);
        context.write(
                50451,
                HeroUnlockIndexesResp.newBuilder()
                        .addUnlockHeroIndexes(0)
                        .addUnlockHeroIndexes(SECOND_HERO_INDEX)
                        .build(),
                0);
        LOGGER.info(
                "Second hero slot unlocked: player={}",
                context.getId());
    }

    /** 普通炼制 source=0；抓包 idx 611/1639/5357 均为4颗、每颗280修为。 */
    @PlayerCmd
    public void makeAlchemy(
            IPlayerContext context,
            AlchemyNewMakeReq request) {
        long playerId = context.getId();
        AlchemyState state = alchemyStates.computeIfAbsent(playerId, ignored -> new AlchemyState());
        refreshAlchemyDay(context);
        int level = alchemyLevels.getOrDefault(playerId, heroStages.getOrDefault(playerId, 1));
        com.fasterxml.jackson.databind.JsonNode config = Chapter17Data.row("elixirBase", "Key", level);
        int itemId = config.path("CostItemId").asInt();
        int cost = config.path("CostRide").asInt();
        UpdateItem material = alchemyMaterial(context, itemId);
        if (material == null || material.getPackItem().getSize() < cost) {
            LOGGER.info("Alchemy denied: player={}, item={}, reason=insufficient_inventory", playerId, itemId);
            return;
        }
        long used = state.dailyCosts.getOrDefault(itemId, 0L);
        int noLimitDay = config.path("NoLimitCondition").path(0).path("Context").path("Value").asInt();
        int openDay = Math.max(serverOpenDay, serverClock.openDayAt(alchemyNow()));
        if (openDay < noLimitDay && used + cost > config.path("DailyLimit").asLong()) {
            writeAlchemyCosts(context, state);
            LOGGER.info("Alchemy denied: player={}, item={}, used={}, reason=daily_limit", playerId, itemId, used);
            return;
        }
        int makeTimes = alchemyMakeTimes.getOrDefault(playerId, 0) + 1;
        alchemyMakeTimes.put(playerId, makeTimes);
        int nextId = nextAlchemyId.getOrDefault(playerId, 0);
        // source=2 是独立产物推送，抓包中不增加 makeTimes，不能混入普通炼制。
        int count = config.path("CostItemNum").asInt();
        // 已验证的普通/斗者配置各只有一个产物（权重 10000）。
        int rid = Integer.parseInt(config.path("CreatePer").fieldNames().next());
        com.fasterxml.jackson.databind.JsonNode product = Chapter17Data.row("elixirAccumulate", "Key", rid);
        int exp = product.path("Exp").asInt();
        int qlt = product.path("Quality").asInt();
        List<AlchemyNewMakeVo> mergeRoots = state.products.values().stream()
                .filter(pill -> state.productKinds.getOrDefault(
                        pill.getId(), pill.getRid()) == rid)
                .sorted(java.util.Comparator.comparingLong(AlchemyNewMakeVo::getId))
                .limit(count)
                .collect(java.util.stream.Collectors.toList());
        context.write(50402, packUpdate(75052, changeAlchemyMaterial(context, itemId, -cost, 0)), 0);
        AlchemyNewMakeResp.Builder make =
                AlchemyNewMakeResp.newBuilder()
                        .setMakeTimes(makeTimes)
                        .setSource(0);
        List<AlchemyNewMakeVo> made = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int id = nextId + i;
            AlchemyNewMakeVo pill = AlchemyNewMakeVo.newBuilder()
                    .setId(id)
                    .setRid(rid)
                    .setExp(exp)
                    .setQlt(qlt)
                    .setNum(1)
                    .setRate(0).build();
            state.products.put((long) id, pill);
            state.productKinds.put((long) id, rid);
            make.addMakeIds(pill);
            made.add(pill);
        }
        nextAlchemyId.put(playerId, nextId + count);
        context.write(75053, make.build(), 0);
        if (mergeRoots.size() == count) {
            AlchemyNewMergeResp.Builder response = AlchemyNewMergeResp.newBuilder();
            for (int i = 0; i < count; i++) {
                AlchemyNewMakeVo root = mergeRoots.get(i);
                AlchemyNewMakeVo consumed = made.get(i);
                AlchemyNewMakeVo merged = root.toBuilder()
                        .setRid(0)
                        .setExp(Math.addExact(root.getExp(), consumed.getExp()))
                        .setNum(Math.addExact(root.getNum(), consumed.getNum()))
                        .build();
                state.products.put(root.getId(), merged);
                state.products.remove(consumed.getId());
                state.productKinds.remove(consumed.getId());
                response.addMerges(AlchemyNewMergeVo.newBuilder()
                        .setMake(merged)
                        .addUseIds(consumed.getId()));
            }
            context.write(75068, response.build(), 0);
        }

        state.dailyCosts.put(itemId, used + cost);
        writeAlchemyCosts(context, state);
    }

    java.time.Instant alchemyNow() {
        return java.time.Instant.now();
    }

    /** 心跳也检查跨日，避免客户端因旧上限禁用按钮后无法发起炼制。 */
    public void refreshAlchemyDay(IPlayerContext context) {
        AlchemyState state = alchemyStates.get(context.getId());
        if (state == null) return;
        java.time.LocalDate today = alchemyNow().atZone(java.time.ZoneId.of("Asia/Shanghai")).toLocalDate();
        if (state.costDay == null) {
            state.costDay = today;
        } else if (!state.costDay.equals(today)) {
            state.costDay = today;
            state.dailyCosts.clear();
            writeAlchemyCosts(context, state);
        }
    }

    private static void writeAlchemyCosts(IPlayerContext context, AlchemyState state) {
        AlchemyNewMakeCostItemNumUpdateResp.Builder response = AlchemyNewMakeCostItemNumUpdateResp.newBuilder();
        // 客户端先清空字典再加载，此处必须下发所有材料的计数快照。
        state.dailyCosts.forEach((itemId, used) -> response.addCostItem2Nums(
                IntegerAndLongPairEntry.newBuilder().setKey(itemId).setValue(used)));
        context.write(75088, response.build(), 0);
    }

    private UpdateItem alchemyMaterial(IPlayerContext context, int itemId) {
        if (context instanceof com.doupo.server.foundation.player.PlayerConnectionContext) {
            return ((com.doupo.server.foundation.player.PlayerConnectionContext) context).itemStack(itemId);
        }
        AlchemyState state = alchemyStates.get(context.getId());
        return state == null ? null : state.materials.get(itemId);
    }

    UpdateItem changeAlchemyMaterial(IPlayerContext context, int itemId, long delta, int newItemIndex) {
        AlchemyState state = alchemyStates.computeIfAbsent(context.getId(), ignored -> new AlchemyState());
        UpdateItem previous = alchemyMaterial(context, itemId);
        long total = Math.addExact(previous == null ? 0L : previous.getPackItem().getSize(), delta);
        if (total < 0) throw new IllegalArgumentException("Insufficient elixir " + itemId);
        int index = previous == null ? newItemIndex : previous.getItemIndex();
        UpdateItem.Builder update = UpdateItem.newBuilder().setItemIndex(index);
        if (total > 0) {
            long now = System.currentTimeMillis();
            PackItemVo.Builder stack = previous == null ? PackItemVo.newBuilder().setKey(itemId)
                    .setObjectId(context.getId() * 1000 + index).setCreateTime(now) : previous.getPackItem().toBuilder();
            if (delta > 0) stack.setLastGainTime(now);
            update.setPackItem(stack.setSize(Math.toIntExact(total)));
        }
        UpdateItem result = update.build();
        if (total == 0) state.materials.remove(itemId);
        else state.materials.put(itemId, result);
        return result;
    }

    /** 服用首次炼制物，匹配抓包 idx 630-645。 */
    @PlayerCmd
    public void takeAlchemy(
            IPlayerContext context,
            AlchemyNewTakingReq request) {
        long playerId = context.getId();
        int takingCount = alchemyTakingCounts.getOrDefault(playerId, 0);
        long exp = alchemyExpPools.getOrDefault(playerId, 0L);
        List<Long> takingIds = new ArrayList<>();
        AlchemyState state = alchemyStates.computeIfAbsent(playerId, ignored -> new AlchemyState());

        for (long alchemyId : request.getAlchemyIdList()) {
            AlchemyNewMakeVo pill = state.products.remove(alchemyId);
            if (pill == null) {
                continue;
            }
            state.productKinds.remove(alchemyId);
            takingIds.add(alchemyId);
            takingCount++;
            state.takingCounts.merge(pill.getRid(), 1, Integer::sum);
            exp += pill.getExp();

            if (takingCount == 2 && exp == 560L && heroLevels.getOrDefault(playerId, 1) == 1) {
                writeAlchemyTaking(
                        context,
                        exp,
                        state,
                        takingIds);
                takingIds.clear();
                context.write(
                        75057,
                        HeroLevelUpgradeResp.newBuilder()
                                .setHeroVo(HeroLevelVo.newBuilder()
                                        .setHeroIndex(0)
                                        .setLevel(2)
                                        .setStage(1))
                                .build(),
                        0);
                heroLevels.put(playerId, 2);
                context.write(
                        75060,
                        AlchemyNewExpChangeResp.newBuilder()
                                .setExp(60)
                                .setLevel(1)
                                .build(),
                        0);
                exp = 60L;
            }
        }

        if (!takingIds.isEmpty()) {
            writeAlchemyTaking(context, exp, state, takingIds);
        }
        alchemyExpPools.put(playerId, exp);
        alchemyTakingCounts.put(playerId, takingCount);
        LOGGER.info(
                "Alchemy taken: player={}, exp={}, takingCount={}, level={}, stage={}",
                playerId,
                exp,
                takingCount,
                heroLevels.getOrDefault(playerId, 2),
                heroStages.getOrDefault(playerId, 1));
        tryAutoStepUpgradeAfterExp(context);
        tryOpenEighthRealmBagModules(context);
    }

    private static void writeAlchemyTaking(
            IPlayerContext context,
            long exp,
            AlchemyState state,
            List<Long> takingIds) {
        AlchemyNewTakingResp.Builder resp = AlchemyNewTakingResp.newBuilder()
                .setExp(exp)
                .addAllAlchemyVo(takingIds);
        state.takingCounts.forEach((rid, count) -> resp.addTakingInfo(
                IntegerAndIntegerPairEntry.newBuilder().setKey(rid).setValue(count)));
        context.write(75055, resp.build(), 0);
    }

    /** 2级升阶，匹配抓包 idx 674-689 中与升阶相关的响应。 */
    @PlayerCmd
    public void breakHeroLevel(
            IPlayerContext context,
            HeroLevelBreakReq request) {
        long playerId = context.getId();
        long sceneUnitId = playerId * 1000 + 1;
        int heroIndex = request.getHeroIndex();
        int currentLevel = heroLevels.getOrDefault(playerId, 2);
        if (PlayerRealmConfig.atStageCap(
                currentLevel,
                heroStages.getOrDefault(playerId, 1))) {
            upgradeHeroStep(
                    context,
                    HeroStepUpgradeReq.newBuilder()
                            .setHeroIndex(heroIndex)
                            .build());
            return;
        }
        long currentExp = alchemyExpPools.getOrDefault(playerId, 0L);
        long requiredExp = breakRequiredExp(currentLevel);
        if (currentExp < requiredExp) {
            LOGGER.info(
                    "Hero level break ignored, exp short: player={}, level={}, "
                            + "stage={}, exp={}, need={}",
                    playerId,
                    currentLevel,
                    heroStages.getOrDefault(playerId, 1),
                    currentExp,
                    requiredExp);
            return;
        }

        int nextLevel = currentLevel + 1;
        long remainingExp = currentExp - requiredExp;
        heroLevels.put(playerId, nextLevel);
        alchemyExpPools.put(playerId, remainingExp);
        LOGGER.info(
                "Hero level broke: player={}, level={}, expLeft={}",
                playerId, nextLevel, remainingExp);

        context.write(
                75067,
                HeroLevelBreakResp.newBuilder()
                        .setHeroVo(HeroLevelVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .setLevel(nextLevel)
                                .setStage(heroStages.getOrDefault(playerId, 1)))
                        .build(),
                0);
        context.write(
                75060,
                AlchemyNewExpChangeResp.newBuilder()
                        .setExp(remainingExp)
                        .setLevel(alchemyLevels.getOrDefault(playerId,
                                heroStages.getOrDefault(playerId, 1)))
                        .build(),
                0);

        int shortInfoLevel = nextLevel;
        if (nextLevel == 6 && remainingExp >= 1000) {
            remainingExp -= 1000;
            heroLevels.put(playerId, 7);
            alchemyExpPools.put(playerId, remainingExp);
            context.write(
                    75057,
                    HeroLevelUpgradeResp.newBuilder()
                            .setHeroVo(HeroLevelVo.newBuilder()
                                    .setHeroIndex(heroIndex)
                                    .setLevel(7)
                                    .setStage(1))
                            .build(),
                    0);
            context.write(
                    75060,
                    AlchemyNewExpChangeResp.newBuilder()
                            .setExp(remainingExp)
                            .setLevel(1)
                            .build(),
                    0);
            shortInfoLevel = 7;
        }
        context.write(
                75153,
                HeroShortInfoUpdateResp.newBuilder()
                        .setShortInfoVo(HeroShortInfoVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .setCrossHeroShortInfo(
                                        buildHeroShortInfo(playerId)
                                                .toBuilder()
                                                .setLevel(shortInfoLevel)
                                                .setStage(heroStages.getOrDefault(
                                                        playerId, 1))))
                        .build(),
                0);
        syncHeroLevelTasks(context);
        if (shortInfoLevel == 7) {
            writeEighthLevelBreakUpdates(context, heroIndex, sceneUnitId);
        } else if (shortInfoLevel >= 8) {
            writeFighterRealmStats(context, heroIndex, sceneUnitId, shortInfoLevel);
            tryAutoStepUpgradeAfterExp(context);
        } else if (shortInfoLevel >= THIRD_SKILL_SLOT_UNLOCK_LEVEL) {
            pushThirdSkillSlotUnlock(context, heroIndex);
        }
        if (nextLevel == 4) {
            GuidanceState state = guidanceStates.get(playerId);
            if (state != null
                    && state.chapterId == 10100501
                    && state.autoSkillOpened
                    && !state.autoSkillTaskFollowUpOpened) {
                state.autoSkillTaskFollowUpOpened = true;
                context.write(50906, nextTaskUpdate(200010), 0);
            }
            writeFourthLevelBreakUpdates(context, heroIndex, sceneUnitId);
            return;
        }
        if (nextLevel == 5) {
            writeFifthLevelBreakUpdates(context, heroIndex, sceneUnitId);
            return;
        }
        if (nextLevel != 3) {
            return;
        }
        context.write(
                78602,
                StrongStatueInfoResp.getDefaultInstance(),
                0);
        context.write(60751, BattlePassInfoResp.getDefaultInstance(), 0);
        context.write(60751, BattlePassInfoResp.getDefaultInstance(), 0);
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(200005, TaskPhase.FINISHED, 1))
                        .addTaskVos(task(40001, TaskPhase.PROGRESS, 3))
                        .build(),
                0);

        context.write(
                50455,
                HeroStatUpdateResp.newBuilder()
                        .setHeroVo(HeroStatVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .addStats(stat(102001, 255))
                                .addStats(stat(105001, 1))
                                .addStats(stat(106001, 1))
                                .addStats(stat(102002, 230))
                                .addStats(stat(103001, 21137))
                                .addStats(stat(101001, 809.2535))
                                .addStats(stat(101002, 685))
                                .addStats(stat(103002, 17130))
                                .addStats(stat(104001, 100)))
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103011, 18522.73185629457))
                        .addAttrList(attribute(103001, 21137))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(101001, 809.2535))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        grantPlayerStats(context.getId(), 809.2535, 21137);
        context.write(
                52351,
                PlayerFightForceResp.newBuilder()
                        .setPlayerFightForce(14014)
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                75151,
                HeroFightForceResp.newBuilder()
                        .addHeroVoList(HeroFightForceVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .setFightForce(14014))
                        .build(),
                0);
        context.write(
                52355,
                PlayerFightForceCheckPointChangeResp.newBuilder()
                        .setCheckPointType(2)
                        .setPlayerFightForceBefore(12579)
                        .setPlayerFightForceAfter(14014)
                        .addPlayerStatMapBefore(stringDouble("def", 193))
                        .addPlayerStatMapBefore(stringDouble("hp", 17551.3))
                        .addPlayerStatMapBefore(stringDouble("atk", 671.616))
                        .addPlayerStatMapAfter(stringDouble("def", 255))
                        .addPlayerStatMapAfter(stringDouble("hp", 21137))
                        .addPlayerStatMapAfter(stringDouble("atk", 809.2535))
                        .build(),
                0);

        TaskUpdateResp.Builder fightTasks = TaskUpdateResp.newBuilder();
        for (int taskId : new int[] {
                4002009, 4002004, 4002003, 4002002,
                4002008, 4002007, 4002006, 4002005
        }) {
            fightTasks.addTaskVos(
                    task(taskId, TaskPhase.PROGRESS, 14014));
        }
        context.write(50906, fightTasks.build(), 0);
    }

    /** 四段突破后的属性和任务更新，对应抓包 idx 1678-1688。 */
    private void writeFourthLevelBreakUpdates(
            IPlayerContext context,
            int heroIndex,
            long sceneUnitId) {
        context.write(78602, StrongStatueInfoResp.getDefaultInstance(), 0);
        context.write(60751, BattlePassInfoResp.getDefaultInstance(), 0);
        context.write(60751, BattlePassInfoResp.getDefaultInstance(), 0);
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(40001, TaskPhase.PROGRESS, 4))
                        .addTaskVos(task(200010, TaskPhase.FINISHED, 1))
                        .build(),
                0);
        context.write(
                50455,
                HeroStatUpdateResp.newBuilder()
                        .setHeroVo(HeroStatVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .addStats(stat(102001, 369))
                                .addStats(stat(105001, 1))
                                .addStats(stat(106001, 1))
                                .addStats(stat(102002, 294))
                                .addStats(stat(103001, 27985.1))
                                .addStats(stat(101001, 1060.8796000000002))
                                .addStats(stat(101002, 812))
                                .addStats(stat(103002, 20324))
                                .addStats(stat(104001, 100)))
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103011, 27409.559114840406))
                        .addAttrList(attribute(103001, 27985.1))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(101001, 1060.8796000000002))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        grantPlayerStats(context.getId(), 1060.8796000000002, 27985.1);
        context.write(
                52351,
                PlayerFightForceResp.newBuilder()
                        .setPlayerFightForce(16670)
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                75151,
                HeroFightForceResp.newBuilder()
                        .addHeroVoList(HeroFightForceVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .setFightForce(16670))
                        .build(),
                0);
        context.write(
                52355,
                PlayerFightForceCheckPointChangeResp.newBuilder()
                        .setCheckPointType(2)
                        .setPlayerFightForceBefore(15202)
                        .setPlayerFightForceAfter(16670)
                        .addPlayerStatMapBefore(stringDouble("def", 305))
                        .addPlayerStatMapBefore(stringDouble("hp", 24312))
                        .addPlayerStatMapBefore(stringDouble("atk", 920.7605))
                        .addPlayerStatMapAfter(stringDouble("def", 369))
                        .addPlayerStatMapAfter(stringDouble("hp", 27985.1))
                        .addPlayerStatMapAfter(
                                stringDouble("atk", 1060.8796000000002))
                        .build(),
                0);

        TaskUpdateResp.Builder fightTasks = TaskUpdateResp.newBuilder();
        for (int taskId : new int[] {
                4002009, 4002004, 4002003, 4002002,
                4002008, 4002007, 4002006, 4002005
        }) {
            fightTasks.addTaskVos(task(taskId, TaskPhase.PROGRESS, 16670));
        }
        context.write(50906, fightTasks.build(), 0);
    }

    /** 纳戒抽取：抽取次数只由当前能量与请求类型决定。 */
    @PlayerCmd
    public void drawNewFightSkill(
            IPlayerContext context,
            LotteryDrawReq request) {
        if (request.getLotteryTypeValue() == 13) {
            drawChapter17CommonSkill(context, request);
            return;
        }
        if (request.getLotteryType()
                != LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL) {
            return;
        }

        writeNewFightSkillDraw(context, request);
    }

    /** 首次抽取后的斗技激活、任务和战力同步，协议依据抓包 idx 1095-1106。 */
    private void writeFirstNewFightSkillMilestone(
            IPlayerContext context) {
        activatedSkills(context.getId()).add(80130011L);
        noteLearnedSkill(context.getId(), 80130011, 1, 2);
        pendingStarCredits(context.getId()).add(80130011);
        removeLotteryItem(context.getId(), 4);
        long sceneUnitId = context.getId() * 1000 + 1;
        context.write(
                50402,
                packUpdate(
                        75004,
                        UpdateItem.newBuilder()
                                .setItemIndex(4)
                                .build()),
                0);

        SkillVo skill = SkillVo.newBuilder()
                .setBaseId(80130011)
                .setStar(1)
                .setActStar(0)
                .setAwaken(false)
                .setSelElement(0)
                .build();
        context.write(
                75011,
                HeroSkillActResp.newBuilder()
                        .setHeroIndex(0)
                        .setShowTips(true)
                        .setPlayerSkill(PlayerSkillVo.newBuilder()
                                .setBaseId(80130011)
                                .addHeroSkills(PlayerHeroSkillVo.newBuilder()
                                        .setHeroIdx(-1)
                                        .setHeroSkill(skill)))
                        .build(),
                0);
        context.write(
                75049,
                HeroSkillStarTotalMaxHisUpdateResp.newBuilder()
                        .setHeroIndex(0)
                        .setSkillStarTotalMaxHis(2)
                        .build(),
                0);
        context.write(
                75039,
                HeroSkillBaseUpdateResp.newBuilder()
                        .addBaseIds(80130011)
                        .build(),
                0);
        context.write(60751, BattlePassInfoResp.getDefaultInstance(), 0);
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(200007, TaskPhase.FINISHED, 1))
                        .addTaskVos(task(2201006, TaskPhase.PROGRESS, 1))
                        .build(),
                0);
        context.write(
                50455,
                HeroStatUpdateResp.newBuilder()
                        .setHeroVo(HeroStatVo.newBuilder()
                                .setHeroIndex(0)
                                .addStats(stat(107002, 13))
                                .addStats(stat(102001, 262.5))
                                .addStats(stat(105001, 1))
                                .addStats(stat(106001, 1))
                                .addStats(stat(103001, 21568.25))
                                .addStats(stat(101001, 825.97955))
                                .addStats(stat(104001, 100)))
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103011, 21067.23231773667))
                        .addAttrList(attribute(103001, 21568.25))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(101001, 825.97955))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        grantPlayerStats(context.getId(), 825.97955, 21568.25);
        context.write(
                52351,
                PlayerFightForceResp.newBuilder()
                        .setPlayerFightForce(14188)
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                75151,
                HeroFightForceResp.newBuilder()
                        .addHeroVoList(HeroFightForceVo.newBuilder()
                                .setHeroIndex(0)
                                .setFightForce(14188))
                        .build(),
                0);

        TaskUpdateResp.Builder fightTasks = TaskUpdateResp.newBuilder();
        for (int taskId : new int[] {
                4002009, 4002004, 4002003, 4002002,
                4002008, 4002007, 4002006, 4002005
        }) {
            fightTasks.addTaskVos(
                    task(taskId, TaskPhase.PROGRESS, 14188));
        }
        context.write(50906, fightTasks.build(), 0);
    }

    /**
     * 纳戒抽取事务。消耗来自 LotteryTypeConfig(Id=12)：单次 100200×10，
     * 十连 100200×100；实际结算次数由当前余额决定。
     */
    private void writeNewFightSkillDraw(
            IPlayerContext context,
            LotteryDrawReq request) {
        int energy = newFightSkillEnergy.getOrDefault(context.getId(), 0);
        int count = request.getTen()
                ? (energy >= NEW_FIGHT_SKILL_TEN_COST ? 10 : 0)
                : energy / NEW_FIGHT_SKILL_DRAW_COST;
        int cost = request.getTen()
                ? NEW_FIGHT_SKILL_TEN_COST
                : count * NEW_FIGHT_SKILL_DRAW_COST;
        if (count <= 0 || energy < cost) {
            LOGGER.info(
                    "New fight skill draw rejected: player={}, ten={}, "
                            + "subId={}, energy={}, required={}",
                    context.getId(),
                    request.getTen(),
                    request.getSubId(),
                    energy,
                    request.getTen()
                            ? NEW_FIGHT_SKILL_TEN_COST
                            : NEW_FIGHT_SKILL_DRAW_COST);
            context.write(
                    77352,
                    LotteryDrawResp.newBuilder()
                            .setLotteryType(
                                    LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                            .setTen(request.getTen())
                            .setSubId(request.getSubId())
                            .setFailure(true)
                            .build(),
                    0);
            return;
        }

        int completedDrawTime = newFightSkillLotteryDrawTimes.getOrDefault(
                context.getId(), 0);
        int after = energy - cost;
        long now = System.currentTimeMillis();
        context.write(
                50402,
                packUpdate(
                        77351,
                        changeNewFightSkillEnergy(
                                context, -cost, 1, now)),
                0);

        LotteryDrawResp.Builder response = LotteryDrawResp.newBuilder()
                .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                .setTen(request.getTen())
                .setSubId(request.getSubId())
                .setAdvFlag(false)
                .setFailure(false);
        Map<Integer, LotteryItemStack> changedStacks = new LinkedHashMap<>();
        List<Integer> rewardKeys = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            int ordinal = completedDrawTime + i + 1;
            int skill = newFightSkillRewardKey(ordinal);
            int amount = newFightSkillRewardAmount(ordinal);
            rewardKeys.add(skill);
            response.addRewardItemVos(RewardItemVo.newBuilder()
                    .setItemKey(skill)
                    .setAmount(amount)
                    .setOperationType(77353)
                    .setGiveFlag(false));
            response.addHitPondIds(newFightSkillPondId(ordinal));
            // 同一次请求可能从累计 3 抽跨到 7 抽，必须逐个奖励判断引导边界。
            LotteryItemStack stack = ordinal > 5
                    ? addOrStackLotteryItem(
                            context.getId(), skill, amount, now)
                    : putLotteryItem(
                            context.getId(),
                            newFightSkillRewardItemIndex(ordinal),
                            skill,
                            amount,
                            now);
            changedStacks.put(stack.itemIndex, stack);
        }
        UpdateItem[] rewardItems = new UpdateItem[changedStacks.size()];
        int rewardSlot = 0;
        for (LotteryItemStack stack : changedStacks.values()) {
            rewardItems[rewardSlot++] = lotteryUpdateItem(stack);
        }
        context.write(50402, packUpdate(77353, rewardItems), 0);
        context.write(77352, response.build(), 0);

        int totalDrawTime = completedDrawTime + count;
        newFightSkillLotteryDrawTimes.put(
                context.getId(), totalDrawTime);
        LOGGER.info(
                "New fight skill draw settled: player={}, ten={}, subId={}, "
                        + "energyBefore={}, cost={}, energyAfter={}, "
                        + "drawCount={}, totalDrawTime={}",
                context.getId(),
                request.getTen(),
                request.getSubId(),
                energy,
                cost,
                after,
                count,
                totalDrawTime);
        int stage = newFightSkillLotteryStage.getOrDefault(
                context.getId(), 1);
        NewFightSkillLotteryVo.Builder lotteryVo =
                NewFightSkillLotteryVo.newBuilder().setShowStage(stage);
        int remaining = totalDrawTime;
        for (int currentStage = 1;
                currentStage <= stage && remaining > 0;
                currentStage++) {
            int stageDrawTime = Math.min(
                    remaining,
                    NEW_FIGHT_SKILL_STAGE_TIMES[currentStage - 1]);
            lotteryVo.addShowStage2DrawCountInfoList(
                    IntegerAndIntegerPairEntry.newBuilder()
                            .setKey(currentStage)
                            .setValue(stageDrawTime));
            remaining -= stageDrawTime;
        }
        context.write(
                77354,
                LotteryInfoResp.newBuilder()
                        .setLotteryType(
                                LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                        .setTotalDrawTime(totalDrawTime)
                        .setTodayDrawTime(totalDrawTime)
                        .setMustRewardNeedTime(-totalDrawTime)
                        .setExtraDrawTime(totalDrawTime)
                        .setNewFightSkillLotteryVo(lotteryVo)
                        .build(),
                0);

        if (completedDrawTime == 0) {
            writeFirstNewFightSkillMilestone(context);
        }
        if (completedDrawTime < 5 && totalDrawTime >= 5) {
            writeFifthNewFightSkillMilestone(context);
        } else if (totalDrawTime > 1 && totalDrawTime < 5) {
            context.write(
                    50906,
                    TaskUpdateResp.newBuilder()
                            .addTaskVos(task(
                                    2201006,
                                    TaskPhase.PROGRESS,
                                    totalDrawTime))
                            .build(),
                    0);
        }

        // 先处理本批跨过的引导节点，再激活第 6 抽以后的新技能，不能漏掉同批的裂爪击。
        for (int i = 0; i < rewardKeys.size(); i++) {
            if (completedDrawTime + i + 1 > 5) {
                writeNewFightSkillActivation(context, rewardKeys.get(i));
            }
        }

        Integer lotteryTask = pendingLotteryMainTasks.remove(context.getId());
        if (lotteryTask != null) {
            context.write(
                    50906,
                    TaskUpdateResp.newBuilder()
                            .addTaskVos(task(
                                    lotteryTask,
                                    TaskPhase.FINISHED,
                                    1))
                            .build(),
                    0);
        }
    }

    /** 手动学习普通斗技；点击「可激活」发送 75010，不是装配请求 75004。 */
    @PlayerCmd
    public void activateHeroSkill(IPlayerContext context, HeroSkillActReq request) {
        int skillId = request.getSkillBaseId();
        com.fasterxml.jackson.databind.JsonNode config = HeroSkillConfig.get(skillId);
        if (request.getHeroIndex() != 0 || config == null
                || skillId < 80000000 || skillId >= 90000000) {
            return;
        }
        // 本入口只处理已核对的单本道具学习；带额外解锁条件的技能不绕过校验。
        com.fasterxml.jackson.databind.JsonNode needs = config.path("activeNeed");
        if (needs.size() != 1 || !"item".equals(needs.path(0).path("Type").asText())
                || needs.path(0).path("Amount").asInt() != 1
                || config.path("skillUnlock").size() != 0
                || config.path("arcaneUnlock").size() != 0) {
            return;
        }
        boolean learned = learnedSkills(context.getId()).contains((long) skillId);
        if (!learned) {
            LotteryItemStack stack = consumeLotteryItem(context.getId(), needs.path(0).path("Id").asInt());
            if (stack == null) {
                LOGGER.info("Skill activation rejected, no book: player={}, skill={}", context.getId(), skillId);
                return;
            }
            int star = config.path("star").asInt();
            noteLearnedSkill(context.getId(), skillId, star, 0);
            activatedSkills(context.getId()).add((long) skillId);
            context.write(50402, packUpdate(HERO_SKILL_ACT_COST, lotteryUpdateItem(stack)), 0);
        }
        ConcurrentMap<Integer, Integer> stars = heroSkillStars.get(context.getId());
        int star = stars == null ? config.path("star").asInt()
                : stars.getOrDefault(skillId, config.path("star").asInt());
        int total = 0;
        for (long baseId : learnedSkills(context.getId())) {
            com.fasterxml.jackson.databind.JsonNode row = HeroSkillConfig.get((int) baseId);
            int baseStar = row == null ? 0 : row.path("star").asInt();
            total += stars == null ? baseStar : stars.getOrDefault((int) baseId, baseStar);
        }
        int maxHis = skillStarMaxHis.merge(context.getId(), total, Math::max);
        // 自动激活包未被客户端接收时允许补发，但不重复扣道具、重置星级或累计任务。
        writeSkillActivationState(context, skillId, star, maxHis, !learned);
        noteQualitySkill(context, skillId);
    }

    /**
     * 抽到 80 开头的斗技道具时激活成技能（道具 id + 1），按抓包 idx 1942-1945
     * 下发消耗 pack + HeroSkillActResp + HeroSkillStarTotalMaxHisUpdateResp +
     * HeroSkillBaseUpdateResp。纯道具（100053/201010 等）不激活。
     */
    private void writeNewFightSkillActivation(
            IPlayerContext context, int rewardKey) {
        if (rewardKey < 80000000 || rewardKey >= 90000000) {
            return;
        }
        int skillId = rewardKey + 1;
        com.fasterxml.jackson.databind.JsonNode config = HeroSkillConfig.get(skillId);
        if (config == null || learnedSkills(context.getId()).contains((long) skillId)) {
            return;
        }
        LotteryItemStack stack = consumeLotteryItem(context.getId(), rewardKey);
        if (stack == null) return;
        Set<Long> playerSkills = activatedSkills(context.getId());
        playerSkills.add((long) skillId);
        int activationIndex = playerSkills.size();
        int star = config.path("star").asInt();
        int maxHis;
        switch (activationIndex) {
            case 1: maxHis = 2; break;
            case 2: maxHis = 4; break;
            case 3: maxHis = 7; break;
            case 4: maxHis = 11; break;
            default: maxHis = 15 + (activationIndex - 5) * 4; break;
        }
        noteLearnedSkill(context.getId(), skillId, star, maxHis);
        pendingStarCredits(context.getId()).add(skillId);
        context.write(
                50402,
                packUpdate(HERO_SKILL_ACT_COST, lotteryUpdateItem(stack)),
                0);
        writeSkillActivationState(context, skillId, star, maxHis, true);
        noteQualitySkill(context, skillId);
        if (skillId == SECOND_SKILL_BASE_ID) {
            writeBlowPalmActStats(context);
        }
        LOGGER.info(
                "New fight skill activated: player={}, item={}, skill={}, "
                        + "star={}, maxHis={}",
                context.getId(), rewardKey, skillId, star, maxHis);
    }

    /** 第二次抓包 1464～1466：学习、星级历史、已学基础 ID 三个推送。 */
    private void writeSkillActivationState(
            IPlayerContext context, int skillId, int star, int maxHis, boolean showTips) {
        SkillVo skill = SkillVo.newBuilder()
                .setBaseId(skillId)
                .setStar(star)
                .setActStar(0)
                .setAwaken(false)
                .setSelElement(0)
                .build();
        context.write(
                75011,
                HeroSkillActResp.newBuilder()
                        .setHeroIndex(0)
                        .setShowTips(showTips)
                        .setPlayerSkill(PlayerSkillVo.newBuilder()
                                .setBaseId(skillId)
                                .addHeroSkills(PlayerHeroSkillVo.newBuilder()
                                        .setHeroIdx(-1)
                                        .setHeroSkill(skill)))
                        .build(),
                0);
        context.write(
                75049,
                HeroSkillStarTotalMaxHisUpdateResp.newBuilder()
                        .setHeroIndex(0)
                        .setSkillStarTotalMaxHis(maxHis)
                        .build(),
                0);
        context.write(
                75039,
                HeroSkillBaseUpdateResp.newBuilder()
                        .addBaseIds(skillId)
                        .build(),
                0);
    }

    /**
     * 前五抽来自抓包 idx 1093/1404/1483/1747；升 2 阶后前两抽保证 80128010
     *（idx 1940），之后按 idx 6086/6462/6944/7341/7444/8733。
     */
    private static int newFightSkillRewardKey(int ordinal) {
        switch (ordinal) {
            case 1:
            case 2:
            case 3:
                return 80130010;
            case 4:
                return 80140000;
            case 5:
                return 80130100;
            case 6:
            case 7:
                return 80128010;
            case 8:
                return 80130010;
            case 9:
                return 80140000;
            case 10:
                return 80128000;
            case 11:
            case 15:
                return 80130100;
            case 12:
                return 201010;
            case 13:
                return 100053;
            case 14:
                return 80128010;
            case 16:
                return 80130000;
            default:
                return NEW_FIGHT_SKILL_POOL[
                        (ordinal - 8) % NEW_FIGHT_SKILL_POOL.length];
        }
    }

    private static int newFightSkillRewardAmount(int ordinal) {
        return newFightSkillRewardKey(ordinal) == 201010 ? 50 : 1;
    }

    private static int newFightSkillPondId(int ordinal) {
        switch (ordinal) {
            case 1:
            case 2:
            case 3:
                return 7001002;
            case 4:
                return 7001001;
            case 5:
                return 7001005;
            case 6:
            case 7:
            case 14:
                return 7002009;
            case 8:
                return 7002002;
            case 9:
                return 7002001;
            case 10:
                return 7002006;
            case 11:
            case 15:
                return 7002005;
            case 12:
                return 7002016;
            case 13:
                return 7002014;
            case 16:
                return 7003014;
            default:
                return ordinal % 2 == 0 ? 7002002 : 7002009;
        }
    }

    private static int newFightSkillRewardItemIndex(int ordinal) {
        switch (ordinal) {
            case 1:
                return 4;
            case 2:
                return 5;
            case 3:
                return 6;
            case 4:
                return 9;
            case 5:
                return 10;
            default:
                return 12;
        }
    }

    private LotteryItemStack putLotteryItem(
            long playerId,
            int itemIndex,
            int key,
            int amount,
            long now) {
        LotteryItemStack stack = new LotteryItemStack(
                itemIndex, key, amount, playerId * 1000 + itemIndex, now);
        lotteryStacks(playerId).put(itemIndex, stack);
        int next = lotteryNextItemIndex.getOrDefault(playerId, 12);
        if (itemIndex + 1 > next) {
            lotteryNextItemIndex.put(playerId, itemIndex + 1);
        }
        return stack;
    }

    private LotteryItemStack addOrStackLotteryItem(
            long playerId, int key, int amount, long now) {
        ConcurrentMap<Integer, LotteryItemStack> stacks = lotteryStacks(playerId);
        LotteryItemStack last = null;
        for (LotteryItemStack stack : stacks.values()) {
            if (stack.key == key
                    && (last == null || stack.itemIndex > last.itemIndex)) {
                last = stack;
            }
        }
        if (last != null) {
            last.size += amount;
            last.lastGainTime = now;
            return last;
        }
        int itemIndex = allocateLotteryItemIndex(playerId);
        LotteryItemStack created = new LotteryItemStack(
                itemIndex, key, amount, playerId * 1000 + itemIndex, now);
        stacks.put(itemIndex, created);
        return created;
    }

    private int allocateLotteryItemIndex(long playerId) {
        int next = lotteryNextItemIndex.getOrDefault(playerId, 12);
        if (next < 12) {
            next = 12;
        }
        ConcurrentMap<Integer, LotteryItemStack> stacks = lotteryStacks(playerId);
        Integer energyIndex = newFightSkillEnergyItemIndexes.get(playerId);
        while (stacks.containsKey(next)
                || (energyIndex != null && next == energyIndex)) {
            next++;
        }
        lotteryNextItemIndex.put(playerId, next + 1);
        return next;
    }

    private LotteryItemStack consumeLotteryItem(long playerId, int key) {
        ConcurrentMap<Integer, LotteryItemStack> stacks = lotteryStacks(playerId);
        LotteryItemStack last = null;
        for (LotteryItemStack stack : stacks.values()) {
            if (stack.key == key
                    && (last == null || stack.itemIndex > last.itemIndex)) {
                last = stack;
            }
        }
        if (last == null || last.size <= 0) {
            return null;
        }
        last.size -= 1;
        if (last.size <= 0) {
            stacks.remove(last.itemIndex);
            return new LotteryItemStack(
                    last.itemIndex, last.key, 0, last.objectId, last.createTime);
        }
        return last;
    }

    private void removeLotteryItem(long playerId, int itemIndex) {
        lotteryStacks(playerId).remove(itemIndex);
    }

    private static UpdateItem lotteryUpdateItem(LotteryItemStack stack) {
        UpdateItem.Builder item = UpdateItem.newBuilder()
                .setItemIndex(stack.itemIndex);
        if (stack.size > 0) {
            item.setPackItem(PackItemVo.newBuilder()
                    .setObjectId(stack.objectId)
                    .setKey(stack.key)
                    .setSize(stack.size)
                    .setCreateTime(stack.createTime)
                    .setLastGainTime(stack.lastGainTime));
        }
        return item.build();
    }

    /** 纳戒升级，对应官服抓包 idx 1678-1680（第一次，showStage 1→2）与 idx 5236 附近（第二次，2→3）。 */
    @PlayerCmd
    public void upgradeLotteryPool(
            IPlayerContext context,
            LotteryPoolUpReq request) {
        if (request.getLotteryType()
                != LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL) {
            return;
        }

        int stage = newFightSkillLotteryStage.getOrDefault(
                context.getId(), 1);
        int drawTime = newFightSkillLotteryDrawTimes.getOrDefault(
                context.getId(), 0);
        int totalDrawTime = drawTime;

        int required = upgradeRequiredDrawTime(stage);
        if (required <= 0 || totalDrawTime < required) {
            LOGGER.info(
                    "New fight skill lottery upgrade rejected: player={}, "
                            + "stage={}, totalDrawTime={}, required={}",
                    context.getId(), stage, totalDrawTime, required);
            return;
        }

        int newStage = stage + 1;
        newFightSkillLotteryStage.put(context.getId(), newStage);
        LOGGER.info(
                "New fight skill lottery upgraded: player={}, stageBefore={}, "
                        + "stageAfter={}, totalDrawTime={}",
                context.getId(), stage, newStage, totalDrawTime);

        context.write(
                77354,
                buildUpgradedLotteryInfo(totalDrawTime, newStage),
                0);
        context.write(
                77364,
                LotteryPoolUpResp.newBuilder()
                        .setLotteryType(
                                LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                        .setSubId(request.getSubId())
                        .build(),
                0);
    }

    /** NewSkillLotteryPoolConfig 8001-8008 每级升级所需的抽卡次数（等级 1-8）。 */
    private static final int[] NEW_FIGHT_SKILL_STAGE_TIMES = {
            5, 10, 50, 100, 100, 150, 300, 0
    };

    /** 升到 stage+1 级所需的累计抽卡次数（前 stage 级 Times 之和）。 */
    private static int upgradeRequiredDrawTime(int stage) {
        int total = 0;
        for (int i = 0; i < stage && i < NEW_FIGHT_SKILL_STAGE_TIMES.length; i++) {
            total += NEW_FIGHT_SKILL_STAGE_TIMES[i];
        }
        return total;
    }

    private static LotteryInfoResp buildUpgradedLotteryInfo(
            int totalDrawTime, int newStage) {
        NewFightSkillLotteryVo.Builder vo = NewFightSkillLotteryVo.newBuilder()
                .setShowStage(newStage);
        for (int stage = 1; stage < newStage; stage++) {
            vo.addShowStage2DrawCountInfoList(
                    IntegerAndIntegerPairEntry.newBuilder()
                            .setKey(stage)
                            .setValue(NEW_FIGHT_SKILL_STAGE_TIMES[stage - 1]));
        }
        return LotteryInfoResp.newBuilder()
                .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                .setFreeTime(0)
                .setTotalDrawTime(totalDrawTime)
                .setTodayDrawTime(totalDrawTime)
                .setMustRewardNeedTime(-totalDrawTime)
                .setExtraDrawTime(totalDrawTime)
                .setAdvertiseFreeTime(0)
                .setNewFightSkillLotteryVo(vo)
                .build();
    }

    private static long breakRequiredExp(int heroLevel) {
        return PlayerRealmConfig.breakRequiredExp(heroLevel);
    }

    /** 第五抽后的斗技激活、任务和战力同步，协议依据抓包 idx 1749-1761。 */
    private void writeFifthNewFightSkillMilestone(
            IPlayerContext context) {
        activatedSkills(context.getId()).add(80130101L);
        noteLearnedSkill(context.getId(), 80130101, 1, 4);
        pendingStarCredits(context.getId()).add(80130101);
        removeLotteryItem(context.getId(), 10);
        lotteryNextItemIndex.merge(context.getId(), 12, Math::max);
        long sceneUnitId = context.getId() * 1000 + 1;
        context.write(
                50402,
                packUpdate(
                        75004,
                        UpdateItem.newBuilder().setItemIndex(10).build()),
                0);

        SkillVo skill = SkillVo.newBuilder()
                .setBaseId(80130101)
                .setStar(1)
                .setActStar(0)
                .setAwaken(false)
                .setSelElement(0)
                .build();
        context.write(
                75011,
                HeroSkillActResp.newBuilder()
                        .setHeroIndex(0)
                        .setShowTips(true)
                        .setPlayerSkill(PlayerSkillVo.newBuilder()
                                .setBaseId(80130101)
                                .addHeroSkills(PlayerHeroSkillVo.newBuilder()
                                        .setHeroIdx(-1)
                                        .setHeroSkill(skill)))
                        .build(),
                0);
        context.write(
                75049,
                HeroSkillStarTotalMaxHisUpdateResp.newBuilder()
                        .setHeroIndex(0)
                        .setSkillStarTotalMaxHis(4)
                        .build(),
                0);
        context.write(
                75039,
                HeroSkillBaseUpdateResp.newBuilder()
                        .addBaseIds(80130101)
                        .build(),
                0);
        context.write(60751, BattlePassInfoResp.getDefaultInstance(), 0);
        context.write(60751, BattlePassInfoResp.getDefaultInstance(), 0);
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(200011, TaskPhase.FINISHED, 1))
                        .addTaskVos(task(2201006, TaskPhase.FINISHED, 5))
                        .build(),
                0);
        context.write(
                50455,
                HeroStatUpdateResp.newBuilder()
                        .setHeroVo(HeroStatVo.newBuilder()
                                .setHeroIndex(0)
                                .addStats(stat(107002, 33))
                                .addStats(stat(102001, 376.5))
                                .addStats(stat(105001, 1))
                                .addStats(stat(106001, 1))
                                .addStats(stat(103001, 28416.35))
                                .addStats(stat(101001, 1077.64756))
                                .addStats(stat(104001, 100)))
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103011, 28396.041800279432))
                        .addAttrList(attribute(103001, 28416.35))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(101001, 1077.64756))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        grantPlayerStats(context.getId(), 1077.64756, 28416.35);
        context.write(
                52351,
                PlayerFightForceResp.newBuilder()
                        .setPlayerFightForce(16844)
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                75151,
                HeroFightForceResp.newBuilder()
                        .addHeroVoList(HeroFightForceVo.newBuilder()
                                .setHeroIndex(0)
                                .setFightForce(16844))
                        .build(),
                0);
        TaskUpdateResp.Builder fightTasks = TaskUpdateResp.newBuilder();
        for (int taskId : new int[] {
                4002009, 4002004, 4002003, 4002002,
                4002008, 4002007, 4002006, 4002005
        }) {
            fightTasks.addTaskVos(task(taskId, TaskPhase.PROGRESS, 16844));
        }
        context.write(50906, fightTasks.build(), 0);
    }

    private static StringAndDoublePairEntry stringDouble(
            String key,
            double value) {
        return StringAndDoublePairEntry.newBuilder()
                .setKey(key)
                .setValue(value)
                .build();
    }

    @PlayerCmd
    public ChangeSceneResp changeScene(
            IPlayerContext context,
            ChangeSceneReq request) {

        int mapId = request.getMapId();

        if (mapId != 2 && mapId != 3) {
            throw new IllegalArgumentException(
                    "Unsupported scene: " + mapId);
        }

        return buildSceneResponse(mapId, 8);
    }

    @PlayerCmd
    public void finishSceneLoadA(
            IPlayerContext context,
            ChangeSceneFinishAReq request) {
    }

    @PlayerCmd
    public SceneUpdateVisibleResp finishSceneLoadB(
            IPlayerContext context,
            ChangeSceneFinishBReq request) {
        if (request.getMapId() != 2
                && request.getMapId() != 3) {
            throw new IllegalArgumentException(
                    "Unsupported scene: " + request.getMapId());
        }

        context.write(75153, currentHeroShortInfoUpdate(context.getId()), 0);
        writeAllHeroLevelInfo(context);
        tryOpenEighthRealmBagModules(context);
        tryRefreshFirstCharge(context);
        SceneUpdateVisibleResp snapshot = overlayVisibleHeroLevel(
                visibleSnapshot(context.getId()), context.getId());
        if (chapter9TestPlayers.contains(context.getId())) {
            SceneUnitVo player = checkpointPlayerUnit(snapshot.getVisibleList(0), context.getId());
            snapshot = snapshot.toBuilder().setVisibleList(0, player).build();
            context.write(50790, SyncNonSceneHeroVoUpdateResp.newBuilder()
                    .addVoList(checkpointPlayerUnit(buildPlayerUnit(context.getId(), false), context.getId()))
                    .setOperationType(ServerVoUpdateType.ALL).build(), 0);
            writeEquippedActiveSkills(context);
        }
        if (request.getMapId() == 2 && hasPassedChapter(context.getId(), 10200505)) {
            GuidanceState state = guidanceStates.get(context.getId());
            // 乌坦城小怪和 Boss 都走 61971 战报，地图加载不要改成场地战斗。
            if (state == null || isWutanChapter(state.chapterId)) {
                SceneUpdateVisibleResp.Builder response = snapshot.toBuilder();
                response.getVisibleListBuilder(0).getHeroVoBuilder()
                        .setClientDriven(false);
                return response.build();
            }
        }
        return snapshot;
    }

    @PlayerCmd
    public void pauseGuidanceMainMap(
            IPlayerContext context,
            GuidanceMainMapPauseReq request) {
        CombatSession session = CombatSessionRegistry.get(context.getId());
        if (session != null) {
            session.setPaused(request.getPause());
        }
    }

    @PlayerCmd
    public void savePlayerGuide(
            IPlayerContext context,
            PlayerGuideSaveReq request) {
        if (request.getGuideGroup() == 10043 && request.getGuideId() == 1004303) {
            grantNinthTeachingSkill(context);
        }
    }

    /** 自动战斗引导：对应抓包 idx 1466-1469。 */
    @PlayerCmd
    public void openGuidanceAutoSkill(
            IPlayerContext context,
            GuidanceOpenAutoSkillReq request) {
        GuidanceState state = guidanceStates.get(context.getId());
        if (state == null
                || state.chapterId != 10100501
                || state.autoSkillOpened) {
            return;
        }

        state.autoSkillOpened = true;
        context.write(60751, BattlePassInfoResp.getDefaultInstance(), 0);
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(
                                200009,
                                TaskPhase.FINISHED,
                                1))
                        .build(),
                0);
    }

    /** 斗技升星：对应抓包 idx 1422-1431 / 1766-1776 / 6957。 */
    @PlayerCmd
    public void starUpHeroSkill(
            IPlayerContext context,
            HeroSkillStarUpReq request) {
        if (request.getHeroIndex() != 0
                || request.getItem2CostNumsCount() != 1
                || request.getItem2CostNums(0).getValue() < 1) {
            return;
        }

        if (request.getSkillBaseId() == 80140001
                && request.getItem2CostNums(0).getKey() == 80140000) {
            if (!FIRST_SKILL_STAR_PLAYERS.add(context.getId())) {
                return;
            }
            pendingStarCredits(context.getId()).remove(80140001);
            removeLotteryItem(context.getId(), 9);
            writeFirstSkillStarUp(context);
            syncHeroLevelTasks(context);
            return;
        }

        if (request.getSkillBaseId() == 80130011
                && request.getItem2CostNums(0).getKey() == 80130010
                && THIRD_SKILL_STAR_PLAYERS.add(context.getId())) {
            pendingStarCredits(context.getId()).remove(80130011);
            long sceneUnitId = context.getId() * 1000 + 1;
            context.write(
                    50402,
                    packUpdate(
                            75003,
                            UpdateItem.newBuilder().setItemIndex(5).build()),
                    0);
            removeLotteryItem(context.getId(), 5);
            context.write(
                    75046,
                    HeroSkillSkillUpdateResp.newBuilder()
                            .setReason(0)
                            .addPlayerSkillUpdates(PlayerSkillVo.newBuilder()
                                    .setBaseId(80130011)
                                    .addHeroSkills(PlayerHeroSkillVo.newBuilder()
                                            .setHeroIdx(-1)
                                            .setHeroSkill(SkillVo.newBuilder()
                                                    .setBaseId(80130011)
                                                    .setStar(2)
                                                    .setActStar(0)
                                                    .setAwaken(false)
                                                    .setSelElement(0)))
                                    .build())
                            .setUpType(0)
                            .build(),
                    0);
            context.write(
                    75049,
                    HeroSkillStarTotalMaxHisUpdateResp.newBuilder()
                            .setHeroIndex(0)
                            .setSkillStarTotalMaxHis(3)
                            .build(),
                    0);
            writeThirdSkillStarStats(context, sceneUnitId);
            noteLearnedSkill(context.getId(), 80130011, 2, 3);
            syncHeroLevelTasks(context);
            return;
        }

        starUpLotterySkill(context, request);
    }

    /**
     * 纳戒后抽斗技升星。抓包 idx 6957 升 80130101；第 9 关抽到的 80130001
     * 等也走这里。激活时已消耗的那一张允许立刻再升一次。
     */
    private void starUpLotterySkill(
            IPlayerContext context,
            HeroSkillStarUpReq request) {
        int skillBaseId = request.getSkillBaseId();
        if (skillBaseId < 80000000 || skillBaseId >= 90000000) {
            return;
        }
        if (!learnedSkills(context.getId()).contains((long) skillBaseId)) {
            LOGGER.info(
                    "Star-up ignored, skill not learned: player={}, skill={}",
                    context.getId(), skillBaseId);
            return;
        }
        int costKey = (int) request.getItem2CostNums(0).getKey();
        LotteryItemStack stack = consumeLotteryItem(context.getId(), costKey);
        boolean usedCredit = pendingStarCredits(context.getId()).remove(skillBaseId);
        if (stack == null && !usedCredit) {
            LOGGER.info(
                    "Star-up rejected, no fragment: player={}, skill={}, cost={}",
                    context.getId(), skillBaseId, costKey);
            return;
        }
        if (stack != null) {
            context.write(
                    50402,
                    packUpdate(75003, lotteryUpdateItem(stack)),
                    0);
        }
        int newStar = skillStarOf(context.getId(), skillBaseId) + 1;
        int currentMax = skillStarMaxHis.getOrDefault(context.getId(), 0);
        int maxHis = skillBaseId == 80130101 && newStar == 2
                ? Math.max(currentMax, 12)
                : currentMax + 1;
        noteLearnedSkill(context.getId(), skillBaseId, newStar, maxHis);
        context.write(
                75046,
                HeroSkillSkillUpdateResp.newBuilder()
                        .setReason(0)
                        .addPlayerSkillUpdates(PlayerSkillVo.newBuilder()
                                .setBaseId(skillBaseId)
                                .addHeroSkills(PlayerHeroSkillVo.newBuilder()
                                        .setHeroIdx(-1)
                                        .setHeroSkill(SkillVo.newBuilder()
                                                .setBaseId(skillBaseId)
                                                .setStar(newStar)
                                                .setActStar(0)
                                                .setAwaken(false)
                                                .setSelElement(0)))
                                .build())
                        .setUpType(0)
                        .build(),
                0);
        context.write(
                75049,
                HeroSkillStarTotalMaxHisUpdateResp.newBuilder()
                        .setHeroIndex(0)
                        .setSkillStarTotalMaxHis(maxHis)
                        .build(),
                0);
        if (skillBaseId == 80130101
                && grantedAttack.getOrDefault(context.getId(), 0d)
                        <= 1846.5465600000002) {
            writeNinthLotterySkillStarStats(context);
        }
        LOGGER.info(
                "Lottery skill star-up: player={}, skill={}, star={}, maxHis={}",
                context.getId(), skillBaseId, newStar, maxHis);
        syncHeroLevelTasks(context);
    }

    /** 80130101 升星后的战力，对应抓包 idx 6962-6966。 */
    private void writeNinthLotterySkillStarStats(IPlayerContext context) {
        long sceneUnitId = context.getId() * 1000 + 1;
        context.write(
                50455,
                HeroStatUpdateResp.newBuilder()
                        .setHeroVo(HeroStatVo.newBuilder()
                                .setHeroIndex(0)
                                .addStats(stat(107002, 64))
                                .addStats(stat(102001, 728))
                                .addStats(stat(105001, 1))
                                .addStats(stat(106001, 1))
                                .addStats(stat(103001, 47938.799999999996))
                                .addStats(stat(101001, 1846.5465600000002))
                                .addStats(stat(104001, 100)))
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103011, 45192.86666083305))
                        .addAttrList(attribute(103001, 47938.799999999996))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(101001, 1846.5465600000002))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        grantPlayerStats(context.getId(), 1846.5465600000002, 47938.799999999996);
        noteFightForce(context.getId(), 25778);
        context.write(
                52351,
                PlayerFightForceResp.newBuilder()
                        .setPlayerFightForce(25778)
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                75151,
                HeroFightForceResp.newBuilder()
                        .addHeroVoList(HeroFightForceVo.newBuilder()
                                .setHeroIndex(0)
                                .setFightForce(25778))
                        .build(),
                0);
    }

    /**
     * 大境界升阶。九段→一星斗者对应抓包 idx 9582-9600。
     * 客户端只在境界顶格发 75058；重启后内存境界被清掉时也按斗之气顶格处理。
     */
    @PlayerCmd
    public void upgradeHeroStep(
            IPlayerContext context,
            HeroStepUpgradeReq request) {
        long playerId = context.getId();
        int currentLevel = heroLevels.getOrDefault(playerId, 2);
        int currentStage = heroStages.getOrDefault(playerId, 1);
        boolean qiRealmStep = currentStage <= 1 && currentLevel <= 7;
        if (!qiRealmStep
                && !PlayerRealmConfig.atStageCap(currentLevel, currentStage)) {
            LOGGER.info(
                    "Hero step upgrade ignored: player={}, level={}, stage={}",
                    playerId, currentLevel, currentStage);
            return;
        }
        int nextLevel = currentLevel + 1;
        int nextStage = currentStage + 1;
        if (qiRealmStep) {
            nextLevel = 8;
            nextStage = 2;
        }
        long requiredExp = PlayerRealmConfig.expToReach(nextLevel);
        long currentExp = alchemyExpPools.getOrDefault(playerId, 0L);
        if (currentExp < requiredExp && nextLevel != 8) {
            LOGGER.info(
                    "Hero step upgrade ignored, exp short: player={}, "
                            + "level={}, stage={}, exp={}, need={}",
                    playerId, currentLevel, currentStage, currentExp, requiredExp);
            return;
        }
        long remainingExp = Math.max(0L, currentExp - requiredExp);
        alchemyExpPools.put(playerId, remainingExp);

        int heroIndex = request.getHeroIndex();
        heroLevels.put(playerId, nextLevel);
        heroStages.put(playerId, nextStage);
        alchemyLevels.put(playerId, 2);

        context.write(
                75059,
                HeroStepUpgradeResp.newBuilder()
                        .setHeroVo(HeroLevelVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .setLevel(nextLevel)
                                .setStage(nextStage))
                        .build(),
                0);
        context.write(
                75060,
                AlchemyNewExpChangeResp.newBuilder()
                        .setExp(remainingExp)
                        .setLevel(1)
                        .build(),
                0);
        context.write(
                50406,
                RewardResp.newBuilder()
                        .setOperationType(75054)
                        .build(),
                0);
        context.write(
                75074,
                HeroRoadRewardResp.newBuilder().setId(1).build(),
                0);
        context.write(
                75153,
                HeroShortInfoUpdateResp.newBuilder()
                        .setShortInfoVo(HeroShortInfoVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .setCrossHeroShortInfo(
                                        buildHeroShortInfo(playerId)
                                                .toBuilder()
                                                .setLevel(currentLevel)
                                                .setStage(nextStage)))
                        .build(),
                0);
        context.write(
                75153,
                HeroShortInfoUpdateResp.newBuilder()
                        .setShortInfoVo(HeroShortInfoVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .setCrossHeroShortInfo(
                                        currentHeroShortInfo(playerId)))
                        .build(),
                0);
        context.write(
                75065,
                AlchemyNewLevelUpgradeResp.newBuilder()
                        .setLevel(2)
                        .setMakeNum(alchemyMakeTimes.getOrDefault(playerId, 5))
                        .build(),
                0);
        context.write(78602, StrongStatueInfoResp.getDefaultInstance(), 0);
        context.write(
                78657,
                AnimationStateSkillSlotUpdateResp.newBuilder()
                        .addUpdates(AnimationStateSkillSlotUpdateVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .addSkillSlots(AnimationStateSkillSlotVo
                                        .newBuilder()
                                        .setSlotId(1)
                                        .setStar(1)))
                        .build(),
                0);
        context.write(60751, BattlePassInfoResp.getDefaultInstance(), 0);
        context.write(60751, BattlePassInfoResp.getDefaultInstance(), 0);
        context.write(60751, BattlePassInfoResp.getDefaultInstance(), 0);
        TaskUpdateResp.Builder tasks = TaskUpdateResp.newBuilder();
        acceptedMainTasks.remove(playerId, 200025);
        tasks.addTaskVos(task(200025, TaskPhase.FINISHED, 1));
        tasks.addTaskVos(task(4000011, TaskPhase.FINISHED, 1));
        tasks.addTaskVos(task(40001, TaskPhase.PROGRESS, nextLevel));
        context.write(50906, tasks.build(), 0);
        writeAllHeroLevelInfo(context);
        writeFighterRealmStats(
                context,
                heroIndex,
                playerId * 1000 + 1,
                nextLevel);
        LOGGER.info(
                "Hero step upgraded: player={}, level={}, stage={}",
                playerId, nextLevel, nextStage);
        syncHeroLevelTasks(context);
        pushThirdSkillSlotUnlock(context, heroIndex);
    }

    /** 境界之路打开时要的全服人数，缺回包客户端会卡在 RoleStage。 */
    @PlayerCmd
    public void queryRealmInfo(
            IPlayerContext context,
            RealmInfoReq request) {
        context.write(
                76652,
                RealmInfoResp.newBuilder()
                        .setInBattle(false)
                        .addStage2Nums(
                                IntegerAndIntegerPairEntry.newBuilder()
                                        .setKey(1)
                                        .setValue(1))
                        .addStage2Nums(
                                IntegerAndIntegerPairEntry.newBuilder()
                                        .setKey(2)
                                        .setValue(1))
                        .build(),
                0);
        tryOpenEighthRealmBagModules(context);
    }

    /**
     * 服药后尽量往上升。
     * <p>八段 NeedUpStage=0，修为池走 75056、斗之气面板走 75066，现场经常两
     * 个都不点。九段升一星官服要点 75058，本服也经常发不出来。
     */
    private void tryAutoStepUpgradeAfterExp(IPlayerContext context) {
        long playerId = context.getId();
        int currentLevel = heroLevels.getOrDefault(playerId, 2);
        int currentStage = heroStages.getOrDefault(playerId, 1);
        while (!PlayerRealmConfig.needLevelBreak(currentLevel)
                && !PlayerRealmConfig.atStageCap(currentLevel, currentStage)
                && alchemyExpPools.getOrDefault(playerId, 0L)
                        >= PlayerRealmConfig.breakRequiredExp(currentLevel)) {
            LOGGER.info(
                    "Auto hero level upgrade after taking: player={}, level={}",
                    playerId, currentLevel);
            upgradeHeroLevel(
                    context,
                    HeroLevelUpgradeReq.newBuilder().setHeroIndex(0).build());
            currentLevel = heroLevels.getOrDefault(playerId, currentLevel);
            currentStage = heroStages.getOrDefault(playerId, currentStage);
        }
        if (currentLevel != 7
                && !PlayerRealmConfig.atStageCap(currentLevel, currentStage)) {
            return;
        }
        int nextLevel = currentLevel <= 7 ? 8 : currentLevel + 1;
        if (alchemyExpPools.getOrDefault(playerId, 0L)
                < PlayerRealmConfig.expToReach(nextLevel)) {
            return;
        }
        upgradeHeroStep(
                context,
                HeroStepUpgradeReq.newBuilder().setHeroIndex(0).build());
    }

    private void writeAllHeroLevelInfo(IPlayerContext context) {
        long playerId = context.getId();
        context.write(
                75061,
                AllHeroLevelInfoResp.newBuilder()
                        .addHeroVoList(HeroLevelVo.newBuilder()
                                .setHeroIndex(0)
                                .setLevel(heroLevels.getOrDefault(playerId, 1))
                                .setStage(heroStages.getOrDefault(playerId, 1)))
                        .build(),
                0);
    }

    /**
     * NeedUpStage=0 的小级用 75056，例如二星斗者→三星。修为消耗取下一级 Exp。
     */
    @PlayerCmd
    public void upgradeHeroLevel(
            IPlayerContext context,
            HeroLevelUpgradeReq request) {
        long playerId = context.getId();
        int currentLevel = heroLevels.getOrDefault(playerId, 2);
        if (PlayerRealmConfig.needLevelBreak(currentLevel)
                || PlayerRealmConfig.atStageCap(
                        currentLevel,
                        heroStages.getOrDefault(playerId, 1))) {
            return;
        }
        long currentExp = alchemyExpPools.getOrDefault(playerId, 0L);
        long requiredExp = PlayerRealmConfig.breakRequiredExp(currentLevel);
        if (currentExp < requiredExp) {
            LOGGER.info(
                    "Hero level upgrade ignored, exp short: player={}, "
                            + "level={}, exp={}, need={}",
                    playerId, currentLevel, currentExp, requiredExp);
            return;
        }
        int nextLevel = currentLevel + 1;
        long remainingExp = currentExp - requiredExp;
        heroLevels.put(playerId, nextLevel);
        alchemyExpPools.put(playerId, remainingExp);
        int heroIndex = request.getHeroIndex();
        context.write(
                75057,
                HeroLevelUpgradeResp.newBuilder()
                        .setHeroVo(HeroLevelVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .setLevel(nextLevel)
                                .setStage(heroStages.getOrDefault(playerId, 1)))
                        .build(),
                0);
        context.write(
                75060,
                AlchemyNewExpChangeResp.newBuilder()
                        .setExp(remainingExp)
                        .setLevel(alchemyLevels.getOrDefault(playerId,
                                heroStages.getOrDefault(playerId, 1)))
                        .build(),
                0);
        context.write(
                75153,
                HeroShortInfoUpdateResp.newBuilder()
                        .setShortInfoVo(HeroShortInfoVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .setCrossHeroShortInfo(
                                        currentHeroShortInfo(playerId)))
                        .build(),
                0);
        syncHeroLevelTasks(context);
        if (nextLevel >= 8) {
            writeFighterRealmStats(
                    context,
                    heroIndex,
                    playerId * 1000 + 1,
                    nextLevel);
        }
    }

    /**
     * 斗者境界属性。一星斗者用抓包 idx 9597-9600；其后按 Luban Stats+FixNum
     * 加上同一套额外部分，战力按攻击比从 60577 对齐。
     */
    private void writeFighterRealmStats(
            IPlayerContext context,
            int heroIndex,
            long sceneUnitId,
            int level) {
        PlayerRealmConfig.RealmStats stats = PlayerRealmConfig.statsOf(level);
        double combatAtk = PlayerRealmConfig.combatAtk(level);
        double combatDef = PlayerRealmConfig.combatDef(level);
        double combatHp = PlayerRealmConfig.combatHp(level);
        double fightForce = PlayerRealmConfig.fightForce(level);
        context.write(
                50455,
                HeroStatUpdateResp.newBuilder()
                        .setHeroVo(HeroStatVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .addStats(stat(102001, combatDef))
                                .addStats(stat(106001, 1))
                                .addStats(stat(103001, combatHp))
                                .addStats(stat(107001, stats.atkFix))
                                .addStats(stat(101002, stats.atk))
                                .addStats(stat(104001, 100))
                                .addStats(stat(105001, 1))
                                .addStats(stat(108001, stats.defFix))
                                .addStats(stat(102002, stats.def))
                                .addStats(stat(101001, combatAtk))
                                .addStats(stat(147101, 10000))
                                .addStats(stat(103002, stats.hp))
                                .addStats(stat(109001,
                                        level == 8 ? 32720 : stats.hpFix)))
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                52351,
                PlayerFightForceResp.newBuilder()
                        .setPlayerFightForce(fightForce)
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                75151,
                HeroFightForceResp.newBuilder()
                        .addHeroVoList(HeroFightForceVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .setFightForce(fightForce))
                        .build(),
                0);
        if (level == 8) {
            context.write(
                    52355,
                    PlayerFightForceCheckPointChangeResp.newBuilder()
                            .setCheckPointType(1)
                            .setPlayerFightForceBefore(34959)
                            .setPlayerFightForceAfter(60577)
                            .addPlayerStatMapBefore(stringDouble("def", 838))
                            .addPlayerStatMapBefore(
                                    stringDouble("hp", 54483.799999999996))
                            .addPlayerStatMapBefore(
                                    stringDouble("atk", 2093.8881600000004))
                            .addPlayerStatMapAfter(stringDouble("def", 1508))
                            .addPlayerStatMapAfter(stringDouble("hp", 88506.5))
                            .addPlayerStatMapAfter(
                                    stringDouble("atk", 3449.48976))
                            .build(),
                    0);
        }
        grantPlayerStats(context.getId(), combatAtk, combatHp);
        noteFightForce(context.getId(), fightForce);
    }

    /** 狂狮怒罡首次升星，对应官服抓包 idx 1766-1776。 */
    private void writeFirstSkillStarUp(IPlayerContext context) {
        long sceneUnitId = context.getId() * 1000 + 1;
        context.write(
                50402,
                packUpdate(
                        75003,
                        UpdateItem.newBuilder().setItemIndex(9).build()),
                0);
        context.write(
                75046,
                HeroSkillSkillUpdateResp.newBuilder()
                        .setReason(0)
                        .addPlayerSkillUpdates(PlayerSkillVo.newBuilder()
                                .setBaseId(80140001)
                                .addHeroSkills(PlayerHeroSkillVo.newBuilder()
                                        .setHeroIdx(-1)
                                        .setHeroSkill(SkillVo.newBuilder()
                                                .setBaseId(80140001)
                                                .setStar(2)
                                                .setActStar(0)
                                                .setAwaken(false)
                                                .setSelElement(0)))
                                .build())
                        .setUpType(0)
                        .build(),
                0);
        context.write(
                75049,
                HeroSkillStarTotalMaxHisUpdateResp.newBuilder()
                        .setHeroIndex(0)
                        .setSkillStarTotalMaxHis(5)
                        .build(),
                0);
        writeFirstSkillStarStats(context, sceneUnitId);
        noteLearnedSkill(context.getId(), 80140001, 2, 5);
    }

    /** 五段突破后的属性和任务更新，对应抓包 idx 5546-5557。 */
    private void writeFifthLevelBreakUpdates(
            IPlayerContext context,
            int heroIndex,
            long sceneUnitId) {
        context.write(78602, StrongStatueInfoResp.getDefaultInstance(), 0);
        context.write(60751, BattlePassInfoResp.getDefaultInstance(), 0);
        context.write(60751, BattlePassInfoResp.getDefaultInstance(), 0);
        context.write(60751, BattlePassInfoResp.getDefaultInstance(), 0);
        TaskUpdateResp.Builder tasks = TaskUpdateResp.newBuilder();
        if (Integer.valueOf(200013).equals(
                acceptedMainTasks.get(context.getId()))) {
            acceptedMainTasks.remove(context.getId());
            tasks.addTaskVos(task(200013, TaskPhase.FINISHED, 1));
        }
        tasks.addTaskVos(task(30101, TaskPhase.REWARDED, 1));
        tasks.addTaskVos(task(40001, TaskPhase.PROGRESS, 5));
        appendSecondHeroChapterTasks(tasks);
        context.write(50906, tasks.build(), 0);
        secondHeroChapterTasksSent.add(context.getId());
        context.write(
                50455,
                HeroStatUpdateResp.newBuilder()
                        .setHeroVo(HeroStatVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .addStats(stat(102001, 541.5))
                                .addStats(stat(105001, 1))
                                .addStats(stat(108001, 50))
                                .addStats(stat(106001, 1))
                                .addStats(stat(102002, 359))
                                .addStats(stat(103001, 37251.85))
                                .addStats(stat(101001, 1433.88277))
                                .addStats(stat(107001, 100))
                                .addStats(stat(101002, 943))
                                .addStats(stat(103002, 23594))
                                .addStats(stat(104001, 100))
                                .addStats(stat(109001, 2500)))
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103011, 36093.11430762132))
                        .addAttrList(attribute(103001, 37251.85))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(101001, 1433.88277))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        grantPlayerStats(context.getId(), 1433.88277, 37251.85);
        context.write(
                52351,
                PlayerFightForceResp.newBuilder()
                        .setPlayerFightForce(21479)
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                75151,
                HeroFightForceResp.newBuilder()
                        .addHeroVoList(HeroFightForceVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .setFightForce(21479))
                        .build(),
                0);
        context.write(
                52355,
                PlayerFightForceCheckPointChangeResp.newBuilder()
                        .setCheckPointType(2)
                        .setPlayerFightForceBefore(17972)
                        .setPlayerFightForceAfter(21479)
                        .addPlayerStatMapBefore(stringDouble("def", 426.5))
                        .addPlayerStatMapBefore(stringDouble("hp", 30991.35))
                        .addPlayerStatMapBefore(
                                stringDouble("atk", 1189.0766800000001))
                        .addPlayerStatMapAfter(stringDouble("def", 541.5))
                        .addPlayerStatMapAfter(stringDouble("hp", 37251.85))
                        .addPlayerStatMapAfter(stringDouble("atk", 1433.88277))
                        .build(),
                0);
    }

    /**
     * 八段突破后修为溢出，官服同包升到九段。对应抓包 idx 6712-6734：
     * 解锁功法/斗技槽，完成 200021，并刷新 25603 战力。
     */
    private void writeEighthLevelBreakUpdates(
            IPlayerContext context,
            int heroIndex,
            long sceneUnitId) {
        if (Integer.valueOf(200021).equals(
                acceptedMainTasks.get(context.getId()))) {
            acceptedMainTasks.remove(context.getId());
        }
        bagModulesOpened.add(context.getId());
        context.write(
                50852,
                ModuleNewOpenResp.newBuilder()
                        .addOpens(QUESTIONNAIRE_MODULE_ID)
                        .addOpens(ASCEND_MODULE_ID)
                        .addOpens(BAG_MODULE_ID)
                        .addOpens(BAG_COMPOSE_MODULE_ID)
                        .addOpens(4303)
                        .build(),
                0);
        context.write(
                50384,
                cumulateLoginDays(new int[] {
                        1600, 1601, 102, 1510, 4007, 103, 1608, 4008,
                        4201, 4202, 107, 4203, 4204, 112, 4304, 4401,
                        4404, 3701, 4411, 42011, 3101, 3102, 4415
                }),
                0);
        context.write(78602, StrongStatueInfoResp.getDefaultInstance(), 0);
        context.write(60751, BattlePassInfoResp.getDefaultInstance(), 0);
        pushThirdSkillSlotUnlock(context, heroIndex);
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(200021, TaskPhase.FINISHED, 1))
                        .addTaskVos(task(40001, TaskPhase.PROGRESS, 7))
                        .build(),
                0);
        context.write(
                50455,
                HeroStatUpdateResp.newBuilder()
                        .setHeroVo(HeroStatVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .addStats(stat(102001, 720.5))
                                .addStats(stat(105001, 1))
                                .addStats(stat(106001, 1))
                                .addStats(stat(102002, 495))
                                .addStats(stat(103001, 47507.549999999996))
                                .addStats(stat(101001, 1829.6459400000003))
                                .addStats(stat(101002, 1214))
                                .addStats(stat(103002, 30362))
                                .addStats(stat(104001, 100)))
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103011, 45577.31865071423))
                        .addAttrList(attribute(103001, 47507.549999999996))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(101001, 1829.6459400000003))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        grantPlayerStats(context.getId(), 1829.6459400000003, 47507.549999999996);
        context.write(
                52351,
                PlayerFightForceResp.newBuilder()
                        .setPlayerFightForce(25603)
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                75151,
                HeroFightForceResp.newBuilder()
                        .addHeroVoList(HeroFightForceVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .setFightForce(25603))
                        .build(),
                0);
        context.write(
                52355,
                PlayerFightForceCheckPointChangeResp.newBuilder()
                        .setCheckPointType(2)
                        .setPlayerFightForceBefore(22475)
                        .setPlayerFightForceAfter(25603)
                        .addPlayerStatMapBefore(stringDouble("def", 584.5))
                        .addPlayerStatMapBefore(stringDouble("hp", 39724.35))
                        .addPlayerStatMapBefore(
                                stringDouble("atk", 1529.7275300000003))
                        .addPlayerStatMapAfter(stringDouble("def", 720.5))
                        .addPlayerStatMapAfter(
                                stringDouble("hp", 47507.549999999996))
                        .addPlayerStatMapAfter(
                                stringDouble("atk", 1829.6459400000003))
                        .build(),
                0);
        TaskUpdateResp.Builder powerTasks = TaskUpdateResp.newBuilder();
        for (int taskId : new int[] {
                4002009, 4002004, 4002003, 4002002,
                4002008, 4002007, 4002006, 4002005
        }) {
            powerTasks.addTaskVos(task(taskId, TaskPhase.PROGRESS, 25603));
        }
        context.write(50906, powerTasks.build(), 0);
    }

    /**
     * 八段（level≥6）解锁 HUD 包裹。官服 idx 6712 在完成 200021 时开
     * 3701/4203/3101/3102；本服玩家常停在八段不溢出，不能只等九段包。
     * 4203 飞升仍按表 Level=7，九段及以后才开。
     */
    private void tryOpenEighthRealmBagModules(IPlayerContext context) {
        long playerId = context.getId();
        int level = heroLevels.getOrDefault(playerId, 2);
        if (level < THIRD_SKILL_SLOT_UNLOCK_LEVEL) {
            return;
        }
        if (!bagModulesOpened.add(playerId)) {
            return;
        }
        ModuleNewOpenResp.Builder opens = ModuleNewOpenResp.newBuilder()
                .addOpens(QUESTIONNAIRE_MODULE_ID)
                .addOpens(BAG_MODULE_ID)
                .addOpens(BAG_COMPOSE_MODULE_ID);
        if (level >= 7) {
            opens.addOpens(ASCEND_MODULE_ID);
        }
        context.write(50852, opens.build(), 0);
        context.write(
                50384,
                cumulateLoginDays(new int[] {
                        1600, 1601, 102, 1510, 4007, 103, 1608, 4008,
                        4201, 4202, 107, 4203, 4204, 112, 4304, 4401,
                        4404, 3701, 4411, 42011, 3101, 3102, 4415
                }),
                0);
        LOGGER.info(
                "Opened bag modules: player={}, level={}, opens={}",
                playerId, level, opens.getOpensList());
    }

    /** 官服抓包 idx 15：空物品背包快照，客户端 BagModel 才能初始化。 */
    public void writeInitialPackInfo(IPlayerContext context) {
        context.write(
                50401,
                PackInfoResp.newBuilder()
                        .addPacks(PackUpdateVo.newBuilder().setPackType(1))
                        .build(),
                0);
    }

    @PlayerCmd
    public void updateHeroSkillSchema(
            IPlayerContext context,
            HeroSkillSchemaUpdateReq request) {
        if (request.getSkillsCount() == 0) {
            return;
        }

        // 第二次装配：槽位 2001 装上 80130011（对应抓包 idx 1109-1120）
        if (request.getHeroIndex() == 0
                && request.getReqSource() == 1
                && request.getSkillsCount() == 2
                && request.getSkills(0).getKey() == 1001
                && request.getSkills(0).getValue() == 80140001
                && request.getSkills(1).getKey() == 2001
                && request.getSkills(1).getValue() == 80130011) {
            persistSkillSchema(context.getId(), request);
            writeSecondSkillEquip(context);
            return;
        }

        if (request.getHeroIndex() == 0
                && request.getReqSource() == 1
                && request.getSkillsCount() == 1
                && request.getSkills(0).getKey() == 1001
                && request.getSkills(0).getValue() == 80140001) {
            persistSkillSchema(context.getId(), request);
            IntegerAndIntegerPairEntry learnedSkill =
                    IntegerAndIntegerPairEntry.newBuilder()
                            .setKey(1001)
                            .setValue(80140001)
                            .build();
            context.write(
                    75005,
                    HeroSkillSchemaUpdateResp.newBuilder()
                            .setHeroIndex(0)
                            .setSchema(HeroSkillSchemaVo.newBuilder()
                                    .setId(1)
                                    .addSlot2SkillBaseIds(learnedSkill)
                                    .setActiveTime(schemaActiveTime(context.getId()))
                                    .setActive(true))
                            .setReqSource(1)
                            .build(),
                    0);
            context.write(
                    75015,
                    HeroSkillSlotUpdateResp.newBuilder()
                            .setHeroIndex(0)
                            .addSlots(HeroSkillSlotVo.newBuilder()
                                    .setId(1001)
                                    .setLv(0)
                                    .setMasterLv(0)
                                    .setAnyOnSkill(true))
                            .build(),
                    0);
            context.write(60751, ByteString.EMPTY, 0);
            context.write(
                    50906,
                    TaskUpdateResp.newBuilder()
                            .addTaskVos(task(
                                    200002,
                                    TaskPhase.FINISHED,
                                    1))
                            .build(),
                    0);
            writeLearnedSkillStats(context);
            return;
        }

        persistSkillSchema(context.getId(), request);
        syncYanfenEquipped(context.getId(), request);
        IntegerAndIntegerPairEntry thirdSlot =
                schemaSlot(request, THIRD_SKILL_SLOT_ID);
        if (thirdSlot != null && thirdSlot.getValue() > 0) {
            writeThirdSlotEquip(context, request);
            maybeWriteBlowPalmEquipStats(context, request);
            return;
        }
        writeSkillSchemaEcho(context, request);
        maybeWriteBlowPalmEquipStats(context, request);
        LOGGER.info(
                "Hero skill schema replaced: player={}, heroIndex={}, "
                        + "reqSource={}, slots={}",
                context.getId(),
                request.getHeroIndex(),
                request.getReqSource(),
                request.getSkillsCount());
    }

    private void persistSkillSchema(
            long playerId, HeroSkillSchemaUpdateReq request) {
        SkillSchemaState state = heroSkillSchemas.computeIfAbsent(
                playerId, ignored -> new SkillSchemaState());
        if (state.activeTimeSeconds == 0) {
            state.activeTimeSeconds = System.currentTimeMillis() / 1000;
        }
        state.heroIndex = request.getHeroIndex();
        state.slots.clear();
        List<IntegerAndIntegerPairEntry> slots =
                new ArrayList<>(request.getSkillsList());
        slots.sort(Comparator.comparingInt(
                IntegerAndIntegerPairEntry::getKey).reversed());
        state.slots.addAll(slots);
    }

    private long schemaActiveTime(long playerId) {
        SkillSchemaState state = heroSkillSchemas.get(playerId);
        if (state != null && state.activeTimeSeconds > 0) {
            return state.activeTimeSeconds;
        }
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 斗技替换只回显 75005，对应抓包 idx 1963（80128011）与 idx 5859（100001）。
     * 槽位按 key 降序，与官服 slot2SkillBaseIds 顺序一致。
     */
    private void writeSkillSchemaEcho(
            IPlayerContext context,
            HeroSkillSchemaUpdateReq request) {
        HeroSkillSchemaVo.Builder schema = HeroSkillSchemaVo.newBuilder()
                .setId(1)
                .setActiveTime(schemaActiveTime(context.getId()))
                .setActive(true);
        SkillSchemaState state = heroSkillSchemas.get(context.getId());
        List<IntegerAndIntegerPairEntry> slots = state != null
                ? state.slots
                : request.getSkillsList();
        for (IntegerAndIntegerPairEntry slot : slots) {
            schema.addSlot2SkillBaseIds(slot);
        }
        context.write(
                75005,
                HeroSkillSchemaUpdateResp.newBuilder()
                        .setHeroIndex(request.getHeroIndex())
                        .setSchema(schema)
                        .setReqSource(request.getReqSource())
                        .build(),
                0);
    }

    private static boolean schemaHasSlot(
            HeroSkillSchemaUpdateReq request, int slotId, int skillBaseId) {
        for (IntegerAndIntegerPairEntry slot : request.getSkillsList()) {
            if (slot.getKey() == slotId && slot.getValue() == skillBaseId) {
                return true;
            }
        }
        return false;
    }

    private static IntegerAndIntegerPairEntry schemaSlot(
            HeroSkillSchemaUpdateReq request, int slotId) {
        for (IntegerAndIntegerPairEntry slot : request.getSkillsList()) {
            if (slot.getKey() == slotId) {
                return slot;
            }
        }
        return null;
    }

    /**
     * 血脉槽 3 装上任意已有斗技。75005 按 2001/1001/3001 回显，75015 点亮
     * anyOnSkill；81751 下发第三格技能对应的战斗技能。焰分噬浪尺是第 9 关
     * 给的技能，只有方案里真的装着 80128001 时才走火元素和抓包战力。
     */
    private void writeThirdSlotEquip(
            IPlayerContext context,
            HeroSkillSchemaUpdateReq request) {
        long playerId = context.getId();
        int heroIndex = request.getHeroIndex();
        HeroSkillSchemaVo.Builder schema = HeroSkillSchemaVo.newBuilder()
                .setId(1)
                .setActiveTime(schemaActiveTime(playerId))
                .setActive(true);
        int[] slotOrder = { 2001, 1001, THIRD_SKILL_SLOT_ID };
        for (int slotId : slotOrder) {
            IntegerAndIntegerPairEntry slot = schemaSlot(request, slotId);
            if (slot != null) {
                schema.addSlot2SkillBaseIds(slot);
            }
        }
        context.write(
                75005,
                HeroSkillSchemaUpdateResp.newBuilder()
                        .setHeroIndex(heroIndex)
                        .setSchema(schema)
                        .setReqSource(request.getReqSource())
                        .build(),
                0);
        context.write(
                75015,
                HeroSkillSlotUpdateResp.newBuilder()
                        .setHeroIndex(heroIndex)
                        .addSlots(HeroSkillSlotVo.newBuilder()
                                .setId(THIRD_SKILL_SLOT_ID)
                                .setLv(0)
                                .setMasterLv(0)
                                .setAnyOnSkill(true))
                        .build(),
                0);
        boolean yanfenEquipped =
                schemaHasSkill(request, YANFEN_SKILL_BASE_ID);
        int skillElement = yanfenEquipped ? SKILL_ELEMENT_FIRE : 0;
        FightSchemaHeroInfoVo.Builder heroInfo = FightSchemaHeroInfoVo.newBuilder()
                .setIndex(0)
                .setJobId(1001)
                .setSubId(1)
                .setSkillElementEnum(skillElement);
        for (IntegerAndIntegerPairEntry slot : schema.getSlot2SkillBaseIdsList()) {
            heroInfo.addSkillInfo(slot.getValue());
        }
        context.write(
                79565,
                FightSchemaInfoResp.newBuilder()
                        .setCurrentSchema(1)
                        .addFightSchemaVos(FightSchemaVo.newBuilder()
                                .setId(1)
                                .setUnlock(true)
                                .setFightSchemaInfo(FightSchemaInfo.newBuilder()
                                        .addHeroInfoVos(heroInfo)))
                        .setChangeTime(0)
                        .build(),
                0);
        if (yanfenEquipped) {
            writeYanfenEquipExtras(context, heroIndex);
        }
        writeEquippedActiveSkills(context);
        LOGGER.info(
                "Third bloodline slot equipped: player={}, skill={}, yanfen={}",
                playerId,
                schemaSlot(request, THIRD_SKILL_SLOT_ID).getValue(),
                yanfenEquipped);
    }

    private void writeYanfenEquipExtras(
            IPlayerContext context, int heroIndex) {
        long playerId = context.getId();
        long playerUnitId = playerId * 1000 + 1;
        context.write(
                78073,
                HeroSkillJobElementUpdateResp.newBuilder()
                        .addJobSkillElements(HeroJobSkillElementVo.newBuilder()
                                .setJob(1001)
                                .setSkillElement(SKILL_ELEMENT_FIRE))
                        .build(),
                0);
        context.write(
                75048,
                HeroSkillElementUpdateResp.newBuilder()
                        .setHeroIndex(heroIndex)
                        .setSkillElement(SKILL_ELEMENT_FIRE)
                        .build(),
                0);
        context.write(
                75153,
                HeroShortInfoUpdateResp.newBuilder()
                        .setShortInfoVo(HeroShortInfoVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .setCrossHeroShortInfo(
                                        currentHeroShortInfo(playerId)))
                        .build(),
                0);
        context.write(
                50455,
                HeroStatUpdateResp.newBuilder()
                        .setHeroVo(HeroStatVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .addStats(stat(107002, 104))
                                .addStats(stat(102001, 838))
                                .addStats(stat(105001, 1))
                                .addStats(stat(106001, 1))
                                .addStats(stat(103001, 54483.799999999996))
                                .addStats(stat(101001, 2093.8881600000004))
                                .addStats(stat(194101, 20))
                                .addStats(stat(104001, 100))
                                .addStats(stat(109001, 2720)))
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103011, 43979.09950070799))
                        .addAttrList(attribute(103001, 54483.799999999996))
                        .setTargetId(playerUnitId)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(101001, 2093.8881600000004))
                        .setTargetId(playerUnitId)
                        .build(),
                0);
        grantPlayerStats(playerId, 2093.8881600000004, 54483.799999999996);
        noteFightForce(playerId, 34959);
        context.write(
                52351,
                PlayerFightForceResp.newBuilder()
                        .setPlayerFightForce(34959)
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                75151,
                HeroFightForceResp.newBuilder()
                        .addHeroVoList(HeroFightForceVo.newBuilder()
                                .setHeroIndex(heroIndex)
                                .setFightForce(34959))
                        .build(),
                0);
    }

    /**
     * 81751：吹火掌（若已装）+ 第三格技能。抓包 idx 9044 在第三格是焰分时
     * 为 10150210100/80130001 与 10150310100/80128001。
     */
    private void writeEquippedActiveSkills(IPlayerContext context) {
        SkillSchemaState state = heroSkillSchemas.get(context.getId());
        if (state == null || state.slots.isEmpty()) {
            return;
        }
        LinkedHashMap<Long, Integer> mapped = new LinkedHashMap<>();
        int thirdSkill = 0;
        boolean hasBlowPalm = false;
        for (IntegerAndIntegerPairEntry slot : state.slots) {
            if (slot.getValue() == SECOND_SKILL_BASE_ID) {
                hasBlowPalm = true;
            }
            if (slot.getKey() == THIRD_SKILL_SLOT_ID) {
                thirdSkill = slot.getValue();
            }
        }
        if (hasBlowPalm) {
            mapped.put(SECOND_FIGHT_SKILL_ID, SECOND_SKILL_BASE_ID);
        }
        long thirdFightId = fightSkillIdForBase(thirdSkill);
        if (thirdFightId > 0) {
            mapped.put(thirdFightId, thirdSkill);
        }
        if (mapped.isEmpty()) {
            return;
        }
        ActiveSkillChangeResp.Builder resp = ActiveSkillChangeResp.newBuilder();
        for (Map.Entry<Long, Integer> entry : mapped.entrySet()) {
            resp.addBuffTrigger2BaseSkill(LongAndIntegerPairEntry.newBuilder()
                    .setKey(entry.getKey())
                    .setValue(entry.getValue()));
            resp.addCanUseSkill(entry.getKey());
        }
        context.write(81751, resp.build(), 0);
    }

    private void syncYanfenEquipped(
            long playerId, HeroSkillSchemaUpdateReq request) {
        if (schemaHasSkill(request, YANFEN_SKILL_BASE_ID)) {
            YANFEN_SLOT_PLAYERS.add(playerId);
        } else {
            YANFEN_SLOT_PLAYERS.remove(playerId);
        }
    }

    private static boolean schemaHasSkill(
            HeroSkillSchemaUpdateReq request, int skillBaseId) {
        for (IntegerAndIntegerPairEntry slot : request.getSkillsList()) {
            if (slot.getValue() == skillBaseId) {
                return true;
            }
        }
        return false;
    }

    private static long fightSkillIdForBase(int skillBaseId) {
        switch (skillBaseId) {
            case YANFEN_SKILL_BASE_ID:
                return YANFEN_FIGHT_SKILL_ID;
            case CLAW_SKILL_BASE_ID:
                return CLAW_FIGHT_SKILL_ID;
            case SECOND_SKILL_BASE_ID:
                return SECOND_FIGHT_SKILL_ID;
            case 80130011:
                return 10151110100L;
            default:
                return 0;
        }
    }

    @PlayerCmd
    public void enterGuidanceMainMapMonster(
            IPlayerContext context,
            GuidanceMainMapMonsterEnterReq request) {
        GuidanceState state = guidanceStates.get(context.getId());
        int chapterId = request.getChapterId();
        if (chapterId == 0 && state != null) {
            ChapterConfig.Chapter current = ChapterConfig.get(state.chapterId);
            chapterId = state.fightResultSent
                            && current != null
                            && isSixthWave(state.chapterId)
                    ? current.getNextChapterId()
                    : state.chapterId;
        }
        ChapterConfig.Chapter chapter =
                ChapterConfig.get(chapterId);

        if (chapter == null) {
            throw new IllegalArgumentException(
                    "Unsupported guidance chapter: "
                            + chapterId);
        }

        long playerUnitId = context.getId() * 1000 + 1;
        boolean preload = state != null
                && !state.fightResultSent
                && isSixthWave(state.chapterId)
                && ChapterConfig.get(state.chapterId).getNextChapterId()
                        == chapterId;

        /*
         * 客户端在战斗点里会把同一波重复请求一次（本服日志里每一波都是两条
         * 61973，间隔 0.3~3.5 秒）。抓包 idx 1960 首次进第一波后，idx 1973 又报了
         * 一次同样的 10200101，官服只回了一条 50801，没有重发 50756、也没有把怪
         * 拉回满血。所以重复进入必须复用当前实例，只补玩家攻击属性。
         *
         * 章节号对上还不够：家族试炼回来后客户端仍报当前波（本服 11:22:26
         * 的 10200102），但战斗点 z 已经回到上一波（22.93）。只看章节号会当成
         * 重进，怪留在 67.7，人站在空地。战斗点变了就必须忘掉旧实例。
         */
        WaveInstance current = currentWaves.get(context.getId());
        boolean rejoin = !preload
                && current != null
                && current.chapterId == chapterId
                && current.spawnedAt(request.getZ())
                && state != null
                && state.chapterId == chapterId
                && !state.fightResultSent
                && CombatSessionRegistry.get(context.getId()) != null;

        if (state == null
                || (!preload && state.chapterId != chapterId)
                || state.fightResultSent) {
            state = new GuidanceState(chapterId);
            guidanceStates.put(context.getId(), state);
        }

        // 客户端自己算出下一个战斗点的坐标；魔兽山脉的跳台段 y 会抬到 7 左右。
        LOGGER.info(
                "Chapter enter: player={}, chapter={}, reqChapter={}, "
                        + "x={}, y={}, z={}, preload={}, rejoin={}",
                context.getId(),
                chapterId,
                request.getChapterId(),
                request.getX(),
                request.getY(),
                request.getZ(),
                preload,
                rejoin);

        if (chapterId >= 10200101) {
            tryFinishAcceptedMainTask(context, 200012, 1);
        }
        tryRefreshFirstCharge(context);

        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(
                                101001,
                                playerAttack(
                                        context.getId(),
                                        chapterId)))
                        .setAttackId(0)
                        .setTargetId(playerUnitId)
                        .setHitId(0)
                        .build(),
                0);

        if (rejoin) {
            return;
        }

        if (!preload) {
            WaveInstance leftover = currentWaves.remove(context.getId());
            if (leftover != null) {
                LOGGER.info(
                        "Forget leftover wave: player={}, chapter={}, "
                                + "firstUnitId={}, spawnZ={}, reqChapter={}, reqZ={}",
                        context.getId(),
                        leftover.chapterId,
                        leftover.firstUnitId,
                        leftover.spawnZ,
                        chapterId,
                        request.getZ());
                forgetWave(context, leftover);
            }
        }

        WaveInstance wave = takeWaveInstance(context, chapter, request);

        // 第六关会提前请求下一波怪物快照；预加载不能覆盖当前战斗会话。
        if (preload) {
            preloadedWaves.put(context.getId(), wave);
            return;
        }

        currentWaves.put(context.getId(), wave);

        // 第六关由首次出手按抓包 idx 1983/1984/2005 分段下发遇敌提示。
        if (!chapter.isBoss()
                && !isSixthWave(chapter.getChapterId())
                && wave.monsterCount() > 0) {
            context.write(
                    50793,
                    WarningResp.newBuilder()
                            .setId(wave.unitIdAt(0))
                            .build(),
                    0);
        }

        startCombatSession(context, chapterId, wave.snapshots, request.getZ());
    }

    /**
     * 取到本次请求要用的波次实例：能复用预加载那一批就复用，否则新发一批 ID。
     *
     * <p>抓包 idx 2183 玩家真正走进预加载过的第二波时，官服没有再下发 50756，
     * 直接沿用 idx 1976-1978 那三只怪。所以只有<b>新建</b>实例才下发快照；
     * 复用预加载实例时保持沉默，客户端手里的对象就是这批怪。
     *
     * <p>预加载的章节对得上、但战斗点 z 换了（循环回第一波时客户端会带着新的
     * z 再请求同一个章节号），那批怪就站在旧点位上，不能复用：先让客户端遗忘，
     * 再按新坐标重新发一批。
     */
    private WaveInstance takeWaveInstance(
            IPlayerContext context,
            ChapterConfig.Chapter chapter,
            GuidanceMainMapMonsterEnterReq request) {

        WaveInstance preloaded = preloadedWaves.remove(context.getId());
        if (preloaded != null
                && preloaded.chapterId == chapter.getChapterId()
                && preloaded.spawnedAt(request.getZ())) {
            LOGGER.info(
                    "Reuse preloaded wave: player={}, chapter={}, firstUnitId={}",
                    context.getId(),
                    preloaded.chapterId,
                    preloaded.firstUnitId);
            return preloaded;
        }
        if (preloaded != null) {
            LOGGER.info(
                    "Drop stale preloaded wave: player={}, chapter={}, "
                            + "firstUnitId={}, spawnZ={}, reqChapter={}, reqZ={}",
                    context.getId(),
                    preloaded.chapterId,
                    preloaded.firstUnitId,
                    preloaded.spawnZ,
                    chapter.getChapterId(),
                    request.getZ());
            forgetWave(context, preloaded);
        }

        long firstUnitId = allocateWaveUnitIdBase(context.getId());
        WaveInstance wave = new WaveInstance(
                chapter.getChapterId(),
                firstUnitId,
                request.getZ(),
                monsterSnapshots(chapter, request, firstUnitId));
        for (SceneUpdateVisibleResp monster : wave.snapshots) {
            context.write(50756, monster, 0);
        }
        return wave;
    }

    /**
     * 玩家当前波次实例里第 index 只怪的场景单位 ID；没有实例时返回 0。
     *
     * <p>包级可见：协议测试要用真实发出去的那批 ID 构造击杀与技能请求，
     * 不能再按公式硬算。
     */
    long waveUnitIdAt(long playerId, int index) {
        WaveInstance wave = currentWaves.get(playerId);
        return wave == null || index >= wave.monsterCount()
                ? 0
                : wave.unitIdAt(index);
    }

    /**
     * 让客户端遗忘该玩家当前波与已预加载波的全部怪物，并丢掉这两个实例。
     *
     * <p>玩家点「来打我噻」重置战斗点时，场上和已预加载的怪都要清干净；
     * 留着不清，下一轮循环走到同一章节号时就会撞上这些孤儿对象。
     */
    private void forgetPlayerWaves(IPlayerContext context) {
        WaveInstance current = currentWaves.remove(context.getId());
        if (current != null) {
            forgetWave(context, current);
        }
        WaveInstance preloaded = preloadedWaves.remove(context.getId());
        if (preloaded != null) {
            forgetWave(context, preloaded);
        }
    }

    /** 让客户端遗忘一个波次实例的全部怪物。 */
    private static void forgetWave(
            IPlayerContext context,
            WaveInstance wave) {
        for (int i = 0; i < wave.monsterCount(); i++) {
            context.write(
                    50757,
                    SceneForgetVisibleResp.newBuilder()
                            .addForgetIds(wave.unitIdAt(i))
                            .build(),
                    0);
        }
    }

    /**
     * 为当前引导战创建并绑定战斗会话，随后启动怪物普攻调度。
     *
     * <p>只有在客户端确实生成了怪物（下发过 50756 快照）时才创建，避免无怪
     * 空转。会话按玩家 ID 注册到 {@link CombatSessionRegistry}，由
     * {@link CombatScheduler} 定时推进——只依赖 {@link IPlayerContext} 接口，
     * 不假设具体的 context 实现类型。
     */
    private void startCombatSession(
            IPlayerContext context,
            int chapterId,
            List<SceneUpdateVisibleResp> monsters,
            float battleCenterZ) {

        CombatSession session = new CombatSession(
                chapterId,
                new CombatUnit(
                        context.getId() * 1000 + 1,
                        playerAttack(context.getId(), chapterId),
                        restoreHp(context.getId(), chapterId),
                        0,
                        0,
                        -15));
        session.setBattleCenterZ(battleCenterZ);

        if (chapterId == 10200405) {
            SceneUnitVo.Builder playerSnapshot = buildPlayerUnit(context.getId(), true)
                    .toBuilder();
            playerSnapshot.getHeroVoBuilder().setCrossHeroShortInfo(
                    currentHeroShortInfo(context.getId()));
            if (chapter9TestPlayers.contains(context.getId())) {
                playerSnapshot = checkpointPlayerUnit(playerSnapshot.build(), context.getId()).toBuilder();
            }
            session.setNinthBossPlayerSnapshot(playerSnapshot.build());
        }

        for (SceneUpdateVisibleResp monster : monsters) {
            if (monster.getVisibleListCount() == 0) {
                continue;
            }
            session.addMonster(
                    CombatSessionFactory.monsterUnit(
                            monster.getVisibleList(0)));
        }

        /*
         * 上一波的会话可能还有在途的 CombatTick。先停掉，新会话绑定后旧 tick
         * 既停在自己的会话上（见 CombatTickProcessor 的会话比对），也不会再
         * 续排下一次调度。
         */
        CombatSession previous = CombatSessionRegistry.get(context.getId());
        if (previous != null && previous != session) {
            previous.deactivate();
        }

        CombatSessionRegistry.bind(context.getId(), session);
        LOGGER.info(
                "Combat session created: player={}, chapterId={}, monsters={}",
                context.getId(),
                chapterId,
                session.getMonsters().size());
        writeEquippedActiveSkills(context);
        CombatScheduler.start(context);
    }

    @PlayerCmd
    public void startGuidanceMainMapFight(
            IPlayerContext context,
            GuidanceMainMapStartFightReq request) {
        CombatSession session = CombatSessionRegistry.get(context.getId());
        if (session != null) {
            session.updatePlayerPosition(
                    request.getX(),
                    request.getY(),
                    request.getZ());
        }

    }

    /**
     * 乌坦城战报开打。对应抓包 idx 9554→9556：客户端发 61962，官服回 61971
     *（type=1001、fin/end=true、result=0），随后按配置模板 ID 上报 61964。
     */
    @PlayerCmd
    public void startMainMapFight(
            IPlayerContext context,
            MainMapStartFightReq request) {
        ChapterConfig.Chapter chapter =
                ChapterConfig.get(request.getMainMapChapterId());
        if (chapter == null || !isWutanChapter(chapter.getChapterId())) {
            return;
        }
        if (chapter.getChapterId() >= 10300601
                && reputationLevels.getOrDefault(context.getId(), 0) < 2) {
            LOGGER.info("Chapter locked by reputation: player={}, chapter={}", context.getId(), chapter.getChapterId());
            return;
        }
        if (chapter.getChapterId() >= 10400101 && reputationLevels.getOrDefault(context.getId(), 0) < 3) return;

        if (chapter.isBoss() && chapter.getChapterId() >= 10300505) {
            int firstWave = chapter.getChapterId() - 4;
            GuidanceState current = guidanceStates.get(context.getId());
            boolean selectedBoss = current != null && current.chapterId == chapter.getChapterId();
            if (!selectedBoss && bossUnlockedWave.getOrDefault(context.getId(), 0) != firstWave) {
                LOGGER.warn("Boss request before three waves cleared: player={}, chapter={}",
                        context.getId(), chapter.getChapterId());
                return;
            }
        }

        CombatSessionRegistry.clear(context.getId());
        GuidanceState state = new GuidanceState(chapter.getChapterId());
        guidanceStates.put(context.getId(), state);
        if (chapter.getChapterId() == 10300101) {
            tryFinishAcceptedMainTask(context, 200026, 1);
        }
        if (chapter.getChapterId() >= 10300601) {
            later(context.getId()).arrivedSixteen = true;
            refreshChapter17Tasks(context);
        }
        tryRefreshFirstCharge(context);

        /*
         * 战报 50804 的坐标必须跟着 61962 的战斗点走。乌坦城同一波会在城里
         * 多个 Location 重复打（抓包 idx 9554 z≈28.7，idx 9641 z≈121.3）。
         * 以前用默认 (0,0,0) 生成时间轴，怪刷在出生点，人站在空战斗点。
         */
        GuidanceMainMapMonsterEnterReq enter =
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(chapter.getChapterId())
                        .setX(request.getX())
                        .setY(request.getY())
                        .setZ(request.getZ())
                        .build();
        long firstUnitId = allocateWaveUnitIdBase(context.getId());
        List<SceneUpdateVisibleResp> monsters = monsterSnapshots(
                chapter, enter, firstUnitId);
        LOGGER.info(
                "Wutan battle log: player={}, chapter={}, monsters={}, "
                        + "x={}, y={}, z={}",
                context.getId(),
                chapter.getChapterId(),
                monsters.size(),
                request.getX(),
                request.getY(),
                request.getZ());
        context.write(
                61971,
                WutanBattleLog.startFightResp(
                        context,
                        chapter,
                        monsters,
                        request.getX(),
                        request.getY(),
                        request.getZ()),
                0);
        if (chapter.getMonsterCount() == 0) {
            state.fightResultSent = true;
        }
    }

    @PlayerCmd
    public void useGuidanceSkill(
            IPlayerContext context,
            GuidanceUseSkillReq request) {
        CombatSession session = CombatSessionRegistry.get(context.getId());
        if (session != null) {
            session.updatePlayerPosition(
                    request.getX(),
                    request.getY(),
                    request.getZ());
        }

        GuidanceState state = guidanceStates.get(context.getId());
        if (state != null
                && state.chapterId == 10100501
                && state.autoSkillOpened
                && !state.autoSkillTaskFollowUpOpened
                && request.getSkillId() == 10150510100L) {
            state.autoSkillTaskFollowUpOpened = true;
            context.write(50906, nextTaskUpdate(200010), 0);
        }
    }

    /** 第六关技能请求，按抓包 idx 1980-2070 回放玩家技能与怪物扣血。 */
    @PlayerCmd
    public void useGuidanceMainMapSkill(
            IPlayerContext context,
            GuidanceMainMapUseSkillReq request) {
        CombatSession session = CombatSessionRegistry.get(context.getId());
        if (session == null || !isFightChapter(session.getChapterId())) {
            return;
        }

        session.updatePlayerPosition(
                request.getX(),
                request.getY(),
                request.getZ());

        CombatUnit target = session.getMonster(request.getTargetId());
        if (target == null || !target.isAlive()) {
            target = firstAliveMonster(session);
        }
        if (target == null) {
            writeUseSkillAck(
                    context, session, request, request.getSkillId(),
                    request.getTargetId(), 1);
            return;
        }
        if (writeNinthBossGuideSkillReject(context, session, request, target)) {
            return;
        }

        LOGGER.info(
                "Guidance skill: player={}, chapter={}, skill={}, target={}",
                context.getId(),
                session.getChapterId(),
                request.getSkillId(),
                target.getSceneUnitId());

        // 抓包 idx 1982：进入第六关第一次出手时完成 200012。
        // 人已经走到第 7/8 关后再领 200011，会把 200012 重新推成 0/1，
        // 这时必须在后续出手里补一次 FINISHED，不能再死守「第六关第一次」。
        if (isSixthChapter(session.getChapterId())) {
            sixthRealmTaskPlayers.add(context.getId());
        }
        boolean firstSixthWaveAttack = isSixthWave(session.getChapterId())
                && session.markPlayerAttackStarted();
        if (session.getChapterId() >= 10200101) {
            finishMainChapterArrive200012(context);
        }
        if (firstSixthWaveAttack) {
            // 第六关保留先前排、后后排的时序；第 7～10 关每波有六只，不能只激活三只。
            writeSixthWarnings(context, session, 0,
                    isSixthChapter(session.getChapterId()) ? 2 : session.getMonsters().size());
        }

        long playerUnitId = session.getPlayer().getSceneUnitId();
        long responseSkillId = session.getChapterId() == 10200405
                && request.getSkillId() == 10120110101L
                ? 10120110102L
                : request.getSkillId();
        int bloodlineBaseId = equippedSkillBaseForFightId(
                context.getId(), request.getSkillId());
        if (bloodlineBaseId > 0) {
            writeEquippedActiveSkills(context);
        }
        writeUseSkillAck(
                context, session, request, responseSkillId,
                target.getSceneUnitId(), 0);
        // 怒气（官服 MpResp 50798）：主动技能 10150510101 / 千焰剑罡 10151410100
        // 消耗 100 怒气，普通攻击/技能命中 +2。焰分噬浪尺 UseAddPower=6。
        if (request.getSkillId() == 10150510101L
                || request.getSkillId() == SECOND_FIGHT_SKILL_ID
                || (session.getChapterId() == 10200405
                        && request.getSkillId() == 10120110101L)
                || isQianyanSwordSkill(request.getSkillId())) {
            session.addMp(-100);
        } else if (isYanfenWaveSkill(bloodlineBaseId, request.getSkillId())) {
            session.addMp(6);
        } else {
            session.addMp(2);
        }
        context.write(
                50798,
                MpResp.newBuilder()
                        .setId(playerUnitId)
                        .setMp(session.getMp())
                        .build(),
                0);
        FightSkillUpdateResp.Builder skillCd =
                FightSkillUpdateResp.newBuilder()
                        .setId(playerUnitId)
                        .setSkillId(request.getSkillId())
                        .setCd(request.getTime());
        if (session.getChapterId() == 10200405
                && request.getSkillId() == 10120110101L) {
            // idx 8304-8305：借招回包使用变体 10120110102，消耗 100 怒气，CD 8 秒。
            skillCd.setSkillId(10120110102L).setCd(request.getTime() + 8000)
                    .setCostMp(100).setCostCd(8000).setActiveSkill(true);
        } else if (request.getSkillId() == SECOND_FIGHT_SKILL_ID) {
            // 官服 idx 8986：吹火掌是消耗怒气的 5 秒主动技能。
            skillCd.setCd(request.getTime() + 5000)
                    .setCostMp(100).setCostCd(5000).setActiveSkill(true);
        } else if (isQianyanSwordSkill(request.getSkillId())) {
            skillCd.setCostMp(100)
                    .setCostCd(5000)
                    .setActiveSkill(true);
        } else if (isYanfenWaveSkill(bloodlineBaseId, request.getSkillId())) {
            skillCd.setCd(request.getTime() + 8000)
                    .setCostCd(8000);
        } else {
            skillCd.setCostCd(1);
        }
        context.write(50766, skillCd.build(), 0);

        float dx = request.getTx() - request.getX();
        float dz = request.getTz() - request.getZ();
        float distance = (float) Math.hypot(dx, dz);
        boolean shouldChase = isSixthWave(session.getChapterId())
                && request.getSkillId() == 10110710101L
                && distance > 3.5f;
        if (shouldChase) {
            float stopDistance = 2.165f;
            float ratio = distance > stopDistance
                    ? (distance - stopDistance) / distance
                    : 0;
            float destX = request.getX() + dx * ratio;
            float destZ = request.getZ() + dz * ratio;
            if (session.getChapterId() % 10 == 3) {
                float[] clamped = clampChaseBeforeJump(
                        session.getBattleCenterZ(),
                        request.getX(),
                        request.getZ(),
                        destX,
                        destZ);
                destX = clamped[0];
                destZ = clamped[1];
            }
            final float chaseDestX = destX;
            final float chaseDestZ = destZ;
            if (chaseDestZ <= request.getZ() + 0.05f
                    && Math.abs(chaseDestX - request.getX()) <= 0.05f) {
                shouldChase = false;
            }
            if (shouldChase) {
                context.write(
                        50832,
                        ChaseStartResp.newBuilder()
                                .setFighterId(playerUnitId)
                                .setX(request.getX())
                                .setY(request.getY())
                                .setZ(request.getZ())
                                .setTx(chaseDestX)
                                .setTy(request.getTy())
                                .setTz(chaseDestZ)
                                .setSpeed(35.537384f)
                                .setSkillId(request.getSkillId())
                                .build(),
                        0);

                int chapterId = session.getChapterId();
                CombatScheduler.schedulePlayerChase(
                        context,
                        () -> completeGuidanceMainMapChase(
                                context,
                                request,
                                chapterId,
                                playerUnitId,
                                chaseDestX,
                                request.getTy(),
                                chaseDestZ,
                                firstSixthWaveAttack));
                return;
            }
        }
        if (!isQianyanSwordSkill(request.getSkillId())
                && request.getSkillId() != SECOND_FIGHT_SKILL_ID
                && !isYanfenWaveSkill(bloodlineBaseId, request.getSkillId())) {
            session.updatePlayerPosition(
                    request.getTx(),
                    request.getTy(),
                    request.getTz());
        }

        writeGuidanceMainMapPlayerDamage(
                context,
                session,
                target,
                request,
                firstSixthWaveAttack);
    }

    /** 按抓包顺序在追击开始后的逻辑帧结算技能伤害。 */
    public void completeGuidanceMainMapChase(
            IPlayerContext context,
            GuidanceMainMapUseSkillReq request,
            int chapterId,
            long fighterId,
            float x,
            float y,
            float z,
            boolean firstWaveAttack) {
        CombatSession session = CombatSessionRegistry.get(context.getId());
        if (session == null
                || session.getChapterId() != chapterId) {
            return;
        }

        session.updatePlayerPosition(x, y, z);
        CombatUnit target = session.getMonster(request.getTargetId());
        if (target != null && target.isAlive()) {
            writeGuidanceMainMapPlayerDamage(
                    context,
                    session,
                    target,
                    request,
                    firstWaveAttack);
        }

        CombatScheduler.schedulePlayerChaseStop(
                context,
                () -> stopGuidanceMainMapChase(
                        context,
                        chapterId,
                        fighterId,
                        request.getSkillId(),
                        x,
                        y,
                        z));
    }

    /** 抓包中技能伤害后一逻辑帧下发 ChaseStopResp。 */
    public void stopGuidanceMainMapChase(
            IPlayerContext context,
            int chapterId,
            long fighterId,
            long skillId,
            float x,
            float y,
            float z) {
        CombatSession session = CombatSessionRegistry.get(context.getId());
        if (session == null
                || session.getChapterId() != chapterId) {
            return;
        }

        context.write(
                50834,
                ChaseStopResp.newBuilder()
                        .setFighterId(fighterId)
                        .setX(x)
                        .setY(y)
                        .setZ(z)
                        .setSkillId(skillId)
                        .build(),
                0);
    }

    private void writeGuidanceMainMapPlayerDamage(
            IPlayerContext context,
            CombatSession session,
            CombatUnit target,
            GuidanceMainMapUseSkillReq request,
            boolean firstSixthWaveAttack) {
        long playerUnitId = session.getPlayer().getSceneUnitId();

        if (request.getSkillId() == SECOND_FIGHT_SKILL_ID) {
            writeBlowPalmHits(context, session);
            if (firstSixthWaveAttack && isSixthChapter(session.getChapterId())) {
                writeSixthWarnings(context, session, 2, 3);
            }
            return;
        }
        int bloodlineBaseId = equippedSkillBaseForFightId(
                context.getId(), request.getSkillId());
        if (isYanfenWaveSkill(bloodlineBaseId, request.getSkillId())) {
            writeYanfenWaveHit(context, session, target, request);
            if (firstSixthWaveAttack && isSixthChapter(session.getChapterId())) {
                writeSixthWarnings(context, session, 2, 3);
            }
            return;
        }
        if (bloodlineBaseId > 0) {
            writeBloodlineSlotHit(
                    context, session, target, request, bloodlineBaseId);
            if (firstSixthWaveAttack && isSixthChapter(session.getChapterId())) {
                writeSixthWarnings(context, session, 2, 3);
            }
            return;
        }

        if (isQianyanSwordSkill(request.getSkillId())) {
            writeQianyanSwordHits(context, session);
            if (firstSixthWaveAttack && isSixthChapter(session.getChapterId())) {
                writeSixthWarnings(context, session, 2, 3);
            }
            return;
        }

        if (session.getChapterId() == 10200405
                && request.getSkillId() == 10120110101L) {
            writePlayerDamage(
                    context, playerUnitId, target,
                    10120110102L, 10120110102L,
                    10120110151L, DamageTypeVo.FIGHT_SKILL,
                    128, 660074, 1);
            finishSixthTargetIfDead(context, session, target);
            return;
        }

        if (request.getSkillId() == 10150510101L) {
            long[] actions = {
                    10150510151L, 10150510153L,
                    10150510155L, 10150510157L
            };
            // Skill/Skills.json：hit_0..3。
            long[] hitTimes = { 200, 333, 467, 833 };
            for (int i = 0; i < actions.length; i++) {
                final int hit = i;
                scheduleSkillAction(context, session, hitTimes[i], () -> {
                    if (!session.isActive() || !target.isAlive()) {
                        return;
                    }
                    double hpBefore = target.getCurrentHp();
                    writePlayerDamage(
                            context, playerUnitId, target,
                            request.getSkillId(), request.getSkillId(),
                            actions[hit], DamageTypeVo.FIGHT_SKILL,
                            128, 753, hit);
                    logPlayerSkillHit(context, session, request.getSkillId(),
                            hit, target, hpBefore);
                    finishSixthTargetIfDead(context, session, target);
                });
            }
            scheduleSkillEnd(context, session, request.getSkillId(), 1600);
            if (firstSixthWaveAttack && isSixthChapter(session.getChapterId())) {
                writeSixthWarnings(context, session, 2, 3);
            }
            return;
        }

        if (request.getSkillId() == 10110710101L
                && target.getAttack() == 287.5) {
            writePlayerDamage(
                    context, playerUnitId, target,
                    request.getSkillId(), request.getSkillId(),
                    10110710102L, DamageTypeVo.GENERAL_SKILL,
                    2, 0, 1);
            writePlayerDamage(
                    context, playerUnitId, target,
                    0, 0,
                    10100401L, DamageTypeVo.GENERAL,
                    0, 2282, 0);
            finishSixthTargetIfDead(context, session, target);
            if (firstSixthWaveAttack && isSixthChapter(session.getChapterId())) {
                writeSixthWarnings(context, session, 2, 3);
            }
            return;
        }

        long actionId = request.getSkillId() == 10110710101L
                ? 10110710102L
                : request.getSkillId();
        writePlayerDamage(
                context, playerUnitId, target,
                request.getSkillId(), request.getSkillId(),
                actionId, DamageTypeVo.GENERAL_SKILL,
                0, generalSkillDamage(session.getChapterId()),
                request.getSkillId() == 10110710101L ? 1 : 0);
        finishSixthTargetIfDead(context, session, target);
        if (firstSixthWaveAttack && isSixthChapter(session.getChapterId())) {
            writeSixthWarnings(context, session, 2, 3);
        }
    }

    private static void writeSixthWarnings(
            IPlayerContext context,
            CombatSession session,
            int from,
            int to) {
        List<CombatUnit> monsters = new ArrayList<>(session.getMonsters());
        for (int i = from; i < to && i < monsters.size(); i++) {
            session.alertMonster(monsters.get(i).getSceneUnitId());
            context.write(
                    50793,
                    WarningResp.newBuilder()
                            .setId(monsters.get(i).getSceneUnitId())
                            .build(),
                    0);
        }
    }

    /**
     * 第六关由服务端结算伤害，目标归零后必须主动切到下一只存活怪。
     *
     * <p>官服第一波的击杀顺序是 1000602 → 1000601 → 1000603，不能再用
     * “已杀数量 + 出生基址”推算下一目标，否则第一只死后会重新锁回死怪。
     */
    private void finishSixthTargetIfDead(
            IPlayerContext context,
            CombatSession session,
            CombatUnit target) {
        if (target.isAlive()) {
            return;
        }

        GuidanceState state = guidanceStates.get(context.getId());
        if (state == null || !state.recordUnit(target.getSceneUnitId())) {
            return;
        }
        state.lastKilledMonsterId = target.getSceneUnitId();

        CombatUnit nextTarget = firstAliveMonster(session);
        if (nextTarget != null) {
            session.alertMonster(nextTarget.getSceneUnitId());
            context.write(
                    50793,
                    WarningResp.newBuilder()
                            .setId(nextTarget.getSceneUnitId())
                            .build(),
                    0);
            context.write(
                    50757,
                    SceneForgetVisibleResp.newBuilder()
                            .addForgetIds(target.getSceneUnitId())
                            .build(),
                    0);
            return;
        }

        ChapterConfig.Chapter chapter = ChapterConfig.get(state.chapterId);
        if (chapter != null && !state.fightResultSent) {
            writeFightResult(context, chapter);
            state.fightResultSent = true;
        }
    }

    private static CombatUnit firstAliveMonster(CombatSession session) {
        if (session == null) {
            return null;
        }
        for (CombatUnit monster : session.getMonsters()) {
            if (monster.isAlive()) {
                return monster;
            }
        }
        return null;
    }

    /** 焰分噬浪尺：第 9 关给的危机/血脉斗技 80128001，战斗技能 10150310100。 */
    private static boolean isYanfenWaveSkill(int equippedBaseId, long skillId) {
        return equippedBaseId == YANFEN_SKILL_BASE_ID
                && skillId == YANFEN_FIGHT_SKILL_ID;
    }

    private int equippedSkillBaseForFightId(long playerId, long fightSkillId) {
        SkillSchemaState state = heroSkillSchemas.get(playerId);
        if (state == null) {
            return 0;
        }
        for (IntegerAndIntegerPairEntry slot : state.slots) {
            if (fightSkillIdForBase(slot.getValue()) == fightSkillId) {
                return slot.getValue();
            }
        }
        return 0;
    }

    /**
     * 吹火掌：Skills.json 的 hit_0/1/2；Action.json 半径 10、最近最多 7 个敌人。
     * 抓包 idx 9115/9121/9123 分别为 action 01/04/07、group 101/102/103。
     */
    private void writeBlowPalmHits(IPlayerContext context, CombatSession session) {
        long[] actions = {10150210101L, 10150210104L, 10150210107L};
        long[] times = {433, 767, 1167};
        // 保留当前单段攻击力结算，不用抓包 2124/2331 反推虚构倍率。
        // unresolved: skilldamageconfig 的星级、固定附伤与最终伤害修正尚未完整复算。
        // 装备更新可能发生在本波内，取最新已下发属性，不读入场时冻结的 attack。
        double damage = Math.rint(grantedAttack.getOrDefault(
                context.getId(), session.getPlayer().getAttack()));
        long playerUnitId = session.getPlayer().getSceneUnitId();
        for (int i = 0; i < times.length; i++) {
            final int stage = i;
            scheduleSkillAction(context, session, times[i], () -> {
                if (!session.isActive()) {
                    return;
                }
                List<CombatUnit> victims = new ArrayList<>();
                for (CombatUnit monster : session.getMonsters()) {
                    if (monster.isAlive() && session.getPlayer().distanceTo(monster) <= 10) {
                        victims.add(monster);
                    }
                }
                victims.sort(java.util.Comparator.comparingDouble(session.getPlayer()::distanceTo));
                if (victims.isEmpty()) {
                    return;
                }
                SkillActionResp.Builder response = SkillActionResp.newBuilder()
                        .setSkillId(SECOND_FIGHT_SKILL_ID);
                for (CombatUnit victim : victims.subList(0, Math.min(7, victims.size()))) {
                    double hpBefore = victim.getCurrentHp();
                    victim.applyDamage(damage);
                    logPlayerSkillHit(context, session, SECOND_FIGHT_SKILL_ID, stage, victim, hpBefore);
                    response.addActionList(ActionVo.newBuilder().setActionVoTypeId(1)
                            .setDamageVo(DamageVO.newBuilder().setDamage(damage).setSpecial(128)
                                    .setActionId(actions[stage]).setType(DamageTypeVo.FIGHT_SKILL)
                                    .setGroup(101 + stage).setSkillId(SECOND_FIGHT_SKILL_ID)
                                    .setSkillBaseId(SECOND_SKILL_BASE_ID).setAttackId(playerUnitId)
                                    .setTargetId(victim.getSceneUnitId()).setHitId(stage)));
                    if (!victim.isAlive()) {
                        response.addActionList(ActionVo.newBuilder().setActionVoTypeId(6)
                                .setDieVo(DieVO.newBuilder().setMonsterTemplateId(victim.getTemplateId())
                                        .setX(victim.getX()).setY(victim.getY()).setZ(victim.getZ())
                                        .setDir(victim.getDir()).setAttackId(playerUnitId)
                                        .setTargetId(victim.getSceneUnitId()).setHitId(stage)));
                    }
                    response.addActionList(ActionVo.newBuilder().setActionVoTypeId(18)
                            .setAttributeActionVo(AttributeActionVO.newBuilder()
                                    .addAttrList(attribute(103011, victim.getCurrentHp()))
                                    .setAttackId(playerUnitId).setTargetId(victim.getSceneUnitId())
                                    .setHitId(stage)));
                }
                context.write(50763, response.build(), 0);
                // 复用延迟清场流程，不能在飘字/死亡动作播放前移除场景单位。
                finishQianyanWaveIfCleared(context, session);
            });
        }
        scheduleSkillEnd(context, session, SECOND_FIGHT_SKILL_ID, 2333);
    }

    /** 第三格（或其它血脉槽）已装配斗技的通用 FightSkill 伤害。 */
    private void writeBloodlineSlotHit(
            IPlayerContext context,
            CombatSession session,
            CombatUnit target,
            GuidanceMainMapUseSkillReq request,
            int skillBaseId) {
        long playerUnitId = session.getPlayer().getSceneUnitId();
        double damage = Math.rint(session.getPlayer().getAttack());
        writePlayerDamage(
                context, playerUnitId, target,
                request.getSkillId(), request.getSkillId(),
                0, DamageTypeVo.FIGHT_SKILL,
                128, damage, 0, skillBaseId);
        finishSixthTargetIfDead(context, session, target);
    }

    /**
     * 焰分噬浪尺输出。抓包 idx 9131-9138：8 秒 CD、BaoBu 前冲、FightSkill
     * 伤害带 skillBaseId=80128001。
     */
    private void writeYanfenWaveHit(
            IPlayerContext context,
            CombatSession session,
            CombatUnit target,
            GuidanceMainMapUseSkillReq request) {
        long playerUnitId = session.getPlayer().getSceneUnitId();
        context.write(
                50844,
                BaoBuStartResp.newBuilder()
                        .setFighterId(playerUnitId)
                        .setSkillId(YANFEN_FIGHT_SKILL_ID)
                        .setMoveTime(150)
                        .setBackOffset(4)
                        .setStopTargetId(target.getSceneUnitId())
                        .setX(request.getX())
                        .setY(request.getY())
                        .setZ(request.getZ())
                        .build(),
                0);
        double damage = Math.rint(session.getPlayer().getAttack() * 1.889);
        writePlayerDamage(
                context, playerUnitId, target,
                YANFEN_FIGHT_SKILL_ID, YANFEN_FIGHT_SKILL_ID,
                0, DamageTypeVo.FIGHT_SKILL,
                128, damage, 0, YANFEN_SKILL_BASE_ID);
        finishSixthTargetIfDead(context, session, target);
    }

    /** 千焰剑罡：危机斗技 100001，战斗技能 10151410100。 */
    private static boolean isQianyanSwordSkill(long skillId) {
        return skillId == 10151410100L || skillId == 10151410101L;
    }

    /**
     * 第 9 关 Boss 引导 10043 打开斗技时，客户端会先发普攻 10111120101。
     * 官服 idx 8230-8233 回 75011 并 failCode=7，不打伤害。
     */
    private boolean writeNinthBossGuideSkillReject(
            IPlayerContext context,
            CombatSession session,
            GuidanceMainMapUseSkillReq request,
            CombatUnit target) {
        if (!session.isPaused()
                || session.getChapterId() != 10200405
                || request.getSkillId() != 10111120101L) {
            return false;
        }
        grantNinthTeachingSkill(context);
        context.write(
                50762,
                UseSkillResp.newBuilder()
                        .setAttackId(session.getPlayer().getSceneUnitId())
                        .setSkillId(request.getSkillId())
                        .setTargetId(target.getSceneUnitId())
                        .setServerAdvance(false)
                        .setFailCode(7)
                        .build(),
                0);
        return true;
    }

    /** idx 8229-8232：教学进度到达斗技选择页即发借招，不依赖普攻与暂停的竞态。 */
    private void grantNinthTeachingSkill(IPlayerContext context) {
        CombatSession session = CombatSessionRegistry.get(context.getId());
        GuidanceState state = guidanceStates.get(context.getId());
        if (session == null || !session.isNinthBossGuideStarted()
                || state == null || state.chapterId != 10200405
                || state.fightResultSent || state.ninthTeachingSkillGranted) {
            return;
        }
        state.ninthTeachingSkillGranted = true;
        context.write(
                75011,
                HeroSkillActResp.newBuilder()
                        .setHeroIndex(0)
                        .setShowTips(false)
                        .setPlayerSkill(PlayerSkillVo.newBuilder()
                                .setBaseId(101001)
                                .addHeroSkills(PlayerHeroSkillVo.newBuilder()
                                        .setHeroIdx(0)
                                        .setHeroSkill(SkillVo.newBuilder()
                                                .setBaseId(101001)
                                                .setStar(6)
                                                .setActStar(0)
                                                .setAwaken(false)
                                                .setSelElement(0))))
                        .build(),
                0);
        LOGGER.info("Ninth teaching skill granted: player={}, skill=101001", context.getId());
    }

    /** idx 8314/8316/8323：借招不是永久技能，战后撤回并恢复原主动槽。 */
    private void removeNinthTeachingSkill(IPlayerContext context) {
        GuidanceState state = guidanceStates.get(context.getId());
        if (state == null || !state.ninthTeachingSkillGranted) {
            return;
        }
        state.ninthTeachingSkillGranted = false;
        context.write(75046, HeroSkillSkillUpdateResp.newBuilder()
                .addPlayerSkillUpdates(PlayerSkillVo.newBuilder().setBaseId(101001))
                .build(), 0);
        SkillSchemaState schema = heroSkillSchemas.get(context.getId());
        if (schema == null) {
            return;
        }
        HeroSkillSchemaUpdateReq.Builder restored = HeroSkillSchemaUpdateReq.newBuilder()
                .setHeroIndex(schema.heroIndex);
        boolean replaced = false;
        for (IntegerAndIntegerPairEntry slot : schema.slots) {
            if (slot.getValue() != 101001) {
                restored.addSkills(slot);
            } else {
                replaced = true;
            }
        }
        if (!replaced) {
            return;
        }
        persistSkillSchema(context.getId(), restored.build());
        writeSkillSchemaEcho(context, restored.build());
        restored.addSkills(IntegerAndIntegerPairEntry.newBuilder().setKey(1001).setValue(100001));
        persistSkillSchema(context.getId(), restored.build());
        writeSkillSchemaEcho(context, restored.build());
    }

    /**
     * 施法回包。千焰剑罡官服 idx 5971 把特效锚在玩家脚下；
     * failCode != 0 时客户端会立刻结束施法，避免人冻住。
     */
    private static void writeUseSkillAck(
            IPlayerContext context,
            CombatSession session,
            GuidanceMainMapUseSkillReq request,
            long skillId,
            long targetId,
            int failCode) {
        boolean qianyan = isQianyanSwordSkill(request.getSkillId())
                || request.getSkillId() == SECOND_FIGHT_SKILL_ID
                || (session.getChapterId() == 10200405 && request.getSkillId() == 10120110101L);
        float effectX = qianyan ? request.getX() : request.getTx();
        float effectY = qianyan ? request.getY() : request.getTy();
        float effectZ = qianyan ? request.getZ() : request.getTz();
        float angle = (float) Math.toDegrees(Math.atan2(
                request.getTz() - request.getZ(),
                request.getTx() - request.getX()));
        context.write(
                50762,
                UseSkillResp.newBuilder()
                        .setAttackId(session.getPlayer().getSceneUnitId())
                        .setSkillId(skillId)
                        .setTargetId(targetId)
                        .setCurX(request.getX())
                        .setCurY(request.getY())
                        .setCurZ(request.getZ())
                        .setTargetX(effectX)
                        .setTargetY(effectY)
                        .setTargetZ(effectZ)
                        .setAngle(angle)
                        .setStartType(0)
                        .setServerAdvance(true)
                        .setSkillNo(ThreadLocalRandom.current().nextInt())
                        .setFailCode(failCode)
                        .setSkipTime(request.getSkipTime())
                        .setTimeRatio(1)
                        .setPlaySkillBlackMask(false)
                        .build(),
                0);
    }

    /**
     * 千焰剑罡三段群体伤害，对应抓包 idx 5990/6011/6050。
     *
     * <p>客户端只认 {@code FightSkill + actionId 10151410102/05/08 +
     * skillBaseId=100001}，走普攻通道时动画放得出但伤害会被丢掉。
     */
    private void writeQianyanSwordHits(
            IPlayerContext context,
            CombatSession session) {
        long playerUnitId = session.getPlayer().getSceneUnitId();
        double attack = session.getPlayer().getAttack();
        long[] actions = { 10151410102L, 10151410105L, 10151410108L };
        int[] groups = { 101, 102, 103 };
        int[] specials = { 128, 128, 129 };
        double[] ratios = { 1.282, 1.0, 1.726 };
        // Skill/Skills.json：skill13 的 hit_0/1/2，抓包 idx 7292/7296/7301。
        long[] hitTimes = { 667, 1133, 2033 };

        for (int i = 0; i < actions.length; i++) {
            final int stage = i;
            scheduleSkillAction(context, session, hitTimes[i], () -> {
                if (!session.isActive()) {
                    return;
                }
                List<CombatUnit> victims = new ArrayList<>();
                for (CombatUnit monster : session.getMonsters()) {
                    if (monster.isAlive()) {
                        victims.add(monster);
                    }
                }
                if (victims.isEmpty()) {
                    return;
                }

                double damage = Math.rint(attack * ratios[stage]);
                SkillActionResp.Builder action = SkillActionResp.newBuilder()
                        .setSkillId(10151410100L);
                for (CombatUnit victim : victims) {
                    double hpBefore = victim.getCurrentHp();
                    victim.applyDamage(damage);
                    logPlayerSkillHit(context, session, 10151410100L,
                            stage, victim, hpBefore);
                    DamageVO.Builder damageVo = DamageVO.newBuilder()
                            .setDamage(damage)
                            .setSpecial(specials[stage])
                            .setActionId(actions[stage])
                            .setType(DamageTypeVo.FIGHT_SKILL)
                            .setGroup(groups[stage])
                            .setSkillId(10151410100L)
                            .setSkillBaseId(100001)
                            .setAttackId(playerUnitId)
                            .setTargetId(victim.getSceneUnitId())
                            .setHitId(stage);
                    action.addActionList(ActionVo.newBuilder()
                            .setActionVoTypeId(1)
                            .setDamageVo(damageVo));
                    action.addActionList(ActionVo.newBuilder()
                            .setActionVoTypeId(18)
                            .setAttributeActionVo(
                                    AttributeActionVO.newBuilder()
                                            .addAttrList(attribute(
                                                    103011,
                                                    victim.getCurrentHp()))
                                            .setAttackId(playerUnitId)
                                            .setTargetId(victim.getSceneUnitId())
                                            .setHitId(stage)));
                    if (!victim.isAlive()) {
                        action.addActionList(ActionVo.newBuilder()
                                .setActionVoTypeId(6)
                                .setDieVo(DieVO.newBuilder()
                                        .setMonsterTemplateId(victim.getTemplateId())
                                        .setX(victim.getX())
                                        .setY(victim.getY())
                                        .setZ(victim.getZ())
                                        .setDir(victim.getDir())
                                        .setAttackId(playerUnitId)
                                        .setTargetId(victim.getSceneUnitId())
                                        .setHitId(stage)));
                    }
                }
                context.write(50763, action.build(), 0);
                finishQianyanWaveIfCleared(context, session);
            });
        }
        scheduleSkillEnd(context, session, 10151410100L, 4100);
    }

    private void scheduleSkillAction(
            IPlayerContext context, CombatSession session,
            long delayMillis, Runnable action) {
        scheduleCombatAction(context, delayMillis, () -> {
            // 同一章节重进也会更换会话，不能只比较 chapterId。
            if (CombatSessionRegistry.get(context.getId()) == session
                    && session.getPlayer().isAlive()) {
                action.run();
            }
        });
    }

    void scheduleCombatAction(
            IPlayerContext context, long delayMillis, Runnable action) {
        CombatScheduler.scheduleOnPlayer(
                context, java.time.Duration.ofMillis(delayMillis), action);
    }

    private void scheduleSkillEnd(
            IPlayerContext context, CombatSession session,
            long skillId, long delayMillis) {
        // 官服 idx 7320：动画结束仍需空 SkillActionResp，死亡后也要发送。
        scheduleSkillAction(context, session, delayMillis, () ->
                context.write(50763, SkillActionResp.newBuilder()
                        .setSkillId(skillId).build(), 0));
    }

    private static void logPlayerSkillHit(
            IPlayerContext context, CombatSession session, long skillId,
            int hit, CombatUnit target, double hpBefore) {
        LOGGER.info("Player skill hit: player={}, chapter={}, skill={}, hit={}, "
                        + "target={}, hpBefore={}, hpAfter={}",
                context.getId(), session.getChapterId(), skillId, hit,
                target.getSceneUnitId(), hpBefore, target.getCurrentHp());
    }

    /**
     * 千焰击杀后只结算 FightResult，不立刻 ForgetVisible。
     *
     * <p>客户端飘字和 hit 特效要等 skill13 的 hit_0/1/2（667/1133/2033ms）。
     * 提前拆掉场景单位时 {@code FightResultDamage.Excute} 找不到目标，
     * 数字和火焰都不会出现。
     */
    private void finishQianyanWaveIfCleared(
            IPlayerContext context,
            CombatSession session) {
        if (firstAliveMonster(session) != null) {
            return;
        }
        GuidanceState state = guidanceStates.get(context.getId());
        if (state == null || state.fightResultSent) {
            return;
        }
        for (CombatUnit monster : session.getMonsters()) {
            state.recordUnit(monster.getSceneUnitId());
            state.lastKilledMonsterId = monster.getSceneUnitId();
        }
        ChapterConfig.Chapter chapter = ChapterConfig.get(state.chapterId);
        if (chapter != null) {
            LOGGER.info(
                    "Qianyan wave cleared: player={}, chapter={}, boss={}",
                    context.getId(),
                    chapter.getChapterId(),
                    chapter.isBoss());
            writeFightResult(context, chapter);
            state.fightResultSent = true;
        }
    }

    private static void writePlayerDamage(
            IPlayerContext context,
            long playerUnitId,
            CombatUnit target,
            long responseSkillId,
            long damageSkillId,
            long actionId,
            DamageTypeVo damageType,
            int special,
            double damage,
            int hitId) {
        writePlayerDamage(
                context, playerUnitId, target,
                responseSkillId, damageSkillId, actionId, damageType,
                special, damage, hitId, 0);
    }

    private static void writePlayerDamage(
            IPlayerContext context,
            long playerUnitId,
            CombatUnit target,
            long responseSkillId,
            long damageSkillId,
            long actionId,
            DamageTypeVo damageType,
            int special,
            double damage,
            int hitId,
            long skillBaseId) {
        target.applyDamage(damage);
        DamageVO.Builder damageVo = DamageVO.newBuilder()
                .setDamage(damage)
                .setSpecial(special)
                .setActionId(actionId)
                .setType(damageType)
                .setSkillId(damageSkillId)
                .setAttackId(playerUnitId)
                .setTargetId(target.getSceneUnitId())
                .setHitId(hitId);
        if (skillBaseId != 0) {
            damageVo.setSkillBaseId(skillBaseId);
        }
        context.write(
                50763,
                SkillActionResp.newBuilder()
                        .setSkillId(responseSkillId)
                        .addActionList(ActionVo.newBuilder()
                                .setActionVoTypeId(1)
                                .setDamageVo(damageVo))
                        .addActionList(ActionVo.newBuilder()
                                .setActionVoTypeId(18)
                                .setAttributeActionVo(
                                        AttributeActionVO.newBuilder()
                                                .addAttrList(attribute(
                                                        103011,
                                                        target.getCurrentHp()))
                                                .setAttackId(playerUnitId)
                                                .setTargetId(
                                                        target.getSceneUnitId())
                                                .setHitId(hitId)))
                        .build(),
                0);
    }

    /**
     * 把普攻追击截在跳台之前。
     *
     * <p>官服第三波远怪会先 {@code 50761} 走近，玩家追击最高落到 z≈109.9
     * （抓包 idx 2527），跳点在 114.7。我们还没下发怪物走位时，技能目标仍在
     * 战斗点 +16，按停距 2.165 追会落到约 118，客户端就会跳过
     * {@code SceneJumpPointAction}、沿坡走上去。
     */
    private static float[] clampChaseBeforeJump(
            float battleCenterZ,
            float fromX,
            float fromZ,
            float chaseX,
            float chaseZ) {
        if (battleCenterZ == 0f || chaseZ <= battleCenterZ + 8f) {
            return new float[] { chaseX, chaseZ };
        }
        float limitZ = battleCenterZ + 8f;
        if (chaseZ == fromZ) {
            return new float[] { chaseX, Math.min(chaseZ, limitZ) };
        }
        float t = (limitZ - fromZ) / (chaseZ - fromZ);
        if (t < 0f) {
            t = 0f;
        } else if (t > 1f) {
            t = 1f;
        }
        return new float[] {
                fromX + (chaseX - fromX) * t,
                fromZ + (chaseZ - fromZ) * t
        };
    }

    /** 第六至十关的三波小怪关。 */
    private static boolean isSixthWave(int chapterId) {
        return (chapterId >= 10200101 && chapterId <= 10200103)
                || (chapterId >= 10200201 && chapterId <= 10200203)
                || (chapterId >= 10200301 && chapterId <= 10200303)
                || (chapterId >= 10200401 && chapterId <= 10200403)
                || (chapterId >= 10200501 && chapterId <= 10200503);
    }

    /** 乌坦城战报小怪关（第11–14关各三波）。 */
    private static boolean isWutanWave(int chapterId) {
        int stage = chapterId % 10;
        return chapterId >= 10300101
                && chapterId <= 10301003
                && stage >= 1
                && stage <= 3;
    }

    /** 乌坦城本章（三波 + Boss，第11–14关）。 */
    private static boolean isWutanChapter(int chapterId) {
        return chapterId >= 10300101 && chapterId <= 10400101
                && ChapterConfig.get(chapterId) != null;
    }

    /** 三波循环关：打完后按 sixthWaveAdvanced 推进。 */
    private static boolean isLoopWave(int chapterId) {
        return isSixthWave(chapterId) || isWutanWave(chapterId);
    }

    /** 第六关战斗章节：三波小怪（10200101-10200103）与 Boss（10200105）。 */
    private static boolean isSixthChapter(int chapterId) {
        return (chapterId >= 10200101 && chapterId <= 10200103)
                || chapterId == 10200105;
    }

    /** 第六至十关战斗章节（三波小怪 + Boss），以及乌坦城占位关。 */
    private static boolean isFightChapter(int chapterId) {
        return chapterId >= 10200101 && ChapterConfig.get(chapterId) != null;
    }

    /** 玩家通用技能伤害，按章节区分（抓包 Boss 战：第六关 261、第七关 327、第八关 406）。 */
    private static double generalSkillDamage(int chapterId) {
        if (chapterId >= 10200301) {
            return 406.0;
        }
        if (chapterId >= 10200201) {
            return 327.0;
        }
        return 261.0;
    }

    @PlayerCmd
    public void killMainMapMonster(
            IPlayerContext context,
            MainMapKillMonsterReq request) {

        GuidanceState state = guidanceStates.get(context.getId());

        if (state == null) {
            return;
        }

        ChapterConfig.Chapter chapter = ChapterConfig.get(state.chapterId);
        if (chapter == null) {
            return;
        }

        if (isWutanChapter(chapter.getChapterId())) {
            if (request.getMainMapChapterId() != chapter.getChapterId()) return;
            if (state.fightResultSent) {
                return;
            }
            if (chapter.getChapterId() >= 10300501) {
                long allowed = chapter.getMonsters().stream()
                        .filter(m -> m.getTemplateId() == request.getMonsterId()).count();
                int received = state.battleLogTemplateKills.getOrDefault(request.getMonsterId(), 0);
                if (received >= allowed) return;
                state.battleLogTemplateKills.put(request.getMonsterId(), received + 1);
            }
            int progress = state.recordBattleLogKill(request.getMonsterId());
            if (chapter.getChapterId() >= 10300501) {
                Chapter17Progress laterState = later(context.getId());
                long count = ++laterState.killCount;
                TaskUpdateResp.Builder tasks = TaskUpdateResp.newBuilder();
                if (count <= 300) tasks.addTaskVos(task(1110,
                        count == 300 ? TaskPhase.FINISHED : TaskPhase.PROGRESS, count));
                if (count <= 500) tasks.addTaskVos(task(1113,
                        count == 500 ? TaskPhase.FINISHED : TaskPhase.PROGRESS, count));
                if (tasks.getTaskVosCount() > 0) context.write(50906, tasks.build(), 0);
            }
            if (chapter.getChapterId() < 10300501 && progress > 0 && progress < chapter.getMonsterCount()) {
                long cumulative =
                        chapter.getKillProgressBefore() + progress;
                if (cumulative == 200) {
                    context.write(
                            60751,
                            BattlePassInfoResp.getDefaultInstance(),
                            0);
                }
                context.write(
                        50906,
                        wutanKillTaskUpdate(chapter, progress, false),
                        0);
            }
            if (progress >= chapter.getMonsterCount()) {
                state.fightResultSent = true;
                if (chapter.isBoss() && chapter.getChapterId() < 10300501) {
                    writeFightResult(context, chapter);
                }
            }
            return;
        }

        /*
         * 第八关 Boss 千焰击杀（抓包 idx 7316/7319）：客户端报 61964 后，
         * 官服再下 50757 ForgetVisible，客户端才会发 61963 EndFight。
         * Boss 通关任务仍由战斗结算负责，这里只补遗忘。
         */
        if (chapter.isBoss()) {
            /*
             * 乌坦城七彩吞天蟒和千焰一样是 ClickBattle：61964 只表示客户端
             * 认为打死了。血还在时不能秒结算，否则 50756 刚刷出来就被 61964
             * 拆掉，玩家看不见 Boss。
             */
            forgetBossAfterMainMapKill(context, state);
            return;
        }

        int progress = state.recordTemplate(request.getMonsterId());

        // 最后一只怪的进度随战斗结算一起下发，这里只推中间进度。
        if (progress > 0 && progress < chapter.getMonsterCount()) {
            context.write(50906, killTaskUpdate(chapter, progress), 0);
        }
    }

    /**
     * 千焰把 Boss 打死时客户端只报 61964，不报 61997。
     *
     * <p>伤害已按命中时刻执行，按抓包 idx 7316→7319 在击杀后约一秒遗忘。
     */
    private void forgetBossAfterMainMapKill(
            IPlayerContext context,
            GuidanceState state) {
        if (state.bossForgotten) {
            return;
        }
        if (!state.fightResultSent) {
            CombatSession session = CombatSessionRegistry.get(context.getId());
            if (firstAliveMonster(session) != null) {
                LOGGER.info(
                        "Ignore boss 61964 before settle: player={}, chapter={}",
                        context.getId(),
                        state.chapterId);
                return;
            }
            ChapterConfig.Chapter chapter = ChapterConfig.get(state.chapterId);
            if (chapter == null) {
                return;
            }
            if (session != null) {
                for (CombatUnit monster : session.getMonsters()) {
                    state.recordUnit(monster.getSceneUnitId());
                    state.lastKilledMonsterId = monster.getSceneUnitId();
                }
            }
            LOGGER.info(
                    "Boss 61964 settling missed fight result: player={}, chapter={}",
                    context.getId(),
                    chapter.getChapterId());
            writeFightResult(context, chapter);
            state.fightResultSent = true;
        }
        if (state.lastKilledMonsterId == 0) {
            return;
        }
        state.bossForgotten = true;
        final long forgetId = state.lastKilledMonsterId;
        scheduleCombatAction(
                context,
                1000L,
                () -> {
                    if (guidanceStates.get(context.getId()) != state) {
                        return;
                    }
                    LOGGER.info(
                            "Boss ForgetVisible after 61964: player={}, id={}",
                            context.getId(),
                            forgetId);
                    context.write(
                            50757,
                            SceneForgetVisibleResp.newBuilder()
                                    .addForgetIds(forgetId)
                                    .build(),
                            0);
                });
    }

    @PlayerCmd
    public void killGuidanceMonster(
            IPlayerContext context,
            GuidanceKillMonsterReq request) {

        GuidanceState state = guidanceStates.get(context.getId());

        if (state == null
                || !state.recordUnit(request.getMonsterId())) {
            return;
        }

        CombatSession session = CombatSessionRegistry.get(context.getId());
        if (session != null) {
            /*
             * 击杀上报里带的是玩家击杀这只怪时所站的位置。客户端只在放技能和
             * 击杀时上报坐标，这里同步一次，玩家往下一只怪推进后距离判定才准确。
             */
            session.updatePlayerPosition(
                    request.getX(),
                    request.getY(),
                    request.getZ());
            session.markMonsterDead(request.getMonsterId());
        }

        state.lastKilledMonsterId = request.getMonsterId();

        ChapterConfig.Chapter chapter = ChapterConfig.get(state.chapterId);
        if (chapter == null) {
            return;
        }

        // Boss 关只有一只怪，死亡后立即结算。
        if (chapter.isBoss()) {
            if (!state.fightResultSent) {
                writeFightResult(context, chapter);
                state.fightResultSent = true;
            }
            return;
        }

        int killCount = state.killedUnitIds.size();

        LOGGER.info(
                "Guidance kill: player={}, chapter={}, monsterId={}, "
                        + "killCount={}/{}, boss={}",
                context.getId(),
                chapter.getChapterId(),
                request.getMonsterId(),
                killCount,
                chapter.getMonsterCount(),
                chapter.isBoss());

        // 还有小怪没死：指向下一只，并让客户端遗忘刚死的这只。
        if (killCount < chapter.getMonsterCount()) {
            CombatUnit nextMonster = firstAliveMonster(session);
            long nextMonsterId = nextMonster != null
                    ? nextMonster.getSceneUnitId()
                    : waveUnitIdAt(context.getId(), killCount);
            if (nextMonsterId == 0) {
                return;
            }

            context.write(
                    50793,
                    WarningResp.newBuilder()
                            .setId(nextMonsterId)
                            .build(),
                    0);

            context.write(
                    50757,
                    SceneForgetVisibleResp.newBuilder()
                            .addForgetIds(request.getMonsterId())
                            .build(),
                    0);
            return;
        }

        if (!state.fightResultSent) {
            LOGGER.info("Writing fight result: player={}, chapter={}",
                    context.getId(), chapter.getChapterId());
            writeFightResult(context, chapter);
            state.fightResultSent = true;
        }
    }

    @PlayerCmd
    public void endMainMapFight(
            IPlayerContext context,
            MainMapEndFightReq request) {

        GuidanceState state = guidanceStates.get(context.getId());

        if (state == null || !state.fightResultSent) {
            return;
        }

        CombatSessionRegistry.clear(context.getId());

        ChapterConfig.Chapter chapter = ChapterConfig.get(state.chapterId);
        if (chapter == null) {
            return;
        }

        // Boss 打完：直接推进到下一大关。
        if (chapter.getChapterId() >= 10300501) {
            if (request.getMainMapChapterId() != chapter.getChapterId()) return;
            state.fightResultSent = false;
            finishChapter17Fight(context, chapter);
            return;
        }
        if (chapter.isBoss()) {
            LOGGER.info("Boss EndFight: player={}, chapter={}, next={}",
                    context.getId(), chapter.getChapterId(), chapter.getNextChapterId());
            ChapterConfig.Chapter nextChapter =
                    ChapterConfig.get(chapter.getNextChapterId());

            if (!isWutanChapter(chapter.getChapterId())) {
                writeSkillReset(context);
            }

            if (nextChapter != null) {
                context.write(
                        61952,
                        chapterAdvanced(nextChapter, chapter.getChapterId()),
                        0);
            }

            if (state.lastKilledMonsterId != 0) {
                context.write(
                        50757,
                        SceneForgetVisibleResp.newBuilder()
                                .addForgetIds(state.lastKilledMonsterId)
                                .build(),
                        0);
            }

            if (nextChapter != null) {
                guidanceStates.put(
                        context.getId(),
                        new GuidanceState(nextChapter.getChapterId()));
            }
            return;
        }

        /*
         * 小怪关打完。抓包 idx 439-449 的顺序是：
         *   61963 EndFight
         *   → 50906 主线任务 finished（idx 441）
         *   → 50906 通用任务累计更新，不含主线（idx 448）
         *   → 61952 解锁 Boss
         * 两次任务推送缺一不可，否则客户端任务状态机卡住。
         */
        LOGGER.info(
                "Mob chapter cleared: player={}, chapter={}, next={}",
                context.getId(),
                chapter.getChapterId(),
                chapter.getNextChapterId());

        // 第一次推送：主线任务标记 finished（本关击杀数），通用任务累计值
        if (isWutanWave(chapter.getChapterId())) {
            context.write(
                    50906,
                    wutanKillTaskUpdate(
                            chapter, chapter.getMonsterCount(), true),
                    0);
            context.write(
                    50406,
                    RewardResp.newBuilder()
                            .setOperationType(61951)
                            .build(),
                    0);
        } else {
            context.write(
                    50906,
                    killTaskUpdate(chapter, chapter.getMonsterCount()),
                    0);
        }

        // 第二关抓包有第二次推送：只含通用任务的最终累计值（idx 448）。
        // 第一关抓包 idx 210 后直接进入章节更新，不能一概多发一次。
        if (chapter.getChapterId() == 10100201) {
            context.write(50906, cumulativeKillTaskUpdate(chapter), 0);
        }

        if (!isWutanWave(chapter.getChapterId())) {
            writeSkillReset(context);
        }
        if (isLoopWave(chapter.getChapterId())) {
            int firstWave = chapter.getChapterId()
                    - chapter.getChapterId() % 10 + 1;
            // 第三波首次解锁 Boss；官服后续地图循环仍持续携带该 Boss ID。
            if (chapter.getChapterId() % 10 == 3) {
                bossUnlockedWave.put(
                        context.getId(),
                        firstWave);
            }
            boolean bossUnlocked = bossUnlockedWave.getOrDefault(
                    context.getId(), 0) == firstWave;
            context.write(
                    61952,
                    sixthWaveAdvanced(chapter, bossUnlocked),
                    0);
        } else {
            context.write(61952, chapterCleared(chapter), 0);
        }

        if (state.lastKilledMonsterId != 0) {
            context.write(
                    50757,
                    SceneForgetVisibleResp.newBuilder()
                            .addForgetIds(state.lastKilledMonsterId)
                            .build(),
                    0);
        }
    }

    @PlayerCmd
    public void rewardGuidanceTask(
            IPlayerContext context,
            TaskRewardReq request) {
        int taskId = rewardTaskId(context, request);
        if (taskId >= 200035 && taskId <= 200038
                || taskId >= 200100 && taskId <= 200111) {
            rewardChapter17Task(context, taskId);
            return;
        }
        if (taskId == 200001) {
            writeFirstBossTaskReward(context);
            learnedSkills(context.getId()).add(80140001L);
            return;
        }
        if (taskId == 200002) {
            writeLearnedSkillTaskReward(context);
            return;
        }
        if (taskId == 200004) {
            writeSecondBossTaskReward(context);
            return;
        }
        if (taskId == 200006) {
            writeThirdBossTaskReward(context);
            return;
        }
        if (taskId == 200007) {
            writeNewFightSkillTaskReward(context);
            return;
        }
        if (taskId == 200008) {
            writeFourthBossTaskReward(context);
            return;
        }
        if (taskId == 200009) {
            writeAutoSkillTaskReward(context);
            return;
        }
        if (taskId == 200010) {
            writeFourthLevelTaskReward(context);
            return;
        }
        if (taskId == 200011) {
            writeFifthLotteryTaskReward(context);
            return;
        }
        if (taskId == 200012) {
            writeSixthChapterTaskReward(context);
            return;
        }
        boolean laterMainTask =
                (taskId >= 200013 && taskId <= 200025)
                        || taskId == 200053
                        || taskId == 200054
                        || (taskId >= 200026 && taskId <= 200031)
                        || (taskId >= 200033 && taskId <= 200036);
        if (laterMainTask) {
            writeLaterMainTaskReward(context, taskId);
            return;
        }
        if (taskId != 200000
                && taskId != 200003
                && taskId != 200005) {
            return;
        }

        boolean firstTask = taskId == 200000;
        long gold = firstTask ? 5 : (taskId == 200003 ? 15 : 25);
        int nextTaskId = taskId + 1;
        context.write(
                50651,
                PurseUpdateResp.newBuilder()
                        .addItems(CurrencyItemVo.newBuilder()
                                .setType(2)
                                .setValue(gold))
                        .addItems(CurrencyItemVo.newBuilder()
                                .setType(22)
                                .setValue(0))
                        .build(),
                0);
        context.write(
                50406,
                RewardResp.newBuilder()
                        .setOperationType(50907)
                        .addRewardItemVos(RewardItemVo.newBuilder()
                                .setItemKey(2)
                                .setAmount(5)
                                .setGiveFlag(false))
                        .build(),
                0);
        context.write(
                53702,
                ReputationLvOnTaskRewardUpdateResp.newBuilder()
                        .setTaskId(taskId)
                        .setReputationLv(0)
                        .build(),
                0);
        if (firstTask) {
            context.write(60751, ByteString.EMPTY, 0);
        }
        context.write(
                50906,
                rewardedTaskUpdate(
                        taskId,
                        firstTask,
                        taskId == 200005 ? 1 : 4),
                0);
        context.write(50906, nextTaskUpdate(nextTaskId), 0);
    }

    /** 第七至九关主线领奖；奖励与后继任务来自 taskconfig 及对应抓包。 */
    private void writeLaterMainTaskReward(
            IPlayerContext context,
            int taskId) {
        if (taskId >= 200030 && !later(context.getId()).rewarded.add(taskId)) return;
        int rewardKey;
        int rewardAmount;
        int nextTaskId;
        int itemIndex = 0;
        switch (taskId) {
            case 200013:
            case 200014:
            case 200015:
            case 200017:
            case 200018:
                rewardKey = 100200;
                rewardAmount = 2;
                nextTaskId = taskId == 200018 ? 200020 : taskId + 1;
                break;
            case 200016:
                rewardKey = 100200;
                rewardAmount = 10;
                nextTaskId = 200017;
                break;
            case 200020:
                rewardKey = 101;
                rewardAmount = 1;
                nextTaskId = 200021;
                itemIndex = 16;
                break;
            case 200021:
                rewardKey = 100200;
                rewardAmount = 20;
                nextTaskId = 200053;
                break;
            case 200053:
                rewardKey = 100200;
                rewardAmount = 2;
                nextTaskId = 200022;
                break;
            case 200022:
                rewardKey = 100200;
                rewardAmount = 20;
                nextTaskId = 200023;
                break;
            case 200023:
                rewardKey = 100200;
                rewardAmount = 2;
                nextTaskId = 200024;
                break;
            case 200024:
                rewardKey = 101;
                rewardAmount = 1;
                nextTaskId = 200025;
                itemIndex = 22;
                break;
            case 200025:
                rewardKey = 100200;
                rewardAmount = 10;
                nextTaskId = 200054;
                break;
            case 200054:
                rewardKey = 100200;
                rewardAmount = 2;
                nextTaskId = 200026;
                break;
            case 200026:
                rewardKey = 100200;
                rewardAmount = 20;
                nextTaskId = 200027;
                break;
            case 200027:
                rewardKey = 102;
                rewardAmount = 1;
                nextTaskId = 200028;
                itemIndex = 23;
                break;
            case 200028:
                rewardKey = 100200;
                rewardAmount = 2;
                nextTaskId = 200029;
                break;
            case 200029:
                rewardKey = 100200;
                rewardAmount = 2;
                nextTaskId = 200030;
                break;
            case 200030:
                rewardKey = 10060;
                rewardAmount = 100;
                nextTaskId = 200031;
                itemIndex = ITEM_10060_INDEX;
                break;
            case 200031:
                rewardKey = 100200;
                rewardAmount = 30;
                nextTaskId = 200033;
                break;
            case 200033:
                rewardKey = 10060;
                rewardAmount = 100;
                nextTaskId = 200034;
                itemIndex = ITEM_10060_INDEX;
                break;
            case 200034:
                rewardKey = 10060;
                rewardAmount = 100;
                nextTaskId = 200035;
                itemIndex = ITEM_10060_INDEX;
                break;
            case 200035:
                rewardKey = 10060;
                rewardAmount = 100;
                nextTaskId = 200036;
                itemIndex = ITEM_10060_INDEX;
                break;
            case 200036:
                rewardKey = 102;
                rewardAmount = 5;
                nextTaskId = 200037;
                itemIndex = 27;
                break;
            default:
                return;
        }

        long now = System.currentTimeMillis();
        UpdateItem update;
        if (rewardKey == 100200) {
            update = changeNewFightSkillEnergy(
                    context, rewardAmount, 11, now);
        } else if (rewardKey == 10060) {
            update = grantStackedItem10060(context, rewardAmount, now);
        } else if (rewardKey == 101 || rewardKey == 102) {
            update = changeAlchemyMaterial(context, rewardKey, rewardAmount, itemIndex);
        } else {
            update = UpdateItem.newBuilder()
                    .setItemIndex(itemIndex)
                    .setPackItem(PackItemVo.newBuilder()
                            .setObjectId(context.getId() * 1000 + itemIndex)
                            .setKey(rewardKey)
                            .setSize(rewardAmount)
                            .setCreateTime(now)
                            .setLastGainTime(now))
                    .build();
        }
        context.write(50402, packUpdate(50907, update), 0);
        context.write(
                50406,
                RewardResp.newBuilder()
                        .setOperationType(50907)
                        .addRewardItemVos(RewardItemVo.newBuilder()
                                .setItemKey(rewardKey)
                                .setAmount(rewardAmount)
                                .setGiveFlag(false))
                        .build(),
                0);
        context.write(
                53702,
                ReputationLvOnTaskRewardUpdateResp.newBuilder()
                        .setTaskId(taskId)
                        .setReputationLv(reputationLevels.getOrDefault(context.getId(), 0))
                        .build(),
                0);
        if (taskId == 200026) {
            writeMedicineCauldronOpen(context);
        }
        if (taskId == 200029) {
            writeReputationOpen(context);
        }
        if (taskId == 200034) {
            later(context.getId()).towerOpen = true;
            context.write(50852, ModuleNewOpenResp.newBuilder().addOpens(3304).addOpens(3501).addOpens(4701).build(), 0);
            writeFireTowerInfo(context);
        }
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(
                                taskId,
                                TaskPhase.REWARDED,
                                rewardedProgress(taskId)))
                        .build(),
                0);
        if (nextTaskId != 0) {
            acceptNextMainTask(context, nextTaskId);
        }
    }

    /**
     * 第 11 关到达任务 200026 领奖后开万兽鼎（抓包第二次 idx 8698-8700）。
     * 模块 1622，未购买：haveBuy=false、activeLevel=0。
     */
    private void writeMedicineCauldronOpen(IPlayerContext context) {
        context.write(
                50852,
                ModuleNewOpenResp.newBuilder()
                        .addOpens(MEDICINE_CAULDRON_MODULE_ID)
                        .build(),
                0);
        context.write(
                50384,
                cumulateLoginDays(new int[] {
                        1600, 1601, 1608, 4304, MEDICINE_CAULDRON_MODULE_ID,
                        42011, 3101, 3102, 101, 102, 1510, 4007, 103, 4008,
                        4201, 4202, 107, 4203, 4204, 112, 4401, 115, 4404,
                        3701, 4411, 4415
                }),
                0);
        context.write(61501, unboughtMedicineCauldronInfo(), 0);
    }

    /**
     * 第 11 关 Boss 任务 200029 领奖后开名望（抓包第二次 idx 9056-9063）。
     * 模块 23011 挂机奖励、4308 斗铠、10 名望；53703 斗师声望 1 级。
     * 客户端 OnReputationInitResp 会 CheckShow(10)，弹出声望引导手指。
     */
    private void writeReputationOpen(IPlayerContext context) {
        context.write(
                50852,
                ModuleNewOpenResp.newBuilder()
                        .addOpens(HANG_UP_REWARD_MODULE_ID)
                        .addOpens(MAIN_EQUIP_MODULE_ID)
                        .addOpens(REPUTATION_MODULE_ID)
                        .build(),
                0);
        context.write(
                50384,
                cumulateLoginDays(new int[] {
                        1600, 1601, 1608, 4304, MEDICINE_CAULDRON_MODULE_ID,
                        42011, 3101, 3102, HANG_UP_REWARD_MODULE_ID,
                        101, 102, 1510, 4007, 103, 4008,
                        4201, 4202, 107, 4203, 4204, 112, 4401, 115, 4404,
                        3701, 4411, 4415, MAIN_EQUIP_MODULE_ID,
                        REPUTATION_MODULE_ID
                }),
                0);
        context.write(53703, initialDouShiReputation(), 0);
        reputationLevels.put(context.getId(), 1);
        context.write(
                75353,
                mainEquipInfo(context.getId()),
                0);
        List<MainEquipVO> equips = firstHangUpEquips(context.getId());
        pendingHangUpEquips.put(context.getId(), equips);
        long now = System.currentTimeMillis();
        context.write(
                61951,
                MainMapChapterInfoResp.newBuilder()
                        .setMainMapChapterId(10300201)
                        .setChangeReason(1)
                        .setHasReward(true)
                        .setStageTime(9)
                        .setLastStageTime(9)
                        .setLoseBackId(10300101)
                        .setHistoryTopId(10300105)
                        .setMainMapChapterHangUpCalTime(2)
                        .setNextHangUpRewardTime(now + 60_000L)
                        .setKillMonsterPreHour(180)
                        .addAllMainMapChapterHangUpReward(
                                hangUpRewardItems(equips))
                        .build(),
                0);
    }

    /**
     * 领取挂机斗铠（抓包第二次 idx 9095-9101）。两件品质 0 装备后完成 200030。
     */
    @PlayerCmd
    public void takeHangUpReward(
            IPlayerContext context,
            MainMapTakeHangUpRewardReq request) {
        List<MainEquipVO> pending =
                pendingHangUpEquips.remove(context.getId());
        if (pending == null || pending.isEmpty()) {
            return;
        }
        MainEquipState equipment = mainEquipState(context.getId());
        List<MainEquipVO> equips = new ArrayList<>();
        for (MainEquipVO equip : pending) {
            if (!equipment.owns(equip.getObjectId())) {
                equipment.bag.put(equip.getObjectId(), equip);
                equips.add(equip);
            }
        }
        if (equips.isEmpty()) return;
        long now = System.currentTimeMillis();
        context.write(
                75355,
                MainEquipBagAddResp.newBuilder()
                        .setFlag(true)
                        .addAllAddList(equips)
                        .build(),
                0);
        RewardResp.Builder reward = RewardResp.newBuilder()
                .setOperationType(HANG_UP_REWARD_OPERATION);
        for (RewardItemVo item : hangUpRewardItems(equips)) {
            reward.addRewardItemVos(item);
        }
        context.write(50406, reward.build(), 0);
        context.write(
                61954,
                MainMapTakeHangUpRewardResp.newBuilder()
                        .addAllMainMapChapterHangUpReward(
                                hangUpRewardItems(equips))
                        .setMainMapChapterHangUpCalTime(10283)
                        .setNextHangUpRewardTime(now + 60_000L)
                        .build(),
                0);
        context.write(60751, BattlePassInfoResp.getDefaultInstance(), 0);
        tryFinishAcceptedMainTask(context, 200030, 2);
        if (reputationLevels.getOrDefault(context.getId(), 0) >= 2) {
            later(context.getId()).watchedEquips += equips.size();
            refreshChapter17Tasks(context);
        }
    }

    /** 抓包 idx 9126-9128；换装 idx 10060-10063：退旧装备后刷新对应槽位。 */
    @PlayerCmd
    public void dressMainEquip(IPlayerContext context, MainEquipDressReq request) {
        int heroIndex = request.getHeroIndex();
        if (heroIndex != 0 && (heroIndex != SECOND_HERO_INDEX
                || !secondHeroUnlocked.contains(context.getId()))) {
            return;
        }
        MainEquipState state = mainEquipState(context.getId());
        MainEquipVO equip = state.bag.get(request.getObjectId());
        if (equip == null) {
            LOGGER.info("MainEquip dress ignored: player={}, objectId={} not in owned bag",
                    context.getId(), request.getObjectId());
            return;
        }
        com.fasterxml.jackson.databind.JsonNode definition = null;
        for (com.fasterxml.jackson.databind.JsonNode row : Chapter17Data.DATA.path("mainEquips")) {
            if (row.path("Id").asInt() == equip.getEquipId()) {
                definition = row;
                break;
            }
        }
        if (definition == null || definition.path("Type").asInt() != request.getPosition()) {
            LOGGER.info("MainEquip dress ignored: player={}, equipId={}, wrong position={}",
                    context.getId(), equip.getEquipId(), request.getPosition());
            return;
        }
        Map<Integer, MainEquipSlotVO> slots = state.heroes.computeIfAbsent(
                heroIndex, ignored -> new LinkedHashMap<>());
        MainEquipSlotVO previous = slots.get(request.getPosition());
        // 强化属于槽位。换装不把新装备的 0 级覆盖已有槽位等级，旧装备入包归零。
        int level = previous == null ? 0 : previous.getLevel();
        long exp = previous == null ? 0 : previous.getExp();
        MainEquipSlotVO slot = MainEquipSlotVO.newBuilder()
                .setPosition(request.getPosition()).setLevel(level).setExp(exp)
                .setVo(equip.toBuilder().setLevel(level).setExp(exp)).build();
        state.bag.remove(equip.getObjectId());
        slots.put(request.getPosition(), slot);
        context.write(75354, MainEquipBagReduceResp.newBuilder()
                .setType(1).addReduceList(equip.getObjectId()).build(), 0);
        if (previous != null && previous.hasVo()) {
            MainEquipVO returned = previous.getVo().toBuilder().setLevel(0).setExp(0).build();
            state.bag.put(returned.getObjectId(), returned);
            context.write(75355, MainEquipBagAddResp.newBuilder()
                    .setFlag(false).addAddList(returned).build(), 0);
        }
        context.write(75356, MainEquipPositionInfoResp.newBuilder()
                .setHeroIndex(heroIndex).setPosition(request.getPosition())
                .setMain(request.getMain()).setVo(slot).build(), 0);
        LOGGER.info("MainEquip dressed: player={}, hero={}, position={}, objectId={}, equipId={}",
                context.getId(), heroIndex, request.getPosition(), equip.getObjectId(), equip.getEquipId());
    }

    /** 快装弹窗在全部领取后还会发送 61999；抓包 idx 9108/9125，无二次发奖回包。 */
    @PlayerCmd
    public void confirmClaimedMainEquip(IPlayerContext context,
            com.doupo.protocol.MainMapTakeHangUpMainEquipRewardReq request) {
        MainEquipState state = mainEquipState(context.getId());
        if (state.owns(request.getObjectId())) return;
        LOGGER.info("MainEquip claim ignored: player={}, objectId={} has not been collected",
                context.getId(), request.getObjectId());
    }

    private MainEquipState mainEquipState(long playerId) {
        return mainEquipStates.computeIfAbsent(playerId, ignored -> new MainEquipState());
    }

    /** 6.9 MainEquipModel.CalculateLevelUp：下一等级条件、当前等级经验、下一个 Mark。 */
    @PlayerCmd
    public void levelUpMainEquip(IPlayerContext context, com.doupo.protocol.MainEquipLevelUpReq request) {
        Map<Integer, MainEquipSlotVO> slots = mainEquipState(context.getId()).heroes.get(request.getHeroIndex());
        if (slots == null) return;
        MainEquipSlotVO before = slots.get(request.getPosition());
        if (before == null || !before.hasVo()) return;
        int level = before.getLevel();
        int max = Chapter17Data.row("mainEquips", "Id", before.getVo().getEquipId()).path("MaxLevel").asInt();
        int target = level + 1;
        if (request.getQuickUpgrade()) {
            target = max;
            for (com.fasterxml.jackson.databind.JsonNode row : Chapter17Data.CONTINUATION.path("equipStrengthNeed")) {
                if (row.path("Level").asInt() > level && row.path("Mark").asInt() == 1) {
                    target = Math.min(target, row.path("Level").asInt());
                }
            }
        }
        com.fasterxml.jackson.databind.JsonNode material = Chapter17Data.CONTINUATION.path("equipStrengthMaterial").get(0);
        int itemId = material.path("ItemId").asInt();
        long available = chapter17ItemCount(context, itemId);
        long spent = 0;
        long exp = before.getExp();
        while (level < Math.min(max, target)) {
            com.fasterxml.jackson.databind.JsonNode next = Chapter17Data.row("equipStrengthNeed", "Level", level + 1);
            boolean allowed = true;
            for (com.fasterxml.jackson.databind.JsonNode condition : next.path("Condition")) {
                if (!"HERO_LEVEL_CONDITION".equals(condition.path("Type").asText())
                        || heroLevels.getOrDefault(context.getId(), 2) < condition.path("Context").path("Level").asInt()) {
                    allowed = false;
                }
            }
            long cost = Math.max(0, Chapter17Data.row("equipStrengthNeed", "Level", level).path("Exp").asLong() - exp);
            if (!allowed || material.path("Exp").asInt() != 1 || cost > available - spent) break;
            spent += cost;
            level++;
            exp = 0;
        }
        if (level == before.getLevel()) return;
        MainEquipSlotVO updated = before.toBuilder().setLevel(level).setExp(exp)
                .setVo(before.getVo().toBuilder().setLevel(level).setExp(exp)).build();
        if (spent > 0) context.write(50402, packUpdate(75355, changeChapter17Item(context, itemId, -spent)), 0);
        slots.put(request.getPosition(), updated);
        context.write(75358, com.doupo.protocol.MainEquipLevelUpResp.newBuilder().setHeroIndex(request.getHeroIndex())
                .setPosition(request.getPosition()).setVo(updated).build(), 0);
        context.write(75356, MainEquipPositionInfoResp.newBuilder().setHeroIndex(request.getHeroIndex())
                .setPosition(request.getPosition()).setVo(updated).build(), 0);
        Chapter17Progress state = later(context.getId());
        if (state.tasks.contains(200106) && !state.rewarded.contains(200106)) {
            state.taskEquipMaterialConsumed = Math.min(10, state.taskEquipMaterialConsumed + spent);
        }
        refreshChapter17Tasks(context);
        LOGGER.info("MainEquip strengthened: player={}, slot={}, level={}->{}, materialConsumed={}",
                context.getId(), request.getPosition(), before.getLevel(), level, spent);
    }

    /** 抓包75390→75354(type=2)→50402→75391；只分解自己包内未锁定的装备。 */
    @PlayerCmd
    public void decomposeMainEquip(IPlayerContext context, com.doupo.protocol.MainEquipDecomposeReq request) {
        MainEquipState state = mainEquipState(context.getId());
        Set<Long> ids = new java.util.LinkedHashSet<>(request.getObjectIdsList());
        long amount = 0;
        for (long id : ids) {
            MainEquipVO equip = state.bag.get(id);
            if (equip == null || equip.getLock()) return;
            amount += Chapter17Data.row("mainEquips", "Id", equip.getEquipId()).path("DecomposeExp").asLong();
        }
        if (ids.isEmpty() || amount <= 0) return;
        int itemId = Chapter17Data.CONTINUATION.path("equipStrengthMaterial").get(0).path("ItemId").asInt();
        UpdateItem material = changeChapter17Item(context, itemId, amount);
        for (long id : ids) state.bag.remove(id);
        context.write(75354, MainEquipBagReduceResp.newBuilder().setType(2).addAllReduceList(ids).build(), 0);
        context.write(50402, packUpdate(75354, material), 0);
        context.write(75391, com.doupo.protocol.MainEquipDecomposeResp.newBuilder().addAllObjectIds(ids)
                .setClientParam(request.getClientParam()).addAssetList(RewardItemVo.newBuilder()
                        .setItemKey(itemId).setAmount(amount)).build(), 0);
    }

    private long chapter17ItemCount(IPlayerContext context, int itemId) {
        UpdateItem item = context instanceof com.doupo.server.foundation.player.PlayerConnectionContext
                ? ((com.doupo.server.foundation.player.PlayerConnectionContext) context).itemStack(itemId) : null;
        return item == null ? later(context.getId()).items.getOrDefault(itemId, 0L) : item.getPackItem().getSize();
    }

    /** 抓包10436–10440：普通改名消费改名卡，成功后才推进200105。 */
    @PlayerCmd
    public void changePlayerName(IPlayerContext context, com.doupo.protocol.ChangeNameReq request) {
        if (request.getOpType() != 0) return;
        int maxLength = 0;
        com.fasterxml.jackson.databind.JsonNode cost = null;
        for (com.fasterxml.jackson.databind.JsonNode row : Chapter17Data.CONTINUATION.path("nameSettings")) {
            if ("word.maxNameLength".equals(row.path("Id").asText())) maxLength = row.path("IntValue").asInt();
            if ("changeNameConsumeResource".equals(row.path("Id").asText())) cost = row.path("AssetValue").get(0);
        }
        String name = request.getName();
        Chapter17Progress state = later(context.getId());
        if (name.trim().isEmpty() || name.length() > maxLength || name.equals(state.playerName)
                || name.chars().anyMatch(c -> Character.isDigit(c) || Character.isISOControl(c)) || cost == null) return;
        int itemId = cost.path("Id").asInt();
        long amount = cost.path("Amount").asLong();
        if (amount <= 0 || chapter17ItemCount(context, itemId) < amount) return;
        context.write(50402, packUpdate(51351, changeChapter17Item(context, itemId, -amount)), 0);
        state.playerName = name;
        context.write(51352, com.doupo.protocol.ChangeNameResp.newBuilder().setPlayerId(context.getId()).setName(name).build(), 0);
        refreshChapter17Tasks(context);
    }

    MainEquipInfoResp mainEquipInfo(long playerId) {
        MainEquipState state = mainEquipState(playerId);
        MainEquipInfoResp.Builder info = MainEquipInfoResp.newBuilder()
                .setPoolId(1).addAllPacks(state.bag.values());
        for (Map.Entry<Integer, Map<Integer, MainEquipSlotVO>> hero : state.heroes.entrySet()) {
            info.addHeroVoList(MainEquipHeroVO.newBuilder().setHeroId(hero.getKey())
                    .addAllSlotVoList(hero.getValue().values()));
        }
        return info.build();
    }

    private static final class MainEquipState {
        final Map<Long, MainEquipVO> bag = new LinkedHashMap<>();
        final Map<Integer, Map<Integer, MainEquipSlotVO>> heroes = new LinkedHashMap<>();

        MainEquipState() {
            heroes.put(0, new LinkedHashMap<>());
        }

        boolean owns(long objectId) {
            if (bag.containsKey(objectId)) return true;
            for (Map<Integer, MainEquipSlotVO> slots : heroes.values()) {
                for (MainEquipSlotVO slot : slots.values()) {
                    if (slot.hasVo() && slot.getVo().getObjectId() == objectId) return true;
                }
            }
            return false;
        }
    }

    /**
     * 斗师声望升级（抓包第二次 idx 9883-9885）。1→2 消耗 10060×700。
     */
    @PlayerCmd
    public void reputationLvUp(
            IPlayerContext context,
            ReputationLvUpReq request) {
        if (request.getRange() != 1) {
            return;
        }
        long playerId = context.getId();
        int level = reputationLevels.getOrDefault(playerId, 0);
        if (level < 1) {
            return;
        }
        int cost = reputationUpgradeCost(level);
        if (cost <= 0
                || item10060Counts.getOrDefault(playerId, 0) < cost) {
            return;
        }
        long now = System.currentTimeMillis();
        context.write(
                50402,
                packUpdate(
                        REPUTATION_LV_UP_OPERATION,
                        consumeStackedItem10060(context, cost, now)),
                0);
        int nextLevel = level + 1;
        reputationLevels.put(playerId, nextLevel);
        context.write(
                53705,
                ReputationLvUpResp.newBuilder()
                        .setUpdateRangeVo(ReputationRangeVo.newBuilder()
                                .setRange(1)
                                .setRangeInsideLv(nextLevel)
                                .setRangeInsideNotSeeOldLv(-1)
                                .build())
                        .build(),
                0);
        if (nextLevel == 2) {
            openReputationTwoTasks(context);
        } else {
            refreshChapter17Tasks(context);
            GuidanceState current = guidanceStates.get(playerId);
            if (nextLevel == 3 && hasPassedChapter(playerId, 10301005) && current != null
                    && current.chapterId >= 10301001 && current.chapterId <= 10301005) {
                context.write(61952, MainMapPassChapterUpdateResp.newBuilder().setMainMapChapterId(current.chapterId)
                        .setNextChallengeId(10400101).setHistoryTopId(10301005).setLoseBackId(10300901)
                        .setStageTime(9).setLastStageTime(8).setChangeReason(4).build(), 0);
            }
        }
    }

    void setItem10060Count(long playerId, int size) {
        if (size <= 0) {
            item10060Counts.remove(playerId);
        } else {
            item10060Counts.put(playerId, size);
        }
    }

    private static ReputationInitResp initialDouShiReputation() {
        return ReputationInitResp.newBuilder()
                .addRangeVoList(ReputationRangeVo.newBuilder()
                        .setRange(1)
                        .setRangeInsideLv(1)
                        .setRangeInsideNotSeeOldLv(0)
                        .build())
                .build();
    }

    private static MedicineCauldronInfoResp unboughtMedicineCauldronInfo() {
        return MedicineCauldronInfoResp.newBuilder()
                .setHaveBuy(false)
                .setActiveLevel(0)
                .setCanUpgradeLevel(0)
                .addHeroIndex2DailyExtraExpList(
                        IntegerAndLongPairEntry.newBuilder()
                                .setKey(0)
                                .setValue(0)
                                .build())
                .build();
    }

    /** 客户端打开万兽鼎后会拉历史消耗（抓包第二次 idx 8703/8704）。 */
    @PlayerCmd
    public MedicineCauldronHistoryConsumeResp medicineCauldronHistoryConsume(
            IPlayerContext context,
            MedicineCauldronHistoryConsumeReq request) {
        return MedicineCauldronHistoryConsumeResp.newBuilder()
                .setHistoryConsumeItemNum(0)
                .build();
    }

    /** 第一关 Boss 任务 200001 领奖，按抓包 idx 329-343 下发。 */
    private void writeFirstBossTaskReward(IPlayerContext context) {
        long now = System.currentTimeMillis();
        long playerSceneUnitId = context.getId() * 1000 + 1;

        context.write(
                50402,
                packUpdate(
                        50907,
                        UpdateItem.newBuilder()
                                .setItemIndex(2)
                                .setPackItem(PackItemVo.newBuilder()
                                        .setObjectId(context.getId() * 1000 + 2)
                                        .setKey(80140000)
                                        .setSize(1)
                                        .setCreateTime(now)
                                        .setLastGainTime(now))
                                .build()),
                0);
        context.write(
                50406,
                RewardResp.newBuilder()
                        .setOperationType(50907)
                        .addRewardItemVos(RewardItemVo.newBuilder()
                                .setItemKey(80140000)
                                .setAmount(1)
                                .setGiveFlag(false))
                        .build(),
                0);
        context.write(
                53702,
                ReputationLvOnTaskRewardUpdateResp.newBuilder()
                        .setTaskId(200001)
                        .setReputationLv(0)
                        .build(),
                0);
        context.write(
                50402,
                packUpdate(
                        75004,
                        UpdateItem.newBuilder()
                                .setItemIndex(2)
                                .build()),
                0);

        SkillVo skill = SkillVo.newBuilder()
                .setBaseId(80140001)
                .setStar(1)
                .setActStar(0)
                .setAwaken(false)
                .setSelElement(0)
                .build();
        context.write(
                75011,
                HeroSkillActResp.newBuilder()
                        .setHeroIndex(0)
                        .setShowTips(true)
                        .setPlayerSkill(PlayerSkillVo.newBuilder()
                                .setBaseId(80140001)
                                .addHeroSkills(PlayerHeroSkillVo.newBuilder()
                                        .setHeroIdx(-1)
                                        .setHeroSkill(skill)))
                        .build(),
                0);
        context.write(
                75049,
                HeroSkillStarTotalMaxHisUpdateResp.newBuilder()
                        .setHeroIndex(0)
                        .setSkillStarTotalMaxHis(1)
                        .build(),
                0);
        context.write(
                75039,
                HeroSkillBaseUpdateResp.newBuilder()
                        .addBaseIds(80140001)
                        .build(),
                0);
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(
                                200001,
                                TaskPhase.REWARDED,
                                1))
                        .build(),
                0);

        HeroStatVo heroStats = HeroStatVo.newBuilder()
                .setHeroIndex(0)
                .addStats(stat(107002, 3))
                .addStats(stat(102001, 114.5))
                .addStats(stat(105001, 1))
                .addStats(stat(106001, 1))
                .addStats(stat(103001, 13044.45))
                .addStats(stat(101001, 498.44454))
                .addStats(stat(104001, 100))
                .build();
        context.write(
                50455,
                HeroStatUpdateResp.newBuilder()
                        .setHeroVo(heroStats)
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103011, 12812.791356673959))
                        .addAttrList(attribute(103001, 13044.45))
                        .setTargetId(playerSceneUnitId)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(101001, 498.44454))
                        .setTargetId(playerSceneUnitId)
                        .build(),
                0);
        grantPlayerStats(context.getId(), 498.44454, 13044.45);
        context.write(
                52351,
                PlayerFightForceResp.newBuilder()
                        .setPlayerFightForce(10772)
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                75151,
                HeroFightForceResp.newBuilder()
                        .addHeroVoList(HeroFightForceVo.newBuilder()
                                .setHeroIndex(0)
                                .setFightForce(10772))
                        .build(),
                0);
        context.write(50906, firstBossRewardFollowUpTasks(), 0);
    }

    private static PackUpdateResp packUpdate(
            int operationType,
            UpdateItem... items) {
        PackUpdateResp.Builder response = PackUpdateResp.newBuilder()
                .setOperationType(operationType);
        for (int packType = 1; packType <= 9; packType++) {
            PackUpdateVo.Builder pack = PackUpdateVo.newBuilder()
                    .setPackType(packType);
            if (packType == 1) {
                pack.addAllUpdateItems(Arrays.asList(items));
            }
            response.addPacks(pack);
        }
        return response.build();
    }

    /**
     * 主线 10060 堆叠。官服抓包从 itemIndex=26、size=100 起累加。
     */
    private UpdateItem grantStackedItem10060(
            IPlayerContext context,
            int amount,
            long now) {
        long playerId = context.getId();
        int size = item10060Counts.merge(playerId, amount, Integer::sum);
        return UpdateItem.newBuilder()
                .setItemIndex(ITEM_10060_INDEX)
                .setPackItem(PackItemVo.newBuilder()
                        .setObjectId(playerId * 1000 + ITEM_10060_INDEX)
                        .setKey(10060)
                        .setSize(size)
                        .setCreateTime(now)
                        .setLastGainTime(now))
                .build();
    }

    private UpdateItem consumeStackedItem10060(
            IPlayerContext context,
            int amount,
            long now) {
        long playerId = context.getId();
        int size = item10060Counts.merge(playerId, -amount, Integer::sum);
        if (size < 0) {
            item10060Counts.put(playerId, 0);
            throw new IllegalStateException(
                    "Item 10060 would become negative: player=" + playerId);
        }
        if (size == 0) {
            item10060Counts.remove(playerId);
            return UpdateItem.newBuilder()
                    .setItemIndex(ITEM_10060_INDEX)
                    .build();
        }
        return UpdateItem.newBuilder()
                .setItemIndex(ITEM_10060_INDEX)
                .setPackItem(PackItemVo.newBuilder()
                        .setObjectId(playerId * 1000 + ITEM_10060_INDEX)
                        .setKey(10060)
                        .setSize(size)
                        .setCreateTime(now)
                        .setLastGainTime(now))
                .build();
    }

    private static int reputationUpgradeCost(int currentLevel) {
        if (currentLevel == 1) {
            return DOUSHI_LV1_UPGRADE_COST;
        }
        if (currentLevel == 2) {
            return 800;
        }
        return 0;
    }

    private static List<MainEquipVO> firstHangUpEquips(long playerId) {
        return Arrays.asList(
                mainEquip(
                        playerId * 1000 + 200,
                        12,
                        randomAttr(1, "nomalAtkRatePatamsReduce", 70, 0, 1, 90),
                        randomAttr(2, "beTreatDmgRate", 20, 0, 1, 45)),
                mainEquip(
                        playerId * 1000 + 201,
                        15,
                        randomAttr(1, "dotHurtRate", 40, 0, 1, 180),
                        randomAttr(2, "dotHurtRate", 20, 0, 1, 180)));
    }

    private static MainEquipVO mainEquip(
            long objectId,
            int equipId,
            MainEquipRandomAttrVO... attrs) {
        MainEquipVO.Builder builder = MainEquipVO.newBuilder()
                .setObjectId(objectId)
                .setEquipId(equipId)
                .setDropLevel(0)
                .setLevel(0)
                .setExp(0)
                .setLock(false);
        for (MainEquipRandomAttrVO attr : attrs) {
            builder.addRandomAttrList(attr);
        }
        return builder.build();
    }

    private static MainEquipRandomAttrVO randomAttr(
            int id,
            String attrType,
            double value,
            int quality,
            int min,
            int max) {
        return MainEquipRandomAttrVO.newBuilder()
                .setId(id)
                .setAttrType(attrType)
                .setValue(value)
                .setQuality(quality)
                .setMin(min)
                .setMax(max)
                .build();
    }

    private static List<RewardItemVo> hangUpRewardItems(List<MainEquipVO> equips) {
        List<RewardItemVo> items = new ArrayList<RewardItemVo>();
        for (MainEquipVO equip : equips) {
            items.add(RewardItemVo.newBuilder()
                    .setItemKey(equip.getEquipId())
                    .setAmount(1)
                    .setMainEquipVo(equip)
                    .setGiveFlag(false)
                    .build());
        }
        return items;
    }

    /**
     * 统一变更纳戒能量并生成客户端背包更新。沿用当前真实槽位；数量归零时
     * 只下发 itemIndex 删除该堆叠，下次获得时才采用 preferredItemIndex。
     */
    private UpdateItem changeNewFightSkillEnergy(
            IPlayerContext context,
            int delta,
            int preferredItemIndex,
            long now) {
        long playerId = context.getId();
        int before = newFightSkillEnergy.getOrDefault(playerId, 0);
        int after = before + delta;
        if (after < 0) {
            throw new IllegalStateException(
                    "New fight skill energy would become negative: player="
                            + playerId + ", before=" + before
                            + ", delta=" + delta);
        }

        int itemIndex;
        long createTime;
        if (before > 0) {
            itemIndex = newFightSkillEnergyItemIndexes.getOrDefault(
                    playerId, preferredItemIndex);
            createTime = newFightSkillEnergyCreateTimes.getOrDefault(
                    playerId, now);
        } else {
            itemIndex = preferredItemIndex;
            createTime = now;
        }

        UpdateItem.Builder update = UpdateItem.newBuilder()
                .setItemIndex(itemIndex);
        if (after > 0) {
            newFightSkillEnergy.put(playerId, after);
            newFightSkillEnergyItemIndexes.put(playerId, itemIndex);
            newFightSkillEnergyCreateTimes.put(playerId, createTime);
            update.setPackItem(PackItemVo.newBuilder()
                    .setObjectId(playerId * 1000 + itemIndex)
                    .setKey(100200)
                    .setSize(after)
                    .setCreateTime(createTime)
                    .setLastGainTime(now));
        } else {
            newFightSkillEnergy.remove(playerId);
            newFightSkillEnergyItemIndexes.remove(playerId);
            newFightSkillEnergyCreateTimes.remove(playerId);
        }
        return update.build();
    }

    private static StatVo stat(int type, double value) {
        return StatVo.newBuilder()
                .setType(type)
                .setValue(value)
                .build();
    }

    /** 抓包 idx 343：战力类任务刷新，并开启下一条主线 200002。 */
    private static TaskUpdateResp firstBossRewardFollowUpTasks() {
        TaskUpdateResp.Builder response = TaskUpdateResp.newBuilder();
        for (int taskId : new int[] {
                4002009, 4002004, 4002003, 4002002,
                4002008, 4002007, 4002006
        }) {
            response.addTaskVos(
                    task(taskId, TaskPhase.PROGRESS, 10772));
        }
        response.addTaskVos(task(200002, TaskPhase.PROGRESS, 0));
        response.addTaskVos(task(4002005, TaskPhase.PROGRESS, 10772));
        return response.build();
    }

    /**
     * 抽到吹火掌 80130001 激活时的属性。抓包 idx 8740-8744：
     * 攻击 1920.75、气血 49836.3、战力 26545。
     */
    private void writeBlowPalmActStats(IPlayerContext context) {
        if (grantedAttack.getOrDefault(context.getId(), 0d)
                >= 1920.7490400000002) {
            return;
        }
        writeCapturedHeroStats(
                context,
                76,
                761,
                49836.299999999996,
                1920.7490400000002,
                43140.354162807576,
                26545);
    }

    /**
     * 把吹火掌装进方案后的属性。抓包 idx 8841-8845：攻击 1999.62、
     * 气血 51848.8、战力 27360。焰分同装时走 34959，不再重复刷这份。
     */
    private void maybeWriteBlowPalmEquipStats(
            IPlayerContext context,
            HeroSkillSchemaUpdateReq request) {
        if (!schemaHasSkill(request, SECOND_SKILL_BASE_ID)
                || schemaHasSkill(request, YANFEN_SKILL_BASE_ID)
                || grantedAttack.getOrDefault(context.getId(), 0d)
                        >= 1999.6186) {
            return;
        }
        writeCapturedHeroStats(
                context,
                90,
                796,
                51848.799999999996,
                1999.6186,
                44668.13832721485,
                27360);
    }

    private void writeCapturedHeroStats(
            IPlayerContext context,
            int defend,
            double atkBonus,
            double maxHp,
            double attack,
            double currentHp,
            double fightForce) {
        long sceneUnitId = context.getId() * 1000 + 1;
        context.write(
                50455,
                HeroStatUpdateResp.newBuilder()
                        .setHeroVo(HeroStatVo.newBuilder()
                                .setHeroIndex(0)
                                .addStats(stat(107002, defend))
                                .addStats(stat(102001, atkBonus))
                                .addStats(stat(105001, 1))
                                .addStats(stat(106001, 1))
                                .addStats(stat(103001, maxHp))
                                .addStats(stat(101001, attack))
                                .addStats(stat(104001, 100)))
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103011, currentHp))
                        .addAttrList(attribute(103001, maxHp))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(101001, attack))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        grantPlayerStats(context.getId(), attack, maxHp);
        noteFightForce(context.getId(), fightForce);
        context.write(
                52351,
                PlayerFightForceResp.newBuilder()
                        .setPlayerFightForce(fightForce)
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                75151,
                HeroFightForceResp.newBuilder()
                        .addHeroVoList(HeroFightForceVo.newBuilder()
                                .setHeroIndex(0)
                                .setFightForce(fightForce))
                        .build(),
                0);
    }

    /** 装配玄阶低级斗技后的属性与战力刷新，对应抓包 idx 353-358。 */
    private void writeLearnedSkillStats(IPlayerContext context) {
        long playerSceneUnitId = context.getId() * 1000 + 1;
        context.write(
                50455,
                HeroStatUpdateResp.newBuilder()
                        .setHeroVo(HeroStatVo.newBuilder()
                                .setHeroIndex(0)
                                .addStats(stat(107002, 10))
                                .addStats(stat(102001, 132))
                                .addStats(stat(105001, 1))
                                .addStats(stat(106001, 1))
                                .addStats(stat(103001, 14050.7))
                                .addStats(stat(101001, 537.2818))
                                .addStats(stat(104001, 100)))
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103011, 13801.1711889132))
                        .addAttrList(attribute(103001, 14050.7))
                        .setTargetId(playerSceneUnitId)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(101001, 537.2818))
                        .setTargetId(playerSceneUnitId)
                        .build(),
                0);
        grantPlayerStats(context.getId(), 537.2818, 14050.7);
        context.write(
                52351,
                PlayerFightForceResp.newBuilder()
                        .setPlayerFightForce(11176)
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                75151,
                HeroFightForceResp.newBuilder()
                        .addHeroVoList(HeroFightForceVo.newBuilder()
                                .setHeroIndex(0)
                                .setFightForce(11176))
                        .build(),
                0);

        TaskUpdateResp.Builder tasks = TaskUpdateResp.newBuilder();
        for (int taskId : new int[] {
                4002009, 4002004, 4002003, 4002002,
                4002008, 4002007, 4002006, 4002005
        }) {
            tasks.addTaskVos(
                    task(taskId, TaskPhase.PROGRESS, 11176));
        }
        context.write(50906, tasks.build(), 0);
    }

    /** 第二次装配：槽位 2001 装上 80130011 后的属性/战力/任务刷新，对应抓包 idx 1110-1120。 */
    private void writeSecondSkillEquip(IPlayerContext context) {
        long playerSceneUnitId = context.getId() * 1000 + 1;
        context.write(
                75005,
                HeroSkillSchemaUpdateResp.newBuilder()
                        .setHeroIndex(0)
                        .setSchema(HeroSkillSchemaVo.newBuilder()
                                .setId(1)
                                .addSlot2SkillBaseIds(IntegerAndIntegerPairEntry.newBuilder()
                                        .setKey(2001)
                                        .setValue(80130011))
                                .addSlot2SkillBaseIds(IntegerAndIntegerPairEntry.newBuilder()
                                        .setKey(1001)
                                        .setValue(80140001))
                                .setActiveTime(System.currentTimeMillis() / 1000)
                                .setActive(true))
                        .setReqSource(1)
                        .build(),
                0);
        context.write(
                75015,
                HeroSkillSlotUpdateResp.newBuilder()
                        .setHeroIndex(0)
                        .addSlots(HeroSkillSlotVo.newBuilder()
                                .setId(2001)
                                .setLv(0)
                                .setMasterLv(0)
                                .setAnyOnSkill(true))
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103011, 21568.25))
                        .addAttrList(attribute(103001, 21568.25))
                        .setTargetId(playerSceneUnitId)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103111, 30))
                        .addAttrList(attribute(103101, 100))
                        .setTargetId(playerSceneUnitId)
                        .build(),
                0);
        context.write(
                50455,
                HeroStatUpdateResp.newBuilder()
                        .setHeroVo(HeroStatVo.newBuilder()
                                .setHeroIndex(0)
                                .addStats(stat(107002, 20))
                                .addStats(stat(102001, 280))
                                .addStats(stat(105001, 1))
                                .addStats(stat(106001, 1))
                                .addStats(stat(103001, 22874.5))
                                .addStats(stat(101001, 865.007))
                                .addStats(stat(104001, 100))
                                .addStats(stat(109001, 300)))
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103011, 22874.5))
                        .addAttrList(attribute(103001, 22874.5))
                        .setTargetId(playerSceneUnitId)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(101001, 865.007))
                        .setTargetId(playerSceneUnitId)
                        .build(),
                0);
        grantPlayerStats(context.getId(), 865.007, 22874.5);
        context.write(
                52351,
                PlayerFightForceResp.newBuilder()
                        .setPlayerFightForce(14623)
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                75151,
                HeroFightForceResp.newBuilder()
                        .addHeroVoList(HeroFightForceVo.newBuilder()
                                .setHeroIndex(0)
                                .setFightForce(14623))
                        .build(),
                0);

        TaskUpdateResp.Builder tasks = TaskUpdateResp.newBuilder();
        for (int taskId : new int[] {
                4002009, 4002004, 4002003, 4002002,
                4002008, 4002007, 4002006, 4002005
        }) {
            tasks.addTaskVos(
                    task(taskId, TaskPhase.PROGRESS, 14623));
        }
        context.write(50906, tasks.build(), 0);
    }

    private void writeThirdSkillStarStats(
            IPlayerContext context,
            long sceneUnitId) {
        context.write(
                50455,
                HeroStatUpdateResp.newBuilder()
                        .setHeroVo(HeroStatVo.newBuilder()
                                .setHeroIndex(0)
                                .addStats(stat(107002, 30))
                                .addStats(stat(102001, 305))
                                .addStats(stat(105001, 1))
                                .addStats(stat(106001, 1))
                                .addStats(stat(103001, 24311.999999999996))
                                .addStats(stat(101001, 920.7605))
                                .addStats(stat(104001, 100)))
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103011, 24311.999999999996))
                        .addAttrList(attribute(103001, 24311.999999999996))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(101001, 920.7605))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        grantPlayerStats(context.getId(), 920.7605, 24311.999999999996);
        context.write(
                52351,
                PlayerFightForceResp.newBuilder()
                        .setPlayerFightForce(15202)
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                75151,
                HeroFightForceResp.newBuilder()
                        .addHeroVoList(HeroFightForceVo.newBuilder()
                                .setHeroIndex(0)
                                .setFightForce(15202))
                        .build(),
                0);

        TaskUpdateResp.Builder tasks = TaskUpdateResp.newBuilder();
        for (int taskId : new int[] {
                4002009, 4002004, 4002003, 4002002,
                4002008, 4002007, 4002006, 4002005
        }) {
            tasks.addTaskVos(
                    task(taskId, TaskPhase.PROGRESS, 15202));
        }
        context.write(50906, tasks.build(), 0);
    }

    private void writeFirstSkillStarStats(
            IPlayerContext context,
            long sceneUnitId) {
        context.write(
                50455,
                HeroStatUpdateResp.newBuilder()
                        .setHeroVo(HeroStatVo.newBuilder()
                                .setHeroIndex(0)
                                .addStats(stat(107002, 43))
                                .addStats(stat(102001, 401.5))
                                .addStats(stat(105001, 1))
                                .addStats(stat(106001, 1))
                                .addStats(stat(103001, 29853.85))
                                .addStats(stat(101001, 1133.54076))
                                .addStats(stat(104001, 100)))
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103011, 29832.514467877547))
                        .addAttrList(attribute(103001, 29853.85))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(101001, 1133.54076))
                        .setTargetId(sceneUnitId)
                        .build(),
                0);
        grantPlayerStats(context.getId(), 1133.54076, 29853.85);
        context.write(
                52351,
                PlayerFightForceResp.newBuilder()
                        .setPlayerFightForce(17424)
                        .addFightStatPowerSources(1)
                        .build(),
                0);
        context.write(
                75151,
                HeroFightForceResp.newBuilder()
                        .addHeroVoList(HeroFightForceVo.newBuilder()
                                .setHeroIndex(0)
                                .setFightForce(17424))
                        .build(),
                0);

        TaskUpdateResp.Builder tasks = TaskUpdateResp.newBuilder();
        for (int taskId : new int[] {
                4002009, 4002004, 4002003, 4002002,
                4002008, 4002007, 4002006, 4002005
        }) {
            tasks.addTaskVos(
                    task(taskId, TaskPhase.PROGRESS, 17424));
        }
        context.write(50906, tasks.build(), 0);
    }

    /** 任务 200002 领奖并开启 200003，对应抓包 idx 368-372。 */
    private static void writeLearnedSkillTaskReward(
            IPlayerContext context) {
        context.write(
                50651,
                PurseUpdateResp.newBuilder()
                        .addItems(CurrencyItemVo.newBuilder()
                                .setType(2)
                                .setValue(10))
                        .addItems(CurrencyItemVo.newBuilder()
                                .setType(22)
                                .setValue(0))
                        .build(),
                0);
        context.write(
                50406,
                RewardResp.newBuilder()
                        .setOperationType(50907)
                        .addRewardItemVos(RewardItemVo.newBuilder()
                                .setItemKey(2)
                                .setAmount(5)
                                .setGiveFlag(false))
                        .build(),
                0);
        context.write(
                53702,
                ReputationLvOnTaskRewardUpdateResp.newBuilder()
                        .setTaskId(200002)
                        .setReputationLv(0)
                        .build(),
                0);
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(
                                200002,
                                TaskPhase.REWARDED,
                                1))
                        .build(),
                0);
        context.write(50906, nextTaskUpdate(200003), 0);
    }

    /** 第二关 Boss 任务 200004 领奖，按抓包 idx 574-585 下发。 */
    private static void writeSecondBossTaskReward(
            IPlayerContext context) {
        context.write(
                50651,
                PurseUpdateResp.newBuilder()
                        .addItems(CurrencyItemVo.newBuilder()
                                .setType(2)
                                .setValue(20))
                        .addItems(CurrencyItemVo.newBuilder()
                                .setType(22)
                                .setValue(0))
                        .build(),
                0);
        context.write(
                50406,
                RewardResp.newBuilder()
                        .setOperationType(50907)
                        .addRewardItemVos(RewardItemVo.newBuilder()
                                .setItemKey(2)
                                .setAmount(5)
                                .setGiveFlag(false))
                        .build(),
                0);
        context.write(
                53702,
                ReputationLvOnTaskRewardUpdateResp.newBuilder()
                        .setTaskId(200004)
                        .setReputationLv(0)
                        .build(),
                0);
        context.write(
                50852,
                ModuleNewOpenResp.newBuilder()
                        .addOpens(4201)
                        .addOpens(42011)
                        .build(),
                0);
        context.write(
                50384,
                cumulateLoginDays(new int[] {
                        112, 4304, 4401, 102, 4007, 103,
                        1608, 4201, 4202, 107, 4411, 4415
                }),
                0);

        // protobuf-net 会保留显式设置的默认值；该 8 字节正文与抓包 idx 579 一致。
        context.write(
                75051,
                ByteString.copyFrom(new byte[] {
                        0x08, 0x00, 0x10, 0x01,
                        0x28, 0x00, 0x30, 0x00
                }),
                0);
        context.write(75077, ByteString.EMPTY, 0);
        context.write(
                50384,
                cumulateLoginDays(new int[] {
                        102, 4007, 103, 1608, 4201, 4202,
                        107, 112, 4304, 4401, 4411, 42011, 4415
                }),
                0);
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(
                                200004,
                                TaskPhase.REWARDED,
                                1))
                        .build(),
                0);
        context.write(50906, nextTaskUpdate(200005), 0);
    }

    /** 第三关 Boss 任务 200006 领奖，按抓包 idx 1074-1081 下发。 */
    private void writeThirdBossTaskReward(
            IPlayerContext context) {
        long now = System.currentTimeMillis();
        context.write(
                50402,
                packUpdate(
                        50907,
                        changeNewFightSkillEnergy(
                                context, 10, 1, now)),
                0);
        context.write(
                50406,
                RewardResp.newBuilder()
                        .setOperationType(50907)
                        .addRewardItemVos(RewardItemVo.newBuilder()
                                .setItemKey(100200)
                                .setAmount(10)
                                .setGiveFlag(false))
                        .build(),
                0);
        context.write(
                53702,
                ReputationLvOnTaskRewardUpdateResp.newBuilder()
                        .setTaskId(200006)
                        .setReputationLv(0)
                        .build(),
                0);
        context.write(
                50852,
                ModuleNewOpenResp.newBuilder()
                        .addOpens(4008)
                        .build(),
                0);
        context.write(
                50384,
                cumulateLoginDays(new int[] {
                        102, 4007, 103, 1608, 4008, 4201, 4202,
                        107, 112, 4304, 4401, 4411, 42011, 4415
                }),
                0);
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(
                                200006,
                                TaskPhase.REWARDED,
                                1))
                        .build(),
                0);
        context.write(50906, nextTaskUpdate(200007), 0);
    }

    /** 纳戒抽取任务 200007 领奖，按抓包 idx 1152-1156 下发。 */
    private void writeNewFightSkillTaskReward(
            IPlayerContext context) {
        long now = System.currentTimeMillis();
        context.write(
                50402,
                packUpdate(
                        50907,
                        changeNewFightSkillEnergy(
                                context, 2, 1, now)),
                0);
        context.write(
                50406,
                RewardResp.newBuilder()
                        .setOperationType(50907)
                        .addRewardItemVos(RewardItemVo.newBuilder()
                                .setItemKey(100200)
                                .setAmount(2)
                                .setGiveFlag(false))
                        .build(),
                0);
        context.write(
                53702,
                ReputationLvOnTaskRewardUpdateResp.newBuilder()
                        .setTaskId(200007)
                        .setReputationLv(0)
                        .build(),
                0);
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(
                                200007,
                                TaskPhase.REWARDED,
                                1))
                        .build(),
                0);
        context.write(50906, nextTaskUpdate(200008), 0);
    }

    /** 第四关 Boss 任务 200008 领奖，按抓包 idx 1445-1449 下发。 */
    private void writeFourthBossTaskReward(IPlayerContext context) {
        long now = System.currentTimeMillis();
        context.write(
                50402,
                packUpdate(
                        50907,
                        changeNewFightSkillEnergy(
                                context, 2, 1, now)),
                0);
        context.write(
                50406,
                RewardResp.newBuilder()
                        .setOperationType(50907)
                        .addRewardItemVos(RewardItemVo.newBuilder()
                                .setItemKey(100200)
                                .setAmount(2)
                                .setGiveFlag(false))
                        .build(),
                0);
        context.write(
                53702,
                ReputationLvOnTaskRewardUpdateResp.newBuilder()
                        .setTaskId(200008)
                        .setReputationLv(0)
                        .build(),
                0);
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(
                                200008,
                                TaskPhase.REWARDED,
                                1))
                        .build(),
                0);
        context.write(50906, nextTaskUpdate(200009), 0);
    }

    /** 自动战斗任务 200009 领奖：对应抓包 idx 1505-1512。 */
    private void writeAutoSkillTaskReward(IPlayerContext context) {
        long now = System.currentTimeMillis();
        context.write(
                50402,
                packUpdate(
                        50907,
                        changeAlchemyMaterial(context, 101, 1, 7)),
                0);
        context.write(
                50406,
                RewardResp.newBuilder()
                        .setOperationType(50907)
                        .addRewardItemVos(RewardItemVo.newBuilder()
                                .setItemKey(101)
                                .setAmount(1)
                                .setGiveFlag(false))
                        .build(),
                0);
        context.write(
                53702,
                ReputationLvOnTaskRewardUpdateResp.newBuilder()
                        .setTaskId(200009)
                        .setReputationLv(0)
                        .build(),
                0);
        context.write(
                50852,
                ModuleNewOpenResp.newBuilder()
                        .addOpens(4204)
                        .build(),
                0);
        context.write(
                50384,
                cumulateLoginDays(new int[] {
                        102, 4007, 103, 1608, 4008, 4201, 4202, 107,
                        4204, 112, 4304, 4401, 4411, 42011, 4415
                }),
                0);
        context.write(
                76701,
                CrisisEventInfoResp.newBuilder()
                        .addEvents(CrisisEventVo.newBuilder()
                                .setId(20010)
                                .setStartMillis(now)
                                .setReward(false))
                        .build(),
                0);
        TaskUpdateResp.Builder autoSkillTasks = TaskUpdateResp.newBuilder()
                .addTaskVos(task(200009, TaskPhase.REWARDED, 1));
        appendSecondHeroChapterTasks(autoSkillTasks);
        context.write(50906, autoSkillTasks.build(), 0);
        secondHeroChapterTasksSent.add(context.getId());
    }

    /** 四段任务 200010 领奖，对应官服抓包 idx 1733-1737。 */
    private void writeFourthLevelTaskReward(IPlayerContext context) {
        long now = System.currentTimeMillis();
        context.write(
                50402,
                packUpdate(
                        50907,
                        changeNewFightSkillEnergy(
                                context, 20, 8, now)),
                0);
        context.write(
                50406,
                RewardResp.newBuilder()
                        .setOperationType(50907)
                        .addRewardItemVos(RewardItemVo.newBuilder()
                                .setItemKey(100200)
                                .setAmount(20)
                                .setGiveFlag(false))
                        .build(),
                0);
        context.write(
                53702,
                ReputationLvOnTaskRewardUpdateResp.newBuilder()
                        .setTaskId(200010)
                        .setReputationLv(0)
                        .build(),
                0);
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(200010, TaskPhase.REWARDED, 1))
                        .build(),
                0);
        context.write(50906, nextTaskUpdate(200011), 0);
    }

    /** 第五抽任务 200011 领奖，对应官服抓包 idx 1838-1842。 */
    private void writeFifthLotteryTaskReward(IPlayerContext context) {
        long now = System.currentTimeMillis();
        context.write(
                50402,
                packUpdate(
                        50907,
                        changeNewFightSkillEnergy(
                                context, 2, 11, now)),
                0);
        context.write(
                50406,
                RewardResp.newBuilder()
                        .setOperationType(50907)
                        .addRewardItemVos(RewardItemVo.newBuilder()
                                .setItemKey(100200)
                                .setAmount(2)
                                .setGiveFlag(false))
                        .build(),
                0);
        context.write(
                53702,
                ReputationLvOnTaskRewardUpdateResp.newBuilder()
                        .setTaskId(200011)
                        .setReputationLv(0)
                        .build(),
                0);
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(200011, TaskPhase.REWARDED, 1))
                        .build(),
                0);
        acceptNextMainTask(context, 200012);
    }

    /** 第六关任务 200012 领奖，对应官服抓包 idx 4919-4922 + 4928。 */
    private void writeSixthChapterTaskReward(IPlayerContext context) {
        context.write(
                50402,
                packUpdate(
                        50907,
                        changeAlchemyMaterial(context, 101, 1, 13)),
                0);
        context.write(
                50406,
                RewardResp.newBuilder()
                        .setOperationType(50907)
                        .addRewardItemVos(RewardItemVo.newBuilder()
                                .setItemKey(101)
                                .setAmount(1)
                                .setGiveFlag(false))
                        .build(),
                0);
        context.write(
                53702,
                ReputationLvOnTaskRewardUpdateResp.newBuilder()
                        .setTaskId(200012)
                        .setReputationLv(0)
                        .build(),
                0);
        context.write(
                50852,
                ModuleNewOpenResp.newBuilder()
                        .addOpens(1600)
                        .addOpens(1601)
                        .addOpens(1510)
                        .build(),
                0);
        // 第六关任务领奖后推送首充（抓包 idx 4924/4925）。
        // 开服第 1 天只有 chargeId=1（6 元）；第 2 天起补 2/3（30/98）。
        writeFirstChargeInfo(context);
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(200012, TaskPhase.REWARDED, 1))
                        .build(),
                0);
        acceptNextMainTask(context, 200013);
    }

    /**
     * 首充档：chargeId 1=6 元（开服当天），2=30 元、3=98 元（开服第 2 天，
     * {@code RealFirstChargeConfig} SERVER_OPEN_DAY &gt;= 2）。
     * Id 4 配置 openClose=false，不下发。
     */
    private void writeFirstChargeInfo(IPlayerContext context) {
        firstChargeUnlocked.put(context.getId(), Boolean.TRUE);
        List<Integer> ids = unlockedFirstChargeIds(context.getId());
        RealFirstChargeUpdateResp.Builder update =
                RealFirstChargeUpdateResp.newBuilder();
        RealFirstChargeInfoResp.Builder info =
                RealFirstChargeInfoResp.newBuilder();
        int maxId = 1;
        for (int id : ids) {
            RealFirstChargeDayRewardVo vo = firstChargeVo(id);
            update.addUpdates(vo);
            info.addDayRewards(vo);
            if (id > maxId) {
                maxId = id;
            }
        }
        context.write(77006, update.build(), 0);
        context.write(77001, info.build(), 0);
        firstChargePushedMax.put(context.getId(), maxId);
    }

    private void tryRefreshFirstCharge(IPlayerContext context) {
        if (!Boolean.TRUE.equals(firstChargeUnlocked.get(context.getId()))) {
            return;
        }
        List<Integer> ids = unlockedFirstChargeIds(context.getId());
        int want = ids.get(ids.size() - 1).intValue();
        int sent = firstChargePushedMax.getOrDefault(context.getId(), 0)
                .intValue();
        if (sent >= want) {
            return;
        }
        writeFirstChargeInfo(context);
    }

    private List<Integer> unlockedFirstChargeIds(long playerId) {
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(1);
        if (serverOpenDay >= 2 || hasReachedNinthChapter(playerId)) {
            ids.add(2);
            ids.add(3);
        }
        return ids;
    }

    private boolean hasReachedNinthChapter(long playerId) {
        if (hasPassedChapter(playerId, 10200305)
                || hasPassedChapter(playerId, 10200401)) {
            return true;
        }
        GuidanceState state = guidanceStates.get(playerId);
        return state != null && state.chapterId >= 10200401;
    }

    private static RealFirstChargeDayRewardVo firstChargeVo(int chargeId) {
        return RealFirstChargeDayRewardVo.newBuilder()
                .setChargeId(chargeId)
                .setExpireMillis(0)
                .setBuyMillis(0)
                .setRewardExpireMillis(0)
                .build();
    }

    private static PlayerCumulateLoginDaysResp cumulateLoginDays(
            int[] moduleIds) {
        PlayerCumulateLoginDaysResp.Builder response =
                PlayerCumulateLoginDaysResp.newBuilder()
                        .setCumulateLoginDays(1);
        for (int moduleId : moduleIds) {
            response.addModuleOpenId2CumulateLoginDays(
                    IntegerAndIntegerPairEntry.newBuilder()
                            .setKey(moduleId)
                            .setValue(1));
        }
        return response.build();
    }

    private int rewardTaskId(
            IPlayerContext context,
            TaskRewardReq request) {
        if (request.hasTaskId()) {
            return request.getTaskId().getTaskResourceId();
        }

        // 官服抓包 idx 478 的 50901 正文没有解析出 taskId；此时客户端
        // 已从第二关小怪推进到 Boss，待领取的仍是上一关任务 200003。
        GuidanceState state = guidanceStates.get(context.getId());
        if (state == null) {
            return 0;
        }
        if (state.chapterId == 10100101) {
            return 200000;
        }
        if (state.chapterId == 10100201
                || state.chapterId == 10100205) {
            return 200003;
        }
        return 0;
    }

    @PlayerCmd
    public void resetGuidanceMainMap(
            IPlayerContext context,
            GuidanceMainMapResetReq request) {
        if (!request.getEnterNext()) {
            return;
        }

        GuidanceState state = guidanceStates.get(context.getId());
        if (state == null) {
            return;
        }

        ChapterConfig.Chapter cleared = ChapterConfig.get(state.chapterId);
        if (cleared == null) {
            return;
        }

        ChapterConfig.Chapter bossChapter;
        int currentId = cleared.getChapterId();
        if (currentId >= 10301001 && currentId <= 10301005 && hasPassedChapter(context.getId(), 10301005)) {
            if (reputationLevels.getOrDefault(context.getId(), 0) >= 3) {
                context.write(61952, chapterEntered(ChapterConfig.get(10400101), 10301005), 0);
                guidanceStates.put(context.getId(), new GuidanceState(10400101));
                bossUnlockedWave.remove(context.getId());
            }
            return;
        }
        if (currentId >= 10300501 && currentId <= 10300505
                && hasPassedChapter(context.getId(), 10300505)) {
            if (reputationLevels.getOrDefault(context.getId(), 0) >= 2) {
                context.write(61952, MainMapPassChapterUpdateResp.newBuilder()
                        .setMainMapChapterId(10300601).setHasReward(true).setStageTime(6).setLastStageTime(10)
                        .setLoseBackId(10300401).setHistoryTopId(10300505).setChangeReason(0)
                        .setFromResetReq(1).setResetState(true).build(), 0);
                guidanceStates.put(context.getId(), new GuidanceState(10300601));
                bossUnlockedWave.remove(context.getId());
            }
            return;
        }
        int firstWave = currentId - currentId % 10 + 1;
        Integer unlockedWave = bossUnlockedWave.get(context.getId());
        // 6-10 关第三波打完后客户端常停在当前波或循环到 1/2 波再点
        // 「来打我噻」。只要本关 Boss 已解锁，reset 就必须进 Boss，
        // 不能再走 nextChapterId（第三波 next 是第一波，第二波 next 是第三波）。
        if (unlockedWave != null
                && unlockedWave == firstWave
                && (isSixthWave(currentId) || isWutanWave(currentId))) {
            bossUnlockedWave.remove(context.getId());
            bossChapter = ChapterConfig.get(unlockedWave + 4);
        } else {
            bossChapter = ChapterConfig.get(cleared.getNextChapterId());
        }
        if (bossChapter == null) {
            return;
        }

        context.write(
                61952,
                chapterEntered(
                        bossChapter,
                        bossChapter.getChapterId() >= 10200105
                                ? bossChapter.getChapterId() - 2
                                : cleared.getChapterId()),
                0);
        CombatSessionRegistry.clear(context.getId());
        forgetPlayerWaves(context);
        writeFightAttributes(
                context,
                restoreHp(context.getId(), cleared.getChapterId()));
        guidanceStates.put(
                context.getId(),
                new GuidanceState(bossChapter.getChapterId()));
    }

    static SceneUpdateVisibleResp visibleSnapshot(long playerId) {
        SceneUnitVo player = buildPlayerUnit(playerId, true);
        long commonSkillId = player.getBaseInfoVo().getId() + 1;

        SceneUnitVo commonSkill = SceneUnitVo.newBuilder()
                .setBaseInfoVo(SceneUnitBaseInfoVo.newBuilder()
                        .setId(commonSkillId)
                        .setX(0)
                        .setY(0)
                        .setZ(-15)
                        .setState(0)
                        .setDir(0)
                        .setUnitType(24))
                .setFightInfoVo(SceneFightUnitInfoVo.newBuilder()
                        .setCampId(2)
                        .addAttributeList(attribute(103101, 1))
                        .addAttributeList(attribute(103111, 1)))
                .setCommonSkillVo(SceneCommonSkillVO.newBuilder()
                        .setPlayerId(playerId)
                        .setCreateTime(System.currentTimeMillis()))
                .build();

        return SceneUpdateVisibleResp.newBuilder()
                .setSnapshot(true)
                .addVisibleList(player)
                .addVisibleList(commonSkill)
                .build();
    }


    /**
     * 下发战斗结算。
     *
     * <p>Boss 关会带上怪物掉落（抓包中 Boss 的 {@code mainMapReward}），
     * 小怪关的 {@code RewardResp} 没有物品条目。
     */
    private void writeFightResult(
            IPlayerContext context,
            ChapterConfig.Chapter chapter) {

        if (chapter.getChapterId() == 10100105) {
            long now = System.currentTimeMillis();
            context.write(
                    50402,
                    packUpdate(
                            61951,
                            changeNewFightSkillEnergy(
                                    context, 3, 1, now)),
                    0);
        }

        if (chapter.getChapterId() == 10100205) {
            context.write(
                    50402,
                    packUpdate(
                            61951,
                            changeAlchemyMaterial(context, 101, 1, 3)),
                    0);
        }

        if (chapter.getChapterId() == 10100305) {
            long now = System.currentTimeMillis();
            context.write(
                    50402,
                    packUpdate(
                            61951,
                            changeNewFightSkillEnergy(
                                    context, 3, 1, now)),
                    0);
        }

        if (chapter.getChapterId() == 10100405) {
            long now = System.currentTimeMillis();
            context.write(
                    50402,
                    packUpdate(
                            61951,
                            changeNewFightSkillEnergy(
                                    context, 10, 1, now)),
                    0);
        }

        if (chapter.getChapterId() == 10100505) {
            long now = System.currentTimeMillis();
            context.write(
                    50402,
                    packUpdate(
                            61951,
                            changeNewFightSkillEnergy(
                                    context, 20, 11, now)),
                    0);
        }

        LOGGER.info(
                "Writing fight result: player={}, chapter={}, boss={}",
                context.getId(),
                chapter.getChapterId(),
                chapter.isBoss());

        if (chapter.getChapterId() >= 10200105 && chapter.isBoss()) {
            for (ChapterConfig.MonsterSpawn spawn : chapter.getMonsters()) {
                if (spawn.getRewardItemKey() != 100200) {
                    continue;
                }
                long now = System.currentTimeMillis();
                context.write(
                        50402,
                        packUpdate(
                                61951,
                                changeNewFightSkillEnergy(
                                        context,
                                        (int) spawn.getRewardAmount(),
                                        11,
                                        now)),
                        0);
                break;
            }
        }

        RewardResp.Builder reward = RewardResp.newBuilder()
                .setOperationType(61951);

        for (ChapterConfig.MonsterSpawn spawn : chapter.getMonsters()) {
            if (spawn.hasReward()) {
                reward.addRewardItemVos(RewardItemVo.newBuilder()
                        .setItemKey(spawn.getRewardItemKey())
                        .setAmount(spawn.getRewardAmount())
                        .setGiveFlag(false));
            }
        }

        context.write(50406, reward.build(), 0);

        CombatSession session = CombatSessionRegistry.get(context.getId());
        if (chapter.getChapterId() == 10200405
                && session != null && session.isNinthBossGuideStarted()) {
            removeNinthTeachingSkill(context);
            // 抓包 idx 8324..8325：借招击败狮王后先接教学收尾，再结算。
            context.write(77066, com.doupo.protocol.PlayerFightGuildCallResp.newBuilder()
                    .setGuildGroupId(10044).build(), 0);
            LOGGER.info("Ninth boss guide completed: player={}, guide=10044", context.getId());
        }

        if (!isWutanChapter(chapter.getChapterId())) {
            context.write(
                    50781,
                    FightResultResp.newBuilder()
                            .setBattleFieldId(1001)
                            .setBattleResultCode(0)
                            .build(),
                    0);
        }

        if (chapter.isBoss()) {
            writeFightAttributes(
                    context,
                    restoreHp(context.getId(), chapter.getChapterId()));
            context.write(60751, ByteString.EMPTY, 0);
            context.write(60751, ByteString.EMPTY, 0);
            markChapterPassed(context.getId(), chapter.getChapterId());
            if (chapter.getChapterId() == 10100105) {
                context.write(50906, firstBossTaskUpdate(chapter), 0);
            } else if (chapter.getChapterId() == 10100205) {
                context.write(50906, secondBossTaskUpdate(chapter), 0);
            } else if (chapter.getChapterId() == 10100305) {
                context.write(50906, thirdBossTaskUpdate(chapter), 0);
            } else if (chapter.getChapterId() == 10100405) {
                context.write(50906, fourthBossTaskUpdate(chapter), 0);
            } else if (chapter.getChapterId() == 10100505) {
                context.write(50906, fifthBossTaskUpdate(chapter), 0);
            } else if (chapter.getChapterId() == 10200105) {
                tryFinishAcceptedMainTask(context, 200016, 1);
            } else if (chapter.getChapterId() == 10200205
                    || chapter.getChapterId() == 10200305
                    || chapter.getChapterId() == 10200405
                    || chapter.getChapterId() == 10200505
                    || chapter.getChapterId() == 10300105
                    || chapter.getChapterId() == 10300205
                    || chapter.getChapterId() == 10300305
                    || chapter.getChapterId() == 10300405) {
                if (chapter.getChapterId() == 10200205) {
                    acceptedMainTasks.remove(context.getId(), 200020);
                }
                if (chapter.getChapterId() == 10300105) {
                    acceptedMainTasks.remove(context.getId(), 200029);
                }
                if (chapter.getChapterId() == 10300205) {
                    acceptedMainTasks.remove(context.getId(), 200031);
                }
                if (chapter.getChapterId() == 10300305) {
                    acceptedMainTasks.remove(context.getId(), 200034);
                }
                if (chapter.getChapterId() == 10300405) {
                    acceptedMainTasks.remove(context.getId(), 200036);
                }
                context.write(50906, laterBossTaskUpdate(chapter), 0);
                if (chapter.getChapterId() == 10200505) {
                    tryFinishAcceptedMainTask(context, 200026, 1);
                }
            }
            return;
        }

        context.write(60751, ByteString.EMPTY, 0);
        context.write(60751, ByteString.EMPTY, 0);
        writeFightAttributes(
                context,
                restoreHp(context.getId(), chapter.getChapterId()));
    }

    /** 第一关 Boss 通关任务，精确对应抓包 idx 305。 */
    private static TaskUpdateResp firstBossTaskUpdate(
            ChapterConfig.Chapter chapter) {
        TaskUpdateResp.Builder response = TaskUpdateResp.newBuilder();
        for (int taskId : new int[] { 1101, 1104, 1107, 1110, 1113 }) {
            response.addTaskVos(task(taskId, TaskPhase.PROGRESS, 9));
        }
        response.addTaskVos(
                task(chapter.getMainTaskId(), TaskPhase.FINISHED, 1));
        return response.build();
    }

    /** 第二关 Boss 通关任务，精确对应抓包 idx 540。 */
    private static TaskUpdateResp secondBossTaskUpdate(
            ChapterConfig.Chapter chapter) {
        TaskUpdateResp.Builder response = TaskUpdateResp.newBuilder();
        for (int taskId : new int[] { 1104, 1107, 1110, 1113 }) {
            response.addTaskVos(task(taskId, TaskPhase.PROGRESS, 19));
        }
        response.addTaskVos(
                task(chapter.getMainTaskId(), TaskPhase.FINISHED, 1));
        return response.build();
    }

    /** 第三关 Boss 通关任务，精确对应抓包 idx 1053。 */
    private static TaskUpdateResp thirdBossTaskUpdate(
            ChapterConfig.Chapter chapter) {
        TaskUpdateResp.Builder response = TaskUpdateResp.newBuilder();
        for (int taskId : new int[] { 1104, 1107, 1110, 1113 }) {
            response.addTaskVos(task(taskId, TaskPhase.PROGRESS, 29));
        }
        response.addTaskVos(
                task(chapter.getMainTaskId(), TaskPhase.FINISHED, 1));
        return response.build();
    }

    /** 第四关 Boss 通关任务，精确对应抓包 idx 1393。 */
    private static TaskUpdateResp fourthBossTaskUpdate(
            ChapterConfig.Chapter chapter) {
        TaskUpdateResp.Builder response = TaskUpdateResp.newBuilder();
        for (int taskId : new int[] { 1104, 1107, 1110, 1113 }) {
            response.addTaskVos(task(taskId, TaskPhase.PROGRESS, 40));
        }
        response.addTaskVos(
                task(chapter.getMainTaskId(), TaskPhase.FINISHED, 1));
        return response.build();
    }

    /** 第五关 Boss 通关任务：只刷新通用击杀进度到 53，无主线任务（推进主地图）。 */
    private static TaskUpdateResp fifthBossTaskUpdate(
            ChapterConfig.Chapter chapter) {
        TaskUpdateResp.Builder response = TaskUpdateResp.newBuilder();
        for (int taskId : new int[] { 1104, 1107, 1110, 1113 }) {
            response.addTaskVos(task(taskId, TaskPhase.PROGRESS, 53));
        }
        return response.build();
    }

    /** 第七至十关 Boss 任务同步：对应抓包累计进度 128/151/174/197。 */
    private static TaskUpdateResp laterBossTaskUpdate(
            ChapterConfig.Chapter chapter) {
        int cumulativeProgress;
        switch (chapter.getChapterId()) {
            case 10200205:
                cumulativeProgress = 128;
                break;
            case 10200305:
                cumulativeProgress = 151;
                break;
            case 10200405:
                cumulativeProgress = 174;
                break;
            case 10200505:
                cumulativeProgress = 197;
                break;
            case 10300105:
                cumulativeProgress = 213;
                break;
            case 10300205:
                cumulativeProgress = 229;
                break;
            case 10300305:
                cumulativeProgress = 245;
                break;
            case 10300405:
                cumulativeProgress = 261;
                break;
            default:
                throw new IllegalArgumentException(
                        "Unsupported later boss: " + chapter.getChapterId());
        }
        TaskUpdateResp.Builder response = TaskUpdateResp.newBuilder()
                .addTaskVos(task(
                        chapter.getMainTaskId(),
                        TaskPhase.FINISHED,
                        1));
        int[] killTasks = chapter.getChapterId() == 10300105
                || chapter.getChapterId() == 10300205
                || chapter.getChapterId() == 10300305
                || chapter.getChapterId() == 10300405
                ? new int[] { 1110, 1113 }
                : new int[] { 1107, 1110, 1113 };
        for (int taskId : killTasks) {
            response.addTaskVos(
                    task(taskId, TaskPhase.PROGRESS, cumulativeProgress));
        }
        return response.build();
    }

    /** 每关 Boss/小怪结算后恢复玩家满血，满血值随升级/装配技能增长（抓包确认）。 */
    private double restoreHp(long playerId, int chapterId) {
        return Math.max(
                chapterBaseHp(chapterId),
                grantedMaxHp.getOrDefault(playerId, 0.0));
    }

    private static double chapterBaseHp(int chapterId) {
        switch (chapterId) {
            case 10100101:
            case 10100105:
                return 12613.2;
            case 10100201:
            case 10100205:
                return 14050.7;
            case 10100301:
            case 10100305:
                return 21137.0;
            case 10100401:
            case 10100405:
                return 22874.5;
            case 10100501:
            case 10100505:
                return 29853.85;
            case 10200101:
            case 10200102:
            case 10200103:
            case 10200105:
            case 10200201:
            case 10200202:
            case 10200203:
            case 10200205:
                return 30991.35;
            case 10200301:
            case 10200302:
            case 10200303:
            case 10200305:
            case 10200401:
            case 10200402:
            case 10200403:
            case 10200405:
                return 47938.8;
            case 10200501:
            case 10200502:
            case 10200503:
            case 10200505:
            case 10300101:
            case 10300102:
            case 10300103:
            case 10300105:
            case 10300201:
            case 10300202:
            case 10300203:
            case 10300205:
            case 10300301:
            case 10300302:
            case 10300303:
            case 10300305:
            case 10300401:
            case 10300402:
            case 10300403:
            case 10300405:
                return 54483.8;
            default:
                return 12613.2;
        }
    }

    private static void writeFightAttributes(
            IPlayerContext context,
            double hp) {
        long playerUnitId = context.getId() * 1000 + 1;

        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103011, hp))
                        .addAttrList(attribute(103001, hp))
                        .setAttackId(0)
                        .setTargetId(playerUnitId)
                        .setHitId(0)
                        .build(),
                0);
        context.write(
                50801,
                AttributeActionVO.newBuilder()
                        .addAttrList(attribute(103111, 30))
                        .addAttrList(attribute(103101, 100))
                        .setAttackId(0)
                        .setTargetId(playerUnitId)
                        .setHitId(0)
                        .build(),
                0);
    }

    private static void writeSkillReset(IPlayerContext context) {
        long playerUnitId = context.getId() * 1000 + 1;
        long[] skillIds = {
                10111110101L,
                10111120101L,
                10111130101L,
                10111140101L,
                10110710101L
        };

        for (long skillId : skillIds) {
            context.write(
                    50766,
                    FightSkillUpdateResp.newBuilder()
                            .setId(playerUnitId)
                            .setSkillId(skillId)
                            .setCostCd(1)
                            .build(),
                    0);
        }
    }

    /**
     * 杀怪进度推送。
     *
     * <p>抓包显示两套进度数字是分开的（对照第二关 idx 441）：
     * <ul>
     *   <li>通用统计任务（1101/1104/1107/1110/1113）用的是<b>全局累计值</b>
     *       （第二关是 11→14，不是每关从 1 重新计数）；</li>
     *   <li>主线任务（200000/200003/...）用的是<b>本关击杀数</b>（1→4），
     *       与通用任务的进度值不是同一个数字。</li>
     * </ul>
     * 第一关因为是从 0 起算，两个数字刚好相等，容易误以为它们是同一套。
     *
     * @param chapter        当前章节
     * @param killedInChapter 本关已击杀数量
     */
    static TaskUpdateResp killTaskUpdate(
            ChapterConfig.Chapter chapter,
            int killedInChapter) {

        if (chapter.getChapterId() == 10100201) {
            return secondChapterKillTaskUpdate(
                    chapter,
                    killedInChapter);
        }

        long cumulativeProgress =
                chapter.getKillProgressBefore() + killedInChapter;
        boolean chapterCleared = killedInChapter >= chapter.getMonsterCount();

        TaskUpdateResp.Builder response = TaskUpdateResp.newBuilder();

        // 通用杀怪统计任务：全局累计值。
        for (int taskId : new int[] { 1101, 1104, 1107, 1110, 1113 }) {
            response.addTaskVos(
                    task(taskId, TaskPhase.PROGRESS, cumulativeProgress));
        }

        // 主线任务：本关击杀数，不是全局累计值。
        if (chapter.getMainTaskId() != 0) {
            response.addTaskVos(task(
                    chapter.getMainTaskId(),
                    chapterCleared
                            ? TaskPhase.FINISHED
                            : TaskPhase.PROGRESS,
                    killedInChapter));
        }

        return response.build();
    }

    /**
     * 乌坦城击杀进度：只推 1107/1110/1113。1107 在累计 200 时 finished，
     * 之后 EndFight 不再重复（抓包 idx 9566/9577）。
     */
    private static TaskUpdateResp wutanKillTaskUpdate(
            ChapterConfig.Chapter chapter,
            int killedInChapter,
            boolean endFight) {
        long cumulative =
                chapter.getKillProgressBefore() + killedInChapter;
        TaskUpdateResp.Builder response = TaskUpdateResp.newBuilder();
        if (!endFight && cumulative == 200) {
            response.addTaskVos(task(1107, TaskPhase.FINISHED, 200));
            response.addTaskVos(task(1110, TaskPhase.PROGRESS, 200));
            response.addTaskVos(task(1113, TaskPhase.PROGRESS, 200));
            return response.build();
        }
        if (endFight || cumulative > 200) {
            response.addTaskVos(task(1110, TaskPhase.PROGRESS, cumulative));
            response.addTaskVos(task(1113, TaskPhase.PROGRESS, cumulative));
            return response.build();
        }
        for (int taskId : new int[] { 1107, 1110, 1113 }) {
            response.addTaskVos(task(taskId, TaskPhase.PROGRESS, cumulative));
        }
        return response.build();
    }

    /** 第二关逐只击杀的任务列表，精确对应抓包 idx 385/404/423/441。 */
    private static TaskUpdateResp secondChapterKillTaskUpdate(
            ChapterConfig.Chapter chapter,
            int killedInChapter) {
        long cumulativeProgress =
                chapter.getKillProgressBefore() + killedInChapter - 1;

        TaskUpdateResp.Builder response = TaskUpdateResp.newBuilder();
        if (killedInChapter == 1) {
            response.addTaskVos(
                    task(1101, TaskPhase.FINISHED, cumulativeProgress));
        }
        for (int taskId : new int[] { 1104, 1107, 1110 }) {
            response.addTaskVos(
                    task(taskId, TaskPhase.PROGRESS, cumulativeProgress));
        }
        response.addTaskVos(task(
                chapter.getMainTaskId(),
                killedInChapter >= chapter.getMonsterCount()
                        ? TaskPhase.FINISHED
                        : TaskPhase.PROGRESS,
                killedInChapter));
        response.addTaskVos(
                task(1113, TaskPhase.PROGRESS, cumulativeProgress));
        return response.build();
    }

    /**
     * 章节打完后紧跟着的第二次任务推送，只含通用统计任务的最终累计值，
     * 不重复推主线任务（抓包 idx 448：441 推过 finished 之后不再重复）。
     */
    private static TaskUpdateResp cumulativeKillTaskUpdate(
            ChapterConfig.Chapter chapter) {

        long finalProgress =
                chapter.getKillProgressBefore() + chapter.getMonsterCount();

        TaskUpdateResp.Builder response = TaskUpdateResp.newBuilder();
        for (int taskId : new int[] { 1104, 1107, 1110, 1113 }) {
            response.addTaskVos(
                    task(taskId, TaskPhase.PROGRESS, finalProgress));
        }
        return response.build();
    }

    private static TaskUpdateResp rewardedTaskUpdate(
            int taskId,
            boolean includeFirstTaskFollowUp,
            long progress) {
        TaskUpdateResp.Builder response = TaskUpdateResp.newBuilder()
                .addTaskVos(task(taskId, TaskPhase.REWARDED, progress));
        if (includeFirstTaskFollowUp) {
            response.addTaskVos(
                    task(2201001, TaskPhase.FINISHED, 1));
        }
        return response.build();
    }

    /** 领奖进度：200018/200030 目标值是 2，其余主线为 1。 */
    private static long rewardedProgress(int taskId) {
        return taskId == 200018 || taskId == 200030 ? 2 : 1;
    }

    /**
     * 领取当前主线后接取下一任务；条件已满足则按 AutoFinish 直接 FINISHED。
     *
     * <p>200030（挂机看装备，引导 503）和 200035（天焚塔，引导 11001）私服都没做。
     * 若只下发 FINISHED，客户端会卡在引导里，不接 200036，14 关任务栏空。
     * 这里直接代领。200030 挂机看装备已接 61953，不再代领。
     */
    private void acceptNextMainTask(IPlayerContext context, int nextTaskId) {
        if (nextTaskId == 200017
                || nextTaskId == 200053
                || nextTaskId == 200023
                || nextTaskId == 200054
                || nextTaskId == 200027
                || nextTaskId == 200033) {
            pendingLotteryMainTasks.put(context.getId(), nextTaskId);
        }
        if (nextTaskId >= 200035 && nextTaskId <= 200038) {
            later(context.getId()).tasks.add(nextTaskId);
            pushChapter17Task(context, nextTaskId, true);
            return;
        }
        long autoProgress = autoFinishProgress(context.getId(), nextTaskId);
        if (autoProgress >= 0) {
            if (nextTaskId == 200012) {
                finishedArriveChapter6.add(context.getId());
            }
            context.write(60751, BattlePassInfoResp.getDefaultInstance(), 0);
            context.write(
                    50906,
                    TaskUpdateResp.newBuilder()
                            .addTaskVos(task(
                                    nextTaskId,
                                    TaskPhase.FINISHED,
                                    autoProgress))
                            .build(),
                    0);
            if (nextTaskId == 200021) {
                tryOpenEighthRealmBagModules(context);
            }
            return;
        }
        acceptedMainTasks.put(context.getId(), nextTaskId);
        if (nextTaskId == 200015 || nextTaskId == 200018) {
            syncQualitySkillTask(context);
        } else {
            context.write(50906, nextTaskUpdate(nextTaskId), 0);
        }
    }

    private long autoFinishProgress(long playerId, int taskId) {
        switch (taskId) {
            case 200012:
                return hasArrivedChapter6(playerId) ? 1 : -1;
            case 200013:
                return heroLevels.getOrDefault(playerId, 2) >= 5 ? 1 : -1;
            case 200014:
                return crisisEvent20010Rewarded.contains(playerId) ? 1 : -1;
            case 200015:
                return qualitySkillCount(playerId) >= 1
                        ? 1 : -1;
            case 200016:
                return hasPassedChapter(playerId, 10200105) ? 1 : -1;
            case 200018:
                int quality3 = qualitySkillCount(playerId);
                return quality3 >= 2 ? quality3 : -1;
            case 200020:
                return hasPassedChapter(playerId, 10200205) ? 1 : -1;
            case 200021:
                return heroLevels.getOrDefault(playerId, 2) >= 6 ? 1 : -1;
            case 200025:
                return heroLevels.getOrDefault(playerId, 2) >= 8 ? 1 : -1;
            case 200026:
                return hasPassedChapter(playerId, 10200505) ? 1 : -1;
            case 200028:
                return heroLevels.getOrDefault(playerId, 2) >= 9 ? 1 : -1;
            case 200029:
                return hasPassedChapter(playerId, 10300105) ? 1 : -1;
            case 200031:
                return hasPassedChapter(playerId, 10300205) ? 1 : -1;
            case 200034:
                return hasPassedChapter(playerId, 10300305) ? 1 : -1;
            case 200036:
                return hasPassedChapter(playerId, 10300405) ? 1 : -1;
            default:
                return -1;
        }
    }

    private void tryFinishAcceptedMainTask(
            IPlayerContext context,
            int taskId,
            long progress) {
        if (!Integer.valueOf(taskId).equals(
                acceptedMainTasks.get(context.getId()))) {
            return;
        }
        acceptedMainTasks.remove(context.getId());
        if (taskId == 200012) {
            finishedArriveChapter6.add(context.getId());
        }
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(taskId, TaskPhase.FINISHED, progress))
                        .build(),
                0);
    }

    /**
     * 到达魔兽山脉第6关（MainChapterArrive / 10200101）。
     * 官服 idx 1982 在第六关首次出手下发；若玩家先打到第6关再领 200011，
     * 领取包会把 200012 重新推成 progress 0，后续任意第6关及以后出手都要补完。
     */
    private void finishMainChapterArrive200012(IPlayerContext context) {
        if (!finishedArriveChapter6.add(context.getId())) {
            return;
        }
        acceptedMainTasks.remove(context.getId(), 200012);
        context.write(
                50906,
                TaskUpdateResp.newBuilder()
                        .addTaskVos(task(200012, TaskPhase.FINISHED, 1))
                        .build(),
                0);
    }

    private boolean hasArrivedChapter6(long playerId) {
        if (finishedArriveChapter6.contains(playerId)
                || sixthRealmTaskPlayers.contains(playerId)
                || hasPassedChapter(playerId, 10200105)) {
            return true;
        }
        GuidanceState state = guidanceStates.get(playerId);
        if (state != null && state.chapterId >= 10200101) {
            return true;
        }
        CombatSession session = CombatSessionRegistry.get(playerId);
        return session != null && session.getChapterId() >= 10200101;
    }

    private void markChapterPassed(long playerId, int chapterId) {
        passedMainChapters
                .computeIfAbsent(playerId, ignored -> ConcurrentHashMap.newKeySet())
                .add(chapterId);
    }

    private boolean hasPassedChapter(long playerId, int chapterId) {
        Set<Integer> passed = passedMainChapters.get(playerId);
        return passed != null && passed.contains(chapterId);
    }

    private void noteQualitySkill(IPlayerContext context, int skillBaseId) {
        if (skillQuality(skillBaseId) < 3) {
            return;
        }
        syncQualitySkillTask(context);
    }

    private int qualitySkillCount(long playerId) {
        int count = 0;
        for (long baseId : learnedSkills(playerId)) {
            if (skillQuality((int) baseId) >= 3) count++;
        }
        return count;
    }

    private void syncQualitySkillTask(IPlayerContext context) {
        Integer taskId = acceptedMainTasks.get(context.getId());
        if (taskId == null || (taskId != 200015 && taskId != 200018)) return;
        int count = qualitySkillCount(context.getId());
        int required = taskId == 200015 ? 1 : 2;
        if (count >= required) {
            tryFinishAcceptedMainTask(context, taskId, count);
        } else {
            context.write(50906, TaskUpdateResp.newBuilder()
                    .addTaskVos(task(taskId, TaskPhase.PROGRESS, count)).build(), 0);
        }
    }

    /** 斗技品质来自 newskillbaseconfig.Quality。 */
    private static int skillQuality(int skillBaseId) {
        return HeroSkillConfig.quality(skillBaseId);
    }

    private static void appendSecondHeroChapterTasks(
            TaskUpdateResp.Builder tasks) {
        tasks.addTaskVos(task(30201, TaskPhase.PROGRESS, 0));
        tasks.addTaskVos(task(30202, TaskPhase.PROGRESS, 0));
        tasks.addTaskVos(task(30301, TaskPhase.PROGRESS, 0));
        tasks.addTaskVos(task(30302, TaskPhase.PROGRESS, 1));
        tasks.addTaskVos(task(30401, TaskPhase.PROGRESS, 0));
        tasks.addTaskVos(task(30402, TaskPhase.PROGRESS, 1));
    }

    private void refreshChapter17Tasks(IPlayerContext context) {
        for (int taskId : new ArrayList<>(later(context.getId()).tasks)) {
            pushChapter17Task(context, taskId, false);
        }
    }

    private long chapter17TaskProgress(long playerId, int taskId) {
        Chapter17Progress state = later(playerId);
        switch (taskId) {
            case 200035: return state.towerPass >= 2 ? 1 : 0;
            case 200036: return hasPassedChapter(playerId, 10300405) ? 1 : 0;
            case 200037: return heroLevels.getOrDefault(playerId, 2) >= 13 ? 1 : 0;
            case 200038: return hasPassedChapter(playerId, 10300505) ? 1 : 0;
            case 200100: return state.arrivedSixteen ? 1 : 0;
            case 200101: return Math.min(state.watchedEquips, 2);
            case 200102: return hasPassedChapter(playerId, 10300605) ? 1 : 0;
            case 200103: return state.commonDrawn ? 1 : 0;
            case 200104: return hasPassedChapter(playerId, 10300705) ? 1 : 0;
            case 200105: return state.playerName == null ? 0 : 1;
            case 200106: return Math.min(state.taskEquipMaterialConsumed, 10);
            case 200107: return state.continuationDrawn ? 1 : 0;
            case 200108: return hasPassedChapter(playerId, 10300905) ? 1 : 0;
            case 200109: return heroLevels.getOrDefault(playerId, 2) >= 17 ? 1 : 0;
            case 200110: return hasPassedChapter(playerId, 10301005) ? 1 : 0;
            case 200111: return reputationLevels.getOrDefault(playerId, 0);
            default: return 0;
        }
    }

    private void pushChapter17Task(IPlayerContext context, int taskId, boolean initial) {
        Chapter17Progress state = later(context.getId());
        if (state.rewarded.contains(taskId)) return;
        long progress = chapter17TaskProgress(context.getId(), taskId);
        long target = Chapter17Data.row("tasks", "TaskId", taskId)
                .path("TaskTargets").get(0).path("TargetValue").asLong();
        boolean finished = progress >= target;
        boolean changed = finished && state.finished.add(taskId);
        if (initial || changed) {
            context.write(50906, TaskUpdateResp.newBuilder().addTaskVos(task(taskId,
                    finished ? TaskPhase.FINISHED : TaskPhase.PROGRESS, progress)).build(), 0);
        }
    }

    private void rewardChapter17Task(IPlayerContext context, int taskId) {
        Chapter17Progress state = later(context.getId());
        if (!state.tasks.contains(taskId) || state.rewarded.contains(taskId)) return;
        pushChapter17Task(context, taskId, false);
        if (!state.finished.contains(taskId)) return;
        state.rewarded.add(taskId);
        com.fasterxml.jackson.databind.JsonNode definition = Chapter17Data.row("tasks", "TaskId", taskId);
        grantChapter17Rewards(context, definition.path("RewardResources"), 50907, true);
        context.write(53702, ReputationLvOnTaskRewardUpdateResp.newBuilder().setTaskId(taskId)
                .setReputationLv(reputationLevels.getOrDefault(context.getId(), 0)).build(), 0);
        context.write(50906, TaskUpdateResp.newBuilder().addTaskVos(task(taskId, TaskPhase.REWARDED,
                chapter17TaskProgress(context.getId(), taskId))).build(), 0);
        if (taskId == 200102) {
            context.write(50852, ModuleNewOpenResp.newBuilder().addOpens(4601).addOpens(1641).addOpens(1642).build(), 0);
            context.write(77354, parseLotteryInfo("10136-77354.bin"), 0);
            context.write(50402, packUpdate(50851, changeChapter17Item(context, 100212, 10)), 0);
            writeCommonSkills(context);
        }
        if (taskId == 200104) {
            context.write(50852, ModuleNewOpenResp.newBuilder().addOpens(109).build(), 0);
        }
        for (com.fasterxml.jackson.databind.JsonNode row : Chapter17Data.DATA.path("tasks")) {
            int next = row.path("TaskId").asInt();
            if (next > 200111) continue;
            for (com.fasterxml.jackson.databind.JsonNode condition : row.path("AcceptableConditionResources")) {
                if ("TASK_REWARD".equals(condition.path("Type").asText())
                        && condition.path("Context").path("RewardTaskId").asInt() == taskId) {
                    boolean allowed = true;
                    for (com.fasterxml.jackson.databind.JsonNode gate : row.path("AcceptableConditionResources")) {
                        if (next >= 200105 && "reputationRangeLv".equals(gate.path("Type").asText())
                                && reputationLevels.getOrDefault(context.getId(), 0) < gate.path("Context").path("Level").asInt()) {
                            allowed = false;
                        }
                    }
                    if (allowed && state.tasks.add(next)) pushChapter17Task(context, next, true);
                }
            }
        }
    }

    private void openReputationTwoTasks(IPlayerContext context) {
        Chapter17Progress state = later(context.getId());
        try {
            MainMapChapterInfoResp sample = MainMapChapterInfoResp.parseFrom(Chapter17Data.bytes("9886-61951.bin"));
            List<MainEquipVO> equips = new ArrayList<>();
            int index = 300;
            for (RewardItemVo item : sample.getMainMapChapterHangUpRewardList()) {
                if (item.hasMainEquipVo()) equips.add(item.getMainEquipVo().toBuilder()
                        .setObjectId(context.getId() * 1000 + index++).build());
            }
            pendingHangUpEquips.put(context.getId(), equips);
            context.write(61955, com.doupo.protocol.MainMapHangUpRewardUpdateResp.newBuilder()
                    .addAllMainMapChapterHangUpReward(hangUpRewardItems(equips))
                    .setNextHangUpRewardTime(System.currentTimeMillis() + 60000).build(), 0);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) { throw new IllegalStateException(e); }
        if (hasPassedChapter(context.getId(), 10300505)) {
            GuidanceState current = guidanceStates.get(context.getId());
            int id = current == null ? 10300501 : current.chapterId;
            context.write(61952, MainMapPassChapterUpdateResp.newBuilder().setMainMapChapterId(id)
                    .setNextChallengeId(10300601).setHistoryTopId(10300505).setLoseBackId(10300401)
                    .setStageTime(6).setLastStageTime(10).setChangeReason(4).build(), 0);
        }
        for (int id : new int[] { 200100, 200101, 200111 }) {
            state.tasks.add(id);
            pushChapter17Task(context, id, true);
        }
    }

    private UpdateItem changeChapter17Item(IPlayerContext context, int itemId, long delta) {
        if (itemId == 101 || itemId == 102) return changeAlchemyMaterial(context, itemId, delta, 1000 + itemId);
        if (itemId == 100200) return changeNewFightSkillEnergy(context, (int) delta, 11, System.currentTimeMillis());
        if (itemId == 10060) return grantStackedItem10060(context, (int) delta, System.currentTimeMillis());
        Chapter17Progress state = later(context.getId());
        UpdateItem previous = context instanceof com.doupo.server.foundation.player.PlayerConnectionContext
                ? ((com.doupo.server.foundation.player.PlayerConnectionContext) context).itemStack(itemId) : null;
        long before = previous == null ? state.items.getOrDefault(itemId, 0L) : previous.getPackItem().getSize();
        long total = Math.addExact(before, delta);
        if (total < 0) throw new IllegalArgumentException("Insufficient item " + itemId);
        state.items.put(itemId, total);
        int index = previous == null ? 1000 + itemId : previous.getItemIndex();
        UpdateItem.Builder item = UpdateItem.newBuilder().setItemIndex(index);
        if (total > 0) item.setPackItem(PackItemVo.newBuilder().setKey(itemId).setSize((int) total)
                .setObjectId(previous == null ? context.getId() * 1000000 + itemId : previous.getPackItem().getObjectId())
                .setCreateTime(previous == null ? System.currentTimeMillis() : previous.getPackItem().getCreateTime())
                .setLastGainTime(System.currentTimeMillis()));
        return item.build();
    }

    private void grantChapter17Rewards(IPlayerContext context,
            com.fasterxml.jackson.databind.JsonNode rewards, int operation, boolean show) {
        List<UpdateItem> items = new ArrayList<>();
        PurseUpdateResp.Builder purse = PurseUpdateResp.newBuilder();
        RewardResp.Builder visible = RewardResp.newBuilder().setOperationType(operation);
        for (com.fasterxml.jackson.databind.JsonNode r : rewards) {
            int id = r.path("Id").asInt();
            long amount = r.path("Amount").asLong();
            if ("currency".equals(r.path("Type").asText())) {
                Long previous = context instanceof com.doupo.server.foundation.player.PlayerConnectionContext
                        ? ((com.doupo.server.foundation.player.PlayerConnectionContext) context).currencyBalance(id) : null;
                long total = Math.addExact(previous == null ? later(context.getId()).currencies.getOrDefault(id, 0L) : previous, amount);
                later(context.getId()).currencies.put(id, total);
                purse.addItems(CurrencyItemVo.newBuilder().setType(id).setValue(total));
            } else {
                items.add(changeChapter17Item(context, id, amount));
            }
            visible.addRewardItemVos(RewardItemVo.newBuilder().setItemKey(id).setAmount(amount));
        }
        if (!items.isEmpty()) context.write(50402, packUpdate(operation, items.toArray(new UpdateItem[0])), 0);
        if (purse.getItemsCount() > 0) context.write(50651, purse.build(), 0);
        if (show) context.write(50406, visible.build(), 0);
    }

    private void finishChapter17Fight(IPlayerContext context, ChapterConfig.Chapter chapter) {
        int id = chapter.getChapterId();
        Chapter17Progress state = later(context.getId());
        int next = chapter.getNextChapterId();
        int history = chapter.isBoss() ? id : id - id % 10 + 3;
        if (chapter.isBoss()) {
            if (state.bossRewarded.add(id)) {
                grantChapter17Rewards(context, Chapter17Data.row("chapters", "Id", id).path("PassRewardRes"), 61951, true);
            }
            markChapterPassed(context.getId(), id);
            refreshChapter17Tasks(context);
            if (id == 10300505 && reputationLevels.getOrDefault(context.getId(), 0) < 2) next = 10300501;
            if (id == 10301005 && reputationLevels.getOrDefault(context.getId(), 0) < 3) next = 10301001;
        } else if (id % 10 == 3) {
            bossUnlockedWave.put(context.getId(), id - 2);
        }
        if (next == 0) {
            LOGGER.warn("UNRESOLVED battle continuation beyond captured scope: player={}, chapter={}", context.getId(), id);
            return;
        }
        ChapterConfig.Chapter following = ChapterConfig.get(next);
        int gateBoss = id >= 10300501 && id <= 10300505 ? 10300505
                : id >= 10301001 && id <= 10301005 ? 10301005 : 0;
        boolean gate = gateBoss != 0 && hasPassedChapter(context.getId(), gateBoss)
                && next >= gateBoss - 4 && next <= gateBoss;
        boolean gateReady = reputationLevels.getOrDefault(context.getId(), 0) >= (gateBoss == 10300505 ? 2 : 3);
        int firstWave = id - id % 10 + 1;
        boolean bossOpen = bossUnlockedWave.getOrDefault(context.getId(), 0) == firstWave;
        // 抓包 18–20：首通 01/02 与未卡声望的 Boss 为 true；第三波循环、填充波、声望门槛循环为 false。
        boolean looping = gate || (!chapter.isBoss() && bossOpen);
        MainMapPassChapterUpdateResp.Builder response = MainMapPassChapterUpdateResp.newBuilder()
                .setMainMapChapterId(next).setHistoryTopId(gate ? gateBoss : chapter.isBoss() ? id : bossOpen ? history : id)
                .setStageTime(following.getStageTime()).setLastStageTime(continuationLastStageTime(following))
                .setHasReward(!looping)
                .setKillMonsterPreHour(180)
                .setLoseBackId(following.getLoseBackId()).setResetState(chapter.isBoss() || id % 10 == 3)
                .setChangeReason(gate ? gateReady ? 4 : 3 : !chapter.isBoss() && bossOpen ? 4 : 1)
                .setNextChallengeId(gate ? gateBoss == 10300505 ? 10300601 : 10400101
                        : !chapter.isBoss() && bossOpen ? firstWave + 4 : 0);
        context.write(61952, response.build(), 0);
        context.write(62003, ByteString.EMPTY, 0);
        guidanceStates.put(context.getId(), new GuidanceState(next));
        LOGGER.info("Main chapter advanced: player={}, cleared={}, next={}, reason={}, nextChallenge={}",
                context.getId(), id, next, response.getChangeReason(), response.getNextChallengeId());
    }

    private static int continuationLastStageTime(ChapterConfig.Chapter chapter) {
        if (chapter.getChapterId() >= 10300801) {
            return Chapter17Data.row("transitions", "chapterId", chapter.getChapterId()).path("lastStageTime").asInt();
        }
        return chapter.getStageTime() == 10 ? 6 : 10;
    }

    private void writeFireTowerInfo(IPlayerContext context) {
        Chapter17Progress state = later(context.getId());
        if (!state.towerOpen) return;
        com.doupo.protocol.FireTowerInfoResp.Builder info = com.doupo.protocol.FireTowerInfoResp.newBuilder()
                .setCurrentChapterId(1).setOpenCG(state.towerCG).setMaxPassNum(19)
                .setHosting(state.hosting).setStartTime(state.towerRewardStart);
        for (com.fasterxml.jackson.databind.JsonNode chapter : Chapter17Data.DATA.path("towerChapters")) {
            int id = chapter.path("Id").asInt();
            info.addChapters(com.doupo.protocol.FireTowerChapterProgressVo.newBuilder()
                    .setChapterId(id).setUnlocked(id == 1).setPassNum(id == 1 ? state.towerPass : 0));
        }
        context.write(75251, info.build(), 0);
    }

    @PlayerCmd
    public void fireTowerSeeCG(IPlayerContext context, com.doupo.protocol.FireTowerSeeCGReq request) {
        if (!later(context.getId()).towerOpen) return;
        later(context.getId()).towerCG = true;
        context.write(75267, com.doupo.protocol.FireTowerSeeCGResp.getDefaultInstance(), 0);
    }

    @PlayerCmd
    public void fireTowerIn(IPlayerContext context, com.doupo.protocol.FireTowerInReq request) {
        writeFireTowerInfo(context);
    }

    @PlayerCmd
    public void fireTowerGiftInfo(IPlayerContext context, com.doupo.protocol.FireTowerGiftInfoReq request) {
        if (!later(context.getId()).towerOpen) return;
        com.doupo.protocol.FireTowerGiftInfoResp.Builder info = com.doupo.protocol.FireTowerGiftInfoResp.newBuilder();
        Set<Integer> ids = new java.util.TreeSet<>();
        for (com.fasterxml.jackson.databind.JsonNode row : Chapter17Data.DATA.path("towerRewards")) {
            ids.add(row.path("TowerId").asInt());
        }
        for (int id : ids) info.addGifts(com.doupo.protocol.FireTowerGiftVo.newBuilder().setTowerId(id));
        // 本地服不冒用官服的全服通关人数和排名。
        context.write(75284, info.build(), 0);
    }

    @PlayerCmd
    public void fireTowerPrepare(IPlayerContext context, com.doupo.protocol.PrePareFightReq request) {
        if (request.getPrePareSceneType() != 3 || !later(context.getId()).towerOpen) return;
        Chapter17Progress state = later(context.getId());
        int floor = state.towerPending > 0 ? state.towerPending : state.towerPass + 1;
        if (floor > 3) return;
        String file = floor == 1 ? "9503-79552.bin" : floor == 2 ? "9554-79552.bin" : "9569-79552.bin";
        try {
            com.doupo.protocol.PrePareFightResp.Builder response = com.doupo.protocol.PrePareFightResp
                    .parseFrom(Chapter17Data.bytes(file)).toBuilder();
            for (com.doupo.protocol.PrePareSceneUnitVo.Builder unit : response.getAtkBuilderList()) {
                unit.setSceneUnitVo(WutanBattleLogIdentity.bindPreviewHero(unit.getSceneUnitVo(), context.getId()));
            }
            context.write(79552, response.build(), 0);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) { throw new IllegalStateException(e); }
    }

    @PlayerCmd
    public void fireTowerChallenge(IPlayerContext context, com.doupo.protocol.FireTowerChallengeReq request) {
        Chapter17Progress state = later(context.getId());
        if (!state.towerOpen || state.hosting || state.towerPass >= 3) {
            writeFireTowerInfo(context);
            return;
        }
        int floor = state.towerPass + 1;
        com.doupo.protocol.BattleLogResp log = WutanBattleLog.towerLog(context, floor,
                request.getX(), request.getY(), request.getZ());
        state.towerPending = floor;
        context.write(75253, com.doupo.protocol.FireTowerChallengeResp.newBuilder()
                .setConfigId(floor).setBattleLog(log).setResult(0).build(), 0);
    }

    @PlayerCmd
    public void fireTowerAfter(IPlayerContext context, com.doupo.protocol.FireTowerChallengeAfterReq request) {
        Chapter17Progress state = later(context.getId());
        int floor = state.towerPending;
        if (floor == 0 || floor != state.towerPass + 1 || state.hosting) return;
        state.towerPending = 0;
        WutanCapturedLog.Capture capture = WutanCapturedLog.load(floor);
        if (capture == null || !capture.win) return;
        long duration;
        try {
            com.doupo.protocol.BattleLogVO log = com.doupo.protocol.BattleLogVO.parseFrom(capture.data);
            duration = log.getEntryList(log.getEntryListCount() - 1).getTime();
        } catch (com.google.protobuf.InvalidProtocolBufferException e) { throw new IllegalStateException(e); }
        context.write(75257, com.doupo.protocol.FireTowerChallengeAfterResp.newBuilder()
                .setPassId(floor).setWin(true).setFightDurationMs(duration).build(), 0);
        passFireTowerFloor(context, floor, false);
    }

    private void passFireTowerFloor(IPlayerContext context, int floor, boolean hosting) {
        Chapter17Progress state = later(context.getId());
        if (floor != state.towerPass + 1) return;
        state.towerPass = floor;
        if (state.towerRewardStart == 0) state.towerRewardStart = System.currentTimeMillis();
        if (!hosting) {
            grantChapter17Rewards(context, Chapter17Data.row("tower", "Id", floor).path("Rewards"), 75254, false);
        }
        writeFireTowerInfo(context);
        refreshChapter17Tasks(context);
    }

    @PlayerCmd
    public void fireTowerHost(IPlayerContext context, com.doupo.protocol.FireTowerHostReq request) {
        Chapter17Progress state = later(context.getId());
        if (!state.towerOpen || state.towerPass < 2) return;
        if (state.hosting == request.getEnable()) {
            context.write(75276, com.doupo.protocol.FireTowerHostResp.newBuilder().setHosting(state.hosting).build(), 0);
            return;
        }
        state.hosting = request.getEnable() && state.towerPass < 19;
        int generation = ++state.hostGeneration;
        context.write(75276, com.doupo.protocol.FireTowerHostResp.newBuilder().setHosting(state.hosting).build(), 0);
        if (!state.hosting) { writeFireTowerInfo(context); return; }
        state.hostStartPass = state.towerPass;
        state.towerPending = 0;
        long offset = 0;
        for (com.fasterxml.jackson.databind.JsonNode event : Chapter17Data.DATA.path("towerHost")) {
            if (event.path("pass").asInt() <= state.towerPass) offset = event.path("afterMs").asLong();
        }
        for (com.fasterxml.jackson.databind.JsonNode event : Chapter17Data.DATA.path("towerHost")) {
            int floor = event.path("pass").asInt();
            if (floor <= state.hostStartPass) continue;
            scheduleCombatAction(context, event.path("afterMs").asLong() - offset, () -> {
                if (laterProgress.get(context.getId()) != state || !state.hosting || state.hostGeneration != generation) return;
                passFireTowerFloor(context, floor, true);
                if (floor == 19) {
                    state.hosting = false;
                    context.write(75277, com.doupo.protocol.FireTowerHostEndResp.newBuilder().setReason(1).build(), 0);
                    writeFireTowerInfo(context);
                }
            });
        }
    }

    @PlayerCmd
    public void fireTowerHostResult(IPlayerContext context, com.doupo.protocol.FireTowerHostResultReq request) {
        Chapter17Progress state = later(context.getId());
        if (!state.towerOpen) return;
        if (state.hostStartPass >= 2 && state.towerPass > state.hostStartPass) {
            LOGGER.warn("UNRESOLVED tower host reward settlement: player={}, from={}, to={}",
                    context.getId(), state.hostStartPass, state.towerPass);
        }
        // 抓包只有 hasResult=false。未取得正向结算证据前，不伪造已领奖或奖励入账。
        context.write(75279, com.doupo.protocol.FireTowerHostResultResp.getDefaultInstance(), 0);
    }

    private static LotteryInfoResp parseLotteryInfo(String name) {
        try { return LotteryInfoResp.parseFrom(Chapter17Data.bytes(name)); }
        catch (com.google.protobuf.InvalidProtocolBufferException e) { throw new IllegalStateException(e); }
    }

    /** 抓包 idx10161–10180 的首次通用技能引导抽取；非通用随机奖池。 */
    private void drawChapter17CommonSkill(IPlayerContext context, LotteryDrawReq request) {
        Chapter17Progress state = later(context.getId());
        if (state.commonDrawn) {
            drawContinuationCommonSkill(context, request);
            return;
        }
        if (state.commonDrawn || !state.rewarded.contains(200102) || request.getTen()
                || request.getSubId() != 0 || state.items.getOrDefault(100212, 0L) < 10) return;
        try {
            LotteryDrawResp response = LotteryDrawResp.parseFrom(Chapter17Data.bytes("10165-77352.bin"));
            PackUpdateResp.Builder pack = PackUpdateResp.parseFrom(Chapter17Data.bytes("10164-50402.bin")).toBuilder();
            long now = System.currentTimeMillis();
            for (PackUpdateVo.Builder p : pack.getPacksBuilderList()) {
                for (UpdateItem.Builder item : p.getUpdateItemsBuilderList()) {
                    item.getPackItemBuilder().setObjectId(context.getId() * 1000 + 500 + item.getItemIndex())
                            .setCreateTime(now).setLastGainTime(now);
                }
            }
            context.write(50402, packUpdate(77351, changeChapter17Item(context, 100212, -10)), 0);
            state.commonDrawn = true;
            context.write(50402, pack.build(), 0);
            context.write(77352, response, 0);
            context.write(77354, parseLotteryInfo("10166-77354.bin"), 0);
            for (int idx : new int[] { 10168, 10171, 10174, 10177 }) {
                com.doupo.protocol.CommonSkillLevelUpResp level = com.doupo.protocol.CommonSkillLevelUpResp
                        .parseFrom(Chapter17Data.bytes(idx + "-82056.bin"));
                PackUpdateResp.Builder consume = PackUpdateResp.newBuilder().setOperationType(82052);
                for (PackUpdateVo.Builder p : pack.getPacksBuilderList()) {
                    PackUpdateVo.Builder changes = PackUpdateVo.newBuilder().setPackType(p.getPackType());
                    for (UpdateItem.Builder item : p.getUpdateItemsBuilderList()) {
                        if (item.getPackItem().getKey() == level.getCommonSkillId()) {
                            item.getPackItemBuilder().setSize(item.getPackItem().getSize() - 1);
                            changes.addUpdateItems(item);
                        }
                    }
                    consume.addPacks(changes);
                }
                context.write(50402, consume.build(), 0);
                context.write(82056, level, 0);
                state.commonSkillLevels.put(level.getCommonSkillId(), level.getNewLevel());
                writeCommonSkills(context);
            }
            for (PackUpdateVo p : pack.getPacksList()) {
                if (p.getPackType() != 8) continue;
                for (UpdateItem item : p.getUpdateItemsList()) {
                    if (item.hasPackItem() && item.getPackItem().getSize() > 0) {
                        state.commonSkillItems.put(item.getPackItem().getKey(), item);
                    }
                }
            }
            refreshChapter17Tasks(context);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) { throw new IllegalStateException(e); }
    }

    /** 同一阶第二次十连使用抓包10205奖项样本；库存累加，不覆盖成官服账号的包。 */
    private void drawContinuationCommonSkill(IPlayerContext context, LotteryDrawReq request) {
        Chapter17Progress state = later(context.getId());
        if (!state.tasks.contains(200107) || state.continuationDrawn || request.getTen()
                || request.getSubId() != 0 || chapter17ItemCount(context, 100212) < 10) return;
        try {
            LotteryDrawResp result = LotteryDrawResp.parseFrom(Chapter17Data.bytes("10205-77352.bin"));
            Map<Integer, Integer> gains = new java.util.TreeMap<>();
            for (RewardItemVo reward : result.getRewardItemVosList()) {
                gains.merge(reward.getItemKey(), Math.toIntExact(reward.getAmount()), Math::addExact);
            }
            PackUpdateVo.Builder changes = PackUpdateVo.newBuilder().setPackType(8);
            long now = System.currentTimeMillis();
            for (Map.Entry<Integer, Integer> gain : gains.entrySet()) {
                UpdateItem previous = state.commonSkillItems.get(gain.getKey());
                UpdateItem updated = previous == null
                        ? UpdateItem.newBuilder().setItemIndex(gain.getKey()).setPackItem(PackItemVo.newBuilder()
                                .setKey(gain.getKey()).setSize(gain.getValue())
                                .setObjectId(context.getId() * 1000000 + gain.getKey())
                                .setCreateTime(now).setLastGainTime(now)).build()
                        : previous.toBuilder().setPackItem(previous.getPackItem().toBuilder()
                                .setSize(Math.addExact(previous.getPackItem().getSize(), gain.getValue()))
                                .setLastGainTime(now)).build();
                state.commonSkillItems.put(gain.getKey(), updated);
                changes.addUpdateItems(updated);
            }
            context.write(50402, packUpdate(77351, changeChapter17Item(context, 100212, -10)), 0);
            context.write(50402, PackUpdateResp.newBuilder().setOperationType(77352).addPacks(changes).build(), 0);
            context.write(77352, result, 0);
            context.write(77354, parseLotteryInfo("10206-77354.bin"), 0);
            for (int skill : gains.keySet()) {
                if (state.commonSkillLevels.getOrDefault(skill, 0) != 0) continue;
                UpdateItem item = state.commonSkillItems.get(skill);
                int count = item.getPackItem().getSize() - 1;
                UpdateItem consumed = count == 0 ? item.toBuilder().clearPackItem().build()
                        : item.toBuilder().setPackItem(item.getPackItem().toBuilder().setSize(count)).build();
                if (count == 0) state.commonSkillItems.remove(skill); else state.commonSkillItems.put(skill, consumed);
                writeCommonSkillConsumption(context, java.util.Collections.singletonList(consumed));
                state.commonSkillLevels.put(skill, 1);
                context.write(82056, com.doupo.protocol.CommonSkillLevelUpResp.newBuilder()
                        .setCommonSkillId(skill).setOldLevel(0).setNewLevel(1).build(), 0);
            }
            writeCommonSkills(context);
            state.continuationDrawn = true;
            refreshChapter17Tasks(context);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) { throw new IllegalStateException(e); }
    }

    private void writeCommonSkills(IPlayerContext context) {
        Chapter17Progress state = later(context.getId());
        com.doupo.protocol.PlayerCommonSkillResp.Builder info = com.doupo.protocol.PlayerCommonSkillResp.newBuilder();
        state.commonSkillSlots.forEach((slot, skill) -> info.addWearIds(
                IntegerAndIntegerPairEntry.newBuilder().setKey(slot).setValue(skill)));
        state.commonSkillLevels.forEach((skill, level) -> info.addCommonSkillLevel(
                IntegerAndIntegerPairEntry.newBuilder().setKey(skill).setValue(level)));
        context.write(82051, info.build(), 0);
    }

    /** 第二次抓包 10233-10236；客户端提交完整槽位快照，空列表表示全部卸下。 */
    @PlayerCmd
    public void putOnCommonSkill(IPlayerContext context, com.doupo.protocol.CommonSkillPutOnReq request) {
        Chapter17Progress state = later(context.getId());
        java.util.Map<Integer, Integer> selected = new java.util.TreeMap<>();
        java.util.Set<Integer> skillIds = new java.util.HashSet<>();
        com.fasterxml.jackson.databind.JsonNode slots = Chapter17Data.COMMON_SKILLS.path("slots");
        int heroLevel = heroLevels.getOrDefault(context.getId(), 1);
        for (IntegerAndIntegerPairEntry pair : request.getSlotAndSkillIdList()) {
            int slot = pair.getKey();
            int skill = pair.getValue();
            if (slot < 1 || slot > slots.size() || heroLevel < slots.get(slot - 1).asInt()
                    || state.commonSkillLevels.getOrDefault(skill, 0) <= 0
                    || selected.containsKey(slot) || !skillIds.add(skill)) {
                LOGGER.warn("Invalid common skill assignment: player={}, slot={}, skill={}", context.getId(), slot, skill);
                return;
            }
            selected.put(slot, skill);
        }
        state.commonSkillSlots.clear();
        state.commonSkillSlots.putAll(selected);
        writeCommonSkills(context);
        com.doupo.protocol.CommonSkillPutOnResp.Builder response = com.doupo.protocol.CommonSkillPutOnResp.newBuilder();
        selected.forEach((slot, skill) -> response.addSlotAndSkillId(
                IntegerAndIntegerPairEntry.newBuilder().setKey(slot).setValue(skill)));
        context.write(82055, response.build(), 0);
        LOGGER.info("Common skills equipped: player={}, slots={}", context.getId(), selected);
    }

    @PlayerCmd
    public void commonSkillLevelUp(IPlayerContext context, com.doupo.protocol.CommonSkillLevelUpReq request) {
        int skill = request.getCommonSkillId();
        Chapter17Progress state = later(context.getId());
        int before = state.commonSkillLevels.getOrDefault(skill, 0);
        UpdateItem consumed = upgradeCommonSkill(state, skill, false);
        if (consumed == null) return;
        writeCommonSkillConsumption(context, java.util.Collections.singletonList(consumed));
        context.write(82056, com.doupo.protocol.CommonSkillLevelUpResp.newBuilder()
                .setCommonSkillId(skill).setOldLevel(before).setNewLevel(state.commonSkillLevels.get(skill)).build(), 0);
        writeCommonSkills(context);
    }

    /** 使用当前库存和6.9表的逐级Exp成本；不能照抄官服账号的一键升级结果。 */
    @PlayerCmd
    public void oneKeyCommonSkillLevelUp(IPlayerContext context, com.doupo.protocol.CommonSkillOneKeyLevelUpReq request) {
        Chapter17Progress state = later(context.getId());
        List<UpdateItem> consumed = new ArrayList<>();
        com.doupo.protocol.CommonSkillOneKeyLevelUpResp.Builder response = com.doupo.protocol.CommonSkillOneKeyLevelUpResp.newBuilder();
        for (int skill : state.commonSkillLevels.keySet()) {
            int before = state.commonSkillLevels.get(skill);
            UpdateItem item = upgradeCommonSkill(state, skill, true);
            if (item == null) continue;
            consumed.add(item);
            response.addOldSkillLevel(IntegerAndIntegerPairEntry.newBuilder().setKey(skill).setValue(before));
            response.addNewSkillLevel(IntegerAndIntegerPairEntry.newBuilder().setKey(skill).setValue(state.commonSkillLevels.get(skill)));
        }
        if (!consumed.isEmpty()) writeCommonSkillConsumption(context, consumed);
        context.write(82057, response.build(), 0);
        writeCommonSkills(context);
        LOGGER.info("Common skills upgraded: player={}, changed={}", context.getId(), consumed.size());
    }

    private static UpdateItem upgradeCommonSkill(Chapter17Progress state, int skill, boolean all) {
        int level = state.commonSkillLevels.getOrDefault(skill, 0);
        UpdateItem item = state.commonSkillItems.get(skill);
        if (level == 0 || item == null) return null;
        int remaining = item.getPackItem().getSize();
        int before = level;
        while (true) {
            com.fasterxml.jackson.databind.JsonNode current = commonSkillLevelConfig(skill, level);
            if (current == null || commonSkillLevelConfig(skill, level + 1) == null) break;
            int cost = current.path("Exp").asInt();
            if (cost <= 0 || remaining < cost) break;
            remaining -= cost;
            level++;
            if (!all) break;
        }
        if (before == level) return null;
        state.commonSkillLevels.put(skill, level);
        UpdateItem.Builder update = item.toBuilder();
        if (remaining == 0) {
            update.clearPackItem();
            state.commonSkillItems.remove(skill);
        } else {
            update.getPackItemBuilder().setSize(remaining);
            state.commonSkillItems.put(skill, update.build());
        }
        return update.build();
    }

    private static com.fasterxml.jackson.databind.JsonNode commonSkillLevelConfig(int skill, int level) {
        int group = -1;
        for (com.fasterxml.jackson.databind.JsonNode base : Chapter17Data.COMMON_SKILLS.path("skills")) {
            if (base.path("Id").asInt() == skill) { group = base.path("LevelGroup").asInt(); break; }
        }
        for (com.fasterxml.jackson.databind.JsonNode row : Chapter17Data.COMMON_SKILLS.path("levels")) {
            if (row.path("LevelGroup").asInt() == group && row.path("Level").asInt() == level) return row;
        }
        return null;
    }

    private static void writeCommonSkillConsumption(IPlayerContext context, List<UpdateItem> items) {
        context.write(50402, PackUpdateResp.newBuilder().setOperationType(82052)
                .addPacks(PackUpdateVo.newBuilder().setPackType(8).addAllUpdateItems(items)).build(), 0);
    }

    private static TaskUpdateResp nextTaskUpdate(int taskId) {
        return TaskUpdateResp.newBuilder()
                .addTaskVos(task(taskId, TaskPhase.PROGRESS, 0))
                .build();
    }

    private static TaskVo task(
            int taskResourceId,
            TaskPhase phase,
            long progress) {
        return TaskVo.newBuilder()
                .setTaskId(TaskUniqueKey.newBuilder()
                        .setTaskResourceId(taskResourceId)
                        .setActivityId(0))
                .setTaskPhase(phase)
                .addProgressValues(progress)
                .build();
    }

    /**
     * 小怪关打完，解锁本关 Boss。
     *
     * <p>对应抓包 {@code changeReason=4}：{@code mainMapChapterId} 仍是刚打完的
     * 小怪关，{@code nextChallengeId} 指向 Boss 关。
     */
    private static MainMapPassChapterUpdateResp chapterCleared(
            ChapterConfig.Chapter chapter) {
        return MainMapPassChapterUpdateResp.newBuilder()
                .setMainMapChapterId(chapter.getChapterId())
                .setPassStar(0)
                .setHasReward(false)
                .setStageTime(chapter.getStageTime())
                .setLastStageTime(0)
                .setLoseBackId(chapter.getLoseBackId())
                .setNextChallengeId(chapter.getNextChapterId())
                .setGm(false)
                .setResetState(true)
                .setHistoryTopId(chapter.getChapterId())
                .setChangeReason(4)
                .setFromResetReq(0)
                .setFastPass(false)
                .setKillMonsterPreHour(0)
                .build();
    }

    /** 6-8 关三波循环推进，第三波打完后解锁本关 Boss（抓包 idx 2181/6281/7164）。 */
    private static MainMapPassChapterUpdateResp sixthWaveAdvanced(
            ChapterConfig.Chapter chapter,
            boolean bossUnlocked) {
        int firstWave = chapter.getChapterId() - chapter.getChapterId() % 10 + 1;
        int thirdWave = firstWave + 2;
        int bossChapterId = firstWave + 4;
        int nextChapterId = chapter.getNextChapterId();
        return MainMapPassChapterUpdateResp.newBuilder()
                .setMainMapChapterId(nextChapterId)
                .setPassStar(0)
                .setHasReward(!bossUnlocked)
                .setStageTime(chapter.getStageTime())
                .setLastStageTime(previousBossStageTime(chapter))
                .setLoseBackId(chapter.getLoseBackId())
                .setNextChallengeId(bossUnlocked ? bossChapterId : 0)
                .setGm(false)
                .setResetState(nextChapterId == firstWave)
                .setHistoryTopId(
                        bossUnlocked ? thirdWave : chapter.getChapterId())
                .setChangeReason(bossUnlocked ? 4 : 1)
                .setFromResetReq(0)
                .setFastPass(false)
                .setKillMonsterPreHour(0)
                .build();
    }

    /**
     * 玩家确认进入 Boss 关。
     *
     * <p>对应抓包 {@code changeReason=0, fromResetReq=1}。
     */
    private static MainMapPassChapterUpdateResp chapterEntered(
            ChapterConfig.Chapter chapter,
            int historyTopId) {
        return MainMapPassChapterUpdateResp.newBuilder()
                .setMainMapChapterId(chapter.getChapterId())
                .setPassStar(0)
                .setHasReward(true)
                .setStageTime(chapter.getStageTime())
                .setLastStageTime(previousBossStageTime(chapter))
                .setLoseBackId(chapter.getLoseBackId())
                .setNextChallengeId(0)
                .setGm(false)
                .setResetState(true)
                .setHistoryTopId(historyTopId)
                .setChangeReason(0)
                .setFromResetReq(1)
                .setFastPass(false)
                .setKillMonsterPreHour(chapter.getChapterId() >= 10300501 ? 180 : 0)
                .build();
    }

    private static int previousBossStageTime(
            ChapterConfig.Chapter chapter) {
        if (chapter.getChapterId() >= 10300801) return continuationLastStageTime(chapter);
        ChapterConfig.Chapter previousBoss =
                ChapterConfig.get(chapter.getLoseBackId() + 4);
        if (previousBoss == null
                || previousBoss.getChapterId() == chapter.getChapterId()) {
            return 0;
        }
        return previousBoss.getStageTime();
    }

    /**
     * Boss 打完，推进到下一大关。
     *
     * <p>对应抓包 {@code changeReason=1}：{@code mainMapChapterId} 已经是新的一关，
     * {@code historyTopId} 是刚打完的 Boss 关。
     */
    private static MainMapPassChapterUpdateResp chapterAdvanced(
            ChapterConfig.Chapter nextChapter,
            int clearedBossChapterId) {
        ChapterConfig.Chapter clearedBoss =
                ChapterConfig.get(clearedBossChapterId);
        return MainMapPassChapterUpdateResp.newBuilder()
                .setMainMapChapterId(nextChapter.getChapterId())
                .setPassStar(0)
                .setHasReward(true)
                .setStageTime(nextChapter.getStageTime())
                .setLastStageTime(
                        clearedBoss == null
                                ? 0
                                : clearedBoss.getStageTime())
                .setLoseBackId(nextChapter.getLoseBackId())
                .setNextChallengeId(0)
                .setGm(false)
                .setResetState(true)
                .setHistoryTopId(clearedBossChapterId)
                .setChangeReason(1)
                .setFromResetReq(0)
                .setFastPass(false)
                .setKillMonsterPreHour(0)
                .build();
    }

    /**
     * 按章节配置生成本关所有怪物的可见快照。
     *
     * <p>场景单位 ID 由 {@link #allocateWaveUnitIdBase} 按波次实例发号，
     * 调用方负责把这批 ID 连同快照一起保存成 {@link WaveInstance}；
     * 站位、属性、掉落全部来自 {@link ChapterConfig}。
     */
    private static List<SceneUpdateVisibleResp> monsterSnapshots(
            ChapterConfig.Chapter chapter,
            GuidanceMainMapMonsterEnterReq request,
            long firstUnitId) {

        long createTime = System.currentTimeMillis();
        List<ChapterConfig.MonsterSpawn> spawns = chapter.getMonsters();
        List<SceneUpdateVisibleResp> result = new ArrayList<>(spawns.size());
        float requestX = request.getX();
        float requestY = request.getY();
        float requestZ = request.getZ();
        if (chapter.getChapterId() == 10200105
                && request.getChapterId() == 0) {
            requestX = 0;
            requestY = 7.042f;
            requestZ = 1080.9f;
        }

        for (int i = 0; i < spawns.size(); i++) {
            ChapterConfig.MonsterSpawn spawn = spawns.get(i);

            result.add(monsterSnapshot(
                    firstUnitId + i,
                    requestX + spawn.getX(),
                    requestY,
                    requestZ + spawn.getZ(),
                    spawn.getDir(),
                    spawn.getMonsterId(),
                    spawn.getTemplateId(),
                    spawn.getAttack(),
                    spawn.getMaxHp(),
                    spawn.getSceneHp(),
                    spawn.getAttribute161001(),
                    spawn.getAttribute165001(),
                    createTime,
                    chapter.getChapterId(),
                    spawn.getRewardItemKey(),
                    spawn.getRewardAmount()));
        }

        return result;
    }

    /** 一个波次实例的 ID 号段宽度；第七、八关每波 6 只怪，留 10 个够用。 */
    private static final int WAVE_UNIT_ID_STRIDE = 10;

    /** 号段格数；100 + 80 * 10 + 6 仍落在玩家自己的 1000 号段内。 */
    private static final int WAVE_UNIT_ID_SLOTS = 80;

    /**
     * 给一个新的波次实例分配怪物场景单位 ID 的起始号。
     *
     * <p>官服每个波次实例都拿全新的 ID：抓包 idx 1964 的第一波是
     * 763746993383145819 起，idx 1976 预加载的第二波是 763746993766924635 起，
     * 两波之间差了三亿多，绝不会重叠。旧写法按章节后缀发号
     *（{@code 100 + chapterId % 1000}）相邻两波只差 1，而一波有 3~6 只怪，
     * 于是第一波和第二波的 ID 大面积重叠——客户端
     * {@code SceneComponentObjectMgr.TryReuseExistingServerObject} 见到重复 ID
     * 会复用旧对象并 {@code ReDeserialize}，旧波的 50757 遗忘也会把已经变成
     * 下一波的怪删掉，表现就是预加载之后少怪、错位、怪物凭空消失。
     *
     * <p>本服的 ID 必须留在玩家自己的 {@code playerId * 1000} 号段内，
     * 所以按 10 一格滚动发号：相邻波次、同一波的下一轮循环、Boss 各占一格，
     * 走满 {@link #WAVE_UNIT_ID_SLOTS} 格才会重用，那时旧对象早已被遗忘。
     */
    private long allocateWaveUnitIdBase(long playerId) {
        int slot = waveUnitIdSlots.merge(
                playerId,
                1,
                (current, unused) -> current % WAVE_UNIT_ID_SLOTS + 1);
        return playerId * 1000 + 100 + (long) slot * WAVE_UNIT_ID_STRIDE;
    }

    private static SceneUpdateVisibleResp monsterSnapshot(
            long sceneUnitId,
            float x,
            float y,
            float z,
            float dir,
            int monsterId,
            long templateId,
            double health,
            double defense,
            double sceneHealth,
            double attribute161001,
            double attribute165001,
            long createTime,
            int chapterId,
            int rewardItemKey,
            long rewardAmount) {
        SceneFightUnitInfoVo.Builder fightInfo = SceneFightUnitInfoVo.newBuilder()
                .setCampId(1)
                .addAttributeList(attribute(101001, health))
                .addAttributeList(attribute(102001, 1))
                .addAttributeList(attribute(103001, defense))
                .addAttributeList(attribute(103011, defense))
                .addAttributeList(attribute(103012, 0))
                .addAttributeList(attribute(103101, 100))
                .addAttributeList(attribute(103111, 30))
                .addAttributeList(attribute(104001, 100))
                .addAttributeList(attribute(105001, 1))
                .addAttributeList(attribute(105101, 0))
                .addAttributeList(attribute(106001, 99999999))
                .addAttributeList(attribute(106101, 5));

        if (attribute161001 != 0) {
            fightInfo.addAttributeList(
                    attribute(161001, attribute161001));
        }
        if (attribute165001 != 0) {
            fightInfo.addAttributeList(
                    attribute(165001, attribute165001));
        }

        fightInfo.addAttributeList(attribute(191001, sceneHealth))
                .addAttributeList(attribute(191002, 0))
                .addAttributeList(attribute(191005, 0))
                .addAttributeList(attribute(196002, 10000))
                .addAttributeList(attribute(196004, 10000));

        SceneMonsterVo.Builder monsterVo = SceneMonsterVo.newBuilder()
                .setMonsterId(monsterId)
                .setTemplateId(templateId)
                .setElementId(0)
                .setCreateTime(createTime)
                .setLockTargetId(0)
                .setSceneBossResId(0)
                .setMainMapId(chapterId)
                .setFirstKill(true)
                .setScale(1);

        if (rewardItemKey != 0) {
            monsterVo.addMainMapReward(MonsterRewardVo.newBuilder()
                    .setItemKey(rewardItemKey)
                    .setAmount(rewardAmount));
        }

        SceneUnitVo unit = SceneUnitVo.newBuilder()
                .setBaseInfoVo(SceneUnitBaseInfoVo.newBuilder()
                        .setId(sceneUnitId)
                        .setX(x)
                        .setY(y)
                        .setZ(z)
                        .setState(0)
                        .setDir(dir)
                        .setUnitType(2))
                .setFightInfoVo(fightInfo)
                .setSceneMonsterVo(monsterVo)
                .build();

        return SceneUpdateVisibleResp.newBuilder()
                .setSnapshot(false)
                .addVisibleList(unit)
                .build();
    }

    public static SyncNonSceneHeroVoUpdateResp nonSceneHeroSnapshot(
            long playerId) {
        return SyncNonSceneHeroVoUpdateResp.newBuilder()
                .addVoList(buildPlayerUnit(playerId, false))
                .setOperationType(ServerVoUpdateType.ALL)
                .build();
    }

    private SceneUnitVo checkpointPlayerUnit(SceneUnitVo unit, long playerId) {
        SceneUnitVo.Builder player = unit.toBuilder();
        GuidanceState state = guidanceStates.get(playerId);
        int chapter = state == null ? Chapter9TestCheckpoint.CHAPTER : state.chapterId;
        double attack = playerAttack(playerId, chapter);
        double hp = restoreHp(playerId, chapter);
        int level = heroLevels.getOrDefault(playerId, Chapter9TestCheckpoint.LEVEL);
        PlayerRealmConfig.RealmStats base = PlayerRealmConfig.statsOf(level);
        SceneFightUnitInfoVo.Builder fight = player.getFightInfoVoBuilder();
        for (int i = 0; i < fight.getAttributeListCount(); i++) {
            int type = fight.getAttributeList(i).getType();
            double value = fight.getAttributeList(i).getValue();
            if (type == 101001 || type == 191001 || type == 191005) value = attack;
            if (type == 103001 || type == 103011) value = hp;
            if (type == 101002) value = base.atk;
            if (type == 102002) value = base.def;
            if (type == 103002) value = base.hp;
            if (type == 102001) value = level == Chapter9TestCheckpoint.LEVEL
                    ? Chapter9TestCheckpoint.DEFENSE : PlayerRealmConfig.combatDef(level);
            fight.setAttributeList(i, attribute(type, value));
        }
        player.getHeroVoBuilder().setCrossHeroShortInfo(currentHeroShortInfo(playerId))
                .setFightPower(playerFightForce.getOrDefault(playerId, (double) Chapter9TestCheckpoint.POWER));
        return player.build();
    }

    private HeroShortInfoUpdateResp currentHeroShortInfoUpdate(long playerId) {
        return HeroShortInfoUpdateResp.newBuilder()
                .setShortInfoVo(HeroShortInfoVo.newBuilder()
                        .setHeroIndex(0)
                        .setCrossHeroShortInfo(currentHeroShortInfo(playerId)))
                .build();
    }

    private CrossHeroShortInfo currentHeroShortInfo(long playerId) {
        return buildHeroShortInfo(playerId).toBuilder()
                .setLevel(heroLevels.getOrDefault(playerId, 1))
                .setStage(heroStages.getOrDefault(playerId, 1))
                .build();
    }

    private SceneUpdateVisibleResp overlayVisibleHeroLevel(
            SceneUpdateVisibleResp snapshot,
            long playerId) {
        if (snapshot.getVisibleListCount() == 0) {
            return snapshot;
        }
        SceneUnitVo.Builder player = snapshot.getVisibleList(0).toBuilder();
        player.getHeroVoBuilder()
                .setCrossHeroShortInfo(currentHeroShortInfo(playerId));
        return snapshot.toBuilder()
                .setVisibleList(0, player.build())
                .build();
    }

    static HeroShortInfoUpdateResp heroShortInfoUpdate(long playerId) {
        return HeroShortInfoUpdateResp.newBuilder()
                .setShortInfoVo(HeroShortInfoVo.newBuilder()
                        .setHeroIndex(0)
                        .setCrossHeroShortInfo(
                                buildHeroShortInfo(playerId)))
                .build();
    }

    private static SceneUnitVo buildPlayerUnit(
            long playerId,
            boolean sceneVisible) {
        long sceneUnitId = playerId * 1000 + 1;
        boolean firstSkillStarred =
                FIRST_SKILL_STAR_PLAYERS.contains(playerId);
        boolean thirdSkillStarred =
                THIRD_SKILL_STAR_PLAYERS.contains(playerId);
        double attack = firstSkillStarred
                ? 1133.54076
                : (thirdSkillStarred ? 920.7605 : 481.8);
        double defense = firstSkillStarred
                ? 401.5
                : (thirdSkillStarred ? 305 : 107);
        double hp = firstSkillStarred
                ? 29853.85
                : (thirdSkillStarred ? 24311.999999999996 : 12613.2);

        CrossHeroShortInfo heroShortInfo = buildHeroShortInfo(playerId);

        SceneHeroVo.Builder hero = SceneHeroVo.newBuilder()
                .setServerId(1)
                .setServerName("斗帝本地测试服")
                .setPlayerId(playerId)
                .setPlayerName("本地玩家")
                .setFightPower(sceneVisible
                        ? (firstSkillStarred
                                ? 17424
                                : (thirdSkillStarred ? 15202 : 10599))
                        : 0)
                .setLevel(sceneVisible ? 1 : 0)
                .setClientDriven(sceneVisible)
                .setCrossHeroShortInfo(heroShortInfo)
                .setDomainId(3);

        if (sceneVisible) {
            SkillContainerVO.Builder skills = SkillContainerVO.newBuilder();
            long[] skillIds = {
                    10111110101L,
                    10111120101L,
                    10111130101L,
                    10111140101L,
                    10110710101L,
                    10150510101L
            };

            for (long skillId : skillIds) {
                FightSkillUpdateResp.Builder skill =
                        FightSkillUpdateResp.newBuilder()
                                .setId(sceneUnitId)
                                .setSkillId(skillId)
                                .setCostCd(1);
                if (skillId == 10150510101L) {
                    // 主动技能（大招）：官服抓包 idx 1927，activeSkill=true。
                    skill.setCostMp(100)
                            .setCostCd(1000)
                            .setActiveSkill(true);
                }
                skills.addSkillList(skill);
            }

            VectorVo zero = VectorVo.newBuilder().build();
            hero.setOffset(zero)
                    .setSkillContainerVo(skills)
                    .setRunOffset(zero)
                    .setFightOffset(zero);
        }

        return SceneUnitVo.newBuilder()
                .setBaseInfoVo(SceneUnitBaseInfoVo.newBuilder()
                        .setId(sceneUnitId)
                        .setX(0)
                        .setY(0)
                        .setZ(-15)
                        .setState(0)
                        .setDir(0)
                        .setUnitType(1))
                .setFightInfoVo(SceneFightUnitInfoVo.newBuilder()
                        .setCampId(2)
                        .addAttributeList(attribute(101001, attack))
                        .addAttributeList(attribute(101002, 438))
                        .addAttributeList(attribute(102001, defense))
                        .addAttributeList(attribute(102002, 107))
                        .addAttributeList(attribute(103001, hp))
                        .addAttributeList(attribute(103002, 10968))
                        .addAttributeList(attribute(103011, hp))
                        .addAttributeList(attribute(103012, 0))
                        .addAttributeList(attribute(103101, 100))
                        .addAttributeList(attribute(103111, 30))
                        .addAttributeList(attribute(104001, 100))
                        .addAttributeList(attribute(105001, 1))
                        .addAttributeList(attribute(105101, 0))
                        .addAttributeList(attribute(106001, 1))
                        .addAttributeList(attribute(106101, 5))
                        .addAttributeList(attribute(161001, 1000))
                        .addAttributeList(attribute(165001, 1500))
                        .addAttributeList(attribute(
                                191001,
                                sceneVisible ? attack : 0))
                        .addAttributeList(attribute(
                                191002,
                                sceneVisible ? 1 : 0))
                        .addAttributeList(attribute(
                                191005,
                                sceneVisible ? attack : 0))
                        .addAttributeList(attribute(196002, 10000))
                        .addAttributeList(attribute(196004, 10000)))
                .setHeroVo(hero)
                .build();
    }

    private double playerAttack(long playerId, int chapterId) {
        return Math.max(
                chapterBaseAttack(playerId, chapterId),
                grantedAttack.getOrDefault(playerId, 0.0));
    }

    private static double chapterBaseAttack(long playerId, int chapterId) {
        switch (chapterId) {
            case 10100305:
                return 809.2535;
            case 10100405:
                return 865.007;
            case 10200101:
            case 10200102:
            case 10200103:
            case 10200105:
            case 10200201:
            case 10200202:
            case 10200203:
            case 10200205:
                return 1189.0766800000001;
            case 10200301:
            case 10200302:
            case 10200303:
            case 10200305:
                return 1529.7275300000003;
            case 10200401:
            case 10200402:
            case 10200403:
            case 10200405:
                return 1846.5465600000002;
            case 10200501:
            case 10200502:
            case 10200503:
            case 10200505:
            case 10300101:
            case 10300102:
            case 10300103:
            case 10300105:
            case 10300201:
            case 10300202:
            case 10300203:
            case 10300205:
                return 2093.8881600000004;
            default:
                break;
        }
        if (FIRST_SKILL_STAR_PLAYERS.contains(playerId)) {
            return 1133.54076;
        }
        return THIRD_SKILL_STAR_PLAYERS.contains(playerId) ? 920.7605 : 481.8;
    }

    private static CrossHeroShortInfo buildHeroShortInfo(long playerId) {
        CrossHeroShortInfo.Builder builder = CrossHeroShortInfo.newBuilder()
                .setHeroIdx(0)
                .setJob(1001)
                .setSceneUnitUid(playerId * 1000 + 1)
                .setLevel(1)
                .setStage(1)
                .addSkinList(HeroSkinPart.newBuilder()
                        .setType(1)
                        .setCfgId(101))
                .addSkinList(HeroSkinPart.newBuilder()
                        .setType(2)
                        .setCfgId(100101));
        if (YANFEN_SLOT_PLAYERS.contains(playerId)) {
            builder.setSkillElement(SKILL_ELEMENT_FIRE);
        }
        return builder.build();
    }

    private static AttributeVO attribute(int type, double value) {
        return AttributeVO.newBuilder()
                .setType(type)
                .setValue(value)
                .build();
    }

    private static ChangeSceneResp buildSceneResponse(
            int mapId,
            int mapChangeType) {

        VectorVo point = VectorVo.newBuilder()
                .setX(0)
                .setY(0)
                .setZ(-15)
                .build();

        return ChangeSceneResp.newBuilder()
                .setMapId(mapId)
                .setPoint(point)
                .setDir(0)
                .setMapChangeType(mapChangeType)
                .build();
    }

    /**
     * 一波怪物的实例：一批场景单位 ID 加上已经下发给客户端的那批快照。
     *
     * <p>官服抓包证明预加载出来的就是正式战斗要用的实例：idx 1974 客户端预加载
     * 第二波（10200102），官服在 idx 1976-1978 下发三只怪；等玩家真的走到第二波
     * 时（idx 2183）官服只回一条 50757 遗忘上一波的尸体，<b>不再下发 50756</b>，
     * 之后战斗里出现的 ID 正是预加载那三个。所以预加载、正式进入、击杀、遗忘
     * 必须共用同一个实例，不能各生成一遍。
     */
    private static final class WaveInstance {

        private final int chapterId;
        private final long firstUnitId;
        /** 生成这批怪时客户端上报的战斗点 z；换点了就不能再复用这批实例。 */
        private final float spawnZ;
        private final List<SceneUpdateVisibleResp> snapshots;

        private WaveInstance(
                int chapterId,
                long firstUnitId,
                float spawnZ,
                List<SceneUpdateVisibleResp> snapshots) {
            this.chapterId = chapterId;
            this.firstUnitId = firstUnitId;
            this.spawnZ = spawnZ;
            this.snapshots = snapshots;
        }

        private long unitIdAt(int index) {
            return firstUnitId + index;
        }

        private int monsterCount() {
            return snapshots.size();
        }

        private boolean spawnedAt(float z) {
            return Math.abs(spawnZ - z) < 0.01f;
        }
    }

    private static final class GuidanceState {

        private final int chapterId;
        private final Set<Long> killedTemplates = new HashSet<>();
        private final Set<Long> killedUnitIds = new HashSet<>();
        private boolean autoSkillOpened;
        private boolean autoSkillTaskFollowUpOpened;
        private boolean fightResultSent;
        private boolean bossForgotten;
        private boolean ninthTeachingSkillGranted;
        private long lastKilledMonsterId;
        private int battleLogKills;
        private final Map<Long, Integer> battleLogTemplateKills = new java.util.HashMap<>();

        private GuidanceState(int chapterId) {
            this.chapterId = chapterId;
        }

        private int recordTemplate(long templateId) {
            if (!killedTemplates.add(templateId)) {
                return -1;
            }
            return killedTemplates.size();
        }

        private int recordBattleLogKill(long templateId) {
            killedTemplates.add(templateId);
            return ++battleLogKills;
        }

        private boolean recordUnit(long sceneUnitId) {
            return killedUnitIds.add(sceneUnitId);
        }
    }

    private static final class LotteryItemStack {
        private final int itemIndex;
        private final int key;
        private int size;
        private final long objectId;
        private final long createTime;
        private long lastGainTime;

        private LotteryItemStack(
                int itemIndex,
                int key,
                int size,
                long objectId,
                long now) {
            this.itemIndex = itemIndex;
            this.key = key;
            this.size = size;
            this.objectId = objectId;
            this.createTime = now;
            this.lastGainTime = now;
        }
    }

    private static final class SkillSchemaState {
        private int heroIndex;
        private long activeTimeSeconds;
        private final List<IntegerAndIntegerPairEntry> slots = new ArrayList<>();
    }
}
