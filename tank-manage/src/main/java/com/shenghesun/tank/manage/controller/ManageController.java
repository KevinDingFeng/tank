package com.shenghesun.tank.manage.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.sso.model.LoginInfo;
import com.shenghesun.tank.utils.JsonUtils;

@RestController
@RequestMapping(value = "/")
public class ManageController {

	@RequestMapping(method = RequestMethod.GET)
	public JSONObject index() {
		Subject subject = SecurityUtils.getSubject();
		
		if(subject.getPrincipal() != null) {
			LoginInfo info = (LoginInfo)subject.getPrincipal();
			return JsonUtils.getSuccessJSONObject(info);	
		}
		return JsonUtils.getFailJSONObject();
	}
}
