package com.shenghesun.tank.http.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpFactory {

	private HttpClient getHttpClient(){
		@SuppressWarnings({ "deprecation", "resource" })
		HttpClient httpclient = new DefaultHttpClient();
		return httpclient;
	}
	
	public String httpApiPath = "https://api.weixin.qq.com/";
	public String httpOpenPath = "https://open.weixin.qq.com/";

	private HttpGet getHttpGet(String httpUrlAndParam){
		HttpGet httpget = new HttpGet(
				httpUrlAndParam);
		
		return httpget;
	}
	private HttpPost getHttpPost(String httpUrlAndParam, HttpEntity entity){
		HttpPost httpPost = new HttpPost(
				httpUrlAndParam);
//		if(entity != null){
//			httpPost.setEntity(entity); 
//		}
		StringEntity e;
		try {
			e = new StringEntity("{\"button\":[{\"type\":\"view\",\"name\":\"pureget\",\"url\":\"http://dsp.paiyue.com/\"}]}");
			httpPost.setEntity(e); 
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return httpPost;
	}
	
	public HttpResponse getHttpResponse(String httpUrlAndParam) {
		try {
			return this.getHttpClient().execute(
					this.getHttpGet(httpUrlAndParam));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public HttpResponse getHttpPostResponse(String httpUrlAndParam, HttpEntity entity) {
		try {
			return this.getHttpClient().execute(
					this.getHttpPost(httpUrlAndParam, entity));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
