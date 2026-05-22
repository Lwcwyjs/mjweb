var pathUri = window.location.protocol + "//" + window.location.host + "/kfweb";

function dialog(title, msg, callback) {
    var dialog2 = '\
        <div class="weui_dialog_alert" id="dialog2" style="display: none;">\
            <div class="weui_mask"></div>\
           <div class="weui_dialog">\
            <div class="weui_dialog_hd"><strong class="weui_dialog_title">' + title + '</strong></div>\
            <div class="weui_dialog_bd">' + msg + '</div>\
            <div class="weui_dialog_ft">\
             <a href="javascript:;" class="weui_btn_dialog primary">确定</a>\
            </div>\
           </div>\
          </div>\
         ';
    if (!$('#dialog2').length) {
        $('body').append(dialog2);
    } else {
        $('#dialog2 .weui_dialog_title').html(title);
        $('#dialog2 .weui_dialog_bd').html(msg);
    }
    $('#dialog2').fadeIn('fast');
    $('#dialog2 .primary').on('click', function () {
        $('#dialog2').fadeOut('fast');
        var url = callback;
        if (url !== '') {
            window.location.href = url;
        }
    });
}

function dialog1(title, msg, callback) {
    var dialog1 = '\
        <div class="weui_dialog_confirm" id="dialog1" style="display: none;">\
            <div class="weui_mask"></div>\
           <div class="weui_dialog">\
            <div class="weui_dialog_hd"><strong class="weui_dialog_title">' + title + '</strong></div>\
            <div class="weui_dialog_bd">' + msg + '</div>\
            <div class="weui_dialog_ft">\
             <a href="javascript:;" class="weui_btn_dialog primary">确定</a>\
             <a href="javascript:;" class="weui_btn_dialog default">取消</a>\
            </div>\
           </div>\
          </div>\
         ';
    if (!$('#dialog1').length) {
        $('body').append(dialog1);
    } else {
        $('#dialog1 .weui_dialog_title').html(title);
        $('#dialog1 .weui_dialog_bd').html(msg);
    }
    $('#dialog1').fadeIn('fast');
    $('#dialog1 .primary').on('click', function () {
        callback();
    });
    $('#dialog1 .default').on('click', function () {
        $('#dialog1').fadeOut('fast', function () {
            $('#dialog1').remove();
        });
    });
}

function dialog3(title, html, callback) {
    var dialog3 = '\
        <div class="weui_dialog_confirm" id="dialog3" style="display: none;">\
            <div class="weui_mask"></div>\
           <div class="weui_dialog">\
            <div class="weui_dialog_hd"><strong class="weui_dialog_title">' + title + '</strong></div>\
            <div class="weui_dialog_bd">' + html + '</div>\
            <div class="weui_dialog_ft">\
             <a href="javascript:;" class="weui_btn_dialog primary">确定</a>\
             <a href="javascript:;" class="weui_btn_dialog default">取消</a>\
            </div>\
           </div>\
          </div>\
         ';
    if (!$('#dialog3').length) {
        $('body').append(dialog3);
    } else {
        $('#dialog3 .weui_dialog_title').html(title);
        $('#dialog3 .weui_dialog_bd').html(html);
    }
    $('#dialog3').fadeIn('fast');
    $('#dialog3 .primary').on('click', function () {
        if($("#sqcs").val()==""){
            document.getElementById("cs").style.display = "block"
        }else {
            callback();
        }
    });
    $('#dialog3 .default').on('click', function () {
        $('#dialog3').fadeOut('fast', function () {
            $('#dialog3').remove();
        });
    });
}

function backMainPage() {
    window.location.href = pathUri + "/myinfo/chat"
}
function backMyInfoPage() {
    window.location.href =pathUri+"/myinfo/MyInfoPage"
}

function backFoundPage() {
    dialog("提示", "敬请期待", "");
}

function backServicePage() {
    dialog("提示", "敬请期待", "");
}

function backStationPage() {
    dialog("提示", "敬请期待", "");
}