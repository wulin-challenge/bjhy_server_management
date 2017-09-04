package org.apel.server.management.sub.container.watcher;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

/**
 * 文件观察者
 * @author wubo
 *
 */
public class FileWatcher {
	
	/**
	 * 传输处理
	 */
	private TransferDealWith transferDealWith;
	
	/**
	 * 是否销毁监听对象
	 */
	private Boolean destroy = false;
	
	/**
	 *观察目录
	 */
	private String watcherDirectory;
	
	public FileWatcher(String watcherDirectory,TransferDealWith transferDealWith) {
		super();
		this.watcherDirectory = watcherDirectory;
		this.transferDealWith = transferDealWith;
		FileWatcherRegister();
	}

	/**
	 * 文件目录观察者注册
	 * @param watcherDirectory
	 */
	private void FileWatcherRegister(){
		Path listenerDirectory = Paths.get(watcherDirectory); //这是要监听的文件目录
		
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			
			listenerDirectory.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, //注册事件:文件创建事件,ENTRY_DELETE:文件删除事件,ENTRY_MODIFY:更改文件内容事件
					StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);
			while (true) {
				WatchKey watckKey = watcher.take(); //阻塞队列,当监听的文件目录没有任何改变时,就阻塞在这里
				List<WatchEvent<?>> events = watckKey.pollEvents();
				
				//当 destroy 为true表示销毁监听文件对象
				if(destroy){
					break;
				}
				for (WatchEvent<?> event : events) {
					if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) { //当文件被创建时,被监听的事件
						fileCreateEvent(event);
					}
					
					if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) { //当文件被更改时,被监听的事件
						fileModifyEvent(event);
					}
					
					if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) { //当文件被删除时,被监听的事件
						fileDeleteEvent(event);
					}
				}
				if (!watckKey.reset()) { //重置
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 文件被创建事件
	 * @param event
	 */
	private void fileCreateEvent(WatchEvent<?> event){
		transferDealWith.fileCreateEvent(event);
	}
	
	/**
	 * 文件被更改事件
	 * @param event
	 */
	private void fileModifyEvent(WatchEvent<?> event){
		transferDealWith.fileModifyEvent(event);
	}
	
	/**
	 * 文件被删除事件
	 * @param event
	 */
	private void fileDeleteEvent(WatchEvent<?> event){
		transferDealWith.fileDeleteEvent(event);
	}

	public Boolean getDestroy() {
		return destroy;
	}

	public void setDestroy(Boolean destroy) {
		this.destroy = destroy;
	}
	
}
