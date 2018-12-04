package com.shenghesun.tank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.tank.service.entity.Resource;
import com.shenghesun.tank.service.entity.Resource.ResorceTypes;

@Service
public class ResourceService {
	
	@Autowired
	private ResourceDao resourceDao;

	public List<Resource> findByProductTypeId(Long productTypeId,boolean removed){
		return resourceDao.findByProductTypeIdAndRemoved(productTypeId,removed);
	}
	
	public List<Resource> findByCoachId(Long coachId,boolean removed){
		return resourceDao.findByCoachIdAndRemoved(coachId, removed);
	}
	
	public List<Resource> findByCoachIdAndResorseType(Long coachId,ResorceTypes type,boolean removed){
		return resourceDao.findByCoachIdAndResourceTypeAndRemoved(coachId, type, removed);
	}
	
}
