package com.example.demo.service;

import com.example.demo.model.Timestamp;
import com.example.demo.model.TimestampDto;
import com.example.demo.repository.TimestampRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TimestampService {

    private final TimestampRepository timestampRepository;

    public void save(Timestamp timestamp) {
        timestampRepository.save(timestamp);
    }

    public List<TimestampDto> getAllTimestamps(Integer page, Integer pageSize) {
        Pageable pageable = createPageable(page, pageSize);

        return timestampRepository.findAll(pageable).getContent().stream()
                .map(timestamp -> new TimestampDto(timestamp.getId(), timestamp.getTimestamp()))
                .toList();
    }

    private Pageable createPageable(Integer page, Integer pageSize) {
        if (page == null || page < 1) {
            page = 0;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 100;
        }
        return PageRequest.of(page, pageSize);
    }
}
