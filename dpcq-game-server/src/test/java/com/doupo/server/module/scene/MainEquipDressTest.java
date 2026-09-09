package com.doupo.server.module.scene;

import com.doupo.protocol.*;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.fakecmd.side.game.PlayerCmdRegister;
import org.gaming.fakecmd.side.game.IPlayerContext;
import com.google.protobuf.Message;
import org.junit.Test;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

public class MainEquipDressTest {
    @Test
    public void dressRequestIsRegisteredOnThePlayerActorRoute() {
        for (Method method : SceneHandler.class.getMethods()) {
            if (method.isAnnotationPresent(PlayerCmd.class)
                    && method.getParameterTypes().length == 2
                    && method.getParameterTypes()[1].getSimpleName().equals("MainEquipDressReq")) return;
        }
        fail("75351 MainEquipDressReq has no PlayerCmd handler; the equipment bar cannot update");
    }

    @Test
    public void capturedLeggingsDressUpdatesSlotFiveAndPreservesTheMainAnimationFlag() throws Exception {
        SceneHandler handler = new SceneHandler();
        Context c = opened(handler, 100000000941L);
        handler.takeHangUpReward(c, MainMapTakeHangUpRewardReq.getDefaultInstance());
        MainEquipVO leggings = handler.mainEquipInfo(c.id).getPacks(1);
        assertEquals(15, leggings.getEquipId());
        c.clear();
        handler.confirmClaimedMainEquip(c, MainMapTakeHangUpMainEquipRewardReq.newBuilder()
                .setObjectId(leggings.getObjectId()).setShowReward(false).build());
        assertTrue(c.writes.isEmpty());
        MainEquipDressReq request = MainEquipDressReq.parseFrom(fixture("9126-75351.bin"));
        assertEquals(770167612774547824L, request.getObjectId());
        handler.dressMainEquip(c, request.toBuilder().setObjectId(leggings.getObjectId()).build());
        assertEquals(Arrays.asList(75354, 75356), c.ids);
        MainEquipBagReduceResp reduce = MainEquipBagReduceResp.parseFrom(fixture("9127-75354.bin"));
        assertEquals(reduce.toBuilder().clearReduceList().addReduceList(leggings.getObjectId()).build(),
                c.last(MainEquipBagReduceResp.class));
        MainEquipPositionInfoResp captured = MainEquipPositionInfoResp.parseFrom(fixture("9128-75356.bin"));
        assertEquals(captured.toBuilder().setVo(captured.getVo().toBuilder()
                .setVo(captured.getVo().getVo().toBuilder().setObjectId(leggings.getObjectId()))).build(),
                c.last(MainEquipPositionInfoResp.class));
        MainEquipInfoResp snapshot = MainEquipInfoResp.parseFrom(handler.mainEquipInfo(c.id).toByteArray());
        assertEquals(1, snapshot.getPacksCount());
        assertEquals(5, snapshot.getHeroVoList(0).getSlotVoList(0).getPosition());
        assertEquals(leggings, snapshot.getHeroVoList(0).getSlotVoList(0).getVo());
    }

    @Test
    public void helmetAndLeggingsStayInIndependentSlotsAndDuplicateClicksDoNothing() {
        SceneHandler handler = new SceneHandler();
        Context c = opened(handler, 100000000942L);
        handler.takeHangUpReward(c, MainMapTakeHangUpRewardReq.getDefaultInstance());
        List<MainEquipVO> equips = handler.mainEquipInfo(c.id).getPacksList();
        handler.dressMainEquip(c, request(equips.get(0), 2));
        handler.dressMainEquip(c, request(equips.get(1), 5));
        c.clear();
        handler.dressMainEquip(c, request(equips.get(1), 5));
        handler.takeHangUpReward(c, MainMapTakeHangUpRewardReq.getDefaultInstance());
        assertTrue(c.writes.isEmpty());
        assertEquals(0, handler.mainEquipInfo(c.id).getPacksCount());
        assertEquals(2, handler.mainEquipInfo(c.id).getHeroVoList(0).getSlotVoListCount());
    }

    @Test
    public void wrongSlotForeignObjectAndLockedHeroCannotConsumeOwnedEquipment() {
        SceneHandler handler = new SceneHandler();
        Context owner = opened(handler, 100000000943L);
        Context other = opened(handler, 100000000944L);
        handler.takeHangUpReward(owner, MainMapTakeHangUpRewardReq.getDefaultInstance());
        MainEquipVO leggings = handler.mainEquipInfo(owner.id).getPacks(1);
        owner.clear(); other.clear();
        handler.dressMainEquip(owner, request(leggings, 2));
        handler.dressMainEquip(owner, request(leggings, 5).toBuilder().setHeroIndex(1).build());
        handler.dressMainEquip(owner, request(leggings, 5).toBuilder().setHeroIndex(-1).build());
        handler.dressMainEquip(other, request(leggings, 5));
        assertTrue(owner.writes.isEmpty());
        assertTrue(other.writes.isEmpty());
        assertEquals(2, handler.mainEquipInfo(owner.id).getPacksCount());
        assertEquals(0, handler.mainEquipInfo(owner.id).getHeroVoList(0).getSlotVoListCount());
    }

    @Test
    public void pendingRewardCannotBeWornBeforeBeingClaimed() {
        SceneHandler handler = new SceneHandler();
        Context c = opened(handler, 100000000945L);
        MainEquipVO pending = c.last(MainMapChapterInfoResp.class)
                .getMainMapChapterHangUpReward(1).getMainEquipVo();
        c.clear();
        handler.confirmClaimedMainEquip(c, MainMapTakeHangUpMainEquipRewardReq.newBuilder()
                .setObjectId(pending.getObjectId()).build());
        handler.dressMainEquip(c, request(pending, 5));
        assertTrue(c.writes.isEmpty());
        handler.takeHangUpReward(c, MainMapTakeHangUpRewardReq.getDefaultInstance());
        assertEquals(2, handler.mainEquipInfo(c.id).getPacksCount());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void capturedReplacementReturnsTheOldObjectAndRetainsSlotLevel() throws Exception {
        SceneHandler handler = new SceneHandler();
        Context c = opened(handler, 100000000946L);
        handler.takeHangUpReward(c, MainMapTakeHangUpRewardReq.getDefaultInstance());
        MainEquipVO old = handler.mainEquipInfo(c.id).getPacks(0);
        handler.dressMainEquip(c, request(old, 2));
        // 抓包换装前槽位已强化到 5 级。本用例设定前置状态，不伪造强化请求。
        Map<Long, ?> states = (Map<Long, ?>) field(handler, "mainEquipStates");
        Map<Integer, Map<Integer, MainEquipSlotVO>> heroes =
                (Map<Integer, Map<Integer, MainEquipSlotVO>>) field(states.get(c.id), "heroes");
        MainEquipSlotVO slot = heroes.get(0).get(2);
        heroes.get(0).put(2, slot.toBuilder().setLevel(5)
                .setVo(old.toBuilder().setLevel(5)).build());
        MainEquipPositionInfoResp expected = MainEquipPositionInfoResp.parseFrom(fixture("10063-75356.bin"));
        MainEquipVO incoming = expected.getVo().getVo().toBuilder().setLevel(0).build();
        Map<Long,List<MainEquipVO>> pending = (Map<Long,List<MainEquipVO>>) field(handler,"pendingHangUpEquips");
        pending.put(c.id, Arrays.asList(incoming));
        handler.takeHangUpReward(c, MainMapTakeHangUpRewardReq.getDefaultInstance());
        c.clear();
        handler.dressMainEquip(c, MainEquipDressReq.parseFrom(fixture("10060-75351.bin")));
        assertEquals(Arrays.asList(75354, 75355, 75356), c.ids);
        assertEquals(MainEquipBagReduceResp.parseFrom(fixture("10061-75354.bin")), c.last(MainEquipBagReduceResp.class));
        MainEquipBagAddResp returned = MainEquipBagAddResp.parseFrom(fixture("10062-75355.bin"));
        assertEquals(returned.toBuilder().setAddList(0, returned.getAddList(0).toBuilder()
                .setObjectId(old.getObjectId())).build(), c.last(MainEquipBagAddResp.class));
        assertEquals(expected, c.last(MainEquipPositionInfoResp.class));
        assertEquals(2, handler.mainEquipInfo(c.id).getPacksCount()); // 旧头盔 + 未穿护腿。
        assertEquals(incoming.getObjectId(), handler.mainEquipInfo(c.id).getHeroVoList(0).getSlotVoList(0).getVo().getObjectId());
    }

    @Test
    public void tutorialResetClearsBothTheBagAndTheEquipmentBar() {
        SceneHandler handler = new SceneHandler();
        Context c = opened(handler, 100000000947L);
        handler.takeHangUpReward(c, MainMapTakeHangUpRewardReq.getDefaultInstance());
        handler.dressMainEquip(c, request(handler.mainEquipInfo(c.id).getPacks(1), 5));
        handler.resetTutorialPlayer(c.id);
        assertEquals(0, handler.mainEquipInfo(c.id).getPacksCount());
        assertEquals(0, handler.mainEquipInfo(c.id).getHeroVoList(0).getSlotVoListCount());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void replayedRewardCannotPutAWornObjectBackIntoTheBag() throws Exception {
        SceneHandler handler = new SceneHandler();
        Context c = opened(handler, 100000000948L);
        handler.takeHangUpReward(c, MainMapTakeHangUpRewardReq.getDefaultInstance());
        MainEquipVO leggings = handler.mainEquipInfo(c.id).getPacks(1);
        handler.dressMainEquip(c, request(leggings, 5));
        Map<Long,List<MainEquipVO>> pending = (Map<Long,List<MainEquipVO>>) field(handler,"pendingHangUpEquips");
        pending.put(c.id, Arrays.asList(leggings));
        c.clear();
        handler.takeHangUpReward(c, MainMapTakeHangUpRewardReq.getDefaultInstance());
        assertTrue(c.writes.isEmpty());
        assertEquals(1, handler.mainEquipInfo(c.id).getPacksCount());
        assertEquals(1, handler.mainEquipInfo(c.id).getHeroVoList(0).getSlotVoListCount());
    }

    @Test
    public void protobufRequestDispatchesThroughTheActualGamingCommandRegistry() throws Exception {
        SceneHandler handler = new SceneHandler();
        Context c = opened(handler, 100000000949L);
        handler.takeHangUpReward(c, MainMapTakeHangUpRewardReq.getDefaultInstance());
        MainEquipVO equip = handler.mainEquipInfo(c.id).getPacks(1);
        java.lang.reflect.Constructor<PlayerCmdRegister> ctor = PlayerCmdRegister.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        PlayerCmdRegister registry = ctor.newInstance();
        registry.register(handler);
        assertNotNull(registry.getInvoker(61999));
        assertNotNull(registry.getInvoker(75351));
        c.clear();
        registry.handle(new PlayerCmdRegister.IPlayerCmdMessage() {
            public IPlayerContext getPlayerContext() { return c; }
            public int getMessageId() { return 1; }
            public int getCmd() { return 75351; }
            public ByteString getData() { return request(equip, 5).toByteString(); }
            public void onException(Exception e, Message request) { throw new AssertionError(e); }
            public void onResponse(Object response) { fail("75351 uses 75354/75356 pushes, not a same-ID response"); }
        });
        assertEquals(Arrays.asList(75354, 75356), c.ids);
        assertEquals(equip, c.last(MainEquipPositionInfoResp.class).getVo().getVo());
    }

    private static MainEquipDressReq request(MainEquipVO vo, int position) {
        return MainEquipDressReq.newBuilder().setHeroIndex(0).setPosition(position)
                .setObjectId(vo.getObjectId()).setMain(true).build();
    }

    private static Context opened(SceneHandler handler, long id) {
        Context c = new Context(id);
        handler.rewardGuidanceTask(c, TaskRewardReq.newBuilder().setTaskId(
                TaskUniqueKey.newBuilder().setTaskResourceId(200029)).build());
        return c;
    }

    private static Object field(Object target, String name) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return f.get(target);
    }

    private static ByteString fixture(String name) throws Exception {
        try (InputStream in = MainEquipDressTest.class.getResourceAsStream("/main-equip/" + name)) {
            assertNotNull(name, in);
            return ByteString.readFrom(in);
        }
    }

    static class Context extends Chapter17FlowTest.Context {
        final List<Integer> ids = new ArrayList<>();
        Context(long id) { super(id); }
        @Override public void write(int id, GeneratedMessageV3 message, int requestId) {
            ids.add(id); super.write(id, message, requestId);
        }
        void clear() { ids.clear(); writes.clear(); }
    }
}
