$(document).ready(function() {
	var _user_name = sessionStorage.getItem('username');
	var _user_title = sessionStorage.getItem('usertitle');
	//用户头像
	$(".my_tou").attr("src",_user_title);
	//用户姓名
	$(".user_name").html(_user_name);
	
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
				
			}else{
				//用户头像
				$(".my_tou").attr("src",_user_xx.headImgUrl);
				//用户姓名
				$(".user_name").html(_user_xx.nickName);
			}
				/*if(_user_xx.cellphoneVerified == true){//绑定手机号
					$("#cellphone").html(_user_xx.cellphone);
					$("#btn_cellphone").click(function(){
						window.location.href="edit_password.html";
					})
				}else {//未绑定手机号
					$("#cellphone").html("未绑定");
					$("#btn_cellphone").html("绑定手机号");
					$("#btn_cellphone").click(function(){
						window.location.href="edit_password.html"
					})
				}
				if(_user_xx.account == "" || _user_xx.account == null || _user_xx.account == undefined){//未绑用户
					$(".zh_name").html("未绑定");
					$("#zh_xx").html("绑定登录账号");
					$(".pass_word").html("未绑定");
					$("#word_xx").css("display","none");
					$("#zh_xx").click(function(){
						$.prompt({
							title: '绑定用户名',
							text: '此用户名为pc端用户名',
							input: '请输入用户名',
							empty: false, // 是否允许为空
							onOK: function (input) {
								//点击确认
							},
							onCancel: function () {
								//点击取消
							}
						});
					})
				}else{//绑定用户
					$(".zh_name").html(_user_xx.account);
					$(".pass_word").html("******");
					$("#word_xx").css("display","block");
					$("#zh_xx").click(function(){
						$.confirm({
						  title: '解绑登录账号',
						  text: "确认解绑后，将只能通过微信授权登录，无法通过输入账号和密码的方式登录.",
						  onOK: function () {
						    //点击确认
						  },
						  onCancel: function () {
						  }
						});
					})
					
				}
			}else if(resp.code == 400){
				$.toast("查询用户信息失败！错误信息:" + resp.message, "forbidden");
			}*/
		}
	});
})