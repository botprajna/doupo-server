package com.doupo.server.module.combat;

/** 在玩家 Actor 信箱内串行执行的延迟战斗动作。 */
public final class PlayerCombatAction {

    private final Runnable action;

    public PlayerCombatAction(Runnable action) {
        if (action == null) {
            throw new IllegalArgumentException("action must not be null");
        }
        this.action = action;
    }

    public void run() {
        action.run();
    }
}
