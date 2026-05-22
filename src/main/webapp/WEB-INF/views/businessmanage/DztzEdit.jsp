<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<html>
<head>
    <%--<%@ include file="/commons/basejs.jsp"%>--%>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <%--    <meta http-equiv="X-UA-Compatible" content="edge"/>--%>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>登记运输货物</title>
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
            color: #313131;
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
                <input id="id" name="id" value="${mjDataDztz.id}" type="hidden">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">进厂运输货物名称</label>
                            <div class="layui-input-inline">
                                <input id="jcyshwmc" name="jcyshwmc" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjDataDztz.jcyshwmc}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">进场运输单位</label>
                            <div class="layui-input-inline">
                                <select id="jcysdw" name="jcysdw" lay-filter="crkbhFilter" lay-verify="required"
                                        lay-search>
                                    <option value="">请选择</option>
                                    <option value="T">吨</option>
                                    <option value="L">升</option>
                                    <option value="P">包</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">进厂运输量</label>
                            <div class="layui-input-inline">
                                <input id="jcysl" name="jcysl" type="number" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjDataDztz.jcysl}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">出厂运输货物名称</label>
                            <div class="layui-input-inline">
                                <input id="ccyshwmc" name="ccyshwmc" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjDataDztz.ccyshwmc}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">出厂运输单位</label>
                            <div class="layui-input-inline">
                                <select id="ccysdw" name="ccysdw" lay-filter="crkbhFilter" lay-verify="required"
                                        lay-search>
                                    <option value="">请选择</option>
                                    <option value="T">吨</option>
                                    <option value="L">升</option>
                                    <option value="P">包</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">出厂运输量</label>
                            <div class="layui-input-inline">
                                <input id="ccysl" name="ccysl" type="number" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjDataDztz.ccysl}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">运输车队名称</label>
                            <div class="layui-input-inline">
                                <input id="syr" name="syr" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjDataDztz.syr}"/>
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
    }).use(['form', 'tree', 'laydate'], function () {
        var form = layui.form;
        var tree = layui.tree;
        var $ = layui.jquery;
        form.on('submit(*)', function (data) {
            //data.field 当前容器的全部表单字段，名值对形式：{name: value}
            $.ajax({
                type: "post",
                url: '${path}/business/dztz/edityshw',
                data: data.field,
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        layer.msg('登记成功', {icon: 1});
                        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                        setTimeout(function () {
                            parent.layer.close(index); //再执行关闭
                        }, 1000);
                    } else {
                        layer.msg(data.msg, {icon: 2});
                    }
                }
            });
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });
        $('#ccysdw').val('${mjDataDztz.ccysdw}');
        $('#jcysdw').val('${mjDataDztz.jcysdw}');
        form.render();
        $('#userBtnClear').click(function () {
            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
            parent.layer.close(index); //再执行关闭
        })
    })
</script>
</body>
</html>