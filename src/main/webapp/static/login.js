// 判断时候在Iframe框架内,在则刷新父页面
if (self != top) {
    parent.location.reload(true);
    if (!!(window.attachEvent && !window.opera)) {
        document.execCommand("stop");
    } else {
        window.stop();
    }
}

$(function () {
    // 得到焦点
    $("#password").focus(function () {
        $("#left_hand").animate({
            left: "150",
            top: " -38"
        }, {
            step: function () {
                if (parseInt($("#left_hand").css("left")) > 140) {
                    $("#left_hand").attr("class", "left_hand");
                }
            }
        }, 2000);
        $("#right_hand").animate({
            right: "-64",
            top: "-38px"
        }, {
            step: function () {
                if (parseInt($("#right_hand").css("right")) > -70) {
                    $("#right_hand").attr("class", "right_hand");
                }
            }
        }, 2000);
    });
    // 失去焦点
    $("#password").blur(function () {
        $("#left_hand").attr("class", "initial_left_hand");
        $("#left_hand").attr("style", "left:100px;top:-12px;");
        $("#right_hand").attr("class", "initial_right_hand");
        $("#right_hand").attr("style", "right:-112px;top:-12px");
    });
});

// var index = 1;
$(function () {
    $("#imgcode").attr("src",
        basePath + "/front/register/code/" + Math.random());
    $("#imgcode").css("display", "block");
});

function kickout() {
    var href = location.href;
    if (href.indexOf("kickout") > 0) {
        //alert();
        toastr.info("您的账号在另一台设备上登录，您被挤下线，若不是您本人操作，请立即修改密码！")
    }
    if (href.indexOf("timeout") > 0) {
        //alert();
        toastr.info("会话超期，请重新登陆！")
    }
}

window.onload = function () {
    kickout();
};

function reset() {
    $("#loginname").val("");

    $("#password").val("");

}

function nologin() {
    location.href = basePath + "/guest";
}

document.onkeyup = function (e) {      //onkeyup是javascript的一个事件、当按下某个键弹起 var _key;                                                 //的时触发
    if (e == null) { // ie
        _key = event.keyCode;
    } else { // firefox              //获取你按下键的keyCode
        _key = e.which;          //每个键的keyCode是不一样的
    }
    if (_key == 13) {   //判断keyCode是否是13，也就是回车键(回车的keyCode是13)
        //if (validator(document.loginform)){ //这个因该是调用了一个验证函数
        document.getElementById('login').click()    //验证成功触发一个Id为btnLogin的
        //}                                                                        //按钮的click事件，达到提交表单的目的
    }
}


layui.config({
    base: basePath + 'static/assets/module/'
}).use(['form'], function () {

    // let uuid = Math.uuid();

    var $ = layui.jquery;

    // $("input[name='deviceId']").val(uuid);

    var form = layui.form;

    // 表单提交
    form.on('submit(login-submit)', function (obj) {
        var field = obj.field;
        field.grant_type = 'password';
        field.scope = 'app';
        field.client_id = 'webApp';
        field.client_secret = 'webApp';
        // field.uuid = uuid;

        field.username = userEncrypt(field.username);
        field.password = userEncrypt(field.password);
        field.verifycode = userEncrypt(field.verifycode);

        layer.load(2);
        $.ajax({
            url: basePath + '/admin/login',
            data: field,
            type: 'POST',
            dataType: 'JSON',
            success: function (result) {
                if (result.success) {
                    layer.msg('登录成功', {icon: 1, time: 500}, function () {
                        window.location.href = basePath
                            + '/admin/index';
                        /*if (getPasswordStrong($("#password").val())) {
                            window.location.href = basePath
                                + '/admin/index';
                        } else {
                            window.location.href = basePath
                                + '/admin/updatePwd';
                        }*/
                    });


                } else {
                    var str = result.msg.split(':');
                    layer.msg(str[0], {icon: 5, time: 1500});
                    layer.closeAll('loading');

                    if (str[1] != undefined) {
                        timer(str[1])
                        var odiv = document.getElementById("time-item");
                        odiv.style.display = "block";
                    }

                    // 图形验证码
                    $('.login-captcha').attr("src", basePath + "/front/register/code/" + Math.random());
                    $('.login-captcha').attr("style", "");
                }
            },
            error: function (xhr) {
                layer.closeAll('loading');
                console.log(xhr);
                if (xhr.status == 400) {
                    layer.msg('账号或密码错误', {icon: 5, time: 500});
                } else if (xhr.status == 401) {
                    layer.msg('验证码异常', {icon: 5, time: 500});
                }
                else if (xhr.status == 500) {
                    layer.msg('服务器异常,请联系管理员', {icon: 5, time: 500});
                } else if (xhr.status == 0) {
                    layer.msg('网关异常,请联系管理员', {icon: 5, time: 900});
                }
            }
        });
        return false; //阻止表单跳转
    });
    // 表单提交
    //     form.on('submit(login-submit)', function (obj) {
    //         var basePath="http://27.129.145.98:18088/mjweb";
    //         var field = obj.field;
    //         $.ajax({
    //             url: basePath + '/admin/loginex',
    //             data: field,
    //             type: 'POST',
    //             dataType: 'JSON',
    //             // 关键：允许跨域携带Cookie
    //             xhrFields: {
    //                 withCredentials: true
    //             },
    //             crossDomain: true,
    //             success: function (result) {
    //                 if (result.success) {
    //                     layer.msg('登录成功', {icon: 1, time: 500}, function () {
    //                         window.location.href = basePath
    //                             + '/admin/index';
    //                     });
    //                 } else {
    //                     var str = result.msg.split(':');
    //                     alert(str[0]);
    //                 }
    //             },
    //             error: function (xhr) {
    //                 if (xhr.status == 400) {
    //                     alert('账号或密码错误');
    //                 } else if (xhr.status == 401) {
    //                     alert('验证码异常');
    //                 }
    //                 else if (xhr.status == 500) {
    //                     alert('服务器异常,请联系管理员');
    //                 } else if (xhr.status == 0) {
    //                     alert('网关异常,请联系管理员');
    //                 }
    //             }
    //         });
    //         return false; //阻止表单跳转
    //     });

    // 图形验证码
    //$('.login-captcha').attr("src", basePath + "/front/register/code/" + Math.random());
    //$('.login-captcha').attr("style", "");

    // // 图形验证码
    // $('.login-captcha').click(function () {
    //     this.src = this.src + '?timestamp=' + (new Date).getTime();
    // });

});