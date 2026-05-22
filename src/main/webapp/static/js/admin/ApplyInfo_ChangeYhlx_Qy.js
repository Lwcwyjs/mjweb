$(function () {
    getApplyInfo();
});

function getApplyInfo() {
    var id = localStorage.getItem("applyId")
    $.ajax({
        type: "GET",
        data: {
            "id": id
        },
        url: pathUri + "/myinfo/ApplyInfo",
        async: false,
        success: function (result) {
            if (result.code === 1) {
                fillInput(result.data)
            } else {
                dialog("失败", result.msg, "");
            }
        }
    });
}

function fillInput(data) {
    var contentStr = data.apply_content;
    var contentArr = contentStr.split("|"); //字符分割
    $("#company").append(contentArr[0]);
    $("#tyxydm").append(contentArr[1]);
    $("#email").append(contentArr[2]);
    $("#sub_user").append(data.sub_user);
    $("#sub_time").append(data.sub_time);
    $("#deal_time").append(data.deal_time);
    $("#result").append(data.result);
    switch (data.status) {
        case "1":
            $("#status").append("已提交，等待审核");
            break;
        case "2":
            $("#status").append("审核通过");
            break;
        case "3":
            $("#status").append("审核不通过");
            break;
    }
    if (data.status === "1") {
        var html = "<a href=\"#\" onclick=\"cancelApply('" + data.id + "')\" class=\"weui-btn weui-btn_primary\">取消申请</a>"
        $("#body").append(html)
    } else if (data.status === "2") {
        var html = "<a href=\"#\" onclick=\"reApply('" + data.id + "')\" class=\"weui-btn weui-btn_primary\">重新申请</a>"
        $("#body").append(html)
    }
}

function cancelApply(id) {
    $.ajax({
        type: "DELETE",
        data: {
            "id": id
        },
        url: pathUri + "/myinfo/ApplyInfo",
        async: false,
        success: function (result) {
            if (result.code === 1) {
                dialog("成功", "取消申请成功", pathUri + "/myinfo/MyInfoPage")
            } else {
                dialog("失败", result.msg, "");
            }
        }
    });
}

function reApply() {
    window.location.href = pathUri + "/myinfo/updateYhlxPage";
}