$(function () {
    getWxUserInfo();
});

function getWxUserInfo() {
    // $.ajax({
    //     type: "GET",
    //     url: pathUri + "/myinfo/WxUserData",
    //     success: function (result) {
    //         if (result.code === 1) {
    //             var data = result.data;
    //             $("#headImg").attr("src", data.headimgurl);
    //             $("#nickname").append(data.nickName);
    //             if (data.phoneNum == null) {
    //                 var html = "<span onclick='bindPhone()' class=\"weui-cell__ft\">绑定手机</span>";
    //                 $("#nickname").append(html);
    //                 $("#status").append("绑定手机激活");
    //                 $("#sykyysl").append(0);
    //             } else {
    //                 var html = "<span class=\"weui-cell__ft\" onclick='updateYhlx()'>" + data.yhlx + "</span>";
    //                 $("#nickname").append(html);
    //                 $("#status").append(data.status);
    //                 $("#sykyysl").append(data.sykyysl);
    //             }
    //         } else {
    //             dialog("错误", result.msg, "")
    //         }
    //     }
    // });
}

function bindPhone() {
    window.location.href = pathUri + "/myinfo/bindPhonePage"
}

function myOrder() {
    window.location.href = pathUri + "/myinfo/myOrderPage"
}

function myApply() {
    window.location.href = pathUri + "/myinfo/myApplyPage"
}

function myEvaluate() {
    dialog("提示", "敬请期待", "")
}

function updateYhlx() {
    window.location.href = pathUri + "/myinfo/updateYhlxPage"
}

function userMsg() {
    window.location.href = pathUri + "/myinfo/userMsgPage"
}