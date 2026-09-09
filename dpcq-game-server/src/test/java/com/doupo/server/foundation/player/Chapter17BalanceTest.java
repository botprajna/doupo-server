package com.doupo.server.foundation.player;

import com.doupo.protocol.*;
import com.doupo.server.module.scene.SceneHandler;
import akka.actor.ActorRef;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import java.lang.reflect.Field;
import static org.junit.Assert.*;

public class Chapter17BalanceTest {
    @Test
    public void towerRewardsAddToExistingPurseAndItemStack() throws Exception {
        Field actors=PlayerActorPool.class.getDeclaredField("actors");
        actors.setAccessible(true);Object previous=actors.get(null);
        EmbeddedChannel channel=new EmbeddedChannel();
        try {
            actors.set(null,new ActorRef[]{null});
            PlayerConnectionContext c=new PlayerConnectionContext(channel);
            c.bindPlayer(100000000910L);
            c.write(50652,PurseUpdateResp.newBuilder().addItems(CurrencyItemVo.newBuilder()
                    .setType(1).setValue(900)).build().toByteString(),0);
            c.write(50401,PackInfoResp.newBuilder().addPacks(PackUpdateVo.newBuilder().setPackType(1)
                    .addUpdateItems(UpdateItem.newBuilder().setItemIndex(19).setPackItem(PackItemVo.newBuilder()
                            .setKey(112303).setSize(52).setObjectId(12345)))).build(),0);
            SceneHandler h=new SceneHandler();
            h.rewardGuidanceTask(c,TaskRewardReq.newBuilder().setTaskId(TaskUniqueKey.newBuilder()
                    .setTaskResourceId(200034)).build());
            h.fireTowerChallenge(c,FireTowerChallengeReq.newBuilder().setZ(6.5f).build());
            h.fireTowerAfter(c,FireTowerChallengeAfterReq.getDefaultInstance());
            assertEquals(Long.valueOf(2150),c.currencyBalance(1));
            assertEquals(102,c.itemStack(112303).getPackItem().getSize());
            assertEquals(19,c.itemStack(112303).getItemIndex());
            assertEquals(12345,c.itemStack(112303).getPackItem().getObjectId());
            c.write(50402,PackUpdateResp.newBuilder().addPacks(PackUpdateVo.newBuilder().setPackType(1)
                    .addUpdateItems(UpdateItem.newBuilder().setItemIndex(19))).build(),0);
            assertNull(c.itemStack(112303));
        } finally {
            actors.set(null,previous);
            channel.finishAndReleaseAll();
        }
    }
}
