package com.example.demo.producer;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.jupiter.api.Assertions.assertFalse;

class TimestampProducerTest {

    @Test
    public void testProduce_addsTimestampToQueue() throws InterruptedException {
        // given
        BlockingDeque<LocalDateTime> timestampDeque = new LinkedBlockingDeque<>(10);
        TimestampProducer timestampProducer = new TimestampProducer(timestampDeque, Executors.newSingleThreadScheduledExecutor(), Clock.systemUTC());

        // when
        timestampProducer.produce();
        Thread.sleep(1000);

        // then
        assertFalse(timestampDeque.isEmpty());
    }
}
