package com.doupo.server.module.scene;

import com.doupo.protocol.*;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Message;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.gaming.fakecmd.side.game.PlayerCmdRegister;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import static org.junit.Assert.*;

public class HeroSkillActivationTest {
    @Test
    public void manualClawActivationDispatches75010AndConsumesOneRealBook() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000009810L);
        book(h, c, 80128010, 2);
        dispatch(h, c, 0, 80128011);
        HeroSkillActResp act = c.last(HeroSkillActResp.class);
        assertNotNull(act);
        assertEquals(0, act.getHeroIndex());
        assertEquals(80128011, act.getPlayerSkill().getBaseId());
        assertEquals(-1, act.getPlayerSkill().getHeroSkills(0).getHeroIdx());
        assertEquals(2, act.getPlayerSkill().getHeroSkills(0).getHeroSkill().getStar());
        assertTrue(act.getShowTips());
        PackUpdateResp pack = c.last(PackUpdateResp.class);
        assertEquals(75004, pack.getOperationType());
        assertEquals(1, pack.getPacks(0).getUpdateItems(0).getPackItem().getSize());
        assertEquals(80128011, c.last(HeroSkillBaseUpdateResp.class).getBaseIds(0));
        c.writes.clear();
        dispatch(h, c, 0, 80128011);
        assertNull("retry must not consume twice", c.last(PackUpdateResp.class));
        assertNotNull("retry repairs a missed client activation push", c.last(HeroSkillActResp.class));
        assertFalse(c.last(HeroSkillActResp.class).getShowTips());
        assertEquals(2, c.last(HeroSkillActResp.class).getPlayerSkill().getHeroSkills(0).getHeroSkill().getStar());
    }

    @Test
    public void invalidHeroUnknownSkillAndMissingBookCannotActivate() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000009811L);
        dispatch(h, c, 0, 80128011);
        assertNull(c.last(HeroSkillActResp.class));
        book(h, c, 80128010, 1);
        dispatch(h, c, -1, 80128011);
        dispatch(h, c, 1, 80128011);
        dispatch(h, c, 0, -1);
        dispatch(h, c, 0, 80128012); // 升星行不是可学习的 baseId。
        assertNull(c.last(HeroSkillActResp.class));
        assertNull(c.last(PackUpdateResp.class));
        dispatch(h, c, 0, 80128011);
        assertFalse("last book removed", c.last(PackUpdateResp.class).getPacks(0).getUpdateItems(0).hasPackItem());
    }

    @Test
    public void acceptedTaskShowsOneThenFinishesOnClawWithoutDuplicateCounting() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000009812L);
        h.claimCrisisEventReward(c, CrisisEventRewardReq.newBuilder().setEventId(20010).build());
        c.writes.clear();
        acceptTwoSkillTask(h, c);
        assertTask(c, TaskPhase.PROGRESS, 1);
        book(h, c, 80128010, 2);
        c.writes.clear();
        dispatch(h, c, 0, 80128011);
        assertTask(c, TaskPhase.FINISHED, 2);
        c.writes.clear();
        dispatch(h, c, 0, 80128011);
        assertNull(c.last(TaskUpdateResp.class));
    }

    @Test
    public void oneNewSkillUpdatesPartialProgressAndNeverFinishesAlone() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000009813L);
        acceptTwoSkillTask(h, c);
        assertTask(c, TaskPhase.PROGRESS, 0);
        book(h, c, 80128010, 1);
        c.writes.clear();
        dispatch(h, c, 0, 80128011);
        assertTask(c, TaskPhase.PROGRESS, 1);
        c.writes.clear();
        dispatch(h, c, 0, 80128011);
        assertFalse(c.task(200018, TaskPhase.FINISHED));
    }

    @Test
    public void previouslyLearnedQualityThreeSkillsCompleteTaskWhenAccepted() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000009814L);
        // 6.9 爆步也是品质 3，不能遗漏；两个不同技能才算两个。
        learned(h, c, 80128001, 2);
        learned(h, c, 80128011, 2);
        acceptTwoSkillTask(h, c);
        assertTask(c, TaskPhase.FINISHED, 2);
    }

    @Test
    public void activationDoesNotResetLearnedStarOrInventHistoricalStarTotals() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000009815L);
        learned(h, c, 80130011, 2);
        learned(h, c, 80130101, 1);
        learned(h, c, 80140001, 1);
        book(h, c, 80128010, 1);
        dispatch(h, c, 0, 80128011);
        assertEquals(6, c.last(HeroSkillStarTotalMaxHisUpdateResp.class).getSkillStarTotalMaxHis());
        learned(h, c, 80128011, 3);
        c.writes.clear();
        dispatch(h, c, 0, 80128011);
        assertEquals(3, c.last(HeroSkillActResp.class).getPlayerSkill().getHeroSkills(0).getHeroSkill().getStar());
        assertNull(c.last(PackUpdateResp.class));
    }

    private static void assertTask(Chapter17FlowTest.Context c, TaskPhase phase, long progress) {
        TaskVo task = c.last(TaskUpdateResp.class).getTaskVos(0);
        assertEquals(200018, task.getTaskId().getTaskResourceId());
        assertEquals(phase, task.getTaskPhase());
        assertEquals(progress, task.getProgressValues(0));
    }

    @Test
    public void activationStateMatchesAllThreeCapturedPushes() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000009816L);
        // 第二次抓包激活前总星级为 4，加上裂爪击初始 2 星后为 6。
        learned(h, c, 80130011, 2);
        learned(h, c, 80140001, 1);
        learned(h, c, 80130101, 1);
        book(h, c, 80128010, 1);
        dispatch(h, c, 0, 80128011);
        assertEquals(HeroSkillActResp.parseFrom(fixture("1464-75011.bin")), c.last(HeroSkillActResp.class));
        assertEquals(HeroSkillStarTotalMaxHisUpdateResp.parseFrom(fixture("1465-75049.bin")),
                c.last(HeroSkillStarTotalMaxHisUpdateResp.class));
        assertEquals(HeroSkillBaseUpdateResp.parseFrom(fixture("1466-75039.bin")), c.last(HeroSkillBaseUpdateResp.class));
    }

    @Test
    public void lowerQualityDoesNotCountAndHigherQualityUsesLubanValue() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000009817L);
        learned(h, c, 80130011, 5); // 星级不是品质，狂狮吟仍是品质 2。
        acceptTwoSkillTask(h, c);
        assertTask(c, TaskPhase.PROGRESS, 0);
        c.writes.clear();
        learned(h, c, 80130001, 3); // 吹火掌品质 4，旧代码误判为 2。
        acceptTwoSkillTask(h, c);
        assertTask(c, TaskPhase.PROGRESS, 1);
    }

    @Test
    public void resetClearsLearnedSkillsAndDoesNotGiveFreeReactivation() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000009818L);
        book(h, c, 80128010, 2);
        dispatch(h, c, 0, 80128011);
        h.resetTutorialPlayer(c.id);
        c.writes.clear();
        dispatch(h, c, 0, 80128011);
        assertNull(c.last(HeroSkillActResp.class));
        acceptTwoSkillTask(h, c);
        assertTask(c, TaskPhase.PROGRESS, 0);
    }

    private static ByteString fixture(String name) throws Exception {
        try (java.io.InputStream in = HeroSkillActivationTest.class.getResourceAsStream("/skill-learning/" + name)) {
            assertNotNull(name, in);
            return ByteString.readFrom(in);
        }
    }

    private static void acceptTwoSkillTask(SceneHandler h, Chapter17FlowTest.Context c) {
        h.rewardGuidanceTask(c, TaskRewardReq.newBuilder().setTaskId(
                TaskUniqueKey.newBuilder().setTaskResourceId(200017)).build());
    }

    private static void book(SceneHandler h, Chapter17FlowTest.Context c, int key, int amount) throws Exception {
        Method method = SceneHandler.class.getDeclaredMethod("addOrStackLotteryItem", long.class, int.class, int.class, long.class);
        method.setAccessible(true);
        method.invoke(h, c.id, key, amount, 1788777301251L);
    }

    private static void learned(SceneHandler h, Chapter17FlowTest.Context c, int key, int star) throws Exception {
        Method method = SceneHandler.class.getDeclaredMethod("noteLearnedSkill", long.class, int.class, int.class, int.class);
        method.setAccessible(true);
        method.invoke(h, c.id, key, star, star);
    }

    private static void dispatch(SceneHandler h, Chapter17FlowTest.Context c, int hero, int skill) throws Exception {
        java.lang.reflect.Constructor<PlayerCmdRegister> ctor = PlayerCmdRegister.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        PlayerCmdRegister registry = ctor.newInstance();
        registry.register(h);
        assertNotNull("manual skill activation must be registered", registry.getInvoker(75010));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CodedOutputStream wire = CodedOutputStream.newInstance(out);
        wire.writeInt32(1, hero);
        wire.writeInt32(2, skill);
        wire.flush();
        registry.handle(new PlayerCmdRegister.IPlayerCmdMessage() {
            public IPlayerContext getPlayerContext() { return c; }
            public int getMessageId() { return 1; }
            public int getCmd() { return 75010; }
            public ByteString getData() { return ByteString.copyFrom(out.toByteArray()); }
            public void onException(Exception e, Message request) { throw new AssertionError(e); }
            public void onResponse(Object response) { fail("activation uses 75011 pushes"); }
        });
    }
}
