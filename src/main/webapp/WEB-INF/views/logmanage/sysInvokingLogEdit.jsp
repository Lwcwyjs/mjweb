<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<html>
<head>
	<%@ include file="/commons/baseloginjs.jsp" %>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
	<title>接口调用查看</title>
	<style>

		.layui-tree li i {
			color: white;
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

		.layui-tree-icon {
			color: #f9f8ff;
		}

		.layui-tree-txt {
			color: white;
		}
		.layui-form-label {
			width: 100px;
		}
	</style>
</head>
<body>
<div class="layui-fluid">
	<div class="layui-card">
		<div class="layui-card-body">
			<div class="layui-form toolbar" id="queryFormItem">
				<div class="layui-form-item">
					<div class="layui-inline">
						<div class="layui-inline">
							<label class="layui-form-label">NUM_ID</label>
							<div class="layui-input-inline">
								<input id="num_id" name="num_id" placeholder="" class="layui-input" lay-verify="required" readonly="readonly"
									   value="${invokinglog.num_id}"/>
							</div>
						</div>
					</div>
					<div class="layui-inline">
						<div class="layui-inline">
							<label class="layui-form-label">接口id</label>
							<div class="layui-input-inline">
								<input id="jkid" name="jkid" placeholder="" class="layui-input" lay-verify="required" readonly="readonly"
									   value="${invokinglog.jkid}"/>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<div class="layui-inline">
							<label class="layui-form-label">接口服务时间</label>
							<div class="layui-input-inline">
								<input id="operate_time" name="operate_time" placeholder="" class="layui-input" lay-verify="required" readonly="readonly"
									   value="${invokinglog.operate_time}"/>
							</div>
						</div>
					</div>
					<div class="layui-inline">
						<div class="layui-inline">
							<label class="layui-form-label">调用IP</label>
							<div class="layui-input-inline">
								<input id="terminal_id" name="terminal_id" placeholder="" class="layui-input" lay-verify="required" readonly="readonly"
									   value="${invokinglog.terminal_id}"/>
							</div>
						</div>
					</div>
				</div>

				<div class="layui-form-item  layui-form-text">
					<div class="layui-inline">
						<div class="layui-inline">
							<label class="layui-form-label">调用内容</label>
							<div  class="layui-input-inline">
								<textarea id="operate_content" class="layui-textarea" name="operate_content" placeholder=""  readonly="readonly"
								></textarea>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<div class="layui-inline">
							<label class="layui-form-label">调用返回</label>
							<div class="layui-input-inline">
								<textarea id="operate_result" class="layui-textarea" name="operate_result" placeholder=""  readonly="readonly"
								></textarea>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline" style="float: right; margin: 5px 20px;">
						<button type="reset" class="layui-btn icon-btn" style="margin-right: 10px;" id="userBtnClear"><i
								class="layui-icon"></i>关闭
						</button>
					</div>
				</div>

			</div>

		</div>
	</div>
</div>
<script type="text/javascript" src="${staticPath}/static/js/colmunFormat.js" charset="utf-8"></script>
<script>
	layui.config({
		base: '${staticPath}/static/module/'
	}).use(['form', 'tree', 'laydate'], function () {
		var form = layui.form;
		var $ = layui.jquery;
		$('#operate_content').val('${invokinglog.operate_content}');
		$('#operate_result').val('${invokinglog.operate_result}');
		if ("${invokinglog.operate_time}" != '') {
			var value = "${invokinglog.operate_time}";
			value = value.substring(0,4)+'-'+value.substring(4,6)+'-'+value.substring(6,8)+'  '+value.substring(8,10)+':'+value.substring(10,12)+':'+value.substring(12,14);

			$("#operate_time").val(value);
		}
		$('#userBtnClear').click(function () {
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index); //再执行关闭
		})
	})
</script>
</body>
</html>