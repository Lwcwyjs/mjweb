var csysMap = {"白": "A", "灰": "B", "黄": "C", "粉": "D", "红": "E", "紫": "F", "绿": "G", "蓝": "H", "棕": "I", "黑": "J",};
var syxzMap = {
    "非营运": "A",
    "公路客运": "B",
    "公交客运": "C",
    "出租客运": "D",
    "旅游客运": "E",
    "货运": "F",
    "租赁": "G",
    "警用": "H",
    "消防": "I",
    "救护": "J",
    "工程救险": "K",
    "营转非": "L",
    "出租转非": "M",
    "教练": "N",
    "幼儿校车": "O",
    "小学生校车": "P",
    "初中生校车": "Q",
    "危化品运输": "R",
    "中小学生校车": "S",
    "预约出租客运": "T",
    "预约出租转非": "U",
};
var appId, timestamp, nonceStr, signature;
var hgz = "";
$(function () {
    initSelect();
    getConfigPara();
    initConfig();
});

function initSelect() {
    $("#csys1").select({
        title: "请选择",
        items: ["白", "灰", "黄", "粉", "红", "紫", "绿", "蓝", "棕", "黑"],
        onChange: function (d) {
        }
    });
    $("#csys2").select({
        title: "请选择",
        items: ["白", "灰", "黄", "粉", "红", "紫", "绿", "蓝", "棕", "黑"],
        onChange: function (d) {
        }
    });
    $("#csys3").select({
        title: "请选择",
        items: ["白", "灰", "黄", "粉", "红", "紫", "绿", "蓝", "棕", "黑"],
        onChange: function (d) {
        }
    });
    $("#syxz").select({
        title: "请选择",
        items: ["非营运", "公路客运", "公交客运", "出租客运", "旅游客运", "货运", "租赁", "警用", "消防", "救护", "工程救险", "营转非", "出租转非", "教练", "幼儿校车", "小学生校车", "初中生校车", "危化品运输", "中小学生校车", "预约出租客运", "预约出租转非"],
        onChange: function (d) {
        }
    });
}

function submitVeh() {
    if (checkInput()) {
        $.ajax({
            type: "POST",
            url: pathUri + "/order/OrderData",
            data: {
                "organ": localStorage.getItem("organ"),
                "ywlx": localStorage.getItem("ywyy"),
                "bookingDate": localStorage.getItem("bookingDate"),
                "bookingTime": localStorage.getItem("bookingTime"),
                "hgz": hgz,
                "syr": $("#syr").val(),
                "csys1": csysMap[$("#csys1").val()],
                "csys2": csysMap[$("#csys2").val()],
                "csys3": csysMap[$("#csys3").val()],
                "syxz": syxzMap[$("#syxz").val()],
                "clsbdh": $("#clsbdh").val(),
                "gcjk": localStorage.getItem("gcjk")
            },
            success: function (result) {
                if (result.code === 1) {
                    dialog("成功", result.data, pathUri);
                    localStorage.clear();
                } else {
                    dialog("错误", result.msg, "");
                }
            }
        });
    }
}

function checkInput() {
    if ($("#syr").val() === "") {
        dialog("错误", "请输入所有人名称", "")
        return false;
    }
    if ($("#csys1").val() === "" && $("#csys1").val() === "" && $("#csys1").val() === "") {
        dialog("错误", "请至少选择一个车身颜色", "");
        return false;
    }
    if ($("#syxz").val() === "") {
        dialog("错误", "请选择使用性质", "")
        return false;
    }
    if ($("#clsbdh").val() === "") {
        dialog("错误", "请先扫描合格证二维码", "")
        return false;
    }
    if (hgz === "") {
        dialog("错误", "请先扫描合格证二维码", "")
        return false;
    }
    return true;
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
        jsApiList: ["scanQRCode"] // 必填，需要使用的JS接口列表
    });
}

function scanQrCode() {
    wx.scanQRCode({
        needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
        scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
        success: function (res) {
            hgz = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
            analyzQrCode(res.resultStr);
        }
    });
}

function analyzQrCode(hgz) {
    $.ajax({
        type: "POST",
        data: {
            'hgz': hgz
        },
        url: pathUri + "/order/analyzQrCode",
        success: function (result) {
            if (result.code === 1) {
                $("#clsbdh").val(result.data)
            } else {
                dialog("错误", result.msg, "");
            }
        }
    });
}