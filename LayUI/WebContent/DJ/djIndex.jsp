<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>慧展软件管理后台</title>
<meta ZHMC="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta ZHMC="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="statics/layui/css/layui.css" media="all">
</head>
<body>
<div class="layui-layout layui-layout-admin">
  <div class="layui-header">
   <div class="layui-logo">慧展</div>
	   <ul class="layui-nav layui-layout-left" lay-filter="top_expo_nav" id="top_expo_nav">
	      <li class="layui-nav-item" id ="zhxx">
	      </li>
	    </ul>
		<ul class="layui-nav layui-layout-right">
		      <li class="layui-nav-item">
		        <a href="javascript:;">
		        	<%String user = request.getSession().getAttribute("user").toString();%>  
					<%=user %>  
					<%String role = request.getSession().getAttribute("role").toString();%>  
						&nbsp;<%=role %>  
		        </a>
		      </li>
		      <li class="layui-nav-item"><a href="javascript:userOut();" target="_parent">退出</a></li>
  		</ul>
	</div>
  
  <div class="layui-side layui-bg-black">
    <div class="layui-side-scroll">
      <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
      <ul class="layui-nav layui-nav-tree site-demo-nav" lay-filter="test">
       			<li class="layui-nav-item layui-nav-itemed" >
			          <a href="javascript:findZhxx();" >展会信息</a>
		        </li>
				<!-- <li class="layui-nav-item layui-nav-itemed">
				<a class="" href="javascript:;">模型管理</a>
					<dl class="layui-nav-child" id="model">
						<dd>
							<a href="javascript:toIndex();" data-url="">模型列表</a>
						</dd>
					</dl>
				</li> -->
				 <li class="layui-nav-item layui-nav-itemed">
			          <a href="javascript:;">系统设置</a>
			          <dl class="layui-nav-child" id="system" >
			            <dd><a href="javascript:toSystem();" data-url="toMenu">菜单管理</a></dd>
			            <dd><a href="javascript:toRule();" data-url="">权限管理</a></dd>
			          </dl>
		        </li>
			</ul>
    </div>
  </div>
	<div class="layui-body">
    <!-- 内容主体区域 -->
    	<div style="width: 99%; height: 100%">
    		<iframe src="findZhxx" frameborder="0" id="demoAdmin"
		style="width: 99%; height: 99%"></iframe>
	
		</div>
 	 </div>
	<div class="layui-footer" style="text-align:  center;">
			<!-- 底部固定区域 -->
			© - 底部固定区域
	</div>
</div>
	<script src="statics/layui/layui.js"></script>
	<script type="text/javascript" src="statics/js/jquery-1.8.0.js"></script>
	<script src="statics/js/concisejs.js"></script>
	<script type="text/javascript" src="statics/js/model/adminIndex.js"></script>
	<script type="text/javascript">
	var reloadExpo;
	layui.use(['element', 'layer', 'form', 'laytpl'], function () {
	    var element = layui.element,
	        layer = layui.layer,
	        form = layui.form,
	        laytpl = layui.laytpl;
	    	reloadExpo = function(){
	    	 $.post("zhxx/findAllZhxx", 
    		    function(data) {
	    		 debugger
	    		 if (data.length==0) {
	                 layer.alert('请先添加一个展会');
	                 html = '<a href="javascript:;" data-action="add">添加展会</a>';
	                 $('#top_expo_nav li').html(html);
	                 return;
	             }
	    		 $('#top_expo_nav li').html('');
	                if (data.length > 0) { // 有展会，渲染数据
	                	var selected = cj.getCookie('selected_expo_id');
	                    var ids = [];
	                    for (var j = 0; j < data.length; j++) {
	                        ids.push(data[j].guid);
	                    }
	                    var html;
	                    var other;
	                    if (selected !== '' && $.inArray(selected, ids) !== -1) {
	                        other = '<dl class="layui-nav-child">';
	                        for (var k = 0; k < data.length; k++) {
	                            if (data[k].guid === selected) html = '<a href="javascript:;" id="g_expo" data-id="' + data[k].guid + '">' + data[k].ZHMC + '</a>';
	                            else other += '<dd><a href="javascript:;" id="g_expo" data-id="' + data[k].guid + '">' + data[k].ZHMC + '</a></dd>';
	                        }

	                        if (data.length > 1) html += other;

	                        $('#top_expo_nav li').html(html);
	                        element.render('nav', 'top_expo_nav');

	                    } else {
	                        html = '<a href="javascript:;" id="g_expo" data-id="' + data[0].guid + '">' + data[0].ZHMC + '</a>';
	                        if (data.length > 1) {
	                            other = '<dl class="layui-nav-child">';
	                            for (var i = 1; i < data.length; i++) {
	                                other += '<dd><a href="javascript:;" id="g_expo" data-id="' + data[i].guid + '">' + data[i].ZHMC + '</a></dd>';
	                            }
	                            other += '</dl>';
	                            html += other;
	                        }
	                        $('#top_expo_nav li').html(html);
	                        cj.setCookie('selected_expo_id', data[0].guid, 365);
	                        element.render('nav', 'top_expo_nav');
	                    }
	                }  else { // 无展会，提示添加
	                    cj.removeCookie('selected_expo_id');
	                        html = '<a href="javascript:;" data-action="add">添加展会</a>';
	                    $('#top_expo_nav li').html(html);
	                    element.render('nav', 'top_expo_nav');
	                }
	    	 });	
	    }	
	    reloadExpo()
		// 展会切换监听
	    element.on('nav(top_expo_nav)', function (elem) {
	    	if (elem.data('action') === 'add') {
	            layer.alert('点击左侧「展会基本信息」进行添加');
	            return;
	        }
	    	var selected = cj.getCookie('selected_expo_id');
	        var elemId = elem.data('id');
	        if (selected === elemId) return;
	        layer.confirm('切换展会需要刷新页面，请先保存当前操作', {
	            btn: ['切换', '不切换']
	        }, function () {
	        	cj.setCookie('selected_expo_id', elemId, 365);
	            location.reload();
	            $("#demoAdmin").attr("src", "findZhxx");
	        });
	    });
	});
	</script>
</body>
</html>