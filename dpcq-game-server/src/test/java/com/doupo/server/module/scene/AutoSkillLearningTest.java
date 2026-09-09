package com.doupo.server.module.scene;

import com.doupo.protocol.*;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.gaming.fakecmd.side.game.PlayerCmdRegister;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class AutoSkillLearningTest {
    @Test
    public void actualOneTwoFourDrawSequenceLearnsClawUpdatesTaskAndAllowsEquip() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000009830L);
        h.claimCrisisEventReward(c, CrisisEventRewardReq.newBuilder().setEventId(20010).build());
        draw(h, c, 1, false);
        draw(h, c, 2, false);
        h.rewardGuidanceTask(c, TaskRewardReq.newBuilder().setTaskId(
                TaskUniqueKey.newBuilder().setTaskResourceId(200017)).build());
        c.writes.clear();
        // 实际日志 2026-09-08 20:02:37：累计 3，一次结算 4 抽到累计 7。
        draw(h, c, 4, false);
        assertClawLearned(c);
        assertTrue(c.task(200018, TaskPhase.FINISHED));
        TaskVo task = c.last(TaskUpdateResp.class).getTaskVos(0);
        assertEquals(2, task.getProgressValues(0));
        assertEquals(1, books(h, c, 80128010));
        c.writes.clear();
        dispatch(h, c, 75004, HeroSkillSchemaUpdateReq.newBuilder().setHeroIndex(0).setReqSource(2)
                .addSkills(IntegerAndIntegerPairEntry.newBuilder().setKey(2001).setValue(80128011)).build());
        assertEquals(80128011, c.last(HeroSkillSchemaUpdateResp.class).getSchema().getSlot2SkillBaseIds(0).getValue());
        assertNull("装配不能再扣学习道具", c.last(PackUpdateResp.class));
        assertEquals(1, books(h, c, 80128010));
    }

    @Test
    public void anyBatchCrossingFifthDrawLearnsClawAndPreservesBothRewardBooks() throws Exception {
        for (int completed = 0; completed <= 5; completed++) {
            SceneHandler h = new SceneHandler();
            Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000009840L + completed);
            if (completed > 0) draw(h, c, completed, false);
            c.writes.clear();
            draw(h, c, 7 - completed, false);
            assertClawLearned(c);
            assertEquals(7, c.last(LotteryInfoResp.class).getTotalDrawTime());
            assertEquals("two awarded, one learned, completed=" + completed, 1, books(h, c, 80128010));
            PackUpdateResp reward = c.writes.stream().filter(PackUpdateResp.class::isInstance)
                    .map(PackUpdateResp.class::cast).filter(p -> p.getOperationType() == 77353).findFirst().get();
            assertEquals(2, reward.getPacks(0).getUpdateItemsList().stream()
                    .filter(i -> i.getPackItem().getKey() == 80128010).mapToLong(i -> i.getPackItem().getSize()).sum());
        }
    }

    @Test
    public void firstTenDrawLearnsWithoutManualActivationAndKeepsInventoryConsistent() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000009850L);
        draw(h, c, 10, true);
        assertClawLearned(c);
        assertEquals(10, c.last(LotteryInfoResp.class).getTotalDrawTime());
        assertEquals(1, books(h, c, 80128010));
        Map<Integer, PackItemVo> bag = new HashMap<>();
        for (Object message : c.writes) if (message instanceof PackUpdateResp) {
            for (PackUpdateVo pack : ((PackUpdateResp) message).getPacksList()) {
                if (pack.getPackType() != 1) continue;
                for (UpdateItem item : pack.getUpdateItemsList()) {
                    if (item.hasPackItem()) bag.put(item.getItemIndex(), item.getPackItem());
                    else bag.remove(item.getItemIndex());
                }
            }
        }
        assertEquals(1, bag.values().stream().filter(i -> i.getKey() == 80128010).mapToLong(PackItemVo::getSize).sum());
    }

    @Test
    public void duplicateClawRewardDoesNotLearnOrConsumeAgain() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000009851L);
        draw(h, c, 7, false);
        assertClawLearned(c);
        c.writes.clear();
        draw(h, c, 7, false); // 累计第 14 抽再得到一张裂爪击。
        assertEquals(0, clawPushes(c));
        assertEquals(2, books(h, c, 80128010));
    }

    @Test
    public void firstPostGuideSkillUsesLubanInitialStarAndCannotLearnWithoutInventory() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000009852L);
        Method activate = SceneHandler.class.getDeclaredMethod("writeNewFightSkillActivation", IPlayerContext.class, int.class);
        activate.setAccessible(true);
        activate.invoke(h, c, 80128010);
        assertEquals(0, clawPushes(c));
        Method book = SceneHandler.class.getDeclaredMethod("addOrStackLotteryItem", long.class, int.class, int.class, long.class);
        book.setAccessible(true);
        book.invoke(h, c.id, 80128010, 1, 1788777301251L);
        activate.invoke(h, c, 80128010);
        assertClawLearned(c); // 不管是第几个获得的新技能，裂爪击初始都是 2 星。
        assertEquals(0, books(h, c, 80128010));
    }

    private static void assertClawLearned(Chapter17FlowTest.Context c) throws Exception {
        assertEquals("无需发送 75010 就必须出现一次学习推送", 1, clawPushes(c));
        HeroSkillActResp act = c.writes.stream().filter(HeroSkillActResp.class::isInstance)
                .map(HeroSkillActResp.class::cast).filter(a -> a.getPlayerSkill().getBaseId() == 80128011).findFirst().get();
        assertEquals(-1, act.getPlayerSkill().getHeroSkills(0).getHeroIdx());
        assertEquals(2, act.getPlayerSkill().getHeroSkills(0).getHeroSkill().getStar());
        try (java.io.InputStream in = AutoSkillLearningTest.class.getResourceAsStream("/skill-learning/1464-75011.bin")) {
            assertNotNull(in);
            assertEquals("完整自动学习推送对照第二次抓包", HeroSkillActResp.parseFrom(in), act);
        }
    }

    private static long clawPushes(Chapter17FlowTest.Context c) {
        return c.writes.stream().filter(HeroSkillActResp.class::isInstance).map(HeroSkillActResp.class::cast)
                .filter(a -> a.getPlayerSkill().getBaseId() == 80128011).count();
    }

    private static void draw(SceneHandler h, Chapter17FlowTest.Context c, int count, boolean ten) throws Exception {
        Method energy = SceneHandler.class.getDeclaredMethod("changeNewFightSkillEnergy",
                org.gaming.fakecmd.side.game.IPlayerContext.class, int.class, int.class, long.class);
        energy.setAccessible(true);
        energy.invoke(h, c, count * 10, 11, 1788777301251L);
        dispatch(h, c, 77351, LotteryDrawReq.newBuilder()
                .setLotteryType(LotteryType.LOTTERY_TYPE_NEW_FIGHT_SKILL).setTen(ten).build());
    }

    private static void dispatch(SceneHandler h, Chapter17FlowTest.Context c, int id, GeneratedMessageV3 request) throws Exception {
        java.lang.reflect.Constructor<PlayerCmdRegister> ctor = PlayerCmdRegister.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        PlayerCmdRegister registry = ctor.newInstance();
        registry.register(h);
        assertNotNull(registry.getInvoker(id));
        registry.handle(new PlayerCmdRegister.IPlayerCmdMessage() {
            public IPlayerContext getPlayerContext() { return c; }
            public int getMessageId() { return 1; }
            public int getCmd() { return id; }
            public ByteString getData() { return request.toByteString(); }
            public void onException(Exception e, Message parsed) { throw new AssertionError(e); }
            public void onResponse(Object response) { fail("uses protocol pushes"); }
        });
    }

    private static int books(SceneHandler h, Chapter17FlowTest.Context c, int key) throws Exception {
        Field inventory = SceneHandler.class.getDeclaredField("lotteryItemStacks");
        inventory.setAccessible(true);
        Map<?, ?> stacks = (Map<?, ?>) ((Map<?, ?>) inventory.get(h)).get(c.id);
        int result = 0;
        if (stacks == null) return result;
        for (Object stack : stacks.values()) {
            Field k = stack.getClass().getDeclaredField("key"), size = stack.getClass().getDeclaredField("size");
            k.setAccessible(true);
            size.setAccessible(true);
            if (k.getInt(stack) == key) result += size.getInt(stack);
        }
        return result;
    }
}
