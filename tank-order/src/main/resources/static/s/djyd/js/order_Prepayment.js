$(document).ready(function() {
	//url取参
	function getQueryString(name) {
	    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	    var r = window.location.search.substr(1).match(reg);
	    if (r != null) {
	        return unescape(r[2]);
	    }
	    return null;
	}
	//类型
	var _type = getQueryString("others");//
	var _qpId = getQueryString("qpId");////服务报价Id
	var _num = getQueryString("num");//数量 
	var _course_id = getQueryString("course_id");//课程
	var _iphone = /^[1][0-9][0-9]{9}$/;//手机号码正则
	var wxreg=/^[a-zA-Z][-_a-zA-Z0-9]{5,19}$/;//微信号正则
	init();
	function init(){
		$("#timeStart").val(_num);
		get_xx()
		if(_type == "gods"){
			$("#gods_first").show();
			$("#others_first").hide();
			$("#class_ds").hide();
		}else if(_type == "others"){
			$("#others_first").show();
			$("#gods_first").hide();
			$("#class_ds").show();
			
		}
		function get_xx(){
			var obj=""
			if(_course_id == null || _course_id=="" ||_course_id==undefined){
				obj="qpId="+_qpId+"&count="+_num
			}else{
				obj="qpId="+_qpId+"&count="+_num+"&courseId="+_course_id
			}
			$.ajax({
				type: "GET",
				url: $main_URL_yd+"/v2/order/v2form?"+obj,
				dataType: "json",
				
				async: true,
				error: function(xhr, errorInfo, ex) {
					$.toast("查询当前信息错误！错误信息:" + errorInfo, "forbidden");
				},
				success: function(resp) { //请求完成
					if(resp.code =="200"){
						var _data = resp.data;
						//教练
						$(".gods_name").html(_data.coach.name);
						$(".gods_name").attr("gods_id",_data.coach.id);
						//最后一级code
						var _code = "v"+_data.product.productType.code;
						$(".fw_name").attr("code",_data.product.productType.code);
						//课程
						var _course = _data.course;
						if(_course == null || _course == "" || _course ==undefined){
							$(".class_name").html("暂无");
						}else{
							$(".class_name").html(_course);
						}
						//匹配数距
						var _arr = _data.typeJson;
						var _fw = _arr[_code];
						//服务大类
						$(".dl_type").html(_fw.v1_name);
						//服务说明
						$(".fw_content").html(_fw.content);
						//服务说明
						if(_fw.hasOwnProperty("v4_name")){
							$(".fw_name").html(_fw.v2_name+">"+_fw.v3_name+">"+_fw.v4_name)
						}else{
							$(".fw_name").html(_fw.v2_name+">"+_fw.v3_name)
						}
						
						//价格 数量
						var _num = parseInt(_data.count);
						var _price = _data.quotedProduct.price;
						$(".Prepayment_price .order_price").html(_price);
						$("#z_price").html(_num*_price);
						//手机号
						var _user = _data.wxUser.cellphone;
						if(_user == "" || _user == null || _user == undefined){
							
						}else{
							$(".phone_num").val(_user);
						}
					}
				}
			})
		}
		//手机号验证
		$(".phone_num").blur(function(){
			var m_iphone = $.trim($(".phone_num").val());
			if(m_iphone && !_iphone.test(m_iphone)){//只有存在有效值时才校验是否合法，即空白不给提示
				$.toast(`手机号码 格式错误！`, "forbidden");
			    return false;
			}
		})
		//验证微信
		$(".wx_num").blur(function(){
			var _w_chart = $.trim($(".wx_num").val());//微信号
			if(!wxreg.test(_w_chart)){
				$.toast(`微信号格式错误！`, "forbidden");
			    return false;
			}
		})
		//验证备注
		$("#m_remark").blur(function(){			
			var m_remark = $.trim($(".order_remark").val());//备注
			if(m_remark.length>200){
				$.toast(`备注字数不能超过200字！`, "forbidden");
			    return false;
			}
		})
		var appIdVal,timeStampVal,nonceStrVal,packageVal,signTypeVal,paySignVal;
		//去支付
		$("#go_zf").click(function(){
			//手机号
			var m_iphone = $.trim($(".phone_num").val());
			if(m_iphone && !_iphone.test(m_iphone)){//只有存在有效值时才校验是否合法，即空白不给提示
				$.toast(`手机号码 格式错误！`, "forbidden");
			    return false;
			}
			//微信号
			var m_wx = $.trim($(".wx_num").val());
			if(m_wx && !wxreg.test(m_wx)){
				$.toast(`微信号格式错误！`, "forbidden");
			    return false;
			}
			if(!(m_iphone ||m_wx)){
				$.toast("手机号和微信号，至少填写一项", "forbidden");
				return;
			}
			//备注
			var m_remark = $(".order_remark").val();
			if(m_remark.length>200){
				$.toast(`备注字数不能超过200字！`, "forbidden");
			    return false;
			}
			//服务类型Id
			var _code = $(".fw_name").attr("code");
			//大神id
			var gods_id = $(".gods_name").attr("gods_id");
			//数量
			var _num = $("#timeStart").val();
			$.ajax({
				type: "POST",
				url: $main_URL_yd+"/v2/order/save_order",
				dataType: "json",
				data:{
					coachId:gods_id,
					productTypeId:_code,
					duration:_num,
					remark:m_remark,
					cellphone:m_iphone,
					wxAccount:m_wx
				},
				async: true,
				error: function(xhr, errorInfo, ex) {
					$.toast("生成订单错误！错误信息:" + errorInfo, "forbidden");
				},
				success: function(resp) { //请求完成
					if (resp.code == "200") {
						//$.toast("Loading", 40000);
						var _playorder = resp.data.playOrder;
						var order_id = _playorder.id
						appIdVal = resp.data.prepay.appId;
						timeStampVal = resp.data.prepay.timeStamp;
						nonceStrVal = resp.data.prepay.nonceStr;
						packageVal = resp.data.prepay.prepayId;
						signTypeVal = resp.data.prepay.signType;
						paySignVal = resp.data.prepay.paySign;
						if (typeof WeixinJSBridge == "undefined") {
							if (document.addEventListener) {
								document.addEventListener('WeixinJSBridgeReady', onBridgeReady,
										false);
							} else if (document.attachEvent) {
								document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
								document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
							}
						} else {
							onBridgeReady(order_id);
						}
					}
				}
			})			
		})
		//唤醒微信支付
		function onBridgeReady(order_id) {
			WeixinJSBridge.invoke('getBrandWCPayRequest', {
				"appId" : appIdVal, //公众号名称，由商户传入     
				"timeStamp" : timeStampVal, //时间戳，自1970年以来的秒数     
				"nonceStr" : nonceStrVal, //随机串     
				"package" : packageVal,
				"signType" : signTypeVal, //微信签名方式：     
				"paySign" : paySignVal //微信签名 
			}, function(res) {
				if (res.err_msg == "get_brand_wcpay_request:ok") {
					$.toast(`支付成功！！`, 20000);
					window.location.href = "../order_look.html?id="+order_id;
				}else if (res.err_msg == "get_brand_wcpay_request: cancel") {
					$.toast(`支付取消！！`, 20000);
					window.location.href = "../order_look.html?id="+order_id;
				}else if (res.err_msg == "get_brand_wcpay_request: fail") {
					$.toast(`支付失败！！`, 20000);
					window.location.href = "../order_look.html?id="+order_id;
				}
			});
		}
		var MAX = 1000,
		MIN = 1;
		//减少
		$('.weui-count__decrease').click(function(e) {
			var $input = $(e.currentTarget).parent().find('.weui-count__number');
			var number = parseInt($input.val())-1; //- _cc
			if(number <= MIN){
				number = MIN;
				$.toast("数量不能少于1", "forbidden");
			} 
			$input.val(number);
			var _price = $(".order_price").html();
			$("#z_price").html(number*_price)
		})
		//增加
		$('.weui-count__increase').click(function(e) {
			var $input = $(e.currentTarget).parent().find('.weui-count__number');
			var number = parseInt($input.val())+1; //+ _cc
			if(number > MAX){
				number = MAX;
				$.toast("数量不能大于1000", "forbidden");
			}
			$input.val(number);
			var _price = $(".order_price").html();
			$("#z_price").html(number*_price);
		})
		//手动输入
		$("#timeStart").blur(function(){
			var _inpt = $("#timeStart").val();
			if(_inpt > MAX){
				_inpt = MAX;
				$.toast("数量不能大于1000", "forbidden");
			}else if(_inpt <= MIN){
				_inpt = MIN;
				$.toast("数量不能小于1", "forbidden");
			}
			var _price = $(".order_price").html();
			$("#z_price").html(_inpt*_price)
			$("#timeStart").val(_inpt);
		})	
	}
})