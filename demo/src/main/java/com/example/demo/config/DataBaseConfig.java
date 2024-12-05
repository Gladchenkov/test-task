package com.example.demo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration
public class DataBaseConfig {

    @Bean
    public HikariDataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/example");
        config.setUsername("user");
        config.setPassword("user");
        config.setDriverClassName("org.postgresql.Driver");

        config.setMaximumPoolSize(Runtime.getRuntime().availableProcessors());
        config.setConnectionTimeout(5000);
        return new HikariDataSource(config);
    }
}
