package com.shenghesun.tank.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.service.ProductTypeService;
import com.shenghesun.tank.service.entity.ProductType;
import com.shenghesun.tank.utils.JsonUtils;

@RestController
@RequestMapping(value = "/order")
public class ProductTypeController {
	
	@Autowired
	private ProductTypeService productTypeService;

	/**
	 * 提供 根据 level 2 的类型获取 level 3 的所有类型的接口
	 * @param codeLevel2
	 * @return
	 */
	@RequestMapping(value = "/level3", method = RequestMethod.GET)
	public JSONObject level3(
			@RequestParam(value = "code") int codeLevel2) {
		List<ProductType> list = productTypeService.findByParentCode(codeLevel2);
		JSONObject json = new JSONObject();
		json.put("level3", list);
		return JsonUtils.getSuccessJSONObject(json);
	}
	
}
