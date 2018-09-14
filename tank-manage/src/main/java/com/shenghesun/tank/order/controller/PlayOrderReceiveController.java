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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.order.PlayOrderService;
import com.shenghesun.tank.order.entity.PlayOrder;
import com.shenghesun.tank.order.entity.PlayOrder.PlayOrderStatus;
import com.shenghesun.tank.utils.JsonUtils;

@Controller
@RequestMapping(value = "/play_order/receive")
public class PlayOrderReceiveController {

	@Autowired
	private PlayOrderService playOrderService;
	/**
	 * 获取接单列表信息
	 * @param pageNum
	 * @param keyword
	 * @return
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
	public String list(
		@RequestParam(value = "pageNum", required = false) Integer pageNum,
		@RequestParam(value = "keyword", required = false) String keyword, Model model) {
		//TODO 获取当前登录用户，判断当前用户是否有接单的权限
		
		Long loginUserId = 2L;
		
		pageNum = pageNum == null ? 0 : pageNum;
		Pageable pageable = this.getListPageable(pageNum);
		Page<PlayOrder> page = playOrderService.findBySpecification(this.getSpecification(loginUserId, keyword), pageable);

		model.addAttribute("page", page);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageNum", pageNum);
		return "order_receive/list";
	}
	private Pageable getListPageable(Integer pageNum) {
		Sort sort = new Sort(Direction.DESC, "creation");
		Pageable pageable = new PageRequest(pageNum, 20, sort);
		return pageable;
	}
	private Specification<PlayOrder> getSpecification(Long loginUserId, String keyword) {

		return new Specification<PlayOrder>() {
			@Override
			public Predicate toPredicate(Root<PlayOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				
				root.fetch("wxUser", JoinType.LEFT);
				root.fetch("coach", JoinType.LEFT);
				root.fetch("product", JoinType.LEFT);
				
				list.add(cb.equal(root.get("removed"), false));
				list.add(cb.equal(root.get("main"), true));//因为订单实体设计时，加入了操作记录，所以每个订单编号是由多条组成，这里只找出其中最新的一条
				//根据订单的执行大神 对应的 用户 id，匹配对应的订单
				list.add(cb.equal(root.get("executor").get("sysUserId").as(Long.class), loginUserId));

				if (StringUtils.isNotBlank(keyword)) {
					String t = "%" + keyword.trim() + "%";
					Predicate p1 = cb.like(root.get("no").as(String.class), t);
					Predicate p2 = cb.like(root.get("cellphone").as(String.class), t);
					list.add(cb.or(p1, p2));
				}
				
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}
		};
	}
	@RequestMapping(value = "/exe/{id}", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject exeComplete(
			@PathVariable("id") Long id) {
		// TODO 校验当前登录用户是否有权限操作 该 订单
		PlayOrder order = playOrderService.findById(id);
		order.setStatus(PlayOrderStatus.Complete);
		playOrderService.save(order);
		
		return JsonUtils.getSuccessJSONObject();
	}
}
