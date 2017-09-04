package org.apel.server.management.build;

import java.util.concurrent.CountDownLatch;

import org.apel.gaia.container.boot.SimplePlatformStarter;
import org.apel.server.management.application.start.StartApplication;
import org.apel.server.management.application.start.StartApplication.Application;
import org.apel.server.management.application.start.StartApplication.StartEvent;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainStart {
	
	public static void main(String[] args) throws Exception {
//		StartApplication.startup(new Application(){
//			public void startRun(StartEvent startEvent) throws Exception {
//				
//				//这种方式即可在windows上启动,又可以在linux上启动
//				CountDownLatch cdl = new CountDownLatch(1);
//				ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:META-INF/spring/module-*.xml");
//				context.start();
//				System.out.println("文件服务已经启动****");
//				startEvent.afterStareEvent();
//				cdl.await();
//				context.close();
//			}
//		});
		
		
		SimplePlatformStarter.start(args);
	}

}
