package com.shenghesun.tank.sso.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoginInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7708543734504343497L;
	
	private Long id;
	private String name;
	private String account;
	
	private Long wxUserId;
	
	public LoginInfo() {
		
	}
	public LoginInfo(Long id, String name, String account) {
		this.id = id;
		this.name = name;
		this.account = account;
	}

}
