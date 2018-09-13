package com.shenghesun.tank.coach;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shenghesun.tank.coach.entity.Coach;

@Service
public class CoachService {

	@Autowired
	private CoachDao coachDao;

	public List<Coach> findAll() {
		return coachDao.findAll();
	}

	public Coach findBySpecial(boolean bool) {
		return coachDao.findBySpecial(bool);
	}

	public Coach findById(Long id) {
		return coachDao.findOne(id);
	}

	public List<Coach> findAll(Sort sort) {
		return coachDao.findAll(sort);
	}
}
