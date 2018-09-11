package com.shenghesun.tank.system.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shenghesun.tank.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 系统用户，平台用户
 * @author kevin
 *
 */
@Entity
@Table
@Data
@ToString(callSuper = true, exclude = { "sysPool" })
@EqualsAndHashCode(callSuper = true, exclude = { "sysPool" })
public class SysUser extends BaseEntity {

	/**
	 * 登录名 用户名
	 */
	@Column(nullable = false, length = 64)
	private String account;

	/**
	 * 是否激活
	 */
	private boolean active = true;

	/**
	 * 是否审核通过
	 */
	private boolean audited = true;

	/**
	 * 联系方式
	 */
	@Column(nullable = true, length = 32)
	private String cellphone;
	/**
	 * 联系方式是否通过了校验
	 */
	private boolean cellphoneVerified = false;

	/**
	 * 邮箱
	 */
	@Column(nullable = true, length = 255)
	private String email;
	/**
	 * 邮箱是否通过了校验
	 */
	private boolean emailVerified = false;

	/**
	 * 名称
	 */
	@Column(nullable = false, length = 64)
	private String name;
	/**
	 * 密码，加密加盐后的结果
	 */
	@JsonIgnore
	@Column(nullable = false, length = 200)
	private String password;

	@JsonIgnore
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "sys_pool_id", nullable = false)
	private SysPool sysPool;
	
	/**
	 * 池 id
	 */
	@Column(name = "sys_pool_id", insertable = false, updatable = false, nullable = false)
	private Long sysPoolId;

	/**
	 * 是否已删除
	 */
	private boolean removed = false;
	/**
	 * 盐值
	 */
	@JsonIgnore
	@Column(nullable = false, length = 20)
	private String salt;

	/**
	 * 系统标志
	 */
	@Column(nullable = true, length = 64)
	private String sysId;

	/**
	 * 微信用户唯一的 open id
	 */
	@Column(nullable = true, length = 255)
	private String openId;
	

	/**
	 * 身份，如教练、普通
	 * 	如果是Coach，必须存在对应的 Coach 实体记录。如果是 Common ，目前可能是平台用户，也可能是普通用户，需要通过角色或者其它属性进一步区别。
	 * 	如果后期确定 Coach 和 平台用户不可以是同一个人，则在这里添加一个 Platform，表明改 sysUser 是平台用户，而 Common 则只表示为普通用户。
	 */
	@Column(nullable = false, length = 40)
	@Enumerated(EnumType.STRING)
	private Position position = Position.Common;
	
	public enum Position {
		Common("普通"), Coach("大神");
		
		private String text;
		
		private Position(String text) {
			this.text = text;
		}
		
		public String getText() {
			return this.text;
		}
		
		public static List<Position> getAllPositionList(){
			return Arrays.asList(Position.values());
		}
		public static Map<String, String> getAllPositionMap(){
			Map<String, String> map = new HashMap<>();
			for(Position p : Position.values()) {
				map.put(p.name(), p.getText());
			}
			return map;
		}
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "sys_user_role", inverseJoinColumns = { @JoinColumn(name = "role_id") }, joinColumns = {
			@JoinColumn(name = "user_id") })
	private Set<SysRole> roles;
	
}
