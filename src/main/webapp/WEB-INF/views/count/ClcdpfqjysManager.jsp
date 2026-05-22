<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>

<html>
<head>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <script type="text/javascript" src="${staticPath}/static/echarts/js/echarts.js" charset="utf-8"></script>
    <link href="${staticPath}/static/layui/formSelects/formSelects-v4.css" rel="stylesheet" />
    <%--    <script src="${staticPath}/static/layui/formSelects/formSelects-v4.js"></script>--%>
    <title>清洁运输</title>
    <style>
        .layui-badge-rim + .layui-badge-rim {
            margin-left: 5px;
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

        .layui-tree-icon {
            color: #f9f8ff;
        }

        .layui-tree-txt {
            color: white;
        }
        td {
            width: 100px;
            height: 20px;
            text-align: center;
            table-layout: fixed;
            border-width: 1px;
            border-color: black;
        }
        table td {
            border: 1px solid black; /* 设置1像素的黑色实线边框 */
            padding: 8px; /* 可选：设置内边距，使内容不会紧贴着边框 */
        }
        th {
            height: 50px;
        }

        table {
            position: relative;
            border-width:1px;
            border-color: black;
            width:50%;
            align:center;
            cellspacing:0;
            cellpadding:0;
        }

    </style>
    <script type="text/javascript" src="${staticPath}/static/js/colmunFormat.js" charset="utf-8"></script>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <form id="" class="layui-form" method="post">
                <div class="layui-form-item" lay-filter="searchForm">
                    <label class="layui-form-label">企业名称</label>
                    <div class="layui-input-inline">
                        <div class="layui-unselect layui-form-select downpanel" id="downpl">
                            <div class="layui-select-title">
                                <span class="layui-input layui-unselect" id="treeclass">请选择企业</span>
                                <input type="hidden" name="organ" id="organ" value="">
                                <i class="layui-edge"></i>
                            </div>
                            <dl class="layui-anim layui-anim-upbit" id="divselect">
                                <dd>
                                    <ul id="selectOrgan"></ul>
                                </dd>
                            </dl>
                        </div>
                    </div>
                    <label class="layui-form-label">时间</label>
                    <div class="layui-input-inline">
                        <input name="kssj" id="kssj" class="layui-input"/>
                    </div>
                    <div class="layui-input-inline">
                        <input name="jssj" id="jssj" class="layui-input"/>
                    </div>

                    <div class="layui-input-inline">
                        <button class="layui-btn" id="serach" lay-submit="" lay-filter="searchSubmit">查询</button>
                        <button type="reset" class="layui-btn">重置</button>
                    </div>
                </div>

            </form>

        </div>
    </div>
    <div class="layui-col-xs6 layui-col-sm6 layui-col-md6" style="display: flex;justify-content: center;">
        <div id="cltxpfPie" style="width: 80%; height:42vh; margin: 10px;"></div>
    </div>
    <div class="layui-col-xs6 layui-col-sm6 layui-col-md6" style="display: flex;justify-content: center;">
        <div id="clqjysPie" style="width: 80%; height:42vh; margin: 10px;"></div>
    </div>
    <div class="layui-col-xs6 layui-col-sm6 layui-col-md6">
        <div id="cltxptable" style="display: flex;justify-content: center;"></div>
    </div>
    <div class="layui-col-xs6 layui-col-sm6 layui-col-md6">
        <div id="clqjystable" style="display: flex;justify-content: center;"></div>
    </div>
</div>
<!-- 表格状态列 -->
<script>
    var cltxpfPie = echarts.init(document.getElementById('cltxpfPie'));
    var clqjysPie = echarts.init(document.getElementById('clqjysPie'));
    layui.config({
        base: '${staticPath}/static/layui/'
    }).extend({
        formSelects: 'formSelects-v4'
    });
    layui.use(['element','form', 'table', 'tree', 'laydate', 'formSelects'], function () {
        var $ = layui.jquery;
        var table = layui.table;
        var form = layui.form;
        var tree = layui.tree;
        var laydate = layui.laydate;
        var formSelects = layui.formSelects;
        form.render();
        //日期
        laydate.render({
            elem: '#kssj'
        });

        laydate.render({
            elem: '#jssj'
        });
        $('#kssj').val(new Date().Format('yyyy-MM-dd'));
        $('#jssj').val(new Date().Format('yyyy-MM-dd'));
        var params = {qybh: $('#organ').val(),  kssj: $('#kssj').val(), jssj: $('#jssj').val()};
        // 初始化
        getChartData(params);
        // formSelects.on('select2', function(id, vals, val, isAdd, isDisabled){
        //     console.log(vals);
        //     console.log(val);
        // }, true);

        form.on('submit(searchSubmit)', function () {
            var getName = JSON.stringify(layui.formSelects.value('select2', 'id'));//取值name数组
            console.log(getName);

            var params = {qybh: $('#organ').val(),  kssj: $('#kssj').val(), jssj: $('#jssj').val()};
            // 初始化
            getChartData(params);
            return false;
        });
        laydate.render({
            elem: '#kssj'
        });
        laydate.render({
            elem: '#jssj'
        });
        $.ajax({
            type: "post",
            url: '${path}/admin/organization/orgtreelevelForOrgan?organtype=',
            dataType: 'json',
            success: function (d) {
                tree.render({
                    elem: '#selectOrgan'
                    , data: d
                    , showCheckbox: false  //是否显示复选框
                    , showLine: true
                    , id: 'resourcetree'
                    , isJump: true //是否允许点击节点时弹出新窗口跳转
                    , click: function (obj) {
                        var data = obj.data;  //获取当前点击的节点数据
                        $('#organ').val(data.id);
                        $('#treeclass').html(data.title);
                        $('#downpl').toggleClass("layui-form-selected");
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
    });
    function getOptionsForPie(data) {
        var g5Num = data.g5arr[0];
        var g6Num = data.g6arr[0];
        var dNum = data.darr[0];
        var xNum = data.cdarr[0];
        // 指定图表的配置项和数据
        option = {
            title: {
                text: '产品运输',
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
                            value: g5Num, name: '其他'
                        },
                        {
                            value: g6Num, name: '国Ⅵ'
                        },
                        {
                            value: dNum, name: '电'
                        },
                        {
                            value: xNum, name: '皮带传输'
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
    function getOptionsForPieYsw(data) {
        var g5Num = data.g5arr[0];
        var g6Num = data.g6arr[0];
        var dNum = data.darr[0];
        var xNum = data.cdarr[0];
        // 指定图表的配置项和数据
        option = {
            title: {
                text: '原燃料清洁运输',
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
                            value: g5Num, name: '其他'
                        },
                        {
                            value: g6Num, name: '国Ⅵ'
                        },
                        {
                            value: dNum, name: '电'
                        },
                        {
                            value: xNum, name: '皮带传输'
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
    function getChartData(params) {
        $.ajax({
            type: "post",
            url: '${path}/count/clcdpfqjys/clqjyssj',
            data: params,
            dataType: 'json',
            success: function (cltxpfList) {
                if(cltxpfList!=""){
                    shtml="";
                    $("#cltxptable").html("");
                    shtml="<table>"
                    shtml = shtml + '<tr><td>排放阶段</td><td>运输量</td></tr>';
                    for (var i = 0; i < cltxpfList.length; i++) {
                        shtml = shtml + '<tr><td>'+cltxpfList[i].yshwmc+'</td><td>'+cltxpfList[i].yslhj+'</td></tr>';
                    }
                    shtml=shtml+"</table>";
                    $("#cltxptable").html(shtml);
                }
            }
        });
        $.ajax({
            type: "post",
            url: '${path}/count/clcdpfqjys/clqjyspie',
            data: params,
            dataType: 'json',
            success: function (d) {
                // $("[selectOrgan]").html("");
                if (d.code == '1') {
                    cltxpfPie.setOption(getOptionsForPie(d));
                }
            }
        });
        $.ajax({
            type: "post",
            url: '${path}/count/clcdpfqjys/clqjyswsj',
            data: params,
            dataType: 'json',
            success: function (cltxpfList) {
                if(cltxpfList!=""){
                    shtml="";
                    $("#clqjystable").html("");
                    shtml="<table>"
                    shtml = shtml + '<tr><td>排放阶段</td><td>运输量</td></tr>';
                    for (var i = 0; i < cltxpfList.length; i++) {
                        shtml = shtml + '<tr><td>'+cltxpfList[i].yshwmc+'</td><td>'+cltxpfList[i].yslhj+'</td></tr>';
                    }
                    shtml=shtml+"</table>";
                    $("#clqjystable").html(shtml);
                }
            }
        });
        $.ajax({
            type: "post",
            url: '${path}/count/clcdpfqjys/clqjyswpie',
            data: params,
            dataType: 'json',
            success: function (d) {
                // $("[selectOrgan]").html("");
                if (d.code == '1') {
                    clqjysPie.setOption(getOptionsForPieYsw(d));
                }
            }
        });
    }

    function cleanFun() {
        $('#kssj').val('');
        $('#jssj').val('');
        $('#organ').combotree("clear");
    }

    function renderForm() {
        layui.use('form', function () {
            var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
            form.render();
        });
    }
</script>

</body>
</html>