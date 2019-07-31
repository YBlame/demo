<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
		<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="utf-8">
<title>慧展软件管理后台</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="statics/layui/css/layui.css" media="all">
</head>
<body>
<div class="layui-layout layui-layout-admin">
  <div class="layui-header">
   <div class="layui-logo">慧展</div>
		<ul class="layui-nav layui-layout-right">
		      <li class="layui-nav-item">
		        <a href="javascript:;">
		        	<%String user = request.getSession().getAttribute("user").toString();%>  
					<%=user %>  
					<%String role = request.getSession().getAttribute("role").toString();%>  
					<%=role %>  
		        </a>
		      </li>
		      <li class="layui-nav-item"><a href="javascript:userOut();" target="_parent">退出</a></li>
  		</ul>
	</div>
  
  <div class="layui-side layui-bg-black">
    <div class="layui-side-scroll">
      <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
      <ul class="layui-nav layui-nav-tree site-demo-nav" lay-filter="test">
				<li class="layui-nav-item layui-nav-itemed">
				<a class="" href="javascript:;">模型管理</a>
					<dl class="layui-nav-child" id="model">
						<dd>
							<a href="javascript:toIndexSystem();" data-url="">模型列表</a>
						</dd>
						<dd>
							<a href="javascript:toIndexAdmin();" data-url="">栏目列表</a>
						</dd>
					</dl>
				</li>
		         <li class="layui-nav-item layui-nav-itemed">
			          <a href="javascript:;">展会信息管理</a>
			          <dl class="layui-nav-child" id="system" >
			            <dd>
							<a href="javascript:findZhxx();" data-url="">展会管理</a>
						</dd>
					</dl>
		        </li>
			</ul>
    </div>
  </div>
	<div class="layui-body">
    <!-- 内容主体区域 -->
    	<div style="width: 100%; height: 100%">
    		<iframe src="bmodel_Index.jsp?from=demo" frameborder="0" id="demoAdmin"
		style="width: 100%; height: 100%"></iframe>
	
		</div>
 	 </div>
	<div class="layui-footer" style="text-align:  center;">
			<!-- 底部固定区域 -->
			© - 底部固定区域
	</div>
</div>
	<script src="statics/layui/layui.js"></script>
	<script type="text/javascript" src="statics/js/jquery-1.8.0.js"></script>
	<script type="text/javascript" src="statics/js/model/index.js"></script>
</body>
</html>