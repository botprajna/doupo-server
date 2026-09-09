package com.doupo.server.foundation.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import org.gaming.ruler.netty.websocket.BinaryFrameDecoder;
import org.gaming.ruler.netty.websocket.BinaryFrameEncoder;

public class DoupoWebSocketChannelInitializer
        extends ChannelInitializer<Channel> {

    private final int idleSeconds;
    private final ChannelHandler[] handlers;

    public DoupoWebSocketChannelInitializer(
            int idleSeconds,
            ChannelHandler... handlers) {
        this.idleSeconds = idleSeconds;
        this.handlers = handlers;
    }

    @Override
    protected void initChannel(Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new IdleStateHandler(idleSeconds, 0, 0));
        pipeline.addLast(new WebSocketServerProtocolHandler(
                "/", null, true, 65536, false, true));
        pipeline.addLast(new BinaryFrameDecoder());
        pipeline.addLast(new BinaryFrameEncoder());
        pipeline.addLast(handlers);
    }
}