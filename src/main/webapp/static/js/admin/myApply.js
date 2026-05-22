$(function () {
    getApplyList();
});

function getApplyList() {
    $.ajax({
        type: "GET",
        url: pathUri + "/myinfo/application",
        success: function (result) {
            console.log(result)
            if (result.code === 1) {
                var data = result.data
                if (data.length === 0) {
                    var html = "<center>没有更多记录了</center>";
                    $("#body").append(html)
                } else {
                    for (var i = 0; i < data.length; i++) {
                        var html = "<div class=\"weui-panel weui-panel_access\"><div class=\"weui-panel__ft\">" +
                            "<a href=\"#\" onclick=\"getInfoMsg('" + data[i].id + "')\" class=\"weui-cell weui-cell_access weui-cell_link\">" +
                            "<div class=\"weui-cell__bd\"><img src=\"/neworder/images/icon_nav_panel.png\" style=\"float:left;" +
                            "margin-right:5px;width:22px\">" + data[i].apply_kind + "</div>\n" +
                            "<span class=\"weui-cell__ft\">" + data[i].status + "</span></a></div><div class=\"weui-panel__bd\">\n" +
                            "<div class=\"weui-media-box weui-media-box_text\">\n" +
                            "<p class=\"weui-media-box__desc\"><img src=\"/neworder/images/icon_nav_icons.png\" style=\"margin-right:11px;width:15px\" alt=\"\">提交时间：" + data[i].sub_time + "</p>\n" +
                            "</div></div></div>";
                        $("#body").append(html)
                    }
                }
            } else {
                dialog("错误", result.msg, "")
            }
        }
    });
}

function getInfoMsg(id) {
    localStorage.setItem("applyId", id);
    window.location.href = pathUri + "/myinfo/ApplyInfoPage?id=" + id
}
