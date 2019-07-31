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

<link rel="stylesheet" href="statics/css/exseen.css">
<link rel="stylesheet" href="statics/layui/css/layui.css" media="all">

</head>
			<fieldset class="layui-elem-field layui-field-title"
				style="margin-top: 20px;">
				<legend>字段编辑&nbsp;<button type="reset" class="layui-btn layui-btn-primary" onclick="toBack()">返回</button></legend>
			</fieldset>
			<div class="site-text site-block">
				<form class="layui-form" action="zhxx/doUpdate" method="post">
					<div class="layui-form-item">
						<label class="layui-form-label">项目名</label>
						<div class="layui-input-inline">
							<input type="text" id="zdmc" name="zdmc" required value="${map.zdmc}"
								lay-verify="required" placeholder="请输入项目名" autocomplete="off"
								class="layui-input">
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					
					<input type="hidden" value="${map.formtypes}" id="formtypesStr"/>
					<div class="layui-form-item">
						<label class="layui-form-label">表单类型</label>
						<div class="layui-input-inline">
							<select name="formtypes" id="formtypes" lay-filter="formtypes">
								<option value="text">文本</option>
								<option value="textarea">多行文本</option>
								<option value="number">数字</option>
								<option value="date">日期</option>
								<option value="datetime">日期+时间</option>
								<option value="pic">图片</option>
								<option value="file">附件</option>
								<option value="select">下拉列表</option>
								<option value="radio">单项选择</option>
								<option value="checkbox">多项选择</option>
								<option value="country">国家</option>
								<option value="city">省市</option>

							</select>
						</div>
					</div>
					
					<div class="layui-form-item">
						<label class="layui-form-label">必填项</label>
						<div class="layui-input-block">
						<c:if test="${map.isform =='1' }">
							<input type="radio" name="isform" value="1" title="是" checked>
							<input type="radio" name="isform" value="0" title="否" >
						</c:if>
						<c:if test="${map.isform =='0' }">
						<input type="radio" name="isform" value="1" title="是" >
							<input type="radio" name="isform" value="0" title="否" checked>
						</c:if>
						
						</div>
						<div class="layui-form-mid layui-word-aux"></div>
					</div>
					
					<input id="guid" name="guid" style="display: none" value="<%=request.getParameter("guid")%>" >
					<input id="id" name="id" style="display: none" value="${map.id}" >
					<input id="bm" name="bm" style="display: none"
						value="<%=request.getParameter("bm")%>">
					<input id="bmc" name="bmc" style="display: none"
						value="<%=request.getParameter("bmc")%>">
					<div class="layui-form-item">
						<div class="layui-input-block">
							<button class="layui-btn" >立即提交</button>
							<button type="reset" class="layui-btn layui-btn-primary">重置</button>
						</div>
					</div>
				</form>
			</div>
		<script src="statics/layui/layui.js"></script>
		<script type="text/javascript" src="statics/js/jquery-1.8.0.js"></script>
		<script src="statics/js/model/zhBzdk_edit.js"></script>
		<script src="statics/js/exseen.js"></script>
</html>
