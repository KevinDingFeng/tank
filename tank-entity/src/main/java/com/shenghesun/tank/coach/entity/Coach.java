package com.shenghesun.tank.coach.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shenghesun.tank.base.entity.BaseEntity;
import com.shenghesun.tank.service.entity.Course;
import com.shenghesun.tank.service.entity.Product;
import com.shenghesun.tank.system.entity.SysUser;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 大神、教练
 * @author kevin
 *
 */
@Entity
@Table
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Coach extends BaseEntity {

	@JsonIgnore
	@OneToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "sys_user_id", nullable = false)
	private SysUser sysUser;
	
	/**
	 * 系统用户 id
	 */
	@Column(name = "sys_user_id", insertable = false, updatable = false, nullable = false)
	private Long sysUserId;
	/**
	 * 头像
	 */
	@Column(nullable = true, length = 255)
	private String iconUrl;
	/**
	 * 名称，默认和 sysUser 中的 name 相同
	 */
	@Column(nullable = false, length = 255)
	private String name;
	/**
	 * 简介
	 */
	@Column(nullable = true, length = 255)
	private String introduction;

	private boolean active = true;
	private boolean removed = false;
	/**
	 * 大神的介绍图片
	 */
	@OneToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER, mappedBy = "coach")
	private List<CoachImage> imgs;
	
	/**
	 * 大神所拥有的课程
	 */
	@JsonIgnore
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "coaches")
	private Set<Course> courses;
	
	/**
	 * 大神提供的服务
	 */
	@JsonIgnore
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "coaches")
	private Set<Product> products;
}
//暂时不用
//private int orderCounts;
//private int orderHours;
//private int PraiseIndex;
//private boolean randomType;
////大神 可服务的类型--》使用大神和服务的关联解决这个关系
//private String summary;
