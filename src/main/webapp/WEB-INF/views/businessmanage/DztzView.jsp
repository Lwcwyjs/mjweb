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
    <title>电子台账详情</title>
</head>
<style>
    td {
        width: 25%;
        height: 50px;
        text-align: center;
        table-layout: fixed;
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

                <td>企业名称</td>
                <td>${mjDataDztz.qybh}</td>
                <td>车辆类型</td>
                <td>${mjDataDztz.cllx}</td>
            </tr>
            <tr>
                <td>车牌号码</td>
                <td>${mjDataDztz.cphm}</td>
                <td>车牌颜色</td>
                <td>${mjDataDztz.cpys}</td>
            </tr>
            <tr>
                <td>车辆识别代号</td>
                <td>${mjDataDztz.clsbdh}</td>
                <td>初次登记日期</td>
                <td><fmt:formatDate value="${mjDataDztz.ccdjrq}" pattern="yyyy-MM-dd hh:mm:ss"></fmt:formatDate></td>
            </tr>
            <tr>
                <td>车辆品牌型号</td>
                <td>${mjDataDztz.clppxh}</td>
                <td>燃料种类</td>
                <td>${mjDataDztz.rlzl}</td>
            </tr>

            <tr>
                <td>排放标准</td>
                <td>${mjDataDztz.pfbz}</td>
                <td>联网状态</td>
                <td>${mjDataDztz.lwzt}</td>
            </tr>
            <tr>
                <td>使用性质</td>
                <td>${mjDataDztz.syxz}</td>
                <td>所有人</td>
                <td>${mjDataDztz.syr}</td>
            </tr>
            <tr>
                <td>进厂出入口编号</td>
                <td>${mjDataDztz.jccrkbh}</td>
                <td>进厂道闸编号</td>
                <td>${mjDataDztz.jcdzbh}</td>
            </tr>
            <tr>
                <td>进厂运输货物名称</td>
                <td>${mjDataDztz.jcyshwmc}</td>
                <td>进厂运输货物单位</td>
                <td>${mjDataDztz.jcysdw}</td>
            </tr>
            <tr>
                <td>进厂运输量</td>
                <td>${mjDataDztz.jcysl}</td>
                <td>出厂出入口编号</td>
                <td>${mjDataDztz.cccrkbh}</td>

            </tr>
            <tr>
                <td>出厂道闸编号</td>
                <td>${mjDataDztz.ccdzbh}</td>
                <td>出厂运输货物名称</td>
                <td>${mjDataDztz.ccyshwmc}</td>
            <tr>
                <td>出厂运输单位</td>
                <td>${mjDataDztz.ccysdw}</td>
                <td>出厂运输量</td>
                <td>${mjDataDztz.ccysl}</td>
            </tr>
            <tr>
                <td>进厂时间</td>
                <td><fmt:formatDate value="${mjDataDztz.jcsj}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                <td>出厂时间</td>
                <td><fmt:formatDate value="${mjDataDztz.ccsj}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
            </tr>
        </table>
    </div>
    <div style="position: relative; float: right;width: 47.5%;height: 100%;top: 2%;overflow-y: auto;right: 1%">
        <div style="background:cornflowerblue; color:#FFF;text-align: center;">通过过程照片</div>
        <div id="PicList" style="width: 100%;height: 100%">
        </div>
    </div>
</div>
<script>
    $(function () {
        var mjDataDztz = "${mjDataDztz}";
        var jczp = "${mjDataDztz.jczp}";
        var cczp = "${mjDataDztz.cczp}";
        var xszzp = "${mjDataDztz.xszzp}";
        var scqdzp = "${mjDataDztz.scqdzp}";
        var imageStr = '';
        if (jczp != "") {
            imageStr = imageStr + '<div class = "photoDiv"><div class = "carPhoto" style="width: 100%;height: 88%"><img src = "'
                + jczp + '" style="width: 100%;height: 100%" onclick="showpic(1)"/></div><div class = "photoName" >进厂照片</div></div>';
        }
        if (cczp != "") {
            imageStr = imageStr + '<div class = "photoDiv"><div class = "carPhoto" style="width: 100%;height: 88%"><img src = "'
                + cczp + '" style="width: 100%;height: 100%" onclick="showpic(1)"/></div><div class = "photoName" >出厂照片</div></div>';
        }
        if (xszzp != "") {
            imageStr = imageStr + '<div class = "photoDiv"><div class = "carPhoto" style="width: 100%;height: 88%"><img src = "'
                + xszzp + '" style="width: 100%;height: 100%" onclick="showpic(1)"/></div><div class = "photoName" >行驶证照片</div></div>';
        }
        if (scqdzp != "") {
            imageStr = imageStr + '<div class = "photoDiv"><div class = "carPhoto" style="width: 100%;height: 88%"><img src = "'
                + scqdzp + '" style="width: 100%;height: 100%" onclick="showpic(1)"/></div><div class = "photoName" >随车清单照片</div></div>';
        }
        $("#PicList").html(imageStr);
    });

    function showpic(id) {
        $("#PicList").viewer({imgsrc: "data-original"});
    }

    function close1() {
        $('.banner').css("display", "none");//点击关闭按钮关闭暂停视频
    }
</script>
</body>
</html>
