<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>manage platform - 管理平台</title>
  <meta name="description" content="manage platform - 管理平台">
  <meta name="keywords" content="form">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <link rel="icon" type="image/png" href="/assets/i/favicon.png">
  <link rel="apple-touch-icon-precomposed" href="/assets/i/app-icon72x72@2x.png">
  <meta name="apple-mobile-web-app-title" content="Amaze UI" />
  <link rel="stylesheet" href="/assets/css/amazeui.min.css"/>
  <link rel="stylesheet" href="/assets/css/admin.css">
  <script src="/assets/js/jquery-2.1.4.min.js"></script>
</head>
<body>
<!--[if lte IE 9]>
<p class="browsehappy">你正在使用<strong>过时</strong>的浏览器，Amaze UI 暂不支持。 请 <a href="http://browsehappy.com/" target="_blank">升级浏览器</a>
  以获得更好的体验！</p>
<![endif]-->

<header class="am-topbar am-topbar-inverse admin-header">
  <div class="am-topbar-brand">
    <strong>Tank World</strong> <small>管理平台</small>
  </div>

  <button class="am-topbar-btn am-topbar-toggle am-btn am-btn-sm am-btn-success am-show-sm-only" data-am-collapse="{target: '#topbar-collapse'}"><span class="am-sr-only">导航切换</span> <span class="am-icon-bars"></span></button>

  <div class="am-collapse am-topbar-collapse" id="topbar-collapse">

    <ul class="am-nav am-nav-pills am-topbar-nav am-topbar-right admin-header-list">
      <#--
      <li><a href="javascript:;"><span class="am-icon-envelope-o"></span> 收件箱 <span class="am-badge am-badge-warning">5</span></a></li>
      <li class="am-hide-sm-only"><a href="javascript:;" id="admin-fullscreen"><span class="am-icon-arrows-alt"></span> <span class="admin-fullText">开启全屏</span></a></li>
        <ul class="am-dropdown-content">
          <li><a href="#"><span class="am-icon-user"></span> 资料</a></li>
          <li><a href="#"><span class="am-icon-cog"></span> 设置</a></li>
          <li><a href="#"><span class="am-icon-power-off"></span> 退出</a></li>
        </ul>
      -->
      <li class="am-dropdown" data-am-dropdown>
        <a class="am-dropdown-toggle" data-am-dropdown-toggle href="javascript:;">
          <span class="am-icon-users"></span> ${(loginUser.name)!"管理员"} <#-- span class="am-icon-caret-down" --></span>
        </a>
      </li>
      <li class="am-dropdown" data-am-dropdown onclick="javascript:window.location.href='/logout'">
        <a class="am-dropdown-toggle" data-am-dropdown-toggle href="/logout">
          <span class="am-icon-power-off"></span> 退出<#-- span class="am-icon-caret-down" --></span>
        </a>
      </li>
    </ul>
  </div>
</header>

<div class="am-cf admin-main">
  <!-- sidebar start -->
  <div class="admin-sidebar am-offcanvas" id="admin-offcanvas">
    <div class="am-offcanvas-bar admin-offcanvas-bar">
      <ul class="am-list admin-sidebar-list">
        <li><a href="admin-index.html"><span class="am-icon-home"></span> 首页</a></li>
        <@shiro.hasPermissionOne name=["order:assign","order:receive"]>
        <li class="admin-parent">
          <a class="am-cf" data-am-collapse="{target: '#collapse-nav'}"><span class="am-icon-file"></span> 订单管理  <span class="am-icon-angle-right am-fr am-margin-right"></span></a>
          <ul class="am-list am-collapse admin-sidebar-sub am-in" id="collapse-nav">
          	<@shiro.hasPermission name="order:assign">
        	<li><a href="/play_order/assign/list"><span class="am-icon-table"></span> 普通订单</a></li>
        	<li><a href="/play_order/assign/rapid_list"><span class="am-icon-table"></span> 快捷订单</a></li>
        	</@shiro.hasPermission>
        	<@shiro.hasPermission name="order:receive">
        	<li><a href="/play_order/receive/list"><span class="am-icon-table"></span> 接单</a></li>
        	</@shiro.hasPermission>
          </ul>
        </li>
        </@shiro.hasPermissionOne>
      </ul>
    </div>
  </div>
  <!-- sidebar end -->

<!-- content start -->
<div class="admin-content">
  <div class="admin-content-body">
    <div class="am-cf am-padding am-padding-bottom-0">
      <div class="am-fl am-cf">
        <strong class="am-text-primary am-text-lg"><#if entity.orderType == 'Quick'>快捷派单<#else>普通派单</#if></strong> / <small>Order Assign</small>
      </div>
    </div>

    <hr>
    <div class="am-tabs am-margin" data-am-tabs>
      <ul class="am-tabs-nav am-nav am-nav-tabs">
        <li class="am-active"><a href="#tab1">订单信息</a></li>
      </ul>
	  <form method="post" class="am-form" action="/play_order/assign/form" id="assignCoachForm">
      <input type="hidden" name="id" value="${entity.id}" />
      <div class="am-tabs-bd">
        <div class="am-tab-panel am-fade am-in am-active" id="tab1">
            <div class="am-g am-margin-top">
              <div class="am-u-sm-4 am-u-md-2 am-text-right">编号</div>
              <div class="am-u-sm-8 am-u-md-4">${entity.no}</div>
              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
			<div class="am-g am-margin-top">
              <div class="am-u-sm-4 am-u-md-2 am-text-right">下单用户</div>
              <div class="am-u-sm-8 am-u-md-4">${(entity.wxUser.nickName)!"未命名"}</div>
              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
        	<div class="am-g am-margin-top">
              <div class="am-u-sm-4 am-u-md-2 am-text-right">期望大神</div>
              <div class="am-u-sm-8 am-u-md-4">${entity.coach.name}</div>
              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
        	<div class="am-g am-margin-top">
              <div class="am-u-sm-4 am-u-md-2 am-text-right">服务类型</div>
              <div class="am-u-sm-8 am-u-md-4">${entity.product.productType.name}</div>
              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            <#if entity.orderType == 'Quick'>
           	<div class="am-g am-margin-top">
            	<input type="hidden" id="code" name="code" />
           		<div class="am-u-sm-4 am-u-md-2 am-text-right">选择服务:</div>
           		<div class="am-u-sm-8 am-u-md-4">
            	<select id ="level1" >
            		<option> --- 请选择一级服务 --- </option>
            		<#list productTypes as p>
            			<option value="${p.level1Code}">${p.level1Name}</option>
            		</#list>
            	</select>
            	<select id ="level2" >
            		<option> --- 请选择二级服务 --- </option>
            	</select>
            	<select id ="level3" >
            		<option> --- 请选择三级服务 --- </option>
            	</select>
            	<select id ="level4" >
            		<option> --- 请选择四级服务 --- </option>
            	</select>
            	</div>
            	<div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            <div class="am-g am-margin-top">
              <div class="am-u-sm-4 am-u-md-2 am-text-right">服务补充</div>
              <div class="am-u-sm-8 am-u-md-4"><textarea rows="10" cols="40" style="resize:none" readonly>${entity.remark!}</textarea></div>
              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            <#else>
            	<input type="hidden" id="code" name ="code" value="${code?c}">
            </#if>
        	<div class="am-g am-margin-top">
              <div class="am-u-sm-4 am-u-md-2 am-text-right">金额</div>
              <div class="am-u-sm-8 am-u-md-4">${entity.totalFee} 元</div>
              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
        	<div class="am-g am-margin-top">
              <div class="am-u-sm-4 am-u-md-2 am-text-right">联系方式</div>
              <div class="am-u-sm-8 am-u-md-4">${entity.cellphone!}</div>
              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
        	<div class="am-g am-margin-top">
              <div class="am-u-sm-4 am-u-md-2 am-text-right">微信账号</div>
              <div class="am-u-sm-8 am-u-md-4">${entity.wxAccount!}</div>
              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            <div class="am-g am-margin-top">
              <div class="am-u-sm-4 am-u-md-2 am-text-right">QQ账号</div>
              <div class="am-u-sm-8 am-u-md-4">${entity.qqAccount!"-"}</div>
              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            <div class="am-g am-margin-top">
              <div class="am-u-sm-4 am-u-md-2 am-text-right">YY账号</div>
              <div class="am-u-sm-8 am-u-md-4">${entity.yyAccount!"-"}</div>
              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
        	<div class="am-g am-margin-top">
            <div class="am-u-sm-4 am-u-md-2 am-text-right">执行大神</div>
            <div class="am-u-sm-8 am-u-md-10">
              <div class="am-btn-group" data-am-button>
              <p>推荐大神</p>
              <#if coaches??>
              	<#list coaches as em>
	                <label class="am-btn am-btn-default am-btn-xs <#if entity.executorId == em.id>am-active</#if>">
	                  <input type="radio" name="coach" value="${em.id}" <#if entity.executorId == em.id>checked="checked"</#if> id="coach${em.id}"> ${em.name}
	                </label>
              	</#list>
              </#if>
              <br />
              <br />
              <p>备选大神</p>
              <#if spareCoach??>
              	<#list spareCoach as em>
	                <label class="am-btn am-btn-default am-btn-xs <#if entity.executorId == em.id>am-active</#if>">
	                  <input type="radio" name="coach" value="${em.id}" <#if entity.executorId == em.id>checked="checked"</#if> id="coach${em.id}"> ${em.name}
	                </label>
              	</#list>
              </#if>
              </div> 
            </div>
          </div>
          
        </div>
      </form>
      </div>
    </div>
    
    <div class="am-margin">
      <button type="button" onclick="cancel()" class="am-btn am-btn-primary am-btn-xs">取消</button>
      <button type="button" onclick="doSubmit()" class="am-btn am-btn-primary am-btn-xs">提交保存</button>
    </div>
    <script>
function cancel(){
	window.history.back();
}
function doSubmit(){
	//console.log($("input[name='coach']:checked").val());
	var code = $("#code").val();
	console.log(code);
	if(code == "" || code ==null || code == undefined){
		alert("请选择服务类型");
		return false;
	}
	$("#assignCoachForm").submit();
}
$("#level1").change(function(){
	$("#level2").html("<option>" + "--- 请选择二级服务 ---" + "</option>");
	$("#level3").html("<option>" + "--- 请选择三级服务 ---" + "</option>");
	$("#level4").html("<option>" + "--- 请选择四级服务 ---" + "</option>");
	var level1_code = $("#level1").find("option:selected").val();
	var selectedIndex = $("#level1").get(0).selectedIndex
	if(selectedIndex > 0){
		$.ajax({url:"/play_order/assign/product_type?level1="+level1_code , type:"get",
			success:function(res){
				if(res.code =="200"){
					var data = res.data.level2;
					for(var i=0;i<data.length;i++){
						$("#level2").append("<option value= '" + data[i].code + "'>" + data[i].name + "</option>");
					}
				}	
			}
		})
	}
	$("#code").val("");
	
})
$("#level2").change(function(){
	$("#level3").html("<option>" + "--- 请选择三级服务 ---" + "</option>");
	$("#level4").html("<option>" + "--- 请选择四级服务 ---" + "</option>");
	var level2_code = $("#level2").find("option:selected").val();
	var selectedIndex = $("#level2").get(0).selectedIndex
	if(selectedIndex > 0){
		$.ajax({url:"/play_order/assign/product_type?level2="+level2_code , type:"get",
			success:function(res){
				if(res.code =="200"){
					var data = res.data.level3;
					for(var i=0;i<data.length;i++){
						$("#level3").append("<option value= '" + data[i].code + "'>" + data[i].name + "</option>");
					}
				}
			}
		})
	}
	$("#code").val("");
	
})
$("#level3").change(function(){
	$("#level4").html("<option>" + "--- 请选择四级服务 ---" + "</option>");
	var selectedIndex = $("#level3").get(0).selectedIndex
	var level3_code = $("#level3").find("option:selected").val();
	if(selectedIndex > 0){
		$.ajax({url:"/play_order/assign/product_type?level3="+level3_code , type:"get",
			success:function(res){
				if(res.code !="200"){
					$("#level4").html("<option>" + "--- 无 ---" + "</option>");
					$("#code").val($("#level3").find("option:selected").val());
				}else{
					var data = res.data.level4;
					for(var i=0;i<data.length;i++){
						$("#level4").append("<option value= '" + data[i].code + "'>" + data[i].name + "</option>");
					}
					var selectedIndex = $("#level4").get(0).selectedIndex;
					if(selectedIndex == 0){
						$("#code").val("");
					}else{
						$("#code").val($("#level4").find("option:selected").val());
					}
				}
			}
		})
	}else{
		$("#code").val("");
	}
})
$("#level4").change(function(){
	var selectedIndex = $("#level4").get(0).selectedIndex;
	var level4_code = $("#level4").find("option:selected").val();
	if(selectedIndex > 0){
		$("#code").val($("#level4").find("option:selected").val());
	}else{
		$("#code").val("");
	}
})

    </script>
  </div>

  <footer class="admin-content-footer">
    <hr>
    <p class="am-padding-left">© 2014 AllMobilize, Inc. Licensed under MIT license.</p>
  </footer>
</div>
<!-- content end -->

</div>

<a href="#" class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}"></a>

<footer>
  <hr>
  <p class="am-padding-left">© 2014 AllMobilize, Inc. Licensed under MIT license.</p>
</footer>

<!--[if lt IE 9]>
<script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script>
<script src="http://cdn.staticfile.org/modernizr/2.8.3/modernizr.js"></script>
<script src="/assets/js/amazeui.ie8polyfill.min.js"></script>
<![endif]-->

<!--[if (gte IE 9)|!(IE)]><!-->
  <script src="/assets/js/jquery-2.1.4.min.js"></script>
<!--<![endif]-->
<script src="/assets/js/amazeui.min.js"></script>
<script src="/assets/js/app.js"></script>
</body>
</html>
