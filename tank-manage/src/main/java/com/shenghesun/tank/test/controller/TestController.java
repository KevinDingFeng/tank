package com.shenghesun.tank.test.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.coach.CoachService;
import com.shenghesun.tank.coach.entity.Coach;
import com.shenghesun.tank.service.ProductService;
import com.shenghesun.tank.service.ProductTypeService;
import com.shenghesun.tank.service.QuotedDefaultService;
import com.shenghesun.tank.service.QuotedProductService;
import com.shenghesun.tank.service.entity.Product;
import com.shenghesun.tank.service.entity.ProductType;
import com.shenghesun.tank.service.entity.QuotedDefault;
import com.shenghesun.tank.service.entity.QuotedProduct;
import com.shenghesun.tank.utils.JsonUtils;

@RestController
@RequestMapping(value = "/test")
public class TestController {

	@RequestMapping(method = RequestMethod.GET)
	public JSONObject test() {
		return JsonUtils.getSuccessJSONObject();
	}
	
	@Autowired
	private CoachService coachService;
	@Autowired
	private ProductTypeService productTypeService;
	@Autowired
	private QuotedDefaultService defaultService;
	@Autowired
	private ProductService productService;
	@Autowired
	private QuotedProductService quotedProductService;
	
	
	@RequestMapping(value = "/init", method = RequestMethod.GET)
	public JSONObject init() {
		List<Coach> coaches = coachService.findAll();
		List<ProductType> types = productTypeService.findAll();
//		List<QuotedDefault> qds = defaultService.findAll();
		
		
		
		if(types != null) {
			for(ProductType t : types) {
				//创建产品
				Product p = new Product();
				p.setProductType(t);
				p.setProductTypeId(t.getId());
				p.setCapacity(0);
				//时长 和时长单位 ： 根据类型的默认报价中的参数设置
				Coach coach = coachService.findBySpecial(true);
				QuotedDefault d = defaultService.findByCoachIdAndProductTypeId(coach.getId(), t.getId());
				p.setDuration(d.getDuration());
				p.setDurationType(d.getDurationType());
				p.setContent(t.getName());
				p = productService.save(p);
			}
		}
		
		List<Product> products = productService.findAll();
		if(coaches != null && products != null) {
			for(Coach c : coaches) {
				for(Product p : products) {
					QuotedProduct qp = new QuotedProduct();
					qp.setCoach(c);
					qp.setCoachId(c.getId());
					qp.setProduct(p);
					qp.setProductId(p.getId());
					qp.setActivityRatio(BigDecimal.ZERO);
					qp.setSurchargeRatio(BigDecimal.ZERO);
					qp.setSurcharge(BigDecimal.ZERO);
					//大神针对单个服务的报价，价格根据大神针对服务类型的默认报价设置
					QuotedDefault d = defaultService.findByCoachIdAndProductTypeId(c.getId(), p.getProductTypeId());
					if(p.getDurationType().name().equals(d.getDurationType().name())) {
						System.out.println("设置价格");
						if(p.getDuration() != 0 && d.getDuration() != 0) {
							qp.setPrice(d.getPrice().multiply(new BigDecimal(p.getDuration() / d.getDuration())));
						}
					}
					quotedProductService.save(qp);					
					
				}
			}
		}
		
//		if(qds != null) {
//			for(QuotedDefault qd : qds) {
//				System.out.println(qd.getId());
//			}
//		}
		
		return JsonUtils.getSuccessJSONObject();
	}
}
