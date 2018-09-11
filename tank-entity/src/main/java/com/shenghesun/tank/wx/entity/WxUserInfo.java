package com.shenghesun.tank.wx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shenghesun.tank.base.entity.BaseEntity;
import com.shenghesun.tank.system.entity.SysUser;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 普通用户
 * @author kevin
 *
 */
@Entity
@Table
@Data
@ToString(callSuper = true, exclude = { "sysUser" })
@EqualsAndHashCode(callSuper = true, exclude = { "sysUser" })
public class WxUserInfo extends BaseEntity {

	@JsonIgnore
	@ManyToOne(cascade = { javax.persistence.CascadeType.REFRESH }, fetch = javax.persistence.FetchType.LAZY)
	@JoinColumn(name = "sys_user_id", nullable = false)
	private SysUser sysUser;
	
	/**
	 * 用户 id
	 */
	@Column(name = "sys_user_id", insertable = false, updatable = false, nullable = false)
	private Long sysUserId;
	
	/**
	 * 微信用户唯一的 open id
	 */
	@Column(nullable = false, length = 255)
	private String openId;
	
	@Column(nullable = true, length = 64)
	private String country;
	@Column(nullable = true, length = 64)
	private String province;
	@Column(nullable = true, length = 64)
	private String city;
	private int sex = 1;//1 标识男性
	@Column(nullable = true, length = 255)
	private String nickName;
	@Column(nullable = true, length = 255)
	private String headImgUrl;
	@Column(nullable = true, length = 64)
	private String language;
	@Column(nullable = true, length = 255)
	private String privilege;
	
	/**
	 * 是否已删除
	 */
	private boolean removed = false;
}
