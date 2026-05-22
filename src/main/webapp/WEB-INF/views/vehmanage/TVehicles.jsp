<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>车型库查询</title>
    <style>
        .layui-badge-rim + .layui-badge-rim {
            margin-left: 5px;
        }


        .layui-form-label {
            width: 70px;
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
            width: 70px;
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
        <div class="layui-card-body">
            <div class="layui-form toolbar" id="vehicleForm">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">车辆型号</label>
                        <div class="layui-input-inline">
                            <input name="clxh" id="clxh" placeholder="可模糊查询" class="layui-input" onkeyup="this.value=this.value.toUpperCase()"/>
                        </div>
                        <label class="layui-form-label">发动机型号</label>
                        <div class="layui-input-inline">
                            <input name="fdjxh" id="fdjxh" placeholder="可模糊查询" class="layui-input" onkeyup="this.value=this.value.toUpperCase()"/>
                        </div>
                        <div class="layui-inline">
                            <button id="userBtnSearch" class="layui-btn icon-btn"><i
                                    class="layui-icon"></i>查询
                            </button>
                            <button type="reset" class="layui-btn icon-btn" id="userBtnClear"><i
                                    class="layui-icon"></i>清空
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="layui-row">
                <table class="layui-hide" id="vehTable" lay-filter="vehicleFilter"></table>
            </div>

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
        $('#kssj').val(new Date().Format('yyyy-MM-dd'));
        $('#jssj').val(new Date().Format('yyyy-MM-dd'));
        var renderTable = function () {
            var organ = $('#organ').val();
            table.render({
                elem: '#vehTable'
                , url: '${path}/business/tvehicles/dataGrid'
                , method: 'GET'
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
                , where: {
                    fdjxh: "111111111",
                    clxh: "2222222222",
                }
                , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                , cols: [[
                    {field: 'clxh', title: '车辆型号', align: 'center', sort: true},
                    {field: 'fdjxh', title: '发动机型号', align: 'center', sort: true},
                    {field: 'manuf', title: '汽车生产厂', align: 'center', sort: true},
                    {
                        field: 'pf',
                        title: '排放标准',
                        align: 'center',
                        sort: true,
                        templet: function (d, row, index) {
                            var value = d.pf;
                            switch (value) {
                                case "0":
                                    return "国0";
                                    break;
                                case "1":
                                    return "国Ⅰ";
                                    break;
                                case "2":
                                    return "国Ⅱ";
                                    break;
                                case "3":
                                    return "国Ⅲ";
                                    break;
                                case "4":
                                    return "国Ⅳ";
                                    break;
                                case "5":
                                    return "国Ⅴ";
                                    break;
                                case "6":
                                    return "国Ⅵ";
                                    break;
                                case "D":
                                    return "电动";
                                    break;
                                case "x":
                                    return "未知";
                                    break;
                                default:
                                    return "";
                                    break;
                            }
                        }
                    },
                    {field: 'cllb', title: '车辆类别', align: 'center', sort: true},
                    {field: 'clmc', title: '车辆名称', align: 'center', sort: true},
                    {field: 'fdjscc', title: '发动机生产厂', align: 'center', sort: true},
                    {field: 'filename', title: '公告日期', align: 'center', sort: true}
                ]]
                , page: true
                , limit: 15
                , limits: [5, 10, 15, 20, 25, 30]
            });
        }
        renderTable();
        // 搜索按钮点击事件
        $('#userBtnSearch').click(function () {
            if($('#clxh').val()==""&&$('#fdjxh').val()==""){
                layer.msg("车辆型号和发动机型号至少录入一项", {icon: 2});
                return;
            }
            table.reload('vehTable', {
                page: {
                    curr: 1//重新从第一页开始
                }
                , where: {
                    clxh: $('#clxh').val(),
                    fdjxh: $('#fdjxh').val()
                }
            });
        });

        // 清空按钮点击事件
        $('#userBtnClear').click(function () {
            $('#clxh').val('');
            $('#fdjxh').val('');
        });

    });
</script>

</body>
</html>