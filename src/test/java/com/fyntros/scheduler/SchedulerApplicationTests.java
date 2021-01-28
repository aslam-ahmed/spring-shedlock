package com.fyntros.scheduler;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
class SchedulerApplicationTests {

	private static Logger logger = LoggerFactory.getLogger(SchedulerApplicationTests.class);
	
	@Test
	void givenMultipleApplication_whenStarted_thenOnlyOneAppSchedules() throws InterruptedException {
		SpringApplicationBuilder firstApp = new SpringApplicationBuilder(SchedulerApplication.class);
		SpringApplicationBuilder secondApp = new SpringApplicationBuilder(SchedulerApplication.class);
		firstApp.run("");
		secondApp.run("");
		
		Thread.sleep(1000l);
		ScheduledCurrentTimeTask firstScheduler = firstApp.context().getBean(ScheduledCurrentTimeTask.class);
		ScheduledCurrentTimeTask secondScheduler = secondApp.context().getBean(ScheduledCurrentTimeTask.class);
		
		int firstAppInvocationCount = firstScheduler.getInvocationCount();
		int secondAppInvocationCount = secondScheduler.getInvocationCount();
		
		logger.info("First application scheduler invocation count is {}", firstAppInvocationCount );
		logger.info("Second application scheduler invocation count is {}", secondAppInvocationCount );
		
		assertTrue(firstAppInvocationCount > 0, "First Scheduler Invocation Count " + firstAppInvocationCount);
		assertTrue(secondAppInvocationCount == 0, "Second Scheduler Invocation Count " + secondAppInvocationCount);
		
		logger.info("Killing first application");
		SpringApplication.exit(firstApp.context(), () -> 0);
		
		Thread.sleep(5000l);
		logger.info("Second application scheduler invocation count is {}", secondScheduler.getInvocationCount());
		assertTrue(secondScheduler.getInvocationCount() > 0, "Second Scheduler Invocation Count " + secondScheduler.getInvocationCount());
	}
}
