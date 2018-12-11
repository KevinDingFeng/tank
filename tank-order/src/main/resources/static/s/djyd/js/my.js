$(document).ready(function() {
	$("#bottom_nav").load("../djyd/footer.html");
	$.ajax({
		type: "GET",
		url: $main_URL_yd+"/wx_user/info",
		dataType: "json",
		async: true,
		error: function(xhr, errorInfo, ex) {
			$.toast("查询用户信息错误！错误信息:" + errorInfo, "forbidden");
		},
		success: function(resp) { //请求完成
			var _user_xx = resp.wxUser;
			if(_user_xx == '' || _user_xx == null || _user_xx == undefined){
				$.toast("查询用户信息失败", "forbidden");
				/*footer*/
				var _footer = window.location.href;
				_footer = _footer.split("/djyd/");
				if(_footer[1] == "my.html"){
					$(".nav_center>img").attr("src","images/new/homes_h.png");
					$(".nav_right>img").attr("src","images/new/my_x.png")
				}
			}else{
				//用户头像
				$(".my_tou").attr("src",_user_xx.headImgUrl);
				//用户姓名
				$(".my_name>p").html(_user_xx.nickName);
			}
		}
	})
	
})