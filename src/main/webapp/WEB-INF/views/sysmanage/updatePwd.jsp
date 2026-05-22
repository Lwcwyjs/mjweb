<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ include file="/commons/global.jsp" %>
<html>

<head>
    <title>${title}</title>
    <style>
        .pw-strength {
            clear: both;
            position: relative;
            top: 8px;
            width: 180px;
        }

        .pw-bar {
            background: url("${staticPath }/static/pwd/images/pwd-1.png") no-repeat;
            height: 14px;
            overflow: hidden;
            width: 179px;
        }

        .pw-bar-on {
            background: url("${staticPath }/static/pwd/images/pwd-2.png") no-repeat;
            width: 0px;
            height: 14px;
            position: absolute;
            top: 1px;
            left: 2px;
            transition: width .5s ease-in;
            -moz-transition: width .5s ease-in;
            -webkit-transition: width .5s ease-in;
            -o-transition: width .5s ease-in;
        }

        .pw-weak .pw-defule {
            width: 0px;
        }

        .pw-weak .pw-bar-on {
            width: 60px;
        }

        .pw-medium .pw-bar-on {
            width: 120px;
        }

        .pw-strong .pw-bar-on {
            width: 179px;
        }

        .pw-txt {
            padding-top: 2px;
            width: 180px;
            overflow: hidden;
        }

        .pw-txt span {
            color: #707070;
            float: left;
            font-size: 12px;
            text-align: center;
            width: 58px;
        }
    </style>
</head>
<script type="text/javascript"
        src="${staticPath }/static/pwd/passwordvalidate.js" charset="utf-8"></script>
<body class="layui-layout-body">
<%@ include file="/commons/baseloginjs.jsp" %>
<%--<%@ include file="/commons/basejs.jsp" %>--%>
<div class="layui-layout layui-layout-admin">
    <!-- 头部 -->
    <div class="layui-header">
        <div class="layui-logo">
            <a href="#">
                <img src="${staticPath}/static/assets/images/logo.png"/>
                <cite>机动车检验智能审核系统</cite>
            </a>
        </div>
        <ul class="layui-nav layui-layout-left">
            <li class="layui-nav-item" lay-unselect>
                <a ew-event="flexible" title="侧边伸缩"><i class="layui-icon layui-icon-shrink-right"></i></a>
            </li>
            <%--            <li class="layui-nav-item" lay-unselect>--%>
            <%--                <a ew-event="refresh" title="刷新"><i class="layui-icon layui-icon-refresh-3"></i></a>--%>
            <%--            </li>--%>
        </ul>
        <ul class="layui-nav layui-layout-right">
            <%--            <li class="layui-nav-item" lay-unselect>--%>
            <%--                <a id="btnMessage" title="消息"><i class="layui-icon layui-icon-notice"></i></a>--%>
            <%--            </li>--%>
            <%--            <li class="layui-nav-item layui-hide-xs" lay-unselect>--%>
            <%--                <a ew-event="fullScreen" title="全屏"><i class="layui-icon layui-icon-screen-full"></i></a>--%>
            <%--            </li>--%>
            <%--            <li class="layui-nav-item layui-hide-xs" lay-unselect>--%>
            <%--                <a ew-event="Setting" title="设置"><i class="layui-icon layui-icon-set"></i> <cite>设置</cite> </a>--%>
            <%--            </li>--%>
            <%--            <li class="layui-nav-item layui-hide-xs" lay-unselect>--%>
            <%--                <a ew-event="Help" title="帮助"><i class="layui-icon layui-icon-help"></i> <cite>帮助</cite> </a>--%>
            <%--            </li>--%>
            <%--            <li class="layui-nav-item layui-hide-xs" lay-unselect>--%>
            <%--                <a ew-event="logout" title="注销"><i class="layui-icon layui-icon-close"></i> <cite>注销</cite> </a>--%>
            <%--            </li>--%>
            <li class="layui-nav-item layui-hide-xs" lay-unselect style="margin-right: 10px;">
                <span>欢迎 ${username}</span>
            </li>
            <li class="layui-nav-item layui-hide-xs" lay-unselect style="margin-right: 10px;">
                <a ew-event="updatepwd" title="密码修改"><i class="layui-icon menu icon-pencilico"></i> <cite>密码修改</cite>
                </a>
            </li>
            <li class="layui-nav-item layui-hide-xs" lay-unselect style="margin-right: 10px;">
                <a ew-event="logout" title="注销"><i class="layui-icon menu icon-logoutico"></i> <cite>注销</cite> </a>
            </li>
        </ul>
    </div>

    <!-- 侧边栏 -->
    <div class="layui-side">
        <%--        <%@ include file="/commons/side.jsp"%>--%>
        <div class="layui-side-scroll">
            <ul class="layui-nav layui-nav-tree" lay-filter="admin-side-nav" lay-accordion="true"
                style="margin-top: 15px;">

            </ul>
        </div>
    </div>

    <!-- 主体部分 -->
    <div class="layui-body">
        <div style="width: 600px;height: 340px;margin: 10% 20%">
            <div class="layui-form toolbar" id="queryFormItem">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <span style="height: 30px; font-size: 24px;">密码强度不够，请修改密码！</span>
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
                                       lay-verify="required"
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
                                style="margin-right: 60px; font-size: 18px"><i
                                class="layui-icon"></i>保存
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <!-- 底部 -->

    <!-- 手机屏幕遮罩层 -->
    <div class="site-mobile-shade"></div>
</div>

<!-- 页面加载loading -->
<div class="page-loading">
    <div class="rubik-loader"></div>
</div>


<script>
    layui.use(['form', 'layer', 'element', 'index'], function () {
        var $ = layui.jquery;
        var layer = layui.layer;
        var index = layui.index;
        var form = layui.form;


        index.loadSetting();  // 加载本地缓存的设置属性
        form.on('submit(*)', function (data) {
            var field = data.field;
            var oldpwd = field.oldPwd;
            var pwd = field.pwd;
            var repwd = field.repwd;
            if (pwd != repwd) {
                layer.msg('两次密码不一致', {icon: 2});
                return;
            }
            if (!getPasswordStrong(pwd)) {
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

                        layer.msg('修改成功，请重新登录', {icon: 1});
                        setTimeout(function () {
                            window.location.href = '${path }';
                        }, 1000);

                    } else {
                        layer.msg(data.msg, {icon: 2});
                    }
                }
            });
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });
    });

</script>
</body>

</html>