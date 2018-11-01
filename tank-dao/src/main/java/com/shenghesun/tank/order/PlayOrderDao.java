package com.shenghesun.tank.order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.tank.order.entity.PlayOrder;
import com.shenghesun.tank.order.entity.PlayOrder.OrderType;

@Repository
public interface PlayOrderDao extends JpaRepository<PlayOrder, Long>, JpaSpecificationExecutor<PlayOrder> {

//	Page<PlayOrder> findByWxUserId(Long wxUserId, Pageable pageable);

	List<PlayOrder> findByNoAndMain(String no, boolean b);
	
	Page<PlayOrder> findByWxUserIdAndOrderType(Long wxUserId,OrderType orderType,Pageable pageable);

}
