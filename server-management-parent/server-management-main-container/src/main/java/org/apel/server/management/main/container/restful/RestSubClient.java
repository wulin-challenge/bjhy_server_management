package org.apel.server.management.main.container.restful;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apel.server.management.base.domain.TransferAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * 主容器调用子容器接口的客户端
 * @author wubo
 */
@Component
public class RestSubClient {
	
	@Autowired
    private RestTemplate subTemplate;
	
	public TransferAgent accessCheck(String agentIp, String agentPort) {
		String restSubUrl = getAgentUrl(agentIp, agentPort);
		return subTemplate.getForObject(restSubUrl + "accessCheck",TransferAgent.class);
	}

	/**
	 * 得到访问代理容器的url
	 * @param agentIp 代理IP
	 * @param agentPort 代理端口
	 * @return
	 */
	public String getAgentUrl(String agentIp, String agentPort) {
		if(StringUtils.isEmpty(agentPort)){
			agentPort = "5555"; //默认端口
		}
		
		// 主容器调用子容器接口的url
		String agentUrl = "http://" + agentIp + ":" + agentPort
				+ "/sub-container/restSub/";
		return agentUrl;
	}
	
	/**
	 * 得到部署目录
	 * @return
	 */
	public TransferAgent getDeployDirectory(String agentIp, String agentPort){
		String restSubUrl = getAgentUrl(agentIp, agentPort);
		return subTemplate.getForObject(restSubUrl + "getDeployDirectory",TransferAgent.class);
	}
	
	/**
	 * 部署应用文件
	 * @param agentIp
	 * @param agentPort
	 * @param deployDirectory
	 * @param file
	 * @return
	 */
	public TransferAgent deployApplication(String agentIp, String agentPort,String deployDirectory,File file){
		 String restSubUrl = getAgentUrl(agentIp, agentPort);
		 
		 FileSystemResource resource = new FileSystemResource(file);
		 MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();  
		 param.add("zipFile", resource);  
		 param.add("deployDirectory", deployDirectory);  
		 param.add("fileName","application.zip");
		 
		 HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String,Object>>(param);  
		 ResponseEntity<TransferAgent> transferAgent = subTemplate.exchange(restSubUrl + "deployApplication", HttpMethod.POST, httpEntity, TransferAgent.class);  
		
//		 TransferAgent transferAgent = subTemplate.postForObject(restSubUrl + "deployApplication", param, TransferAgent.class);  
		return transferAgent.getBody();
	
	}
	
	//升级
	public TransferAgent upgrade(String agentIp, String agentPort,File file) {
		// TODO Auto-generated method stub
		return null;
	}

	//启动
	/**
	 * 相对路径
	 * @param agentIp
	 * @param agentPort
	 * @param relativePath
	 */
	public TransferAgent startup(String agentIp, String agentPort,String relativePath,String appAndAgentId) {
		 String restSubUrl = getAgentUrl(agentIp, agentPort);
		 return subTemplate.getForObject(restSubUrl + "startup?relativePath={relativePath}&appAndAgentId={appAndAgentId}",TransferAgent.class,relativePath,appAndAgentId);
	}

	//停止
	public TransferAgent suspend(String agentIp, String agentPort,String appAndAgentId,String pid) {
		 String restSubUrl = getAgentUrl(agentIp, agentPort);
		 return subTemplate.getForObject(restSubUrl + "suspend?pid={pid}&appAndAgentId={appAndAgentId}",TransferAgent.class,pid,appAndAgentId);
	}
	
	//停止
	public TransferAgent delete(String agentIp, String agentPort,String appAndAgentId,String pid,String deployDirectory) {
		 String restSubUrl = getAgentUrl(agentIp, agentPort);
		 return subTemplate.getForObject(restSubUrl + "delete?pid={pid}&appAndAgentId={appAndAgentId}&deployDirectory={deployDirectory}",TransferAgent.class,pid,appAndAgentId,deployDirectory);
	}
	
	
	   
}
