package com.doupo.server.module.system;

import com.doupo.protocol.AnimationStateHeroInfoVo;
import com.doupo.protocol.AnimationStateHeroUpdateResp;
import com.doupo.protocol.AnimationStateSkillSlotVo;
import com.doupo.protocol.HeartbeatReq;
import com.doupo.protocol.HeartbeatResp;
import com.doupo.protocol.HeroJobCreateReq;
import com.doupo.protocol.HeroJobCreateResp;
import com.doupo.protocol.HeroJobVo;
import com.doupo.protocol.PlayerIpLocationResp;
import com.doupo.protocol.PlayerIpLocationUpdateReq;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.springframework.stereotype.Controller;

@Controller
public class PlayerRuntimeHandler {

    private final com.doupo.server.module.scene.SceneHandler sceneHandler;

    public PlayerRuntimeHandler() {
        this(null);
    }

    @org.springframework.beans.factory.annotation.Autowired
    public PlayerRuntimeHandler(com.doupo.server.module.scene.SceneHandler sceneHandler) {
        this.sceneHandler = sceneHandler;
    }

    @PlayerCmd
    public HeartbeatResp heartbeat(
            IPlayerContext context,
            HeartbeatReq request) {

        if (sceneHandler != null) sceneHandler.refreshAlchemyDay(context);
        return HeartbeatResp.newBuilder()
                .setServerTime(System.currentTimeMillis())
                .build();
    }

    @PlayerCmd
    public PlayerIpLocationResp updateIpLocation(
            IPlayerContext context,
            PlayerIpLocationUpdateReq request) {

        return PlayerIpLocationResp.newBuilder()
                .setIpLocation("本地测试")
                .build();
    }

    @PlayerCmd
    public HeroJobCreateResp createHeroJob(
            IPlayerContext context,
            HeroJobCreateReq request) {

        HeroJobVo heroJob = HeroJobVo.newBuilder()
                .setHeroIndex(request.getHeroIndex())
                .setJob(request.getId())
                .build();

        // 官服抓包 idx 75：选职业后立刻挂上斗之气槽，否则升段面板没有英雄。
        context.write(
                78652,
                AnimationStateHeroUpdateResp.newBuilder()
                        .setHeroVo(AnimationStateHeroInfoVo.newBuilder()
                                .setHeroIndex(request.getHeroIndex())
                                .setProficiencyLevel(0)
                                .addSkillSlots(AnimationStateSkillSlotVo
                                        .newBuilder()
                                        .setSlotId(1)
                                        .setStar(0)))
                        .build(),
                0);

        return HeroJobCreateResp.newBuilder()
                .setHeroVo(heroJob)
                .build();
    }
}
