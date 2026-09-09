package com.doupo.server.module.scene;

import com.doupo.protocol.*;
import com.google.protobuf.ByteString;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;
import static com.doupo.server.module.scene.Chapter18FlowTest.*;

public class ContinuationTaskTest {
    private static ByteString fixture(String name) throws Exception {
        try (java.io.InputStream in = ContinuationTaskTest.class.getResourceAsStream("/continuation/" + name)) {
            assertNotNull(name, in);
            return ByteString.readFrom(in);
        }
    }

    @Test
    public void capturedRenameConsumesOneCardAndPaysMaterialRewardOnlyOnce() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = opened(h, 100000001811L);
        Chapter17Progress s = progress(h, c);
        s.tasks.add(200105);
        ChangeNameReq req = ChangeNameReq.parseFrom(fixture("10436-51351.bin"));
        h.changePlayerName(c, req);
        assertTrue(c.writes.isEmpty());
        s.items.put(10041, 1L);
        h.changePlayerName(c, req);
        assertEquals(ChangeNameResp.parseFrom(fixture("10438-51352.bin")).toBuilder().setPlayerId(c.id).build(),
                c.last(ChangeNameResp.class));
        assertEquals(Long.valueOf(0), s.items.get(10041));
        assertTrue(c.task(200105, TaskPhase.FINISHED));
        reward(h, c, 200105);
        assertEquals(Long.valueOf(26000), s.items.get(200001));
        c.writes.clear();
        reward(h, c, 200105);
        h.changePlayerName(c, req);
        assertTrue(c.writes.isEmpty());
        s.items.put(10041, 1L);
        for (String invalid : new String[]{"", "123", "abcdefg", "\n"}) {
            h.changePlayerName(c, req.toBuilder().setName(invalid).build());
        }
        assertEquals(Long.valueOf(1), s.items.get(10041));
        assertTrue(c.writes.isEmpty());
    }

    @Test
    public void capturedQuickStrengthenCosts900AndCompletesConsumptionNotDecompositionTask() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = opened(h, 100000001812L);
        Chapter17Progress s = progress(h, c);
        s.items.put(200001, 26000L);
        s.tasks.add(200106);
        MainEquipPositionInfoResp sample = MainEquipPositionInfoResp.parseFrom(fixture("10461-75356.bin"));
        MainEquipVO equip = sample.getVo().getVo().toBuilder().setObjectId(c.id * 1000 + 1).setLevel(0).setExp(0).build();
        addBag(h, c, equip);
        h.dressMainEquip(c, MainEquipDressReq.newBuilder().setObjectId(equip.getObjectId()).setPosition(3).build());
        c.writes.clear();
        MainEquipLevelUpReq req = MainEquipLevelUpReq.parseFrom(fixture("10458-75357.bin"));
        h.levelUpMainEquip(c, req);
        assertEquals(5, c.last(MainEquipLevelUpResp.class).getVo().getLevel());
        assertEquals(Long.valueOf(25100), s.items.get(200001));
        assertEquals(sample.toBuilder().setVo(sample.getVo().toBuilder()
                .setVo(sample.getVo().getVo().toBuilder().setObjectId(equip.getObjectId()))).build(),
                c.last(MainEquipPositionInfoResp.class));
        assertEquals(TaskUpdateResp.parseFrom(fixture("10463-50906.bin")), c.last(TaskUpdateResp.class));
        c.writes.clear();
        h.levelUpMainEquip(c, req); // 升6级要求人物13级；不得扣料。
        assertTrue(c.writes.isEmpty());
        ((Map<Long,Integer>)field(h,"heroLevels")).put(c.id,13);
        h.levelUpMainEquip(c, req);
        assertEquals(10, c.last(MainEquipLevelUpResp.class).getVo().getLevel());
        assertEquals(Long.valueOf(23100), s.items.get(200001));
        c.writes.clear();
        h.levelUpMainEquip(c, req); // 升11级要求人物17级。
        assertTrue(c.writes.isEmpty());
        reward(h,c,200106);
        assertEquals(Long.valueOf(10),s.items.get(100212));
    }

    @Test
    public void strengthMaterialBeforeTaskDoesNotCountAndInsufficientCostDoesNotDisappear() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = opened(h, 100000001813L);
        Chapter17Progress s = progress(h,c);
        MainEquipVO equip = MainEquipVO.newBuilder().setObjectId(10001).setEquipId(12).build();
        addBag(h,c,equip);
        h.dressMainEquip(c,MainEquipDressReq.newBuilder().setObjectId(10001).setPosition(2).build());
        MainEquipLevelUpReq req = MainEquipLevelUpReq.newBuilder().setPosition(2).setQuickUpgrade(true).build();
        s.items.put(200001,150L);
        h.levelUpMainEquip(c,req);
        assertEquals(1,c.last(MainEquipLevelUpResp.class).getVo().getLevel());
        assertEquals(Long.valueOf(50),s.items.get(200001));
        assertEquals(0,s.taskEquipMaterialConsumed);
        s.tasks.add(200106);
        c.writes.clear();
        h.levelUpMainEquip(c,req);
        assertTrue(c.writes.isEmpty());
        assertEquals(Long.valueOf(50),s.items.get(200001));
        s.items.put(200001,100L);
        h.levelUpMainEquip(c,req);
        assertEquals(2,c.last(MainEquipLevelUpResp.class).getVo().getLevel());
        assertTrue(c.task(200106,TaskPhase.FINISHED));
    }

    @Test
    public void decomposeDeduplicatesIdsAndNeverConsumesWornLockedOrForeignEquipment() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = opened(h, 100000001814L);
        Chapter17Progress s = progress(h,c);
        s.tasks.add(200106);
        MainEquipVO equip = MainEquipVO.newBuilder().setObjectId(1).setEquipId(12).build();
        addBag(h,c,equip,equip.toBuilder().setObjectId(2).setLock(true).build(),equip.toBuilder().setObjectId(3).build());
        h.dressMainEquip(c,MainEquipDressReq.newBuilder().setObjectId(3).setPosition(2).build());
        c.writes.clear();
        for (long invalid : new long[]{2,3,999}) h.decomposeMainEquip(c,
                MainEquipDecomposeReq.newBuilder().addObjectIds(1).addObjectIds(invalid).build());
        assertTrue(c.writes.isEmpty());
        MainEquipDecomposeReq req = MainEquipDecomposeReq.newBuilder().addObjectIds(1).addObjectIds(1).setClientParam(true).build();
        h.decomposeMainEquip(c,req);
        assertEquals(1,c.last(MainEquipDecomposeResp.class).getObjectIdsCount());
        assertTrue(c.last(MainEquipDecomposeResp.class).getClientParam());
        assertEquals(Long.valueOf(20),s.items.get(200001));
        assertEquals(2,c.last(MainEquipBagReduceResp.class).getType());
        assertEquals(0,s.taskEquipMaterialConsumed);
        assertFalse(c.task(200106,TaskPhase.FINISHED));
        c.writes.clear();
        h.decomposeMainEquip(c,req);
        assertTrue(c.writes.isEmpty());
        assertEquals(Long.valueOf(20),s.items.get(200001));
    }

    @Test
    public void postTaskDrawAddsToInventoryThenOpensChapter19GoalWithoutDuplicateReward() throws Exception {
        SceneHandler h = new SceneHandler();
        Chapter17FlowTest.Context c = opened(h, 100000001815L);
        Chapter17Progress s = progress(h,c);
        s.rewarded.add(200102);
        s.items.put(100212,10L);
        LotteryDrawReq req = LotteryDrawReq.newBuilder().setLotteryTypeValue(13).build();
        h.drawNewFightSkill(c,req);
        s.tasks.add(200107);
        c.writes.clear();
        h.drawNewFightSkill(c,req);
        assertTrue(c.writes.isEmpty());
        Map<Integer,Integer> before = new HashMap<>();
        s.commonSkillItems.forEach((id,item)->before.put(id,item.getPackItem().getSize()));
        Set<Integer> known = new HashSet<>(s.commonSkillLevels.keySet());
        s.items.put(100212,10L);
        h.drawNewFightSkill(c,req);
        assertEquals(20,c.last(LotteryInfoResp.class).getTotalDrawTime());
        Map<Integer,Integer> gains = new HashMap<>();
        for (RewardItemVo r:c.last(LotteryDrawResp.class).getRewardItemVosList()) gains.merge(r.getItemKey(),(int)r.getAmount(),Integer::sum);
        for (int id:gains.keySet()) {
            int count=before.getOrDefault(id,0)+gains.get(id)-(known.contains(id)?0:1);
            assertEquals(count,s.commonSkillItems.containsKey(id)?s.commonSkillItems.get(id).getPackItem().getSize():0);
        }
        assertTrue(c.task(200107,TaskPhase.FINISHED));
        assertEquals(Long.valueOf(0),s.items.get(100212));
        reward(h,c,200107);
        assertTrue(c.task(200108,TaskPhase.PROGRESS));
        c.writes.clear();
        reward(h,c,200107);
        h.drawNewFightSkill(c,req);
        assertTrue(c.writes.isEmpty());
    }

    private static void addBag(SceneHandler h, Chapter17FlowTest.Context c, MainEquipVO... equips) throws Exception {
        ((Map<Long,List<MainEquipVO>>)field(h,"pendingHangUpEquips")).put(c.id,Arrays.asList(equips));
        h.takeHangUpReward(c,MainMapTakeHangUpRewardReq.getDefaultInstance());
    }
}
