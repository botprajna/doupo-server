package com.doupo.server.foundation.netty;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.doupo.protocol.MessageWrapper;
import com.doupo.server.foundation.player.PlayerConnectionContext;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;

@Sharable
public class GameNettyHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GameNettyHandler.class);

    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger();

    private static final ConcurrentHashMap<Channel, PlayerConnectionContext>
            ONLINE =
            new ConcurrentHashMap<Channel, PlayerConnectionContext>();

    static final AttributeKey<PlayerConnectionContext> CONTEXT_KEY =
            AttributeKey.valueOf("PLAYER_CONTEXT");

    public static PlayerConnectionContext contextOf(Channel channel) {
        if (channel == null) {
            return null;
        }
        return channel.attr(CONTEXT_KEY).get();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        PlayerConnectionContext context =
                new PlayerConnectionContext(ctx.channel());

        ctx.channel().attr(CONTEXT_KEY).set(context);
        ONLINE.put(ctx.channel(), context);

        int online = ONLINE_COUNT.incrementAndGet();
        LOGGER.info(
                "Client connected: channel={}, remote={}, online={}",
                ctx.channel().id(),
                ctx.channel().remoteAddress(),
                online);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ONLINE.remove(ctx.channel());
        ctx.channel().attr(CONTEXT_KEY).set(null);

        int online = ONLINE_COUNT.decrementAndGet();
        LOGGER.info(
                "Client disconnected: channel={}, online={}",
                ctx.channel().id(),
                online);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!(msg instanceof byte[])) {
            LOGGER.debug(
                    "Ignore non-binary frame: channel={}, type={}",
                    ctx.channel().id(),
                    msg == null
                            ? "null"
                            : msg.getClass().getSimpleName());
            return;
        }

        PlayerConnectionContext context =
                ctx.channel().attr(CONTEXT_KEY).get();

        if (context == null) {
            ctx.close();
            return;
        }

        byte[] payload = (byte[]) msg;

        LOGGER.info(
                "WS binary received: channel={}, bytes={}, head={}",
                ctx.channel().id(),
                payload.length,
                toHexPreview(payload, 32));

        try {
            if (GameFrameCodec.isGameFrame(payload)) {
                GameFrameCodec.DecodedFrame frame =
                        GameFrameCodec.decode(
                                payload,
                                context.isLogin());

                LOGGER.info(
                        "Game request decoded: protoId={}, requestId={}, bodyBytes={}",
                        frame.getProtocolId(),
                        frame.getChecksum(),
                        frame.getBody().length);

                context.setGameFrame(true);
                context.tell(MessageWrapper.newBuilder()
                        .setProtoId(frame.getProtocolId())
                        .setRequestId(frame.getChecksum())
                        .setData(ByteString.copyFrom(frame.getBody()))
                        .build());
                return;
            }

            MessageWrapper wrapper = MessageWrapper.parseFrom(payload);
            context.tell(wrapper);
        } catch (InvalidProtocolBufferException
                | IllegalArgumentException exception) {
            LOGGER.warn(
                    "Unrecognized binary frame: channel={}, bytes={}, head={}",
                    ctx.channel().id(),
                    payload.length,
                    toHexPreview(payload, 64));
        }
    }

    private static String toHexPreview(byte[] data, int max) {
        int length = Math.min(data.length, max);
        StringBuilder builder = new StringBuilder(length * 2);

        for (int i = 0; i < length; i++) {
            builder.append(String.format("%02x", data[i] & 0xff));
        }

        if (data.length > max) {
            builder.append("...");
        }

        return builder.toString();
    }

    @Override
    public void userEventTriggered(
            ChannelHandlerContext ctx,
            Object event) {

        if (event instanceof IdleStateEvent
                && ((IdleStateEvent) event).state()
                == IdleState.READER_IDLE) {
            LOGGER.info(
                    "Closing idle connection: channel={}",
                    ctx.channel().id());
            ctx.close();
            return;
        }

        String name =
                event == null
                        ? "null"
                        : event.getClass().getSimpleName();

        if (name.contains("HandshakeComplete")) {
            LOGGER.info(
                    "WebSocket handshake complete: channel={}, remote={}",
                    ctx.channel().id(),
                    ctx.channel().remoteAddress());
        }

        ctx.fireUserEventTriggered(event);
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx,
            Throwable cause) {
        LOGGER.error(
                "Connection error: channel={}",
                ctx.channel().id(),
                cause);
        ctx.close();
    }
}
