package com.doupo.server.module.scene;

import com.doupo.protocol.AttributeActionVO;
import com.doupo.protocol.BaoBuStartResp;
import com.doupo.protocol.BattleLogEntryVO;
import com.doupo.protocol.BattleLogItemVO;
import com.doupo.protocol.BattleLogUpdateVisibleResp;
import com.doupo.protocol.BattleLogForgetVisibleResp;
import com.doupo.protocol.BattleLogVO;
import com.doupo.protocol.ChaseStartResp;
import com.doupo.protocol.ChaseStopResp;
import com.doupo.protocol.FightSkillUpdateResp;
import com.doupo.protocol.InFightStatisticResp;
import com.doupo.protocol.LockTargetResp;
import com.doupo.protocol.MoveResp;
import com.doupo.protocol.PullEndResp;
import com.doupo.protocol.PullStartResp;
import com.doupo.protocol.SceneUnitVo;
import com.doupo.protocol.SkillActionResp;
import com.doupo.protocol.SkillMoveResp;
import com.doupo.protocol.UseSkillResp;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** 将单主角官服战报绑定到本地场景身份，不改技能、时间轴、伤害和胜负。 */
final class WutanBattleLogIdentity {

    // 只转换已由客户端 ProtoMember 确认的对象引用，不能全局替换 int64：
    // 技能 ID、Buff ID、时间戳和数值也使用 int64。
    private static final Set<String> UNIT_REFERENCES = new HashSet<String>(Arrays.asList(
            "SceneUnitBaseInfoVo:1", "CrossHeroShortInfo:3", "SceneMonsterVo:5",
            "SceneHeroVo:17", "SceneMissileVo:3", "SceneMissileVo:4",
            "FightSkillUpdateResp:1", "FightMoveVo:1", "LockTargetResp:1",
            "LockTargetResp:2", "UseSkillResp:1", "UseSkillResp:3",
            "DamageVO:9", "DamageVO:10", "DieVO:6", "DieVO:7",
            "AttributeActionVO:2", "AttributeActionVO:3",
            "BuffAddActionVO:2", "BuffAddActionVO:3", "BuffVO:8",
            "BuffRemoveActionVO:2", "BuffRemoveActionVO:3",
            "SceneForgetVisibleResp:1", "SimpleStatisticsResp:1",
            "BaoBuStartResp:1", "BaoBuStartResp:5", "BaoBuStartResp:6",
            "PullStartResp:1", "PullStartResp:9", "PullEndResp:1",
            "SkillMoveResp:1", "ChaseStartResp:1", "ChaseStopResp:1",
            "ChangeFightValueActionVO:2", "ChangeFightValueActionVO:3",
            "HitActionVO:13", "HitActionVO:14"));

    private WutanBattleLogIdentity() {
    }

    static SceneUnitVo bindPreviewHero(SceneUnitVo hero, long playerId) {
        return (SceneUnitVo) remap(hero, hero.getHeroVo().getCrossHeroShortInfo().getSceneUnitUid(),
                playerId * 1000 + 1, hero.getHeroVo().getPlayerId(), playerId);
    }

    static ByteString rebind(ByteString data, long playerId) {
        try {
            BattleLogVO log = BattleLogVO.parseFrom(data);
            SceneUnitVo hero = findHero(log);
            long oldUnitId = hero.getBaseInfoVo().getId();
            long oldPlayerId = hero.getHeroVo().getPlayerId();
            long localUnitId = Math.addExact(Math.multiplyExact(playerId, 1000L), 1L);
            BattleLogVO.Builder out = log.toBuilder();
            for (BattleLogEntryVO.Builder entry : out.getEntryListBuilderList()) {
                for (BattleLogItemVO.Builder item : entry.getItemListBuilderList()) {
                    Message packet = parsePacket(item.getPacketId(), item.getData());
                    item.setData(remap(packet, oldUnitId, localUnitId, oldPlayerId, playerId)
                            .toByteString());
                }
            }
            return out.build().toByteString();
        } catch (InvalidProtocolBufferException e) {
            // 不允许解析失败后静默发送含官服身份的原始战报。
            throw new IllegalArgumentException("Invalid captured Wutan battle log", e);
        }
    }

    private static SceneUnitVo findHero(BattleLogVO log)
            throws InvalidProtocolBufferException {
        SceneUnitVo hero = null;
        for (BattleLogEntryVO entry : log.getEntryListList()) {
            for (BattleLogItemVO item : entry.getItemListList()) {
                if (item.getPacketId() != 50804) {
                    continue;
                }
                for (SceneUnitVo unit : BattleLogUpdateVisibleResp.parseFrom(item.getData())
                        .getSceneUpdateVisibleResp().getVisibleListList()) {
                    if (!unit.hasHeroVo()) {
                        continue;
                    }
                    if (unit.getBaseInfoVo().getId() == 0L
                            || unit.getHeroVo().getPlayerId() == 0L
                            || unit.getFightInfoVo().getCampId() != 2L
                            || unit.getHeroVo().getCrossHeroShortInfo().getHeroIdx() != 0
                            || (hero != null && (hero.getBaseInfoVo().getId()
                                    != unit.getBaseInfoVo().getId()
                                    || hero.getHeroVo().getPlayerId()
                                    != unit.getHeroVo().getPlayerId()))) {
                        throw new IllegalArgumentException("Expected one friendly hero in Wutan log");
                    }
                    hero = unit;
                }
            }
        }
        if (hero == null) {
            throw new IllegalArgumentException("Missing friendly hero in Wutan log");
        }
        return hero;
    }

    private static Message remap(Message packet, long oldUnitId, long localUnitId,
            long oldPlayerId, long playerId) {
        Message.Builder out = packet.toBuilder();
        for (Map.Entry<FieldDescriptor, Object> entry : packet.getAllFields().entrySet()) {
            FieldDescriptor field = entry.getKey();
            String key = field.getContainingType().getName() + ":" + field.getNumber();
            boolean owner = "SceneHeroVo:3".equals(key) || "SceneCommonSkillVO:1".equals(key);
            int count = field.isRepeated() ? packet.getRepeatedFieldCount(field) : 1;
            for (int i = 0; i < count; i++) {
                Object value = field.isRepeated() ? packet.getRepeatedField(field, i) : entry.getValue();
                Object replacement = value;
                if (field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
                    replacement = remap((Message) value, oldUnitId, localUnitId, oldPlayerId, playerId);
                } else if (owner && ((Long) value).longValue() == oldPlayerId) {
                    replacement = playerId;
                } else if (UNIT_REFERENCES.contains(key) && ((Long) value).longValue() == oldUnitId) {
                    replacement = localUnitId;
                }
                if (field.isRepeated()) {
                    out.setRepeatedField(field, i, replacement);
                } else {
                    out.setField(field, replacement);
                }
            }
        }
        return out.build();
    }

    private static Message parsePacket(int packetId, ByteString data)
            throws InvalidProtocolBufferException {
        switch (packetId) {
            case 50760: return LockTargetResp.parseFrom(data);
            case 50761: return MoveResp.parseFrom(data);
            case 50762: return UseSkillResp.parseFrom(data);
            case 50763: return SkillActionResp.parseFrom(data);
            case 50766: return FightSkillUpdateResp.parseFrom(data);
            case 50801: return AttributeActionVO.parseFrom(data);
            case 50804: return BattleLogUpdateVisibleResp.parseFrom(data);
            case 50805: return BattleLogForgetVisibleResp.parseFrom(data);
            case 50828: return PullStartResp.parseFrom(data);
            case 50829: return PullEndResp.parseFrom(data);
            case 50831: return SkillMoveResp.parseFrom(data);
            case 50832: return ChaseStartResp.parseFrom(data);
            case 50834: return ChaseStopResp.parseFrom(data);
            case 50835: return com.doupo.protocol.ChangeFightValueActionVO.parseFrom(data);
            case 50843: return com.doupo.protocol.CommSkillActionResp.parseFrom(data);
            case 50844: return BaoBuStartResp.parseFrom(data);
            case 77073: return InFightStatisticResp.parseFrom(data);
            default: throw new IllegalArgumentException("Unsupported Wutan battle packet: " + packetId);
        }
    }
}
