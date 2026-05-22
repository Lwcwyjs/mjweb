var csysMap = {"A": "白", "B": "灰", "C": "黄", "D": "粉", "E": "红", "F": "紫", "G": "绿", "H": "蓝", "I": "棕", "J": "黑"};
var syxzMap = {
    "A": "非营运",
    "B": "公路客运",
    "C": "公交客运",
    "D": "出租客运",
    "E": "旅游客运",
    "F": "货运",
    "G": "租赁",
    "H": "警用",
    "I": "消防",
    "J": "救护",
    "K": "工程救险",
    "L": "营转非",
    "M": "出租转非",
    "N": "教练",
    "O": "幼儿校车",
    "P": "小学生校车",
    "Q": "初中生校车",
    "R": "危化品运输",
    "S": "中小学生校车",
    "T": "预约出租客运",
    "U": "预约出租转非"
};
$(function () {
    $("#btgyyDiv").hide();
    $("#syrDiv").hide();
    $("#syxzDiv").hide();
    $("#csysDiv").hide();
    $("#hphmDiv").hide();
    $("#hpzlDiv").hide()
    $("#djzsbhDiv").hide();
    getOrderMsg();
    getConfigPara();
    initConfig();
});

function getOrderMsg() {
    $.ajax({
        type: "GET",
        data: {
            "id": localStorage.getItem("orderId")
        },
        url: pathUri + "/myinfo/orderInfo",
        success: function (result) {
            if (result.code === 1) {
                var data = result.data;
                showDataByYwlx(data);
            } else {
                dialog("错误", result.msg, "")
            }
        }
    });
}

function showDataByYwlx(data) {
    $("#ywlx").val(data.ywlx);
    $("#organ").val(data.organ);
    $("#bookingDate").val(data.bookingDate);
    $("#bookingTime").val(data.bookingTime);
    $("#clsbdh").val(data.clsbdh);
    $("#lszt").val(data.lszt);
    if (data.ywlx === "注册登记") {
        $("#syrDiv").show();
        $("#syr").val(data.syr);
        $("#syxzDiv").show();
        $("#syxz").val(syxzMap[data.syxz]);
        $("#csysDiv").show();
        var csys1 = data.csys1 === null ? "" : csysMap[data.csys1];
        var csys2 = data.csys2 === null ? "" : csysMap[data.csys2];
        var csys3 = data.csys3 === null ? "" : csysMap[data.csys3];
        $("#csys").val(csys1 + "|" + csys2 + "|" + csys3);
    } else {
        $("#hphmDiv").show();
        $("#hphm").val(data.hphm);
        $("#hpzlDiv").show();
        $("#hpzl").val(data.hpzl);
        $("#djzsbhDiv").show();
        $("#djzsbh").val(data.djzsbh)
    }
    //以下流水状态不显示二维码
    var nopassArr = ['A0', 'A1', 'B1', 'B2', 'B3', '90', "100"];
    if (!nopassArr.includes(data.lsztcode)) {
        $("#cylsh").val(data.cylsh)
        if (data.cylsh !== null && data.cylsh !== "") {
            $("#barcode").qrcode({
                render: "canvas",
                width: 180,
                height: 180,
                text: data.cylsh
            });
        }
    }
    //流水状态B1为预约受理不通过
    else if (data.lsztcode === "B1") {
        $("#btgyyDiv").show();
        $("#btgyy").val(data.cylsh) //显示不通过原因
    }
    //可取消预约的状态
    var arr = ['A0', 'A1', '00', '01', '02', '05']
    //已办结的状态
    var arr1 = ['99']
    var html = "";
    if (arr.includes(data.lsztcode)) {
        html = "<a href=\"#\" onclick=\"cancleOrder('" + data.id + "')\" class=\"weui-tabbar__item weui-tabbar__item-lists\">取消预约</a>" +
            "<a href=\"#\" onclick=\"Navigation('" + data.lat + "','" + data.lng + "','" + data.organ + "','" + data.address + "')\" class=\"weui-tabbar__item\">导航至站点</a>";
    } else if (arr1.includes(data.lsztcode)) {
        $("#cylshDiv").hide();
        html = "<a href=\"#\" onclick=\"assess()\" class=\"weui-tabbar__item\">评价本次服务</a>";
    } else {
        $("#cylshDiv").hide();
        html = "<a href=\"#\" onclick=\"Navigation('" + data.lat + "','" + data.lng + "','" + data.organ + "','" + data.address + "')\" class=\"weui-tabbar__item\">导航至站点</a>";
    }
    $("#tabbar").append(html)
}

function getConfigPara() {
    $.ajax({
        type: "GET",
        data: {
            'url': window.location.href
        },
        url: pathUri + "/jsApi/configPara",
        async: false,
        success: function (result) {
            var data = result.data;
            console.log(data);
            appId = data.appId;
            timestamp = data.timestamp;
            nonceStr = data.nonceStr;
            signature = data.signature
        }
    });
}

function initConfig() {
    wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: appId, // 必填，公众号的唯一标识
        timestamp: timestamp, // 必填，生成签名的时间戳
        nonceStr: nonceStr, // 必填，生成签名的随机串
        signature: signature,// 必填，签名
        jsApiList: ["openLocation"] // 必填，需要使用的JS接口列表
    });
}

function Navigation(lat, lng, organ, address) {
    wx.openLocation({
        latitude: parseFloat(lat), // 纬度，浮点数，范围为90 ~ -90
        longitude: parseFloat(lng), // 经度，浮点数，范围为180 ~ -180。
        name: organ, // 位置名
        address: address, // 地址详情说明
        scale: 16, // 地图缩放级别,整形值,范围从1~28。默认为最大
        infoUrl: '' // 在查看位置界面底部显示的超链接,可点击跳转
    });
}

function cancleOrder(id) {
    dialog1("提示", "您确定要取消本次预约么？", function () {
        $.ajax({
            type: "DELETE",
            data: {
                "id": id
            },
            url: pathUri + "/myinfo/orderInfo",
            success: function (result) {
                if (result.code === 1) {
                    dialog("成功", result.data, pathUri + "/myinfo/myOrderPage")
                } else {
                    dialog("错误", result.msg, "")
                }
            }
        });
    })
}

function assess() {
    window.location.href = pathUri + "/myinfo/assessPage"
}