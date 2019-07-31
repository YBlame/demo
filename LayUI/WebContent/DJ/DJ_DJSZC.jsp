<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
		<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<base href="<%=basePath%>">
<!-- saved from url=(0044)http://demob.exseen.com/html/dj/DJ_DJSZC.htm -->
    
    <title>搭建商注册</title>
    
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">

    <link type="text/css" rel="stylesheet" href="statics/js/dj/layout.css">
    <link type="text/css" rel="stylesheet" href="statics/js/dj/registerpwd.css">
    <link rel="stylesheet" href="statics/js/dj/jquery.gritter.css">
    <script type="text/javascript" src="statics/js/dj/jquery-1.7.1.js"></script>
    <script src="statics/js/concisejs.js" type="text/javascript"></script>
    <script src="statics/js/dj/jquery-1.7.1.js" type="text/javascript"></script>
    <script src="statics/js/dj/dj_djszc.js" type="text/javascript"></script>
    <script type="text/javascript" src="statics/js/dj/lhgcore.lhgdialog.min.js"></script>
    <script type="text/javascript" src="statics/js/dj/demo.js"></script>
	<link rel="stylesheet" href="statics/layui/css/layui.css" media="all">
	<script src="statics/layui/layui.js"></script>
</head>

    <body><div class="" style="left: 0px; top: 0px; visibility: hidden; position: absolute;"><table class="ui_border"><tbody><tr><td class="ui_lt"></td><td class="ui_t"></td><td class="ui_rt"></td></tr><tr><td class="ui_l"></td><td class="ui_c"><div class="ui_inner"><table class="ui_dialog"><tbody><tr><td colspan="2"><div class="ui_title_bar"><div class="ui_title" unselectable="on" style="cursor: move;"></div><div class="ui_title_buttons"><a class="ui_min" href="javascript:void(0);" title="最小化" style="display: inline-block;"><b class="ui_min_b"></b></a><a class="ui_max" href="javascript:void(0);" title="最大化" style="display: inline-block;"><b class="ui_max_b"></b></a><a class="ui_res" href="javascript:void(0);" title="还原"><b class="ui_res_b"></b><b class="ui_res_t"></b></a><a class="ui_close" href="javascript:void(0);" title="关闭(esc键)" style="display: inline-block;">×</a></div></div></td></tr><tr><td class="ui_icon" style="display: none;"></td><td class="ui_main" style="width: auto; height: auto;"><div class="ui_content" style="padding: 10px;"></div></td></tr><tr><td colspan="2"><div class="ui_buttons" style="display: none;"></div></td></tr></tbody></table></div></td><td class="ui_r"></td></tr><tr><td class="ui_lb"></td><td class="ui_b"></td><td class="ui_rb" style="cursor: se-resize;"></td></tr></tbody></table></div>
        
<div class="layout bugfix_ie6">
  <div class="n-logo-area clearfix">
    <a href="http://demob.exseen.com/html/dj/DJ_DJSZC.htm#" class="fl-l">
  
             
        
    </a>
    
  </div>
  
</div>

<div class="layout">
  <div class="n-frame device-frame reg_frame" id="main_container">
    <div class="title-item dis_bot35 t_c">
      <h4 class="title-big">注册搭建商帐号</h4>          
    </div>  
  <div><div class="regbox">
  <div class="phone_step1">
      <div class="listwrap" id="select-cycode">
          

          <form id="form1" name="form1" action="http://demob.exseen.com/html/dj/DJ_DJSZC.htm#" method="POST" class="form-horizontal">

                  
                  <div class="inputbg">
                      <div class="t1">姓名</div> <div class="t2"><input name="LXR" id="LXR" type="text" class="ti" clabel="姓名" placeholder=""><span class="tips" id="LXRT">请填写姓名</span></div>
                  </div>                 


                  
                  <div class="inputbg">
                      <div class="t1">手机</div> <div class="t2"><input name="SJ" id="SJ" type="text" class="ti" clable="手机" placeholder=" "><span class="tips" id="SJT">请正确填写手机号码，此手机号码将会接收账号信息</span></div>
                  </div>


                   
                  <div class="inputbg">
                      <div class="t1">邮箱</div> <div class="t2"><input name="EMAIL" id="EMAIL" type="text" class="span11" clabel="邮箱" placeholder=" "><span class="tips" id="EMAILT">请正确填写邮箱，认证通过后系统会向此邮箱发送注册号和密码</span></div>
                  </div>



                   
                  <div class="inputbg">
                      <div class="t1">公司名称</div> <div class="t2"><input name="DWMC" id="DWMC" type="text" class="span11" clabel="公司名称" placeholder=""><span class="tips" id="DWMCT">请填写完整公司名称</span></div>
                  </div>
                 

                  
                  <div class="inputbg">
                      <div class="t1">办公地址</div> <div class="t2"><input name="DWDZ" id="DWDZ" type="text" class="span11" clabel="办公地址" placeholder=" "><span class="tips" id="DWDZT">请填写公司办公地址</span></div>
                  </div>

          <div class="inputbg" style="color:red;padding-left: 80px;">
              <p id="yhm"></p>
          </div>
          <div class="fixed_bot" style="clear: both;">
              <input class=" btn-primary btn-large pull-left" data-to="phone-step2" value="立即注册" type="button" onclick="DJSXX()">
              <a href="login" class=" btn-large pull-left">返回首页</a>
              <p class="msg">
                  <br><br>点击“立即注册”，即表示您同意并愿意遵守本系统
<a href="javascript:getXY(0);" title="用户协议" class="runcode" name="demo_content">用户注册协议</a>
              </p>
          </div>

          </form>
          <div id="myShow" style="display: none;">
              <img src="statics/js/dj/loading1.gif">
          </div>
    
    
    

      </div>
</div>
</div></div>
</div>
    <div class="n-footer">
  
        <p class="nf-intro"><span>版权所有：慧展软件</span></p>
        <div class="nf-link-area clearfix">
        </div>
    </div>



</div></body></html>