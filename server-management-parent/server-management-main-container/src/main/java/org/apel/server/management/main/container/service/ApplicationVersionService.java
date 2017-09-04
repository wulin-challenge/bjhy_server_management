package org.apel.server.management.main.container.service;

import java.util.List;

import org.apel.gaia.infrastructure.BizCommonService;
import org.apel.server.management.main.container.domain.ApplicationVersion;
public interface ApplicationVersionService  extends BizCommonService<ApplicationVersion,String>{

	/**
	 * 通过应用和版本号找到对应的应用版本
	 * @param application
	 * @param applicationVersion
	 * @return
	 */
	public ApplicationVersion findByApplicationAndAppVersion(String applicationId,int applicationVersion);
	

	/**
	 * 得到应用程序的版本
	 * @param applicationId
	 * @return
	 */
	public int getVersionMax(String applicationId);
	
	/**
	 * 通过应用找到对应的应用版本
	 * @param application
	 * @param applicationVersion
	 * @return
	 */
	public List<ApplicationVersion> findApplicationVersion(String applicationId);
}
