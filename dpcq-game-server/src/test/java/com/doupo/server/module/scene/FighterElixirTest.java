package com.doupo.server.module.scene;

import com.doupo.protocol.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.Map;
import java.time.Instant;
import com.doupo.server.foundation.GameServerClock;
import com.doupo.server.module.system.PlayerRuntimeHandler;
import static org.junit.Assert.*;

public class FighterElixirTest {
    @Test
    public void fiveFighterMaterialsMergeAndSettleOverflowLikeCapturedFlow() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000000950L);
        fighter(h, c.id);
        set(h, "heroLevels", c.id, 9);
        setExp(h, c.id, 320L);
        h.changeAlchemyMaterial(c, 102, 5, 25);

        for (int round = 0; round < 5; round++) {
            c.writes.clear();
            h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
            if (round == 0) {
                assertNull(c.last(AlchemyNewMergeResp.class));
            } else {
                assertEquals(4, c.last(AlchemyNewMergeResp.class).getMergesCount());
            }
        }
        AlchemyNewMergeResp merge = c.last(AlchemyNewMergeResp.class);
        for (AlchemyNewMergeVo vo : merge.getMergesList()) {
            assertEquals(0, vo.getMake().getRid());
            assertEquals(1400, vo.getMake().getExp());
            assertEquals(5, vo.getMake().getNum());
            assertEquals(1, vo.getUseIdsCount());
        }

        c.writes.clear();
        h.takeAlchemy(c, AlchemyNewTakingReq.newBuilder()
                .addAlchemyId(0).addAlchemyId(1).addAlchemyId(2).addAlchemyId(3).build());
        assertEquals(5920, c.last(AlchemyNewTakingResp.class).getExp());
        assertEquals(10, c.last(HeroLevelUpgradeResp.class).getHeroVo().getLevel());
        assertEquals(5120, c.last(AlchemyNewExpChangeResp.class).getExp());

        c.writes.clear();
        h.breakHeroLevel(c, HeroLevelBreakReq.getDefaultInstance());
        assertEquals(11, c.last(HeroLevelBreakResp.class).getHeroVo().getLevel());
        assertEquals(12, c.last(HeroLevelUpgradeResp.class).getHeroVo().getLevel());
        assertEquals(3120, c.last(AlchemyNewExpChangeResp.class).getExp());

        c.writes.clear();
        h.breakHeroLevel(c, HeroLevelBreakReq.getDefaultInstance());
        assertEquals(13, c.last(HeroLevelBreakResp.class).getHeroVo().getLevel());
        assertEquals(14, c.last(HeroLevelUpgradeResp.class).getHeroVo().getLevel());
        assertEquals(720, c.last(AlchemyNewExpChangeResp.class).getExp());
    }

    @Test
    public void fighterUsesTask200027MaterialBeforeTask200036() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000000951L);
        fighter(h, c.id);
        reward(h, c, 200027);
        c.writes.clear();
        h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        assertNotNull(c.last(AlchemyNewMakeResp.class));
        assertEquals(1102, c.last(AlchemyNewMakeResp.class).getMakeIds(0).getRid());
        assertEquals(102, c.last(AlchemyNewMakeCostItemNumUpdateResp.class).getCostItem2Nums(0).getKey());
        assertEquals(1, c.last(AlchemyNewMakeCostItemNumUpdateResp.class).getCostItem2Nums(0).getValue());
        c.writes.clear();
        h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        assertNull("没有库存不能继续产药", c.last(AlchemyNewMakeResp.class));
    }

    @Test
    public void emptyOrdinaryInventoryCannotProduceFreePills() {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000000952L);
        h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        assertNull("普通灵液也必须扣除真实库存", c.last(AlchemyNewMakeResp.class));
    }

    @Test
    public void fighterTakingInfoUsesActualRidAndCannotTakeTwice() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000000953L);
        fighter(h, c.id);
        reward(h, c, 200027);
        h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        AlchemyNewTakingReq take = AlchemyNewTakingReq.newBuilder().addAlchemyId(0).build();
        c.writes.clear();
        h.takeAlchemy(c, take);
        assertEquals(1102, c.last(AlchemyNewTakingResp.class).getTakingInfo(0).getKey());
        assertEquals(1, c.last(AlchemyNewTakingResp.class).getTakingInfo(0).getValue());
        c.writes.clear();
        h.takeAlchemy(c, take);
        assertNull(c.last(AlchemyNewTakingResp.class));
    }

    @Test
    public void fiveOrdinaryThenOneFighterMatchesCapturedFullCostSnapshot() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000000954L);
        h.changeAlchemyMaterial(c, 101, 5, 3);
        for (int i = 0; i < 5; i++) {
            h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
            AlchemyNewTakingReq.Builder take = AlchemyNewTakingReq.newBuilder();
            c.last(AlchemyNewMakeResp.class).getMakeIdsList()
                    .forEach(pill -> take.addAlchemyId(pill.getId()));
            h.takeAlchemy(c, take.build());
        }
        fighter(h, c.id);
        reward(h, c, 200027);
        c.writes.clear();
        h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        assertEquals(AlchemyNewMakeCostItemNumUpdateResp.parseFrom(fixture("9010-75088.bin")),
                c.last(AlchemyNewMakeCostItemNumUpdateResp.class));
        AlchemyNewMakeResp made = c.last(AlchemyNewMakeResp.class);
        AlchemyNewMakeResp captured = AlchemyNewMakeResp.parseFrom(fixture("9009-75053.bin"));
        assertEquals(captured.getMakeTimes(), made.getMakeTimes());
        assertEquals(captured.getMakeIdsCount(), made.getMakeIdsCount());
        for (int i = 0; i < made.getMakeIdsCount(); i++) {
            // 抓包此前有两颗独立 source=2 产物；本用例未发，故 id 只对齐本地游标。
            assertEquals(captured.getMakeIds(i).toBuilder().setId(made.getMakeIds(i).getId()).build(),
                    made.getMakeIds(i));
        }
        h.takeAlchemy(c, AlchemyNewTakingReq.newBuilder().addAlchemyId(20).build());
        AlchemyNewTakingResp taking = c.last(AlchemyNewTakingResp.class);
        assertEquals(2, taking.getTakingInfoCount());
        assertEquals(1101, taking.getTakingInfo(0).getKey());
        assertEquals(20, taking.getTakingInfo(0).getValue());
        assertEquals(1102, taking.getTakingInfo(1).getKey());
        assertEquals(1, taking.getTakingInfo(1).getValue());
        AlchemyNewTakingResp capturedTaking = AlchemyNewTakingResp.parseFrom(fixture("9015-75055.bin"));
        assertEquals(capturedTaking.getTakingInfoList().subList(1, 3), taking.getTakingInfoList());
    }

    @Test
    public void firstDayLimitsAreSeparateAndDoNotBlockAlreadyMadePills() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000000955L);
        h.changeAlchemyMaterial(c, 101, 6, 3);
        for (int i = 0; i < 5; i++) h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        c.writes.clear();
        h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        assertNull(c.last(AlchemyNewMakeResp.class));
        assertEquals(5, c.last(AlchemyNewMakeCostItemNumUpdateResp.class).getCostItem2Nums(0).getValue());
        fighter(h, c.id);
        h.changeAlchemyMaterial(c, 102, 11, 25);
        for (int i = 0; i < 10; i++) h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        c.writes.clear();
        h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        assertNull(c.last(AlchemyNewMakeResp.class));
        assertNull(c.last(PackUpdateResp.class));
        assertEquals(5, c.last(AlchemyNewMakeCostItemNumUpdateResp.class).getCostItem2Nums(0).getValue());
        assertEquals(10, c.last(AlchemyNewMakeCostItemNumUpdateResp.class).getCostItem2Nums(1).getValue());
        h.takeAlchemy(c, AlchemyNewTakingReq.newBuilder().addAlchemyId(20).addAlchemyId(21).build());
        assertEquals(5600, c.last(AlchemyNewTakingResp.class).getExp());
        assertNull("斗者不能再次触发第一关的自动升二级", c.last(HeroLevelUpgradeResp.class));
        h.setServerOpenDay(2);
        h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        assertEquals(16, c.last(AlchemyNewMakeResp.class).getMakeTimes());
        assertFalse(c.last(PackUpdateResp.class).getPacks(0).getUpdateItems(0).hasPackItem());
    }

    @Test
    public void realRealmUpgradeImmediatelySwitchesTheNextMakeToFighter() {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000000960L);
        h.changeAlchemyMaterial(c, 101, 5, 3);
        for (int round = 0; round < 5; round++) {
            h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
            AlchemyNewTakingReq.Builder take = AlchemyNewTakingReq.newBuilder();
            c.last(AlchemyNewMakeResp.class).getMakeIdsList().forEach(p -> take.addAlchemyId(p.getId()));
            h.takeAlchemy(c, take.build());
            if (round < 3) h.breakHeroLevel(c, HeroLevelBreakReq.getDefaultInstance());
        }
        h.breakHeroLevel(c, HeroLevelBreakReq.getDefaultInstance());
        h.upgradeHeroStep(c, HeroStepUpgradeReq.getDefaultInstance());
        assertEquals(2, c.last(HeroStepUpgradeResp.class).getHeroVo().getStage());
        assertEquals(2, c.last(AlchemyNewLevelUpgradeResp.class).getLevel());
        h.changeAlchemyMaterial(c, 102, 1, 25);
        c.writes.clear();
        h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        assertEquals(1102, c.last(AlchemyNewMakeResp.class).getMakeIds(0).getRid());
        assertEquals(5, c.last(AlchemyNewMakeCostItemNumUpdateResp.class).getCostItem2Nums(0).getValue());
        assertEquals(1, c.last(AlchemyNewMakeCostItemNumUpdateResp.class).getCostItem2Nums(1).getValue());
    }

    @Test
    public void midnightHeartbeatClearsOnlyDailyCountsAndDayTwoAllowsMoreThanTen() throws Exception {
        TimedHandler h = new TimedHandler();
        h.setGameServerClock(new GameServerClock("2026-09-08T00:00:00+08:00"));
        // 固定在开服当天，测试不依赖执行机器日期。
        h.setServerOpenDay(1);
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000000956L);
        fighter(h, c.id);
        h.changeAlchemyMaterial(c, 102, 21, 25);
        for (int i = 0; i < 10; i++) h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        c.writes.clear();
        h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        assertNull(c.last(AlchemyNewMakeResp.class));
        h.now = Instant.parse("2026-09-08T16:00:00Z"); // 北京时间 9 日零点。
        c.writes.clear();
        new PlayerRuntimeHandler(h).heartbeat(c, HeartbeatReq.getDefaultInstance());
        assertEquals(0, c.last(AlchemyNewMakeCostItemNumUpdateResp.class).getCostItem2NumsCount());
        int writes = c.writes.size();
        new PlayerRuntimeHandler(h).heartbeat(c, HeartbeatReq.getDefaultInstance());
        assertEquals("同一天不重复清空", writes, c.writes.size());
        h.takeAlchemy(c, AlchemyNewTakingReq.newBuilder().addAlchemyId(0).build());
        assertEquals("跨日保留未服用丹药", 2800, c.last(AlchemyNewTakingResp.class).getExp());
        for (int i = 0; i < 11; i++) h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        assertEquals(21, c.last(AlchemyNewMakeResp.class).getMakeTimes());
        assertEquals(11, c.last(AlchemyNewMakeCostItemNumUpdateResp.class).getCostItem2Nums(0).getValue());
        c.writes.clear();
        h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        assertNull("第二天不限次数不等于无限库存", c.last(AlchemyNewMakeResp.class));
    }

    @Test
    public void fighterBreakKeepsJuqiLevelInCapturedResponse() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000000961L);
        fighter(h, c.id);
        setExp(h, c.id, 1180L);
        h.breakHeroLevel(c, HeroLevelBreakReq.getDefaultInstance());
        assertEquals(HeroLevelBreakResp.parseFrom(fixture("9018-75067.bin")),
                c.last(HeroLevelBreakResp.class));
        assertEquals(AlchemyNewExpChangeResp.parseFrom(fixture("9019-75060.bin")),
                c.last(AlchemyNewExpChangeResp.class));
    }

    @Test
    public void fighterUpgradeKeepsJuqiLevelInCapturedResponse() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000000962L);
        fighter(h, c.id);
        set(h, "heroLevels", c.id, 9);
        setExp(h, c.id, 1530L);
        h.upgradeHeroLevel(c, HeroLevelUpgradeReq.getDefaultInstance());
        assertEquals(HeroLevelUpgradeResp.parseFrom(fixture("9733-75057.bin")),
                c.last(HeroLevelUpgradeResp.class));
        assertEquals(AlchemyNewExpChangeResp.parseFrom(fixture("9734-75060.bin")),
                c.last(AlchemyNewExpChangeResp.class));
    }

    @Test
    public void everyFighterStarKeepsClientLevelAndActualMaterialInSync() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000000963L);
        fighter(h, c.id);
        h.changeAlchemyMaterial(c, 101, 5, 3);
        h.changeAlchemyMaterial(c, 102, 8, 25);
        for (int level = 8; level < 16; level++) {
            set(h, "heroLevels", c.id, level);
            setExp(h, c.id, PlayerRealmConfig.breakRequiredExp(level) + 1);
            c.writes.clear();
            HeroLevelVo hero;
            if (PlayerRealmConfig.needLevelBreak(level)) {
                h.breakHeroLevel(c, HeroLevelBreakReq.getDefaultInstance());
                hero = c.last(HeroLevelBreakResp.class).getHeroVo();
            } else {
                h.upgradeHeroLevel(c, HeroLevelUpgradeReq.getDefaultInstance());
                hero = c.last(HeroLevelUpgradeResp.class).getHeroVo();
            }
            assertEquals(level + 1, hero.getLevel());
            assertEquals("升星不能把斗者写回斗之气", 2, hero.getStage());
            assertEquals("75060 决定客户端灵液种类，不能重置为筑基", 2,
                    c.last(AlchemyNewExpChangeResp.class).getLevel());
            h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
            AlchemyNewMakeResp made = c.last(AlchemyNewMakeResp.class);
            assertEquals(4, made.getMakeIdsCount());
            assertEquals(1102, made.getMakeIds(0).getRid());
            assertEquals(102, c.last(AlchemyNewMakeCostItemNumUpdateResp.class)
                    .getCostItem2Nums(0).getKey());
            assertEquals(level - 7, c.last(AlchemyNewMakeCostItemNumUpdateResp.class)
                    .getCostItem2Nums(0).getValue());
            AlchemyNewTakingReq.Builder taking = AlchemyNewTakingReq.newBuilder();
            made.getMakeIdsList().forEach(p -> taking.addAlchemyId(p.getId()));
            h.takeAlchemy(c, taking.build());
            assertEquals(1121, c.last(AlchemyNewTakingResp.class).getExp());
            assertEquals(1102, c.last(AlchemyNewTakingResp.class).getTakingInfo(0).getKey());
        }
        assertEquals("聚气炼制不能消耗残留筑基库存", 5,
                h.changeAlchemyMaterial(c, 101, 0, 3).getPackItem().getSize());
        c.writes.clear();
        h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        assertNull("聚气用完不能回退扣筑基", c.last(AlchemyNewMakeResp.class));
    }

    @Test
    public void rewardStacksInsteadOfOverwritingAndDeductsItsActualSlot() {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000000957L);
        UpdateItem first = h.changeAlchemyMaterial(c, 101, 2, 47);
        reward(h, c, 200009); // 新增一份，不能丢掉原来的两份或固定扣槽 3。
        UpdateItem grant = c.last(PackUpdateResp.class).getPacks(0).getUpdateItems(0);
        assertEquals(47, grant.getItemIndex());
        assertEquals(3, grant.getPackItem().getSize());
        assertEquals(first.getPackItem().getObjectId(), grant.getPackItem().getObjectId());
        for (int i = 2; i >= 0; i--) {
            h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
            UpdateItem update = c.last(PackUpdateResp.class).getPacks(0).getUpdateItems(0);
            assertEquals(47, update.getItemIndex());
            assertEquals(i > 0, update.hasPackItem());
            if (i > 0) assertEquals(i, update.getPackItem().getSize());
        }
    }

    @Test
    public void playerResetClearsInventoryProductsAndCounters() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = new Chapter17FlowTest.Context(100000000958L);
        fighter(h, c.id);
        reward(h, c, 200027);
        h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        h.resetTutorialPlayer(c.id);
        c.writes.clear();
        h.takeAlchemy(c, AlchemyNewTakingReq.newBuilder().addAlchemyId(0).build());
        h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        assertTrue(c.writes.isEmpty());
        h.changeAlchemyMaterial(c, 101, 1, 3);
        h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
        assertEquals(1, c.last(AlchemyNewMakeResp.class).getMakeTimes());
        assertEquals(0, c.last(AlchemyNewMakeResp.class).getMakeIds(0).getId());
        assertEquals(101, c.last(AlchemyNewMakeCostItemNumUpdateResp.class).getCostItem2Nums(0).getKey());
    }

    @Test
    public void realConnectionDeductsCapturedFighterStackAndCannotReviveIt() throws Exception {
        Field actors = com.doupo.server.foundation.player.PlayerActorPool.class.getDeclaredField("actors");
        actors.setAccessible(true);
        Object previous = actors.get(null);
        io.netty.channel.embedded.EmbeddedChannel channel = new io.netty.channel.embedded.EmbeddedChannel();
        try {
            actors.set(null, new akka.actor.ActorRef[]{null});
            com.doupo.server.foundation.player.PlayerConnectionContext c =
                    new com.doupo.server.foundation.player.PlayerConnectionContext(channel);
            c.bindPlayer(100000000959L);
            SceneHandler h = new SceneHandler();
            fighter(h, c.getId());
            PackUpdateResp captured = PackUpdateResp.parseFrom(fixture("9717-50402.bin"));
            UpdateItem stack = captured.getPacks(0).getUpdateItems(0);
            c.write(50402, captured.toBuilder().setPacks(0, captured.getPacks(0).toBuilder()
                    .setUpdateItems(0, stack.toBuilder().setPackItem(stack.getPackItem().toBuilder().setSize(5))))
                    .build(), 0);
            // 丢弃准备库存的包；随后检查实际连接发出的扣减正文。
            while (channel.readOutbound() != null) { }
            h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
            byte[] packet = channel.readOutbound();
            MessageWrapper wrapper = MessageWrapper.parseFrom(packet);
            assertEquals(50402, wrapper.getProtoId());
            assertEquals(captured, PackUpdateResp.parseFrom(wrapper.getData()));
            assertEquals(4, c.itemStack(102).getPackItem().getSize());
            for (int i = 0; i < 4; i++) h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
            assertNull(c.itemStack(102));
            while (channel.readOutbound() != null) { }
            h.makeAlchemy(c, AlchemyNewMakeReq.getDefaultInstance());
            assertNull("真实连接库存归零后，不能从旧状态复活灵液", channel.readOutbound());
        } finally {
            actors.set(null, previous);
            channel.finishAndReleaseAll();
        }
    }

    private static byte[] fixture(String name) throws Exception {
        try (java.io.InputStream in = FighterElixirTest.class.getResourceAsStream("/elixir/" + name)) {
            assertNotNull(name, in);
            return com.google.protobuf.ByteString.readFrom(in).toByteArray();
        }
    }

    private static class TimedHandler extends SceneHandler {
        Instant now = Instant.parse("2026-09-08T15:59:59Z");
        @Override Instant alchemyNow() { return now; }
    }

    static void fighter(SceneHandler h, long id) throws Exception {
        set(h, "heroLevels", id, 8);
        set(h, "heroStages", id, 2);
        set(h, "alchemyLevels", id, 2);
    }

    @SuppressWarnings("unchecked")
    private static void setExp(SceneHandler h, long id, long exp) throws Exception {
        Field field = SceneHandler.class.getDeclaredField("alchemyExpPools");
        field.setAccessible(true);
        ((Map<Long, Long>) field.get(h)).put(id, exp);
    }

    @SuppressWarnings("unchecked")
    static void set(SceneHandler h, String name, long id, int value) throws Exception {
        Field field = SceneHandler.class.getDeclaredField(name);
        field.setAccessible(true);
        ((Map<Long, Integer>) field.get(h)).put(id, value);
    }

    static void reward(SceneHandler h, Chapter17FlowTest.Context c, int task) {
        h.rewardGuidanceTask(c, TaskRewardReq.newBuilder().setTaskId(
                TaskUniqueKey.newBuilder().setTaskResourceId(task)).build());
    }
}
