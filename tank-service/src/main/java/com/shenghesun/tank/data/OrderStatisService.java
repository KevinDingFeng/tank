package com.shenghesun.tank.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shenghesun.tank.data.entity.OrderStatis;
import com.shenghesun.tank.service.entity.model.StatisDataModel;

@Service
public class OrderStatisService {

	@Autowired
	private OrderStatisDao orderStatisDao;

	public OrderStatis findByProductTypeIdAndCoachId(Long productTypeId, Long coachId) {
		return orderStatisDao.findByProductTypeIdAndCoachId(productTypeId, coachId);
	}
	
	public List<StatisDataModel> findEntiresData(){
		return orderStatisDao.findGroupByProductType();
	}
	
	public StatisDataModel findDataByCoachId(Long coachId ) {
		return orderStatisDao.findByCoachId(coachId);
	}
	
}
