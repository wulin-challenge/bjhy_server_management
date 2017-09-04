package org.apel.server.management.sub.container.core;

import org.apel.server.management.sub.container.restful.RestMainClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 代理容器
 * @author wubo
 */
@Component
public class AgentContainer {
	
	@Autowired
	private RestMainClient restMainClient;
	
	/**
	 * 初始化代理容器
	 */
	public void initAgentContainer(){
		AgentStorePath.createSubContainerDirectory();
	}
   /** 
    * 部署应用程序的方法
    */
   public int deployApplication() {
      return 0;
   }
   
   /** 
    * 启动应用程序
    */
   public int startupAppliction() {
      return 0;
   }
   
   /** 
    * 停止应用程序
    */
   public int shutdownAppliction() {
      return 0;
   }
   
   /** 
    * 删除应用程序
    */
   public int deleteAppliction() {
      return 0;
   }
}
