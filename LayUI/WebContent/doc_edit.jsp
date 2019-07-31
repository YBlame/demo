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
<link rel="stylesheet" href="statics/css/exseen.css">
<link rel="stylesheet" href="statics/layui/css/layui.css" media="all">
</head>
		<script src="statics/layui/laydate/laydate.js"></script>
		<script src="statics/layui/layui.js"></script>
		<script type="text/javascript" src="statics/js/jquery-1.8.0.js"></script>
			<fieldset class="layui-elem-field layui-field-title"
				style="margin-top: 20px;">
				<legend>数据编辑</legend>
			</fieldset>
			<div class="site-text site-block">
				<input id="guid"  style="display: none" 
						value="<%=request.getParameter("guid")%>">
				<input id="guidBmodel"  style="display: none" 
						value="<%=request.getParameter("guidBmodel")%>" >
				<input id="flag"  style="display: none"
						value="<%=request.getParameter("flag")%>">
				<form id="layui-form" class="layui-form" action="doc/doc_doEdit" onsubmit="return fomrSubmit(this);" method="post" >
				</form>
			</div>
		<script src="statics/layui/laydate/laydate.js"></script>
		<script src="statics/layui/layui.js"></script>
		<script type="text/javascript" src="statics/js/jquery-1.8.0.js"></script>
		<script src="statics/js/concisejs.js"></script>
		<script src="statics/js/model/doc_edit.js"></script>
</html>
