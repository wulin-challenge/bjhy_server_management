package org.apel.server.management.main.web;

import org.apache.commons.lang3.StringUtils;
import org.apel.server.management.base.domain.TransferAgent;
import org.apel.server.management.main.container.domain.AppAndAgent;
import org.apel.server.management.main.container.domain.enums.StartStatus;
import org.apel.server.management.main.container.service.AppAndAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 提供给子容器调用的主容器接口Controller
 * @author wubo
 *
 */
@Controller
@RequestMapping("restMain")
public class RestMainController {
	
	@Autowired
	private AppAndAgentService appAndAgentService;
	
	//启动
	@RequestMapping(value = "setPidById", method = RequestMethod.GET)
	public @ResponseBody TransferAgent setPidById(String appAndAgentId,String pid,String startStatus) {
		AppAndAgent appAndAgent = appAndAgentService.findById(appAndAgentId);
		
		if(StringUtils.isNotEmpty(pid)){
			appAndAgent.setApplicationPid(Integer.parseInt(pid));
			
			if("startupSuccess".equals(startStatus)){
				appAndAgent.setStartStatus(StartStatus.running.getCode());
				appAndAgent.setStartStatusName(StartStatus.running.getName());
			}else if("startFailure".equals(startStatus)){
				appAndAgent.setStartStatus(StartStatus.startFail.getCode());
				appAndAgent.setStartStatusName(StartStatus.startFail.getName());
			}else if("nowStarting".equals(startStatus)){
				appAndAgent.setStartStatus(StartStatus.starting.getCode());
				appAndAgent.setStartStatusName(StartStatus.starting.getName());
			}
		}
		appAndAgentService.update(appAndAgent);
		System.out.println();
		return null;
	}
	
	

}
