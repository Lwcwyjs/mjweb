<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>运输车辆注册</title>
    <!-- 移除重复的weui样式，保留本地版本避免跨域问题 -->
    <link rel="stylesheet" href="${staticPath}/static/lib/weui.min.css">
    <link rel="stylesheet" href="${staticPath}/static/css/jquery-weui.css">
    <link rel="stylesheet" href="${staticPath}/static/layui/css/layui.css" type="text/css">
    <link rel="stylesheet" href="${staticPath}/static/css/jquery.mCustomScrollbar.min.css"/>
    <link rel="stylesheet" href="${staticPath}/static/css/jquery.emoji.css"/>
    <link rel="stylesheet" href="${staticPath}/static/css/style.css">
    <link href="${staticPath}/static/css/main.css?r=<%=Math.floor(Math.random() * 10000) + 1%>" rel="stylesheet"
          type="text/css">

    <!-- 引入JS库（按依赖顺序排序） -->
    <script src="${staticPath}/static/lib/jquery-2.1.4.js"></script>
    <script src="${staticPath}/static/lib/fastclick.js"></script>
    <script src="${staticPath}/static/js/jquery.mCustomScrollbar.min.js"></script>
    <script src="${staticPath}/static/js/jquery.emoji.js"></script>
    <script src="${staticPath}/static/js/jquery-weui.js"></script>
    <script src="${staticPath}/static/js/swiper.js"></script>
    <script src="${staticPath}/static/js/common.js"></script>
    <script src="${staticPath}/static/Scripts/iscroll.js" type="text/javascript"></script>
    <script type="text/javascript" src="${staticPath}/static/js/jweixin-1.6.0.js"></script>

    <style>
        /* 全局重置与基础样式 */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        /* 核心优化：添加背景图片+页面整体居中 */
        body {
            background: url("${staticPath}/static/images/bg-transport.jpg") no-repeat center top;
            background-size: cover;
            background-attachment: fixed;
            font-size: 14px;
            color: #333;
            font-family: -apple-system, BlinkMacSystemFont, "PingFang SC", "Helvetica Neue", sans-serif;
            /* 让body占满屏幕高度，方便居中 */
            min-height: 100vh;
            padding: 20px 15px;
        }

        /* 页面容器：实现内容居中，限制最大宽度 */
        .page-wrapper {
            max-width: 500px;
            margin: 0 auto;
            width: 100%;
        }

        /* 表单容器优化：居中+更优的视觉效果 */
        .form-container {
            background-color: rgba(255, 255, 255, 0.95); /* 半透明白色，兼容背景图 */
            margin: 0 auto;
            border-radius: 12px;
            padding: 20px 15px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            max-width: 100%;
            overflow: hidden;
        }

        /* 表单单元格样式优化 */
        .weui-cells {
            margin: 0 !important;
        }

        .weui-cell {
            padding: 15px 0;
            border-bottom: 1px solid #f0f0f0;
            display: flex;
            align-items: center;
        }

        .weui-cell:last-child {
            border-bottom: none;
        }

        .weui-cell__hd {
            padding-right: 12px;
            flex-shrink: 0; /* 固定标签宽度，不被挤压 */
        }

        .weui-label {
            width: 110px !important;
            font-size: 14px;
            color: #333;
            font-weight: 500;
        }

        /* 必填项星号样式优化 */
        .required-star {
            color: #f53f3f;
            margin-left: 2px;
        }

        /* 车牌号输入框专项优化 */
        .plate-input {
            font-size: 15px !important;
            color: #333;
            padding: 10px 12px !important;
            border: 1px solid #eee !important;
            border-radius: 6px !important;
            background-color: #fafafa !important;
            width: 100% !important;
            transition: all 0.2s ease;
        }

        .plate-input:focus {
            border-color: #2b97f2 !important;
            background-color: #fff !important;
            outline: none !important;
            box-shadow: 0 0 0 3px rgba(43, 151, 242, 0.1) !important;
        }

        .plate-input::placeholder {
            color: #999;
            font-size: 14px;
        }

        /* 修复：车牌颜色下拉框（解决文字显示不全问题） */
        .color-select {
            font-size: 14px !important;
            color: #333;
            border: 1px solid #eee !important;
            border-radius: 6px !important;
            background-color: #fafafa !important;
            width: 100% !important;
            appearance: none; /* 去除默认下拉样式 */
            /* 关键修复：防止文字换行、处理溢出 */
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            /* 调整箭头位置，避免遮挡文字 */
            background-image: url("data:image/svg+xml;charset=utf-8,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='6'%3E%3Cpath fill='%23999' d='M0 0l6 6 6-6z'/%3E%3C/svg%3E");
            background-repeat: no-repeat;
            background-position: right 10px center;
            background-size: 12px;
            transition: all 0.2s ease;
        }

        .color-select:focus {
            border-color: #2b97f2 !important;
            background-color: #fff !important;
            outline: none !important;
            box-shadow: 0 0 0 3px rgba(43, 151, 242, 0.1) !important;
        }

        /* 提交按钮样式优化 */
        .submit-btn {
            position: fixed;
            bottom: 0;
            left: 0;
            width: 100%;
            height: 50px;
            line-height: 50px;
            background: linear-gradient(90deg, #2b97f2, #1e88e5) !important;
            color: #fff !important;
            font-size: 16px !important;
            font-weight: 600;
            border: none !important;
            border-radius: 0 !important;
            display: flex !important;
            justify-content: center !important;
            align-items: center !important;
            box-shadow: 0 -2px 10px rgba(43, 151, 242, 0.1);
            z-index: 99;
            text-decoration: none; /* 去除a标签下划线 */
        }

        .submit-btn:active {
            background: linear-gradient(90deg, #1e88e5, #1976d2) !important;
        }

        /* 加载遮罩样式优化 */
        .weui-mask_transparent {
            position: fixed;
            z-index: 9999;
            top: 0;
            right: 0;
            left: 0;
            bottom: 0;
            background: rgba(0, 0, 0, 0.6) !important;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .weui-mask__content {
            text-align: center;
            color: #fff;
            background: rgba(0, 0, 0, 0.8);
            padding: 20px 30px;
            border-radius: 8px;
            min-width: 150px;
        }

        .spin {
            font-size: 24px;
            margin-bottom: 10px;
            animation: spin 2s linear infinite;
        }

        @keyframes spin {
            0% {
                transform: rotate(0deg);
            }
            100% {
                transform: rotate(360deg);
            }
        }

        /* 适配底部按钮的间距 */
        .form-content {
            padding-bottom: 60px;
        }

        /* 提示弹窗样式 */
        .dialog-mask {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0, 0, 0, 0.5);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 10000;
        }

        .dialog-box {
            background: #fff;
            border-radius: 8px;
            width: 80%;
            max-width: 300px;
            padding: 20px;
            text-align: center;
        }

        .dialog-title {
            font-size: 16px;
            font-weight: 600;
            color: #333;
            margin-bottom: 10px;
        }

        .dialog-content {
            font-size: 14px;
            color: #666;
            margin-bottom: 20px;
        }

        .dialog-btn {
            background: #2b97f2;
            color: #fff;
            border: none;
            border-radius: 6px;
            padding: 8px 20px;
            font-size: 14px;
            cursor: pointer;
        }
    </style>
</head>
<body ontouchstart>
<!-- 页面容器：实现整体居中 -->
<div class="page-wrapper">
    <!-- 主表单容器 -->
    <div class="form-container form-content">
        <!-- 表单内容区域 -->
        <div class="weui-cells weui-cells_form">
            <!-- 车牌号输入项（核心优化） -->
            <div class="weui-cell">
                <div class="weui-cell__hd">
                    <label class="weui-label">车牌号码<span class="required-star">*</span></label>
                </div>
                <div class="weui-cell__bd">
                    <input class="weui-input plate-input"
                           id="cphm"
                           type="text"
                           placeholder="请输入车牌号（如：粤A12345）"
                           maxlength="10"
                           onblur="formatPlateInput(this)">
                </div>
            </div>
            <!-- 车牌颜色选择项 -->
            <div class="weui-cell">
                <div class="weui-cell__hd">
                    <label class="weui-label">车牌颜色<span class="required-star">*</span></label>
                </div>
                <div class="weui-cell__bd">
                    <select class="weui-select color-select" id="cpys">
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
        </div>
    </div>
</div>

<!-- 加载遮罩 -->
<div id="loading-overlay" class="weui-mask_transparent" style="display: none;">
    <div class="weui-mask__content">
        <i class="iconfont icon-loading spin">🔄</i> <!-- 替换为通用加载图标，避免依赖iconfont -->
        <p>请等待...</p>
    </div>
</div>

<!-- 提交按钮（固定底部） -->
<a href="javascript:submitVeh()" class="weui-btn weui-btn_primary submit-btn">查询车辆</a>

<!-- 提示弹窗（默认隐藏） -->
<div id="dialog" class="dialog-mask" style="display: none;">
    <div class="dialog-box">
        <div class="dialog-title" id="dialogTitle"></div>
        <div class="dialog-content" id="dialogContent"></div>
        <button class="dialog-btn" onclick="hideDialog()">确定</button>
    </div>
</div>

<script>
    // 初始化FastClick，提升移动端点击体验
    $(function() {
        FastClick.attach(document.body);
    });

    /**
     * 显示提示弹窗
     * @param {string} title 标题
     * @param {string} content 内容
     */
    function dialog(title, content) {
        $("#dialogTitle").text(title);
        $("#dialogContent").text(content);
        $("#dialog").show();
    }
    function dialogcb(title, content, callback) {
        $("#dialogTitle").text(title);
        $("#dialogContent").text(content);
        $("#dialog").show();
        // 绑定确定按钮的点击事件（先解绑避免重复绑定）
        $(".dialog-btn").off("click").on("click", function() {
            hideDialog();
            // 如果有回调函数，执行
            if (typeof callback === "function") {
                callback();
            }
        });
    }

    /**
     * 隐藏提示弹窗
     */
    function hideDialog() {
        $("#dialog").hide();
    }

    /**
     * 重置表单
     */
    function reset() {
        $("#cphm").val("");
        $("#cpys").val("");
    }
    // 格式化车牌号：过滤无效字符 + 仅字母转大写（不影响中文）
    function formatPlateInput(input) {
        // 1. 先过滤无效字符（保留中文、字母、数字）
        let cleanValue = input.value.replace(/[^A-Za-z0-9\u4e00-\u9fa5]/g, '');
        // 2. 只把字母转大写（中文/数字不变）
        let finalValue = cleanValue.replace(/[a-z]/g, function(char) {
            return char.toUpperCase();
        });
        // 3. 赋值回输入框（避免光标位置跑偏）
        input.value = finalValue;
    }
    /**
     * 提交车辆查询
     */
    function submitVeh() {
        // 1. 获取并格式化输入值
        const cphm = $.trim($("#cphm").val()); // 去除首尾空格
        const cpys = $.trim($("#cpys").val());

        // 2. 表单验证
        if (!cphm) {
            dialog("温馨提示", "请输入车牌号");
            $("#cphm").focus(); // 聚焦到车牌号输入框
            return;
        }
        if (!cpys) {
            dialog("温馨提示", "请选择车牌颜色");
            $("#cpys").focus(); // 聚焦到车牌颜色下拉框
            return;
        }

        // 3. 显示加载遮罩
        $("#loading-overlay").show();

        // 4. 发送AJAX请求
        $.ajax({
            type: "post",
            url: '${path}/wechat/queryveh',
            data: {
                "cphm": cphm,       // 修复：改用val()获取输入值
                "cpys": cpys        // 修复：定义cpys变量
            },
            dataType: 'json',
            success: function (data) {
                // 隐藏加载遮罩
                $("#loading-overlay").hide();
                var code=data.code;
                var msg=data.msg;
                console.log(data);
                if(code=="1"){
                    dialog("车辆查询结果","已有此车信息，无需录入")
                }
                else{
                    dialogcb("车辆查询结果","无此车信息，请录入", function() {
                        window.location.href = "${path}/wechat/vehRegNew"; // 目标页面
                    })
                }
            },
            error: function (xhr, status, error) {
                // 隐藏加载遮罩
                $("#loading-overlay").hide();
                dialog("错误", "网络异常，请重试");
                console.error("AJAX请求失败：", status, error); // 调试用
            }
        });
    }
</script>
</body>
</html>