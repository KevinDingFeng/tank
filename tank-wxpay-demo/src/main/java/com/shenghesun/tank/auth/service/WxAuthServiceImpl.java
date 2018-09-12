package com.shenghesun.tank.auth.service;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.http.utils.HttpFactory;
import com.shenghesun.tank.http.utils.MyWechatParam;
import com.shenghesun.tank.http.utils.StringInstreamUtil;

@Service
public class WxAuthServiceImpl {
	
	
//	@Value("${wx.app.key}")
//	private String defaultAppId;
//
//	@Value("${wx.app.secret}")
//	private String defaultAppSecret;

//	public String getDefaultAppId() {
//		return defaultAppId;
//	}

//	public String getDefaultAppSecret() {
//		return defaultAppSecret;
//	}


	/**
	 * 第三步，刷新授权后的 accessToken 未开发
	 * 第四步，拉取用户信息；
	 * @param accessToken
	 * @param openId
	 * @return
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	public JSONObject getUserInfoJson(String accessToken, String openId) throws UnsupportedOperationException, IOException {
		HttpFactory http = new HttpFactory();
		HttpResponse response = http.getHttpResponse(
				http.httpApiPath + "sns/userinfo?access_token=" + accessToken
				+ "&openid=" + openId + "&lang=zh_CN");
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String str = StringInstreamUtil.getInputString(entity.getContent());
			JSONObject obj = JSONObject.parseObject(str);
			return obj;
		} else {
			System.out.println("没有返回实体");
			return null;
		}
	}
	/**
	 * 通过微信页面 确认授权 后的回调请求获取到 code ， 使用 code 获取授权 access_token 和 open_id 等信息
	 * 通过上述 access_token 可以获取用户的详细信息，暂时没必要获取，且 access_token 暂时不需要保存
	 * @param code
	 * @return
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	public JSONObject getAuthToken(String code) throws UnsupportedOperationException, IOException {
		HttpFactory http = new HttpFactory();
		HttpResponse response = http.getHttpResponse(
				http.httpApiPath + "sns/oauth2/access_token?appid=" + MyWechatParam.APP_ID //this.getDefaultAppId()  
				+ "&secret=" + MyWechatParam.APP_SECRET //this.getDefaultAppSecret()  
				+ "&code=" + code + "&grant_type=authorization_code");
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String str = StringInstreamUtil.getInputString(entity.getContent());
			JSONObject obj = JSONObject.parseObject(str);
			return obj;
		} else {
			System.out.println("没有返回实体");
			return null;
		}
	}

	
	public String getWXSysUser(JSONObject userInfoJson) {
/*		{
			"country":"中国",
			"province":"河北",
			"city":"唐山",
			"openid":"oRLD0vzqxp5_1PdZuHMrUhyj1GuY",
			"sex":1,
			"nickname":"任强",
			"headimgurl":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKicT22uicmXjkSzyO0qFju3bia2L52RlTt6xGFKe3lTkWiac051zyKmvQekhXwffYcbumCLibRBbTuPgQ/132",
			"language":"zh_CN",
			"privilege":[]
		}*/
//		System.out.println("创建真实的微信用户信息");
//		SysUser user = new SysUser();
//		String nickName = userInfoJson.getString("nickname");
//		System.out.println("nick_name" + nickName);
//		String openId = userInfoJson.getString("openid");
//		user.setAccount(openId);
//		user.setPassword(openId);
//		user.setSalt(openId);
//		//昵称可能超出可用长度，截取
//		nickName = nickName.length() > 64 ? nickName.substring(0, 64) : nickName;
//		user.setName(nickName);
////		user.setCellphone("123456789101");
//		Long sysPoolId = sysPoolService.getDefaultId();
//		user.setSysPoolId(sysPoolId);
//		user.setSysPool(sysPoolService.findById(sysPoolId));
//		user.setSysId(UserSysIdEnum.PLAYER.name());
////		user.setCellphoneVerified(true);
//		user.setOpenId(openId);
//		return user;
		return userInfoJson.getString("openid");
	}
}
