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
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>登录</title>
		<link rel="stylesheet" type="text/css" href="statics/admin/layui/css/layui.css" />
		<link rel="stylesheet" type="text/css" href="statics/admin/css/login.css" />
	</head>

	<body>
		<div class="m-login-bg">
			<div class="m-login">
				<h3>登录</h3>
				<div class="m-login-warp">
					<form class="layui-form" action="loginIn"  onsubmit="return false">
						<div class="layui-form-item">
							<input type="text" id="phone" name="phone" required lay-verify="required" placeholder="手机号" autocomplete="off" class="layui-input">
						</div>
						<div class="layui-form-item">
							<input type="password" id="password" name="password" required lay-verify="required" placeholder="密码" autocomplete="off" class="layui-input">
						</div>
						<div class="layui-form-item m-login-btn">
							<div class="layui-inline">
								<button class="layui-btn layui-btn-normal" lay-submit onclick="toSubmit()">登录</button>
							</div>
							<div class="layui-inline">
								<button type="reset" class="layui-btn layui-btn-primary">重置</button>
							</div>
						</div>
					</form>
				</div>
				<p class="copyright"></p>
			</div>
		</div>
		<script src="statics/admin/layui/layui.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript" src="statics/js/jquery-1.8.0.js"></script>
		<script>
			layui.use(['form', 'layedit', 'laydate'], function() {
				var form = layui.form(),
					layer = layui.layer;
			});
			
			function toSubmit(){
				var sj = $("#phone").val();
				var mm = $("#password").val();
				$.post("loginIn", {
					sj : sj,
					mm : mm
				}, function(result) {
					if (result=="loginLose") {
						layui.use("layer", function() {
							var layer = layui.layer; //layer初始化
							layer.msg('账号或密码错误..');
						})
	                }else {
	                	layui.use("layer", function() {
							var layer = layui.layer; //layer初始化
							layer.msg('登录成功..');
						})
	                    location.href = 'toIndex'; //后台主页
	                } 
				});
			}
		</script>
	</body>

</html>