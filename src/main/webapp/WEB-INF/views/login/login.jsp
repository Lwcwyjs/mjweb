<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ include file="/commons/global.jsp" %>
<head>
    <title>${title}</title>
    <%--    <%@ include file="/commons/basejs.jsp" %>--%>
    <%--    <%@ include file="/commons/basejs.jsp" %>--%>
    <%@ include file="/commons/baseloginjs.jsp" %>

    <link rel="stylesheet" href="${staticPath}/static/assets/libs/layui/css/layui.css"/>
    <link rel="stylesheet" href="${staticPath}/static/assets/css/login.css">
    <script>
        if (window != top)
            top.location.replace(location.href);
    </script>
</head>


<body>

<div class="login-wrapper">
    <div class="login-body main-body">
        <div class="login_title" style="display: flex">
            <img src="${staticPath}/static/assets/images/logo.png">
            <p style="font-family: 微软雅黑;font-size: 44px;color: #ffffff;text-align: center;">${title}</p>
        </div>

        <div class="main-card">
            <form class="layui-form layui-form-pane main-card-form">
                <div class="main-form" >
                    <div class="form-left" style="position: relative;float: left">

                    </div>
                    <div class="form-right" style="position: relative;float: right">
                        <div class="layui-form-item">
                            <%--                    <label class="layui-form-label"><i class="layui-icon layui-icon-username"></i></label>--%>
                            <div class="layui-input-block" style="display: flex">
                                <span id="username-img"></span>
                                <input id="username" name="username" type="text" lay-verify="required" placeholder="请输入用户名" class="username">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <%--                    <label class="layui-form-label"><i class="layui-icon layui-icon-password"></i></label>--%>
                            <div class="layui-input-block">
                                <input id="verifycode" id="password" name="password" type="password" lay-verify="required"
                                       placeholder="请输入密码"
                                       class=" password">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <%--                    <label class="layui-form-label"><i class="layui-icon layui-icon-vercode"></i></label>--%>
                            <div class="layui-input-block">
                                <div class="layui-row inline-block">
                                    <div class="layui-col-xs7">
                                        <input name="verifycode" style="width:204px" type="text" lay-verify="required"
                                               placeholder="请输入验证码" class="layui-input captcha">
                                        <input name="deviceId" type="hidden"/>
                                    </div>
                                    <div class="layui-col-xs5" style="padding: 0 15px;margin-top: 15px;position: relative;right: 22px;" >
                                        <img class="login-captcha" src="" id="imgcode" style="CURSOR: hand; display: none;">
                                    </div>
                                </div>
                            </div>
                        </div>


                        <div class="login-submit">
                            <button lay-filter="login-submit" class="layui-btn " lay-submit style="font-size: 25px;letter-spacing: 12px">登 录</button>
                        </div>
                    </div>
                </div>
            </form>
            <div class="login-footer img-show">
<%--                <p style="width: 812px; !important;text-align: center">技术支持 广西鑫维思信息技术有限公司 0771-4735167</p>--%>
                <p style="width: 812px !important;text-align: center">版本号：V1.0 版本日期： 20230710</p>
            </div>
        </div>
    </div>

</div>

<script type="text/javascript" src="${staticPath}/static/pwd/passwordvalidate.js" charset="utf-8"></script>
<script type="text/javascript" src="${staticPath}/static/des/deshelper.js" charset="utf-8"></script>
<script type="text/javascript" src="${staticPath}/static/des/tripledes.js" charset="utf-8"></script>
<script type="text/javascript" src="${staticPath}/static/des/mode-ecb.js" charset="utf-8"></script>
<script type="text/javascript" src="${staticPath}/static/countdown/countdown.js" charset="utf-8"></script>

<script type="text/javascript" src="${staticPath}/static/assets/libs/layui/layui.all.js"></script>

<script type="text/javascript" src="${staticPath}/static/login.js" charset="utf-8"></script>
</body>