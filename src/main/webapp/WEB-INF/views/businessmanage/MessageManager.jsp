<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>

<html>
<head>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>聊天记录查询</title>
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

    </style>
    <script type="text/javascript" src="${staticPath}/static/js/colmunFormat.js" charset="utf-8"></script>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <form id="" class="layui-form" method="post">
                <div class="layui-form-item" lay-filter="searchForm">
                    <label class="layui-form-label">客服名称</label>
                    <div class="layui-input-inline">
                        <select id="kfid" name="kfid" lay-filter="shztFilter">
                            <option value="">请选择</option>
                        </select>
                    </div>
                    <label class="layui-form-label">客户名称</label>
                    <div class="layui-input-inline">
                        <select id="wxid" name="wxid" lay-filter="shztFilter" lay-search>
                            <option value="">请选择</option>
                        </select>
                    </div>
                    <label class="layui-form-label">发送内容</label>
                    <div class="layui-input-inline">
                        <input name="messageInfo" id="messageInfo" class="layui-input"/>
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
                        <button class="layui-btn" id="serach" lay-submit="" lay-filter="searchSubmit">查询</button>
                    </div>
                </div>

            </form>

        </div>
    </div>
    <div class="layui-card">
        <div class="layui-card-body">
<%--            <script type="text/html" id="actionHandle">--%>
<%--                &lt;%&ndash;                <shiro:hasPermission name="/admin/information/aivehicleDataDetails">&ndash;%&gt;--%>
<%--                <span style="height: 24px; background-color: transparent; color: white; cursor: pointer;"--%>
<%--                      lay-event="detail">--%>
<%--                        <span style="margin: 10px; cursor: pointer;color:#27d9ff">详细</span>--%>
<%--                </span>--%>
<%--            </script>--%>

            <table class="layui-hide" id="vehicleTable" lay-filter="view"></table>
        </div>

    </div>
</div>
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
            elem: '#kssj'
        });

        laydate.render({
            elem: '#jssj'
        });
        $('#kssj').val(new Date().Format('yyyy-MM-dd'));
        $('#jssj').val(new Date().Format('yyyy-MM-dd'));

        var renderTable = function () {
            table.render({
                elem: '#vehicleTable'
                , url: '${path}/message/DataGrid?nowTime=' + new Date().getTime()
                , method: 'GET'
                , request: {
                    pageName: 'page' //页码的参数名称，默认：page
                    , limitName: 'rows' //每页数据量的参数名，默认：limit
                }
                , where: {
                    kfid: $('#kfid').val(),
                    wxid: $('#wxid').val(),
                    messageInfo: $('#messageInfo').val(),
                    kssj: $('#kssj').val(),
                    jssj: $('#jssj').val()
                }
                , response: {
                    statusName: 'code' //规定数据状态的字段名称，默认：code
                    , statusCode: 0 //规定成功的状态码，默认：0
                    , countName: 'total' //规定数据总数的字段名称，默认：count
                    , dataName: 'rows' //规定数据列表的字段名称，默认：data
                }
                , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                , cols: [[
                    {field: 'messagesender', title: '发送者', align: 'center', sort: true, templet: function (d, row, index) {
                            var value = d.messagesender;
                            var username="";
                            $.ajax({
                                type: "post",
                                url: '${path}/message/username?userid='+value,
                                dataType: 'json',
                                async:false,
                                success: function (data) {
                                    username=data.username;
                                }
                            });
                            return username;
                        }
                    },
                    {field: 'messagereciver', title: '接收者', align: 'center', sort: true, templet: function (d, row, index) {
                            var value = d.messagereciver;
                            var username="";
                            $.ajax({
                                type: "post",
                                url: '${path}/message/username?userid='+value,
                                dataType: 'json',
                                async:false,
                                success: function (data) {
                                    username=data.username;
                                }
                            });
                            return username;
                        }
                    },
                    {field: 'messagedate', title: '发送时间', align: 'center', sort: true},
                    {field: 'messageinfo', title: '发送内容', align: 'center', sort: true}
                ]]
                , page: true
                , limit: 15
                , limits: [5, 10, 15, 20, 25, 30]
                ,done: function(res, curr, count){
                }
            });
        };
        renderTable();

        form.on('submit(searchSubmit)', function () {
            table.reload('vehicleTable', {
                url: '${path}/message/DataGrid?nowTime=' + new Date().getTime(),
                page: {
                    curr:1
                },
                where: {
                    kfid: $('#kfid').val(),
                    wxid: $('#wxid').val(),
                    messageInfo: $('#messageInfo').val(),
                    kssj: $('#kssj').val(),
                    jssj: $('#jssj').val()
                }
            });
            return false;
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
        $.ajax({
            type: "post",
            url: '${path}/message/combox?oi_name=微信',
            dataType: 'json',
            success: function (data) {
                $('#wxid').empty();
                var t;
                var t = "<option value='' selected='selected'>选择客户名称</option>";
                for (var i = 0; i < data.length; i++) {
                    if (data[i].userId != undefined && data[i].userName != undefined) {
                        t += '<option value="' + data[i].userId + '">' + data[i].userName + '</option>';
                    }
                }
                $('#wxid').append(t);
                form.render('select');
            }
        });
    });


    function cleanFun() {
        $('#kssj').val('');
        $('#jssj').val('');
        $('#kfid').combobox('clear');
        $('#wxid').combobox('clear');
        $('#messageInfo').val('');
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