$(document).ready(function() {
	init()
	function init(){
		get_pt();
		$(".my_order_list>li").click(function(){
			$(this).addClass("my_order_list_active");
			$(this).siblings().removeClass("my_order_list_active");
			var _type = $(this).attr("_type");
			if(_type == "pt"){
				get_pt();
			}else if(_type == "kj"){
				get_kj();
			}
		})
		//获取快捷单
		function get_kj(){
			$.ajax({
				type: "GET",
				url: $main_URL_yd+"/order/rapid_list",
				dataType: "json",
				async: true,
				error: function(xhr, errorInfo, ex) {
					$.toast("查询快捷单信息错误！错误信息:" + errorInfo, "forbidden");
				},
				success: function(resp) { //请求完成
					if(resp.code == "200"){
						
						var list_xx = resp.data.page.content;
						if(list_xx.length<=0){
							$(".order_list").empty();
							$(".order_list").append(`
									<div class="no_list">暂无订单</div>
							`);
						}else{
							var list_order = "";//order列表
							for(var i=0; i< list_xx.length;i++){
								var _order_xx = list_xx[i].product.productType;//订单信息
								if(_order_xx.name == "手工代练"){
									_order_xx.name = "代练"
								}
								if(list_xx[i].status == "NotPay"){
									list_xx[i].status = "未支付"
								}else if(list_xx[i].status == "Operation"){
									list_xx[i].status = "待服务"
								}else if(list_xx[i].status == "Complete"){
									list_xx[i].status = "已服务"
								}else if(list_xx[i].status == "Cancel"){
									list_xx[i].status = "已取消"
								}
								list_order+=`
									<div class="orderbg_list" order_id=${list_xx[i].id}>
										<p class="fw_order">${_order_xx.name}</p>
										<p class="price_order"><span class="r_col f_weight">￥${list_xx[i].totalFee}元</span></p>
										<p class="zt_order">${list_xx[i].status}</p>
									</div>
								`;
							}
							$(".order_list").empty();
							$(".order_list").append(list_order);
							if($(".zt_order").html("未支付")){
								$(".zt_order").addClass("col_red")
							}else if($(".zt_order").html("待服务")){
								$(".zt_order").addClass("y_col")
							}else if($(".zt_order").html("已服务")){
								$(".zt_order").addClass("b_col")
							}
							//点击查询详情
							$(".orderbg_list").click(function(){
								var _id = $(this).attr("order_id");
								window.location.href = "order_look.html?id="+_id;
							})
						}
					}
				}
			})
		}
		//获取普通单
		function get_pt(){
			$.ajax({
				type: "GET",
				url: $main_URL_yd+"/order/list",
				dataType: "json",
				async: true,
				error: function(xhr, errorInfo, ex) {
					$.toast("查询订单信息错误！错误信息:" + errorInfo, "forbidden");
				},
				success: function(resp) { //请求完成
					if(resp.code == "200"){
						var list_xx = resp.data.page.content;
						if(list_xx.length<=0){
							$(".order_list").empty();
							$(".order_list").append(`
									<div class="no_list">暂无订单</div>
							`);
						}else{
							
							var list_order = "";//order列表
							for(var i=0; i< list_xx.length;i++){
								var _id ="t"+list_xx[i].product.productTypeId;
								var _t = resp.data.types[_id];
								if(list_xx[i].status == "NotPay"){
									list_xx[i].status = "未支付"
								}else if(list_xx[i].status == "Operation"){
									list_xx[i].status = "待服务"
								}else if(list_xx[i].status == "Complete"){
									list_xx[i].status = "已服务"
								}else if(list_xx[i].status == "Cancel"){
									list_xx[i].status = "已取消"
								}
								list_order+=`
									<div class="orderbg_list" order_id=${list_xx[i].id}>
										<p class="fw_order">${_t.level1Name}</p>
										<p class="type_order">${_t.level2Name}</p>
										<p class="price_order"><span class="r_col f_weight">￥${list_xx[i].totalFee}元</span></p>
										<p class="zt_order">${list_xx[i].status}</p>
									</div>
								`;
							}
							$(".order_list").empty();
							$(".order_list").append(list_order);
							if($(".zt_order").html("未支付")){
								$(".zt_order").addClass("col_red")
							}else if($(".zt_order").html("待服务")){
								$(".zt_order").addClass("y_col")
							}else if($(".zt_order").html("已服务")){
								$(".zt_order").addClass("b_col")
							}
							//点击查询详情
							$(".orderbg_list").click(function(){
								var _id = $(this).attr("order_id");
								window.location.href = "order_look.html?id="+_id;
							})
						}
					}else if(resp.code == "400"){
						$.toast("查询订单信息失败！失败信息:" + resp.message, "forbidden");
					}				
				}
			});
		}
	}
})