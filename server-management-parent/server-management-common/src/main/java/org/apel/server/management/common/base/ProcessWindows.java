package org.apel.server.management.common.base;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apel.server.management.common.util.ProcessUtil;

/**
 * windows的Process操作
 * @author wubo
 *
 */
public class ProcessWindows {
	/**
	 * 启动进程
	 * @throws IOException 
	 */
	public Process startup(String startCMD) throws IOException{
		return executeCMD(startCMD);
	}
	
	/**
	 * down掉应用程序
	 * @param pid
	 * @throws IOException 
	 */
	public Process shutdown(String pid) throws IOException{
		//杀死应用程序的命令
		String killCMD = "taskkill /pid "+pid+" -t -f";//待执行的do命令(chdir命令作用是列出当前的工作目录)  
		return executeCMD(killCMD);
	}
	
	/**
	 * 检测pid
	 * @return
	 * @throws IOException 
	 */
	public Process checkPid(String pid) throws IOException{
		String checkPid = "tasklist -v |findstr "+pid;
		Process process = executeCMD(checkPid);
		return process;
	}
	
	/**
	 * 命令
	 * @param cmd
	 * @return
	 * @throws IOException
	 */
	private Process executeCMD(String cmd) throws IOException{
		if(StringUtils.isEmpty(cmd)){
			System.out.println("应用程序启动失败!因为启动命令为空...");
		}
		Process process = Runtime.getRuntime().exec("cmd /c " + cmd);//通过cmd程序执行cmd命令  
		return process;
	}
}
