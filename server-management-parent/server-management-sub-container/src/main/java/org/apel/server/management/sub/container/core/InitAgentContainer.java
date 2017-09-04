package org.apel.server.management.sub.container.core;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitAgentContainer implements InitializingBean{

	@Autowired
	private AgentContainer agentContainer;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		agentContainer.initAgentContainer();
		
	}
	
	
//	SubContainer subContainer = new SubContainer();
//	subContainer.createSubContainer();
//	subContainer.startApplication(); 
//	subContainer.listenerStartApplication();
//	System.out.println();
//	String property = System.getProperty("user.dir");
//	System.out.println(property);

}
