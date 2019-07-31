
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>出错了</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
<link rel="stylesheet" href="statics/layui/css/layui.css">
<link rel="stylesheet" href="statics/layui/admin.css"
	media="all">
</head>
<body>


	<div class="layui-fluid">
		<div class="layadmin-tips">
			<i class="layui-icon" face>&#xe664;</i>
			<div class="layui-text" style="font-size: 20px;">好像出错了..</div>

		</div>
	</div>


	<script type="text/javascript" src="statics/layui/layui.js"></script>
	<script>
		layui.config({
			base : 'statics/layui/' //静态资源所在路径
		}).extend({
			index : 'lib/index' //主入口模块
		}).use([ 'index' ]);
	</script>
</body>
</html>