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
	var dd_xx = getQueryString("orderId");
	var dd_type = getQueryString("type");
	init()
	function init(){
		//服务产品
		if(dd_type == "TEACH"){
			/*$(".cp_xx_content").html("教学>"+ _xx.serviceType2 +">"+_xx.serviceType3);
			$(".dl_time").css("dispaly","none");
			$(".yx_zh").html("角色名称");
			//角色名称
			$(".game_hao").html(_xx.roleName);
			//其他需求
			if(_xx.remark.length == 0){
				$(".game_xq").html("暂无需求！");
			}else{
				$(".game_xq").html(_xx.remark);
			}*/
			//价格
			/*$(".zong_jx_price").html(_xx.money);*/
			//提交订单
				$.ajax({
					type: "GET",
					url: $main_URL_yd+"/play_order/detail/"+dd_xx,
					dataType: "json",
					async: true,
					error: function(xhr, errorInfo, ex) {
						$.toast("查询订单信息错误！错误信息:" + errorInfo, "forbidden");
					},
					success: function(resp) { //请求完成
						if(resp.code == "200"){
							var _dx = resp.data;
							$(".dd_hao").html(_dx.orderNo);
							$(".zh_phone").html(_dx.cellphoneNo);
							//服务教练
							$(".jl_xx").css("display","block");
							$(".jl_name").html(_dx.wishInstructor.nickName);
							$(".cp_xx_content").html(_dx.sysService.productName + ">"+_dx.sysService.supClassName + ">"+ _dx.sysService.subClassName);
							$("#yx_xx").html("角色信息");
							$(".game_hao").html(_dx.roleName);
							$(".dl_time").css("display","none");
							$(".game_xq").html(_dx.remark);
							$(".zong_jx_price").html(_dx.totalFee);
						}else if(resp.code == "400"){
							$.toast("查询订单信息失败！失败信息:" + resp.message, "forbidden");
						}				
					}
				});
		}else if(dd_type == "PRACTICE"){
			$.ajax({
				type: "GET",
				url: $main_URL_yd+"/play_order/detail/"+dd_xx,
				dataType: "json",
				async: true,
				error: function(xhr, errorInfo, ex) {
					$.toast("查询订单信息错误！错误信息:" + errorInfo, "forbidden");
				},
				success: function(resp) { //请求完成
					if(resp.code == "200"){
						var _dx = resp.data;
						
						$(".dd_hao").html(_dx.orderNo);
						$(".zh_phone").html(_dx.cellphoneNo)
						$(".cp_xx_content").html(_dx.sysService.productName + ">"+_dx.sysService.supClassName + ">"+ _dx.sysService.subClassName);
						$(".game_hao").html(_dx.roleName);
						$(".dl_time").css("display","none");
						$(".game_hao").html(_dx.account);//游戏账号
						$(".game_xq").html(_dx.remark);
						if(_dx.totalFee == "0"){
							$(".zong_jx_price").html("详情咨询客服");
							$(".sure_content_title .mar_b").css("display","none");
						}else{
							$(".zong_jx_price").html(_dx.totalFee);
						}
					}else if(resp.code == "400"){
						$.toast("查询订单信息失败！失败信息:" + resp.message, "forbidden");
					}				
				}
			});
		}else{
			$.ajax({
				type: "GET",
				url: $main_URL_yd+"/play_order/detail/"+dd_xx,
				dataType: "json",
				async: true,
				error: function(xhr, errorInfo, ex) {
					$.toast("查询订单信息错误！错误信息:" + errorInfo, "forbidden");
				},
				success: function(resp) { //请求完成
					if(resp.code == "200"){
						var _dx = resp.data;
						$(".dd_hao").html(_dx.orderNo)
						$(".zh_phone").html(_dx.cellphoneNo);
						if(_dx.sysService.subClassName == ""){
							$(".cp_xx_content").html(_dx.sysService.productName + ">"+_dx.sysService.supClassName);
						}else{
							$(".cp_xx_content").html(_dx.sysService.productName + ">"+_dx.sysService.supClassName + ">"+ _dx.sysService.subClassName);
						}
						
						$(".game_hao").html(_dx.roleName);
						$(".dl_time").css("display","none");
						$(".game_hao").html(_dx.account);//游戏账号
						$(".game_xq").html(_dx.remark);
						$(".zong_jx_price").html(_dx.totalFee);
					}else if(resp.code == "400"){
						$.toast("查询订单信息失败！失败信息:" + resp.message, "forbidden");
					}				
				}
			});
		}
		
		
	}
})