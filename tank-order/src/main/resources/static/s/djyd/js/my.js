$(document).ready(function() {
	var _user_name = sessionStorage.getItem('username');
	var _user_title = sessionStorage.getItem('usertitle');
	//用户头像
	$(".my_tou").attr("src",_user_title);
	//用户姓名
	$(".my_name p").html(_user_name);		
})