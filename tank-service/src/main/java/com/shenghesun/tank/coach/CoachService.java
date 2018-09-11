package com.shenghesun.tank.coach;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.tank.coach.entity.Coach;

@Service
public class CoachService {

	@Autowired
	private CoachDao coachDao;

	public List<Coach> findAll() {
		return coachDao.findAll();
	}
}
