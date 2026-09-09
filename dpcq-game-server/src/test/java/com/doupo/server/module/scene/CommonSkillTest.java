package com.doupo.server.module.scene;

import com.doupo.protocol.*;
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.Map;
import static org.junit.Assert.*;

public class CommonSkillTest {
    private static Chapter17Progress progress(SceneHandler handler, long player) throws Exception {
        Method method = SceneHandler.class.getDeclaredMethod("later", long.class);
        method.setAccessible(true);
        return (Chapter17Progress) method.invoke(handler, player);
    }

    private static void invoke(SceneHandler handler, Chapter17FlowTest.Context context,
                               String method, com.google.protobuf.GeneratedMessageV3 request) throws Exception {
        handler.getClass().getMethod(method, org.gaming.fakecmd.side.game.IPlayerContext.class,
                request.getClass()).invoke(handler, context, request);
    }

    private static Chapter17FlowTest.Context draw(SceneHandler handler, long player) throws Exception {
        Chapter17FlowTest.Context context = new Chapter17FlowTest.Context(player);
        progress(handler, player).rewarded.add(200102);
        progress(handler, player).items.put(100212, 10L);
        handler.drawNewFightSkill(context, LotteryDrawReq.newBuilder().setLotteryTypeValue(13).build());
        assertEquals(4, context.last(PlayerCommonSkillResp.class).getCommonSkillLevelCount());
        context.writes.clear();
        return context;
    }

    private static CommonSkillPutOnReq wear(int slot, int skill) {
        return CommonSkillPutOnReq.newBuilder().addSlotAndSkillId(
                IntegerAndIntegerPairEntry.newBuilder().setKey(slot).setValue(skill)).build();
    }

    @Test
    public void equipReplaceAndRemoveUseFullSnapshotWithoutConsumingCards() throws Exception {
        SceneHandler handler = new SceneHandler();
        Chapter17FlowTest.Context context = draw(handler, 100000000931L);
        invoke(handler, context, "putOnCommonSkill", wear(1, 9100003));
        assertEquals(9100003, context.last(PlayerCommonSkillResp.class).getWearIds(0).getValue());
        assertEquals(4, context.last(PlayerCommonSkillResp.class).getCommonSkillLevelCount());
        assertEquals(1, context.last(CommonSkillPutOnResp.class).getSlotAndSkillId(0).getKey());
        assertNull(context.last(PackUpdateResp.class));
        invoke(handler, context, "putOnCommonSkill", wear(1, 9100002));
        assertEquals(9100002, context.last(PlayerCommonSkillResp.class).getWearIds(0).getValue());
        invoke(handler, context, "putOnCommonSkill", CommonSkillPutOnReq.getDefaultInstance());
        assertEquals(0, context.last(PlayerCommonSkillResp.class).getWearIdsCount());
        assertEquals(0, context.last(CommonSkillPutOnResp.class).getSlotAndSkillIdCount());
    }

    @Test
    public void rejectLockedUnknownAndDuplicateAssignmentsAtomically() throws Exception {
        SceneHandler handler = new SceneHandler();
        Chapter17FlowTest.Context context = draw(handler, 100000000932L);
        invoke(handler, context, "putOnCommonSkill", wear(1, 9100003));
        context.writes.clear();
        for (CommonSkillPutOnReq invalid : new CommonSkillPutOnReq[] {
                wear(0, 9100003), wear(7, 9100003), wear(2, 9100002),
                wear(6, 9100002), wear(1, 9100001), wear(1, 999),
                wear(1, 9100002).toBuilder().addAllSlotAndSkillId(wear(1, 9100003).getSlotAndSkillIdList()).build()}) {
            invoke(handler, context, "putOnCommonSkill", invalid);
        }
        assertTrue(context.writes.isEmpty());
        invoke(handler, context, "putOnCommonSkill", wear(1, 9100003));
        assertEquals(9100003, context.last(PlayerCommonSkillResp.class).getWearIds(0).getValue());
        java.lang.reflect.Field levels = SceneHandler.class.getDeclaredField("heroLevels");
        levels.setAccessible(true);
        ((Map<Long,Integer>) levels.get(handler)).put(context.id, 15);
        CommonSkillPutOnReq two = wear(1, 9100003).toBuilder()
                .addAllSlotAndSkillId(wear(2, 9100002).getSlotAndSkillIdList()).build();
        invoke(handler, context, "putOnCommonSkill", two);
        assertEquals(2, context.last(PlayerCommonSkillResp.class).getWearIdsCount());
        context.writes.clear();
        invoke(handler, context, "putOnCommonSkill", wear(1, 9100003).toBuilder()
                .addAllSlotAndSkillId(wear(2, 9100003).getSlotAndSkillIdList()).build());
        assertTrue(context.writes.isEmpty());
    }

    @Test
    public void oneKeyUpgradeUsesActualInventoryAndLubanCostNotCaptureTotals() throws Exception {
        SceneHandler handler = new SceneHandler();
        Chapter17FlowTest.Context context = draw(handler, 100000000933L);
        invoke(handler, context, "putOnCommonSkill", wear(1, 9100003));
        context.writes.clear();
        invoke(handler, context, "oneKeyCommonSkillLevelUp", CommonSkillOneKeyLevelUpReq.getDefaultInstance());
        CommonSkillOneKeyLevelUpResp result = context.last(CommonSkillOneKeyLevelUpResp.class);
        assertEquals(1, result.getNewSkillLevelCount());
        assertEquals(9100002, result.getNewSkillLevel(0).getKey());
        assertEquals(1, result.getOldSkillLevel(0).getValue());
        assertEquals(2, result.getNewSkillLevel(0).getValue());
        assertEquals(1, context.last(PackUpdateResp.class).getPacks(0).getUpdateItems(0).getPackItem().getSize());
        assertEquals(9100003, context.last(PlayerCommonSkillResp.class).getWearIds(0).getValue());
        context.writes.clear();
        invoke(handler, context, "oneKeyCommonSkillLevelUp", CommonSkillOneKeyLevelUpReq.getDefaultInstance());
        assertEquals(0, context.last(CommonSkillOneKeyLevelUpResp.class).getNewSkillLevelCount());
        assertNull(context.last(PackUpdateResp.class));
        assertEquals(4, context.last(PlayerCommonSkillResp.class).getCommonSkillLevelCount());
    }

    @Test
    public void singleUpgradeRemovesEmptyStackAndCannotUpgradeUnknownOrMaxLevel() throws Exception {
        SceneHandler handler = new SceneHandler();
        Chapter17FlowTest.Context context = draw(handler, 100000000935L);
        Chapter17Progress state = progress(handler, context.id);
        UpdateItem original = state.commonSkillItems.get(9100002);
        state.commonSkillItems.put(9100002, original.toBuilder()
                .setPackItem(original.getPackItem().toBuilder().setSize(2)).build());
        handler.commonSkillLevelUp(context, CommonSkillLevelUpReq.newBuilder().setCommonSkillId(9100002).build());
        assertEquals(2, context.last(CommonSkillLevelUpResp.class).getNewLevel());
        UpdateItem consumed = context.last(PackUpdateResp.class).getPacks(0).getUpdateItems(0);
        assertEquals(original.getItemIndex(), consumed.getItemIndex());
        assertFalse(consumed.hasPackItem());
        assertFalse(state.commonSkillItems.containsKey(9100002));
        context.writes.clear();
        handler.commonSkillLevelUp(context, CommonSkillLevelUpReq.newBuilder().setCommonSkillId(9100002).build());
        handler.commonSkillLevelUp(context, CommonSkillLevelUpReq.newBuilder().setCommonSkillId(999).build());
        assertTrue(context.writes.isEmpty());
        int maximum = 0;
        for (com.fasterxml.jackson.databind.JsonNode row : Chapter17Data.COMMON_SKILLS.path("levels")) {
            if (row.path("LevelGroup").asInt() == 2) maximum = Math.max(maximum, row.path("Level").asInt());
        }
        state.commonSkillLevels.put(9100002, maximum);
        state.commonSkillItems.put(9100002, original);
        handler.commonSkillLevelUp(context, CommonSkillLevelUpReq.newBuilder().setCommonSkillId(9100002).build());
        assertTrue(context.writes.isEmpty());
        assertEquals(original, state.commonSkillItems.get(9100002));
    }
}
