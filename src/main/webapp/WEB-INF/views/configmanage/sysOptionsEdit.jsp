<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <%--<%@ include file="/commons/basejs.jsp"%>--%>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <%--    <meta http-equiv="X-UA-Compatible" content="edge"/>--%>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>系统配置编辑</title>
    <style>
        .layui-textarea {
            background-color: white;
            color: #313131;
            width: 600px;
            height: 400px;
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
                        <input id="id" name="id" type="hidden" lay-verify="required" value="${cysysoptions.id}">
                        <div class="layui-inline">
                            <label class="layui-form-label">配置类别</label>
                            <%--<div class="layui-input-inline">
                                <input id="oi_name" name="oi_name" placeholder="请输入代码类别" class="layui-input"
                                       lay-verify="required"/>
                            </div>--%>
                            <div class="layui-input-inline">
                                <select id="option_kind" name="option_kind" lay-filter="oiNameFilter" lay-search>
                                    <option value="">请选择</option>
                                </select>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">

                        <div class="layui-inline">
                            <label class="layui-form-label">配置名称</label>
                            <div class="layui-input-inline">
                                <input id="option_des" name="option_des" placeholder="请输入配置名称" class="layui-input" style="width: 600px"
                                       lay-verify="required" value="${cysysoptions.option_des}"/>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">

                        <div class="layui-inline">
                            <label class="layui-form-label">配置代码(关键字)</label>
                            <div class="layui-input-inline">
                                <input id="option_value" name="option_value" placeholder="请输入配置代码,关键字以|隔开" class="layui-input" style="width: 600px"
                                       lay-verify="required" value="${cysysoptions.option_value}"/>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">

                        <div class="layui-inline">
                            <label class="layui-form-label">参数</label>
                            <div class="layui-input-inline">
                                <textarea id="option_params"  name="option_params" class="layui-textarea" placeholder="请输入参数" lay-verify="required"
                                ></textarea>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">状态</label>
                            <%--<div class="layui-input-inline">
                                <input id="oi_name" name="oi_name" placeholder="请输入代码类别" class="layui-input"
                                       lay-verify="required"/>
                            </div>--%>
                            <div class="layui-input-inline">
                                <select id="status" name="status" lay-filter="oiNameFilter" lay-search>
                                    <option value="">请选择</option>
                                    <option value="0">停用</option>
                                    <option value="1">启用</option>
                                </select>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline" style="float: right; margin: 5px 20px;">
                        <button id="userBtnSearch" class="layui-btn icon-btn" lay-submit lay-filter="*"
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
    }).use(['form'], function () {

        var form = layui.form;


        form.on('submit(*)', function (data) {



            //data.field 当前容器的全部表单字段，名值对形式：{name: value}
            $.ajax({
                type: "post",
                url: '${path}/admin/cysysoptions/edit',
                data: data.field,
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        layer.msg('编辑成功', {icon: 1});
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

        // 代码类别
        $.ajax({
            type: "post",
            url: '${path}/admin/cysysoptions/tree',
            dataType: 'json',
            success: function (d) {
                $('#option_kind').empty();
                form.render('select');

                if (d != undefined) {
                    var childArr = d[0].children;

                    var childHtml = "<option value='' selected='selected'>选择代码类别</option><option  value='"+ d[0].code +"'>" + d[0].text + "</option>";

                    for (var i = 0; i < childArr.length; i++) {
                            childHtml += "<option value='" + childArr[i].code + "'>" + childArr[i].text + "</option>";
                    }

                    $('#option_kind').append(childHtml);
                    $('select').val('${cysysoptions.option_kind}');
                    $("#status").val('${cysysoptions.status}');
                    form.render();
                }
            }
        });

    });
    $("#option_params").val('${cysysoptions.option_params}');

    // 清空按钮点击事件
    $('#userBtnClear').click(function () {
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });
</script>
</body>
</html>