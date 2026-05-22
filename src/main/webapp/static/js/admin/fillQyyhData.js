function subYhlx() {
    $.ajax({
        type: "PUT",
        data: {
            company: $("#company").val(),
            tyxydm: $("#tyxydm").val(),
            email: $("#email").val(),
            realname:$("#realname").val()
        },
        url: pathUri + "/myinfo/yhlx_qy",
        success: function (result) {
            if (result.code === 1) {
                dialog("成功", "申请已提交，请等待车管所进行审核。您可以在我的申请中查看进度！", pathUri + "/myinfo/MyInfoPage")
            } else {
                dialog("错误", result.msg, "")
            }
        }
    });
}