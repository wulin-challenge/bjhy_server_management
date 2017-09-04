package org.apel.server.management.application.domain;

/**
 * 应用程序需要传输的参数
 * @author wubo
 */
public class ApplicationParams {
	
	/**
	 * 未启动
	 */
	public static final String NOT_STARTED= "notStarted";
	
	/**
	 * 正在启动
	 */
	public static final String NOW_STARTING= "nowStarting";
	
	/**
	 * 启动成功
	 */
	public static final String STARTUP_SUCCESS= "startupSuccess";
	
	/**
	 * 启动失败
	 */
	public static final String START_FAILURE= "startFailure";
	
	/**
	 * 操作系统
	 */
	private String operatingSystem;
	
	/**
	 * 当前程序的pid
	 */
	private String pid;
	
	/**
	 * 应用程序的启动状态
	 */
	private String startStatus;

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getStartStatus() {
		return startStatus;
	}

	public void setStartStatus(String startStatus) {
		this.startStatus = startStatus;
	}
}
