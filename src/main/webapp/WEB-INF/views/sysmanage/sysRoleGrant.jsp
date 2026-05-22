<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <%--<%@ include file="/commons/basejs.jsp"%>--%>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <%--    <meta http-equiv="X-UA-Compatible" content="edge"/>--%>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>访问授权</title>
    <style>
        .layui-tree li i {
            color: white;
        }

        .layui-tree-txt {
            color: #313131;
        }

        .layui-tree-icon {
            color: #f9f8ff;
        }

        .layui-icon layui-icon-addition {
            color: #f9f8ff;
        }

        .layui-tree-icon .layui-icon {
            color: white;
        }
    </style>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-body">
            <div class="layui-row">
                <div class="layui-col-md10" id="grantlist" align="left">
                </div>
                <div class="layui-col-md2" align="left">
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-inline" style="float: right; margin: 5px 20px;">
                    <button id="userBtnConfirm" lay-submit lay-filter="*" class="layui-btn icon-btn"
                            style="margin-right: 30px;"><i
                            class="layui-icon"></i>确认
                    </button>
                    <button type="reset" class="layui-btn icon-btn" style="margin-right: 10px;" id="userBtnClose"><i
                            class="layui-icon"></i>关闭
                    </button>
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
        $.ajax({
            type: "post",
            url: '${path }/admin/resource/allTrees/${pid}',
            spread: true,
            dataType: 'json',
            success: function (data) {
                tree.render({
                    elem: '#grantlist'
                    , data: data
                    , showCheckbox: true  //是否显示复选框
                    , id: 'roletree'
                    , isJump: true //是否允许点击节点时弹出新窗口跳转
                    , click: function (obj) {
                        var data = obj.data;  //获取当前点击的节点数据
                    }
                });
                $.ajax({
                    type: "post",
                    url: '${path }/admin/role/findResourceIdListByRoleId/${id}',
                    data: data.field,
                    dataType: 'json',
                    success: function (result) {
                        console.log(result);
                        if ((result.success) && (result.obj != undefined)) {
                            tree.setChecked('roletree', result.obj); //勾选指定节点
                            tree.render();
                        }
                    }
                })
            }
        })
        $('#userBtnConfirm').click(function () {
            var resourceIds = tree.getChecked('roletree');
            var ids = [];
            if (resourceIds && resourceIds.length > 0) {
                for (var i = 0; i < resourceIds.length; i++) {
                    if (resourceIds[i].id != -1) {
                        ids.push(resourceIds[i].id);
                    }
                    var childrens1 = resourceIds[i].children;
                    if (childrens1 && childrens1.length > 0) {
                        for (var j = 0; j < childrens1.length; j++) {
                            if (childrens1[j].id != -1) {
                                ids.push(childrens1[j].id);
                            }
                            var childrens2 = childrens1[j].children;
                            if (childrens2 && childrens2.length > 0) {
                                for (var m = 0; m < childrens2.length; m++) {
                                    if (childrens2[m].id != -1) {
                                        ids.push(childrens2[m].id);
                                    }
                                    var childrens3 = childrens2[m].children;
                                    if (childrens3 && childrens3.length > 0) {
                                        for (var n = 0; n < childrens3.length; n++) {
                                            if (childrens3[n].id != -1) {
                                                ids.push(childrens3[n].id);
                                            }
                                            var childrens4 = childrens3[n].children;
                                            if (childrens4 && childrens4.length > 0) {
                                                for (var o = 0; o < childrens4.length; o++) {
                                                    if (childrens4[o].id != -1) {
                                                        ids.push(childrens4[o].id);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            $.ajax({
                type: "post",
                url: '${path }/admin/role/grant/${id}/'+ids,
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

        $('#userBtnClose').click(function () {
            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
            parent.layer.close(index); //再执行关闭
        })
    })
</script>
</body>
</html>