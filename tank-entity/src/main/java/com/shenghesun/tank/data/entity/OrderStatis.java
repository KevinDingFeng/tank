package com.shenghesun.tank.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shenghesun.tank.base.entity.BaseEntity;
import com.shenghesun.tank.coach.entity.Coach;
import com.shenghesun.tank.service.entity.ProductType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 订单数据统计
 * 	针对每个大神对应某类服务类型的订单数量和订单服务时长
 * 目前这个实体只是分类型统计总数，没有对每天或者更详细的时间进行分类。level 表示当前记录是统计的第几级的类型
 * @author 程任强
 *
 */

@Entity
@Table
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class OrderStatis extends BaseEntity {

	@JsonIgnore
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "coach_id", nullable = false)
	private Coach coach;
	
	/**
	 * 大神 id
	 */
	@Column(name = "coach_id", insertable = false, updatable = false, nullable = false)
	private Long coachId;
	
	@JsonIgnore
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "product_type_id", nullable = false)
	private ProductType productType;

	/**
	 * 服务类型 id
	 */
	@Column(name = "product_type_id", insertable = false, updatable = false, nullable = false)
	private Long productTypeId;
	
	/**
	 * 等级
	 * 	数据统计对应的等级
	 */
	private int level;
	
	/**
	 * 接单数量
	 */
	private int counts;
	
	/**
	 * 服务时长
	 */
	private int hours;
}
