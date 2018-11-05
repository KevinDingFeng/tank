package com.shenghesun.tank.service.entity.model;

/**
 * 时长单位 枚举类型
 * @author kevin
 *
 */

public enum DurationType {
	Hour("时"),
	Day("天"),
	Week("周"),
	Month("月"),
	TenThousandExp("1万经验"),
	HundredThousandExp("10万经验"),
	HundredFiftyThousandExp("15万经验"),
	Unit("台"),
	Time("次"),
	Site("场"),
	Human("人"),
	SevenHour("7小时"),
	NoLimitation("不限");
	
	private String text;
	
	public String getText() {
		return this.text;
	}
	private DurationType(String text) {
		this.text = text;
	}
}
