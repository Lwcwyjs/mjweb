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
    <title>用戶信息編輯</title>
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
                <input id="id" name="id"  value="${userinfo.id}" type="hidden">
                <input id="userid" name="userid"  value="${userinfo.userid}" type="hidden">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">组织机构</label>
                        <div class="layui-input-inline">
                            <div class="layui-unselect layui-form-select downpanel"style="width:300px" id="downpl">
                                <div class="layui-select-title">
                                    <span class="layui-input layui-unselect" id="organname" style="width:300px">选择机构名称</span>
                                    <input type="hidden" name="organ" id="organ" value="">
                                    <i class="layui-edge"></i>
                                </div>
                                <dl class="layui-anim layui-anim-upbit">
                                    <dd>
                                        <ul id="selectOrgan"></ul>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">人员姓名</label>
                            <div class="layui-input-inline">
                                <input id="name" name="name" placeholder="" class="layui-input" lay-verify="required"
                                       value="${userinfo.name}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">所属角色</label>
                        <div class="layui-input-inline">
                            <div class="layui-unselect layui-form-select downpanel" id="downplrole">
                                <div class="layui-select-title">
                                    <span class="layui-input layui-unselect" id="rolename">选择角色</span>
                                    <input type="hidden" name="role_id" id="role_id" value="">
                                    <i class="layui-edge"></i>
                                </div>
                                <dl class="layui-anim layui-anim-upbit">
                                    <dd>
                                        <ul id="selectRole"></ul>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">登录名</label>
                            <div class="layui-input-inline">
                                <input id="loginname" name="loginname" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${userinfo.loginname}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">密码</label>
                            <div class="layui-input-inline">
                                <input id="password" name="password" placeholder="" class="layui-input"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">电话</label>
                            <div class="layui-input-inline">
                                <input id="phone" name="phone" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${userinfo.phone}" onkeyup='this.value=this.value.replace(/\D/gi,"")'/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">用户状态</label>
                        <div class="layui-input-inline">
                            <select id="status" name="status" lay-filter="oiNameFilter" lay-search
                                    lay-verify="required">
                                <option value="">请选择</option>
                                <option value="0">正常</option>
                                <option value="1">停用</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">账户有效期</label>
                            <div class="layui-input-inline">
                                <input type="text" name="zhyxq" id="zhyxq" lay-verify="zhyxq"
                                       placeholder=""
                                       autocomplete="off" class="layui-input" value="${yhLimitDate}">
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
        var laydate = layui.laydate;
        //日期
        laydate.render({
            elem: '#zhyxq'
        });

        laydate.render({
            elem: '#mmyxq'
        });
        $.ajax({
            type: "post",
            url: '${path}/admin/organization/orgtreelevelForOrgan?organtype=',
            dataType: 'json',
            success: function (d) {
                tree.render({
                    elem: '#selectOrgan'
                    , data: d
                    , showCheckbox: false  //是否显示复选框
                    , showLine: true
                    , id: 'organtree'
                    , isJump: true //是否允许点击节点时弹出新窗口跳转
                    , click: function (obj) {
                        var data = obj.data;  //获取当前点击的节点数据
                        $('#organ').val(data.id);
                        $('#organname').html(data.title);
                        $('#downpl').toggleClass("layui-form-selected");
                    }
                });
            }
        });
        $.ajax({
            type: "post",
            url: '${path }/admin/role/tree',
            dataType: 'json',
            success: function (d) {
                tree.render({
                    elem: '#selectRole'
                    , data: d
                    , showCheckbox: false  //是否显示复选框
                    , showLine: true
                    , id: 'roletree'
                    , isJump: true //是否允许点击节点时弹出新窗口跳转
                    , click: function (obj) {
                        var data = obj.data;  //获取当前点击的节点数据
                        $('#role_id').val(data.id);
                        $('#rolename').html(data.title);
                        $('#downplrole').toggleClass("layui-form-selected");
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
        $("#organname").html('${organname}');
        $('#organ').val('${userinfo.organ}');
        $("#rolename").html('${rolename}');
        $('#role_id').val('${userinfo.role_id}');
        $('#status').val('${userinfo.status}');
        form.render();
        form.on('submit(*)', function (data) {
            //data.field 当前容器的全部表单字段，名值对形式：{name: value}
            $.ajax({
                type: "post",
                url: '${path}/admin/user/${oper}',
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
    })
</script>
</body>
</html>