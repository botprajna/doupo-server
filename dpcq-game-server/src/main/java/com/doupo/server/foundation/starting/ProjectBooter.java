package com.doupo.server.foundation.starting;

import org.gaming.ruler.lifecycle.LifecycleSupport;
import org.gaming.ruler.spring.Spring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

public class ProjectBooter implements ApplicationListener<ApplicationContextEvent> {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ProjectBooter.class);

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            Spring.setContext(event.getApplicationContext());
            LOGGER.info("Starting gaming lifecycles");
            LifecycleSupport.start();
        } else if (event instanceof ContextClosedEvent) {
            LOGGER.info("Stopping gaming lifecycles");
            LifecycleSupport.stop();
        }
    }
}