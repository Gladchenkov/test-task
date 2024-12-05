package com.example.demo.consumer;

import com.example.demo.constants.Constants;
import com.example.demo.model.Timestamp;
import com.example.demo.service.TimestampService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionException;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
@AllArgsConstructor
public class TimestampConsumer {
    private final BlockingDeque<LocalDateTime> timestampDeque;
    private final ExecutorService consumerThreadPool;
    private final TimestampService timestampService;

    private void consume() {
        consumerThreadPool.execute(() -> {
            while (true) {
                LocalDateTime dateTime = null;
                try {
                    dateTime = timestampDeque.takeFirst();
                    timestampService.save(new Timestamp(dateTime));
                    log.info("Saved timestamp: {}", Constants.DATE_TIME_FORMATTER.format(dateTime));
                } catch (TransactionException | DataAccessException exception) {
                    log.error("Database error, requeueing timestamp: {}", dateTime);
                    if (dateTime != null) {
                        // Put the timestamp back to the front of the queue
                        timestampDeque.addFirst(dateTime);
                    }
                } catch (InterruptedException exception) {
                    log.error("Interrupted while requeueing timestamp: {}", dateTime);
                }
            }
        });
    }

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        consume();
    }
}
