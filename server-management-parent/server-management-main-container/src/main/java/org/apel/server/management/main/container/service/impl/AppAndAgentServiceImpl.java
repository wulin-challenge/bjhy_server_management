package org.apel.server.management.main.container.service.impl;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.server.management.base.domain.TransferAgent;
import org.apel.server.management.main.container.domain.AppAndAgent;
import org.apel.server.management.main.container.domain.Application;
import org.apel.server.management.main.container.domain.ApplicationAgent;
import org.apel.server.management.main.container.domain.ApplicationVersion;
import org.apel.server.management.main.container.domain.enums.StartStatus;
import org.apel.server.management.main.container.restful.RestSubClient;
import org.apel.server.management.main.container.service.AppAndAgentService;
import org.apel.server.management.main.container.service.ApplicationAgentService;
import org.apel.server.management.main.container.service.ApplicationService;
import org.apel.server.management.main.container.service.ApplicationVersionService;
import org.apel.server.management.main.container.util.MainStorePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppAndAgentServiceImpl extends AbstractBizCommonService<AppAndAgent, String> implements AppAndAgentService{

	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private ApplicationVersionService applicationVersionService;
	
	@Autowired
	private ApplicationAgentService applicationAgentService;
	
	@Autowired
	private RestSubClient restSubClient;
	
	@Override
	public void deploy(String id) {
		AppAndAgent appAndAgent = getRepository().findOne(id);
		
		Application appliction = appAndAgent.getAppliction();
		ApplicationAgent applictionAgent = appAndAgent.getApplictionAgent();
		
		//部署目录
		String deployDirectory = getDeployDirectory(appAndAgent, applictionAgent);
		
		//应用程序zip
		File applicationZip = getApplicationZip(appAndAgent, appliction);
		
		TransferAgent transferAgent = restSubClient.deployApplication(applictionAgent.getIp(), applictionAgent.getPort(), deployDirectory, applicationZip);
		
		//远程部署成功后
		if("2".equals(transferAgent.getTransferStatus())){
			appAndAgent.setStartStatus(StartStatus.deploy.getCode());
			appAndAgent.setStartStatusName(StartStatus.deploy.getName());
			appAndAgent.setAppDeployDirectory(transferAgent.getDeployDirectory());
			
			getRepository().update(appAndAgent);
		}
		
	}
	
	/**
	 * 得到部署目录
	 * @param appAndAgent
	 * @param applictionAgent
	 * @return
	 */
	private String getDeployDirectory(AppAndAgent appAndAgent,ApplicationAgent applictionAgent){
		String appDeployDirectory = appAndAgent.getAppDeployDirectory();
		if(StringUtils.isNotEmpty(appDeployDirectory)){
			return appDeployDirectory.substring(0,appDeployDirectory.lastIndexOf("/"));
		}
		
		TransferAgent deployDirectory = restSubClient.getDeployDirectory(applictionAgent.getIp(), applictionAgent.getPort());
		if(deployDirectory != null){
			return deployDirectory.getDeployDirectory();
		}
		return null;
	}
	
	/**
	 * 得到应用文件zip
	 * @return
	 */
	private File getApplicationZip(AppAndAgent appAndAgent,Application application){
		Boolean hasJar = application.getHasJar();
		if(hasJar){
			ApplicationVersion applicationVersion = applicationVersionService.findByApplicationAndAppVersion(application.getId(), appAndAgent.getApplicationVersion());
			File deployApplicationFile = MainStorePath.getDeployApplicationFile(applicationVersion.getStorePath());
			return deployApplicationFile;
		}
		return null;
		
	}

	@Override
	public void upgrade(String id) {
		System.out.println();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startup(String id) {
		System.out.println();
		
		AppAndAgent appAndAgent = getRepository().findOne(id);
		
		Application appliction = appAndAgent.getAppliction();
		ApplicationAgent applictionAgent = appAndAgent.getApplictionAgent();
		
		TransferAgent transferAgent = restSubClient.startup(applictionAgent.getIp(), applictionAgent.getPort(), appAndAgent.getAppDeployDirectory(),appAndAgent.getId());
		
		//启动成功
		if("6".equals(transferAgent.getTransferStatus())){
			appAndAgent.setStartDate(new Date());
			appAndAgent.setStartStatus(StartStatus.starting.getCode());
			appAndAgent.setStartStatusName(StartStatus.starting.getName());
			getRepository().update(appAndAgent);
		}
	}

	@Override
	public void suspend(String id) {
		
		AppAndAgent appAndAgent = getRepository().findOne(id);
		
		Application appliction = appAndAgent.getAppliction();
		ApplicationAgent applictionAgent = appAndAgent.getApplictionAgent();
		
		if(appAndAgent.getApplicationPid() != null && appAndAgent.getApplicationPid() != 0){
			TransferAgent transferAgent = restSubClient.suspend(applictionAgent.getIp(), applictionAgent.getPort(),appAndAgent.getId(), appAndAgent.getApplicationPid()+"");
			if("5".equals(transferAgent.getTransferStatus())){
				appAndAgent.setStartStatus(StartStatus.notStarted.getCode());
				appAndAgent.setStartStatusName(StartStatus.notStarted.getName());
				getRepository().update(appAndAgent);
			}
			
		}else{
			appAndAgent.setStartStatus(StartStatus.notStarted.getCode());
			appAndAgent.setStartStatusName(StartStatus.notStarted.getName());
			getRepository().update(appAndAgent);
//			throw new RuntimeException("pid不存在,停止失败!");
		}
		
	}

	@Override
	public void deleteById(String... ids) {
		for (String id : ids) {
			AppAndAgent appAndAgent = getRepository().findOne(id);
			
			Application appliction = appAndAgent.getAppliction();
			ApplicationAgent applictionAgent = appAndAgent.getApplictionAgent();
			
			//部署目录
			String deployDirectory = getDeployDirectory(appAndAgent, applictionAgent);
			
			TransferAgent transferAgent = restSubClient.delete(applictionAgent.getIp(), applictionAgent.getPort(),appAndAgent.getId(), appAndAgent.getApplicationPid()+"",deployDirectory);
			
			//正式删除业务数据
			if("12".equals(transferAgent.getTransferStatus())){
				super.deleteById(id);
			}
			
		}
	}
	
	
}
