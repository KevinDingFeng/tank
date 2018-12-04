package com.shenghesun.tank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.tank.service.entity.Course;

@Service
public class CourseService {

	@Autowired
	private CourseDao courseDao;
	
	public List<Course> findAll(){
		return courseDao.findAll();
	}
	
//	public List<Course> findByProductTypeId(Long productTypeId){
//		return courseDao.findByProductTypeId(productTypeId);
//	}
	
	public List<Course> findByProductTypeIdAndCoachId(Long productTypeId,Long coachId){
		return courseDao.findByProductTypeIdAndCoachId(productTypeId, coachId);
	}
	
}
