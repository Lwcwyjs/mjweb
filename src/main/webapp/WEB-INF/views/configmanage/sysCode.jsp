<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>

<html>
<head>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>基础代码管理</title>
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
            background-color: #F3FAFF;
            font-size: 16px;
            line-height: 30px;
            border: solid 1px #f2f2f2;
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
            background-color: #3392FE;
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
            <div class="layui-form toolbar" id="vehicleForm">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">代码类别</label>
                        <div class="layui-input-inline">
                            <input name="oi_name" id="oi_name" class="layui-input" value="类别" placeholder="请输入代码类别"/>
                        </div>

                        <%--                        <div class="layui-input-inline">--%>
                        <%--                            <select id="oi_name" name="oi_name" lay-filter="oiNameFilter" lay-search>--%>
                        <%--                                <option value="">请选择</option>--%>
                        <%--                            </select>--%>
                        <%--                        </div>--%>

                        <label class="layui-form-label">代码名称</label>
                        <div class="layui-input-inline">
                            <input name="oi_value" id="oi_value" class="layui-input" placeholder="请输入代码名称"/>
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
                <div class="layui-col-xs6 layui-col-sm6 layui-col-md2" style="width: 20%">
                    <div class="parentTypeName" data-code="0" style="cursor: pointer;">
                        类别
                    </div>
                    <div id="main"
                         style="width: 95%; height: 70vh; margin-top: 10px; color: #313131; cursor: pointer;">

                    </div>
                </div>
                <div class="layui-col-xs6 layui-col-sm6 layui-col-md10" style="width: 80%">
                    <div style="color: #3392FE; background-color: #F3FAFF; font-size: 18px; padding: 5px;">数据列表
                        <div style="float: right; cursor: pointer;" id="addDetail">
                            <img src="${staticPath}/static/assets/images/add.png" style="margin-bottom: 2px;"> 新建
                        </div>
                    </div>

                    <table class="layui-hide" id="tblsysCode" lay-filter="vehicleFilter"></table>
                </div>
            </div>


            <script type="text/html" id="actionHandle" lay-event="detail">
                <shiro:hasPermission name="/admin/syscode/edit">
                    <%--                    <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="itemDetail">--%>

                    <%--                    </a>--%>
                    <span style="height: 24px; background-color: transparent; color: #313131; cursor: pointer;"
                          lay-event="editDetail">
                        <span style="margin-left: 1px;"></span>
                        <img src="${staticPath}/static/assets/images/edit.png" style="margin-bottom: 5px;">
                        <span style="margin: 5px; font-size: 16px;"> 编 辑 </span>
                    </span>

                </shiro:hasPermission>
                <shiro:hasPermission name="/admin/syscode/delete">
                    <%--                    <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="itemDetail">--%>

                    <%--                    </a>--%>
                    <span style="height: 24px; background-color: transparent; color: #313131; cursor: pointer;"
                          lay-event="deleteDetail">
                        <span style="margin-left: 1px;"></span>
                        <img src="${staticPath}/static/assets/images/delete.png"
                             style="margin-bottom: 5px; cursor: pointer;">
                        <span style="margin: 10px; font-size: 16px; cursor: pointer;">删除</span>
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
    }).use(['form', 'table', 'tree'], function () {

        var $ = layui.jquery;
        var table = layui.table;
        var form = layui.form;
        var tree = layui.tree;

        $.ajax({
            type: "post",
            url: '${path}/admin/syscode/treeType',
            dataType: 'json',
            success: function (d) {
                // $("[selectOrgan]").html("");
                // tree({
                //     elem: '#main',
                //     nodes: d,
                //     spread: true,
                //     click: function (node, a) {
                //         //console.log(a);
                //         var $select = $($(this)[0].elem).parents(".layui-form-select");
                //         $select.removeClass("layui-form-selected").find(".layui-select-title span").html(node.name).end().find("input:hidden[name='treeType']").val(node.id);
                //     },
                //     success: function () {
                //
                //     }
                // });

                if (d != undefined) {
                    var childArr = d[0].children;

                    var childHtml = "";
                    for (var i = 0; i < childArr.length; i++) {
                        // if (i == 0) {
                        //     childHtml += "<div class='childType active' data-id='" + childArr[i].code + "'>" + childArr[i].name + "</div>";
                        // } else {
                        //     childHtml += "<div class='childType' data-id='" + childArr[i].code + "'>" + childArr[i].name + "</div>";
                        // }
                        childHtml += "<div class='childType' data-id='" + childArr[i].code + "'>" + childArr[i].name + "</div>";
                    }
                    $('#main').html(childHtml);

                    $('.childType').click(function (e) {
                        // $('.childType .active').removeClass('active');
                        // $(e).addClass("active");

                        $('.parentTypeName').css('color', 'white');
                        $(this).addClass("active").siblings().removeClass("active");
                        $('#oi_name').val($(this).html().trim());
                        $('#userBtnSearch').click();
                    });

                    // $('.parentTypeName').click(function (e) {
                    //     $(this).addClass("active");
                    // });

                }

                $('#main li ul').addClass('layui-show');
            }
        });

        $(".downpanel").on("click", ".layui-select-title", function (e) {
            $(".layui-form-select").not($(this).parents(".layui-form-select")).removeClass("layui-form-selected");
            $(this).parents(".downpanel").toggleClass("layui-form-selected");
            layui.stope(e);
        }).on("click", "dl i", function (e) {
            layui.stope(e);
        });


        var renderTable = function () {
            var oi_name = $('#oi_name').val();
            var oi_value = $('#oi_value').val();
            table.render({
                elem: '#tblsysCode'
                , url: '${path}/admin/syscode/dataGrid'
                , method: 'post'
                , where: {
                    oi_name: oi_name
                    , oi_value: oi_value
                }
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
                    {field: 'id', title: '序号', align: 'center', sort: true}
                    , {field: 'oi_name', title: '代码类别', align: 'center', sort: true}
                    , {field: 'oi_value', title: '代码名称', align: 'center', sort: true}
                    , {
                        field: 'oi_code', title: '代码', align: 'center', sort: true
                    }
                    , {
                        field: 'seq', title: '排序用编号', align: 'center', sort: true
                    } //minWidth：局部定义当前单元格的最小宽度，layui 2.2.1 新增
                    , {
                        field: 'action', title: '操作', align: 'center',
                        templet: '#actionHandle'
                    }
                ]]
                , page: true
                , limit: 10
                , limits: [5, 10, 15, 20, 25, 30]
            });
        }
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
                area: ['500px', '330px'],
                content: ['${path}/admin/syscode/addPage'],
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
                    area: ['500px', '330px'],
                    skin: 'my-skin',
                    content: ['${path}/admin/syscode/editPage?id=' + data.id],
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
                            url: '${path }/admin/syscode/delete',
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
            var oi_name = $('#oi_name').val();
            var oi_value = $('#oi_value').val();

            table.reload('tblsysCode', {
                page: {
                    curr: 1//重新从第一页开始
                },
                where: {
                    oi_name: oi_name
                    , oi_value: oi_value
                }
            });
        });

        // 清空按钮点击事件
        $('#userBtnClear').click(function () {

            $('#oi_name').val('');
            $('#oi_value').val('');
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