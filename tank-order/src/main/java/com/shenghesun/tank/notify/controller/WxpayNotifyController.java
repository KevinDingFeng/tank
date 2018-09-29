package com.shenghesun.tank.notify.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shenghesun.tank.order.PlayOrderPayRecordService;
import com.shenghesun.tank.order.PlayOrderService;
import com.shenghesun.tank.order.entity.PlayOrder;
import com.shenghesun.tank.order.entity.PlayOrder.PlayOrderStatus;
import com.shenghesun.tank.order.entity.PlayOrderPayRecord;
import com.shenghesun.tank.wxpay.sdk.WXPayConfig;
import com.shenghesun.tank.wxpay.sdk.WXPayUtil;
import com.shenghesun.tank.wxpay.sdk.impl.WXPayConfigImpl;

@RestController
@RequestMapping(value = "/wxpay")
public class WxpayNotifyController {

	@Autowired
	private PlayOrderService playOrderService;
	@Autowired
	private PlayOrderPayRecordService recordService;

	@RequestMapping(value = "/notify", method = { RequestMethod.GET, RequestMethod.POST })
	public String wxnotify(HttpServletRequest request, HttpServletResponse response)
			throws IOException, DocumentException {
		String xmlStr = this.getXMLStr(request);
		// 支付通知校验
		try {
			if (this.checkSign(xmlStr)) {
				Element root = this.getRootElement(xmlStr);
				String returnCode = root.element("return_code").getText();
				if ("SUCCESS".equals(returnCode.toUpperCase())) {
					if ("SUCCESS".equals(root.element("result_code").getText().toUpperCase())) {
						String no = root.element("out_trade_no").getText();
						PlayOrder order = playOrderService.findMainByNo(no);
						if (order != null && order.getId() != null) {
//							order.setStatus(PlayOrderStatus.Complete);
							order.setStatus(PlayOrderStatus.Operation);
							order = playOrderService.save(order);
							// TODO 支付记录 多条订单对应一条支付记录
//							PlayOrderPayRecord record = recordService.findByPlayOrderId(order.getId());
							String prepayId = this.formatString(root.element("transaction_id"));
							PlayOrderPayRecord record = recordService.findByPrepayId(prepayId);
							if (record == null || record.getId() == null) {
								record = recordService.getDefaultRecord(order);
								record.setAmount(this.formatBigDecimal(root.element("total_fee")));
								record.setPayTime(this.formatTimestamp(root.element("time_end")));
								record.setPrepayId(prepayId);
								recordService.save(record);
							}
						}
					} else {
						System.out.println("回调出现错误" + root.element("err_code_des"));
					}
				} else {
					System.out.println("回调出现错误" + root.element("return_msg"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("校验时发生异常");
		}
		Document document = DocumentHelper.createDocument();
		Element xmlE = document.addElement("xml");
		xmlE.addElement("return_code").setText("SUCCESS");
		xmlE.addElement("return_msg").setText("OK");
		System.out.println("支付通知接收信息结束，返回 xml : " + document.asXML());
		return document.asXML();
	}

	private boolean checkSign(String xmlStr) throws Exception {
		Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
		WXPayConfig conf = new WXPayConfigImpl();
		return WXPayUtil.isSignatureValid(map, conf.getKey());
	}

	private String formatString(Element element) {
		if (element != null) {
			String str = element.getText();
			return str;
		}
		return "";
	}

	private Timestamp formatTimestamp(Element element) {
		if (element != null) {
			String str = element.getText();
			Timestamp t = new Timestamp(Long.valueOf(str) * 1000);
			return t;
		}
		return new Timestamp(System.currentTimeMillis());
	}

	private BigDecimal formatBigDecimal(Element element) {
		if (element != null) {
			String str = element.getText();
			BigDecimal b = new BigDecimal(str);
			return b;
		}
		return BigDecimal.ZERO;
	}

	private String getXMLStr(HttpServletRequest request) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	private Element getRootElement(String xmlStr) throws IOException, DocumentException {
		Document document = DocumentHelper.parseText(xmlStr);
		Element root = document.getRootElement();
		return root;
	}
}
