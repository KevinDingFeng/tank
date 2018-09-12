package com.shenghesun.tank.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.auth.service.WxAuthServiceImpl;
import com.shenghesun.tank.utils.JsonUtils;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
	
	@Autowired
	private WxAuthServiceImpl wxService;

	/**
	 * 大v完成授权，进入聚合页
	 * 
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/callback", method = { RequestMethod.GET, RequestMethod.POST })
	public JSONObject wxAuthCallback(HttpServletRequest req, HttpServletResponse res) {
		try {
			// 授权步骤结束后返回的参数包括 code 和 state
			String code = req.getParameter("code");
			String state = req.getParameter("state");
			// 根据 code 获取用户的 openId 和 accessToken
			JSONObject tokenMsg = wxService.getAuthToken(code);
			if (tokenMsg == null) {
				return JsonUtils.getFailJSONObject("获取 openId 出错");
			}
			String openId = tokenMsg.getString("openid");
			String token = tokenMsg.getString("access_token");
			// 保存 state 和 openId 的匹配记录
//			redisUtils.set(STATE_KEY + state, openId, 300L);
			// 根据 accessToke 和 opernId 获取用户的基础信息
//			JSONObject userInfoJson = wxService.getUserInfoJson(token, openId);
//			if (userInfoJson == null) {
//				return JsonUtils.getFailJSONObject("获取用户基础信息出错");
//			}
//			System.out.println(userInfoJson.toString());
			// 保存 userInfo 到数据库，使用 openId 和用户信息匹配 TODO 正式 使用时需要修改逻辑
			// redisUtils.set(OPEN_ID_KEY + openId, userInfoJson.toString(), 86400L);
//			SysUser sysUser = sysUserService.findByOpenId(openId);
//			if (sysUser == null) {
//				sysUser = wxService.getWXSysUser(userInfoJson);
//			}
//			sysUser = sysUserService.save(sysUser);
			// SecurityUtils.getSubject().login(new UsernamePasswordToken(sysUser.getName(),
			// sysUser.getPassword()));
//			WxUserInfo wxUserInfo = wxUserInfoService.getOne(userInfoJson);
//			wxUserInfo = wxUserInfoService.save(wxUserInfo);
			return JsonUtils.getSuccessJSONObject(openId);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtils.getFailJSONObject("后台获取用户信息发生错误");
		}
	}
}
