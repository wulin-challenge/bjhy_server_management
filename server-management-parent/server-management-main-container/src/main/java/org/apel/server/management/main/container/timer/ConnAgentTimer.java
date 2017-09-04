package org.apel.server.management.main.container.timer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apel.server.management.main.container.service.ApplicationAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 连接代理程序定时器
 * @author wubo
 *
 */
@Component
public class ConnAgentTimer {
	
	@Autowired
	private ApplicationAgentService applicationAgentService;
	
	/**
	 * 连接代理容器
	 */
	public void connAgentContainer(){
		Timer timer = new Timer();
		
		timer.scheduleAtFixedRate(new TimerTask (){
			@Override
			public void run() {
				applicationAgentService.checkAgentConnStatus();
			}
			
		}, 1000*5, 1000*60); //每60秒执行一次
	}

}
