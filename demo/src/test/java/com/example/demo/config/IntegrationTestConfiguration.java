package com.example.demo.config;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.ClassRule;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

import javax.sql.DataSource;
import java.util.function.Consumer;

import static org.testcontainers.containers.PostgreSQLContainer.IMAGE;

@TestConfiguration
@Import({MainConfig.class})
public class IntegrationTestConfiguration {
    @ClassRule
    public static PostgreSQLContainer POSTGRESQL_SERVER_CONTAINER = new PostgreSQLContainer<>(IMAGE);

    static {
        Consumer<CreateContainerCmd> cmd = e -> e.withPortBindings(new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432)));
        POSTGRESQL_SERVER_CONTAINER.withCreateContainerCmdModifier(cmd);
        POSTGRESQL_SERVER_CONTAINER.start();
        var containerDelegate = new JdbcDatabaseDelegate(POSTGRESQL_SERVER_CONTAINER, "");
        ScriptUtils.runInitScript(containerDelegate, "db/Init.sql");
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(POSTGRESQL_SERVER_CONTAINER.getJdbcUrl());
        config.setUsername(POSTGRESQL_SERVER_CONTAINER.getUsername());
        config.setPassword(POSTGRESQL_SERVER_CONTAINER.getPassword());
        config.setDriverClassName(POSTGRESQL_SERVER_CONTAINER.getDriverClassName());
        config.setMaximumPoolSize(Runtime.getRuntime().availableProcessors());
        config.setConnectionTimeout(1000); // decreased timeouts to speed up tests
        config.setValidationTimeout(1000);
        return new HikariDataSource(config);
    }
}
