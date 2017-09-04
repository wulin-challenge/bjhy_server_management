package org.apel.server.management.main.container.domain.enums;

/**
 * 服务的类型
 * @author wubo
 */
public enum ServerType {
	
	web("webServer","web服务"),local("localServer","本地服务");
	
	private String code;
	private String name;
	
	private ServerType(String code, String name) {
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
