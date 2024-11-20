package com.example.app_one.controller;

import com.example.app_one.controller.model.Status;
import com.example.app_one.service.AppOneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AppOneController {

	private final AppOneService appOneService;

	@GetMapping(value = "/successfulCall")
	void successfulCall() {
		log.info("Endpoint successfulCall called");
		appOneService.successfulCall();
	}

	@GetMapping("/exceptionThrown")
	void exceptionThrown() {
		log.info("Endpoint exceptionThrown called");
		appOneService.exceptionThrown();
	}

	@GetMapping("/incrementCounter")
	void incrementCounter() {
		log.info("Endpoint incrementCounter called");
		appOneService.incrementCounter();
	}

	@GetMapping("/observed/{status}")
	void observed(Status status) {
		log.info("Endpoint observed called");
		appOneService.iAmTheObservedMethod(status);
	}

	@GetMapping("/manuallyConfiguredRestTemplate")
	void appTwoRestTemplateWithManualSettings() {
		log.info("Endpoint appTwoRestTemplateWithManualSettings called");
		appOneService.appTwoRestTemplateWithManualSettings();
	}

	@GetMapping("/appTwoRestTemplateWithoutObservationRegistry")
	void appTwoRestTemplateWithoutObservationRegistry() {
		log.info("Endpoint appTwoRestTemplateWithoutObservationRegistry called");
		appOneService.appTwoRestTemplateWithoutObservationRegistry();
	}

	@GetMapping("/runOnDifferentThreadWontPropagateTrace")
	void runOnDifferentThreadWontPropagateTrace() {
		log.info("Endpoint runOnDifferentThread called");
		appOneService.runOnDifferentThreadWontPropagateTrace();
	}

	@GetMapping("/runOnDifferentThread")
	void runOnDifferentThread() {
		log.info("Endpoint runOnDifferentThread called");
		appOneService.runOnDifferentThread();
	}

	@GetMapping("/runOnDifferentThreadWrappedExecutorService")
	void runOnDifferentThreadExecutorServiceAdapter() {
		log.info("Endpoint runOnDifferentThreadWrappedExecutorService called");
		appOneService.runOnDifferentThreadWrappedExecutorService();
	}

	@GetMapping("/setActiveUsers/{activeUsers}")
	void setActiveUsers(Integer activeUsers) {
		log.info("Endpoint setActiveUsers called");
		appOneService.setActiveUsers(activeUsers);
	}

	@GetMapping("/badLog")
	void badLog() {
		log.info("Here is a log containing Bad log and something more...");
	}

	@GetMapping("/allocateHeapMemory")
	void allocateHeapMemory() {
		List<String> list = IntStream.range(1, 10000000).boxed().map(String::valueOf).toList();
		String.join("", list);
	}

}
