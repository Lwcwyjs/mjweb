<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ include file="/commons/global.jsp" %>
<html>

<head>
        <title>${title}</title>
<%--    <title>template</title>--%>
    <style>
        .layui-nav div{
            font-size: 12px;
        }
    </style>
</head>

<body class="layui-layout-body">
<%@ include file="/commons/baseloginjs.jsp" %>
<%--<%@ include file="/commons/basejs.jsp" %>--%>
<div class="layui-layout layui-layout-admin">
    <!-- 头部 -->
    <div class="layui-header">
        <div class="layui-logo">
            <a href="#">
                <img src="${staticPath}/static/assets/images/logo.png"/>
                <cite>深圳市车驾管业务综合音视频监管系统</cite>
            </a>
        </div>
        <%--        <ul class="layui-nav layui-layout-left">--%>
        <%--            <li class="layui-nav-item" lay-unselect>--%>
        <%--                <a ew-event="flexible" title="侧边伸缩"><i class="layui-icon layui-icon-shrink-right"></i></a>--%>
        <%--            </li>--%>
        <%--            &lt;%&ndash;            <li class="layui-nav-item" lay-unselect>&ndash;%&gt;--%>
        <%--            &lt;%&ndash;                <a ew-event="refresh" title="刷新"><i class="layui-icon layui-icon-refresh-3"></i></a>&ndash;%&gt;--%>
        <%--            &lt;%&ndash;            </li>&ndash;%&gt;--%>
        <%--        </ul>--%>
        <ul class="layui-nav layui-layout-left" style="left: 600px">
            <li class="layui-nav-item"><a href="">综合业务</a></li>
            <li class="layui-nav-item"><a href="">机动车查验</a></li>
            <li class="layui-nav-item"><a href="">驾考人考试</a></li>
            <li class="layui-nav-item"><a href="">机动车检验</a></li>
            <li class="layui-nav-item">
                <a href="javascript:;">系统管理</a>
            </li>
        </ul>
        <ul class="layui-nav layui-layout-right">

            <div style="font-size: 12px; line-height: 80px">
                <li class="layui-nav-item layui-hide-xs" lay-unselect style="margin-right: 0px; ">
                    <span><i class="layui-icon menu icon-manico"></i> <cite style="font-style: normal">${username}</cite> </span>
                </li>
                <li class="layui-nav-item layui-hide-xs" lay-unselect style="margin-right: 0px;">
                    <a ew-event="updatepwd" title="密码修改"><i class="layui-icon menu icon-updatepwd"></i> <cite>密码修改</cite> </a>
                </li>
                <li class="layui-nav-item layui-hide-xs" lay-unselect style="margin-right: 0px;">
                    <a ew-event="logout" title="注销"><i class="layui-icon menu icon-logoutico"></i> <cite>注销</cite> </a>
                </li>
            </div>

            <%--            <li class="layui-nav-item" lay-unselect>--%>
            <%--                <a>--%>
            <%--                    <img src="${staticPath}/static/assets/images/head.png" class="layui-nav-img">--%>
            <%--                    &lt;%&ndash;                    <cite>${userinfo.organization.name}-${userinfo.user.name}</cite>&ndash;%&gt;--%>
            <%--                    <cite>${userinfo.user.name}</cite>--%>
            <%--                    &lt;%&ndash;                    <cite>${userinfo.getUser().getName()}</cite>&ndash;%&gt;--%>
            <%--                </a>--%>
            <%--                <dl class="layui-nav-child">--%>
            <%--                    <dd lay-unselect>--%>
            <%--                        <a id="setInfo">个人信息</a>--%>
            <%--                    </dd>--%>
            <%--                    <dd lay-unselect>--%>
            <%--                        <a id="setPsw">修改密码</a>--%>
            <%--                    </dd>--%>
            <%--                    <hr>--%>
            <%--                    <dd lay-unselect>--%>
            <%--                        <a id="btnLogout">退出</a>--%>
            <%--                    </dd>--%>
            <%--                </dl>--%>
            <%--            </li>--%>
        </ul>
    </div>

    <!-- 侧边栏 -->
    <div class="layui-side">
        <%--        <%@ include file="/commons/side.jsp"%>--%>
        <div class="layui-side-scroll">
            <ul class="layui-nav layui-nav-tree" lay-filter="admin-side-nav" lay-accordion="true"
                style="margin-top: 15px;">
                <li class="layui-nav-item layui-nav-itemed">
                    <a href="javascript:"><i
                            class="layui-icon menu icon-homeico" ></i>&emsp;<cite>首页</cite></a>
                    <dl class="layui-nav-child">
                        <dd class="layui-this">
                            <a lay-href="/admin/console"><i class="layui-icon subMenu icon-nosettingico"></i>&emsp;<cite class="subMenu">首页</cite></a>
                        </dd>
                    </dl>
                </li>

                <c:forEach var="menu" items="${menus}">
                    <li class="layui-nav-item">
                        <c:if test="${not empty menu.attributes}">
                            <a lay-href="${not empty menu.attributes}"><i
                                    class="layui-icon layui-icon-home menu ${menu.iconCls}"></i>&emsp;<cite>${menu.text}</cite></a>
                        </c:if>
                        <c:if test="${empty menu.attributes}">
                            <a lay-href="javascript:;"><i
                                    class="layui-icon menu ${menu.iconCls}"></i>&emsp;<cite>${menu.text}</cite></a>
                        </c:if>
                        <c:if test="${not empty menu.children}">
                            <dl class="layui-nav-child">
                                <c:forEach var="item" items="${menu.children}">
                                    <dd>
                                    <%--                                    <a lay-href="${ item.attributes }"><i--%>
                                    <%--                                            class="layui-icon ${item.iconCls}"></i>&emsp;<cite>${item.text}</cite></a>--%>
                                    <a lay-href="${ item.attributes }"><i
                                            class="layui-icon icon-nosettingico subMenu"></i>&emsp;<cite class="subMenu">${item.text}</cite></a>

                                    <c:forEach var="subItem" items="${item.children}">
                                        <dd>
                                            <a href="${ subItem.attributes }">${ subItem.text }</a>
                                        </dd>
                                    </c:forEach>
                                    </dd>
                                </c:forEach>
                            </dl>
                        </c:if>
                    </li>
                </c:forEach>

            </ul>
        </div>
    </div>

    <!-- 主体部分 -->
    <div class="layui-body">
        <div class="layui-tab" lay-allowClose="true" lay-filter="admin-pagetabs">
            <ul class="layui-tab-title">
            </ul>
            <div class="layui-tab-content">
            </div>
        </div>
        <%--        <div class="layui-icon admin-tabs-control layui-icon-prev" ew-event="leftPage"></div>--%>
        <%--        <div class="layui-icon admin-tabs-control layui-icon-next" ew-event="rightPage"></div>--%>
        <%--        <div class="layui-icon admin-tabs-control layui-icon-down">--%>
        <%--            <ul class="layui-nav admin-tabs-select" lay-filter="admin-pagetabs-nav">--%>
        <%--                <li class="layui-nav-item" lay-unselect>--%>
        <%--                    <a href="javascript:"></a>--%>
        <%--                    <dl class="layui-nav-child layui-anim-fadein">--%>
        <%--                        <dd ew-event="closeThisTabs" lay-unselect><a href="javascript:">关闭当前标签页</a></dd>--%>
        <%--                        <dd ew-event="closeOtherTabs" lay-unselect><a href="javascript:">关闭其它标签页</a></dd>--%>
        <%--                        <dd ew-event="closeAllTabs" lay-unselect><a href="javascript:">关闭全部标签页</a></dd>--%>
        <%--                    </dl>--%>
        <%--                </li>--%>
        <%--            </ul>--%>
        <%--        </div>--%>
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
    layui.use(['layer', 'element', 'index'], function () {
        var $ = layui.jquery;
        var layer = layui.layer;
        var index = layui.index;


        index.loadSetting();  // 加载本地缓存的设置属性
        index.loadMenu();
        // 默认加载主页
        index.loadHome({
            menuPath: 'console',
            menuName: '<i class="layui-icon layui-icon-home"></i>'
        });

    });
</script>
</body>

</html>