<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>

<html>
<head>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <script type="text/javascript" src="${staticPath}/static/echarts/js/echarts.js" charset="utf-8"></script>
    <style type="text/css">
    </style>
    <title>操作日志</title>
    <style>
        .layui-badge-rim + .layui-badge-rim {
            margin-left: 5px;
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


        .layui-form-label {
            width: 60px;
        }

        .layui-form-item .layui-input-inline {
            width: 160px;
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
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">机构名称</label>
                        <div class="layui-input-inline">
                            <div class="layui-unselect layui-form-select downpanel" id="downpl">
                                <div class="layui-select-title">
                                    <span class="layui-input layui-unselect" id="treeclass">选择机构</span>
                                    <input type="hidden" name="organization" id="organization" value="">
                                    <i class="layui-edge"></i>
                                </div>
                                <dl class="layui-anim layui-anim-upbit" id="divselect">
                                    <dd>
                                        <ul id="selectOrgan"></ul>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">操作时间:</label>
                        <div class="layui-input-inline">
                            <input name="dateofstart" id="createdate" class="layui-input"/>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline">
                            <input name="dateofend" id="createdateend" class="layui-input"/>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">登录IP</label>
                        <div class="layui-input-inline">
                            <input name="terminal_id" id="terminal_id" class="layui-input"/>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">用户名</label>
                        <div class="layui-input-inline">
                            <input name="user_name" id="user_name" class="layui-input"/>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">操作类型:</label>
                        <div class="layui-input-inline">
                            <select id="operate_type" name="operate_type">
                                <option value="">请选择</option>
                                <option value="0">登录操作</option>
                                <option value="1">查询操作</option>
                                <option value="2">新增操作</option>
                                <option value="3">修改操作</option>
                                <option value="4">删除操作</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">操作返回:</label>
                        <div class="layui-input-inline">
                            <select id="operate_result" name="operate_result">
                                <option value="">请选择</option>
                                <option value="0">操作失败</option>
                                <option value="1">操作成功</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <button id="userBtnSearch" lay-submit="" lay-filter="vehicleSearchFilter"
                                class="layui-btn icon-btn"><i
                                class="layui-icon"></i>查询
                        </button>
                        <button type="reset" class="layui-btn icon-btn" id="userBtnClear"><i
                                class="layui-icon"></i>清空
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div id="toolbox" style="position: absolute; right: 83px; width: 125px; height: 32px;">
            <img src="${staticPath}/static/assets/images/chart_show.png" id="showChart">
            <img src="${staticPath}/static/assets/images/line_hide.png" id="showLineChart">
            <img src="${staticPath}/static/assets/images/show_bg.png" id="showTable">
        </div>


    </div>

    <div class="left" id="isShowChart" style="margin: 20px 0;">
        <div style="margin-top: 38px;">
            <div id="demo1" style="width: 100%; height:553px"></div>
        </div>
    </div>
    <div class="left" id="isShowLine" style="margin: 20px 0;">
        <div style="margin-top: 38px;">
            <div id="demo2" style="width: 100%; height:553px;"></div>
        </div>
    </div>
    <div id="isShowTable" style="display: none;">
        <table id="vehicleTable" style="margin: 20px 0;" lay-filter="vehicleFilter"></table>
    </div>
    <script type="text/html" id="actionHandle">
        <shiro:hasPermission name="/admin/loginfo/edit">
					<span style="height: 24px; background-color: transparent; color: white; cursor: pointer;"
                          lay-event="detail">
                        <span style="margin: 10px; font-size: 16px; cursor: pointer;color:#27d9ff">详情</span>
                    </span>
        </shiro:hasPermission>
    </script>
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
        var form = layui.form;
        var tree = layui.tree;
        var laydate = layui.laydate;

        //日期
        laydate.render({
            elem: '#createdate'
        });

        laydate.render({
            elem: '#createdateend'
        });
        $('#createdateend').val(new Date().Format('yyyy-MM-dd'));
        $('#createdate').val(new Date().Format('yyyy-MM-dd'));

        table.render({
            elem: '#vehicleTable'
            , url: '${path}/admin/loginfo/dataGrid'
            , method: 'post'
            , request: {
                pageName: 'page' //页码的参数名称，默认：page
                , limitName: 'rows' //每页数据量的参数名，默认：limit
            }
            , where: {
                dateofstart: $('#createdate').val()
                , dateofend: $('#createdateend').val()
            }
            , response: {
                statusName: 'code' //规定数据状态的字段名称，默认：code
                , statusCode: 0 //规定成功的状态码，默认：0
                , countName: 'total' //规定数据总数的字段名称，默认：count
                , dataName: 'rows' //规定数据列表的字段名称，默认：data
            }
            , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
            , cols: [[
                {
                    field: 'organization', title: '所属机构',sort: true, templet: function (d, row, index) {
                        var value = d.organization;
                        return getValue("organ", value, "${path }/admin/user/basedata/organ");
                    }
                },
                {field: 'user_name', title: '用户名', align: 'center', sort: true}
                , {
                    field: 'terminal_id', title: '登录IP', align: 'center', sort: true
                }
                , {
                    field: 'operate_time', title: '操作时间', align: 'center', sort: true,
                    templet: function (d) {
                        var value = d.operate_time;
                        return value.substring(0, 4) + '-'
                            + value.substring(4, 6)
                            + '-'
                            + value.substring(6, 8)
                            + '  '
                            + value.substring(8, 10)
                            + ':'
                            + value.substring(10, 12)
                            + ':'
                            + value.substring(12, 14);
                    }
                }
                , {
                    field: 'operate_type', title: '操作类型', sort: true, templet: function (d, row, index) {
                        var value = d.operate_type;
                        var operate_str = "未知类型";
                        if (value == 0) {
                            operate_str = "登录操作";
                        } else if (value == 1) {
                            operate_str = "查询操作";
                        } else if (value == 2) {
                            operate_str = "新增操作";
                        } else if (value == 3) {
                            operate_str = "修改操作";
                        } else if (value == 4) {
                            operate_str = "删除操作";
                        }
                        return operate_str;
                    }
                }
                , {
                    field: 'operate_module', title: '操作模块', align: 'center', sort: true
                }
                , {
                    field: 'operate_result', title: '操作返回', sort: true, templet: function (d, row, index) {
                        var value = d.operate_result;
                        var operate_str = "未知类型";
                        if (value == 0) {
                            operate_str = "操作失败";
                        } else if (value == 1) {
                            operate_str = "操作成功";
                        }
                        return operate_str;
                    }
                }
                , {
                    field: 'action', align: 'center', title: '操作',
                    templet: '#actionHandle'
                }
            ]]
            , done: function (res, curr, count) {
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                // console.log(res);

                //得到当前页码
                // console.log(curr);

                //得到数据总量
                // console.log(count);
                getParData();
                // chart1.setOption(getOptionsForBar(res.rows));

            }
            , page: true
            , limit: 15
            , limits: [5, 10, 15, 20, 25, 30]
        });

        // 机构名称
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
                    , isJump: true //是否允许点击节点时弹出新窗口跳转
                    , click: function (obj) {
                        var data = obj.data;  //获取当前点击的节点数据
                        $('#organization').val(data.id);
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
        table.on('tool(vehicleFilter)', function (obj) {
            var data = obj.data;
            if (obj.event === 'detail') {
                // layer.msg('JYLSH：' + data.jylsh + ' 的查看操作');
                //iframe窗
                parent.layer.closeAll();
                parent.layer.open({
                    type: 2,
                    title: '详情',
                    shadeClose: true,
                    shade: false,
                    maxmin: false, //开启最大化最小化按钮
                    area: ['800px', '500px'],
                    skin: 'my-skin',
                    content: ['${path}/admin/loginfo/editPage?id=' + data.num_id]
                });
            }
        });

        // 搜索按钮点击事件
        $('#userBtnSearch').click(function () {
            table.reload('vehicleTable', {
                page1: 1,
                where: {
                    organization: $('#organization').val()
                    , sort: 'dis'
                    , order: 'asc'
                    , user_name: $('#user_name').val()
                    , terminal_id: $('#terminal_id').val()
                    , operate_type: $('#operate_type').val()
                    , operate_result: $('#operate_result').val()
                    , dateofstart: $('#createdate').val()
                    , dateofend: $('#createdateend').val()
                }
            });
        });

        // 清空按钮点击事件
        $('#userBtnClear').click(function () {
            $('#organization').val('');
            $('#treeclass').html("选择机构");
            $('#user_name').val('');
            $('#operate_result').val('');
            $('#operate_type').val('');
            form.render('select');
            $('#createdate').val('');
            $('#createdateend').val('');

            var cllx = $('#cllx').val();
            var organ = $('#organ').val();
            var dateofstart = $('#createdate').val();
            var dateofend = $('#createdateend').val();
        });
        // 显示表格按钮
        $('#showTable').click(function () {
            // alert("123456");
            $('#showTable').attr("src", "${staticPath}/static/assets/images/table_show.png");
            $('#showChart').attr("src", "${staticPath}/static/assets/images/show_zhu.png");
            $('#showLineChart').attr("src", "${staticPath}/static/assets/images/line_hide.png");
            $('#isShowChart').css("display", "none");
            $('#isShowLine').css("display", "none");
            $('#isShowTable').css("display", "block");
            chart1.resize();
            chart2.resize();
        });
        // 显示条形图按钮
        $('#showChart').click(function () {
            // alert("123456");
            $('#showTable').attr("src", "${staticPath}/static/assets/images/show_bg.png");
            $('#showChart').attr("src", "${staticPath}/static/assets/images/chart_show.png");
            $('#showLineChart').attr("src", "${staticPath}/static/assets/images/line_hide.png");
            $('#isShowLine').css("display", "none");
            $('#isShowChart').css("display", "block");
            $('#isShowTable').css("display", "none");
            chart1.resize();
            chart2.resize();
        });

        // 显示线性图按钮
        $('#showLineChart').click(function () {
            // alert("123456");
            $('#showTable').attr("src", "${staticPath}/static/assets/images/show_bg.png");
            $('#showChart').attr("src", "${staticPath}/static/assets/images/show_zhu.png");
            $('#showLineChart').attr("src", "${staticPath}/static/assets/images/linechart_show.png");

            $('#isShowTable').css("display", "none");
            $('#isShowChart').css("display", "none");
            $('#isShowLine').css("display", "block");
            chart1.resize();
            chart2.resize();
        });

        $(window).resize(function () {
            chart1.resize();
            chart2.resize();
        });

        $('#isShowLine').css("display", "none");
    });

    var chart1 = echarts.init(document.getElementById('demo1'));
    var chart2 = echarts.init(document.getElementById('demo2'));

    // 获取数据
    function getParData() {

        $.ajax({
            type: "post",
            url:'${path }/admin/loginfo/logBarCount',
            data: {
                "organization": $('#organization').val()
                , "user_name": $('#user_name').val()
                , "terminal_id": $('#terminal_id').val()
                , "operate_type": $('#operate_type').val()
                , "operate_result": $('#operate_result').val()
                , "dateofstart": $('#createdate').val()
                , "dateofend": $('#createdateend').val()
            },
            dataType: 'json',
            success: function (data) {
                chart1.setOption(getOptionsForBar(data));
                chart2.setOption(getOptionsForLine(data));
            }
        });

    }

    // 图表数据
    function getOptionsForBar(data) {
        var option = {
            title: {
                text: '整车合格率统计表',
                x: 'center',
                y: 'top',
                textStyle: {
                    color: 'white',
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
                        color: 'white' //设置颜色
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
                textStyle: {color: 'white'},
                data: ['操作次数']
            },
            grid: {
                left: '10%',
                right: '5%',
                top: '12%',
                bottom: '15%',
                containLabel: false
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
                        color: 'white',
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
                    data: data.type,
                    axisPointer: {
                        type: 'shadow'
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    axisLabel: {
                        color: 'white',
                        textStyle: {
                            color: '#fff'
                        }
                    }
                }
            ],
            series: [
                {
                    name: '操作次数',
                    type: 'bar',
                    barGap: '-80%',
                    data: data.num,
                    itemStyle: {
                        normal: {
                            color: 'rgba(92,217,152,0.8)'
                        }
                    }
                }
            ]
        };

        return option;
    }

    function getOptionsForLine(data) {
        var option = {
            title: {
                text: '整车通过率图表',
                x: 'center',
                y: 'top',
                textStyle: {
                    color: 'white',
                    fontWeight: 'normal'
                }
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                },
                formatter: '{b}<br />{a0}: {c0}%<br />{a1}: {c1}%'
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
                        color: 'white' //设置颜色
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
                textStyle: {color: 'white'},
                data: ['操作次数']
            },
            grid: {
                left: '10%',
                right: '5%',
                top: '12%',
                bottom: '15%',
                containLabel: false
            },
            xAxis: [
                {
                    type: 'category',
                    // axisLabel: {
                    //     interval:0,
                    //     rotate:40,
                    //     formatter:function(value)
                    //     {
                    //         return value.split("").join("\n");
                    //     }
                    // },
                    axisLabel: {
                        color: 'white',
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
                    data: data.type
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    axisLabel: {
                        color: 'white',
                        textStyle: {
                            color: '#fff'
                        },
                        formatter: '{value} %'
                    },
                }
            ],
            series: [
                {
                    name: '操作次数',
                    type: 'line',
                    data: data.num,
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    itemStyle: {
                        normal: {
                            color: 'rgb(252,213,91)'
                        }
                    }
                }
            ]
        };

        return option;
    }


</script>
</body>
</html>