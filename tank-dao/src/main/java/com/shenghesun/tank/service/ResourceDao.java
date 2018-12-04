package com.shenghesun.tank.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.tank.service.entity.Resource;
import com.shenghesun.tank.service.entity.Resource.ResorceTypes;

@Repository
public interface ResourceDao extends JpaRepository<Resource, Long>, JpaSpecificationExecutor<Resource> {
	
	
	List<Resource> findByProductTypeIdAndRemoved(Long productTypeId,boolean removed);
	
	
	List<Resource> findByCoachIdAndRemoved(Long coachId,boolean removed);
	
	
	List<Resource> findByCoachIdAndResourceTypeAndRemoved(Long coachId,ResorceTypes type,boolean removed);

}
