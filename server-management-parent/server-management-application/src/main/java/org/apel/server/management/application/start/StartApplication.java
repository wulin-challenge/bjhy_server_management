package org.apel.server.management.application.start;

import org.apel.server.management.application.domain.ApplicationParams;
import org.apel.server.management.application.util.ApplicationStorePath;
import org.apel.server.management.application.util.ApplicationToSubContainerProperties;
import org.apel.server.management.common.base.LocalParams;
import org.apel.server.management.common.domain.enums.ApplicationTransferEnum;
import org.apel.server.management.common.domain.enums.OperatingSystemEnum;

public class StartApplication {
	
	public static void startup(Application application) throws Exception{
		//删除本地文件,然后重新创建
//		ApplicationStorePath.deleteFile(ApplicationStorePath.getApplicationToSubContainerPath());
		ApplicationStorePath.createApplicationToSubContainerFile();
		
		ApplicationParams applicationParams = new ApplicationParams();
		StartEvent startEvent = new StartApplication().new StartEventImpl();
			try{
			OperatingSystemEnum operatingSystemEnum = LocalParams.loadLocalOperatingSystem();
			String currentProcessPID = LocalParams.getCurrentProcessPID();
			
			OperatingSystemEnum loadLocalOperatingSystem = LocalParams.loadLocalOperatingSystem();
			applicationParams.setOperatingSystem(loadLocalOperatingSystem.getCode());
			applicationParams.setPid(LocalParams.getCurrentProcessPID());
			applicationParams.setStartStatus(ApplicationParams.NOW_STARTING);
			
			//打印控制台
			System.out.println("操作系统: "+operatingSystemEnum);
			System.out.println("进程PID: "+currentProcessPID);
			
			//输出到本地文件
			ApplicationToSubContainerProperties.setProperty(ApplicationTransferEnum.operatingSystem.getCode(), applicationParams.getOperatingSystem());
			ApplicationToSubContainerProperties.setProperty(ApplicationTransferEnum.pid.getCode(), applicationParams.getPid());
			ApplicationToSubContainerProperties.setProperty(ApplicationTransferEnum.startStatus.getCode(), applicationParams.getStartStatus());
			
			//开始启动应用程序
			application.startRun(startEvent);
		}catch(Exception e){
			
			//应用程序启动失败!!
			applicationParams.setStartStatus(ApplicationParams.START_FAILURE);
			ApplicationToSubContainerProperties.setProperty(ApplicationTransferEnum.startStatus.getCode(), applicationParams.getStartStatus());
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * 应用程序启动接口
	 * @author wubo
	 *
	 */
	public interface Application{
		public void startRun(StartEvent startEvent) throws Exception;
	}
	
	/**
	 * 启动过程中的事件接口
	 * @author wubo
	 *
	 */
	public interface StartEvent{
		
		/**
		 * 启动之前的事件
		 */
		public void beforeStareEvent();
		
		/**
		 * 启动之后的事件
		 */
		public void afterStareEvent();
	}
	
	public class StartEventImpl implements StartEvent{

		@Override
		public void beforeStareEvent() {
			
		}

		@Override
		public void afterStareEvent() {
			ApplicationParams applicationParams = new ApplicationParams();
			//应用程序启动成功!!
			applicationParams.setStartStatus(ApplicationParams.STARTUP_SUCCESS);
			ApplicationToSubContainerProperties.setProperty(ApplicationTransferEnum.startStatus.getCode(), applicationParams.getStartStatus());
		}
		
	}
	
	

}
