package com.shenghesun.tank.service.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shenghesun.tank.base.entity.BaseEntity;
import com.shenghesun.tank.coach.entity.Coach;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 课程
 * 	针对某一种特定的服务类型，比如：教学类型的服务需要以课程为基础
 * @author kevin
 *
 */
@Entity
@Table
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Course extends BaseEntity {
	
	/**
	 * 名称
	 */
	@Column(nullable = false, length = 255)
	private String name;
	/**
	 * 介绍
	 */
	@Column(nullable = false, length = 255)
	private String introduction;
	

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
	 * 课程和可执行大神的关联关系
	 */
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "course_coach_rel", inverseJoinColumns = { @JoinColumn(name = "course_id") }, joinColumns = {
			@JoinColumn(name = "coach_id") })
	private Set<Coach> coaches;
	
	/**
	 * 是否已激活
	 */
	private boolean active = true;
	
	private boolean removed = false;
}
