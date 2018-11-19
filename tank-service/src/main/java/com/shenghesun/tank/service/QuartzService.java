package com.shenghesun.tank.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shenghesun.tank.order.PlayOrderService;
import com.shenghesun.tank.order.entity.PlayOrder;
import com.shenghesun.tank.order.entity.PlayOrder.PlayOrderStatus;

@Component
public class QuartzService {
	
	@Autowired
	private PlayOrderService playOrderService;

	/**
	 * 每小时自动更新订单 完成状态
	 */
	@Scheduled(cron = "0 0 */1 * * ?")
	public void autoUpdatePlayOrder() {
		List<PlayOrder> list = playOrderService.findByStatus(this.getSpecification());
		List<PlayOrder> orders = new ArrayList<>();
		if (list.size() > 0) {
			for (PlayOrder playOrder : list) {
				if(( System.currentTimeMillis() - playOrder.getLastModified().getTime() ) > 24*60*60*1000) {
					playOrder.setStatus(PlayOrderStatus.Complete);
					playOrder.setPlayCompleteTime(new Timestamp(System.currentTimeMillis()));
					orders.add(playOrder);
				}
			}
			System.out.println("自动完成"+orders.size()+"个订单！");
			playOrderService.saveAll(orders);
		}
		
	}
	
	public Specification<PlayOrder> getSpecification(){
		return new Specification<PlayOrder>() {
			public Predicate toPredicate(Root<PlayOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				predicate.getExpressions().add(cb.equal(root.get("status"), PlayOrderStatus.ToBeConfirmed));
				return predicate;
			}
		};
	}
	
}
