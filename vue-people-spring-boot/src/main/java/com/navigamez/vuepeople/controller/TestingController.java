package com.navigamez.vuepeople.controller;

import com.navigamez.vuepeople.exception.BadRequestException;
import com.navigamez.vuepeople.service.TestingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testing")
public class TestingController {

    private final TestingService testingService;

    public TestingController(TestingService testingService) {
        this.testingService = testingService;
    }

    @PostMapping("/deleteAll")
    public void deleteAll() {
        testingService.deleteAll();
    }

    @PostMapping("/fill")
    public void fill(@RequestParam int count) {
        if (count > 10000 || count < 1) {
            throw new BadRequestException("Count must be a positive number and cannot be larger than 10,000; count was [" + count + "]");
        }
        testingService.fill(count);
    }
}
