package com.doupo.server.module.scene;

import com.doupo.protocol.BaoBuStartResp;
import com.doupo.protocol.BattleLogEntryVO;
import com.doupo.protocol.BattleLogItemVO;
import com.doupo.protocol.BattleLogUpdateVisibleResp;
import com.doupo.protocol.BattleLogVO;
import com.doupo.protocol.FightSkillUpdateResp;
import com.doupo.protocol.SceneUnitVo;
import com.doupo.protocol.SkillActionResp;
import com.doupo.protocol.UseSkillResp;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedOutputStream;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class WutanBattleLogIdentityTest {

    private static final long PLAYER = 100100000001L;
    private static final long UNIT = PLAYER * 1000L + 1L;

    @Test
    public void allCapturedWavesReuseLocalHeroAndRemoveEveryRecordedIdentity() throws Exception {
        for (int stage = 1; stage <= 10; stage++) {
            for (int wave : new int[] {1, 2, 3, 5}) {
                int chapter = 10300000 + stage * 100 + wave;
                WutanCapturedLog.Capture capture = WutanCapturedLog.load(chapter);
                assertNotNull("missing capture " + chapter, capture);
                SceneUnitVo recorded = hero(BattleLogVO.parseFrom(capture.data));
                // 同坐标和坐标缺省也要换身份，不能被坐标转换的提前返回跳过。
                float[][] points = {{0f, 0f, 0f},
                        {capture.originX, capture.originY, capture.originZ},
                        {capture.originX + 12f, capture.originY + 2f, capture.originZ + 40f}};
                for (float[] point : points) {
                    ByteString data = WutanBattleLogIdentity.rebind(WutanCapturedLog.relocated(
                            capture, point[0], point[1], point[2]), PLAYER);
                    SceneUnitVo local = hero(BattleLogVO.parseFrom(data));
                    Set<Long> clientHeroIds = new HashSet<Long>(Arrays.asList(UNIT));
                    clientHeroIds.add(local.getBaseInfoVo().getId());
                    assertEquals("existing hero must be reused: " + chapter, 1, clientHeroIds.size());
                    assertEquals(PLAYER, local.getHeroVo().getPlayerId());
                    assertEquals(UNIT, local.getHeroVo().getCrossHeroShortInfo().getSceneUnitUid());
                    assertFalse(local.getHeroVo().getClientDriven());
                    assertEquals(recorded.getHeroVo().getSkillContainerVo().getSkillListCount(),
                            local.getHeroVo().getSkillContainerVo().getSkillListCount());
                    for (FightSkillUpdateResp skill : local.getHeroVo()
                            .getSkillContainerVo().getSkillListList()) {
                        assertEquals(UNIT, skill.getId());
                    }
                    // 独立检查整个 protobuf 字节流，连未知字段里的遗留 ID 也能发现。
                    assertAbsent(data, recorded.getBaseInfoVo().getId());
                    assertAbsent(data, recorded.getHeroVo().getPlayerId());
                }
            }
        }
    }

    @Test
    public void bossKeepsBothSidesSkillsDamageAndTimelineWhileMovingBaoBu() throws Exception {
        WutanCapturedLog.Capture capture = WutanCapturedLog.load(10300105);
        BattleLogVO source = BattleLogVO.parseFrom(capture.data);
        BattleLogVO result = BattleLogVO.parseFrom(WutanBattleLogIdentity.rebind(
                WutanCapturedLog.relocated(capture, capture.originX + 12f,
                        capture.originY + 2f, capture.originZ + 40f), PLAYER));
        long recordedUnit = hero(source).getBaseInfoVo().getId();
        int heroAttacks = 0;
        int bossAttacks = 0;
        int baoBuCount = 0;
        assertEquals(source.getEntryListCount(), result.getEntryListCount());
        for (int e = 0; e < source.getEntryListCount(); e++) {
            BattleLogEntryVO before = source.getEntryList(e);
            BattleLogEntryVO after = result.getEntryList(e);
            assertEquals(before.getTime(), after.getTime());
            assertEquals(before.getItemListCount(), after.getItemListCount());
            for (int i = 0; i < before.getItemListCount(); i++) {
                BattleLogItemVO original = before.getItemList(i);
                BattleLogItemVO mapped = after.getItemList(i);
                assertEquals(original.getPacketId(), mapped.getPacketId());
                if (original.getPacketId() == 50762) {
                    UseSkillResp oldSkill = UseSkillResp.parseFrom(original.getData());
                    UseSkillResp newSkill = UseSkillResp.parseFrom(mapped.getData());
                    assertEquals(oldSkill.getSkillId(), newSkill.getSkillId());
                    assertEquals(oldSkill.getCurX() + 12f, newSkill.getCurX(), 0.001f);
                    assertEquals(oldSkill.getTargetZ() + 40f, newSkill.getTargetZ(), 0.001f);
                    assertEquals(oldSkill.getAttackId() == recordedUnit ? UNIT : oldSkill.getAttackId(),
                            newSkill.getAttackId());
                    assertEquals(oldSkill.getTargetId() == recordedUnit ? UNIT : oldSkill.getTargetId(),
                            newSkill.getTargetId());
                    if (newSkill.getAttackId() == UNIT) {
                        heroAttacks++;
                    } else {
                        bossAttacks++;
                    }
                } else if (original.getPacketId() == 50763) {
                    SkillActionResp oldActions = SkillActionResp.parseFrom(original.getData());
                    SkillActionResp newActions = SkillActionResp.parseFrom(mapped.getData());
                    assertEquals(oldActions.getSkillId(), newActions.getSkillId());
                    assertEquals(oldActions.getActionListCount(), newActions.getActionListCount());
                    for (int a = 0; a < oldActions.getActionListCount(); a++) {
                        assertEquals(oldActions.getActionList(a).getDamageVo().getDamage(),
                                newActions.getActionList(a).getDamageVo().getDamage(), 0d);
                        assertEquals(oldActions.getActionList(a).getAttributeActionVo().getAttrListList(),
                                newActions.getActionList(a).getAttributeActionVo().getAttrListList());
                    }
                } else if (original.getPacketId() == 50844) {
                    BaoBuStartResp oldBaoBu = BaoBuStartResp.parseFrom(original.getData());
                    BaoBuStartResp newBaoBu = BaoBuStartResp.parseFrom(mapped.getData());
                    assertEquals(oldBaoBu.getX() + 12f, newBaoBu.getX(), 0.001f);
                    assertEquals(oldBaoBu.getY() + 2f, newBaoBu.getY(), 0.001f);
                    assertEquals(oldBaoBu.getZ() + 40f, newBaoBu.getZ(), 0.001f);
                    baoBuCount++;
                }
            }
        }
        assertTrue(heroAttacks > 0);
        assertTrue(bossAttacks > 0);
        assertEquals(1, baoBuCount);
    }

    @Test
    public void laterChapterHitActionsReuseLocalHeroAndMoveWithTheBattle() throws Exception {
        int hits = 0;
        for (int chapter : new int[] {10300501, 10300502, 10300503, 10300601,
                10300602, 10300603, 10300701, 10300702, 10300703}) {
            WutanCapturedLog.Capture capture = WutanCapturedLog.load(chapter);
            BattleLogVO source = BattleLogVO.parseFrom(capture.data);
            long oldUnit = hero(source).getBaseInfoVo().getId();
            BattleLogVO result = BattleLogVO.parseFrom(WutanBattleLogIdentity.rebind(
                    WutanCapturedLog.relocated(capture, capture.originX + 12f,
                            capture.originY + 2f, capture.originZ + 40f), PLAYER));
            for (int e = 0; e < source.getEntryListCount(); e++) {
                for (int i = 0; i < source.getEntryList(e).getItemListCount(); i++) {
                    BattleLogItemVO packet = source.getEntryList(e).getItemList(i);
                    if (packet.getPacketId() != 50763) continue;
                    SkillActionResp before = SkillActionResp.parseFrom(packet.getData());
                    SkillActionResp after = SkillActionResp.parseFrom(result.getEntryList(e).getItemList(i).getData());
                    for (int a = 0; a < before.getActionListCount(); a++) {
                        if (!before.getActionList(a).hasHitActionVo()) continue;
                        com.doupo.protocol.HitActionVO old = before.getActionList(a).getHitActionVo();
                        com.doupo.protocol.HitActionVO mapped = after.getActionList(a).getHitActionVo();
                        assertEquals(old.getAttackId() == oldUnit ? UNIT : old.getAttackId(), mapped.getAttackId());
                        assertEquals(old.getTargetId() == oldUnit ? UNIT : old.getTargetId(), mapped.getTargetId());
                        assertEquals(old.getX() + 12f, mapped.getX(), 0.001f);
                        assertEquals(old.getY() + 2f, mapped.getY(), 0.001f);
                        assertEquals(old.getZ() + 40f, mapped.getZ(), 0.001f);
                        assertEquals(old.getTx() + 12f, mapped.getTx(), 0.001f);
                        assertEquals(old.getTy() + 2f, mapped.getTy(), 0.001f);
                        assertEquals(old.getTz() + 40f, mapped.getTz(), 0.001f);
                        assertEquals(old.toBuilder().setAttackId(mapped.getAttackId()).setTargetId(mapped.getTargetId())
                                .setX(mapped.getX()).setY(mapped.getY()).setZ(mapped.getZ())
                                .setTx(mapped.getTx()).setTy(mapped.getTy()).setTz(mapped.getTz())
                                .build(), mapped);
                        hits++;
                    }
                }
            }
        }
        assertTrue("capture must exercise hit actions", hits > 0);
    }

    @Test
    public void differentPlayersAndRetriesDoNotMutateCachedCapture() throws Exception {
        WutanCapturedLog.Capture capture = WutanCapturedLog.load(10300105);
        ByteString original = capture.data;
        ByteString first = WutanBattleLogIdentity.rebind(original, PLAYER);
        ByteString second = WutanBattleLogIdentity.rebind(original, PLAYER + 1L);
        assertEquals(PLAYER + 1L, hero(BattleLogVO.parseFrom(second)).getHeroVo().getPlayerId());
        assertEquals((PLAYER + 1L) * 1000L + 1L,
                hero(BattleLogVO.parseFrom(second)).getBaseInfoVo().getId());
        assertEquals(first, WutanBattleLogIdentity.rebind(original, PLAYER));
        assertEquals(original, WutanCapturedLog.load(10300105).data);
        assertFalse(original.equals(first));
    }

    @Test
    public void onlyIdentityFieldsAreRewrittenAndUnknownFieldsArePreserved() throws Exception {
        BattleLogVO source = BattleLogVO.parseFrom(WutanCapturedLog.load(10300105).data);
        long oldId = hero(source).getBaseInfoVo().getId();
        FightSkillUpdateResp skill = FightSkillUpdateResp.newBuilder().setId(oldId)
                .setSkillId(oldId).setCd(oldId).setNextChargeTime(oldId)
                .setUnknownFields(com.google.protobuf.UnknownFieldSet.newBuilder()
                        .addField(100, com.google.protobuf.UnknownFieldSet.Field.newBuilder()
                                .addVarint(12345L).build()).build()).build();
        BattleLogVO custom = source.toBuilder().addEntryList(BattleLogEntryVO.newBuilder()
                .setTime(oldId).addItemList(BattleLogItemVO.newBuilder().setPacketId(50766)
                        .setData(skill.toByteString()))).build();
        BattleLogVO mapped = BattleLogVO.parseFrom(WutanBattleLogIdentity.rebind(custom.toByteString(), PLAYER));
        BattleLogEntryVO last = mapped.getEntryList(mapped.getEntryListCount() - 1);
        FightSkillUpdateResp actual = FightSkillUpdateResp.parseFrom(last.getItemList(0).getData());
        assertEquals(UNIT, actual.getId());
        assertEquals(oldId, actual.getSkillId());
        assertEquals(oldId, actual.getCd());
        assertEquals(oldId, actual.getNextChargeTime());
        assertEquals(oldId, last.getTime());
        assertEquals(skill.getUnknownFields(), actual.getUnknownFields());
    }

    @Test(expected = IllegalArgumentException.class)
    public void malformedLogMustNotFallBackToForeignIdentity() {
        WutanBattleLogIdentity.rebind(ByteString.copyFrom(new byte[] {10, 127}), PLAYER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void unknownPacketMustNotBeSilentlyPassedThrough() throws Exception {
        BattleLogVO source = BattleLogVO.parseFrom(WutanCapturedLog.load(10300105).data);
        WutanBattleLogIdentity.rebind(source.toBuilder().addEntryList(BattleLogEntryVO.newBuilder()
                .addItemList(BattleLogItemVO.newBuilder().setPacketId(99999)))
                .build().toByteString(), PLAYER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void anotherRecordedHeroMustNotBeMergedIntoLocalHero() throws Exception {
        BattleLogVO source = BattleLogVO.parseFrom(WutanCapturedLog.load(10300105).data);
        SceneUnitVo first = hero(source);
        SceneUnitVo other = first.toBuilder().setBaseInfoVo(first.getBaseInfoVo().toBuilder()
                .setId(first.getBaseInfoVo().getId() + 1L)).build();
        BattleLogUpdateVisibleResp visible = BattleLogUpdateVisibleResp.newBuilder()
                .setSceneUpdateVisibleResp(com.doupo.protocol.SceneUpdateVisibleResp.newBuilder()
                        .addVisibleList(other)).build();
        WutanBattleLogIdentity.rebind(source.toBuilder().addEntryList(BattleLogEntryVO.newBuilder()
                .addItemList(BattleLogItemVO.newBuilder().setPacketId(50804)
                        .setData(visible.toByteString()))).build().toByteString(), PLAYER);
    }

    private static SceneUnitVo hero(BattleLogVO log) throws Exception {
        List<SceneUnitVo> heroes = new ArrayList<SceneUnitVo>();
        for (BattleLogEntryVO entry : log.getEntryListList()) {
            for (BattleLogItemVO item : entry.getItemListList()) {
                if (item.getPacketId() == 50804) {
                    for (SceneUnitVo unit : BattleLogUpdateVisibleResp.parseFrom(item.getData())
                            .getSceneUpdateVisibleResp().getVisibleListList()) {
                        if (unit.hasHeroVo()) {
                            heroes.add(unit);
                        }
                    }
                }
            }
        }
        assertEquals("single hero per capture", 1, heroes.size());
        return heroes.get(0);
    }

    private static void assertAbsent(ByteString data, long id) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        CodedOutputStream coded = CodedOutputStream.newInstance(bytes);
        coded.writeUInt64NoTag(id);
        coded.flush();
        byte[] needle = bytes.toByteArray();
        byte[] haystack = data.toByteArray();
        for (int i = 0; i <= haystack.length - needle.length; i++) {
            boolean equal = true;
            for (int j = 0; j < needle.length; j++) {
                if (haystack[i + j] != needle[j]) {
                    equal = false;
                    break;
                }
            }
            assertFalse("recorded identity remains: " + id + " at byte " + i, equal);
        }
    }
}
