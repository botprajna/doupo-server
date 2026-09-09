package com.doupo.server.module.scene;

import com.doupo.protocol.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.Map;
import static org.junit.Assert.*;

public class Chapter18FlowTest {
    @Test
    public void chapter18ClearsThreeWavesThenBossAndEntersChapter19() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = opened(h, 100000001801L);
        for (int wave = 1; wave <= 3; wave++) {
            clear(h, c, 10300800 + wave);
            MainMapPassChapterUpdateResp update = c.last(MainMapPassChapterUpdateResp.class);
            assertNotNull("18关每波结束都必须推进", update);
            assertEquals(wave == 3 ? 10300801 : 10300801 + wave, update.getMainMapChapterId());
            assertEquals(7, update.getStageTime());
            assertEquals(10, update.getLastStageTime());
            assertEquals(wave == 3 ? 10300805 : 0, update.getNextChallengeId());
        }
        h.resetGuidanceMainMap(c, GuidanceMainMapResetReq.newBuilder().setEnterNext(true).build());
        assertEquals(10300805, c.last(MainMapPassChapterUpdateResp.class).getMainMapChapterId());
        clear(h, c, 10300805);
        MainMapPassChapterUpdateResp update = c.last(MainMapPassChapterUpdateResp.class);
        assertEquals(10300901, update.getMainMapChapterId());
        assertEquals(8, update.getStageTime());
        assertEquals(7, update.getLastStageTime());
    }

    @Test
    public void rewardingChapter17OpensAllThreeCapturedFollowups() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = opened(h, 100000001802L);
        progress(h, c).tasks.add(200104);
        for (int wave : new int[] {1, 2, 3, 5}) clear(h, c, 10300700 + wave);
        reward(h, c, 200104);
        for (int id : new int[] {200105, 200106, 200107}) {
            assertTrue("官服 idx10300 的后续任务缺失: " + id, c.task(id, TaskPhase.PROGRESS));
        }
        c.writes.clear();
        reward(h, c, 200104);
        assertTrue("重复领奖不得重置后续任务或重复发奖", c.writes.isEmpty());
    }

    @Test
    public void chapter18TasksHaveRegisteredRequests() {
        for (String request : new String[] {"ChangeNameReq", "MainEquipLevelUpReq", "MainEquipDecomposeReq"}) {
            assertTrue("缺少请求处理: " + request, java.util.Arrays.stream(SceneHandler.class.getMethods())
                    .anyMatch(m -> m.isAnnotationPresent(org.gaming.fakecmd.annotation.PlayerCmd.class)
                            && m.getParameterCount() == 2 && m.getParameterTypes()[1].getSimpleName().equals(request)));
        }
    }

    @Test
    public void chapter19And20CompleteTasksAndReputationGateIsPreservedAcrossLoops() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = opened(h, 100000001803L);
        Chapter17Progress state = progress(h,c);
        state.tasks.add(200108);
        for (int wave : new int[]{1,2,3,5}) clear(h,c,10300900+wave);
        assertEquals(10301001,c.last(MainMapPassChapterUpdateResp.class).getMainMapChapterId());
        assertTrue(c.task(200108,TaskPhase.FINISHED));
        reward(h,c,200108);
        assertTrue(c.task(200109,TaskPhase.PROGRESS));
        assertEquals(4,c.last(RewardResp.class).getRewardItemVos(0).getAmount());
        ((Map<Long,Integer>)field(h,"heroLevels")).put(c.id,17);
        reward(h,c,200109);
        assertTrue(c.task(200110,TaskPhase.PROGRESS));
        for (int wave : new int[]{1,2,3,5}) clear(h,c,10301000+wave);
        assertTrue(c.task(200110,TaskPhase.FINISHED));
        assertEquals(10301001,c.last(MainMapPassChapterUpdateResp.class).getMainMapChapterId());
        assertEquals(10400101,c.last(MainMapPassChapterUpdateResp.class).getNextChallengeId());
        assertEquals(3,c.last(MainMapPassChapterUpdateResp.class).getChangeReason());
        c.writes.clear();
        h.resetGuidanceMainMap(c,GuidanceMainMapResetReq.newBuilder().setEnterNext(true).build());
        assertTrue(c.writes.isEmpty());
        for (int wave : new int[]{1,2,3}) {
            clear(h,c,10301000+wave);
            assertEquals(10400101,c.last(MainMapPassChapterUpdateResp.class).getNextChallengeId());
            assertEquals(10301005,c.last(MainMapPassChapterUpdateResp.class).getHistoryTopId());
            assertNull(c.last(RewardResp.class));
        }
        state.tasks.add(200111);
        ((Map<Long,Integer>)field(h,"item10060Counts")).put(c.id,10000);
        h.reputationLvUp(c,ReputationLvUpReq.newBuilder().setRange(1).build());
        assertEquals(3,c.last(ReputationLvUpResp.class).getUpdateRangeVo().getRangeInsideLv());
        assertTrue(c.task(200111,TaskPhase.FINISHED));
        assertEquals(4,c.last(MainMapPassChapterUpdateResp.class).getChangeReason());
        clear(h,c,10301001); // 升级声望后又打一波，不能丢失进入下一章的入口。
        assertEquals(10400101,c.last(MainMapPassChapterUpdateResp.class).getNextChallengeId());
        assertEquals(4,c.last(MainMapPassChapterUpdateResp.class).getChangeReason());
        h.resetGuidanceMainMap(c,GuidanceMainMapResetReq.newBuilder().setEnterNext(true).build());
        assertEquals(10400101,c.last(MainMapPassChapterUpdateResp.class).getMainMapChapterId());
        assertEquals(9,c.last(MainMapPassChapterUpdateResp.class).getStageTime());
        assertEquals(8,c.last(MainMapPassChapterUpdateResp.class).getLastStageTime());
    }

    @Test
    public void chapter15LoopDoesNotReplaceChapter16EntryWithAnotherBossAfterReputationUpgrade() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = opened(h,100000001804L);
        ((Map<Long,Integer>)field(h,"reputationLevels")).put(c.id,1);
        for (int wave : new int[]{1,2,3,5}) clear(h,c,10300500+wave);
        assertEquals(10300501,c.last(MainMapPassChapterUpdateResp.class).getMainMapChapterId());
        ((Map<Long,Integer>)field(h,"reputationLevels")).put(c.id,2);
        for (int wave : new int[]{1,2,3,1}) {
            clear(h,c,10300500+wave);
            assertEquals(10300601,c.last(MainMapPassChapterUpdateResp.class).getNextChallengeId());
            assertEquals(4,c.last(MainMapPassChapterUpdateResp.class).getChangeReason());
            assertEquals(10300505,c.last(MainMapPassChapterUpdateResp.class).getHistoryTopId());
            assertNull(c.last(RewardResp.class));
        }
        h.resetGuidanceMainMap(c,GuidanceMainMapResetReq.newBuilder().setEnterNext(true).build());
        assertEquals(10300601,c.last(MainMapPassChapterUpdateResp.class).getMainMapChapterId());
        c.writes.clear();
        h.startMainMapFight(c,MainMapStartFightReq.newBuilder().setMainMapChapterId(10300605).build());
        assertNull(c.last(MainMapStartFightResp.class));
    }

    static Chapter17FlowTest.Context opened(SceneHandler h, long id) throws Exception {
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(id);
        ((Map<Long, Integer>) field(h, "reputationLevels")).put(id, 2);
        return c;
    }

    static Chapter17Progress progress(SceneHandler h, Chapter17FlowTest.Context c) throws Exception {
        Map<Long, Chapter17Progress> states = (Map<Long, Chapter17Progress>) field(h, "laterProgress");
        return states.computeIfAbsent(c.id, ignored -> new Chapter17Progress());
    }

    static Object field(Object target, String name) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return f.get(target);
    }

    static void reward(SceneHandler h, Chapter17FlowTest.Context c, int id) {
        h.rewardGuidanceTask(c, TaskRewardReq.newBuilder().setTaskId(
                TaskUniqueKey.newBuilder().setTaskResourceId(id)).build());
    }

    static void clear(SceneHandler h, Chapter17FlowTest.Context c, int id) throws Exception {
        ChapterConfig.Chapter chapter = ChapterConfig.get(id);
        assertNotNull("Missing chapter " + id, chapter);
        c.writes.clear();
        h.startMainMapFight(c, MainMapStartFightReq.newBuilder().setMainMapChapterId(id)
                .setX(20).setY(1).setZ(55).build());
        MainMapStartFightResp start = c.last(MainMapStartFightResp.class);
        assertNotNull("Missing battle " + id, start);
        assertTrue(BattleLogVO.parseFrom(start.getBattleLog().getData()).getEntryListCount() > 0);
        for (ChapterConfig.MonsterSpawn monster : chapter.getMonsters()) {
            h.killMainMapMonster(c, MainMapKillMonsterReq.newBuilder().setMainMapChapterId(id)
                    .setMonsterId(monster.getTemplateId()).build());
        }
        h.endMainMapFight(c, MainMapEndFightReq.newBuilder().setMainMapChapterId(id).build());
    }
}
