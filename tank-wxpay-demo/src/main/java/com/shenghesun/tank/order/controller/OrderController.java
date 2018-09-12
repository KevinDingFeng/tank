package com.shenghesun.tank.order.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.http.utils.MyWechatParam;
import com.shenghesun.tank.utils.JsonUtils;
import com.shenghesun.tank.utils.RandomUtil;
import com.shenghesun.tank.wxpay.sdk.WXPay;
import com.shenghesun.tank.wxpay.sdk.WXPayUtil;
import com.shenghesun.tank.wxpay.sdk.impl.WXPayConfigImpl;

@RestController
@RequestMapping(value = "/order")
public class OrderController {
	
	@RequestMapping(value = "/unified", method = RequestMethod.GET)
	public JSONObject unified(HttpServletRequest request) throws Exception {
		
//		"o8ood1Eo3QzMT1JyxqXtE9Xv_QR0"
		
		
		Map<String,String> map = new HashMap<>();
        map.put("openId", "o8ood1Eo3QzMT1JyxqXtE9Xv_QR0");//用户标识openId
        map.put("remoteIp",request.getRemoteAddr());//请求Ip地址
        //调用统一下单service
//        Map<String,Object> resultMap = this.unifiedOrder(map);
        WXPay wxPay = new WXPay(new WXPayConfigImpl(), "https://wxpay.dazonghetong.com/wxpay/notify");
        Map<String,String> resultMap = wxPay.unifiedOrder(map);
        String returnCode = (String) resultMap.get("return_code");//通信标识
        String resultCode = (String) resultMap.get("result_code");//交易标识
        //只有当returnCode与resultCode均返回“success”，才代表微信支付统一下单成功
//        if (WeChatConstant.RETURN_SUCCESS.equals(resultCode)&&WeChatConstant.RETURN_SUCCESS.equals(returnCode)){
            String appId = (String) resultMap.get("appid");//微信公众号AppId
            String timeStamp = String.valueOf(System.currentTimeMillis()/1000);;//当前时间戳
            String prepayId = "prepay_id="+resultMap.get("prepay_id");//统一下单返回的预支付id
            String nonceStr = RandomUtil.randomString(20);//不长于32位的随机字符串
            SortedMap<String, String> signMap = new TreeMap<>();//自然升序map
            signMap.put("appId",appId);
            signMap.put("package",prepayId);
            signMap.put("timeStamp",timeStamp);
            signMap.put("nonceStr",nonceStr);
            signMap.put("signType","MD5");
            
            JSONObject json = new JSONObject();
            json.put("appId",appId);
            json.put("timeStamp",timeStamp);
            json.put("nonceStr",nonceStr);
            json.put("prepayId",prepayId);
            json.put("paySign",WXPayUtil.generateSignature(signMap, MyWechatParam.APP_SECRET));//获取签名
//        }else {
//            logger.error("微信统一下单失败,订单编号:"+order.getOrderNumber()+",失败原因:"+resultMap.get("err_code_des"));
//            return "redirect:/m/orderList";//支付下单失败，重定向至订单列表
//        }
		
		return JsonUtils.getSuccessJSONObject(null);
	}
//	public Map<String, String> sendPrepaidTransactionRequest(PayOrder order)
//			throws DocumentException {
//		Map<String, String> data = this.getUnifiedOrderData(order);
//		String xmlStr = this.getUnifiedOrderXmlStr(data);
//
//		PostMethod postMethod = new PostMethod(UNIFIED_ORDER);
//		postMethod.setRequestBody(xmlStr);
//		HttpMethodParams param = postMethod.getParams();
//		param.setContentCharset("UTF-8");
//		HttpClient client = new HttpClient();
//		try {
//			client.executeMethod(postMethod);
//			int responseCode = postMethod.getStatusCode();
//			if (200 == responseCode) {
//				Map<String, String> prepayData = new HashMap<>();
//				System.out.println("提交预支付申请，返回结果"
//						+ postMethod.getResponseBodyAsString());
//				// JSONObject json = new
//				// JSONObject(postMethod.getResponseBodyAsString());
//				String resXml = postMethod.getResponseBodyAsString();
//				Document document = DocumentHelper.parseText(resXml);
//				// String returnCode = json.getString("return_code");
//				Element root = document.getRootElement();
//				String returnCode = root.element("return_code").getText();
//				String returnMsg = root.element("return_msg").getText();
//				prepayData.put("return_code", returnCode);
//				prepayData.put("return_msg", returnMsg);
//				if ("SUCCESS".equals(returnCode)) {
//					// String returnMsg = json.getString("return_msg");
//					// String prepayId = json.getString("prepay_id");
//					// String resultCode = json.getString("result_code");
//					// String resultMsg = json.getString("err_code_des");
//					String resultCode = root.element("result_code").getText();
////					String resultMsg = root.element("err_code_des").getText();
//					if(root.element("err_code_des") != null){
//						String resultMsg = root.element("err_code_des").getText();
//						prepayData.put("err_code_des", resultMsg);
//					}
//					prepayData.put("result_code", resultCode);
////					prepayData.put("err_code_des", resultMsg);
//					if ("SUCCESS".equals(resultCode)) {
//						String prepayId = root.element("prepay_id").getText();
//						prepayData.put("prepay_id", prepayId);
//					}
//				} else {
//					System.out.println("提交预支付申请，失败：" + returnMsg);
//				}
//				return prepayData;
//			}
//		} catch (IOException e) {
//			System.out.println("统一下单请求失败");
//			e.printStackTrace();
//		}
//		return null;
//	}
//	public Map<String, Object> unifiedOrder(Map<String,Object> map){
//		/**
//		*微信支付统一下单
//		**/
//		    Map<String,Object> resultMap;
//		    try {
//		            WxPaySendData paySendData = new WxPaySendData();
//		            //构建微信支付请求参数集合
//		            paySendData.setAppId(WeChatConstant.APP_ID);
//		            paySendData.setAttach("微信订单支付:"+order.getOrderNumber());
//		            paySendData.setBody("商品描述");
//		            paySendData.setMchId(WeChatConfig.MCH_ID);
//		            paySendData.setNonceStr(WeChatUtils.getRandomStr(32));
//		            paySendData.setNotifyUrl(WeChatConfig.NOTIFY_URL);
//		            paySendData.setDeviceInfo("WEB");
//		            paySendData.setOutTradeNo(order.getOrderNumber());
//		            paySendData.setTotalFee(order.getSumFee());
//		            paySendData.setTradeType(WeChatConfig.TRADE_TYPE_JSAPI);
//		            paySendData.setSpBillCreateIp((String) map.get("remoteIp"));
//		            paySendData.setOpenId((String) map.get("openId"));
//		            //将参数拼成map,生产签名
//		            SortedMap<String,Object> params = buildParamMap(paySendData);
//		            paySendData.setSign(WeChatUtils.getSign(params));
//		            //将请求参数对象转换成xml
//		            String reqXml = WeChatUtils.sendDataToXml(paySendData);
//		            //发送请求
//		            byte[] xmlData = reqXml.getBytes();
//		            URL url = new URL(WeChatConfig.UNIFIED_ORDER_URL);
//		            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//		            urlConnection.setDoOutput(true);
//		            urlConnection.setDoInput(true);
//		            urlConnection.setUseCaches(false);
//		            urlConnection.setRequestProperty("Content_Type","text/xml");
//		            urlConnection.setRequestProperty("Content-length",String.valueOf(xmlData.length));
//		            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
//		            outputStream.write(xmlData);
//		            outputStream.flush();
//		            outputStream.close();
//		            resultMap = WeChatUtils.parseXml(urlConnection.getInputStream());
//		        } catch (Exception e) {
//		            throw new ServiceException("微信支付统一下单异常",e);
//		        }
//		        return resultMap;
//		}
//	}
}
