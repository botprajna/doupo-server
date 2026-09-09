package com.doupo.server.module.combat;

import java.util.List;

import com.doupo.protocol.MpResp;
import com.doupo.protocol.MoveResp;
import com.doupo.server.module.scene.NinthBossGuide;

import org.gaming.fakecmd.side.game.IPlayerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;

/**
 * 玩家 Actor 处理 {@link CombatTick} 的入口。
 *
 * <p>在 Actor 信箱内同步执行：推进 {@link CombatSession}、下推怪物普攻协议、
 * 重新调度下一次 tick。因为是在 Actor 内运行，与 {@code PlayerProtocolMessage}
 * 的处理天然串行。
 */
public final class CombatTickProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CombatTickProcessor.class);

    private CombatTickProcessor() {
    }

    /**
     * 处理一次定时战斗推进。
     *
     * @param self  当前 Actor（用于重新调度）
     * @param tick  定时消息
     */
    public static void onTick(ActorRef self, CombatTick tick) {
        onTick(self, tick, System.currentTimeMillis());
    }

    static void onTick(ActorRef self, CombatTick tick, long nowMillis) {
        IPlayerContext context = tick.getContext();
        CombatSession session = CombatSessionRegistry.get(context.getId());

        if (session == null || !session.isActive()) {
            LOGGER.info("Combat no longer active, stop ticking: player={}, "
                            + "sessionNull={}",
                    context.getId(), session == null);
            return;
        }

        // 上一波的 tick 不能推进新一波的会话，也不能替它续排调度。
        if (tick.getSession() != null && tick.getSession() != session) {
            LOGGER.info("Stale combat tick dropped: player={}, tickChapter={}, "
                            + "currentChapter={}",
                    context.getId(),
                    tick.getSession().getChapterId(),
                    session.getChapterId());
            return;
        }

        for (MoveResp move : session.advanceMonsterMovement(nowMillis)) {
            context.write(50761, move, 0);
        }
        List<MonsterAttack> attacks = session.tick(nowMillis);

        if (!attacks.isEmpty()) {
            LOGGER.info("Combat tick: player={}, attacks={}",
                    context.getId(), attacks.size());
        }

        for (MonsterAttack attack : attacks) {
            CombatProtocolWriter.writeMonsterAttack(
                    attack,
                    (protocolId, message) -> context.write(
                            protocolId,
                            message,
                            0));
            // 怒气：怪物攻击命中玩家（挨打）+18，抓包 idx 2008/2025 对齐。
            session.addMp(18);
            context.write(
                    50798,
                    MpResp.newBuilder()
                            .setId(session.getPlayer().getSceneUnitId())
                            .setMp(session.getMp())
                            .build(),
                    0);
        }

        NinthBossGuide.tryStart(context, session, nowMillis);
        tick.reschedule(self);
    }
}
