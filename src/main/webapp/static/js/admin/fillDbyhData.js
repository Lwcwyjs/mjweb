var organArr = new Array();
var organMap = new Map();
$(function () {
    getCyqList();
    initOrganSelect();
});

function initOrganSelect() {
    console.log(organArr)
    $("#organ").select({
        title: "请选择",
        items: organArr,
        onChange: function (d) {
        }
    });
}

function getCyqList() {
    $.ajax({
        type: "GET",
        url: pathUri + "/myinfo/CyqList",
        async: false,
        success: function (result) {
            var data = result.data;
            for (var i = 0; i < data.length; i++) {
                organArr.push(data[i].organName)
                organMap.set(data[i].organName, data[i].organ)
            }
        }
    });
}

function subYhlx() {
    $.ajax({
        type: "PUT",
        data: {
            organ: $("#organ").val(),
            company: $("#company").val(),
            realname: $("#realname").val(),
            sfzmhm: $("#sfzmhm").val(),
            email: $("#email").val()
        },
        url: pathUri + "/myinfo/yhlx_db",
        success: function (result) {
            if (result.code === 1) {
                dialog("成功", "申请已提交，请等待车管所进行审核。您可以在我的申请中查看进度！", pathUri + "/myinfo/MyInfoPage")
            } else {
                dialog("错误", result.msg, "")
            }
        }
    });
}