var Week = ['日', '一', '二', '三', '四', '五', '六'];
var bookingDate = localStorage.getItem("bookingDate");
var organ = localStorage.getItem("organ");
$(function () {
    buildTitle();
    getTimeData();
});

function buildTitle() {
    if (bookingDate !== null && bookingDate !== "") {
        $("#bookingDate").append(bookingDate)
        var week = '星期' + Week[new Date(bookingDate).getDay()];
        $("#week").append(week)
    } else {
        dialog("提示", "请先选择预约日期", "")
    }
}

function getTimeData() {
    $.ajax({
        type: "GET",
        data: {
            'bookingDate': bookingDate,
            'organ': organ
        },
        url: pathUri + "/order/TimeList",
        success: function (result) {
            if (result.code === 1) {
                var now = new Date();
                var hour = now.getHours();
                var date = stringToDate(localStorage.getItem("bookingDate"), "-");
                if (date.getFullYear() == now.getFullYear() && date.getMonth() == now.getMonth() && date.getDay() == now.getDay()) {
                    if (hour >= 11 && hour < 17) {
                        var data = result.data;
                        if (data["pm"] !== undefined) {
                            var html = '<label onclick="choose(\'下午\')" class="weui-cell weui-check__label" for="pm"><div class="weui-cell__bd"><p>下午</p>' +
                                '</div><span style="color: #3c3f45">' + data["pm"].remainder + '/' + data["pm"].AllCount + '</span><div class="weui-cell__ft">' +
                                '<input type="radio" class="weui-check" name="radio1" id="pm">' +
                                '<span class="weui-icon-checked"></span></div></label>'
                            $("#timeList").append(html)
                        }
                    } else if (hour < 11) {
                        var data = result.data;
                        if (data["am"] !== undefined) {
                            var html = '<label onclick="choose(\'上午\')" class="weui-cell weui-check__label" for="am"><div class="weui-cell__bd"><p>上午</p>' +
                                '</div><span style="color: #3c3f45">' + data["am"].remainder + '/' + data["am"].AllCount + '</span><div class="weui-cell__ft">' +
                                '<input type="radio" class="weui-check" name="radio1" id="am">' +
                                '<span class="weui-icon-checked"></span></div></label>'
                            $("#timeList").append(html)
                        }
                        if (data["pm"] !== undefined) {
                            var html = '<label onclick="choose(\'下午\')" class="weui-cell weui-check__label" for="pm"><div class="weui-cell__bd"><p>下午</p>' +
                                '</div><span style="color: #3c3f45">' + data["pm"].remainder + '/' + data["pm"].AllCount + '</span><div class="weui-cell__ft">' +
                                '<input type="radio" class="weui-check" name="radio1" id="pm">' +
                                '<span class="weui-icon-checked"></span></div></label>'
                            $("#timeList").append(html)
                        }
                    }
                } else {
                    var data = result.data;
                    if (data["am"] !== undefined) {
                        var html = '<label onclick="choose(\'上午\')" class="weui-cell weui-check__label" for="am"><div class="weui-cell__bd"><p>上午</p>' +
                            '</div><span style="color: #3c3f45">' + data["am"].remainder + '/' + data["am"].AllCount + '</span><div class="weui-cell__ft">' +
                            '<input type="radio" class="weui-check" name="radio1" id="am">' +
                            '<span class="weui-icon-checked"></span></div></label>'
                        $("#timeList").append(html)
                    }
                    if (data["pm"] !== undefined) {
                        var html = '<label onclick="choose(\'下午\')" class="weui-cell weui-check__label" for="pm"><div class="weui-cell__bd"><p>下午</p>' +
                            '</div><span style="color: #3c3f45">' + data["pm"].remainder + '/' + data["pm"].AllCount + '</span><div class="weui-cell__ft">' +
                            '<input type="radio" class="weui-check" name="radio1" id="pm">' +
                            '<span class="weui-icon-checked"></span></div></label>'
                        $("#timeList").append(html)
                    }
                }
            } else {
                dialog("错误", result.msg, "");
            }
        }
    });
}

function choose(bookingTime) {
    localStorage.setItem("bookingTime", bookingTime);
}

function Next() {
    var bookingTime = localStorage.getItem("bookingTime");
    if (bookingTime !== null && bookingTime !== "") {
        window.location.href = pathUri + "/order/VehDataPage"
    } else {
        dialog("提示", "请先选择预约时间段", "")
    }
}

function stringToDate(dateStr, separator) {
    if (!separator) {
        separator = "-";
    }
    var dateArr = dateStr.split(separator);
    var year = parseInt(dateArr[0]);
    var month;
    if (dateArr[1].indexOf("0") == 0) {
        month = parseInt(dateArr[1].substring(1));
    } else {
        month = parseInt(dateArr[1]);
    }
    var day = parseInt(dateArr[2]);
    var date = new Date(year, month - 1, day);
    return date;
}