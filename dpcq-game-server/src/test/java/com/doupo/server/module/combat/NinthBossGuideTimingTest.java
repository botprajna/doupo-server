package com.doupo.server.module.combat;

import com.doupo.protocol.*;
import com.doupo.server.module.scene.NinthBossGuide;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class NinthBossGuideTimingTest {
    @Test
    public void teachingMustNotRestoreBeforeCachedLethalHitPlays() {
        Context context = new Context(100000000941L);
        CombatSession session = session(context.id, 100);
        try {
            // 与客户端一致：50763等待攻击动画命中，50756/50801立即执行。
            ClientHp client = new ClientHp(100);
            for (MonsterAttack attack : session.tick(1000)) {
                CombatProtocolWriter.writeMonsterAttack(attack, (id, message) -> client.receive(message));
            }
            NinthBossGuide.tryStart(context, session, 1000);
            context.messages.forEach(client::receive);
            assertFalse("致命攻击刚发出不能立即回血", context.has(AttributeActionVO.class));
            assertFalse(context.has(PlayerFightGuildCallResp.class));
            assertFalse(session.isNinthBossGuideStarted());
            assertTrue(session.isPaused());

            client.hit();
            assertEquals(1, client.hp, 0.001);
            assertEquals(1, client.oneHpDrops);

            // 等待期间客户端发解除暂停也不能产生第二次Boss攻击。
            session.setPaused(false);
            assertTrue(session.tick(3000).isEmpty());
            long ready = 1000 + (long) Math.ceil(3467d / 1.333f);
            NinthBossGuide.tryStart(context, session, ready - 1);
            assertTrue(context.messages.isEmpty());
            NinthBossGuide.tryStart(context, session, ready);
            context.messages.forEach(client::receive);
            client.hit();
            assertEquals("回血后不再被旧扣血覆盖", 100, client.hp, 0.001);
            assertEquals("整个教学只发生一次降到1血", 1, client.oneHpDrops);
            assertTrue(session.isNinthBossGuideStarted());
            assertTrue(session.isPaused());
            context.messages.clear();
            NinthBossGuide.tryStart(context, session, ready + 1000);
            assertTrue(context.messages.isEmpty());
        } finally { CombatSessionRegistry.clear(context.id); }
    }

    @Test
    public void realTickPathWaitsThenRestoresAndTeachingCanResume() {
        Context context = new Context(100000000942L);
        CombatSession session = session(context.id, 100);
        try {
            CombatTick tick = new CombatTick(context, session);
            CombatTickProcessor.onTick(null, tick, 1000);
            assertTrue(context.has(SkillActionResp.class));
            assertFalse(context.has(AttributeActionVO.class));
            context.messages.clear();
            CombatTickProcessor.onTick(null, tick, 2000);
            CombatTickProcessor.onTick(null, tick, 3000);
            assertTrue(context.messages.isEmpty());
            CombatTickProcessor.onTick(null, tick, 4000);
            assertTrue(context.has(PlayerFightGuildCallResp.class));
            assertEquals(100, session.getPlayer().getCurrentHp(), 0.001);
            context.messages.clear();
            CombatTickProcessor.onTick(null, tick, 5000);
            assertTrue(context.messages.isEmpty());
            session.setPaused(false);
            CombatTickProcessor.onTick(null, tick, 6000);
            assertTrue(context.has(UseSkillResp.class));
            assertFalse(context.has(PlayerFightGuildCallResp.class));
        } finally { CombatSessionRegistry.clear(context.id); }
    }

    @Test
    public void alternateBossSkillUsesItsOwnRecoveryTiming() {
        Context context = new Context(100000000943L);
        CombatSession session = session(context.id, 5000);
        try {
            session.tick(0);
            assertFalse(session.isPaused());
            assertEquals(50810110101L, session.tick(2000).get(0).getSkillId());
            long ready = 2000 + (long) Math.ceil(1100d / 1.333f);
            NinthBossGuide.tryStart(context, session, ready - 1);
            assertTrue(context.messages.isEmpty());
            NinthBossGuide.tryStart(context, session, ready);
            assertEquals(5000, session.getPlayer().getCurrentHp(), 0.001);
            assertTrue(context.has(PlayerFightGuildCallResp.class));
        } finally { CombatSessionRegistry.clear(context.id); }
    }

    @Test
    public void stoppedOrReplacedFightCannotRunOldPendingTeaching() {
        Context context = new Context(100000000944L);
        CombatSession old = session(context.id, 100);
        try {
            old.tick(1000);
            old.deactivate();
            NinthBossGuide.tryStart(context, old, 10000);
            assertTrue(context.messages.isEmpty());
            old = session(context.id, 100);
            old.tick(1000);
            CombatSession replacement = session(context.id, 200);
            NinthBossGuide.tryStart(context, old, 10000);
            CombatTickProcessor.onTick(null, new CombatTick(context, old), 10000);
            assertTrue(context.messages.isEmpty());
            assertEquals(200, replacement.getPlayer().getCurrentHp(), 0.001);
        } finally { CombatSessionRegistry.clear(context.id); }
    }

    private static CombatSession session(long playerId, double hp) {
        CombatSession session = new CombatSession(10200405,
                new CombatUnit(playerId * 1000 + 1, 100, hp, 0, 0, 0));
        session.addMonster(new CombatUnit(playerId * 1000 + 2, 100, 100000, 0, 0, 0));
        session.setNinthBossPlayerSnapshot(SceneUnitVo.newBuilder()
                .setBaseInfoVo(SceneUnitBaseInfoVo.newBuilder().setId(session.getPlayer().getSceneUnitId()).setUnitType(1))
                .setFightInfoVo(SceneFightUnitInfoVo.newBuilder()
                        .addAttributeList(AttributeVO.newBuilder().setType(103011).setValue(hp))
                        .addAttributeList(AttributeVO.newBuilder().setType(103001).setValue(hp)))
                .build());
        CombatSessionRegistry.bind(playerId, session);
        return session;
    }

    private static class ClientHp {
        double hp;
        int oneHpDrops;
        final List<AttributeActionVO> cached = new ArrayList<>();
        ClientHp(double hp) { this.hp = hp; }
        void receive(GeneratedMessageV3 message) {
            if (message instanceof SkillActionResp) {
                for (ActionVo action : ((SkillActionResp) message).getActionListList()) {
                    if (action.hasAttributeActionVo()) cached.add(action.getAttributeActionVo());
                }
            } else if (message instanceof AttributeActionVO) {
                apply(((AttributeActionVO) message).getAttrListList());
            } else if (message instanceof SceneUpdateVisibleResp) {
                for (SceneUnitVo unit : ((SceneUpdateVisibleResp) message).getVisibleListList()) {
                    apply(unit.getFightInfoVo().getAttributeListList());
                }
            }
        }
        void hit() {
            cached.forEach(value -> apply(value.getAttrListList()));
            cached.clear();
        }
        void apply(List<AttributeVO> attributes) {
            for (AttributeVO attribute : attributes) {
                if (attribute.getType() != 103011) continue;
                if (attribute.getValue() == 1 && hp > 1) oneHpDrops++;
                hp = attribute.getValue();
            }
        }
    }

    private static class Context implements IPlayerContext {
        final long id;
        final List<GeneratedMessageV3> messages = new ArrayList<>();
        Context(long id) { this.id = id; }
        boolean has(Class<?> type) { return messages.stream().anyMatch(type::isInstance); }
        public long getId() { return id; }
        public int getServerZone() { return 1; }
        public int getCurrMsgId() { return 0; }
        public void setCurrMsgId(int id) { }
        public boolean isLogin() { return true; }
        public void write(int protocolId, GeneratedMessageV3 message, int requestId) { messages.add(message); }
        public void write(int protocolId, ByteString message, int requestId) { throw new AssertionError("Unexpected raw packet"); }
    }
}
