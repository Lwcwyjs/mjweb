<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <%--<%@ include file="/commons/basejs.jsp"%>--%>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <%--    <meta http-equiv="X-UA-Compatible" content="edge"/>--%>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>数字字典添加</title>
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

                        <div class="layui-inline">
                            <label class="layui-form-label">代码类别</label>
                            <%--<div class="layui-input-inline">
                                <input id="oi_name" name="oi_name" placeholder="请输入代码类别" class="layui-input"
                                       lay-verify="required"/>
                            </div>--%>
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
                                <input id="oi_value" name="oi_value" placeholder="请输入代码名称" class="layui-input"
                                       lay-verify="required"/>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">

                        <div class="layui-inline">
                            <label class="layui-form-label">代码值</label>
                            <div class="layui-input-inline">
                                <input id="oi_code" name="oi_code" placeholder="请输入代码值" class="layui-input"
                                       lay-verify="required"/>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">

                        <div class="layui-inline">
                            <label class="layui-form-label">排序用编号</label>
                            <div class="layui-input-inline">
                                <input id="seq" name="seq" placeholder="请输入代码名称" class="layui-input"
                                       lay-verify="required"
                                       value="${syscode.seq}" onkeyup='this.value=this.value.replace(/\D/gi,"")'/>
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
                url: '${path}/admin/syscode/add',
                data: data.field,
                dataType: 'json',
                success: function (data) {
                    // console.log(data);
                    layer.msg('提交成功', {icon: 1});
                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    setTimeout(function () {
                        parent.layer.close(index); //再执行关闭
                    }, 1000);
                }
            });
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });

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
                    form.render('select');
                }
            }
        });

    });

    // 清空按钮点击事件
    $('#userBtnClear').click(function () {
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });
</script>
</body>
</html>