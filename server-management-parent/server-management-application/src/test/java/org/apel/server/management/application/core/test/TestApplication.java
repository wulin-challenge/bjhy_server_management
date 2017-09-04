package org.apel.server.management.application.core.test;

import org.apel.server.management.application.start.StartApplication;
import org.apel.server.management.application.start.StartApplication.Application;
import org.apel.server.management.application.start.StartApplication.StartEvent;

public class TestApplication {
	
	public static void main(String[] args) throws Exception {
		StartApplication.startup(new Application(){
			@Override
			public void startRun(StartEvent startEvent) throws Exception {
				System.out.println("xxxxxx");
				
			}
		});
	}

}
