var currYhlx;
$(function () {
    $("#gryh_check").hide()
    $("#dbyh_check").hide()
    $("#qyyh_check").hide()
    getCurrYhlx();
    getYyMsg();
});

function getCurrYhlx() {
    $.ajax({
        type: "GET",
        url: pathUri + "/myinfo/CurrYhlx",
        success: function (result) {
            if (result.code === 1) {
                var yhlx = result.data;
                $("#checkedYhlx").val(yhlx);
                currYhlx = yhlx;
                switch (yhlx) {
                    case "1":
                        $("#gryh_check").show()
                        break;
                    case "2":
                        $("#dbyh_check").show()
                        break;
                    case "3":
                        $("#qyyh_check").show()
                        break;
                }
            } else {
                dialog("错误", result.msg, "")
            }
        }
    });
}

function getYyMsg() {
    $.ajax({
        type: "GET",
        url: pathUri + "/myinfo/orderCountOption",
        success: function (result) {
            if (result.code === 1) {
                var data = result.data
                $("#grkyy").append("可预约次数：" + data.gryhkyysl)
                $("#grkqx").append("可取消次数：" + data.gryhkqxsl)
                $("#grksy").append("可爽约次数：" + data.gryhksysl)
                $("#dbkyy").append("可预约次数：" + data.dbyhkyysl)
                $("#dbkqx").append("可取消次数：" + data.dbyhkqxsl)
                $("#dbksy").append("可爽约次数：" + data.dbyhksysl)
                $("#qykyy").append("可预约次数：" + data.qyyhkyysl)
                $("#qykqx").append("可取消次数：" + data.qyyhkqxsl)
                $("#qyksy").append("可爽约次数：" + data.qyyhksysl)
            } else {
                dialog("错误", result.msg, "")
            }
        }
    });
}

function selectYhlx(yhlx) {
    if (yhlx === "1") {
        $("#checkedYhlx").val("1")
        $("#gryh_check").show()
        $("#dbyh_check").hide()
        $("#qyyh_check").hide()
    } else if (yhlx === "2") {
        $("#checkedYhlx").val("2")
        $("#dbyh_check").show()
        $("#gryh_check").hide()
        $("#qyyh_check").hide()
    } else if (yhlx === "3") {
        $("#checkedYhlx").val("3")
        $("#qyyh_check").show()
        $("#dbyh_check").hide()
        $("#gryh_check").hide()
    }
}

function modifyYhlx() {
    var checkedYhlx = $("#checkedYhlx").val();
    if (currYhlx === checkedYhlx) {
        dialog("提示", "您已经是此用户类型，无需变更", "")
    } else {
        $.ajax({
            type: "GET",
            url: pathUri + "/myinfo/checkAllowModifyYhlx",
            success: function (result) {
                if (result.code === 1) {
                    window.location.href = pathUri + "/myinfo/fillYhlxDataPage?yhlx=" + checkedYhlx
                } else {
                    dialog("提示", result.msg, "")
                }
            }
        });
    }
}