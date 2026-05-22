/**
 * 
 */

//获取系统当前时间
Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(),    //day
        "h+": this.getHours(),   //hour       
        "m+": this.getMinutes(), //minute        
        "s+": this.getSeconds(), //second       
        "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter        
        "S": this.getMilliseconds() //millisecond        
    }
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
    return format;

    
}
dateFormat = function (date, format) {
	 
    date = new Date(date);

    var o = {
        'M+' : date.getMonth() + 1, //month
        'd+' : date.getDate(), //day
        'H+' : date.getHours(), //hour
        'm+' : date.getMinutes(), //minute
        's+' : date.getSeconds(), //second
        'q+' : Math.floor((date.getMonth() + 3) / 3), //quarter
        'S' : date.getMilliseconds() //millisecond
    };

    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));

    for (var k in o)
        if (new RegExp('(' + k + ')').test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ('00' + o[k]).substr(('' + o[k]).length));

    return format;
}

//字符串转date格式
function getDate(strDate) {
    var date = eval('new Date(' + strDate.replace(/\d+(?=-[^-]+$)/,
             function (a) { return parseInt(a, 10) - 1; }).match(/\d+/g) + ')');
    return date;
}


//当前日期加减年数、加减天数函数。
//dd：为当前日期，dyear：为添加的年数, dadd：为添加的天数，负数为当前日期减的天数。
function AddDate(dd, dyear, dadd) {
    var OldDate = new Date(dd) //把传过来的字符串转换成日期
    var addYear = OldDate.getFullYear() + dyear; //在原来的日期上加上需要加减的年数
    var Months = OldDate.getMonth() + 1;  //获取原来日期的月
    var Days = OldDate.getDate(); //获取原来日期的天数
    var NewDate = new Date(addYear + "/" + Months + "/" + Days); //重新转换成日期格式
    NewDate = NewDate.valueOf()
    NewDate = NewDate + dadd * 24 * 60 * 60 * 1000
    NewDate = new Date(NewDate)
    return NewDate;
}