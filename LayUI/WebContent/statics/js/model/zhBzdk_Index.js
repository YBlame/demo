//初始化页面信息
$(document).ready(function() {
					var flag =$("#flag").val();
					$("#flag").val("500");
					if (flag == 0) {
						layui.use("layer", function() {
							var layer = layui.layer; //layer初始化
							layer.msg('数据表生成成功..');
						})
					} else if (flag == 2) {
						layui.use("layer", function() {
							var layer = layui.layer; //layer初始化
							layer.msg('数据表修改成功..');
						})
					} else if (flag == 5) {
						layui.use("layer", function() {
							var layer = layui.layer; //layer初始化
							layer.msg('数据表删除成功..');
						})
					} else if (flag == -404) {
						layui.use("layer", function() {
							var layer = layui.layer; //layer初始化
							layer.msg('数据表生成异常..');
						})
					}
				})
				
//渲染表格页面
var guid = $("#guid").val();
				layui.config({
					version : '1554901097999' //为了更新 js 缓存，可忽略
				});
				layui.use([ 'laydate', 'laypage', 'layer', 'table',
										'carousel', 'upload', 'element',
										'slider' ],
								function() {
									var laydate = layui.laydate //日期
									, laypage = layui.laypage //分页
									, layer = layui.layer //弹层
									, table = layui.table //表格
									, carousel = layui.carousel //轮播
									, upload = layui.upload //上传
									, element = layui.element //元素操作
									, slider = layui.slider //滑块
									, index  = layer.load(1)
									//监听Tab切换
									element.on('tab(demo)', function(data) {
										layer.tips('切换了 ' + data.index + '：'
												+ this.innerHTML, this, {
											tips : 1
										});
									});
									var bm = $("#bm").val();
									setTimeout(function () {
										//执行一个 table 实例
										table.render({
											elem : '#demo',
											height : 420,
											url : 'zhxx/findAllMenu?guid='+guid+'&bm='+bm, //数据接口
											page : {
												layout: ['prev', 'page', 'next', 'skip', 'count'] //自定义分页布局
												,first: false //不显示首页
									            ,last: false //不显示尾页
											}, //开启分页
											limits: [3,5,10,20,50],
											limit: 10,
											toolbar : '#toolbarDemo' //开启工具栏，此处显示默认图标，可以自定义模型，详见文档
											,
											done:function () {
								                   layer.close(index) //加载完数据
								            },
											 totalRow : true //开启合计行
											, 
											cols : [ [ //表头
											{
												type : 'checkbox',
												fixed : 'left'
											}, {
												field : 'zdm',
												title : '字段名',
												hide : true,
												width : 140,
												sort: true
											}, {
												field : 'zdmc',
												title : '项目名',
												width : 140,
												sort: true
											}, {
												field : 'guid',
												title : '编号',
												width : 140,
												hide : true,
												sort: true
											}, {
												field : 'id',
												title : '自动编号',
												width : 140,
												hide : true,
												sort: true
											}, {
												field : 'isform',
												title : '必填项',
												width : 140,
												sort: true
											}, {
												field : 'formtypes',
												title : '表单类型',
												width : 140,
												sort: true
											}, {
												fixed : 'right',
												title : '操作',
												width : 240,
												align : 'center',
												toolbar : '#barDemo',
												fixed: 'right'
											} ] ]
										});
									},50);
									//监听头工具栏事件
									table.on('toolbar(test)',function(obj) {
														var checkStatus = table.checkStatus(obj.config.id)
										                ,data = checkStatus.data; //获取选中的数据
										                data = eval("("+JSON.stringify(data)+")");
														switch (obj.event) {
														case 'add':
															openAdd();
															break;
														case 'update':
															window.history.back(-1);
															break;
														case 'delete':
															if (data.length === 0) {
																layer.msg('请选择一行');
															} else {
																layer.confirm('真的要删除这'+data.length+'条数据么', function(index) {
																	layer.close(index);
																for (var i=0;i<data.length;i++){
																	$.post("toDelete", {
																		zdm : data[i].zdm,
																		guid : data[i].guid
																	}, function(result) {
																		if (result==-404) {
																			layer.msg("删除失败...");
																		}else{
																			window.location.reload();
																		}
																	});
																}
																});
															}
															break;
														}
														;
													});
									// 打开查看按钮
									function openAdd() {
										var guid = $("#guid").val();
										var bm = $("#bm").val();
										var bmc =$("#bmc").val();
										window.location = "zhxx/bzdk_add.jsp?bm="
												+ bm+"&bmc="+bmc+"&guid="+guid;
									}
									//监听行工具事件
									table.on('tool(test)', function(obj) { //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
										var data = obj.data //获得当前行数据
										, layEvent = obj.event
										,editList=[]//获得 lay-event 对应的值
										if (layEvent === 'detail') {
											layer.msg('查看操作');
										} else if (layEvent === 'del') {
											layer.confirm('真的删除么', function(
													index) {
												var zdm = data['zdm'];
												var id = data['id'];
												var guid = $("#guid").val();
												var bm=$("#bm").val();
												layer.close(index);
												$.post("zhxx/toDelete", {
													guid : guid,
													zdm : zdm,
													id : id,
													bm : bm
												}, function(result) {
													if (result==-404) {
														layer.msg("删除失败...");
													}else{
														obj.del();
														layer.msg("删除成功...");
													}
												});

											});
										} else if (layEvent === 'edit') {
											var zdGuid = data['guid'];
											var guid = $("#guid").val();
											var bm = $("#bm").val();
											var bmc =$("#bmc").val();
											window.location.href="zhxx/toUpdate?zdGuid="+zdGuid+"&guid="+guid+"&bm="+bm+"&bmc="+bmc;
								              
										}
									});
								});