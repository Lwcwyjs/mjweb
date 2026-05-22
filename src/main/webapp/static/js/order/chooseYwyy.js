var ywyyMap = new Map();
var hpzlMap = new Map();
$(function () {
    //默认选中国产、不是新能源、不是校车
    localStorage.setItem("gcjk", "0");
    localStorage.setItem("is_xny", "0");
    localStorage.setItem("is_xc", "0");
    //默认选中小型汽车
    localStorage.setItem("hpzl", "02");
    var ywlx = localStorage.getItem('ywlx');
    getYwyyData(ywlx);
    buildOther();
    getHpzlData();
});

function getYwyyData(ywlx) {
    $.ajax({
        type: "GET",
        data: {
            'ywlx': ywlx
        },
        url: pathUri + "/order/YwyyList",
        success: function (result) {
            if (result.code === 1) {
                buildYwyy(result.data)
            } else {
                dialog("错误", result.msg, "/");
            }
        }
    });
}

function getHpzlData() {
    $.ajax({
        type: "GET",
        data: {
            'name': "号牌种类"
        },
        url: pathUri + "/SysCode/list",
        success: function (result) {
            if (result.code === 1) {
                buildHpzl(result.data)
            } else {
                dialog("错误", result.msg, "/");
            }
        }
    });
}

function buildYwyy(data) {
    var ywyyArray = new Array();
    data.forEach(function (element) {
        ywyyMap.set(element.msg, element.code);
        ywyyArray.push(element.msg)
    });
    $("#ywyy").select({
        title: "请选择业务原因",
        items: ywyyArray,
        onChange: function (d) {
            saveLocalStorage("ywyy", ywyyMap.get(d.titles))
        }
    });
}

function buildOther() {
    $("#gcjk").select({
        input: "国产",
        title: "请选择",
        items: ["国产", "进口"],
        onChange: function (d) {
            if (d.titles === "国产") {
                localStorage.setItem("gcjk", "0");
            } else {
                localStorage.setItem("gcjk", "1");
            }
        }
    });
    $("#is_xny").select({
        input: "否",
        title: "请选择",
        items: ["是", "否"],
        onChange: function (d) {
            if (d.titles === "是") {
                localStorage.setItem("is_xny", "1");
            } else {
                localStorage.setItem("is_xny", "0");
            }
        }
    });
    $("#is_xc").select({
        input: "否",
        title: "请选择",
        items: ["是", "否"],
        onChange: function (d) {
            if (d.titles === "是") {
                localStorage.setItem("is_xc", "1");
            } else {
                localStorage.setItem("is_xc", "0");
            }
        }
    });
}

function buildHpzl(data) {
    var hpzlArray = new Array();
    data.forEach(function (element) {
        hpzlMap.set(element.msg, element.code);
        hpzlArray.push(element.msg)
    });
    $("#hpzl").select({
        input: "小型汽车",
        title: "请选择号牌种类",
        items: hpzlArray,
        onChange: function (d) {
            saveLocalStorage("hpzl", hpzlMap.get(d.titles))
        }
    });
}

function saveLocalStorage(name, value) {
    localStorage.setItem(name, value);
}

function Next() {
    var ywyy = localStorage.getItem("ywyy");
    //因为不选择业务原因的话localstorge内存储的是undefined字符串，所以要加双引号
    if (ywyy !== null && ywyy !== "" && ywyy !== "undefined") {
        window.location.href = pathUri + '/order/StationPage';
    } else {
        dialog("提示", "请先选择业务原因", "")
    }
}