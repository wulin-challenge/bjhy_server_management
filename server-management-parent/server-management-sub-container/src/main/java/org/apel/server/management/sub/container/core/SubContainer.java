package org.apel.server.management.sub.container.core;

import java.io.IOException;

import org.apel.server.management.common.util.ProcessUtil;
import org.apel.server.management.sub.container.watcher.FileTransferDealWith;
import org.apel.server.management.sub.container.watcher.FileWatcher;

/**
 * 子容器
 * @author wubo
 *
 */
public class SubContainer {
	
	
	public void xx(){
//		FileUtils.copyInputStreamToFile(source, destination);
	}
	
	/**
	 * 创建子容器目录
	 */
	public void createSubContainer(){
		SubContainerStorePath.createSubContainerDirectory();
	}
	
	/**
	 * 启动应用项目
	 */
	public void startApplication(){
		String applicationStartPath = getApplicationStartPath();
		try {
			Process process = ProcessUtil.startup(applicationStartPath);
			ProcessUtil.console(process);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 监听启动应用项目
	 */
	public void listenerStartApplication(){
		FileWatcher fileWatcher = new FileWatcher(getTransferStartDirectory(),new FileTransferDealWith());
	}
	
	/**
	 * 子应用项目的启动路径
	 * @return
	 */
	public String getApplicationStartPath(){
		return getApplicationStartDirectory()+"start.bat";
	}
	
	/**
	 * 子应用项目的启动目录
	 * @return
	 */
	public String getApplicationStartDirectory(){
		String applicationStartDirectory = SubContainerStorePath.getSubContainerDirectory()+"application";
		return SubContainerStorePath.replaceSpritAndSriptEnd(applicationStartDirectory);
	}
	
	/**
	 * 应用项目与子容器之间的传输目录
	 * @return
	 */
	public String getTransferStartDirectory(){
		String startDirectory = getApplicationStartDirectory()+"start-directory";
		return SubContainerStorePath.replaceSpritAndSriptEnd(startDirectory);
	}
	
	/**
	 * 应用项目与子容器之间的传输文件
	 * @return
	 */
	public String getTransferStartDirectoryPath(){
		String startDirectory = getTransferStartDirectory()+"application_to_sub_container.properties";
		return SubContainerStorePath.replaceSpritAndSriptEnd(startDirectory);
	}

}
