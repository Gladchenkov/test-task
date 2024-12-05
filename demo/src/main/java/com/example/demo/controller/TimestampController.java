package com.example.demo.controller;

import com.example.demo.model.TimestampDto;
import com.example.demo.service.TimestampService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class TimestampController {

    private final TimestampService timestampService;

    @GetMapping("/timestamps")
    public List<TimestampDto> getAllTimestamps(@RequestParam(required = false) Integer page,
                                               @RequestParam(required = false) Integer pageSize) {
        return timestampService.getAllTimestamps(page, pageSize);
    }
}
