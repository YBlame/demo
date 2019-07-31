<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="utf-8">
<link rel="stylesheet" href="statics/layui/css/layui.css" media="all">
</head>
			<input id="" name="guidBmodel" style="display: none"
				value="<%=request.getParameter("guidBmodel")%>" >
			<input id="guid" name="guid" style="display: none"
				value="<%=request.getParameter("guid")%>">
			<!-- 表名称 -->
			<input type="hidden" id="bmc" value="<%=request.getParameter("bmc")%>">
			<!-- flag判断值 -->
			<input type="hidden" id="flag" value="<%=request.getParameter("flag")%>">
			<fieldset class="layui-elem-field layui-field-title"
				 style="margin-top: 5px;">
				<legend><%=request.getParameter("bmc")%></legend>
			</fieldset>
			<form class='layui-form' id="vform" onsubmit="return false" >
				<div id="demoTable">
					<div id='button'  style="display: none" >
							&nbsp;&nbsp;&nbsp;<button type='button' id='reload_btn' class='layui-btn layui-inline' data-type='reload'>搜索</button>
							&nbsp;<button type="reset" id='reset' class="layui-btn layui-btn-primary">重置</button>
					</div>
				</div>
			</form>
			<table class="layui-hide"  id="demo" lay-filter="test"></table>
			<script type="text/html" id="barDemo">
  					<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
  					<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
			</script>
			<script type="text/html" id="toolbarDemo">
  				<div class="layui-btn-container">
    				<button type="button" class="layui-btn layui-btn-primary layui-btn-sm" lay-event="add">添加</button>
					<button type="button" class="layui-btn layui-btn-primary layui-btn-sm" lay-event="delete">返回上一级</button>
				</div>
			</script>
			
		<script src="statics/layui/layui.js"></script>
		<script src="statics/js/concisejs.js"></script>
		<script type="text/javascript" src="statics/js/jquery-1.8.0.js"></script>
		<script src="statics/js/model/doc_Index.js"></script>
</html>
