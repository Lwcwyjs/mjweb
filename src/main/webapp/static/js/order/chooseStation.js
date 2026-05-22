var map;
var appId, timestamp, nonceStr, signature;
var lat, lng;
var driving;
$(function () {
    getConfigPara();
    initConfig();
});

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
        jsApiList: ["getLocation"] // 必填，需要使用的JS接口列表
    });
    wx.ready(function () {
        wx.getLocation({
            type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
            success: function (res) {
                lat = res.latitude === undefined ? "39.90923" : res.latitude; // 纬度，浮点数，范围为90 ~ -90
                lng = res.longitude === undefined ? "116.397428" : res.longitude; // 经度，浮点数，范围为180 ~ -180。
                buildMap(lat, lng);
                getNearStation(lat, lng);
            }
        });
    });
}

function buildMap(lat, lng) {
    map = new AMap.Map('container', {
        resizeEnable: true, //是否监控地图容器尺寸变化
        zoom: 15, //初始化地图层级
        center: [lng, lat], //初始化地图中心点
        dragEnable: true
    });
    var marker = new AMap.Marker({
        position: new AMap.LngLat(lng, lat),   // 经纬度对象，也可以是经纬度构成的一维数组[116.39, 39.9]
        title: '我的位置'
    });
    map.add(marker);
}

function getNearStation(lat, lng) {
    $.ajax({
        type: "GET",
        data: {
            "lat": lat,
            "lng": lng,
            "ywyy": localStorage.getItem("ywyy"),
            "gcjk": localStorage.getItem("gcjk"),
            "hpzl": localStorage.getItem("hpzl"),
            "is_xc": localStorage.getItem("is_xc"),
            "is_xny": localStorage.getItem("is_xny")
        },
        url: pathUri + "/order/StationList",
        success: function (result) {
            var data = result.data
            buildStationList(data);
        }
    });
}

function buildStationList(data) {
    for (var i = 0; i < data.length; i++) {
        var a = '<a class="weui-cell weui-cell_access"><div onclick="Call(' + data[i].phone + ')" class="weui-cell__hd">' +
            '<img src="../images/icon-help2.png" alt="" style="width:30px;margin-right:5px;display:block"></div>' +
            '<div onclick="RoutePlan(' + data[i].lng + ',' + data[i].lat + ')" class="weui-cell__bd weui-cell_primary">' +
            '<p>' + data[i].organName + '</p><p class="weui-media-box__desc">工作时间：8:30~18:00</p>' +
            '<p class="weui-media-box__desc">地址：' + data[i].address + '</p></div>' +
            '<div onclick="toCheckDatePage(\'' + data[i].organ + '\')" class="weui-tabbar__icon"><img src="../images/st-1.png">' +
            '<p class="weui-tabbar__label">预约</p></div></a>';
        $("#stationList").append(a);
    }
}

function RoutePlan(stationLng, stationLat) {
    if (driving) {
        driving.clear();
    }
    driving = new AMap.Driving({
        map: map
    });
    driving.search(new AMap.LngLat(lng, lat), new AMap.LngLat(stationLng, stationLat), function (status, result) {
        if (status === 'complete') {
        } else {
            dialog("错误", "绘制驾车路线失败", "");
        }
    });
}

function Call(phone) {
    window.location.href = "tel://" + phone;
}

function toCheckDatePage(organ) {
    localStorage.setItem('organ', organ);
    window.location.href = pathUri + '/order/DatePage';
}