package com.doupo.server.module.system;

import com.doupo.protocol.IntegerAndStringPairEntry;
import com.doupo.protocol.ServerGroupInfoReq;
import com.doupo.protocol.ServerGroupInfoResp;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.springframework.stereotype.Controller;

@Controller
public class ServerGroupInfoHandler {

    @PlayerCmd
    public ServerGroupInfoResp serverGroupInfo(
            IPlayerContext context,
            ServerGroupInfoReq request) {

        return ServerGroupInfoResp.newBuilder()
                .addServerInfoList(
                        IntegerAndStringPairEntry.newBuilder()
                                .setKey(context.getServerZone())
                                .setValue("斗帝本地测试服")
                                .build())
                .build();
    }
}
