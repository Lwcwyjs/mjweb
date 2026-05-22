$(function () {
    getLocalHpqz();
});

function submitVeh() {
    if (checkInput()) {
        $.ajax({
            type: "POST",
            url: pathUri + "/order/OrderData",
            data: {
                "organ": localStorage.getItem("organ"),
                "ywlx": localStorage.getItem("ywyy"),
                "bookingDate": localStorage.getItem("bookingDate"),
                "bookingTime": localStorage.getItem("bookingTime"),
                "hpzl": localStorage.getItem("hpzl"),
                "hphm": $("#hpqz").val() + $("#qzzm").val() + $("#hphm").val(),
                "clsbdh": $("#clsbdh").val()
            },
            success: function (result) {
                if (result.code === 1) {
                    dialog("成功", result.data, pathUri);
                    localStorage.clear();
                }
            }
        });
    }
}

function getLocalHpqz() {
    $.ajax({
        type: "GET",
        url: pathUri + "/SysOption/option_code",
        data: {
            "option_kind": "系统设置",
            "option_name": "号牌前缀"
        },
        success: function (result) {
            if (result.code === 1) {
                buildHpqz(result.data);
            } else {
                dialog("错误", result.msg, "");
            }
        }
    });
}

function buildHpqz(hpqz) {
    $("#hpqz").select({
        input: hpqz.substring(0, 1),
        title: "请选择",
        items: ["京", "津", "沪", "冀", "豫", "云", "辽", "黑", "湘", "皖", "鲁", "新", "苏", "浙", "赣", "鄂", "桂", "甘", "晋", "蒙", "陕", "吉", "闽", "贵", "粤", "川", "青", "藏", "琼", "宁", "渝", "港", "澳"]
    });
    $("#qzzm").select({
        input: hpqz.substring(1, 2),
        title: "请选择",
        items: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]
    });
}

function checkInput() {
    if ($("#clsbdh").val().length === 4) {
        return true;
    } else {
        dialog("错误", "车辆识别代号长度不正确", "")
        return false;
    }

}

function toUpperCase(obj) {
    obj.value = obj.value.toUpperCase()
}