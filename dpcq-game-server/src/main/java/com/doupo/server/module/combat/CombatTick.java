package com.doupo.server.module.combat;

import org.gaming.fakecmd.side.game.IPlayerContext;

import akka.actor.ActorRef;

/**
 * 发送给玩家 Actor 的定时战斗推进消息。
 *
 * <p>通过 Akka scheduler 定时投递到玩家自己的 Actor，使战斗推进与协议处理
 * 在同一个 Actor 内串行执行——这正是本服 {@code PlayerActor} 的并发模型。
 *
 * <p>只持有 {@link IPlayerContext} 接口而非具体实现类，这样战斗模块不与
 * {@code PlayerConnectionContext} 耦合，{@code SceneHandler} 在单元测试里替换任意
 * {@link IPlayerContext} 实现时仍然可用。
 */
public final class CombatTick {

    private final IPlayerContext context;
    private final CombatSession session;

    public CombatTick(IPlayerContext context, CombatSession session) {
        this.context = context;
        this.session = session;
    }

    public IPlayerContext getContext() {
        return context;
    }

    /**
     * 这次 tick 要推进的战斗会话。
     *
     * <p>波次切换时会绑定一份新的会话，而旧会话的 tick 可能还在 Actor 信箱里。
     * tick 自带会话，{@link CombatTickProcessor} 才能认出「这条 tick 属于上一波」
     * 并就地终止，不去驱动新一波，也不重复续排调度。
     */
    public CombatSession getSession() {
        return session;
    }

    /** 让所属 Actor 重新调度下一次自身。 */
    public void reschedule(ActorRef self) {
        CombatScheduler.scheduleNext(self, context, session);
    }
}
