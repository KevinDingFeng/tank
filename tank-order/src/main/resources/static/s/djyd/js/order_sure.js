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
	var _id = getQueryString("_id");
	//大神id
	var gods_id = getQueryString("gods_id");
	//代练类型
	var _code = getQueryString("code");
	//第二级
	var er_codes = getQueryString("er_code");
	//第三级
	var _zx_code = getQueryString("zx_code");
	
	var  gods_list =null;//第三级
	
	var _fw_id= _zx_code;//产品id
	var _jl_id= gods_id;//教练id
	var _num = null;//服务时长
	var bc_num = null;//记录步长
	var quotedProductId =null;//选中报价id
	var _iphone = /^[1][0-9][0-9]{9}$/;//手机号码正则
	var wxreg=/^[a-zA-Z]([-_a-zA-Z0-9]{5,40})+$/;//微信号正则
	var qqreg=/^\d{4,20}$/;//QQ正则
	var yyreg=/^[a-zA-Z]([-_a-zA-Z0-9]{5,40})+$/;//yy正则
	var ex = /^\d+$/;//正整数正则
	init();
	function init(){
		//代练类型
		if(_code == "11"){
			var _type = "代练";
			$(".sure_title").html("代练");
			$(".fw_name").html("服务数量")
			get_user();
			//点击二级
			$(".er_dj").on("touchstart",".get_san",function(e){
				e.stopPropagation()  
				$(this).addClass("dw_list_active");
				$(this).siblings().removeClass("dw_list_active");
				$("#timeStart").val(bc_num)
				var er_code = $(this).attr("er_code");//当前二级的code
				get_level3(er_code);
			})
			//点击三级
			$(".zi_arr").on("touchstart",".three_list",function(e){
				e.stopPropagation()  
				$(this).addClass("zi_arr_active");
				$(this).siblings().removeClass("zi_arr_active");
				var three_code = $(this).attr("code");//当前三级的code
				var three_id = $(this).attr("id");//当前三级的id
				var gods_id = $(".zi_arr_active1").attr("id");//大神id
				//做用于提交
				_fw_id = three_code;//提交三级的code
				_jl_id = gods_id; //提交教练的id
				get_price(gods_id,three_code);//获取价格
			})
			//点击教练
			$(".zi_arr_jl").on("touchstart",".gods_a",function(e){
				e.stopPropagation()  
				$(this).addClass("zi_arr_active1");
				$(this).siblings().removeClass("zi_arr_active1");
				var gods_id = $(this).attr("id");//大神id
				var three_code = $(".zi_arr_active").attr("code");//产品code
				//做用于提交
				_fw_id = three_code;//提交三级的code
				_jl_id = gods_id; //提交教练的id
				get_price(gods_id,three_code);//获取价格
			})
			//点击支付
			$(".go_zf").click(function(){
				var _fw_id = $(this).attr("fw_id");//三级的code
				var _jl_id = $(this).attr("gods_id");//教练的id
				go_zf(_fw_id,_jl_id);
			})
		}
		//陪玩类型
		else if(_code == "12"){
			var _type = "陪玩";
			$(".sure_title").html("陪玩");
			$(".dl_xz").css("display","block");
			get_user();
			//点击二级
			$(".er_dj").on("click",".get_san",function(event){
				event.stopPropagation();
				$(this).addClass("dw_list_active");
				$(this).siblings().removeClass("dw_list_active");
				$("#timeStart").val(bc_num);
				_num =bc_num;
				var er_code = $(this).attr("er_code");//当前二级的code
				get_level3(er_code);
			})
			//点击三级
			$(".zi_arr").on("click",".three_list",function(event){
				event.stopPropagation();
				$(this).addClass("zi_arr_active");
				$(this).siblings().removeClass("zi_arr_active");
				$("#timeStart").val("");
				bc_num="";
				_num =bc_num;
				var three_code = $(this).attr("code");//当前三级的code
				var three_id = $(this).attr("id");//当前三级的id
				var gods_id = $(".zi_arr_active1").attr("id");//大神id
				_fw_id = three_code;//提交三级的code
				_jl_id = gods_id; //提交教练的id
				get_price(gods_id,three_code);//获取价格
			})
			//点击教练
			$(".zi_arr_jl").on("click",".gods_a",function(event){
				event.stopPropagation();
				$(this).addClass("zi_arr_active1");
				$(this).siblings().removeClass("zi_arr_active1");
				$("#timeStart").val(bc_num);
				var gods_id = $(this).attr("id");//大神id
				var three_code = $(".zi_arr_active").attr("code");//产品code
				//做用于提交
				_fw_id = three_code;//提交三级的code
				_jl_id = gods_id; //提交教练的id
				get_price(gods_id,three_code);//获取价格
			})
			//点击支付
			$(".go_zf").click(function(){
				var _fw_id = $(this).attr("fw_id");//三级的code
				var _jl_id = $(this).attr("gods_id");//教练的id
				go_zf(_fw_id,_jl_id);
			})
		}
		//教学类型
		else if(_code == "10"){
			var _type = "教学";
			$(".sure_title").html("教学");
			$(".dl_xz").css("display","block");
			get_user();
			//点击二级
			$(".er_dj").on("click",".get_san",function(){
				$(this).addClass("dw_list_active");
				$(this).siblings().removeClass("dw_list_active");
				$("#timeStart").val("");
				bc_num="";
				_num =bc_num;
				var er_code = $(this).attr("er_code");//当前二级的code
				get_level3(er_code);
			})
			//点击三级
			$(".zi_arr").on("click",".three_list",function(){
				$(this).addClass("zi_arr_active");
				$(this).siblings().removeClass("zi_arr_active");
				$("#timeStart").val("");
				bc_num="";
				_num =bc_num;
				var three_code = $(this).attr("code");//当前三级的code
				var three_id = $(this).attr("id");//当前三级的id
				var gods_id = $(".zi_arr_active1").attr("id");//大神id
				_fw_id = three_code;//提交三级的code
				_jl_id = gods_id; //提交教练的id
				get_price(gods_id,three_code);//获取价格
			})
			//点击教练
			$(".zi_arr_jl").on("click",".gods_a",function(){
				$(this).addClass("zi_arr_active1");
				$(this).siblings().removeClass("zi_arr_active1");
				$("#timeStart").val(bc_num);
				var gods_id = $(this).attr("id");//大神id
				var three_code = $(".zi_arr_active").attr("code");//产品code
				//做用于提交
				_fw_id = three_code;//提交三级的code
				_jl_id = gods_id; //提交教练的id
				get_price(gods_id,three_code);//获取价格
			})
			//点击支付
			$(".go_zf").click(function(){
				var _fw_id = $(this).attr("fw_id");//三级的code
				var _jl_id = $(this).attr("gods_id");//教练的id
				go_zf(_fw_id,_jl_id);
			})
		}
		
		var MAX = 90,
		MIN = bc_num;
		//减少
		$('.weui-count__decrease').click(function(e) {
			var _cc = bc_num;
			var $input = $(e.currentTarget).parent().find('.weui-count__number');
			var number = parseInt($input.val() || "0") - _cc
			if(number <= MIN){
				number = MIN;
				$.toast("时间不能少于初始时间", "forbidden");
			} 
			$input.val(number);
			_num = number;
			var _fw_id1= _fw_id;//产品id
			var _jl_id1= _jl_id;//教练id
			get_price(_jl_id1,_fw_id1,number)
		})
		//增加
		$('.weui-count__increase').click(function(e) {
			var _cc = bc_num;
			var $input = $(e.currentTarget).parent().find('.weui-count__number');
			var number = parseInt($input.val() || "0") + _cc
			if(number > MAX){
				number = MAX;
			}
			$input.val(number);
			
			_num = number;
			var _fw_id1= _fw_id;//产品id
			var _jl_id1= _jl_id;//教练id
			get_price(_jl_id1,_fw_id1,number)
		})
		//验证微信
		$("#w_chart").blur(function(){			
			var _w_chart = $.trim($("#w_chart").val());//微信号
			if(!wxreg.test(_w_chart)){
				$.toast(`微信号格式错误！`, "forbidden");
			    return false;
			} //TODO kevin 暂时去掉 微信账号格式校验
		})
		//验证QQ
		$("#t_qq").blur(function(){
			var _qq = $.trim($("#t_qq").val());//QQ号
			if(!qqreg.test(_qq)){
				$.toast(`QQ号格式错误！`, "forbidden");
			    return false;
			} 
		})
		//验证YY
		$("#y_yy").blur(function(){
			var _yy = $.trim($("#y_yy").val());//QQ号
			if(!yyreg.test(_yy)){
				$.toast(`YY号格式错误！`, "forbidden");
			    return false;
			} 
		})
		//验证手机号
		$("#m_iphone").blur(function(){			
			var _m_iphone = $.trim($("#m_iphone").val());//手机号
			if(_m_iphone && !_iphone.test(_m_iphone)){//只有存在有效值时才校验是否合法，即空白不给提示
				$.toast(`手机号码 格式错误！`, "forbidden");
			    return false;
			}
		})
		//验证备注
		$("#m_remark").blur(function(){			
			var m_remark = $.trim($("#m_remark").val());//备注
			if(m_remark.length>50){
				$.toast(`备注字数不能超过50字！`, "forbidden");
			    return false;
			}
		})
	}
	//获取 大神产品
	function get_user(){
		$.ajax({
			type: "GET",
			url: $main_URL_yd + "/order/form?quotedProductId="+_id,
			async: true,		
			error: function (xhr, errorInfo, ex) {
				$.toast("查询产品信息错误！错误信息:" + errorInfo, "forbidden");
			},
			success: function (resp) { //请求完成
				if (resp.code == "200") {
					var product_list = resp.data.typeLevel2List;//产品信息
					gods_list = resp.data.coaches;//教练信息
					var list1 = "";//二级档位
					for(var i=0; i<product_list.length;i++){
						if(product_list[i].code == er_codes){
							list1+=`
								<li class="get_san dw_list_active" er_code = ${product_list[i].code} er_id = ${product_list[i].id}>${product_list[i].name}</li>
							`;
						}else{
							list1+=`
								<li class="get_san" er_code = ${product_list[i].code} er_id = ${product_list[i].id}>${product_list[i].name}</li>
							`;
						}
					}
					$(".er_dj").empty();
					$(".er_dj").append(list1);
					product_list_three = null;
					product_list_three = resp.data.typeLevel3List;
					//获取教练信息
					get_gods(gods_id);
					
					if(product_list_three){
						get_level3(er_codes);
					}				
				}
			}
		})
	}
	//点击第二级获取第三级
	function get_level3(er_code){
		var _code = er_code;
		var _gods_id = $(".zi_arr_active1").attr("id");
		$.ajax({
			type: "GET",
			url: $main_URL_yd + "/product_type/level3?code="+_code+"&coachId="+_gods_id,
			async: true,		
			error: function (xhr, errorInfo, ex) {
				$.toast("查询第三级错误！错误信息:" + errorInfo, "forbidden");
			},
			success: function (resp) { //请求完成
				if (resp.code == "200") {
					var level3_list = resp.data.level3;
					
					var list_3 = "";//第三级的列表
					for(var i=0;i<level3_list.length;i++){
						if(level3_list[i].code == _zx_code){
							list_3+=`
								<li class="three_list zi_arr_active" code=${level3_list[i].code} id=${level3_list[i].id}>${level3_list[i].name}</li>
							`;
						}else{
							list_3+=`
								<li class="three_list" code=${level3_list[i].code} id=${level3_list[i].id}>${level3_list[i].name}</li>
							`;
						}
					}
					$(".zi_arr").empty();
					$(".zi_arr").append(list_3);
					if($(".three_list").hasClass("zi_arr_active")){
						
					}else{
						$(".zi_arr").find("li:first-child").addClass("zi_arr_active");
					}

					var _aa = $(".zi_arr_active1").attr("id");//大神id
					var _cc = $(".zi_arr_active").attr("code");//产品code
					_fw_id = _cc;//提交三级的code
					_jl_id = _aa; //提交教练的id
					get_price(_aa,_cc);//获取价格
				}
			}
		})
	}
	//获取教练信息
	function get_gods(gods_id){
		var gods_id = gods_id;
		var _gods_list = gods_list;
		var list_gods = "";//教练列表
		for(var i=0;i<_gods_list.length;i++){
			if(_gods_list[i].id == gods_id){
				list_gods+=`
					<li class="gods_a zi_arr_active1" id=${_gods_list[i].id}>${_gods_list[i].name}</li>
				`;
			}else{
				list_gods+=`
					<li class="gods_a" id=${_gods_list[i].id}>${_gods_list[i].name}</li>
				`;
			}
		}
		$(".zi_arr_jl").empty();
		$(".zi_arr_jl").append(list_gods);		
	}
	//获取价格
	function get_price(gods_id,fw_id,num){
		var gods_id = gods_id;
		var fw_id = fw_id;
		$(".go_zf").attr("fw_id",fw_id);
		$(".go_zf").attr("gods_id",gods_id);
		$.ajax({
			type: "GET",
			url: $main_URL_yd + "/quoted_product/coach_code?code="+fw_id+"&coachId="+gods_id,
			async: true,		
			error: function (xhr, errorInfo, ex) {
				$.toast("查询产品价格错误！错误信息:" + errorInfo, "forbidden");
			},
			success: function (resp) { //请求完成
				if (resp.code == "200") {
					if(resp.data.quotes == null){
						$(".money_num").html("暂无价格");
						return;
					}
					var _duration = resp.data.quotes.product.duration;
					var _dw = resp.data.quotes.product.durationType;//单位
					$(".dl_xz").css("display","block");
					if(_dw == "Hour"){
						_dw ="小时"
					}else if(_dw == "Day"){
						_dw ="日"
					}else if(_dw == "Month"){
						_dw ="月"
					}else if(_dw == "TenThousandExp"){
						_dw = "1万经验"
					}else if(_dw == "Time"){
						_dw = "次"
					}else if(_dw == "Site"){
						_dw = "场"
					}else if(_dw == "SevenHour"){
						_dw = "7小时"
					}else if(_dw == "NoLimitation"){
						_dw = "不限"
						$(".dl_xz").css("display","none");
					}
					
					var z_num = resp.data.quotes.price/resp.data.quotes.product.duration;
					
					z_num = z_num.toFixed(0);//保留两位小数
					if(_dw == "日"){
						$("#price_qd").html("约"+z_num+"/"+_dw);
					}else{
						$("#price_qd").html(z_num+"/"+_dw);
					}
					quotedProductId = resp.data.quotes.product.productType.id;//大神id				
					bc_num = resp.data.quotes.product.duration;			
					if(_num){
						_duration = _num
					}else{
						$("#timeStart").val(_duration);
						_num =_duration;
					}
					if(num){
						_duration = num;
					}
					$.ajax({
						type: "GET",
						url: $main_URL_yd + "/quoted_product/total_fee?code="+fw_id+"&coachId="+gods_id+"&duration="+_duration,
						async: true,		
						error: function (xhr, errorInfo, ex) {
							$.toast("查询产品价格2错误！错误信息:" + errorInfo, "forbidden");
						},
						success: function (resp) { //请求完成
							if (resp.code == "200") {
								var _price = resp.data.totalFee;
								if(_price == "0"){
									_price="详情咨询客服"
									$(".mon_q").css("display","none");
									$(".go_zf").css("display","none");
								}else{
									$(".mon_q").css("display","inline-block");
									$(".go_zf").css("display","inline-block");
								}
								$(".money_num").html(_price+`<span class="title_span">元</span>`);
								$(".sure_title_red").html(_price+`<span class="title_span">元</span>`);
							}
						}
					})	
				}
			}
		})	
	}
	//服务训练
	$("#timeStart").on('input',function(){
		var _num  = bc_num;//步长
		var _val = parseInt($("#timeStart").val());
		var _cc = _val/_num;
		if(isNaN(_val)){
			return
		}
		if(_val == 0){
			$.toast(`请输入大于0的正整数`, "forbidden");
			$("#timeStart").val(bc_num);
			return
		}else if(_val >=100){
			$.toast(`输入最大数字不能大于99`, "forbidden");
			$("#timeStart").val(bc_num);
			var three_code = $(".zi_arr_active").attr("code");//产品id
			var gods_id = $(".zi_arr_active1").attr("id");//大神id
			var num = 1;
			get_price(gods_id,three_code,num);//获取价格
			return
		}
		if(!ex.test(_cc)){
			$.toast(`请输入</br>${bc_num}的整数倍`, "forbidden");
			$("#timeStart").val(bc_num);
			return
		}else{
			var three_code = $(".zi_arr_active").attr("code");//产品id
			var gods_id = $(".zi_arr_active1").attr("id");//大神id
			var num = _val;
			get_price(gods_id,three_code,num);//获取价格
		}
		
	})
	//服务训练
	$("#timeStart").change(function(){
		var _num  = bc_num;//步长
		var _val = $("#timeStart").val();
		var _cc = _val/_num;
		if(_val == ""){
			$.toast(`服务数量不能为空！`, "forbidden");
			$("#timeStart").val(bc_num);
			return;
		}
		else if(!ex.test(_cc)){
			$.toast(`请输入</br>${bc_num}的整数倍11`, "forbidden");
			$("#timeStart").val(bc_num);
			return
		}else{
			var three_code = $(".zi_arr_active").attr("code");//产品id
			var gods_id = $(".zi_arr_active1").attr("id");//大神id
			var num = _val;
			get_price(gods_id,three_code,num);//获取价格
		}
	})
	//支付
	var appIdVal,timeStampVal,nonceStrVal,packageVal,signTypeVal,paySignVal;
	function go_zf(_fw_id,_jl_id){
		var fw_id = _fw_id;//三级的code
		var jl_id = _jl_id;//教练的id
		$.ajax({
			type: "GET",
			url: $main_URL_yd + "/quoted_product/coach_code?code="+fw_id+"&coachId="+jl_id,
			async: true,		
			error: function (xhr, errorInfo, ex) {
				$.toast("查询下单id错误！错误信息:" + errorInfo, "forbidden");
			},
			success: function (resp) { //请求完成
				if (resp.code == "200") {
					var _quotedProductId = resp.data.quotes;//选中的报价id
					var _w_chart = $.trim($("#w_chart").val());//微信号
					var _m_iphone = $.trim($("#m_iphone").val());//手机号
					var _t_qq = $.trim($("#t_qq").val());//QQ号
					var _y_yy = $.trim($("#y_yy").val());//YY号
					
					if(_w_chart && !wxreg.test(_w_chart)){
						$.toast(`微信号格式错误！`, "forbidden");
					    return false;
					    //TODO kevin 暂时去掉 微信账号格式校验
					}
					if(_m_iphone && !_iphone.test(_m_iphone)){
						$.toast(`手机号码 格式错误！`, "forbidden");
					    return false;
					}
					//验证QQ
					if(_t_qq && !qqreg.test(_t_qq)){
						$.toast(`QQ号格式错误！`, "forbidden");
					    return false;
					} 
					//验证YY
					if(_y_yy && !yyreg.test(_y_yy)){
						$.toast(`YY号格式错误！`, "forbidden");
					    return false;
					} 
					
					if(!(_w_chart || _m_iphone || _t_qq || _y_yy)){
						$.toast(`4种联系方式必须选择一种！`, "forbidden");
					    return false;
					}
					var _m_remark = $.trim($("#m_remark").val());//备注
					if(_m_remark.length>50){
						$.toast(`备注字数不能超过50字！`, "forbidden");
						return false;
					}
					var _time = "";//选择的服务时长
					_time = _num;
					/*var _arr = {
						wxAccount: _w_chart,
						cellphone: _m_iphone,
						remark: _m_remark,
						duration: _time,
						quotedProductId:_quotedProductId.id
					};*/
					$.ajax({
						type: "POST",
						url: $main_URL_yd + "/order/save",
						data:{
							wxAccount: _w_chart,
							qqAccount: _t_qq,
							yyAccount: _y_yy,
							cellphone: _m_iphone,
							remark: _m_remark,
							duration: _time,
							quotedProductId:_quotedProductId.id
						},
						async: true,		
						error: function (xhr, errorInfo, ex) {
							$.toast("下单信息错误！错误信息:" + errorInfo, "forbidden");
						},
						success: function (resp) { //请求完成
							
							if (resp.code == "200") {
								//$.toast("Loading", 40000);
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
				}
			}
		})	
	
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















