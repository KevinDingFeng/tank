package com.shenghesun.tank.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.http.utils.MyWechatParam;
import com.shenghesun.tank.http.utils.MyWechatSignatureUtil;
import com.shenghesun.tank.system.SysUserService;
import com.shenghesun.tank.system.entity.SysUser;
import com.shenghesun.tank.utils.JsonUtils;
import com.shenghesun.tank.wx.WxUserInfoService;
import com.shenghesun.tank.wx.auth.service.WxAuthServiceImpl;
import com.shenghesun.tank.wx.entity.WxUserInfo;

@RestController
@RequestMapping("/auth")
public class AuthController {

	/**
	 * ***************重点*************** 用于微信公共平台验证接口使用， 80 端口
	 * 	公众号平台-基础配置，服务器配置的域名设置
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/myWechat", method = { RequestMethod.GET, RequestMethod.POST })
	public String wechat(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		System.out.println("signature = " + signature + "; timestamp = " + timestamp + "; nonce = " + nonce
				+ "; echostr = " + echostr + ";");
		return MyWechatSignatureUtil.checkSignature(signature, timestamp, nonce, MyWechatParam.TOKEN) ? echostr
				: "false";
	}
//
//	public static final String STATE_KEY = "state_";
//	public static final String OPEN_ID_KEY = "open_id_";
	@Autowired
	private WxAuthServiceImpl wxService;
	// @Autowired
	// private DefaultAuthServiceImpl wxService;
//	@Autowired
//	private RedisUtils redisUtils;

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private WxUserInfoService wxUserInfoService;


	@RequestMapping(value = "/callback_by_code", method = { RequestMethod.GET, RequestMethod.POST })
	public JSONObject wxAuthCallbackByCode(HttpServletRequest req, HttpServletResponse res,
			@RequestParam("code") String code, @RequestParam("state") String state) {
		try {
			// 授权步骤结束后返回的参数包括 code 和 state
			// String state = req.getParameter("state");
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
			SysUser sysUser = sysUserService.findByOpenId(openId);
//			SysUser sysUser = sysUserService.findById(1L);
			if (sysUser == null) {
				sysUser = wxService.getWXSysUser(userInfoJson);
			}
			sysUser = sysUserService.save(sysUser);
			WxUserInfo wxUserInfo = wxUserInfoService.getOne(userInfoJson);
//			WxUserInfo wxUserInfo = wxUserInfoService.findById(1L);
			wxUserInfo = wxUserInfoService.save(wxUserInfo);
			
			SecurityUtils.getSubject().login(new UsernamePasswordToken(sysUser.getAccount(), sysUser.getPassword()));
			JSONObject json = new JSONObject();
			json.put("wxUser", wxUserInfo);
			return JsonUtils.getSuccessJSONObject(json);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtils.getFailJSONObject("后台获取用户信息发生错误");
		}
	}

//	@RequestMapping(value = "/user_info", method = RequestMethod.GET)
//	public JSONObject getUserInfo(HttpServletRequest req, HttpServletResponse res,
//			@RequestParam(value = "state") String state) {
//		if (redisUtils.exists(STATE_KEY + state)) {
//			String openId = redisUtils.get(STATE_KEY + state);
//			SysUser sysUser = sysUserService.findByOpenId(this.format(openId));
//			if (sysUser != null) {
//				return JsonUtils.getSuccessJSONObject(sysUser);
//			} else {
//				System.out.println("openId 不存在 " + openId);
//			}
//		} else {
//			System.out.println("state 不存在 " + state);
//		}
//		return JsonUtils.getFailJSONObject();
//	}

//	@RequestMapping(value = "/binding_cellphone_no/{cellphoneNo}", method = RequestMethod.POST)
//	public JSONObject alterCellphoneNo(@PathVariable String cellphoneNo, @RequestParam String code) {
//		if (sysUserService.alterCellphoneNo(cellphoneNo, code)) {
//			return JsonUtils.getSuccessJSONObject();
//		}
//		return JsonUtils.getFailJSONObject("校验失败");
//	}

//	@RequestMapping("/check_cellphone/{cellphoneNo}")
//	public JSONObject checkCellphone(@PathVariable String cellphoneNo) {
//		return JsonUtils.getSuccessJSONObject();
//		try {
//			if (sysUserService.checkCellphone(cellphoneNo)) {
//				return JsonUtils.getSuccessJSONObject(true);
//			}
//			return JsonUtils.getSuccessJSONObject(false);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return JsonUtils.getFailJSONObject(e.getMessage());
//		}
//	}
}
