package com.shenghesun.tank.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.tank.system.entity.SysUser;

@Service
public class SysUserService {
	
	@Autowired
	private SysUserDao userDao;

	public SysUser findByAccount(String account) {
		return userDao.findByAccount(account);
	}

	
}
