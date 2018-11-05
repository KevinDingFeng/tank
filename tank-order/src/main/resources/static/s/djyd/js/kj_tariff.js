$(document).ready(function() {
	init();
	var _type_cc="10";
	function init(){
		
		get_list("10");
		//点击头部切换
		$(".tariff_ul>li").click(function(){
			var _type = $(this).attr("type");
			if(_type == "10"){
				$(this).find("img").attr("src","images/kj/kj_stu_x.png");
				var _arr = $(this).siblings().find("img");
				_arr[0].src = "images/kj/kj_dl_w.png";
				_arr[1].src = "images/kj/kj_sg_w.png";
				_arr[2].src = "images/kj/kj_pw_w.png";
				
				$(this).find("div").addClass("kj_active");
				$(this).siblings().children("div").removeClass("kj_active");
				get_list("10")
			}else if(_type == "11"){
				$(this).find("img").attr("src","images/kj/kj_dl_x.png");
				var _arr = $(this).siblings().find("img");
				
				_arr[0].src = "images/kj/kj_stu_w.png";
				_arr[1].src = "images/kj/kj_sg_w.png";
				_arr[2].src = "images/kj/kj_pw_w.png";
				
				$(this).find("div").addClass("kj_active");
				$(this).siblings().children("div").removeClass("kj_active");
				get_list("11")
			}else if(_type == "12"){
				$(this).find("img").attr("src","images/kj/kj_sg_x.png");
				var _arr = $(this).siblings().find("img");
				
				_arr[0].src = "images/kj/kj_stu_w.png";
				_arr[1].src = "images/kj/kj_dl_w.png";
				_arr[2].src = "images/kj/kj_pw_w.png";
				
				$(this).find("div").addClass("kj_active");
				$(this).siblings().children("div").removeClass("kj_active");
				get_list("12")
			}else if(_type == "13"){
				$(this).find("img").attr("src","images/kj/kj_pw_x.png");
				var _arr = $(this).siblings().find("img");
				_arr[0].src = "images/kj/kj_stu_w.png";
				_arr[1].src = "images/kj/kj_dl_w.png";
				_arr[2].src = "images/kj/kj_sg_w.png";
				
				$(this).find("div").addClass("kj_active");
				$(this).siblings().children("div").removeClass("kj_active");
				get_list("13")
			}
		})
		//点击立即支付
		$(".kj_zf").click(function(){
			var _cc = _type_cc;
			window.location.href="../djyd/kj_order.html?_type="+_cc;
		})
	}
	
	function get_list(type){
		var _type = type;
		_type_cc = _type;
		$.ajax({
			type: "GET",
			url: $main_URL_yd+"/quoted_product/rapid_price",
			dataType: "json",
			async: true,
			error: function(xhr, errorInfo, ex) {
				$.toast("查询价目表信息错误！错误信息:" + errorInfo, "forbidden");
			},
			success: function(resp) { //请求完成
				if(resp.code == "200"){
					if(_type == "10"){
						var _arr = resp.data.teaching;//教学
					}else if(type == "11"){
						var _arr = resp.data.replacing;//代练
					}else if(type == "12"){
						var _arr = resp.data.craftreplacing;//手工代练
					}else if(type == "13"){
						var _arr = resp.data.accompany;//陪玩
					}
					var list = "";
					for(var i=0;i<_arr.price.length;i++){
						
						if(_arr.price[i].durationType =="时"){
							var _dd = "小时"
						}else if(_arr.price[i].durationType =="1万经验"){
							var _dd = "无";
							_arr.price[i].duration =""
						}else if(_arr.price[i].durationType =="10万经验"){
							var _dd = "无";
							_arr.price[i].duration =""
						}else if(_arr.price[i].durationType =="15万经验"){
							var _dd = "无";
							_arr.price[i].duration =""
						}else if(_arr.price[i].durationType =="次"){
							var _dd = "次";
						}else if(_arr.price[i].durationType =="天"){
							var _dd = "天";
						}
						if(_arr.price[i].hasOwnProperty("level4Name")){//判断有没有第四级
							var _level4Name= _arr.price[i].level4Name;//
							
							if(_arr.price[i].level2Name.length == 4){
								list +=`
									<li>
										<div>${_arr.price[i].level3Name}<span>${_level4Name}</span></div>
										<div class="price_div">
											<div class="pirce_left">
												￥<span>${_arr.price[i].price}</span>/${_arr.price[i].durationType}
											</div>
											<div class="pirce_right">
												<span class="pirce_time pirce_right">服务时长${_arr.price[i].duration}${_dd}</span>
											</div>
										</div>
										<span class="kj_pq">${_arr.price[i].level2Name}</span>
									</li>
								`;
							}else if(_arr.price[i].level2Name.length == 5){
								list +=`
									<li>
										<div>${_arr.price[i].level3Name}<span class="span_nor"}>${_level4Name}</span></div>
										<div class="price_div">
											<div class="pirce_left">
												￥<span>${_arr.price[i].price}</span>${_arr.price[i].durationType}
											</div>
											<div class="pirce_right">
												<span class="pirce_time pirce_right">服务时长${_arr.price[i].duration}${_dd}</span>
											</div>
										</div>
										<span class="kj_pq" style="right:0.6rem">${_arr.price[i].level2Name}</span>
									</li>
								`;
							}else if(_arr.price[i].level2Name.length == 2){
								list +=`
									<li>
										<div>${_arr.price[i].level3Name}<span class="span_nor">${_level4Name}</span></div>
										<div class="price_div">
											<div class="pirce_left">
												￥<span>${_arr.price[i].price}</span>/${_arr.price[i].durationType}
											</div>
											<div class="pirce_right">
												<span class="pirce_time pirce_right">服务时长${_arr.price[i].duration}${_dd}</span>
											</div>
										</div>
										<span class="kj_pq" style="right:1rem">${_arr.price[i].level2Name}</span>
									</li>
								`;
							}
							
						}
						else{
							if(_arr.price[i].level2Name.length == 4){
								list +=`
									<li>
										<div>${_arr.price[i].level3Name}</div>
										<div class="price_div">
											<div class="pirce_left">
												￥<span>${_arr.price[i].price}</span>/${_arr.price[i].durationType}
											</div>
											<div class="pirce_right">
												<span class="pirce_time pirce_right">服务时长${_arr.price[i].duration}${_dd}</span>
											</div>
										</div>
										<span class="kj_pq">${_arr.price[i].level2Name}</span>
									</li>
								`;
							}else if(_arr.price[i].level2Name.length == 5){
								list +=`
									<li>
										<div>${_arr.price[i].level3Name}</div>
										<div class="price_div">
											<div class="pirce_left">
												￥<span>${_arr.price[i].price}</span>/${_arr.price[i].durationType}
											</div>
											<div class="pirce_right">
												<span class="pirce_time pirce_right">服务时长${_arr.price[i].duration}${_dd}</span>
											</div>
										</div>
										<span class="kj_pq" style="right:0.6rem">${_arr.price[i].level2Name}</span>
									</li>
								`;
							}else if(_arr.price[i].level2Name.length == 2){
								list +=`
									<li>
										<div>${_arr.price[i].level3Name}</div>
										<div class="price_div">
											<div class="pirce_left">
												￥<span>${_arr.price[i].price}</span>/${_arr.price[i].durationType}
											</div>
											<div class="pirce_right">
												<span class="pirce_time pirce_right">服务时长${_arr.price[i].duration}${_dd}</span>
											</div>
										</div>
										<span class="kj_pq" style="right:1rem">${_arr.price[i].level2Name}</span>
									</li>
								`;
							}
						}
					}
					$(".tariff_list").empty();
					$(".tariff_list").append(list);
				}
			
			}
		})
	}
})