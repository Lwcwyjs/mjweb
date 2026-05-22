<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <%--<%@ include file="/commons/basejs.jsp"%>--%>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <%--    <meta http-equiv="X-UA-Compatible" content="edge"/>--%>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <script type="text/javascript" src="${staticPath}/static/echarts/js/echarts.min.js" charset="utf-8"></script>
    <title>首页</title>
    <style>
        .layui-badge-rim + .layui-badge-rim {
            margin-left: 5px;
        }

        .icon-btn {
            background: none;
        }

        .layui-laypage a, .layui-laypage span {
            display: inline-block;
            *display: inline;
            *zoom: 1;
            vertical-align: middle;
            padding: 0 15px;
            height: 28px;
            line-height: 28px;
            margin: 0 -1px 5px 0;
            background-color: #fff;
            color: white;
            font-size: 14px;
        }

        .layui-form-label {
            width: 60px;
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

        .icon-btn {
            background: none;
        }

        .layui-laypage a, .layui-laypage span {
            display: inline-block;
            *display: inline;
            *zoom: 1;
            vertical-align: middle;
            padding: 0 15px;
            height: 28px;
            line-height: 28px;
            margin: 0 -1px 5px 0;
            background-color: #fff;
            color: white;
            font-size: 14px;
        }

        .layui-logo {
            /*width: 290px;*/
            background-color: #293a5e;
            color: #f2f2f2;
            text-align: center;
            font-size: 30px;
            font-family: Myriad Pro, Helvetica Neue, Arial, Helvetica, sans-serif;
            font-weight: bold;
            overflow: hidden;
            line-height: 60px;
            transition: all .3s;
            white-space: nowrap;
            box-shadow: 1px 0 2px 0 rgba(0, 0, 0, .05)
        }

        .layui-logo img {
            height: 35px
        }

        .layui-logo cite {
            font-style: normal;
            font-size: 30px;
            margin: 0 8px;
            color: white;
        }

        .layui-tree-icon {
            color: #f9f8ff;
        }

        .layui-tree-txt {
            color: white;
        }

    </style>

</head>
<body>
<div class="layui-fluid">
    <div class="layui-card">
        <div class="layui-row">
            <div class="layui-col-xs4 layui-col-sm12 layui-col-md12" id="bigScreenShow">
                <div class="layui-col-xs4 layui-col-sm12 layui-col-md3" style="background-color: #01aaed;margin-left: 40px">
                    <div id="yscl" style="width: 100%; margin: 10px">
                        <span style="font-size: 22px; color: black;">登记车辆数</span>
                        <span style="font-size: 28px; color: white; margin-left: 20px;" id="yscls"></span>
                    </div>
                </div>
                <div class="layui-col-xs4 layui-col-sm12 layui-col-md3" style="background-color: #01aaed;margin-left: 40px">
                    <div id="jsl" style="width: 100%; margin: 10px">
                        <span style="font-size: 22px; color: black;">今日进车数</span>
                        <span style="font-size: 28px; color: white; margin-left: 20px;" id="jsls"></span>
                    </div>
                </div>
                <div class="layui-col-xs4 layui-col-sm12 layui-col-md3" style="background-color: #01aaed;margin-left: 40px">
                    <div id="csl" style="width: 100%; margin: 10px">
                        <span style="font-size: 22px; color: black;">今日出车数</span>
                        <span style="font-size: 28px; color: white; margin-left: 20px;" id="csls"></span>
                    </div>
                </div>
            </div>
            <div class="layui-col-xs6 layui-col-sm12 layui-col-md6">
                <div id="rlzlzb" style="width: 100%; height:42vh; margin: 10px"></div>
            </div>
            <div class="layui-col-xs6 layui-col-sm12 layui-col-md6">
                <div id="pfbzzb" style="width: 100%; height:42vh; margin: 10px"></div>
            </div>
            <div class="layui-col-xs4 layui-col-sm12 layui-col-md12" id="bigScreenHidden">
                <div class="layui-col-xs4 layui-col-sm12 layui-col-md12">
                    <div id="txjlweek" style="width: 100%; height:42vh"></div>
                </div>
            </div>

        </div>
    </div>
</div>
<script>
    // 基于准备好的dom，初始化echarts实例
    var chartrlzlzb = echarts.init(document.getElementById('rlzlzb'));
    var chartrpfbzzb = echarts.init(document.getElementById('pfbzzb'));
    var charttxjlweek = echarts.init(document.getElementById('txjlweek'));

    // 定时刷新
    var getAjaxData;

    layui.config({
        base: '${staticPath}/static/module/'
    }).use(['layer', 'form', 'tree', 'table', 'laydate'], function () {
        var $ = layui.jquery;
        var laydate = layui.laydate;
        var tree = layui.tree;
        var form = layui.form;
        var params = {qybh: ''};
        // 初始化
        getChartData(params);
        showsssl();

        // 机构
        $.ajax({
            type: "post",
            url: '${path}/admin/organization/orgtreelevel?organtype=',
            dataType: 'json',
            success: function (d) {
                tree.render({
                    elem: '#selectOrgan'
                    , data: d
                    , showCheckbox: false  //是否显示复选框
                    , showLine: true
                    , id: 'resourcetree'
                    , isJump: false //是否允许点击节点时弹出新窗口跳转
                    , click: function (obj) {
                        var data = obj.data;  //获取当前点击的节点数据
                        $('#organ').val(data.id);
                        $('#treeclass').html(data.title);
                    }
                });
            }
        });

        $(".downpanel").on("click", ".layui-select-title", function (e) {
            $(".layui-form-select").not($(this).parents(".layui-form-select")).removeClass("layui-form-selected");
            $(this).parents(".downpanel").toggleClass("layui-form-selected");
            layui.stope(e);
        }).on("click", "dl i", function (e) {
            layui.stope(e);
        });
        // clearInterval(getAjaxData);
        // getAjaxData = setInterval(function () {
        //     showsssl();
        // }, 10000);
    });

    function getOptionsForPieRlzl(data) {
        var qyNum = data.qyarr[0];
        var cyNum = data.cyarr[0];
        var trqNum = data.trqarr[0];
        var dNum = data.darr[0];
        var qtNum = data.qtarr[0];
        // 指定图表的配置项和数据
        option = {
            title: {
                text: '车辆燃油清洁率',
                subtext: '',
                left: 'center'
            },
            borderColor: '#01aaed',
            label: {
                show: true, // 显示标签
                position: 'outside', // 在饼图内部显示标签
                formatter: "{b}:{c} ({d}%)"
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a}<br/>{b}:{c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: 'left'
            },
            series: [
                {
                    name: '占比',
                    type: 'pie',
                    radius: '50%',
                    data: [
                        {
                            value: qyNum, name: '汽油'
                        },
                        {
                            value: cyNum, name: '柴油'
                        },
                        {
                            value: trqNum, name: '天然气'
                        },
                        {
                            value: dNum, name: '电'
                        },
                        {
                            value: qtNum, name: '其他'
                        }
                    ],
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };

        return option;
    }

    function getOptionsForPiePfbz(data) {
        var g0Num = data.g0arr[0];
        var g1Num = data.g1arr[0];
        var g2Num = data.g2arr[0];
        var g3Num = data.g3arr[0];
        var g4Num = data.g4arr[0];
        var g5Num = data.g5arr[0];
        var g6Num = data.g6arr[0];
        var dNum = data.darr[0];
        var xNum = data.xarr[0];
        // 指定图表的配置项和数据
        option = {
            title: {
                text: '车辆排放占比',
                subtext: '',
                left: 'center'
            },
            borderColor: '#01aaed',
            label: {
                show: true, // 显示标签
                position: 'outside', // 在饼图内部显示标签
                formatter: "{b}:{c} ({d}%)"
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a}<br/>{b}:{c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: 'left'
            },
            series: [
                {
                    name: '占比',
                    type: 'pie',
                    radius: '50%',
                    data: [
                        {
                            value: g0Num, name: '国0'
                        },
                        {
                            value: g1Num, name: '国Ⅰ'
                        },
                        {
                            value: g2Num, name: '国Ⅱ'
                        },
                        {
                            value: g3Num, name: '国Ⅲ'
                        },
                        {
                            value: g4Num, name: '国Ⅳ'
                        },
                        {
                            value: g5Num, name: '国Ⅴ'
                        },
                        {
                            value: g6Num, name: '国Ⅵ'
                        },
                        {
                            value: dNum, name: '电'
                        },
                        {
                            value: xNum, name: '其他'
                        }
                    ],
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };

        return option;
    }

    function getOptionsForBar(data) {
        var jarr = [];
        var carr = [];
        var rqarr = [];
        for (var i = 0; i < data.rqarr.length; i++) {
            jarr[i] = data.jarr[i];
            carr[i] = data.carr[i];
            rqarr[i] = data.rqarr[i];
        }
        var option = {
            title: {
                text: '近一周进出情况',
                subtext: ''
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['进', '出']
            },
            toolbox: {
                show: true,
                feature: {
                    dataView: { show: true, readOnly: false },
                    magicType: { show: true, type: ['line', 'bar'] },
                    restore: { show: true },
                    saveAsImage: { show: true }
                }
            },
            calculable: true,
            xAxis: [
                {
                    type: 'category',
                    // prettier-ignore
                    data:rqarr
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [
                {
                    name: '进',
                    type: 'bar',
                    data: jarr,
                    markPoint: {
                        data: [
                            { type: 'max', name: 'Max' },
                            { type: 'min', name: 'Min' }
                        ]
                    },
                    markLine: {
                        data: [{ type: 'average', name: 'Avg' }]
                    }
                },
                {
                    name: '出',
                    type: 'bar',
                    data: carr,
                    markPoint: {
                        data: [
                            { type: 'max', name: 'Max' },
                            { type: 'min', name: 'Min' }
                        ]
                    },
                    markLine: {
                        data: [{ type: 'average', name: 'Avg' }]
                    }
                }
            ]
        };
        return option;

    }

    function getChartData(params) {
        $.ajax({
            type: "post",
            url: '${path}/admin/count/txjlweek',
            data: params,
            dataType: 'json',
            success: function (d) {
                // $("[selectOrgan]").html("");
                if (d.code == '1') {
                    charttxjlweek.setOption(getOptionsForBar(d));
                }
            }
        });

        $.ajax({
            type: "post",
            url: '${path}/admin/count/rlzlzb',
            data: params,
            dataType: 'json',
            success: function (d) {
                // $("[selectOrgan]").html("");
                if (d.code == '1') {
                    chartrlzlzb.setOption(getOptionsForPieRlzl(d));
                }
            }
        })
        $.ajax({
            type: "post",
            url: '${path}/admin/count/pfbzzb',
            data: params,
            dataType: 'json',
            success: function (d) {
                if (d.code == '1') {
                    // 使用刚指定的配置项和数据显示图表。
                    chartrpfbzzb.setOption(getOptionsForPiePfbz(d));
                }
            }
        })

    }
    function showsssl(){
        // 已审核照片数量  /admin/taskDataCount/taskCount
        $.ajax({
            type: "post",
            url: '${path}/admin/count/sssl',
            data: {},
            dataType: 'json',
            success: function (d) {
                // $("[selectOrgan]").html("");
                if (d.code == '1') {
                    // 照片合格和总数渲染
                    $('#yscls').html(d.ysclarr[0] == undefined ? 0 : d.ysclarr[0]);
                    $('#jsls').html(d.jslarr[0] == undefined ? 0 : d.jslarr[0]);
                    $('#csls').html(d.cslarr[0] == undefined ? 0 : d.cslarr[0]);
                }
            }
        })
    }

    $(window).resize(function () {
        myChart.resize();
        myChart1.resize();
    });

</script>
</body>
</html>