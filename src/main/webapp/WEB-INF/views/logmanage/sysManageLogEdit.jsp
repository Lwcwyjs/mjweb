<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<html>
<head>
	<%@ include file="/commons/baseloginjs.jsp" %>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
	<title>操作日志查看</title>
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
									   value="${managelog.num_id}"/>
							</div>
						</div>
					</div>
					<div class="layui-inline">
						<div class="layui-inline">
							<label class="layui-form-label">用户ID</label>
							<div class="layui-input-inline">
								<input id="user_id" name="user_id" placeholder="" class="layui-input" lay-verify="required" readonly="readonly"
									   value="${managelog.user_id}"/>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<div class="layui-inline">
							<label class="layui-form-label">机构名称</label>
							<div class="layui-input-inline">
								<input id="organization" name="organization" placeholder="" class="layui-input" lay-verify="required" style="width: 320px" readonly="readonly"/>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<div class="layui-inline">
							<label class="layui-form-label">用户名</label>
							<div class="layui-input-inline">
								<input id="user_name" name="user_name" placeholder="" class="layui-input"
									   lay-verify="required" readonly="readonly"
									   value="${managelog.user_name}"/>
							</div>
						</div>
					</div>
					<div class="layui-inline">
						<div class="layui-inline">
							<label class="layui-form-label">操作类型</label>
							<div class="layui-input-inline">
								<input id="operate_type" name="operate_type" placeholder="" class="layui-input" lay-verify="required" readonly="readonly"
								/>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<div class="layui-inline">
							<label class="layui-form-label">操作时间</label>
							<div class="layui-input-inline">
								<input id="operate_time" name="operate_time" placeholder="" class="layui-input" lay-verify="required" readonly="readonly"
									   value="${managelog.operate_time}"/>
							</div>
						</div>
					</div>
					<div class="layui-inline">
						<div class="layui-inline">
							<label class="layui-form-label">终端标识</label>
							<div class="layui-input-inline">
								<input id="terminal_id" name="terminal_id" placeholder="" class="layui-input" lay-verify="required" readonly="readonly"
									   value="${managelog.terminal_id}"/>
							</div>
						</div>
					</div>
				</div>

				<div class="layui-form-item  layui-form-text">
					<div class="layui-inline">
						<div class="layui-inline">
							<label class="layui-form-label">操作内容</label>
							<div  class="layui-input-inline">
								<textarea id="operate_condition" class="layui-textarea" name="operate_condition" placeholder=""  readonly="readonly"
								></textarea>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<div class="layui-inline">
							<label class="layui-form-label">操作结果</label>
							<div class="layui-input-inline">
								<input id="operate_result" name="operate_result" placeholder="" class="layui-input" lay-verify="required" readonly="readonly"
									   value="${managelog.operate_result}"/>
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
		$('#operate_condition').val('${managelog.operate_condition}');
		if ("${managelog.organization}" != '') {
			var organization = getValue("organ", "${managelog.organization}",
					"${path }/admin/loginfo/basedata/organ");
			$("#organization").val(organization);
		}
		if ("${managelog.operate_type}" != '') {
			var operate_str = "未知类型";
			if ("${managelog.operate_type}" == 0) {
				operate_str = "登录操作";
			} else if ("${managelog.operate_type}" == 1) {
				operate_str = "查询操作";
			} else if ("${managelog.operate_type}" == 2) {
				operate_str = "新增操作";
			} else if ("${managelog.operate_type}" == 3) {
				operate_str = "修改操作";
			} else if ("${managelog.operate_type}" == 4) {
				operate_str = "删除操作";
			}
			$("#operate_type").val(operate_str);
		}
		if ("${managelog.operate_result}" != '') {
			var operate_str = "未知结果";
			if ("${managelog.operate_result}" == 0) {
				operate_str = "操作失败";
			} else if ("${managelog.operate_result}" == 1) {
				operate_str = "操作成功";
			}
			$("#operate_result").val(operate_str);
		}
		if ("${loginfo.operate_time}" != '') {
			var value = "${managelog.operate_time}";
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