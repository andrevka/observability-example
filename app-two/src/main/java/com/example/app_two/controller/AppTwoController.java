package com.example.app_two.controller;

import com.example.app_two.service.AppTwoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/two")
@RequiredArgsConstructor
public class AppTwoController {

    private final AppTwoService appTwoService;

    @GetMapping("/one")
    void endpointOne() {
        log.info("Endpoint one called");
        appTwoService.method1();
    }

    @GetMapping("/two")
    void endpointTwo() {
        log.info("Endpoint two called");
        appTwoService.method2();
    }

    @GetMapping("/four")
    void endpointFour() {
        log.info("Endpoint four called");
        appTwoService.method4();
    }

    @GetMapping("/five")
    void endpointFive() {
        log.info("Endpoint five called");
        appTwoService.method5();
    }

    @GetMapping("/six")
    void endpointSix() {
        log.info("Endpoint six called");
        appTwoService.method6();
    }

    @GetMapping("/runOnDifferentThread")
    void runOnDifferentThread() {
        log.info("Endpoint runOnDifferentThread called");
    }

}
