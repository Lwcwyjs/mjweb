<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ include file="/commons/global.jsp" %>
<%@ include file="/commons/baseloginjs.jsp" %>
<html>

<head>
    <title>${title}</title>
    <style>
        .layui-nav div{
            font-size: 12px;
        }

        .layui-nav-tree .layui-this > a{
            background-color: white;
        }

    </style>
    <script type="text/javascript">

        layui.use(['element','layer'], function() {
            var element = layui.element,layer=layui.layer;

            /*****************加载导航开始*****************/
            var id="";

            $.post('${path }/admin/resource/treePara',{resourcetype:'9'},
                function(data) {
                    var str="";
                    var oData=eval(data);
                    $.each(oData,function(i, n) {
                        if(i==0)
                            id=n.id;
                        var open = i > 0 ? '' : ' layui-nav-itemed';
                        str += '<li class="layui-nav-item'+open+'">';
                        str += '<a href="javascript:;" class="t-menu" lay-id="'+n.id+'" lay-tips="'+n.text+'">';
                        str += '<i class="layui-icon '+n.iconCls+'"></i>';
                        str += '<cite>' + n.text + '</cite>';
                        str += '</a>';
                        var children = n.children;
                        if (children.length > 0) {
                            str += '<dl class="layui-nav-child">';
                            $.each(
                                children,
                                function(j,	c) {
                                    str += '<dd data-name="page'+j+'">';
                                    str += '<a href="#" class="c-menu" lay-id="'+c.id+'" lay-title="'+c.text+'" lay-href="'+c.attributes+'">'+ c.text + '</a>';
                                    str += '</dd>';
                                });
                            str += '</dl>';
                        }
                        str += '</li>';
                    });
                    $("#LAY-menu-group").html(str);

                    element.render("nav");

                    $(".t-menu").on("click",function(){loadMenu($(this).attr("lay-id"));});

                    loadMenu(id);
                });
            function loadMenu(id){
                var str = "";
                $.post('${path }/admin/resource/treePara',{id:id,resourcetype:'0'},
                    function(data) {
                        var tree = eval(data);
                        $.each(tree,function(i, n) {
                            var open = i > 0 ? '' : ' layui-nav-itemed';
                            str += '<li data-name="group'+i+'" class="layui-nav-item layui-bg-white'+open+'">';
                            str += '<a href="javascript:;" lay-tips="'+n.text+'" lay-direction="2">';
                            str += '<i class="layui-icon '+n.iconCls+'"></i>  ';
                            str += '<cite>' + n.text + '</cite>';
                            str += '</a>';
                            if (n.children.length > 0) {
                                str += '<dl class="layui-nav-child">';
                                $.each(
                                    n.children,
                                    function(j,	c) {
                                        str += '<dd data-name="page'+j+'">';
                                        str += '<a href="#" class="l-menu" lay-id="'+c.id+'" lay-title="'+c.text+'" lay-href="'+c.attributes+'">'+ c.text + '</a>';
                                        str += '</dd>';
                                    });
                                str += '</dl>';
                            }
                            str += '</li>';
                        });
                        $("#LAY-system-side-menu").html(str);

                        element.render("nav");

                        $(".l-menu").on("click",function(){openModule($(this));});
                    });
            }

            /*****************加载导航结束*****************/
            /*****************选项卡开始*******************/
            $("#LAY_btn_home").width(40);
            $("#LAY_app_home").height($(".layui-tab").height()-$(".layui-tab-title").height());
            function openModule(obj){
                var id=obj.attr("lay-id");
                var title=obj.attr("lay-title");
                var url=obj.attr("lay-href");
                if ($(".layui-tab-title li[lay-id='"+id+"']").length <= 0 ) {
                    element.tabAdd('page', {
                        title: title+'<i class="layui-icon layui-unselect layui-tab-close">&#x1006;</i>'
                        ,content: '<iframe src="${path }' + url + '" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>'
                        ,id: id
                    });

                    $(".layui-tab-item").height($(".layui-tab").height()-$(".layui-tab-title").height());
                }
                element.tabChange('page', id);
                $(".layui-tab-close").unbind("click").on("click",function(){
                    element.tabDelete('page',$(this).parent("li").attr("lay-id"));
                });
            }
            /*****************选项卡结束*******************/
            $('#logout').on('click', function(){
                layer.confirm('确定要退出系统吗？', function(index){
                    layer.load(2);
                    $.post('${path }/admin/logout', function(result) {
                        if (result.success) {
                            layer.closeAll('loading');
                            window.location.href = '${path }';
                        }
                    }, 'json');
                });
            });

            $("#setting").on("click",function(){openModule($(this));});
            $("#setting1").on("click",function(){openModule($(this));});
        });
    </script>
</head>

<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <!-- 头部 -->
    <div class="layui-header">
        <div class="layui-logo">
            <img src="${staticPath }/static/assets/images/logo.png"/>
            <cite>${title}</cite>
        </div>
        <!-- 头部区域（配合layui已有的水平导航） -->
        <ul id="LAY-menu-group" class="layui-nav layui-layout-left" style="left: 361px">
        </ul>
        <!--头部导航栏尾部-->
        <ul class="layui-nav layui-layout-right">

            <div style="font-size: 12px; line-height: 80px">
                <li class="layui-nav-item layui-hide-xs" lay-unselect style="margin-right: 0px;">
                    <a href="${staticPath}/static/download/vlc-3.0.4-win32.exe" title="下载播放插件"><i class="layui-icon menu layui-icon-download-circle"></i> <cite>下载播放插件</cite> </a>
                </li>
                <li class="layui-nav-item layui-hide-xs" lay-unselect style="margin-right: 0px; ">
                    <span style="padding: 0 15px 0 0;"><i class="layui-icon menu icon-manico"></i> <cite style="font-style: normal">${username}</cite> </span>
                </li>
                <li class="layui-nav-item layui-hide-xs" lay-unselect style="margin: 0px;padding: 0">
                    <a ew-event="updatepwd" title="密码修改" style="padding: 0 15px 0 0"><i class="layui-icon menu icon-updatepwd"></i> <cite>密码修改</cite> </a>
                </li>
                <li class="layui-nav-item layui-hide-xs" lay-unselect style="margin: 0px;padding: 0;">
                    <a ew-event="logout" title="注销" style="padding: 0 15px 0 0"><i class="layui-icon menu icon-logoutico"></i> <cite >注销</cite> </a>
                </li>
            </div>
        </ul>
    </div>

    <!-- 侧边栏 -->
    <div class="layui-side">
        <%--        <%@ include file="/commons/side.jsp"%>--%>
        <div class="layui-side-scroll">
            <p style="background-color: #2B95D1;height: 3px;"></p>
            <ul id="LAY-system-side-menu" class="layui-nav layui-nav-tree"
                lay-shrink="all" lay-filter="layadmin-system-side-menu">
            </ul>
        </div>
    </div>

    <!-- 主体部分 -->
    <div class="layui-body" id="LAY_app_body" style="overflow:hidden;">
        <div class="layui-tab" lay-filter="page">
            <ul class="layui-tab-title">
                <li id="LAY_btn_home" class="layui-this">
                    <i class="layui-icon icon-home">&#xe68e;</i>
                </li>
            </ul>
            <div class="layui-tab-content">
                <div class="layui-tab-item layui-show" id="LAY_app_home">
                    <iframe src="${path }/admin/console" frameborder="0" style="border:0;width:100%;height:99.5%;"></iframe>

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
</body>

</html>