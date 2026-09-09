package com.doupo.server.module.social;

import com.doupo.protocol.FriendApplyListReq;
import com.doupo.protocol.FriendApplyListResp;
import com.doupo.protocol.FriendBlackListReq;
import com.doupo.protocol.FriendBlackListResp;
import com.doupo.protocol.FriendListReq;
import com.doupo.protocol.FriendListResp;
import com.doupo.protocol.RedDotUpdateReq;
import com.doupo.server.module.scene.SceneHandler;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.springframework.stereotype.Controller;

@Controller
public class FriendInitHandler {
    private final SceneHandler sceneHandler;

    public FriendInitHandler(SceneHandler sceneHandler) {
        this.sceneHandler = sceneHandler;
    }

    @PlayerCmd
    public FriendListResp friendList(
            IPlayerContext context,
            FriendListReq request) {

        return FriendListResp.getDefaultInstance();
    }

    @PlayerCmd
    public FriendApplyListResp friendApplyList(
            IPlayerContext context,
            FriendApplyListReq request) {

        return FriendApplyListResp.getDefaultInstance();
    }

    @PlayerCmd
    public FriendBlackListResp friendBlackList(
            IPlayerContext context,
            FriendBlackListReq request) {

        return FriendBlackListResp.getDefaultInstance();
    }

    @PlayerCmd
    public void updateRedDot(
            IPlayerContext context,
            RedDotUpdateReq request) {
        sceneHandler.writeSecondHeroChapterTasks(context);
    }
}
