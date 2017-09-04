package org.apel.server.management.main.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apel.gaia.commons.i18n.Message;
import org.apel.gaia.commons.i18n.MessageUtil;
import org.apel.gaia.commons.jqgrid.QueryParams;
import org.apel.gaia.commons.pager.PageBean;
import org.apel.gaia.util.jqgrid.JqGridUtil;
import org.apel.server.management.main.container.domain.Application;
import org.apel.server.management.main.container.domain.ApplicationVersion;
import org.apel.server.management.main.container.domain.enums.ServerType;
import org.apel.server.management.main.container.service.ApplicationService;
import org.apel.server.management.main.container.service.ApplicationVersionService;
import org.apel.server.management.main.container.util.MainStorePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/application")
public class ApplicationController {
	
	private final static String INDEX_URL = "application_index";
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private ApplicationVersionService applicationVersionService;
	
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
		applicationService.pageQuery(pageBean);
		return pageBean;
	}
	
	//得到服务类型
	@RequestMapping(value="getServerType",method=RequestMethod.GET)
	public @ResponseBody List<Map<String,Object>> getServerType(){
		List<Map<String,Object>> serverTypeList = new ArrayList<Map<String,Object>>();
		Map<String,Object> web = new HashMap<String,Object>();
		web.put("value", ServerType.web.getCode());
		web.put("label",ServerType.web.getName());
		
		Map<String,Object> local = new HashMap<String,Object>();
		local.put("value", ServerType.local.getCode());
		local.put("label",ServerType.local.getName());
		
		serverTypeList.add(web);
		serverTypeList.add(local);
		return serverTypeList;
	}
	
	//新增
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Message create(Application application){
		ServerType[] values = ServerType.values();
		for (ServerType serverType : values) {
			if(application.getServerType() != null && serverType.getCode().equals(application.getServerType())){
				application.setServerTypeName(serverType.getName());
				break;
			}
		}
		applicationService.save(application);
		return MessageUtil.message("application.create.success");
	}
	
	//更新
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public @ResponseBody Message create(@PathVariable String id, Application application){
		ServerType[] values = ServerType.values();
		for (ServerType serverType : values) {
			if(application.getServerType() != null && serverType.getCode().equals(application.getServerType())){
				application.setServerTypeName(serverType.getName());
				break;
			}
		}
		
		application.setId(id);
		applicationService.update(application);
		return MessageUtil.message("application.update.success");
	}
	
	//上传
	@RequestMapping(value = "upload/{id}", method = RequestMethod.POST)
	public @ResponseBody Message upload(@PathVariable String id,MultipartFile uploadFile){
		
		String storeFile = null;
		try {
			storeFile = MainStorePath.storeFile(uploadFile.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Application application = applicationService.findById(id);
		ApplicationVersion applicationVersion = new ApplicationVersion();
		applicationVersion.setAppVersion(applicationVersionService.getVersionMax(id));
		applicationVersion.setApplication(application);
		applicationVersion.setStoreDate(new Date());
		applicationVersion.setStorePath(storeFile);
		applicationService.upload(application, applicationVersion);
		return MessageUtil.message("application.upload.success");
	}
	
	//查看
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody Application view(@PathVariable String id){
		return applicationService.findById(id);
	}
	
	//删除
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Message delete(@PathVariable String id){
		applicationService.deleteById(id);
		return MessageUtil.message("application.delete.success");
	}
	
	//批量删除
	@RequestMapping(method = RequestMethod.DELETE)
	public @ResponseBody Message batchDelete(@RequestParam("ids[]") String[] ids){
		applicationService.deleteById(ids);
		return MessageUtil.message("application.delete.success");
	}
	
	//查询全部数据
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public @ResponseBody List<Application> getAll(){
		return applicationService.findAll(new Sort(Direction.DESC, "createDate"));
	}
	
	
	
}
