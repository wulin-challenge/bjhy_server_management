package org.apel.server.management.sub.web;

import java.io.File;
import java.io.IOException;

import org.apel.server.management.base.domain.TransferAgent;
import org.apel.server.management.common.domain.enums.ApplicationTransferEnum;
import org.apel.server.management.common.util.ProcessUtil;
import org.apel.server.management.sub.container.core.AgentContainer;
import org.apel.server.management.sub.container.core.AgentStorePath;
import org.apel.server.management.sub.container.util.TransferProperties;
import org.apel.server.management.sub.container.watcher.FileTransferListener;
import org.apel.server.management.sub.container.watcher.FileWatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 提供给主容器调用的子容器接口Controller
 * @author wubo
 */
@Controller
@RequestMapping("restSub")
public class RestSubController {
	
	@Autowired
	private AgentContainer agentContainer;
	
	@Autowired
	private FileTransferListener fileTransferListener;
	
	/**
	 * 检查访问
	 * @return
	 */
	@RequestMapping(value = "accessCheck", method = RequestMethod.GET)
	public @ResponseBody TransferAgent accessCheck(){
		TransferAgent transferAgent = new TransferAgent();
		transferAgent.setTransferStatus("1");
		return transferAgent;
	}
	
	/**
	 * 得到部署目录
	 * @return
	 */
	@RequestMapping(value = "getDeployDirectory", method = RequestMethod.GET)
	public @ResponseBody TransferAgent getDeployDirectory(){
		TransferAgent transferAgent = new TransferAgent();
		transferAgent.setTransferStatus("1");
		transferAgent.setDeployDirectory(AgentStorePath.createApplicationDirectory());
		return transferAgent;
	}
	
	  
	@RequestMapping(value = "/deployApplication", method = RequestMethod.POST)  
	public @ResponseBody TransferAgent deployApplication(String deployDirectory,String fileName, MultipartFile zipFile) {  
		TransferAgent transferAgent = new TransferAgent();
		Boolean deployApplication = false;
		try {
			String relativePath = AgentStorePath.replaceSprit(deployDirectory+"/"+fileName);
			
			//若应用存在就删除应用在部署
			AgentStorePath.deleteFile(AgentStorePath.getApplicationDirectory(deployDirectory));
			
//			File deployDirectoryFile = new File(AgentStorePath.getApplicationDirectory(deployDirectory));
//			
//			if(!deployDirectoryFile.exists()){
//				deployDirectoryFile.mkdirs();
//			}
			
			deployApplication = AgentStorePath.deployApplication(relativePath, zipFile.getInputStream());
			if(deployApplication){
				
				//解压文件
				AgentStorePath.unzip(deployDirectory, relativePath);
				AgentStorePath.createStartupFile(deployDirectory, relativePath);
				transferAgent.setTransferStatus("2");
				transferAgent.setDeployDirectory(relativePath);
				
				String zipDirectory = AgentStorePath.getApplicationZipDirectory(relativePath);
				
				String startDirectory = AgentStorePath.replaceSprit(zipDirectory+"/start-directory");
				
				File startDirectoryFile = new File(startDirectory);
				if(!startDirectoryFile.exists()){
					startDirectoryFile.mkdirs();
				}
				
				String transferStartDirectoryPath = AgentStorePath.replaceSprit(startDirectory+"/application_to_sub_container.properties");
				File transferStartDirectoryPathFile = new File(transferStartDirectoryPath);
				
				if(!transferStartDirectoryPathFile.exists()){
					transferStartDirectoryPathFile.createNewFile();
				}
				
				TransferProperties transferProperties = new TransferProperties(transferStartDirectoryPath);
				
				transferProperties.setProperty(ApplicationTransferEnum.startStatus.getCode(),"");
				transferProperties.setProperty(ApplicationTransferEnum.operatingSystem.getCode(),"");
				transferProperties.setProperty(ApplicationTransferEnum.pid.getCode(),"");
				
			}else{
				transferAgent.setTransferStatus("3");
			}
		} catch (Exception e) {
			transferAgent.setTransferStatus("2");
			e.printStackTrace();
		}
	    return transferAgent;  
	}  
	
	//升级
	@RequestMapping(value = "/upgrade", method = RequestMethod.POST)  
	public @ResponseBody TransferAgent upgrade(String deployDirectory,String fileName, MultipartFile zipFile) {
		// TODO Auto-generated method stub
		
		return null;
	}

	//启动
	@RequestMapping(value = "startup", method = RequestMethod.GET)
	public @ResponseBody TransferAgent startup(String appAndAgentId,String relativePath) {
		TransferAgent transferAgent = new TransferAgent();
		AgentStorePath.startup(relativePath);
		
		Thread thread = new Thread(new Runnable() {
			public void run() {
				fileTransferListener.listenerPID(appAndAgentId, relativePath);;
			}
		});
		
		thread.start();
		
		transferAgent.setTransferStatus("6");
		return transferAgent;
	}

	//停止
	@RequestMapping(value = "suspend", method = RequestMethod.GET)
	public @ResponseBody TransferAgent suspend(String appAndAgentId,String pid) {
		TransferAgent transferAgent = new TransferAgent();
		try {
			
			FileWatcher fileWatcher = FileTransferListener.listenerQueue.get(appAndAgentId);
			
			if(fileWatcher != null){
				//销毁文件监听对象,停止监听
				fileWatcher.setDestroy(true);
				FileTransferListener.listenerQueue.remove(appAndAgentId);
			}
			
			ProcessUtil.shutdown(pid);
			transferAgent.setTransferStatus("5");
		} catch (IOException e) {
			e.printStackTrace();
			transferAgent.setTransferStatus("4");
		}
		return transferAgent;
	}
	
	//删除
	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public @ResponseBody TransferAgent delete(String appAndAgentId,String pid,String deployDirectory) {
		TransferAgent transferAgent = new TransferAgent();
		try {
			
			//若应用存在就删除应用在部署
			AgentStorePath.deleteFile(AgentStorePath.getApplicationDirectory(deployDirectory));
			transferAgent.setTransferStatus("12");
		} catch (Exception e) {
			e.printStackTrace();
			transferAgent.setTransferStatus("11");
		}
		return transferAgent;
	}
	
}
