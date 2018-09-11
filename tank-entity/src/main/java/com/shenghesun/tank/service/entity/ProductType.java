package com.shenghesun.tank.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.shenghesun.tank.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 服务类型
 * 	分多个等级的服务类型
 * @author kevin
 *
 */
@Entity
@Table
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProductType extends BaseEntity {
	
	/**
	 * 编号
	 */
	private int code;
	
	/**
	 * 名称
	 */
	@Column(nullable = false, length = 64)
	private String name;
	
	/**
	 * 父级编号
	 */
	private int parentCode;
	
	/**
	 * 等级
	 */
	private int level;
	
	/**
	 * 备注
	 */
	@Column(nullable = true, length = 255)
	private String remark;
	/**
	 * 是否已删除
	 */
	private boolean removed = false;
}
