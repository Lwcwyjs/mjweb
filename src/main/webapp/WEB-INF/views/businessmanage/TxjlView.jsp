<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <link rel="stylesheet" href="${staticPath }/static/viewer/viewer.min.css">
    <script src="${staticPath}/static/viewer/viewer-jquery.min.js"></script>
    <title>通行记录详情</title>
</head>
<style>
    td {
        width: 25%;
        height: 50px;
        text-align: center;
    }

    .main-picsrc {
        width: 46.9%;
        height: 100%;
    }

    th {
        height: 50px;
    }

    table {
        position: relative;
    }

    .photoName {
        width: 100%;
        position: relative;
        bottom: 0px;
        background-color: #2F4056;
        text-align: center;
    }

    .photoDiv {
        width: 24%;
        height: 200px;
        background-color: cornflowerblue;
        margin-right: 1%;
        margin-bottom: 2%;
        white-space: nowrap !important;
        float: left;
        color: #FFFFFF;
    }

    .rol {
        width: 100%;
        height: 100%;
    }

    .banner {
        width: 100%;
        height: 100%;
        white-space: nowrap;
        padding-bottom: 10px;
    }

    .one {
        position: relative;
        width: 305px;
        height: 200px;
        top: 10px;
        left: 30px;
        float: left;
        border-style: solid;
        border-width: 2px;
        border-radius: 10px;
        border-color: #2B97F2;
        margin-right: 30px;
        margin-top: 20px;
    }

    .one img {
        position: absolute;
        height: 200px;
        width: 304px;
    }

    .text {
        position: absolute;
        font-size: 18px;
        color: #FFF;
        top: 83%;
        left: 5%;
    }

    .zol {
        width: 100%;
        height: 27%;
        position: absolute;
        top: 37%;
        left: 0;
    }

    /*.videos {
        display: none;
        position: fixed;
        left: 50%;
        top: 30%;
        z-index: 100;
        transform: translate(-50%, -50%);
        height: 92%;
        width: 500px;
    }*/

    .vclose {
        position: absolute;
        right: 1%;
        top: 1%;
        border-radius: 100%;
        width: 25px !important;
        height: 25px !important;
    }

    .right-top {
        position: absolute;
        z-index: 1;
        float: right;
        right: 5px;
        top: 10px
    }

    .bottom-left {
        position: absolute;
        z-index: 1;
        float: left;
        width: 100%;
        background: rgb(43, 151, 242);
        color: white;
        bottom: 0;
        text-align: center;
        width: 100%;
        height: 10%;
    }

    .middle {
        position: absolute;
        left: 50%;
        top: 55%;
        transform: translate(-50%, -50%);
    }
</style>
<body>
<div style="width: 100%;height: 95%; position: absolute;">
    <div style="position: relative; float: left;width: 47.5%;height: 100%;left: 1%;top: 2%;">
        <div style="background:cornflowerblue; color:#FFF;text-align: center;">通行信息</div>
        <table border="1" width="100%" align="center" cellspacing="0" cellpadding="0;">
            <tr>
                <td>流水号</td>
                <td>${mjDataBase.lsh}</td>
                <td>企业名称</td>
                <td>${mjDataBase.qybh}</td>
            </tr>
            <tr>
                <td>车牌号码</td>
                <td>${mjDataBase.cphm}</td>
                <td>车牌颜色</td>
                <td>${mjDataBase.cpys}</td>
            </tr>
            <tr>
                <td>排放标准</td>
                <td>${mjDataBase.pfbz}</td>
                <td>燃料种类</td>
                <td>${mjDataBase.rlzl}</td>
            </tr>
            <tr>
                <td>出入口编号</td>
                <td>${mjDataBase.crkbh}</td>
                <td>摄像头编号</td>
                <td>${mjDataBase.sxtbh}</td>
            </tr>
            <tr>
                <td>道闸编号</td>
                <td>${mjDataBase.dzbh}</td>
                <td>进出类型</td>
                <td>${mjDataBase.jclx}</td>
            </tr>
            <tr>
                <td>摆杆状态</td>
                <td>${mjDataBase.bgzt}</td>
                <td>管控结果</td>
                <td>${mjDataBase.gkjg}</td>
            </tr>
            <tr>
                <td>通过开始时间</td>
                <td><fmt:formatDate value="${mjDataBase.tgkssj}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                <td>通过结束时间</td>
                <td><fmt:formatDate value="${mjDataBase.tgjssj}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
            </tr>
            <tr>
                <td>运输货物名称</td>
                <td>${mjDataBase.yshwmc}</td>
                <td>运输货物单位</td>
                <td>${mjDataBase.ysdw}</td>
            </tr>
            <tr>
                <td>运输量</td>
                <td>${mjDataBase.ysl}</td>
            </tr>
        </table>
        <div style="background:cornflowerblue; color:#FFF;text-align: center;">视频回放</div>
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
        <button id="btPlay" class="layui-btn icon-btn"><i
                class="layui-icon"></i>播放
        </button>
    </div>
    <div style="position: relative; float: right;width: 47.5%;height: 100%;top: 2%;overflow-y: auto;right: 1%">
        <div style="background:cornflowerblue; color:#FFF;text-align: center;">通过过程照片</div>
        <div id="PicList" style="width: 100%;height: 100%">
        </div>
    </div>
</div>
<script type="text/javascript" src="${staticPath}/static/js/colmunFormat.js" charset="utf-8"></script>
<script type="text/javascript" src="${staticPath}/static/js/video.min.js" charset="utf-8"></script>
<link rel="stylesheet" href="${staticPath}/static/css/video-js.css"/>
<script>
    $(function () {
        var cszp = "${mjDataBase.cszp}";
        var ctzp = "${mjDataBase.ctzp}";
        var cpzp = "${mjDataBase.cpzp}";
        var imageStr = '';
        if (cszp != "") {
            imageStr = imageStr + '<div class = "photoDiv"><div class = "carPhoto" style="width: 100%;height: 88%"><img src = "'
                + cszp + '" style="width: 100%;height: 100%" onclick="showpic(1)"/></div><div class = "photoName" >车身照片</div></div>';
        }
        if (ctzp != "") {
            imageStr = imageStr + '<div class = "photoDiv"><div class = "carPhoto" style="width: 100%;height: 88%"><img src = "'
                + ctzp + '" style="width: 100%;height: 100%" onclick="showpic(1)"/></div><div class = "photoName" >车头照片</div></div>';
        }
        if (cpzp != "") {
            imageStr = imageStr + '<div class = "photoDiv"><div class = "carPhoto" style="width: 100%;height: 88%"><img src = "'
                + cpzp + '" style="width: 100%;height: 100%" onclick="showpic(1)"/></div><div class = "photoName" >车牌照片</div></div>';
        }
        $("#PicList").html(imageStr);
        var vlc = document.getElementById('vlc');
        $('#vlcplayer').hide();
        var videortsp = "${videortsp}";
        console.log(videortsp);
        var id = "${mjDataBase.id}";

        $('#btPlay').click(function () {
            if (videortsp != "") {
                $.ajax({
                    type: "post",
                    url: '${path}/business/txjl/playvlc',
                    data: {"videortsp": videortsp, "id": id},
                    dataType: 'json',
                    success: function (data) {
                        console.log(data);
                        var m3u8Url = data.m3u8Url;
                        $('#vlcplayer').show();
                        playvlc(m3u8Url);
                    }
                });
            }
        });

    });

    function showpic(id) {
        $("#PicList").viewer({imgsrc: "data-original"});
    }

    function close1() {
        $('.banner').css("display", "none");//点击关闭按钮关闭暂停视频
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
</script>
</body>
</html>
