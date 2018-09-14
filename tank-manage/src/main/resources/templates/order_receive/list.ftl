<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>manage platform - 管理平台</title>
  <meta name="description" content="manage platform - 管理平台">
  <meta name="keywords" content="manage">
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
      <li class="am-dropdown" data-am-dropdown>
        <a class="am-dropdown-toggle" data-am-dropdown-toggle href="/logout">
          <span class="am-icon-power-off"></span> 退出<span class="am-icon-caret-down"></span>
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
        <li><a href="javascript:;"><span class="am-icon-home"></span> 首页</a></li>
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
        <div class="am-fl am-cf"><strong class="am-text-primary am-text-lg">订单列表</strong> / <small>Order List</small></div>
      </div>

      <hr>

      <div class="am-g">
        <div class="am-u-sm-12 am-u-md-6">
          <div class="am-btn-toolbar">
            <div class="am-btn-group am-btn-group-xs">
            	<#--
              <button type="button" class="am-btn am-btn-default"><span class="am-icon-plus"></span> 新增</button>
              <button type="button" class="am-btn am-btn-default"><span class="am-icon-save"></span> 保存</button>
              <button type="button" class="am-btn am-btn-default"><span class="am-icon-archive"></span> 审核</button>
              <button type="button" class="am-btn am-btn-default"><span class="am-icon-trash-o"></span> 删除</button>
              	-->
            </div>
          </div>
        </div>
        <div class="am-u-sm-12 am-u-md-3">
          <div class="am-form-group">
            	<#--
            <select data-am-selected="{btnSize: 'sm'}">
              <option value="option1">所有类别</option>
              <option value="option2">IT业界</option>
              <option value="option3">数码产品</option>
              <option value="option3">笔记本电脑</option>
              <option value="option3">平板电脑</option>
              <option value="option3">只能手机</option>
              <option value="option3">超极本</option>
            </select>
              	-->
          </div>
        </div>
        <div class="am-u-sm-12 am-u-md-3">
          <form method="post" class="am-form" action="/play_order/receive/list" id="receiveOrderForm">
          <div class="am-input-group am-input-group-sm">
              <input type="text" class="am-form-field" name="keyword" value="${keyword!}" placeholder="请输入手机号或订单号" />
	          <span class="am-input-group-btn">
	            <button class="am-btn am-btn-default" type="button" onclick="doSearch()">搜索</button>
	          </span>
          </div>
          </form>
          <script>
          	function doSearch(){
          		$("#receiveOrderForm").submit();
          	}
          </script>
        </div>
      </div>

      <div class="am-g">
        <div class="am-u-sm-12">
            <table class="am-table am-table-striped am-table-hover table-main">
              <thead>
              <tr>
                <#--<th class="table-check"><input type="checkbox" /></th>
                <th class="table-id">ID</th>-->
                <th class="table-title">编号</th>
                <th class="table-author am-hide-sm-only">下单用户</th>
                <th class="table-date am-hide-sm-only">下单日期</th>
                <th class="table-author am-hide-sm-only">期望大神</th>
                <th class="table-author am-hide-sm-only">执行大神</th>
                <th class="table-type">服务类别</th>
                <th class="table-author am-hide-sm-only">金额</th>
                <th class="table-author am-hide-sm-only">下单时长</th>
                <th class="table-author am-hide-sm-only">联系方式</th>
                <th class="table-author am-hide-sm-only">微信账号</th>
                <th class="table-author am-hide-sm-only">状态</th>
                <th class="table-set">操作</th>
              </tr>
              </thead>
              <tbody>
              <#if page.content??>
              <#list page.content as em>
              <tr>
                <#--<td><input type="checkbox" /></td>
                <td>1</td>-->
                <td>${em.no}</td>
                <td>${(em.wxUser.nickName)!"未命名"}</td>
                <td>${em.creation}</td>
                <td>${em.coach.name}</td>
                <td>${em.executor.name}</td>
                <td>${em.product.productType.name}</td>
                <td>${em.totalFee}</td>
                <td>${em.duration} / ${em.product.durationType.text}</td>
                <td>${em.cellphone!}</td>
                <td>${em.wxAccount!}</td>
                <td>${em.status.text}</td>
                <td>
                  <div class="am-btn-toolbar">
                    <div class="am-btn-group am-btn-group-xs">
                      <button onclick="exeComplete(${em.id}, ${em.no})" class="am-btn am-btn-default am-btn-xs am-text-secondary"><span class="am-icon-pencil-square-o"></span> 执行完成</button>
                    </div>
                  </div>
                </td>
              </tr>
              </#list>
              <#else>
              <tr>
              	<td>沒有符合的数据</td>
              </tr>
              </#if>
              </tbody>
            </table>
            <#--
            <#import "/ftl/component.ftl" as p>
			<@p.pageInfo page=pageInfo.page totalpages=pageInfo.totalPages 
			rownum=pageInfo.rowNum totalrownums=pageInfo.totalRowNums toURL="/exp_audit/"/>
			-->
            <form method="post" class="am-form" action="/play_order/receive/list" id="assignOrderPageForm">
          	<input type="hidden" name="keyword" value="${keyword!}" />
	        <input type="hidden" id="pageNum" name="pageNum" value="${pageNum!}" />
            <div class="am-cf">
            
              共 ${page.totalElements} 条记录
              <div class="am-fr">
                <ul class="am-pagination">
                  <#if pageNum == 0>
                  <li class="am-disabled"><a href="javascript:;">«</a></li>
                  <#else>
                  <li><a href="javascript:;" onclick="doPages(0)">«</a></li>
                  </#if>
                  
                  <#if pageNum == 0>
                  <li class="am-active"><a href="javascript:;">1</a></li>
                  	<#if page.totalPages gt 1>
                  	<li><a href="javascript:;" onclick="doPages(1)">2</a></li>
                  	</#if>
                  	<#if page.totalPages gt 2>
                  	<li>...</li>
                  	</#if>
                  <#else>
                    <#if pageNum gt 1>
                  	<li>...</li>
                  	</#if>
                  <li><a href="javascript:;" onclick="doPages(${pageNum - 1})">${pageNum}</a></li>
				  <li class="am-active"><a href="javascript:;">${pageNum + 1}</a></li>                  	
                  	<#if page.totalPages gt (pageNum + 1)>
                  	<li><a href="javascript:;" onclick="doPages(${pageNum + 1})">${pageNum + 2}</a></li>
                  	</#if>
                  	<#if page.totalPages gt (pageNum + 2)>
                  	<li>...</li>
                  	</#if>
                  </#if>
                  <#if page.totalPages == (pageNum + 1)>
				  <li class="am-disabled"><a href="javascript:;">»</a></li>
                  <#else>
                  <li><a href="javascript:;" onclick="doPages(pageNum + 1)">»</a></li>
                  </#if>
                </ul>
              </div>
            </div>
            </form>
            <script>
            function doPages(n){
            	$("#pageNum").val(n);
            	$("#assignOrderPageForm").submit();
            }
            function exeComplete(id, no){
            	if(confirm("确定编号为" + no + "的订单已执行完成？")){
            		$.ajax({
					  type: 'POST',
					  url: "/play_order/receive/exe/" + id,
					  data: {},
					  success: function(res){
					  	console.log(res);
					  	if(res.code == 200){
							window.location.reload();
					  	}else{
					  		alert(res.data);
					  	}
					  }
					});
            	}else{
            		console.log("选择了取消");
            	}
            }
            </script>
            <hr />
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
