package org.apel.server.management.main.container.service.impl;

import java.util.List;

import org.apel.gaia.commons.pager.PageBean;
import org.apel.gaia.infrastructure.impl.AbstractBizCommonService;
import org.apel.server.management.base.domain.TransferAgent;
import org.apel.server.management.main.container.dao.ApplicationAgentRepository;
import org.apel.server.management.main.container.domain.ApplicationAgent;
import org.apel.server.management.main.container.domain.enums.AgentStatus;
import org.apel.server.management.main.container.restful.RestSubClient;
import org.apel.server.management.main.container.service.ApplicationAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApplicationAgentServiceImpl extends AbstractBizCommonService<ApplicationAgent, String> implements ApplicationAgentService{

	@Autowired
	private RestSubClient restSubClient;
	
	@Autowired
	private ApplicationAgentRepository applicationAgentRepository;
	
	@Override
	@Transactional(readOnly=true)
	public void pageQuery(PageBean pageBean) {
//		System.out.println("xxxxxxxxxxxxxx");   
//		TransferAgent accessCheck = restSubClient.accessCheck();
//		System.out.println();
		super.pageQuery(pageBean); 
	}

	@Override
	public void checkAgentConnStatus() {
		List<ApplicationAgent> agentList = findAll(new Sort(new Order(Direction.ASC,"createDate")));
		for (ApplicationAgent applicationAgent : agentList) {
			try {
				TransferAgent accessCheck = restSubClient.accessCheck(applicationAgent.getIp(), applicationAgent.getPort());
				if(accessCheck != null && "1".equals(accessCheck.getTransferStatus())){
					applicationAgent.setStatus(AgentStatus.able.getCode()); 
					applicationAgent.setStatusName(AgentStatus.able.getName());
					applicationAgentRepository.update(applicationAgent);
				}
			} catch (Exception e) {
				applicationAgent.setStatus(AgentStatus.disable.getCode()); 
				applicationAgent.setStatusName(AgentStatus.disable.getName());
				applicationAgentRepository.update(applicationAgent);
				System.out.println("连接失败:"+e.getMessage());
			}
		}
	}
	
	
}
