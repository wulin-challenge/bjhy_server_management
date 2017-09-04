package org.apel.server.management.main.container.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "base_app_and_agent")
public class AppAndAgent {

	@Id
	private String id;

	private Date createDate;
	
	// 应用代理IP
	private String  applicationIP;
	// 应用程序的端口
	private Integer  applicationPort;
	// 应用程序版本
	private Integer  applicationVersion;
	//应用代理上下文
	private String applicationContext;
	// 操作系统
	private String  operatingSystem;
	// 应用程序日志
	private String  applicationLog;
	// 应用程序PID
	private Integer  applicationPid;
	// 应用代理IP
	private String  agentIP;
	// 应用代理端口
	private String agentPort;
	//应用代理上下文
	private String agentContext;
	// 代理程序日志
	private String  agentLog;
	// 启动日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date  startDate;
	// 启动状态
	private String  startStatus;
	// 启动状态名称
	private String  startStatusName;
	
	//应用程序的部署目录
	private String appDeployDirectory;
	
	/**
	 * 应用程序实体
	 */
	@ManyToOne(targetEntity=Application.class)
	@JoinColumn(name="application_id")
	private Application appliction;

	/**
	 * 应用程序代理实体
	 */
	@ManyToOne(targetEntity=ApplicationAgent.class)
	@JoinColumn(name="app_agent_id")
	private ApplicationAgent applictionAgent;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public Integer getApplicationPort() {
		return applicationPort;
	}
	public void setApplicationPort(Integer applicationPort) {
		this.applicationPort = applicationPort;
	}
	public Integer getApplicationVersion() {
		return applicationVersion;
	}
	public void setApplicationVersion(Integer applicationVersion) {
		this.applicationVersion = applicationVersion;
	}
	public String getOperatingSystem() {
		return operatingSystem;
	}
	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
	public String getApplicationLog() {
		return applicationLog;
	}
	public void setApplicationLog(String applicationLog) {
		this.applicationLog = applicationLog;
	}
	public Integer getApplicationPid() {
		return applicationPid;
	}
	public void setApplicationPid(Integer applicationPid) {
		this.applicationPid = applicationPid;
	}
	public String getAgentIP() {
		return agentIP;
	}
	public void setAgentIP(String agentIP) {
		this.agentIP = agentIP;
	}
	public String getAgentLog() {
		return agentLog;
	}
	public void setAgentLog(String agentLog) {
		this.agentLog = agentLog;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getStartStatus() {
		return startStatus;
	}
	public void setStartStatus(String startStatus) {
		this.startStatus = startStatus;
	}

	public Application getAppliction() {
		return appliction;
	}

	public void setAppliction(Application appliction) {
		this.appliction = appliction;
	}

	public ApplicationAgent getApplictionAgent() {
		return applictionAgent;
	}

	public void setApplictionAgent(ApplicationAgent applictionAgent) {
		this.applictionAgent = applictionAgent;
	}

	public String getApplicationIP() {
		return applicationIP;
	}

	public void setApplicationIP(String applicationIP) {
		this.applicationIP = applicationIP;
	}

	public String getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(String applicationContext) {
		this.applicationContext = applicationContext;
	}

	public String getAgentPort() {
		return agentPort;
	}

	public void setAgentPort(String agentPort) {
		this.agentPort = agentPort;
	}

	public String getAgentContext() {
		return agentContext;
	}

	public void setAgentContext(String agentContext) {
		this.agentContext = agentContext;
	}

	public String getStartStatusName() {
		return startStatusName;
	}

	public void setStartStatusName(String startStatusName) {
		this.startStatusName = startStatusName;
	}

	public String getAppDeployDirectory() {
		return appDeployDirectory;
	}

	public void setAppDeployDirectory(String appDeployDirectory) {
		this.appDeployDirectory = appDeployDirectory;
	}

}
