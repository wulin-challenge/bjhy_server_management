package org.apel.server.management.main.container.service.impl;

import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.server.management.main.container.domain.Application;
import org.apel.server.management.main.container.domain.ApplicationVersion;
import org.apel.server.management.main.container.service.ApplicationService;
import org.apel.server.management.main.container.service.ApplicationVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApplicationServiceImpl extends AbstractBizCommonService<Application, String> implements ApplicationService{

	@Autowired
	private ApplicationVersionService applicationVersionService;
	
	@Override
	public void upload(Application application,ApplicationVersion applicationVersion) {
		application = getRepository().findOne(application.getId());
		applicationVersionService.save(applicationVersion);
		application.setHasJar(true);
		getRepository().save(application);
	}
}
