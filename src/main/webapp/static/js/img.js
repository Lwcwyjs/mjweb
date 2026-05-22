/********************
*基本描述：
* 图片缩略,使用js的静态类实现
* by 西楼冷月 20080817 | | QQ : 39949376
*参数说明：
* @ im : 可以为image对象或img的id
*基本功能:
* Img(im).Resize(nWidth,nHeight)             : 按给定的宽和高进行智能缩小
* Img(im).ResizedByWH(nWidth,nHeight)   : 按给定的宽和高进行固定缩小(会出现图片变形情况)
* Img(im).ResizedByWidth(nWidth)             : 按给定的宽进行等比例缩小
* Img(im).ResizedByHeight(nHeight)           : 按给定的高进行等比例缩小
* Img(im).ResizedByPer(nWidthPer,nHeightPer) : 宽和高按百分比缩小
*使用例子:
* <img src="demo.gif" />
* <img src="demo.gif" />
********************/
function Img(im)
{
    ImgCls.Obj = ( im && typeof im == 'object' ) ? im : document.getElementById(im) ;
    return ImgCls ;
}
var ImgCls =
{
    Obj : null ,
    
    //按给定的宽和高进行智能缩小
    Resize : function ( nWidth , nHeight )
    {
        var w , h , p1 , p2 ;
        //计算宽和高的比例
        p1 = nWidth / nHeight ;
        p2 = ImgCls.Obj.width / ImgCls.Obj.height ;
        w = 0 ; h = 0 ;
        if( p1 < p2 )
        {
            //按宽度来计算新图片的宽和高
            w = nWidth ;
            h = nWidth * ( 1 / p2 ) ;
        }
        else
        {
            //按高度来计算新图片的宽和高
            h = nHeight ;
            w = nHeight * p2 ;
        }
        ImgCls.Obj.width  = w ;
        ImgCls.Obj.height = h ;
    },
    
    //按给定的宽和高进行固定缩小(会出现图片变形情况)
    //http://bizhi.knowsky.com/
    ResizedByWH : function ( nWidth , nHeight )
    {
        ImgCls.Obj.width  = nWidth ;
        ImgCls.Obj.height = nHeight ;
    },
    
    //按给定的宽进行等比例缩小
    ResizedByWidth : function ( nWidth )
    {
        var p = nWidth / ImgCls.Obj.width ;
        ImgCls.Obj.width  = nWidth ;
        ImgCls.Obj.height = parseInt ( ImgCls.Obj.height * p ) ;
    },
    
    //按给定的高进行等比例缩小
    ResizedByHeight : function ( nHeight )
    {
        var p = nHeight / ImgCls.Obj.height ;
        ImgCls.Obj.height  = nHeight ;
        ImgCls.Obj.width = parseInt ( ImgCls.Obj.width * p ) ;
    },
    
    //宽和高按百分比缩小
    ResizedByPer : function ( nWidthPer , nHeightPer )
    {
        ImgCls.Obj.width  = parseInt(ImgCls.Obj.width * nWidthPer / 100) ;
        ImgCls.Obj.height = parseInt(ImgCls.Obj.height * nHeightPer / 100) ;
    }
}

