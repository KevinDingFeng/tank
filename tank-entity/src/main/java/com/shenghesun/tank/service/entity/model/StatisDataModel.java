package com.shenghesun.tank.service.entity.model;

import lombok.Data;

@Data
public class StatisDataModel {

	
	private Long productTypeId;
	
	private Long counts;
	
	private Long hours;
	
	//一级服务码
	private int code;
	
	public StatisDataModel(Long productTypeId,Long counts,Long hours,int code) {
		this.productTypeId = productTypeId;
		this.counts = counts;
		this.hours = hours;
		this.code = code;
	}
	
	public StatisDataModel(Long counts,Long hours) {
		this.hours = hours;
		this.counts = counts;
	}
	
}
