package com.example.demo.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("service_timestamp")
@Getter
public class Timestamp {
    @Id
    private Long id;
    private LocalDateTime timestamp;

    public Timestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
