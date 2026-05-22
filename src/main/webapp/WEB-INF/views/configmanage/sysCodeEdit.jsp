<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <%--<%@ include file="/commons/basejs.jsp"%>--%>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <%--    <meta http-equiv="X-UA-Compatible" content="edge"/>--%>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>数字字典编辑</title>
    <style>
    </style>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-body">
            <div class="layui-form toolbar" id="queryFormItem">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <input id="id" name="id" type="hidden" lay-verify="required" value="${syscode.id}">
                        <div class="layui-inline">
                            <label class="layui-form-label">代码类别</label>
<%--                            <div class="layui-input-inline">--%>
<%--                                <input id="oi_name" name="oi_name" placeholder="请输入代码类别" class="layui-input" lay-verify="required"--%>
<%--                                       value="${syscode.oi_name}"/>--%>
<%--                            </div>--%>
                            <div class="layui-input-inline">
                                <select id="oi_name" name="oi_name" lay-filter="oiNameFilter" lay-search>
                                    <option value="">请选择</option>
                                </select>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">

                        <div class="layui-inline">
                            <label class="layui-form-label">代码名称</label>
                            <div class="layui-input-inline">
                                <input id="oi_value" name="oi_value" placeholder="请输入代码名称" class="layui-input" lay-verify="required"
                                       value="${syscode.oi_value}"/>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">

                        <div class="layui-inline">
                            <label class="layui-form-label">代码值</label>
                            <div class="layui-input-inline">
                                <input id="oi_code" name="oi_code" placeholder="请输入代码值" class="layui-input" lay-verify="required"
                                       value="${syscode.oi_code}"/>
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
                                       value="${syscode.seq}" onkeyup='this.value=this.value.replace(/\D/gi,"")'/>
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
    }).use(['form'], function () {
        var form = layui.form;
        var $ = layui.jquery;

        form.on('submit(*)', function (data) {

            //data.field 当前容器的全部表单字段，名值对形式：{name: value}
            $.ajax({
                type: "post",
                url: '${path}/admin/syscode/edit',
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

        $('#userBtnClear').click(function () {
            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
            parent.layer.close(index); //再执行关闭
        })

        // 代码类别
        $.ajax({
            type: "post",
            url: '${path}/admin/syscode/treeType',
            dataType: 'json',
            success: function (d) {
                $('#oi_name').empty();

                if (d != undefined) {
                    var childArr = d[0].children;

                    var childHtml = "<option value='' selected='selected'>选择代码类别</option><option  value='"+ d[0].code +"'>" + d[0].name + "</option>";

                    for (var i = 0; i < childArr.length; i++) {
                        // if (i == 0) {
                        //     childHtml += "<div class='childType active' data-id='" + childArr[i].code + "'>" + childArr[i].name + "</div>";
                        // } else {
                        //     childHtml += "<div class='childType' data-id='" + childArr[i].code + "'>" + childArr[i].name + "</div>";
                        // }
                        childHtml += "<option value='" + childArr[i].code + "'>" + childArr[i].name + "</option>";
                    }

                    $('#oi_name').append(childHtml);
                    $('select').val('${syscode.oi_name}');
                    form.render('select');
                }
            }
        });
    })
</script>
</body>
</html>