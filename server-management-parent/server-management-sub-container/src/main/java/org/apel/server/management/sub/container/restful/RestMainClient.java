package org.apel.server.management.sub.container.restful;

import org.apache.commons.lang3.StringUtils;
import org.apel.server.management.base.domain.TransferAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 子容器调用主容器接口的客户端
 * @author wubo
 */
@Component
public class RestMainClient {
	
	@Autowired
    private RestTemplate mainTemplate;
	
	@Value("${main_ip}")
	private String mainIp;
	
	private String MainPort = "6555";
	
	/**
	 * 得到访问主容器的url
	 * @param agentIp 代理IP
	 * @param agentPort 代理端口
	 * @return
	 */
	public String getMainUrl() {
		
		if(StringUtils.isEmpty(MainPort)){
			MainPort = "6555"; //默认端口
		}
		
		// 主容器调用主容器接口的url
		String mainUrl = "http://" + mainIp + ":" + MainPort
				+ "/main-container/restMain/";
		return mainUrl;
	}
	
	/**
	 * 设置对应启动的应用程序的pid
	 * @param agentIp
	 * @param agentPort
	 * @param relativePath
	 */
	public TransferAgent setPidById(String appAndAgentId, String pid,String startStatus) {
		 String restSubUrl = getMainUrl();
		 return mainTemplate.getForObject(restSubUrl + "setPidById?appAndAgentId={appAndAgentId}&pid={pid}&startStatus={startStatus}",TransferAgent.class,appAndAgentId,pid,startStatus);
	}
	
	
}
