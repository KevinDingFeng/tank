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
	var _type = getQueryString("code");//10教学11代练12陪玩
	var _first="1";
	var _qpId ="";
	init();
	function init(){
		var _height = $(document).height();
		$("#xq_cumtor").css("height",_height);
		//轮播
		var mySwiper = new Swiper('.swiper-container', {
			autoplay: 100000, //可选选项，自动滑动
			pagination: ".swiper-pagination",
			paginationClickable:true,//--实现小圆点点击
		})
		//点击选择
		$("#choose").click(function(){
			$("#xq_cumtor").css("display","block");
			$(".dl_choose a").css("padding","0 47px");
			$(".dl_choose a").css("width","12%");
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
		})
		//关闭弹窗
		$("#btn_close").click(function(){
			$("#xq_cumtor").css("display","none");
			$(".t_pop").css("display","none");
			$(".dl_choose a").css("padding","0");
			$(".dl_choose a").css("width","49%");
			$('body').removeClass("ds_tc");
		})
		var coach = [];//教练
		var v3_arr=[];//第三级
		var v4_arr=[];//第四级
		var v4_arr_c=[];//第四级
		
		if(_type == "10"){
			document.title = '教学';
			$("#c_type").html('教学');
			$(".dl_choose").hide();
			getlist(_type);
			//点击服务大类
			$(".fw_dl").on("touchstart","div",function(e){
				e.stopPropagation();
				$(this).addClass("dl_active");
				$(this).siblings().removeClass("dl_active");
				var v2_code =$(this).attr("v2_code");
				
				get_san(v2_code);
			})
			//点击服务子类
			$("#fw_san").on("touchstart","div",function(e){
				e.stopPropagation();
				$(this).addClass("zl_active");
				$(this).siblings().removeClass("zl_active");
				var v3_code =$(this).attr("v3_code");
				var gods_id = $(".gods_list>.gods_active").attr("gods_id");//大神id
				var _content =$(this).attr("content");
				$(".sm_area").html(_content)
				get_price(v3_code,gods_id)
			})
			//点击课程
			$(".fw_class").on("touchstart","div",function(e){
				e.stopPropagation();
				$(this).addClass("class_active");
				$(this).siblings().removeClass("class_active");
			})
			//点击教练
			$(".gods_list").on("touchstart","div",function(e){
				e.stopPropagation();
				$(this).addClass("gods_active");
				$(this).siblings().removeClass("gods_active");
				var v3_code = $("#fw_san>.zl_active").attr("v3_code");
				var gods_id = $(this).attr("gods_id");//大神id
				get_price(v3_code,gods_id)
			})
		}else if(_type == "11"){
			document.title = '代练';
			$("#c_type").html('代练');
			$("#xq_fw").html("即下即开 I 专业代练  I 安全无忧");
			$(".dl_choose").show();
			$("#class_biao").hide();
			getlist(_type);
			//点击服务大类
			$(".fw_dl").on("touchstart","div",function(e){
				e.stopPropagation();
				$(this).addClass("dl_active");
				$(this).siblings().removeClass("dl_active");
				var v2_code =$(this).attr("v2_code");
				get_san(v2_code);
			})
			//点击服务子类
			$("#fw_san").on("touchstart","div",function(e){
				e.stopPropagation();
				$(this).addClass("zl_active");
				$(this).siblings().removeClass("zl_active");
				var v3_code =$(this).attr("v3_code");
				var gods_id = $(".gods_list>.gods_active").attr("gods_id");//大神id
				var _content =$(this).attr("content");
				if(_content ==undefined){
					$(".sm_area").html("")
				}else{
					$(".sm_area").html(_content);
				}
				get_si(v3_code)
			})
			//点击服务子类四级
			$("#fw_si").on("touchstart","div",function(e){
				e.stopPropagation();
				$(this).addClass("zl_active");
				$(this).siblings().removeClass("zl_active");
				var v4_code =$(this).attr("v4_code");
				var gods_id = $(".gods_list>.gods_active").attr("gods_id");//大神id
				var _content =$(this).attr("content");
				if(_content ==undefined){
					$(".sm_area").html("")
				}else{
					$(".sm_area").html(_content);
				}
				get_price(v4_code,gods_id);
			})
			//点击教练
			$(".gods_list").on("touchstart","div",function(e){
				e.stopPropagation();
				$(this).addClass("gods_active");
				$(this).siblings().removeClass("gods_active");
				var v3_code = $("#fw_san>.zl_active").attr("v3_code");
				var gods_id = $(this).attr("gods_id");//大神id
				get_si(v3_code)			
			})
			$(".dl_choose>a").click(function(){
				$(this).addClass("active");
				$(this).children().css("display","block");
				$(this).siblings().removeClass("active");
				$(this).siblings().children().css("display","none");
				_type = $(this).attr("code");
				if(_type == "11"){
					getlist(_type);
					//点击服务大类
					$(".fw_dl").on("touchstart","div",function(e){
						e.stopPropagation();
						$(this).addClass("dl_active");
						$(this).siblings().removeClass("dl_active");
						var v2_code =$(this).attr("v2_code");
						get_san(v2_code);
					})
					//点击服务子类
					$("#fw_san").on("touchstart","div",function(e){
						e.stopPropagation();
						$(this).addClass("zl_active");
						$(this).siblings().removeClass("zl_active");
						var v3_code =$(this).attr("v3_code");
						var gods_id = $(".gods_list>.gods_active").attr("gods_id");//大神id
						var _content =$(this).attr("content");
						if(_content ==undefined){
							$(".sm_area").html("")
						}else{
							$(".sm_area").html(_content);
						}
						get_si(v3_code)
					})
					//点击服务子类四级
					$("#fw_si").on("touchstart","div",function(e){
						e.stopPropagation();
						$(this).addClass("zl_active");
						$(this).siblings().removeClass("zl_active");
						var v4_code =$(this).attr("v4_code");
						var gods_id = $(".gods_list>.gods_active").attr("gods_id");//大神id
						var _content =$(this).attr("content");
						if(_content ==undefined){
							$(".sm_area").html("")
						}else{
							$(".sm_area").html(_content);
						}
						get_price(v4_code,gods_id);
					})
					//点击教练
					$(".gods_list").on("touchstart","div",function(e){
						e.stopPropagation();
						$(this).addClass("gods_active");
						$(this).siblings().removeClass("gods_active");
						var v3_code = $("#fw_san>.zl_active").attr("v3_code");
						var gods_id = $(this).attr("gods_id");//大神id
						if(v4_arr.length<=0){
							get_price(v3_code,gods_id)
						}else{
							$(".pop_center").css("min-height","14rem");
							get_si(v3_code)
						}
					})
				}else if(_type == "12"){
					getlist(_type);
					//点击服务大类
					$(".fw_dl").on("touchstart","div",function(e){
						e.stopPropagation();
						$(this).addClass("dl_active");
						$(this).siblings().removeClass("dl_active");
						var v2_code =$(this).attr("v2_code");
						get_san(v2_code);
					})
					//点击服务子类
					$("#fw_san").on("touchstart","div",function(e){
						e.stopPropagation();
						$(this).addClass("zl_active");
						$(this).siblings().removeClass("zl_active");
						var v3_code =$(this).attr("v3_code");
						var gods_id = $(".gods_list>.gods_active").attr("gods_id");//大神id
						var _content =$(this).attr("content");
						if(_content ==undefined){
							$(".sm_area").html("")
						}else{
							$(".sm_area").html(_content);
						}
						get_si(v3_code)
					})
					//点击服务子类四级
					$("#fw_si").on("touchstart","div",function(e){
						e.stopPropagation();
						$(this).addClass("zl_active");
						$(this).siblings().removeClass("zl_active");
						var v4_code =$(this).attr("v4_code");
						var gods_id = $(".gods_list>.gods_active").attr("gods_id");//大神id
						var _content =$(this).attr("content");
						if(_content ==undefined){
							$(".sm_area").html("")
						}else{
							$(".sm_area").html(_content);
						}
						get_price(v4_code,gods_id);
					})
					//点击教练
					$(".gods_list").on("touchstart","div",function(e){
						e.stopPropagation();
						$(this).addClass("gods_active");
						$(this).siblings().removeClass("gods_active");
						var v3_code = $("#fw_san>.zl_active").attr("v3_code");
						var gods_id = $(this).attr("gods_id");//大神id
						debugger
						if(v4_arr.length<=0){
							get_price(v3_code,gods_id)
						}else{
							$(".pop_center").css("min-height","14rem");
							get_si(v3_code)
						}
					})
				}
			})
		}else if(_type == "13"){
			document.title = '陪玩';
			$("#c_type").html('陪玩');
			$("#xq_fw").html("即开即下 I 专业陪玩  I 安全可靠");
			$("#class_biao").hide();
			$(".dl_choose").hide();
			getlist(_type);
			//点击教练
			$(".gods_list").on("touchstart","div",function(e){
				e.stopPropagation();
				$(this).addClass("gods_active");
				$(this).siblings().removeClass("gods_active");
				var v3_code = $("#fw_san>.zl_active").attr("v3_code");
				var gods_id = $(this).attr("gods_id");//大神id
				if(v4_arr.length<=0){
					get_price(v3_code,gods_id)
				}else{
					$(".pop_center").css("min-height","14rem");
					get_si(v3_code)
				}
			})
			
		}
	}
	//去预支付页面
	$("#go_money").click(function(){
		window.location.href="../djyd/order_Prepayment.html?others=others"
	})
	//处理三级
	function get_san(v2_code){
		var san_list = v3_arr;
		var show_list = "";
		var show_list_4 = "";
		for(var i=0;i<san_list.length;i++){
			if(san_list[i].parent_code == v2_code){
				show_list+=`
					<div v3_code = ${san_list[i].code} content=${san_list[i].content}>${san_list[i].name}</div>
				`
				if(san_list[i].course_name!=undefined){
					if(san_list[i].course_name.length!=0){
						show_list_4=`
							<div class="class_active" course_id = ${san_list[i].course_name[0].id}>${san_list[i].course_name[0].name}</div>
						`
						for(var j=1;j<san_list[i].course_name.length;j++){
							show_list_4+=`
								<div course_id = ${san_list[i].course_name[j].id}>${san_list[i].course_name[j].name}</div>
							`
						}
					}
				}
			}
		}
		$("#fw_san").empty();
		$("#fw_san").append(show_list);
		$("#fw_san>div")[0].className="zl_active";
		if($("#fw_san>.zl_active").attr("content") ==undefined){
			$(".sm_area").html("")
		}else{
			$(".sm_area").html($("#fw_san>.zl_active").attr("content"));
		}
		if(show_list_4.length!=0){
			$("#class_ds").show();
			$(".fw_class").empty();
			$(".fw_class").append(show_list_4);
		}else{
			$("#class_ds").hide();
		}
		
		
		var v3_code = $("#fw_san>.zl_active").attr("v3_code");//第三级code
		var gods_id = $(".gods_list>.gods_active").attr("gods_id");//大神id
		if(v4_arr.length<=0){
			get_price(v3_code,gods_id);
		}else{
			get_si(v3_code)
		}	
	}
	//获取第四级
	function get_si(v3_code){
		var san_list = v4_arr;
		var list_v4="";
		for(var i=0;i<san_list.length;i++){
			if(san_list[i].parent_code == v3_code){
				list_v4+=`
					<div v4_code = ${san_list[i].code} content=${san_list[i].content}>${san_list[i].name}</div>
				`
			}
		}
		if(list_v4!=""){
			$(".hassi").show();
			$("#fw_si").empty();
			$("#fw_si").append(list_v4);
			
			$("#fw_si>div")[0].className="zl_active";
			if($("#fw_si>.zl_active").attr("content") ==undefined){
				$(".sm_area").html("")
			}else{
				$(".sm_area").html($("#fw_si>.zl_active").attr("content"));
			}
			var v3_code = $("#fw_si>.zl_active").attr("v4_code");//第三级code
			var gods_id = $(".gods_list>.gods_active").attr("gods_id");//大神id
			get_price(v3_code,gods_id);
		}else{
			$(".hassi").hide();
			var v3_code = $("#fw_san>.zl_active").attr("v3_code");//第三级code
			var gods_id = $(".gods_list>.gods_active").attr("gods_id");//大神id
			get_price(v3_code,gods_id);
		}
	}
	//获取默认价格
	function get_price(v3_code,gods_id){
		$.ajax({
			type: "POST",
			url: $main_URL_yd+"/v2/quoted_product/price",
			dataType: "json",
			data:{
				coachId:gods_id,
				code:v3_code
			},
			async: true,
			error: function(xhr, errorInfo, ex) {
				$.toast("查询价格信息错误！错误信息:" + errorInfo, "forbidden");
			},
			success: function(resp) { //请求完成
				if(resp.code == "200"){
					_qpId=resp.data.quotedProduct.id;
					var _price = resp.data.quotedProduct.price;
					$(".pop_price").html(_price+"元/人");//展示的价格
					$("#zd_price").html(_price.price);//展示的价格
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
					$("#fw_choose").html(fw_er+" I "+fw_san+ fw_si);
//					var v2_code = $(".fw_dl>.dl_active").attr("v2_code");
//					if(v2_code == "1101"){
//						$(".pop_center").css("min-height","14rem");
//					}else if(v2_code == "1105" ||v2_code == "1104" ||v2_code == "1106"){
//						$(".pop_center").css("min-height","12rem");
//					}else if(v2_code == "1102" ||v2_code == "1103"){
//						$(".pop_center").css("min-height","16rem");
//					}
//					if(_type =="12"){
//						$(".pop_center").css("min-height","14rem");
//					}
				}
			}
		})
	}
	//获取数据 添加默认
	function getlist(_type){
		$.ajax({
			type: "GET",
			url: $main_URL_yd+"/v2/product_type/detail?typeCode="+_type,
			dataType: "json",
			async: true,
			error: function(xhr, errorInfo, ex) {
				$.toast("查询当前信息错误！错误信息:" + errorInfo, "forbidden");
			},
			success: function(resp) { //请求完成
				if(resp.code == "200"){
					var _v1 = resp.data.v1;//类型介绍
					var _statis = resp.data.statis;//数据
					//接单数
					if(_statis.counts<=1000){
						$("#jd_num").html("&nbsp;"+"923")
					}else{
						$("#jd_num").html("&nbsp;"+_statis.counts)
					}
					//简介
					$("#type_name").html(_v1.remark);
					
					//服务时长
					$("#fw_num").html("&nbsp;"+_statis.hours);
					//列表
					var _list = resp.data.types;
					//上来默认选中
					var _def = resp.data.def;
					_qpId=_def.qp.id
					var def_v2 = resp.data.def.pro_type.v2_code;//默认v2id
					
					var def_qp = resp.data.def.qp;//默认qb
					$(".pop_price").html(def_qp.price+"元/人");//展示的价格
					$("#zd_price").html(def_qp.price);//展示的价格
					var show_list = "";
					var show_list_3_arr  = [];
					var show_list_4 = "";
					var show_list_4_arr  = [];
					for(var i=0;i<_list.length;i++){
						if(_list[i].v2_code == def_v2){
							show_list+=`
								<div class="dl_active" v2_code = ${_list[i].v2_code}>${_list[i].v2_name}</div>
							`
						}else{
							show_list+=`
								<div v2_code = ${_list[i].v2_code}>${_list[i].v2_name}</div>
							`
						}
						for(var j=0;j<_list[i].v3.length;j++){							
							if(_list[i].v3[j].v4 !=null){
								obj_3={
									parent_code:_list[i].v2_code,
									code:_list[i].v3[j].v3_code,
									name:_list[i].v3[j].v3_name,
									course_name:_list[i].v3[j].course
								}
								show_list_3_arr.push(obj_3);
								for(var k=0;k<_list[i].v3[j].v4.length;k++){
									obj_4={
										parent_code:_list[i].v3[j].v3_code,
										code:_list[i].v3[j].v4[k].v4_code,
										name:_list[i].v3[j].v4[k].v4_name,
										content:_list[i].v3[j].v4[k].content
									}
									show_list_4_arr.push(obj_4);
								}
							}else{
								obj_3={
									parent_code:_list[i].v2_code,
									code:_list[i].v3[j].v3_code,
									name:_list[i].v3[j].v3_name,
									course_name:_list[i].v3[j].course,
									content:_list[i].v3[j].content
								}
								show_list_3_arr.push(obj_3);
							}
						}						
					}
					$(".fw_dl").empty();
					$(".fw_dl").append(show_list);
					
					
					var def_v3 = resp.data.def.pro_type.v3_code;//默认v3id
					var def_v2 = resp.data.def.pro_type.v2_code;//默认v3id
					var show_list_3 = "";
					var show_list_course = "";
					for(var i=0;i<show_list_3_arr.length;i++){
						if(show_list_3_arr[i].parent_code == def_v2){
							if(show_list_3_arr[i].code == def_v3){
								$(".sm_area").html(show_list_3_arr[i].content)
								show_list_3+=`
									<div class="zl_active" v3_code = ${show_list_3_arr[i].code} content=${show_list_3_arr[i].content}>${show_list_3_arr[i].name}</div>
								`
								if(show_list_3_arr[i].course_name!=undefined){
									if(show_list_3_arr[i].course_name.length!=0){
										show_list_course=`
											<div class="class_active" course_id=${show_list_3_arr[i].course_name[0].id}>${show_list_3_arr[i].course_name[0].name}</div>
										`
										for(var j=1;j<show_list_3_arr[i].course_name.length;j++){
											show_list_course+=`
												<div course_id=${show_list_3_arr[i].course_name[j].id}>${show_list_3_arr[i].course_name[j].name}</div>
											`
										}
									}
								}
							}else{
								show_list_3+=`
									<div v3_code = ${show_list_3_arr[i].code} content=${show_list_3_arr[i].content}>${show_list_3_arr[i].name}</div>
								`
							}
						}
					}
					$("#fw_san").empty();
					$("#fw_san").append(show_list_3);
					$(".fw_class").empty();
					if(show_list_course.length!=0){
						$("#class_ds").show();
						$(".fw_class").append(show_list_course);
					}else{
						$("#class_ds").hide();
					}
					
/*					if(show_list_4_arr.length!=0){
						$(".hassi").css("display","block")
					}
					*/
					/**/
					
					//教练
					var _coash = resp.data.coaches;//教练
					var def_coach = resp.data.def.coachId;//默认教练id
					var list_coash = "";
					for(var i=0;i<_coash.length;i++){
						if(_coash[i].id == def_coach){
							list_coash+=`
								<div class="gods_active" gods_id=${_coash[i].id}>${_coash[i].name}</div>
							`
						}else{
							list_coash+=`
								<div gods_id=${_coash[i].id}>${_coash[i].name}</div>
							`
						}
					}
					$(".gods_list").empty();
					$(".gods_list").append(list_coash);
										
					coach = _coash;//教练
					v3_arr=show_list_3_arr;//第三级
					v4_arr= show_list_4_arr;//第四级
					v4_arr_c= show_list_4_arr;//第四级
				}else{
					console.log(resp.message)
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
		if(number <= MIN){
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
	//下单
	$("#go_money").click(function(){
		if(_first=='1'){
			$.toast("请先选择服务", "forbidden");
			return;
		}else{
			var qpId = _qpId;//服务报价Id
			//数量
			var _num = $("#timeStart").val();
			//课程id
			var _cc = $("#class_ds").css("display");
			if(_cc == "block"){
				var course_id = $(".fw_class>.class_active").attr("course_id");
			}else{
				var course_id = "";
			}
			debugger
			window.location.href="../djyd/order_Prepayment.html?others=others&num="+_num+"&qpId="+_qpId+"&course_id="+course_id;
		}
	})
	//下单
	$("#go_money_1").click(function(){
		if(_first=='1'){
			$.toast("请先选择服务", "forbidden");
			return;
		}else{
			var qpId = _qpId;//服务报价Id
			//数量
			var _num = $("#timeStart").val();
			//课程id
			var _cc = $("#class_ds").css("display");
			if(_cc == "block"){
				var course_id = $(".fw_class>.class_active").attr("course_id");
			}else{
				var course_id = "";
			}
			window.location.href="../djyd/order_Prepayment.html?others=others&num="+_num+"&qpId="+_qpId+"&course_id="+course_id;
		}
	})
})
	





















