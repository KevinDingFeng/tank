package com.shenghesun.tank.wx.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.sso.model.LoginInfo;
import com.shenghesun.tank.wx.WxUserInfoService;
import com.shenghesun.tank.wx.entity.WxUserInfo;

@RestController
@RequestMapping(value = "/wx_user")
public class WxUserController {
	
	@Autowired
	private WxUserInfoService wxUserService;

	/**
	 * 获取当前授权的 普通用户信息 ，根据 header中的open id 等
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public JSONObject info(HttpServletRequest request) {
		LoginInfo info = (LoginInfo) SecurityUtils.getSubject().getPrincipal();
		WxUserInfo wxUser = wxUserService.findById(info.getWxUserId());
		JSONObject json = new JSONObject();
		json.put("wxUser", wxUser);
		return json;
	}
	
}
