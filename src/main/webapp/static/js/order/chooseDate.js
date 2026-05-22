var myCalendar;
$(function () {
    getCalendarMark();
});

function getCalendarMark() {
    $.ajax({
        type: "GET",
        data: {
            'organ': localStorage.getItem('organ')
        },
        url: pathUri + "/order/AvailableNum",
        success: function (result) {
            if (result.code === 1) {
                var data = result.data;
                buildCalendar(data)
            } else {
                dialog("提示", result.msg, pathUri + "/order/StationPage")
            }
        }
    });
}

function buildCalendar(mark) {
    var options = {
        showMark: true, //标记
        timeRange: {
            startYear: 2019,
            endYear: 2049
        },
        mark: mark
    };
    myCalendar = new SimpleCalendar('#calendar', options);
}

function Next() {
    var bookingDate = localStorage.getItem("bookingDate")
    if (bookingDate !== "" && bookingDate !== null) {
        window.location.href = pathUri + '/order/TimePage';
    } else {
        dialog("提示", "请先选择预约日期", "")
    }
}
