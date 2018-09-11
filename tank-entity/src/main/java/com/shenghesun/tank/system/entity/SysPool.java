package com.shenghesun.tank.system.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.shenghesun.tank.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 池，作为系统用户的上一层级
 * @author kevin
 *
 */
@Entity
@Table
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysPool extends BaseEntity {

	/**
	 * 是否为模拟数据
	 */
	private boolean demo = false;

	/**
	 * 是否为内部使用
	 */
	private boolean internal = false;
	/**
	 * 等级
	 */
	private int level;
	
	/**
	 * 名称
	 */
	@Column(nullable = false, length = 255)
	private String name;
	
	/**
	 * 备注
	 */
	@Column(nullable = false, length = 255)
	private String remark;
	/**
	 * 是否已删除
	 */
	private boolean removed = false;
	/**
	 * 系统标志
	 */
	@Column(nullable = true, length = 64)
	private String sysId;
}
