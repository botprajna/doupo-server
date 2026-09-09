package com.doupo.server.module.system;

import com.doupo.protocol.HeartbeatRequest;
import com.doupo.protocol.HeartbeatResponse;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class HeartbeatHandler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(HeartbeatHandler.class);

    @PlayerCmd(needLogin = false)
    public HeartbeatResponse heartbeat(
            IPlayerContext context,
            HeartbeatRequest request) {

        LOGGER.info(
                "Heartbeat handled by PlayerCmd: requestId={}",
                context.getCurrMsgId());

        return HeartbeatResponse.newBuilder()
                .setServerTime(System.currentTimeMillis())
                .build();
    }
}