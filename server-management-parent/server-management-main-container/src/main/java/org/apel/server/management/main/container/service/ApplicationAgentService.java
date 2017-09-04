package org.apel.server.management.main.container.service;

import org.apel.gaia.infrastructure.BizCommonService;
import org.apel.server.management.main.container.domain.ApplicationAgent;

public interface ApplicationAgentService extends BizCommonService<ApplicationAgent,String>{

	/**
	 * 检测代理容器的连接状态
	 */
	public void checkAgentConnStatus();
}
