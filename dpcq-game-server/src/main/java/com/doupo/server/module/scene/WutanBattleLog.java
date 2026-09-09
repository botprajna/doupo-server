package com.doupo.server.module.scene;

import com.doupo.protocol.ActionVo;
import com.doupo.protocol.BattleLogEntryVO;
import com.doupo.protocol.BattleLogItemVO;
import com.doupo.protocol.BattleLogPlayerInfo;
import com.doupo.protocol.BattleLogResp;
import com.doupo.protocol.BattleLogUpdateVisibleResp;
import com.doupo.protocol.BattleLogVO;
import com.doupo.protocol.DieVO;
import com.doupo.protocol.FightStatisticsResp;
import com.doupo.protocol.MainMapStartFightResp;
import com.doupo.protocol.PreBattleInfo;
import com.doupo.protocol.SceneForgetVisibleResp;
import com.doupo.protocol.SceneUnitVo;
import com.doupo.protocol.SceneUpdateVisibleResp;
import com.doupo.protocol.SkillActionResp;
import com.doupo.protocol.StatisticsResp;
import com.google.protobuf.ByteString;
import org.gaming.fakecmd.side.game.IPlayerContext;

import java.util.List;

/**
 * 乌坦城 61971：优先回放抓包双方出手时间轴，没有资源时再生成最短可播日志。
 */
final class WutanBattleLog {

    static BattleLogResp towerLog(IPlayerContext player, int floor, float x, float y, float z) {
        WutanCapturedLog.Capture capture = WutanCapturedLog.load(floor);
        if (capture == null) throw new IllegalArgumentException("No captured tower fight for " + floor);
        long id = player.getId() * 1000 + floor;
        return BattleLogResp.newBuilder().setBattleLogId(id).setType(1003).setIndex(0)
                .setData(WutanBattleLogIdentity.rebind(WutanCapturedLog.relocated(capture, x, y, z), player.getId()))
                .setFightStatisticsResp(WutanCapturedLog.statistics(capture, id))
                .setStartTime(capture.startTime).setSpeed(capture.speed).setFin(true)
                .setPreBattleInfo(PreBattleInfo.newBuilder().setCreateTime(capture.startTime)
                        .addPlayerInfoList(BattleLogPlayerInfo.newBuilder().setPlayerId(player.getId())
                                .setCampId(2).setInBattle(true))).build();
    }

    private WutanBattleLog() {
    }

    static MainMapStartFightResp startFightResp(
            IPlayerContext player,
            ChapterConfig.Chapter chapter,
            List<SceneUpdateVisibleResp> monsters,
            float fightX,
            float fightY,
            float fightZ) {
        long battleLogId = player.getId() * 1000L + chapter.getChapterId();
        WutanCapturedLog.Capture capture =
                WutanCapturedLog.load(chapter.getChapterId());
        ByteString data;
        FightStatisticsResp.Builder stats;
        float speed = 1.1f;
        long startTime = 66L;
        if (capture != null) {
            data = WutanBattleLogIdentity.rebind(
                    WutanCapturedLog.relocated(capture, fightX, fightY, fightZ), player.getId());
            stats = WutanCapturedLog.statistics(capture, battleLogId);
            speed = capture.speed;
            startTime = capture.startTime;
        } else {
            data = generated(monsters);
            stats = dummyStats(battleLogId, monsters);
        }
        if (stats.getWinnerCount() == 0) {
            stats.addWinner(StatisticsResp.newBuilder()
                    .setEssenceFireId(0)
                    .setHeroJobId(1001)
                    .setMonsterId(0)
                    .setLevel(8)
                    .setDamage(1)
                    .setTakeDamage(0)
                    .setCure(0));
        }
        return MainMapStartFightResp.newBuilder()
                .setMainMapChapterId(chapter.getChapterId())
                .setBattleLog(BattleLogResp.newBuilder()
                        .setBattleLogId(battleLogId)
                        .setType(1001)
                        .setIndex(0)
                        .setData(data)
                        .setFightStatisticsResp(stats)
                        .setStartTime(startTime)
                        .setPreBattleInfo(PreBattleInfo.newBuilder()
                                .setCreateTime(startTime)
                                .addPlayerInfoList(BattleLogPlayerInfo.newBuilder()
                                        .setPlayerId(player.getId())
                                        .setCampId(2)
                                        .setInBattle(true)))
                        .setFin(true)
                        .setSpeed(speed))
                .setResult(0)
                .setIndex(0)
                .setEnd(true)
                .build();
    }

    private static FightStatisticsResp.Builder dummyStats(
            long battleLogId,
            List<SceneUpdateVisibleResp> monsters) {
        FightStatisticsResp.Builder stats = FightStatisticsResp.newBuilder()
                .setBattleLogId(battleLogId)
                .setWin(true)
                .addWinner(StatisticsResp.newBuilder()
                        .setEssenceFireId(0)
                        .setHeroJobId(1001)
                        .setMonsterId(0)
                        .setLevel(8)
                        .setDamage(1)
                        .setTakeDamage(0)
                        .setCure(0));
        if (monsters == null) {
            return stats;
        }
        for (SceneUpdateVisibleResp monster : monsters) {
            if (monster.getVisibleListCount() == 0) {
                continue;
            }
            SceneUnitVo unit = monster.getVisibleList(0);
            stats.addLoser(StatisticsResp.newBuilder()
                    .setEssenceFireId(0)
                    .setHeroJobId(0)
                    .setMonsterId(unit.getSceneMonsterVo().getTemplateId())
                    .setLevel(0)
                    .setDamage(0)
                    .setTakeDamage(1)
                    .setCure(0));
        }
        return stats;
    }

    private static ByteString generated(List<SceneUpdateVisibleResp> monsters) {
        BattleLogVO.Builder log = BattleLogVO.newBuilder();
        BattleLogEntryVO.Builder spawn = BattleLogEntryVO.newBuilder()
                .setTime(0);
        if (monsters != null) {
            for (SceneUpdateVisibleResp monster : monsters) {
                spawn.addItemList(BattleLogItemVO.newBuilder()
                        .setPacketId(50804)
                        .setData(BattleLogUpdateVisibleResp.newBuilder()
                                .setSceneUpdateVisibleResp(monster)
                                .setType(1001)
                                .build()
                                .toByteString()));
            }
        }
        if (spawn.getItemListCount() > 0) {
            log.addEntryList(spawn);
        }
        long time = 400;
        if (monsters == null) {
            return log.build().toByteString();
        }
        for (SceneUpdateVisibleResp monster : monsters) {
            if (monster.getVisibleListCount() == 0) {
                continue;
            }
            SceneUnitVo unit = monster.getVisibleList(0);
            long unitId = unit.getBaseInfoVo().getId();
            log.addEntryList(BattleLogEntryVO.newBuilder()
                    .setTime(time)
                    .addItemList(BattleLogItemVO.newBuilder()
                            .setPacketId(50763)
                            .setData(SkillActionResp.newBuilder()
                                    .setSkillId(10110710101L)
                                    .addActionList(ActionVo.newBuilder()
                                            .setActionVoTypeId(6)
                                            .setDieVo(DieVO.newBuilder()
                                                    .setMonsterTemplateId(
                                                            unit.getSceneMonsterVo()
                                                                    .getTemplateId())
                                                    .setX(unit.getBaseInfoVo().getX())
                                                    .setY(unit.getBaseInfoVo().getY())
                                                    .setZ(unit.getBaseInfoVo().getZ())
                                                    .setDir(unit.getBaseInfoVo().getDir())
                                                    .setAttackId(0)
                                                    .setTargetId(unitId)
                                                    .setHitId(0)))
                                    .build()
                                    .toByteString()))
                    .addItemList(BattleLogItemVO.newBuilder()
                            .setPacketId(50757)
                            .setData(SceneForgetVisibleResp.newBuilder()
                                    .addForgetIds(unitId)
                                    .build()
                                    .toByteString())));
            time += 400;
        }
        return log.build().toByteString();
    }
}
