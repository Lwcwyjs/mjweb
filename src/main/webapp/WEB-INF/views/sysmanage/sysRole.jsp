<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>

<html>
<head>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>角色管理</title>
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
            background-color: inherit;
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
            color: white;
        }

        .layui-show cite {
            color: white;
        }

        #main .childType {
            text-align: center;
            /* margin: 20px 10px; */
            background-color: #595763;
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
            background-color: #575d68;
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
            background-color: #47444d;
            color: white;
        }

        .parentTypeName {
            color: #06ddf5;
            text-align: center;
            background-color: #434857;
            font-size: 18px;
            padding: 5px;
            margin-right: 20px;
            /*cursor: pointer;*/
        }
    </style>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-body">
            <div class="layui-row">
                <div style="color: #3392FE; background-color: white; font-size: 16px; padding: 5px;height: 20px">
                    <div style="float: right; cursor: pointer;" id="addDetail">
                        <img src="${staticPath}/static/assets/images/add.png" style="margin-bottom: 2px;"> 新建
                    </div>
                </div>

                <table class="layui-hide" id="tblResource" lay-filter="vehicleFilter"></table>
            </div>


            <script type="text/html" id="actionHandle" lay-event="detail">
                <shiro:hasPermission name="/admin/role/grant">
					<span style="height: 24px; background-color: transparent;  cursor: pointer;"
                          lay-event="grantDetail">
                        <span style="margin-left: 1px;"></span>
                        <img src="${staticPath}/static/assets/images/transmit.png" style="margin-bottom: 5px;">
                        <span style="margin: 5px; font-size: 16px;"> 访问授权</span>
                    </span>
                </shiro:hasPermission>
<%--                <shiro:hasPermission name="/admin/role/transmission">--%>
<%--					<span style="height: 24px; background-color: transparent;  cursor: pointer;"--%>
<%--                          lay-event="transmissionDetail">--%>
<%--                        <span style="margin-left: 1px;"></span>--%>
<%--                        <img src="${staticPath}/static/assets/images/grant.png"--%>
<%--                             style="margin-bottom: 5px; cursor: pointer;">--%>
<%--                        <span style="margin: 5px; font-size: 16px; cursor: pointer;">传播授权</span>--%>
<%--                    </span>--%>
<%--                </shiro:hasPermission>--%>
                <shiro:hasPermission name="/admin/role/edit">
					<span style="height: 24px; background-color: transparent; cursor: pointer;"
                          lay-event="editDetail">
                        <span style="margin-left: 1px;"></span>
                        <img src="${staticPath}/static/assets/images/edit.png"
                             style="margin-bottom: 5px; cursor: pointer;">
                        <span style="margin: 5px; font-size: 16px; cursor: pointer;">编辑</span>
                    </span>
                </shiro:hasPermission>
                <shiro:hasPermission name="/admin/role/delete">
					<span style="height: 24px; background-color: transparent; cursor: pointer;"
                          lay-event="deleteDetail">
                        <span style="margin-left: 1px;"></span>
                        <img src="${staticPath}/static/assets/images/delete.png"
                             style="margin-bottom: 5px; cursor: pointer;">
                        <span style="margin: 5px; font-size: 16px; cursor: pointer;">删除</span>
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

        var renderTable = function (pid) {//树桩表格参考文档：https://gitee.com/whvse/treetable-lay
            if(pid==undefined){
                var pid = ${id};
            }
            pid = pid == undefined ? "-1" : pid;
            pid = pid == "" ? "-1" : pid;
            if (pid == 1){
                pid = -1;
            }
            layer.load(2);
            treetable.render({
                treeColIndex: 2,//树形图标显示在第几列
                treeSpid: pid,//最上级的父级id
                treeIdName: 'id',//id字段的名称
                treePidName: 'pid',//pid字段的名称
                treeDefaultClose: false,//是否默认折叠
                treeLinkage: true,//父级展开时是否自动展开所有子级
                elem: '#tblResource',
                url: '${path }/admin/role/treeGrid',
                page: false,
                where: {
                    pid: pid
                },
                cols: [[
                    {type: 'numbers'}
                    , {field: 'id', title: 'id', width: 80, align: 'center'}
                    , {field: 'name', title: '名称', width: 300, align: 'left'}
                    , {field: 'seq', title: '排序', width: 80, align: 'center'}
                    , {field: 'description', title: '描述', width: 200, align: 'center'}
                    , {
                        field: 'status', title: '状态', width: 100, align: 'center', templet: function (d, row, index) {
                            var value = d.status;
                            switch (value) {
                                case 0:
                                    return "正常";
                                    break;
                                case 1:
                                    return "停用";
                                    break;
                                default:
                                    return "未配置";
                                    break;
                            }
                        }
                    }
                    , {fixed: 'right', title: '操作', align: 'center', toolbar: '#actionHandle'}
                ]],
                done: function () {
                    layer.closeAll('loading');
                }
            });
        };
        //重新加载数据
        var reloadTable=function(){
            renderTable();
        };

        renderTable(${id});
        $(document).on('click','#addDetail',function(){
            parent.layer.closeAll();
            parent.layer.open({
                type: 2,
                title: '新增',
                shadeClose: true,
                shade: false,
                maxmin: false, //开启最大化最小化按钮
                // btn: ['新增', '关闭'],
                skin: 'my-skin',
                area: ['500px', '420px'],
                content: ['${path}/admin/role/addPage'],
                end:  function () {
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
                    area: ['500px', '420px'],
                    skin: 'my-skin',
                    content: ['${path}/admin/role/editPage?id=' + data.id],
                    end:  function () {
                        renderTable();
                    }
                });
            } else if (obj.event === 'deleteDetail') {
                layer.confirm('删除可能会影响系统正常运行，您是否要删除当前基础代码记录信息？',
                    function (index) {
                        // 删除
                        $.ajax({
                            type: "post",
                            url: '${path }/admin/role/delete',
                            data: {"id": data.id},
                            dataType: 'json',
                            success: function (data) {
                                if (data.success) {
                                    layer.msg('删除成功', {icon: 1});
                                    setTimeout(function () {
                                        renderTable();
                                    }, 1000);
                                } else {
                                    layer.msg(data.msg, {icon: 2});
                                }
                            }
                        });

                    })
            } else if (obj.event === 'grantDetail') {
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
                    area: ['500px', '420px'],
                    skin: 'my-skin',
                    content: ['${path}/admin/role/grantPage?id=' + data.id],
                    end:  function () {
                        renderTable();
                    }
                });
            } else if (obj.event === 'transmissionDetail') {
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
                    area: ['420px', '420px'],
                    skin: 'my-skin',
                    content: ['${path}/admin/role/transmissionPage?id=' + data.id],
                    end:  function () {
                        renderTable();
                    }
                });
            } else if (obj.event === 'edit') {
                layer.alert('编辑行：<br>' + JSON.stringify(data))
            }
        });
    });
</script>

</body>
</html>