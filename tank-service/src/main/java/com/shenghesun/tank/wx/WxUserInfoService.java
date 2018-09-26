package com.shenghesun.tank.wx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.system.SysUserService;
import com.shenghesun.tank.system.entity.SysUser;
import com.shenghesun.tank.wx.entity.WxUserInfo;

@Service
public class WxUserInfoService {

	@Autowired
	private WxUserInfoDao wxUserInfoDao;
	@Autowired
	private SysUserService sysUserService;

	/**
	 * 根据传入的用户信息，先使用唯一键查询数据库，如果存在则做修改操作，不存在则做新建操作
	 * @param userInfoJson
	 * @return
	 */
	public WxUserInfo getOne(JSONObject userInfoJson) {
		/*
		 * { "country":"中国", "province":"河北", "city":"唐山",
		 * "openid":"oRLD0vzqxp5_1PdZuHMrUhyj1GuY", "sex":1, "nickname":"任强",
		 * "headimgurl":
		 * "http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKicT22uicmXjkSzyO0qFju3bia2L52RlTt6xGFKe3lTkWiac051zyKmvQekhXwffYcbumCLibRBbTuPgQ/132",
		 * "language":"zh_CN", "privilege":[] }
		 */
		WxUserInfo wxUserInfo = null;
		if (userInfoJson.containsKey("openid")) {
			String openId = userInfoJson.getString("openid");
			wxUserInfo = this.findByOpenId(openId);
		}
		if (wxUserInfo == null) {
			wxUserInfo = new WxUserInfo();
		}
		// SysUser user = sysUserService.findByOpenId(openId);
		// if(user == null) {
		//
		// }
		SysUser sysUser = sysUserService.findById(1L);
		wxUserInfo.setSysUser(sysUser);
		wxUserInfo.setSysUserId(1L);
		if (userInfoJson.containsKey("openid")) {
			wxUserInfo.setOpenId(userInfoJson.getString("openid"));
		}

		if (userInfoJson.containsKey("country")) {
			wxUserInfo.setCountry(userInfoJson.getString("country"));
		}

		if (userInfoJson.containsKey("province")) {
			wxUserInfo.setProvince(userInfoJson.getString("province"));
		}

		if (userInfoJson.containsKey("city")) {
			wxUserInfo.setCity(userInfoJson.getString("city"));
		}

		if (userInfoJson.containsKey("sex")) {
			wxUserInfo.setSex(userInfoJson.getInteger("sex"));
		}

		if (userInfoJson.containsKey("nickname")) {
			wxUserInfo.setNickName(userInfoJson.getString("nickname"));
		}

		if (userInfoJson.containsKey("headimgurl")) {
			wxUserInfo.setHeadImgUrl(userInfoJson.getString("headimgurl"));
		}

		if (userInfoJson.containsKey("language")) {
			wxUserInfo.setLanguage(userInfoJson.getString("language"));
		}

		if (userInfoJson.containsKey("privilege")) {
			// userInfoJson.get
			// wxUserInfo.setPrivilege(privilege);userInfoJson.getString("privilege");
		}
		return wxUserInfo;
	}

	public WxUserInfo findByOpenId(String openId) {
		return wxUserInfoDao.findByOpenId(openId);
	}

	public WxUserInfo save(WxUserInfo wxUserInfo) {
		return wxUserInfoDao.save(wxUserInfo);
	}

	public WxUserInfo findById(long id) {
		return wxUserInfoDao.findOne(id);
	}

}
