package com.shenghesun.tank.order.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shenghesun.tank.base.entity.BaseEntity;
import com.shenghesun.tank.coach.entity.Coach;
import com.shenghesun.tank.service.entity.Product;
import com.shenghesun.tank.wx.entity.WxUserInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 玩家订单
 * 
 * @author kevin
 *
 */
@Entity
@Table
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PlayOrder extends BaseEntity {
	
//	@JsonIgnore
	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "wx_user_id", nullable = false)
	private WxUserInfo wxUser;
	
	/**
	 * 普通用户 id
	 */
	@Column(name = "wx_user_id", insertable = false, updatable = false, nullable = false)
	private Long wxUserId;

//	@JsonIgnore
	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "coach_id", nullable = false)
	private Coach coach;
	
	/**
	 * 下单预期大神 id
	 */
	@Column(name = "coach_id", insertable = false, updatable = false, nullable = false)
	private Long coachId;
	
//	@JsonIgnore
	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "executor_id", nullable = false)
	private Coach executor;
	
	/**
	 * 执行大神 id
	 */
	@Column(name = "executor_id", insertable = false, updatable = false, nullable = false)
	private Long executorId;
	
	@JsonIgnore
//	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	/**
	 * 服务 id
	 */
	@Column(name = "product_id", insertable = false, updatable = false, nullable = false)
	private Long productId;

	/**
	 * 操作类型
	 */
	@Column(nullable = false, length = 64)
	@Enumerated(EnumType.STRING)
	private OperationType operationType;
	
	public enum OperationType{
		Create("创建"),BaseUpdate("修改基础信息");
		
		private String text;
		
		public String text() {
			return this.text;
		}
		private OperationType(String text) {
			this.text = text;
		}
	}
	
	/**
	 * 期望开始时间
	 */
	@JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm")
	@Column(nullable=true)
	private Timestamp startTime;
	/**
	 * 期望结束时间
	 */
	@JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm")
	@Column(nullable=true)
	private Timestamp endTime;
	/**
	 * 订单号，主号，订单在操作过程中不做修改
	 */
	private String no;

	/**
	 * 订单号，副号，订单在操作过程中发生规律变化
	 */
	private String viceNo;
	/**
	 * 订单的序列号
	 * 	等于0时为原生订单，值最大的为最新的订单修改结果
	 */
	private int seq = 0;
	/**
	 * 是否为 主 订单 信息
	 * 	同一个 no 下，只有一个 playOrder 记录是 主订单，其余都是副订单。主订单用于列表展示和所有不需要展示修改订单历史记录的展示
	 */
	private boolean main = true;
	
	/**
	 * 总金额
	 */
	private BigDecimal totalFee;
	/**
	 * 服务的时长
	 */
	private int duration;
	
	/**
	 * 玩家确认完成时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Timestamp playCompleteTime;
	/**
	 * 大神确认完成时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Timestamp coachCompleteTime;

	/**
	 * 玩家取消时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Timestamp playCancelTime;

	private boolean removed = false;

	@Column(length = 1000)
	private String remark;

	/**
	 * 预留联系方式
	 */
	private String cellphone;
	/**
	 * 微信账号
	 */
	private String wxAccount;

	/**
	 * 状态
	 */
	@Column(nullable = false, length = 64)
	@Enumerated(EnumType.STRING)
	private PlayOrderStatus status = PlayOrderStatus.NotPay;
	
	public enum PlayOrderStatus{
		NotPay("未支付"),Operation("待执行"),Complete("完成"),Cancel("取消");
		
		private String text;
		
		public String text() {
			return this.text;
		}
		private PlayOrderStatus (String text) {
			this.text = text;
		}
	}

}
//private int hour;
//@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//private Date deadLine;//截止时间
//private String account;//用户名
//private String roleName;//角色
