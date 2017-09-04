package org.apel.server.management.sub.container.watcher;

import java.nio.file.WatchEvent;

/**
 * 传输处理接口
 * @author wubo
 *
 */
public interface TransferDealWith {
	/**
	 * 文件被创建事件
	 * @param event
	 */
	public void fileCreateEvent(WatchEvent<?> event);
	
	/**
	 * 文件被更改事件
	 * @param event
	 */
	public void fileModifyEvent(WatchEvent<?> event);
	
	/**
	 * 文件被删除事件
	 * @param event
	 */
	public void fileDeleteEvent(WatchEvent<?> event);
}
