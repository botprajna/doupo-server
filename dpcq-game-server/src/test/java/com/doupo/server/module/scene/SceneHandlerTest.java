package com.doupo.server.module.scene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.doupo.protocol.AlchemyNewMakeCostItemNumUpdateResp;
import com.doupo.protocol.AlchemyNewMakeReq;
import com.doupo.protocol.AlchemyNewMakeResp;
import com.doupo.protocol.AlchemyNewMergeResp;
import com.doupo.protocol.AlchemyNewExpChangeResp;
import com.doupo.protocol.AlchemyNewLevelUpgradeResp;
import com.doupo.protocol.HeroLevelUpgradeReq;
import com.doupo.protocol.HeroRoadRewardResp;
import com.doupo.protocol.AlchemyNewTakingReq;
import com.doupo.protocol.AlchemyNewTakingResp;
import com.doupo.protocol.AttributeActionVO;
import com.doupo.protocol.CrisisEventFreeRewardReq;
import com.doupo.protocol.CrisisEventFreeRewardResp;
import com.doupo.protocol.CrisisEventInfoResp;
import com.doupo.protocol.CrisisEventRewardReq;
import com.doupo.protocol.CrisisEventUpdateResp;
import com.doupo.protocol.HeroFightForceResp;
import com.doupo.protocol.HeroLevelBreakReq;
import com.doupo.protocol.HeroLevelBreakResp;
import com.doupo.protocol.HeroLevelUpgradeResp;
import com.doupo.protocol.HeroSkillActResp;
import com.doupo.protocol.HeroStatUpdateResp;
import com.doupo.protocol.MainEquipBagAddResp;
import com.doupo.protocol.MainMapChapterInfoResp;
import com.doupo.protocol.MainMapPassChapterUpdateResp;
import com.doupo.protocol.MainMapKillMonsterReq;
import com.doupo.protocol.MainMapStartFightReq;
import com.doupo.protocol.MainMapStartFightResp;
import com.doupo.protocol.MainMapTakeHangUpRewardReq;
import com.doupo.protocol.MedicineCauldronHistoryConsumeReq;
import com.doupo.protocol.MedicineCauldronHistoryConsumeResp;
import com.doupo.protocol.MedicineCauldronInfoResp;
import com.doupo.protocol.LotteryDrawReq;
import com.doupo.protocol.LotteryDrawResp;
import com.doupo.protocol.LotteryInfoResp;
import com.doupo.protocol.LotteryPoolUpReq;
import com.doupo.protocol.LotteryPoolUpResp;
import com.doupo.protocol.LotteryType;
import com.doupo.protocol.ModuleNewOpenResp;
import com.doupo.protocol.PackUpdateResp;
import com.doupo.protocol.PlayerCumulateLoginDaysResp;
import com.doupo.protocol.PlayerFightForceCheckPointChangeResp;
import com.doupo.protocol.PlayerFightForceResp;
import com.doupo.protocol.PurseUpdateResp;
import com.doupo.protocol.ReputationInitResp;
import com.doupo.protocol.ReputationLvOnTaskRewardUpdateResp;
import com.doupo.protocol.ReputationLvUpReq;
import com.doupo.protocol.ReputationLvUpResp;
import com.doupo.protocol.RewardResp;
import com.doupo.protocol.RealFirstChargeDayRewardVo;
import com.doupo.protocol.RealFirstChargeInfoResp;
import com.doupo.protocol.RealFirstChargeUpdateResp;
import com.doupo.protocol.SceneUnitVo;
import com.doupo.protocol.SceneUpdateVisibleResp;
import com.doupo.protocol.SyncNonSceneHeroVoUpdateResp;
import com.doupo.protocol.GuidanceMainMapMonsterEnterReq;
import com.doupo.protocol.GuidanceMainMapUseSkillReq;
import com.doupo.protocol.GuidanceMainMapPauseReq;
import com.doupo.protocol.GuidanceOpenAutoSkillReq;
import com.doupo.protocol.HeroOriginNodeStatReq;
import com.doupo.protocol.HeroOriginNodeStatResp;
import com.doupo.protocol.HeroSkillSkillUpdateResp;
import com.doupo.protocol.HeroSkillStarTotalMaxHisUpdateResp;
import com.doupo.protocol.HeroSkillStarUpReq;
import com.doupo.protocol.HeroStepUpgradeReq;
import com.doupo.protocol.HeroStepUpgradeResp;
import com.doupo.protocol.GuidanceMainMapResetReq;
import com.doupo.protocol.HeroSkillSchemaUpdateReq;
import com.doupo.protocol.HeroShortInfoUpdateResp;
import com.doupo.protocol.ActiveSkillChangeResp;
import com.doupo.protocol.BattleLogVO;
import com.doupo.protocol.BattleLogEntryVO;
import com.doupo.protocol.BattleLogItemVO;
import com.doupo.protocol.BattleLogUpdateVisibleResp;
import com.doupo.protocol.BaoBuStartResp;
import com.doupo.protocol.HeroSkillSlotUpdateResp;
import com.doupo.protocol.ChangeSceneFinishBReq;
import com.doupo.protocol.TaskPhase;
import com.doupo.protocol.TaskRewardReq;
import com.doupo.protocol.TaskUniqueKey;
import com.doupo.protocol.TaskUpdateResp;
import com.doupo.protocol.DamageTypeVo;
import com.doupo.protocol.StoryReadFinishReq;
import com.doupo.protocol.StoryReadFinishResp;
import com.doupo.protocol.UseSkillResp;
import com.doupo.protocol.FightSkillUpdateResp;
import com.doupo.protocol.MpResp;
import com.doupo.protocol.ChaseStartResp;
import com.doupo.protocol.ChaseStopResp;
import com.doupo.protocol.SkillActionResp;
import com.doupo.protocol.FightResultResp;
import com.doupo.protocol.SceneForgetVisibleResp;
import com.doupo.server.module.combat.CombatSession;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import com.doupo.server.module.combat.CombatSessionRegistry;
import com.doupo.server.module.system.PlayerCreateHandler;
import com.doupo.protocol.PlayerCreateReq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gaming.fakecmd.side.game.IPlayerContext;
import org.junit.Test;

public class SceneHandlerTest {

    @Test
    public void blowPalmUsesThreeTimedAreaHitsAndCapturedCooldown() {
        long playerId = 100_000_002_951L;
        TimedSceneHandler handler = new TimedSceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(context, GuidanceMainMapMonsterEnterReq.newBuilder()
                .setChapterId(10200501).build());
        CombatSession session = CombatSessionRegistry.get(playerId);
        // Equip after entering: the live skill must use the newly announced attack.
        handler.updateHeroSkillSchema(context, HeroSkillSchemaUpdateReq.newBuilder()
                .setHeroIndex(0).addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                        .newBuilder().setKey(1001).setValue(80130001)).build());
        int index = 0;
        for (com.doupo.server.module.combat.CombatUnit monster : session.getMonsters()) {
            monster.moveTo(++index, 0, 1);
        }
        session.addMp(100);
        context.writes.clear();
        long targetId = handler.waveUnitIdAt(playerId, 0);
        handler.useGuidanceMainMapSkill(context, GuidanceMainMapUseSkillReq.newBuilder()
                .setSkillId(10150210100L).setTargetId(targetId).setTime(10000)
                .setTx(1).setTz(1).build());
        UseSkillResp ack = (UseSkillResp) context.writes.stream()
                .filter(w -> w.protocolId == 50762).findFirst().get().message;
        assertEquals(0, ack.getTargetX(), 0);
        assertEquals(0, ack.getTargetZ(), 0);
        FightSkillUpdateResp cd = (FightSkillUpdateResp) context.writes.stream()
                .filter(w -> w.protocolId == 50766).findFirst().get().message;
        assertEquals(5000, cd.getCostCd());
        assertEquals(15000, cd.getCd());
        assertEquals(100, cd.getCostMp());
        assertTrue(cd.getActiveSkill());
        assertEquals(0, session.getMp());
        assertEquals(0, session.getPlayer().getX(), 0);
        assertEquals(0, session.getPlayer().getZ(), 0);
        long[] times = {433, 767, 1167};
        long[] actions = {10150210101L, 10150210104L, 10150210107L};
        for (int stage = 0; stage < times.length; stage++) {
            handler.advanceTo(times[stage] - 1);
            assertEquals(stage, context.writes.stream().filter(w -> w.protocolId == 50763).count());
            handler.advanceTo(times[stage]);
            SkillActionResp hit = (SkillActionResp) context.writes.stream()
                    .filter(w -> w.protocolId == 50763).reduce((a, b) -> b).get().message;
            List<com.doupo.protocol.DamageVO> damages = new ArrayList<>();
            hit.getActionListList().forEach(a -> { if (a.hasDamageVo()) damages.add(a.getDamageVo()); });
            assertEquals(6, damages.size());
            for (com.doupo.protocol.DamageVO damage : damages) {
                assertEquals(actions[stage], damage.getActionId());
                assertEquals(stage, damage.getHitId());
                assertEquals(101 + stage, damage.getGroup());
                assertEquals(80130001, damage.getSkillBaseId());
                assertEquals(DamageTypeVo.FIGHT_SKILL, damage.getType());
                assertEquals(2000, damage.getDamage(), 0);
            }
        }
        handler.advanceTo(2333);
        assertTrue(context.writes.stream().anyMatch(w -> w.protocolId == 50763
                && ((SkillActionResp) w.message).getActionListCount() == 0));
        CombatSessionRegistry.clear(playerId);
    }

    @Test
    public void blowPalmLimitsRangeAndTargetsAndDoesNotHitReplacementSession() {
        long playerId = 100_000_002_952L;
        TimedSceneHandler handler = new TimedSceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(context, GuidanceMainMapMonsterEnterReq.newBuilder()
                .setChapterId(10200501).build());
        CombatSession session = CombatSessionRegistry.get(playerId);
        int index = 0;
        for (com.doupo.server.module.combat.CombatUnit monster : session.getMonsters()) {
            monster.moveTo(++index, 0, 0);
        }
        for (int i = 7; i <= 9; i++) {
            session.addMonster(new com.doupo.server.module.combat.CombatUnit(
                    playerId * 1000 + 900 + i, 1, 20000, i == 9 ? 10.01f : i, 0, 0));
        }
        context.writes.clear();
        handler.useGuidanceMainMapSkill(context, GuidanceMainMapUseSkillReq.newBuilder()
                .setSkillId(10150210100L).setTargetId(handler.waveUnitIdAt(playerId, 0)).build());
        handler.advanceTo(433);
        SkillActionResp hit = (SkillActionResp) context.writes.stream()
                .filter(w -> w.protocolId == 50763).findFirst().get().message;
        assertEquals(7, hit.getActionListList().stream().filter(a -> a.hasDamageVo()).count());
        assertEquals(20000, session.getMonster(playerId * 1000 + 908).getCurrentHp(), 0);
        assertEquals(20000, session.getMonster(playerId * 1000 + 909).getCurrentHp(), 0);
        // Only the distant monster remains: no unlimited-range fallback to the target.
        session.getMonsters().forEach(m -> { if (m.getX() <= 10) m.applyDamage(100000); });
        context.writes.clear();
        handler.advanceTo(767);
        assertFalse(context.writes.stream().anyMatch(w -> w.protocolId == 50763));
        CombatSessionRegistry.clear(playerId);
        handler.advanceTo(2333);
        assertTrue(context.writes.isEmpty());
    }

    @Test
    public void blowPalmKillProducesDeathAndSingleResultWithoutEarlyForget() {
        long playerId = 100_000_002_953L;
        TimedSceneHandler handler = new TimedSceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(context, GuidanceMainMapMonsterEnterReq.newBuilder()
                .setChapterId(10200501).build());
        CombatSession session = CombatSessionRegistry.get(playerId);
        session.getMonsters().forEach(m -> {
            m.moveTo(1, 0, 1);
            m.applyDamage(m.getCurrentHp() - 3000);
        });
        context.writes.clear();
        handler.useGuidanceMainMapSkill(context, GuidanceMainMapUseSkillReq.newBuilder()
                .setSkillId(10150210100L).setTargetId(handler.waveUnitIdAt(playerId, 0)).build());
        handler.advanceTo(433);
        assertFalse(context.writes.stream().anyMatch(w -> w.protocolId == 50781));
        handler.advanceTo(767);
        assertTrue(session.getMonsters().stream().noneMatch(m -> m.isAlive()));
        assertEquals(1, context.writes.stream().filter(w -> w.protocolId == 50781).count());
        assertFalse(context.writes.stream().anyMatch(w -> w.protocolId == 50757));
        SkillActionResp killingHit = (SkillActionResp) context.writes.stream()
                .filter(w -> w.protocolId == 50763).reduce((a, b) -> b).get().message;
        assertEquals(6, killingHit.getActionListList().stream().filter(a -> a.hasDieVo()).count());
        handler.advanceTo(1167);
        assertEquals(1, context.writes.stream().filter(w -> w.protocolId == 50781).count());
        // A new session must not receive the previous cast's delayed end/action.
        handler.enterGuidanceMainMapMonster(context, GuidanceMainMapMonsterEnterReq.newBuilder()
                .setChapterId(10200502).build());
        context.writes.clear();
        handler.advanceTo(2333);
        assertFalse(context.writes.stream().anyMatch(w -> w.protocolId == 50763));
        CombatSessionRegistry.clear(playerId);
    }

    @Test
    public void ninthGuideGrantsSkillWithoutRacingNormalAttackAndRemovesItAfterFight() {
        long playerId = 100_000_002_901L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(context, GuidanceMainMapMonsterEnterReq.newBuilder()
                .setChapterId(10200405).setZ(1299.3f).build());
        context.writes.clear();
        handler.savePlayerGuide(context, com.doupo.protocol.PlayerGuideSaveReq.newBuilder()
                .setGuideGroup(10043).setGuideId(1004303).build());
        assertTrue(context.writes.isEmpty());
        CombatSession session = CombatSessionRegistry.get(playerId);
        session.getPlayer().applyDamage(session.getPlayer().getMaxHp() - 1);
        NinthBossGuide.tryStart(context, session);
        context.writes.clear();
        com.doupo.protocol.PlayerGuideSaveReq guide = com.doupo.protocol.PlayerGuideSaveReq
                .newBuilder().setGuideGroup(10043).setGuideId(1004303).build();
        handler.savePlayerGuide(context, guide);
        assertEquals(1, context.writes.size());
        HeroSkillActResp skill = (HeroSkillActResp) context.writes.get(0).message;
        assertEquals(75011, context.writes.get(0).protocolId);
        assertEquals(101001, skill.getPlayerSkill().getBaseId());
        assertEquals(6, skill.getPlayerSkill().getHeroSkills(0).getHeroSkill().getStar());
        assertFalse(skill.getShowTips());
        handler.savePlayerGuide(context, guide);
        assertEquals(1, context.writes.size());
        handler.updateHeroSkillSchema(context, HeroSkillSchemaUpdateReq.newBuilder()
                .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry.newBuilder().setKey(2001).setValue(80128011))
                .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry.newBuilder().setKey(1001).setValue(101001))
                .build());
        session.setPaused(false);
        context.writes.clear();
        handler.useGuidanceMainMapSkill(context, GuidanceMainMapUseSkillReq.newBuilder()
                .setSkillId(10120110101L).setTargetId(session.getMonsters().iterator().next().getSceneUnitId())
                .setTime(10000).setZ(1296.8f).setTz(1299.3f).build());
        FightSkillUpdateResp cd = (FightSkillUpdateResp) context.writes.stream()
                .filter(w -> w.protocolId == 50766).findFirst().get().message;
        assertEquals(10120110102L, cd.getSkillId());
        assertEquals(8000, cd.getCostCd());
        assertEquals(100, cd.getCostMp());
        assertTrue(cd.getActiveSkill());
        assertTrue(context.writes.stream().anyMatch(w -> w.message instanceof HeroSkillSkillUpdateResp
                && ((HeroSkillSkillUpdateResp) w.message).getPlayerSkillUpdatesList().stream()
                .anyMatch(s -> s.getBaseId() == 101001 && s.getHeroSkillsCount() == 0)));
        com.doupo.protocol.HeroSkillSchemaUpdateResp restored = (com.doupo.protocol.HeroSkillSchemaUpdateResp)
                context.writes.stream().filter(w -> w.protocolId == 75005).reduce((a,b) -> b).get().message;
        assertTrue(restored.getSchema().getSlot2SkillBaseIdsList().stream()
                .anyMatch(s -> s.getKey() == 1001 && s.getValue() == 100001));
        assertTrue(restored.getSchema().getSlot2SkillBaseIdsList().stream()
                .anyMatch(s -> s.getKey() == 2001 && s.getValue() == 80128011));
        CombatSessionRegistry.clear(playerId);
    }

    @Test
    public void tenthBossSceneChangeContinuesWithWutanBattleLog() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(100_000_002_902L);
        for (int wave = 10200501; wave <= 10200503; wave++) {
            handler.enterGuidanceMainMapMonster(context, GuidanceMainMapMonsterEnterReq.newBuilder()
                    .setChapterId(wave).build());
            CombatSession session = CombatSessionRegistry.get(context.getId());
            assertEquals(6, session.getMonsters().size());
            for (com.doupo.server.module.combat.CombatUnit monster : session.getMonsters()) {
                handler.killGuidanceMonster(context, com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                        .setMonsterId(monster.getSceneUnitId()).build());
            }
            handler.endMainMapFight(context, com.doupo.protocol.MainMapEndFightReq.newBuilder()
                    .setMainMapChapterId(wave).build());
            MainMapPassChapterUpdateResp cleared = lastChapterUpdate(context);
            assertEquals(wave == 10200503 ? 10200501 : wave + 1, cleared.getMainMapChapterId());
        }
        assertEquals(10200505, lastChapterUpdate(context).getNextChallengeId());
        handler.resetGuidanceMainMap(context, GuidanceMainMapResetReq.newBuilder().setEnterNext(true).build());
        assertEquals(10200505, lastChapterUpdate(context).getMainMapChapterId());
        killGuidanceBoss(handler, context, 10200505, 605);
        handler.endMainMapFight(context, com.doupo.protocol.MainMapEndFightReq.newBuilder()
                .setMainMapChapterId(10200505).build());
        MainMapPassChapterUpdateResp next = lastChapterUpdate(context);
        assertEquals(10300101, next.getMainMapChapterId());
        assertEquals(10200505, next.getHistoryTopId());
        assertTrue(next.getResetState());
        com.doupo.protocol.ChangeSceneResp scene = handler.changeScene(context,
                com.doupo.protocol.ChangeSceneReq.newBuilder().setMapId(2).build());
        assertEquals(2, scene.getMapId());
        assertEquals(8, scene.getMapChangeType());
        assertEquals(-15, scene.getPoint().getZ(), 0);
        SceneUpdateVisibleResp loaded = handler.finishSceneLoadB(context,
                ChangeSceneFinishBReq.newBuilder().setMapId(2).build());
        assertFalse(loaded.getVisibleList(0).getHeroVo().getClientDriven());
        handler.resetGuidanceMainMap(context, GuidanceMainMapResetReq.getDefaultInstance());
        handler.startMainMapFight(context, MainMapStartFightReq.newBuilder()
                .setMainMapChapterId(10300101).build());
        MainMapStartFightResp battle = (MainMapStartFightResp) context.writes.stream()
                .filter(w -> w.protocolId == 61971).findFirst().get().message;
        assertTrue(battle.getEnd());
        assertFalse(battle.getBattleLog().getData().isEmpty());
        assertEquals(4, battle.getBattleLog().getFightStatisticsResp().getLoserCount());
    }

    @Test
    public void reinitializationResetsEconomyAndOneLiquidReachesFiveDuan() {
        SceneHandler handler = new SceneHandler();
        PlayerCreateHandler initializer = new PlayerCreateHandler(handler);
        RecordingPlayerContext context = new RecordingPlayerContext(901L);
        RecordingPlayerContext other = new RecordingPlayerContext(902L);
        makeAlchemyWithInventory(handler, other);
        rewardTask(handler, other, 200006);
        other.writes.clear();

        // 同一服务进程内重复进入第一关，不能继承上一轮的余额、等级或炼制次数。
        for (int run = 0; run < 2; run++) {
            rewardTask(handler, context, 200006);
            makeAlchemyWithInventory(handler, context);
            makeAlchemyWithInventory(handler, context);
            handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                    .addAlchemyId(0).addAlchemyId(1).addAlchemyId(2).addAlchemyId(3).build());
            handler.breakHeroLevel(context, HeroLevelBreakReq.getDefaultInstance());
            initializer.createPlayer(context, PlayerCreateReq.newBuilder().setName("test").build());
            context.writes.clear();

            killGuidanceBoss(handler, context, 10100105, 205);
            PackUpdateResp pack = (PackUpdateResp) context.writes.stream()
                    .filter(w -> w.protocolId == 50402).findFirst().get().message;
            assertEquals(100200, pack.getPacks(0).getUpdateItems(0).getPackItem().getKey());
            assertEquals(3, pack.getPacks(0).getUpdateItems(0).getPackItem().getSize());
            context.writes.clear();
            makeAlchemyWithInventory(handler, context);
            AlchemyNewMakeResp make = (AlchemyNewMakeResp) context.writes.get(1).message;
            assertEquals(1, make.getMakeTimes());
            assertEquals(0, make.getMakeIds(0).getId());
            assertEquals(4, make.getMakeIdsCount());
            context.writes.clear();
            handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                    .addAlchemyId(0).addAlchemyId(1).addAlchemyId(2).addAlchemyId(3).build());
            context.writes.clear();
            handler.breakHeroLevel(context, HeroLevelBreakReq.getDefaultInstance());
            // PlayerLevelConfig：level=3 才是“五段斗之气”，总修为1120-500-600=20。
            assertEquals(3, ((HeroLevelBreakResp) context.writes.get(0).message).getHeroVo().getLevel());
            assertEquals(20, ((AlchemyNewExpChangeResp) context.writes.get(1).message).getExp());
        }

        handler.takeAlchemy(other, AlchemyNewTakingReq.newBuilder().addAlchemyId(0).build());
        assertTaking(other, 280, 1, 0);
        other.writes.clear();
        handler.drawNewFightSkill(other, LotteryDrawReq.newBuilder()
                .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL).build());
        assertFalse(((LotteryDrawResp) other.writes.stream()
                .filter(w -> w.protocolId == 77352).findFirst().get().message).getFailure());
    }

    @Test
    public void thirdNormalAlchemyMakeStillProducesFourPills() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(901L);
        makeAlchemyWithInventory(handler, context);
        makeAlchemyWithInventory(handler, context);
        context.writes.clear();
        makeAlchemyWithInventory(handler, context);

        AlchemyNewMakeResp make = (AlchemyNewMakeResp) context.writes.stream()
                .filter(w -> w.protocolId == 75053).findFirst().get().message;
        // 抓包 idx 5357：普通炼制 source=0、makeTimes=3，4颗各280。
        assertEquals(3, make.getMakeTimes());
        assertEquals(0, make.getSource());
        assertEquals(4, make.getMakeIdsCount());
        for (int i = 0; i < 4; i++) {
            assertEquals(1101, make.getMakeIds(i).getRid());
            assertEquals(280, make.getMakeIds(i).getExp());
        }
    }

    @Test
    public void twoPlayersCanConsumeTheirOwnIdenticalAlchemyIds() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext first = new RecordingPlayerContext(901L);
        RecordingPlayerContext second = new RecordingPlayerContext(902L);
        makeAlchemyWithInventory(handler, first);
        makeAlchemyWithInventory(handler, second);
        first.writes.clear();
        second.writes.clear();
        AlchemyNewTakingReq take = AlchemyNewTakingReq.newBuilder().addAlchemyId(0).build();
        handler.takeAlchemy(first, take);
        handler.takeAlchemy(second, take);
        assertEquals(1, first.writes.size());
        assertEquals(1, second.writes.size());
        assertTaking(first, 280, 1, 0);
        assertTaking(second, 280, 1, 0);
    }

    /** 自动战斗引导必须按抓包 idx 1466-1469 完成任务 200009。 */
    @Test
    public void autoSkillGuideCompletesTask200009() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100501)
                        .build());
        context.writes.clear();

        handler.openGuidanceAutoSkill(
                context,
                GuidanceOpenAutoSkillReq.getDefaultInstance());

        assertEquals(2, context.writes.size());
        assertEquals(60751, context.writes.get(0).protocolId);
        assertEquals(50906, context.writes.get(1).protocolId);
        assertTask((TaskUpdateResp) context.writes.get(1).message,
                0, 200009, TaskPhase.FINISHED, 1);
    }

    /** 自动战斗任务领奖必须按抓包 idx 1505-1512 解锁斗技模块。 */
    @Test
    public void autoSkillTaskRewardMatchesCapturedResponses() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200009))
                        .build());

        int[] protocolIds = {
                50402, 50406, 53702, 50852, 50384, 76701, 50906
        };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        RewardResp reward = (RewardResp) context.writes.get(1).message;
        assertEquals(50907, reward.getOperationType());
        assertEquals(101, reward.getRewardItemVos(0).getItemKey());
        assertEquals(1, reward.getRewardItemVos(0).getAmount());

        ModuleNewOpenResp modules =
                (ModuleNewOpenResp) context.writes.get(3).message;
        assertEquals(4204, modules.getOpens(0));

        CrisisEventInfoResp crisis =
                (CrisisEventInfoResp) context.writes.get(5).message;
        assertEquals(20010, crisis.getEvents(0).getId());
        assertFalse(crisis.getEvents(0).getReward());
        assertTask((TaskUpdateResp) context.writes.get(6).message,
                0, 200009, TaskPhase.REWARDED, 1);
        assertTask((TaskUpdateResp) context.writes.get(6).message,
                1, 30201, TaskPhase.PROGRESS, 0);
        assertTask((TaskUpdateResp) context.writes.get(6).message,
                2, 30202, TaskPhase.PROGRESS, 0);
    }

    /** 家族测试右下角免费宝箱：协议来自客户端，奖励来自 crisisfreerewardconfig。 */
    @Test
    public void crisisEventFreeRewardGrantsConfiguredGoldOnlyOnce() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);
        CrisisEventFreeRewardReq request =
                CrisisEventFreeRewardReq.newBuilder()
                        .setOpenDay(1)
                        .build();

        handler.claimCrisisEventFreeReward(context, request);

        assertEquals(3, context.writes.size());
        assertEquals(50651, context.writes.get(0).protocolId);
        PurseUpdateResp purse =
                (PurseUpdateResp) context.writes.get(0).message;
        assertEquals(2, purse.getItems(0).getType());
        assertEquals(35, purse.getItems(0).getValue());

        assertEquals(50406, context.writes.get(1).protocolId);
        RewardResp reward = (RewardResp) context.writes.get(1).message;
        assertEquals(76702, reward.getOperationType());
        assertEquals(2, reward.getRewardItemVos(0).getItemKey());
        assertEquals(10, reward.getRewardItemVos(0).getAmount());

        assertEquals(76706, context.writes.get(2).protocolId);
        CrisisEventFreeRewardResp response =
                (CrisisEventFreeRewardResp) context.writes.get(2).message;
        assertEquals(1, response.getOpenDay());

        context.writes.clear();
        handler.claimCrisisEventFreeReward(context, request);

        assertEquals(1, context.writes.size());
        assertEquals(76706, context.writes.get(0).protocolId);
        assertEquals(1,
                ((CrisisEventFreeRewardResp) context.writes.get(0).message)
                        .getOpenDay());
    }

    @Test
    public void crisisEventFreeRewardRejectsDayOutsideConfig() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.claimCrisisEventFreeReward(
                context,
                CrisisEventFreeRewardReq.newBuilder()
                        .setOpenDay(15)
                        .build());

        assertTrue(context.writes.isEmpty());
    }

    /** 领取后首次施放 10150510100 时开启下一任务 200010（抓包 idx 1515-1516）。 */
    @Test
    public void autoSkillRewardFollowUpStartsTask200010() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100501)
                        .build());
        handler.openGuidanceAutoSkill(
                context,
                GuidanceOpenAutoSkillReq.getDefaultInstance());
        context.writes.clear();

        handler.useGuidanceSkill(
                context,
                com.doupo.protocol.GuidanceUseSkillReq.newBuilder()
                        .setSkillId(10150510100L)
                        .build());

        assertEquals(1, context.writes.size());
        assertEquals(50906, context.writes.get(0).protocolId);
        assertTask((TaskUpdateResp) context.writes.get(0).message,
                0, 200010, TaskPhase.PROGRESS, 0);
    }

    /** 跳去家族试炼后先升到六段，也必须先接取再完成任务 200010。 */
    @Test
    public void earlyLevelFourBreakOpensThenCompletesTask200010() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_002L);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100501)
                        .build());
        handler.openGuidanceAutoSkill(
                context,
                GuidanceOpenAutoSkillReq.getDefaultInstance());

        makeAlchemyWithInventory(handler, context);
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(0)
                .addAlchemyId(1)
                .addAlchemyId(2)
                .addAlchemyId(3)
                .build());
        handler.breakHeroLevel(context, HeroLevelBreakReq.newBuilder()
                .setHeroIndex(0)
                .build());

        makeAlchemyWithInventory(handler, context);
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(4)
                .addAlchemyId(5)
                .addAlchemyId(6)
                .addAlchemyId(7)
                .build());
        context.writes.clear();
        handler.breakHeroLevel(context, HeroLevelBreakReq.newBuilder()
                .setHeroIndex(0)
                .build());

        assertEquals(15, context.writes.size());
        assertEquals(50906, context.writes.get(3).protocolId);
        assertTask((TaskUpdateResp) context.writes.get(3).message,
                0, 200010, TaskPhase.PROGRESS, 0);
        assertEquals(50906, context.writes.get(7).protocolId);
        assertTask((TaskUpdateResp) context.writes.get(7).message,
                1, 200010, TaskPhase.FINISHED, 1);

        context.writes.clear();
        handler.useGuidanceSkill(
                context,
                com.doupo.protocol.GuidanceUseSkillReq.newBuilder()
                        .setSkillId(10150510100L)
                        .build());
        assertEquals(0, context.writes.size());
        CombatSessionRegistry.clear(context.getId());
    }

    /** 斗技升星必须按抓包 idx 1422-1431 刷新数值与战力。 */
    @Test
    public void heroSkillStarUpMatchesCapturedStats() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_001_001L);

        handler.starUpHeroSkill(
                context,
                HeroSkillStarUpReq.newBuilder()
                        .setHeroIndex(0)
                        .setSkillBaseId(80130011)
                        .addItem2CostNums(
                                com.doupo.protocol.IntegerAndLongPairEntry
                                        .newBuilder()
                                        .setKey(80130010)
                                        .setValue(1))
                        .build());

        int[] protocolIds = {
                50402, 75046, 75049, 50455, 50801, 50801,
                52351, 75151, 50906
        };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        HeroSkillSkillUpdateResp skillUpdate =
                (HeroSkillSkillUpdateResp) context.writes.get(1).message;
        assertEquals(2, skillUpdate.getPlayerSkillUpdates(0)
                .getHeroSkills(0).getHeroSkill().getStar());

        HeroStatUpdateResp stats =
                (HeroStatUpdateResp) context.writes.get(3).message;
        assertEquals(24311.999999999996,
                stats.getHeroVo().getStats(4).getValue(), 0.0001);
        assertEquals(920.7605,
                stats.getHeroVo().getStats(5).getValue(), 0.0001);

        PlayerFightForceResp force =
                (PlayerFightForceResp) context.writes.get(6).message;
        assertEquals(15202, force.getPlayerFightForce(), 0.0001);
        assertTask((TaskUpdateResp) context.writes.get(8).message,
                0, 4002009, TaskPhase.PROGRESS, 15202);

        SceneUnitVo player = SceneHandler.visibleSnapshot(context.getId())
                .getVisibleList(0);
        assertEquals(920.7605, attributeOf(player, 101001), 0.0001);
        assertEquals(24311.999999999996,
                attributeOf(player, 103001), 0.0001);
    }

    /** 狂狮怒罡升星必须逐条匹配抓包 idx 1766-1776。 */
    @Test
    public void firstHeroSkillStarUpMatchesCapturedStats() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_001_002L);

        HeroSkillStarUpReq request = HeroSkillStarUpReq.newBuilder()
                .setHeroIndex(0)
                .setSkillBaseId(80140001)
                .addItem2CostNums(
                        com.doupo.protocol.IntegerAndLongPairEntry
                                .newBuilder()
                                .setKey(80140000)
                                .setValue(1))
                .build();
        handler.starUpHeroSkill(context, request);

        int[] protocolIds = {
                50402, 75046, 75049, 50455, 50801, 50801,
                52351, 75151, 50906
        };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        PackUpdateResp pack =
                (PackUpdateResp) context.writes.get(0).message;
        assertEquals(9, pack.getPacks(0).getUpdateItems(0).getItemIndex());
        assertFalse(pack.getPacks(0).getUpdateItems(0).hasPackItem());

        HeroSkillSkillUpdateResp skillUpdate =
                (HeroSkillSkillUpdateResp) context.writes.get(1).message;
        assertEquals(80140001, skillUpdate.getPlayerSkillUpdates(0)
                .getHeroSkills(0).getHeroSkill().getBaseId());
        assertEquals(2, skillUpdate.getPlayerSkillUpdates(0)
                .getHeroSkills(0).getHeroSkill().getStar());

        HeroSkillStarTotalMaxHisUpdateResp total =
                (HeroSkillStarTotalMaxHisUpdateResp)
                        context.writes.get(2).message;
        assertEquals(5, total.getSkillStarTotalMaxHis());

        HeroStatUpdateResp stats =
                (HeroStatUpdateResp) context.writes.get(3).message;
        assertEquals(29853.85,
                stats.getHeroVo().getStats(4).getValue(), 0.0001);
        assertEquals(1133.54076,
                stats.getHeroVo().getStats(5).getValue(), 0.0001);

        PlayerFightForceResp force =
                (PlayerFightForceResp) context.writes.get(6).message;
        assertEquals(17424, force.getPlayerFightForce(), 0.0001);

        TaskUpdateResp tasks =
                (TaskUpdateResp) context.writes.get(8).message;
        assertEquals(8, tasks.getTaskVosCount());
        assertTask(tasks, 0, 4002009, TaskPhase.PROGRESS, 17424);
        assertTask(tasks, 7, 4002005, TaskPhase.PROGRESS, 17424);

        SceneUnitVo player = SceneHandler.visibleSnapshot(context.getId())
                .getVisibleList(0);
        assertEquals(1133.54076, attributeOf(player, 101001), 0.0001);
        assertEquals(401.5, attributeOf(player, 102001), 0.0001);
        assertEquals(29853.85, attributeOf(player, 103001), 0.0001);

        context.writes.clear();
        handler.starUpHeroSkill(context, request);
        assertEquals(0, context.writes.size());
    }

    /**
     * 第五抽激活 80130101 时碎片已消耗；客户端立刻带同一 cost 升星，
     * 对应抓包 idx 6957。战斗中空 cost 的 75008 必须忽略。
     */
    @Test
    public void lotterySkillStarUpUsesActivationCredit() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_001_010L);
        LotteryDrawReq draw = LotteryDrawReq.newBuilder()
                .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                .setTen(false)
                .build();
        prepareFifthDrawEnergy(handler, context, draw);
        handler.drawNewFightSkill(context, draw);
        context.writes.clear();

        handler.starUpHeroSkill(
                context,
                HeroSkillStarUpReq.newBuilder()
                        .setHeroIndex(0)
                        .setSkillBaseId(80130101)
                        .addItem2CostNums(
                                com.doupo.protocol.IntegerAndLongPairEntry
                                        .newBuilder()
                                        .setKey(80130100)
                                        .setValue(1))
                        .build());

        HeroSkillSkillUpdateResp skillUpdate = null;
        HeroSkillStarTotalMaxHisUpdateResp total = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 75046) {
                skillUpdate = (HeroSkillSkillUpdateResp) write.message;
            }
            if (write.protocolId == 75049) {
                total = (HeroSkillStarTotalMaxHisUpdateResp) write.message;
            }
        }
        assertTrue(skillUpdate != null);
        assertEquals(80130101, skillUpdate.getPlayerSkillUpdates(0)
                .getHeroSkills(0).getHeroSkill().getBaseId());
        assertEquals(2, skillUpdate.getPlayerSkillUpdates(0)
                .getHeroSkills(0).getHeroSkill().getStar());
        assertTrue(total != null);
        assertEquals(12, total.getSkillStarTotalMaxHis());

        context.writes.clear();
        handler.starUpHeroSkill(
                context,
                HeroSkillStarUpReq.newBuilder()
                        .setHeroIndex(0)
                        .setSkillBaseId(80130101)
                        .build());
        assertEquals(0, context.writes.size());

        handler.starUpHeroSkill(
                context,
                HeroSkillStarUpReq.newBuilder()
                        .setHeroIndex(0)
                        .setSkillBaseId(80130101)
                        .addItem2CostNums(
                                com.doupo.protocol.IntegerAndLongPairEntry
                                        .newBuilder()
                                        .setKey(80130100)
                                        .setValue(1))
                        .build());
        assertEquals(0, context.writes.size());
        CombatSessionRegistry.clear(context.getId());
    }
    @Test
    public void stackedLotteryFragmentStarsActivatedSkill() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_001_011L);
        LotteryDrawReq draw = LotteryDrawReq.newBuilder()
                .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                .setTen(false)
                .build();
        prepareFifthDrawEnergy(handler, context, draw);
        handler.drawNewFightSkill(context, draw);
        handler.upgradeLotteryPool(
                context,
                LotteryPoolUpReq.newBuilder()
                        .setLotteryType(
                                LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                        .build());
        rewardTask(handler, context, 200011);
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100505)
                        .build());
        handler.killGuidanceMonster(
                context,
                com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                        .setMonsterId(100_000_001_011L * 1000 + 505)
                        .build());
        handler.drawNewFightSkill(context, draw);
        context.writes.clear();

        handler.starUpHeroSkill(
                context,
                HeroSkillStarUpReq.newBuilder()
                        .setHeroIndex(0)
                        .setSkillBaseId(80128011)
                        .addItem2CostNums(
                                com.doupo.protocol.IntegerAndLongPairEntry
                                        .newBuilder()
                                        .setKey(80128010)
                                        .setValue(1))
                        .build());

        HeroSkillSkillUpdateResp skillUpdate = null;
        PackUpdateResp pack = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 75046) {
                skillUpdate = (HeroSkillSkillUpdateResp) write.message;
            }
            if (write.protocolId == 50402) {
                pack = (PackUpdateResp) write.message;
            }
        }
        assertTrue(pack != null);
        assertFalse(pack.getPacks(0).getUpdateItems(0).hasPackItem());
        assertTrue(skillUpdate != null);
        assertEquals(80128011, skillUpdate.getPlayerSkillUpdates(0)
                .getHeroSkills(0).getHeroSkill().getBaseId());
        assertEquals(3, skillUpdate.getPlayerSkillUpdates(0)
                .getHeroSkills(0).getHeroSkill().getStar());
        CombatSessionRegistry.clear(context.getId());
    }

    /** 首次服用灵液必须逐条匹配抓包 idx 607/610-612。 */
    @Test
    public void firstAlchemyMakeMatchesCapturedResponses() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        makeAlchemyWithInventory(handler, context);

        assertEquals(3, context.writes.size());
        assertEquals(50402, context.writes.get(0).protocolId);
        assertEquals(75053, context.writes.get(1).protocolId);
        assertEquals(75088, context.writes.get(2).protocolId);

        PackUpdateResp pack =
                (PackUpdateResp) context.writes.get(0).message;
        assertEquals(75052, pack.getOperationType());
        assertEquals(3,
                pack.getPacks(0).getUpdateItems(0).getItemIndex());
        assertFalse(pack.getPacks(0)
                .getUpdateItems(0).hasPackItem());

        AlchemyNewMakeResp make =
                (AlchemyNewMakeResp) context.writes.get(1).message;
        assertEquals(4, make.getMakeIdsCount());
        for (int i = 0; i < 4; i++) {
            assertEquals(i, make.getMakeIds(i).getId());
            assertEquals(1101, make.getMakeIds(i).getRid());
            assertEquals(280, make.getMakeIds(i).getExp());
            assertEquals(4, make.getMakeIds(i).getQlt());
            assertEquals(1, make.getMakeIds(i).getNum());
            assertEquals(0, make.getMakeIds(i).getRate());
        }
        assertEquals(1, make.getMakeTimes());
        assertEquals(0, make.getSource());

        AlchemyNewMakeCostItemNumUpdateResp cost =
                (AlchemyNewMakeCostItemNumUpdateResp)
                        context.writes.get(2).message;
        assertEquals(101, cost.getCostItem2Nums(0).getKey());
        assertEquals(1, cost.getCostItem2Nums(0).getValue());
    }

    /** 未服用首批药丸时再次炼制，必须像官服一样合并四个药槽。 */
    @Test
    public void secondAlchemyMakeMergesExistingPills() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        makeAlchemyWithInventory(handler, context);
        context.writes.clear();
        makeAlchemyWithInventory(handler, context);

        PackUpdateResp pack =
                (PackUpdateResp) context.writes.get(0).message;
        assertEquals(7,
                pack.getPacks(0).getUpdateItems(0).getItemIndex());
        assertFalse(pack.getPacks(0)
                .getUpdateItems(0).hasPackItem());

        AlchemyNewMakeResp make =
                (AlchemyNewMakeResp) context.writes.get(1).message;
        assertEquals(4, make.getMakeIdsCount());
        for (int i = 0; i < 4; i++) {
            assertEquals(4 + i, make.getMakeIds(i).getId());
        }
        assertEquals(2, make.getMakeTimes());

        AlchemyNewMergeResp merge =
                (AlchemyNewMergeResp) context.writes.get(2).message;
        assertEquals(4, merge.getMergesCount());
        assertEquals(560, merge.getMerges(0).getMake().getExp());
        assertEquals(2, merge.getMerges(0).getMake().getNum());

        AlchemyNewMakeCostItemNumUpdateResp cost =
                (AlchemyNewMakeCostItemNumUpdateResp)
                        context.writes.get(3).message;
        assertEquals(2, cost.getCostItem2Nums(0).getValue());
    }

    /** 首次四颗炼制物的服用顺序必须匹配抓包 idx 630-645。 */
    @Test
    public void tutorialAlchemyTakingMatchesCapturedResponses() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        makeAlchemyWithInventory(handler, context);
        context.writes.clear();
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(0)
                .build());
        assertTaking(context, 280, 1, 0);

        context.writes.clear();
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(1)
                .build());
        assertEquals(3, context.writes.size());
        assertTaking(context, 560, 2, 1);
        assertEquals(75057, context.writes.get(1).protocolId);
        HeroLevelUpgradeResp upgrade =
                (HeroLevelUpgradeResp) context.writes.get(1).message;
        assertEquals(0, upgrade.getHeroVo().getHeroIndex());
        assertEquals(2, upgrade.getHeroVo().getLevel());
        assertEquals(1, upgrade.getHeroVo().getStage());
        assertEquals(75060, context.writes.get(2).protocolId);
        AlchemyNewExpChangeResp exp =
                (AlchemyNewExpChangeResp) context.writes.get(2).message;
        assertEquals(60, exp.getExp());
        assertEquals(1, exp.getLevel());

        context.writes.clear();
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(2)
                .addAlchemyId(3)
                .build());
        assertTaking(context, 620, 4, 2, 3);
    }

    /** 客户端合并后三颗时，仍须复现抓包 idx 637-645 的两段结算。 */
    @Test
    public void batchedAlchemyTakingPreservesCapturedUpgradeSequence() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        makeAlchemyWithInventory(handler, context);
        context.writes.clear();
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(0)
                .build());
        context.writes.clear();
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(1)
                .addAlchemyId(2)
                .addAlchemyId(3)
                .build());

        assertEquals(4, context.writes.size());
        assertEquals(75055, context.writes.get(0).protocolId);
        AlchemyNewTakingResp beforeUpgrade =
                (AlchemyNewTakingResp) context.writes.get(0).message;
        assertEquals(560, beforeUpgrade.getExp());
        assertEquals(1, beforeUpgrade.getAlchemyVoCount());
        assertEquals(1, beforeUpgrade.getAlchemyVo(0));
        assertEquals(2, beforeUpgrade.getTakingInfo(0).getValue());
        assertEquals(75057, context.writes.get(1).protocolId);
        assertEquals(75060, context.writes.get(2).protocolId);
        assertEquals(75055, context.writes.get(3).protocolId);
        AlchemyNewTakingResp afterUpgrade =
                (AlchemyNewTakingResp) context.writes.get(3).message;
        assertEquals(620, afterUpgrade.getExp());
        assertEquals(2, afterUpgrade.getAlchemyVoCount());
        assertEquals(2, afterUpgrade.getAlchemyVo(0));
        assertEquals(3, afterUpgrade.getAlchemyVo(1));
        assertEquals(4, afterUpgrade.getTakingInfo(0).getValue());
    }

    /** 2级升阶必须匹配抓包 idx 674-689 中与升阶相关的响应。 */
    @Test
    public void heroLevelBreakMatchesCapturedResponseSequence() {
        long playerId = 100_100_000_001L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(playerId);

        makeAlchemyWithInventory(handler, context);
        context.writes.clear();
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(0)
                .addAlchemyId(1)
                .addAlchemyId(2)
                .addAlchemyId(3)
                .build());
        context.writes.clear();
        handler.breakHeroLevel(context, HeroLevelBreakReq.newBuilder()
                .setHeroIndex(0)
                .build());

        int[] protocolIds = {
                75067, 75060, 75153, 78602, 60751, 60751, 50906,
                50455, 50801, 50801, 52351, 75151, 52355, 50906
        };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        HeroLevelBreakResp level =
                (HeroLevelBreakResp) context.writes.get(0).message;
        assertEquals(0, level.getHeroVo().getHeroIndex());
        assertEquals(3, level.getHeroVo().getLevel());
        assertEquals(1, level.getHeroVo().getStage());
        AlchemyNewExpChangeResp exp =
                (AlchemyNewExpChangeResp) context.writes.get(1).message;
        assertEquals(20, exp.getExp());
        assertEquals(1, exp.getLevel());
        HeroShortInfoUpdateResp shortInfo =
                (HeroShortInfoUpdateResp) context.writes.get(2).message;
        assertEquals(3,
                shortInfo.getShortInfoVo()
                        .getCrossHeroShortInfo().getLevel());

        TaskUpdateResp mainTask =
                (TaskUpdateResp) context.writes.get(6).message;
        assertEquals(200005,
                mainTask.getTaskVos(0).getTaskId().getTaskResourceId());
        assertEquals(TaskPhase.FINISHED,
                mainTask.getTaskVos(0).getTaskPhase());

        HeroStatUpdateResp stats =
                (HeroStatUpdateResp) context.writes.get(7).message;
        assertEquals(255, stats.getHeroVo().getStats(0).getValue(), 0);
        AttributeActionVO hp =
                (AttributeActionVO) context.writes.get(8).message;
        assertEquals(playerId * 1000 + 1, hp.getTargetId());
        PlayerFightForceResp playerForce =
                (PlayerFightForceResp) context.writes.get(10).message;
        assertEquals(14014, playerForce.getPlayerFightForce(), 0);
        HeroFightForceResp heroForce =
                (HeroFightForceResp) context.writes.get(11).message;
        assertEquals(14014,
                heroForce.getHeroVoList(0).getFightForce(), 0);
        PlayerFightForceCheckPointChangeResp checkpoint =
                (PlayerFightForceCheckPointChangeResp)
                        context.writes.get(12).message;
        assertEquals(2, checkpoint.getCheckPointType());
        assertEquals(12579, checkpoint.getPlayerFightForceBefore(), 0);
        assertEquals(14014, checkpoint.getPlayerFightForceAfter(), 0);
        assertEquals("def", checkpoint.getPlayerStatMapBefore(0).getKey());

        TaskUpdateResp fightTasks =
                (TaskUpdateResp) context.writes.get(13).message;
        assertEquals(8, fightTasks.getTaskVosCount());
        assertEquals(4002009,
                fightTasks.getTaskVos(0).getTaskId().getTaskResourceId());
        assertEquals(14014,
                fightTasks.getTaskVos(0).getProgressValues(0));
    }

    /** 二段突破后，下一次突破必须按抓包推进为四段，不能回写三段。 */
    @Test
    public void secondHeroLevelBreakAdvancesToFourAsCaptured() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_002L);

        makeAlchemyWithInventory(handler, context);
        context.writes.clear();
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(0)
                .addAlchemyId(1)
                .addAlchemyId(2)
                .addAlchemyId(3)
                .build());
        context.writes.clear();
        handler.breakHeroLevel(context, HeroLevelBreakReq.newBuilder()
                .setHeroIndex(0)
                .build());

        context.writes.clear();
        makeAlchemyWithInventory(handler, context);
        context.writes.clear();
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(4)
                .addAlchemyId(5)
                .addAlchemyId(6)
                .addAlchemyId(7)
                .build());
        context.writes.clear();
        handler.breakHeroLevel(context, HeroLevelBreakReq.newBuilder()
                .setHeroIndex(0)
                .build());

        int[] protocolIds = {
                75067, 75060, 75153, 78602, 60751, 60751, 50906,
                50455, 50801, 50801, 52351, 75151, 52355, 50906
        };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }
        assertEquals(75067, context.writes.get(0).protocolId);
        HeroLevelBreakResp level =
                (HeroLevelBreakResp) context.writes.get(0).message;
        assertEquals(4, level.getHeroVo().getLevel());
        assertEquals(1, level.getHeroVo().getStage());
        assertEquals(75060, context.writes.get(1).protocolId);
        AlchemyNewExpChangeResp exp =
                (AlchemyNewExpChangeResp) context.writes.get(1).message;
        assertEquals(440, exp.getExp());
        HeroShortInfoUpdateResp shortInfo =
                (HeroShortInfoUpdateResp) context.writes.get(2).message;
        assertEquals(4, shortInfo.getShortInfoVo()
                .getCrossHeroShortInfo().getLevel());
        TaskUpdateResp mainTask =
                (TaskUpdateResp) context.writes.get(6).message;
        assertTask(mainTask, 0, 40001, TaskPhase.PROGRESS, 4);
        assertTask(mainTask, 1, 200010, TaskPhase.FINISHED, 1);
        PlayerFightForceResp force =
                (PlayerFightForceResp) context.writes.get(10).message;
        assertEquals(16670, force.getPlayerFightForce(), 0.0001);
    }

    /** 升到八段并溢出到九段时，按抓包 idx 6712-6734 解锁模块、完成 200021 并刷新战力。 */
    @Test
    public void seventhHeroLevelCompletesFamilyTrialTask() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_004L);

        for (int round = 0; round < 3; round++) {
            makeAlchemyWithInventory(handler, context);
            handler.takeAlchemy(
                    context,
                    AlchemyNewTakingReq.newBuilder()
                            .addAlchemyId(round * 4L)
                            .addAlchemyId(round * 4L + 1)
                            .addAlchemyId(round * 4L + 2)
                            .addAlchemyId(round * 4L + 3)
                            .build());
            handler.breakHeroLevel(
                    context,
                    HeroLevelBreakReq.newBuilder().setHeroIndex(0).build());
        }
        // 再服用两轮普通丹药，修为充足后突破 level=6 并自动升到 level=7。
        for (int round = 3; round < 5; round++) {
            makeAlchemyWithInventory(handler, context);
            handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                    .addAlchemyId(round * 4L).addAlchemyId(round * 4L + 1)
                    .addAlchemyId(round * 4L + 2).addAlchemyId(round * 4L + 3).build());
        }
        context.writes.clear();

        handler.breakHeroLevel(
                context,
                HeroLevelBreakReq.newBuilder().setHeroIndex(0).build());

        ModuleNewOpenResp modules = null;
        TaskUpdateResp tasks = null;
        HeroStatUpdateResp stats = null;
        PlayerFightForceResp force = null;
        com.doupo.protocol.HeroSkillSlotUpdateResp thirdSlot = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 50852) {
                modules = (ModuleNewOpenResp) write.message;
            }
            if (write.protocolId == 50906) {
                TaskUpdateResp update = (TaskUpdateResp) write.message;
                if (update.getTaskVosCount() > 0
                        && update.getTaskVos(0).getTaskId()
                                .getTaskResourceId() == 200021) {
                    tasks = update;
                }
            }
            if (write.protocolId == 50455) {
                stats = (HeroStatUpdateResp) write.message;
            }
            if (write.protocolId == 52351) {
                force = (PlayerFightForceResp) write.message;
            }
            if (write.protocolId == 75015) {
                com.doupo.protocol.HeroSkillSlotUpdateResp slot =
                        (com.doupo.protocol.HeroSkillSlotUpdateResp) write.message;
                if (slot.getSlotsCount() > 0
                        && slot.getSlots(0).getId() == 3001) {
                    thirdSlot = slot;
                }
            }
        }
        assertTrue(modules != null);
        assertEquals(3701, modules.getOpens(0));
        assertEquals(4203, modules.getOpens(1));
        assertTrue(modules.getOpensList().contains(3101));
        assertTrue(modules.getOpensList().contains(3102));
        assertTrue(tasks != null);
        assertTask(tasks, 0, 200021, TaskPhase.FINISHED, 1);
        assertTask(tasks, 1, 40001, TaskPhase.PROGRESS, 7);
        assertTrue(stats != null);
        assertEquals(720.5, stats.getHeroVo().getStats(0).getValue(), 0.0001);
        assertTrue(force != null);
        assertEquals(25603, force.getPlayerFightForce(), 0.0001);
        assertTrue(thirdSlot != null);
        assertFalse(thirdSlot.getSlots(0).getAnyOnSkill());
        TaskUpdateResp powerTasks = lastTaskUpdate(context);
        assertTask(powerTasks, 0, 4002009, TaskPhase.PROGRESS, 25603);
        assertTask(powerTasks, 7, 4002005, TaskPhase.PROGRESS, 25603);
    }

    /**
     * 修为不够溢出时停在八段（level=6），主线 200021 也必须 FINISHED。
     */
    @Test
    public void eighthRealmBreakFinishesTask200021WithoutOverflow() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_021L);

        breakHeroToLevelFive(handler, context);
        makeAlchemyWithInventory(handler, context);
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(12)
                .addAlchemyId(13)
                .build());
        rewardTask(handler, context, 200020);
        assertTask(lastTaskUpdate(context), 0, 200021, TaskPhase.PROGRESS, 0);

        context.writes.clear();
        handler.breakHeroLevel(
                context,
                HeroLevelBreakReq.newBuilder().setHeroIndex(0).build());

        HeroLevelBreakResp level = null;
        boolean overflowed = false;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 75067) {
                level = (HeroLevelBreakResp) write.message;
            }
            if (write.protocolId == 75057) {
                overflowed = true;
            }
        }
        assertTrue(level != null);
        assertEquals(6, level.getHeroVo().getLevel());
        assertFalse(overflowed);
        assertTrue(containsTask(
                lastTaskUpdate(context), 200021, TaskPhase.FINISHED));
        assertThirdSkillSlotUnlocked(context);
        ModuleNewOpenResp bagModules = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 50852) {
                bagModules = (ModuleNewOpenResp) write.message;
            }
        }
        assertTrue(bagModules != null);
        assertEquals(java.util.Arrays.asList(3701, 3101, 3102),
                bagModules.getOpensList());
    }

    /**
     * 八段 NeedUpStage=0，现场只服药不点 75056/75066 时也要升到九段再冲一星。
     */
    @Test
    public void eighthRealmTakingAutoUpgradesThenStepsToOneStar() {
        SceneHandler handler = new SceneHandler();
        handler.setServerOpenDay(2); // 此用例连续用第六份普通灵液，按官服开服第二天后的条件。
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_312L);

        breakHeroToLevelFive(handler, context);
        makeAlchemyWithInventory(handler, context);
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(12)
                .addAlchemyId(13)
                .build());
        handler.breakHeroLevel(
                context,
                HeroLevelBreakReq.newBuilder().setHeroIndex(0).build());
        makeAlchemyWithInventory(handler, context);
        makeAlchemyWithInventory(handler, context);
        context.writes.clear();
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(16).addAlchemyId(17)
                .addAlchemyId(18).addAlchemyId(19)
                .addAlchemyId(20).addAlchemyId(21)
                .addAlchemyId(22).addAlchemyId(23)
                .build());
        HeroStepUpgradeResp step = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 75059) {
                step = (HeroStepUpgradeResp) write.message;
            }
        }
        assertTrue(step != null);
        assertEquals(8, step.getHeroVo().getLevel());
        assertEquals(2, step.getHeroVo().getStage());
    }

    /**
     * 点锁定的第三格必须回 75003。重启后内存等级可能仍是 2，客户端已是八段。
     */
    @Test
    public void tappingLockedThirdSkillSlotUnlocks3001() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_023L);

        handler.unlockHeroSkillSlot(
                context,
                com.doupo.protocol.HeroSkillSlotUnlockReq.newBuilder()
                        .setHeroIndex(0)
                        .setSlotId(3001)
                        .build());

        assertEquals(1, context.writes.size());
        assertEquals(75003, context.writes.get(0).protocolId);
        com.doupo.protocol.HeroSkillSlotUnlockResp resp =
                (com.doupo.protocol.HeroSkillSlotUnlockResp)
                        context.writes.get(0).message;
        assertEquals(0, resp.getHeroIndex());
        assertEquals(3001, resp.getSlot().getId());
        assertEquals(0, resp.getSlot().getLv());
        assertFalse(resp.getSlot().getAnyOnSkill());
    }

    /** 突破已经推过 3001 后，再打开斗技不能重复 75015。 */
    @Test
    public void eighthRealmOriginNodeStatDoesNotDuplicateThirdSlot() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_024L);

        breakHeroToLevelFive(handler, context);
        makeAlchemyWithInventory(handler, context);
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(12)
                .addAlchemyId(13)
                .build());
        handler.breakHeroLevel(
                context,
                HeroLevelBreakReq.newBuilder().setHeroIndex(0).build());
        context.writes.clear();

        handler.queryHeroOriginNodeStat(
                context,
                HeroOriginNodeStatReq.newBuilder()
                        .setHeroIndex(0)
                        .setStatShowNode(3)
                        .build());

        assertEquals(50459, context.writes.get(0).protocolId);
        assertEquals(1, context.writes.size());
    }

    /** 第三格装焰分噬浪尺（第 9 关技能）必须回显方案、点亮槽位并下发 10150310100。 */
    @Test
    public void equippingYanfenOnThirdSlotUnlocksFightOutput() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_031L);

        handler.updateHeroSkillSchema(
                context,
                HeroSkillSchemaUpdateReq.newBuilder()
                        .setHeroIndex(0)
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(1001)
                                .setValue(80130001))
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(2001)
                                .setValue(80128011))
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(3001)
                                .setValue(80128001))
                        .setReqSource(1)
                        .build());

        com.doupo.protocol.HeroSkillSchemaUpdateResp schema = null;
        HeroSkillSlotUpdateResp slot = null;
        ActiveSkillChangeResp active = null;
        PlayerFightForceResp force = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 75005) {
                schema = (com.doupo.protocol.HeroSkillSchemaUpdateResp)
                        write.message;
            }
            if (write.protocolId == 75015) {
                slot = (HeroSkillSlotUpdateResp) write.message;
            }
            if (write.protocolId == 81751) {
                active = (ActiveSkillChangeResp) write.message;
            }
            if (write.protocolId == 52351) {
                force = (PlayerFightForceResp) write.message;
            }
        }
        assertTrue(schema != null);
        assertEquals(3, schema.getSchema().getSlot2SkillBaseIdsCount());
        assertEquals(2001, schema.getSchema().getSlot2SkillBaseIds(0).getKey());
        assertEquals(1001, schema.getSchema().getSlot2SkillBaseIds(1).getKey());
        assertEquals(3001, schema.getSchema().getSlot2SkillBaseIds(2).getKey());
        assertEquals(80128001,
                schema.getSchema().getSlot2SkillBaseIds(2).getValue());
        assertTrue(slot != null);
        assertEquals(3001, slot.getSlots(0).getId());
        assertTrue(slot.getSlots(0).getAnyOnSkill());
        assertTrue(active != null);
        assertEquals(10150310100L, active.getCanUseSkill(1));
        assertEquals(80128001, active.getBuffTrigger2BaseSkill(1).getValue());
        assertTrue(force != null);
        assertEquals(34959, force.getPlayerFightForce(), 0.0001);
        handler.resetTutorialPlayer(context.getId());
    }

    /**
     * 八段解锁的第三格是空槽，装已有斗技（裂爪击 80128011）必须点亮槽位并
     * 下发对应战斗技能，不能当成焰分噬浪尺去改火元素和 34959 战力。
     */
    @Test
    public void thirdSlotAcceptsAnyOwnedSkillWithoutYanfenStats() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_041L);

        handler.updateHeroSkillSchema(
                context,
                HeroSkillSchemaUpdateReq.newBuilder()
                        .setHeroIndex(0)
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(1001)
                                .setValue(80140001))
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(2001)
                                .setValue(80130011))
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(3001)
                                .setValue(80128011))
                        .setReqSource(1)
                        .build());

        com.doupo.protocol.HeroSkillSchemaUpdateResp schema = null;
        HeroSkillSlotUpdateResp slot = null;
        ActiveSkillChangeResp active = null;
        PlayerFightForceResp force = null;
        boolean fireElement = false;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 75005) {
                schema = (com.doupo.protocol.HeroSkillSchemaUpdateResp)
                        write.message;
            }
            if (write.protocolId == 75015) {
                slot = (HeroSkillSlotUpdateResp) write.message;
            }
            if (write.protocolId == 81751) {
                active = (ActiveSkillChangeResp) write.message;
            }
            if (write.protocolId == 52351) {
                force = (PlayerFightForceResp) write.message;
            }
            if (write.protocolId == 78073 || write.protocolId == 75048) {
                fireElement = true;
            }
        }
        assertTrue(schema != null);
        assertEquals(3001, schema.getSchema().getSlot2SkillBaseIds(2).getKey());
        assertEquals(80128011,
                schema.getSchema().getSlot2SkillBaseIds(2).getValue());
        assertTrue(slot != null);
        assertEquals(3001, slot.getSlots(0).getId());
        assertTrue(slot.getSlots(0).getAnyOnSkill());
        assertTrue(active != null);
        assertEquals(1, active.getCanUseSkillCount());
        assertEquals(10150410100L, active.getCanUseSkill(0));
        assertEquals(80128011, active.getBuffTrigger2BaseSkill(0).getValue());
        assertTrue(force == null);
        assertFalse(fireElement);
        handler.resetTutorialPlayer(context.getId());
    }

    /** 第三格装裂爪击后，10150410100 必须打出带 80128011 的 FightSkill。 */
    @Test
    public void thirdSlotOwnedSkillWritesFightSkillDamage() {
        long playerId = 100_100_000_042L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        handler.updateHeroSkillSchema(
                context,
                HeroSkillSchemaUpdateReq.newBuilder()
                        .setHeroIndex(0)
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(1001)
                                .setValue(80140001))
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(2001)
                                .setValue(80130011))
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(3001)
                                .setValue(80128011))
                        .setReqSource(1)
                        .build());
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200101)
                        .setZ(22.93f)
                        .build());
        long targetId = handler.waveUnitIdAt(playerId, 0);
        context.writes.clear();

        handler.useGuidanceMainMapSkill(
                context,
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10150410100L)
                        .setTargetId(targetId)
                        .setX(0)
                        .setZ(10)
                        .setTx(1)
                        .setTz(12)
                        .setTime(1L)
                        .build());

        SkillActionResp hit = context.writes.stream()
                .filter(write -> write.protocolId == 50763)
                .map(write -> (SkillActionResp) write.message)
                .filter(action -> action.getActionListCount() > 0)
                .findFirst().get();
        assertEquals(10150410100L, hit.getSkillId());
        assertEquals(DamageTypeVo.FIGHT_SKILL,
                hit.getActionList(0).getDamageVo().getType());
        assertEquals(80128011,
                hit.getActionList(0).getDamageVo().getSkillBaseId());
        assertFalse(context.writes.stream()
                .anyMatch(write -> write.protocolId == 50844));
        CombatSessionRegistry.clear(playerId);
        handler.resetTutorialPlayer(playerId);
    }

    /** 焰分噬浪尺出手必须是 FightSkill 伤害，带 80128001，8 秒 CD。 */
    @Test
    public void yanfenWaveSkillWritesCapturedFightSkillDamage() {
        long playerId = 100_100_000_032L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        handler.updateHeroSkillSchema(
                context,
                HeroSkillSchemaUpdateReq.newBuilder()
                        .setHeroIndex(0)
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(1001)
                                .setValue(80130001))
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(2001)
                                .setValue(80128011))
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(3001)
                                .setValue(80128001))
                        .setReqSource(1)
                        .build());
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200503)
                        .setZ(114.6f)
                        .build());
        long targetId = handler.waveUnitIdAt(playerId, 0);
        context.writes.clear();

        handler.useGuidanceMainMapSkill(
                context,
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10150310100L)
                        .setTargetId(targetId)
                        .setX(2.74f)
                        .setZ(105.71f)
                        .setTx(3.10f)
                        .setTz(106.47f)
                        .setTime(1787283303203L)
                        .build());

        UseSkillResp use = (UseSkillResp) context.writes.stream()
                .filter(write -> write.protocolId == 50762)
                .findFirst().get().message;
        assertEquals(10150310100L, use.getSkillId());
        FightSkillUpdateResp cd = (FightSkillUpdateResp) context.writes.stream()
                .filter(write -> write.protocolId == 50766)
                .findFirst().get().message;
        assertEquals(10150310100L, cd.getSkillId());
        assertEquals(8000, cd.getCostCd());
        assertTrue(context.writes.stream()
                .anyMatch(write -> write.protocolId == 50844));
        SkillActionResp hit = context.writes.stream()
                .filter(write -> write.protocolId == 50763)
                .map(write -> (SkillActionResp) write.message)
                .filter(action -> action.getActionListCount() > 0)
                .findFirst().get();
        assertEquals(10150310100L, hit.getSkillId());
        assertEquals(DamageTypeVo.FIGHT_SKILL,
                hit.getActionList(0).getDamageVo().getType());
        assertEquals(80128001,
                hit.getActionList(0).getDamageVo().getSkillBaseId());
        assertTrue(hit.getActionList(0).getDamageVo().getDamage() > 1000);
        CombatSessionRegistry.clear(playerId);
        handler.resetTutorialPlayer(playerId);
    }

    /**
     * 九段升阶到一星斗者完成 200025，对应抓包 idx 9582-9595。
     */
    @Test
    public void heroStepUpgradeFinishesTask200025() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_025L);

        for (int round = 0; round < 3; round++) {
            makeAlchemyWithInventory(handler, context);
            handler.takeAlchemy(
                    context,
                    AlchemyNewTakingReq.newBuilder()
                            .addAlchemyId(round * 4L)
                            .addAlchemyId(round * 4L + 1)
                            .addAlchemyId(round * 4L + 2)
                            .addAlchemyId(round * 4L + 3)
                            .build());
            handler.breakHeroLevel(
                    context,
                    HeroLevelBreakReq.newBuilder().setHeroIndex(0).build());
        }
        for (int round = 3; round < 5; round++) {
            makeAlchemyWithInventory(handler, context);
            handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                    .addAlchemyId(round * 4L).addAlchemyId(round * 4L + 1)
                    .addAlchemyId(round * 4L + 2).addAlchemyId(round * 4L + 3)
                    .build());
        }
        handler.breakHeroLevel(
                context,
                HeroLevelBreakReq.newBuilder().setHeroIndex(0).build());
        rewardTask(handler, context, 200024);
        assertTask(lastTaskUpdate(context), 0, 200025, TaskPhase.PROGRESS, 0);

        context.writes.clear();
        handler.upgradeHeroStep(
                context,
                HeroStepUpgradeReq.newBuilder().setHeroIndex(0).build());

        HeroStepUpgradeResp step = (HeroStepUpgradeResp) context.writes.get(0).message;
        assertEquals(75059, context.writes.get(0).protocolId);
        assertEquals(8, step.getHeroVo().getLevel());
        assertEquals(2, step.getHeroVo().getStage());
        assertTrue(containsTask(
                lastTaskUpdate(context), 200025, TaskPhase.FINISHED));

        AlchemyNewLevelUpgradeResp alchemyLv = null;
        HeroRoadRewardResp road = null;
        HeroStatUpdateResp stats = null;
        PlayerFightForceResp force = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 75065) {
                alchemyLv = (AlchemyNewLevelUpgradeResp) write.message;
            }
            if (write.protocolId == 75074) {
                road = (HeroRoadRewardResp) write.message;
            }
            if (write.protocolId == 50455) {
                stats = (HeroStatUpdateResp) write.message;
            }
            if (write.protocolId == 52351) {
                force = (PlayerFightForceResp) write.message;
            }
        }
        assertTrue(alchemyLv != null);
        assertEquals(2, alchemyLv.getLevel());
        assertTrue(road != null);
        assertEquals(1, road.getId());
        assertTrue(stats != null);
        assertEquals(3449.48976, statValue(stats, 101001), 0.0001);
        assertEquals(1508, statValue(stats, 102001), 0.0001);
        assertEquals(88506.5, statValue(stats, 103001), 0.0001);
        assertEquals(1354, statValue(stats, 101002), 0.0001);
        assertTrue(force != null);
        assertEquals(60577, force.getPlayerFightForce(), 0.0001);

        context.writes.clear();
        rewardTask(handler, context, 200025);
        assertTask(lastTaskUpdate(context), 0, 200054, TaskPhase.PROGRESS, 0);
    }

    /** 九段顶格再点突破，必须当成升阶打到一星斗者。 */
    @Test
    public void ninthRealmBreakUpgradesToOneStarFighter() {
        long playerId = 100_100_000_310L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        for (int round = 0; round < 3; round++) {
            makeAlchemyWithInventory(handler, context);
            handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                    .addAlchemyId(round * 4L).addAlchemyId(round * 4L + 1)
                    .addAlchemyId(round * 4L + 2).addAlchemyId(round * 4L + 3)
                    .build());
            handler.breakHeroLevel(
                    context, HeroLevelBreakReq.newBuilder().setHeroIndex(0).build());
        }
        for (int round = 3; round < 5; round++) {
            makeAlchemyWithInventory(handler, context);
            handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                    .addAlchemyId(round * 4L).addAlchemyId(round * 4L + 1)
                    .addAlchemyId(round * 4L + 2).addAlchemyId(round * 4L + 3)
                    .build());
        }
        handler.breakHeroLevel(
                context, HeroLevelBreakReq.newBuilder().setHeroIndex(0).build());
        context.writes.clear();
        handler.breakHeroLevel(
                context, HeroLevelBreakReq.newBuilder().setHeroIndex(0).build());
        HeroStepUpgradeResp step = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 75059) {
                step = (HeroStepUpgradeResp) write.message;
            }
        }
        assertTrue(step != null);
        assertEquals(8, step.getHeroVo().getLevel());
        assertEquals(2, step.getHeroVo().getStage());
        assertTrue(containsTask(
                lastTaskUpdate(context), 200025, TaskPhase.FINISHED));
        CombatSessionRegistry.clear(playerId);
    }

    /** 九段服药修为够了，必须直接升到一星斗者。 */
    @Test
    public void ninthRealmTakingAutoStepsToOneStar() {
        long playerId = 100_100_000_311L;
        SceneHandler handler = new SceneHandler();
        handler.setServerOpenDay(2); // 此用例需要第六份普通灵液；第 1 天配置上限为 5。
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        for (int round = 0; round < 3; round++) {
            makeAlchemyWithInventory(handler, context);
            handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                    .addAlchemyId(round * 4L).addAlchemyId(round * 4L + 1)
                    .addAlchemyId(round * 4L + 2).addAlchemyId(round * 4L + 3)
                    .build());
            handler.breakHeroLevel(
                    context, HeroLevelBreakReq.newBuilder().setHeroIndex(0).build());
        }
        for (int round = 3; round < 5; round++) {
            makeAlchemyWithInventory(handler, context);
            handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                    .addAlchemyId(round * 4L).addAlchemyId(round * 4L + 1)
                    .addAlchemyId(round * 4L + 2).addAlchemyId(round * 4L + 3)
                    .build());
        }
        handler.breakHeroLevel(
                context, HeroLevelBreakReq.newBuilder().setHeroIndex(0).build());
        makeAlchemyWithInventory(handler, context);
        context.writes.clear();
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(20).addAlchemyId(21)
                .addAlchemyId(22).addAlchemyId(23)
                .build());
        HeroStepUpgradeResp step = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 75059) {
                step = (HeroStepUpgradeResp) write.message;
            }
        }
        assertTrue(step != null);
        assertEquals(8, step.getHeroVo().getLevel());
        CombatSessionRegistry.clear(playerId);
    }

    /**
     * 一星斗者之后继续斗之气修行：突破到二星，普通升级到三星。
     */
    @Test
    public void fighterRealmContinuesAfterOneStar() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_308L);
        stepToOneStarFighter(handler, context);
        rewardTask(handler, context, 200027);
        assertTask(lastTaskUpdate(context), 0, 200028, TaskPhase.PROGRESS, 0);

        makeAlchemyWithInventory(handler, context);
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(20)
                .addAlchemyId(21)
                .addAlchemyId(22)
                .build());
        context.writes.clear();
        handler.breakHeroLevel(
                context,
                HeroLevelBreakReq.newBuilder().setHeroIndex(0).build());

        HeroLevelBreakResp twoStar = null;
        PlayerFightForceResp force = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 75067) {
                twoStar = (HeroLevelBreakResp) write.message;
            }
            if (write.protocolId == 52351) {
                force = (PlayerFightForceResp) write.message;
            }
        }
        assertTrue(twoStar != null);
        assertEquals(9, twoStar.getHeroVo().getLevel());
        assertTrue(containsTask(
                lastTaskUpdate(context), 200028, TaskPhase.FINISHED));
        assertTrue(force != null);
        assertEquals(PlayerRealmConfig.fightForce(9),
                force.getPlayerFightForce(),
                0.0001);

        makeAlchemyWithInventory(handler, context);
        context.writes.clear();
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(24)
                .addAlchemyId(25)
                .addAlchemyId(26)
                .build());
        HeroLevelUpgradeResp threeStar = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 75057) {
                threeStar = (HeroLevelUpgradeResp) write.message;
            }
        }
        assertTrue(threeStar != null);
        assertEquals(10, threeStar.getHeroVo().getLevel());
    }

    @Test
    public void sixthAlchemyMakeKeepsFourPills() {
        SceneHandler handler = new SceneHandler();
        handler.setServerOpenDay(2);
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_309L);
        for (int i = 0; i < 5; i++) {
            makeAlchemyWithInventory(handler, context);
        }
        context.writes.clear();
        makeAlchemyWithInventory(handler, context);
        AlchemyNewMakeResp make = (AlchemyNewMakeResp) context.writes.stream()
                .filter(write -> write.protocolId == 75053)
                .findFirst()
                .get()
                .message;
        assertEquals(6, make.getMakeTimes());
        assertEquals(4, make.getMakeIdsCount());
        assertEquals(280, make.getMakeIds(0).getExp());
    }

    /** 同一颗灵液只能按抓包中的 alchemyVo 消耗一次。 */
    @Test
    public void consumedAlchemyCannotBeTakenTwice() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_003L);

        makeAlchemyWithInventory(handler, context);
        context.writes.clear();
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(0)
                .build());
        assertTaking(context, 280, 1, 0);

        context.writes.clear();
        handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(0)
                .build());
        assertEquals(0, context.writes.size());
    }

    private static void assertTaking(
            RecordingPlayerContext context,
            long exp,
            int takingCount,
            long... alchemyIds) {
        assertEquals(75055, context.writes.get(0).protocolId);
        AlchemyNewTakingResp taking =
                (AlchemyNewTakingResp) context.writes.get(0).message;
        assertEquals(exp, taking.getExp());
        assertEquals(alchemyIds.length, taking.getAlchemyVoCount());
        for (int i = 0; i < alchemyIds.length; i++) {
            assertEquals(alchemyIds[i], taking.getAlchemyVo(i));
        }
        assertEquals(1101, taking.getTakingInfo(0).getKey());
        assertEquals(takingCount, taking.getTakingInfo(0).getValue());
    }

    @Test
    public void finishSceneLoadPublishesCapturedHeroShortInfo() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(playerId);

        SceneUpdateVisibleResp visible = handler.finishSceneLoadB(
                context,
                ChangeSceneFinishBReq.newBuilder()
                        .setMapId(3)
                        .build());

        assertEquals(2, visible.getVisibleListCount());
        assertEquals(2, context.writes.size());
        assertEquals(75153, context.writes.get(0).protocolId);
        assertEquals(75061, context.writes.get(1).protocolId);

        HeroShortInfoUpdateResp update =
                (HeroShortInfoUpdateResp) context.writes.get(0).message;
        assertEquals(0, update.getShortInfoVo().getHeroIndex());
        assertEquals(1001,
                update.getShortInfoVo()
                        .getCrossHeroShortInfo()
                        .getJob());
        assertEquals(playerId * 1000 + 1,
                update.getShortInfoVo()
                        .getCrossHeroShortInfo()
                        .getSceneUnitUid());
        assertEquals(1,
                update.getShortInfoVo()
                        .getCrossHeroShortInfo()
                        .getLevel());
        assertEquals(1,
                update.getShortInfoVo()
                        .getCrossHeroShortInfo()
                        .getStage());
        assertEquals(2,
                update.getShortInfoVo()
                        .getCrossHeroShortInfo()
                        .getSkinListCount());
        assertEquals(101,
                update.getShortInfoVo()
                        .getCrossHeroShortInfo()
                        .getSkinList(0)
                        .getCfgId());
        assertEquals(100101,
                update.getShortInfoVo()
                        .getCrossHeroShortInfo()
                        .getSkinList(1)
                        .getCfgId());
    }

    /**
     * 切图 50755 不能把已突破的境界写回 level=1（客户端会显示成三段斗之气）。
     */
    @Test
    public void finishSceneLoadKeepsBrokenHeroLevel() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_043L);
        breakHeroToLevelFive(handler, context);
        context.writes.clear();

        SceneUpdateVisibleResp visible = handler.finishSceneLoadB(
                context,
                ChangeSceneFinishBReq.newBuilder()
                        .setMapId(3)
                        .build());

        HeroShortInfoUpdateResp update =
                (HeroShortInfoUpdateResp) context.writes.get(0).message;
        assertEquals(5,
                update.getShortInfoVo()
                        .getCrossHeroShortInfo()
                        .getLevel());
        assertEquals(5,
                visible.getVisibleList(0)
                        .getHeroVo()
                        .getCrossHeroShortInfo()
                        .getLevel());
        handler.resetTutorialPlayer(context.getId());
    }

    @Test
    public void visibleSnapshotContainsLocalPlayerAtSceneBirthPoint() {
        long playerId = 100_000_000_001L;

        SceneUpdateVisibleResp response =
                SceneHandler.visibleSnapshot(playerId);

        assertTrue(response.getSnapshot());
        assertEquals(2, response.getVisibleListCount());

        SceneUnitVo player = response.getVisibleList(0);
        assertEquals(playerId, player.getHeroVo().getPlayerId());
        assertEquals(1001,
                player.getHeroVo()
                        .getCrossHeroShortInfo()
                        .getJob());
        assertEquals(-15.0f,
                player.getBaseInfoVo().getZ(),
                0.0f);
        assertEquals(1,
                player.getBaseInfoVo().getUnitType());
        assertEquals(22,
                player.getFightInfoVo().getAttributeListCount());
        assertEquals(6,
                player.getHeroVo()
                        .getSkillContainerVo()
                        .getSkillListCount());
        // 主动技能（大招）10150510101：官服 idx 1927 activeSkill=true。
        FightSkillUpdateResp activeSkill = player.getHeroVo()
                .getSkillContainerVo()
                .getSkillList(5);
        assertEquals(10150510101L, activeSkill.getSkillId());
        assertTrue(activeSkill.getActiveSkill());
        assertEquals(100, activeSkill.getCostMp());
        assertEquals(1000, activeSkill.getCostCd());
        assertEquals(24,
                response.getVisibleList(1)
                        .getBaseInfoVo()
                        .getUnitType());
    }

    @Test
    public void nonSceneSnapshotMatchesCapturedHeroShape() {
        long playerId = 100_000_000_001L;

        SyncNonSceneHeroVoUpdateResp response =
                SceneHandler.nonSceneHeroSnapshot(playerId);

        assertEquals(1, response.getVoListCount());
        assertEquals(playerId,
                response.getVoList(0)
                        .getHeroVo()
                        .getPlayerId());
        assertEquals(22,
                response.getVoList(0)
                        .getFightInfoVo()
                        .getAttributeListCount());
    }

    /** 通过 handler 入口驱动，验证第一关四只小怪与抓包一致。 */
    @Test
    public void firstChapterSpawnsCapturedFourMonsters() {
        List<SceneUnitVo> monsters = enterChapter(10100101, 5.229984f);

        assertEquals(4, monsters.size());
        assertEquals(121010,
                monsters.get(0).getSceneMonsterVo().getMonsterId());
        assertEquals(1000101L,
                monsters.get(0).getSceneMonsterVo().getTemplateId());
        assertEquals(1000104L,
                monsters.get(3).getSceneMonsterVo().getTemplateId());
        assertEquals(5.229984f,
                monsters.get(0).getBaseInfoVo().getZ(), 0.0001f);
        assertEquals(50.229984f,
                monsters.get(3).getBaseInfoVo().getZ(), 0.0001f);
        assertEquals(2, monsters.get(0).getBaseInfoVo().getUnitType());
        assertTrue(monsters.get(0).getSceneMonsterVo().getFirstKill());
    }

    @Test
    public void fourthKillTaskUpdateMarksMainTaskFinished() {
        TaskUpdateResp response = SceneHandler.killTaskUpdate(
                ChapterConfig.get(10100101), 4);

        assertEquals(6, response.getTaskVosCount());
        assertEquals(200000,
                response.getTaskVos(5)
                        .getTaskId()
                        .getTaskResourceId());
        assertEquals(TaskPhase.FINISHED,
                response.getTaskVos(5).getTaskPhase());
        assertEquals(4L,
                response.getTaskVos(5).getProgressValues(0));
    }

    /**
     * 第二关的主线任务是 200003。抓包 idx 441 显示两套进度数字是分开的：
     * 通用统计任务（1104 等）用全局累计值 11→14，主线任务用本关击杀数 1→4——
     * 第一关因为从 0 起算容易把两者当成同一个数字，第二关会立刻暴露差异。
     */
    @Test
    public void secondChapterMainTaskUsesInChapterCountNotCumulative() {
        ChapterConfig.Chapter chapter = ChapterConfig.get(10100201);

        TaskUpdateResp midway = SceneHandler.killTaskUpdate(chapter, 1);
        TaskUpdateResp cleared = SceneHandler.killTaskUpdate(chapter, 4);

        assertTask(midway, 0, 1101, TaskPhase.FINISHED, 10);
        assertTask(midway, 4, 200003, TaskPhase.PROGRESS, 1);

        assertTask(cleared, 0, 1104, TaskPhase.PROGRESS, 13);
        assertTask(cleared, 3, 200003, TaskPhase.FINISHED, 4);
    }

    /** 第二关结算的两次 50906 必须逐项匹配抓包 idx 441/448。 */
    @Test
    public void secondChapterEndFightMatchesCapturedTaskUpdates() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100201)
                        .build());

        long base = handler.waveUnitIdAt(100_000_000_001L, 0);
        for (int i = 0; i < 4; i++) {
            handler.killGuidanceMonster(
                    context,
                    com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                            .setMonsterId(base + i)
                            .build());
        }

        context.writes.clear();
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10100201)
                        .build());

        List<TaskUpdateResp> updates = new ArrayList<>();
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 50906) {
                updates.add((TaskUpdateResp) write.message);
            }
        }

        assertEquals(2, updates.size());

        TaskUpdateResp finished = updates.get(0);
        assertEquals(5, finished.getTaskVosCount());
        assertTask(finished, 0, 1104, TaskPhase.PROGRESS, 13);
        assertTask(finished, 1, 1107, TaskPhase.PROGRESS, 13);
        assertTask(finished, 2, 1110, TaskPhase.PROGRESS, 13);
        assertTask(finished, 3, 200003, TaskPhase.FINISHED, 4);
        assertTask(finished, 4, 1113, TaskPhase.PROGRESS, 13);

        TaskUpdateResp cumulative = updates.get(1);
        assertEquals(4, cumulative.getTaskVosCount());
        assertTask(cumulative, 0, 1104, TaskPhase.PROGRESS, 14);
        assertTask(cumulative, 1, 1107, TaskPhase.PROGRESS, 14);
        assertTask(cumulative, 2, 1110, TaskPhase.PROGRESS, 14);
        assertTask(cumulative, 3, 1113, TaskPhase.PROGRESS, 14);
    }

    /** 第二关领奖必须匹配抓包 idx 479-483，并兼容抓包中未解析出 taskId 的请求。 */
    @Test
    public void secondChapterRewardMatchesCapturedResponses() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100201)
                        .build());
        handler.resetGuidanceMainMap(
                context,
                GuidanceMainMapResetReq.newBuilder()
                        .setEnterNext(true)
                        .build());

        context.writes.clear();
        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.getDefaultInstance());

        assertEquals(5, context.writes.size());
        assertEquals(50651, context.writes.get(0).protocolId);
        assertEquals(50406, context.writes.get(1).protocolId);
        assertEquals(53702, context.writes.get(2).protocolId);
        assertEquals(50906, context.writes.get(3).protocolId);
        assertEquals(50906, context.writes.get(4).protocolId);

        PurseUpdateResp purse =
                (PurseUpdateResp) context.writes.get(0).message;
        assertEquals(15L, purse.getItems(0).getValue());
        assertEquals(0L, purse.getItems(1).getValue());

        RewardResp reward = (RewardResp) context.writes.get(1).message;
        assertEquals(50907, reward.getOperationType());
        assertEquals(2, reward.getRewardItemVos(0).getItemKey());
        assertEquals(5L, reward.getRewardItemVos(0).getAmount());

        ReputationLvOnTaskRewardUpdateResp reputation =
                (ReputationLvOnTaskRewardUpdateResp)
                        context.writes.get(2).message;
        assertEquals(200003, reputation.getTaskId());

        TaskUpdateResp rewarded =
                (TaskUpdateResp) context.writes.get(3).message;
        assertEquals(1, rewarded.getTaskVosCount());
        assertTask(rewarded, 0, 200003, TaskPhase.REWARDED, 4);

        TaskUpdateResp next =
                (TaskUpdateResp) context.writes.get(4).message;
        assertEquals(1, next.getTaskVosCount());
        assertTask(next, 0, 200004, TaskPhase.PROGRESS, 0);
    }

    /**
     * 小怪关打完必须立即下发 61952 解锁 Boss，不能等客户端发 61975——
     * 抓包 idx 449 显示章节更新紧跟在 EndFight 之后。
     */
    @Test
    public void clearingMobChapterImmediatelyUnlocksBoss() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100201)
                        .build());

        long base = handler.waveUnitIdAt(100_000_000_001L, 0);
        for (int i = 0; i < 4; i++) {
            handler.killGuidanceMonster(
                    context,
                    com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                            .setMonsterId(base + i)
                            .build());
        }

        context.writes.clear();
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10100201)
                        .build());

        MainMapPassChapterUpdateResp chapterUpdate = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 61952) {
                chapterUpdate = (MainMapPassChapterUpdateResp) write.message;
            }
        }

        assertTrue("expected 61952 right after EndFight",
                chapterUpdate != null);
        assertEquals(10100201, chapterUpdate.getMainMapChapterId());
        assertEquals(10100205, chapterUpdate.getNextChallengeId());
        assertEquals(4, chapterUpdate.getChangeReason());
    }

    @Test
    public void mainMapKillsPushCapturedTaskProgressBeforeFourthKill() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100101)
                        .build());
        context.writes.clear();

        for (int i = 1; i <= 4; i++) {
            handler.killMainMapMonster(
                    context,
                    MainMapKillMonsterReq.newBuilder()
                            .setMainMapChapterId(10100101)
                            .setMonsterId(1_000_100L + i)
                            .build());
        }

        assertEquals(3, context.writes.size());
        for (int i = 0; i < context.writes.size(); i++) {
            RecordedWrite write = context.writes.get(i);
            assertEquals(50906, write.protocolId);
            TaskUpdateResp response = (TaskUpdateResp) write.message;
            assertEquals(i + 1,
                    response.getTaskVos(5).getProgressValues(0));
        }
    }

    @Test
    public void firstBossMatchesCapturedBoss() {
        List<SceneUnitVo> monsters = enterChapter(10100105, 472.7f);

        assertEquals(1, monsters.size());
        SceneUnitVo boss = monsters.get(0);
        assertEquals(100004, boss.getSceneMonsterVo().getMonsterId());
        assertEquals(1000140L, boss.getSceneMonsterVo().getTemplateId());
        assertEquals(10100105, boss.getSceneMonsterVo().getMainMapId());
        assertEquals(1, boss.getSceneMonsterVo().getMainMapRewardCount());
        assertEquals(100200,
                boss.getSceneMonsterVo().getMainMapReward(0).getItemKey());
        assertEquals(472.7f, boss.getBaseInfoVo().getZ(), 0.0001f);
    }

    /** 第一关 Boss 死亡后必须立即把主线任务 200001 标记为 1/1。 */
    @Test
    public void firstBossKillWritesCapturedTaskCompletion() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100105)
                        .build());
        context.writes.clear();

        handler.killGuidanceMonster(
                context,
                com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                        .setMonsterId(handler.waveUnitIdAt(100_000_000_001L, 0))
                        .build());

        List<TaskUpdateResp> updates = new ArrayList<>();
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 50906) {
                updates.add((TaskUpdateResp) write.message);
            }
        }

        assertEquals(1, updates.size());
        TaskUpdateResp completion = updates.get(0);
        assertEquals(6, completion.getTaskVosCount());
        assertTask(completion, 0, 1101, TaskPhase.PROGRESS, 9);
        assertTask(completion, 1, 1104, TaskPhase.PROGRESS, 9);
        assertTask(completion, 2, 1107, TaskPhase.PROGRESS, 9);
        assertTask(completion, 3, 1110, TaskPhase.PROGRESS, 9);
        assertTask(completion, 4, 1113, TaskPhase.PROGRESS, 9);
        assertTask(completion, 5, 200001, TaskPhase.FINISHED, 1);
    }

    /** 第一关 Boss 任务领奖必须匹配抓包 idx 329-343 的响应顺序。 */
    @Test
    public void firstBossRewardMatchesCapturedResponses() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100105)
                        .build());
        context.writes.clear();

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(com.doupo.protocol.TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200001))
                        .build());

        int[] protocolIds = {
                50402, 50406, 53702, 50402, 75011, 75049, 75039,
                50906, 50455, 50801, 50801, 52351, 75151, 50906
        };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        RewardResp reward = (RewardResp) context.writes.get(1).message;
        assertEquals(50907, reward.getOperationType());
        assertEquals(80140000, reward.getRewardItemVos(0).getItemKey());
        assertEquals(1L, reward.getRewardItemVos(0).getAmount());

        TaskUpdateResp rewarded =
                (TaskUpdateResp) context.writes.get(7).message;
        assertTask(rewarded, 0, 200001, TaskPhase.REWARDED, 1);

        TaskUpdateResp followUp =
                (TaskUpdateResp) context.writes.get(13).message;
        assertTask(followUp, 7, 200002, TaskPhase.PROGRESS, 0);
    }

    /** 装配 80140001 后必须按抓包 idx 349-358 完成任务 200002。 */
    @Test
    public void learningFirstSkillFinishesMainTask200002() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.updateHeroSkillSchema(
                context,
                HeroSkillSchemaUpdateReq.newBuilder()
                        .setHeroIndex(0)
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(1001)
                                .setValue(80140001))
                        .setReqSource(1)
                        .build());

        int[] protocolIds = {
                75005, 75015, 60751, 50906, 50455,
                50801, 50801, 52351, 75151, 50906
        };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        TaskUpdateResp finished =
                (TaskUpdateResp) context.writes.get(3).message;
        assertTask(finished, 0, 200002, TaskPhase.FINISHED, 1);
    }

    /** 第二次装配（槽位 2001 装 80130011）必须按抓包 idx 1110-1120 刷新属性与战力。 */
    @Test
    public void secondSkillEquipRefreshesStatsAndCombatPower() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.updateHeroSkillSchema(
                context,
                HeroSkillSchemaUpdateReq.newBuilder()
                        .setHeroIndex(0)
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(1001)
                                .setValue(80140001))
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(2001)
                                .setValue(80130011))
                        .setReqSource(1)
                        .build());

        int[] protocolIds = {
                75005, 75015, 50801, 50801, 50455,
                50801, 50801, 52351, 75151, 50906
        };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        com.doupo.protocol.HeroSkillSchemaUpdateResp schema =
                (com.doupo.protocol.HeroSkillSchemaUpdateResp)
                        context.writes.get(0).message;
        assertEquals(2, schema.getSchema().getSlot2SkillBaseIdsCount());
        assertEquals(2001,
                schema.getSchema().getSlot2SkillBaseIds(0).getKey());
        assertEquals(80130011,
                schema.getSchema().getSlot2SkillBaseIds(0).getValue());
        assertEquals(1001,
                schema.getSchema().getSlot2SkillBaseIds(1).getKey());

        com.doupo.protocol.HeroSkillSlotUpdateResp slot =
                (com.doupo.protocol.HeroSkillSlotUpdateResp)
                        context.writes.get(1).message;
        assertEquals(2001, slot.getSlots(0).getId());

        PlayerFightForceResp force =
                (PlayerFightForceResp) context.writes.get(7).message;
        assertEquals(14623.0, force.getPlayerFightForce(), 0.0);
    }

    /** 升 2 阶后替换斗技只回显 75005，对应抓包 idx 1962-1963。 */
    @Test
    public void replacingSecondSlotEchoesSchemaWithoutTutorialStats() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.updateHeroSkillSchema(
                context,
                HeroSkillSchemaUpdateReq.newBuilder()
                        .setHeroIndex(0)
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(1001)
                                .setValue(80140001))
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(2001)
                                .setValue(80128011))
                        .setReqSource(1)
                        .build());

        assertEquals(1, context.writes.size());
        assertEquals(75005, context.writes.get(0).protocolId);
        com.doupo.protocol.HeroSkillSchemaUpdateResp schema =
                (com.doupo.protocol.HeroSkillSchemaUpdateResp)
                        context.writes.get(0).message;
        assertEquals(2, schema.getSchema().getSlot2SkillBaseIdsCount());
        assertEquals(2001, schema.getSchema().getSlot2SkillBaseIds(0).getKey());
        assertEquals(80128011,
                schema.getSchema().getSlot2SkillBaseIds(0).getValue());
        assertEquals(1001, schema.getSchema().getSlot2SkillBaseIds(1).getKey());
        assertEquals(80140001,
                schema.getSchema().getSlot2SkillBaseIds(1).getValue());
    }

    /**
     * 把吹火掌 80130001 装进 1001 必须刷新属性与战力，对应抓包 idx 8838-8845。
     */
    @Test
    public void equippingBlowPalmOnAttackSlotRefreshesStatsAndPower() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_051L);

        handler.updateHeroSkillSchema(
                context,
                HeroSkillSchemaUpdateReq.newBuilder()
                        .setHeroIndex(0)
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(2001)
                                .setValue(80128011))
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(1001)
                                .setValue(80130001))
                        .setReqSource(1)
                        .build());

        int[] protocolIds = { 75005, 50455, 50801, 50801, 52351, 75151 };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        com.doupo.protocol.HeroSkillSchemaUpdateResp schema =
                (com.doupo.protocol.HeroSkillSchemaUpdateResp)
                        context.writes.get(0).message;
        assertEquals(80130001,
                schema.getSchema().getSlot2SkillBaseIds(1).getValue());

        HeroStatUpdateResp stats =
                (HeroStatUpdateResp) context.writes.get(1).message;
        assertEquals(1999.6186, statValue(stats, 101001), 0.0001);
        assertEquals(51848.799999999996, statValue(stats, 103001), 0.0001);

        PlayerFightForceResp force =
                (PlayerFightForceResp) context.writes.get(4).message;
        assertEquals(27360, force.getPlayerFightForce(), 0.0001);

        context.writes.clear();
        handler.queryHeroOriginNodeStat(
                context,
                HeroOriginNodeStatReq.newBuilder()
                        .setHeroIndex(0)
                        .setStatShowNode(3)
                        .build());
        HeroOriginNodeStatResp node =
                (HeroOriginNodeStatResp) context.writes.get(0).message;
        assertEquals(27360, node.getHeroVo().getHeroPower(), 0);
        handler.resetTutorialPlayer(context.getId());
    }

    /** 战斗中把 1001 换成危机斗技 100001，对应抓包 idx 5856-5859。 */
    @Test
    public void replacingAttackSkillWithCrisisSkillEchoesSchema() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.updateHeroSkillSchema(
                context,
                HeroSkillSchemaUpdateReq.newBuilder()
                        .setHeroIndex(0)
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(2001)
                                .setValue(80128011))
                        .addSkills(com.doupo.protocol.IntegerAndIntegerPairEntry
                                .newBuilder()
                                .setKey(1001)
                                .setValue(100001))
                        .setReqSource(1)
                        .build());

        assertEquals(1, context.writes.size());
        com.doupo.protocol.HeroSkillSchemaUpdateResp schema =
                (com.doupo.protocol.HeroSkillSchemaUpdateResp)
                        context.writes.get(0).message;
        assertEquals(2001, schema.getSchema().getSlot2SkillBaseIds(0).getKey());
        assertEquals(80128011,
                schema.getSchema().getSlot2SkillBaseIds(0).getValue());
        assertEquals(1001, schema.getSchema().getSlot2SkillBaseIds(1).getKey());
        assertEquals(100001,
                schema.getSchema().getSlot2SkillBaseIds(1).getValue());
    }

    /** 任务 200002 领奖按抓包 idx 368-372 开启下一主线 200003。 */
    @Test
    public void learnedSkillTaskRewardStartsMainTask200003() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(com.doupo.protocol.TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200002))
                        .build());

        int[] protocolIds = {50651, 50406, 53702, 50906, 50906};
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        PurseUpdateResp purse =
                (PurseUpdateResp) context.writes.get(0).message;
        assertEquals(10L, purse.getItems(0).getValue());

        TaskUpdateResp rewarded =
                (TaskUpdateResp) context.writes.get(3).message;
        assertTask(rewarded, 0, 200002, TaskPhase.REWARDED, 1);

        TaskUpdateResp next =
                (TaskUpdateResp) context.writes.get(4).message;
        assertTask(next, 0, 200003, TaskPhase.PROGRESS, 0);
    }

    @Test
    public void secondChapterMonstersMatchCapturedWave() {
        List<SceneUnitVo> monsters = enterChapter(10100201, 505.22998f);

        assertEquals(4, monsters.size());
        assertEquals(1000201L,
                monsters.get(0).getSceneMonsterVo().getTemplateId());
        assertEquals(1000204L,
                monsters.get(3).getSceneMonsterVo().getTemplateId());
        assertEquals(10100201,
                monsters.get(0).getSceneMonsterVo().getMainMapId());
        assertEquals(505.22998f,
                monsters.get(0).getBaseInfoVo().getZ(), 0.0001f);
        assertEquals(550.22998f,
                monsters.get(3).getBaseInfoVo().getZ(), 0.0001f);
    }

    @Test
    public void secondBossMatchesCapturedBoss() {
        List<SceneUnitVo> monsters = enterChapter(10100205, 972.7f);

        assertEquals(1, monsters.size());
        SceneUnitVo boss = monsters.get(0);
        assertEquals(100002, boss.getSceneMonsterVo().getMonsterId());
        assertEquals(1000240L, boss.getSceneMonsterVo().getTemplateId());
        assertEquals(101,
                boss.getSceneMonsterVo().getMainMapReward(0).getItemKey());
        assertEquals(1L,
                boss.getSceneMonsterVo().getMainMapReward(0).getAmount());
    }

    /** 第二关 Boss 死亡后必须精确下发抓包 idx 540 的任务结算。 */
    @Test
    public void secondBossKillWritesCapturedTaskCompletion() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100205)
                        .build());
        context.writes.clear();

        handler.killGuidanceMonster(
                context,
                com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                        .setMonsterId(handler.waveUnitIdAt(100_000_000_001L, 0))
                        .build());

        assertEquals(50402, context.writes.get(0).protocolId);
        PackUpdateResp pack =
                (PackUpdateResp) context.writes.get(0).message;
        assertEquals(61951, pack.getOperationType());
        assertEquals(3,
                pack.getPacks(0).getUpdateItems(0).getItemIndex());
        assertEquals(101,
                pack.getPacks(0)
                        .getUpdateItems(0)
                        .getPackItem()
                        .getKey());
        assertEquals(1,
                pack.getPacks(0)
                        .getUpdateItems(0)
                        .getPackItem()
                        .getSize());

        List<TaskUpdateResp> updates = new ArrayList<>();
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 50906) {
                updates.add((TaskUpdateResp) write.message);
            }
        }

        assertEquals(1, updates.size());
        TaskUpdateResp completion = updates.get(0);
        assertEquals(5, completion.getTaskVosCount());
        assertTask(completion, 0, 1104, TaskPhase.PROGRESS, 19);
        assertTask(completion, 1, 1107, TaskPhase.PROGRESS, 19);
        assertTask(completion, 2, 1110, TaskPhase.PROGRESS, 19);
        assertTask(completion, 3, 1113, TaskPhase.PROGRESS, 19);
        assertTask(completion, 4, 200004, TaskPhase.FINISHED, 1);
    }

    /** 第二关 Boss 任务领奖必须逐条匹配抓包 idx 574-585。 */
    @Test
    public void secondBossRewardMatchesCapturedResponses() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(com.doupo.protocol.TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200004))
                        .build());

        int[] protocolIds = {
                50651, 50406, 53702, 50852, 50384,
                75051, 75077, 50384, 50906, 50906
        };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        PurseUpdateResp purse =
                (PurseUpdateResp) context.writes.get(0).message;
        assertEquals(20L, purse.getItems(0).getValue());
        assertEquals(0L, purse.getItems(1).getValue());

        RewardResp reward = (RewardResp) context.writes.get(1).message;
        assertEquals(50907, reward.getOperationType());
        assertEquals(2, reward.getRewardItemVos(0).getItemKey());
        assertEquals(5L, reward.getRewardItemVos(0).getAmount());

        ReputationLvOnTaskRewardUpdateResp reputation =
                (ReputationLvOnTaskRewardUpdateResp)
                        context.writes.get(2).message;
        assertEquals(200004, reputation.getTaskId());

        ModuleNewOpenResp modules =
                (ModuleNewOpenResp) context.writes.get(3).message;
        assertEquals(2, modules.getOpensCount());
        assertEquals(4201, modules.getOpens(0));
        assertEquals(42011, modules.getOpens(1));

        PlayerCumulateLoginDaysResp firstLoginDays =
                (PlayerCumulateLoginDaysResp) context.writes.get(4).message;
        assertEquals(82, firstLoginDays.getSerializedSize());
        assertCumulateLoginDays(
                firstLoginDays,
                new int[] {
                        112, 4304, 4401, 102, 4007, 103,
                        1608, 4201, 4202, 107, 4411, 4415
                });
        assertEquals(
                ByteString.copyFrom(new byte[] {
                        0x08, 0x00, 0x10, 0x01,
                        0x28, 0x00, 0x30, 0x00
                }),
                context.writes.get(5).message);
        assertEquals(ByteString.EMPTY, context.writes.get(6).message);
        PlayerCumulateLoginDaysResp secondLoginDays =
                (PlayerCumulateLoginDaysResp) context.writes.get(7).message;
        assertEquals(90, secondLoginDays.getSerializedSize());
        assertCumulateLoginDays(
                secondLoginDays,
                new int[] {
                        102, 4007, 103, 1608, 4201, 4202,
                        107, 112, 4304, 4401, 4411, 42011, 4415
                });

        TaskUpdateResp rewarded =
                (TaskUpdateResp) context.writes.get(8).message;
        assertTask(rewarded, 0, 200004, TaskPhase.REWARDED, 1);

        TaskUpdateResp next =
                (TaskUpdateResp) context.writes.get(9).message;
        assertTask(next, 0, 200005, TaskPhase.PROGRESS, 0);
    }

    /** 任务 200005 领奖必须按抓包 idx 726-732 开启第三关任务 200006。 */
    @Test
    public void thirdStageEntryTaskRewardStartsTask200006() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200005))
                        .build());

        int[] protocolIds = {50651, 50406, 53702, 50906, 50906};
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        PurseUpdateResp purse =
                (PurseUpdateResp) context.writes.get(0).message;
        assertEquals(25L, purse.getItems(0).getValue());

        TaskUpdateResp rewarded =
                (TaskUpdateResp) context.writes.get(3).message;
        assertTask(rewarded, 0, 200005, TaskPhase.REWARDED, 1);

        TaskUpdateResp next =
                (TaskUpdateResp) context.writes.get(4).message;
        assertTask(next, 0, 200006, TaskPhase.PROGRESS, 0);
    }

    /** 第一关 Boss 奖励必须按抓包 idx 298-299 立即把纳戒能量更新为 3。 */
    @Test
    public void firstBossRewardUpdatesNewFightSkillEnergyBeforeReward() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100105)
                        .build());
        context.writes.clear();

        handler.killGuidanceMonster(
                context,
                com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                        .setMonsterId(handler.waveUnitIdAt(100_000_000_001L, 0))
                        .build());

        assertEquals(50402, context.writes.get(0).protocolId);
        PackUpdateResp pack =
                (PackUpdateResp) context.writes.get(0).message;
        assertEquals(61951, pack.getOperationType());
        assertEquals(1, pack.getPacks(0).getUpdateItems(0).getItemIndex());
        assertEquals(100200,
                pack.getPacks(0)
                        .getUpdateItems(0)
                        .getPackItem()
                        .getKey());
        assertEquals(3,
                pack.getPacks(0)
                        .getUpdateItems(0)
                        .getPackItem()
                        .getSize());

        assertEquals(50406, context.writes.get(1).protocolId);
        RewardResp reward = (RewardResp) context.writes.get(1).message;
        assertEquals(61951, reward.getOperationType());
        assertEquals(100200, reward.getRewardItemVos(0).getItemKey());
        assertEquals(3L, reward.getRewardItemVos(0).getAmount());
    }

    /** 第四关 Boss 击杀必须按抓包 idx 1386-1393 更新纳戒能量到 18 并完成主线 200008。 */
    @Test
    public void fourthBossRewardUpdatesEnergyTo18AndFinishes200008() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100405)
                        .build());
        context.writes.clear();

        handler.killGuidanceMonster(
                context,
                com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                        .setMonsterId(100_000_000_001L * 1000 + 405)
                        .build());

        int[] protocolIds = {
                50402, 50406, 50781, 50801, 50801,
                60751, 60751, 50906
        };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        PackUpdateResp pack =
                (PackUpdateResp) context.writes.get(0).message;
        assertEquals(100200,
                pack.getPacks(0).getUpdateItems(0).getPackItem().getKey());
        assertEquals(10,
                pack.getPacks(0).getUpdateItems(0).getPackItem().getSize());

        RewardResp reward = (RewardResp) context.writes.get(1).message;
        assertEquals(100200, reward.getRewardItemVos(0).getItemKey());
        assertEquals(10L, reward.getRewardItemVos(0).getAmount());

        TaskUpdateResp tasks =
                (TaskUpdateResp) context.writes.get(7).message;
        assertEquals(200008, tasks.getTaskVos(4).getTaskId().getTaskResourceId());
        assertEquals(TaskPhase.FINISHED, tasks.getTaskVos(4).getTaskPhase());
    }

    /** 第四关 Boss 任务 200008 领奖按抓包 idx 1445-1449 下发纳戒能量并开启 200009。 */
    @Test
    public void fourthBossTaskRewardGivesEnergyAndStarts200009() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(com.doupo.protocol.TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200008))
                        .build());

        int[] protocolIds = {50402, 50406, 53702, 50906, 50906};
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        PackUpdateResp pack = (PackUpdateResp) context.writes.get(0).message;
        assertEquals(100200,
                pack.getPacks(0).getUpdateItems(0).getPackItem().getKey());
        assertEquals(2,
                pack.getPacks(0).getUpdateItems(0).getPackItem().getSize());

        RewardResp reward = (RewardResp) context.writes.get(1).message;
        assertEquals(100200, reward.getRewardItemVos(0).getItemKey());
        assertEquals(2L, reward.getRewardItemVos(0).getAmount());

        TaskUpdateResp rewarded =
                (TaskUpdateResp) context.writes.get(3).message;
        assertTask(rewarded, 0, 200008, TaskPhase.REWARDED, 1);

        TaskUpdateResp next =
                (TaskUpdateResp) context.writes.get(4).message;
        assertTask(next, 0, 200009, TaskPhase.PROGRESS, 0);
    }

    /** 第五关 Boss 击杀必须按抓包 idx 1920 增加纳戒能量 20。 */
    @Test
    public void fifthBossRewardAddsTwentyEnergy() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100505)
                        .build());
        context.writes.clear();

        handler.killGuidanceMonster(
                context,
                com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                        .setMonsterId(100_000_000_001L * 1000 + 505)
                        .build());

        assertEquals(50402, context.writes.get(0).protocolId);
        PackUpdateResp pack =
                (PackUpdateResp) context.writes.get(0).message;
        assertEquals(100200,
                pack.getPacks(0).getUpdateItems(0).getPackItem().getKey());
        assertEquals(20,
                pack.getPacks(0).getUpdateItems(0).getPackItem().getSize());

        assertEquals(50406, context.writes.get(1).protocolId);
        RewardResp reward = (RewardResp) context.writes.get(1).message;
        assertEquals(100200, reward.getRewardItemVos(0).getItemKey());
        assertEquals(20L, reward.getRewardItemVos(0).getAmount());

        // 第五关 Boss 只刷新通用击杀进度，无主线任务（推进主地图）
        TaskUpdateResp tasks =
                (TaskUpdateResp) context.writes.get(7).message;
        assertEquals(4, tasks.getTaskVosCount());
        assertEquals(1104,
                tasks.getTaskVos(0).getTaskId().getTaskResourceId());
        assertEquals(TaskPhase.PROGRESS, tasks.getTaskVos(0).getTaskPhase());
    }

    /** 第五关 Boss 结算必须按抓包 idx 1953 推进到第六关。 */
    @Test
    public void fifthBossEndFightAdvancesToSixthChapter() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100505)
                        .build());
        handler.killGuidanceMonster(
                context,
                com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                        .setMonsterId(handler.waveUnitIdAt(playerId, 0))
                        .build());
        context.writes.clear();

        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10100505)
                        .build());

        MainMapPassChapterUpdateResp update = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 61952) {
                update = (MainMapPassChapterUpdateResp) write.message;
            }
        }
        assertTrue(update != null);
        assertEquals(10200101, update.getMainMapChapterId());
        assertEquals(11, update.getStageTime());
        assertEquals(13, update.getLastStageTime());
        assertEquals(10100501, update.getLoseBackId());
        assertEquals(10100505, update.getHistoryTopId());
        assertEquals(1, update.getChangeReason());
    }

    /** 第六关小怪的数量、模板、站位和属性必须匹配抓包 idx 1964-1966。 */
    @Test
    public void sixthChapterMonstersMatchCapturedWave() {
        List<SceneUnitVo> monsters = enterChapter(10200101, 22.93f);

        assertEquals(3, monsters.size());
        assertEquals(1000601L,
                monsters.get(0).getSceneMonsterVo().getTemplateId());
        assertEquals(1000602L,
                monsters.get(1).getSceneMonsterVo().getTemplateId());
        assertEquals(1000603L,
                monsters.get(2).getSceneMonsterVo().getTemplateId());
        assertEquals(-2f, monsters.get(0).getBaseInfoVo().getX(), 0.0001f);
        assertEquals(2f, monsters.get(1).getBaseInfoVo().getX(), 0.0001f);
        assertEquals(26.93f, monsters.get(0).getBaseInfoVo().getZ(), 0.0001f);
        assertEquals(38.93f, monsters.get(2).getBaseInfoVo().getZ(), 0.0001f);
        assertEquals(275, attributeOf(monsters.get(0), 101001), 0.0001);
        assertEquals(3450, attributeOf(monsters.get(0), 103001), 0.0001);
        assertEquals(287.5, attributeOf(monsters.get(2), 101001), 0.0001);
        assertEquals(3000, attributeOf(monsters.get(2), 103001), 0.0001);
    }

    /** 第六关第二、三波必须按抓包 idx 2183/2373 接受预加载并生成怪物。 */
    @Test
    public void sixthChapterFollowUpWavesAreSupported() {
        List<SceneUnitVo> second = enterChapter(10200102, 67.7f);
        List<SceneUnitVo> third = enterChapter(10200103, 104.6f);

        assertEquals(3, second.size());
        assertEquals(3, third.size());
        assertEquals(71.7f,
                second.get(0).getBaseInfoVo().getZ(), 0.0001f);
        assertEquals(108.6f,
                third.get(0).getBaseInfoVo().getZ(), 0.0001f);
        assertEquals(10200102,
                ChapterConfig.get(10200101).getNextChapterId());
        assertEquals(10200103,
                ChapterConfig.get(10200102).getNextChapterId());
        assertEquals(10200101,
                ChapterConfig.get(10200103).getNextChapterId());
    }

    /** 第六关下一波预加载不得覆盖当前波战斗会话。 */
    @Test
    public void sixthChapterPreloadKeepsCurrentCombatSession() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200101)
                        .setZ(22.93f)
                        .build());
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200102)
                        .setZ(67.7f)
                        .build());

        assertEquals(10200101,
                CombatSessionRegistry.get(playerId).getChapterId());
        CombatSessionRegistry.clear(playerId);
    }

    /**
     * 预加载的下一波必须拿全新的怪物 ID，并在正式进入时原样复用。
     *
     * <p>抓包 idx 1974 预加载第二波，官服 idx 1976-1978 下发三只全新 ID 的怪；
     * idx 2183 玩家真正走进第二波时官服<b>不再下发 50756</b>，战斗里用的就是
     * 预加载那三只。ID 重叠会让客户端复用旧对象，旧波的遗忘也会删掉新怪。
     */
    @Test
    public void preloadedWaveKeepsFreshIdsAndIsReusedOnEntry() {
        long playerId = 100_000_000_301L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200101)
                        .setZ(22.93f)
                        .build());
        List<Long> firstWaveIds = monsterIdsOf(context);
        context.writes.clear();

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200102)
                        .setZ(67.7f)
                        .build());
        List<Long> preloadedIds = monsterIdsOf(context);

        assertEquals(3, firstWaveIds.size());
        assertEquals(3, preloadedIds.size());
        assertFalse(preloadedIds.stream().anyMatch(firstWaveIds::contains));

        // 打完第一波，客户端再报一次第二波：这时才切会话，且不重发快照。
        for (long monsterId : firstWaveIds) {
            handler.killGuidanceMonster(
                    context,
                    com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                            .setMonsterId(monsterId)
                            .build());
        }
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10200101)
                        .build());
        context.writes.clear();
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200102)
                        .setZ(67.7f)
                        .build());

        assertTrue(monsterIdsOf(context).isEmpty());
        CombatSession session = CombatSessionRegistry.get(playerId);
        assertEquals(10200102, session.getChapterId());
        for (long preloadedId : preloadedIds) {
            assertTrue(session.getMonster(preloadedId) != null);
        }
        CombatSessionRegistry.clear(playerId);
    }

    /**
     * 同一波被重复请求时不得重发快照、不得把怪拉回满血、不得换战斗会话。
     *
     * <p>抓包 idx 1960 首次进第一波，idx 1973 客户端又报了一次同样的
     * 10200101，官服只回一条 50801（idx 1975），没有 50756、也没有重开战斗。
     */
    @Test
    public void rejoinSameWaveKeepsMonsterHpAndCombatSession() {
        long playerId = 100_000_000_302L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        GuidanceMainMapMonsterEnterReq enter =
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200101)
                        .setZ(22.93f)
                        .build();

        handler.enterGuidanceMainMapMonster(context, enter);
        CombatSession session = CombatSessionRegistry.get(playerId);
        long firstMonsterId = handler.waveUnitIdAt(playerId, 0);
        session.getMonster(firstMonsterId).applyDamage(1000);
        double wounded = session.getMonster(firstMonsterId).getCurrentHp();
        context.writes.clear();

        handler.enterGuidanceMainMapMonster(context, enter);

        assertEquals(1, context.writes.size());
        assertEquals(50801, context.writes.get(0).protocolId);
        assertTrue(session == CombatSessionRegistry.get(playerId));
        assertEquals(firstMonsterId, handler.waveUnitIdAt(playerId, 0));
        assertEquals(wounded,
                session.getMonster(firstMonsterId).getCurrentHp(),
                0.0001);
        CombatSessionRegistry.clear(playerId);
    }

    /**
     * 家族试炼回来后，客户端仍报当前波章节号，但战斗点 z 已经回到上一波。
     *
     * <p>本服 11:22:26 日志：第二波正在 67.7 开打，试炼剧情 20010 结束后
     * 客户端带着 10200102 + z=22.93 再发 61973。旧 rejoin 只看章节号，
     * 不发快照、也不遗忘 67.7 的怪，人站在空地、怪还在远处。
     * 战斗点变了就必须忘掉旧实例，按新坐标重新发一批。
     */
    @Test
    public void familyTrialReturnAtDifferentZRespawnsCurrentChapter() {
        long playerId = 100_000_000_306L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200101)
                        .setZ(22.93f)
                        .build());
        for (int i = 0; i < 3; i++) {
            handler.killGuidanceMonster(
                    context,
                    com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                            .setMonsterId(handler.waveUnitIdAt(playerId, i))
                            .build());
        }
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10200101)
                        .build());
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200102)
                        .setZ(67.7f)
                        .build());
        CombatSession midFight = CombatSessionRegistry.get(playerId);
        long oldFirst = handler.waveUnitIdAt(playerId, 0);
        midFight.getMonster(oldFirst).applyDamage(1000);
        context.writes.clear();

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200102)
                        .setZ(22.93f)
                        .build());

        List<Long> forgotten = new ArrayList<>();
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 50757) {
                forgotten.addAll(((SceneForgetVisibleResp) write.message)
                        .getForgetIdsList());
            }
        }
        List<Long> spawned = monsterIdsOf(context);
        long newFirst = handler.waveUnitIdAt(playerId, 0);
        CombatSession resumed = CombatSessionRegistry.get(playerId);

        assertTrue(forgotten.contains(oldFirst));
        assertEquals(3, spawned.size());
        assertFalse(spawned.contains(oldFirst));
        assertEquals(newFirst, spawned.get(0).longValue());
        assertTrue(midFight != resumed);
        assertEquals(10200102, resumed.getChapterId());
        assertEquals(resumed.getMonster(newFirst).getMaxHp(),
                resumed.getMonster(newFirst).getCurrentHp(),
                0.0001);
        assertEquals(26.93f,
                spawnedZ(context, newFirst),
                0.01f);
        CombatSessionRegistry.clear(playerId);
    }

    /**
     * 当前波还没打完就预加载下一波，随后遗忘旧怪时不能碰到新怪。
     *
     * <p>这是旧发号方式最直接的后果：第一波 201-203、第二波 202-204，
     * 杀掉第一波第二只后下发的 50757 会把客户端里已经变成第二波的对象删掉。
     */
    @Test
    public void forgettingCurrentWaveNeverTouchesPreloadedWave() {
        long playerId = 100_000_000_303L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200101)
                        .setZ(22.93f)
                        .build());
        long secondMonsterId = handler.waveUnitIdAt(playerId, 1);
        context.writes.clear();
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200102)
                        .setZ(67.7f)
                        .build());
        List<Long> preloadedIds = monsterIdsOf(context);
        context.writes.clear();

        handler.killGuidanceMonster(
                context,
                com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                        .setMonsterId(secondMonsterId)
                        .build());

        List<Long> forgotten = new ArrayList<>();
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 50757) {
                forgotten.addAll(((SceneForgetVisibleResp) write.message)
                        .getForgetIdsList());
            }
        }
        assertEquals(1, forgotten.size());
        assertEquals(secondMonsterId, forgotten.get(0).longValue());
        assertFalse(preloadedIds.contains(forgotten.get(0)));
        CombatSessionRegistry.clear(playerId);
    }

    /** 三波循环回到第一波时必须换一批 ID，不能和上一轮的怪撞号。 */
    @Test
    public void loopedWavesNeverReuseMonsterIds() {
        long playerId = 100_000_000_304L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        int[] waves = { 10200101, 10200102, 10200103, 10200101 };
        List<Long> seen = new ArrayList<>();

        for (int wave : waves) {
            handler.enterGuidanceMainMapMonster(
                    context,
                    GuidanceMainMapMonsterEnterReq.newBuilder()
                            .setChapterId(wave)
                            .setZ(22.93f)
                            .build());
            for (int i = 0; i < 3; i++) {
                long monsterId = handler.waveUnitIdAt(playerId, i);
                assertFalse(seen.contains(monsterId));
                seen.add(monsterId);
                handler.killGuidanceMonster(
                        context,
                        com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                                .setMonsterId(monsterId)
                                .build());
            }
            handler.endMainMapFight(
                    context,
                    com.doupo.protocol.MainMapEndFightReq.newBuilder()
                            .setMainMapChapterId(wave)
                            .build());
        }

        assertEquals(12, seen.size());
        CombatSessionRegistry.clear(playerId);
    }

    /** 上一波的 CombatTick 不能驱动新一波的会话。 */
    @Test
    public void staleCombatTickDoesNotDriveNextWaveSession() {
        long playerId = 100_000_000_305L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200101)
                        .setZ(22.93f)
                        .build());
        CombatSession first = CombatSessionRegistry.get(playerId);
        for (int i = 0; i < 3; i++) {
            handler.killGuidanceMonster(
                    context,
                    com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                            .setMonsterId(handler.waveUnitIdAt(playerId, i))
                            .build());
        }
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10200101)
                        .build());
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200102)
                        .setZ(67.7f)
                        .build());
        CombatSession second = CombatSessionRegistry.get(playerId);
        // 站到第二波第一只怪身上，新会话被推进时必然打出普攻。
        handler.startGuidanceMainMapFight(
                context,
                com.doupo.protocol.GuidanceMainMapStartFightReq.newBuilder()
                        .setX(-2)
                        .setZ(71.7f)
                        .build());
        context.writes.clear();

        com.doupo.server.module.combat.CombatTickProcessor.onTick(
                null,
                new com.doupo.server.module.combat.CombatTick(context, first));

        assertTrue(first != second);
        assertFalse(first.isActive());
        assertTrue(context.writes.isEmpty());

        com.doupo.server.module.combat.CombatTickProcessor.onTick(
                null,
                new com.doupo.server.module.combat.CombatTick(context, second));

        assertTrue(context.writes.stream()
                .anyMatch(write -> write.protocolId == 50762));
        CombatSessionRegistry.clear(playerId);
    }

    /** 第六关首次出手必须先追击，抵达后才结算伤害并停止，依据抓包 idx 1980-2001。 */
    @Test
    public void sixthChapterUseSkillCompletesRealmTaskAndDamagesMonster() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200101)
                        .setZ(22.93f)
                        .build());
        long targetId = handler.waveUnitIdAt(playerId, 0);
        context.writes.clear();

        GuidanceMainMapUseSkillReq request =
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10110710101L)
                        .setTargetId(targetId)
                        .setX(0)
                        .setZ(18.902935f)
                        .setTime(1_787_282_904_897L)
                        .setTx(-2)
                        .setTz(26.93f)
                        .build();
        handler.useGuidanceMainMapSkill(context, request);

        assertEquals(61972,
                GuidanceMainMapUseSkillReq.Proto.ID_VALUE);
        assertEquals(7, context.writes.size());
        assertEquals(50906, context.writes.get(0).protocolId);
        assertTask((TaskUpdateResp) context.writes.get(0).message,
                0, 200012, TaskPhase.FINISHED, 1);
        assertEquals(50793, context.writes.get(1).protocolId);
        assertEquals(50793, context.writes.get(2).protocolId);
        assertEquals(50762, context.writes.get(3).protocolId);
        // 怒气更新（官服 MpResp 50798）：普通攻击命中 +2（抓包 idx 2008→2016）。
        assertEquals(50798, context.writes.get(4).protocolId);
        MpResp mpResp = (MpResp) context.writes.get(4).message;
        assertEquals(playerId * 1000 + 1, mpResp.getId());
        assertEquals(2.0, mpResp.getMp(), 0.001);
        assertEquals(50766, context.writes.get(5).protocolId);
        assertEquals(50832, context.writes.get(6).protocolId);

        assertFalse(context.writes.stream()
                .anyMatch(write -> write.protocolId == 50763));

        UseSkillResp use = (UseSkillResp) context.writes.get(3).message;
        assertEquals(10110710101L, use.getSkillId());
        assertEquals(targetId, use.getTargetId());
        // 对齐官服抓包：skillNo 是每次施放的技能实例编号（非 0），
        // target 坐标取目标位置而非玩家位置，angle/failCode/playSkillBlackMask 齐全。
        assertTrue(use.getSkillNo() != 0);
        assertEquals(request.getTx(), use.getTargetX(), 0.001f);
        assertEquals(request.getTz(), use.getTargetZ(), 0.001f);
        assertTrue(Math.abs(use.getAngle()) > 0.001f);
        assertEquals(0, use.getFailCode());
        assertFalse(use.getPlaySkillBlackMask());

        ChaseStartResp chase =
                (ChaseStartResp) context.writes.get(6).message;
        assertEquals(playerId * 1000 + 1, chase.getFighterId());
        assertEquals(10110710101L, chase.getSkillId());
        assertTrue(chase.getTz() > 18.902935f);
        assertEquals(2.165f,
                Math.hypot(chase.getTx() + 2, chase.getTz() - 26.93f),
                0.01f);

        handler.completeGuidanceMainMapChase(
                context,
                request,
                10200101,
                chase.getFighterId(),
                chase.getTx(),
                chase.getTy(),
                chase.getTz(),
                true);

        assertEquals(50763, context.writes.get(7).protocolId);
        assertEquals(50793, context.writes.get(8).protocolId);
        SkillActionResp action =
                (SkillActionResp) context.writes.get(7).message;
        assertEquals(261,
                action.getActionList(0).getDamageVo().getDamage(),
                0.0001);
        assertEquals(3189,
                action.getActionList(1)
                        .getAttributeActionVo()
                        .getAttrList(0)
                        .getValue(),
                0.0001);

        handler.stopGuidanceMainMapChase(
                context,
                10200101,
                chase.getFighterId(),
                chase.getSkillId(),
                chase.getTx(),
                chase.getTy(),
                chase.getTz());
        assertEquals(50834, context.writes.get(9).protocolId);
        ChaseStopResp stop =
                (ChaseStopResp) context.writes.get(9).message;
        assertEquals(chase.getTx(), stop.getX(), 0.0001f);
        assertEquals(chase.getTz(), stop.getZ(), 0.0001f);
        CombatSessionRegistry.clear(playerId);
    }

    /** 第六关遇敌提示必须在首次出手后按抓包 idx 1983/1984/2005 下发。 */
    @Test
    public void sixthChapterWarnsAllThreeMonsters() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200101)
                        .setZ(22.93f)
                        .build());
        long firstMonsterId = handler.waveUnitIdAt(playerId, 0);

        assertEquals(0,
                context.writes.stream()
                        .filter(write -> write.protocolId == 50793)
                        .count());
        GuidanceMainMapUseSkillReq request =
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10110710101L)
                        .setTargetId(firstMonsterId + 1)
                        .setX(0)
                        .setZ(18.902935f)
                        .setTx(2)
                        .setTz(26.93f)
                        .build();
        handler.useGuidanceMainMapSkill(context, request);
        ChaseStartResp chase = context.writes.stream()
                .filter(write -> write.protocolId == 50832)
                .map(write -> (ChaseStartResp) write.message)
                .findFirst()
                .get();
        handler.completeGuidanceMainMapChase(
                context,
                request,
                10200101,
                chase.getFighterId(),
                chase.getTx(),
                chase.getTy(),
                chase.getTz(),
                true);

        List<com.doupo.protocol.WarningResp> warnings = new ArrayList<>();
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 50793) {
                warnings.add((com.doupo.protocol.WarningResp) write.message);
            }
        }
        assertEquals(3, warnings.size());
        assertEquals(firstMonsterId, warnings.get(0).getId());
        assertEquals(firstMonsterId + 1, warnings.get(1).getId());
        assertEquals(firstMonsterId + 2, warnings.get(2).getId());

        // 感叹号后必须驱动后排怪本人走近，而不是只让主角 Chase。
        context.writes.clear();
        CombatSession session = CombatSessionRegistry.get(playerId);
        com.doupo.server.module.combat.CombatTickProcessor.onTick(null,
                new com.doupo.server.module.combat.CombatTick(context, session));
        assertTrue(context.writes.stream().anyMatch(write -> write.protocolId == 50761
                && ((com.doupo.protocol.MoveResp) write.message).getMove().getId()
                        == firstMonsterId + 2
                && ((com.doupo.protocol.MoveResp) write.message).getMove().getType() == 1));
        CombatSessionRegistry.clear(playerId);
    }

    @Test
    public void sixthLoopedWavesActivateAllThreeMonstersEvenWhenOpeningWithActiveSkill() {
        long playerId = 100_000_006_801L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        int[] chapters = {10200101, 10200102, 10200103, 10200101};
        for (int wave = 0; wave < chapters.length; wave++) {
            float center = 22.93f + wave * 40f;
            handler.enterGuidanceMainMapMonster(context, GuidanceMainMapMonsterEnterReq.newBuilder()
                    .setChapterId(chapters[wave]).setY(7f).setZ(center).build());
            CombatSession session = CombatSessionRegistry.get(playerId);
            assertEquals(chapters[wave], session.getChapterId());
            context.writes.clear();
            // 吹火掌分支会提前返回，仍必须激活后排；距离 10 外本次不命中前排。
            handler.useGuidanceMainMapSkill(context, GuidanceMainMapUseSkillReq.newBuilder()
                    .setSkillId(10150210100L).setTargetId(handler.waveUnitIdAt(playerId, 0))
                    .setY(7f).setZ(center - 7f).setTy(7f).setTz(center - 7f).build());
            assertEquals(3L, context.writes.stream().filter(write -> write.protocolId == 50793).count());
            java.util.List<com.doupo.protocol.MoveResp> moves = session.advanceMonsterMovement(0);
            assertEquals(3, moves.size());
            for (int i = 0; i < moves.size(); i++) {
                assertEquals(handler.waveUnitIdAt(playerId, i), moves.get(i).getMove().getId());
                assertEquals(1, moves.get(i).getMove().getType());
                assertEquals(7f, moves.get(i).getMove().getY(), 0.0001f);
            }
            for (int i = 0; i < 3; i++) {
                handler.killGuidanceMonster(context, com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                        .setMonsterId(handler.waveUnitIdAt(playerId, i)).build());
            }
            handler.endMainMapFight(context, com.doupo.protocol.MainMapEndFightReq.newBuilder()
                    .setMainMapChapterId(chapters[wave]).build());
        }
        CombatSessionRegistry.clear(playerId);
    }

    @Test
    public void sixthTickWritesRearMonsterStopBeforeItsAttackAndDamage() {
        long playerId = 100_000_006_802L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(context, GuidanceMainMapMonsterEnterReq.newBuilder()
                .setChapterId(10200101).setZ(22.93f).build());
        CombatSession session = CombatSessionRegistry.get(playerId);
        long rearId = handler.waveUnitIdAt(playerId, 2);
        session.updatePlayerPosition(0, 0, 22.93f);
        session.alertMonster(rearId);
        session.advanceMonsterMovement(System.currentTimeMillis() - 10000L);
        context.writes.clear();
        com.doupo.server.module.combat.CombatTickProcessor.onTick(null,
                new com.doupo.server.module.combat.CombatTick(context, session));
        int stopIndex = -1;
        int attackIndex = -1;
        for (int i = 0; i < context.writes.size(); i++) {
            RecordedWrite write = context.writes.get(i);
            if (write.protocolId == 50761) {
                com.doupo.protocol.MoveResp move = (com.doupo.protocol.MoveResp) write.message;
                if (move.getMove().getId() == rearId && move.getMove().getType() == 4) {
                    stopIndex = i;
                }
            }
            if (write.protocolId == 50762 && ((UseSkillResp) write.message).getAttackId() == rearId) {
                attackIndex = i;
                assertEquals(220310110101L, ((UseSkillResp) write.message).getSkillId());
            }
        }
        assertTrue(stopIndex >= 0 && attackIndex > stopIndex);
        assertEquals(50763, context.writes.get(attackIndex + 1).protocolId);
        assertTrue(session.getPlayer().getCurrentHp() < session.getPlayer().getMaxHp());
        CombatSessionRegistry.clear(playerId);
    }

    /** 第六关每一波首次远距离普攻都必须重新下发追击，不能只处理第一波。 */
    @Test
    public void sixthChapterSecondWaveStartsChaseAgain() {
        long playerId = 100_000_000_005L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200101)
                        .setZ(22.93f)
                        .build());
        handler.useGuidanceMainMapSkill(
                context,
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10110710101L)
                        .setTargetId(handler.waveUnitIdAt(playerId, 0))
                        .setX(0)
                        .setZ(18.902935f)
                        .setTx(-2)
                        .setTz(26.93f)
                        .build());

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200102)
                        .setZ(67.7f)
                        .build());
        context.writes.clear();
        handler.useGuidanceMainMapSkill(
                context,
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10110710101L)
                        .setTargetId(handler.waveUnitIdAt(playerId, 1))
                        .setX(0)
                        .setZ(63.66718f)
                        .setTx(-2)
                        .setTz(71.7f)
                        .build());

        assertTrue(context.writes.stream()
                .anyMatch(write -> write.protocolId == 50832));
        CombatSessionRegistry.clear(playerId);
    }

    /**
     * 第三波远怪在战斗点 +16，跳点在 +10.1。官服靠怪物走近，追击只到 109.9；
     * 未走近时不能把玩家追过 114.7，否则会走路爬上浮岛。
     */
    @Test
    public void thirdWaveChaseStopsBeforeJumpPoint() {
        long playerId = 100_000_000_006L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200103)
                        .setZ(104.6f)
                        .build());
        long farMonsterId = handler.waveUnitIdAt(playerId, 2);
        context.writes.clear();
        handler.useGuidanceMainMapSkill(
                context,
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10110710101L)
                        .setTargetId(farMonsterId)
                        .setX(0)
                        .setZ(100.5f)
                        .setTx(0)
                        .setTz(120.6f)
                        .build());

        ChaseStartResp chase = context.writes.stream()
                .filter(write -> write.protocolId == 50832)
                .map(write -> (ChaseStartResp) write.message)
                .findFirst()
                .orElse(null);
        assertTrue(chase != null);
        assertTrue(chase.getTz() <= 112.6f + 0.01f);
        assertTrue(chase.getTz() < 114.7f);
        CombatSessionRegistry.clear(playerId);
    }

    /**
     * 第八关第三波同样紧挨跳台；追击远怪不能把玩家送过跳点。
     */
    @Test
    public void eighthChapterThirdWaveChaseStopsBeforeJumpPoint() {
        long playerId = 100_000_000_007L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200303)
                        .setZ(984.6f)
                        .build());
        long farMonsterId = handler.waveUnitIdAt(playerId, 2);
        context.writes.clear();
        handler.useGuidanceMainMapSkill(
                context,
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10110710101L)
                        .setTargetId(farMonsterId)
                        .setX(0)
                        .setZ(980.5f)
                        .setTx(0)
                        .setTz(1000.6f)
                        .build());

        ChaseStartResp chase = context.writes.stream()
                .filter(write -> write.protocolId == 50832)
                .map(write -> (ChaseStartResp) write.message)
                .findFirst()
                .orElse(null);
        assertTrue(chase != null);
        assertTrue(chase.getTz() <= 992.6f + 0.01f);
        assertTrue(chase.getTz() < 994.7f);
        CombatSessionRegistry.clear(playerId);
    }

    /** 先杀第二只时必须切到第一只存活怪，不能重新锁回刚死目标。 */
    @Test
    public void sixthChapterSwitchesToNextAliveMonsterOutOfOrder() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200101)
                        .setZ(22.93f)
                        .build());
        long firstMonsterId = handler.waveUnitIdAt(playerId, 0);
        long secondMonsterId = handler.waveUnitIdAt(playerId, 1);
        GuidanceMainMapUseSkillReq ultimate =
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10150510101L)
                        .setTargetId(secondMonsterId)
                        .setTz(26.93f)
                        .build();
        handler.useGuidanceMainMapSkill(context, ultimate);
        context.writes.clear();
        handler.useGuidanceMainMapSkill(context, ultimate);

        // 命中后还有独立的技能结束包，切目标与遗忘仍须保持相邻顺序。
        SkillActionResp end = (SkillActionResp)
                context.writes.get(context.writes.size() - 1).message;
        assertEquals(0, end.getActionListCount());
        context.writes.remove(context.writes.size() - 1);
        assertEquals(50793,
                context.writes.get(context.writes.size() - 2).protocolId);
        com.doupo.protocol.WarningResp warning =
                (com.doupo.protocol.WarningResp)
                        context.writes.get(context.writes.size() - 2).message;
        assertEquals(firstMonsterId, warning.getId());
        assertEquals(50757,
                context.writes.get(context.writes.size() - 1).protocolId);
        com.doupo.protocol.SceneForgetVisibleResp forget =
                (com.doupo.protocol.SceneForgetVisibleResp)
                        context.writes.get(context.writes.size() - 1).message;
        assertEquals(secondMonsterId, forget.getForgetIds(0));
        assertFalse(CombatSessionRegistry.get(playerId)
                .getMonster(secondMonsterId).isAlive());
        assertTrue(CombatSessionRegistry.get(playerId)
                .getMonster(firstMonsterId).isAlive());
        CombatSessionRegistry.clear(playerId);
    }

    /** 第六关 Boss 必须匹配抓包 idx 5007。 */
    @Test
    public void sixthBossMatchesCapturedBoss() {
        List<SceneUnitVo> monsters = enterChapter(10200105, 1080.9f);

        assertEquals(1, monsters.size());
        SceneUnitVo boss = monsters.get(0);
        assertEquals(100003, boss.getSceneMonsterVo().getMonsterId());
        assertEquals(1000640L, boss.getSceneMonsterVo().getTemplateId());
        assertEquals(2300, attributeOf(boss, 101001), 0.0001);
        assertEquals(17000, attributeOf(boss, 103001), 0.0001);
        assertEquals(100200, boss.getSceneMonsterVo().getMainMapReward(0).getItemKey());
        assertEquals(5, boss.getSceneMonsterVo().getMainMapReward(0).getAmount());
    }

    /** 第六关平台跳跃前后的协议必须匹配抓包 idx 4995-5007。 */
    @Test
    public void sixthBossPlatformTransitionAcceptsCapturedEmptyEnter() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200103)
                        .setY(7.042f)
                        .setZ(640.9f)
                        .build());
        long monsterId = handler.waveUnitIdAt(playerId, 0);
        for (int i = 0; i < 3; i++) {
            handler.killGuidanceMonster(
                    context,
                    com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                            .setMonsterId(monsterId + i)
                            .build());
        }
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10200103)
                        .build());
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200101)
                        .setY(7.025f)
                        .setZ(683f)
                        .build());

        context.writes.clear();
        handler.resetGuidanceMainMap(
                context,
                GuidanceMainMapResetReq.newBuilder()
                        .setEnterNext(true)
                        .build());

        MainMapPassChapterUpdateResp update =
                (MainMapPassChapterUpdateResp) context.writes.get(0).message;
        assertEquals(61952, context.writes.get(0).protocolId);
        assertEquals(10200105, update.getMainMapChapterId());
        assertEquals(11, update.getStageTime());
        assertEquals(13, update.getLastStageTime());
        assertEquals(10200103, update.getHistoryTopId());
        assertEquals(0, update.getChangeReason());
        assertEquals(1, update.getFromResetReq());
        assertEquals(50757, context.writes.get(1).protocolId);
        assertEquals(50757, context.writes.get(2).protocolId);
        assertEquals(50757, context.writes.get(3).protocolId);
        assertTrue(CombatSessionRegistry.get(playerId) == null);

        context.writes.clear();
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.getDefaultInstance());

        SceneUpdateVisibleResp visible =
                (SceneUpdateVisibleResp) context.writes.get(1).message;
        SceneUnitVo boss = visible.getVisibleList(0);
        assertEquals(10200105,
                boss.getSceneMonsterVo().getMainMapId());
        assertEquals(7.042f,
                boss.getBaseInfoVo().getY(), 0.0001f);
        assertEquals(1080.9f,
                boss.getBaseInfoVo().getZ(), 0.0001f);
        assertEquals(10200105,
                CombatSessionRegistry.get(playerId).getChapterId());
        CombatSessionRegistry.clear(playerId);
    }

    /** 第六关跳台段的空章节请求必须进入下一轮 01（抓包 idx 4364-4368）。 */
    @Test
    public void sixthPlatformEmptyEnterUsesNextWave() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200103)
                        .setZ(507.7f)
                        .build());
        long monsterId = handler.waveUnitIdAt(playerId, 0);
        for (int i = 0; i < 3; i++) {
            handler.killGuidanceMonster(
                    context,
                    com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                            .setMonsterId(monsterId + i)
                            .build());
        }
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10200103)
                        .build());

        context.writes.clear();
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setZ(544.6f)
                        .build());

        SceneUpdateVisibleResp visible = firstSpawnedMonster(context);
        assertEquals(10200101,
                visible.getVisibleList(0).getSceneMonsterVo().getMainMapId());
        assertEquals(548.6f,
                visible.getVisibleList(0).getBaseInfoVo().getZ(), 0.0001f);
        CombatSessionRegistry.clear(playerId);
    }

    /** Boss 解锁后，后续跳台波仍须保持 10200105（抓包 idx 2558-4988）。 */
    @Test
    public void sixthBossUnlockPersistsAcrossPlatformWaves() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200103)
                        .setZ(243f)
                        .build());
        for (int i = 0; i < 3; i++) {
            handler.killGuidanceMonster(
                    context,
                    com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                            .setMonsterId(handler.waveUnitIdAt(playerId, i))
                            .build());
        }
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10200103)
                        .build());
        context.writes.clear();
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200101)
                        .setY(7.025f)
                        .setZ(280.4f)
                        .build());
        for (int i = 0; i < 3; i++) {
            handler.killGuidanceMonster(
                    context,
                    com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                            .setMonsterId(handler.waveUnitIdAt(playerId, i))
                            .build());
        }
        context.writes.clear();
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10200101)
                        .build());

        MainMapPassChapterUpdateResp update =
                (MainMapPassChapterUpdateResp) context.writes.stream()
                        .filter(write -> write.protocolId == 61952)
                        .findFirst()
                        .get()
                        .message;
        assertEquals(10200105, update.getNextChallengeId());
        assertFalse(update.getHasReward());
        assertEquals(10200103, update.getHistoryTopId());
        assertEquals(4, update.getChangeReason());
        CombatSessionRegistry.clear(playerId);
    }

    /** 第七关第三波打完必须解锁本关 Boss（10200205），并允许玩家进入。 */
    @Test
    public void seventhBossUnlocksAfterThirdWave() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200203)
                        .build());
        long monsterId = handler.waveUnitIdAt(playerId, 0);
        for (int i = 0; i < 6; i++) {
            handler.killGuidanceMonster(
                    context,
                    com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                            .setMonsterId(monsterId + i)
                            .build());
        }
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10200203)
                        .build());
        // 第三波打完客户端切回第一波。
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200201)
                        .build());

        context.writes.clear();
        handler.resetGuidanceMainMap(
                context,
                GuidanceMainMapResetReq.newBuilder()
                        .setEnterNext(true)
                        .build());

        MainMapPassChapterUpdateResp update =
                (MainMapPassChapterUpdateResp) context.writes.get(0).message;
        assertEquals(61952, context.writes.get(0).protocolId);
        assertEquals(10200205, update.getMainMapChapterId());
        assertEquals(10200203, update.getHistoryTopId());
        assertEquals(0, update.getChangeReason());
        assertEquals(1, update.getFromResetReq());
    }

    /** 第六关 Boss 必须能被玩家攻击，伤害与抓包 idx 5020 一致。 */
    @Test
    public void sixthBossAcceptsPlayerSkill() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200105)
                        .build());
        long bossId = handler.waveUnitIdAt(playerId, 0);
        context.writes.clear();

        handler.useGuidanceMainMapSkill(
                context,
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10110710101L)
                        .setTargetId(bossId)
                        .build());

        // 首次出手先完成任务 200012，再回放技能与 Boss 扣血。
        assertEquals(5, context.writes.size());
        assertEquals(50906, context.writes.get(0).protocolId);
        assertTask((TaskUpdateResp) context.writes.get(0).message,
                0, 200012, TaskPhase.FINISHED, 1);
        assertEquals(50762, context.writes.get(1).protocolId);
        // 怒气更新（官服 MpResp 50798）：普通攻击命中 +2。
        assertEquals(50798, context.writes.get(2).protocolId);
        assertEquals(2.0,
                ((MpResp) context.writes.get(2).message).getMp(),
                0.001);
        assertEquals(50766, context.writes.get(3).protocolId);
        assertEquals(50763, context.writes.get(4).protocolId);

        SkillActionResp action =
                (SkillActionResp) context.writes.get(4).message;
        assertEquals(261,
                action.getActionList(0).getDamageVo().getDamage(), 0.0001);
        assertEquals(16739,
                action.getActionList(1)
                        .getAttributeActionVo()
                        .getAttrList(0)
                        .getValue(), 0.0001);
        CombatSessionRegistry.clear(playerId);
    }

    /** 进第七关不能当作 200013 的触发点；该任务在领取 200012 后出现。 */
    @Test
    public void seventhChapterEntryDoesNotStartTask200013() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200201)
                        .build());

        assertFalse(context.writes.stream()
                .filter(write -> write.protocolId == 50906)
                .map(write -> write.message)
                .filter(m -> m instanceof TaskUpdateResp)
                .map(m -> (TaskUpdateResp) m)
                .anyMatch(resp -> containsTask(
                        resp, 200013, TaskPhase.PROGRESS)
                        || containsTask(
                                resp, 200013, TaskPhase.FINISHED)));
        CombatSessionRegistry.clear(playerId);
    }

    /** 第七关杀怪只累计通用击杀，不能完成 200013。 */
    @Test
    public void seventhChapterKillsDoNotFinishTask200013() {
        ChapterConfig.Chapter chapter = ChapterConfig.get(10200201);

        TaskUpdateResp resp = SceneHandler.killTaskUpdate(
                chapter,
                chapter.getMonsterCount());

        for (com.doupo.protocol.TaskVo task : resp.getTaskVosList()) {
            int taskId = task.getTaskId().getTaskResourceId();
            assertTrue(taskId != 200013);
            assertTrue(taskId != 200014);
            assertTrue(taskId != 200015);
        }
    }

    /** 第七关第一波必须匹配抓包：6 只怪，攻击 440、气血 4025。 */
    @Test
    public void seventhChapterFirstWaveMatchesCapturedMonsters() {
        List<SceneUnitVo> monsters = enterChapter(10200201, 22.93f);

        assertEquals(6, monsters.size());
        assertEquals(1000701L,
                monsters.get(0).getSceneMonsterVo().getTemplateId());
        assertEquals(440, attributeOf(monsters.get(0), 101001), 0.0001);
        assertEquals(4025, attributeOf(monsters.get(0), 103001), 0.0001);
        assertEquals(1000706L,
                monsters.get(5).getSceneMonsterVo().getTemplateId());
        assertEquals(121001,
                monsters.get(0).getSceneMonsterVo().getMonsterId());
        assertEquals(121002,
                monsters.get(5).getSceneMonsterVo().getMonsterId());
    }

    /** 第七关 Boss 必须匹配抓包：攻击 1800、气血 20000、掉落 100200 x5。 */
    @Test
    public void seventhBossMatchesCapturedBoss() {
        List<SceneUnitVo> monsters = enterChapter(10200205, 415.3f);

        assertEquals(1, monsters.size());
        SceneUnitVo boss = monsters.get(0);
        assertEquals(100002, boss.getSceneMonsterVo().getMonsterId());
        assertEquals(1000740L, boss.getSceneMonsterVo().getTemplateId());
        assertEquals(1800, attributeOf(boss, 101001), 0.0001);
        assertEquals(20000, attributeOf(boss, 103001), 0.0001);
        assertEquals(100200,
                boss.getSceneMonsterVo().getMainMapReward(0).getItemKey());
        assertEquals(5, boss.getSceneMonsterVo().getMainMapReward(0).getAmount());
    }

    /** 第八关第一波必须匹配抓包：前 2 只 825/5750，后 4 只 862.5/5000。 */
    @Test
    public void eighthChapterFirstWaveMatchesCapturedMonsters() {
        List<SceneUnitVo> monsters = enterChapter(10200301, 462.93f);

        assertEquals(6, monsters.size());
        assertEquals(1000801L,
                monsters.get(0).getSceneMonsterVo().getTemplateId());
        assertEquals(825, attributeOf(monsters.get(0), 101001), 0.0001);
        assertEquals(5750, attributeOf(monsters.get(0), 103001), 0.0001);
        assertEquals(862.5, attributeOf(monsters.get(2), 101001), 0.0001);
        assertEquals(5000, attributeOf(monsters.get(2), 103001), 0.0001);
        assertEquals(1000806L,
                monsters.get(5).getSceneMonsterVo().getTemplateId());
        assertEquals(121001,
                monsters.get(0).getSceneMonsterVo().getMonsterId());
        assertEquals(13,
                monsters.get(2).getSceneMonsterVo().getMonsterId());
        assertEquals(13,
                monsters.get(5).getSceneMonsterVo().getMonsterId());
        assertEquals(466.93f,
                monsters.get(0).getBaseInfoVo().getZ(), 0.0001f);
        assertEquals(478.93f,
                monsters.get(2).getBaseInfoVo().getZ(), 0.0001f);
        assertEquals(474.93f,
                monsters.get(5).getBaseInfoVo().getZ(), 0.0001f);
        assertEquals(0f,
                monsters.get(2).getBaseInfoVo().getX(), 0.0001f);
        assertEquals(-2f,
                monsters.get(5).getBaseInfoVo().getX(), 0.0001f);
    }

    /** 第八关 Boss 必须匹配抓包：攻击 2000、气血 30000、掉落 100200 x5。 */
    @Test
    public void eighthBossMatchesCapturedBoss() {
        List<SceneUnitVo> monsters = enterChapter(10200305, 855.3f);

        assertEquals(1, monsters.size());
        SceneUnitVo boss = monsters.get(0);
        assertEquals(100001, boss.getSceneMonsterVo().getMonsterId());
        assertEquals(1000840L, boss.getSceneMonsterVo().getTemplateId());
        assertEquals(2000, attributeOf(boss, 101001), 0.0001);
        assertEquals(30000, attributeOf(boss, 103001), 0.0001);
        assertEquals(100200,
                boss.getSceneMonsterVo().getMainMapReward(0).getItemKey());
        assertEquals(5, boss.getSceneMonsterVo().getMainMapReward(0).getAmount());
    }

    /** 第九关第三波打完后直接点「来打我噻」，不能回退到循环波。 */
    @Test
    public void ninthBossButtonEntersBossWithoutLoopingToWaveThree() {
        long playerId = 100_000_000_109L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200403)
                        .build());
        long monsterId = handler.waveUnitIdAt(playerId, 0);
        for (int i = 0; i < 6; i++) {
            handler.killGuidanceMonster(
                    context,
                    com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                            .setMonsterId(monsterId + i)
                            .build());
        }
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10200403)
                        .build());

        context.writes.clear();
        handler.resetGuidanceMainMap(
                context,
                GuidanceMainMapResetReq.newBuilder()
                        .setEnterNext(true)
                        .build());

        MainMapPassChapterUpdateResp update =
                (MainMapPassChapterUpdateResp) context.writes.get(0).message;
        assertEquals(61952, context.writes.get(0).protocolId);
        assertEquals(10200405, update.getMainMapChapterId());
        assertEquals(10200403, update.getHistoryTopId());
        assertEquals(0, update.getChangeReason());
        assertEquals(1, update.getFromResetReq());

        context.writes.clear();
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.getDefaultInstance());
        assertEquals(10200405,
                CombatSessionRegistry.get(playerId).getChapterId());
        CombatSessionRegistry.clear(playerId);
    }

    /** 第九关 Boss 解锁后若已走到第二波，点「来打我噻」仍须进 Boss。 */
    @Test
    public void ninthBossButtonEntersBossFromSecondWaveAfterUnlock() {
        long playerId = 100_000_000_110L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200403)
                        .build());
        long monsterId = handler.waveUnitIdAt(playerId, 0);
        for (int i = 0; i < 6; i++) {
            handler.killGuidanceMonster(
                    context,
                    com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                            .setMonsterId(monsterId + i)
                            .build());
        }
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10200403)
                        .build());
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200402)
                        .build());

        context.writes.clear();
        handler.resetGuidanceMainMap(
                context,
                GuidanceMainMapResetReq.newBuilder()
                        .setEnterNext(true)
                        .build());

        MainMapPassChapterUpdateResp update =
                (MainMapPassChapterUpdateResp) context.writes.get(0).message;
        assertEquals(10200405, update.getMainMapChapterId());
        assertEquals(10200403, update.getHistoryTopId());
        assertEquals(1, update.getFromResetReq());
        CombatSessionRegistry.clear(playerId);
    }

    /** 第九关第一波必须匹配抓包 idx 7331-7336。 */
    @Test
    public void ninthChapterFirstWaveMatchesCapturedMonsters() {
        List<SceneUnitVo> monsters = enterChapter(10200401, 902.93f);

        assertEquals(6, monsters.size());
        assertEquals(1000901L,
                monsters.get(0).getSceneMonsterVo().getTemplateId());
        assertEquals(121001,
                monsters.get(0).getSceneMonsterVo().getMonsterId());
        assertEquals(880, attributeOf(monsters.get(0), 101001), 0.0001);
        assertEquals(6900, attributeOf(monsters.get(0), 103001), 0.0001);
        assertEquals(1000906L,
                monsters.get(5).getSceneMonsterVo().getTemplateId());
        assertEquals(121002,
                monsters.get(5).getSceneMonsterVo().getMonsterId());
        assertEquals(1000, attributeOf(monsters.get(5), 161001), 0.0001);
        assertEquals(1500, attributeOf(monsters.get(5), 165001), 0.0001);
    }

    /** 第九关 Boss 必须匹配抓包 idx 8101/8312-8313。 */
    @Test
    public void ninthBossMatchesCapturedBoss() {
        List<SceneUnitVo> monsters = enterChapter(10200405, 1299.3f);

        assertEquals(1, monsters.size());
        SceneUnitVo boss = monsters.get(0);
        assertEquals(100004, boss.getSceneMonsterVo().getMonsterId());
        assertEquals(1000940L, boss.getSceneMonsterVo().getTemplateId());
        assertEquals(20000, attributeOf(boss, 101001), 0.0001);
        assertEquals(380000, attributeOf(boss, 103001), 0.0001);
        assertEquals(100200,
                boss.getSceneMonsterVo().getMainMapReward(0).getItemKey());
        assertEquals(10, boss.getSceneMonsterVo().getMainMapReward(0).getAmount());
    }

    /** 第十关第一波必须匹配抓包 idx 8381-8387。 */
    @Test
    public void tenthChapterFirstWaveMatchesCapturedMonsters() {
        List<SceneUnitVo> monsters = enterChapter(10200501, 22.93f);

        assertEquals(6, monsters.size());
        assertEquals(1001001L,
                monsters.get(0).getSceneMonsterVo().getTemplateId());
        assertEquals(121001,
                monsters.get(0).getSceneMonsterVo().getMonsterId());
        assertEquals(990, attributeOf(monsters.get(0), 101001), 0.0001);
        assertEquals(11500, attributeOf(monsters.get(0), 103001), 0.0001);
        assertEquals(1001006L,
                monsters.get(5).getSceneMonsterVo().getTemplateId());
        assertEquals(121002,
                monsters.get(5).getSceneMonsterVo().getMonsterId());
        assertEquals(1000, attributeOf(monsters.get(5), 161001), 0.0001);
        assertEquals(1500, attributeOf(monsters.get(5), 165001), 0.0001);
    }

    /** 第十关 Boss 必须匹配抓包 idx 9342。 */
    @Test
    public void tenthBossMatchesCapturedBoss() {
        List<SceneUnitVo> monsters = enterChapter(10200505, 419.3f);

        assertEquals(1, monsters.size());
        SceneUnitVo boss = monsters.get(0);
        assertEquals(100002, boss.getSceneMonsterVo().getMonsterId());
        assertEquals(1001040L, boss.getSceneMonsterVo().getTemplateId());
        assertEquals(3000, attributeOf(boss, 101001), 0.0001);
        assertEquals(45000, attributeOf(boss, 103001), 0.0001);
        assertEquals(100200,
                boss.getSceneMonsterVo().getMainMapReward(0).getItemKey());
        assertEquals(10, boss.getSceneMonsterVo().getMainMapReward(0).getAmount());
    }

    /** 第十关第三波打完必须解锁本关 Boss（10200505），并允许玩家进入。 */
    @Test
    public void tenthBossUnlocksAfterThirdWave() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200503)
                        .build());
        long monsterId = handler.waveUnitIdAt(playerId, 0);
        for (int i = 0; i < 6; i++) {
            handler.killGuidanceMonster(
                    context,
                    com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                            .setMonsterId(monsterId + i)
                            .build());
        }
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10200503)
                        .build());
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200501)
                        .build());

        context.writes.clear();
        handler.resetGuidanceMainMap(
                context,
                GuidanceMainMapResetReq.newBuilder()
                        .setEnterNext(true)
                        .build());

        MainMapPassChapterUpdateResp update =
                (MainMapPassChapterUpdateResp) context.writes.get(0).message;
        assertEquals(61952, context.writes.get(0).protocolId);
        assertEquals(10200505, update.getMainMapChapterId());
        assertEquals(10200503, update.getHistoryTopId());
        assertEquals(0, update.getChangeReason());
        assertEquals(1, update.getFromResetReq());
        assertEquals(55, update.getStageTime());
        assertEquals(12, update.getLastStageTime());
    }

    /** 第十关第三波打完后直接点「来打我噻」，不能回退到循环波。 */
    @Test
    public void tenthBossButtonEntersBossWithoutLoopingToWaveThree() {
        long playerId = 100_000_000_111L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200503)
                        .build());
        long monsterId = handler.waveUnitIdAt(playerId, 0);
        for (int i = 0; i < 6; i++) {
            handler.killGuidanceMonster(
                    context,
                    com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                            .setMonsterId(monsterId + i)
                            .build());
        }
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10200503)
                        .build());

        context.writes.clear();
        handler.resetGuidanceMainMap(
                context,
                GuidanceMainMapResetReq.newBuilder()
                        .setEnterNext(true)
                        .build());

        MainMapPassChapterUpdateResp update =
                (MainMapPassChapterUpdateResp) context.writes.get(0).message;
        assertEquals(10200505, update.getMainMapChapterId());
        assertEquals(10200503, update.getHistoryTopId());
        assertEquals(0, update.getChangeReason());
        assertEquals(1, update.getFromResetReq());

        context.writes.clear();
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.getDefaultInstance());
        assertEquals(10200505,
                CombatSessionRegistry.get(playerId).getChapterId());
        CombatSessionRegistry.clear(playerId);
    }

    /** 第八至十关 Boss 分别完成 200022/200024/4001001，并推进到下一关。 */
    @Test
    public void laterBossesCompleteCapturedTasksAndAdvance() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        killGuidanceBoss(handler, context, 10200305, 405);
        TaskUpdateResp eighthTasks = lastTaskUpdate(context);
        assertTask(eighthTasks, 0, 200022, TaskPhase.FINISHED, 1);
        context.writes.clear();
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10200305)
                        .build());
        MainMapPassChapterUpdateResp ninth = lastChapterUpdate(context);
        assertEquals(10200401, ninth.getMainMapChapterId());
        assertEquals(12, ninth.getStageTime());
        assertEquals(10, ninth.getLastStageTime());

        context.writes.clear();
        killGuidanceBoss(handler, context, 10200405, 505);
        TaskUpdateResp ninthTasks = lastTaskUpdate(context);
        assertTask(ninthTasks, 0, 200024, TaskPhase.FINISHED, 1);
        assertTask(ninthTasks, 1, 1107, TaskPhase.PROGRESS, 174);
        context.writes.clear();
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10200405)
                        .build());
        MainMapPassChapterUpdateResp tenth = lastChapterUpdate(context);
        assertEquals(10200501, tenth.getMainMapChapterId());
        assertEquals(55, tenth.getStageTime());
        assertEquals(12, tenth.getLastStageTime());
        assertEquals(10200401, tenth.getLoseBackId());

        context.writes.clear();
        killGuidanceBoss(handler, context, 10200505, 605);
        TaskUpdateResp tenthTasks = lastTaskUpdate(context);
        assertTask(tenthTasks, 0, 4001001, TaskPhase.FINISHED, 1);
        assertTask(tenthTasks, 1, 1107, TaskPhase.PROGRESS, 197);
        context.writes.clear();
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10200505)
                        .build());
        MainMapPassChapterUpdateResp wutan = lastChapterUpdate(context);
        assertEquals(10300101, wutan.getMainMapChapterId());
        assertEquals(13, wutan.getStageTime());
        assertEquals(55, wutan.getLastStageTime());
        assertEquals(10200501, wutan.getLoseBackId());
    }

    /** 乌坦城第一波走 61962 战报，四只狐狸击杀进度 198→201 后推进 10300102。 */
    @Test
    public void wutanFirstWaveUsesBattleLogAndAdvancesToSecondWave()
            throws Exception {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_201L);

        handler.startMainMapFight(
                context,
                MainMapStartFightReq.newBuilder()
                        .setMainMapChapterId(10300101)
                        .build());
        MainMapStartFightResp start = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 61971) {
                start = (MainMapStartFightResp) write.message;
            }
        }
        assertTrue(start != null);
        assertEquals(10300101, start.getMainMapChapterId());
        assertTrue(start.getEnd());
        assertEquals(0, start.getResult());
        assertEquals(1001, start.getBattleLog().getType());
        assertTrue(start.getBattleLog().getFin());
        assertEquals(4, start.getBattleLog().getFightStatisticsResp()
                .getLoserCount());
        BattleLogVO waveLog = BattleLogVO.parseFrom(start.getBattleLog().getData());
        assertTrue(waveLog.getEntryListCount() > 10);
        assertTrue(battleLogHasSkill(waveLog, 42010110101L));
        context.writes.clear();

        long[] foxes = { 1004101L, 1004103L, 1004104L, 1004102L };
        for (int i = 0; i < foxes.length; i++) {
            handler.killMainMapMonster(
                    context,
                    MainMapKillMonsterReq.newBuilder()
                            .setMainMapChapterId(10300101)
                            .setMonsterId(foxes[i])
                            .build());
            if (i < 3) {
                TaskUpdateResp progress = lastTaskUpdate(context);
                if (i == 2) {
                    assertTask(progress, 0, 1107, TaskPhase.FINISHED, 200);
                    assertTask(progress, 1, 1110, TaskPhase.PROGRESS, 200);
                } else {
                    assertTask(progress, 0, 1107, TaskPhase.PROGRESS, 198 + i);
                }
            }
        }

        context.writes.clear();
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10300101)
                        .build());
        TaskUpdateResp cleared = lastTaskUpdate(context);
        assertTask(cleared, 0, 1110, TaskPhase.PROGRESS, 201);
        assertTask(cleared, 1, 1113, TaskPhase.PROGRESS, 201);
        RewardResp passReward = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 50406) {
                passReward = (RewardResp) write.message;
            }
        }
        assertTrue(passReward != null);
        assertEquals(61951, passReward.getOperationType());
        MainMapPassChapterUpdateResp next = lastChapterUpdate(context);
        assertEquals(10300102, next.getMainMapChapterId());
        assertEquals(10300101, next.getHistoryTopId());
        assertEquals(1, next.getChangeReason());
        assertEquals(0, next.getNextChallengeId());
        assertFalse(next.getResetState());
        assertEquals(55, next.getLastStageTime());
        assertEquals(10200501, next.getLoseBackId());
    }

    /** 战报 50804 必须跟着 61962 的战斗点平移，不能再生成 x-2/z+4 秒杀坐标。 */
    @Test
    public void wutanBattleLogSpawnsAtRequestedFightPoint() throws Exception {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_211L);

        handler.startMainMapFight(
                context,
                MainMapStartFightReq.newBuilder()
                        .setMainMapChapterId(10300101)
                        .setX(9.802692f)
                        .setY(-0.023f)
                        .setZ(28.6852f)
                        .build());
        MainMapStartFightResp originStart = lastWutanStart(context);
        assertTrue(originStart != null);
        BattleLogVO originLog = BattleLogVO.parseFrom(
                originStart.getBattleLog().getData());
        SceneUnitVo originFirst = firstBattleLogMonster(originLog);
        assertTrue(originFirst != null);
        assertEquals(1004101L, originFirst.getSceneMonsterVo().getTemplateId());
        float originX = originFirst.getBaseInfoVo().getX();
        float originZ = originFirst.getBaseInfoVo().getZ();

        context.writes.clear();
        handler.startMainMapFight(
                context,
                MainMapStartFightReq.newBuilder()
                        .setMainMapChapterId(10300101)
                        .setX(9.802692f + 12f)
                        .setY(-0.023f)
                        .setZ(28.6852f + 40f)
                        .build());
        MainMapStartFightResp movedStart = lastWutanStart(context);
        BattleLogVO movedLog = BattleLogVO.parseFrom(
                movedStart.getBattleLog().getData());
        SceneUnitVo movedFirst = firstBattleLogMonster(movedLog);
        assertTrue(movedFirst != null);
        assertEquals(originX + 12f, movedFirst.getBaseInfoVo().getX(), 0.05f);
        assertEquals(originZ + 40f, movedFirst.getBaseInfoVo().getZ(), 0.05f);
    }

    /** 手动进场 61973 也要在战斗点刷出狐狸，不能再空返回。 */
    @Test
    public void wutanManualEnterSpawnsFieldMonstersAtFightPoint() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_212L);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10300101)
                        .setX(9.802692f)
                        .setZ(28.6852f)
                        .build());
        SceneUpdateVisibleResp visible = firstSpawnedMonster(context);
        assertTrue(visible != null);
        SceneUnitVo first = visible.getVisibleList(0);
        assertEquals(1004101L, first.getSceneMonsterVo().getTemplateId());
        assertEquals(9.802692f - 2f, first.getBaseInfoVo().getX(), 0.0001f);
        assertEquals(28.6852f + 4f, first.getBaseInfoVo().getZ(), 0.0001f);
        assertEquals(4, CombatSessionRegistry.get(context.getId())
                .getMonsters().size());
    }

    /** 第三波打完点「来打我噻」必须进 Boss，不能再绕回 10300101。 */
    @Test
    public void wutanResetAfterThirdWaveEntersBoss() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_213L);

        clearWutanWave(handler, context, 10300101,
                1004101L, 1004102L, 1004103L, 1004104L);
        clearWutanWave(handler, context, 10300102,
                1004201L, 1004202L, 1004203L, 1004204L, 1004205L);
        clearWutanWave(handler, context, 10300103,
                1004301L, 1004302L, 1004303L, 1004304L, 1004305L, 1004305L);
        context.writes.clear();
        handler.resetGuidanceMainMap(
                context,
                GuidanceMainMapResetReq.newBuilder()
                        .setEnterNext(true)
                        .build());
        MainMapPassChapterUpdateResp boss = lastChapterUpdate(context);
        assertEquals(10300105, boss.getMainMapChapterId());
        assertEquals(10300103, boss.getHistoryTopId());
    }

    /** 乌坦城第三波首次打完解锁 Boss 10300105，对应抓包 idx 9636。 */
    @Test
    public void wutanThirdWaveUnlocksCapturedBoss() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_202L);

        clearWutanWave(handler, context, 10300101,
                1004101L, 1004102L, 1004103L, 1004104L);
        clearWutanWave(handler, context, 10300102,
                1004201L, 1004202L, 1004203L, 1004204L, 1004205L);

        handler.startMainMapFight(
                context,
                MainMapStartFightReq.newBuilder()
                        .setMainMapChapterId(10300103)
                        .build());
        context.writes.clear();
        long[] boars = {
                1004301L, 1004302L, 1004303L, 1004304L, 1004305L, 1004305L
        };
        for (long monsterId : boars) {
            handler.killMainMapMonster(
                    context,
                    MainMapKillMonsterReq.newBuilder()
                            .setMainMapChapterId(10300103)
                            .setMonsterId(monsterId)
                            .build());
        }
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10300103)
                        .build());
        MainMapPassChapterUpdateResp update = lastChapterUpdate(context);
        assertEquals(10300101, update.getMainMapChapterId());
        assertEquals(10300105, update.getNextChallengeId());
        assertEquals(10300103, update.getHistoryTopId());
        assertEquals(4, update.getChangeReason());
        assertTrue(update.getResetState());
        assertFalse(update.getHasReward());
        TaskUpdateResp progress = lastTaskUpdate(context);
        assertTask(progress, 0, 1110, TaskPhase.PROGRESS, 212);
        assertTask(progress, 1, 1113, TaskPhase.PROGRESS, 212);
    }

    /** 乌坦城 Boss 回放抓包战报完成 200029，并推进到 10300201。 */
    @Test
    public void wutanBossFinishesTask200029AndAdvances() throws Exception {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_203L);

        handler.startMainMapFight(
                context,
                MainMapStartFightReq.newBuilder()
                        .setMainMapChapterId(10300105)
                        .build());
        MainMapStartFightResp start = lastWutanStart(context);
        assertTrue(start != null);
        assertTrue(start.getEnd());
        assertTrue(start.getBattleLog().getFin());
        assertTrue(CombatSessionRegistry.get(context.getId()) == null);
        assertFalse(context.writes.stream().anyMatch(
                write -> write.protocolId == 50756));
        BattleLogVO bossLog = BattleLogVO.parseFrom(
                start.getBattleLog().getData());
        assertTrue(battleLogHasSkill(bossLog, 50610110101L));
        context.writes.clear();
        handler.killMainMapMonster(
                context,
                MainMapKillMonsterReq.newBuilder()
                        .setMainMapChapterId(10300105)
                        .setMonsterId(1004401L)
                        .build());
        assertFalse(context.writes.stream().anyMatch(
                write -> write.protocolId == 50781));
        TaskUpdateResp bossTasks = lastTaskUpdate(context);
        assertTask(bossTasks, 0, 200029, TaskPhase.FINISHED, 1);
        assertTask(bossTasks, 1, 1110, TaskPhase.PROGRESS, 213);

        context.writes.clear();
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10300105)
                        .build());
        MainMapPassChapterUpdateResp next = lastChapterUpdate(context);
        assertEquals(10300201, next.getMainMapChapterId());
        assertEquals(10300105, next.getHistoryTopId());
        assertEquals(10300101, next.getLoseBackId());
    }

    /**
     * 第11关 Boss 也走完整 61971 战报，不能再 50756 刷场地怪，
     * 也不能再发 0.4 秒秒杀日志。
     */
    @Test
    public void wutanBossReplaysCapturedBattleLogInsteadOfFieldSpawn()
            throws Exception {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_214L);

        handler.startMainMapFight(
                context,
                MainMapStartFightReq.newBuilder()
                        .setMainMapChapterId(10300105)
                        .setX(388.62366f)
                        .setY(4.297f)
                        .setZ(672.1174f)
                        .build());

        assertTrue(firstSpawnedMonster(context) == null);
        assertTrue(CombatSessionRegistry.get(context.getId()) == null);

        MainMapStartFightResp start = lastWutanStart(context);
        assertTrue(start != null);
        assertTrue(start.getEnd());
        assertTrue(start.getBattleLog().getFin());
        BattleLogVO log = BattleLogVO.parseFrom(start.getBattleLog().getData());
        SceneUnitVo boss = firstBattleLogMonster(log);
        assertTrue(boss != null);
        assertEquals(1004401L, boss.getSceneMonsterVo().getTemplateId());
        assertEquals(100127, boss.getSceneMonsterVo().getMonsterId());
        assertTrue(battleLogHasSkill(log, 50610110101L));
        assertTrue(log.getEntryListCount() > 10);

        // 战报必须复用场景内的主角，否则客户端按不同对象 ID 再创建一人。
        int heroCount = 0;
        for (BattleLogEntryVO entry : log.getEntryListList()) {
            for (BattleLogItemVO item : entry.getItemListList()) {
                if (item.getPacketId() != 50804) {
                    continue;
                }
                BattleLogUpdateVisibleResp visible =
                        BattleLogUpdateVisibleResp.parseFrom(item.getData());
                for (SceneUnitVo unit : visible.getSceneUpdateVisibleResp()
                        .getVisibleListList()) {
                    if (unit.hasHeroVo()) {
                        heroCount++;
                        assertEquals(context.getId() * 1000L + 1L,
                                unit.getBaseInfoVo().getId());
                        assertEquals(context.getId(), unit.getHeroVo().getPlayerId());
                        assertEquals(unit.getBaseInfoVo().getId(), unit.getHeroVo()
                                .getCrossHeroShortInfo().getSceneUnitUid());
                    }
                }
            }
        }
        assertEquals(1, heroCount);

        context.writes.clear();
        handler.killMainMapMonster(
                context,
                MainMapKillMonsterReq.newBuilder()
                        .setMainMapChapterId(10300105)
                        .setMonsterId(1004401L)
                        .build());
        assertFalse(context.writes.stream().anyMatch(
                write -> write.protocolId == 50781));
        assertTrue(lastTaskUpdate(context) != null);
    }

    /** 第12关第一波同样回放抓包双方出手，打完推进 10300202。 */
    @Test
    public void wutanChapter12FirstWaveUsesCapturedBattleLog() throws Exception {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_221L);

        handler.startMainMapFight(
                context,
                MainMapStartFightReq.newBuilder()
                        .setMainMapChapterId(10300201)
                        .build());
        MainMapStartFightResp start = lastWutanStart(context);
        assertTrue(start != null);
        assertTrue(start.getEnd());
        assertTrue(start.getBattleLog().getFin());
        assertEquals(4, start.getBattleLog().getFightStatisticsResp()
                .getLoserCount());
        BattleLogVO log = BattleLogVO.parseFrom(start.getBattleLog().getData());
        assertTrue(log.getEntryListCount() > 10);
        assertTrue(battleLogHasSkill(log, 42010110101L));
        SceneUnitVo first = firstBattleLogMonster(log);
        assertTrue(first != null);
        assertEquals(1004501L, first.getSceneMonsterVo().getTemplateId());

        context.writes.clear();
        long[] foxes = { 1004501L, 1004502L, 1004503L, 1004504L };
        for (long monsterId : foxes) {
            handler.killMainMapMonster(
                    context,
                    MainMapKillMonsterReq.newBuilder()
                            .setMainMapChapterId(10300201)
                            .setMonsterId(monsterId)
                            .build());
        }
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10300201)
                        .build());
        MainMapPassChapterUpdateResp next = lastChapterUpdate(context);
        assertEquals(10300202, next.getMainMapChapterId());
        assertEquals(10300201, next.getHistoryTopId());
        assertEquals(10300101, next.getLoseBackId());
        TaskUpdateResp progress = lastTaskUpdate(context);
        assertTask(progress, 0, 1110, TaskPhase.PROGRESS, 217);
        assertTask(progress, 1, 1113, TaskPhase.PROGRESS, 217);
    }

    /** 第12关 Boss 回放抓包并完成 200031，推进第13关。 */
    @Test
    public void wutanChapter12BossFinishesTask200031AndAdvances()
            throws Exception {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_222L);

        handler.startMainMapFight(
                context,
                MainMapStartFightReq.newBuilder()
                        .setMainMapChapterId(10300205)
                        .build());
        MainMapStartFightResp start = lastWutanStart(context);
        assertTrue(start != null);
        BattleLogVO log = BattleLogVO.parseFrom(start.getBattleLog().getData());
        assertTrue(battleLogHasSkill(log, 50610110101L));
        context.writes.clear();
        handler.killMainMapMonster(
                context,
                MainMapKillMonsterReq.newBuilder()
                        .setMainMapChapterId(10300205)
                        .setMonsterId(1004801L)
                        .build());
        assertFalse(context.writes.stream().anyMatch(
                write -> write.protocolId == 50781));
        TaskUpdateResp bossTasks = lastTaskUpdate(context);
        assertTask(bossTasks, 0, 200031, TaskPhase.FINISHED, 1);
        assertTask(bossTasks, 1, 1110, TaskPhase.PROGRESS, 229);

        context.writes.clear();
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10300205)
                        .build());
        MainMapPassChapterUpdateResp next = lastChapterUpdate(context);
        assertEquals(10300301, next.getMainMapChapterId());
        assertEquals(10300205, next.getHistoryTopId());
        assertEquals(10300201, next.getLoseBackId());
    }

    /** 领 200029 后开斗师声望，接挂机装备任务 200030。 */
    @Test
    public void chapterTwelveOpensReputationAndHangUpTask200030() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_410L);

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200029))
                        .build());
        assertTrue(containsTask(
                lastTaskUpdate(context), 200030, TaskPhase.PROGRESS));

        ModuleNewOpenResp opens = null;
        ReputationInitResp reputation = null;
        MainMapChapterInfoResp hangUp = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 50852) {
                opens = (ModuleNewOpenResp) write.message;
            }
            if (write.protocolId == 53703) {
                reputation = (ReputationInitResp) write.message;
            }
            if (write.protocolId == 61951) {
                hangUp = (MainMapChapterInfoResp) write.message;
            }
        }
        assertTrue(opens != null);
        assertEquals(10, opens.getOpens(2));
        assertTrue(reputation != null);
        assertEquals(1, reputation.getRangeVoList(0).getRangeInsideLv());
        assertTrue(hangUp != null);
        assertTrue(hangUp.getHasReward());
        assertEquals(2, hangUp.getMainMapChapterHangUpRewardCount());
        assertEquals(12, hangUp.getMainMapChapterHangUpReward(0).getItemKey());
        assertEquals(15, hangUp.getMainMapChapterHangUpReward(1).getItemKey());
    }

    /** 领挂机奖励得到 2 件斗铠，主线 200030 完成。 */
    @Test
    public void takeHangUpRewardFinishesTask200030() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_414L);

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200029))
                        .build());
        context.writes.clear();
        handler.takeHangUpReward(
                context,
                MainMapTakeHangUpRewardReq.getDefaultInstance());
        assertTrue(containsTask(
                lastTaskUpdate(context), 200030, TaskPhase.FINISHED));
        MainEquipBagAddResp bag = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 75355) {
                bag = (MainEquipBagAddResp) write.message;
            }
        }
        assertTrue(bag != null);
        assertTrue(bag.getFlag());
        assertEquals(2, bag.getAddListCount());
    }

    /** 10060 凑满 700 后可升斗师声望 2 级。 */
    @Test
    public void reputationLvUpConsumesItem10060() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_415L);

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200029))
                        .build());
        handler.setItem10060Count(context.getId(), 700);
        context.writes.clear();
        handler.reputationLvUp(
                context,
                ReputationLvUpReq.newBuilder().setRange(1).build());
        ReputationLvUpResp lvUp = null;
        PackUpdateResp pack = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 53705) {
                lvUp = (ReputationLvUpResp) write.message;
            }
            if (write.protocolId == 50402) {
                pack = (PackUpdateResp) write.message;
            }
        }
        assertTrue(lvUp != null);
        assertEquals(2, lvUp.getUpdateRangeVo().getRangeInsideLv());
        assertTrue(pack != null);
        assertEquals(53701, pack.getOperationType());
        assertFalse(pack.getPacks(0).getUpdateItems(0).hasPackItem());
    }

    /** 领 200031 后接纳戒任务 200033；抽一次纳戒完成。 */
    @Test
    public void chapterThirteenLotteryTask200033FinishesAfterDraw() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_411L);

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200031))
                        .build());
        assertTrue(containsTask(
                lastTaskUpdate(context), 200033, TaskPhase.PROGRESS));

        context.writes.clear();
        handler.drawNewFightSkill(
                context,
                LotteryDrawReq.newBuilder()
                        .setLotteryType(
                                LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                        .setTen(false)
                        .build());
        assertTrue(context.writes.stream().anyMatch(write ->
                write.protocolId == 50906
                        && containsTask((TaskUpdateResp) write.message,
                                200033, TaskPhase.FINISHED)));
    }

    /** 领 200033 后接 200034；第13关 Boss 完成后推进第14关。 */
    @Test
    public void wutanChapter13BossFinishesTask200034AndAdvances()
            throws Exception {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_412L);

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200033))
                        .build());
        assertTrue(containsTask(
                lastTaskUpdate(context), 200034, TaskPhase.PROGRESS));

        handler.startMainMapFight(
                context,
                MainMapStartFightReq.newBuilder()
                        .setMainMapChapterId(10300305)
                        .build());
        MainMapStartFightResp start = lastWutanStart(context);
        assertTrue(start != null);
        assertEquals(10300305, start.getMainMapChapterId());
        BattleLogVO log = BattleLogVO.parseFrom(start.getBattleLog().getData());
        assertTrue(log.getEntryListCount() > 10);
        context.writes.clear();
        handler.killMainMapMonster(
                context,
                MainMapKillMonsterReq.newBuilder()
                        .setMainMapChapterId(10300305)
                        .setMonsterId(1005201L)
                        .build());
        TaskUpdateResp bossTasks = lastTaskUpdate(context);
        assertTask(bossTasks, 0, 200034, TaskPhase.FINISHED, 1);
        assertTask(bossTasks, 1, 1110, TaskPhase.PROGRESS, 245);

        context.writes.clear();
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10300305)
                        .build());
        MainMapPassChapterUpdateResp next = lastChapterUpdate(context);
        assertEquals(10300401, next.getMainMapChapterId());
        assertEquals(10300305, next.getHistoryTopId());
        assertEquals(10300301, next.getLoseBackId());
    }

    /** 领 200034 后实际通过天焚塔第2层，再接第14关通关任务。 */
    @Test
    public void wutanChapter14BossFinishesTask200036AndAdvances()
            throws Exception {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_413L);

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200034))
                        .build());
        assertTrue(containsTask(
                lastTaskUpdate(context), 200035, TaskPhase.PROGRESS));
        assertFalse(context.writes.stream().anyMatch(write ->
                write.protocolId == 50906
                        && containsTask((TaskUpdateResp) write.message,
                                200035, TaskPhase.REWARDED)));

        for (int floor = 1; floor <= 2; floor++) {
            handler.fireTowerChallenge(context, com.doupo.protocol.FireTowerChallengeReq.newBuilder().setZ(6.5f).build());
            handler.fireTowerAfter(context, com.doupo.protocol.FireTowerChallengeAfterReq.getDefaultInstance());
        }
        handler.rewardGuidanceTask(context, TaskRewardReq.newBuilder()
                .setTaskId(TaskUniqueKey.newBuilder().setTaskResourceId(200035)).build());
        assertTrue(containsTask(lastTaskUpdate(context), 200036, TaskPhase.PROGRESS));

        handler.startMainMapFight(
                context,
                MainMapStartFightReq.newBuilder()
                        .setMainMapChapterId(10300405)
                        .build());
        assertTrue(lastWutanStart(context) != null);
        context.writes.clear();
        handler.killMainMapMonster(
                context,
                MainMapKillMonsterReq.newBuilder()
                        .setMainMapChapterId(10300405)
                        .setMonsterId(1005601L)
                        .build());
        TaskUpdateResp bossTasks = lastTaskUpdate(context);
        assertTask(bossTasks, 0, 200036, TaskPhase.FINISHED, 1);

        context.writes.clear();
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10300405)
                        .build());
        MainMapPassChapterUpdateResp next = lastChapterUpdate(context);
        assertEquals(10300501, next.getMainMapChapterId());
        assertEquals(10300405, next.getHistoryTopId());
        assertEquals(10300401, next.getLoseBackId());
    }

    /** 第13关第一波战报推进 10300302。 */
    @Test
    public void wutanChapter13FirstWaveAdvancesToSecondWave() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_414L);

        clearWutanWave(handler, context, 10300301,
                1004901L, 1004902L, 1004903L, 1004904L);
        MainMapPassChapterUpdateResp next = lastChapterUpdate(context);
        assertEquals(10300302, next.getMainMapChapterId());
        assertEquals(10300301, next.getHistoryTopId());
        assertEquals(10300201, next.getLoseBackId());
    }

    /** 已通关第10关后再领 200054，到达乌坦城的 200026 直接 finished。 */
    @Test
    public void wutanArriveTask200026AutoFinishesAfterChapterTen() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_204L);

        killGuidanceBoss(handler, context, 10200505, 605);
        rewardTask(handler, context, 200025);
        context.writes.clear();
        rewardTask(handler, context, 200054);
        assertTrue(containsTask(
                lastTaskUpdate(context), 200026, TaskPhase.FINISHED));
    }

    /** 200022→纳戒抽取→200023→200024 领奖严格按抓包同步。 */
    @Test
    public void ninthChapterTaskChainUsesCapturedRewardsAndSingleDraw() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        rewardTask(handler, context, 200022);
        PackUpdateResp energyReward = firstPackUpdate(context);
        assertEquals(20,
                energyReward.getPacks(0).getUpdateItems(0)
                        .getPackItem().getSize());
        assertTask(lastTaskUpdate(context), 0,
                200023, TaskPhase.PROGRESS, 0);

        context.writes.clear();
        handler.drawNewFightSkill(
                context,
                LotteryDrawReq.newBuilder()
                        .setLotteryType(
                                LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                        .setTen(false)
                        .build());
        LotteryDrawResp draw = firstLotteryDraw(context);
        assertEquals(2, draw.getRewardItemVosCount());
        PackUpdateResp consume = firstPackUpdate(context);
        assertFalse(consume.getPacks(0).getUpdateItems(0).hasPackItem());
        assertTrue(context.writes.stream().anyMatch(write ->
                write.protocolId == 50906
                        && containsTask((TaskUpdateResp) write.message,
                                200023, TaskPhase.FINISHED)));

        context.writes.clear();
        rewardTask(handler, context, 200023);
        PackUpdateResp smallReward = firstPackUpdate(context);
        assertEquals(2,
                smallReward.getPacks(0).getUpdateItems(0)
                        .getPackItem().getSize());
        assertTask(lastTaskUpdate(context), 0,
                200024, TaskPhase.PROGRESS, 0);

        context.writes.clear();
        rewardTask(handler, context, 200024);
        PackUpdateResp itemReward = firstPackUpdate(context);
        assertEquals(22,
                itemReward.getPacks(0).getUpdateItems(0).getItemIndex());
        assertEquals(101,
                itemReward.getPacks(0).getUpdateItems(0)
                        .getPackItem().getKey());
        assertTask(lastTaskUpdate(context), 0,
                200025, TaskPhase.PROGRESS, 0);
    }

    /**
     * 千焰剑罡必须按抓包 idx 5990/6011/6050 打出三段 FightSkill 伤害。
     *
     * <p>走普攻通道时客户端会播动画但不结算伤害。
     */
    @Test
    public void qianyanSwordHitsAllMonstersWithCapturedFightSkillActions() {
        long playerId = 100_000_000_114L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200201)
                        .build());
        long firstMonsterId = handler.waveUnitIdAt(playerId, 0);
        context.writes.clear();

        handler.useGuidanceMainMapSkill(
                context,
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10151410100L)
                        .setTargetId(firstMonsterId)
                        .setX(1.88f)
                        .setZ(24.2f)
                        .setTx(-0.15f)
                        .setTz(25.9f)
                        .build());

        UseSkillResp use = (UseSkillResp) context.writes.stream()
                .filter(write -> write.protocolId == 50762)
                .findFirst().get().message;
        assertEquals(10151410100L, use.getSkillId());
        assertEquals(1.88f, use.getTargetX(), 0.01f);
        assertEquals(24.2f, use.getTargetZ(), 0.01f);
        assertEquals(use.getCurX(), use.getTargetX(), 0.01f);
        assertEquals(use.getCurZ(), use.getTargetZ(), 0.01f);

        List<SkillActionResp> hits = context.writes.stream()
                .filter(write -> write.protocolId == 50763)
                .map(write -> (SkillActionResp) write.message)
                .filter(hit -> hit.getActionListCount() > 0)
                .collect(java.util.stream.Collectors.toList());
        assertEquals(3, hits.size());
        assertEquals(10151410102L,
                hits.get(0).getActionList(0).getDamageVo().getActionId());
        assertEquals(DamageTypeVo.FIGHT_SKILL,
                hits.get(0).getActionList(0).getDamageVo().getType());
        assertEquals(100001,
                hits.get(0).getActionList(0).getDamageVo().getSkillBaseId());
        assertEquals(101,
                hits.get(0).getActionList(0).getDamageVo().getGroup());
        assertEquals(10151410105L,
                hits.get(1).getActionList(0).getDamageVo().getActionId());
        assertEquals(10151410108L,
                hits.get(2).getActionList(0).getDamageVo().getActionId());
        assertTrue(hits.get(0).getActionList(0).getDamageVo().getDamage() > 1000);
        assertTrue(hits.stream().anyMatch(hit -> hit.getActionListList().stream()
                .anyMatch(action -> action.hasDieVo())));
        assertFalse(context.writes.stream()
                .anyMatch(write -> write.protocolId == 50757));
        assertFalse(CombatSessionRegistry.get(playerId)
                .getMonster(firstMonsterId).isAlive());
        CombatSessionRegistry.clear(playerId);
    }

    /**
     * 千焰若在前两段就打死 Boss，不能 return 掉后续结算。
     * 否则没有 50781，客户端卡在第 8 关。
     */
    @Test
    public void qianyanBossKillOnEarlyStageStillWritesFightResult() {
        long playerId = 100_000_000_117L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200305)
                        .build());
        long bossId = handler.waveUnitIdAt(playerId, 0);
        CombatSession session = CombatSessionRegistry.get(playerId);
        session.getMonster(bossId).applyDamage(29000);
        context.writes.clear();

        handler.useGuidanceMainMapSkill(
                context,
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10151410100L)
                        .setTargetId(bossId)
                        .build());

        assertTrue(context.writes.stream()
                .anyMatch(write -> write.protocolId == 50781
                        && write.message instanceof FightResultResp));
        assertTrue(context.writes.stream().anyMatch(write ->
                write.protocolId == 50906
                        && containsTask((TaskUpdateResp) write.message,
                                200022, TaskPhase.FINISHED)));
        assertFalse(context.writes.stream()
                .anyMatch(write -> write.protocolId == 50757));
        CombatSessionRegistry.clear(playerId);
    }

    /**
     * 抓包 idx 7316/7319：Boss 死后客户端只报 61964，必须回 50757，
     * 随后 EndFight 才能推进到 10200401。
     */
    @Test
    public void eighthBossMainMapKillForgetsBossThenEndFightAdvances() {
        long playerId = 100_000_000_118L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200305)
                        .build());
        long bossId = handler.waveUnitIdAt(playerId, 0);
        CombatSession session = CombatSessionRegistry.get(playerId);
        session.getMonster(bossId).applyDamage(29000);
        handler.useGuidanceMainMapSkill(
                context,
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10151410100L)
                        .setTargetId(bossId)
                        .build());
        context.writes.clear();

        handler.killMainMapMonster(
                context,
                MainMapKillMonsterReq.newBuilder()
                        .setMainMapChapterId(10200305)
                        .setMonsterId(1000840L)
                        .build());

        SceneForgetVisibleResp forget = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 50757) {
                forget = (SceneForgetVisibleResp) write.message;
            }
        }
        assertTrue(forget != null);
        assertEquals(bossId, forget.getForgetIds(0));

        context.writes.clear();
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10200305)
                        .build());
        MainMapPassChapterUpdateResp ninth = lastChapterUpdate(context);
        assertEquals(10200401, ninth.getMainMapChapterId());
        CombatSessionRegistry.clear(playerId);
    }

    @Test
    public void qianyanDamageWaitsForHitsAndBossCanAdvance() {
        long playerId = 100_000_000_119L;
        TimedSceneHandler handler = new TimedSceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200305).build());
        long bossId = handler.waveUnitIdAt(playerId, 0);
        CombatSession session = CombatSessionRegistry.get(playerId);
        session.getMonster(bossId).applyDamage(25000);
        context.writes.clear();
        handler.useGuidanceMainMapSkill(context,
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10151410100L).setTargetId(bossId).build());
        assertEquals(5000, session.getMonster(bossId).getCurrentHp(), 0);
        handler.advanceTo(666);
        assertEquals(5000, session.getMonster(bossId).getCurrentHp(), 0);
        handler.advanceTo(667);
        double firstHp = session.getMonster(bossId).getCurrentHp();
        assertTrue(firstHp > 0 && firstHp < 5000);
        handler.advanceTo(1132);
        assertEquals(firstHp, session.getMonster(bossId).getCurrentHp(), 0);
        handler.advanceTo(1133);
        double secondHp = session.getMonster(bossId).getCurrentHp();
        assertTrue(secondHp > 0 && secondHp < firstHp);
        handler.advanceTo(2032);
        assertEquals(secondHp, session.getMonster(bossId).getCurrentHp(), 0);
        assertFalse(context.writes.stream().anyMatch(w -> w.protocolId == 50781));
        handler.advanceTo(2033);
        assertFalse(session.getMonster(bossId).isAlive());
        assertEquals(1, context.writes.stream().filter(w -> w.protocolId == 50781).count());
        assertTrue(context.writes.stream().anyMatch(w -> w.protocolId == 50906
                && containsTask((TaskUpdateResp) w.message, 200022, TaskPhase.FINISHED)));
        handler.killMainMapMonster(context, MainMapKillMonsterReq.newBuilder()
                .setMainMapChapterId(10200305).setMonsterId(1000840).build());
        handler.advanceTo(3032);
        assertFalse(context.writes.stream().anyMatch(w -> w.protocolId == 50757));
        handler.advanceTo(3033);
        assertEquals(1, context.writes.stream().filter(w -> w.protocolId == 50757).count());
        handler.advanceTo(4100);
        assertTrue(context.writes.stream().anyMatch(w -> w.protocolId == 50763
                && ((SkillActionResp) w.message).getActionListCount() == 0));
        handler.endMainMapFight(context, com.doupo.protocol.MainMapEndFightReq.newBuilder()
                .setMainMapChapterId(10200305).build());
        assertEquals(10200401, lastChapterUpdate(context).getMainMapChapterId());
        CombatSessionRegistry.clear(playerId);
    }

    @Test
    public void pendingSkillHitsDoNotReachReenteredChapter() {
        long playerId = 100_000_000_120L;
        TimedSceneHandler handler = new TimedSceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        GuidanceMainMapMonsterEnterReq enter = GuidanceMainMapMonsterEnterReq.newBuilder()
                .setChapterId(10200305).build();
        handler.enterGuidanceMainMapMonster(context, enter);
        long bossId = handler.waveUnitIdAt(playerId, 0);
        handler.useGuidanceMainMapSkill(context, GuidanceMainMapUseSkillReq.newBuilder()
                .setSkillId(10151410100L).setTargetId(bossId).build());
        CombatSessionRegistry.clear(playerId);
        handler.enterGuidanceMainMapMonster(context, enter);
        long reenteredBossId = handler.waveUnitIdAt(playerId, 0);
        CombatSession replacement = CombatSessionRegistry.get(playerId);
        double hp = replacement.getMonster(reenteredBossId).getCurrentHp();
        context.writes.clear();
        handler.advanceTo(5000);
        assertEquals(hp, replacement.getMonster(reenteredBossId).getCurrentHp(), 0);
        assertTrue(context.writes.isEmpty());
        CombatSessionRegistry.clear(playerId);
    }

    @Test
    public void lionSkillAppliesFourHitsOverTime() {
        long playerId = 100_000_000_121L;
        TimedSceneHandler handler = new TimedSceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(context, GuidanceMainMapMonsterEnterReq.newBuilder()
                .setChapterId(10200305).build());
        long bossId = handler.waveUnitIdAt(playerId, 0);
        CombatSession session = CombatSessionRegistry.get(playerId);
        double hp = session.getMonster(bossId).getCurrentHp();
        handler.useGuidanceMainMapSkill(context, GuidanceMainMapUseSkillReq.newBuilder()
                .setSkillId(10150510101L).setTargetId(bossId).build());
        int hit = 0;
        for (long time : new long[] { 200, 333, 467, 833 }) {
            handler.advanceTo(time - 1);
            assertEquals(hp - 753 * hit, session.getMonster(bossId).getCurrentHp(), 0);
            handler.advanceTo(time);
            assertEquals(hp - 753 * ++hit, session.getMonster(bossId).getCurrentHp(), 0);
        }
        CombatSessionRegistry.clear(playerId);
    }

    private static final class TimedSceneHandler extends SceneHandler {
        private long now;
        private final java.util.TreeMap<Long, List<Runnable>> pending = new java.util.TreeMap<>();

        @Override
        void scheduleCombatAction(IPlayerContext context, long delayMillis, Runnable action) {
            pending.computeIfAbsent(now + delayMillis, key -> new ArrayList<>()).add(action);
        }

        private void advanceTo(long time) {
            while (!pending.isEmpty() && pending.firstKey() <= time) {
                java.util.Map.Entry<Long, List<Runnable>> next = pending.pollFirstEntry();
                now = next.getKey();
                next.getValue().forEach(Runnable::run);
            }
            now = time;
        }
    }

    /** 目标已死时必须回 UseSkillResp，否则客户端会锁在施法里。 */
    @Test
    public void deadSkillTargetStillGetsUseSkillAck() {
        long playerId = 100_000_000_115L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200201)
                        .build());
        long deadId = playerId * 1000 + 999;
        context.writes.clear();

        handler.useGuidanceMainMapSkill(
                context,
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10111110101L)
                        .setTargetId(deadId)
                        .build());

        UseSkillResp use = (UseSkillResp) context.writes.stream()
                .filter(write -> write.protocolId == 50762)
                .findFirst().get().message;
        assertEquals(10111110101L, use.getSkillId());
        CombatSessionRegistry.clear(playerId);
    }

    /** 剧情 20010 读完必须回 51157，否则人会冻住、特效不播。 */
    @Test
    public void storyReadFinishAcksCapturedStory20010() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_116L);
        handler.finishStoryRead(
                context,
                StoryReadFinishReq.newBuilder()
                        .setFinishStoryId(20010)
                        .build());
        assertEquals(51157, context.writes.get(0).protocolId);
        StoryReadFinishResp resp =
                (StoryReadFinishResp) context.writes.get(0).message;
        assertEquals(20010, resp.getFinishStoryId());
    }

    /** 第九关引导终结技按抓包 idx 8300/8304/8315 击杀 Boss 并完成任务。 */
    @Test
    public void ninthBossFinisherKillsBossAndCompletesTask() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200405)
                        .setZ(1299.3f)
                        .build());
        long bossId = handler.waveUnitIdAt(playerId, 0);
        CombatSession session = CombatSessionRegistry.get(playerId);
        session.getPlayer().applyDamage(session.getPlayer().getMaxHp() - 1);
        NinthBossGuide.tryStart(context, session);
        session.setPaused(false);
        context.writes.clear();

        handler.useGuidanceMainMapSkill(
                context,
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10120110101L)
                        .setTargetId(bossId)
                        .setZ(1296.8f)
                        .setTz(1299.3f)
                        .build());

        UseSkillResp use = (UseSkillResp) context.writes.stream()
                .filter(write -> write.protocolId == 50762)
                .findFirst().get().message;
        assertEquals(10120110102L, use.getSkillId());
        SkillActionResp damage = (SkillActionResp) context.writes.stream()
                .filter(write -> write.protocolId == 50763)
                .findFirst().get().message;
        assertEquals(660074,
                damage.getActionList(0).getDamageVo().getDamage(), 0.0001);
        assertEquals(0,
                damage.getActionList(1).getAttributeActionVo()
                        .getAttrList(0).getValue(), 0.0001);
        assertTrue(context.writes.stream().anyMatch(write ->
                write.protocolId == 50906
                        && containsTask((TaskUpdateResp) write.message,
                                200024, TaskPhase.FINISHED)));
        int guideIndex = -1;
        int resultIndex = -1;
        for (int i = 0; i < context.writes.size(); i++) {
            RecordedWrite write = context.writes.get(i);
            if (write.protocolId == 77066) {
                guideIndex = i;
                assertEquals(10044, ((com.doupo.protocol.PlayerFightGuildCallResp)
                        write.message).getGuildGroupId());
            } else if (write.protocolId == 50781) {
                resultIndex = i;
            }
        }
        assertTrue(guideIndex >= 0 && resultIndex > guideIndex);
    }

    @Test
    public void ninthBossTickStartsCapturedGuideOnceAndCanResume() {
        long playerId = 100_000_000_921L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200405).setZ(1299.3f).build());
        CombatSession session = CombatSessionRegistry.get(playerId);
        SceneUnitVo.Builder snapshot = session.getNinthBossPlayerSnapshot().toBuilder();
        snapshot.getHeroVoBuilder().getCrossHeroShortInfoBuilder().setLevel(9).setStage(2);
        session.setNinthBossPlayerSnapshot(snapshot.build());
        session.getPlayer().applyDamage(session.getPlayer().getMaxHp() - 1);
        session.addMp(43);
        context.writes.clear();

        com.doupo.server.module.combat.CombatTickProcessor.onTick(null,
                new com.doupo.server.module.combat.CombatTick(context, session));

        int[] expected = {50756, 50801, 77066, 50798, 50798};
        assertEquals(expected.length, context.writes.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], context.writes.get(i).protocolId);
        }
        SceneUpdateVisibleResp visible = (SceneUpdateVisibleResp) context.writes.get(0).message;
        assertFalse(visible.getSnapshot());
        SceneUnitVo hero = visible.getVisibleList(0);
        assertEquals(9, hero.getHeroVo().getCrossHeroShortInfo().getLevel());
        assertEquals(2, hero.getHeroVo().getCrossHeroShortInfo().getStage());
        assertEquals(5, hero.getHeroVo().getSkillContainerVo().getSkillListCount());
        assertEquals(1, hero.getFightInfoVo().getAttributeListList().stream()
                .filter(attr -> attr.getType() == 103011).findFirst().get().getValue(), 0.0001);
        AttributeActionVO restored = (AttributeActionVO) context.writes.get(1).message;
        assertEquals(playerId * 1000 + 1, restored.getTargetId());
        assertEquals(session.getPlayer().getMaxHp(), restored.getAttrList(0).getValue(), 0.0001);
        assertEquals(10043, ((com.doupo.protocol.PlayerFightGuildCallResp)
                context.writes.get(2).message).getGuildGroupId());
        assertEquals(0, ((MpResp) context.writes.get(3).message).getMp(), 0.0001);
        assertEquals(80, ((MpResp) context.writes.get(4).message).getMp(), 0.0001);
        assertTrue(session.isActive());
        assertTrue(session.isPaused());
        assertTrue(session.tick(Long.MAX_VALUE / 2).isEmpty());

        context.writes.clear();
        NinthBossGuide.tryStart(context, session);
        assertTrue(context.writes.isEmpty());
        handler.pauseGuidanceMainMap(context,
                GuidanceMainMapPauseReq.newBuilder().setPause(false).build());
        session.updatePlayerPosition(0, 0, 1299.3f);
        assertFalse(session.tick(Long.MAX_VALUE / 2).isEmpty());
        session.getPlayer().applyDamage(session.getPlayer().getCurrentHp() - 1);
        NinthBossGuide.tryStart(context, session);
        assertTrue(context.writes.stream().noneMatch(write -> write.protocolId == 77066));
        CombatSessionRegistry.clear(playerId);
    }

    /** 第 9 关引导打开斗技必须回 50459，对应抓包 idx 8225-8226。 */
    @Test
    public void ninthBossGuideHeroOriginNodeStatAcksCapturedPower() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_001_010L);
        LotteryDrawReq draw = LotteryDrawReq.newBuilder()
                .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                .setTen(false)
                .build();
        prepareFifthDrawEnergy(handler, context, draw);
        handler.drawNewFightSkill(context, draw);
        handler.starUpHeroSkill(
                context,
                HeroSkillStarUpReq.newBuilder()
                        .setHeroIndex(0)
                        .setSkillBaseId(80130101)
                        .addItem2CostNums(
                                com.doupo.protocol.IntegerAndLongPairEntry
                                        .newBuilder()
                                        .setKey(80130100)
                                        .setValue(1))
                        .build());
        context.writes.clear();

        handler.queryHeroOriginNodeStat(
                context,
                HeroOriginNodeStatReq.newBuilder()
                        .setHeroIndex(0)
                        .setStatShowNode(3)
                        .build());

        assertEquals(1, context.writes.size());
        assertEquals(50459, context.writes.get(0).protocolId);
        HeroOriginNodeStatResp resp =
                (HeroOriginNodeStatResp) context.writes.get(0).message;
        assertEquals(0, resp.getHeroVo().getHeroIndex());
        assertEquals(3, resp.getHeroVo().getStatShowNodeVo().getStatShowNode());
        assertEquals(0, resp.getHeroVo().getStatShowNodeVo().getFightForce(), 0);
        assertEquals(25778, resp.getHeroVo().getHeroPower(), 0);
        assertEquals(0, resp.getHeroVo().getHeroNodePower(), 0);
    }

    /**
     * 第 9 关 Boss 引导暂停时普攻 10111120101 必须 failCode=7，对应抓包 idx 8230-8233。
     */
    @Test
    public void ninthBossGuidePausedNormalAttackIsRejected() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200405)
                        .setZ(1299.3f)
                        .build());
        long bossId = handler.waveUnitIdAt(playerId, 0);
        CombatSession guideSession = CombatSessionRegistry.get(playerId);
        guideSession.getPlayer().applyDamage(guideSession.getPlayer().getMaxHp() - 1);
        NinthBossGuide.tryStart(context, guideSession);
        handler.pauseGuidanceMainMap(
                context,
                GuidanceMainMapPauseReq.newBuilder()
                        .setPause(true)
                        .build());
        context.writes.clear();

        handler.useGuidanceMainMapSkill(
                context,
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10111120101L)
                        .setTargetId(bossId)
                        .setZ(1296.8f)
                        .setTz(1299.3f)
                        .build());

        assertEquals(75011, context.writes.get(0).protocolId);
        HeroSkillActResp act = (HeroSkillActResp) context.writes.get(0).message;
        assertEquals(101001, act.getPlayerSkill().getBaseId());
        UseSkillResp use = (UseSkillResp) context.writes.get(1).message;
        assertEquals(50762, context.writes.get(1).protocolId);
        assertEquals(10111120101L, use.getSkillId());
        assertEquals(7, use.getFailCode());
        assertFalse(context.writes.stream().anyMatch(write ->
                write.protocolId == 50763));
    }

    /** 第五关任务链会重复进入同一章节，第二轮必须换一批怪物 ID 并重置击杀状态。 */
    @Test
    public void repeatedFifthChapterResetsKilledMonsterState() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100501)
                        .build());
        long firstRoundId = handler.waveUnitIdAt(playerId, 0);
        for (int i = 0; i < 4; i++) {
            handler.killGuidanceMonster(
                    context,
                    com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                            .setMonsterId(firstRoundId + i)
                            .build());
        }

        context.writes.clear();
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100501)
                        .build());
        long secondRoundId = handler.waveUnitIdAt(playerId, 0);
        // 第二轮是一批新的怪；沿用上一轮的 ID，客户端会复用旧对象，
        // 上一轮的 50757 也会把新怪删掉。
        assertTrue(secondRoundId >= firstRoundId + 4);

        context.writes.clear();
        handler.killGuidanceMonster(
                context,
                com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                        .setMonsterId(secondRoundId)
                        .build());

        assertEquals(2, context.writes.size());
        assertEquals(50793, context.writes.get(0).protocolId);
        assertEquals(50757, context.writes.get(1).protocolId);
        assertEquals(secondRoundId,
                ((SceneForgetVisibleResp) context.writes.get(1).message)
                        .getForgetIds(0));
    }

    /** 第三关 Boss 死亡后必须按抓包 idx 1053 完成主线 200006。 */
    @Test
    public void thirdBossKillWritesCapturedTaskCompletion() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100305)
                        .build());
        context.writes.clear();

        handler.killGuidanceMonster(
                context,
                com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                        .setMonsterId(handler.waveUnitIdAt(100_000_000_001L, 0))
                        .build());

        assertEquals(50402, context.writes.get(0).protocolId);
        PackUpdateResp pack =
                (PackUpdateResp) context.writes.get(0).message;
        assertEquals(61951, pack.getOperationType());
        assertEquals(100200,
                pack.getPacks(0)
                        .getUpdateItems(0)
                        .getPackItem()
                        .getKey());
        assertEquals(3,
                pack.getPacks(0)
                        .getUpdateItems(0)
                        .getPackItem()
                        .getSize());

        List<TaskUpdateResp> updates = new ArrayList<>();
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 50906) {
                updates.add((TaskUpdateResp) write.message);
            }
        }

        assertEquals(1, updates.size());
        TaskUpdateResp completion = updates.get(0);
        assertEquals(5, completion.getTaskVosCount());
        assertTask(completion, 0, 1104, TaskPhase.PROGRESS, 29);
        assertTask(completion, 1, 1107, TaskPhase.PROGRESS, 29);
        assertTask(completion, 2, 1110, TaskPhase.PROGRESS, 29);
        assertTask(completion, 3, 1113, TaskPhase.PROGRESS, 29);
        assertTask(completion, 4, 200006, TaskPhase.FINISHED, 1);
    }

    /** 第三关任务 200006 领奖必须逐条匹配抓包 idx 1074-1081。 */
    @Test
    public void thirdBossTaskRewardMatchesCapturedResponses() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200006))
                        .build());

        int[] protocolIds = {
                50402, 50406, 53702, 50852, 50384, 50906, 50906
        };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        PackUpdateResp pack =
                (PackUpdateResp) context.writes.get(0).message;
        assertEquals(50907, pack.getOperationType());
        assertEquals(100200,
                pack.getPacks(0)
                        .getUpdateItems(0)
                        .getPackItem()
                        .getKey());
        assertEquals(10,
                pack.getPacks(0)
                        .getUpdateItems(0)
                        .getPackItem()
                        .getSize());

        RewardResp reward = (RewardResp) context.writes.get(1).message;
        assertEquals(100200, reward.getRewardItemVos(0).getItemKey());
        assertEquals(10L, reward.getRewardItemVos(0).getAmount());

        ModuleNewOpenResp modules =
                (ModuleNewOpenResp) context.writes.get(3).message;
        assertEquals(1, modules.getOpensCount());
        assertEquals(4008, modules.getOpens(0));

        assertCumulateLoginDays(
                (PlayerCumulateLoginDaysResp)
                        context.writes.get(4).message,
                new int[] {
                        102, 4007, 103, 1608, 4008, 4201, 4202,
                        107, 112, 4304, 4401, 4411, 42011, 4415
                });

        assertTask((TaskUpdateResp) context.writes.get(5).message,
                0, 200006, TaskPhase.REWARDED, 1);
        assertTask((TaskUpdateResp) context.writes.get(6).message,
                0, 200007, TaskPhase.PROGRESS, 0);
    }

    /** 纳戒引导首次抽取必须逐条匹配抓包 idx 1091-1106。 */
    @Test
    public void firstNewFightSkillLotteryMatchesCapturedResponses() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        prepareFirstDrawEnergy(handler, context);

        handler.drawNewFightSkill(
                context,
                LotteryDrawReq.newBuilder()
                        .setLotteryType(
                                LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                        .setTen(false)
                        .setSubId(0)
                        .build());

        int[] protocolIds = {
                50402, 50402, 77352, 77354,
                50402, 75011, 75049, 75039,
                60751, 50906, 50455, 50801,
                50801, 52351, 75151, 50906
        };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        PackUpdateResp consume =
                (PackUpdateResp) context.writes.get(0).message;
        assertEquals(77351, consume.getOperationType());
        assertEquals(100200,
                consume.getPacks(0).getUpdateItems(0)
                        .getPackItem().getKey());
        assertEquals(6,
                consume.getPacks(0).getUpdateItems(0)
                        .getPackItem().getSize());

        LotteryDrawResp draw =
                (LotteryDrawResp) context.writes.get(2).message;
        assertEquals(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL,
                draw.getLotteryType());
        assertFalse(draw.getTen());
        assertEquals(80130010,
                draw.getRewardItemVos(0).getItemKey());
        assertEquals(7001002, draw.getHitPondIds(0));
        assertFalse(draw.getFailure());

        LotteryInfoResp info =
                (LotteryInfoResp) context.writes.get(3).message;
        assertEquals(1, info.getTotalDrawTime());
        assertEquals(1, info.getTodayDrawTime());
        assertEquals(-1, info.getMustRewardNeedTime());
        assertEquals(1,
                info.getNewFightSkillLotteryVo()
                        .getShowStage2DrawCountInfoList(0).getValue());

        TaskUpdateResp taskUpdate =
                (TaskUpdateResp) context.writes.get(9).message;
        assertTask(taskUpdate, 0, 200007, TaskPhase.FINISHED, 1);
        assertTask(taskUpdate, 1, 2201006, TaskPhase.PROGRESS, 1);

        PlayerFightForceResp force =
                (PlayerFightForceResp) context.writes.get(13).message;
        assertEquals(14188, force.getPlayerFightForce(), 0.0001);
    }

    /** 纳戒第二次引导抽取必须逐条匹配抓包 idx 1401-1406。 */
    @Test
    public void secondNewFightSkillLotteryMatchesCapturedResponses() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);
        LotteryDrawReq request = LotteryDrawReq.newBuilder()
                .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                .setTen(false)
                .setSubId(0)
                .build();

        prepareSecondDrawEnergy(handler, context, request);
        handler.drawNewFightSkill(context, request);

        int[] protocolIds = { 50402, 50402, 77352, 77354, 50906 };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        PackUpdateResp consume =
                (PackUpdateResp) context.writes.get(0).message;
        assertEquals(77351, consume.getOperationType());
        assertEquals(8, consume.getPacks(0).getUpdateItems(0)
                .getPackItem().getSize());

        PackUpdateResp reward =
                (PackUpdateResp) context.writes.get(1).message;
        assertEquals(77353, reward.getOperationType());
        assertEquals(5, reward.getPacks(0).getUpdateItems(0).getItemIndex());
        assertEquals(80130010, reward.getPacks(0).getUpdateItems(0)
                .getPackItem().getKey());

        LotteryInfoResp info =
                (LotteryInfoResp) context.writes.get(3).message;
        assertEquals(2, info.getTotalDrawTime());
        assertEquals(-2, info.getMustRewardNeedTime());
        assertEquals(2, info.getNewFightSkillLotteryVo()
                .getShowStage2DrawCountInfoList(0).getValue());

        assertTask((TaskUpdateResp) context.writes.get(4).message,
                0, 2201006, TaskPhase.PROGRESS, 2);
    }

    /** 第三次纳戒抽取必须推进累计到 3，对应抓包 idx 1481-1485。 */
    @Test
    public void thirdNewFightSkillLotteryMatchesCapturedResponses() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);
        LotteryDrawReq request = LotteryDrawReq.newBuilder()
                .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                .build();

        prepareThirdDrawEnergy(handler, context, request);
        handler.drawNewFightSkill(context, request);

        int[] protocolIds = { 50402, 50402, 77352, 77354, 50906 };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }
        PackUpdateResp consume =
                (PackUpdateResp) context.writes.get(0).message;
        assertEquals(1,
                consume.getPacks(0).getUpdateItems(0).getItemIndex());
        assertFalse(consume.getPacks(0).getUpdateItems(0).hasPackItem());
        LotteryInfoResp info =
                (LotteryInfoResp) context.writes.get(3).message;
        assertEquals(3, info.getTotalDrawTime());
        assertEquals(3, info.getNewFightSkillLotteryVo()
                .getShowStage2DrawCountInfoList(0).getValue());
        assertTask((TaskUpdateResp) context.writes.get(4).message,
                0, 2201006, TaskPhase.PROGRESS, 3);
    }

    /** 第五抽必须激活斗技并完成任务 200011，对应抓包 idx 1745-1761。 */
    @Test
    public void fifthNewFightSkillLotteryFinishesTask200011() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);
        LotteryDrawReq request = LotteryDrawReq.newBuilder()
                .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                .build();

        prepareFifthDrawEnergy(handler, context, request);
        handler.drawNewFightSkill(context, request);

        int[] protocolIds = {
                50402, 50402, 77352, 77354, 50402, 75011, 75049,
                75039, 60751, 60751, 50906, 50455, 50801, 50801,
                52351, 75151, 50906
        };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }
        PackUpdateResp consume =
                (PackUpdateResp) context.writes.get(0).message;
        assertEquals(8,
                consume.getPacks(0).getUpdateItems(0).getItemIndex());
        assertFalse(consume.getPacks(0).getUpdateItems(0).hasPackItem());
        LotteryDrawResp draw =
                (LotteryDrawResp) context.writes.get(2).message;
        assertEquals(2, draw.getRewardItemVosCount());
        assertEquals(80140000, draw.getRewardItemVos(0).getItemKey());
        assertEquals(80130100, draw.getRewardItemVos(1).getItemKey());
        LotteryInfoResp info =
                (LotteryInfoResp) context.writes.get(3).message;
        assertEquals(5, info.getTotalDrawTime());
        TaskUpdateResp taskUpdate =
                (TaskUpdateResp) context.writes.get(10).message;
        assertTask(taskUpdate, 0, 200011, TaskPhase.FINISHED, 1);
        assertTask(taskUpdate, 1, 2201006, TaskPhase.FINISHED, 5);
    }

    /** 第 6 抽起抽到未激活的斗技道具必须下发激活链路（抓包 idx 1942-1945）。 */
    @Test
    public void postGuideDrawActivatesNewFightSkill() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);
        LotteryDrawReq draw = LotteryDrawReq.newBuilder()
                .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                .setTen(false)
                .setSubId(0)
                .build();

        // 抽到第 5 抽（累计 5）。
        prepareFifthDrawEnergy(handler, context, draw);
        handler.drawNewFightSkill(context, draw);
        // 攒能量：第五关 Boss +20、领 200011 +2。
        killGuidanceBoss(handler, context, 10100505, 505);
        rewardTask(handler, context, 200011);
        context.writes.clear();

        // 第 6 抽（energy 22 → 一次抽 2 下、累计 7：两张 80128010，激活 80128011）。
        handler.drawNewFightSkill(context, draw);

        boolean activatedXuanYan = false;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 75011) {
                HeroSkillActResp act = (HeroSkillActResp) write.message;
                if (act.getPlayerSkill().getBaseId() == 80128011L) {
                    activatedXuanYan = true;
                    assertEquals(2, act.getPlayerSkill()
                            .getHeroSkills(0).getHeroSkill().getStar());
                }
            }
        }
        assertTrue(activatedXuanYan);
    }

    /** 纳戒升级（showStage 1→2）必须按抓包 idx 1678-1680 刷新纳戒等级。 */
    @Test
    public void upgradeLotteryPoolRaisesStageToTwo() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);
        LotteryDrawReq draw = LotteryDrawReq.newBuilder()
                .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                .setTen(false)
                .setSubId(0)
                .build();
        // 前三次各抽 1 下，第四次请求按余额 20 一次抽 2 下，累计 5。
        prepareFifthDrawEnergy(handler, context, draw);
        handler.drawNewFightSkill(context, draw);
        context.writes.clear();

        handler.upgradeLotteryPool(
                context,
                LotteryPoolUpReq.newBuilder()
                        .setLotteryType(
                                LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                        .setSubId(0)
                        .build());
        int[] protocolIds = { 77354, 77364 };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        LotteryInfoResp info =
                (LotteryInfoResp) context.writes.get(0).message;
        assertEquals(5, info.getTotalDrawTime());
        assertEquals(5, info.getTodayDrawTime());
        assertEquals(-5, info.getMustRewardNeedTime());
        assertEquals(5, info.getExtraDrawTime());
        assertEquals(2, info.getNewFightSkillLotteryVo().getShowStage());
        assertEquals(1, info.getNewFightSkillLotteryVo()
                .getShowStage2DrawCountInfoListCount());
        assertEquals(1, info.getNewFightSkillLotteryVo()
                .getShowStage2DrawCountInfoList(0).getKey());
        assertEquals(5, info.getNewFightSkillLotteryVo()
                .getShowStage2DrawCountInfoList(0).getValue());

        LotteryPoolUpResp up =
                (LotteryPoolUpResp) context.writes.get(1).message;
        assertEquals(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL,
                up.getLotteryType());
        assertEquals(0, up.getSubId());
    }

    /** 累计抽卡不足时纳戒升级必须被静默忽略。 */
    @Test
    public void upgradeLotteryPoolIgnoredWhenDrawTimeInsufficient() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_002L);
        LotteryDrawReq draw = LotteryDrawReq.newBuilder()
                .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                .build();
        // 只抽 3 次（totalDrawTime=3 < 5）。
        prepareThirdDrawEnergy(handler, context, draw);
        handler.drawNewFightSkill(context, draw);
        context.writes.clear();

        handler.upgradeLotteryPool(
                context,
                LotteryPoolUpReq.newBuilder()
                        .setLotteryType(
                                LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                        .setSubId(0)
                        .build());

        assertEquals(0, context.writes.size());
    }

    /** 引导后（drawTime>4）单抽：能量不足（8 < 10）时返回失败且不扣能量。 */
    @Test
    public void generalSingleDrawFailsWhenEnergyInsufficient() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);
        LotteryDrawReq draw = LotteryDrawReq.newBuilder()
                .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                .setTen(false)
                .build();
        // 官服首次 16 能量扣 10 后余 6，再点必须失败（抓包 idx 1137-1138）。
        prepareFirstDrawEnergy(handler, context);
        handler.drawNewFightSkill(context, draw);
        context.writes.clear();
        handler.drawNewFightSkill(context, draw);

        assertEquals(1, context.writes.size());
        assertEquals(77352, context.writes.get(0).protocolId);
        LotteryDrawResp resp =
                (LotteryDrawResp) context.writes.get(0).message;
        assertEquals(true, resp.getFailure());
    }

    /** 引导后十连：能量不足（8 < 100）时返回失败，不发射能量扣减 pack。 */
    @Test
    public void generalTenDrawFailsWhenEnergyInsufficient() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_002L);
        LotteryDrawReq draw = LotteryDrawReq.newBuilder()
                .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                .setTen(false)
                .build();
        prepareFirstDrawEnergy(handler, context);
        handler.drawNewFightSkill(context, draw);
        context.writes.clear();
        handler.drawNewFightSkill(
                context,
                LotteryDrawReq.newBuilder()
                        .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                        .setTen(true)
                        .build());

        assertEquals(1, context.writes.size());
        assertEquals(77352, context.writes.get(0).protocolId);
        LotteryDrawResp resp =
                (LotteryDrawResp) context.writes.get(0).message;
        assertEquals(true, resp.getFailure());
    }

    /** 引导后单抽按能量算本次次数：28 能量 → 抽 2 下、一次性扣 20、一次性返回 2 个（抓包 idx 1937 同语义）。 */
    @Test
    public void generalDrawConsumesEnergyTimesCount() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_003L);
        LotteryDrawReq draw = LotteryDrawReq.newBuilder()
                .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                .setTen(false)
                .build();
        // 前 5 抽后升级到 Lv.2。
        prepareFifthDrawEnergy(handler, context, draw);
        handler.drawNewFightSkill(context, draw);
        handler.upgradeLotteryPool(
                context,
                LotteryPoolUpReq.newBuilder()
                        .setLotteryType(
                                LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                        .build());
        // 抓包 idx 1838 的任务奖励 +2，idx 1920 的第五关 Boss 奖励 +20。
        rewardTask(handler, context, 200011);
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100505)
                        .build());
        handler.killGuidanceMonster(
                context,
                com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                        .setMonsterId(100_000_000_003L * 1000 + 505)
                        .build());
        context.writes.clear();

        // Lv.2 单次请求：能量 22 → 抽 2 下、扣 20、剩余 2（抓包 idx 1937-1941）。
        handler.drawNewFightSkill(context, draw);

        assertEquals(50402, context.writes.get(0).protocolId);
        PackUpdateResp energyPack =
                (PackUpdateResp) context.writes.get(0).message;
        assertEquals(100200,
                energyPack.getPacks(0).getUpdateItems(0).getPackItem().getKey());
        assertEquals(11,
                energyPack.getPacks(0).getUpdateItems(0).getItemIndex());
        assertEquals(2,
                energyPack.getPacks(0).getUpdateItems(0).getPackItem().getSize());

        LotteryDrawResp resp =
                (LotteryDrawResp) context.writes.get(2).message;
        assertEquals(77352, context.writes.get(2).protocolId);
        assertEquals(false, resp.getFailure());
        assertEquals(2, resp.getRewardItemVosCount());
        assertEquals(80128010, resp.getRewardItemVos(0).getItemKey());
        assertEquals(80128010, resp.getRewardItemVos(1).getItemKey());
        assertEquals(2, resp.getHitPondIdsCount());
        assertEquals(7002009, resp.getHitPondIds(0));
        assertEquals(7002009, resp.getHitPondIds(1));

        PackUpdateResp rewardPack =
                (PackUpdateResp) context.writes.get(1).message;
        assertEquals(77353, rewardPack.getOperationType());
        assertEquals(1, rewardPack.getPacks(0).getUpdateItemsCount());
        assertEquals(12, rewardPack.getPacks(0).getUpdateItems(0).getItemIndex());
        assertEquals(80128010,
                rewardPack.getPacks(0).getUpdateItems(0).getPackItem().getKey());
        assertEquals(2,
                rewardPack.getPacks(0).getUpdateItems(0).getPackItem().getSize());

        PackUpdateResp actCost =
                (PackUpdateResp) context.writes.get(4).message;
        assertEquals(75004, actCost.getOperationType());
        assertEquals(12, actCost.getPacks(0).getUpdateItems(0).getItemIndex());
        assertEquals(1,
                actCost.getPacks(0).getUpdateItems(0).getPackItem().getSize());
        HeroSkillActResp act =
                (HeroSkillActResp) context.writes.get(5).message;
        assertEquals(80128011, act.getPlayerSkill().getBaseId());
        assertEquals(2, act.getPlayerSkill().getHeroSkills(0).getHeroSkill().getStar());
        HeroSkillStarTotalMaxHisUpdateResp maxHis =
                (HeroSkillStarTotalMaxHisUpdateResp) context.writes.get(6).message;
        assertEquals(7, maxHis.getSkillStarTotalMaxHis());

        LotteryInfoResp info =
                (LotteryInfoResp) context.writes.get(3).message;
        assertEquals(77354, context.writes.get(3).protocolId);
        assertEquals(7, info.getTotalDrawTime());
        assertEquals(2, info.getNewFightSkillLotteryVo().getShowStage());
        assertEquals(2, info.getNewFightSkillLotteryVo()
                .getShowStage2DrawCountInfoListCount());
        assertEquals(1, info.getNewFightSkillLotteryVo()
                .getShowStage2DrawCountInfoList(0).getKey());
        assertEquals(5, info.getNewFightSkillLotteryVo()
                .getShowStage2DrawCountInfoList(0).getValue());
        assertEquals(2, info.getNewFightSkillLotteryVo()
                .getShowStage2DrawCountInfoList(1).getKey());
        assertEquals(2, info.getNewFightSkillLotteryVo()
                .getShowStage2DrawCountInfoList(1).getValue());
    }

    /** 200010/200011 领奖必须依次开启后续主线任务。 */
    @Test
    public void fourthLevelAndFifthLotteryRewardsOpenNextTasks() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_215L);

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200010))
                        .build());
        assertEquals(5, context.writes.size());
        PackUpdateResp levelReward =
                (PackUpdateResp) context.writes.get(0).message;
        assertEquals(8, levelReward.getPacks(0)
                .getUpdateItems(0).getItemIndex());
        assertEquals(20, levelReward.getPacks(0)
                .getUpdateItems(0).getPackItem().getSize());
        assertTask((TaskUpdateResp) context.writes.get(3).message,
                0, 200010, TaskPhase.REWARDED, 1);
        assertTask((TaskUpdateResp) context.writes.get(4).message,
                0, 200011, TaskPhase.PROGRESS, 0);

        context.writes.clear();
        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200011))
                        .build());
        assertEquals(5, context.writes.size());
        assertTask((TaskUpdateResp) context.writes.get(3).message,
                0, 200011, TaskPhase.REWARDED, 1);
        assertTask((TaskUpdateResp) context.writes.get(4).message,
                0, 200012, TaskPhase.PROGRESS, 0);
    }

    /** 已经打进第6关后再领 200011，到达第6关的 200012 必须直接 finished。 */
    @Test
    public void arriveChapter6TaskAutoFinishesIfAlreadyEntered() {
        long playerId = 100_000_000_216L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200101)
                        .setZ(22.93f)
                        .build());
        handler.useGuidanceMainMapSkill(
                context,
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10110710101L)
                        .setTargetId(handler.waveUnitIdAt(playerId, 0))
                        .build());
        context.writes.clear();

        rewardTask(handler, context, 200011);
        assertTrue(containsTask(
                lastTaskUpdate(context), 200012, TaskPhase.FINISHED));
        CombatSessionRegistry.clear(playerId);
    }

    /** 人已经打到第8关时，出手必须补完卡住的 200012。 */
    @Test
    public void eighthChapterSkillFinishesStuckArriveChapter6Task() {
        long playerId = 100_000_000_217L;
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context = new RecordingPlayerContext(playerId);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200301)
                        .setZ(462.93f)
                        .build());
        context.writes.clear();
        handler.useGuidanceMainMapSkill(
                context,
                GuidanceMainMapUseSkillReq.newBuilder()
                        .setSkillId(10110710101L)
                        .setTargetId(handler.waveUnitIdAt(playerId, 0))
                        .build());
        assertTrue(context.writes.stream()
                .filter(write -> write.protocolId == 50906)
                .map(write -> write.message)
                .filter(message -> message instanceof TaskUpdateResp)
                .map(message -> (TaskUpdateResp) message)
                .anyMatch(resp -> containsTask(
                        resp, 200012, TaskPhase.FINISHED)));
        CombatSessionRegistry.clear(playerId);
    }

    /** 第六关任务 200012 领奖必须按抓包 idx 4919-4922+4928 下发灵液并开启模块。 */
    @Test
    public void sixthChapterTaskRewardMatchesCapturedResponses() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200012))
                        .build());

        int[] protocolIds = {
            50402, 50406, 53702, 50852, 77006, 77001, 50906, 50906
        };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        PackUpdateResp pack =
                (PackUpdateResp) context.writes.get(0).message;
        assertEquals(50907, pack.getOperationType());
        assertEquals(13, pack.getPacks(0).getUpdateItems(0).getItemIndex());
        assertEquals(101, pack.getPacks(0).getUpdateItems(0)
                .getPackItem().getKey());
        assertEquals(1, pack.getPacks(0).getUpdateItems(0)
                .getPackItem().getSize());

        RewardResp reward = (RewardResp) context.writes.get(1).message;
        assertEquals(50907, reward.getOperationType());
        assertEquals(101, reward.getRewardItemVos(0).getItemKey());
        assertEquals(1, reward.getRewardItemVos(0).getAmount());

        ReputationLvOnTaskRewardUpdateResp reputation =
                (ReputationLvOnTaskRewardUpdateResp)
                        context.writes.get(2).message;
        assertEquals(200012, reputation.getTaskId());
        assertEquals(0, reputation.getReputationLv());

        ModuleNewOpenResp modules =
                (ModuleNewOpenResp) context.writes.get(3).message;
        assertEquals(3, modules.getOpensCount());
        assertEquals(1600, modules.getOpens(0));
        assertEquals(1601, modules.getOpens(1));
        assertEquals(1510, modules.getOpens(2));

        // 第六关领奖后首充弹窗推送（抓包 idx 4924/4925，chargeId=1 未购买）。
        RealFirstChargeUpdateResp chargeUpdate =
                (RealFirstChargeUpdateResp) context.writes.get(4).message;
        assertEquals(1, chargeUpdate.getUpdatesCount());
        assertEquals(1, chargeUpdate.getUpdates(0).getChargeId());
        assertEquals(0, chargeUpdate.getUpdates(0).getBuyMillis());
        RealFirstChargeInfoResp chargeInfo =
                (RealFirstChargeInfoResp) context.writes.get(5).message;
        assertEquals(1, chargeInfo.getDayRewardsCount());
        assertEquals(1, chargeInfo.getDayRewards(0).getChargeId());

        assertTask((TaskUpdateResp) context.writes.get(6).message,
                0, 200012, TaskPhase.REWARDED, 1);
        assertTask((TaskUpdateResp) context.writes.get(7).message,
                0, 200013, TaskPhase.PROGRESS, 0);
    }

    /** 开服第 2 天领 200012：抓包第二次 idx 1560/1561，chargeId=1/2/3。 */
    @Test
    public void secondOpenDayFirstChargeUnlocksThirtyAndNinetyEight() {
        SceneHandler handler = new SceneHandler();
        handler.setServerOpenDay(2);
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_301L);

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200012))
                        .build());
        RealFirstChargeInfoResp chargeInfo = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 77001) {
                chargeInfo = (RealFirstChargeInfoResp) write.message;
            }
        }
        assertTrue(chargeInfo != null);
        assertEquals(3, chargeInfo.getDayRewardsCount());
        assertEquals(1, chargeInfo.getDayRewards(0).getChargeId());
        assertEquals(2, chargeInfo.getDayRewards(1).getChargeId());
        assertEquals(3, chargeInfo.getDayRewards(2).getChargeId());
    }

    /** 第 9 关再打开首充：补发 30/98 两档，不改开服当天 6 元那次推送。 */
    @Test
    public void ninthChapterReopensFirstChargeWithExtraTiers() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_302L);

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200012))
                        .build());
        RealFirstChargeInfoResp dayOne = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 77001) {
                dayOne = (RealFirstChargeInfoResp) write.message;
            }
        }
        assertEquals(1, dayOne.getDayRewardsCount());

        context.writes.clear();
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200301)
                        .setZ(22.93f)
                        .build());
        assertFalse(context.writes.stream().anyMatch(
                write -> write.protocolId == 77001));

        context.writes.clear();
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200401)
                        .setZ(22.93f)
                        .build());
        RealFirstChargeInfoResp ninth = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 77001) {
                ninth = (RealFirstChargeInfoResp) write.message;
            }
        }
        assertTrue(ninth != null);
        assertEquals(3, ninth.getDayRewardsCount());
        assertEquals(1, ninth.getDayRewards(0).getChargeId());
        assertEquals(2, ninth.getDayRewards(1).getChargeId());
        assertEquals(3, ninth.getDayRewards(2).getChargeId());
    }

    /** 第 11 关领 200026：开万兽鼎模块 1622，并推未购买的 61501。 */
    @Test
    public void eleventhChapterTaskRewardOpensMedicineCauldron() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_401L);

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200026))
                        .build());

        ModuleNewOpenResp opens = null;
        PlayerCumulateLoginDaysResp days = null;
        MedicineCauldronInfoResp info = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 50852) {
                opens = (ModuleNewOpenResp) write.message;
            }
            if (write.protocolId == 50384) {
                days = (PlayerCumulateLoginDaysResp) write.message;
            }
            if (write.protocolId == 61501) {
                info = (MedicineCauldronInfoResp) write.message;
            }
        }
        assertTrue(opens != null);
        assertEquals(1, opens.getOpensCount());
        assertEquals(1622, opens.getOpens(0));
        assertTrue(days != null);
        boolean cauldronDay = false;
        for (int i = 0; i < days.getModuleOpenId2CumulateLoginDaysCount(); i++) {
            if (days.getModuleOpenId2CumulateLoginDays(i).getKey() == 1622) {
                cauldronDay = true;
                assertEquals(1, days.getModuleOpenId2CumulateLoginDays(i)
                        .getValue());
            }
        }
        assertTrue(cauldronDay);
        assertTrue(info != null);
        assertFalse(info.getHaveBuy());
        assertEquals(0, info.getActiveLevel());
        assertEquals(0, info.getCanUpgradeLevel());
        assertEquals(1, info.getHeroIndex2DailyExtraExpListCount());
        assertEquals(0, info.getHeroIndex2DailyExtraExpList(0).getKey());
        assertEquals(0L, info.getHeroIndex2DailyExtraExpList(0).getValue());
        assertTask(lastTaskUpdate(context), 0, 200027, TaskPhase.PROGRESS, 0);
    }

    /** 打开万兽鼎后客户端拉 61506，回 0 次历史消耗。 */
    @Test
    public void medicineCauldronHistoryConsumeReturnsZero() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_402L);
        MedicineCauldronHistoryConsumeResp resp =
                handler.medicineCauldronHistoryConsume(
                        context,
                        MedicineCauldronHistoryConsumeReq.getDefaultInstance());
        assertEquals(0L, resp.getHistoryConsumeItemNum());
    }

    /** 领取 200012 后 200013 保持 PROGRESS 0；升到 level=5 才 FINISHED。 */
    @Test
    public void heroLevelFiveBreakFinishesTask200013() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_013L);

        rewardTask(handler, context, 200012);
        assertTask(lastTaskUpdate(context), 0, 200013, TaskPhase.PROGRESS, 0);

        breakHeroToLevelFive(handler, context);
        TaskUpdateResp finished = lastTaskUpdate(context);
        assertTrue(containsTask(finished, 200013, TaskPhase.FINISHED));
        assertEquals(1, finished.getTaskVosList().stream()
                .filter(task -> task.getTaskId().getTaskResourceId() == 200013)
                .findFirst()
                .get()
                .getProgressValues(0));
        HeroLevelBreakResp level = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 75067) {
                level = (HeroLevelBreakResp) write.message;
            }
        }
        assertTrue(level != null);
        assertEquals(5, level.getHeroVo().getLevel());
    }

    /** 危机事件 20010 领奖后完成 200014；第 7 关杀怪不能代替该事件。 */
    @Test
    public void crisisEvent20010FinishesTask200014() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_014L);

        rewardTask(handler, context, 200013);
        assertTask(lastTaskUpdate(context), 0, 200014, TaskPhase.PROGRESS, 0);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200201)
                        .build());
        context.writes.clear();
        handler.killGuidanceMonster(
                context,
                com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                        .setMonsterId(context.getId() * 1000 + 201)
                        .build());
        assertFalse(context.writes.stream()
                .filter(write -> write.protocolId == 50906)
                .map(write -> (TaskUpdateResp) write.message)
                .anyMatch(resp -> containsTask(
                        resp, 200014, TaskPhase.FINISHED)));

        context.writes.clear();
        handler.claimCrisisEventReward(
                context,
                CrisisEventRewardReq.newBuilder().setEventId(20010).build());
        assertTrue(context.writes.stream()
                .filter(write -> write.protocolId == 50906)
                .map(write -> (TaskUpdateResp) write.message)
                .anyMatch(resp -> containsTask(
                        resp, 200014, TaskPhase.FINISHED)));
        assertTrue(context.writes.stream()
                .filter(write -> write.protocolId == 76702)
                .map(write -> (CrisisEventUpdateResp) write.message)
                .anyMatch(resp -> resp.getUpdatesCount() > 0
                        && resp.getUpdates(0).getId() == 20018));
        assertTrue(context.writes.stream()
                .filter(write -> write.protocolId == 50906)
                .map(write -> (TaskUpdateResp) write.message)
                .anyMatch(resp -> containsTask(
                        resp, 30201, TaskPhase.PROGRESS)
                        && containsTask(resp, 30202, TaskPhase.PROGRESS)));
        assertFalse(context.writes.stream()
                .anyMatch(write -> write.protocolId == 75651));
        CombatSessionRegistry.clear(context.getId());
    }

    /** 家族试炼领奖后，薰儿节点可解锁第二角色槽。 */
    @Test
    public void fireTargetUnlocksSecondHeroAfterFamilyTrial() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_022L);

        handler.findFireTarget(
                context, com.doupo.protocol.FireTargetFindReq.getDefaultInstance());
        com.doupo.protocol.FireTargetFindResp before =
                (com.doupo.protocol.FireTargetFindResp) context.writes.get(0).message;
        assertEquals(1, before.getNodeId());

        handler.claimCrisisEventReward(
                context,
                CrisisEventRewardReq.newBuilder().setEventId(20010).build());
        context.writes.clear();
        handler.unlockFireTargetRole(
                context,
                com.doupo.protocol.FireTargetRoleUnlockReq.getDefaultInstance());

        assertEquals(75651, context.writes.get(0).protocolId);
        assertEquals(75662, context.writes.get(1).protocolId);
        assertEquals(50453, context.writes.get(2).protocolId);
        assertEquals(50451, context.writes.get(3).protocolId);
        com.doupo.protocol.FireTargetRoleUnlockResp unlock =
                (com.doupo.protocol.FireTargetRoleUnlockResp)
                        context.writes.get(1).message;
        assertEquals(22, unlock.getNodeId());
        assertEquals(true, unlock.getUnlocked());
        com.doupo.protocol.HeroUnlockResp heroUnlock =
                (com.doupo.protocol.HeroUnlockResp) context.writes.get(2).message;
        assertEquals(1, heroUnlock.getUnlockHeroIndex());
        com.doupo.protocol.HeroUnlockIndexesResp indexes =
                (com.doupo.protocol.HeroUnlockIndexesResp)
                        context.writes.get(3).message;
        assertEquals(2, indexes.getUnlockHeroIndexesCount());
        assertEquals(0, indexes.getUnlockHeroIndexes(0));
        assertEquals(1, indexes.getUnlockHeroIndexes(1));
    }

    /** 危机事件 20010 已领奖时，领取 200013 应立即完成 200014。 */
    @Test
    public void claiming200013AutoFinishes200014AfterCrisisReward() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_114L);

        handler.claimCrisisEventReward(
                context,
                CrisisEventRewardReq.newBuilder().setEventId(20010).build());
        context.writes.clear();
        rewardTask(handler, context, 200013);
        assertTrue(containsTask(
                lastTaskUpdate(context), 200014, TaskPhase.FINISHED));
    }

    /** 品质 3 斗技数量达到 1 时完成 200015（危机事件激活 100001）。 */
    @Test
    public void qualityThreeSkillFinishesTask200015() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_015L);

        handler.claimCrisisEventReward(
                context,
                CrisisEventRewardReq.newBuilder().setEventId(20010).build());
        context.writes.clear();
        rewardTask(handler, context, 200014);
        assertTrue(containsTask(
                lastTaskUpdate(context), 200015, TaskPhase.FINISHED));
    }

    /** 通关 10200105 后领取 200015，立即完成 200016。 */
    @Test
    public void passingSixthBossFinishesTask200016() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_016L);

        killGuidanceBoss(handler, context, 10200105, 205);
        context.writes.clear();
        rewardTask(handler, context, 200015);
        assertTrue(containsTask(
                lastTaskUpdate(context), 200016, TaskPhase.FINISHED));
        CombatSessionRegistry.clear(context.getId());
    }

    /** 领取 200016 后 200017 为 PROGRESS 0；纳戒抽取 1 次后 FINISHED。 */
    @Test
    public void lotteryDrawFinishesTask200017() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_017L);

        rewardTask(handler, context, 200016);
        assertTask(lastTaskUpdate(context), 0, 200017, TaskPhase.PROGRESS, 0);

        context.writes.clear();
        handler.drawNewFightSkill(
                context,
                LotteryDrawReq.newBuilder()
                        .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL)
                        .setTen(false)
                        .build());
        assertTrue(containsTask(
                lastTaskUpdate(context), 200017, TaskPhase.FINISHED));
    }

    /** 品质 3 斗技不足 2 个时，领取 200017 不能完成 200018。 */
    @Test
    public void task200018StaysProgressUntilTwoQualityThreeSkills() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_018L);

        handler.claimCrisisEventReward(
                context,
                CrisisEventRewardReq.newBuilder().setEventId(20010).build());
        context.writes.clear();
        rewardTask(handler, context, 200017);
        assertTask(lastTaskUpdate(context), 0, 200018, TaskPhase.PROGRESS, 1);
    }

    /** 领取 200018 后 200020 为 PROGRESS 0；通关 10200205 后 FINISHED。 */
    @Test
    public void seventhBossPassFinishesTask200020() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_100_000_020L);

        rewardTask(handler, context, 200018);
        assertTask(lastTaskUpdate(context), 0, 200020, TaskPhase.PROGRESS, 0);

        context.writes.clear();
        killGuidanceBoss(handler, context, 10200205, 305);
        assertTrue(containsTask(
                lastTaskUpdate(context), 200020, TaskPhase.FINISHED));
        CombatSessionRegistry.clear(context.getId());
    }

    /** 纳戒抽取任务 200007 领奖必须匹配抓包 idx 1152-1156。 */
    @Test
    public void newFightSkillTaskRewardMatchesCapturedResponses() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(TaskUniqueKey.newBuilder()
                                .setTaskResourceId(200007))
                        .build());

        int[] protocolIds = { 50402, 50406, 53702, 50906, 50906 };
        assertEquals(protocolIds.length, context.writes.size());
        for (int i = 0; i < protocolIds.length; i++) {
            assertEquals(protocolIds[i], context.writes.get(i).protocolId);
        }

        PackUpdateResp pack =
                (PackUpdateResp) context.writes.get(0).message;
        assertEquals(50907, pack.getOperationType());
        assertEquals(100200,
                pack.getPacks(0).getUpdateItems(0)
                        .getPackItem().getKey());
        assertEquals(2,
                pack.getPacks(0).getUpdateItems(0)
                        .getPackItem().getSize());

        RewardResp reward = (RewardResp) context.writes.get(1).message;
        assertEquals(100200, reward.getRewardItemVos(0).getItemKey());
        assertEquals(2, reward.getRewardItemVos(0).getAmount());
        assertTask((TaskUpdateResp) context.writes.get(3).message,
                0, 200007, TaskPhase.REWARDED, 1);
        assertTask((TaskUpdateResp) context.writes.get(4).message,
                0, 200008, TaskPhase.PROGRESS, 0);
    }

    /** 第三关四只小怪，抓包 idx 562-565：模板递增而属性相同。 */
    @Test
    public void thirdChapterMonstersMatchCapturedWave() {
        List<SceneUnitVo> monsters = enterChapter(10100301, 1005.22998f);

        assertEquals(4, monsters.size());
        assertEquals(1000301L,
                monsters.get(0).getSceneMonsterVo().getTemplateId());
        assertEquals(1000304L,
                monsters.get(3).getSceneMonsterVo().getTemplateId());
        assertEquals(10100301,
                monsters.get(0).getSceneMonsterVo().getMainMapId());
        assertEquals(1005.22998f,
                monsters.get(0).getBaseInfoVo().getZ(), 0.0001f);
        assertEquals(1050.22998f,
                monsters.get(3).getBaseInfoVo().getZ(), 0.0001f);

        for (SceneUnitVo monster : monsters) {
            assertEquals(121001,
                    monster.getSceneMonsterVo().getMonsterId());
            assertEquals(132.0,
                    attributeOf(monster, 101001), 0.0001);
            assertEquals(2875.0,
                    attributeOf(monster, 103001), 0.0001);
        }
    }

    /** 第三关小怪只累计通用击杀，不能在 Boss 前提前完成 200006。 */
    @Test
    public void thirdChapterMobsDoNotFinishBossTask() {
        TaskUpdateResp update = SceneHandler.killTaskUpdate(
                ChapterConfig.get(10100301), 4);

        assertEquals(5, update.getTaskVosCount());
        for (com.doupo.protocol.TaskVo task : update.getTaskVosList()) {
            assertTrue(task.getTaskId().getTaskResourceId() != 200006);
        }
    }

    @Test
    public void thirdBossMatchesCapturedBoss() {
        List<SceneUnitVo> monsters = enterChapter(10100305, 1472.7f);

        assertEquals(1, monsters.size());
        SceneUnitVo boss = monsters.get(0);
        assertEquals(100001, boss.getSceneMonsterVo().getMonsterId());
        assertEquals(1000340L, boss.getSceneMonsterVo().getTemplateId());
        assertEquals(800.0, attributeOf(boss, 101001), 0.0001);
        assertEquals(12000.0, attributeOf(boss, 103001), 0.0001);
        assertEquals(100200,
                boss.getSceneMonsterVo().getMainMapReward(0).getItemKey());
    }

    @Test
    public void bossCombatUsesChapterPlayerStatsFromCapture() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();

        RecordingPlayerContext third = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(
                third,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100305)
                        .setZ(1472.7f)
                        .build());
        AttributeActionVO thirdAttack =
                (AttributeActionVO) third.writes.get(0).message;
        assertEquals(809.2535,
                thirdAttack.getAttrList(0).getValue(), 0.0001);
        assertEquals(21137,
                CombatSessionRegistry.get(playerId)
                        .getPlayer().getMaxHp(),
                0.0001);

        RecordingPlayerContext fourth = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(
                fourth,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100405)
                        .setZ(472.7f)
                        .build());
        AttributeActionVO fourthAttack =
                (AttributeActionVO) fourth.writes.get(0).message;
        assertEquals(865.007,
                fourthAttack.getAttrList(0).getValue(), 0.0001);
        assertEquals(22874.5,
                CombatSessionRegistry.get(playerId)
                        .getPlayer().getMaxHp(),
                0.0001);

        RecordingPlayerContext sixth = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(
                sixth,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200101)
                        .setZ(22.93f)
                        .build());
        AttributeActionVO sixthAttack =
                (AttributeActionVO) sixth.writes.get(0).message;
        assertEquals(1189.0766800000001,
                sixthAttack.getAttrList(0).getValue(), 0.0001);
        assertEquals(30991.35,
                CombatSessionRegistry.get(playerId)
                        .getPlayer().getMaxHp(),
                0.0001);
        CombatSessionRegistry.clear(playerId);
    }

    /**
     * 第六关三波的玩家攻击与满血必须一致（抓包 idx 2190/2552/2796）。
     *
     * <p>第二、三波曾漏配，导致攻击掉到默认 481.8、满血掉到 12613.2，
     * 玩家在跳台那一段会被打死后重新落地。
     */
    @Test
    public void sixthChapterWavesShareCapturedPlayerStats() {
        long playerId = 100_000_000_001L;
        SceneHandler handler = new SceneHandler();

        for (int chapterId : new int[] { 10200101, 10200102, 10200103 }) {
            RecordingPlayerContext context =
                    new RecordingPlayerContext(playerId);
            handler.enterGuidanceMainMapMonster(
                    context,
                    GuidanceMainMapMonsterEnterReq.newBuilder()
                            .setChapterId(chapterId)
                            .setY(7.025f)
                            .setZ(150.8f)
                            .build());

            AttributeActionVO attack =
                    (AttributeActionVO) context.writes.get(0).message;
            assertEquals(1189.0766800000001,
                    attack.getAttrList(0).getValue(), 0.0001);
            assertEquals(30991.35,
                    CombatSessionRegistry.get(playerId)
                            .getPlayer().getMaxHp(),
                    0.0001);
        }
        CombatSessionRegistry.clear(playerId);
    }

    /**
     * 五段突破拿到的攻击与满血不能被后面几波进场时的关卡基线抹掉。
     *
     * <p>抓包 idx 5542 玩家在第七关第一波中途突破，攻击 1189.077→1433.883、
     * 满血 30991.35→37251.85；idx 5791/6295 之后每次进场下发的都是涨过的值。
     */
    @Test
    public void chapterEntryKeepsStatsGainedFromLevelBreak() {
        long playerId = 100_000_000_107L;
        SceneHandler handler = new SceneHandler();

        RecordingPlayerContext before = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(
                before,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200201)
                        .setZ(22.93f)
                        .build());
        assertEquals(1189.0766800000001,
                ((AttributeActionVO) before.writes.get(0).message)
                        .getAttrList(0).getValue(),
                0.0001);

        breakHeroToLevelFive(handler, new RecordingPlayerContext(playerId));

        RecordingPlayerContext after = new RecordingPlayerContext(playerId);
        handler.enterGuidanceMainMapMonster(
                after,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10200203)
                        .setZ(104.6f)
                        .build());
        assertEquals(1433.88277,
                ((AttributeActionVO) after.writes.get(0).message)
                        .getAttrList(0).getValue(),
                0.0001);
        assertEquals(37251.85,
                CombatSessionRegistry.get(playerId).getPlayer().getMaxHp(),
                0.0001);
        CombatSessionRegistry.clear(playerId);
    }

    /** 第四关四只小怪必须匹配抓包 idx 1123-1126。 */
    @Test
    public void fourthChapterMonstersMatchCapturedWave() {
        List<SceneUnitVo> monsters = enterChapter(10100401, 5.229984f);

        assertEquals(4, monsters.size());
        assertEquals(1000401L,
                monsters.get(0).getSceneMonsterVo().getTemplateId());
        assertEquals(1000404L,
                monsters.get(3).getSceneMonsterVo().getTemplateId());
        assertEquals(121001,
                monsters.get(0).getSceneMonsterVo().getMonsterId());
        assertEquals(13,
                monsters.get(3).getSceneMonsterVo().getMonsterId());
        assertEquals(132, attributeOf(monsters.get(0), 101001), 0.0001);
        assertEquals(4370, attributeOf(monsters.get(0), 103001), 0.0001);
        assertEquals(138, attributeOf(monsters.get(3), 101001), 0.0001);
        assertEquals(3800, attributeOf(monsters.get(3), 103001), 0.0001);
        assertEquals(1500, attributeOf(monsters.get(3), 161001), 0.0001);
        assertEquals(0, attributeOf(monsters.get(3), 165001), 0.0001);
    }

    /** 第三关 Boss 结算必须按抓包 idx 1083 推进到 10100401。 */
    @Test
    public void thirdBossEndFightAdvancesToFourthChapter() {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(10100305)
                        .build());
        handler.killGuidanceMonster(
                context,
                com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                        .setMonsterId(handler.waveUnitIdAt(100_000_000_001L, 0))
                        .build());
        context.writes.clear();

        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(10100305)
                        .build());

        MainMapPassChapterUpdateResp chapterUpdate = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 61952) {
                chapterUpdate = (MainMapPassChapterUpdateResp) write.message;
            }
        }
        assertTrue(chapterUpdate != null);
        assertEquals(10100401, chapterUpdate.getMainMapChapterId());
        assertEquals(11, chapterUpdate.getStageTime());
        assertEquals(14, chapterUpdate.getLastStageTime());
        assertEquals(10100301, chapterUpdate.getLoseBackId());
        assertEquals(10100305, chapterUpdate.getHistoryTopId());
        assertEquals(1, chapterUpdate.getChangeReason());
    }

    /** 章节链路必须首尾相接，否则玩家会卡在某一关无法推进。 */
    @Test
    public void chapterChainAdvancesThroughThirdBoss() {
        assertEquals(10100105,
                ChapterConfig.get(10100101).getNextChapterId());
        assertEquals(10100201,
                ChapterConfig.get(10100105).getNextChapterId());
        assertEquals(10100205,
                ChapterConfig.get(10100201).getNextChapterId());
        assertEquals(10100301,
                ChapterConfig.get(10100205).getNextChapterId());
        assertEquals(10100305,
                ChapterConfig.get(10100301).getNextChapterId());
        assertEquals(10100401,
                ChapterConfig.get(10100305).getNextChapterId());

        assertTrue(ChapterConfig.get(10100105).isBoss());
        assertTrue(ChapterConfig.get(10100205).isBoss());
        assertTrue(ChapterConfig.get(10100305).isBoss());
        assertFalse(ChapterConfig.get(10100301).isBoss());
    }

    private static void prepareFirstDrawEnergy(
            SceneHandler handler,
            RecordingPlayerContext context) {
        killGuidanceBoss(handler, context, 10100105, 205);
        killGuidanceBoss(handler, context, 10100305, 405);
        rewardTask(handler, context, 200006);
        context.writes.clear();
    }

    private static void prepareSecondDrawEnergy(
            SceneHandler handler,
            RecordingPlayerContext context,
            LotteryDrawReq draw) {
        prepareFirstDrawEnergy(handler, context);
        handler.drawNewFightSkill(context, draw);
        rewardTask(handler, context, 200007);
        killGuidanceBoss(handler, context, 10100405, 405);
        context.writes.clear();
    }

    private static void prepareThirdDrawEnergy(
            SceneHandler handler,
            RecordingPlayerContext context,
            LotteryDrawReq draw) {
        prepareSecondDrawEnergy(handler, context, draw);
        handler.drawNewFightSkill(context, draw);
        rewardTask(handler, context, 200008);
        context.writes.clear();
    }

    private static void prepareFifthDrawEnergy(
            SceneHandler handler,
            RecordingPlayerContext context,
            LotteryDrawReq draw) {
        prepareThirdDrawEnergy(handler, context, draw);
        handler.drawNewFightSkill(context, draw);
        rewardTask(handler, context, 200010);
        context.writes.clear();
    }

    private static double statValue(HeroStatUpdateResp stats, int type) {
        for (int i = 0; i < stats.getHeroVo().getStatsCount(); i++) {
            if (stats.getHeroVo().getStats(i).getType() == type) {
                return stats.getHeroVo().getStats(i).getValue();
            }
        }
        return Double.NaN;
    }

    /** 老用例只验证服药/突破；显式补齐已有材料的前置条件，库存限制由 FighterElixirTest 验证。 */
    @SuppressWarnings("unchecked")
    private static void makeAlchemyWithInventory(SceneHandler handler, RecordingPlayerContext context) {
        try {
            java.lang.reflect.Field stages = SceneHandler.class.getDeclaredField("heroStages");
            java.lang.reflect.Field times = SceneHandler.class.getDeclaredField("alchemyMakeTimes");
            stages.setAccessible(true);
            times.setAccessible(true);
            int stage = ((Map<Long, Integer>) stages.get(handler)).getOrDefault(context.id, 1);
            int ordinal = ((Map<Long, Integer>) times.get(handler)).getOrDefault(context.id, 0);
            int[] indexes = {3, 7, 13, 16, 22};
            int index = ordinal < indexes.length ? indexes[ordinal] : 23;
            java.lang.reflect.Method material = SceneHandler.class.getDeclaredMethod(
                    "alchemyMaterial", IPlayerContext.class, int.class);
            material.setAccessible(true);
            int itemId = stage >= 2 ? 102 : 101;
            if (material.invoke(handler, context, itemId) == null) {
                handler.changeAlchemyMaterial(context, itemId, 1, index);
            }
            handler.makeAlchemy(context, AlchemyNewMakeReq.getDefaultInstance());
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static void stepToOneStarFighter(
            SceneHandler handler,
            RecordingPlayerContext context) {
        for (int round = 0; round < 3; round++) {
            makeAlchemyWithInventory(handler, context);
            handler.takeAlchemy(
                    context,
                    AlchemyNewTakingReq.newBuilder()
                            .addAlchemyId(round * 4L)
                            .addAlchemyId(round * 4L + 1)
                            .addAlchemyId(round * 4L + 2)
                            .addAlchemyId(round * 4L + 3)
                            .build());
            handler.breakHeroLevel(
                    context,
                    HeroLevelBreakReq.newBuilder().setHeroIndex(0).build());
        }
        for (int round = 3; round < 5; round++) {
            makeAlchemyWithInventory(handler, context);
            handler.takeAlchemy(context, AlchemyNewTakingReq.newBuilder()
                    .addAlchemyId(round * 4L).addAlchemyId(round * 4L + 1)
                    .addAlchemyId(round * 4L + 2).addAlchemyId(round * 4L + 3)
                    .build());
        }
        handler.breakHeroLevel(
                context,
                HeroLevelBreakReq.newBuilder().setHeroIndex(0).build());
        handler.upgradeHeroStep(
                context,
                HeroStepUpgradeReq.newBuilder().setHeroIndex(0).build());
    }

    /** 三轮普通丹药突破到内部 level=5（七段斗之气），不混入 source=2 产物。 */
    private static void breakHeroToLevelFive(
            SceneHandler handler,
            RecordingPlayerContext context) {
        for (int round = 0; round < 3; round++) {
            makeAlchemyWithInventory(handler, context);
            handler.takeAlchemy(
                    context,
                    AlchemyNewTakingReq.newBuilder()
                            .addAlchemyId(round * 4L)
                            .addAlchemyId(round * 4L + 1)
                            .addAlchemyId(round * 4L + 2)
                            .addAlchemyId(round * 4L + 3)
                            .build());
            handler.breakHeroLevel(
                    context,
                    HeroLevelBreakReq.newBuilder().setHeroIndex(0).build());
        }
    }

    private static void rewardTask(
            SceneHandler handler,
            RecordingPlayerContext context,
            int taskId) {
        handler.rewardGuidanceTask(
                context,
                TaskRewardReq.newBuilder()
                        .setTaskId(TaskUniqueKey.newBuilder()
                                .setTaskResourceId(taskId))
                        .build());
    }

    private static void killGuidanceBoss(
            SceneHandler handler,
            RecordingPlayerContext context,
            int chapterId,
            int monsterOffset) {
        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(chapterId)
                        .build());
        handler.killGuidanceMonster(
                context,
                com.doupo.protocol.GuidanceKillMonsterReq.newBuilder()
                        .setMonsterId(context.getId() * 1000 + monsterOffset)
                        .build());
    }

    private static void clearWutanWave(
            SceneHandler handler,
            RecordingPlayerContext context,
            int chapterId,
            long... monsterIds) {
        handler.startMainMapFight(
                context,
                MainMapStartFightReq.newBuilder()
                        .setMainMapChapterId(chapterId)
                        .build());
        for (long monsterId : monsterIds) {
            handler.killMainMapMonster(
                    context,
                    MainMapKillMonsterReq.newBuilder()
                            .setMainMapChapterId(chapterId)
                            .setMonsterId(monsterId)
                            .build());
        }
        handler.endMainMapFight(
                context,
                com.doupo.protocol.MainMapEndFightReq.newBuilder()
                        .setMainMapChapterId(chapterId)
                        .build());
    }

    /** 驱动 handler 进入某一关，返回它下发的所有怪物单位。 */
    private static List<SceneUnitVo> enterChapter(int chapterId, float z) {
        SceneHandler handler = new SceneHandler();
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        handler.enterGuidanceMainMapMonster(
                context,
                GuidanceMainMapMonsterEnterReq.newBuilder()
                        .setChapterId(chapterId)
                        .setX(0)
                        .setY(0)
                        .setZ(z)
                        .build());

        List<SceneUnitVo> monsters = new ArrayList<>();
        for (RecordedWrite write : context.writes) {
            if (write.protocolId != 50756) {
                continue;
            }
            SceneUpdateVisibleResp response =
                    (SceneUpdateVisibleResp) write.message;
            for (SceneUnitVo unit : response.getVisibleListList()) {
                if (unit.hasSceneMonsterVo()) {
                    monsters.add(unit);
                }
            }
        }
        return monsters;
    }

    private static SceneUpdateVisibleResp firstSpawnedMonster(
            RecordingPlayerContext context) {
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 50756) {
                return (SceneUpdateVisibleResp) write.message;
            }
        }
        return null;
    }

    private static MainMapStartFightResp lastWutanStart(
            RecordingPlayerContext context) {
        MainMapStartFightResp start = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 61971) {
                start = (MainMapStartFightResp) write.message;
            }
        }
        return start;
    }

    private static SceneUnitVo firstBattleLogMonster(BattleLogVO log)
            throws Exception {
        for (BattleLogEntryVO entry : log.getEntryListList()) {
            for (BattleLogItemVO item : entry.getItemListList()) {
                if (item.getPacketId() != 50804) {
                    continue;
                }
                BattleLogUpdateVisibleResp visible =
                        BattleLogUpdateVisibleResp.parseFrom(item.getData());
                for (SceneUnitVo unit
                        : visible.getSceneUpdateVisibleResp()
                                .getVisibleListList()) {
                    if (unit.hasSceneMonsterVo()) {
                        return unit;
                    }
                }
            }
        }
        return null;
    }

    private static boolean battleLogHasSkill(BattleLogVO log, long skillId)
            throws Exception {
        for (BattleLogEntryVO entry : log.getEntryListList()) {
            for (BattleLogItemVO item : entry.getItemListList()) {
                if (item.getPacketId() != 50762) {
                    continue;
                }
                UseSkillResp skill = UseSkillResp.parseFrom(item.getData());
                if (skill.getSkillId() == skillId) {
                    return true;
                }
            }
        }
        return false;
    }

    private static float spawnedZ(RecordingPlayerContext context, long unitId) {
        for (RecordedWrite write : context.writes) {
            if (write.protocolId != 50756) {
                continue;
            }
            for (SceneUnitVo unit
                    : ((SceneUpdateVisibleResp) write.message)
                            .getVisibleListList()) {
                if (unit.getBaseInfoVo().getId() == unitId) {
                    return unit.getBaseInfoVo().getZ();
                }
            }
        }
        return Float.NaN;
    }

    /** 已记录的 50756 里下发过的怪物场景单位 ID，按下发顺序。 */
    private static List<Long> monsterIdsOf(RecordingPlayerContext context) {
        List<Long> ids = new ArrayList<>();
        for (RecordedWrite write : context.writes) {
            if (write.protocolId != 50756) {
                continue;
            }
            for (SceneUnitVo unit
                    : ((SceneUpdateVisibleResp) write.message)
                            .getVisibleListList()) {
                if (unit.hasSceneMonsterVo()) {
                    ids.add(unit.getBaseInfoVo().getId());
                }
            }
        }
        return ids;
    }

    private static double attributeOf(SceneUnitVo unit, int type) {
        for (com.doupo.protocol.AttributeVO attribute
                : unit.getFightInfoVo().getAttributeListList()) {
            if (attribute.getType() == type) {
                return attribute.getValue();
            }
        }
        return 0;
    }

    private static PackUpdateResp firstPackUpdate(
            RecordingPlayerContext context) {
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 50402) {
                return (PackUpdateResp) write.message;
            }
        }
        throw new AssertionError("missing PackUpdateResp");
    }

    private static LotteryDrawResp firstLotteryDraw(
            RecordingPlayerContext context) {
        for (RecordedWrite write : context.writes) {
            if (write.protocolId == 77352) {
                return (LotteryDrawResp) write.message;
            }
        }
        throw new AssertionError("missing LotteryDrawResp");
    }

    private static TaskUpdateResp lastTaskUpdate(
            RecordingPlayerContext context) {
        for (int i = context.writes.size() - 1; i >= 0; i--) {
            RecordedWrite write = context.writes.get(i);
            if (write.protocolId == 50906) {
                return (TaskUpdateResp) write.message;
            }
        }
        throw new AssertionError("missing TaskUpdateResp");
    }

    private static void assertThirdSkillSlotUnlocked(
            RecordingPlayerContext context) {
        com.doupo.protocol.HeroSkillSlotUpdateResp slot = null;
        for (RecordedWrite write : context.writes) {
            if (write.protocolId != 75015) {
                continue;
            }
            com.doupo.protocol.HeroSkillSlotUpdateResp update =
                    (com.doupo.protocol.HeroSkillSlotUpdateResp) write.message;
            if (update.getSlotsCount() > 0
                    && update.getSlots(0).getId() == 3001) {
                slot = update;
                break;
            }
        }
        assertTrue(slot != null);
        assertEquals(0, slot.getHeroIndex());
        assertEquals(0, slot.getSlots(0).getLv());
        assertFalse(slot.getSlots(0).getAnyOnSkill());
    }

    private static MainMapPassChapterUpdateResp lastChapterUpdate(
            RecordingPlayerContext context) {
        for (int i = context.writes.size() - 1; i >= 0; i--) {
            RecordedWrite write = context.writes.get(i);
            if (write.protocolId == 61952) {
                return (MainMapPassChapterUpdateResp) write.message;
            }
        }
        throw new AssertionError("missing MainMapPassChapterUpdateResp");
    }

    private static boolean containsTask(
            TaskUpdateResp response,
            int taskId,
            TaskPhase phase) {
        return response.getTaskVosList().stream().anyMatch(task ->
                task.getTaskId().getTaskResourceId() == taskId
                        && task.getTaskPhase() == phase);
    }

    private static void assertTask(
            TaskUpdateResp response,
            int index,
            int taskId,
            TaskPhase phase,
            long progress) {
        assertEquals(taskId,
                response.getTaskVos(index)
                        .getTaskId()
                        .getTaskResourceId());
        assertEquals(phase, response.getTaskVos(index).getTaskPhase());
        assertEquals(progress,
                response.getTaskVos(index).getProgressValues(0));
    }

    private static void assertCumulateLoginDays(
            PlayerCumulateLoginDaysResp response,
            int[] moduleIds) {
        assertEquals(1, response.getCumulateLoginDays());
        assertEquals(moduleIds.length,
                response.getModuleOpenId2CumulateLoginDaysCount());
        for (int i = 0; i < moduleIds.length; i++) {
            assertEquals(moduleIds[i],
                    response.getModuleOpenId2CumulateLoginDays(i).getKey());
            assertEquals(1,
                    response.getModuleOpenId2CumulateLoginDays(i).getValue());
        }
    }

    private static final class RecordingPlayerContext
            implements IPlayerContext {

        private final long id;
        private final List<RecordedWrite> writes = new ArrayList<>();

        private RecordingPlayerContext(long id) {
            this.id = id;
        }

        @Override
        public long getId() {
            return id;
        }

        @Override
        public int getServerZone() {
            return 1;
        }

        @Override
        public int getCurrMsgId() {
            return 0;
        }

        @Override
        public void setCurrMsgId(int msgId) {
        }

        @Override
        public void write(
                int protocolId,
                GeneratedMessageV3 message,
                int requestId) {
            writes.add(new RecordedWrite(protocolId, message));
        }

        @Override
        public void write(
                int protocolId,
                ByteString message,
                int requestId) {
            writes.add(new RecordedWrite(protocolId, message));
        }

        @Override
        public boolean isLogin() {
            return true;
        }
    }

    private static final class RecordedWrite {

        private final int protocolId;
        private final Object message;

        private RecordedWrite(
                int protocolId,
                Object message) {
            this.protocolId = protocolId;
            this.message = message;
        }
    }
}
