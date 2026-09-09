package com.doupo.server.module.combat;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 按玩家 ID 索引的战斗会话注册表。
 *
 * <p>与 {@code SceneHandler} 里 {@code guidanceStates} 的模式一致：只依赖
 * {@code IPlayerContext.getId()}，不依赖具体的 context 实现类，这样
 * {@code SceneHandler} 在单元测试里换用任意 {@code IPlayerContext} 实现都能工作。
 *
 * <p>一个玩家同一时刻只有一场进行中的战斗，因此用 playerId 作为唯一 key。
 */
public final class CombatSessionRegistry {

    private static final ConcurrentMap<Long, CombatSession> SESSIONS =
            new ConcurrentHashMap<>();

    private CombatSessionRegistry() {
    }

    public static void bind(long playerId, CombatSession session) {
        if (session == null) {
            SESSIONS.remove(playerId);
        } else {
            SESSIONS.put(playerId, session);
        }
    }

    public static CombatSession get(long playerId) {
        return SESSIONS.get(playerId);
    }

    public static void clear(long playerId) {
        SESSIONS.remove(playerId);
    }
}
