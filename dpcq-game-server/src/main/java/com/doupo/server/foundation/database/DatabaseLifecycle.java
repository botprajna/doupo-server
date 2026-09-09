package com.doupo.server.foundation.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.Table;
import org.gaming.db.mysql.dao.DaoManager;
import org.gaming.db.mysql.database.DBConfig;
import org.gaming.db.mysql.table.TableBuilder;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.db.usecase.SlimDao;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.lifecycle.LifecycleInfo;
import org.gaming.ruler.lifecycle.Ordinal;
import org.gaming.ruler.lifecycle.Priority;
import org.gaming.ruler.spring.Spring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLifecycle implements Lifecycle {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DatabaseLifecycle.class);

    @Value("${game.database.config-file}")
    private String configFile;

    @Value("${game.server.id:1}")
    private int serverId;

    @Override
    public LifecycleInfo getInfo() {
        return LifecycleInfo.valueOf(
                getClass().getSimpleName(),
                Priority.INITIALIZATION,
                Ordinal.MIN);
    }

    @Override
    public void start() throws Exception {
        TableBuilder.SERVER_IDENTITY = serverId;

        List<DBConfig> configs = DBConfig.loadConfigs(configFile);
        if (configs.isEmpty()) {
            throw new IllegalStateException(
                    "No database configuration found: " + configFile);
        }

        validateConnections(configs);

        List<Class<?>> entityClasses = new ArrayList<>();
        collectEntities(
                Spring.getBeansWithAnnotation(Table.class),
                entityClasses);
        collectEntities(
                Spring.getBeansWithAnnotation(LogTable.class),
                entityClasses);

        SlimDao.build(configs, entityClasses);

        LOGGER.info(
                "Database initialized: dataSources={}, entities={}",
                configs.size(),
                entityClasses.size());
    }

    private void validateConnections(List<DBConfig> configs)
            throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        for (DBConfig config : configs) {
            String url = "jdbc:mysql://"
                    + config.getIpPort()
                    + "/"
                    + config.getDbName()
                    + "?useSSL=false"
                    + "&useUnicode=true"
                    + "&characterEncoding=UTF-8"
                    + "&serverTimezone=GMT%2B8";

            try (Connection connection = DriverManager.getConnection(
                    url,
                    config.getUser(),
                    config.getPassword());
                 Statement statement = connection.createStatement()) {
                statement.execute("SELECT 1");
            }
        }
    }

    private void collectEntities(
            Collection<Object> samples,
            List<Class<?>> classes) {
        for (Object sample : samples) {
            if (sample instanceof AbstractEntity) {
                classes.add(Spring.getBeanRealClass(sample));
            }
        }
    }

    @Override
    public void stop() {
        DaoManager.stopAsync();
    }
}
