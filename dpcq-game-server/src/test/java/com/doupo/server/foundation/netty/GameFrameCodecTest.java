package com.doupo.server.foundation.netty;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.junit.Test;

public class GameFrameCodecTest {

    @Test
    public void decodesLoginFrameWithoutChecksum() {
        byte[] body = new byte[] {10, 3, '1', '2', '3'};
        byte[] frame = GameFrameCodec.encode(
                50051,
                body,
                false,
                0);

        assertTrue(GameFrameCodec.isGameFrame(frame));
        assertEquals(frame.length - 4,
                ByteBuffer.wrap(frame)
                        .order(ByteOrder.BIG_ENDIAN)
                        .getInt());

        GameFrameCodec.DecodedFrame decoded =
                GameFrameCodec.decode(frame, false);

        assertEquals(50051, decoded.getProtocolId());
        assertEquals(0, decoded.getChecksum());
        assertArrayEquals(body, decoded.getBody());
    }

    @Test
    public void decodesAuthenticatedFrameWithChecksum() {
        byte[] frame = GameFrameCodec.encode(
                71601,
                new byte[0],
                true,
                123456);

        GameFrameCodec.DecodedFrame decoded =
                GameFrameCodec.decode(frame, true);

        assertEquals(71601, decoded.getProtocolId());
        assertEquals(123456, decoded.getChecksum());
        assertEquals(0, decoded.getBody().length);
    }

    @Test
    public void rejectsWrapperBytesAsGameFrame() {
        assertFalse(GameFrameCodec.isGameFrame(
                new byte[] {8, 1, 16, 1}));
    }
}
