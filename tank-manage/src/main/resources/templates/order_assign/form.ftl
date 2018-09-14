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
          <span class="am-icon-users"></span> ${(loginUser.name)!"管理员"} <span class="am-icon-caret-down"></span>
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
        <li class="admin-parent">
          <a class="am-cf" data-am-collapse="{target: '#collapse-nav'}"><span class="am-icon-file"></span> 订单管理  <span class="am-icon-angle-right am-fr am-margin-right"></span></a>
          <ul class="am-list am-collapse admin-sidebar-sub am-in" id="collapse-nav">
        	<li><a href="/play_order/assign/list"><span class="am-icon-table"></span> 派单</a></li>
        	<li><a href="/play_order/receive/list"><span class="am-icon-table"></span> 接单</a></li>
          </ul>
        </li>
      </ul>
    </div>
  </div>
  <!-- sidebar end -->

<!-- content start -->
<div class="admin-content">
  <div class="admin-content-body">
    <div class="am-cf am-padding am-padding-bottom-0">
      <div class="am-fl am-cf">
        <strong class="am-text-primary am-text-lg">派单</strong> / <small>Order Assign</small>
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
        	<div class="am-g am-margin-top">
              <div class="am-u-sm-4 am-u-md-2 am-text-right">金额</div>
              <div class="am-u-sm-8 am-u-md-4">${entity.totalFee}</div>
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
            <div class="am-u-sm-4 am-u-md-2 am-text-right">显示状态</div>
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
      <button type="button" onclick="doSubmit()" class="am-btn am-btn-primary am-btn-xs">提交保存</button>
    </div>
    <script>
function doSubmit(){
	//console.log($("input[name='coach']:checked").val());
	$("#assignCoachForm").submit();
}   
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
