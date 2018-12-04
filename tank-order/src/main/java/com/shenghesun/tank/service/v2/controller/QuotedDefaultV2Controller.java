package com.shenghesun.tank.service.v2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.service.QuotedDefaultService;
import com.shenghesun.tank.service.entity.QuotedDefault;
import com.shenghesun.tank.utils.JsonUtils;

@RestController
@RequestMapping(value = "/v2/quoted_default")
public class QuotedDefaultV2Controller {
	
	@Autowired
	private QuotedDefaultService quotedDefaultService;

	/**
	 * 根据服务的一级类型 查询默认报价
	 * @param code 10-教学 11-代练 12-陪玩 ，没有值默认是11-代练
	 * @return
	 */
	@RequestMapping(value = "/type", method = RequestMethod.GET)
	public JSONObject byProductType(
		@RequestParam(value = "code", required = false) Integer code) {
		if(code == null) {
			code = 11;
		}
		Pageable pageable = this.getPageable();
		Page<QuotedDefault> page = quotedDefaultService.findByProductTypeCode(code, pageable);
		JSONObject json = new JSONObject();
		json.put("page", page);
		json.put("code", code);
		return JsonUtils.getSuccessJSONObject(json);
	}
	private Pageable getPageable() {
		Sort sort = new Sort(Direction.DESC, "coach.seqNum");
		Pageable pageable = new PageRequest(0, 20, sort);
		return pageable;
	}
	
}
