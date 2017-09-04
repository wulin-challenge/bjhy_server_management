package org.apel.server.management.main.container.service;

import org.apel.gaia.infrastructure.BizCommonService;
import org.apel.server.management.main.container.domain.AppAndAgent;

public interface AppAndAgentService extends BizCommonService<AppAndAgent,String>{
	
	//部署
	public void deploy(String id);
	//升级
	public void upgrade(String id);
	//启动
	public void startup(String id);
	//停止
	public void suspend(String id);
	
}
