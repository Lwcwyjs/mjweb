$(function () {
    //加载动态轮播图
    $(".swiper-container").swiper({
        loop: true,
        autoplay: 3000
    });
    $(".t_news").swiper({
        loop: true,
        autoplay: 5000
    });
});

function selectYwyy(ywlx) {
    $.ajax({
        type: "GET",
        url: pathUri + "/order/checkAccessOrder",
        success: function (result) {
            if (result.code === 1) {
                localStorage.setItem('ywlx', ywlx);
                window.location.href = pathUri + '/order/YwyyPage';
            } else {
                dialog("提示", result.msg, "");
            }
        }
    });
}