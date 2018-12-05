package com.shenghesun.tank.service.v2.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.shenghesun.tank.coach.CoachService;
import com.shenghesun.tank.coach.entity.Coach;
import com.shenghesun.tank.data.OrderStatisService;
import com.shenghesun.tank.service.CourseService;
import com.shenghesun.tank.service.ProductService;
import com.shenghesun.tank.service.ProductTypeService;
import com.shenghesun.tank.service.QuotedProductService;
import com.shenghesun.tank.service.ResourceService;
import com.shenghesun.tank.service.entity.Course;
import com.shenghesun.tank.service.entity.Product;
import com.shenghesun.tank.service.entity.ProductType;
import com.shenghesun.tank.service.entity.QuotedProduct;
import com.shenghesun.tank.service.entity.Resource;
import com.shenghesun.tank.service.entity.Resource.ResorceTypes;
import com.shenghesun.tank.service.entity.model.StatisDataModel;
import com.shenghesun.tank.utils.JsonUtils;

@RestController
@RequestMapping(value = "/v2/product_type")
public class ProductTypeV2Controller {

	@Autowired
	private ProductTypeService productTypeService;
	@Autowired
	private QuotedProductService quotedProductService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private OrderStatisService orderStatisService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private CoachService coachService;
	@Autowired
	private ProductService productService;

	/**
	 * 提供 根据 level 2 的类型获取 level 3 的所有类型的接口
	 * 
	 * @param codeLevel2
	 * @return
	 */
	@RequestMapping(value = "/level3", method = RequestMethod.GET)
	public JSONObject level3(@RequestParam(value = "code") int codeLevel2,
			@RequestParam(value = "coachId") Long coachId) {

		List<QuotedProduct> quotes = quotedProductService.findByRemovedAndCoachIdAndProductProductTypeParentCode(false,
				coachId, codeLevel2);

		JSONObject json = new JSONObject();
		json.put("level3", this.getTypesInQps(quotes));
		// List<ProductType> list = productTypeService.findByParentCode(codeLevel2);
		// json.put("level3", list);
		return JsonUtils.getSuccessJSONObject(json);
	}

	/**
	 * 根据 真实报价 整理得出需要的 产品类型， level 3
	 * 
	 * @param qps
	 * @return
	 */
	private List<ProductType> getTypesInQps(List<QuotedProduct> qps) {
		if (qps == null) {
			return null;
		}
		Map<Long, ProductType> level3Map = new HashMap<>();// key = id ; value = type

		for (QuotedProduct qp : qps) {
			Product pro = qp.getProduct();
			Long tid = pro.getProductTypeId();// level 3 的 id

			if (level3Map.get(tid) == null) {// 第一次添加
				ProductType t = pro.getProductType();
				level3Map.put(tid, t);
			}
			// 已存在 的时候不做任何操作
		}

		List<ProductType> level3 = new ArrayList<>();

		Set<Long> set3 = level3Map.keySet();
		if (set3 != null) {
			Iterator<Long> its = set3.iterator();
			while (its.hasNext()) {
				Long key = its.next();
				level3.add(level3Map.get(key));
			}
		}
		return level3;
	}

	/**
	 * 2018年11月28日15:24:52
	 * 
	 * v2 版本
	 */

	/**
	 * 服务类型详情 类型I
	 * 
	 * @param typeCode
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public JSONObject type1(@RequestParam(value = "typeCode", required = true) String typeCode,
			HttpServletRequest request) {
		JSONObject json = new JSONObject();
		ProductType productType = productTypeService.findByCode(Integer.parseInt(typeCode));
		if (productType == null || (productType != null && productType.getLevel() != 1)) {
			return JsonUtils.getFailJSONObject("参数错误!");
		}
		//一级服务类型信息
		ProductType p = productTypeService.findByCode(Integer.valueOf(typeCode));
		json.put("v1", p);
		// 服务类型资源文件列表
		List<Resource> list = resourceService.findByProductTypeId(productType.getId(), false);
		json.put("resource", list);
		// 统计 四大服务类型 数据
		List<StatisDataModel> sdms = this.formatDatas();
		List<Integer> codes = new ArrayList<>();
		for (StatisDataModel statisDataModel : sdms) {
			codes.add(statisDataModel.getCode());
		}
		if (!codes.contains(Integer.valueOf(typeCode))) {
			JSONObject sdm = new JSONObject();
			sdm.put("code", Integer.valueOf(typeCode));
			sdm.put("counts", 923);
			sdm.put("hours", 923);
			json.put("statis", sdm);
		}
		for (StatisDataModel statisDataModel : sdms) {
			if (statisDataModel.getCode() == Integer.valueOf(typeCode)) {
				JSONObject sdm = new JSONObject();
				sdm.put("code", statisDataModel.getCode());
				sdm.put("counts", statisDataModel.getCounts() < 1000 ? 923 : statisDataModel.getCounts());
				sdm.put("hours", statisDataModel.getHours());
				json.put("statis", sdm);
			}
		}
		// 课程表 前端固定图片
		// 其他详情 前端固定图片
		List<ProductType> types = productTypeService.findAll();
		JSONObject typesJson = this.formatTypes(types);
		json.put("product_types", typesJson);
		
		// 服务类型 默认价格 默认教练
		json.put("types", this.formatProductTypesByProductType(Integer.valueOf(typeCode)));
		
		//默认信息
		List<Long> ids = this.getProductIds(typeCode);
		List<QuotedProduct> qps = quotedProductService.findByProductIdIn(ids);
		json.put("def", this.formatDefaults(qps, typesJson));
		//大神排序
		json.put("coaches", coachService.findAll(new Sort(Direction.DESC, "seqNum")));

		json.put("pre_path", resourceService.getShowFilePath());

		return JsonUtils.getSuccessJSONObject(json);
	}
	//格式化 默认信息
	private JSONObject formatDefaults(List<QuotedProduct> qps,JSONObject typesJson) {
		JSONObject dfjs = new JSONObject();
		dfjs.put("qp", qps.get(0));
		dfjs.put("coachId", qps.get(0).getCoachId());
		ProductType ptType = qps.get(0).getProduct().getProductType();
		dfjs.put("pro_type", typesJson.get("v"+ptType.getCode()));
		return dfjs;
	}

	//获取product Ids
	private List<Long> getProductIds(String typeCode) {
		// 默认 最低价格 服务类型 大神
		List<ProductType> v2 = productTypeService.findByParentCode(Integer.parseInt(typeCode));
		// 一级服务类型下 服务Ids
		List<Long> ptIds = new ArrayList<>();
		for (ProductType p2 : v2) {
			List<ProductType> v3 = productTypeService.findByParentCode(p2.getCode());
			for (ProductType p3 : v3) {
				List<ProductType> v4 = productTypeService.findByParentCode(p3.getCode());
				if (v4.size() > 0) {
					for (ProductType p4 : v4) {
						ptIds.add(p4.getId());
					}
				} else {
					ptIds.add(p3.getId());
				}
			}
		}
		//获取product Ids
		List<Long> pIds = productService.findByProductTypeIdIn(ptIds);
		
		return pIds;
	}

	/**
	 * 服务类型详情 类型II
	 * 
	 * @param request
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/detail2", method = RequestMethod.GET)
	public JSONObject type2(HttpServletRequest request) {
		List<JSONObject> json_list = new ArrayList<>();
		JSONObject jsonObject = new JSONObject();
		List<String> codesList = Arrays.asList("11", "12");
		for (String typeCode : codesList) {
			JSONObject json = new JSONObject();
			ProductType productType = productTypeService.findByCode(Integer.parseInt(typeCode));
			if (productType == null || (productType != null && productType.getLevel() != 1)) {
				return JsonUtils.getFailJSONObject("参数错误!");
			}
			// 服务类型资源文件列表
			List<Resource> list = resourceService.findByProductTypeId(productType.getId(), false);
			json.put("resource", list);
			// 统计 四大服务类型 数据
			json.put("statis", this.formatDatas());
			// 课程表 前端固定图片
			// 其他详情 前端固定图片
			// 服务类型 默认价格 默认教练
			ProductType p = productTypeService.findByCode(Integer.valueOf(typeCode));
			json.put("v1", p);
			json.put("types", this.formatProductTypesByProductType(Integer.valueOf(typeCode)));
			List<Coach> coachs = coachService.findAll();
			json.put("coach", coachs);
			// 课程 暂无
			// json.put("course", null);
			json_list.add(json);
		}

		List<ProductType> types = productTypeService.findAll();
		JSONObject typesJson = this.formatTypes(types);
		jsonObject.put("product_type", typesJson);

		jsonObject.put("pre_path", resourceService.getShowFilePath());

		jsonObject.put("list", json_list);

		return JsonUtils.getSuccessJSONObject(jsonObject);
	}

	// 统计 四大服务类型 数据
	private List<StatisDataModel> formatDatas() {
		List<StatisDataModel> list = new ArrayList<>();
		List<StatisDataModel> datas = orderStatisService.findEntiresData();
		for (StatisDataModel statisDataModel : datas) {
			int code = this.validProductType(productTypeService.findOne(statisDataModel.getProductTypeId()));
			StatisDataModel sDataModel = new StatisDataModel(statisDataModel.getProductTypeId(),
					statisDataModel.getCounts(), statisDataModel.getHours(), code);
			list.add(sDataModel);
		}
		return list;
	}

	// 校验 服务类型level 返回code
	private int validProductType(ProductType pType) {
		if (pType.getLevel() == 1) {
			return pType.getCode();
		} else {
			ProductType p = productTypeService.findByCode(pType.getParentCode());
			int code = this.validProductType(p);
			return code;
		}

	}

	// 服务类型 层级 二级开始 ---- 服务类型进入
	private List<JSONObject> formatProductTypesByProductType(int code) {
		List<ProductType> v2List = productTypeService.findByParentCode(code);
		List<JSONObject> list2 = new ArrayList<>();
		for (ProductType v2 : v2List) {
			JSONObject json_v2 = new JSONObject();
			json_v2.put("v2_code", v2.getCode());
			json_v2.put("v2_name", v2.getName());

			List<ProductType> v3List = productTypeService.findByParentCode(v2.getCode());
			List<JSONObject> list3 = new ArrayList<>();

			for (ProductType v3 : v3List) {
				JSONObject json_v3 = new JSONObject();
				json_v3.put("v3_code", v3.getCode());
				json_v3.put("v3_name", v3.getName());

				List<ProductType> v4List = productTypeService.findByParentCode(v3.getCode());
				List<JSONObject> list4 = new ArrayList<>();
				if (v4List.size() > 0) {
					for (ProductType v4 : v4List) {
						JSONObject json_v4 = new JSONObject();
						json_v4.put("v4_code", v4.getCode());
						json_v4.put("v4_name", v4.getName());
						json_v4.put("content", StringUtils.isBlank(v4.getRemark()) ? v4.getName() : v4.getRemark());
//						QuotedProduct q = quotedProductService.findByCoachIdAndProductProductTypeCode(1l, v4.getCode());
//						json_v4.put("default_price", q.getPrice());
//						json_v4.put("default_coach", 1l);
//						List<Course> course = courseService.findByProductTypeIdAndCoachId(v4.getId(), 1l);
//						json_v4.put("course", this.formatCourse(course));
						list4.add(json_v4);
					}
					json_v3.put("v4", list4);
				} else {
//					QuotedProduct q = quotedProductService.findByCoachIdAndProductProductTypeCode(1l, v3.getCode());
//					json_v3.put("default_price", q.getPrice());
//					json_v3.put("default_coach", 1l);
//					List<Course> course = courseService.findByProductTypeIdAndCoachId(v3.getId(), 1l);
//					json_v3.put("course", this.formatCourse(course));
					json_v3.put("content", StringUtils.isBlank(v3.getRemark()) ? v3.getName() : v3.getRemark());
					json_v3.put("v4", null);
				}
				list3.add(json_v3);
			}
			json_v2.put("v3", list3);
			list2.add(json_v2);
		}

		return list2;
	}

	/**
	 * 根据大神最低价进行大神排序
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/coachs", method = RequestMethod.GET)
	public JSONObject getCoachs(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		Sort sort = new Sort(Direction.DESC, "creation");
		Pageable pageable = new PageRequest(0, 20, sort);

		Page<QuotedProduct> page = quotedProductService.findMinPriceByGroup(pageable);
		json.put("page", page);

		return JsonUtils.getSuccessJSONObject(json);
	}

	/**
	 * 获取大神详情
	 * 
	 * @param coachId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/coach_detail", method = RequestMethod.GET)
	public JSONObject coachDetail(@RequestParam(value = "coachId", required = false) String coachId,
			HttpServletRequest request) {
		JSONObject json = new JSONObject();
		// 教练详情
		Coach coach = coachService.findById(Long.parseLong(coachId));
		json.put("coach", coach);
		// 课程
		// json.put("course", null);
		// 资源列表
		List<Resource> resources = resourceService.findByCoachId(coach.getId(), false);
		json.put("resource", resources);
		// 精彩视频
		List<Resource> resources2 = resourceService.findByCoachIdAndResorseType(coach.getId(), ResorceTypes.Vid, false);
		json.put("videos", resources2);

		List<ProductType> list = productTypeService.findByParentCode(1);
		List<JSONObject> v1List = new ArrayList<>();
		for (ProductType v1 : list) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("v1_code", v1.getCode());
			jsonObject.put("v1_name", v1.getName());
			List<JSONObject> l2 = this.formatProductTypesByCoach(v1.getCode(), coach.getId());
			jsonObject.put("v2", l2);
			v1List.add(jsonObject);
		}
		// 服务类型
		json.put("types", v1List);
		// 数据统计
		StatisDataModel datas = orderStatisService.findDataByCoachId(Long.parseLong(coachId));
		json.put("statis", datas);

		List<ProductType> types = productTypeService.findAll();
		JSONObject typesJson = this.formatTypes(types);
		json.put("product_type", typesJson);

		json.put("pre_path", resourceService.getShowFilePath());

		return JsonUtils.getSuccessJSONObject(json);
	}

	// 服务类型 层级 二级开始 ---- 大神列表进入
	private List<JSONObject> formatProductTypesByCoach(int code, Long coachId) {
		List<ProductType> v2List = productTypeService.findByParentCode(code);
		List<JSONObject> list2 = new ArrayList<>();
		for (ProductType v2 : v2List) {
			JSONObject json_v2 = new JSONObject();
			json_v2.put("v2_code", v2.getCode());
			json_v2.put("v2_name", v2.getName());

			List<ProductType> v3List = productTypeService.findByParentCode(v2.getCode());
			List<JSONObject> list3 = new ArrayList<>();

			for (ProductType v3 : v3List) {
				JSONObject json_v3 = new JSONObject();
				json_v3.put("v3_code", v3.getCode());
				json_v3.put("v3_name", v3.getName());

				List<ProductType> v4List = productTypeService.findByParentCode(v3.getCode());
				List<JSONObject> list4 = new ArrayList<>();
				if (v4List.size() > 0) {
					for (ProductType v4 : v4List) {
						JSONObject json_v4 = new JSONObject();
						json_v4.put("v4_code", v4.getCode());
						json_v4.put("v4_name", v4.getName());
						json_v4.put("content", StringUtils.isBlank(v4.getRemark()) ? v4.getName() : v4.getRemark());
						QuotedProduct q = quotedProductService.findByCoachIdAndProductProductTypeCode(coachId,
								v4.getCode());
						json_v4.put("default_price", q.getPrice());
						List<Course> course = courseService.findByProductTypeIdAndCoachId(v4.getId(), coachId);

						json_v4.put("course", this.formatCourse(course));
						list4.add(json_v4);
					}
					json_v3.put("v4", list4);
				} else {
					QuotedProduct q = quotedProductService.findByCoachIdAndProductProductTypeCode(coachId,
							v3.getCode());
					json_v3.put("default_price", q.getPrice());
					List<Course> course = courseService.findByProductTypeIdAndCoachId(v3.getId(), coachId);
					json_v3.put("course", this.formatCourse(course));
					json_v3.put("content", StringUtils.isBlank(v3.getRemark()) ? v3.getName() : v3.getRemark());
					json_v3.put("v4", null);
				}
				list3.add(json_v3);
			}
			json_v2.put("v3", list3);
			list2.add(json_v2);
		}
		return list2;
	}

	private List<JSONObject> formatCourse(List<Course> course) {
		List<JSONObject> list = new ArrayList<>();
		for (Course c : course) {
			JSONObject json = new JSONObject();
			json.put("id", c.getId());
			json.put("name", c.getName());
			list.add(json);
		}
		return list;
	}

	/**
	 * 获取课程
	 * 
	 * @param coachId
	 * @param code
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/course", method = RequestMethod.GET)
	public JSONObject getCourse(@RequestParam(value = "coachId") String coachId,
			@RequestParam(value = "code") String code, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		if (StringUtils.isBlank(coachId) || StringUtils.isBlank(code)) {
			return JsonUtils.getFailJSONObject("大神参数错误!");
		}
		ProductType productType = productTypeService.findByCode(Integer.valueOf(code));
		List<Course> courses = courseService.findByProductTypeIdAndCoachId(productType.getId(),
				Long.parseLong(coachId));
		json.put("course", courses);
		return JsonUtils.getSuccessJSONObject(json);
	}

	// 服务类型 层级
	private JSONObject formatTypes(List<ProductType> types) {
		JSONObject json = new JSONObject();
		if (types != null && types.size() > 0) {
			Map<Integer, String> level1Map = new HashMap<>();// level 1 的 code 和 name
			Map<Integer, String> level2Map = new HashMap<>();// level 2 的 code 和 name
			Map<Integer, String> level3Map = new HashMap<>();// level 3 的 code 和 name
			Map<Integer, String> level4Map = new HashMap<>();// level 4 的 code 和 name
			Map<Integer, Integer> level2CodeMap = new HashMap<>();// level 2 的 code 和 parentCode
			Map<Integer, Integer> level3CodeMap = new HashMap<>();// level 3 的 code 和 parentCode
			Map<Integer, Integer> level4CodeMap = new HashMap<>();// level 4 的 code 和 parentCode
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
			for (ProductType t : types) {
				if (t.getLevel() == 3) {
					if (level4CodeMap.values().contains(t.getCode())) {
						List<ProductType> pTypes = productTypeService.findByParentCode(t.getCode());
						for (ProductType pType : pTypes) {

							JSONObject typeJson = new JSONObject();
							typeJson.put("v4_code", pType.getCode());
							typeJson.put("v4_name", pType.getName());

							Integer level3code = level4CodeMap.get(pType.getCode());
							typeJson.put("v3_code", level3code);
							typeJson.put("v3_name", level3Map.get(level3code));

							Integer level2Code = level3CodeMap.get(level3code);
							typeJson.put("v2_code", level2Code);
							typeJson.put("v2_name", level2Map.get(level2Code));

							Integer level1Code = level2CodeMap.get(level2Code);
							typeJson.put("v1_code", level1Code);
							typeJson.put("v1_name", level1Map.get(level1Code));
							json.put("v" + pType.getCode(), typeJson);
						}
					} else {
						JSONObject typeJson = new JSONObject();
						typeJson.put("v3_code", t.getCode());
						typeJson.put("v3_name", t.getName());
						Integer level2Code = t.getParentCode();
						typeJson.put("v2_code", level2Code);
						typeJson.put("v2_name", level2Map.get(level2Code));
						Integer level1Code = level2CodeMap.get(level2Code);
						typeJson.put("v1_code", level1Code);
						typeJson.put("v1_name", level1Map.get(level1Code));
						json.put("v" + t.getCode(), typeJson);
					}
				}
			}
		}
		return json;
	}

}
