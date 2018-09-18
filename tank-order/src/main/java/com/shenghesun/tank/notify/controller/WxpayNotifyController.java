package com.shenghesun.tank.notify.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

import com.shenghesun.tank.order.PlayOrderService;
import com.shenghesun.tank.order.entity.PlayOrder;
import com.shenghesun.tank.order.entity.PlayOrder.PlayOrderStatus;

@RestController
@RequestMapping(value = "/wxpay")
public class WxpayNotifyController {

	@Autowired
	private PlayOrderService playOrderService;

	@RequestMapping(value = "/notify", method = { RequestMethod.GET, RequestMethod.POST })
	public String wxnotify(HttpServletRequest request, HttpServletResponse response)
			throws IOException, DocumentException {

		Element root = this.getRootElement(request);
		String returnCode = root.element("return_code").getText();
		Element e = root.element("return_msg");
		String returnMsg = "success";// TODO
		if (e != null) {
			returnMsg = e.getText();
		}
		if ("SUCCESS".equals(returnCode.toUpperCase())) {
			if("SUCCESS".equals(root.element("result_code").getText().toUpperCase())) {
				String no = root.element("out_trade_no").getText();
				PlayOrder order = playOrderService.findMainByNo(no);
				if(order != null && order.getId() != null) {
					order.setStatus(PlayOrderStatus.Complete);
					playOrderService.save(order);
//					PlayOrderPayRecord record = //TODO 支付记录
				}
				//TODO 支付通知校验
//				transaction_id
//				time_end
//				total_fee
//				appid
//				mch_id
//				sign
				
			}else {
				System.out.println("回调出现错误" + root.element("err_code_des").getText());	
			}
		} else {
			System.out.println("回调出现错误" + returnMsg);
		}

		Document document = DocumentHelper.createDocument();
		Element xmlE = document.addElement("xml");
		xmlE.addElement("return_code").setText(returnCode);
		xmlE.addElement("return_msg").setText(returnMsg);
		System.out.println("支付通知接收信息结束，返回 xml : " + document.asXML());
		return document.asXML();
	}
	
	private Element getRootElement(HttpServletRequest request) throws IOException, DocumentException {
		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		String xmlStr = sb.toString();
		System.out.println(xmlStr);
		Document document = DocumentHelper.parseText(xmlStr);
		Element root = document.getRootElement();
		return root;
	}
}
