$(document).ready(function() {
	//轮播
	var mySwiper = new Swiper('.swiper-container', {
		autoplay: 100000, //可选选项，自动滑动
		pagination: ".swiper-pagination",
		paginationClickable:true,//--实现小圆点点击
	})
	
	//url取参
	function getQueryString(name) {
	    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	    var r = window.location.search.substr(1).match(reg);
	    if (r != null) {
	        return unescape(r[2]);
	    }
	    return null;
	}
	//大神id
	var gods_id = getQueryString("id");
	var qp_id = getQueryString("qp_id");
	var v2_arr=[];//第二级
	var v3_arr=[];//第三级
	var v4_arr=[];//第四级
	var arr_course=[];//第四级
	var _first="1";
	init();
	function init(){
		get_xx(gods_id,qp_id)
		var _height = $(document).height();
		$("#xq_cumtor").css("height",_height);
		//点击选择
		$("#choose").click(function(){
			$("#xq_cumtor").css("display","block");
			//服务大类名字
			var fw_er = $(".fw_dl>.dl_active").html();
			//服务子类名字三级
			var fw_san = $("#fw_san>.zl_active").html();
			//服务子类名字四级
			var fw_si =$("#fw_si>.zl_active").html();
			if(fw_si == undefined || fw_si == "" || fw_si == null){
				fw_si="";
			}else{
				fw_si= "I"+fw_si
			}
			$("#fw_choose").html(fw_er+" I "+fw_san+ fw_si)
			_first="2"
			$(".t_pop").css("display","block");
			$('body').addClass("ds_tc");
			$("#viedo_sq").hide();
		})
		//关闭弹窗
		$("#btn_close").click(function(){
			$("#xq_cumtor").css("display","none");
			$(".t_pop").css("display","none");
			$('body').removeClass("ds_tc");
			$("#viedo_sq").show();
		})
		
		//点击二级 服务大类
		$(".fw_dl").on("touchstart","div",function(e){
			e.stopPropagation();
			$(this).addClass("dl_active");
			$(this).siblings().removeClass("dl_active");
			var v1_code =$(this).attr("v1_code");
			/*$(".pop_center").css("min-height","14rem");*/
			//获取第三级信息
			get_san(v1_code);
		})
		
		//点击三级 服务大类
		$("#fw_san").on("touchstart","div",function(e){
			e.stopPropagation();
			$(this).addClass("zl_active");
			$(this).siblings().removeClass("zl_active");
			var v2_code =$(this).attr("v2_code");
			//获取第四级信息
			get_si(v2_code);
		})
		//点击四级 服务大类
		$("#fw_si").on("touchstart","div",function(e){
			e.stopPropagation();
			$(this).addClass("zl_active");
			$(this).siblings().removeClass("zl_active");
			var v3_code =$(this).attr("v3_code");
			var _content =$(this).attr("content");
			if(_content == undefined){
				$(".sm_area").html("");
			}else{
				$(".sm_area").html(_content);
			}
			//获取
			get_wu(v3_code)
		})
		//点击五级 服务大类
		$("#fw_wu").on("touchstart","div",function(e){
			e.stopPropagation();
			$(this).addClass("zl_active");
			$(this).siblings().removeClass("zl_active");
			var v4_code =$(this).attr("v4_code");
			var _content =$(this).attr("content");
			$(".sm_area").html(_content);
			
			//获取价格
			get_price(v4_code)
		})
		
		//立即下单
		$("#go_money").click(function(){
			var _qpId = qp_id;//服务报价Id
			//数量
			var _num = $("#timeStart").val();
			//课程id
			var _cc = $("#class_name").css("display");
			if(_cc == "block"){
				var course_id = $(".fw_class>.class_active").attr("course_id");
			}else{
				var course_id = "";
			}
			window.location.href="../djyd/order_Prepayment.html?others=gods&num="+_num+"&qpId="+_qpId+"&course_id="+course_id;
		})
		
		//立即下单
		$("#go_money_1").click(function(){
			if(_first=='1'){
				$("#xq_cumtor").css("display","block");
				//服务大类名字
				var fw_er = $(".fw_dl>.dl_active").html();
				//服务子类名字三级
				var fw_san = $("#fw_san>.zl_active").html();
				//服务子类名字四级
				var fw_si =$("#fw_si>.zl_active").html();
				if(fw_si == undefined || fw_si == "" || fw_si == null){
					fw_si="";
				}else{
					fw_si= "I"+fw_si
				}
				$("#fw_choose").html(fw_er+" I "+fw_san+ fw_si)
				_first="2"
				$(".t_pop").css("display","block");
				$('body').addClass("ds_tc");
			}else{
				var _qpId = qp_id;//服务报价Id
				//数量
				var _num = $("#timeStart").val();
				//课程id
				var _cc = $("#class_name").css("display");
				if(_cc == "block"){
					var course_id = $(".fw_class>.class_active").attr("course_id");
				}else{
					var course_id = "";
				}
				window.location.href="../djyd/order_Prepayment.html?others=gods&num="+_num+"&qpId="+_qpId+"&course_id="+course_id;
			}
		})
		
		//三级信息
		function get_san(v1_code){
			var show_list_v2 = "";
			var show_list_course ="";
			
			for(var i=0;i<v2_arr.length;i++){
				if(v2_arr[i].parentCode == v1_code){
					show_list_v2+=`
						<div v2_code=${v2_arr[i].v2_code}>${v2_arr[i].v2_name}</div>
					`
				}
			}	
			$("#fw_san").empty();
			$("#fw_san").append(show_list_v2);
			$("#fw_san>div")[0].className="zl_active";
			var _code = $("#fw_san>.zl_active").attr("v2_code");
			get_si(_code);

		}
		//四级信息
		function get_si(_code){
			var show_list_v3 = "";
			var show_list_course = "";
			for(var i=0;i<v3_arr.length;i++){
				if(v3_arr[i].parentCode == _code){
					show_list_v3+=`
						<div v3_code=${v3_arr[i].v3_code} content=${v3_arr[i].content}>${v3_arr[i].v3_name}</div>
					`
				}
				if(v3_arr[i].course ==null || v3_arr[i].course =="" || v3_arr[i].course ==undefined){
					
				}else{
					for(var k=0;k<v3_arr[i].course.length;k++){
						show_list_course+=`
							<div parentCode =${v3_arr[i].v3_code} id=${v3_arr[i].course[k].id}>${v3_arr[i].course[k].name}</div>
						`
					}
				}
			}
			$("#fw_si").empty();
			$("#fw_si").append(show_list_v3);
			$("#fw_si>div")[0].className="zl_active";
			var _text = $("#fw_si>.zl_active").attr("content");
			if(_text == "undefined" || _text == undefined){
				$(".sm_area").html("");
			}else{
				$(".sm_area").html(_text);
			}
			if(show_list_course ==""){
				$("#class_name").hide();
			}else{
				$("#class_name").show();
				$(".fw_class").empty();
				$(".fw_class").append(show_list_course);
				$(".fw_class>div")[0].className="class_active";
			}
			var _code = $("#fw_si>.zl_active").attr("v3_code");
			get_wu(_code);
		}
		
		//获取五级
		function get_wu(v3_code){
			var show_list_course = "";
			var show_list_v4 = "";
			
			for(var i=0;i<v4_arr.length;i++){
				if(v4_arr[i].parentCode == v3_code){
					show_list_v4+=`
						<div v4_code=${v4_arr[i].v4_code} content =${v4_arr[i].content}>${v4_arr[i].v4_name}</div>
					`
				}
			}
			var _code = ""
			if(show_list_v4 == ""){
				$(".zl_wu").hide();
				_code = v3_code;
			}else{
				$(".zl_wu").show();
				$("#fw_wu").empty();
				$("#fw_wu").append(show_list_v4);
				$("#fw_wu>div")[0].className="zl_active";
				$(".sm_area").html($("#fw_wu>.zl_active").attr("content"));
				_code = $("#fw_wu>.zl_active").attr("v4_code");
			}
			var _course = $(".fw_class>.class_active").attr("parentcode");
			var v3_code = $("#fw_si>.zl_active").attr("v3_code");
			if(_course == v3_code){
				$("#class_name").show();
			}else{
				$("#class_name").hide();
			}
			get_price(_code);
		}
		//获取价格
		function get_price(_code){
			var gods_id = $(".xq_name").attr("gods_id");
			$.ajax({
				type: "POST",
				url: $main_URL_yd+"/v2/quoted_product/price",
				dataType: "json",
				data:{
					coachId:gods_id,
					code:_code
				},
				async: true,
				error: function(xhr, errorInfo, ex) {
					$.toast("查询价格信息错误！错误信息:" + errorInfo, "forbidden");
				},
				success: function(resp) { //请求完成
					if(resp.code == "200"){
						$(".pop_price").html(resp.data.quotedProduct.price+"元/人");
						$(".zs_price").html(resp.data.quotedProduct.price);
						qp_id = resp.data.quotedProduct.id;
//						var v1_code = $(".fw_dl>.dl_active").attr("v1_code");
//						if(v1_code == "10"){
//							$(".pop_center").css("min-height","13.5rem");
//						}else if(v1_code == "11"){
//							$(".pop_center").css("min-height","14rem");
//						}else if(v1_code == "12"){
//							$(".pop_center").css("min-height","14rem");
//						}else if(v1_code == "13"){
//							$(".pop_center").css("min-height","11rem");
//						}
					}else{
						$.toast(("价格获取失败"+resp.message));
					}
				}
			})
			
		}
		
		
		//获取大神信息
		function get_xx(gods_id,qp_id){
			$.ajax({
				type: "GET",
				url: $main_URL_yd + "/v2/product_type/coach_detail?coachId="+gods_id+"&qpId="+qp_id,
				async: true,		
				error: function (xhr, errorInfo, ex) {
					$.toast("查询大神信息错误！错误信息:" + errorInfo, "forbidden");
				},
				success: function (resp) { //请求完成
					if(resp.code == "200"){
						//教练详情
						var _coach = resp.data.coach;
						//大神名称
						$(".xq_name").html(_coach.name);
						$(".xq_name").attr("gods_id",_coach.id);
						//介绍
						$(".gods_js").html(_coach.introduction);
						//大神数据
						var _statis = resp.data.statis;
						//接单数
						if(_statis.counts<=1000){
							$("#gods_counts").html("&nbsp;"+"923")
						}else{
							$("#gods_counts").html("&nbsp;"+_statis.counts)
						}
						$("#gods_hours").html("&nbsp;"+_statis.hours);
						
						//数据数组
						var _list = resp.data.types;
						//默认
						var _def = resp.data.def;
						$(".pop_price").html(_def.qp.price+"元/人");
						$(".zs_price").html(_def.qp.price);
						
						var show_list = "";
						//默认二级code
						var show_list_v2 = "";
						var show_list_v2_arr = [];
						//默认三级code
						var show_list_3_arr  = [];
						var show_list_v3 = "";
						var show_list_course =[];
						//默认四级code
						var show_list_v4 = "";
						var show_list_4_arr = [];
						for(var i=0;i<_list.length;i++){
							if(_list[i].v1_code == _def.pro_type.v1_code){
								show_list+=`
									<div class="dl_active" v1_code = ${_list[i].v1_code}>${_list[i].v1_name}</div>
								`
							}else{
								show_list+=`
									<div v1_code = ${_list[i].v1_code}>${_list[i].v1_name}</div>
								`
							}
							if(!(_list[i].v2.length<=0)){
								for(var j=0;j<_list[i].v2.length;j++){
									if(_list[i].v2[j].v3.length!=0){
										var obj={
											parentCode:_list[i].v1_code,
											v2_code:_list[i].v2[j].v2_code,
											v2_name:_list[i].v2[j].v2_name,
											v3:_list[i].v2[j].v3,
										}
									}else{
										var obj={
											parentCode:_list[i].v1_code,
											v2_code:_list[i].v2[j].v2_code,
											v2_name:_list[i].v2[j].v2_name,
											v3:[]
										}
									}
									show_list_v2_arr.push(obj);
								}	
							}
						}
						$(".fw_dl").empty();
						$(".fw_dl").append(show_list);
						
						for(var i=0;i<show_list_v2_arr.length;i++){
							if(show_list_v2_arr[i].parentCode == _def.pro_type.v1_code){
								if(show_list_v2_arr[i].v2_code == _def.pro_type.v2_code){
									show_list_v2+=`
										<div class="zl_active" v2_code=${show_list_v2_arr[i].v2_code}>${show_list_v2_arr[i].v2_name}</div>
									`
								}else{
									show_list_v2+=`
										<div v2_code=${show_list_v2_arr[i].v2_code}>${show_list_v2_arr[i].v2_name}</div>
									`
								}
							}
							if(show_list_v2_arr[i].v3.length!=0){
								for(var k=0;k<show_list_v2_arr[i].v3.length;k++){
									if(show_list_v2_arr[i].v3[k].length!=0){
										var obj={
											parentCode:show_list_v2_arr[i].v2_code,
											v3_code:show_list_v2_arr[i].v3[k].v3_code,
											v3_name:show_list_v2_arr[i].v3[k].v3_name,
											content:show_list_v2_arr[i].v3[k].content,
											course:show_list_v2_arr[i].v3[k].course,
											v4:show_list_v2_arr[i].v3[k].v4,
										}
									}else{
										var obj={
											parentCode:show_list_v2_arr[i].v2_code,
											v3_code:show_list_v2_arr[i].v3[k].v3_code,
											v3_name:show_list_v2_arr[i].v3[k].v3_name,
											content:show_list_v2_arr[i].v3[k].content,
											course:show_list_v2_arr[i].v3[k].course,
											v4:[],
										}
									}
									show_list_3_arr.push(obj);
								}
							}
						}
						$("#fw_san").empty();
						$("#fw_san").append(show_list_v2);
						
						for(var i=0;i<show_list_3_arr.length;i++){
							if(show_list_3_arr[i].parentCode == _def.pro_type.v2_code){
								if(show_list_3_arr[i].v3_code == _def.pro_type.v3_code){
									show_list_v3+=`
										<div class="zl_active" v3_code=${show_list_3_arr[i].v3_code} content =${show_list_3_arr[i].content}>${show_list_3_arr[i].v3_name}</div>
									`
								}else{
									show_list_v3+=`
										<div v3_code=${show_list_3_arr[i].v3_code} content =${show_list_3_arr[i].content}>${show_list_3_arr[i].v3_name}</div>
									`
								}
							}
							if(show_list_3_arr[i].v4!=null){
								for(var k=0;k<show_list_3_arr[i].v4.length;k++){
									if(show_list_3_arr[i].v4[k].length!=0){
										var obj={
											parentCode:show_list_3_arr[i].v3_code,
											v4_code:show_list_3_arr[i].v4[k].v4_code,
											v4_name:show_list_3_arr[i].v4[k].v4_name,
											content:show_list_3_arr[i].v4[k].content,
										}
									}else{
										var obj={
											parentCode:show_list_v2_arr[i].v2_code,
											v4_code:show_list_v2_arr[i].v3[k].v4_code,
											v4_name:show_list_v2_arr[i].v3[k].v4_name,
											content:show_list_v2_arr[i].v3[k].content,
										}
									}
									show_list_4_arr.push(obj);
								}
							}
						}
						$("#fw_si").empty();
						$("#fw_si").append(show_list_v3);
						$(".sm_area").html($("#fw_si>.zl_active").attr("content"));
						if(_def.pro_type.hasOwnProperty("v4_code")){
							$(".zl_wu").show();
							for(var i=0;i<show_list_4_arr.length;i++){
								if(show_list_4_arr[i].parentCode == _def.pro_type.v3_code){
									if(show_list_4_arr[i].v4_code == _def.pro_type.v4_code){
										
									}
								}
							}
						}else{
							$(".zl_wu").hide();
						}
						var course_list="";
						if(_def.course.length!=0){
							$("#class_name").show();
							for(var i=0;i<_def.course.length;i++){
								course_list+=`
									<div id=${_def.course[i].id}>${_def.course[i].namei}</div>
								`
							}
							$(".fw_class").empty();
							$(".fw_class").append(course_list);
						}else{
							$("#class_name").hide();
						}
						v2_arr= show_list_v2_arr
						v3_arr= show_list_3_arr;//第三级
						v4_arr= show_list_4_arr;//第四级
					}
				}
			})
		}
		
		
		var MAX = 1000,
		MIN = 1;
		//减少
		$('.weui-count__decrease').click(function(e) {
	//		var _cc = bc_num;
			var $input = $(e.currentTarget).parent().find('.weui-count__number');
			var number = parseInt($input.val() || "0")-1; //- _cc
			if(number < MIN){
				number = MIN;
				$.toast("数量不能少于1", "forbidden");
			} 
			$input.val(number);
			_num = number;
	//		var _fw_id1= _fw_id;//产品id
	//		var _jl_id1= _jl_id;//教练id
	//		get_price(_jl_id1,_fw_id1,number)
		})
		//增加
		$('.weui-count__increase').click(function(e) {
	//		var _cc = bc_num;
			var $input = $(e.currentTarget).parent().find('.weui-count__number');
			var number = parseInt($input.val() || "0")+1; //+ _cc
			if(number > MAX){
				number = MAX;
				$.toast("数量不能大于1000", "forbidden");
			}
			$input.val(number);
			
			_num = number;
	//		var _fw_id1= _fw_id;//产品id
	//		var _jl_id1= _jl_id;//教练id
	//		get_price(_jl_id1,_fw_id1,number)
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
			$("#timeStart").val(_inpt);
		})
	}
})