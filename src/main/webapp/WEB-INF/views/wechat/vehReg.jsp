<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<head>
    <meta charset="UTF-8">
    <title>运输车辆注册</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${staticPath}/static/css/jquery.mCustomScrollbar.min.css"/>
    <script src="${staticPath}/static/lib/jquery-2.1.4.js"></script>
    <link rel="stylesheet" href="${staticPath}/static/css/jquery.emoji.css"/>
    <script src="${staticPath}/static/js/jquery.mCustomScrollbar.min.js"></script>
    <script src="${staticPath}/static/js/jquery.emoji.js"></script>
    <script src="${staticPath}/static/lib/fastclick.js"></script>
    <script src="${staticPath}/static/js/jquery-weui.js"></script>
    <script src="${staticPath}/static/js/swiper.js"></script>
    <link rel="stylesheet" href="${staticPath}/static/lib/weui.min.css">
    <link rel="stylesheet" href="${staticPath}/static/css/jquery-weui.css">
    <link rel="stylesheet" href="${staticPath}/static/css/style.css">
    <link rel="stylesheet" href="http://res.wx.qq.com/open/libs/weui/0.4.3/weui.min.css"/>
    <link href="${staticPath}/static/layui/css/layui.css"
          type="text/css" rel="stylesheet">
    <link href="${staticPath}/static/css/main.css?r=Math.floor(Math.random() * 10000) + 1"
          rel="stylesheet" type="text/css">
    <script src="${staticPath}/static/js/common.js"></script>
    <script src="${staticPath}/static/Scripts/iscroll.js"
            rel="script" type="text/javascript"></script>
    <script type="text/javascript" src="${staticPath}/static/js/jweixin-1.6.0.js"></script>
    <style>
        .weui-panel__hd:after {
            border: none
        }

        .weui-cell:before {
            border-top: 0px
        }

        .weui-label {
            width: 120px;
        }
        .weui-labelvalue {
            width: 240px;
        }

        .weui-label span {
            color: #f00
        }
        .weui-labelvalue span {
            color: #f00
        }

        .select {
            width: 30px;
        }
        .weui-btn_primary {
            bottom: 0;
            left: 0px;
            background: rgb(43, 151, 242);
            width: 100%;
            border-radius: 0px;
        }

        .btnfaceimg {
            width: 30px;
            height: 30px;
            margin-left: 5px;
        }
        .weui-mask_transparent {
            position: fixed;
            z-index: 1000;
            top: 0;
            right: 0;
            left: 0;
            bottom: 0;
            background: rgba(0, 0, 0, 0.5);
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .weui-mask__content {
            text-align: center;
            color: #fff;
        }

        .spin {
            /* 使用 CSS 动画来旋转图标 */
            animation: spin 2s linear infinite;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
        .btrecpture {
            background-color: #e8f4ff;
            color: #2b97f2;
            border: none;
            border-radius: 6px;
            padding: 8px 15px;
            font-size: 14px;
            cursor: pointer;
            transition: all 0.2s;
        }
        .btrecpture:hover, .btrecpture:active {
            background-color: #d1e7ff;
        }
        .btrecpture:disabled {
            background-color: #f5f5f5;
            color: #999;
            cursor: not-allowed;
        }
        /* 拍摄规范弹窗样式 */
        .shot-demo-btn{
            color:#2b97f2;
            text-decoration: underline;
            margin-left:10px;
            cursor: pointer;
            font-size:14px;
        }
        .demo-mask {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0,0,0,0.75);
            z-index: 9999;
            display: none;
            /* 关键：禁止mask自己滚动，滚动交给内部容器 */
            overflow: hidden;
        }

        .demo-content {
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            overflow-y: auto;
            -webkit-overflow-scrolling: touch;
            padding: 20px 15px;
            box-sizing: border-box;
        }

        .demo-inner {
            background: #fff;
            border-radius: 8px;
            padding: 15px;
            max-width: 750px;
            margin: 0 auto;
        }
        .demo-title{
            font-size:16px;
            font-weight:bold;
            text-align:center;
            margin-bottom:15px;
            color:#333;
        }
        .demo-item{
            margin-bottom:20px;
            border-bottom:1px solid #eee;
            padding-bottom:15px;
        }
        .demo-item h4{
            margin:0 0 8px 0;
            color:#2b97f2;
            font-size:15px;
        }
        .demo-item p{
            margin:4px 0;
            font-size:13px;
            color:#666;
            line-height:1.6;
        }
        .demo-item img{
            width:100%;
            max-width:400px;
            margin-top:8px;
            border-radius:4px;
            border:1px solid #ddd;
        }
        .close-demo{
            display: block;
            width:100%;
            height:40px;
            line-height:40px;
            text-align:center;
            background:#2b97f2;
            color:#fff;
            border-radius:6px;
            margin-top:15px;
            cursor:pointer;
        }
    </style>
</head>
<body ontouchstart>
<div id="divload" style="display:none"></div>
<input id="zdzzl" value="" type="hidden">
<div class="weui-cells weui-cells_form" style="margin-top:0px;">
    <div class="weui-cell">
        <div class="weui-cell__hd"><label class="weui-label">照片拍摄</label></div>
        <div class="weui-cell__bd">
            <button class="btrecpture" onclick="xszaclick()">正页*</button>
            <input id="fileimgxsza" type="file" name="file" class="file" accept="image/*" onchange="$(this).trigger('originalChange')" value="" style="display: none">
            <button class="btrecpture" onclick="xszbclick()">副页*</button>
            <input id="fileimgxszb" type="file" name="file" class="file" accept="image/*"  onchange="$(this).trigger('originalChange')" value="" style="display: none">
            <button class="btrecpture" onclick="scqdclick()">随车清单</button>
            <input id="fileimgscqd" type="file" name="file" class="file" accept="image/*"  onchange="$(this).trigger('originalChange')" value="" style="display: none">
            <button class="btrecpture" onclick="vehicleclick()">车辆照片*</button>
            <input id="fileimgvehicle" type="file" name="file" class="file" accept="image/*"  onchange="$(this).trigger('originalChange')" value="" style="display: none">
            <span class="shot-demo-btn" onclick="openShotDemo()">规范示例</span>
        </div>
    </div>
    <div class="weui-cell">
        <img id="xszazp" class="img_content" style="height: 100px;margin-left: 10px">
        <img id="xszbzp" class="img_content" style="height: 100px;margin-left: 10px">
        <img id="scqdzp" class="img_content" style="height: 100px;margin-left: 10px">
        <img id="vehiclezp" class="img_content" style="height: 100px;margin-left: 10px">
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd">
            <label class="weui-label">车牌号码</label>
        </div>
        <div class="weui-cell__bd">
            <label class="weui-labelvalue" id="cphm"></label>
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd">
            <label class="weui-label">车架号</label>
        </div>
        <div class="weui-cell__bd">
            <label class="weui-labelvalue" id="clsbdh"></label>
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd">
            <label class="weui-label">发动机号</label>
        </div>
        <div class="weui-cell__bd">
            <label class="weui-labelvalue" id="fdjh"></label>
        </div>
    </div>
    <div id="veccQueryBlock" style="display:none;">
        <div class="weui-cell">
            <div class="weui-cell__hd">
                <label class="weui-label">车架号</label>
            </div>
            <div class="weui-cell__bd">
                <input class="weui-input brand" id="veccVin" type="text" maxlength="17" placeholder="车架号(17位)">
            </div>
        </div>
        <div class="weui-cell">
            <div class="weui-cell__hd">
                <label class="weui-label">发动机后6位</label>
            </div>
            <div class="weui-cell__bd">
                <input class="weui-input brand" id="veccEngine6" type="text" maxlength="6" placeholder="发动机号后6位">
            </div>
        </div>
        <div class="weui-cell">
            <div class="weui-cell__hd">
                <label class="weui-label">验证码</label>
            </div>
            <div class="weui-cell__bd" style="display:flex;align-items:center;gap:8px;">
                <input class="weui-input brand" id="veccCaptcha" type="text" maxlength="6" placeholder="验证码" style="flex:1;">
                <img id="veccCaptchaImg" style="height:34px;width:90px;border:1px solid #ddd;border-radius:4px;background:#fff;" onclick="veccRefreshCaptcha()">
                <button class="btrecpture" type="button" onclick="veccRefreshCaptcha()">刷新</button>
            </div>
        </div>
        <div class="weui-cell">
            <div class="weui-cell__bd">
                <button class="btrecpture" type="button" onclick="veccFetchScqd()">查询随车清单</button>
            </div>
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd">
            <label class="weui-label">注册日期</label>
        </div>
        <div class="weui-cell__bd">
            <label class="weui-labelvalue" id="ccdjrq"></label>
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd">
            <label class="weui-label">排放标准</label>
        </div>
        <div class="weui-cell__bd">
            <label class="weui-labelvalue" id="pfbz"></label>
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd">
            <label class="weui-label">车辆类型</label>
        </div>
        <div class="weui-cell__bd">
            <label class="weui-labelvalue" id="cllx"></label>
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd">
            <label class="weui-label">使用性质</label>
        </div>
        <div class="weui-cell__bd">
            <label class="weui-labelvalue" id="syxz"></label>
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd">
            <label class="weui-label">车辆品牌型号</label>
        </div>
        <div class="weui-cell__bd">
            <label class="weui-labelvalue" id="clppxh"></label>
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd">
            <label class="weui-label">所有人</label>
        </div>
        <div class="weui-cell__bd">
            <label class="weui-labelvalue" id="syr"></label>
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd">
            <label class="weui-label">核定载质量</label>
        </div>
        <div class="weui-cell__bd">
            <label class="weui-labelvalue" id="hdzzl"></label>
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd">
            <label class="weui-label">燃油类型<span>*</span></label>
        </div>
        <div class="weui-cell__bd">
            <select class="weui-select" id="rlzl">
                <option value="">请选择</option>
                <option value="A">汽油</option>
                <option value="B">柴油</option>
                <option value="C">电</option>
                <option value="D">混合油</option>
                <option value="E">天然气</option>
                <option value="Y">无</option>
                <option value="Z">其他</option>
            </select>
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd">
            <label class="weui-label">车辆种类<span>*</span></label>
        </div>
        <div class="weui-cell__bd">
            <select class="weui-select" id="clzl">
                <option value="">请选择</option>
                <option value="0">货运</option>
                <option value="1">危固废运输</option>
                <option value="2">特种作业</option>
                <option value="3">民生保障</option>
                <option value="4">客运</option>
            </select>
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd">
            <label class="weui-label">车牌颜色<span>*</span></label>
        </div>
        <div class="weui-cell__bd">
            <select class="weui-select" id="cpys">
                <option value="">请选择</option>
                <option value="0">蓝牌</option>
                <option value="1">黄牌</option>
                <option value="2">白牌</option>
                <option value="3">黑牌</option>
                <option value="4">绿牌</option>
                <option value="6">绿黄牌</option>
                <option value="5">其他</option>
            </select>
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd">
            <label class="weui-label">是否联网(OBD)<span>*</span></label>
        </div>
        <div class="weui-cell__bd">
            <select class="weui-select" id="lwzt">
                <option value="">请选择</option>
                <option value="0">未联网</option>
                <option value="1">已联网</option>
            </select>
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd">
            <label class="weui-label">最新年检日期<span>*</span>(如20240101)</label>
        </div>
        <div class="weui-cell__bd">
            <input class="weui-input brand" id="zjnjsj" type="text" placeholder="">
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd">
            <label class="weui-label">手机号码<span>*</span></label>
        </div>
        <div class="weui-cell__bd">
            <input class="weui-input brand" id="lxdh" type="text" placeholder="">
        </div>
    </div>
    <!-- 等待动画的容器 -->
    <div id="upload-spinner" class="spinner-container" style="display: none;">
        <!-- 这里可以放置你的转圈等待动画的HTML代码，比如一个CSS Spinner -->
        <div class="spinner">
            <!-- Spinner的实现，可以是一个旋转的图标或者动画 -->
        </div>
        <p>请稍后...</p>
    </div>
    <div id="loading-overlay" class="weui-mask_transparent">
        <div class="weui-mask__content">
            <i class="iconfont icon-loading spin"></i>
            <p>请等待...</p>
        </div>
    </div>
    <a href="#" onclick="submitVeh()" class="weui-btn weui-btn_primary"
       style="height: 50px;display: flex;justify-content: center;align-items: center;color:white;font-size: 20px">提交信息</a>
</div>
<script>
    var imgurlxsza = "";
    var imgurlxszb = "";
    var imgurlscqd = "";
    var imgurlvehicle = "";
    var xszacphm="";
    var xszbcphm="";
    var ccdjrq="";
    var address="";
    var fzrq="";
    var pfpdyj="";
    var pfpdyj="";
    var pfpdyjzl="";
    var imgElementxszazp="";
    var imgElementxszbzp="";
    var imgElementscqdzp="";
    var imgElementvehiclezp="";
    var veccCaptchaLoading=false;
    $(document).ready(function () {
        imgElementxszazp = document.getElementById('xszazp');
        imgElementxszazp.style.display="none";
        imgElementxszbzp = document.getElementById('xszbzp');
        imgElementxszbzp.style.display="none";
        imgElementscqdzp = document.getElementById('scqdzp');
        imgElementscqdzp.style.display="none";
        imgElementvehiclezp = document.getElementById('vehiclezp');
        imgElementvehiclezp.style.display="none";
        document.getElementById('loading-overlay').style.display = 'none';
        // 处理文件上传
        $("input[id='fileimgxsza']").on("originalChange", function (event) {
            var file = event.currentTarget.files[0];
            if (!file) return;
            console.log(file.size);
            // 先判断文件大小，如果已小于300KB直接上传
            if (file.size <= 300 * 1024) {
                uploadFilexsza(file);
                return;
            }

            // 否则进行压缩处理
            compressImage(file, 300 * 1024) // 目标大小：300KB
                .then(compressedBlob => {
                    uploadFilexsza(compressedBlob);
                })
                .catch(err => {
                    dialog("错误", "图片压缩失败，请重试");
                });
        });
        $("input[id='fileimgxszb']").on("originalChange", function (event) {
            var file = event.currentTarget.files[0];
            if (!file) return;
            console.log(file.size);
            // 先判断文件大小，如果已小于300KB直接上传
            if (file.size <= 300 * 1024) {
                uploadFilexszb(file);
                return;
            }

            // 否则进行压缩处理
            compressImage(file, 300 * 1024) // 目标大小：300KB
                .then(compressedBlob => {
                    uploadFilexszb(compressedBlob);
                })
                .catch(err => {
                    dialog("错误", "图片压缩失败，请重试");
                });
        });
        $("input[id='fileimgscqd']").on("originalChange", function (event) {
            var file = event.currentTarget.files[0];
            if (!file) return;
            // 先判断文件大小，如果已小于300KB直接上传
            if (file.size <= 300 * 1024) {
                uploadFilescqd(file);
                return;
            }

            // 否则进行压缩处理
            compressImage(file, 300 * 1024) // 目标大小：300KB
                .then(compressedBlob => {
                    uploadFilescqd(compressedBlob);
                })
                .catch(err => {
                    dialog("错误", "图片压缩失败，请重试");
                });
        });
        $("input[id='fileimgvehicle']").on("originalChange", function (event) {
            var file = event.currentTarget.files[0];
            if (!file) return;
            // 先判断文件大小，如果已小于300KB直接上传
            if (file.size <= 300 * 1024) {
                uploadFilevehicle(file);
                return;
            }

            // 否则进行压缩处理
            compressImage(file, 300 * 1024) // 目标大小：300KB
                .then(compressedBlob => {
                    uploadFilevehicle(compressedBlob);
                })
                .catch(err => {
                    dialog("错误", "图片压缩失败，请重试");
                });
        });
    })
    function formatDate(timestamp) {
        // 创建一个Date对象
        var date = new Date(timestamp);

        // 获取年份（四位数）
        var year = date.getFullYear();

        // 获取月份（0-11），并转换为1-12
        var month = (date.getMonth() + 1).toString();
        month = month.length < 2 ? '0' + month : month;

        // 获取日期（1-31）
        var day = date.getDate().toString();
        day = day.length < 2 ? '0' + day : day;

        // 拼接并返回日期字符串
        return year + '-' + month + '-' + day;
    }
    function isValidDate(dateString) {
        // 尝试解析日期字符串
        const date = new Date(dateString);

        // 如果解析后的日期是"Invalid Date"，则原始字符串不是有效的日期
        // 否则，我们需要检查日期是否实际表示了一个有效的日期（例如，2月30日或4月31日都是无效的）
        // 这可以通过比较解析后的日期和原始字符串重新格式化为字符串后的结果来实现
        return !isNaN(date.getTime()) &&
            date.toISOString().slice(0, 10) === dateString.replace(/-/g, '-'); // 确保年份和月份没有变化
    }

    function isVeccAvailableNow() {
        var h = new Date().getHours();
        return !(h >= 20 || h < 7);
    }

    function setupVeccQuery(vehicle) {
        try {
            if (!isVeccAvailableNow()) {
                $("#veccQueryBlock").hide();
                return;
            }
            var regDate = formatDate(vehicle.ccdjrq);
            if (regDate >= "2017-01-01") {
                $("#veccQueryBlock").show();
            } else {
                $("#veccQueryBlock").hide();
                return;
            }

            var vin = (vehicle.clsbdh || "").toString();
            if (vin.length >= 17) {
                $("#veccVin").val(vin);
            }
            var eng = (vehicle.fdjh || "").toString();
            if (eng.length >= 6) {
                $("#veccEngine6").val(eng.substring(eng.length - 6));
            }
            $("#veccCaptcha").val("");
            veccInitCaptcha();
        } catch (e) {
            $("#veccQueryBlock").hide();
        }
    }

    function veccInitCaptcha() {
        if (veccCaptchaLoading) return;
        if (!isVeccAvailableNow()) {
            $("#veccQueryBlock").hide();
            return;
        }
        veccCaptchaLoading = true;
        $.ajax({
            url: '${path}/wechat/vecc/init',
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                veccCaptchaLoading = false;
                if (typeof data === 'string') {
                    data = $.parseJSON(data);
                }
                if (data.code == 1 && data.captcha) {
                    $("#veccCaptchaImg").attr("src", data.captcha);
                } else {
                    $("#veccQueryBlock").hide();
                }
            },
            error: function () {
                veccCaptchaLoading = false;
            }
        });
    }

    function veccRefreshCaptcha() {
        if (veccCaptchaLoading) return;
        if (!isVeccAvailableNow()) {
            $("#veccQueryBlock").hide();
            return;
        }
        veccCaptchaLoading = true;
        $.ajax({
            url: '${path}/wechat/vecc/refresh',
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                veccCaptchaLoading = false;
                if (typeof data === 'string') {
                    data = $.parseJSON(data);
                }
                if (data.code == 1 && data.captcha) {
                    $("#veccCaptchaImg").attr("src", data.captcha);
                } else {
                    veccInitCaptcha();
                }
            },
            error: function () {
                veccCaptchaLoading = false;
            }
        });
    }

    function veccFetchScqd() {
        if (imgurlxsza == "") {
            dialog("温馨提示", "请先拍摄行驶证正页");
            return;
        }
        if (!isVeccAvailableNow()) {
            dialog("温馨提示", "晚8点至早7点期间不提供数据查询服务");
            $("#veccQueryBlock").hide();
            return;
        }
        var vin = ($("#veccVin").val() || "").trim();
        var engine6 = ($("#veccEngine6").val() || "").trim();
        var captcha = ($("#veccCaptcha").val() || "").trim();
        if (vin.length != 17) {
            dialog("温馨提示", "请输入17位车架号");
            return;
        }
        if (engine6.length != 6) {
            dialog("温馨提示", "请输入发动机号后6位");
            return;
        }
        if (captcha == "") {
            dialog("温馨提示", "请输入验证码");
            return;
        }

        document.getElementById('loading-overlay').style.display = 'flex';
        $.ajax({
            url: '${path}/wechat/vecc/scqd',
            type: 'POST',
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify({
                vin: vin,
                engine6: engine6,
                captcha: captcha,
                fdjh: $("#fdjh").html(),
                clsbdh: $("#clsbdh").html(),
                clppxh: $("#clppxh").html()
            }),
            success: function (data) {
                document.getElementById('loading-overlay').style.display = 'none';
                if (typeof data === 'string') {
                    data = $.parseJSON(data);
                }
                var url = data.url;
                imgElementscqdzp.style.display = "block";
                $("#scqdzp").attr("src", url);
                if (data.code != 1) {
                    dialog("错误", data.message || "查询失败");
                    if (data.captcha) {
                        $("#veccCaptchaImg").attr("src", data.captcha);
                    } else {
                        veccRefreshCaptcha();
                    }
                    return;
                }

                var pfbz = data.pfbz || "";

                imgurlscqd = url;
                if (pfbz != "") {
                    var pfbzex = $("#pfbz").html();
                    if (pfbz != pfbzex) {
                        $("#pfbz").html(pfbz);
                        pfpdyj = data.pfpdyj || "";
                        pfpdyjzl = data.pfbzyjzl || "";
                    }
                }
            },
            error: function () {
                document.getElementById('loading-overlay').style.display = 'none';
                dialog("错误", "查询失败，请重试");
            }
        });
    }
    function submitVeh() {
        var lxdh = $("#lxdh").val();
        var clzl = $("#clzl").val();
        var cphm = $("#cphm").html();
        var cpys =  $("#cpys").val();
        var zjnjsj =  $("#zjnjsj").val();
        var lwzt =  $("#lwzt").val();
        var pfbz=$("#pfbz").html();
        if (imgurlxsza == "") {
            dialog("温馨提示", "行驶证正页不能为空，请拍照");
            return;
        }
        if (imgurlxszb == "") {
            dialog("温馨提示", "行驶证副页不能为空，请拍照");
            return;
        }
        if (imgurlvehicle == "") {
            dialog("温馨提示", "车辆照片不能为空，请拍照");
            return;
        }
        if(pfbz=="") {
            if (ccdjrq >= "2017-01-01") {
                if (imgurlscqd == "") {
                    dialog("温馨提示", "请拍摄随车清单,获取排放标准");
                    return;
                }
            }
        }
        if(!isValidDate(ccdjrq)){
            dialog("温馨提示", "注册登记日期格式不正确");
            return;
        }
        if (lwzt == "") {
            dialog("温馨提示", "是否联网不能为空");
            return;
        }
        var clsbdh=$("#clsbdh").html();
        if(clsbdh.length!=17){
            dialog("温馨提示", "车架号应为17位，请核实");
            return;
        }
        if(zjnjsj!="") {
            if (typeof zjnjsj !== 'string' || zjnjsj.length !== 8) {
                dialog("温馨提示", "最近年检时间错误");
                return ;
            }
            // 使用模板字面量和substring方法来格式化日期
            zjnjsj=zjnjsj.substring(0, 4)+"-"+zjnjsj.substring(4, 6)+"-"+zjnjsj.substring(6, 8);
            // console.log(zjnjsj);
            if (!isValidDate(zjnjsj)) {
                dialog("温馨提示", "请输入正确的最近年检时间");
                return;
            }
        }
        if (lxdh == "") {
            dialog("温馨提示", "手机号码不能为空");
            return;
        }
        if (!checkMobile(lxdh)) {
            dialog("温馨提示", "手机号码不正确");
            return;
        }
        if (cphm == "") {
            dialog("温馨提示", "请拍摄行驶证获取正确的车牌号");
            return;
        }
        if (clzl == "") {
            dialog("温馨提示", "车辆种类不能为空");
            return;
        }
        if ($("#pfbz").html() == "") {
            dialog("温馨提示", "排放标准不能为空");
            return;
        }
        if (rlzl == "") {
            dialog("温馨提示", "燃料种类不能为空");
            return;
        }
        if (cpys == "") {
            dialog("温馨提示", "车牌颜色不能为空");
            return;
        }
        $.ajax({
            type: "post",
            url: '${path}/wechat/updateveh',
            data: {
                "lxdh": $("#lxdh").val(),
                "zjnjsj": zjnjsj,
                "cphm": $("#cphm").html(),
                "clsbdh": $("#clsbdh").html(),
                "fdjh": $("#fdjh").html(),
                "ccdjrq": $("#ccdjrq").html(),
                "fzrq": fzrq,
                "address": address,
                "pfbz": $("#pfbz").html(),
                "cllx": $("#cllx").html(),
                "syxz": $("#syxz").html(),
                "clppxh": $("#clppxh").html(),
                "syr": $("#syr").html(),
                "rlzl": $("#rlzl").val(),
                "hdzzl": $("#hdzzl").html(),
                "zdzzl": $("#zdzzl").val(),
                "clzl": clzl,
                "cpys": cpys,
                "lwzt": lwzt,
                "pfpdyj":pfpdyj,
                "pfpdyjzl":pfpdyjzl,
                "xszazp": imgurlxsza,
                "xszbzp": imgurlxszb,
                "vehiclezp": imgurlvehicle,
                "scqdzp": imgurlscqd
            },
            dataType: 'json',
            success: function (data) {
                //  console.log(data);
                if (data.indexOf("成功")>0) {
                    dialog("温馨提示", "提交成功");
                    reset();
                } else {
                    dialog("错误", data);
                }
            }
        });
    }

    function checkMobile(str) {
        var re = /^1\d{10}$/
        if (re.test(str)) {
            return true;
        } else {
            return false;
        }
    }
    function reset(){
        $("#xszazp").attr("src", "");
        imgurlxsza="";
        $("#xszbzp").attr("src", "");
        imgurlxszb="";
        $("#scqdzp").attr("src", "");
        imgurlscqd="";
        $("#vehiclezp").attr("src", "");
        imgurlvehicle="";
        $("#cphm").html("");
        $("#clsbdh").html("");
        $("#fdjh").html("");
        $("#ccdjrq").html("");
        $("#pfbz").html("");
        $("#cllx").html("");
        $("#syxz").html("");
        $("#clppxh").html("");
        $("#syr").html("");
        $("#hdzzl").html("");
        $("#rlzl").val("");
        $("#clzl").val("");
        $("#cpys").val("");
        $("#lwzt").val("");
        $("#zjnjsj").val("");
        $("#lxdh").val("");
        xszacphm="";
        xszbcphm="";
        ccdjrq="";
        fzrq="";
        address="";
        pfpdyj="";
        pfpdyjzl="";
        $("#veccQueryBlock").hide();
        $("#veccVin").val("");
        $("#veccEngine6").val("");
        $("#veccCaptcha").val("");
        $("#veccCaptchaImg").attr("src", "");
    }

    function dialognew(title, msg, callback) {
        dialog2 = '<div class="weui_dialog_alert" id="dialog2" style="display: none;"><div class="weui_mask"></div>' +
            '<div class="weui_dialog">' +
            '<div class="weui_dialog_hd"><strong class="weui_dialog_title">' + title + '</strong></div>' +
            '<div class="weui_dialog_bd">' + msg + '</div>' +
            '<div class="weui_dialog_ft">' +
            '<a href="javascript:;" class="weui_btn_dialog primary">确定</a>' +
            '</div>' +
            '</div>' +
            '</div>';
        if (!$('#dialog2').length) {
            $('body').append(dialog2);
        } else {
            $('#dialog2 .weui_dialog_title').html(title);
            $('#dialog2 .weui_dialog_bd').html(msg);
        }
        $('#dialog2').fadeIn('fast');
        $('#dialog2 .primary').on('click', function () {
            $('#dialog2').fadeOut('fast');
            var url = callback;
            if (url !== '') {
                window.location.href = url;
            }
        });

    }

    function dialog(title, msg) {
        dialog2 = '<div class="weui_dialog_alert" id="dialog2" style="display: none;"><div class="weui_mask"></div>' +
            '<div class="weui_dialog">' +
            '<div class="weui_dialog_hd"><strong class="weui_dialog_title">' + title + '</strong></div>' +
            '<div class="weui_dialog_bd">' + msg + '</div>' +
            '<div class="weui_dialog_ft">' +
            '<a href="javascript:;" class="weui_btn_dialog primary">确定</a>' +
            '</div>' +
            '</div>' +
            '</div>';
        if (!$('#dialog2').length) {
            $('body').append(dialog2);
        } else {
            $('#dialog2 .weui_dialog_title').html(title);
            $('#dialog2 .weui_dialog_bd').html(msg);
        }
        $('#dialog2').fadeIn('fast');
        $('#dialog2 .primary').on('click', function () {
            $('#dialog2').fadeOut('fast');
        });
    }

    function xszaclick() {
        if (confirm("拍摄时，请对准行驶证正页拍照")) {
            $("#fileimgxsza").val(""); // 清空，解决重复选择
            $("#fileimgxsza").click();
        }
    }
    function xszbclick() {
        if (confirm("拍摄时，请对准行驶证副页拍照")) {
            $("#fileimgxszb").val(""); // 清空
            $("#fileimgxszb").click();
        }
    }
    function scqdclick() {
        if (confirm("拍摄时，请对准随车清单拍照")) {
            $("#fileimgscqd").val(""); // 清空
            $("#fileimgscqd").click();
        }
    }
    function vehicleclick() {
        if (confirm("拍摄时，请从车辆左前方拍摄车辆")) {
            $("#fileimgvehicle").val(""); // 清空
            $("#fileimgvehicle").click();
        }
    }

    $(document).on('click', '.img_content', function () {
        //var strify = JSON.stringify(this.src);//获得当前元素src
        var indexImg = $('.img_content').index(this);//获得当前元素的下标，从class中查找
        var wximg = new Array();//所有src集合
        $(".img_content").each(function (index, item) {
            wximg.push(item.src);//得到src push到数组
        });
        imagePreview(wximg[indexImg], wximg);//第一个参数通过下标得到当前点击图片的src，第二个参数全部的src。
    });


    //微信函数
    function imagePreview(curSrc, srcList) {
        //这个检测是否参数为空
        if (!curSrc || !srcList || srcList.length == 0) {
            return;
        }
        //这个使用了微信浏览器提供的JsAPI 调用微信图片浏览器
        WeixinJSBridge.invoke('imagePreview', {
            'current': curSrc,
            'urls': srcList
        });
    }
    // 图片压缩函数
    function compressImage(file, maxSize) {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onload = function (e) {
                const img = new Image();
                img.onload = function () {
                    const canvas = document.createElement('canvas');
                    const ctx = canvas.getContext('2d');

                    // 计算压缩后的尺寸（保持宽高比）
                    let width = img.width;
                    let height = img.height;
                    const maxDim = Math.max(width, height);
                    // 如果图片过大，先按比例缩小
                    if (maxDim > 1920) {
                        const ratio = 1920 / maxDim;
                        width *= ratio;
                        height *= ratio;
                    }
                    canvas.width = width;
                    canvas.height = height;

                    // 绘制图片到画布
                    ctx.drawImage(img, 0, 0, width, height);

                    // 通过调整质量参数来达到目标大小
                    let quality = 0.9; // 初始质量
                    let blob;

                    function checkSize() {
                        canvas.toBlob(blobObj => {
                            blob = blobObj;
                            // 如果小于目标大小或质量已降到最低，停止压缩
                            if (blob.size <= maxSize || quality <= 0.1) {
                                resolve(blob);
                            } else {
                                // 每次降低10%质量
                                quality -= 0.1;
                                checkSize();
                            }
                        }, 'image/jpeg', quality);
                    }

                    checkSize();
                };
                img.onerror = reject;
                img.src = e.target.result;
            };
            reader.onerror = reject;
            reader.readAsDataURL(file);
        });
    }
    function uploadFilexsza(file) {
        var formFile = new FormData();
        // 如果是压缩后的Blob对象，需要转换为File对象
        const uploadFile = file instanceof Blob ? new File([file], "compressed.jpg", { type: "image/jpeg" }) : file;

        formFile.append("file", uploadFile);
        formFile.append("zpzl", "front");

        // 显示加载提示
        document.getElementById('loading-overlay').style.display = 'flex';

        $.ajax({
            url: '${path}/wechat/sendxszzp',
            type: 'POST',
            data: formFile,
            async: true,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                // 隐藏加载提示
                document.getElementById('loading-overlay').style.display = 'none';
                data = $.parseJSON(data);
                if (data.code != 1) {
                    dialog("错误", data.message);
                } else {
                    // 保持原有的成功处理逻辑
                    var content = data.message;
                    var vehicle = data.vehicle;
                    if(xszbcphm != ""){
                        if(vehicle.cphm != xszbcphm) {
                            dialog("温馨提示", "行驶证正页与副页不一致");
                            return;
                        }
                    } else {
                        xszacphm = vehicle.cphm;
                    }
                    imgElementxszazp.style.display = "block";
                    $("#xszazp").attr("src", content);
                    imgurlxsza = content;

                    $("#cphm").html(vehicle.cphm);
                    $("#clsbdh").html(vehicle.clsbdh);
                    $("#fdjh").html(vehicle.fdjh);
                    $("#ccdjrq").html(formatDate(vehicle.ccdjrq));
                    $("#cllx").html(vehicle.cllx);
                    $("#syxz").html(vehicle.syxz);
                    $("#clppxh").html(vehicle.clppxh);
                    $("#syr").html(vehicle.syr);
                    $("#cpys").val(vehicle.cpys);
                    $("#clzl").val(vehicle.clzl);
                    $("#pfbz").html(vehicle.pfbz);
                    $("#hdzzl").html(vehicle.hdzzl);
                    $("#zdzzl").val(vehicle.zdzzl);
                    $("#rlzl").val(vehicle.rlzl);
                    pfpdyj = vehicle.pfpdyj;
                    pfpdyjzl = vehicle.pfpdyjzl;
                    ccdjrq = formatDate(vehicle.ccdjrq);
                    fzrq = formatDate(vehicle.fzrq);
                    address=vehicle.address;
                    setupVeccQuery(vehicle);
                }
            },
            error: function() {
                document.getElementById('loading-overlay').style.display = 'none';
                dialog("错误", "上传失败，请重试");
            }
        });
    }
    function uploadFilexszb(file) {
        var formFile = new FormData();
        // 如果是压缩后的Blob对象，需要转换为File对象
        const uploadFile = file instanceof Blob ? new File([file], "compressed.jpg", { type: "image/jpeg" }) : file;
        var formFile = new FormData();
        formFile.append("file", uploadFile);
        formFile.append("zpzl", "back");
        document.getElementById('loading-overlay').style.display = 'flex';
        $.ajax({
            url: '${path}/wechat/sendxszzp',
            type: 'POST',
            data: formFile,
            async: true,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                document.getElementById('loading-overlay').style.display = 'none';
                // console.log(data);
                data = $.parseJSON(data);
                if (data.code != 1) {
                    dialog("错误", data.message);
                } else {
                    var content = data.message;
                    var vehicle=data.vehicle;
                    imgElementxszbzp.style.display="block";
                    if(xszacphm!=""){
                        if(vehicle.cphm!=xszacphm) {
                            dialog("温馨提示", "行驶证正页与副页不一致");
                            return;
                        }
                    }
                    else{
                        xszbcphm = vehicle.cphm;
                    }
                    $("#xszbzp").attr("src", content);
                    imgurlxszb = content;

                    $("#hdzzl").html(vehicle.hdzzl);
                    $("#rlzl").val(vehicle.rlzl);
                    $("#zdzzl").val(vehicle.zdzzl);
                }
            }
        })
    }
    function uploadFilescqd(file) {
        var formFile = new FormData();
        // 如果是压缩后的Blob对象，需要转换为File对象
        const uploadFile = file instanceof Blob ? new File([file], "compressed.jpg", { type: "image/jpeg" }) : file;
        var formFile = new FormData();
        formFile.append("file", uploadFile);
        formFile.append("fdjh",$("#fdjh").html() );
        formFile.append("clsbdh",$("#clsbdh").html() );
        formFile.append("clppxh",$("#clppxh").html() );
        if (imgurlxsza == "") {
            dialog("温馨提示", "请先拍摄行驶证正页");
            return;
        }
        document.getElementById('loading-overlay').style.display = 'flex';
        $.ajax({
            url: '${path}/wechat/sendscqdzp',
            type: 'POST',
            data: formFile,
            async: true,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                document.getElementById('loading-overlay').style.display = 'none';
                // console.log(data);
                data = $.parseJSON(data);
                if (data.code != 1) {
                    dialog("错误", data.message);
                } else {
                    var url = data.url;
                    var pfbz=data.pfbz;
                    imgElementscqdzp.style.display="block";
                    $("#scqdzp").attr("src", url);
                    imgurlscqd = url;
                    if(pfbz!=""){
                        var pfbzex=$("#pfbz").html();
                        if(pfbz!=pfbzex){
                            $("#pfbz").html(pfbz);
                            pfpdyj=data.pfpdyj;
                            pfpdyjzl=data.pfpdyjzl;
                        }
                    }
                }
            }
        })
    }
    function uploadFilevehicle(file) {
        var formFile = new FormData();
        // 如果是压缩后的Blob对象，需要转换为File对象
        const uploadFile = file instanceof Blob ? new File([file], "compressed.jpg", { type: "image/jpeg" }) : file;
        var formFile = new FormData();
        formFile.append("file", uploadFile);
        document.getElementById('loading-overlay').style.display = 'flex';
        $.ajax({
            url: '${path}/wechat/sendvehiclezp',
            type: 'POST',
            data: formFile,
            async: true,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                document.getElementById('loading-overlay').style.display = 'none';
                // console.log(data);
                data = $.parseJSON(data);
                if (data.code != 1) {
                    dialog("错误", data.message);
                } else {
                    var url = data.url;
                    imgElementvehiclezp.style.display="block";
                    $("#vehiclezp").attr("src", url);
                    imgurlvehicle = url;
                }
            }
        })
    }
    // 打开拍摄规范弹窗
    function openShotDemo(){
        $("#shotDemoMask").show();
        // 禁止底层页面滚动
        $('body').css({
            'overflow': 'hidden',
            'position': 'fixed',
            'width': '100%',
            'height': '100%'
        });
    }

    // 关闭拍摄规范弹窗
    function closeShotDemo(){
        $("#shotDemoMask").hide();
        // 恢复底层页面滚动
        $('body').removeAttr('style');
    }

    // 点击遮罩空白关闭（只点击mask才触发）
    $(document).on('click','#shotDemoMask',function(e){
        if(e.target === this){
            closeShotDemo();
        }
    });
</script>
<!-- 拍摄规范弹窗 -->
<div class="demo-mask" id="shotDemoMask">
    <div class="demo-content">
        <div class="demo-inner">
            <div class="demo-title">拍摄规范及示例</div>

            <div class="demo-item">
                <h4>1.行驶证正页拍摄要求</h4>
                <p>行驶证正页从皮套里拿出来，避免反光</p>
                <p>摆放端正平整，正向拍摄，不得倾斜、颠倒，字迹清楚</p>
                <img src="${staticPath}/static/images/xsza.jpg" alt="行驶证正页示例">
            </div>

            <div class="demo-item">
                <h4>2.行驶证副页拍摄要求</h4>
                <p>行驶证副页从皮套里拿出来，避免反光</p>
                <p>摆放端正平整，正向拍摄，不得倾斜、颠倒，字迹清楚</p>
                <img src="${staticPath}/static/images/xszb.jpg" alt="行驶证副页示例">
            </div>

            <div class="demo-item">
                <h4>3.随车清单拍摄要求</h4>
                <p>2017年前注册的车辆无需拍摄</p>
                <p>早7点到晚8点间优先自动获取，若获取不到再拍摄纸质照片</p>
                <p>摆放端正平整，正向拍摄，不得倾斜、颠倒，字迹清楚</p>
                <img src="${staticPath}/static/images/scqd.jpg" alt="随车清单示例">
            </div>

            <div class="demo-item" style="border:none;margin-bottom:0;padding-bottom:0;">
                <h4>4.车辆照片拍摄要求</h4>
                <p>从车辆左前方拍摄，能够看到车牌和整个车身</p>
                <img src="${staticPath}/static/images/vehicle.jpg" alt="车辆照片示例">
            </div>

            <div class="close-demo" onclick="closeShotDemo()">关闭</div>
        </div>
    </div>
</div>
</body>
