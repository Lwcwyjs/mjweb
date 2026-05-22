var bookingDate = localStorage.getItem("bookingDate");
var bookingTime = localStorage.getItem("bookingTime")
var ywyy = localStorage.getItem("ywyy")
var organ = localStorage.getItem("organ")
var gcjk = localStorage.getItem("gcjk");
$(function () {
    $("#bookingDate").append(bookingDate)
    $("#bookingTime").append(bookingTime)
    getYwyyValue(ywyy);
    getOrganName(organ);
    getFillVehPage();
});

function getYwyyValue(ywyy) {
    $.ajax({
        type: "GET",
        url: pathUri + "/SysCode/oi_value",
        data: {
            "oi_name": "查验业务类型",
            "oi_code": ywyy
        },
        success: function (result) {
            if (result.code === 1) {
                $("#ywyy").append(result.data)
            } else {
                dialog("错误", "获取业务原因代码错误：" + result.msg, "")
            }
        }
    });
}

function getOrganName(organ) {
    $.ajax({
        type: "GET",
        url: pathUri + "/SysOrgan/OrganName",
        data: {
            "organ": organ
        },
        success: function (result) {
            if (result.code === 1) {
                $("#organ").append(result.data)
            } else {
                dialog("错误", "获取机构名称错误：" + result.msg, "")
            }
        }
    });
}

function getFillVehPage() {
    $.ajax({
        type: "GET",
        url: pathUri + "/order/fillVehPage",
        data: {
            "ywyy": ywyy,
            "gcjk": gcjk
        },
        dataType: "html",
        success: function (result) {
            $("#bodyDiv").html(result);
        }
    });
}