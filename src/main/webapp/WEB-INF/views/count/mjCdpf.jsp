<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>超低排放管理</title>
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
                        <label class="layui-form-label">运输物</label>
                        <div class="layui-input-inline">
                            <input name="ysw" id="ysw" class="layui-input"/>
                        </div>
                        <label class="layui-form-label">时间</label>
                        <div class="layui-input-inline">
                            <input name="kssj" id="kssj" class="layui-input"/>
                        </div>
                        <div class="layui-input-inline">
                            <input name="jssj" id="jssj" class="layui-input"/>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <button id="BtnSearch" class="layui-btn icon-btn"><i
                                    class="layui-icon"></i>查询
                            </button>
                            <button type="reset" class="layui-btn icon-btn" id="bmdBtnClear"><i
                                    class="layui-icon"></i>清空
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="layui-row">
                <div style="color: #3392FE;  font-size: 16px; padding: 5px;height: 20px">
                    <div style="float: right; cursor: pointer;" id="addDetail">
                        <img src="${staticPath}/static/assets/images/add.png" style="margin-bottom: 2px;"> 新建
                    </div>
                </div>

                <table class="layui-hide" id="cdpfTable" lay-filter="vehicleFilter"></table>
            </div>


            <script type="text/html" id="actionHandle" lay-event="detail">
                <shiro:hasPermission name="/count/cdpf/edit">
					<span style="height: 24px; background-color: transparent; cursor: pointer;"
                          lay-event="editDetail">
                        <span style="margin-left: 1px;"></span>
                        <img src="${staticPath}/static/assets/images/edit.png"
                             style="margin-bottom: 5px; cursor: pointer;">
                        <span style="margin: 0px; font-size: 16px; cursor: pointer;">编辑</span>
                    </span>
                </shiro:hasPermission>
                <shiro:hasPermission name="/count/cdpf/del">
					<span style="height: 24px; background-color: transparent; cursor: pointer;"
                          lay-event="deleteDetail">
                        <span style="margin-left: 1px;"></span>
                        <img src="${staticPath}/static/assets/images/delete.png"
                             style="margin-bottom: 5px; cursor: pointer;">
                        <span style="margin: 0px; font-size: 16px; cursor: pointer;">删除</span>
                    </span>
                </shiro:hasPermission>
            </script>

        </div>
        <!-- 遮罩层：覆盖整个页面，阻止底层操作 -->
        <div id="maskLayer"
             style="display: none; position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.3); z-index: 9998;"></div>

        <!-- 进度条容器 -->
        <div id="progressContainer"
             style="display: none; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); padding: 20px; background: #fff; border-radius: 6px; box-shadow: 0 2px 20px rgba(0,0,0,0.2); z-index: 9999; min-width: 300px;">
            <div style="width: 100%; height: 20px; background: #eee; border-radius: 10px; overflow: hidden;">
                <div id="progressBar" style="height: 100%; background: #4CAF50; width: 0%; transition: width 0.3s;"></div>
            </div>
            <p style="text-align: center; margin-top: 10px;">进度：<span id="progressText">0%</span></p>
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
            elem: '#kssj'
        });

        laydate.render({
            elem: '#jssj'
        });

        var renderTable = function () {
            var ysw = $('#ysw').val();
            var organ = $('#organ').val();
            $('#kssj').val(new Date().Format('yyyy-MM-dd'));
            $('#jssj').val(new Date().Format('yyyy-MM-dd'));

            table.render({
                elem: '#cdpfTable'
                , url: '${path}/count/cdpf/DataGrid'
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
                ,where: {
                    ysw:ysw,
                    organ: organ,
                    kssj: $('#kssj').val(),
                    jssj: $('#jssj').val()
                }
                , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                , cols: [[
                    {field: 'organ', title: '企业名称', align: 'center', sort: true, display: false},
                    {field: 'ysw', title: '运输物', align: 'center', sort: true},
                    {field: 'ysl', title: '运输量', align: 'center', sort: true},
                    {field: 'yssj', title: '运输时间', align: 'center', sort: true},
                    {
                        field: 'action', align: 'center', title: '操作',width:150,
                        templet: '#actionHandle'
                    }
                ]]
                , page: true
                , limit: 15
                , limits: [5, 10, 15, 20, 25, 30]
            });
        }
        renderTable();

        $.ajax({
            type: "post",
            url: '${path}/count/organization/orgtreelevelForOrgan?organtype=',
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

        $(document).on('click', '#addDetail', function () {
            parent.layer.closeAll();
            parent.layer.open({
                type: 2,
                title: '新增',
                shadeClose: true,
                shade: false,
                maxmin: false, //开启最大化最小化按钮
                // btn: ['新增', '关闭'],
                skin: 'my-skin',
                area: ['600px', '350px'],
                content: ['${path}/count/cdpf/addPage'],
                end: function () {
                    renderTable();
                }
            });
        })

        $(".downpanel").on("click", ".layui-select-title", function (e) {
            $(".layui-form-select").not($(this).parents(".layui-form-select")).removeClass("layui-form-selected");
            $(this).parents(".downpanel").toggleClass("layui-form-selected");
            layui.stope(e);
        }).on("click", "dl i", function (e) {
            layui.stope(e);
        });

        // 搜索按钮点击事件
        $('#BtnSearch').click(function () {
            var ysw = $('#ysw').val();
            var organ = $('#organ').val();
            table.reload('cdpfTable', {
                page: {
                    curr: 1//重新从第一页开始
                }
                ,where: {
                    ysw:ysw,
                    organ: organ,
                    kssj: $('#kssj').val(),
                    jssj: $('#jssj').val()
                }
            });
        });

        // 清空按钮点击事件
        $('#bmdBtnClear').click(function () {
            $('#treeclass').html("选择机构");
            $('#ysw').val('');
            $('#kssj').val('');
            $('#jssj').val('');
        });
        table.on('tool(vehicleFilter)', function (obj) {
            var data = obj.data;

            if (obj.event === 'editDetail') {
                // layer.msg('JYLSH：' + data.jylsh + ' 的查看操作');
                //iframe窗
                parent.layer.closeAll();
                parent.layer.open({
                    type: 2,
                    title: '编辑',
                    shadeClose: true,
                    shade: false,
                    maxmin: false, //开启最大化最小化按钮
                    // btn: ['确认', '关闭'],
                    area: ['600px', '350px'],
                    skin: 'my-skin',
                    content: ['${path}/count/cdpf/editPage?id=' + data.id],
                    end: function () {
                        renderTable();
                    }
                });
            } else if (obj.event === 'deleteDetail') {
                layer.confirm('删除可能会影响系统正常运行，您是否要删除当前基础代码记录信息？',
                    function (index) {
                        // 删除
                        $.ajax({
                            type: "post",
                            url: '${path }/count/cdpf/delete',
                            data: {"id": data.id},
                            dataType: 'json',
                            success: function (data) {
                                if (data.success) {
                                    layer.msg('删除成功', {icon: 1});
                                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                    setTimeout(function () {
                                        renderTable();
                                    }, 1000);
                                } else {
                                    layer.msg(data.msg, {icon: 2});
                                }
                            }
                        });

                    }
                )
            } else if (obj.event === 'edit') {
                layer.alert('编辑行：<br>' + JSON.stringify(data))
            }
        });

    });

</script>

</body>
</html>