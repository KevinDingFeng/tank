package com.shenghesun.tank.service.entity;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shenghesun.tank.base.entity.BaseEntity;
import com.shenghesun.tank.coach.entity.Coach;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 真实报价
 * 	单价是根据服务的时长和默认报价的时长匹配得到的最终价格，所以在真实报价中不需要存在单价时长和时长的度量
 * @author kevin
 *
 */
@Entity
@Table
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class QuotedProduct extends BaseEntity {
	
//	@JsonIgnore
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "coach_id", nullable = false)
	private Coach coach;
	
	/**
	 * 大神 id
	 */
	@Column(name = "coach_id", insertable = false, updatable = false, nullable = false)
	private Long coachId;
	
//	@JsonIgnore
	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	/**
	 * 服务 id
	 */
	@Column(name = "product_id", insertable = false, updatable = false, nullable = false)
	private Long productId;
	
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
	 * 是否已删除
	 */
	private boolean removed = false;

}
