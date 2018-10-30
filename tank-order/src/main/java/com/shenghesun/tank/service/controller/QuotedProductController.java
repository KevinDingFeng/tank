package com.shenghesun.tank.service.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.coach.CoachService;
import com.shenghesun.tank.coach.entity.Coach;
import com.shenghesun.tank.data.OrderStatisService;
import com.shenghesun.tank.service.ProductTypeService;
import com.shenghesun.tank.service.QuotedProductService;
import com.shenghesun.tank.service.entity.Product;
import com.shenghesun.tank.service.entity.ProductType;
import com.shenghesun.tank.service.entity.QuotedProduct;
import com.shenghesun.tank.service.entity.model.DurationType;
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
	@Autowired
	private OrderStatisService orderStatisService;

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
		// List<ProductType> typeLevel = productTypeService.findByParentCode(code);//根据
		// level 1 的 code 获取所有 level 2 的类型
		// List<QuotedProduct> quotes = quotedProductService.findByCoachId(coachId);
		// 指定大神的所有服务报价，需要根据服务的二级类型，把服务分集合存储
		// 当前 code 是 level 1 的类型，需要获取 level 3 的类型，然后使用 type code in 查询结果集
		List<QuotedProduct> quotes = quotedProductService.findByRemovedAndCoachIdAndProductProductTypeCodeIn(false,
				coachId, productTypeService.getLevel3CodeList(code));

		JSONObject json = new JSONObject();
		json.put("products", this.formatProducts(quotes));
		json.put("coach", coach);
		json.put("code", code);
		json.put("level2", this.getTypesInQps(quotes));
		//20181030 kevin 添加获取大神的统计数据
		ProductType pt = productTypeService.findByCode(code);//获取当前类型
		json.put("orderStatis", orderStatisService.findByProductTypeIdAndCoachId(pt.getId(), coachId));
		
		
		return JsonUtils.getSuccessJSONObject(json);
	}

	/**
	 * 根据 真实报价 整理得出需要的 产品类型， level 2 
	 * 
	 * @param qps
	 * @return
	 */
	private List<ProductType> getTypesInQps(List<QuotedProduct> qps) {
		if (qps == null) {
			return null;
		}
		Map<Integer, ProductType> level2Map = new HashMap<>();// key = level 3 的 parentCode ; value = type

		for (QuotedProduct qp : qps) {
			Product pro = qp.getProduct();
			ProductType t = pro.getProductType();
			int pc = t.getParentCode();
			if (level2Map.get(pc) == null) {// 第一次添加
				level2Map.put(pc, productTypeService.findByCode(pc));
			}
			// 已添加 的情况，不做任何操作
		}

		List<ProductType> level2 = new ArrayList<>();

		Set<Integer> set2 = level2Map.keySet();
		if (set2 != null) {
			Iterator<Integer> its = set2.iterator();
			while (its.hasNext()) {
				int key = its.next();
				level2.add(level2Map.get(key));
			}
		}
		return level2;
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
			@RequestParam(value = "coachId") Long coachId, @RequestParam(value = "duration") int duration) {
		QuotedProduct quotes = quotedProductService.findByCoachIdAndProductProductTypeCode(coachId, code);
		// ProductType typeLevel3 = productTypeService.findByCode(code);
		// ProductType typeLevel2 =
		// productTypeService.findByCode(typeLevel3.getParentCode());
		BigDecimal totalFee = BigDecimal.ZERO;
		Product product = quotes.getProduct();

		// if(typeLevel2.getParentCode() == 11) {
		if (product.getDuration() < 1 || DurationType.NoLimitation.name().equals(product.getDurationType().name())) {
			totalFee = quotes.getPrice();
		} else {
			totalFee = quotedProductService.getTotalFee(duration, quotes.getPrice(), product.getDuration());
		}

		// BigDecimal totalFee = BigDecimal.ZERO;
		// BigDecimal price = quotes.getPrice();//单价有效
		// if(price != null && price.compareTo(BigDecimal.ZERO) > 0) {
		// int dur = quotes.getProduct().getDuration();//产品中存在有效的时长
		// if(dur > 0) {
		// //大神针对单个服务的具体报价的单价 X （选择服务的时长 / 服务的单位时长）
		// totalFee = price.multiply(new BigDecimal(duration / dur));
		// }
		// }

		JSONObject json = new JSONObject();
		json.put("totalFee", totalFee);
		return JsonUtils.getSuccessJSONObject(json);
	}

}
