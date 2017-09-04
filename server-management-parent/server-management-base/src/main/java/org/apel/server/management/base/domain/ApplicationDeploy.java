package org.apel.server.management.base.domain;

import java.io.Serializable;

/**
 * 应用程序部署实体类(放在用于传输的base工程中)
 * @author wubo
 */
public class ApplicationDeploy implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	 /** 部署的应用名称
	    * 
	   */
	   private java.lang.String name;
	   /** 部署应用当前机器ip
	    * 
	    */
	   private java.lang.String ip;
	   /** 部署应用的端口
	    * 
	    */
	   private int port;
	   /** 部署应用的上下文
	    * 
	    */
	   private java.lang.String context;
	   /** 应用的访问入口
	    * 
	    */
	   private java.lang.String entry;
	   /** 当前应用的pid
	    * 
	    */
	   private int pid;
	   /** 当前应用所在的操作系统
	    * 
	    */
	   private java.lang.String operatingSystem;
	   /** 当前部署的应用的启动状态
	    * 
	    */
	   private java.lang.String startStatus;
	   /** 当前部署应用的路径
	    * 
	    */
	   private java.lang.String deployPath;
	   /** 应用的部署日期
	    * 
	    */
	   private java.util.Date deployDate;
	public java.lang.String getName() {
		return name;
	}
	public void setName(java.lang.String name) {
		this.name = name;
	}
	public java.lang.String getIp() {
		return ip;
	}
	public void setIp(java.lang.String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public java.lang.String getContext() {
		return context;
	}
	public void setContext(java.lang.String context) {
		this.context = context;
	}
	public java.lang.String getEntry() {
		return entry;
	}
	public void setEntry(java.lang.String entry) {
		this.entry = entry;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public java.lang.String getOperatingSystem() {
		return operatingSystem;
	}
	public void setOperatingSystem(java.lang.String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
	public java.lang.String getStartStatus() {
		return startStatus;
	}
	public void setStartStatus(java.lang.String startStatus) {
		this.startStatus = startStatus;
	}
	public java.lang.String getDeployPath() {
		return deployPath;
	}
	public void setDeployPath(java.lang.String deployPath) {
		this.deployPath = deployPath;
	}
	public java.util.Date getDeployDate() {
		return deployDate;
	}
	public void setDeployDate(java.util.Date deployDate) {
		this.deployDate = deployDate;
	}
}
