package com.doupo.server.foundation.starting;

import org.gaming.ruler.lifecycle.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ServerFoundationLifecycle implements Lifecycle {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ServerFoundationLifecycle.class);

    @Override
    public void start() {
        LOGGER.info("Doupo server foundation lifecycle started");
    }

    @Override
    public void stop() {
        LOGGER.info("Doupo server foundation lifecycle stopped");
    }
}