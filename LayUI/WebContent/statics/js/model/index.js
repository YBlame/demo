layui.use(['layer','element'], function () {
    var $ = layui.jquery
    , layer = layui.layer //弹层
        , element = layui.element;    
    
});
function toIndexSystem(){//去开发人员的系统表
	$("#demoAdmin").attr("src", "bmodel_Index.jsp")
}
function toIndexAdmin(){//去系统中栏目列表
	$("#demoAdmin").attr("src", "smodel_Index.jsp")
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

      