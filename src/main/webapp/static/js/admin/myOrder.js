var currPage = 1;
$(function () {
    getWaitOrder();
    getHistory(currPage);
});

function getWaitOrder() {
    $.ajax({
        type: "GET",
        url: pathUri + "/myinfo/waitOrder",
        success: function (result) {
            console.log(result)
            if (result.code === 1) {
                var data = result.data
                if (data.length === 0) {
                    var html = "<center>没有更多记录了</center>";
                    $("#tab1").append(html)
                } else {
                    for (var i = 0; i < data.length; i++) {
                        var clbs = data[i].hphm === null || data[i].hphm === "" ? data[i].clsbdh : data[i].hphm;
                        var html = "<div class=\"weui-panel weui-panel_access\"><div class=\"weui-panel__ft\">" +
                            "<a href=\"#\" onclick=\"getOrderMsg('" + data[i].id + "')\" class=\"weui-cell weui-cell_access weui-cell_link\">" +
                            "<div class=\"weui-cell__bd\"><img src=\"/neworder/images/store_03.png\" style=\"float:left;" +
                            "margin-right:5px;width:22px\">" + clbs + "</div>\n" +
                            "<span class=\"weui-cell__ft\">" + data[i].lszt + "</span></a></div><div class=\"weui-panel__bd\">\n" +
                            "<div class=\"weui-media-box weui-media-box_text\">\n" +
                            "<p class=\"weui-media-box__desc\"><img src=\"/neworder/images/icon_nav_icons.png\" style=\"margin-right:11px;width:15px\" alt=\"\">预约时间：" + data[i].bookingDate + "  " + data[i].bookingTime + "</p>\n" +
                            "<p class=\"weui-media-box__desc\"><img src=\"/neworder/images/store_icon.png\" style=\"margin-right:11px;width:15px\" alt=\"\">预约站点：" + data[i].organ + "</p>\n" +
                            "<p class=\"weui-media-box__desc\"><img src=\"/neworder/images/icon_nav_panel.png\" style=\"margin-right:11px;width:15px\" alt=\"\">预约业务：" + data[i].ywlx + "</p>\n" +
                            "</div></div></div>";
                        $("#tab1").append(html)
                    }
                }
            } else {
                dialog("错误", result.msg, "")
            }
        }
    });
}

function getHistory(page) {
    $.ajax({
        type: "GET",
        data: {
            "currPage": page
        },
        url: pathUri + "/myinfo/historyOrder",
        success: function (result) {
            if (result.code === 1) {
                var data = result.data;
                if (data.length === 0) {
                    $("#sljz").empty();
                    $("#sljz").append("没有更多记录了")
                } else {
                    for (var i = 0; i < data.length; i++) {
                        var clbs = data[i].hphm === null || data[i].hphm === "" ? data[i].clsbdh : data[i].hphm;
                        var html = "<div class=\"weui-panel weui-panel_access\"><div class=\"weui-panel__ft\">" +
                            "<a href=\"#\" onclick=\"getOrderMsg('" + data[i].id + "')\" class=\"weui-cell weui-cell_access weui-cell_link\">" +
                            "<div class=\"weui-cell__bd\"><img src=\"/neworder/images/store_03.png\" style=\"float:left;" +
                            "margin-right:5px;width:22px\">" + clbs + "</div>\n" +
                            "<span class=\"weui-cell__ft\">" + data[i].lszt + "</span></a></div><div class=\"weui-panel__bd\">\n" +
                            "<div class=\"weui-media-box weui-media-box_text\">\n" +
                            "<p class=\"weui-media-box__desc\"><img src=\"/neworder/images/icon_nav_icons.png\" style=\"margin-right:11px;width:15px\" alt=\"\">预约时间：" + data[i].bookingDate + "  " + data[i].bookingTime + "</p>\n" +
                            "<p class=\"weui-media-box__desc\"><img src=\"/neworder/images/store_icon.png\" style=\"margin-right:11px;width:15px\" alt=\"\">预约站点：" + data[i].organ + "</p>\n" +
                            "<p class=\"weui-media-box__desc\"><img src=\"/neworder/images/icon_nav_panel.png\" style=\"margin-right:11px;width:15px\" alt=\"\">预约业务：" + data[i].ywlx + "</p>\n" +
                            "</div></div></div>";
                        $("#tab2").append(html)
                    }
                    var loading = "<center class='center'><span id=\"sljz\" onclick='fetchData()'>点击加载更多</span></center>";
                    $("#tab2").append(loading)
                    currPage += 1;
                }
            } else {
                dialog("错误", result.msg, "")
            }
        }
    });
}

function getOrderMsg(id) {
    window.location.href = pathUri + "/myinfo/orderinfoPage";
    localStorage.setItem("orderId", id);
}

function fetchData() {
    $(".center").empty()
    getHistory(currPage);
}