package org.apel.server.management.sub.container.test;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartSubContainer {
	
	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:META-INF/spring/module-*.xml");
		context.start();
		System.out.println("启动子容器成功****");
		System.in.read();
	}

}
