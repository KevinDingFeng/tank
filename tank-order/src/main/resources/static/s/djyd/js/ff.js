$(document).ready(function () {
	//url取参
	function getQueryString(name) {
	    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
	    var r = window.location.search.substr(1).match(reg);
	    if (r != null) {
	        return unescape(r[2]);
	    }
	    return null;
	}
	var _type = getQueryString("type");//类型
	init();

	function init() {
		if(_type == "dl"){
			gods_search("11");
			$("#Ringer").addClass("active");
			$("#Ringer").siblings().removeClass("active");
			$("#Ringer>.bottom_bor").css("display","block");
			$("#ladder>.bottom_bor").css("display","none");
		}else if(_type == "pw"){
			gods_search("12");
			$("#ladder").addClass("active");
			$("#ladder").siblings().removeClass("active");
			$("#ladder>.bottom_bor").css("display","block");
			$("#Ringer>.bottom_bor").css("display","none");
		}else if(_type == "jx"){
			window.location.href = "Ringer_xq.html?id=9&code=10";
		}else{
			gods_search("11");
		}
		
		$(".Ringer_logo_right a").attr("href","Ringer_xq.html?id=9&code=11");
		//导航显示 下面内容
		$(".god_top a").click(function () {
			$(this).addClass("active");
			$(this).siblings().removeClass("active");
			$(this).children().css("display", "block")
			$(this).siblings().children().css("display", "none");
			var c_v = $(this).attr("c_v");
			if (c_v == "11") {
				$(".Ringer").css("display", "block");
				$(".student").css("display", "none");
				$(".ladder").css("display", "none");
				gods_search("11");
				$(".Ringer_logo_left span").html("代练大神")
				$(".Ringer_logo_right a").attr("href","Ringer_xq.html?id=9&code=11");
			} else if (c_v == "12") {
				window.location.href = "Ringer_xq.html?id=1&code=10";
			} else {
				$(".Ringer").css("display", "block");
				$(".student").css("display", "none");
				$(".ladder").css("display", "none");
				$(".Ringer_logo_right a").attr("href","Ringer_xq.html?id=9&code=12");
				$(".Ringer_logo_left span").html("陪玩大神")
				gods_search("12");
			}
		});

		//查询大神
		function gods_search(c_v) {
			$(".Ringer_content").empty();
			$.ajax({
				type: "GET",
				url: $main_URL_yd + "/quoted_default/type?code=" +c_v,
				async: true,		
				error: function (xhr, errorInfo, ex) {
					$.toast("查询打手错误！错误信息:" + errorInfo, "forbidden");
				},
				success: function (resp) { //请求完成
					if (resp.code == "200") {
						var _arrs = resp.data.page.content;
						if (_arrs.length == 0) {
							$.toast("暂无大神", "forbidden");
							return
						} else {
							for (var i = 0; i < _arrs.length; i++) {
								var _img = _arrs[i].coach.iconUrl;
								var gods_tou = _img;
								if(_arrs[i].coach.introduction == ""){
									_arrs[i].coach.introduction ="暂无介绍"
								}else if(_arrs[i].coach.introduction.length > 14){
									_arrs[i].coach.introduction = _arrs[i].coach.introduction.substr(0, 14) + "...";
								}
								$(".Ringer_content").append(`
										<div class="gods_list" list_id=${_arrs[i].coach.id} type="">
											<div class="gods_toux">
												<img src=${gods_tou}/>
											</div>
											<div class="gods_content">
												<p class="gods_content_name">${_arrs[i].coach.name}</p>
												<p class="gods_content_jse">${_arrs[i].coach.introduction}</p>
												<p class="gods_content_price">￥${_arrs[i].price}元/小时起</p>
											</div>
										</div>
								`)
							}

							//代练详情跳转
							$(".gods_list").click(function () {
								var gods_id = $(this).attr("list_id");
								window.location.href = "Ringer_xq.html?id=" + gods_id + "&code=" + c_v;
							})
						}
					} else if (resp.code == "400") {
						$.toast("查询打手失败！失败信息:" + errorInfo, "forbidden");
					}
				}
			});
		}
	}
})