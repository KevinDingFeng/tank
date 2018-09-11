package com.shenghesun.tank.integral.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shenghesun.tank.base.entity.BaseEntity;
import com.shenghesun.tank.order.entity.PlayOrderPayRecord;
import com.shenghesun.tank.wx.entity.WxUserInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 积分账单
 * 	包括获取积分的来源、积分的消费记录
 * @author kevin
 *
 */
@Entity
@Table
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class IntegralBill extends BaseEntity {
	
	@JsonIgnore
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "wx_user_id", nullable = false)
	private WxUserInfo wxUser;
	
	/**
	 * 普通用户 id
	 */
	@Column(name = "wx_user_id", insertable = false, updatable = false, nullable = false)
	private Long wxUserId;
	
	/**
	 * 类型
	 */
	@Column(nullable = false, length = 64)
	@Enumerated(EnumType.STRING)
	private TradeType type = TradeType.Pay;
	
	public enum TradeType{
		Pay("支付"),Reward("支付奖励");
		
		private String text;
		
		public String text() {
			return this.text;
		}
		private TradeType (String text) {
			this.text = text;
		}
	}
	/**
	 * 交易积分数量
	 */
	private Long amount;
	
	/**
	 * 序列号，从0开始，逐个加大，最大的balance 值为用户的积分余额
	 */
	private int seq;
	/**
	 * 积分余额
	 */
	private Long balance;
	
	@JsonIgnore
	@ManyToOne(cascade = { javax.persistence.CascadeType.REFRESH }, fetch = javax.persistence.FetchType.LAZY)
	@JoinColumn(name = "pay_record_id", nullable = true)
	private PlayOrderPayRecord payRecord;
	
	/**
	 * 支付 id
	 * 	只有 type 为 Pay 时，有效
	 */
	@Column(name = "pay_record_id", insertable = false, updatable = false, nullable = true)
	private Long payRecordId;
	/**
	 * 是否已删除
	 */
	private boolean removed = false;
}
