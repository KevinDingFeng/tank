$(document).ready(function() {
	init();
	function init(){
		//点击头部切换
		$(".tariff_ul>li").click(function(){
			var _type = $(this).attr("type");
			if(_type == "1"){
				$(this).find("img").attr("src","images/kj/kj_stu_x.png");
				var _arr = $(this).siblings().find("img");
				_arr[0].src = "images/kj/kj_dl_w.png";
				_arr[1].src = "images/kj/kj_sg_w.png";
				_arr[2].src = "images/kj/kj_pw_w.png";
				
				$(this).find("div").addClass("kj_active");
				$(this).siblings().children("div").removeClass("kj_active");
			}else if(_type == "2"){
				$(this).find("img").attr("src","images/kj/kj_dl_x.png");
				var _arr = $(this).siblings().find("img");
				
				_arr[0].src = "images/kj/kj_stu_w.png";
				_arr[1].src = "images/kj/kj_sg_w.png";
				_arr[2].src = "images/kj/kj_pw_w.png";
				
				$(this).find("div").addClass("kj_active");
				$(this).siblings().children("div").removeClass("kj_active");	
			}else if(_type == "3"){
				$(this).find("img").attr("src","images/kj/kj_sg_x.png");
				var _arr = $(this).siblings().find("img");
				
				_arr[0].src = "images/kj/kj_stu_w.png";
				_arr[1].src = "images/kj/kj_dl_w.png";
				_arr[2].src = "images/kj/kj_pw_w.png";
				
				$(this).find("div").addClass("kj_active");
				$(this).siblings().children("div").removeClass("kj_active");
			}else if(_type == "4"){
				$(this).find("img").attr("src","images/kj/kj_pw_x.png");
				var _arr = $(this).siblings().find("img");
				_arr[0].src = "images/kj/kj_stu_w.png";
				_arr[1].src = "images/kj/kj_dl_w.png";
				_arr[2].src = "images/kj/kj_sg_w.png";
				
				$(this).find("div").addClass("kj_active");
				$(this).siblings().children("div").removeClass("kj_active");
			}
		})
		//点击立即支付
		$(".kj_zf").click(function(){
			window.location.href="../djyd/kj_order.html";
		})
	}
})