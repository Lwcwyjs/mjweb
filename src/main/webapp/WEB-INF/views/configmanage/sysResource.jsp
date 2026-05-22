<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>

<html>
<head>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>功能配置管理</title>
    <style>
        .layui-badge-rim + .layui-badge-rim {
            margin-left: 5px;
        }


        .layui-form-label {
            width: 70px;
        }


        .downpanel .layui-select-title span {
            line-height: 38px;
        }

        /*继承父类颜色*/
        .downpanel dl dd:hover {
            background-color: white;
        }

        .layui-tree-entry:hover {
            background-color: #1E9FFF;
        }

        .downpanel a cite {
            color: white;
        }

        .layui-badge-rim + .layui-badge-rim {
            margin-left: 5px;
        }

        #main a {
            color: white;
        }

        .layui-form-label {
            width: 70px;
        }

        .layui-tree li i {
            color: black;
        }

        .layui-show cite {
            color: white;
        }

        #main .childType {
            text-align: center;
            /* margin: 20px 10px; */
            background-color: white;
            font-size: 16px;
            line-height: 30px;
            border: solid 1px grey;
            padding: 10px;
            cursor: pointer;
        }

        .active {
            color: #06ddf5;
        }

        .layui-layer-btn- {
            background-color: white;
            /*text-align: center !important;*/
        }

        .my-skin .layui-layer-btn a {
            background-color: #188fcf;
            border: 1px solid #188fcf;
            color: white;
            margin: 0 30px 0 20px;
        }

        .layui-layer-btn1 {
            background-color: #188fcf !important;
            border: 1px solid #188fcf !important;
            color: white !important;
            margin-left: 30px !important;
            margin-right: 20px !important;
        }

        .layui-layer-content {
            background-color: white;
            color: #313131;
        }

        .parentTypeName {
            color: #06ddf5;
            text-align: center;
            background-color: white;
            font-size: 18px;
            padding: 5px;
            margin-right: 20px;
            /*cursor: pointer;*/
        }

        .layui-tree-icon {
            color: #f9f8ff;
        }

        .layui-tree-txt {
            color: black;
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
                        <label class="layui-form-label">资源名称</label>
                        <div class="layui-inline">
                            <div class="layui-input-inline">
                                <div class="layui-unselect layui-form-select downpanel" id="downpl">
                                    <div class="layui-select-title">
                                        <span class="layui-input layui-unselect" id="resourceTree">选择资源名称</span>
                                        <input type="hidden" name="resourceid" id="resourceid" value="">
                                        <i class="layui-edge"></i>
                                    </div>
                                    <dl class="layui-anim layui-anim-upbit" id="divselect">
                                        <dd>
                                            <ul id="selectResource"></ul>
                                        </dd>
                                    </dl>
                                </div>
                            </div>
                        </div>
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
            <div style="color: #3392FE; background-color: white; font-size: 18px; padding: 5px;height: 20px">
                <div style="float: right; cursor: pointer;" id="addDetail">
                    <img src="${staticPath}/static/assets/images/add.png" style="margin-bottom: 2px;"> 新建
                </div>
            </div>

            <table class="layui-hide" id="tblResource" lay-filter="vehicleFilter"></table>
        </div>


        <script type="text/html" id="actionHandle" lay-event="detail">
            <shiro:hasPermission name="/admin/resource/edit">
                <%--                    <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="itemDetail">--%>

                <%--                    </a>--%>
                <span style="height: 24px; background-color: transparent; color: black; cursor: pointer;"
                      lay-event="editDetail">
                        <span style="margin-left: 1px;"></span>
                        <img src="${staticPath}/static/assets/images/edit.png" style="margin-bottom: 5px;">
                        <span style="margin: 5px; font-size: 16px;"> 编 辑 </span>
                    </span>

            </shiro:hasPermission>
            <shiro:hasPermission name="/admin/resource/delete">
                <%--                    <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="itemDetail">--%>

                <%--                    </a>--%>
                <span style="height: 24px; background-color: transparent; color: black; cursor: pointer;"
                      lay-event="deleteDetail">
                        <span style="margin-left: 1px;"></span>
                        <img src="${staticPath}/static/assets/images/delete.png"
                             style="margin-bottom: 5px; cursor: pointer;">
                        <span style="margin: 10px; font-size: 16px; cursor: pointer;"> 删 除 </span>
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
    }).use(['form', 'table', 'tree', 'treetable'], function () {

        var $ = layui.jquery;
        var table = layui.table;
        var form = layui.form;
        var tree = layui.tree;
        var treetable = layui.treetable;


        <%--$.ajax({--%>
        <%--    type: "post",--%>
        <%--    url: '${path }/admin/resource/treelist',--%>
        <%--    dataType: 'json',--%>
        <%--    success: function (d) {--%>
        <%--        tree.render({--%>
        <%--            elem: '#selectResource'--%>
        <%--            , data: d--%>
        <%--            , showCheckbox: false  //是否显示复选框--%>
        <%--            , showLine: false--%>
        <%--            , id: 'resourcetree'--%>
        <%--            , isJump: false //是否允许点击节点时弹出新窗口跳转--%>
        <%--            , click: function (obj) {--%>
        <%--                var data = obj.data;  //获取当前点击的节点数据--%>
        <%--                $('#resourceid').val(data.id);--%>
        <%--                $('#resourceTree').html(data.title);--%>
        <%--            }--%>
        <%--        });--%>
        <%--    }--%>
        <%--});--%>
        $.ajax({
            type: "post",
            url: '${path }/admin/resource/tree',
            dataType: 'json',
            success: function (d) {
                tree.render({
                    elem: '#selectResource'
                    , data: d
                    , showCheckbox: false  //是否显示复选框
                    , showLine: true
                    , id: 'resourcetree'
                    , isJump: true //是否允许点击节点时弹出新窗口跳转
                    , click: function (obj) {
                        var data = obj.data;  //获取当前点击的节点数据
                        $('#resourceid').val(data.id);
                        $('#resourceTree').html(data.title);
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
        var renderTable = function (pid) {//树桩表格参考文档：https://gitee.com/whvse/treetable-lay
            if (pid == undefined) {
                var pid = $('#resourceid').val();
            }
            pid = pid == undefined ? "-1" : pid;
            pid = pid == "" ? "-1" : pid;
            layer.load(2);
            treetable.render({
                treeColIndex: 1,//树形图标显示在第几列
                treeSpid: pid,//最上级的父级id
                treeIdName: 'id',//id字段的名称
                treePidName: 'pid',//pid字段的名称
                treeDefaultClose: false,//是否默认折叠
                treeLinkage: true,//父级展开时是否自动展开所有子级
                elem: '#tblResource',
                url: '${path }/admin/resource/treeGrid',
                page: false,
                where: {
                    pid: pid
                },
                cols: [[
                    {type: 'numbers'}
                    , {field: 'id', title: 'ID', width: '10%', align: 'center'}
                    , {field: 'name', title: '资源名称', width: 200}
                    , {field: 'url', title: '资源路径'}
                    , {field: 'seq', title: '排序', align: 'center'}
                    , {field: 'iconCls', title: '图标', align: 'center'}
                    , {field: 'resourcetype', title: '资源类型', align: 'center', templet: '#tResourceType'}
                    , {field: 'pid', title: '父ID', align: 'center'}
                    , {fixed: 'right', title: '操作', align: 'center', toolbar: '#actionHandle', width: 200}
                ]],
                height: 'full-150',
                done: function () {
                    layer.closeAll('loading');
                }
            });
        };

        renderTable();
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
                area: ['460px', '400px'],
                content: ['${path}/admin/resource/addPage'],
                end: function () {
                    renderTable();
                }
            });
        })


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
                    area: ['460px', '400px'],
                    skin: 'my-skin',
                    content: ['${path}/admin/resource/editPage?id=' + data.id],
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
                            url: '${path }/admin/resource/delete',
                            data: {"id": data.id},
                            dataType: 'json',
                            success: function (data) {
                                layer.msg('删除成功', {icon: 1});
                                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                setTimeout(function () {
                                    renderTable();
                                }, 1000);
                            }
                        });

                    }
                )
            } else if (obj.event === 'edit') {
                layer.alert('编辑行：<br>' + JSON.stringify(data))
            }
        });

        // 搜索按钮点击事件
        $('#userBtnSearch').click(function () {
            renderTable();
        });

        // 清空按钮点击事件
        $('#userBtnClear').click(function () {
            $('#resourceTree').html("选择资源名称");
            $('#resourceid').val('');
        });

        $('.parentTypeName').click(function () {
            // 设置选中状态颜色
            $('.parentTypeName').css('color', '#06ddf5');
            // 赋值
            $('#oi_name').val($('.parentTypeName').html().trim());
            // 清单子项颜色
            $('.childType').each(function () {
                $(this).removeClass('active');
            });
            $('#userBtnSearch').click();
        })
    });

</script>

</body>
</html>