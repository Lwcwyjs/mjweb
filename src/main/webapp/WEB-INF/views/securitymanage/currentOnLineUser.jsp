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
	<title>当前在线用户管理</title>
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

		.layui-table-view {
			margin: 40px;
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
						<button id="userBtnSearch" lay-submit="" lay-filter="vehicleSearchFilter"
								class="layui-btn icon-btn"><i
								class="layui-icon"></i>刷新
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<table class="layui-hide" id="vehicleTable" lay-filter="vehicleFilter"></table>
</div>
</div>
</div>
<script type="text/javascript" src="${staticPath}/static/js/colmunFormat.js" charset="utf-8"></script>
<!-- 表格状态列 -->
<script>
	layui.config({
		base: '${staticPath}/static/module/'
	}).use(['form', 'table', 'tree'], function () {

		var $ = layui.jquery;
		var table = layui.table;
		var form = layui.form;
		var tree = layui.tree;


		table.render({
			elem: '#vehicleTable'
			, url: '${path}/admin/securitylog/currOnLineUser'
			, method: 'post'
			, request: {
				pageName: 'page' //页码的参数名称，默认：page
				, limitName: 'rows' //每页数据量的参数名，默认：limit
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
					field: 'name', title: '姓名',  align: 'center', sort: true
				}
				, {
					field: 'sessionid', title: '当前编号',  align: 'center', sort: true
				}
			]]
			, done: function (res, curr, count) {

			}
			, page: true
			, limit: 15
			, limits: [5, 10, 15, 20, 25, 30]
		});



		// 搜索按钮点击事件
		$('#userBtnSearch').click(function () {
			table.reload('vehicleTable', {
				page1: 1
			});
		});
	});


</script>
</body>
</html>