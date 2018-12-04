package com.shenghesun.tank.service.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shenghesun.tank.base.entity.BaseEntity;
import com.shenghesun.tank.coach.entity.Coach;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Table
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Resource extends BaseEntity {
	
	
//	@JsonIgnore
	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_type_id", nullable = false)
	private ProductType productType;

	/**
	 * 服务类型
	 */
	@Column(name = "product_type_id", insertable = false, updatable = false, nullable = false)
	private Long productTypeId;
	
	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
	@ManyToOne(cascade = { CascadeType.REFRESH },fetch = FetchType.LAZY)
	@JoinColumn(name = "coach_id" ,nullable = false)
	private Coach coach;
	
	/**
	 * 大神
	 */
	@Column(name = "coach_id", insertable = false, updatable = false, nullable = false)
	private Long coachId;
	
	
	/**
	 * 路径
	 */
	@Column(nullable = true,length = 64)
	private String path;
	
	/**
	 * 资源类型
	 */
	@Column(nullable = false, length = 64)
	@Enumerated(EnumType.STRING)
	private ResorceTypes resourceType;
	
	public enum ResorceTypes{
		Img("图片"),Vid("视频");
		
		private String text;
		
		public String getText() {
			return text;
		}
		
		private ResorceTypes(String text) {
			this.text = text;
		}
		
	}
	
	/**
	 * 是否删除
	 */
	private boolean removed = false; 
	
}
