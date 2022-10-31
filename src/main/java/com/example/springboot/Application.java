package com.example.springboot;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application implements ApplicationRunner {

	private static Logger log = LogManager.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		// Read environmental variables:
		Map<String, String> env = System.getenv();
		int interval = env.get("interval") != null ? Integer.parseInt(env.get("interval")) : 3;
		int repeatCount = env.get("repeatCount") != null ? Integer.parseInt(env.get("repeatCount")) : 5;
		int timeOut = env.get("timeOut") != null ? Integer.parseInt(env.get("timeOut")) : 20;

		log.info("Interval: {}", interval);
		log.info("Repeat Count: {}", repeatCount);
		log.info("TIme Out: {}", timeOut);

		JobDetail job1 = JobBuilder.newJob(HelloJob.class).build();
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		SimpleScheduleBuilder sb1 = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(interval)
				.withRepeatCount(repeatCount);
		Trigger trigger = TriggerBuilder.newTrigger().withSchedule(sb1).build();

		scheduler.start();
		log.info("Scheduler started.");
		scheduler.scheduleJob(job1, trigger);
		log.info("Trigger {} state: {}", trigger.getKey().getName(), scheduler.getTriggerState(trigger.getKey()));

		Thread.sleep(timeOut * 1000);
		log.info("Trigger {} state: {}", trigger.getKey().getName(), scheduler.getTriggerState(trigger.getKey()));
		if (scheduler.getTriggerState(trigger.getKey()).toString().equals("NONE")) {
			log.info("Shutting down scheduler...");
			scheduler.shutdown();
			System.exit(0);
		}
	}
}
