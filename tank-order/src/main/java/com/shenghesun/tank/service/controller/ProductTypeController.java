package com.shenghesun.tank.service.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.service.ProductTypeService;
import com.shenghesun.tank.service.QuotedProductService;
import com.shenghesun.tank.service.entity.Product;
import com.shenghesun.tank.service.entity.ProductType;
import com.shenghesun.tank.service.entity.QuotedProduct;
import com.shenghesun.tank.utils.JsonUtils;

@RestController
@RequestMapping(value = "/product_type")
public class ProductTypeController {
	
	@Autowired
	private ProductTypeService productTypeService;
	@Autowired
	private QuotedProductService quotedProductService;

	/**
	 * 提供 根据 level 2 的类型获取 level 3 的所有类型的接口
	 * @param codeLevel2
	 * @return
	 */
	@RequestMapping(value = "/level3", method = RequestMethod.GET)
	public JSONObject level3(
			@RequestParam(value = "code") int codeLevel2, 
			@RequestParam(value = "coachId") Long coachId) {
		
		
		List<QuotedProduct> quotes = quotedProductService.findByRemovedAndCoachIdAndProductProductTypeParentCode(false,
				coachId, codeLevel2);
		
		JSONObject json = new JSONObject();
		json.put("level3", this.getTypesInQps(quotes));
//		List<ProductType> list = productTypeService.findByParentCode(codeLevel2);
//		json.put("level3", list);
		return JsonUtils.getSuccessJSONObject(json);
	}
	/**
	 * 根据 真实报价 整理得出需要的 产品类型， level 3
	 * @param qps
	 * @return
	 */
	private List<ProductType> getTypesInQps(List<QuotedProduct> qps) {
		if(qps == null) {
			return null;
		}
		Map<Long, ProductType> level3Map = new HashMap<>();// key = id ; value = type
		
		for(QuotedProduct qp : qps) {
			Product pro = qp.getProduct();
			Long tid = pro.getProductTypeId();//level 3 的 id
			
			if(level3Map.get(tid) == null) {//第一次添加
				ProductType t = pro.getProductType();
				level3Map.put(tid, t);
			}
			//已存在 的时候不做任何操作
		}
		
		List<ProductType> level3 = new ArrayList<>();
		
		Set<Long> set3 = level3Map.keySet();
		if(set3 != null) {
			Iterator<Long> its = set3.iterator();
			while(its.hasNext()) {
				Long key = its.next();
				level3.add(level3Map.get(key));
			}
		}
		return level3;
	}
	
	/**
	 * 2018年11月01日14:17:21
	 * 获取服务类型
	 * @param level1
	 * @param level2
	 * @param level3
	 * @return
	 */
	@RequestMapping(value = "/entire_level")
	public JSONObject getProductTypeByLev(
			@RequestParam(value = "level1",required = false)String level1,
			@RequestParam(value = "level2",required = false)String level2,
			@RequestParam(value = "level3",required = false)String level3) {
		JSONObject json = new JSONObject();
		if(StringUtils.isBlank(level1) && StringUtils.isBlank(level2) 
				&& StringUtils.isBlank(level3)) {
			List<ProductType> list = productTypeService.findByParentCode(1);
			json.put("level1", list);
		}
		if(StringUtils.isNotBlank(level1)) {
			List<ProductType> list = productTypeService.findByParentCode(Integer.valueOf(level1));
			json.put("level2", list);
		}
		if(StringUtils.isNotBlank(level2)) {
			List<ProductType> list = productTypeService.findByParentCode(Integer.valueOf(level2));
			json.put("level3", list);
		}
		if(StringUtils.isNotBlank(level3)) {
			List<ProductType> list = productTypeService.findByParentCode(Integer.valueOf(level3));
			json.put("level4", list);
		}
		return JsonUtils.getSuccessJSONObject(json);
	}
	
}
