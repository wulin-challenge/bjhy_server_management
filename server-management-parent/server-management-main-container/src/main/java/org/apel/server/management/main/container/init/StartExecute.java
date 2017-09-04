package org.apel.server.management.main.container.init;

import org.apel.server.management.main.container.timer.ConnAgentTimer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartExecute implements InitializingBean{
	
	@Autowired
	private ConnAgentTimer connAgentTimer;

	@Override
	public void afterPropertiesSet() throws Exception {
		//执行容器定时器
		connAgentTimer.connAgentContainer();
	}

}
