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
	//订单id
	var order_id = getQueryString("id");
	//订单号
	var order_no = "";
	init();
	var appIdVal,timeStampVal,nonceStrVal,packageVal,signTypeVal,paySignVal;
	function init(){
		//查询订单 详情
		$.ajax({
			type: "GET",
			url: $main_URL_yd+"/order/detail/"+order_id,
			dataType: "json",
			async: true,
			error: function(xhr, errorInfo, ex) {
				$.toast("查询订单信息错误！错误信息:" + errorInfo, "forbidden");
			},
			success: function(resp) { //请求完成
				if(resp.code == "200"){
					var _Level1 = resp.data.typeLevel1;//第一级
					var _Level2 = resp.data.typeLevel2;//第二级
					var _Level3 = resp.data.typeLevel3;//第三级					
					var order_xx = resp.data.playOrder;//订单 信息
					if(_Level3 == "" || _Level3 == null || _Level3 == undefined){
						$(".order_cp").html(_Level1+">"+_Level2)
					}else{
						$(".order_cp").html(_Level1+">"+_Level2+">"+ _Level3)
					}
					var time_dw = order_xx.product.durationType;//时间单位单位
					$(".order_zh_xx").html("服务时长：");
					if(time_dw == "Hour"){
						time_dw = "小时"
					}else if(time_dw == "Day"){
						time_dw = "天"
					}else if(time_dw == "Week"){
						time_dw = "周"
					}else if(time_dw == "Month"){
						time_dw = "月"
					}else if(time_dw == "TenThousandExp"){
						$(".order_zh_xx").html("服务数量：");
						time_dw = "万经验"
					}else if(time_dw == "Time"){
						time_dw = "次"
					}else if(time_dw == "Site"){
						time_dw = "场"
					}else if(time_dw == "SevenHour"){
						time_dw = "7小时"
					}else if(time_dw == "NoLimitation"){
						time_dw = "不限"					
					} 
					$(".zh_js").html(order_xx.duration+time_dw);//服务时长
					if(time_dw == "NoLimitation"){
						$(".zh_js").html("不限");
					}
					$(".gods_qw").html(order_xx.coach.name);//期望服务大神
					if(order_xx.status == "NotPay"){
						$("#tj_btn").attr("order_id","1")
						order_xx.status = "未支付"
					}else if(order_xx.status == "Operation"){
						$("#tj_btn").attr("order_id","2")
						order_xx.status = "待服务"
					}else if(order_xx.status == "Complete"){
						$("#tj_btn").attr("order_id","2")
						order_xx.status = "已服务"
					}else if(order_xx.status == "Cancel"){
						$("#tj_btn").attr("order_id","2")
						order_xx.status = "已取消"
					}
					$(".order_type").html(order_xx.status);//订单状态
					order_no = order_xx.no;
					$(".order_id").html(order_xx.no);//订单号
					$(".order_time").html(order_xx.creation);//下单时间
					
					var _remark =order_xx.remark;//备注
					if(_remark != null){
						$(".order_remker").html(_remark);
					}
					$(".order_price").html(order_xx.totalFee+"元");//服务费用
					
					if(order_xx.status == "未支付"){
						$("#tj_btn").html("去支付")
						$("#tj_btn").attr("order_id","1")
					}else if(order_xx.status == "已服务"){
						$(".my_order_sure").css("display","none")
					}
					
					var _cellphone = order_xx.cellphone;//电话号码
					$(".order_phone").html(_cellphone);
					var _wxAccount = order_xx.wxAccount;//微信号码
					var _qq = order_xx.qqAccount;//QQ号码
					var _yy = order_xx.yyAccount;//YY号码
					if(_cellphone == "" || _cellphone == null || _wxAccount == undefined){
						$(".telPhone").hide();
					}else{
						$(".telPhone").show();
						$(".order_phone").html(_wxAccount);
					}
					
					
					if(_wxAccount == "" || _wxAccount == null || _wxAccount == undefined){
						$(".Wx").hide();
					}else{
						$(".Wx").show();
						$(".order_wx").html(_wxAccount);
					}
					//qq号
					if(_qq == "" || _qq == null || _qq == undefined){
						$(".QQ").hide();
					}else{
						$(".QQ").show();
						$(".order_qq").html(_qq);
					}
					//YY
					if(_yy == "" || _yy == null || _yy == undefined){
						$(".YY").hide();
					}else{
						$(".YY").show();
						$(".order_yy").html(_yy);
					}
					
					//点击 去。。。。
					$("#tj_btn").click(function(){
						var _attr = $(this).attr("order_id");
						if(_attr == "1"){
							$.ajax({
								type: "POST",
								url: $main_URL_yd+"/order/pay/",
								dataType: "json",
								data:{
									no:order_no
								},
								async: true,
								error: function(xhr, errorInfo, ex) {
									$.toast("再次支付错误！错误信息:" + errorInfo, "forbidden");
								},
								success: function(resp) { //请求完成
									if(resp.code == "200"){
										
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
							});
						}else{
							$.ajax({
								type: "POST",
								url: $main_URL_yd+"/order/exe/"+order_id,
								dataType: "json",
								async: true,
								error: function(xhr, errorInfo, ex) {
									$.toast("查询订单信息错误！错误信息:" + errorInfo, "forbidden");
								},
								success: function(resp) { //请求完成
									if(resp.code == "200"){
										$.toast("订单状态修改成功！");
										init();
									}
								}
							});
						}
					})
				}
			}
		});
	}
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
})