var $main_URL_yd = "http://localhost:7002"; //移动连接服务的主地址
//var $main_URL_yd = "http://tank.dazonghetong.com"; //移动连接服务的主地址
//var $main_URL_yd = "http://tk.dazonghetong.com"; //移动连接服务的主地址
(function(win, doc) {
	if(!win.addEventListener) return;
	var html = document.documentElement;

	function setFont() {
		var cliWidth = html.clientWidth;
		html.style.fontSize = 100 * (cliWidth / 750) + 'px';
	}
	win.addEventListener('resize', setFont, false);
	setFont();
})(window, document);
$(document).ready(function() {
	(function () {
        if (typeof WeixinJSBridge == "object" && typeof WeixinJSBridge.invoke == "function") {
            handleFontSize();
        } else {
            if (document.addEventListener) {
                document.addEventListener("WeixinJSBridgeReady", handleFontSize, false);
            } else if (document.attachEvent) {
                document.attachEvent("WeixinJSBridgeReady", handleFontSize);
                document.attachEvent("onWeixinJSBridgeReady", handleFontSize);
            }
        }
        function handleFontSize() {
            WeixinJSBridge.invoke('setFontSizeCallback', { 'fontSize': 0 });
            WeixinJSBridge.on('menu:setfont', function () {
                WeixinJSBridge.invoke('setFontSizeCallback', { 'fontSize': 0 });
            });
        }
    })();
	//点击教学
	$(".type_jx").click(function(){
		$.confirm({
		  title: '敬请期待',
		  text: '当前模块正在努力建设中...<br/>建设过程中，<br/>请通过"快捷下单"入口进入下单，多谢您的理解~',
		  
		  onOK: function () {
		    //点击确认
			window.location.href = "../djyd/kj_tariff.html";
			  
		  },
		  onCancel: function () {
			  
		  }
		});
		$(".weui-dialog__ft>a:nth-child(1)").html("我知道了")
		$(".weui-dialog__ft>a:nth-child(2)").html("快捷下单")
	})
	//点击代练
	$(".type_dl").click(function(){
		$.confirm({
		  title: '敬请期待',
		  text: '当前模块正在努力建设中...<br/>建设过程中，<br/>请通过"快捷下单"入口进入下单，多谢您的理解~',
		  
		  onOK: function () {
		    //点击确认
			window.location.href = "../djyd/kj_tariff.html";
			  
		  },
		  onCancel: function () {
			  
		  }
		});
		$(".weui-dialog__ft>a:nth-child(1)").html("我知道了")
		$(".weui-dialog__ft>a:nth-child(2)").html("快捷下单")
	})
	//点击陪玩
	$(".type_pw").click(function(){
		$.confirm({
		  title: '敬请期待',
		  text: '当前模块正在努力建设中...<br/>建设过程中，<br/>请通过"快捷下单"入口进入下单，多谢您的理解~',
		  
		  onOK: function () {
		    //点击确认
			window.location.href = "../djyd/kj_tariff.html";
			  
		  },
		  onCancel: function () {
			  
		  }
		});
		$(".weui-dialog__ft>a:nth-child(1)").html("我知道了")
		$(".weui-dialog__ft>a:nth-child(2)").html("快捷下单")
	})
	/*$(".nav_left").click(function(){
		$.confirm({
		  title: '敬请期待',
		  text: '当前模块正在努力建设中...<br/>建设过程中，<br/>请通过"快捷下单"入口进入下单，多谢您的理解~',
		  
		  onOK: function () {
		    //点击确认
			window.location.href = "../djyd/kj_order.html";
			  
		  },
		  onCancel: function () {
			  
		  }
		});
		$(".weui-dialog__ft>a:nth-child(2)").html("快捷下单")
	})*/
	//点击qq客服
	$(".qq_kf").click(function(){
		$("#cumtor").show();
		$("#cumtor_img").show();
		$("#cumtor_img").empty();
		$("#cumtor_img").append(`
			<img src="images/qqcode.jpg"/>
		`);
	})
	//点击微信客服
	$(".two_width").click(function(){
		$("#cumtor").show();
		$("#cumtor_img").show();
		$("#cumtor_img").empty();
		$("#cumtor_img").append(`
			<img src="images/wxcode.jpg"/>
		`);
	})
	//点击yy客服
	//点击隐藏
	$("#cumtor").click(function(){
		$("#cumtor").hide();
		$("#cumtor_img").hide();
	})
})
