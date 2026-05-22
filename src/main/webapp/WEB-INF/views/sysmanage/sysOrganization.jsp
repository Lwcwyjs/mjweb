<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>

<html>
<head>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>机构管理</title>
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
                                    <input type="hidden" name="porgan" id="porgan" value="">
                                    <i class="layui-edge"></i>
                                </div>
                                <dl class="layui-anim layui-anim-upbit" id="divselect">
                                    <dd>
                                        <ul id="selectOrgan"></ul>
                                    </dd>
                                </dl>
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
                <div style="color: #3392FE; background-color: white; font-size: 16px; padding: 5px;height: 20px">
                    <div style="float: right; cursor: pointer;" id="addDetail">
                        <img src="${staticPath}/static/assets/images/add.png" style="margin-bottom: 2px;"> 新建
                    </div>
                    <div style="float: right;margin-right: 20px; cursor: pointer;" onclick="refreshFun()">
                        <img src="${staticPath}/static/easyui/themes/icons/refresh.png" style="margin-bottom: 2px;">
                        刷新内存参数
                    </div>
                </div>

                <table class="layui-hide" id="tblResource" lay-filter="vehicleFilter"></table>
            </div>


            <script type="text/html" id="actionHandle" lay-event="detail">
                <shiro:hasPermission name="/admin/organization/edit">
                    <span style="height: 24px;width: 50px; background-color: transparent; color: #313131; cursor: pointer;"
                          lay-event="editDetail">
                        <span style="margin-left: 1px;"></span>
                        <img src="${staticPath}/static/assets/images/edit.png"
                             style="margin-bottom: 5px; cursor: pointer;">
                        <span style="margin: 0px 1px; font-size: 16px; cursor: pointer;">编辑</span>
                    </span>
                </shiro:hasPermission>
                <shiro:hasPermission name="/admin/organization/delete">
					<span style="height: 24px; width: 50px; background-color: transparent; color: #313131; cursor: pointer;"
                          lay-event="deleteDetail">
                        <span style="margin-left: 1px;"></span>
                        <img src="${staticPath}/static/assets/images/delete.png"
                             style="margin-bottom: 5px; cursor: pointer;">
                        <span style="margin: 0px 1px; font-size: 16px; cursor: pointer;">删除</span>
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

        var lastTreeState = null;

        var getTreeOpenIds = function () {
            var ids = [];
            $('#tblResource').next('.treeTable').find('.treeTable-icon.open[lay-ttype="dir"]').each(function () {
                var tid = $(this).attr('lay-tid');
                if (tid) {
                    ids.push(tid);
                }
            });
            return ids;
        };

        var getTreeScrollTop = function () {
            var $body = $('#tblResource').next('.treeTable').find('.layui-table-body');
            if ($body.length === 0) {
                return 0;
            }
            return $body.scrollTop();
        };

        var restoreTreeState = function (state) {
            if (!state) {
                return;
            }
            if (state.openIds && state.openIds.length > 0) {
                for (var i = 0; i < state.openIds.length; i++) {
                    var id = state.openIds[i];
                    var $icon = $('#tblResource').next('.treeTable').find('.treeTable-icon[lay-tid="' + id + '"]');
                    if ($icon.length > 0 && !$icon.hasClass('open')) {
                        $icon.trigger('click');
                    }
                }
            }
            if (state.scrollTop != null) {
                var $body = $('#tblResource').next('.treeTable').find('.layui-table-body');
                if ($body.length > 0) {
                    $body.scrollTop(state.scrollTop);
                }
            }
            if (state.focusId) {
                var $focusIcon = $('#tblResource').next('.treeTable').find('.treeTable-icon[lay-tid="' + state.focusId + '"]');
                if ($focusIcon.length > 0) {
                    var $tr = $focusIcon.closest('tr');
                    if ($tr.length > 0) {
                        $tr.addClass('row-highlight');
                        setTimeout(function () {
                            $tr.removeClass('row-highlight');
                        }, 1500);
                    }
                }
            }
        };

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
        var renderTable = function (porgan, organ, state) {//树桩表格参考文档：https://gitee.com/whvse/treetable-lay
            if (porgan == undefined) {
                porgan = $('#porgan').val();
            }
            if (organ == undefined) {
                organ = $('#organ').val();
            }
            porgan = porgan == undefined ? "-1" : porgan;
            porgan = porgan == "" ? "-1" : porgan;
            organ = organ == undefined ? "-1" : organ;
            organ = organ == "" ? "-1" : organ;
            layer.load(2);
            lastTreeState = state || null;
            treetable.render({
                treeColIndex: 2,//树形图标显示在第几列
                treeSpid: porgan,//最上级的父级id
                treeIdName: 'organ',//id字段的名称
                treePidName: 'porgan',//pid字段的名称
                treeDefaultClose: true,//是否默认折叠
                treeLinkage: false,//父级展开时是否自动展开所有子级
                elem: '#tblResource',
                url: '${path }/admin/organization/treeGrid',
                page: false,
                where: {
                    porgan: porgan,
                    organ: organ
                },
                cols: [[
                    {type: 'numbers'}
                    , {field: 'organ', title: '机构编号', align: 'center'}
                    , {field: 'name', title: '机构名称', align: 'left'}
                    , {field: 'jc', title: '简称', align: 'center'}
                    , {field: 'seq', title: '排序', align: 'center'}
                    , {
                        field: 'organtype', title: '机构类型', align: 'center', templet: function (d, row, index) {
                            var value = d.organtype;
                            switch (value) {
                                case "0":
                                    return "省环保厅";
                                    break;
                                case "1":
                                    return "市环保局";
                                    break;
                                case "2":
                                    return "区县环保局";
                                    break;
                                case "3":
                                    return "企业";
                                    break;
                                default:
                                    return "未配置";
                                    break;
                            }
                        }
                    }
                    , {
                        field: 'status', title: '状态', align: 'center', templet: function (d, row, index) {
                            var value = d.status;
                            switch (value) {
                                case "0":
                                    return "停用";
                                    break;
                                case "1":
                                    return "启用";
                                    break;
                                default:
                                    return "未配置";
                                    break;
                            }
                        }
                    }
                    , {fixed: 'right', title: '操作', align: 'center', width: 150, toolbar: '#actionHandle'}
                ]],
                height: 'full-150',
                done: function (res, curr, count) {
                    layer.closeAll('loading');
                    restoreTreeState(lastTreeState);
                    lastTreeState = null;
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
                area: ['900px', '800px'],
                content: ['${path}/admin/organization/addPage'],
                end: function () {
                    renderTable();
                }
            })
        })


        table.on('tool(vehicleFilter)', function (obj) {
            var data = obj.data;

            if (obj.event === 'editDetail') {
                // layer.msg('JYLSH：' + data.jylsh + ' 的查看操作');
                //iframe窗
                var treeState = {
                    openIds: getTreeOpenIds(),
                    scrollTop: getTreeScrollTop(),
                    focusId: data.organ
                };
                parent.layer.closeAll();
                parent.layer.open({
                    type: 2,
                    title: '编辑',
                    shadeClose: true,
                    shade: false,
                    maxmin: false, //开启最大化最小化按钮
                    // btn: ['确认', '关闭'],
                    area: ['900px', '800px'],
                    skin: 'my-skin',
                    content: ['${path}/admin/organization/editPage?code=' + data.organ],
                    end: function () {
                        renderTable(undefined, undefined, treeState);
                    }
                });
            } else if (obj.event === 'deleteDetail') {
                layer.confirm('删除可能会影响系统正常运行，您是否要删除当前基础代码记录信息？',
                    function (index) {
                        // 删除
                        $.ajax({
                            type: "post",
                            url: '${path }/admin/organization/delete',
                            data: {"code": data.organ},
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

        // 搜索按钮点击事件
        $('#userBtnSearch').click(function () {
            renderTable();
        });

        // 清空按钮点击事件
        $('#userBtnClear').click(function () {
            $('#treeclass').html("选择资源名称");
            $('#organ').val('');
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

    // 刷新内存
    function refreshFun() {
        $.ajax({
            type: "post",
            url: '${path}/admin/cysysoptions/refresh',
            dataType: 'json',
            success: function (data) {
                if (data.success) {
                    layer.msg('刷新成功', {icon: 1});
                } else {
                    layer.msg(data.msg, {icon: 2});
                }
            }
        });
    }
</script>

</body>
</html>
