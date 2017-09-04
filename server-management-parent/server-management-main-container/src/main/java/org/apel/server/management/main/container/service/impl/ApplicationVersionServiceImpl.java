package org.apel.server.management.main.container.service.impl;

import java.util.List;

import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.server.management.main.container.dao.ApplicationVersionRepository;
import org.apel.server.management.main.container.domain.ApplicationVersion;
import org.apel.server.management.main.container.service.ApplicationVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApplicationVersionServiceImpl extends AbstractBizCommonService<ApplicationVersion, String> implements ApplicationVersionService {

	@Autowired
	private ApplicationVersionRepository applicationVersionRepository;
	
	@Override
	public ApplicationVersion findByApplicationAndAppVersion(String applicationId, int applicationVersion) {
		return applicationVersionRepository.findApplicationVersion(applicationId, applicationVersion);
	}

	@Override
	public int getVersionMax(String applicationId) {
		int version = 0;
		try {
			version = applicationVersionRepository.findApplicationMax(applicationId);
		} catch (Exception e) {
			System.out.println("没有找到任何数据: "+e.getMessage());
		}
		version +=1;
		return version;
	}

	@Override
	public List<ApplicationVersion> findApplicationVersion(String applicationId) {
		return applicationVersionRepository.findApplicationVersion(applicationId);
	}
}
