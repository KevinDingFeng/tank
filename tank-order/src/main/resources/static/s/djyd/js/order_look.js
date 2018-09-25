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
			url: $main_URL_yd+"/order/detail/"+order_id,
			dataType: "json",
			async: true,
			error: function(xhr, errorInfo, ex) {
				$.toast("查询订单信息错误！错误信息:" + errorInfo, "forbidden");
			},
			success: function(resp) { //请求完成
				if(resp.code == "200"){
					var _Level1 = resp.data.typeLevel1;//第一级
					var _Level2 = resp.data.typeLevel2;//第二级
					var _Level3 = resp.data.typeLevel3;//第三级					
					var order_xx = resp.data.playOrder;//订单 信息
					if(_Level3 == "" || _Level3 == null || _Level3 == undefined){
						$(".order_cp").html(_Level1+">"+_Level2)
					}else{
						$(".order_cp").html(_Level1+">"+_Level2+">"+ _Level3)
					}
					var time_dw = order_xx.product.durationType;//时间单位单位
					if(time_dw == "Hour"){
						time_dw = "小时"
					}else if(time_dw == "Day"){
						time_dw = "天"
					}else if(time_dw == "Week"){
						time_dw = "周"
					}else if(time_dw == "Month"){
						time_dw = "月"
					} 
					$(".zh_js").html(order_xx.duration+time_dw);//服务时长
					if(time_dw == "NoLimitation"){
						$(".zh_js").html("不限");
					}
					$(".gods_qw").html(order_xx.coach.name);//期望服务大神
					if(order_xx.status == "NotPay"){
						order_xx.status = "未支付"
					}else if(order_xx.status == "Operation"){
						order_xx.status = "待服务"
					}else if(order_xx.status == "Complete"){
						order_xx.status = "已服务"
					}else if(order_xx.status == "Cancel"){
						order_xx.status = "已取消"
					}
					$(".order_type").html(order_xx.status);//订单状态
					$(".order_id").html(order_xx.no);//订单号
					$(".order_time").html(order_xx.creation);//下单时间
					var _cellphone = order_xx.cellphone;//电话号码
					$(".order_phone").html(_cellphone);
					var _wxAccount = order_xx.wxAccount;//微信号码
					if(_wxAccount == "" || _wxAccount == null || _wxAccount == undefined){
						$(".order_wx").html("暂无微信号码");
					}else{
						$(".order_wx").html(_wxAccount);
					}
					var _remark = order_xx.remark;//备注
					if(_remark == null){
						$(".order_remker").html(_remark);
					}
					$(".order_price").html(order_xx.totalFee+"元");//服务费用
					
					if(order_xx.status == "未支付"){
						$("#tj_btn").html("去支付")
					}else if(order_xx.status == "已服务"){
						$(".my_order_sure").css("display","none")
					}
					//点击 去。。。。
					$("#tj_btn").click(function(){
						if(!$("#tj_btn").html("去支付")){
							
						}else{
							$.ajax({
								type: "POST",
								url: $main_URL_yd+"/order/exe/"+order_id,
								dataType: "json",
								async: true,
								error: function(xhr, errorInfo, ex) {
									$.toast("查询订单信息错误！错误信息:" + errorInfo, "forbidden");
								},
								success: function(resp) { //请求完成
									if(resp.code == "200"){
										$.toast("订单状态修改成功！");
										init();
									}
								}
							});
						}
					})
					
				}
					/*var _dx = resp.data;
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
				}				*/
			}
		});
	}
})