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
	var _iphone = /^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/;//手机号码正则
	init();
	function init(){
		//代练类型
		if(_code == "11"){
			var _type = "代练";
			$(".sure_title").html("代练");
			$(".dl_xz").css("display","none");
			get_user();
			//点击二级
			$(".er_dj").on("click",".get_san",function(){
				$(this).addClass("dw_list_active");
				$(this).siblings().removeClass("dw_list_active");
				var er_code = $(this).attr("er_code");//当前二级的code
				get_level3(er_code);
			})
			//点击三级
			$(".zi_arr").on("click",".three_list",function(){
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
			$(".zi_arr_jl").on("click",".gods_a",function(){
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
				$("#timeStart").val(bc_num);
				var er_code = $(this).attr("er_code");//当前二级的code
				get_level3(er_code);
			})
			//点击三级
			$(".zi_arr").on("click",".three_list",function(){
				$(this).addClass("zi_arr_active");
				$(this).siblings().removeClass("zi_arr_active");
				$("#timeStart").val(bc_num);
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
		//时间选择
		//		if ($('#timeStart').val() == '') {
		//			alert("时间不能为空！")
		//		}
		var theme = "ios";
		var mode = "scroller";
		var display = "bottom";
		var lang = "zh";
		$('#timeStart').mobiscroll().date({
			theme: theme,
			mode: mode,
			display: display,
			lang: lang,
			dateFormat: "yyyy-mm-dd",
			minDate: new Date(),
			maxDate: new Date(2050, 0, 1, 00, 00, 00),
			stepMinute: 1,
		});
		var MAX = 99,
		MIN = 1;
		//减少
		$('.weui-count__decrease').click(function(e) {
			var _cc = bc_num;
			var $input = $(e.currentTarget).parent().find('.weui-count__number');
			var number = parseInt($input.val() || "0") - _cc
			if(number < MIN){
				number = MIN;
				$.toast("时间不能少于初始时间", "forbidden");
			} 
			$input.val(number);
			_num = number;
			var _fw_id= _fw_id;//产品id
			var _jl_id= _jl_id;//教练id
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
			if(_w_chart.length<=0){
				$.toast(`微信号不能为空！`, "forbidden");
			    return false;
			}
		})
		//验证手机号
		$("#m_iphone").blur(function(){			
			var _m_iphone = $.trim($("#m_iphone").val());//手机号
			if(!_iphone.test(_m_iphone)){
				$.toast(`手机号码 格式错误！`, "forbidden");
			    return false;
			}
		})
		//验证备注
		$("#m_remark").blur(function(){			
			var _m_iphone = $.trim($("#m_remark").val());//备注
			if(_m_remark.length>30){
				$.toast(`备注字数不能超过30字！`, "forbidden");
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
					if(product_list_three){
						get_level3(er_codes)
					}
					//获取教练信息
					get_gods(gods_id);
					//获取价格
					get_price(gods_id,_zx_code);
				}
			}
		})
	}
	//点击第三级获取第三级
	function get_level3(er_code){
		var _code = er_code;
		$.ajax({
			type: "GET",
			url: $main_URL_yd + "/product_type/level3?code="+_code,
			async: true,		
			error: function (xhr, errorInfo, ex) {
				$.toast("查询第三级错误！错误信息:" + errorInfo, "forbidden");
			},
			success: function (resp) { //请求完成
				if (resp.code == "200") {
					var level3_list = resp.data.level3;
					var list_3 = "";//第三级的列表
					for(var i=0;i<level3_list.length;i++){
						if(level3_list.length == 1){
							list_3+=`
								<li class="three_list zi_arr_active" code=${level3_list[i].code} id=${level3_list[i].id}>${level3_list[i].name}</li>
							`;
						}else if(level3_list[i].code == _zx_code){
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
					$(".zi_arr").find("li:first-child").addClass("zi_arr_active");
					var _aa = $(".zi_arr_active1").attr("id");//大神id
					var _cc = $(".zi_arr_active").attr("code");//产品code
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
					$("#price_qd").html(resp.data.quotes.price);
					quotedProductId = resp.data.quotes.product.productType.id;//大神id
					if(_code == "11"){
						var _duration = "0";
					}
					bc_num = _duration;
					if(num){
						_duration = num
					}else{
						$("#timeStart").val(_duration);
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
								$(".money_num").html(_price);
								$(".sure_title_red").html(_price);
							}
						}
					})	
				}
			}
		})	
	}
	//支付
	function go_zf(_fw_id,_jl_id){
		var fw_id = _fw_id;//三级的code
		var jl_id = _jl_id;//教练的id
		var _w_chart = $.trim($("#w_chart").val());//微信号
		var _m_iphone = $.trim($("#m_iphone").val());//手机号
		if(!_iphone.test(_m_iphone)){
			$.toast(`手机号码 格式错误！`, "forbidden");
		    return false;
		}
		var _m_remark = $.trim($("#m_remark").val());//备注
		if(_m_remark.length>30){
			$.toast(`备注字数不能超过30字！`, "forbidden");
			return false;
		}
		var _time = "";//选择的服务时长
		if(_code == "11"){
			_time =0;
		}else{
			_time = _num;
		}
		
		var _arr = {
			wxAccount: _w_chart,
			cellphone: _m_iphone,
			remark: _m_remark,
			duration: _time,
			quotedProductId:quotedProductId
		}
		debugger
		$.ajax({
			type: "POST",
			url: $main_URL_yd + "/order/save",
			data:{
				_arr
			},
			async: true,		
			error: function (xhr, errorInfo, ex) {
				$.toast("产品下单错误！错误信息:" + errorInfo, "forbidden");
			},
			success: function (resp) { //请求完成
				if (resp.code == "200") {
					debugger
				}
			}
		})	
	
	}
})















