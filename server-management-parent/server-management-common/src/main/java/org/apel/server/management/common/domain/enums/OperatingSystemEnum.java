package org.apel.server.management.common.domain.enums;


/**
 * 操作系统枚举管理
 * @author wubo
 *
 */
public enum OperatingSystemEnum {
	linux("linux","linux操作系统"), windows("windows","windows操作系统");
	
	private final String code;
	private final String name;
	
	private OperatingSystemEnum(String code, String name) {
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

	

