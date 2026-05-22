<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
	<%@ include file="/commons/basejs.jsp"%>
<%--	<%@ include file="/commons/baseloginjs.jsp" %>--%>
	<%--    <meta http-equiv="X-UA-Compatible" content="edge"/>--%>
<%--	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>--%>
	<title>功能模块编辑</title>
	<style>

		.layui-tree li i {
			color: #666;
		}
		.downpanel .layui-select-title span {
			line-height: 38px;
		}

		/*!*继承父类颜色*!*/
		/*.downpanel dl dd:hover {*/
		/*	background-color: inherit;*/
		/*}*/

		/*.layui-form-select dl dd:hover {*/
		/*	background-color: #1E9FFF;*/
		/*}*/

		.layui-tree-entry:hover {
			background-color: #1E9FFF;
		}



		.downpanel a cite {
			color: #666;
		}
		.layui-tree-icon {
			color: #666;
		}
		.layui-tree-txt {
			color: #666;
		}
		.layui-btn{
			color: #313131;
			background-color: #2b97f2
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
							<label class="layui-form-label">资源名称</label>
							<div class="layui-input-inline">
								<input name="id" type="hidden" value="${resource.id}">
								<input id="name" name="name" placeholder="请输入资源名称" class="layui-input" lay-verify="required"
									   value="${resource.name}"/>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<div class="layui-inline">
							<label class="layui-form-label">资源路径</label>
							<div class="layui-input-inline">
								<input id="url" name="url" placeholder="请输入资源路径" class="layui-input"
									   value="${resource.url}"/>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<div class="layui-inline">
							<label class="layui-form-label">资源类型</label>
							<div class="layui-input-inline">
								<select id="resourcetype" name="resourcetype" lay-filter="oiNameFilter" lay-search>
									<option value="">请选择</option>
									<option value="0">菜单</option>
									<option value="1">按钮</option>
									<option value="9">导航栏</option>
								</select>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<div class="layui-inline">
							<label class="layui-form-label">排序用编号</label>
							<div class="layui-input-inline">
								<input id="seq" name="seq" placeholder="" class="layui-input" lay-verify="required"
									   value="${resource.seq}" onkeyup='this.value=this.value.replace(/\D/gi,"")'/>
							</div>
						</div>

					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<div class="layui-inline">
							<label class="layui-form-label">菜单图标</label>
							<div class="layui-input-inline">
								<input id="icon" name="icon" placeholder="" class="layui-input" lay-verify="required"
									   value="${resource.icon}"/>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">上级资源</label>
						<div class="layui-input-inline">
							<div class="layui-unselect layui-form-select downpanel" id="downpl">
								<div class="layui-select-title">
									<span class="layui-input layui-unselect" id="pidname">选择资源名称</span>
									<input type="hidden" name="pid" id="pid" value="">
									<i class="layui-edge"></i>
								</div>
								<dl class="layui-anim layui-anim-upbit" id="divselect">
									<dd>
										<ul id="selectResource"></ul>
									</dd>
								</dl>
							</div>
						</div>
					</div>
				</div>

				<div class="layui-form-item">
					<div class="layui-inline" style="float: right; margin: 5px 20px;">
						<button id="userBtnSearch" lay-submit lay-filter="*" class="layui-btn icon-btn"
								style="margin-right: 30px;"><i
								class="layui-icon"></i>确认
						</button>
						<button type="reset" class="layui-btn icon-btn" style="margin-right: 10px;" id="userBtnClear"><i
								class="layui-icon"></i>关闭
						</button>
					</div>
				</div>

			</div>

		</div>
	</div>
</div>
<script>
	layui.config({
		base: '${staticPath}/static/module/'
	}).use(['form', 'tree'], function () {
		var form = layui.form;
		var tree = layui.tree;
		var $ = layui.jquery;
		$.ajax({
			type: "post",
			url: '${path }/admin/resource/tree',
			dataType: 'json',
			success: function (d) {
				tree.render({
					elem: '#selectResource'
					, data: d
					, showCheckbox: false  //是否显示复选框
					, showLine: true
					, id: 'resourcetree'
					, isJump: true //是否允许点击节点时弹出新窗口跳转
					, click: function (obj) {
						var data = obj.data;  //获取当前点击的节点数据
						$('#pid').val(data.id);
						$('#pidname').html(data.title);
						$('#downpl').toggleClass("layui-form-selected");
					}
				});
			}
		});
		$(".downpanel").on("click", ".layui-select-title", function (e) {
			$(".layui-form-select").not($(this).parents(".layui-form-select")).removeClass("layui-form-selected");
			$(this).parents(".downpanel").toggleClass("layui-form-selected");
			layui.stope(e);
		}).on("click", "dl i", function (e) {
			layui.stope(e);
		});
		$("#resourcetype").val('${resource.resourcetype}');
		var $select = $($(this)[0].elem).parents(".layui-form-select");

		document.getElementById("pidname").innerText='${resource.pidname}';
		$("#pid").val('${resource.pid}');
		form.render();

		form.on('submit(*)', function (data) {

			//data.field 当前容器的全部表单字段，名值对形式：{name: value}
			$.ajax({
				type: "post",
				url: '${path}/admin/resource/${oper}',
				data: data.field,
				dataType: 'json',
				success: function (data) {
					layer.msg('编辑成功', {icon: 1});
					var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					setTimeout(function () {
						parent.layer.close(index); //再执行关闭
					}, 1000);
				}
			});
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});

		$('#userBtnClear').click(function () {
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index); //再执行关闭
		})
	})
</script>
</body>
</html>