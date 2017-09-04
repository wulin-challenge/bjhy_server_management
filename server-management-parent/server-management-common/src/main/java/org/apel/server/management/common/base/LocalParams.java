package org.apel.server.management.common.base;

import java.lang.management.ManagementFactory;

import org.apache.commons.lang.StringUtils;
import org.apel.server.management.common.domain.enums.OperatingSystemEnum;

/**
 * 本地的相关数据
 * @author wubo
 */
public class LocalParams {
	
	
	/**
	 * 加载本地操作系统
	 */
	public static OperatingSystemEnum loadLocalOperatingSystem(){
		String os=System.getProperty("os.name");
		
		if(StringUtils.isEmpty(os)){
			System.out.println("不能识别当前操作系统!!");
			return null;
		}else{
			os = os.toLowerCase();
		}
		
		//windows系统
		if(os.contains(OperatingSystemEnum.windows.getCode())){
			return OperatingSystemEnum.windows;
			
		//linux系统
		}else if(os.contains(OperatingSystemEnum.linux.getCode())){
			return OperatingSystemEnum.linux;
		}
		
		return null;
	}
	
	/**
	 * 得到当前进程pid
	 * @return
	 */
	public static String getCurrentProcessPID(){
		String currentPID = null;
		OperatingSystemEnum operatingSystemEnum = loadLocalOperatingSystem();
		
		//windows系统
		if(OperatingSystemEnum.windows == operatingSystemEnum){
			String name = ManagementFactory.getRuntimeMXBean().getName();  
			// get pid  
			String pid = name.split("@")[0];  
			currentPID = pid;
		//linux系统
		}else if(OperatingSystemEnum.linux == operatingSystemEnum){
			
		}
		
		return currentPID;
	}
	
}
