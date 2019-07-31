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
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="statics/layui/css/layui.css" media="all">
</head>
			<fieldset class="layui-elem-field layui-field-title"
				 style="margin-top: 5px;">
				<legend>字段库列表</legend>
			</fieldset>
			<input id="guid" name="guid" style="display: none"
				value="<%=request.getParameter("guid")%>">
			<input id="flag" name="flag" style="display: none"
				value="<%=request.getParameter("flag")%>">
			<table class="layui-hide" id="demo" lay-filter="test"></table>
			<script type="text/html" id="barDemo">
  			<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
  			<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
		</script>
			<script type="text/html" id="toolbarDemo">
  				<div class="layui-btn-container">
    				<button type="button" class="layui-btn layui-btn-primary layui-btn-sm" lay-event="add">添加</button>
					<button type="button" class="layui-btn layui-btn-primary layui-btn-sm" lay-event="delete">删除</button>
					<button type="button" class="layui-btn layui-btn-primary layui-btn-sm" lay-event="update">返回上一级</button>
 				 </div>
			</script>
			<script src="statics/layui/layui.js"></script>
			<script type="text/javascript" src="statics/js/jquery-1.8.0.js"></script>
			<script src="statics/js/model/bzdk_Index.js"></script>
</html>
