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
	//大神id
	var gods_id = getQueryString("id");
	//类型
	var _type = getQueryString("code");//10教学11代练12陪玩
	
	var jx_name = "最强职业冠军玩家，游戏大神专业指导，一对一在线教学，全程直播，你想要get的技能我们都能满足！";//教学介绍
	var dl_name = "24h代练服务，职业冠军级游戏大神，绝对完美的游戏代练技术，一切准备，只为您展示精彩!";//代练介绍
	var pw_name = "大神陪玩，陪你驰骋沙场，让你开心让你爽，还能学习教练的各种绝活~";//陪玩介绍
	
	var dl_arrlist =[];//代练项目
	var jx_arrlist = [];//教学项目
	var pw_arrlist = [];//陪玩项目
	var _level_3;
	var _product_list;
	var gods_name = "";
	init();
	function init(){
		if(_type == "10"){//教学
			get_user(gods_id);
			//点击左侧代练二级
			$(".prise_left").on('click',".left_text",function(e){
				$(this).addClass("active");
				$(this).siblings().removeClass("active");
				$(this).append(`
					<text class="text_tiao"></text>	
				`);
				$(this).siblings().children(".text_tiao").remove();
				var _code =$(this).attr("code");
				get_product(_code);
				
			})
			
		}else if(_type == "11"){//代练
			get_user(gods_id);
			//点击左侧代练二级
			$(".prise_left").on('click',".left_text",function(e){
				$(this).addClass("active");
				$(this).siblings().removeClass("active");
				$(this).append(`
					<text class="text_tiao"></text>	
				`);
				$(this).siblings().children(".text_tiao").remove();
				var _code =$(this).attr("code");
				var _id =$(this).attr("id");
				get_product(_code,_id);
			})
			
		}else if(_type == "12"){//陪玩
			get_user(gods_id);
			//点击左侧代练二级
			$(".prise_left").on('click',".left_text",function(e){
				$(this).addClass("active");
				$(this).siblings().removeClass("active");
				$(this).append(`
					<text class="text_tiao"></text>	
				`);
				$(this).siblings().children(".text_tiao").remove();
				var _code =$(this).attr("code");
				var _id =$(this).attr("id");
				get_product(_code,_id);
			})
			
		}
	}
	//获取用户数据 大神数据
	function get_user(gods_id){
		$.ajax({
			type: "GET",
			url: $main_URL_yd + "/quoted_product/coach?code="+_type +"&coachId="+gods_id,
			async: true,		
			error: function (xhr, errorInfo, ex) {
				$.toast("查询大神信息错误！错误信息:" + errorInfo, "forbidden");
			},
			success: function (resp) { //请求完成
				if (resp.code == "200") {
					var gods_xx = resp.data.coach;//大神信息
					var _tou = gods_xx.iconUrl;//头像
					$(".gods_content_m").html(gods_xx.name);//大神名称
					if(gods_xx.introduction == ""){
						gods_xx.introduction = "暂无介绍！"
					}
					$(".gods_content_j").html(gods_xx.introduction);//一句话介绍
					$(".dl_name").html(gods_xx.name);//大神名称
					$(".dl_js").html(gods_xx.description);//大神介绍
/*//					$(".listnumber_left_num").html();//接单数
//					$(".listnumber_left_time").html();//时长
//					$(".listnumber_left_xing").html();//星级*/
					//教学
					if(_type == "10"){
						$(".gods_content_m").html("《专业教学》");//大神名称
						$(".gods_content_j").html("");//一句话介绍
						$(".dl_name").html("《专业教学》");//大神名称
						$(".dl_js").html(jx_name);//大神介绍
					}
					var product_list = resp.data.level2;//产品信息
					_product_list="";
					_product_list = product_list;
					var list1 = `
						<div class="left_text" code=${product_list[0].code} id=${product_list[0].id}>
							<text>${product_list[0].name}</text>
							<text class="text_tiao"></text>
						</div>
					`;
					for(var i = 1;i < product_list.length;i++){
						list1+=`
							<div class="left_text" code=${product_list[i].code} id=${product_list[i].id}>
								<text>${product_list[i].name}</text>
							</div>
						`;
					}	
					$(".prise_left").empty();
					$(".prise_left").append(list1);
					var _xx = resp.data.products;//第三级数据
					_level_3 = _xx;
					if(JSON.stringify(_xx)=="{}"){
						var list_2 = `
							<div class="right_content width_cc">
								<div class="title_top">
									<p class="title_pname">暂无</p>
								</div>
							</div>
						`;
						$(".prise_right").empty()
						$(".prise_right").append(list_2);
					}else{
						if(_type == "11"){
							var _cc = "1101"
							get_product(_cc);
						}else if(_type == "12"){
							var _cc = "1201"
							get_product(_cc);
						}else if(_type == "10"){
							var _cc = "1001"
							get_product(_cc);
						}
					}
				}
			}
		})
	}
	//获取第三级
	function get_product(_code,_id){
		var _cc = _code;
		var code = "L"+ _code;
		if(_level_3.hasOwnProperty(code)){
			var level3_list = _level_3[code];
			var list_2 = "";
			for(var i=0; i<level3_list.length;i++){
				if(level3_list[i].product.productType.remark == ""){
					level3_list[i].product.productType.remark ="暂无介绍"
				}
				list_2+=`
					<div class="right_content">
						<div class="title_top">
							<p class="title_pname1">${level3_list[i].product.productType.name}</p>
							<p class="title_price">￥<text class="font_col">${level3_list[i].price}</text>元</p>
							<p class="title_text">${level3_list[i].product.productType.remark}</p>
							<p class="title_xd">
								<text class="xd_font" _id=${level3_list[i].id} er_code = ${_cc} code = ${_type}  zx_code =${level3_list[i].product.productType.code} gods_id = ${gods_id}>我要下单</text>
								<img src="images/xd_jt.png" />
							</p>
						</div> 
					</div> 
				`;
			}
			$(".prise_right").empty()
			$(".prise_right").append(list_2);
			//点击下单跳转下一个界面
			$(".xd_font").click(function(){
				var zx_code = $(this).attr("zx_code");//10教学11代练12陪玩
				var code = $(this).attr("code");//三级code
				var er_code = $(this).attr("er_code");//二级code
				var gods_id = $(this).attr("gods_id");//大神id
				var _id = $(this).attr("_id");//大神id
				window.location.href="order_sure.html?code="+ code +"&zx_code="+ zx_code+"&er_code="+er_code+"&gods_id="+gods_id+"&_id="+_id;
			})
		}else{
			var list_2 = `
				<div class="right_content width_cc">
					<div class="title_top">
						<p class="title_pname">暂无</p>
					</div>
				</div>
			`;
			$(".prise_right").empty()
			$(".prise_right").append(list_2);
			
		}
		
	}
})























