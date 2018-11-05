package com.shenghesun.tank.order.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.coach.CoachService;
import com.shenghesun.tank.coach.entity.Coach;
import com.shenghesun.tank.order.PlayOrderService;
import com.shenghesun.tank.order.entity.PlayOrder;
import com.shenghesun.tank.order.entity.PlayOrder.OperationType;
import com.shenghesun.tank.order.entity.PlayOrder.OrderType;
import com.shenghesun.tank.service.ProductService;
import com.shenghesun.tank.service.ProductTypeService;
import com.shenghesun.tank.service.QuotedProductService;
import com.shenghesun.tank.service.entity.Product;
import com.shenghesun.tank.service.entity.ProductType;
import com.shenghesun.tank.service.entity.QuotedProduct;

@Controller
@RequestMapping(value = "/play_order/assign")
public class PlayOrderAssignController {
	
	@Autowired
	private PlayOrderService playOrderService;
	@Autowired
	private CoachService coachService;
	@Autowired
	private QuotedProductService quotedProductService;
	@Autowired
	private ProductTypeService productTypeService;
	@Autowired
	private ProductService productService;
	
	
	@RequestMapping(value = "/rapid_list" , method = {RequestMethod.GET, RequestMethod.POST})
	public String repidList(
			@RequestParam(value = "pageNum", required = false) Integer pageNum,
			@RequestParam(value = "keyword", required = false) String keyword, Model model) {
		//权限校验  判断当前登录用户，是否拥有 分派 订单的权限
//		Subject subject = SecurityUtils.getSubject();
//		if(!subject.isPermitted("order:assign")) {
//			throw new AuthorizationException("缺少派单权限");
//		}
		pageNum = pageNum == null ? 0 : pageNum;
		Pageable pageable = this.getListPageable(pageNum);
		Page<PlayOrder> page = playOrderService.findBySpecification(this.getSpecification(keyword,OrderType.Quick), pageable);
		model.addAttribute("page", page);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageNum", pageNum);
		return "order_assign/rapid_list";
	}
	
	/**
	 * 获取派单列表信息
	 * @param pageNum
	 * @param keyword
	 * @return
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
	public String list(
		@RequestParam(value = "pageNum", required = false) Integer pageNum,
		@RequestParam(value = "keyword", required = false) String keyword, Model model) {
		//权限校验  判断当前登录用户，是否拥有 分派 订单的权限
//		Subject subject = SecurityUtils.getSubject();
//		if(!subject.isPermitted("order:assign")) {
//			throw new AuthorizationException("缺少派单权限");
//		}
		pageNum = pageNum == null ? 0 : pageNum;
		Pageable pageable = this.getListPageable(pageNum);
		Page<PlayOrder> page = playOrderService.findBySpecification(this.getSpecification(keyword,OrderType.Common), pageable);

		model.addAttribute("page", page);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageNum", pageNum);
		return "order_assign/list";
	}
	private Pageable getListPageable(Integer pageNum) {
		Sort sort = new Sort(Direction.DESC, "creation");
		Pageable pageable = new PageRequest(pageNum, 20, sort);
		return pageable;
	}
	private Specification<PlayOrder> getSpecification(String keyword,OrderType orderType) {

		return new Specification<PlayOrder>() {
			@Override
			public Predicate toPredicate(Root<PlayOrder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				
//				root.fetch("wxUser", JoinType.LEFT);
//				root.fetch("coach", JoinType.LEFT);
//				root.fetch("product", JoinType.LEFT);
				
				list.add(cb.equal(root.get("removed"), false));
				list.add(cb.equal(root.get("main"), true));//因为订单实体设计时，加入了操作记录，所以每个订单编号是由多条组成，这里只找出其中最新的一条
				
				list.add(cb.equal(root.get("orderType").as(OrderType.class), orderType));
				
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
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String form(@RequestParam(value = "id") Long id, Model model) {
		//权限校验  判断当前登录用户，是否拥有 分派 订单的权限
//		Subject subject = SecurityUtils.getSubject();
//		if(!subject.isPermitted("order:assign")) {
//			throw new AuthorizationException("缺少派单权限");
//		}
		PlayOrder playOrder = playOrderService.findById(id);
		List<QuotedProduct> qpList = quotedProductService.findByProductId(playOrder.getProductId());
		List<Coach> coaches = this.getCoaches(qpList);
//		Product product = playOrder.getProduct();
//		Set<Coach> coaches = product.getCoaches();//可为 订单中的产品 提供服务的大神信息
		//需要给出备用的大神信息，这里有两种方式，第一种是通过 上面的 coaches 获取到已有的大神 ids ，然后使用 not in 查询coach 信息。
		//考虑到 not in 关键词在 数据库中的执行效率不高，这里使用第二种方式，直接查出所有可用的coach信息，然后使用代码过滤掉 coaches中的 ids
		List<Coach> all = coachService.findAll();
		List<Coach> spareCoach = this.getSpareCoach(all, coaches);
		
		model.addAttribute("coaches", coaches);
		model.addAttribute("spareCoach", spareCoach);
		
		model.addAttribute("productTypes", this.initProductTypes());
		model.addAttribute("entity", playOrder);
		return "order_assign/form";
	}
	
	// 测试
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/entire_level" ,method = RequestMethod.GET)
	@ResponseBody
	public JSONObject initProductType() {
		JSONObject map = new JSONObject();
		
		List list1 = new ArrayList<>();
		List<ProductType> level1 = productTypeService.findByParentCode(1);
		for (ProductType p1 : level1) {
			JSONObject map1 = new JSONObject();
			
			List<ProductType> level2 = productTypeService.findByParentCode(p1.getCode());
			map1.put("level1Code", p1.getCode());
			map1.put("level1Name", p1.getName());
			
			List list2= new ArrayList<>();
			for (ProductType p2 : level2) {
				JSONObject map2 = new JSONObject();
				
				List<ProductType> level3 = productTypeService.findByParentCode(p2.getCode());
				map2.put("level2Code", p2.getCode());
				map2.put("level2Name", p2.getName());
				
				List list3= new ArrayList<>();
				for (ProductType p3 : level3) {
					JSONObject map3 = new JSONObject();
					
					map3.put("level3Code", p3.getCode());
					map3.put("level3Name", p3.getName());
					
					List list4 = new ArrayList<>();
					List<ProductType> level4 = productTypeService.findByParentCode(p3.getCode());
					if (level4 != null && level4.size() > 0) {
						for (ProductType p4 : level4) {
							JSONObject map4 = new JSONObject();
							map4.put("level4Code", p4.getCode());
							map4.put("level4Name", p4.getName());
							list4.add(map4);
						}
						map3.put("level4", list4);
					}else {
						map3.put("level4", new ArrayList<>(0));
					}
					list3.add(map3);
				}
				map2.put("level3", list3);
				list2.add(map2);
			}
			map1.put("level2", list2);
			list1.add(map1);
		}
		map.put("data", list1);
		return map;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List initProductTypes() {
		List list1 = new ArrayList<>();
		List<ProductType> level1 = productTypeService.findByParentCode(1);
		for (ProductType p1 : level1) {
			JSONObject map1 = new JSONObject();
			
			List<ProductType> level2 = productTypeService.findByParentCode(p1.getCode());
			map1.put("level1Code", p1.getCode());
			map1.put("level1Name", p1.getName());
			
			List list2= new ArrayList<>();
			for (ProductType p2 : level2) {
				JSONObject map2 = new JSONObject();
				
				List<ProductType> level3 = productTypeService.findByParentCode(p2.getCode());
				map2.put("level2Code", p2.getCode());
				map2.put("level2Name", p2.getName());
				
				List list3= new ArrayList<>();
				for (ProductType p3 : level3) {
					JSONObject map3 = new JSONObject();
					
					map3.put("level3Code", p3.getCode());
					map3.put("level3Name", p3.getName());
					
					List list4 = new ArrayList<>();
					List<ProductType> level4 = productTypeService.findByParentCode(p3.getCode());
					if (level4 != null && level4.size() > 0) {
						for (ProductType p4 : level4) {
							JSONObject map4 = new JSONObject();
							map4.put("level4Code", p4.getCode());
							map4.put("level4Name", p4.getName());
							list4.add(map4);
						}
						map3.put("level4", list4);
					}else {
						map3.put("level4", new ArrayList<>(0));
					}
					list3.add(map3);
				}
				map2.put("level3", list3);
				list2.add(map2);
			}
			map1.put("level2", list2);
			list1.add(map1);
		}
		return list1;
	}
	
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String form(@RequestParam(value = "id") Long id, 
			@RequestParam(value = "coach") Long executorId, 
			@RequestParam(value = "code",required = false)String code ,Model model) {
		//权限校验  判断当前登录用户，是否拥有 分派 订单的权限
//		Subject subject = SecurityUtils.getSubject();
//		if(!subject.isPermitted("order:assign")) {
//			throw new AuthorizationException("缺少派单权限");
//		}
		PlayOrder playOrder = playOrderService.findById(id);
		playOrder.setExecutor(coachService.findById(executorId));
		playOrder.setExecutorId(executorId);
		if (code != null) {
			Product product = playOrder.getProduct();
			ProductType productType = productTypeService.findByCode(Integer.valueOf(code));
			product.setProductTypeId(productType.getId());
			product.setProductType(productType);
			product =  productService.save(product);
			playOrder.setProduct(product);
		}
		playOrderService.save(playOrder);
		return "redirect:/play_order/assign/list";
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
