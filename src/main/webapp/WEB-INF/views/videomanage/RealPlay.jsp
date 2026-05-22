<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>

<html>
<head>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>实时视频</title>
    <style>
        .layui-badge-rim + .layui-badge-rim {
            margin-left: 5px;
        }

        .layui-form-label {
            width: 70px;
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

        .layui-badge-rim + .layui-badge-rim {
            margin-left: 5px;
        }

        #main a {
            color: white;
        }

        .layui-form-label {
            width: 70px;
        }

        .layui-tree li i {
            color: white;
        }

        .layui-show cite {
            color: white;
        }

        #main .childType {
            text-align: center;
            /* margin: 20px 10px; */
            background-color: #F3FAFF;
            font-size: 16px;
            line-height: 30px;
            border: solid 1px #f2f2f2;
            padding: 10px;
            cursor: pointer;
        }

        .active {
            color: #06ddf5;
        }

        .layui-layer-btn- {
            background-color: #575d68;
            /*text-align: center !important;*/
        }

        .my-skin .layui-layer-btn a {
            background-color: #188fcf;
            border: 1px solid #188fcf;
            color: white;
            margin: 0 30px 0 20px;
        }

        .layui-layer-btn1 {
            background-color: #188fcf !important;
            border: 1px solid #188fcf !important;
            color: white !important;
            margin-left: 30px !important;
            margin-right: 20px !important;
        }

        .layui-layer-content {
            background-color: #47444d;
            color: white;
        }

        .parentTypeName {
            color: #06ddf5;
            text-align: center;
            background-color: #3392FE;
            font-size: 18px;
            padding: 5px;
            margin-right: 20px;
            /*cursor: pointer;*/
        }

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
                <div class="layui-col-xs6 layui-col-sm6 layui-col-md2">
                    <div class="parentTypeName" data-code="0" style="cursor: pointer;">
                        视频列表
                    </div>
                    <div id="splist"
                         style="width: 100%; height: 70vh; margin-top: 10px; color: #313131; cursor: pointer;">
                        <!-- 这里将动态添加视频列表内容 -->
                    </div>
                </div>
                <div class="layui-col-xs6 layui-col-sm6 layui-col-md10">
                    <div style="color: #3392FE; background-color: #F3FAFF; font-size: 18px; padding: 5px;">实时预览
                    </div>
                    <%--                    <video--%>
                    <%--                            id="my-video"--%>
                    <%--                            class="video-js"--%>
                    <%--                            controls--%>
                    <%--                            preload="auto"--%>
                    <%--                            data-setup="{}"--%>
                    <%--                            style="width: 100%; height: 70vh; margin-top: 10px; position: relative; overflow: hidden;"--%>
                    <%--                    >--%>
                    <div id="video">
                        <%--                            <object classid="clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921" style="width:600px; height:400px;" id="vlcplayer" events="True">--%>
                        <%--                                <param name="MRL" value="" />--%>
                        <%--                                <param name="ShowDisplay" value="True" />--%>
                        <%--                                <param name="AutoLoop" value="False" />--%>
                        <%--                                <param name="AutoPlay" value="False" />--%>
                        <%--                                <param name="Volume" value="50" />--%>
                        <%--                                <param name="toolbar" value="true" />--%>
                        <%--                                <param name="StartTime" value="0" />--%>
                        <%--                            </object>--%>
                        <object type="application/x-vlc-plugin"
                                id="vlcplayer"
                                width="640"
                                height="480"
                                events="true"
                                classid="clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921">
                            <param name="mrl" value=""/>
                            <param name="volume" value="50"/>
                            <param name="autoplay" value="true"/>
                            <param name="loop" value="false"/>
                            <param name="fullscreen" value="true"/>
                            <embed type="application/x-vlc-plugin"
                                   pluginspage="http://www.videolan.org"
                                   width="640"
                                   height="480"
                                   id="vlc_embed"
                                   autoplay="true"
                                   loop="false"
                                   volume="50"
                                   target=""/>
                        </object>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${staticPath}/static/js/colmunFormat.js" charset="utf-8"></script>
<script type="text/javascript" src="${staticPath}/static/js/video.min.js" charset="utf-8"></script>
<link rel="stylesheet" href="${staticPath}/static/css/video-js.css"/>
<!-- 表格状态列 -->
<script>
    layui.config({
        base: '${staticPath}/static/module/'
    }).use(['form', 'table', 'tree'], function () {

        var $ = layui.jquery;
        var table = layui.table;
        var form = layui.form;
        var tree = layui.tree;
        $.ajax({
            type: "post",
            url: '${path }/admin/realplay/dzTrees/${qybh}',
            spread: true,
            dataType: 'json',
            success: function (data) {
                //console.log(data);
                tree.render({
                    elem: '#splist'
                    , data: data
                    , showCheckbox: false  //是否显示复选框
                    , id: 'roletree'
                    , isJump: true //是否允许点击节点时弹出新窗口跳转
                    , click: function (obj) {
                        // console.log(obj);
                        var data = obj.data;  //获取当前点击的节点数据
                        var id = data.id;
                        console.log(id);
                        if (id.length == 32) {
                            $.ajax({
                                type: "post",
                                url: '${path}/admin/realplay/playvlc',
                                data: {"id": id},
                                dataType: 'json',
                                success: function (data) {
                                    console.log(data);
                                    var m3u8Url = data.m3u8Url;
                                    playvlc(m3u8Url);
                                }
                            });
                        }
                    }
                });
            }
        });
    })

    function playvlc(videourl) {
        var vlc = getVLC("vlcplayer");
        vlc.playlist.stop();
        var itemId = vlc.playlist.add(videourl);
        vlc.playlist.playItem(itemId);
    }

    /**
     * 验证是否存在VLC插件
     * @returns {boolean}
     */
    function isInsalledIEVLC() {
        var vlcObj = null;
        var vlcInstalled = false;
        try {
            vlcObj = new ActiveXObject("VideoLAN.Vlcplugin.2");
            if (vlcObj != null) {
                vlcInstalled = true
            }
        } catch (e) {
            vlcInstalled = false;
        }
        return vlcInstalled;
    }

    function getVLC(name) {
        if (window.document[name]) {
            return window.document[name];
        }
        if (navigator.appName.indexOf("Microsoft Internet") == -1) {
            if (document.embeds && document.embeds[name])
                return document.embeds[name];
        } else // if (navigator.appName.indexOf("Microsoft Internet")!=-1)
        {
            return document.getElementById(name);
        }
    }

    function play(videourl) {
        var intervalId = setInterval(function () {
            var player = videojs('my-video');
            player.src({type: 'application/x-mpegURL', src: videourl});
            player.load(); // 加载新源
            // 尝试播放视频
            var playPromise = player.play();
            var retryPromise = player.play();
            if (retryPromise !== undefined) {
                retryPromise.then(function () {
                    // 播放成功后清除定时器
                    console.log('Video is playing after retry!');
                    clearInterval(intervalId);
                }).catch(function () {
                    // 播放仍然失败，继续重试
                    console.log('Retry failed, trying again in 1 second...');
                });
            }
        }, 3000); // 每秒重试
    }
</script>

</body>
</html>