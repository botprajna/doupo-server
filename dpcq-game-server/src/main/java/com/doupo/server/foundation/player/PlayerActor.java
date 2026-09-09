package com.doupo.server.foundation.player;

import com.doupo.server.module.combat.CombatTick;
import com.doupo.server.module.combat.CombatTickProcessor;
import com.doupo.server.module.combat.PlayerCombatAction;

import org.gaming.fakecmd.side.game.PlayerCmdRegister;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class PlayerActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        PlayerProtocolMessage.class,
                        PlayerCmdRegister.INS::handle)
                .match(
                        CombatTick.class,
                        tick -> CombatTickProcessor.onTick(self(), tick))
                .match(
                        PlayerCombatAction.class,
                        PlayerCombatAction::run)
                .build();
    }
}
