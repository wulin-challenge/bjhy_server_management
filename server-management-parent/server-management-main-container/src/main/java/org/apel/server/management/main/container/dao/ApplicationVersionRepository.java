package org.apel.server.management.main.container.dao;

import java.util.List;

import org.apel.gaia.persist.dao.CommonRepository;
import org.apel.server.management.main.container.domain.ApplicationVersion;
import org.springframework.data.jpa.repository.Query;
public interface ApplicationVersionRepository extends CommonRepository<ApplicationVersion, String>{
	
	/**
	 * 通过应用和版本号找到对应的应用版本
	 * @param application
	 * @param applicationVersion
	 * @return
	 */
	@Query("select o from ApplicationVersion o where o.application.id = ?1 and o.appVersion = ?2")
	public ApplicationVersion findApplicationVersion(String applicationId,int appVersion);
	
	/**
	 * 通过应用找到对应的应用版本
	 * @param application
	 * @param applicationVersion
	 * @return
	 */
	@Query("select o from ApplicationVersion o where o.application.id = ?1 order by o.appVersion asc ")
	public List<ApplicationVersion> findApplicationVersion(String applicationId);
	
	
	@Query("select max(o.appVersion) from ApplicationVersion o where o.application.id = ?1")
	public int findApplicationMax(String applicationId);
}
