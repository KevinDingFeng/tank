$(document).ready(function() {
	//url取参
	function getQueryString(name) {
		var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
		var r = window.location.search.substr(1).match(reg);
		if(r != null) {
			return unescape(r[2]);
		}
		return null;
	}
	//选中的报价id
	var _type = getQueryString("_type");
	var _num_reg = /^(?!00)(?:[0-9]{1,4}|20000)$/;
	var _iphone = /^[1][0-9][0-9]{9}$/;//手机号码正则
	//支付
	var appIdVal,timeStampVal,nonceStrVal,packageVal,signTypeVal,paySignVal;
	init();
	function init(){
		var _list = $(".kj_order_list>li");
		if(_type == null){	
			_list[0].className = "kj_order_active";
			
		}else{
			var _cc = _type;
			for(var i=0;i<_list.length;i++){
				if(_list[i].attributes._type.value == _cc){
					_list[i].className = "kj_order_active";
				}
			}
		}
		//点击切换服务类型
		$(".kj_order_list>li").click(function(){
			$(this).addClass("kj_order_active");
			$(this).siblings().removeClass("kj_order_active");
			
		})
		//服务补充
		$(".order_fw_pc").blur(function(){
			var _length = $.trim($(this)[0].value);
			if(_length == ""){
				$.toast(`服务补充字数不能为空！`, "forbidden");
			    return false;
			}else if(_length.length>50){
				$.toast(`服务补充字数不能超过50！`, "forbidden");
			    return false;
			}
		})
		//支付金额
		$(".price_je").blur(function(){
			var _length = $.trim($(this)[0].value);
			if(_length == ""){
				$.toast(`输入金额不能为空！`, "forbidden");
			    return false;
			}else if(!_num_reg.test(_length)){
				$.toast(`输入金额为1~20000(勿加小数点)！`, "forbidden");
			    return false;
			}
			$(".money_num").html(`${_length}<span class="title_span">元</span></span>`)
		})
		//手机号
		$(".phone_code").blur(function(){
			var _length = $.trim($(this)[0].value);
			if(_length == ""){
				$.toast(`手机号不能为空！`, "forbidden");
			    return false;
			}else if(!_iphone.test(_length)){
				$.toast(`手机号不符合规则！`, "forbidden");
			    return false;
			}
		})
		//去支付
		$(".go_zf").click(function(){
			var _code = $(".kj_order_active").attr("_type");//服务类型
			var _remark = $.trim($(".order_fw_pc")[0].value);//服务补充
			var _totalFee = $.trim($(".price_je")[0].value);//支付金额
			var _cellphone = $.trim($(".phone_code")[0].value);//手机号
			var _wxAccount = $.trim($(".wx_code")[0].value);//微信号
			if(_remark == ""){
				$.toast(`服务补充字数不能为空！`, "forbidden");
			    return false;
			}else if(_remark.length>50){
				$.toast(`服务补充字数不能超过50！`, "forbidden");
			    return false;
			}
			
			if(_totalFee == ""){
				$.toast(`输入金额不能为空！`, "forbidden");
			    return false;
			}else if(!_num_reg.test(_totalFee)){
				$.toast(`输入金额为1~20000(勿加小数点)！`, "forbidden");
			    return false;
			}
			
			if(_cellphone == ""){
				$.toast(`手机号不能为空！`, "forbidden");
			    return false;
			}else if(!_iphone.test(_cellphone)){
				$.toast(`手机号不符合规则！`, "forbidden");
			    return false;
			}
			var _obj = {
				code: _code,
				remark: _remark,
				totalFee: _totalFee,
				cellphone: _cellphone,
				wxAccount: _wxAccount
			}
			$.ajax({
				type: "POST",
				url: $main_URL_yd+"/order/save_rapid",
				data:{
					code: _code,
					remark: _remark,
					totalFee: _totalFee,
					cellphone: _cellphone,
					wxAccount: _wxAccount
				},
				dataType: "json",
				async: true,
				error: function(xhr, errorInfo, ex) {
					$.toast("下单信息错误！错误信息:" + errorInfo, "forbidden");
				},
				success: function(resp) { //请求完成
					debugger
					if (resp.code == "200") {
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
							onBridgeReady();
						}
					}
				}
			})
		})	
		//唤醒微信支付
		function onBridgeReady() {
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
					window.location.href = "my_order.html";
				}else if (res.err_msg == "get_brand_wcpay_request: cancel") {
					$.toast(`支付取消！！`, 20000);
					window.location.href = "my_order.html";
				}else if (res.err_msg == "get_brand_wcpay_request: fail") {
					$.toast(`支付失败！！`, 20000);
					window.location.href = "my_order.html";
				}
			});
		}
	}
})