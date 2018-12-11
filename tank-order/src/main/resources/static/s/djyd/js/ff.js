$(document).ready(function () {
	init();
	function init() {
		var _type = "zb_ds"
		gods_search();
		if(_type == "zb_ds"){
			$("#Ringer").addClass("active");
			$("#Ringer").siblings().removeClass("active");
			$("#Ringer>.bottom_bor").css("display","block");
			$("#ladder>.bottom_bor").css("display","none");
		}else if(_type == "qt_ds"){
			$("#ladder").addClass("active");
			$("#ladder").siblings().removeClass("active");
			$("#ladder>.bottom_bor").css("display","block");
			$("#Ringer>.bottom_bor").css("display","none");
		}
		//导航显示 下面内容
		$(".god_top a").click(function () {
			var c_v = $(this).attr("c_v");
			if (c_v == "zb_ds") {
				$(this).addClass("active");
				$(this).siblings().removeClass("active");
				$(this).children().css("display", "block")
				$(this).siblings().children().css("display", "none");
				$(".Ringer").css("display", "block");
				$(".student").css("display", "none");
				$(".ladder").css("display", "none");
				gods_search();
			} else if(c_v == "qt_ds") {
//				$(".Ringer").css("display", "block");
//				$(".student").css("display", "none");
//				$(".ladder").css("display", "none");
				$.confirm({
				  title: '提示',
				  text: '其他大神正在赶来的路上，<br/>客官莫着急~',
				  onOK: function () {
				    //点击确认
				  },
				  onCancel: function () {
				  }
				});
				return;
			}
		});

		//查询大神
		function gods_search() {
			$(".Ringer_content").empty();
			$.ajax({
				type: "GET",
				url: $main_URL_yd + "/v2/product_type/coachs",
				async: true,		
				error: function (xhr, errorInfo, ex) {
					$.toast("查询大神错误！错误信息:" + errorInfo, "forbidden");
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
										<div class="gods_list" list_id=${_arrs[i].coach.id} qp_id=${_arrs[i].id} type="">
											<div class="gods_toux">
												<img src=${gods_tou}/>
											</div>
											<div class="gods_content">
												<p class="gods_content_name">${_arrs[i].coach.name}<span class="col_ffb529">￥${_arrs[i].price}元起</span></p>
												<p class="gods_content_jse">${_arrs[i].coach.introduction}</p>
											</div>
										</div>
								`)
							}

							//代练详情跳转
							$(".gods_list").click(function () {
								var gods_id = $(this).attr("list_id");
								var qp_id = $(this).attr("qp_id");
								window.location.href = "gods_xq.html?id=" + gods_id+"&qp_id="+qp_id;
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