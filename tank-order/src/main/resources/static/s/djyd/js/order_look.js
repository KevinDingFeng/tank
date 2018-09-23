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
	//订单id
	var order_id = getQueryString("id");
	init();
	function init(){
		//查询订单 详情
		$.ajax({
			type: "GET",
			url: $main_URL_yd+"/play_order/detail/"+order_id,
			dataType: "json",
			async: true,
			error: function(xhr, errorInfo, ex) {
				$.toast("查询订单信息错误！错误信息:" + errorInfo, "forbidden");
			},
			success: function(resp) { //请求完成
				if(resp.code == "200"){
					var _dx = resp.data;
					if(_dx.status == "BEFORE"){
						_dx.status = "待服务"
					}else{
						_dx.status = "已服务";
						$(".my_order_sure").css("display","none");
					}
					var _time = _dx.creation;
					_time = _time.split("T");
					var c_time = _time[1].split(".");
					$(".order_time").html(_time[0]+" "+c_time[0]);
					$(".order_id").html(_dx.orderNo);
					$(".order_phone").html(_dx.cellphoneNo);
					$(".order_type").html(_dx.status);
					$(".order_cp").html(_dx.sysService.productName + ">" + _dx.sysService.supClassName + ">" + _dx.sysService.subClassName);
					
					if(_dx.sysService.productCode == "1"){
						$(".order_zh_xx").html("账号信息：")
						$(".zh_js").html(_dx.roleName);
					}else{
						$(".zh_js").html(_dx.account);
					}
					
					$(".order_remker").html(_dx.remark);
					if(_dx.totalFee == null || _dx.totalFee == "" || _dx.totalFee ==undefined){
						$(".order_price").html("详情咨询客服");
					}else if(_dx.totalFee == "0"){
						$(".order_price").html("详情咨询客服");
					}else{
						$(".order_price").html(_dx.totalFee+"元");
					}
					//确认完成
					$(".my_order_sure").click(function(){
						$.ajax({
							type: "GET",
							url: $main_URL_yd+"/play_order/player_complete/"+_dx.id,
							dataType: "json",
							async: true,
							error: function(xhr, errorInfo, ex) {
								$.toast("确认订单错误！错误信息:" + errorInfo, "forbidden");
							},
							success: function(resp) { //请求完成
								if(resp.code == "200"){
									var _sure = resp.data;
									if(_sure == true){
										$.toast("确认成功！");
										$(".my_order_sure").css("display","none");
										window.location.href="/s/djyd/my_order.html";
									}
								}else if(resp.code == "400"){
									$.toast("确认订单失败！失败信息:" + resp.message, "forbidden");
								}				
							}
						});
					})
				}else if(resp.code == "400"){
					$.toast("查询订单信息失败！失败信息:" + resp.message, "forbidden");
				}				
			}
		});
	}
})