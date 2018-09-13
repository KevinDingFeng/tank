package com.shenghesun.tank.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shenghesun.tank.system.entity.SysPool;

@Service
public class SysPoolService {

	@Autowired
	private SysPoolDao sysPoolDao;
	
	
	public List<SysPool> findAll(){
		return sysPoolDao.findAll();
	}


	public SysPool findById(Long id) {
		return sysPoolDao.findOne(id);
	}


	@Value("${sys.pool.default.id}")
	private String defaultId;
	public Long getDefaultId() {
		return Long.parseLong(defaultId);
	}
}
