package com.doupo.server.module.scene;

import com.doupo.protocol.*;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class Chapter17FlowTest {
    private static void reward(SceneHandler handler, Context c, int id) {
        handler.rewardGuidanceTask(c, TaskRewardReq.newBuilder()
                .setTaskId(TaskUniqueKey.newBuilder().setTaskResourceId(id)).build());
    }

    private static void clear(SceneHandler handler, Context c, int id) {
        handler.startMainMapFight(c, MainMapStartFightReq.newBuilder().setMainMapChapterId(id)
                .setX(10).setY(2).setZ(30).build());
        for (ChapterConfig.MonsterSpawn monster : ChapterConfig.get(id).getMonsters()) {
            handler.killMainMapMonster(c, MainMapKillMonsterReq.newBuilder().setMainMapChapterId(id)
                    .setMonsterId(monster.getTemplateId()).build());
        }
        handler.endMainMapFight(c, MainMapEndFightReq.newBuilder().setMainMapChapterId(id).build());
    }

    @Test
    public void actualTowerTaskThenReputationUnlockThenChapter17Tasks() throws Exception {
        SceneHandler h = new SceneHandler();
        Context c = new Context(100000000902L);
        reward(h, c, 200029);
        reward(h, c, 200030);
        reward(h, c, 200033);
        reward(h, c, 200034);
        assertTrue(c.task(200035, TaskPhase.PROGRESS));
        assertFalse(c.task(200035, TaskPhase.REWARDED));
        reward(h, c, 200035);
        assertFalse(c.task(200035, TaskPhase.REWARDED));
        for (int floor=1; floor<=2; floor++) {
            h.fireTowerChallenge(c, FireTowerChallengeReq.newBuilder().setZ(6.5f).build());
            assertEquals(floor, c.last(FireTowerChallengeResp.class).getConfigId());
            h.fireTowerAfter(c, FireTowerChallengeAfterReq.getDefaultInstance());
            assertEquals(2033, c.last(FireTowerChallengeAfterResp.class).getFightDurationMs());
        }
        assertTrue(c.task(200035, TaskPhase.FINISHED));
        reward(h, c, 200035);
        clear(h, c, 10300405);
        reward(h, c, 200036);
        assertTrue(c.task(200037, TaskPhase.PROGRESS));
        java.lang.reflect.Field levels = SceneHandler.class.getDeclaredField("heroLevels");
        levels.setAccessible(true);
        ((java.util.Map<Long,Integer>)levels.get(h)).put(c.id, 9);
        java.lang.reflect.Field stages = SceneHandler.class.getDeclaredField("heroStages");
        stages.setAccessible(true);
        ((java.util.Map<Long,Integer>)stages.get(h)).put(c.id, 2);
        java.lang.reflect.Field taking = SceneHandler.class.getDeclaredField("alchemyTakingCounts");
        taking.setAccessible(true);
        ((java.util.Map<Long,Integer>)taking.get(h)).put(c.id, 2); // 已过前两次新手服药。
        // 只设进入本链路前已有的二星斗者；13级须实际消耗任务奖励的5份灵液。
        for (int i=0; i<5; i++) {
            h.makeAlchemy(c,AlchemyNewMakeReq.getDefaultInstance());
            AlchemyNewMakeResp made=c.last(AlchemyNewMakeResp.class);
            assertEquals(4,made.getMakeIdsCount());
            assertEquals(1102,made.getMakeIds(0).getRid());
            AlchemyNewTakingReq.Builder take=AlchemyNewTakingReq.newBuilder();
            made.getMakeIdsList().forEach(p->take.addAlchemyId(p.getId()));
            h.takeAlchemy(c,take.build());
            h.breakHeroLevel(c,HeroLevelBreakReq.getDefaultInstance());
        }
        assertTrue(((java.util.Map<Long,Integer>)levels.get(h)).get(c.id)>=13);
        assertTrue(c.task(200037,TaskPhase.FINISHED));
        reward(h, c, 200037);
        assertTrue(c.task(200038, TaskPhase.PROGRESS));
        for (int wave : new int[]{1,2,3,5}) clear(h,c,10300500+wave);
        assertEquals(10300501,c.last(MainMapPassChapterUpdateResp.class).getMainMapChapterId());
        assertEquals(10300601,c.last(MainMapPassChapterUpdateResp.class).getNextChallengeId());
        reward(h,c,200038);
        h.reputationLvUp(c,ReputationLvUpReq.newBuilder().setRange(1).build());
        assertEquals(2,c.last(ReputationLvUpResp.class).getUpdateRangeVo().getRangeInsideLv());
        assertTrue(c.task(200100,TaskPhase.PROGRESS));
        assertTrue(c.task(200101,TaskPhase.PROGRESS));
        h.resetGuidanceMainMap(c,GuidanceMainMapResetReq.newBuilder().setEnterNext(true).build());
        assertEquals(10300601,c.last(MainMapPassChapterUpdateResp.class).getMainMapChapterId());
        for (int wave : new int[]{1,2,3,5}) clear(h,c,10300600+wave);
        reward(h,c,200100);
        h.takeHangUpReward(c,MainMapTakeHangUpRewardReq.getDefaultInstance());
        assertTrue(c.task(200101,TaskPhase.FINISHED));
        reward(h,c,200101);
        assertTrue(c.task(200102,TaskPhase.FINISHED));
        reward(h,c,200102);
        h.drawNewFightSkill(c,LotteryDrawReq.newBuilder().setLotteryTypeValue(13).build());
        assertEquals(10,c.last(LotteryDrawResp.class).getRewardItemVosCount());
        assertEquals(4,c.last(PlayerCommonSkillResp.class).getCommonSkillLevelCount());
        assertTrue(c.task(200103,TaskPhase.FINISHED));
        reward(h,c,200103);
        for (int wave : new int[]{1,2,3,5}) clear(h,c,10300700+wave);
        assertEquals(10300801,c.last(MainMapPassChapterUpdateResp.class).getMainMapChapterId());
        assertTrue(c.task(200104,TaskPhase.FINISHED));
        reward(h,c,200104);
        assertEquals(10041,c.last(RewardResp.class).getRewardItemVos(0).getItemKey());
        c.writes.clear();
        reward(h,c,200104);
        h.drawNewFightSkill(c,LotteryDrawReq.newBuilder().setLotteryTypeValue(13).build());
        assertTrue("领奖、引导抽取不可重复发奖",c.writes.isEmpty());
    }

    @Test
    public void unfinishedFightAndForeignKillsCannotAwardChapter15() {
        SceneHandler h=new SceneHandler();Context c=new Context(100000000903L);
        h.startMainMapFight(c,MainMapStartFightReq.newBuilder().setMainMapChapterId(10300505).build());
        h.killMainMapMonster(c,MainMapKillMonsterReq.newBuilder().setMainMapChapterId(10300505).setMonsterId(999).build());
        h.endMainMapFight(c,MainMapEndFightReq.newBuilder().setMainMapChapterId(10300505).build());
        assertNull(c.last(RewardResp.class));
        h.killMainMapMonster(c,MainMapKillMonsterReq.newBuilder().setMainMapChapterId(10300605).setMonsterId(1006001).build());
        assertNull(c.last(RewardResp.class));
    }

    @Test
    public void chapter16RequiresReputationAndTowerCannotDoubleSettle() {
        SceneHandler h=new SceneHandler();Context c=new Context(100000000904L);
        h.startMainMapFight(c,MainMapStartFightReq.newBuilder().setMainMapChapterId(10300601).build());
        assertNull(c.last(MainMapStartFightResp.class));
        reward(h,c,200034);
        h.fireTowerAfter(c,FireTowerChallengeAfterReq.getDefaultInstance());
        assertNull(c.last(FireTowerChallengeAfterResp.class));
        h.fireTowerChallenge(c,FireTowerChallengeReq.newBuilder().setZ(6.5f).build());
        h.fireTowerAfter(c,FireTowerChallengeAfterReq.getDefaultInstance());
        h.fireTowerHostResult(c,FireTowerHostResultReq.getDefaultInstance());
        assertFalse(c.last(FireTowerHostResultResp.class).getHasResult());
        c.writes.clear();
        h.fireTowerAfter(c,FireTowerChallengeAfterReq.getDefaultInstance());
        assertTrue(c.writes.isEmpty());
    }

    @Test
    public void everyNewLogRebindsHeroAndContainsConfiguredMonsters() throws Exception {
        Context c = new Context(100000000905L);
        List<Integer> ids=new ArrayList<>();
        for(int stage=5;stage<=7;stage++)for(int wave:new int[]{1,2,3,5})ids.add(10300000+stage*100+wave);
        ids.add(10300801);ids.add(1);ids.add(2);ids.add(3);
        for(int id:ids){
            WutanCapturedLog.Capture capture=WutanCapturedLog.load(id);
            assertNotNull(capture);
            BattleLogVO log=BattleLogVO.parseFrom(WutanBattleLogIdentity.rebind(
                    WutanCapturedLog.relocated(capture,30,2,40),c.id));
            int heroes=0,monsters=0;
            for(BattleLogEntryVO entry:log.getEntryListList())for(BattleLogItemVO item:entry.getItemListList()){
                if(item.getPacketId()!=50804)continue;
                for(SceneUnitVo u:BattleLogUpdateVisibleResp.parseFrom(item.getData()).getSceneUpdateVisibleResp().getVisibleListList()){
                    if(u.hasHeroVo()){
                        heroes++;assertEquals(c.id,u.getHeroVo().getPlayerId());
                        assertEquals(c.id*1000+1,u.getBaseInfoVo().getId());
                    }
                    if(u.hasSceneMonsterVo())monsters++;
                }
            }
            assertEquals("single hero "+id,1,heroes);
            if(id>3)assertEquals("monster count "+id,ChapterConfig.get(id).getMonsterCount(),monsters);
        }
    }

    @Test
    public void towerHostCancellationInvalidatesScheduledPasses() {
        List<Runnable> callbacks=new ArrayList<>();
        SceneHandler h=new SceneHandler(){
            @Override void scheduleCombatAction(IPlayerContext c,long delay,Runnable callback){callbacks.add(callback);}
        };
        Context c=new Context(100000000906L);reward(h,c,200034);
        for(int floor=1;floor<=2;floor++){
            h.fireTowerChallenge(c,FireTowerChallengeReq.newBuilder().setZ(6.5f).build());
            h.fireTowerAfter(c,FireTowerChallengeAfterReq.getDefaultInstance());
        }
        h.fireTowerHost(c,FireTowerHostReq.newBuilder().setEnable(true).build());
        assertEquals(17,callbacks.size());
        callbacks.get(0).run();
        assertEquals(3,c.last(FireTowerInfoResp.class).getChapters(0).getPassNum());
        h.fireTowerHost(c,FireTowerHostReq.newBuilder().setEnable(false).build());
        callbacks.forEach(Runnable::run);
        assertEquals(3,c.last(FireTowerInfoResp.class).getChapters(0).getPassNum());
    }

    @Test
    public void towerHostCoversCapturedProgressWithoutInventingPayout() {
        SceneHandler h=new SceneHandler();Context c=new Context(100000000907L);
        reward(h,c,200034);
        for(int floor=1;floor<=2;floor++){
            h.fireTowerChallenge(c,FireTowerChallengeReq.newBuilder().setZ(6.5f).build());
            h.fireTowerAfter(c,FireTowerChallengeAfterReq.getDefaultInstance());
        }
        c.writes.clear();
        h.fireTowerHost(c,FireTowerHostReq.newBuilder().setEnable(true).build());
        assertEquals(19,c.last(FireTowerInfoResp.class).getChapters(0).getPassNum());
        assertFalse(c.last(FireTowerInfoResp.class).getHosting());
        assertNull("托管领取缺证据，不应每关擅自入账",c.last(PackUpdateResp.class));
        assertEquals(1,c.last(FireTowerHostEndResp.class).getReason());
    }
    @Test
    public void chapter15FirstWaveHasRealBattleLog() throws Exception {
        SceneHandler handler = new SceneHandler();
        Context context = new Context(100000000901L);
        handler.startMainMapFight(context, MainMapStartFightReq.newBuilder()
                .setMainMapChapterId(10300501).build());
        MainMapStartFightResp start = context.last(MainMapStartFightResp.class);
        assertNotNull("第15关不能只有占位或空战报", start);
        assertTrue(BattleLogVO.parseFrom(start.getBattleLog().getData()).getEntryListCount() > 10);
    }

    static class Context implements IPlayerContext {
        final long id;
        final List<Object> writes = new ArrayList<>();
        Context(long id) { this.id = id; }
        public long getId() { return id; }
        public int getServerZone() { return 1; }
        public int getCurrMsgId() { return 0; }
        public void setCurrMsgId(int id) { }
        public boolean isLogin() { return true; }
        public void write(int protocolId, GeneratedMessageV3 message, int requestId) { writes.add(message); }
        public void write(int protocolId, ByteString message, int requestId) { writes.add(message); }
        <T> T last(Class<T> type) {
            for (int i = writes.size() - 1; i >= 0; i--) {
                if (type.isInstance(writes.get(i))) return type.cast(writes.get(i));
            }
            return null;
        }
        boolean task(int id, TaskPhase phase) {
            return writes.stream().filter(TaskUpdateResp.class::isInstance)
                    .map(TaskUpdateResp.class::cast).flatMap(m -> m.getTaskVosList().stream())
                    .anyMatch(t -> t.getTaskId().getTaskResourceId() == id && t.getTaskPhase() == phase);
        }
    }
}
