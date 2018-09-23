;(function($){
	var stopWindowDefault = {
		windowdefaultEvent: function(windowflag){
			window.ontouchmove = function(e){
				e.preventDefault && e.preventDefault();
				e.stopPropagation && e.stopPropagation();
				if(windowflag){
		            e.returnValue=true;
		            return true;	
				}
				else{
			        e.returnValue=false;
			        return false;
				}
			}
		}
	};
	$.fn.extend({
		scrollEve:function(options){
			document.onselectstart=function (){return false;};
			var defaults = {
				selectors:{
					scrollitem:".scollItem",
					sure:".sureBtn",
					cancle:".cancleBtn",
					active:".active"
				},
				index:2,
				setData:""
			};
			var ops = $.extend({},defaults,options);	
			var obj = $(this);
			var active = ops.selectors.active.split(".")[1];
			var sy,my,ey,st,ix,sh,ah,len,objh,olen;
			var flag = false;
			var touchEvents = {
			    touchstart: "touchstart",
			    touchmove: "touchmove",
			    touchend: "touchend",
			    initTouchEvents: function () {
					var browser={
						versions:function(){
							var u = navigator.userAgent, app = navigator.appVersion;
							return {//移动终端浏览器版本信息
								trident: u.indexOf('Trident') > -1, //IE内核
								presto: u.indexOf('Presto') > -1, //opera内核
								webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
								gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
								mobile: !!u.match(/AppleWebKit.*Mobile.*/)||u.indexOf('iPad') > -1, //是否为移动终端
								ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
								android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
								iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
								iPad: u.indexOf('iPad') > -1, //是否iPad
								webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
							};
						}(),
						language:(navigator.browserLanguage || navigator.language).toLowerCase()
					}
					if(!browser.versions.mobile){
			            this.touchstart = "mousedown";
			            this.touchmove = "mousemove";
			            this.touchend = "mouseup";
					}
			    },
			};
			var nflag = true;
			var scrollEvents = {
				sy:0,
				my:0,
				ey:0,
				st:0,
				ix:0,
				sh:0,
				ah:$(ops.selectors.scrollitem).eq(0).find(ops.selectors.item).eq(0).height(),
				len:0,
				objh:0,
				olen:$(ops.selectors.scrollitem).length,
				_self:"",
				scrolltart:function(e){
					flag = true;
					scrollEvents.sy = e.pageY || e.originalEvent.targetTouches[0].pageY;
					scrollEvents.st = parseInt($(this).children().css("top"));
					scrollEvents.ah = $(this).find(ops.selectors.item).eq(0).height();
					scrollEvents.len = $(this).find(ops.selectors.item).length-1;
					scrollEvents.objh = $(this).height();
					return sy,st,ah,len,objh;
				},
			    scrollmove: function(e){
					if(flag){
						e.preventDefault();
						e.stopPropagation();
						scrollEvents.my = e.pageY || e.originalEvent.targetTouches[0].pageY;
						scrollEvents.ey = scrollEvents.my-scrollEvents.sy;
						if(scrollEvents.ey+scrollEvents.st>=scrollEvents.ah*2){
							scrollEvents.ix = 0;
						}
						else{
							scrollEvents.ix = Math.floor(Math.abs((scrollEvents.ah-scrollEvents.ey-scrollEvents.st+scrollEvents.ah/2)/scrollEvents.ah));
						}
						$(this).find(ops.selectors.item).eq(scrollEvents.ix).addClass(active).siblings().removeClass(active);
						$(this).children().css("top",scrollEvents.ey+scrollEvents.st);
						return my,ey,ix;
					}
			    },
			    scrollend: function(){
					flag = false;
					var dstr = "";
					if(scrollEvents.my){
						scrollEvents.st = parseFloat($(this).children().css("top"));
						var daylen = parseInt($("body").attr("days"));
						if(scrollEvents.st>=scrollEvents.ah){
							scrollEvents.st = scrollEvents.ah;
						}
						else if(scrollEvents.st<=scrollEvents.ah-scrollEvents.len*scrollEvents.ah){
							scrollEvents.ix = scrollEvents.len;
							scrollEvents.st = scrollEvents.ah-scrollEvents.len*scrollEvents.ah;
						}
						else{
							scrollEvents.st = scrollEvents.ah-scrollEvents.ix*scrollEvents.ah;
						}
						$(this).attr("data-on",scrollEvents.ix);
						$(this).children().stop().animate({"top":scrollEvents.st},100);
						$(this).find(ops.selectors.item).eq(scrollEvents.ix).addClass(active).siblings().removeClass(active);
						var v = parseInt($(this).find(ops.selectors.item).eq(scrollEvents.ix).text());
						scrollEvents.my = null;
					}
			    }
			}
			touchEvents.initTouchEvents();
			$(ops.selectors.scrollitem).on(touchEvents.touchstart,scrollEvents.scrolltart);
			$(ops.selectors.scrollitem).on(touchEvents.touchmove,scrollEvents.scrollmove);
			$(ops.selectors.scrollitem).on(touchEvents.touchend,scrollEvents.scrollend);
			$(ops.selectors.scrollitem).on("click",ops.selectors.item,function(){

			})
			function checkNum(d){
				return d>9? d:"0"+d;
			}
			$(ops.selectors.sure).on("click",function(){
				var len  = $(ops.selectors.scrollitem).length;
				var str = $(ops.selectors.scrollitem).eq(0).find(ops.selectors.active).text()
				var i = parseInt($("body").attr("data-ix"));
				$(ops.selectors.obj).text(str);
				$(".scrollList").stop().animate({"bottom":"-100%"},300,function(){
					$(".scroll").fadeOut();
				});
			})
			$(ops.selectors.cancle).on("click",function(){
					
				$(".scrollList").stop().animate({"bottom":"-100%"},300,function(){
					$(".scroll").fadeOut();
				});
			})
			$(".scroll").on("click",function(e){
				if($(e.target).hasClass("scroll")){
					$(".scrollList").stop().animate({"bottom":"-100%"},300,function(){
						$(".scroll").fadeOut();
					});
				}
			})
			$(obj).on("click",function(i){
				$(".scroll").fadeIn();
				$(".scrollList").stop().animate({"bottom":"0"},300);
				var today = new Date();
				var y = today.getFullYear();
				var m = today.getMonth()+1;
				var d = today.getDate();
				var h = today.getHours();
				var min = today.getMinutes();
				var dstr ="";
				var dval = $(this).text();

				//console.log(dval);
				$("body").attr("data-ix",$(this).parent().index())

			})
		}
	});
})(jQuery);