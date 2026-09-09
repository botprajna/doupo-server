package com.doupo.server.module.system;

import com.doupo.protocol.PlayerCreateReq;
import com.doupo.server.foundation.GameServerClock;
import com.doupo.server.module.scene.SceneHandler;
import com.doupo.protocol.PlayerInitBeginResp;
import com.doupo.protocol.PlayerInitEndResp;
import com.doupo.protocol.PlayerGuideSaveResp;
import com.google.protobuf.ByteString;
import com.doupo.protocol.HeroUnlockIndexesResp;
import com.doupo.protocol.HeroUnlockResp;
import com.doupo.protocol.HeroSkillInfoResp;
import com.doupo.protocol.HeroSkillSchemaVo;
import com.doupo.protocol.HeroSkillSlotVo;
import com.doupo.protocol.HeroSkillVo;
import com.doupo.protocol.MainMapChapterInfoResp;
import com.doupo.protocol.ModuleNewOpenResp;
import com.doupo.protocol.ModuleOpenInfoResp;
import com.doupo.protocol.PlayerSkillInfoResp;
import java.util.Arrays;
import java.util.List;
import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PlayerCreateHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerCreateHandler.class);
    private final SceneHandler sceneHandler;
    private final GameServerClock serverClock;

    public PlayerCreateHandler(SceneHandler sceneHandler) {
        this(sceneHandler, GameServerClock.dayOne());
    }

    @Autowired
    public PlayerCreateHandler(
            SceneHandler sceneHandler,
            GameServerClock serverClock) {
        this.sceneHandler = sceneHandler;
        this.serverClock = serverClock != null
                ? serverClock
                : GameServerClock.dayOne();
    }
    private static final List<Integer> INITIAL_MODULE_OPENS =
            Arrays.asList(
                    112, 4304, 4401, 102, 103,
                    4007, 1608, 4202, 4411, 4415);
    private static final List<Integer> ALL_SERVER_MODULES =
            Arrays.asList(
                    5120, 4101, 10, 42011, 16411, 3101, 3102, 3103, 2100, 2101, 2102, 1100,
                    1101, 5201, 5202, 101, 102, 103, 104, 4201, 106, 4202, 4203, 4204,
                    4205, 109, 112, 108, 110, 115, 111, 113, 2200, 2201, 2202, 2211,
                    2212, 1201, 4301, 4302, 4303, 4304, 4305, 4306, 4307, 4308, 4309, 105,
                    3301, 3302, 3303, 3304, 3305, 3306, 3307, 3308, 3309, 3310, 3311, 3312,
                    3313, 3314, 3318, 3321, 3322, 3323, 3324, 2301, 3326, 3325, 2302, 3329,
                    3327, 3331, 3332, 2303, 3328, 1301, 4401, 4402, 4403, 4404, 4405, 4406,
                    4407, 4408, 4409, 4411, 4412, 4413, 4414, 4415, 3400, 3401, 3410, 3411,
                    3412, 3413, 3421, 2400, 3430, 3440, 1401, 4501, 4502, 4503, 4504, 4505,
                    4506, 4507, 4508, 4509, 4511, 4512, 4513, 3501, 2501, 1501, 1502, 1503,
                    23011, 1510, 1511, 1512, 1513, 1525, 1526, 4601, 4602, 4603, 4604, 4605,
                    4606, 3601, 3602, 2600, 2610, 2620, 1600, 1601, 1602, 1603, 1604, 1605,
                    1606, 1607, 1608, 1609, 1610, 1611, 1612, 1613, 2640, 1617, 1618, 1620,
                    1621, 1622, 1623, 1624, 1625, 2650, 4701, 1630, 1634, 1635, 1636, 1637,
                    1638, 1639, 1640, 1641, 1642, 1643, 1644, 1645, 1646, 1647, 1648, 1650,
                    3701, 3702, 3703, 3704, 3705, 3706, 3707, 3708, 1660, 2700, 1677, 1700,
                    1701, 1702, 1703, 1704, 1705, 1706, 1707, 1708, 1710, 1711, 1712, 1713,
                    1720, 1721, 1722, 1723, 107, 4801, 4802, 4805, 4806, 4807, 4808, 4810,
                    4811, 4812, 4820, 4821, 4822, 4823, 1750, 4830, 2800, 4850, 4851, 4852,
                    4853, 4855, 4860, 4870, 4871, 4872, 1801, 1802, 4880, 4881, 4901, 2900,
                    7001, 7002, 7003, 7004, 7005, 7006, 7007, 7008, 7009, 7010, 2910, 2920,
                    1901, 1902, 6001, 6002, 6003, 2930, 1910, 1911, 1912, 2940, 1920, 2950,
                    2951, 5000, 5001, 2952, 5003, 5002, 2960, 5010, 5021, 5022, 4001, 4002,
                    4003, 4004, 4005, 4007, 4008, 5101, 5102, 5103, 5104, 5105, 5106, 5107,
                    5108, 5109, 5110, 5111, 5112);

    @PlayerCmd
    public void createPlayer(
            IPlayerContext context,
            PlayerCreateReq request) {

        sceneHandler.resetTutorialPlayer(context.getId());
        long now = System.currentTimeMillis();
        int messageId = context.getCurrMsgId();

        LOGGER.info(
                "PlayerCreate received: name={}, playerId={}",
                request.getName(),
                context.getId());

        context.write(
                50360,
                ByteString.EMPTY,
                messageId);

        PlayerInitBeginResp initBegin = PlayerInitBeginResp.newBuilder()
                .setPlayerId(context.getId())
                .setName(request.getName())
                .setServerOpen(true)
                .setServerOpenTime(serverClock.openTimeMillis())
                .setCreateTime(now)
                .setFightLogicVersion(0)
                .setServerVersion("2023122701_1")
                .setBattleVersion("")
                .build();
                
        context.write(
                50352,
                initBegin,
                messageId);

        context.write(
                50851,
                ModuleOpenInfoResp.newBuilder()
                        .addOpens(107)
                        .addAllAllServerDefineIds(ALL_SERVER_MODULES)
                        .addOfflineNewOpenIds(107)
                        .build(),
                0);

        // 官服抓包 idx 15：空 PackType_item，先于任何 PackUpdate。
        sceneHandler.writeInitialPackInfo(context);

        context.write(
                50453,
                HeroUnlockResp.newBuilder()
                        .setUnlockHeroIndex(0)
                        .build(),
                0);

        context.write(
                50451,
                HeroUnlockIndexesResp.newBuilder()
                        .addUnlockHeroIndexes(0)
                        .build(),
                0);

        context.write(
                75061,
                ByteString.copyFrom(new byte[] {
                        0x0a, 0x06, 0x08, 0x00,
                        0x10, 0x01, 0x18, 0x01
                }),
                0);

        // 官服抓包 idx 23：空 AnimationStateInfo，斗之气面板靠这份登录数据建模型。
        context.write(
                78651,
                com.doupo.protocol.AnimationStateInfoResp.getDefaultInstance(),
                0);

        // 官服抓包 idx 50：先建立空的抽奖集合模型。
        context.write(77353, ByteString.EMPTY, 0);

        context.write(
                50852,
                ModuleNewOpenResp.newBuilder()
                        .addAllOpens(INITIAL_MODULE_OPENS)
                        .build(),
                0);

        context.write(
                75026,
                PlayerSkillInfoResp.newBuilder()
                        .setAutoStar(false)
                        .setMaxSchemaId(1)
                        .build(),
                0);

        long nowSeconds = now / 1000;
        context.write(
                75001,
                HeroSkillInfoResp.newBuilder()
                        .addHeroVoList(HeroSkillVo.newBuilder()
                                .setHeroIndex(0)
                                .addSlots(HeroSkillSlotVo.newBuilder()
                                        .setId(2001))
                                .addSlots(HeroSkillSlotVo.newBuilder()
                                        .setId(1001))
                                .setCurSchemaId(1)
                                .addSchemas(HeroSkillSchemaVo.newBuilder()
                                        .setId(1)
                                        .setActiveTime(nowSeconds)
                                        .setActive(true))
                                .addSchemas(HeroSkillSchemaVo.newBuilder()
                                        .setId(2)
                                        .setActiveTime(nowSeconds + 2)
                                        .setActive(true))
                                .addSchemas(HeroSkillSchemaVo.newBuilder()
                                        .setId(3)
                                        .setActive(false)))
                        .build(),
                0);

        // 官服抓包 idx 85：NEW_FIGHT_SKILL(12)，显示阶段 1。
        context.write(
                77354,
                ByteString.copyFrom(new byte[] {
                        0x08, 0x0c,
                        (byte) 0x92, 0x01, 0x02, 0x08, 0x01
                }),
                0);

        if (!sceneHandler.initializeChapter9Test(context)) {
            context.write(
                    61951,
                    MainMapChapterInfoResp.newBuilder()
                            .setMainMapChapterId(10100101)
                            .setHasReward(true)
                            .setLoseBackId(10100101)
                            .build(),
                    0);

            context.write(
                    50367,
                    PlayerGuideSaveResp.getDefaultInstance(),
                    0);
        }

        context.write(
                50353,
                PlayerInitEndResp.getDefaultInstance(),
                messageId);
    }
}
