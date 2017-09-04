package org.apel.server.management.main.container.service;

import org.apel.gaia.infrastructure.BizCommonService;
import org.apel.server.management.main.container.domain.Application;
import org.apel.server.management.main.container.domain.ApplicationVersion;

public interface ApplicationService extends BizCommonService<Application,String>{
	
	public void upload(Application application,ApplicationVersion applicationVersion);

}
