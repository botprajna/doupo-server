package com.doupo.server.foundation.player;

import java.util.concurrent.atomic.AtomicInteger;

import org.gaming.ruler.akka.AkkaContext;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.lifecycle.LifecycleInfo;
import org.gaming.ruler.lifecycle.Ordinal;
import org.gaming.ruler.lifecycle.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import akka.actor.ActorRef;

@Component
public class PlayerActorPool implements Lifecycle {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(PlayerActorPool.class);

    private static final AtomicInteger NEXT_INDEX = new AtomicInteger();
    private static ActorRef[] actors;

    @Override
    public LifecycleInfo getInfo() {
        return LifecycleInfo.valueOf(
                getClass().getSimpleName(), Priority.LOW, Ordinal.MIN);
    }

    @Override
    public void start() {
        int actorCount = AkkaContext.PARALLELISM_MAX * 2;
        actors = new ActorRef[actorCount];

        for (int i = 0; i < actorCount; i++) {
            actors[i] = AkkaContext.createActor(PlayerActor.class);
        }

        LOGGER.info("Player actor pool started: actors={}", actorCount);
    }

    @Override
    public void stop() {
        if (actors != null) {
            for (ActorRef actor : actors) {
                AkkaContext.system().stop(actor);
            }
        }

        LOGGER.info("Player actor pool stopped");
    }

    public static ActorRef nextActor() {
        if (actors == null || actors.length == 0) {
            throw new IllegalStateException("Player actor pool is not ready");
        }

        int index = Math.floorMod(
                NEXT_INDEX.getAndIncrement(), actors.length);
        return actors[index];
    }

    public static ActorRef actorFor(long playerId) {
        return actors[(int) Math.floorMod(
                playerId, (long) actors.length)];
    }

    /**
     * 池是否已经启动。
     *
     * <p>供只在真正的服务端运行时才需要 Actor 基础设施的可选功能使用（例如
     * {@code CombatScheduler}），使这类功能在脱离 Akka 系统的单元测试里可以
     * 安全跳过，而不必削弱 {@link #actorFor(long)} / {@link #nextActor()}
     * 对启动顺序错误的强校验。
     */
    public static boolean isStarted() {
        return actors != null && actors.length > 0;
    }
}
