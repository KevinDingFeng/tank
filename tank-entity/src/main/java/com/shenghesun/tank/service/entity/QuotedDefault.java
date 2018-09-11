package com.shenghesun.tank.service.entity;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shenghesun.tank.base.entity.BaseEntity;
import com.shenghesun.tank.coach.entity.Coach;
import com.shenghesun.tank.service.entity.model.DurationType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 默认报价
 * @author kevin
 *
 */
@Entity
@Table
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class QuotedDefault extends BaseEntity {
	
	@JsonIgnore
	@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "coach_id", nullable = false)
	private Coach coach;
	
	/**
	 * 大神 id
	 */
	@Column(name = "coach_id", insertable = false, updatable = false, nullable = false)
	private Long coachId;
	
	@JsonIgnore
//	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_type_id", nullable = false)
	private ProductType productType;

	/**
	 * 服务类型 id
	 */
	@Column(name = "product_type_id", insertable = false, updatable = false, nullable = false)
	private Long productTypeId;
	
	/**
	 * 活动打折力度
	 * 	小数，乘以 100% 为百分比；针对总价
	 */
	private BigDecimal activityRatio;
	/**
	 * 附加费比例
	 * 	小数，乘以 100% 为百分比；针对单价
	 */
	private BigDecimal surchargeRatio;
	
	/**
	 * 附加费
	 * 	具体值，和 surchargeRatio 二选一
	 */
	private BigDecimal surcharge;
	/**	
	 * 单价
	 */
	private BigDecimal price;
	/**
	 * 单位时长
	 */
	private int duration;
	/**
	 * 单位时长度量
	 */
	@Column(nullable = false, length = 64)
	@Enumerated(EnumType.STRING)
	private DurationType durationType;	
	/**
	 * 是否已删除
	 */
	private boolean removed = false;
}
