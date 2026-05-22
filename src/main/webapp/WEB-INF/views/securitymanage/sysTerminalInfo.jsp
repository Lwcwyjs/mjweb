<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>

<html>
<head>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <script type="text/javascript" src="${staticPath}/static/echarts/js/echarts.min.js" charset="utf-8"></script>
    <style type="text/css">
    </style>
    <title>终端管理</title>
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
                        <label class="layui-form-label">终端标识</label>
                        <div class="layui-input-inline">
                            <input name="terminal_id" id="terminal_id" class="layui-input"/>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">锁定状态:</label>
                        <div class="layui-input-inline">
                            <select id="islocked" name="islocked">
                                <option value="">请选择</option>
                                <option value="0">未锁定</option>
                                <option value="1">锁定</option>
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
    </div>
    <table class="layui-hide" id="vehicleTable" lay-filter="vehicleFilter"></table>
</div>
</div>
</div>
<script type="text/javascript" src="${staticPath}/static/js/colmunFormat.js" charset="utf-8"></script>
<!-- 表格状态列 -->
<script>
    layui.config({
        base: '${staticPath}/static/module/'
    }).use(['form', 'table', 'tree'], function () {

        var $ = layui.jquery;
        var table = layui.table;
        var form = layui.form;
        var tree = layui.tree;


        table.render({
            elem: '#vehicleTable'
            , url: '${path}/admin/terminal/dataGrid'
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
            , cols: [[
                {
                    field: 'terminal_id', title: '终端IP',  align: 'center', sort: true
                }
                , {
                    field: 'fail_times', title: '鉴别识别次数',  align: 'center', sort: true
                }
                , {
                    field: 'update_time', title: '更新时间',  align: 'center', sort: true
                }
                , {
                    field: 'create_time', title: '创建时间',  align: 'center', sort: true
                }
                , {
                    field: 'lockedtime', title: '锁定时间', align: 'center', sort: true
                }
                , {
                    field: 'islocked', title: '是否锁定', sort: true, templet: function (d, row, index) {
                        var value = d.islocked;
                        var operate_str = "未知";
                        if (value == 0) {
                            operate_str = "未锁定";
                        } else if (value == 1) {
                            operate_str = "锁定";
                        }
                        return operate_str;
                    }
                }
                , {
                    field: 'action', align: 'center', title: '操作',  templet: function (d, row, index) {
                        var value = d.islocked;
                        var str = "";
                        if (value == 1) {
                            <shiro:hasPermission name="/admin/terminal/unlock">
                            str = "<span style=\"height: 24px; background-color: transparent; color: white; cursor: pointer;\" lay-event=\"unlock\"> <span style=\"margin: 10px; font-size: 16px; cursor: pointer;color:#27d9ff\">解锁</span> </span>"
                            </shiro:hasPermission>
                        }
                        return str;

                    }
                }
            ]]
            , done: function (res, curr, count) {

            }
            , page:true
            , limit: 15
            , limits: [5, 10, 15, 20, 25, 30]
        });

        table.on('tool(vehicleFilter)', function (obj) {
            var data = obj.data;
            if (obj.event === 'unlock') {
                $.ajax({
                    type: "post",
                    url: '${path}/admin/terminal/unlock/' + data.num_id,
                    data: data.field,
                    dataType: 'json',
                    success: function (data) {
                        if (data.success) {
                            layer.msg('解锁成功', {icon: 1});true
                            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                            setTimeout(function () {
                                layer.close(index); //再执行关闭
                                location.reload();
                            }, 1000);
                        } else {
                            layer.msg(data.msg, {icon: 2});
                        }
                    }
                });
            }
        });

        // 搜索按钮点击事件
        $('#userBtnSearch').click(function () {
            table.reload('vehicleTable', {
                page1: 1,
                where: {
                    islocked: $('#islocked').val()
                    , sort: 'dis'
                    , order: 'asc'
                    , terminal_id: $('#terminal_id').val()
                }
            });
        });

        // 清空按钮点击事件
        $('#userBtnClear').click(function () {
            $('#islocked').val('');
            $('#terminal_id').val('');
        });
    });


</script>
</body>
</html>