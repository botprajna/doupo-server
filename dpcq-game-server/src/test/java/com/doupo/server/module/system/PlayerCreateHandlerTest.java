package com.doupo.server.module.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.doupo.protocol.PlayerCreateReq;
import com.doupo.server.module.scene.SceneHandler;
import com.doupo.protocol.PlayerGuideSaveResp;
import com.doupo.protocol.HeroSkillInfoResp;
import com.doupo.protocol.ModuleNewOpenResp;
import com.doupo.protocol.ModuleOpenInfoResp;
import com.doupo.protocol.PackInfoResp;
import com.doupo.protocol.PlayerSkillInfoResp;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import org.gaming.fakecmd.side.game.IPlayerContext;
import org.junit.Test;

public class PlayerCreateHandlerTest {

    @Test
    public void playerInitializationPreparesHeroSkillModel() {
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        new PlayerCreateHandler(new SceneHandler()).createPlayer(
                context,
                PlayerCreateReq.newBuilder()
                        .setName("本地玩家")
                        .build());

        int moduleOpenIndex = context.protocolIds.indexOf(50852);
        int playerSkillIndex = context.protocolIds.indexOf(75026);
        int heroSkillIndex = context.protocolIds.indexOf(75001);
        assertTrue(moduleOpenIndex >= 0);
        assertTrue(playerSkillIndex > moduleOpenIndex);
        assertTrue(heroSkillIndex > playerSkillIndex);
        assertTrue(heroSkillIndex < context.protocolIds.indexOf(50353));

        PlayerSkillInfoResp playerSkill =
                (PlayerSkillInfoResp) context.messages.get(playerSkillIndex);
        assertEquals(1, playerSkill.getMaxSchemaId());

        HeroSkillInfoResp heroSkill =
                (HeroSkillInfoResp) context.messages.get(heroSkillIndex);
        assertEquals(1, heroSkill.getHeroVoListCount());
        assertEquals(0, heroSkill.getHeroVoList(0).getHeroIndex());
        assertEquals(Arrays.asList(2001, 1001),
                Arrays.asList(
                        heroSkill.getHeroVoList(0).getSlots(0).getId(),
                        heroSkill.getHeroVoList(0).getSlots(1).getId()));
        assertEquals(3, heroSkill.getHeroVoList(0).getSchemasCount());
        assertEquals(1, heroSkill.getHeroVoList(0).getCurSchemaId());
    }

    @Test
    public void playerInitializationOpensCapturedHeroSkillModules() {
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        new PlayerCreateHandler(new SceneHandler()).createPlayer(
                context,
                PlayerCreateReq.newBuilder()
                        .setName("本地玩家")
                        .build());

        int openInfoIndex = context.protocolIds.indexOf(50851);
        int newOpenIndex = context.protocolIds.indexOf(50852);
        assertTrue(openInfoIndex > context.protocolIds.indexOf(50352));
        assertTrue(newOpenIndex > openInfoIndex);
        assertTrue(newOpenIndex < context.protocolIds.indexOf(50353));
        assertEquals(openInfoIndex,
                context.protocolIds.lastIndexOf(50851));
        assertEquals(newOpenIndex,
                context.protocolIds.lastIndexOf(50852));

        ModuleOpenInfoResp openInfo =
                (ModuleOpenInfoResp) context.messages.get(openInfoIndex);
        assertTrue(openInfo.getAllServerDefineIdsList().contains(4401));

        ModuleNewOpenResp newOpen =
                (ModuleNewOpenResp) context.messages.get(newOpenIndex);
        assertEquals(Arrays.asList(
                        112, 4304, 4401, 102, 103,
                        4007, 1608, 4202, 4411, 4415),
                newOpen.getOpensList());
    }

    /** 官服抓包 idx 15：登录必须先给空物品背包，否则 BagView 没有 PackInfo。 */
    @Test
    public void playerInitializationSendsEmptyItemPackInfo() {
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        new PlayerCreateHandler(new SceneHandler()).createPlayer(
                context,
                PlayerCreateReq.newBuilder()
                        .setName("本地玩家")
                        .build());

        int packInfoIndex = context.protocolIds.indexOf(50401);
        assertTrue(packInfoIndex > context.protocolIds.indexOf(50851));
        assertTrue(packInfoIndex < context.protocolIds.indexOf(50852));
        PackInfoResp packInfo =
                (PackInfoResp) context.messages.get(packInfoIndex);
        assertEquals(1, packInfo.getPacksCount());
        assertEquals(1, packInfo.getPacks(0).getPackType());
        assertEquals(0, packInfo.getPacks(0).getUpdateItemsCount());
    }

    @Test
    public void playerInitializationIncludesEmptyGuideStateBeforeEnd() {
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        new PlayerCreateHandler(new SceneHandler()).createPlayer(
                context,
                PlayerCreateReq.newBuilder()
                        .setName("本地玩家")
                        .build());

        int guideIndex = context.protocolIds.indexOf(50367);
        assertTrue(guideIndex >= 0);
        assertTrue(context.messages.get(guideIndex)
                instanceof PlayerGuideSaveResp);
        assertEquals(0,
                ((PlayerGuideSaveResp) context.messages.get(guideIndex))
                        .getGuideGroupAndIdPairListCount());
        assertTrue(guideIndex < context.protocolIds.indexOf(50353));
    }

    @Test
    public void playerInitializationIncludesCapturedHeroLevelState() {
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        new PlayerCreateHandler(new SceneHandler()).createPlayer(
                context,
                PlayerCreateReq.newBuilder()
                        .setName("本地玩家")
                        .build());

        int heroLevelIndex = context.protocolIds.indexOf(75061);
        assertTrue(heroLevelIndex >= 0);
        assertTrue(heroLevelIndex < context.protocolIds.indexOf(50353));
        assertEquals(ByteString.copyFrom(new byte[] {
                        0x0a, 0x06, 0x08, 0x00,
                        0x10, 0x01, 0x18, 0x01
                }),
                context.rawMessages.get(heroLevelIndex));

        assertEquals(-1, context.protocolIds.indexOf(75153));
        assertEquals(heroLevelIndex + 1, context.protocolIds.indexOf(78651));
    }

    /** 官服抓包 idx 50、85：纳戒解锁前必须先初始化新斗技抽奖模型。 */
    @Test
    public void playerInitializationPreparesCapturedNewFightSkillLottery() {
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        new PlayerCreateHandler(new SceneHandler()).createPlayer(
                context,
                PlayerCreateReq.newBuilder()
                        .setName("本地玩家")
                        .build());

        int allLotteryIndex = context.protocolIds.indexOf(77353);
        int lotteryInfoIndex = context.protocolIds.indexOf(77354);
        assertTrue(allLotteryIndex >= 0);
        assertTrue(allLotteryIndex < context.protocolIds.indexOf(61951));
        assertTrue(lotteryInfoIndex > context.protocolIds.indexOf(75001));
        assertTrue(lotteryInfoIndex < context.protocolIds.indexOf(50353));
        assertEquals(ByteString.EMPTY,
                context.rawMessages.get(allLotteryIndex));
        assertEquals(ByteString.copyFrom(new byte[] {
                        0x08, 0x0c,
                        (byte) 0x92, 0x01, 0x02, 0x08, 0x01
                }),
                context.rawMessages.get(lotteryInfoIndex));
    }

    private static final class RecordingPlayerContext
            implements IPlayerContext {

        private final long id;
        private final List<Integer> protocolIds = new ArrayList<>();
        private final List<GeneratedMessageV3> messages =
                new ArrayList<>();
        private final List<ByteString> rawMessages =
                new ArrayList<>();

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
            protocolIds.add(protocolId);
            messages.add(message);
            rawMessages.add(null);
        }

        @Override
        public void write(
                int protocolId,
                ByteString message,
                int requestId) {
            protocolIds.add(protocolId);
            messages.add(null);
            rawMessages.add(message);
        }

        @Override
        public boolean isLogin() {
            return true;
        }
    }
}
