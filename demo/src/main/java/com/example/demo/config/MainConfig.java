package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.concurrent.*;

@Configuration
public class MainConfig {
    @Bean
    public BlockingDeque<LocalDateTime> timestampQueue() {
        // max capacity calculated by assuming that database will not be available 7 days (7 days * seconds in day)
        return new LinkedBlockingDeque<>(7 * 86400);
    }

    @Bean
    public ScheduledExecutorService producerThreadPool() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Bean
    public ExecutorService consumerThreadPool() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
