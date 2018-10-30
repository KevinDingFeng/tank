package com.shenghesun.tank.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.tank.data.entity.OrderStatis;

@Service
public class OrderStatisService {

	@Autowired
	private OrderStatisDao orderStatisDao;

	public OrderStatis findByProductTypeIdAndCoachId(Long productTypeId, Long coachId) {
		return orderStatisDao.findByProductTypeIdAndCoachId(productTypeId, coachId);
	}
}
