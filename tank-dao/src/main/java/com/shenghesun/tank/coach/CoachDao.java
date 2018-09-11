package com.shenghesun.tank.coach;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.tank.coach.entity.Coach;

@Repository
public interface CoachDao extends JpaRepository<Coach, Long>, JpaSpecificationExecutor<Coach> {

}
