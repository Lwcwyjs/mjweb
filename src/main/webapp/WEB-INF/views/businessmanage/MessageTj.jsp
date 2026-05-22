<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>

<html>
<head>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <script type="text/javascript" src="${staticPath}/static/echarts/js/echarts.js" charset="utf-8"></script>
    <script type="text/javascript" src="${staticPath}/static/module/layui_exts/excel.js" charset="utf-8"></script>
    <style type="text/css">
    </style>
    <title>消息统计</title>
    <style>
        .layui-badge-rim + .layui-badge-rim {
            margin-left: 5px;
        }

        .icon-btn {
            background-color: #2b97f2;
        }

        .layui-form-label {
            width: 60px;
        }

        .layui-form-item .layui-input-inline {
            width: 160px;
        }

        .downpanel .layui-select-title span {
            line-height: 38px;
        }

        /*继承父类颜色*/
        .downpanel dl dd:hover {
            background-color: inherit;
        }

        .downpanel a cite {
            color: white;
        }

        .layui-badge-rim + .layui-badge-rim {
            margin-left: 5px;
        }

        .layui-form-label {
            width: 60px;
        }

        .layui-form-item .layui-input-inline {
            width: 160px;
        }

        .layui-table-view {
            margin: 40px;
        }

        #showTable {
            width: 28px;
            height: 30px;
            padding: 1px 5px;
            cursor: pointer;
            z-index: 100;
        }

        #showChart {
            width: 28px;
            height: 30px;
            padding: 1px 5px;
            cursor: pointer;
            z-index: 100;
        }

        #showLineChart {
            width: 28px;
            height: 30px;
            padding: 1px 5px;
            cursor: pointer;
            z-index: 100;
        }

        .layui-tree-icon {
            color: #f9f8ff;
        }

        .layui-tree-txt {
            color: white;
        }

    </style>
</head>

<div class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-body">
            <div class="layui-form toolbar" id="dataBaseSearchForm">
                <div class="layui-form-item" lay-filter="searchForm">
                    <label class="layui-form-label">客服名称</label>
                    <div class="layui-input-inline">
                        <select id="kfid" name="kfid" lay-filter="shztFilter">
                            <option value="">请选择</option>
                        </select>
                    </div>
                    <label class="layui-form-label">时间</label>
                    <div class="layui-input-inline">
                        <input name="kssj" id="kssj" class="layui-input"/>
                    </div>
                    <div class="layui-input-inline">
                        <input name="jssj" id="jssj" class="layui-input"/>
                    </div>
                    <div class="layui-input-inline">
                        <button type="reset" class="layui-btn">重置</button>
                        <button class="layui-btn" id="userBtnSearch" lay-submit="" lay-filter="searchSubmit">查询
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div id="toolbox" style="position: absolute; right: 83px; height: 32px;">
            <img src="${staticPath}/static/assets/images/chart_show.png" id="showChart">
            <img src="${staticPath}/static/assets/images/show_bg.png" id="showTable">
            <button type="button" lay-submit="" class="layui-btn" id="exportExcel" name="exportExcel">
                <i class="layui-icon"></i>导出Excel
            </button>
        </div>

        <div class="left" id="isShowChart" style="margin: 20px 0;">
            <div style="margin-top: 38px;">
                <div id="ischart" style="width: 100%; height:553px"></div>
            </div>
        </div>
        <div id="isShowTable" style="display: none;">
            <table id="rateTable" style="margin: 20px 0;"></table>
        </div>
    </div>
</div>
</div>
<script type="text/javascript" src="${staticPath}/static/js/colmunFormat.js" charset="utf-8"></script>
<!-- 表格状态列 -->
<script>
    layui.config({
        base: '${staticPath}/static/module/'
    }).use(['form', 'table', 'tree', 'laydate'], function () {

        var $ = layui.jquery;
        var table = layui.table;
        var laydate = layui.laydate;
        var form = layui.form;
        var tree = layui.tree;
        var excel = layui.excel;
        var documentWidth=

        //日期
        laydate.render({
            elem: '#kssj'
        });

        laydate.render({
            elem: '#jssj'
        });
        $('#kssj').val(new Date().Format('yyyy-MM-dd'));
        $('#jssj').val(new Date().Format('yyyy-MM-dd'));

        getParData();
        table.render({
            elem: '#rateTable'
            , url: '${path }/message/tjgrid'
            , where: {
                kssj: $('#kssj').val()
                , jssj: $('#jssj').val()
            }
            ,totalRow: true // 开启合计行
            , method: 'post'
            , request: {
                pageName: 'page' //页码的参数名称，默认：page
                , limitName: 'rows' //每页数据量的参数名，默认：limit
            }
            , response: {
                statusName: 'code' //规定数据状态的字段名称，默认：code
                , statusCode: 0 //规定成功的状态码，默认：0
                , countName: 'total' //规定数据总数的字段名称，默认：count
                , dataName: 'rows' //规定数据列表的字段名称，默认：data
            }
            , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            , cols: [[{field: 'kfname', title: '客服名称', align: 'center', width: "25%", sort: true, totalRowText:"合计:"}
                , {field: 'messagezs', title: '回复消息数', align: 'center', width: "25%",class:'messages', sort: true,totalRow: true}
                , {
                    field: 'wxzs', title: '回复客户数', align: 'center', width:"44%",class:'fixes', sort: true,totalRow: true
                }
            ]]
            , done: function (res, curr, count) {
                // 表格渲染完成后，计算合计结果
                calculateSum();
            }
            , page: true
            , limit: 10
            , limits: [5, 10, 15, 20, 25, 30]
        });
        $.ajax({
            type: "post",
            url: '${path}/message/combox?oi_name=客服',
            dataType: 'json',
            success: function (data) {
                $('#kfid').empty();
                var t;
                var t = "<option value='' selected='selected'>选择客服名称</option>";
                for (var i = 0; i < data.length; i++) {
                    if (data[i].id != undefined && data[i].name != undefined) {
                        t += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
                    }
                }
                $('#kfid').append(t);
                form.render('select');
            }
        });
        // 搜索按钮点击事件
        $('#userBtnSearch').click(function () {
            var kfid = $('#kfid').val();
            var kssj = $('#kssj').val();
            var jssj = $('#jssj').val();


            table.reload('rateTable', {
                where: {
                    "kfid": kfid
                    , "kssj": kssj
                    , "jssj": jssj
                }
            });
            getParData();
        });
        $('#exportExcel').click(function () {
            var kfid = $('#kfid').val();
            var kssj = $('#kssj').val();
            var jssj = $('#jssj').val();
            window.location.href = '${path}/message/exportMessageExcel?kfid=' + kfid + '&kssj=' + kssj + '&jssj=' + jssj;
            // 模拟从后端接口读取需要导出的数据
        });
        // 清空按钮点击事件
        $('#userBtnClear').click(function () {
            $('#kfid').val('');
            $('#kssj').val('');
            $('#jssj').val('');
            form.render('select');
        });
        // 点击表格按钮
        $('#showTable').click(function () {
            // alert("123456");
            $('#showTable').attr("src", "${staticPath}/static/assets/images/table_show.png");
            $('#showChart').attr("src", "${staticPath}/static/assets/images/show_zhu.png");
            $('#isShowChart').css("display", "none");
            $('#isShowTable').css("display", "block");
            chart1.resize();
        });

        // 显示条形图按钮
        $('#showChart').click(function () {
            $('#showTable').attr("src", "${staticPath}/static/assets/images/show_bg.png");
            $('#showChart').attr("src", "${staticPath}/static/assets/images/chart_show.png");
            $('#isShowChart').css("display", "block");
            $('#isShowTable').css("display", "none");
            chart1.resize();
        });
        $(window).resize(function () {
            chart1.resize();
        });
        $('#isShowTable').css("display", "none");
        $('#isShowChart').css("display", "block");
    });
</script>

<script type="text/javascript">

    var chart1 = echarts.init(document.getElementById('ischart'));

    function getParData() {
        var kfid = $('#kfid').val();
        var kssj = $('#kssj').val();
        var jssj = $('#jssj').val();
        $.ajax({
            type: "post",
            url: '${path }/message/tjbar',
            data: {
                "kfid": kfid
                , "kssj": kssj
                , "jssj": jssj
            },
            dataType: 'json',
            success: function (data) {
                console.log(data)
                chart1.setOption(getOptionsForBar(data));
            }
        });
    }

    // 图表数据
    function getOptionsForBar(data) {
        var kfnameArr = data.kfnameArr;
        var messagezsArr = data.messagezsArr;
        var wxzsArr = data.wxzsArr;
        var option = {
            title: {
                text: '消息记录统计表',
                x: 'center',
                y: 'top',
                textStyle: {
                    color: 'black',
                    fontWeight: 'normal'
                }
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            toolbox: {
                feature: {
                    dataView: {show: false, readOnly: false},
                    magicType: {show: false, type: ['line', 'bar']},
                    restore: {show: false},
                    saveAsImage: {show: false}
                },
                right: '80px',
                iconStyle: {
                    normal: {
                        color: 'black' //设置颜色
                    }
                }
            },
            legend: {
                orient: 'horizontal', // 'vertical'
                x: 'center', // 'center' | 'left' | {number},
                y: 'top', // 'center' | 'bottom' | {number}
                top: '38px',
                // backgroundColor: '#fff',
                borderColor: 'rgba(92,217,152,0.8)',
                // borderWidth: 4,
                padding: 10,    // [5, 10, 15, 20]
                itemGap: 20,
                textStyle: {color: 'black'},
                data: ['回复消息数', '回复客户数']
            },
            grid: {
                left: '10%',
                right: '5%',
                top: '12%',
                bottom: '15%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    // axisLabel: {
                    //     interval: 0,
                    //     rotate: 40,
                    //     formatter: function (value) {
                    //         return value.split("").join("\n");
                    //     }
                    // },
                    axisLabel: {
                        color: 'black',
                        interval: 0,
                        formatter: function (params) {
                            var newParamsName = "";

                            var paramsNameNumber = params.length;
                            // 一行显示几个字
                            var provideNumber = 1;
                            var rowNumber = Math.ceil(paramsNameNumber / provideNumber);
                            if (paramsNameNumber > provideNumber) {
                                for (var p = 0; p < rowNumber; p++) {
                                    var tempStr = "";
                                    var start = p * provideNumber;
                                    var end = start + provideNumber;
                                    if (p == rowNumber - 1) {
                                        tempStr = params.substring(start, paramsNameNumber);
                                    } else {
                                        tempStr = params.substring(start, end) + "\n";
                                    }
                                    newParamsName += tempStr;
                                }

                            } else {
                                newParamsName = params;
                            }
                            return newParamsName
                        }
                    },
                    data: kfnameArr,
                    axisPointer: {
                        type: 'shadow'
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    axisLabel: {
                        color: 'black',
                        textStyle: {
                            color: '#000'
                        }
                    }
                }
            ],
            series: [
                {
                    name: '回复消息数',
                    type: 'bar',
                    barGap: '0',
                    stack: '回复消息数',
                    data: messagezsArr,
                    itemStyle: {
                        normal: {
                            color: 'rgb(252,213,91)'
                        }
                    }
                },
                {
                    name: '回复客户数',
                    type: 'bar',
                    stack: '回复客户数',
                    data: wxzsArr,
                    itemStyle: {
                        normal: {
                            color: 'rgb(255,0,0)'
                        }
                    }
                }
            ]
        }
        return option;
    }
    // 计算合计结果
    function calculateSum() {
        var sum = 0;
        $('.messages').each(function(){
            var value = parseFloat($(this).text());
            if (!isNaN(value)) {
                sum += value;
            }
        });
        // 显示合计结果
        $('.layui-table tfoot').html('<tr><td colspan="2">合计：' + sum + '</td></tr>');
    }


</script>
</body>
</html>