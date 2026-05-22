<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<head>
<title>系统初始化配置</title>
<%@ include file="/commons/basejs.jsp"%>
<script type="text/javascript"
	src="${staticPath }/static/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript"
	src="${staticPath }/static/easyui/jquery.easyui.min.js"></script>
<meta http-equiv="X-UA-Compatible" content="edge" />
<link rel="stylesheet" type="text/css"
	href="${staticPath }/static/style/css/demo_style.css" />
<link rel="stylesheet" type="text/css"
	href="${staticPath }/static/style/css/smart_wizard.css" />
<script type="text/javascript"
	src="${staticPath }/static/js/jquery-1.4.2.min.js" charset="utf-8"></script>
<script type="text/javascript"
	src="${staticPath }/static/js/jquery.smartWizard.js" charset="utf-8"></script>
<script type="text/javascript">
	var radio = 0;

	$(function() {

		var allBox = $(":checkbox");
		allBox.click(function() {
			allBox.removeAttr("checked");
			$(this).attr("checked", "checked");
			if ($("#check").is(":checked")) {
				$("#xzts").show(); 

				alert("地标排放限值请到标准管理内排放限值模块进行编辑");
			}
			if ($("#checks").is(":checked")) {
				$("#xzts").hide(); 

			}
		});

	});
	function fun(evt) {
		radio = 1;
		$("#tishi").show(); 
		alert("非统一标准请到配置管理内系统配置管理进行编辑");
	};
	function funs(evt) {
		radio = 0;
		$("#tishi").hide(); 

	};
	$(document).ready(function() {
		$('#wizard').smartWizard({
			transitionEffect : 'fade',
			onFinish : onFinishCallback
		});

		function onFinishCallback() {
			$.post('${path}/admin/sysinit/addInfo', {
				"xtmc" : encodeURI($('#xtmc').val()),
				"cpqz" : encodeURI($('#cpqz').val()),
				"gljg" : encodeURI($('#gljg').val()),
				"lxdh" : encodeURI($('#lxdh').val()),
				"xtgly" : encodeURI($('#xtglry').val()),
				"ssxq" : encodeURI($('#ssxq').val()),
				"radio" : radio,
			}, function(result) {
				result = $.parseJSON(result);
				if (result.success) {
					alert(result.msg);
				}
				;
			}, 'JSON');
		}

	});

	$(document)
			.ready(
					function() {
						$
								.post(
										'${path }/admin/syscode/combox',
										{
											"oi_name" : "辖区",

										},
										function(result) {

											result = $.parseJSON(result);
											var bb = $(document).find("#ssxq");
											for (var i = 0; i < result.length; i++) {
												if (result[i].oi_value == '${initcontent.ssxq_value}') {
													bb
															.append("<option value='"+result[i].oi_value+"' selected='selected'>"
																	+ result[i].oi_value
																	+ "</option>");

												} else if (result[i].oi_value != '${initcontent.ssxq_value}') {
													bb
															.append("<option value='"+result[i].oi_value+"'>"
																	+ result[i].oi_value
																	+ "</option>");

												}
											}
											;
										}, 'JSON');

					});
	$(document)
			.ready(
					function() {
						$
								.post(
										'${path }/admin/organization/treelevel?organtype=&code=&pcode=${organ}&level=5',
										{

										},
										function(result) {
											result = $.parseJSON(result);
											var bb = $(document)
													.find("#xtglry");

											for (var i = 0; i < result.length; i++) {
												var a = result[i].children;
												if (result[i].id == '${initcontent.xtgly_value}') {
													bb
															.append("<option value='"+result[i].id+"' selected='selected'>"
																	+ result[i].id
																	+ "</option>");
												}
												if (result[i].id != '${initcontent.xtgly_value}') {
													bb
															.append("<option value='"+result[i].id+"' >"
																	+ result[i].id
																	+ "</option>");
												}
												var b = a.length;

												for (var i = 0; i < b; i++) {
													if (a[i].id == '${initcontent.xtgly_value}') {
														bb
																.append("<option value='"+a[i].id+"'  selected='selected'>"
																		+ a[i].id
																		+ "</option>");

													}
													if (a[i].id != '${initcontent.xtgly_value}') {
														bb
																.append("<option value='"+a[i].id+"'>"
																		+ a[i].id
																		+ "</option>");
													}
												}

											}
											;
										}, 'JSON');

					});
</script>
</head>
<body>
	<table align="center" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<!-- Smart Wizard --> <!--         <h2>机动车污染防治信息管理系统配置向导</h2>  -->
				<div id="wizard" class="swMain">
					<ul>
						<li><a href="#step-1"> <label class="stepNumber">1</label>
								<span class="stepDesc"> 第一步<br /> <small>配置管理系统信息</small>
							</span>
						</a></li>
						<li><a href="#step-2"> <label class="stepNumber">2</label>
								<span class="stepDesc"> 第二步<br /> <small>配置组织机构信息</small>
							</span>
						</a></li>
						<li><a href="#step-3"> <label class="stepNumber">3</label>
								<span class="stepDesc"> 第三步<br /> <small>排放限值配置</small>
							</span>
						</a></li>
						<li><a href="#step-4"> <label class="stepNumber">4</label>
								<span class="stepDesc"> 第四步<br /> <small>标定设置</small>
							</span>
						</a></li>
					</ul>
					<div id="step-1">
						<h2 class="StepTitle">配置管理系统信息</h2>
						<p>
							<table
								style="width: 100%; border-collapse: separate; border-spacing: 20px;">
								<tr>
									<td align="right" style="width: 20%;">管理系统名称：</td>
									<td style="width: 40%;"><input id="xtmc" name="xtmc"
										style="width: 340px; height: 25px; font-size: 20px"
										class="easyui-validatebox" value="${initcontent.xtmc_value}"
										data-options="required:true"></input></td>
									<td align="right" style="width: 8%;">车牌前缀：</td>
									<td><input id="cpqz" name="cpqz"
										style="width: 120px; height: 25px; font-size: 20px"
										class="easyui-validatebox" value="${initcontent.cpqz_value}"
										data-options="required:true"></input></td>
								</tr>

								<tr>
									<td align="right" style="width: 20%;">管理机构名称：</td>
									<td style="width: 40%;"><input id="gljg" name="gljg"
										style="width: 340px; height: 25px; font-size: 20px"
										class="easyui-validatebox" value="${initcontent.gljg_value}"
										data-options="required:true"></input></td>
									<td align="right" style="width: 8%;">联系电话：</td>
									<td><input id="lxdh" name="lxdh"
										style="width: 120px; height: 25px; font-size: 20px"
										class="easyui-validatebox" value="${initcontent.lxdh_value}"
										data-options="required:true"></input></td>
								</tr>
							</table>
						</p>
					</div>
					<div id="step-2">
						<h2 class="StepTitle">配置组织机构信息</h2>
						<p>
							<table
								style="width: 100%; border-collapse: separate; border-spacing: 20px;">
								<tr>
									<td align="right" style="width: 20%;">上级服务接口地址：</td>
									<td style="width: 58%;"><input id="sjfwjkdz"
										name="sjfwjkdz"
										style="width: 580px; height: 25px; font-size: 20px"
										class="easyui-validatebox" value="${initcontent.jkdz_value}"
										data-options="required:true"></input></td>

								</tr>

								<tr>
									<td align="right" style="width: 20%;">所属辖区：</td>
									<td><select id="ssxq" name="ssxq"
										style="width: 200px; height: 25px; font-size: 20px;"></select></td>
								</tr>
								<tr>
									<td align="right" style="width: 20%;">管理人员代码：</td>
									<td><select id="xtglry" name="xtglry"
										style="width: 200px; height: 25px; font-size: 20px;"></select></td>
								</tr>
							</table>
						</p>
					</div>
					<div id="step-3">
						<h2 class="StepTitle">排放限值配置</h2>
						<p>
							<table
								style="width: 100%; border-collapse: separate; border-spacing: 20px;">
								<tr>
									<td align="right" style="width: 30%; ">请选择排放限制类型:</td>
									<td><div
											style=" width: 58%;;line-height: 20px; max-height: 40px; overflow: hidden;">
											国标<input type="checkbox" value="1" id="checks" style="font-size: 20px"/> 地标<input id="check"
												name="check" type="checkbox" value="2" style="font-size: 20px"/>
										</div></td>
								</tr>
						<tr id="xzts" style="display:none">
									<td align="right" style="width: 30%; "><font color="#FF0000" >温馨提示:</font></td>
																	<td align="left"><font color="#FF0000" >非统一标准请到配置管理内系统配置管理进行编辑</font></td>
									
								</tr>

							</table>
						</p>

					</div>
					<div id="step-4">
						<h2 class="StepTitle">标定设置</h2>
						<p>
							<table
								style="width: 100%; border-collapse: separate; border-spacing: 20px;">
								<tr>
									<td align="right" style="width: 30%;">标定周期是否为统一版本:</td>
									<td><div
											style=" width: 58%; line-height: 20px; max-height: 40px; overflow: hidden;">
											是 <input type="radio" id="radio" name="radio" value="1"
												onclick="funs()" /> 否<input type="radio" id="radio2"
												name="radio" onclick="fun()" value="2" />
										</div></td>
								</tr>
								<tr id="tishi" style="display:none">
									<td align="right" style="width: 30%; "><font color="#FF0000" >温馨提示:</font></td>
																	<td align="left"><font color="#FF0000" >非统一标准请到配置管理内系统配置管理进行编辑</font></td>
									
								</tr>
							</table>
						</p>


					</div>
				</div> <!-- End SmartWizard Content -->

			</td>
		</tr>
	</table>

</body>
