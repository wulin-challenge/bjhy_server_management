package org.apel.server.management.main.container.domain.enums;

/**
 * 启动状态
 * @author wubo
 */
public enum StartStatus {
	
	notDeploy("notDeploy","未部署"),deploy("deploy","已部署,未启动"),notStarted("notStarted","未启动"),starting("starting","正在启动"),running("running","已运行"),startFail("startFail","启动失败");
	
	private String code;
	private String name;
	
	private StartStatus(String code, String name) {
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
