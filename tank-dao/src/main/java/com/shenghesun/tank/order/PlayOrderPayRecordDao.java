package com.shenghesun.tank.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.tank.order.entity.PlayOrderPayRecord;

@Repository
public interface PlayOrderPayRecordDao extends JpaRepository<PlayOrderPayRecord, Long>, JpaSpecificationExecutor<PlayOrderPayRecord> {

//	PlayOrderPayRecord findByPlayOrderId(Long playOrderId);

	PlayOrderPayRecord findByPrepayId(String prepayId);
	
	List<PlayOrderPayRecord> findByPlayOrderIdOrderByLastModifiedDesc(Long playOrderId);

}
