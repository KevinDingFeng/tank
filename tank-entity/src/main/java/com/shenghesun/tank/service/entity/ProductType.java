package com.shenghesun.tank.service.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shenghesun.tank.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 服务类型
 * 	分多个等级的服务类型
 * 	严格规定 code 和 parentCode 两个属性的规则：
 * 		level 等于 1 的类型：
 * 			code 等于 两位的正整数
 * 			parentCode 等于 1
 * 		level 等于 2 的类型：
 * 			code 等于 parentCode + 两位正整数，即四位正整数
 * 			parentCode 等于 其父级（ level 等于 1 ）的 code，即两位正整数
 * 		level 等于 3 的类型：
 * 			code 等于 parentCode + 两位正整数，即六位正整数
 * 			parentCode 等于 其父级（ level 等于 2 ）的 code，即四位正整数 
 * 		
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
	 * 	该属性在数据库中的规则是 int(11)
	 */
	private int code;
	
	/**
	 * 名称
	 */
	@Column(nullable = false, length = 64)
	private String name;
	
	/**
	 * 父级编号
	 * 	该属性在数据库中的规则是 int(11)
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
	
	/**
	 * 服务类型的图片/视频资源
	 */
	@JsonIgnore
	@OneToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "productType")
	private List<Resource> paths;
}
