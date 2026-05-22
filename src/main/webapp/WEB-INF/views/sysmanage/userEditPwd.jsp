<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript"
        src="${staticPath }/static/pwd/passwordvalidate.js" charset="utf-8"></script>
<html>
<head>
    <%--<%@ include file="/commons/basejs.jsp"%>--%>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <%--    <meta http-equiv="X-UA-Compatible" content="edge"/>--%>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>角色编辑</title>
    <style>

        .layui-tree li i {
            color: #313131;
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
        body{
            font-size: 16px;
            font-family: "Microsoft YaHei";
        }
        .pw-strength {clear: both;position: relative;top: 8px;width: 180px;}
        .pw-bar{background: url("${staticPath }/static/pwd/images/pwd-1.png") no-repeat;height: 14px;overflow: hidden;width: 179px;}
        .pw-bar-on{background:  url("${staticPath }/static/pwd/images/pwd-2.png") no-repeat; width:0px; height:14px;position: absolute;top: 1px;left: 2px;transition: width .5s ease-in;-moz-transition: width .5s ease-in;-webkit-transition: width .5s ease-in;-o-transition: width .5s ease-in;}
        .pw-weak .pw-defule{ width:0px;}
        .pw-weak .pw-bar-on {width: 60px;}
        .pw-medium .pw-bar-on {width: 120px;}
        .pw-strong .pw-bar-on {width: 179px;}
        .pw-txt {padding-top: 2px;width: 180px;overflow: hidden;}
        .pw-txt span {color: #707070;float: left;font-size: 12px;text-align: center;width: 58px;}
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
                            <label class="layui-form-label">登录名</label>
                            <div class="layui-input-inline" >
                                <b><span style="position: relative;top:8px;left:10px;font-size: 20px;color: dodgerblue"><shiro:principal></shiro:principal></span></b>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">原密码</label>
                            <div class="layui-input-inline">
                                <input id="oldPwd" name="oldPwd" placeholder="请输入原密码" class="layui-input"
                                       type="password" lay-verify="required"
                                       onkeyup="CheckIntensity(this.value,$('#levelBar0'))"/>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <div id="levelBar0">
                                <div class="pw-strength">
                                    <div class="pw-bar"></div>
                                    <div class="pw-bar-on"></div>
                                    <div class="pw-txt">
                                        <span>弱</span> <span>中</span> <span>强</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">新密码</label>
                            <div class="layui-input-inline">
                                <input id="pwd" name="pwd" placeholder="请输入新密码" class="layui-input" type="password"
                                       lay-verify="required" data-options="validType:'strongPwd'"
                                       onkeyup="CheckIntensity(this.value,$('#levelBar1'))"/>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <div id="levelBar1">
                                <div class="pw-strength">
                                    <div class="pw-bar"></div>
                                    <div class="pw-bar-on"></div>
                                    <div class="pw-txt">
                                        <span>弱</span> <span>中</span> <span>强</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">重复密码</label>
                            <div class="layui-input-inline">
                                <input id="repwd" name="repwd" placeholder="请再次输入新密码" class="layui-input"
                                       type="password" lay-verify="required"
                                       onkeyup="CheckIntensity(this.value,$('#levelBar2'))"/>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <div id="levelBar2">
                                <div class="pw-strength">
                                    <div class="pw-bar"></div>
                                    <div class="pw-bar-on"></div>
                                    <div class="pw-txt">
                                        <span>弱</span> <span>中</span> <span>强</span>
                                    </div>
                                </div>
                            </div>
                        </div>
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
</div>
<script>

    layui.config({
        base: '${staticPath}/static/module/'
    }).use(['form', 'tree'], function () {
        var form = layui.form;
        var tree = layui.tree;
        var $ = layui.jquery;


        form.on('submit(*)', function (data) {
            var field=data.field;
            var oldpwd=field.oldPwd;
            var pwd=field.pwd;
            var repwd=field.repwd;
            if (pwd!=repwd){
                layer.msg('两次密码不一致', {icon: 2});
                return;
            }
            if (!getPasswordStrong(pwd)){
                layer.msg('密码强度不够', {icon: 2});
                return;
            }

            //data.field 当前容器的全部表单字段，名值对形式：{name: value}
            $.ajax({
                type: "post",
                url: '${path}/admin/user/editUserPwd',
                data: data.field,
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        layer.msg('修改成功', {icon: 1});
                        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                        setTimeout(function () {
                            parent.layer.close(index); //再执行关闭
                            parent.location.reload();
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