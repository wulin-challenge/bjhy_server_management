package org.apel.server.management.main.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apel.gaia.commons.i18n.Message;
import org.apel.gaia.commons.i18n.MessageUtil;
import org.apel.gaia.commons.jqgrid.QueryParams;
import org.apel.gaia.commons.pager.PageBean;
import org.apel.gaia.util.jqgrid.JqGridUtil;
import org.apel.server.management.main.container.domain.ApplicationAgent;
import org.apel.server.management.main.container.domain.enums.AgentStatus;
import org.apel.server.management.main.container.service.ApplicationAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/applicationAgent")
public class ApplicationAgentController {
	
	private final static String INDEX_URL = "applicationAgent_index";
	
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
		applicationAgentService.pageQuery(pageBean);
		return pageBean;
	}
	
	//得到代理状态
	@RequestMapping(value="getAgentStatus",method=RequestMethod.GET)
	public @ResponseBody List<Map<String,Object>> getAgentStatus(){
		List<Map<String,Object>> agentStatusList = new ArrayList<Map<String,Object>>();
		AgentStatus[] values = AgentStatus.values();
		for (AgentStatus agentStatus : values) {
			Map<String,Object> status = new HashMap<String,Object>();
			status.put("label", agentStatus.getCode());
			status.put("value",agentStatus.getName());
			agentStatusList.add(status);
		}
		return agentStatusList;
	}
	
	//新增
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Message create(ApplicationAgent applicationAgent){
		applicationAgentService.save(applicationAgent);
		return MessageUtil.message("applicationAgent.create.success");
	}
	
	//更新
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public @ResponseBody Message create(@PathVariable String id, ApplicationAgent applicationAgent){
		applicationAgent.setId(id);
		applicationAgentService.update(applicationAgent);
		return MessageUtil.message("applicationAgent.update.success");
	}
	
	//查看
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody ApplicationAgent view(@PathVariable String id){
		return applicationAgentService.findById(id);
	}
	
	//删除
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Message delete(@PathVariable String id){
		applicationAgentService.deleteById(id);
		return MessageUtil.message("applicationAgent.delete.success");
	}
	
	//批量删除
	@RequestMapping(method = RequestMethod.DELETE)
	public @ResponseBody Message batchDelete(@RequestParam("ids[]") String[] ids){
		applicationAgentService.deleteById(ids);
		return MessageUtil.message("applicationAgent.delete.success");
	}
	
	//查询全部数据
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public @ResponseBody List<ApplicationAgent> getAll(){
		return applicationAgentService.findAll(new Sort(Direction.DESC, "createDate"));
	}
	
	
	
}
