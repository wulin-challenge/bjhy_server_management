package org.apel.server.management.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apel.server.management.common.base.LocalParams;
import org.apel.server.management.common.base.ProcessWindows;
import org.apel.server.management.common.domain.enums.OperatingSystemEnum;

public class ProcessUtil {
	
	/**
	 * 启动进程
	 * @throws IOException 
	 */
	public static Process startup(String startCMD) throws IOException{
		Process process = null;
		OperatingSystemEnum operatingSystemEnum = LocalParams.loadLocalOperatingSystem();
			
		//windows系统
		if(OperatingSystemEnum.windows == operatingSystemEnum){
			ProcessWindows processWindows = new ProcessWindows();
			process = processWindows.startup(startCMD);
			
		//linux系统
		}else if(OperatingSystemEnum.linux == operatingSystemEnum){
			//.....
		}
		return process;
	}
	
	/**
	 * down掉应用程序
	 * @param pid
	 * @throws IOException 
	 */
	public static Process shutdown(String pid) throws IOException{
		Process process = null;
		OperatingSystemEnum operatingSystemEnum = LocalParams.loadLocalOperatingSystem();
			
		//windows系统
		if(OperatingSystemEnum.windows == operatingSystemEnum){
			ProcessWindows processWindows = new ProcessWindows();
			process = processWindows.shutdown(pid);
			
		//linux系统
		}else if(OperatingSystemEnum.linux == operatingSystemEnum){
			//.....
		}
		return process;
	}
	
	/**
	 * 检测pid
	 * @return true:表示pid存在,false:表示pid不存在
	 * @throws IOException 
	 */
	public static Boolean checkPid(String pid) throws IOException{
		Boolean hasPid = false;
		OperatingSystemEnum operatingSystemEnum = LocalParams.loadLocalOperatingSystem();
			
		//windows系统
		if(OperatingSystemEnum.windows == operatingSystemEnum){
			ProcessWindows processWindows = new ProcessWindows();
			Process process = processWindows.checkPid(pid);
			
			 BufferedReader strCon = null;
				try {
					strCon = new BufferedReader(new InputStreamReader(process.getInputStream(),"gbk"));
			        if (strCon.readLine() != null) {  
			        	hasPid = true;
			        }  
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					try {
						strCon.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			
		//linux系统
		}else if(OperatingSystemEnum.linux == operatingSystemEnum){
			//.....
		}
		return hasPid;
	}
	
	/**
	 * 输出日志到控制台
	 * @param process
	 */
	public static void console(Process process){
		 Thread thread = new Thread(new Runnable(){
				public void run() {
					 BufferedReader strCon = null;
						try {
							strCon = new BufferedReader(new InputStreamReader(process.getInputStream(),"gbk"));
							String line;  
					        while ((line = strCon.readLine()) != null) {  
					            System.out.println(line);  
					            }  
						} catch (Exception e) {
							e.printStackTrace();
						}finally{
							try {
								strCon.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
				}
		       });
		       thread.start();
	}
	
}
