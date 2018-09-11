package com.shenghesun.tank.service.entity;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shenghesun.tank.base.entity.BaseEntity;
import com.shenghesun.tank.coach.entity.Coach;
import com.shenghesun.tank.service.entity.model.DurationType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
/**
 * 服务、产品
 * @author kevin
 *
 */
@Entity
@Table
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity {
	
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
	 * 容纳人数
	 */
	private int capacity;
	
	/**
	 * 开始时间 暂时不使用
	 */
	@JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm")
	@Column(nullable=true)
	private Timestamp startTime;
	/**
	 * 结束时间 暂时不使用
	 */
	@JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm")
	@Column(nullable=true)
	private Timestamp endTime;
	
	/**
	 * 服务和可执行大神的关联关系
	 */
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "product_coach_rel", inverseJoinColumns = { @JoinColumn(name = "product_id") }, joinColumns = {
			@JoinColumn(name = "coach_id") })
	private Set<Coach> coaches;
	
	/**
	 * 是否为活动，默认false
	 */
	private boolean isActivity = false;
	/**
	 * 服务的时长
	 */
	private int duration;
	/**
	 * 服务的时长单位
	 */
	@Column(nullable = false, length = 64)
	@Enumerated(EnumType.STRING)
	private DurationType durationType;
	/**
	 * 服务的内容
	 */
	private String content;
	/**
	 * 是否已删除，是否已下架
	 */
	private boolean removed = false;
	
}
//暂时不需要
//private BigDecimal price;//单位价格
//private String code;//??
//private String productCode;//产品编号
//private String productName;//产品名称
//private String supClassCode;//父级产品编号
//private String supClassName;//父级产品名称
//@Column(unique = true)
//private String subClassCode;//子级产品编号
//private String subClassName;//子级产品名称
