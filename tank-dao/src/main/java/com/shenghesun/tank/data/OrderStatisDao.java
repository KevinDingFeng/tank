package com.shenghesun.tank.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shenghesun.tank.data.entity.OrderStatis;
import com.shenghesun.tank.service.entity.model.StatisDataModel;

@Repository
public interface OrderStatisDao extends JpaRepository<OrderStatis, Long>, JpaSpecificationExecutor<OrderStatis> {

	OrderStatis findByProductTypeIdAndCoachId(Long productTypeId, Long coachId);
	
	@Query("select new com.shenghesun.tank.service.entity.model.StatisDataModel(os.productTypeId , sum(os.counts) ,sum(os.hours),0) from OrderStatis os group by os.productType")
	List<StatisDataModel> findGroupByProductType();
	
	@Query("select new com.shenghesun.tank.service.entity.model.StatisDataModel(sum(os.counts) ,sum(os.hours)) from OrderStatis os where os.coachId = ?1")
	StatisDataModel findByCoachId(Long coachId);

}
