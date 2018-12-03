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
	//类型
	var _type = getQueryString("code");//10教学11代练12陪玩
	init();
	function init(){
		var _height = $(document).height();
		$("#xq_cumtor").css("height",_height);
		//轮播
		var mySwiper = new Swiper('.swiper-container', {
			autoplay: 100000, //可选选项，自动滑动
			pagination: ".swiper-pagination",
			paginationClickable:true,//--实现小圆点点击
		})
		//点击选择
		$("#choose").click(function(){
			$("#xq_cumtor").css("display","block");
			$(".t_pop").css("display","block");
			$('body').css({
		        'overflow':'hidden',
		        'position': 'fixed',
		        'top': "-1"
		    });
		})
		//关闭弹窗
		$("#btn_close").click(function(){
			$("#xq_cumtor").css("display","none");
			$(".t_pop").css("display","none");
			$('body').css({
		        'overflow':'auto',
		        'position': 'static',
		        'top': 'auto'
		    });
		})
		
		if(_type == "10"){
			document.title = '教学'; 
		}else if(_type == "11"){
			document.title = '代练'; 
		}else if(_type == "13"){
			document.title = '陪玩'; 
		}
	}
	//去预支付页面
	$("#go_money").click(function(){
		window.location.href="../djyd/order_Prepayment.html"
	})
})
	





















