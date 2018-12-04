package com.shenghesun.tank.service.v2.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
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
import com.shenghesun.tank.service.CourseService;
import com.shenghesun.tank.service.ProductTypeService;
import com.shenghesun.tank.service.QuotedProductService;
import com.shenghesun.tank.service.entity.Course;
import com.shenghesun.tank.service.entity.Product;
import com.shenghesun.tank.service.entity.ProductType;
import com.shenghesun.tank.service.entity.QuotedProduct;
import com.shenghesun.tank.service.entity.model.DurationType;
import com.shenghesun.tank.utils.JsonUtils;

@RestController
@RequestMapping(value = "/v2/quoted_product")
public class QuotedProductV2Controller {

	@Autowired
	private QuotedProductService quotedProductService;
	@Autowired
	private ProductTypeService productTypeService;
	@Autowired
	private CoachService coachService;
	@Autowired
	private OrderStatisService orderStatisService;
	@Autowired
	private CourseService courseService;

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
		// 20181030 kevin 添加获取大神的统计数据
		ProductType pt = productTypeService.findByCode(code);// 获取当前类型
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

	/**
	 * 
	 * 价目表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/rapid_price", method = RequestMethod.GET)
	public JSONObject rapidPrice(HttpServletRequest request) {
		Coach coach = coachService.findBySpecial(true);
		JSONObject json = new JSONObject();
		List<JSONObject> type1 = new ArrayList<>();
		List<JSONObject> type2 = new ArrayList<>();
		List<JSONObject> type3 = new ArrayList<>();
		List<JSONObject> type4 = new ArrayList<>();
		
		List<ProductType> types = productTypeService.findAll();
		Map<Integer, String> level1Map = new HashMap<>();// level 1 的 code 和 name
		Map<Integer, String> level2Map = new HashMap<>();// level 2 的 code 和 name
		Map<Integer, String> level3Map = new HashMap<>();// level 3 的 code 和 name
		Map<Integer, String> level4Map = new HashMap<>();// level 4 的 code 和 name
		Map<Integer, Integer> level2CodeMap = new HashMap<>();// level 2 的 code 和 parentCode
		Map<Integer, Integer> level3CodeMap = new HashMap<>();// level 3 的 code 和 parentCode
		Map<Integer, Integer> level4CodeMap = new HashMap<>();// level 4 的 code 和 parentCode
		this.initLevelMap(types, level1Map, level2Map, level3Map, level4Map, level2CodeMap, level3CodeMap,
				level4CodeMap);

		List<QuotedProduct> list = quotedProductService.findByRemovedAndCoachId(false, coach.getId());
		for (QuotedProduct quotedProduct : list) {
			Product product = quotedProduct.getProduct();
			int duration =  product.getDuration();
			String durationType = product.getDurationType().getText();
			ProductType productType = product.getProductType();
			
//			JSONObject jsonObject = new JSONObject();
			if(productType.getLevel() == 3) {
				JSONObject typeJson = new JSONObject();
				typeJson.put("level3Code", productType.getCode());
				typeJson.put("level3Name", productType.getName());
				Integer level2Code = productType.getParentCode();
				typeJson.put("level2Code", level2Code);
				typeJson.put("level2Name", level2Map.get(level2Code));
				
				typeJson.put("price", quotedProduct.getPrice());
				typeJson.put("duration", duration);
				typeJson.put("durationType", durationType);
				
//				jsonObject.put("price", typeJson);
				Integer level1Code = level2CodeMap.get(level2Code);
				
				switch (level1Code) {
				case 10:
					type1.add(typeJson);
					break;
				case 11:
					type2.add(typeJson);
					break;
				case 12:
					type3.add(typeJson);
					break;
				case 13:
					type4.add(typeJson);
					break;
				default:
					break;
				}
			}
			if(productType.getLevel() == 4) {
				JSONObject typeJson = new JSONObject();
				typeJson.put("level4Code", productType.getCode());
				typeJson.put("level4Name", productType.getName());
				Integer level3Code = productType.getParentCode();
				typeJson.put("level3Code", level3Code);
				typeJson.put("level3Name", level3Map.get(level3Code));
				Integer level2Code = level3CodeMap.get(level3Code);
				typeJson.put("level2Code", level2Code);
				typeJson.put("level2Name", level2Map.get(level2Code));
				
				typeJson.put("price", quotedProduct.getPrice());
				typeJson.put("duration", duration);
				typeJson.put("durationType", durationType);
//				jsonObject.put("price", typeJson);
				Integer level1Code = level2CodeMap.get(level2Code);
				switch (level1Code) {
				case 10:
					type1.add(typeJson);
					break;
				case 11:
					type2.add(typeJson);
					break;
				case 12:
					type3.add(typeJson);
					break;
				case 13:
					type4.add(typeJson);
					break;
				default:
					break;
				}
			}
		}
		JSONObject teaching = new JSONObject();
		teaching.put("code", 10);
		teaching.put("name", level1Map.get(10));
		teaching.put("price", type1);
		JSONObject replacing = new JSONObject();
		replacing.put("code", 11);
		replacing.put("name", level1Map.get(11));
		replacing.put("price", type2);
		JSONObject accompany = new JSONObject();
		accompany.put("code", 12);
		accompany.put("name", level1Map.get(12));
		accompany.put("price", type3);
		JSONObject craftreplacing = new JSONObject();
		craftreplacing.put("code", 13);
		craftreplacing.put("name", level1Map.get(13));
		craftreplacing.put("price", type4);
		json.put("teaching", teaching);
		json.put("replacing", replacing);
		json.put("accompany", accompany);
		json.put("craftreplacing", craftreplacing);

		return JsonUtils.getSuccessJSONObject(json);
	}

	private void initLevelMap(List<ProductType> types, Map<Integer, String> level1Map, Map<Integer, String> level2Map,
			Map<Integer, String> level3Map, Map<Integer, String> level4Map, Map<Integer, Integer> level2CodeMap,
			Map<Integer, Integer> level3CodeMap, Map<Integer, Integer> level4CodeMap) {
		for (ProductType t : types) {
			switch (t.getLevel()) {
			case 1:
				level1Map.put(t.getCode(), t.getName());
				break;
			case 2:
				level2Map.put(t.getCode(), t.getName());
				level2CodeMap.put(t.getCode(), t.getParentCode());
				break;
			case 3:
				level3Map.put(t.getCode(), t.getName());
				level3CodeMap.put(t.getCode(), t.getParentCode());
				break;
			case 4:
				level4Map.put(t.getCode(), t.getName());
				level4CodeMap.put(t.getCode(), t.getParentCode());
			default:
				break;
			}
		}
	}

//	private JSONObject formatTypes(ProductType p, Map<Integer, String> level1Map, Map<Integer, String> level2Map,
//			Map<Integer, String> level3Map, Map<Integer, String> level4Map, Map<Integer, Integer> level2CodeMap,
//			Map<Integer, Integer> level3CodeMap, Map<Integer, Integer> level4CodeMap) {
//		JSONObject json = new JSONObject();
//		if (p.getLevel() == 3) {
//			if (level4CodeMap.values().contains(p.getCode())) {
//				for (Integer level4Code : level4CodeMap.keySet()) {
//					ProductType productType = productTypeService.findByCode(level4Code);
//					JSONObject typeJson = new JSONObject();
//					typeJson.put("level4Code", level4Code);
//					typeJson.put("level4Name", level4Map.get(level4Code));
//
//					Integer level3code = level4CodeMap.get(level4Code);
//					typeJson.put("level3Code", level3code);
//					typeJson.put("level3Name", level3Map.get(level3code));
//
//					Integer level2Code = level3CodeMap.get(level3code);
//					typeJson.put("level2Code", level2Code);
//					typeJson.put("level2Name", level2Map.get(level2Code));
//
//					Integer level1Code = level2CodeMap.get(level2Code);
//					typeJson.put("level1Code", level1Code);
//					typeJson.put("level1Name", level1Map.get(level1Code));
//					json.put(productType.getId().toString(), typeJson);
//				}
//			} else {
//				JSONObject typeJson = new JSONObject();
//				typeJson.put("level3Code", p.getCode());
//				typeJson.put("level3Name", p.getName());
//				Integer level2Code = p.getParentCode();
//				typeJson.put("level2Code", level2Code);
//				typeJson.put("level2Name", level2Map.get(level2Code));
//				Integer level1Code = level2CodeMap.get(level2Code);
//				typeJson.put("level1Code", level1Code);
//				typeJson.put("level1Name", level1Map.get(level1Code));
//				json.put(p.getId().toString(), typeJson);
//			}
//		}
//		return json;
//	}
	
	
	/**
	 * 2018年11月29日18:28:30
	 * 
	 * v2 版本
	 */
	
	/**
	 * 获取 大神-服务 报价
	 * @param coachId
	 * @param code
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/price" ,method = RequestMethod.POST)
	public JSONObject getPrice(@RequestParam(value = "coachId" , required = true)String coachId,
			@RequestParam(value = "code" ,required = true)String code,HttpServletRequest request) {
		if (StringUtils.isBlank(coachId) || StringUtils.isBlank(code)) {
			return JsonUtils.getFailJSONObject("参数错误!");
		}
		JSONObject json = new JSONObject();
		QuotedProduct quotedProduct = quotedProductService.findByCoachIdAndProductProductTypeCode(Long.parseLong(coachId), Integer.parseInt(code));
		json.put("quotedProduct", quotedProduct);
		ProductType productType = productTypeService.findByCode(Integer.valueOf(code));
		List<Course> courses = courseService.findByProductTypeIdAndCoachId(productType.getId(), Long.parseLong(coachId));
		json.put("course", courses);
		return JsonUtils.getSuccessJSONObject(json);
	}
	
}
