package com.fingerbook.rest.back;

import org.springframework.scheduling.annotation.Scheduled;
	
public class ScheduledProcessor {

    public void process() {
        for (int i = 0; i < 10; i++) {
        	System.out.println("message " + i);
        }
    }
}
