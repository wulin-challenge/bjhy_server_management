package org.apel.server.management.common.domain.enums;

/**
 * 应用程序与子容器之间传输的字段管理
 * @author wubo
 *
 */
public enum ApplicationTransferEnum {
	
	operatingSystem("operating.system","操作系统"),pid("pid","进程pid"),startStatus("start.status","启动状态");
	
	private String code;
	private String name;
	
	private ApplicationTransferEnum(String code, String name) {
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
