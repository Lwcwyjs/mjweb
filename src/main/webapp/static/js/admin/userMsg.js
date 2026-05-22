var yhzt = "";
var realname="";
$(function () {
    $("#sdyyDiv").hide();
    $("#sdqzDiv").hide();
    $("#unclock").hide();
    $("#redutionCancel").hide();
    $("#redutionMiss").hide();
    fillUserData();
});

function fillUserData() {
    $.ajax({
        type: "GET",
        url: pathUri + "/myinfo/userInfoMsg",
        success: function (result) {
            if (result.code === 1) {
                var data = result.data;
                $("#headimgurl").attr("src", data.headimgurl)
                $("#nickname").val(data.nickName)
                $("#yhlx").val(data.yhlx)
                $("#phoneNum").val(data.phoneNum)
                $("#realname").val(data.realname)
                $("#sfzmhm").val(data.sfzmhm)
                $("#status").val(data.status)
                yhzt = data.statuscode;
                realname=data.realname;
                if (data.statuscode === "2") {
                    $("#sdyyDiv").show()
                    $("#sdqzDiv").show()
                    $("#unclock").show();
                    $("#sdyy").val(data.sdyy)
                    $("#sdqz").val(data.sdqz)
                }
                if (data.kqxcs <= 0) {
                    $("#redutionCancel").show();
                }
                if (data.ksycs <= 0) {
                    $("#redutionMiss").show();
                }
                $("#kyycs").val(data.kyycs);
                $("#kqxcs").val(data.kqxcs);
                $("#ksycs").val(data.ksycs);
            } else {
                dialog("提示", result.msg, "")
            }
        }
    });
}

function changeYhlx() {
    window.location.href = pathUri + "/myinfo/updateYhlxPage"
}

function unlock() {
    dialog3("提交申请", "备注：<textarea id='bz' style='width: 100%;height: 60px;' placeholder='选填'></textarea>", function () {
        $.ajax({
            type: "POST",
            data: {
                "apply_kind": "3",
                "apply_content": $("#bz").val()+"|" + $("#realname").val()
            },
            url: pathUri + "/myinfo/wxUserApply",
            success: function (result) {
                if (result.code === 1) {
                    $('#dialog3').remove();
                    dialog("成功", result.data, "")
                } else {
                    $('#dialog3').remove();
                    dialog("错误", result.msg, "")
                }
            }
        });
    });
}

function tempNum() {
    if (yhzt === "1") {
        var html = "<div class=\"weui-cell\">\n" +
            "        <div class=\"weui-cell__hd\"><label class=\"weui-label\">申请次数</label></div>\n" +
            "        <div class=\"weui-cell__bd\">\n" +
            "            <input class=\"weui-input\" id=\"sqcs\" type=\"text\">\n" +
            "        </div>\n" +
            "    </div>\n" +
            "    <div class=\"weui-cell\">\n" +
            "        <div class=\"weui-cell__hd\"><label class=\"weui-label\">申请原因</label></div>\n" +
            "        <div class=\"weui-cell__bd\">\n" +
            "            <input class=\"weui-input\" id=\"sqyy\" type=\"text\">\n" +
            "        </div>\n" +
            "    </div>\n"+
            "    <div class=\"weui-cell\">\n" +
            "        <div class=\"weui-cell__hd\"><label class=\"weui-label\" id=\"cs\" style=\"display: none;\">请输入申请次数</label></div>\n" +
            "    </div>";
        dialog3("提交申请", html, function () {
            $.ajax({
                type: "POST",
                data: {
                    "apply_kind": "4",
                    "apply_content": $("#sqcs").val() + "|" + $("#sqyy").val()+"|" + realname
                },
                url: pathUri + "/myinfo/wxUserApply",
                success: function (result) {
                    if (result.code === 1) {
                        $('#dialog3').remove();
                        dialog("成功", result.data, "")
                    } else {
                        $('#dialog3').remove();
                        dialog("错误", result.msg, "")
                    }
                }
            });
        });
    } else {
        dialog("提示", "用户已锁定，请先解锁用户", "")
    }
}

function redution(type) {
    if (yhzt === "1") {
        var html = "<div class=\"weui-cell\">\n" +
            "        <div class=\"weui-cell__hd\"><label class=\"weui-label\">备注</label></div>\n" +
            "        <div class=\"weui-cell__bd\">\n" +
            "            <input class=\"weui-input\" id=\"bz\" type=\"text\">\n" +
            "        </div>\n" +
            "    </div>";
        dialog3("提交申请", html, function () {
            $.ajax({
                type: "POST",
                data: {
                    "apply_kind": type,
                    "apply_content": $("#bz").val()+"|" + realname
                },
                url: pathUri + "/myinfo/wxUserApply",
                success: function (result) {
                    if (result.code === 1) {
                        $('#dialog3').remove();
                        dialog("成功", result.data, "")
                    } else {
                        $('#dialog3').remove();
                        dialog("错误", result.msg, "")
                    }
                }
            });
        });
    } else {
        dialog("提示", "用户已锁定，请先解锁用户", "")
    }
}
