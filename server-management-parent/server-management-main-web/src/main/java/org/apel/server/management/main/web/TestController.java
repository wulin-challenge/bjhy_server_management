package org.apel.server.management.main.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

/**
 * 测试
 * @author wubo
 *
 */
@Controller
@RequestMapping("test")
public class TestController {
	
	
	

	@Autowired
	private RestTemplate restTemplate;
	
	
	@GetMapping("user/{id}")
	public User findById(@PathVariable("id")String id){
		return this.restTemplate.getForObject("http://microservice-provider-user/user/" + id, User.class);
	}
	
	@RequestMapping(value="index",method=RequestMethod.GET)
	public String index(){
		return "test_index";
	}
	
	@RequestMapping(value="subContainer",method=RequestMethod.GET)
	public String subContainer(){
		return "test_index";
	}

}
