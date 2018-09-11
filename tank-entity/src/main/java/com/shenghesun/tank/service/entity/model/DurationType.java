package com.shenghesun.tank.service.entity.model;

/**
 * 时长单位 枚举类型
 * @author kevin
 *
 */

public enum DurationType {
	Hour("时"),Day("天"),Week("周"),Month("月"),NoLimitation("不限");
	
	private String text;
	
	public String getText() {
		return this.text;
	}
	private DurationType(String text) {
		this.text = text;
	}
}
