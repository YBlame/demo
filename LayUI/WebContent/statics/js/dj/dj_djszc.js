/// <reference path="../jquery-1.7.1.js" />

///搭建商注册
function DJSXX() {
    var boo = validate();
    if (boo) {
        var formData = $("#form1").serialize();
        //获取导航
        $.ajax({
            type: "POST",
            url: "/DLGL/AddDJSXX?time=" + new Date().getMilliseconds(),
            data: formData,
            cache: false,
            dataType: "json",
            beforeSend: function () {
                $("#myShow").css({ display: "", top: "-300px", left: "50%", position: "relative" });
            },
            success: function (data) {
                if (data.success) {
                    $("#myShow").hide();
                    $("#yhm").html("");
                    alert("提交已成功，请等待管理员审核。");
                    window.location.href = "/";
                } else {
                    $("#myShow").hide();
                    alert(data.msg);
                }
            }
        });
    }
}


function validate() {
    var boo = true;
    var v = "";
    var msg = "";
    var vid = "";
    var reg = /^[-+]?\d*$/;
    ///\w\s*$/;                                         //英文、数字和空格
    ///^[A-Za-z]+$/;                                    //英文字母
    //^[A-Za-z0-9_\u4e00-\u9fa5]+$/;                    //中英文、数字和下划线
    //    var regName = /^[A-Za-z\u4e00-\u9fa5]+$/;           //中文和字母
    //    var regNameY = /^(([\u4e00-\u9fff]{1,245})|([a-z\.\s\,]{1,245}))$/i; //英文、数字和空格以及.,
    var regName = /^[A-Za-z\u4e00-\u9fa5\()\（）]+$/;       //中文和字母
    var regNameY = /^(([\u4e00-\u9fff]{0,})|([a-z\.\&\()\-\-\s\,]{0,}))$/i; //英文、数字和空格以及.,
    var regAddY = /^[\u4e00-\u9fa5]+$/;                 //中文

    $(".ti").each(function () {

        v = $(this).val();
        vid = $(this).attr("id");
        if (v == "" || v == null) {
           
            boo = false;
            msg = "请填写" + $(this).attr("clabel");

            $("#" + vid + "T").show();
            $(this).focus();
            return false;
        }
        else {
            $("#" + vid + "T").hide();
        }
    });
    if (!boo) {
        return false;
    }
    $(".span11").each(function () {

        v = $(this).val();
        vid = $(this).attr("id");
        if (v == "" || v == null) {
            
                boo = false;
                msg = "请填写" + $(this).attr("clabel");
                
                $("#" + vid + "T").show();
                $(this).focus();
                return false;
            }
            else {
                $("#" + vid + "T").hide();
            }
    });


        if (boo) {
            var phone = $.trim($("#EMAIL").val());
            var reg = new RegExp(/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/);
            if (!reg.test(phone)) {
                boo = false;
                msg = "邮箱格式不正确";
                $("#EMAILT").show();
                $("#EMAIL").focus();
            }
            else {
                $("#EMAILT").hide();
            }
        }
        if (boo) {
            var phone = $.trim($("#SJ").val());
            var reg = new RegExp(/^0?1[3|4|5|7|8][0-9]\d{8}$/);
            if (!reg.test(phone)) {
                boo = false;
                msg = "请输入正确的手机号";
                $("#SJT").show();
                $("#SJ").focus();
            }
            else {
                $("#SJT").hide();
            }
        }
    if (!boo) {
        //alert(msg);
    }
    return boo;
}

$(function() {
    $("#DWMC").focus(function() {
        $("#DWMCT").show();
    });
    $("#DWMC").blur(function () {
        $("#DWMCT").hide();
    });
    $("#EMAIL").focus(function () {
        $("#EMAILT").show();
    });
    $("#EMAIL").blur(function () {
        $("#EMAILT").hide();
    });
    $("#SJ").focus(function () {
        $("#SJT").show();
    });
    $("#SJ").blur(function () {
        $("#SJT").hide();
    });

});

function getXY()
{
    /*var url = "url:DJ/DJ_XieYi.jsp";
    var dg = new $.dialog({
        title: '用户注册协议', titleBar: false, maxBtn: false, minBtn: false, content: url, width: '990px', height: '610px'
    });*/
layui.use(['laypage','layer'],function(){
	var layer = layui.layer //弹层
    layer.open({
      type: 2,
      title: '用户注册协议',
      shadeClose: true,
      shade: false,
      maxmin: true, //开启最大化最小化按钮
      area: ['893px', '600px'],
      content: 'DJ/DJ_XieYi.jsp'
    });
	 });
}

