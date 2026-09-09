package com.doupo.server;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.doupo.server.foundation.starting.ProjectBooter;

@ComponentScan(basePackages = {"com.doupo", "org.gaming"})
@SpringBootApplication
public class DoupoGameServerApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication();
        application.addListeners(new ProjectBooter());
        application.addPrimarySources(
                Collections.singletonList(DoupoGameServerApplication.class));
        application.run(args);
    }
}