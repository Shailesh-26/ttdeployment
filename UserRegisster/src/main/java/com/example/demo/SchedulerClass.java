package com.example.demo;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component

public class SchedulerClass {
	@Scheduled(fixedDelay=5000)
	public void name() {
		System.out.println("hsfwh");
	}
	//crons 5 * * * * *
	@Scheduled(cron="5 * * * * *")
	public void name1() {
		System.out.println("Happy birthday Shailuuu!");
	}
}
