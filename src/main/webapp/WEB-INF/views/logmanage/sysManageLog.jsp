<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>

<html>
<head>
	<%@ include file="/commons/baseloginjs.jsp" %>
	<meta http-equiv="X-UA-Compatible" content="edge"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
	<script type="text/javascript" src="${staticPath}/static/echarts/js/echarts.min.js" charset="utf-8"></script>
	<style type="text/css">
	</style>
	<title>管理日志</title>
	<style>
		.layui-badge-rim + .layui-badge-rim {
			margin-left: 5px;
		}


		.layui-form-label {
			width: 60px;
		}

		.downpanel .layui-select-title span {
			line-height: 38px;
		}

		/*继承父类颜色*/
		.downpanel dl dd:hover {
			background-color: inherit;
		}

		.downpanel a cite {
			color: white;
		}

		.layui-badge-rim + .layui-badge-rim {
			margin-left: 5px;
		}


		.layui-form-label {
			width: 60px;
		}

		.layui-form-item .layui-input-inline {
			width: 160px;
		}

		#showTable {
			width: 28px;
			height: 30px;
			padding: 1px 5px;
			cursor: pointer;
			z-index: 100;
		}

		#showChart {
			width: 28px;
			height: 30px;
			padding: 1px 5px;
			cursor: pointer;
			z-index: 100;
		}

		#showLineChart {
			width: 28px;
			height: 30px;
			padding: 1px 5px;
			cursor: pointer;
			z-index: 100;
		}

		.layui-tree-icon {
			color: #f9f8ff;
		}

		.layui-tree-txt {
			color: white;
		}

	</style>
</head>

<div class="layui-fluid">
	<div class="layui-card">
		<div class="layui-card-body">
			<div class="layui-form toolbar" id="dataBaseSearchForm">
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">操作时间:</label>
						<div class="layui-input-inline">
							<input name="dateofstart" id="createdate" class="layui-input"/>
						</div>
					</div>
					<div class="layui-inline">
						<div class="layui-form-mid">-</div>
						<div class="layui-input-inline">
							<input name="dateofend" id="createdateend" class="layui-input"/>
						</div>
					</div>

					<div class="layui-inline">
						<button id="userBtnSearch" lay-submit="" lay-filter="vehicleSearchFilter"
								class="layui-btn icon-btn"><i
								class="layui-icon"></i>查询
						</button>
						<button type="reset" class="layui-btn icon-btn" id="userBtnClear"><i
								class="layui-icon"></i>清空
						</button>
					</div>
				</div>
			</div>
		</div>

	</div>

	<table class="layui-hide" id="vehicleTable" lay-filter="vehicleFilter"></table>
	<script type="text/html" id="actionHandle">
		<shiro:hasPermission name="/admin/managelog/edit">
					<span style="height: 24px; background-color: transparent; color: white; cursor: pointer;"
						  lay-event="detail">
                        <span style="margin: 10px; font-size: 16px; cursor: pointer;color:#27d9ff">详情</span>
                    </span>
		</shiro:hasPermission>
	</script>
</div>
</div>
</div>
<script type="text/javascript" src="${staticPath}/static/js/colmunFormat.js" charset="utf-8"></script>
<!-- 表格状态列 -->
<script>
	layui.config({
		base: '${staticPath}/static/module/'
	}).use(['form', 'table', 'tree', 'laydate'], function () {

		var $ = layui.jquery;
		var table = layui.table;
		var form = layui.form;
		var tree = layui.tree;
		var laydate = layui.laydate;

		//日期
		laydate.render({
			elem: '#createdate'
		});

		laydate.render({
			elem: '#createdateend'
		});
		$('#createdateend').val(new Date().Format('yyyy-MM-dd'));
		$('#createdate').val(new Date().Format('yyyy-MM-dd'));

		table.render({
			elem: '#vehicleTable'
			, url: '${path}/admin/managelog/dataGrid'
			, method: 'post'
			, request: {
				pageName: 'page' //页码的参数名称，默认：page
				, limitName: 'rows' //每页数据量的参数名，默认：limit
			}
			, where: {
				dateofstart: $('#createdate').val()
				, dateofend: $('#createdateend').val()
			}
			, response: {
				statusName: 'code' //规定数据状态的字段名称，默认：code
				, statusCode: 0 //规定成功的状态码，默认：0
				, countName: 'total' //规定数据总数的字段名称，默认：count
				, dataName: 'rows' //规定数据列表的字段名称，默认：data
			}
			, cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
			, cols: [[
				{
					field: 'organization', title: '所属机构',sort: true, templet: function (d, row, index) {
						var value = d.organization;
						return getValue("organ", value, "${path }/admin/user/basedata/organ");
					}
				},
				{field: 'user_name', title: '用户名', align: 'center', sort: true}
				, {
					field: 'terminal_id', title: '登录IP',align: 'center', sort: true
				}
				, {
					field: 'operate_time', title: '操作时间', align: 'center', sort: true,
					templet: function (d) {
						var value = d.operate_time;
						return value.substring(0, 4) + '-'
								+ value.substring(4, 6)
								+ '-'
								+ value.substring(6, 8)
								+ '  '
								+ value.substring(8, 10)
								+ ':'
								+ value.substring(10, 12)
								+ ':'
								+ value.substring(12, 14);
					}
				}
				, {
					field: 'operate_type', title: '操作类型', sort: true, templet: function (d, row, index) {
						var value = d.operate_type;
						var operate_str = "未知类型";
						if (value == 0) {
							operate_str = "登录操作";
						} else if (value == 1) {
							operate_str = "查询操作";
						} else if (value == 2) {
							operate_str = "新增操作";
						} else if (value == 3) {
							operate_str = "修改操作";
						} else if (value == 4) {
							operate_str = "删除操作";
						}
						return operate_str;
					}
				}
				, {
					field: 'operate_condition', title: '操作内容', align: 'center', sort: true
				}
				, {
					field: 'operate_result', title: '操作返回',sort: true, templet: function (d, row, index) {
						var value = d.operate_result;
						var operate_str = "未知类型";
						if (value == 0) {
							operate_str = "操作失败";
						} else if (value == 1) {
							operate_str = "操作成功";
						}
						return operate_str;
					}
				}
				, {
					field: 'action', align: 'center', title: '操作',
					templet: '#actionHandle'
				}
			]]
			, done: function (res, curr, count) {

			}
			, page: true
			, limit: 15
			, limits: [5, 10, 15, 20, 25, 30]
		})

		table.on('tool(vehicleFilter)', function (obj) {
			var data = obj.data;
			if (obj.event === 'detail') {
				// layer.msg('JYLSH：' + data.jylsh + ' 的查看操作');
				//iframe窗
				parent.layer.closeAll();
				parent.layer.open({
					type: 2,
					title: '详情',
					shadeClose: true,
					shade: false,
					maxmin: false, //开启最大化最小化按钮
					area: ['800px', '500px'],
					skin: 'my-skin',
					content: ['${path}/admin/managelog/editPage?id=' + data.num_id]
				});
			}
		});

		// 搜索按钮点击事件
		$('#userBtnSearch').click(function () {
			table.reload('vehicleTable', {
				page1: 1,
				where: {
					sort: 'dis'
					, order: 'asc'
					, dateofstart: $('#createdate').val()
					, dateofend: $('#createdateend').val()
				}
			});
		});

		// 清空按钮点击事件
		$('#userBtnClear').click(function () {
			$('#createdate').val('');
			$('#createdateend').val('');
		});
	});
</script>
</body>
</html>