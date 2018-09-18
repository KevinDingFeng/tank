package com.shenghesun.tank.order.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shenghesun.tank.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PlayOrderPayRecord extends BaseEntity {

	@JsonIgnore
	@ManyToOne(cascade = { javax.persistence.CascadeType.REFRESH }, fetch = javax.persistence.FetchType.LAZY)
	@JoinColumn(name = "play_order_id", nullable = false)
	private PlayOrder playOrder;
	
	/**
	 * 订单 id
	 */
	@Column(name = "play_order_id", insertable = false, updatable = false, nullable = false)
	private Long playOrderId;
	
	/**
	 * 支付状态
	 */
	@Column(nullable = false, length = 64)
	@Enumerated(EnumType.STRING)
	private PayStatus status = PayStatus.NotPay;
	
	public enum PayStatus{
		NotPay("未支付"),Complete("完成"),Fail("支付失败");
		
		private String text;
		
		public String text() {
			return this.text;
		}
		private PayStatus (String text) {
			this.text = text;
		}
	}
	/**
	 * 支付id
	 * 微信支付订单号
	 */
	@Column(nullable = true, length = 255)
	private String prepayId;
	/**
	 * 支付时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Timestamp payTime;
	/**
	 * 支付金额
	 */
	private BigDecimal amount;
	/**
	 * 交易的积分额
	 */
	private Long integralAmount;
	/**
	 * 支付途径
	 */
	@Column(nullable = false, length = 64)
	@Enumerated(EnumType.STRING)
	private PayChannel channel = PayChannel.Weixin;
	
	public enum PayChannel{
		Weixin("微信"),Alipay("支付宝"),Customer("客服");
		
		private String text;
		
		public String text() {
			return this.text;
		}
		private PayChannel (String text) {
			this.text = text;
		}
	}
	
	/**
	 * 支付方式
	 */
	@Column(nullable = false, length = 64)
	@Enumerated(EnumType.STRING)
	private PayType type = PayType.Cash;
	
	public enum PayType{
		Cash("现金"),Integral("积分"),Blend("现金和积分混合");

		private String text;
		
		public String text() {
			return this.text;
		}
		private PayType (String text) {
			this.text = text;
		}
	}
	/**
	 * 是否已删除
	 */
	private boolean removed = false;
}
