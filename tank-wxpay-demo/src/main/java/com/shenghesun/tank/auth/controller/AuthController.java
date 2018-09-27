package com.shenghesun.tank.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.auth.service.WxAuthServiceImpl;
import com.shenghesun.tank.utils.EmojiStringUtils;
import com.shenghesun.tank.utils.JsonUtils;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
	
	@Autowired
	private WxAuthServiceImpl wxService;

	/**
	 * 普通测试获取 access token 和 open id ，该 open id 是公众号的 唯一用户标识，和小程序的没有关系
	 * 页面直接跳转到后台弯沉授权，code 是直接由微信服务平台反馈给后端接口，没有经过前端转发；
	 * 	如果前端先获取到微信服务器转发来的 code 然后请求后台，不使用这个接口 
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
			// 根据 accessToke 和 opernId 获取用户的基础信息
//			JSONObject userInfoJson = wxService.getUserInfoJson(token, openId);
//			if (userInfoJson == null) {
//				return JsonUtils.getFailJSONObject("获取用户基础信息出错");
//			}
//			System.out.println(userInfoJson.toString());
//			SysUser sysUser = sysUserService.findByOpenId(openId);
//			if (sysUser == null) {
//				sysUser = wxService.getWXSysUser(userInfoJson);
//			}
//			sysUser = sysUserService.save(sysUser);
			// SecurityUtils.getSubject().login(new UsernamePasswordToken(sysUser.getName(),
			// sysUser.getPassword()));
//			WxUserInfo wxUserInfo = wxUserInfoService.getOne(userInfoJson);
//			wxUserInfo = wxUserInfoService.save(wxUserInfo);
			JSONObject json = new JSONObject();
			json.put("openId", openId);
			json.put("token", token);
			json.put("state", state);
			return JsonUtils.getSuccessJSONObject(json);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtils.getFailJSONObject("后台获取用户信息发生错误");
		}
	}
	/**
	 * 普通测试获取 access token 和 open id ，该 open id 是公众号的 唯一用户标识，和小程序的没有关系
	 * 页面直接跳转到后台弯沉授权，code 是直接由微信服务平台反馈给后端接口，没有经过前端转发；
	 * 	如果前端先获取到微信服务器转发来的 code 然后请求后台，不使用这个接口 
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/callbackinfo", method = { RequestMethod.GET, RequestMethod.POST })
	public JSONObject wxAuthCallbackInfo(HttpServletRequest req, HttpServletResponse res) {
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
			// 根据 accessToke 和 opernId 获取用户的基础信息
			JSONObject userInfoJson = wxService.getUserInfoJson(token, openId);
			if (userInfoJson == null) {
				return JsonUtils.getFailJSONObject("获取用户基础信息出错");
			}
			System.out.println(userInfoJson.toString());
			String nickName = userInfoJson.getString("nickname");
			System.out.println(nickName);
			System.out.println(EmojiStringUtils.hasEmoji(nickName));
			System.out.println(EmojiStringUtils.replaceEmoji(nickName));
//			SysUser sysUser = sysUserService.findByOpenId(openId);
//			if (sysUser == null) {
//				sysUser = wxService.getWXSysUser(userInfoJson);
//			}
//			sysUser = sysUserService.save(sysUser);
			// SecurityUtils.getSubject().login(new UsernamePasswordToken(sysUser.getName(),
			// sysUser.getPassword()));
//			WxUserInfo wxUserInfo = wxUserInfoService.getOne(userInfoJson);
//			wxUserInfo = wxUserInfoService.save(wxUserInfo);
			JSONObject json = new JSONObject();
			json.put("openId", openId);
			json.put("token", token);
			json.put("state", state);
			json.put("nickName", nickName);
			json.put("nickNameReplace", EmojiStringUtils.replaceEmoji(nickName));
			return JsonUtils.getSuccessJSONObject(json);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtils.getFailJSONObject("后台获取用户信息发生错误");
		}
	}
}
