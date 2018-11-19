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
        <strong class="am-text-primary am-text-lg"><#if entity.orderType == 'Quick'>快捷订单<#else>普通订单</#if></strong> / <small>Order Assign</small>
      </div>
    </div>

    <hr>
    <div class="am-tabs am-margin" data-am-tabs>
      <ul class="am-tabs-nav am-nav am-nav-tabs">
        <li class="am-active"><a href="#tab1">订单信息</a></li>
      </ul>
      <div class="am-tabs-bd">
        <div class="am-tab-panel am-fade am-in am-active" id="tab1">
            <div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">订单编号</div>
	              <div class="am-u-sm-8 am-u-md-4">${entity.no}</div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            
            <div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">订单状态</div>
	              <div class="am-u-sm-8 am-u-md-4">${entity.status.text!}</div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            
			<div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">下单用户</div>
	              <div class="am-u-sm-8 am-u-md-4">${(entity.wxUser.nickName)!"未命名"}</div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            
            <div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">下单时间</div>
	              <div class="am-u-sm-8 am-u-md-4">${entity.creation!}</div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            
            <#if record?? >
            <div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">支付方式</div>
	              <div class="am-u-sm-8 am-u-md-4">${channel!}支付</div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            <div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">支付时间</div>
	              <div class="am-u-sm-8 am-u-md-4">${record.payTime!}</div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            </#if>
            
            <#if entity.playCompleteTime??>
            <div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">服务完成时间</div>
	              <div class="am-u-sm-8 am-u-md-4">${entity.playCompleteTime!}</div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            </#if>
            
        	<div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">期望服务大神</div>
	              <div class="am-u-sm-8 am-u-md-4">${entity.coach.name}</div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            
            <div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">接单人</div>
	              <div class="am-u-sm-8 am-u-md-4">${entity.executor.name!}</div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            
            <div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">一级服务类型</div>
	              <div class="am-u-sm-8 am-u-md-4"><#if entity.product.productType.level == 1>${entity.product.productType.name!}<#else>${level1.name!}</#if></div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            
            <#if level2??>
            <div class="am-g am-margin-top">
	             <div class="am-u-sm-4 am-u-md-2 am-text-right">二级服务类型</div>
	             <div class="am-u-sm-8 am-u-md-4">${level2.name!}</div>
	             <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            </#if>
            <#if entity.product.productType.level == 3>
	            <div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">三级服务类型</div>
	              <div class="am-u-sm-8 am-u-md-4">${entity.product.productType.name!}</div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
	            </div>
            </#if>
            <#if entity.product.productType.level == 4>
	            <div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">三级服务类型</div>
	              <div class="am-u-sm-8 am-u-md-4">${level3.name!}</div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
	            </div>
	        	<div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">四级服务类型</div>
	              <div class="am-u-sm-8 am-u-md-4">${entity.product.productType.name!}</div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
	            </div>
            </#if>
            
            <div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">服务补充</div>
	              <div class="am-u-sm-8 am-u-md-4"><textarea rows="10" cols="40" style="resize:none" readonly>${entity.remark!""}</textarea></div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            
            <div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">服务时长</div>
	              <div class="am-u-sm-8 am-u-md-4">${entity.duration} / ${entity.product.durationType.text}</div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            
            <#if entity.orderType == 'Common' && quoted??>
            <div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">单价</div>
	              <div class="am-u-sm-8 am-u-md-4">${quoted.price?c} 元</div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            </#if>
            
        	<div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">总金额</div>
	              <div class="am-u-sm-8 am-u-md-4">${entity.totalFee} 元</div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            
        	<div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">手机号</div>
	              <div class="am-u-sm-8 am-u-md-4">${entity.cellphone!}</div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
            
        	<div class="am-g am-margin-top">
	              <div class="am-u-sm-4 am-u-md-2 am-text-right">微信账号</div>
	              <div class="am-u-sm-8 am-u-md-4">${entity.wxAccount!'-'}</div>
	              <div class="am-hide-sm-only am-u-md-6"></div>
            </div>
        </div>
      </div>
    </div>
    
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
