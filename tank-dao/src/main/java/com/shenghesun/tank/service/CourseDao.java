package com.shenghesun.tank.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.tank.service.entity.Course;

@Repository
public interface CourseDao extends JpaRepository<Course, Long> , JpaSpecificationExecutor<Course>  {

	/**
	 * v2 版本
	 * 
	 */
//	List<Course> findByProductTypeId(Long productTypeId);
	
	List<Course> findByProductTypeIdAndCoachId(Long productTypeId,Long coachId);
	
}
