package com.doupo.server.foundation.netty;

import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.lifecycle.LifecycleInfo;
import org.gaming.ruler.lifecycle.Ordinal;
import org.gaming.ruler.lifecycle.Priority;
import org.gaming.ruler.netty.NettySocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NettyLifecycle implements Lifecycle {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(NettyLifecycle.class);

    @Value("${game.netty.port:19090}")
    private int port;

    @Value("${game.netty.idle-seconds:60}")
    private int idleSeconds;

    @Value("${game.netty.boss-threads:1}")
    private int bossThreads;

    @Value("${game.netty.worker-threads:4}")
    private int workerThreads;

    private NettySocketServer server;

    @Override
    public LifecycleInfo getInfo() {
        return LifecycleInfo.valueOf(
                getClass().getSimpleName(),
                Priority.LOW,
                Ordinal._25);
    }

    @Override
    public void start() {
        server = new NettySocketServer(bossThreads, workerThreads);
        server.startServer(
                port,
                new DoupoWebSocketChannelInitializer(
                        idleSeconds,
                        new GameNettyHandler()));

        LOGGER.info(
                "Game WebSocket server started on port {}",
                port);
    }

    @Override
    public void stop() {
        if (server != null) {
            server.shutdown();
            LOGGER.info("Game WebSocket server stopped");
        }
    }
}