package org.apel.server.management.sub.container.watcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.WatchEvent;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apel.server.management.common.domain.enums.ApplicationTransferEnum;
import org.apel.server.management.sub.container.core.AgentStorePath;
import org.apel.server.management.sub.container.restful.RestMainClient;
import org.apel.server.management.sub.container.util.TransferProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileTransferListener {
	
	/**
	 * 文件监听队列
	 */
	public static Map<String,FileWatcher> listenerQueue = new HashMap<String,FileWatcher>();
	
	@Autowired
	private RestMainClient restMainClient;
	
	/**
	 * 监听pid
	 * @param relativePath
	 */
	public void listenerPID(String appAndAgentId,String relativePath){
		
		String zipDirectory = AgentStorePath.getApplicationZipDirectory(relativePath);
		
		String startDirectory = AgentStorePath.replaceSprit(zipDirectory+"/start-directory");
		
		File startDirectoryFile = new File(startDirectory);
		if(!startDirectoryFile.exists()){
			startDirectoryFile.mkdirs();
		}
		
		String transferStartDirectoryPath = AgentStorePath.replaceSprit(startDirectory+"/application_to_sub_container.properties");
		File transferStartDirectoryPathFile = new File(transferStartDirectoryPath);
		
		if(!transferStartDirectoryPathFile.exists()){
			try {
				transferStartDirectoryPathFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		FileWatcher fileWatcher = new FileWatcher(startDirectory,new TransferDealWith() {
			
			public void fileModifyEvent(WatchEvent<?> event) {
				
				
				
				TransferProperties transferProperties = new TransferProperties(transferStartDirectoryPath);
				
				String startStatus = transferProperties.getProperty(ApplicationTransferEnum.startStatus.getCode());
				String operatingSystem = transferProperties.getProperty(ApplicationTransferEnum.operatingSystem.getCode());
				String pid = transferProperties.getProperty(ApplicationTransferEnum.pid.getCode());
				
				if(StringUtils.isNotEmpty(pid)){
					restMainClient.setPidById(appAndAgentId, pid,startStatus);
				}
				System.out.println(startStatus);
				System.out.println(operatingSystem);
				System.out.println(pid);
			}
			
			public void fileDeleteEvent(WatchEvent<?> event) {
				
			}
			
			public void fileCreateEvent(WatchEvent<?> event) {
				
			}
		});
		
		//添加文件监听对象到文件监听队列中
		FileTransferListener.listenerQueue.put(appAndAgentId, fileWatcher);
	}

}
