package org.apel.server.management.base.domain;

import java.io.Serializable;

public class TransferAgent implements Serializable{

	private static final long serialVersionUID = 5250538344686116098L;
	
	/**
	 * 代理服务的名称
	 */
	private String name;
	
	/**
	 * 代理服务的Ip
	 */
	private String agentIp;
	
	/**
	 * 代理服务的端口
	 */
	private String agentPort;
	
	/**
	 * 代理服务的上下文
	 */
	private String agentContext;
	
	/**
	 * 传输状态{0:不可用,1:可用,2:部署成功,3:部署失败,4:停止失败,5:停止成功,6:启动成功,7:启动失败,11:删除失败,12:删除成功}
	 */
	private String transferStatus;
	
	/**
	 * 部署目录
	 */
	private String deployDirectory;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAgentIp() {
		return agentIp;
	}

	public void setAgentIp(String agentIp) {
		this.agentIp = agentIp;
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

	public String getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}

	public String getDeployDirectory() {
		return deployDirectory;
	}

	public void setDeployDirectory(String deployDirectory) {
		this.deployDirectory = deployDirectory;
	}
}
