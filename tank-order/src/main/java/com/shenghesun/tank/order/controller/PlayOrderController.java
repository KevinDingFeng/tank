package com.shenghesun.tank.order.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.shenghesun.tank.service.ProductService;
import com.shenghesun.tank.service.ProductTypeService;
import com.shenghesun.tank.service.QuotedProductService;
import com.shenghesun.tank.service.entity.Product;
import com.shenghesun.tank.service.entity.ProductType;
import com.shenghesun.tank.service.entity.QuotedProduct;
import com.shenghesun.tank.utils.JsonUtils;
import com.shenghesun.tank.wx.WxUserInfoService;
import com.shenghesun.tank.wx.entity.WxUserInfo;

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
	@Autowired
	private ProductService productService;

	/**
	 * 设置允许自动绑定的属性名称
	 * 
	 * @param binder
	 * @param req
	 */
	@InitBinder("entity")
	private void initBinder(ServletRequestDataBinder binder,
			HttpServletRequest req) {
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
	public PlayOrder prepare(
			@RequestParam(value = "id", required = false) Long id,
			HttpServletRequest req) {
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
	 * @param quotedProductId
	 * @return
	 */
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public JSONObject form(
			@RequestParam(value = "quotedProductId") Long quotedProductId) {
		QuotedProduct qp = quotedProductService.findById(quotedProductId);
		Coach coach = qp.getCoach();//大神
		Product product = qp.getProduct();//服务
		
		
		JSONObject json = new JSONObject();
		json.put("coach", coach);
		json.put("coaches", coachService.findAll(new Sort(Direction.DESC, "seqNum")));//获取所有大神-默认所有大神都可以支持所有类型的服务
		ProductType typeLevel3 = product.getProductType();//level 3 的类型，parentCode 属性是 level 2的类型的 code
		ProductType typeLevel2 = productTypeService.findByCode(typeLevel3.getParentCode());
		//获取 level 2 的类型集合
		//level 2 的 parentCode 属性是 level 1 的类型的 code
		List<ProductType> typeLevel2List = productTypeService.findByParentCode(typeLevel2.getParentCode()); 
		
		//获取 level 3 的类型集合，获取当前 level 2 对应的集合就可以
		List<ProductType> typeLevel3List = productTypeService.findByParentCode(typeLevel3.getParentCode());
		json.put("typeLevel2List", typeLevel2List);
		json.put("typeLevel3List", typeLevel3List);
		
		json.put("product", product);//服务
		json.put("quotedProduct", qp);//具体报价
		return JsonUtils.getSuccessJSONObject(json);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public JSONObject save(@Validated @ModelAttribute("entity") PlayOrder playOrder, BindingResult result, 
//			@RequestParam(value = "coachId") Long coachId,
//			@RequestParam(value = "productId") Long productId
			@RequestParam(value = "quotedProductId") Long quotedProductId 
			) {
		
		if(result.hasErrors()) {
			return JsonUtils.getFailJSONObject("提交信息有误");
		}
		
		WxUserInfo wxUser = wxUserService.findById(1L);
		
		QuotedProduct qp = quotedProductService.findById(quotedProductId);
		Coach coach = qp.getCoach();
		Product product = qp.getProduct();
		
//		Coach coach = coachService.findById(coachId);
//		Product product = productService.findById(productId);
		
		playOrder.setWxUser(wxUser);
		playOrder.setWxUserId(wxUser.getId());
		
		playOrder.setCoach(coach);
		playOrder.setCoachId(coach.getId());
		
		playOrder.setExecutor(coach);
		playOrder.setExecutorId(coach.getId());
		
		playOrder.setProduct(product);
		playOrder.setProductId(product.getId());

		playOrder.setOperationType(OperationType.Create);
		
		
		
		playOrder.setNo(playOrderService.getNO());
		playOrder.setViceNo(playOrderService.getViceNo());
		
		
		playOrder.setTotalFee(quotedProductService.getTotalFee(playOrder.getDuration(), qp.getPrice(), 
				qp.getProduct().getDuration()));
		playOrder = playOrderService.save(playOrder);
		

		
		//调用统一下单流程 TODO

		JSONObject json = new JSONObject();
		json.put("playOrder", playOrder);
		return JsonUtils.getSuccessJSONObject(json);
	}
			
}
