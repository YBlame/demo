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
<input id="guid" name="guid" style="display: none"
	value="<%=request.getParameter("guid")%>" required
	lay-verify="required" autocomplete="off" class="layui-input">
<input type="hidden" id="bmc" value="<%=request.getParameter("bmc")%>">
<fieldset class="layui-elem-field layui-field-title"
	style="margin-top: 5px;">
	<legend>角色管理</legend>
</fieldset>
	<div style="padding: 10px; background-color: #F2F2F2;">
		<div class="layui-row layui-col-space12">
			<div class="layui-col-md6">
				<div class="layui-card">
					<div class="layui-card-header">角色列表</div>
					<table class="layui-hide" id="demo" lay-filter="test" style="height:400px;"></table>
				</div>
			</div>
			<div class="layui-col-md3" id="menuDiv" style="display: none;">
				<div class="layui-card">
					<div class="layui-card-header" id="menu" >菜单列表</div>
					<div class="layui-card-body">
						<div class="layui-btn-container">
							<input id="roleMenuGuid" name="roleMenuGuid"  style="display: none">
							<input id="roleName" name="roleName"  style="display: none">
							<input id="menuGuid" name="menuGuid"  style="display: none">
							<input type='button' class='layui-btn layui-btn-sm' lay-event='reload' value="刷新"/>
							<input type="button" id="checkedAll" class="layui-btn layui-btn-sm" lay-event='checkedAll' value="选中全部"/>
							<input type="button" id="save" class="layui-btn layui-btn-sm"  lay-event='getCheck' value="保存"/></div><!-- lay-submit="" lay-filter="save"  -->
						<div id="test12" class="demo-tree-more" style="height:500px;overflow:auto;"></div>
					</div>
				</div>
			</div>
			<div class="layui-col-md3" data-anim="layui-anim-up" id="ruleDiv" style="display: none;">
				<div class="layui-card">
					<div class="layui-card-header" id="rule" >权限规则</div>
					<div class="layui-card-body">
					<button type='button' class='layui-btn layui-btn-sm' lay-event='selectAll' >全选</button>
					<button type='button' class='layui-btn layui-btn-sm' lay-event='notSelectAll'>全不选</button>
					<button type='button' class='layui-btn layui-btn-sm' onclick='submitRule()'>立即提交</button>
						<div class="layui-form-item" id="ruleList">
							<div class="layui-form">
								<input id="guidRole" name="guidRole"  style="display: none">
								<input id="guidMenu" name="guidMenu"  style="display: none">
								<input id="roleName" name="roleName"  style="display: none">
								<input id="checkedData" name="checkedData"  style="display: none">
								
								<ul>
									<li><input type='checkbox' class='parent' id="INSERT" name='rule'
										 title='新增' value='INSERT'></li>
								</ul>
								<ul>
									<li><input type='checkbox' class='parent' id="EDIT" name='rule'
										 title='修改' value='EDIT'></li>
								</ul>
								<ul>
									<li><input type='checkbox' class='parent' id="ALLDELETE" name='rule'
										 title='全部删除' value='ALLDELETE'></li>
								</ul>
								<ul>
									<li><input type='checkbox' class='parent' id="SELECT" name='rule'
										 title='查询' value='SELECT'></li>
								</ul>
								<ul>
									<li><input type='checkbox' class='parent' id="IMPORT" name='rule'
										 title='导入' value='IMPORT'></li>
								</ul>
								<ul>
									<li><input type='checkbox' class='parent' id="EXPORT" name='rule'
										 title='导出' value='EXPORT'></li>
								</ul>
								<ul>
									<li><input type='checkbox' class='parent' id="DELETE" name='rule'
										 title='删除'  value='DELETE'></li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
<script type="text/html" id="barDemo">
			<a class="layui-btn layui-btn-xs" lay-event="edit">权限设置</a>
</script>
<script type="text/javascript" src="statics/js/jquery-1.8.0.js"></script>
<script src="statics/layui/layui.js"></script>
<script src="statics/js/concisejs.js"></script>
<script src="statics/js/model/rule_Index.js"></script>
</html>
