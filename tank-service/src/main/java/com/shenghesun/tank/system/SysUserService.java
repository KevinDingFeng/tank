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

	public SysUser findById(Long id) {
		return userDao.findOne(id);
	}

	public SysUser findByOpenId(String openId) {
		return userDao.findByOpenId(openId);
	}

	public SysUser save(SysUser entity) {
		return userDao.save(entity);
	}
	
}
