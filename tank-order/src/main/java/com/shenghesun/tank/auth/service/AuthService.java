package com.shenghesun.tank.auth.service;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;

public interface AuthService {

	/**
	 * 第三步，刷新授权后的 accessToken 未开发
	 * 第四步，拉取用户信息；
	 * @param accessToken
	 * @param openId
	 * @return
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	public JSONObject getUserInfoJson(String accessToken, String openId) throws UnsupportedOperationException, IOException;
	/**
	 * 通过微信页面 确认授权 后的回调请求获取到 code ， 使用 code 获取授权 access_token 和 open_id 等信息
	 * 通过上述 access_token 可以获取用户的详细信息，暂时没必要获取，且 access_token 暂时不需要保存
	 * @param code
	 * @return
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	public JSONObject getAuthToken(String code) throws UnsupportedOperationException, IOException;
}
