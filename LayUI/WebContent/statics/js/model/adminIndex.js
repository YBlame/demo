layui.use(['layer','element'], function () {
    var $ = layui.jquery
    , layer = layui.layer //弹层
        , element = layui.element;          
});
var select  = cj.getCookie('selected_expo_id');
function toSystem(){//系统管理
	$("#demoAdmin").attr("src", "menu/toMenu")
}
function toRule(){//角色管理
	$("#demoAdmin").attr("src", "rule/toRuleIndex")
}
function findZhxx(){//添加展会
	$("#demoAdmin").attr("src", "findZhxx")
}
function findZhxx(){//添加展会
	$("#demoAdmin").attr("src", "findZhxx")
}

function userOut(){//退出
	layer.confirm('确定退出吗？', function(
			index) {
			top.location.href = "userOut";
			layer.close(index);
	});
}

      