package com.doupo.server.foundation.netty;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public final class GameFrameCodec {

    private static final int LENGTH_BYTES = 4;
    private static final int PROTOCOL_BYTES = 4;
    private static final int CHECKSUM_BYTES = 4;

    private GameFrameCodec() {
    }

    public static boolean isGameFrame(byte[] payload) {
        if (payload == null
                || payload.length < LENGTH_BYTES + PROTOCOL_BYTES) {
            return false;
        }

        int declaredLength = ByteBuffer.wrap(payload)
                .order(ByteOrder.BIG_ENDIAN)
                .getInt();

        return declaredLength == payload.length - LENGTH_BYTES;
    }

    public static DecodedFrame decode(
            byte[] payload,
            boolean authenticated) {
        if (!isGameFrame(payload)) {
            throw new IllegalArgumentException("Invalid game frame length");
        }

        int headerLength = LENGTH_BYTES + PROTOCOL_BYTES;

        if (authenticated) {
            headerLength += CHECKSUM_BYTES;
        }

        if (payload.length < headerLength) {
            throw new IllegalArgumentException("Incomplete game frame header");
        }

        ByteBuffer buffer = ByteBuffer.wrap(payload)
                .order(ByteOrder.BIG_ENDIAN);

        buffer.getInt();
        int protocolId = buffer.getInt();
        int checksum = authenticated ? buffer.getInt() : 0;
        byte[] body = Arrays.copyOfRange(
                payload,
                headerLength,
                payload.length);

        return new DecodedFrame(protocolId, checksum, body);
    }

    public static byte[] encode(
            int protocolId,
            byte[] body,
            boolean includeChecksum,
            int checksum) {
        byte[] safeBody = body == null ? new byte[0] : body;
        int frameLength = PROTOCOL_BYTES
                + (includeChecksum ? CHECKSUM_BYTES : 0)
                + safeBody.length;

        ByteBuffer buffer = ByteBuffer
                .allocate(LENGTH_BYTES + frameLength)
                .order(ByteOrder.BIG_ENDIAN);

        buffer.putInt(frameLength);
        buffer.putInt(protocolId);

        if (includeChecksum) {
            buffer.putInt(checksum);
        }

        buffer.put(safeBody);
        return buffer.array();
    }

    public static final class DecodedFrame {

        private final int protocolId;
        private final int checksum;
        private final byte[] body;

        private DecodedFrame(
                int protocolId,
                int checksum,
                byte[] body) {
            this.protocolId = protocolId;
            this.checksum = checksum;
            this.body = body;
        }

        public int getProtocolId() {
            return protocolId;
        }

        public int getChecksum() {
            return checksum;
        }

        public byte[] getBody() {
            return body;
        }
    }
}
