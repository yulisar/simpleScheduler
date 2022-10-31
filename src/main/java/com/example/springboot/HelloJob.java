package com.example.springboot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloJob implements Job {

	private static Logger log = LogManager.getLogger(HelloJob.class);
	private static int count;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("===================================");
		log.info("Hello - {}", count++ );
	}

}
