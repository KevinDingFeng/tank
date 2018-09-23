$(document).ready(function() {
	var _user_name = sessionStorage.getItem('username');
	var _user_title = sessionStorage.getItem('usertitle');
	//用户头像
	$(".my_tou").attr("src",_user_title);
	//用户姓名
	$(".my_name p").html(_user_name);
	//手机号
	$("#hu_yzm").click(function(){
		var _cell_phone = $("#cell_phone").val();
		var myreg=/^[1][3,4,5,7,8][0-9]{9}$/;
		if (!myreg.test(_cell_phone)) {
			$.toast("手机号格式不正确", "forbidden");
            return;
        }
		//验证手机号
		$.ajax({
			type: "GET",
			url: $main_URL_yd+"/auth/check_cellphone/"+_cell_phone,
			dataType: "json",
			async: true,
			error: function(xhr, errorInfo, ex) {
				$.toast("手机号验证错误！错误信息:" + errorInfo, "forbidden");
			},
			success: function(resp) { //请求完成
				if(resp.code == "200"){
					if(resp.data == false){
						$.ajax({
							type: "GET",
							url: $main_URL_yd+"/sms/"+_cell_phone,
							dataType: "json",
							async: true,
							error: function(xhr, errorInfo, ex) {
								$.toast("发送短信息错误！错误信息:" + errorInfo, "forbidden");
							},
							success: function(resp) { //请求完成
								if(resp.code == "200"){
									$.toast("手机号验证码已发送，请查询手机!");
								}else if(resp.code == "400"){
									$.toast("发送短信息失败！失败信息:" + resp.message, "forbidden");
								}	
							}
						});
						//点击绑定
						$("#bind_phone").click(function(){
							var _cell_phone = $("#cell_phone").val();
							var myreg=/^[1][3,4,5,7,8][0-9]{9}$/;
							if (!myreg.test(_cell_phone)) {
								$.toast("手机号格式不正确", "forbidden");
					            return;
					        }
							var _code = $("#cell_code").val();
							if(_code == "" || _code == null || _code == undefined){
								$.toast("验证码不能为空", "forbidden");
								return;
							}
							$.ajax({
								type: "POST",
								url: $main_URL_yd+"/auth/binding_cellphone_no/"+_cell_phone,
								data:{
									code: _code
								},
								dataType: "json",
								async: true,
								error: function(xhr, errorInfo, ex) {
									$.toast("手机绑定错误！错误信息:" + errorInfo, "forbidden");
								},
								success: function(resp) { //请求完成
									if(resp.code == "200"){
										$.toast("手机绑定成功！");
										window.location.href="ff.html";
									}else if(resp.code == "400"){
										$.toast("手机绑定失败！失败信息:" + resp.message, "forbidden");
									}	
								}
							});
						})
					}else{
						$.toast("此手机号不可重复绑定！" , "forbidden");
					}
				}else{
					$.toast("发送短信失败"+resp.message,"forbidden");
				}
			}
		})		
	})
})