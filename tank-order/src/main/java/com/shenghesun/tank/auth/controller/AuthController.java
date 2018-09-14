package com.shenghesun.tank.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shenghesun.tank.http.utils.MyWechatParam;
import com.shenghesun.tank.http.utils.MyWechatSignatureUtil;
import com.shenghesun.tank.system.SysUserService;
import com.shenghesun.tank.system.entity.SysUser;
import com.shenghesun.tank.utils.JsonUtils;
import com.shenghesun.tank.utils.RedisUtils;
import com.shenghesun.tank.wx.WxUserInfoService;
import com.shenghesun.tank.wx.auth.service.WxAuthServiceImpl;
import com.shenghesun.tank.wx.entity.WxUserInfo;

@RestController
@RequestMapping("/auth")
public class AuthController {

	/**
	 * ***************重点*************** 用于微信公共平台验证接口使用， 80 端口
	 * 	公众号平台-基础配置，服务器配置的域名设置
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/myWechat", method = { RequestMethod.GET, RequestMethod.POST })
	public String wechat(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		System.out.println("signature = " + signature + "; timestamp = " + timestamp + "; nonce = " + nonce
				+ "; echostr = " + echostr + ";");
		return MyWechatSignatureUtil.checkSignature(signature, timestamp, nonce, MyWechatParam.TOKEN) ? echostr
				: "false";
	}

	public static final String STATE_KEY = "state_";
	public static final String OPEN_ID_KEY = "open_id_";
	@Autowired
	private WxAuthServiceImpl wxService;
	// @Autowired
	// private DefaultAuthServiceImpl wxService;
	@Autowired
	private RedisUtils redisUtils;

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private WxUserInfoService wxUserInfoService;


	@RequestMapping(value = "/callback_by_code", method = { RequestMethod.GET, RequestMethod.POST })
	public JSONObject wxAuthCallbackByCode(HttpServletRequest req, HttpServletResponse res,
			@RequestParam("code") String code, @RequestParam("state") String state) {
		try {
			// 授权步骤结束后返回的参数包括 code 和 state
			// String state = req.getParameter("state");
			// 根据 code 获取用户的 openId 和 accessToken
			JSONObject tokenMsg = wxService.getAuthToken(code);
			if (tokenMsg == null) {
				return JsonUtils.getFailJSONObject("获取 openId 出错");
			}
			String openId = tokenMsg.getString("openid");
			String token = tokenMsg.getString("access_token");
			// 保存 state 和 openId 的匹配记录
			redisUtils.set(STATE_KEY + state, openId, 300L);
			// 根据 accessToke 和 opernId 获取用户的基础信息
			JSONObject userInfoJson = wxService.getUserInfoJson(token, openId);
			if (userInfoJson == null) {
				return JsonUtils.getFailJSONObject("获取用户基础信息出错");
			}
			System.out.println(userInfoJson.toString());
			// 保存 userInfo 到数据库，使用 openId 和用户信息匹配 TODO 正式 使用时需要修改逻辑
			// redisUtils.set(OPEN_ID_KEY + openId, userInfoJson.toString(), 86400L);
			SysUser sysUser = sysUserService.findByOpenId(openId);
			if (sysUser == null) {
				sysUser = wxService.getWXSysUser(userInfoJson);
			}
			sysUser = sysUserService.save(sysUser);
//			SecurityUtils.getSubject().login(new UsernamePasswordToken(sysUser.getAccount(), sysUser.getPassword()));
			WxUserInfo wxUserInfo = wxUserInfoService.getOne(userInfoJson);
			wxUserInfo = wxUserInfoService.save(wxUserInfo);
			JSONObject json = new JSONObject();
			json.put("wxUser", wxUserInfo);
			return JsonUtils.getSuccessJSONObject(json);
//			JSONObject jsonObject = JsonUtils.getSuccessJSONObject(wxUserInfo);
//			jsonObject.put("userInfo", sysUser);
//			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtils.getFailJSONObject("后台获取用户信息发生错误");
		}
	}

	@RequestMapping(value = "/user_info", method = RequestMethod.GET)
	public JSONObject getUserInfo(HttpServletRequest req, HttpServletResponse res,
			@RequestParam(value = "state") String state) {
		if (redisUtils.exists(STATE_KEY + state)) {
			String openId = redisUtils.get(STATE_KEY + state);
			SysUser sysUser = sysUserService.findByOpenId(this.format(openId));
			if (sysUser != null) {
				return JsonUtils.getSuccessJSONObject(sysUser);
			} else {
				System.out.println("openId 不存在 " + openId);
			}
		} else {
			System.out.println("state 不存在 " + state);
		}
		return JsonUtils.getFailJSONObject();
	}

	@RequestMapping("/my_info")
	public JSONObject getMyInfo() {
//		Long userId = UserUtils.currentUserId();
//		if (userId == null) {
//			return JsonUtils.getFailJSONObject();
//		}
//		SysUser user = sysUserService.findOne(userId);
//		return JsonUtils.getSuccessJSONObject(user);
		return JsonUtils.getSuccessJSONObject();//TODO 使用 redis 等方法获取用户信息，需要请求把 token 或者 Openid 待入 header
	}

	@RequestMapping(value = "/binding_cellphone_no/{cellphoneNo}", method = RequestMethod.POST)
	public JSONObject alterCellphoneNo(@PathVariable String cellphoneNo, @RequestParam String code) {
//		if (sysUserService.alterCellphoneNo(cellphoneNo, code)) {
//			return JsonUtils.getSuccessJSONObject();
//		}
		return JsonUtils.getFailJSONObject("校验失败");
	}

	@RequestMapping("/check_cellphone/{cellphoneNo}")
	public JSONObject checkCellphone(@PathVariable String cellphoneNo) {
		return JsonUtils.getSuccessJSONObject();
//		try {
//			if (sysUserService.checkCellphone(cellphoneNo)) {
//				return JsonUtils.getSuccessJSONObject(true);
//			}
//			return JsonUtils.getSuccessJSONObject(false);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return JsonUtils.getFailJSONObject(e.getMessage());
//		}
	}

	private String format(String s) {
		if (s == null) {
			return null;
		}
		if (s.startsWith("\"")) {
			s = s.substring(1);
		}
		if (s.endsWith("\"")) {
			s = s.substring(0, s.length() - 1);
		}
		return s;
	}

//	@Autowired
//	private SysPoolService sysPoolService;
//
//	@RequestMapping(value = "/test", method = RequestMethod.GET)
//	public JSONObject test() {
//		Long poolId = sysPoolService.getDefaultId();
//		System.out.println(poolId);
//		SysPool sysPool = sysPoolService.findById(poolId);
//		return JsonUtils.getSuccessJSONObject(sysPool);
//	}
//	

	/**
	 * 大v完成授权，进入聚合页
	 * 
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
//	@RequestMapping(value = "/callback", method = { RequestMethod.GET, RequestMethod.POST })
//	public JSONObject wxAuthCallback(HttpServletRequest req, HttpServletResponse res) {
//		try {
//			// 授权步骤结束后返回的参数包括 code 和 state
//			String code = req.getParameter("code");
//			String state = req.getParameter("state");
//			// 根据 code 获取用户的 openId 和 accessToken
//			JSONObject tokenMsg = wxService.getAuthToken(code);
//			if (tokenMsg == null) {
//				return JsonUtils.getFailJSONObject("获取 openId 出错");
//			}
//			String openId = tokenMsg.getString("openid");
//			String token = tokenMsg.getString("access_token");
//			// 保存 state 和 openId 的匹配记录
//			redisUtils.set(STATE_KEY + state, openId, 300L);
//			// 根据 accessToke 和 opernId 获取用户的基础信息
//			JSONObject userInfoJson = wxService.getUserInfoJson(token, openId);
//			if (userInfoJson == null) {
//				return JsonUtils.getFailJSONObject("获取用户基础信息出错");
//			}
//			System.out.println(userInfoJson.toString());
//			// 保存 userInfo 到数据库，使用 openId 和用户信息匹配 TODO 正式 使用时需要修改逻辑
//			// redisUtils.set(OPEN_ID_KEY + openId, userInfoJson.toString(), 86400L);
//			SysUser sysUser = sysUserService.findByOpenId(openId);
//			if (sysUser == null) {
//				sysUser = wxService.getWXSysUser(userInfoJson);
//			}
//			sysUser = sysUserService.save(sysUser);
//			// SecurityUtils.getSubject().login(new UsernamePasswordToken(sysUser.getName(),
//			// sysUser.getPassword()));
//			WxUserInfo wxUserInfo = wxUserInfoService.getOne(userInfoJson);
//			wxUserInfo = wxUserInfoService.save(wxUserInfo);
//			return JsonUtils.getSuccessJSONObject(wxUserInfo);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return JsonUtils.getFailJSONObject("后台获取用户信息发生错误");
//		}
//	}
	// /**
	// * 用户完成授权，进入文章页
	// * @param req
	// * @param res
	// * @param artId
	// * @param openId
	// * @param model
	// * @return
	// */
	// @RequestMapping(value = "/art/{artId}/{openId}/", method = {
	// RequestMethod.GET, RequestMethod.POST })
	// public String toArt(HttpServletRequest req, HttpServletResponse res,
	// @PathVariable(value = "artId") String artId,
	// @PathVariable(value = "openId") String openId, Model model){
	// this.setModel(model, openId, artId);
	// String userId = "";
	// String userNickName = "";
	// try {
	// String code = req.getParameter("code");
	// JSONObject userInfo = this.getAuthorizedMsg(code);
	// userId = userInfo.getString("openid");
	// userNickName = userInfo.getString("nickname");
	// } catch (Exception e) {
	// e.printStackTrace();
	// model.addAttribute("msg", "请确认授权");
	// return "huiben/art_error";
	// }
	// try{
	// model.addAttribute("userId", userId);
	// WechatUser bigV = wxService.findByOpenIdAndRemoved(openId, false);
	// if(bigV != null){
	// WechatUserRel rel = this.getWechatUserRel(bigV, userId, userNickName, artId);
	// wxRelService.saveWithAudit(rel);
	// }
	// return "huiben/art";
	// }catch(Exception e){
	// e.printStackTrace();
	// model.addAttribute("msg", "请重新尝试");
	// return "huiben/art_error";
	// }
	// }
	// /**
	// * 用户完成授权，进入商城
	// * @param req
	// * @param res
	// * @param artId
	// * @param openId
	// * @param userId
	// * @param model
	// * @return
	// * @throws Exception
	// */
	// @RequestMapping(value = "/mall/{artId}/{openId}/{userId}/", method = {
	// RequestMethod.GET,
	// RequestMethod.POST })
	// public String toMall(HttpServletRequest req, HttpServletResponse res,
	// @PathVariable(value = "artId") String artId,
	// @PathVariable(value = "openId") String openId,
	// @PathVariable(value = "userId") String userId, Model model)
	// throws Exception {
	// this.setModel(model, openId, artId);
	// String wxUserId = "";
	// String userNickName = "";
	// try {
	// String code = req.getParameter("code");
	// JSONObject userInfo = this.getAuthorizedMsg(code);
	// wxUserId = userInfo.getString("openid");
	// userNickName = userInfo.getString("nickname");
	// } catch (Exception e) {
	// e.printStackTrace();
	// model.addAttribute("msg", "请确认授权");
	// return "huiben/mall_error";
	// }
	// try{
	// model.addAttribute("userId", wxUserId);
	// WechatUser bigV = wxService.findByOpenIdAndRemoved(openId, false);
	// if(bigV != null){
	// WechatUserRel rel = this.getWechatUserRel(bigV, wxUserId, userNickName,
	// artId);
	// wxRelService.saveWithAudit(rel);
	// }
	// return "huiben/mall";
	// }catch(Exception e){
	// e.printStackTrace();
	// model.addAttribute("msg", "请重新尝试");
	// return "huiben/mall_error";
	// }
	// }
	// private WechatUserRel getWechatUserRel(WechatUser bigV, String userId, String
	// userNickName, String artId){
	// WechatUserRel rel = wxRelService.findByUserOpenIdAndRemoved(userId,
	// false);//如粉丝的关联已经存在，则需要修改其大v；粉丝算后者的
	// WechatUser wxUser = this.getWechatUser(userId, userNickName);
	// if(rel == null){
	// rel = this.getDefaultWxRel(bigV, wxUser, artId);
	// }else{
	// rel.setOpenId(bigV.getOpenId());
	// }
	// return rel;
	// }
	// private void setModel(Model model, String openId, String artId){
	// model.addAttribute("openId", openId);
	// model.addAttribute("artId", artId);
	// }
	// private WechatUser getWechatUser(String userId, String userNickName){
	// WechatUser wxUser = wxService.findByOpenIdAndRemoved(userId, false);
	// if(wxUser == null){
	// wxUser = this.getDefaultWxUser(userId);
	// }
	// wxUser.setNickName(userNickName);
	// return wxUser;
	// }
	// private WechatUser getDefaultWxUser(String openId){
	// WechatUser wxUser = new WechatUser();
	// wxUser.setBigV(false);
	// wxUser.setOpenId(openId);
	// return wxUser;
	// }
	// private WechatUserRel getDefaultWxRel(WechatUser bigV, WechatUser wxUser,
	// String artId){
	// WechatUserRel rel = new WechatUserRel();
	// rel.setOpenId(bigV.getOpenId());
	// rel.setUserId(wxUser.getOpenId());
	// rel.setArticleId(artId);
	// return rel;
	// }
	/**
	 * 大v完成授权，进入获取数据
	 * 
	 * @param req
	 * @param res
	 * @param model
	 * @return
	 */
	// @RequestMapping(value = "/data/", method = { RequestMethod.GET,
	// RequestMethod.POST })
	// public String getData(HttpServletRequest req, HttpServletResponse res, Model
	// model){
	// String openId = "";
	// try {
	// String code = req.getParameter("code");
	// JSONObject userInfo = this.getAuthorizedMsg(code);
	// openId = userInfo.getString("openid");
	// model.addAttribute("nickName", userInfo.getString("nickname"));
	// } catch (Exception e) {
	// e.printStackTrace();
	// model.addAttribute("msg", "请确认授权");
	// return "huiben/data_error";
	// }
	// Date date = new Date(System.currentTimeMillis());
	// Date beginDate = new Date(date.getTime() - 86400000l);
	// Date endDate = date;
	// model.addAttribute("openId", openId);
	// model.addAttribute("date", new Form(beginDate, endDate));
	//
	// ReachModel reachModel = this.getReachModelByOpenIdAndDate(openId, beginDate,
	// endDate);
	// List<HuibenReach> reachList = reachService.findByOpenIdAndDate(openId,
	// beginDate, endDate);
	//
	// ConversionModel conversionModel =
	// this.getSumNumAndTotalAmountByOpenIdAndDate(openId, beginDate, endDate);
	// List<HuibenConversion> conversionList =
	// conversionService.findByOpenIdAndDate(openId, beginDate, endDate);
	//
	// model.addAttribute("pv", reachModel.getPv());
	// model.addAttribute("uv", reachModel.getUv());
	// model.addAttribute("reachList", reachList);
	// model.addAttribute("sumNum", conversionModel.getSumNum());
	// model.addAttribute("totalAmount", conversionModel.getTotalAmount());
	// model.addAttribute("conversionList", conversionList);
	// return "huiben/data";
	// }
	// private ConversionModel getSumNumAndTotalAmountByOpenIdAndDate(
	// String openId, Date beginDate, Date endDate) {
	// ConversionModel model =
	// conversionService.findSumNumAndTotalAmountByOpenIdAndDate(openId, beginDate,
	// endDate);
	//
	// return model == null ? new ConversionModel() : model;
	// }
	// private ReachModel getReachModelByOpenIdAndDate(String openId,
	// Date beginDate, Date endDate) {
	// ReachModel model = new ReachModel();
	// List<ReachModel> reachList = reachService.findUvByOpenIdAndDate(openId,
	// beginDate, endDate);
	// int uv = reachList.size();
	// int pv = 0;
	// if(uv > 0){
	// for(ReachModel rm : reachList){
	// pv += rm.getUserPv();
	// }
	// }
	// model.setPv(pv);
	// model.setUv(uv);
	// return model;
	// }
	// @Data
	// public static class Form {
	// private Date beginDate;
	// private Date endDate;
	//
	// public Form(Date beginDate, Date endDate) {
	// this.beginDate = beginDate;
	// this.endDate = endDate;
	// }
	// }
	// /**
	// * 大v传入自己的信息，获取数据
	// * @param req
	// * @param res
	// * @param openId
	// * @param beginDate
	// * @param endDate
	// * @param model
	// * @return
	// */
	// @RequestMapping(value = "/data/{openId}/", method = { RequestMethod.GET,
	// RequestMethod.POST })
	// public String getData(HttpServletRequest req, HttpServletResponse res,
	// @PathVariable(value = "openId") String openId,
	// @RequestParam(value = "beginDate", required = false) Date beginDate,
	// @RequestParam(value = "endDate", required = false) Date endDate, Model
	// model){
	// model.addAttribute("openId", openId);
	// if(beginDate == null){
	// Date date = new Date(System.currentTimeMillis());
	// beginDate = new Date(date.getTime() - 86400000l);
	// endDate = date;
	// }
	// model.addAttribute("date", new Form(beginDate, endDate));
	//
	// ReachModel reachModel = this.getReachModelByOpenIdAndDate(openId, beginDate,
	// endDate);
	// List<HuibenReach> reachList = reachService.findByOpenIdAndDate(openId,
	// beginDate, endDate);
	//
	// ConversionModel conversionModel =
	// this.getSumNumAndTotalAmountByOpenIdAndDate(openId, beginDate, endDate);
	// List<HuibenConversion> conversionList =
	// conversionService.findByOpenIdAndDate(openId, beginDate, endDate);
	//
	// model.addAttribute("pv", reachModel.getPv());
	// model.addAttribute("uv", reachModel.getUv());
	// model.addAttribute("reachList", reachList);
	// model.addAttribute("sumNum", conversionModel.getSumNum());
	// model.addAttribute("totalAmount", conversionModel.getTotalAmount());
	// model.addAttribute("conversionList", conversionList);
	// return "huiben/data";
	// }
	// @RequestMapping(value = "/register/", method = { RequestMethod.GET,
	// RequestMethod.POST })
	// public String register(HttpServletRequest req, HttpServletResponse res, Model
	// model){
	// String openId = "";
	// String nickName = "";
	// try {
	// String code = req.getParameter("code");
	// JSONObject userInfo = this.getAuthorizedMsg(code);
	// openId = userInfo.getString("openid");
	// nickName = userInfo.getString("nickname");
	// } catch (Exception e) {
	// e.printStackTrace();
	// model.addAttribute("msg", "请确认授权");
	// return "huiben/data_error";
	// }
	// WechatUser wxUser = this.getWechatUser(openId, nickName);
	// wxUser.setBigV(true);
	// wxUser = wxService.saveWithAudit(wxUser);
	// model.addAttribute("id", wxUser.getId());
	// model.addAttribute("nickName", nickName);
	// return "huiben/register";
	// }
	// @RequestMapping(value = "/register/{id}/", method = { RequestMethod.GET,
	// RequestMethod.POST })
	// public String register(HttpServletRequest req, HttpServletResponse res,
	// @PathVariable(value = "id") int id,
	// @RequestParam(value = "name", required = false) String name,
	// @RequestParam(value = "telephone", required = false) String telephone, Model
	// model){
	// WechatUser wxUser = wxService.findOne(id);
	// if(wxUser == null){
	// model.addAttribute("msg", "注册失败，请重新注册");
	// return "huiben/register_success";
	// }
	// wxUser.setName(name);
	// wxUser.setTelephone(telephone);
	// wxUser = wxService.saveWithAudit(wxUser);
	// model.addAttribute("id", wxUser.getId());
	// model.addAttribute("nickName", wxUser.getNickName());
	// model.addAttribute("msg", "注册成功");
	// return "huiben/register_success";
	// }

}
