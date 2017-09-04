package org.apel.server.management.main.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apel.gaia.commons.i18n.Message;
import org.apel.gaia.commons.i18n.MessageUtil;
import org.apel.gaia.commons.jqgrid.QueryParams;
import org.apel.gaia.commons.pager.PageBean;
import org.apel.gaia.util.jqgrid.JqGridUtil;
import org.apel.server.management.main.container.domain.AppAndAgent;
import org.apel.server.management.main.container.domain.Application;
import org.apel.server.management.main.container.domain.ApplicationAgent;
import org.apel.server.management.main.container.domain.ApplicationVersion;
import org.apel.server.management.main.container.domain.enums.StartStatus;
import org.apel.server.management.main.container.service.AppAndAgentService;
import org.apel.server.management.main.container.service.ApplicationAgentService;
import org.apel.server.management.main.container.service.ApplicationService;
import org.apel.server.management.main.container.service.ApplicationVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

@Controller
@RequestMapping("/appAndAgent")
public class AppAndAgentController {
	
	private final static String INDEX_URL = "appAndAgent_index";
	
	@Autowired
	private AppAndAgentService appAndAgentService;
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private ApplicationVersionService applicationVersionService;
	
	@Autowired
	private ApplicationAgentService applicationAgentService;
	
	
	//首页
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(){
		return INDEX_URL;
	}
	
	//列表查询
	@RequestMapping
	public @ResponseBody PageBean list(QueryParams queryParams){
		JqGridUtil.getPageBean(queryParams);
		PageBean pageBean = JqGridUtil.getPageBean(queryParams);
		appAndAgentService.pageQuery(pageBean);
		return pageBean;
	}
	
	//得到启动状态
	@RequestMapping(value="getStartStatus")
	public @ResponseBody List<Map<String,Object>> getStartStatus(){
		List<Map<String,Object>> startStatusList = new ArrayList<Map<String,Object>>();
		StartStatus[] values = StartStatus.values();
		for (StartStatus startStatus : values) {
			Map<String,Object> status = new HashMap<String,Object>();
			status.put("label", startStatus.getCode());
			status.put("value", startStatus.getName());
			startStatusList.add(status);
		}
		return startStatusList;
	}
	
//	StartStatus
	@RequestMapping(value="getAllApplication",method=RequestMethod.GET)
	public @ResponseBody List<Map<String,Object>> getAllApplication(){
		
		List<Application> findAll = applicationService.findAll(new Sort(new Order(Direction.ASC,"createDate")));
		List<Map<String,Object>> applicationData = new ArrayList<Map<String,Object>>();
		
		for (Application application : findAll) {
			Map<String,Object> app = new HashMap<String,Object>();
			app.put("label", application.getName());
			app.put("value", application.getId());
			applicationData.add(app);
		}
		return applicationData;
	}
	
	@RequestMapping(value="getAllAgent",method=RequestMethod.GET)
	public @ResponseBody List<Map<String,Object>> getAllAgent(){
		
		List<ApplicationAgent> findAll = applicationAgentService.findAll(new Sort(new Order(Direction.ASC,"createDate")));
		List<Map<String,Object>> applicationData = new ArrayList<Map<String,Object>>();
		
		for (ApplicationAgent agent : findAll) {
			Map<String,Object> app = new HashMap<String,Object>();
			app.put("label", agent.getName());
			app.put("value", agent.getId());
			applicationData.add(app);
		}
		return applicationData;
	}
	
	@RequestMapping(value="getAllAppVersion",method=RequestMethod.GET)
	public @ResponseBody List<Map<String,Object>> getAllAppVersion(String applicationId){
		
		List<ApplicationVersion> findAll = applicationVersionService.findApplicationVersion(applicationId);
		List<Map<String,Object>> applicationVersionData = new ArrayList<Map<String,Object>>();
		
		for (ApplicationVersion applicationVersion : findAll) {
			Map<String,Object> app = new HashMap<String,Object>();
			app.put("label", "版本"+applicationVersion.getAppVersion());
			app.put("value", applicationVersion.getAppVersion());
			applicationVersionData.add(app);
		}
		return applicationVersionData;
	}
	
	//新增
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Message create(AppAndAgent appAndAgent){
		ApplicationAgent agent = applicationAgentService.findById(appAndAgent.getApplictionAgent().getId());
		Application application = applicationService.findById(appAndAgent.getAppliction().getId());
		
		appAndAgent.setAppliction(application);
		appAndAgent.setApplictionAgent(agent);
		
		appAndAgent.setStartStatus(StartStatus.notDeploy.getCode());
		appAndAgent.setStartStatusName(StartStatus.notDeploy.getName());
		appAndAgentService.save(appAndAgent);
		return MessageUtil.message("appAndAgent.create.success");
	}
	
	//更新
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public @ResponseBody Message create(@PathVariable String id, AppAndAgent appAndAgent){
		AppAndAgent appAndAgent2 = appAndAgentService.findById(id);
		if(StartStatus.running.getCode().equals(appAndAgent2.getStartStatus())){
			return MessageUtil.message("appAndAgent.update.running");
		}
		
		if(StartStatus.starting.getCode().equals(appAndAgent2.getStartStatus())){
			return MessageUtil.message("appAndAgent.update.running");
		}
		
		
		appAndAgent.setId(id);
		
		//改变当前应用状态
		if(appAndAgent2.getAppliction().getId() != appAndAgent.getAppliction().getId()
		|| appAndAgent2.getApplicationVersion() != appAndAgent.getApplicationVersion()
		|| appAndAgent2.getApplictionAgent().getId() != appAndAgent.getApplictionAgent().getId()){
			appAndAgent.setStartStatus(StartStatus.notDeploy.getCode());
			appAndAgent.setStartStatusName(StartStatus.notDeploy.getName());
		}
		
		appAndAgentService.update(appAndAgent);
		return MessageUtil.message("appAndAgent.update.success");
	}
	
	//部署
	@RequestMapping(value = "deploy/{id}", method = RequestMethod.GET)
	public @ResponseBody Message deploy(@PathVariable String id){
		AppAndAgent appAndAgent = appAndAgentService.findById(id);
		if(StartStatus.running.getCode().equals(appAndAgent.getStartStatus())){
			return MessageUtil.message("appAndAgent.deploy.running");
		}
		
		if(StartStatus.starting.getCode().equals(appAndAgent.getStartStatus())){
			return MessageUtil.message("appAndAgent.deploy.running");
		}
		
		appAndAgentService.deploy(id);
		return MessageUtil.message("appAndAgent.deploy.success");
	}
	
	//升级
	@RequestMapping(value = "upgrade/{id}", method = RequestMethod.GET)
	public @ResponseBody Message upgrade(@PathVariable String id){
		appAndAgentService.upgrade(id);
		return MessageUtil.message("appAndAgent.upgrade.success");
	}
		
	//启动
	@RequestMapping(value = "startup/{id}", method = RequestMethod.GET)
	public @ResponseBody Message startup(@PathVariable String id){
		AppAndAgent appAndAgent = appAndAgentService.findById(id);
		if(StartStatus.running.getCode().equals(appAndAgent.getStartStatus())){
			return MessageUtil.message("appAndAgent.startup.running");
		}
		
		if(StartStatus.starting.getCode().equals(appAndAgent.getStartStatus())){
			return MessageUtil.message("appAndAgent.startup.running");
		}
		
		appAndAgentService.startup(id);
		return MessageUtil.message("appAndAgent.startup.success");
	}
	
	//停止
	@RequestMapping(value = "suspend/{id}", method = RequestMethod.GET)
	public @ResponseBody Message suspend(@PathVariable String id){
		appAndAgentService.suspend(id);
		return MessageUtil.message("appAndAgent.suspend.success");
	}
	
	//查看
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody AppAndAgent view(@PathVariable String id){
		return appAndAgentService.findById(id);
	}
	
	//删除
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Message delete(@PathVariable String id){
		AppAndAgent appAndAgent = appAndAgentService.findById(id);
		if(StartStatus.running.getCode().equals(appAndAgent.getStartStatus())){
			return MessageUtil.message("appAndAgent.delete.running");
		}
		
		if(StartStatus.starting.getCode().equals(appAndAgent.getStartStatus())){
			return MessageUtil.message("appAndAgent.delete.running");
		}
		
		appAndAgentService.deleteById(id);
		return MessageUtil.message("appAndAgent.delete.success");
	}
	
	//批量删除
	@RequestMapping(method = RequestMethod.DELETE)
	public @ResponseBody Message batchDelete(@RequestParam("ids[]") String[] ids){
		for (String id : ids) {
			AppAndAgent appAndAgent = appAndAgentService.findById(id);
			
			if(appAndAgent.getApplicationPid() == null || appAndAgent.getApplicationPid() == 0){
				appAndAgentService.deleteById(ids);
			}
			
			if(StartStatus.running.getCode().equals(appAndAgent.getStartStatus())){
				return MessageUtil.message("appAndAgent.delete.running");
			}
			
			if(StartStatus.starting.getCode().equals(appAndAgent.getStartStatus())){
				return MessageUtil.message("appAndAgent.delete.running");
			}
		}
		appAndAgentService.deleteById(ids);
		return MessageUtil.message("appAndAgent.delete.success");
	}
	
	//查询全部数据
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public @ResponseBody List<AppAndAgent> getAll(){
		return appAndAgentService.findAll(new Sort(Direction.DESC, "createDate"));
	}
	
	
	
}
