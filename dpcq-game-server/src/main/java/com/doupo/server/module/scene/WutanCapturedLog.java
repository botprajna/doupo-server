package com.doupo.server.module.scene;

import com.doupo.protocol.ActionVo;
import com.doupo.protocol.BaoBuStartResp;
import com.doupo.protocol.BattleLogEntryVO;
import com.doupo.protocol.BattleLogItemVO;
import com.doupo.protocol.BattleLogUpdateVisibleResp;
import com.doupo.protocol.BattleLogVO;
import com.doupo.protocol.ChaseStartResp;
import com.doupo.protocol.ChaseStopResp;
import com.doupo.protocol.DieVO;
import com.doupo.protocol.FightMoveVo;
import com.doupo.protocol.FightStatisticsResp;
import com.doupo.protocol.MoveResp;
import com.doupo.protocol.PullStartResp;
import com.doupo.protocol.PullEndResp;
import com.doupo.protocol.SceneUnitBaseInfoVo;
import com.doupo.protocol.SceneUnitVo;
import com.doupo.protocol.SceneUpdateVisibleResp;
import com.doupo.protocol.SkillActionResp;
import com.doupo.protocol.SkillMoveResp;
import com.doupo.protocol.StatisticsResp;
import com.doupo.protocol.UseSkillResp;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 乌坦城官服 61971 战报：按当前 61962 战斗点平移抓包坐标后回放。
 */
final class WutanCapturedLog {

    private static final int PACKET_VISIBLE = 50804;
    private static final int PACKET_MOVE = 50761;
    private static final int PACKET_USE_SKILL = 50762;
    private static final int PACKET_SKILL_ACTION = 50763;
    private static final int PACKET_PULL_START = 50828;
    private static final int PACKET_SKILL_MOVE = 50831;
    private static final int PACKET_CHASE_START = 50832;
    private static final int PACKET_CHASE_STOP = 50834;
    private static final float ORIGIN_EPS = 1.0e-4f;

    private static final ConcurrentMap<Integer, Capture> CACHE =
            new ConcurrentHashMap<Integer, Capture>();

    private WutanCapturedLog() {
    }

    static Capture load(int chapterId) {
        Capture cached = CACHE.get(chapterId);
        if (cached != null) {
            return cached.isEmpty() ? null : cached;
        }
        Capture loaded = read(chapterId);
        Capture previous = CACHE.putIfAbsent(chapterId, loaded);
        Capture use = previous != null ? previous : loaded;
        return use.isEmpty() ? null : use;
    }

    static ByteString relocated(Capture capture, float fightX, float fightY, float fightZ) {
        if (capture == null || capture.data.isEmpty()) {
            return ByteString.EMPTY;
        }
        if (!hasFightPoint(fightX, fightY, fightZ)) {
            return capture.data;
        }
        float dx = fightX - capture.originX;
        float dy = fightY - capture.originY;
        float dz = fightZ - capture.originZ;
        if (nearZero(dx) && nearZero(dy) && nearZero(dz)) {
            return capture.data;
        }
        try {
            BattleLogVO log = BattleLogVO.parseFrom(capture.data);
            BattleLogVO.Builder out = log.toBuilder();
            for (int e = 0; e < out.getEntryListCount(); e++) {
                BattleLogEntryVO.Builder entry = out.getEntryListBuilder(e);
                for (int i = 0; i < entry.getItemListCount(); i++) {
                    BattleLogItemVO.Builder item = entry.getItemListBuilder(i);
                    item.setData(relocatePacket(
                            item.getPacketId(), item.getData(), dx, dy, dz));
                }
            }
            return out.build().toByteString();
        } catch (InvalidProtocolBufferException ignored) {
            return capture.data;
        }
    }

    static FightStatisticsResp.Builder statistics(Capture capture, long battleLogId) {
        FightStatisticsResp.Builder stats = FightStatisticsResp.newBuilder()
                .setBattleLogId(battleLogId)
                .setWin(capture == null || capture.win);
        if (capture == null) {
            return stats;
        }
        for (StatisticsResp winner : capture.winners) {
            stats.addWinner(winner);
        }
        for (StatisticsResp loser : capture.losers) {
            stats.addLoser(loser);
        }
        return stats;
    }

    private static boolean hasFightPoint(float x, float y, float z) {
        return !nearZero(x) || !nearZero(y) || !nearZero(z);
    }

    private static boolean nearZero(float value) {
        return Math.abs(value) < ORIGIN_EPS;
    }

    private static Capture read(int chapterId) {
        ByteString data = readBytes("wutan-battlelog/" + chapterId + ".bin");
        if (data.isEmpty()) {
            return Capture.empty();
        }
        Properties meta = readMeta("wutan-battlelog/" + chapterId + ".meta");
        return new Capture(
                data,
                parseFloat(meta, "originX"),
                parseFloat(meta, "originY"),
                parseFloat(meta, "originZ"),
                parseFloat(meta, "speed", 1.1f),
                parseLong(meta, "startTime", 66L),
                Boolean.parseBoolean(meta.getProperty("win", "true")),
                parseSide(meta, "winner"),
                parseSide(meta, "loser"));
    }

    private static ByteString readBytes(String path) {
        InputStream in = WutanCapturedLog.class.getClassLoader()
                .getResourceAsStream(path);
        if (in == null) {
            return ByteString.EMPTY;
        }
        try {
            try {
                return ByteString.readFrom(in);
            } finally {
                in.close();
            }
        } catch (IOException ignored) {
            return ByteString.EMPTY;
        }
    }

    private static Properties readMeta(String path) {
        Properties props = new Properties();
        InputStream in = WutanCapturedLog.class.getClassLoader()
                .getResourceAsStream(path);
        if (in == null) {
            return props;
        }
        try {
            try {
                props.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            } finally {
                in.close();
            }
        } catch (IOException ignored) {
            return props;
        }
        return props;
    }

    private static List<StatisticsResp> parseSide(Properties meta, String prefix) {
        int count = parseInt(meta, prefix + ".count", 0);
        if (count <= 0) {
            return Collections.emptyList();
        }
        List<StatisticsResp> rows = new ArrayList<StatisticsResp>(count);
        for (int i = 0; i < count; i++) {
            String csv = meta.getProperty(prefix + "." + i, "");
            String[] parts = csv.split(",");
            if (parts.length < 7) {
                continue;
            }
            rows.add(StatisticsResp.newBuilder()
                    .setEssenceFireId(parseIntPart(parts[0]))
                    .setHeroJobId(parseIntPart(parts[1]))
                    .setMonsterId(parseLongPart(parts[2]))
                    .setLevel(parseIntPart(parts[3]))
                    .setDamage(parseLongPart(parts[4]))
                    .setTakeDamage(parseLongPart(parts[5]))
                    .setCure(parseLongPart(parts[6]))
                    .build());
        }
        return rows;
    }

    private static float parseFloat(Properties meta, String key) {
        return parseFloat(meta, key, 0f);
    }

    private static float parseFloat(Properties meta, String key, float fallback) {
        String raw = meta.getProperty(key);
        if (raw == null || raw.isEmpty()) {
            return fallback;
        }
        try {
            return Float.parseFloat(raw.trim());
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    private static long parseLong(Properties meta, String key, long fallback) {
        String raw = meta.getProperty(key);
        if (raw == null || raw.isEmpty()) {
            return fallback;
        }
        try {
            return Long.parseLong(raw.trim());
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    private static int parseInt(Properties meta, String key, int fallback) {
        String raw = meta.getProperty(key);
        if (raw == null || raw.isEmpty()) {
            return fallback;
        }
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    private static int parseIntPart(String raw) {
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private static long parseLongPart(String raw) {
        try {
            return Long.parseLong(raw.trim());
        } catch (NumberFormatException ignored) {
            return 0L;
        }
    }

    private static ByteString relocatePacket(
            int packetId,
            ByteString data,
            float dx,
            float dy,
            float dz) {
        try {
            switch (packetId) {
                case PACKET_VISIBLE:
                    return shiftVisible(data, dx, dy, dz);
                case PACKET_MOVE:
                    return shiftMove(data, dx, dy, dz);
                case PACKET_USE_SKILL:
                    return shiftUseSkill(data, dx, dy, dz);
                case PACKET_SKILL_ACTION:
                    return shiftSkillAction(data, dx, dy, dz);
                case PACKET_PULL_START:
                    return shiftPull(data, dx, dy, dz);
                case 50829:
                    PullEndResp.Builder end = PullEndResp.parseFrom(data).toBuilder();
                    return end.setX(end.getX() + dx).setY(end.getY() + dy)
                            .setZ(end.getZ() + dz).build().toByteString();
                case 50844:
                    BaoBuStartResp.Builder baoBu = BaoBuStartResp.parseFrom(data).toBuilder();
                    return baoBu.setX(baoBu.getX() + dx).setY(baoBu.getY() + dy)
                            .setZ(baoBu.getZ() + dz).build().toByteString();
                case PACKET_SKILL_MOVE:
                    return shiftSkillMove(data, dx, dy, dz);
                case PACKET_CHASE_START:
                    return shiftChaseStart(data, dx, dy, dz);
                case PACKET_CHASE_STOP:
                    return shiftChaseStop(data, dx, dy, dz);
                default:
                    return data;
            }
        } catch (InvalidProtocolBufferException ignored) {
            return data;
        }
    }

    private static ByteString shiftVisible(
            ByteString data, float dx, float dy, float dz)
            throws InvalidProtocolBufferException {
        BattleLogUpdateVisibleResp.Builder vis =
                BattleLogUpdateVisibleResp.parseFrom(data).toBuilder();
        if (!vis.hasSceneUpdateVisibleResp()) {
            return data;
        }
        SceneUpdateVisibleResp.Builder scene = vis.getSceneUpdateVisibleRespBuilder();
        for (int i = 0; i < scene.getVisibleListCount(); i++) {
            SceneUnitVo.Builder unit = scene.getVisibleListBuilder(i);
            if (!unit.hasBaseInfoVo()) {
                continue;
            }
            SceneUnitBaseInfoVo.Builder base = unit.getBaseInfoVoBuilder();
            base.setX(base.getX() + dx);
            base.setY(base.getY() + dy);
            base.setZ(base.getZ() + dz);
        }
        return vis.build().toByteString();
    }

    private static ByteString shiftMove(
            ByteString data, float dx, float dy, float dz)
            throws InvalidProtocolBufferException {
        MoveResp.Builder move = MoveResp.parseFrom(data).toBuilder();
        if (!move.hasMove()) {
            return data;
        }
        FightMoveVo.Builder vo = move.getMoveBuilder();
        vo.setX(vo.getX() + dx);
        vo.setY(vo.getY() + dy);
        vo.setZ(vo.getZ() + dz);
        vo.setCurX(vo.getCurX() + dx);
        vo.setCurY(vo.getCurY() + dy);
        vo.setCurZ(vo.getCurZ() + dz);
        return move.build().toByteString();
    }

    private static ByteString shiftUseSkill(
            ByteString data, float dx, float dy, float dz)
            throws InvalidProtocolBufferException {
        UseSkillResp.Builder skill = UseSkillResp.parseFrom(data).toBuilder();
        skill.setCurX(skill.getCurX() + dx);
        skill.setCurY(skill.getCurY() + dy);
        skill.setCurZ(skill.getCurZ() + dz);
        skill.setTargetX(skill.getTargetX() + dx);
        skill.setTargetY(skill.getTargetY() + dy);
        skill.setTargetZ(skill.getTargetZ() + dz);
        return skill.build().toByteString();
    }

    private static ByteString shiftSkillAction(
            ByteString data, float dx, float dy, float dz)
            throws InvalidProtocolBufferException {
        SkillActionResp.Builder action = SkillActionResp.parseFrom(data).toBuilder();
        for (int i = 0; i < action.getActionListCount(); i++) {
            ActionVo.Builder vo = action.getActionListBuilder(i);
            if (vo.hasHitActionVo()) {
                com.doupo.protocol.HitActionVO.Builder hit = vo.getHitActionVoBuilder();
                hit.setX(hit.getX() + dx).setY(hit.getY() + dy).setZ(hit.getZ() + dz);
                hit.setTx(hit.getTx() + dx).setTy(hit.getTy() + dy).setTz(hit.getTz() + dz);
            }
            if (!vo.hasDieVo()) {
                continue;
            }
            DieVO.Builder die = vo.getDieVoBuilder();
            die.setX(die.getX() + dx);
            die.setY(die.getY() + dy);
            die.setZ(die.getZ() + dz);
        }
        return action.build().toByteString();
    }

    private static ByteString shiftPull(
            ByteString data, float dx, float dy, float dz)
            throws InvalidProtocolBufferException {
        PullStartResp.Builder pull = PullStartResp.parseFrom(data).toBuilder();
        pull.setX(pull.getX() + dx);
        pull.setY(pull.getY() + dy);
        pull.setZ(pull.getZ() + dz);
        pull.setTx(pull.getTx() + dx);
        pull.setTy(pull.getTy() + dy);
        pull.setTz(pull.getTz() + dz);
        return pull.build().toByteString();
    }

    private static ByteString shiftSkillMove(
            ByteString data, float dx, float dy, float dz)
            throws InvalidProtocolBufferException {
        SkillMoveResp.Builder move = SkillMoveResp.parseFrom(data).toBuilder();
        move.setX(move.getX() + dx);
        move.setY(move.getY() + dy);
        move.setZ(move.getZ() + dz);
        move.setTx(move.getTx() + dx);
        move.setTy(move.getTy() + dy);
        move.setTz(move.getTz() + dz);
        return move.build().toByteString();
    }

    private static ByteString shiftChaseStart(
            ByteString data, float dx, float dy, float dz)
            throws InvalidProtocolBufferException {
        ChaseStartResp.Builder chase = ChaseStartResp.parseFrom(data).toBuilder();
        chase.setX(chase.getX() + dx);
        chase.setY(chase.getY() + dy);
        chase.setZ(chase.getZ() + dz);
        chase.setTx(chase.getTx() + dx);
        chase.setTy(chase.getTy() + dy);
        chase.setTz(chase.getTz() + dz);
        return chase.build().toByteString();
    }

    private static ByteString shiftChaseStop(
            ByteString data, float dx, float dy, float dz)
            throws InvalidProtocolBufferException {
        ChaseStopResp.Builder chase = ChaseStopResp.parseFrom(data).toBuilder();
        chase.setX(chase.getX() + dx);
        chase.setY(chase.getY() + dy);
        chase.setZ(chase.getZ() + dz);
        return chase.build().toByteString();
    }

    static final class Capture {
        final ByteString data;
        final float originX;
        final float originY;
        final float originZ;
        final float speed;
        final long startTime;
        final boolean win;
        final List<StatisticsResp> winners;
        final List<StatisticsResp> losers;

        private Capture(
                ByteString data,
                float originX,
                float originY,
                float originZ,
                float speed,
                long startTime,
                boolean win,
                List<StatisticsResp> winners,
                List<StatisticsResp> losers) {
            this.data = data;
            this.originX = originX;
            this.originY = originY;
            this.originZ = originZ;
            this.speed = speed;
            this.startTime = startTime;
            this.win = win;
            this.winners = winners;
            this.losers = losers;
        }

        private static Capture empty() {
            return new Capture(
                    ByteString.EMPTY,
                    0f, 0f, 0f,
                    1.1f,
                    66L,
                    true,
                    Collections.<StatisticsResp>emptyList(),
                    Collections.<StatisticsResp>emptyList());
        }

        private boolean isEmpty() {
            return data.isEmpty();
        }
    }
}
