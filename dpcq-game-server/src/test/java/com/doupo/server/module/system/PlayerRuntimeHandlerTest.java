package com.doupo.server.module.system;

import static org.junit.Assert.assertEquals;

import com.doupo.protocol.HeroJobCreateReq;
import com.doupo.protocol.HeroJobCreateResp;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import java.util.ArrayList;
import java.util.List;
import org.gaming.fakecmd.side.game.IPlayerContext;
import org.junit.Test;

public class PlayerRuntimeHandlerTest {

    @Test
    public void creatingHeroReturnsBeforeAnyShortInfoUpdate() {
        RecordingPlayerContext context =
                new RecordingPlayerContext(100_000_000_001L);

        HeroJobCreateResp response =
                new PlayerRuntimeHandler().createHeroJob(
                context,
                HeroJobCreateReq.newBuilder()
                        .setId(1001)
                        .setHeroIndex(0)
                        .build());

        assertEquals(0, response.getHeroVo().getHeroIndex());
        assertEquals(1001, response.getHeroVo().getJob());
        assertEquals(1, context.protocolIds.size());
        assertEquals(Integer.valueOf(78652), context.protocolIds.get(0));
    }

    private static final class RecordingPlayerContext
            implements IPlayerContext {

        private final long id;
        private final List<Integer> protocolIds = new ArrayList<>();

        private RecordingPlayerContext(long id) {
            this.id = id;
        }

        @Override
        public long getId() {
            return id;
        }

        @Override
        public int getServerZone() {
            return 1;
        }

        @Override
        public int getCurrMsgId() {
            return 50502;
        }

        @Override
        public void setCurrMsgId(int msgId) {
        }

        @Override
        public void write(
                int protocolId,
                GeneratedMessageV3 message,
                int requestId) {
            protocolIds.add(protocolId);
        }

        @Override
        public void write(
                int protocolId,
                ByteString message,
                int requestId) {
            protocolIds.add(protocolId);
        }

        @Override
        public boolean isLogin() {
            return true;
        }
    }
}
