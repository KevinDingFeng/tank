package com.shenghesun.tank.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.tank.data.entity.OrderStatis;

@Repository
public interface OrderStatisDao extends JpaRepository<OrderStatis, Long>, JpaSpecificationExecutor<OrderStatis> {

	OrderStatis findByProductTypeIdAndCoachId(Long productTypeId, Long coachId);

}
