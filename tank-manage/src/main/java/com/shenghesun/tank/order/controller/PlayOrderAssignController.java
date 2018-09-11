package com.shenghesun.tank.order.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.coach.CoachService;
import com.shenghesun.tank.coach.entity.Coach;
import com.shenghesun.tank.order.PlayOrderService;
import com.shenghesun.tank.order.entity.PlayOrder;
import com.shenghesun.tank.service.QuotedProductService;
import com.shenghesun.tank.service.entity.QuotedProduct;
import com.shenghesun.tank.utils.JsonUtils;

@RestController
@RequestMapping(value = "/play_order/assign")
public class PlayOrderAssignController {
	
	@Autowired
	private PlayOrderService playOrderService;
	@Autowired
	private CoachService coachService;
	@Autowired
	private QuotedProductService quotedProductService;
	
	/**
	 * 获取派单列表信息
	 * @param pageNum
	 * @param keyword
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JSONObject list(
		@RequestParam(value = "pageNum", required = false) Integer pageNum,
		@RequestParam(value = "keyword", required = false) String keyword) {
		//权限校验 TODO 判断当前登录用户，是否拥有 分派 订单的权限
		
		Pageable pageable = this.getListPageable(pageNum);
		Page<PlayOrder> page = playOrderService.findBySpecification(this.getSpecification(keyword), pageable);
		
		JSONObject json = new JSONObject();
		json.put("page", page);
		json.put("keyword", keyword);
		return JsonUtils.getSuccessJSONObject(json);
	}
	private Pageable getListPageable(Integer pageNum) {
		Sort sort = new Sort(Direction.DESC, "creation");
		Pageable pageable = new PageRequest(pageNum == null ? 0 : pageNum, 10, sort);
		return pageable;
	}
	private Specification<PlayOrder> getSpecification(String keyword) {

		return new Specification<PlayOrder>() {
			@Override
			public Predicate toPredicate(Root<PlayOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				
				root.fetch("wxUser", JoinType.LEFT);
				root.fetch("coach", JoinType.LEFT);
				root.fetch("product", JoinType.LEFT);
				
				list.add(cb.equal(root.get("removed"), false));
				list.add(cb.equal(root.get("main"), true));
				
				if (StringUtils.isNotBlank(keyword)) {
					String t = "%" + keyword.trim() + "%";
					Predicate p1 = cb.like(root.get("no").as(String.class), t);
					Predicate p2 = cb.like(root.get("cellphone").as(String.class), t);
					list.add(cb.or(p1, p2));
				}
				
//				Predicate p3 = cb.equal(root.get("status").as(PlayOrderStatus.class), PlayOrderStatus.Operation);
//				Predicate p4 = cb.equal(root.get("status").as(PlayOrderStatus.class), PlayOrderStatus.Complete);
//				list.add(cb.or(p3, p4));
				
				
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}
		};
	}
	/**
	 * 根据订单的id 获取可分派的大神列表
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/coaches", method = RequestMethod.GET)
	public JSONObject coaches(@RequestParam(value = "id") Long id) {
		//权限校验 TODO 判断当前登录用户，是否拥有 分派 订单的权限
		PlayOrder playOrder = playOrderService.findById(id);
		List<QuotedProduct> qpList = quotedProductService.findByProductId(playOrder.getProductId());
		List<Coach> coaches = this.getCoaches(qpList);
//		Product product = playOrder.getProduct();
//		Set<Coach> coaches = product.getCoaches();//可为 订单中的产品 提供服务的大神信息
		//需要给出备用的大神信息，这里有两种方式，第一种是通过 上面的 coaches 获取到已有的大神 ids ，然后使用 not in 查询coach 信息。
		//考虑到 not in 关键词在 数据库中的执行效率不高，这里使用第二种方式，直接查出所有可用的coach信息，然后使用代码过滤掉 coaches中的 ids
		List<Coach> all = coachService.findAll();
		List<Coach> spareCoach = this.getSpareCoach(all, coaches);
		JSONObject json = new JSONObject();
		json.put("coaches", coaches);
		json.put("spareCoach", spareCoach);
		return JsonUtils.getSuccessJSONObject(json);
	}
	private List<Coach> getCoaches(List<QuotedProduct> qpList) {
		if(qpList != null && qpList.size() > 0) {
			List<Coach> list = new ArrayList<>();
			for(QuotedProduct qp : qpList) {
				list.add(qp.getCoach());
			}
			return list;
		}
		return null;
	}
	private List<Coach> getSpareCoach(List<Coach> all, List<Coach> coaches) {
		if(all == null || all.size() < 1) {
			return null;
		}
		if(coaches == null || coaches.size() < 1) {
			return all;
		}
		List<Coach> list = new ArrayList<>();
		for(Coach c : all) {
			boolean notHas = true;
//			Iterator<Coach> its = coaches.iterator();
//			while(its.hasNext()) {
			for(Coach one : coaches) {
//				Coach one = its.next();
				if(c.getId().longValue() == one.getId().longValue()) {
					notHas = false;
					break ;
				}
			}
			if(notHas) {
				list.add(c);
			}
		}
		return list;
	}
}
