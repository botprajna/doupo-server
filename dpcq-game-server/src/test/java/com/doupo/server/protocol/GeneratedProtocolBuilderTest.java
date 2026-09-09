package com.doupo.server.protocol;

import static org.junit.Assert.assertNotNull;

import com.doupo.protocol.ChangeSceneResp;
import com.doupo.protocol.HeroJobCreateResp;
import com.doupo.protocol.MainMapChapterInfoResp;
import com.doupo.protocol.SceneUpdateVisibleResp;

import org.junit.Test;

public class GeneratedProtocolBuilderTest {

    @Test
    public void generatedBuildersAreCallable() {
        assertNotNull(HeroJobCreateResp.newBuilder().build());
        assertNotNull(ChangeSceneResp.newBuilder().build());
        assertNotNull(MainMapChapterInfoResp.newBuilder().build());
        assertNotNull(SceneUpdateVisibleResp.newBuilder().build());
    }
}
