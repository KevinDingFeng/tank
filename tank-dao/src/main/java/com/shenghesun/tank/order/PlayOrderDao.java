package com.shenghesun.tank.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.shenghesun.tank.order.entity.PlayOrder;

@Repository
public interface PlayOrderDao extends JpaRepository<PlayOrder, Long>, JpaSpecificationExecutor<PlayOrder> {

}
