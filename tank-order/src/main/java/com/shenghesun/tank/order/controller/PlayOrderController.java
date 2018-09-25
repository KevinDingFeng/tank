package com.shenghesun.tank.order.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.coach.CoachService;
import com.shenghesun.tank.coach.entity.Coach;
import com.shenghesun.tank.order.PlayOrderService;
import com.shenghesun.tank.order.entity.PlayOrder;
import com.shenghesun.tank.order.entity.PlayOrder.OperationType;
import com.shenghesun.tank.order.entity.PlayOrder.PlayOrderStatus;
import com.shenghesun.tank.service.ProductTypeService;
import com.shenghesun.tank.service.QuotedProductService;
import com.shenghesun.tank.service.entity.Product;
import com.shenghesun.tank.service.entity.ProductType;
import com.shenghesun.tank.service.entity.QuotedProduct;
import com.shenghesun.tank.sso.model.LoginInfo;
import com.shenghesun.tank.utils.JsonUtils;
import com.shenghesun.tank.utils.RandomUtil;
import com.shenghesun.tank.wx.WxUserInfoService;
import com.shenghesun.tank.wx.entity.WxUserInfo;
import com.shenghesun.tank.wxpay.sdk.WXPay;
import com.shenghesun.tank.wxpay.sdk.WXPayConfig;
import com.shenghesun.tank.wxpay.sdk.WXPayUtil;
import com.shenghesun.tank.wxpay.sdk.impl.WXPayConfigImpl;

@RestController
@RequestMapping(value = "/order")
public class PlayOrderController {

	@Autowired
	private PlayOrderService playOrderService;
	@Autowired
	private QuotedProductService quotedProductService;
	@Autowired
	private CoachService coachService;
	@Autowired
	private ProductTypeService productTypeService;
	@Autowired
	private WxUserInfoService wxUserService;

	/**
	 * 设置允许自动绑定的属性名称
	 * 
	 * @param binder
	 * @param req
	 */
	@InitBinder("entity")
	private void initBinder(ServletRequestDataBinder binder, HttpServletRequest req) {
		List<String> fields = new ArrayList<String>(Arrays.asList("wxAccount", "cellphone", "remark", "duration"));
		switch (req.getMethod().toLowerCase()) {
		case "post": // 新增 和 修改
			binder.setAllowedFields(fields.toArray(new String[fields.size()]));
			break;
		default:
			break;
		}
	}

	/**
	 * 预处理，一般用于新增和修改表单提交后的预处理
	 * 
	 * @param id
	 * @param req
	 * @return
	 */
	@ModelAttribute("entity")
	public PlayOrder prepare(@RequestParam(value = "id", required = false) Long id, HttpServletRequest req) {
		String method = req.getMethod().toLowerCase();
		if (id != null && id > 0 && "post".equals(method)) {// 修改表单提交后数据绑定之前执行
			return playOrderService.findById(id);
		} else if ("post".equals(method)) {// 新增表单提交后数据绑定之前执行
			return new PlayOrder();
		} else {
			return null;
		}
	}

	/**
	 * 根据具体报价id，生成预订单信息
	 * 
	 * @param quotedProductId
	 * @return
	 */
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public JSONObject form(@RequestParam(value = "quotedProductId") Long quotedProductId) {
		QuotedProduct qp = quotedProductService.findById(quotedProductId);
		Coach coach = qp.getCoach();// 大神
		Product product = qp.getProduct();// 服务

		JSONObject json = new JSONObject();
		json.put("coach", coach);
		json.put("coaches", coachService.findAll(new Sort(Direction.DESC, "seqNum")));// 获取所有大神-默认所有大神都可以支持所有类型的服务
		ProductType typeLevel3 = product.getProductType();// level 3 的类型，parentCode 属性是 level 2的类型的 code
		ProductType typeLevel2 = productTypeService.findByCode(typeLevel3.getParentCode());
		// 获取 level 2 的类型集合
		// level 2 的 parentCode 属性是 level 1 的类型的 code
		List<ProductType> typeLevel2List = productTypeService.findByParentCode(typeLevel2.getParentCode());

		// 获取 level 3 的类型集合，获取当前 level 2 对应的集合就可以
		List<ProductType> typeLevel3List = productTypeService.findByParentCode(typeLevel3.getParentCode());
		json.put("typeLevel2List", typeLevel2List);
		json.put("typeLevel3List", typeLevel3List);

		json.put("product", product);// 服务
		json.put("quotedProduct", qp);// 具体报价
		return JsonUtils.getSuccessJSONObject(json);
	}

	private void initPlayOrder(PlayOrder playOrder, Coach coach, Product product) {

		LoginInfo info = (LoginInfo) SecurityUtils.getSubject().getPrincipal();
		
		WxUserInfo wxUser = wxUserService.findById(info.getWxUserId());// 当前登录用户

		// Coach coach = qp.getCoach();
		// Product product = qp.getProduct();

		// Coach coach = coachService.findById(coachId);
		// Product product = productService.findById(productId);

		playOrder.setWxUser(wxUser);
		playOrder.setWxUserId(wxUser.getId());

		playOrder.setCoach(coach);
		playOrder.setCoachId(coach.getId());

		playOrder.setExecutor(coach);
		playOrder.setExecutorId(coach.getId());

		playOrder.setProduct(product);
		playOrder.setProductId(product.getId());

		playOrder.setOperationType(OperationType.Create);

		String no = this.getNo();
		playOrder.setNo(no);
		playOrder.setViceNo(no + 1);

		// playOrder.setTotalFee(quotedProductService.getTotalFee(playOrder.getDuration(),
		// qp.getPrice(),
		// qp.getProduct().getDuration()));
	}

	private String getNo() {
		String no = System.currentTimeMillis() + RandomUtil.randomString(3);
		return no;
	}

	/**
	 * 生成订单，调用微信统一下单接口，生成预支付 id 返回到前端
	 * 
	 * @param playOrder
	 * @param result
	 * @param quotedProductId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public JSONObject save(HttpServletRequest request, @Validated @ModelAttribute("entity") PlayOrder playOrder,
			BindingResult result,
			// @RequestParam(value = "coachId") Long coachId,
			// @RequestParam(value = "productId") Long productId
			@RequestParam(value = "quotedProductId") Long qpId) throws Exception {

		if (result.hasErrors()) {
			return JsonUtils.getFailJSONObject("提交信息有误");
		}
		QuotedProduct qp = quotedProductService.findById(qpId);
		Product product = qp.getProduct();
		this.initPlayOrder(playOrder, qp.getCoach(), product);
		ProductType typeLevel3 = productTypeService.findByCode(product.getProductType().getCode());
		ProductType typeLevel2 = productTypeService.findByCode(typeLevel3.getParentCode());
		BigDecimal totalFee = BigDecimal.ZERO;
		if (typeLevel2.getParentCode() == 11) {
			totalFee = qp.getPrice();
		} else {
			totalFee = quotedProductService.getTotalFee(playOrder.getDuration(), qp.getPrice(),
					qp.getProduct().getDuration());
		}
		playOrder.setTotalFee(totalFee);
		playOrder = playOrderService.save(playOrder);

		// 调用统一下单流程
		// QuotedProduct qp = quotedProductService.findById(quotedProductId);
		// BigDecimal totalFee =
		// quotedProductService.getTotalFee(playOrder.getDuration(), qp.getPrice(),
		// qp.getProduct().getDuration());
		String totalFeeStr = this.getTotalFeeStr(playOrder.getTotalFee());
		String openId = request.getHeader("openId");
		JSONObject prepay = this.prepay(playOrder.getNo(), openId, request.getRemoteAddr(), totalFeeStr);
		if (prepay.getString("result") != null) {
			System.out.println("预支付时出现错误");
			return JsonUtils.getFailJSONObject(prepay.getString("result"));// 预支付时出现错误
		}
		JSONObject json = new JSONObject();
		json.put("playOrder", playOrder);
		json.put("prepay", prepay);
		return JsonUtils.getSuccessJSONObject(json);
	}

	@RequestMapping(value = "/pay", method = RequestMethod.POST)
	public JSONObject pay(HttpServletRequest request, @RequestParam(value = "no") String no) throws Exception {

		PlayOrder playOrder = playOrderService.findMainByNo(no);
		String openId = request.getHeader("openId");

		String totalFeeStr = this.getTotalFeeStr(playOrder.getTotalFee());
		JSONObject prepay = this.prepay(playOrder.getNo(), openId, request.getRemoteAddr(), totalFeeStr);
		if (prepay.getString("result") != null) {
			System.out.println("预支付时出现错误");
			return JsonUtils.getFailJSONObject(prepay.getString("result"));// 预支付时出现错误
		}
		JSONObject json = new JSONObject();
		json.put("playOrder", playOrder);
		json.put("prepay", prepay);
		return JsonUtils.getSuccessJSONObject(json);
	}

	private JSONObject prepay(String orderNo, String openId, String ip, String totalFee) throws Exception {
		// 组织统一下单的数据，完成统一下单
		Map<String, String> map = this.getUnifiedOrderData(orderNo, openId, ip, totalFee);
		WXPayConfig conf = new WXPayConfigImpl();
		WXPay wxPay = new WXPay(conf, "https://wxpay.dazonghetong.com/wxpay/notify");
		Map<String, String> resultMap = wxPay.unifiedOrder(map);
		// 解析统一下单返回的信息，生成唤醒微信支付的数据
		String returnCode = (String) resultMap.get("return_code");// 通信标识
		if ("SUCCESS".equals(returnCode.toUpperCase())) {
			String resultCode = (String) resultMap.get("result_code");// 交易标识
			if ("SUCCESS".equals(resultCode.toUpperCase())) {
				String appId = (String) resultMap.get("appid");// 微信公众号AppId
				String prepayId = "prepay_id=" + resultMap.get("prepay_id");// 统一下单返回的预支付id
				String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);// 当前时间戳
				String nonceStr = RandomUtil.randomString(20);// 不长于32位的随机字符串
				String signType = "MD5";
				SortedMap<String, String> signMap = this.getSignMap(appId, prepayId, timeStamp, nonceStr, signType);
				JSONObject json = this.getPrepayJson(appId, prepayId, timeStamp, nonceStr, signType, signMap,
						conf.getKey());
				return json;
			} else {
				String errDes = (String) resultMap.get("err_code_des");// 交易错误信息
				JSONObject json = new JSONObject();
				json.put("result", errDes);
				return json;
			}
		} else {
			String returnMsg = (String) resultMap.get("return_msg");// 通信错误信息
			JSONObject json = new JSONObject();
			json.put("result", returnMsg);
			return json;
		}
	}

	private Map<String, String> getUnifiedOrderData(String orderNo, String openId, String ip, String totalFee) {
		Map<String, String> map = new HashMap<>();
		map.put("openid", openId);// 用户标识openId
		map.put("spbill_create_ip", ip);// 请求Ip地址
		map.put("body", "私人订制电竞服务-游戏");// 商品描述 body 商家名称-销售商品类目
		map.put("out_trade_no", orderNo);// 商户订单号 out_trade_no
		map.put("total_fee", totalFee);// 标价金额 total_fee
		map.put("trade_type", "JSAPI");// 交易类型 trade_type
		return map;
	}

	private SortedMap<String, String> getSignMap(String appId, String prepayId, String timeStamp, String nonceStr,
			String signType) {
		SortedMap<String, String> signMap = new TreeMap<>();// 自然升序map
		signMap.put("appId", appId);
		signMap.put("package", prepayId);
		signMap.put("timeStamp", timeStamp);
		signMap.put("nonceStr", nonceStr);
		signMap.put("signType", signType);
		return signMap;
	}

	private JSONObject getPrepayJson(String appId, String prepayId, String timeStamp, String nonceStr, String signType,
			SortedMap<String, String> signMap, String key) throws Exception {
		JSONObject json = new JSONObject();
		json.put("appId", appId);
		json.put("timeStamp", timeStamp);
		json.put("nonceStr", nonceStr);
		json.put("prepayId", prepayId);
		json.put("signType", signType);
		json.put("paySign", WXPayUtil.generateSignature(signMap, key));
		return json;
	}

	private String getTotalFeeStr(BigDecimal totalFee) {
		totalFee = totalFee.multiply(new BigDecimal(100));// 转 元 为 角
		totalFee = totalFee.setScale(0, BigDecimal.ROUND_HALF_UP);

		return totalFee.intValue() + "";
	}

	// 获取订单列表，根据普通用户id
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public JSONObject list(HttpServletRequest request) {
//		Long wxUserId = 2L;// 
		LoginInfo info = (LoginInfo) SecurityUtils.getSubject().getPrincipal();
		Long wxUserId = info.getWxUserId();
		Pageable pageable = this.getPageable();
		Page<PlayOrder> page = playOrderService.findByWxUserId(wxUserId, pageable);

		List<ProductType> types = productTypeService.findAll();
		JSONObject typesJson = this.formatTypes(types);

		JSONObject json = new JSONObject();
		json.put("page", page);
		json.put("types", typesJson);
		return JsonUtils.getSuccessJSONObject(json);
	}

	private JSONObject formatTypes(List<ProductType> types) {
		JSONObject json = new JSONObject();
		// JSONObject typesLevel1 = new JSONObject();
		// JSONObject typesLevel2 = new JSONObject();
		// JSONObject typesLevel3 = new JSONObject();
		if (types != null && types.size() > 0) {
			Map<Integer, String> level1Map = new HashMap<>();// level 1 的 code 和 name
			Map<Integer, String> level2Map = new HashMap<>();// level 2 的 code 和 name
			Map<Integer, Integer> level2CodeMap = new HashMap<>();// level 2 的 code 和 parentCode
			// Map<Integer, String> level3Map = new HashMap<>();
			for (ProductType t : types) {
				switch (t.getLevel()) {
				case 1:
					level1Map.put(t.getCode(), t.getName());
					break;
				case 2:
					level2Map.put(t.getCode(), t.getName());
					level2CodeMap.put(t.getCode(), t.getParentCode());
					break;
				default:
					break;
				}
			}
			for (ProductType t : types) {
				if(t.getLevel() == 3) {
					JSONObject typeJson = new JSONObject();
					typeJson.put("code", t.getCode());
					typeJson.put("name", t.getName());
					Integer level2Code = t.getParentCode();
					typeJson.put("level2Code", level2Code);
					typeJson.put("level2Name", level2Map.get(level2Code));
					Integer level1Code = level2CodeMap.get(level2Code);
					typeJson.put("level1Code", level1Code);
					typeJson.put("level1Name", level1Map.get(level1Code));
					json.put("t" + t.getId(), typeJson);
				}
			}
		}
		// json.put("typesLevel1", typesLevel1);
		// json.put("typesLevel2", typesLevel2);
		// json.put("typesLevel3", typesLevel3);
		return json;
	}

//	private JSONObject getTypeJson(int code, String name, int parentCode) {
//		JSONObject json = new JSONObject();
//		json.put("code", code);
//		json.put("name", name);
//		json.put("parentCode", parentCode);
//		return json;
//	}

	private Pageable getPageable() {
		Sort sort = new Sort(Direction.DESC, "creation");
		Pageable pageable = new PageRequest(0, 20, sort);
		return pageable;
	}

	// 获取订单详细信息，根据订单编号和普通用户id
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	public JSONObject info(HttpServletRequest request, @PathVariable(value = "id") Long id) {
		// TODO 校验 资源权限
		
		PlayOrder order = playOrderService.findById(id);
		ProductType typeLevel3 = order.getProduct().getProductType();
		ProductType typeLevel2 = productTypeService.findByCode(typeLevel3.getParentCode());
		ProductType typeLevel1 = productTypeService.findByCode(typeLevel2.getParentCode());
		JSONObject json = new JSONObject();
		json.put("playOrder", order);
		json.put("typeLevel1", typeLevel1.getName());
		json.put("typeLevel2", typeLevel2.getName());
		json.put("typeLevel3", typeLevel3.getName());
		return JsonUtils.getSuccessJSONObject(json);
	}
	
	@RequestMapping(value = "/exe/{id}", method = RequestMethod.POST)
	public JSONObject exeComplete(HttpServletRequest request, @PathVariable("id") Long id) {
		// 校验当前登录用户是否有权限操作 该 订单 TODO
//		Subject subject = SecurityUtils.getSubject();
//		if(!subject.isPermitted("order:receive")) {
//			throw new AuthorizationException("缺少接单权限");
//		}
		PlayOrder order = playOrderService.findById(id);
//		LoginInfo info = (LoginInfo)subject.getPrincipal();
//		Long loginUserId = info.getId();
//		if(order.getExecutor().getSysUserId().longValue() != loginUserId.longValue()) {
//			throw new AuthorizationException("操作的订单不属于当前登录用户");
//		}
		order.setStatus(PlayOrderStatus.Complete);
		playOrderService.save(order);
		
		return JsonUtils.getSuccessJSONObject();
	}

}
