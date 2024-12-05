package com.example.demo.producer;

import com.example.demo.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@Component
public class TimestampProducer {

    private final BlockingDeque<LocalDateTime> timestampDeque;
    private final ScheduledExecutorService producerThreadPool;
    private final Clock clock;

    public void produce() {
        producerThreadPool.scheduleAtFixedRate(() -> {
            try {
                LocalDateTime timestamp = LocalDateTime.now(clock);
                timestampDeque.put(timestamp);
                log.info("Generated timestamp: {}, Queue size: {}", Constants.DATE_TIME_FORMATTER.format(timestamp), timestampDeque.size());
            } catch (InterruptedException e) {
                log.error("Producer interrupted: " + e.getMessage());
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        produce();
    }
}
