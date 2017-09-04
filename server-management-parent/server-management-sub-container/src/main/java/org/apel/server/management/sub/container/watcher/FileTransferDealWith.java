package org.apel.server.management.sub.container.watcher;

import java.nio.file.WatchEvent;

import org.apel.server.management.common.domain.enums.ApplicationTransferEnum;
import org.apel.server.management.sub.container.core.SubContainer;
import org.apel.server.management.sub.container.util.TransferProperties;
import org.springframework.stereotype.Component;

/**
 * 文件传输处理实现
 * @author wubo
 */
public class FileTransferDealWith implements TransferDealWith{

	@Override
	public void fileCreateEvent(WatchEvent<?> event) {
		
	}

	@Override
	public void fileModifyEvent(WatchEvent<?> event) {
		SubContainer subContainer = new SubContainer();
		
		String transferStartDirectoryPath = subContainer.getTransferStartDirectoryPath();
		TransferProperties transferProperties = new TransferProperties(transferStartDirectoryPath);
		
		String startStatus = transferProperties.getProperty(ApplicationTransferEnum.startStatus.getCode());
		String operatingSystem = transferProperties.getProperty(ApplicationTransferEnum.operatingSystem.getCode());
		String pid = transferProperties.getProperty(ApplicationTransferEnum.pid.getCode());
		
		System.out.println(startStatus);
		System.out.println(operatingSystem);
		System.out.println(pid);
	}

	@Override
	public void fileDeleteEvent(WatchEvent<?> event) {
		
	}

}
