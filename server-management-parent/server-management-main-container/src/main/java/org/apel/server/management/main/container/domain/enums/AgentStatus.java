package org.apel.server.management.main.container.domain.enums;

/**
 * 代理程序状态
 * @author wubo
 */
public enum AgentStatus {
	able("ableStatus","可用"),disable("disableStatus","不可用");
	private String code;
	private String name;
	private AgentStatus(String code, String name) {
		this.code = code;
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	
}
