package com.shenghesun.tank.service.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.coach.CoachService;
import com.shenghesun.tank.coach.entity.Coach;
import com.shenghesun.tank.service.ProductTypeService;
import com.shenghesun.tank.service.QuotedProductService;
import com.shenghesun.tank.service.entity.Product;
import com.shenghesun.tank.service.entity.ProductType;
import com.shenghesun.tank.service.entity.QuotedProduct;
import com.shenghesun.tank.utils.JsonUtils;

@RestController
@RequestMapping(value = "/quoted_product")
public class QuotedProductController {

	@Autowired
	private QuotedProductService quotedProductService;
	@Autowired
	private ProductTypeService productTypeService;
	@Autowired
	private CoachService coachService;

	/**
	 * 根据大神的id 获取大神针对每个服务的具体报价
	 * 
	 * @param coachId
	 *            如果是null，表示使用神秘大神
	 * @param code
	 *            10-教学 11-代练 12-陪玩 ，没有值默认是11-代练；level 1 的 code
	 * @return
	 */
	@RequestMapping(value = "/coach", method = RequestMethod.GET)
	public JSONObject byCoach(@RequestParam(value = "code", required = false) Integer code,
			@RequestParam(value = "coachId", required = false) Long coachId) {
		Coach coach = null;
		if (coachId == null) {
			coach = coachService.findBySpecial(true);
			coachId = coach.getId();
		} else {
			coach = coachService.findById(coachId);
		}
		if (code == null) {
			code = 11;
		}
		// ProductType productType = productTypeService.findByCode(code);
		List<ProductType> typeLevel = productTypeService.findByParentCode(code);
		// List<QuotedProduct> quotes = quotedProductService.findByCoachId(coachId);
		// 指定大神的所有服务报价，需要根据服务的二级类型，把服务分集合存储
		// 当前 code 是 level 1 的类型，需要获取 level 3 的类型，然后使用 type code in 查询结果集
		List<QuotedProduct> quotes = quotedProductService.findByRemovedAndCoachIdAndProductProductTypeCodeIn(false, coachId,
				this.getLevel3CodeList(code));

		JSONObject json = new JSONObject();
		json.put("products", this.formatProducts(quotes));
		json.put("coach", coach);
		json.put("code", code);
		json.put("level2", typeLevel);
		return JsonUtils.getSuccessJSONObject(json);
	}

	public List<Integer> getLevel3CodeList(int codeLevel1) {
		List<ProductType> typeLevel2List = productTypeService.findByParentCode(codeLevel1);
		List<Integer> typeLevel2IdList = this.getIds(typeLevel2List);
		List<ProductType> typeLevel3List = productTypeService.findByParentCodeIn(typeLevel2IdList);
		return this.getIds(typeLevel3List);
	}

	private List<Integer> getIds(List<ProductType> entityList) {
		List<Integer> codeList = new ArrayList<>();
		if (entityList != null && entityList.size() > 0) {
			for (ProductType pt : entityList) {
				codeList.add(pt.getCode());
			}
		}
		return codeList;
	}

	// {"L1101":[{}，{}]}
	private JSONObject formatProducts(List<QuotedProduct> quotes) {
		JSONObject json = new JSONObject();
		if (quotes != null && quotes.size() > 0) {
			for (QuotedProduct qp : quotes) {
				Product p = qp.getProduct();
				int codeLevel2 = p.getProductType().getParentCode();
				String key = "L" + codeLevel2;
				JSONArray arr = json.getJSONArray(key);
				if (arr == null) {
					arr = new JSONArray();
				}
				arr.add(qp);
				json.put(key, arr);
			}
		}
		return json;
	}

	/**
	 * 根据 level 3 的类型 code 和 当前的大神获取具体服务报价信息 目前默认每个大神对应每个具体类型，只有一个服务在线
	 */
	@RequestMapping(value = "/coach_code", method = RequestMethod.GET)
	public JSONObject byCoachAndLevel3Code(@RequestParam(value = "code") Integer code,
			@RequestParam(value = "coachId") Long coachId) {

		QuotedProduct quotes = quotedProductService.findByCoachIdAndProductProductTypeCode(coachId, code);

		JSONObject json = new JSONObject();
		json.put("quotes", quotes);
		return JsonUtils.getSuccessJSONObject(json);
	}
	
	/**
	 * 根据 level 3 的类型 code 和 当前的大神获取具体服务报价信息，再根据当前选择的时长，计算总金额
	 */
	@RequestMapping(value = "/total_fee", method = RequestMethod.GET)
	public JSONObject totalFee(@RequestParam(value = "code") Integer code,
			@RequestParam(value = "coachId") Long coachId,
			@RequestParam(value = "duration") int duration) {
		QuotedProduct quotes = quotedProductService.findByCoachIdAndProductProductTypeCode(coachId, code);
		ProductType typeLevel3 = productTypeService.findByCode(code);
		ProductType typeLevel2 = productTypeService.findByCode(typeLevel3.getParentCode());
		BigDecimal totalFee = BigDecimal.ZERO;
		if(typeLevel2.getParentCode() == 11) {
			totalFee = quotes.getPrice();
		}else {
			totalFee = quotedProductService.getTotalFee(duration, quotes.getPrice(), 
					quotes.getProduct().getDuration());
		}

//		BigDecimal totalFee = BigDecimal.ZERO;
//		BigDecimal price = quotes.getPrice();//单价有效
//		if(price != null && price.compareTo(BigDecimal.ZERO) > 0) {
//			int dur = quotes.getProduct().getDuration();//产品中存在有效的时长
//			if(dur > 0) {
//				//大神针对单个服务的具体报价的单价 X （选择服务的时长 / 服务的单位时长）
//				totalFee = price.multiply(new BigDecimal(duration / dur)); 
//			}
//		}
		
		JSONObject json = new JSONObject();
		json.put("totalFee", totalFee);
		return JsonUtils.getSuccessJSONObject(json);
	}
	
}
