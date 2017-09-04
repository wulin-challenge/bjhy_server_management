package org.apel.server.management.base.domain;

/**
 * 代理容器的信息(放在用于传输的base工程中)
 * @author wubo
 *
 */
public class AgentInfo {

	 /** 代理程序的pid
	    * 
	    */
	   private int pid;
	   /** 代理程序的名称
	    * 
	    */
	   private java.lang.String name;
	   /** 代理程序所在的操作系统
	    * 
	    */
	   private java.lang.String operatingSystem;
	   /** 代理程序的ip
	    * 
	   */
	   private java.lang.String ip;
	   /** 代理程序的端口
	    * 
	    */
	   private int port;
	   /** 代理程序的上下文
	    * 
	    */
	   private java.lang.String context;
	   
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public java.lang.String getName() {
		return name;
	}
	public void setName(java.lang.String name) {
		this.name = name;
	}
	public java.lang.String getOperatingSystem() {
		return operatingSystem;
	}
	public void setOperatingSystem(java.lang.String operatingSystem) {
		this.operatingSystem = operatingSystem;
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
}
