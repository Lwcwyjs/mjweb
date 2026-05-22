<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>企业在线状态信息</title>
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
        .row-highlight {
            background-color: red !important; /* 使用 !important 可以确保样式优先级更高 */
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
                        <label class="layui-form-label">机构名称</label>
                        <div class="layui-input-inline">
                            <div class="layui-unselect layui-form-select downpanel" id="downpl">
                                <div class="layui-select-title">
                                    <span class="layui-input layui-unselect" id="treeclass">选择机构</span>
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
                        <label class="layui-form-label">在线状态</label>
                        <div class="layui-input-inline">
                            <select id="isonline" name="isonline" lay-filter="shztFilter">
                                <option value="">请选择</option>
                                <option value="0">离线</option>
                                <option value="1">在线</option>
                            </select>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <button id="userBtnSearch" class="layui-btn icon-btn"><i
                                    class="layui-icon"></i>查询
                            </button>
                            <button type="reset" class="layui-btn icon-btn" id="userBtnClear"><i
                                    class="layui-icon"></i>清空
                            </button>
                            <button type="button" lay-submit="" class="layui-btn layui-btn-warm" lay-filter="uploadImg"
                                    id="exportExcel">导出excel
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="layui-row">
                <table class="layui-hide" id="dzxxTable" lay-filter="vehicleFilter"></table>
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
        var dzbh = $('#dzbh').val();
        var organ = $('#organ').val();
        var renderTable = function () {
            table.render({
                elem: '#dzxxTable'
                , url: '${path}/admin/organizationOnline/DataGrid?nowTime=' + new Date().getTime()
                , method: 'GET'
                , request: {
                    pageName: 'page' //页码的参数名称，默认：page
                    , limitName: 'rows' //每页数据量的参数名，默认：limit
                }
                , where: {
                    dzbh: dzbh,
                    qybh: organ
                }
                , response: {
                    statusName: 'code' //规定数据状态的字段名称，默认：code
                    , statusCode: 0 //规定成功的状态码，默认：0
                    , countName: 'total' //规定数据总数的字段名称，默认：count
                    , dataName: 'rows' //规定数据列表的字段名称，默认：data
                }
                , cols: [[
                    {
                        field: 'name', title: '企业名称', sort: true
                    }
                    , {field: 'isonline', title: '在线状态', sort: true, templet: function (d, row, index) {
                            var value = d.isonline;
                            switch (value) {
                                case "0":
                                    return "离线";
                                    break;
                                case "1":
                                    return "在线";
                                    break;
                                default:
                                    return value;
                                    break;
                            }
                        }
                    }
                    , {field: 'onlinetime', title: '更新时间', sort: true}
                ]]
                , page: true
                , limit: 15
                , limits: [5, 10, 15, 20, 25, 30]
                ,done: function (res, curr, count) {
                    layer.closeAll('loading');
                    $.each(res.rows, function (index, item) {
                        var isonline = item.isonline;
                        if (isonline == "0") {
                            // 法1
                            $("#dzxxTable").next().find('tbody tr[data-index="' + index +
                                '"]').css("background-color", "#FF0000");

                        }
                    });

                }
            });
        };
        renderTable();

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
                    , id: 'organtree'
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

        // 搜索按钮点击事件
        $('#userBtnSearch').click(function () {
            var organ = $('#organ').val();
            var isonline = $('#isonline').val();
            table.reload('dzxxTable', {
                page: {
                    curr: 1//重新从第一页开始
                }
                ,where: {
                     qybh: organ
                    , isonline: isonline
                }
            });
        });

        // 清空按钮点击事件
        $('#userBtnClear').click(function () {
            $('#treeclass').html("选择机构");
            $('#isonline').val('');
        });
        $('#exportExcel').click(function () {
            var organ = $('#organ').val();
            var isonline = $('#isonline').val();
            window.location.href = '${path}/admin/organizationOnline/getExcel?qybh=' + organ + '&isonline=' + isonline;
        });
    });
</script>

</body>
</html>