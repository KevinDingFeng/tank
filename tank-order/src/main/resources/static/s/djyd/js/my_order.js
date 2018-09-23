$(document).ready(function() {
	$.ajax({
		type: "GET",
		url: $main_URL_yd+"/play_order/my_orders",
		dataType: "json",
		async: true,
		error: function(xhr, errorInfo, ex) {
			$.toast("查询订单信息错误！错误信息:" + errorInfo, "forbidden");
		},
		success: function(resp) { //请求完成
			if(resp.code == "200"){
				var list_xx = resp.data;
				var list_order ="";//order列表
				for (var i = 0; i < list_xx.length; i++) {
					if(list_xx[i].status == "BEFORE"){
						list_xx[i].status = "待服务"
					}else{
						list_xx[i].status = "已完成"
					}
					if(list_xx[i].totalFee == null || list_xx[i].totalFee == "" || list_xx[i].totalFee == undefined){
						list_xx[i].totalFee ="详情咨询客服"
					}else if(list_xx[i].totalFee == "0"){
						list_xx[i].totalFee ="详情咨询客服"
					}else{
						list_xx[i].totalFee = "￥"+list_xx[i].totalFee+"元"
					}
					list_order+=`
						<div class="orderbg_list" order_id=${list_xx[i].id}>
							<p class="fw_order">${list_xx[i].sysService.productName}</p>
							<p class="type_order">${list_xx[i].sysService.supClassName}</p>
							<p class="price_order"><span class="r_col f_weight">${list_xx[i].totalFee}</span></p>
							<p class="y_col zt_order">${list_xx[i].status}</p>
						</div>
					`;				
				}
				$(".order_list").append(list_order);
				//点击查询详情
				$(".orderbg_list").click(function(){
					var _id = $(this).attr("order_id");
					window.location.href = "order_look.html?id="+_id;
				})
			}else if(resp.code == "400"){
				$.toast("查询订单信息失败！失败信息:" + resp.message, "forbidden");
			}				
		}
	});
})