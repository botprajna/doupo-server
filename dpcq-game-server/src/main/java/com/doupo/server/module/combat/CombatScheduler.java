package com.doupo.server.module.combat;

import com.doupo.server.foundation.player.PlayerActorPool;

import java.time.Duration;

import org.gaming.fakecmd.side.game.IPlayerContext;
import org.gaming.ruler.akka.AkkaContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;

/**
 * 用 Akka scheduler 将 {@link CombatTick} 定时投递给玩家 Actor。
 *
 * <p>这是战斗推进与 {@code PlayerActor} 串行模型的桥梁：定时器线程只投递消息，
 * 实际的战斗逻辑在 Actor 信箱内执行，因此不会与协议处理并发访问同一份战斗状态。
 */
public final class CombatScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CombatScheduler.class);

    private static final Duration IMMEDIATE = Duration.ZERO;

    /** 抓包 idx 1986-1992：开始追击到技能伤害相差约两个逻辑帧。 */
    private static final Duration PLAYER_CHASE_DAMAGE_DELAY =
            Duration.ofMillis(132L);

    /** 抓包 idx 1992-2001：技能伤害后约一个逻辑帧停止追击。 */
    private static final Duration PLAYER_CHASE_STOP_DELAY =
            Duration.ofMillis(67L);

    private CombatScheduler() {
    }

    /**
     * 调度下一次战斗推进。
     *
     * <p>若该玩家已无进行中的战斗，则不调度，自然终止循环。
     */
    public static void scheduleNext(
            ActorRef actor,
            IPlayerContext context,
            CombatSession session) {
        if (session != null) {
            schedule(actor, context, session, Duration.ofMillis(session.getTickIntervalMillis()));
        }
    }

    /**
     * 开始一轮战斗调度。会先立即投递一发，以便首帧准时推进。
     *
     * <p>玩家所属的 Actor 由 {@link PlayerActorPool#actorFor(long)} 解析，
     * 调用方不需要持有具体的 context 实现类。
     *
     * <p>Actor 池未启动（例如单元测试直接调用 handler）时安全跳过；
     * 生产环境按 Lifecycle 顺序池必然先就绪。
     */
    public static void start(IPlayerContext context) {
        if (!PlayerActorPool.isStarted()) {
            LOGGER.debug("Player actor pool not started, skip combat scheduling: "
                    + "player={}", context.getId());
            return;
        }

        schedule(
                PlayerActorPool.actorFor(context.getId()),
                context,
                CombatSessionRegistry.get(context.getId()),
                IMMEDIATE);
    }

    public static void schedulePlayerChase(
            IPlayerContext context,
            Runnable action) {
        if (!PlayerActorPool.isStarted()) {
            return;
        }

        scheduleMessage(
                PlayerActorPool.actorFor(context.getId()),
                new PlayerCombatAction(action),
                PLAYER_CHASE_DAMAGE_DELAY);
    }

    public static void schedulePlayerChaseStop(
            IPlayerContext context,
            Runnable action) {
        if (!PlayerActorPool.isStarted()) {
            return;
        }

        scheduleMessage(
                PlayerActorPool.actorFor(context.getId()),
                new PlayerCombatAction(action),
                PLAYER_CHASE_STOP_DELAY);
    }

    /**
     * 在玩家 Actor 上延迟执行。单元测试没有 Actor 池时立刻跑，方便断言。
     */
    public static void scheduleOnPlayer(
            IPlayerContext context,
            Duration delay,
            Runnable action) {
        if (action == null) {
            return;
        }
        if (!PlayerActorPool.isStarted()) {
            action.run();
            return;
        }
        scheduleMessage(
                PlayerActorPool.actorFor(context.getId()),
                new PlayerCombatAction(action),
                delay);
    }

    private static void schedule(
            ActorRef actor,
            IPlayerContext context,
            CombatSession session,
            Duration delay) {

        if (actor == null || context == null || session == null) {
            return;
        }

        if (!session.isActive()) {
            LOGGER.debug("Combat not active, skip scheduling: player={}",
                    context.getId());
            return;
        }

        // 只给「当前那一份」会话续排，波次切换后旧会话的调度链就此终止。
        if (CombatSessionRegistry.get(context.getId()) != session) {
            LOGGER.debug("Combat session replaced, skip scheduling: player={}",
                    context.getId());
            return;
        }

        AkkaContext.system()
                .scheduler()
                .scheduleOnce(
                        delay,
                        (Runnable) () -> actor.tell(
                                new CombatTick(context, session),
                                ActorRef.noSender()),
                        AkkaContext.system().dispatcher());
    }

    private static void scheduleMessage(
            ActorRef actor,
            Object message,
            Duration delay) {
        if (actor == null || message == null) {
            return;
        }

        AkkaContext.system()
                .scheduler()
                .scheduleOnce(
                        delay,
                        (Runnable) () -> actor.tell(
                                message,
                                ActorRef.noSender()),
                        AkkaContext.system().dispatcher());
    }
}
