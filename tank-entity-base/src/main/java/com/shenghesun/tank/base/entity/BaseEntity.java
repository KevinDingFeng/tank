package com.shenghesun.tank.base.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shenghesun.tank.base.entity.listener.BaseEntityListener;

@MappedSuperclass
@EntityListeners(value = {BaseEntityListener.class})
public class BaseEntity {

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Timestamp getCreation() {
		return creation;
	}

	public void setCreation(Timestamp creation) {
		this.creation = creation;
	}

	public Timestamp getLastModified() {
		return lastModified;
	}

	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	
	@Version
	protected Long version = 0l;
	
	@JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@Column(updatable=false, nullable=false)
	protected Timestamp creation = new Timestamp(System.currentTimeMillis());
	
//	@Index(name="lastModified")
	@JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@Column(nullable=false)
	protected Timestamp lastModified = new Timestamp(System.currentTimeMillis());
	
}
