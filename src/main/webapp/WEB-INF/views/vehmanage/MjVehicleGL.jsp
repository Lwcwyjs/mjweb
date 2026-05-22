<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>运输车辆信息管理</title>
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
                        <label class="layui-form-label">车牌号码</label>
                        <div class="layui-input-inline">
                            <input name="cphm" id="cphm" placeholder="可模糊查询" class="layui-input"/>
                        </div>
                        <label class="layui-form-label">车牌颜色</label>
                        <div class="layui-input-inline">
                            <select id="cpys" name="cpys" lay-filter="shztFilter">
                                <option value="">请选择</option>
                                <option value="0">蓝色</option>
                                <option value="1">黄色</option>
                                <option value="2">白色</option>
                                <option value="3">黑色</option>
                                <option value="4">绿色</option>
                                <option value="5">其他</option>
                                <option value="6">黄绿色</option>
                            </select>
                        </div>
                        <label class="layui-form-label">排放标准</label>
                        <div class="layui-input-inline">
                            <select id="pfbz" name="pfbz" lay-filter="shztFilter">
                                <option value="">请选择</option>
                                <option value="0">国0</option>
                                <option value="1">国Ⅰ</option>
                                <option value="2">国Ⅱ</option>
                                <option value="3">国Ⅲ</option>
                                <option value="4">国Ⅳ</option>
                                <option value="5">国Ⅴ</option>
                                <option value="6">国Ⅵ</option>
                                <option value="D">电动</option>
                                <option value="X">未知</option>
                            </select>
                        </div>

                    </div>
                    <div class="layui-form-item" lay-filter="searchForm">
                        <label class="layui-form-label">使用性质</label>
                        <div class="layui-input-inline">
                            <select id="syxz" name="syxz" lay-filter="shztFilter">
                                <option value="">请选择</option>
                            </select>
                        </div>
                        <label class="layui-form-label">创建时间</label>
                        <div class="layui-input-inline">
                            <input name="kssj" id="kssj" class="layui-input"/>
                        </div>
                        <div class="layui-input-inline">
                            <input name="jssj" id="jssj" class="layui-input"/>
                        </div>
                        <label class="layui-form-label">同步状态</label>
                        <div class="layui-input-inline">
                            <select id="sczt" name="sczt" lay-filter="shztFilter">
                                <option value="">请选择</option>
                                <option value="0">未同步</option>
                                <option value="1">同步成功</option>
                                <option value="2">同步失败</option>
                            </select>
                        </div>
                        <div class="layui-inline">
                            <button id="userBtnSearch" class="layui-btn icon-btn"><i
                                    class="layui-icon"></i>查询
                            </button>
                            <button type="reset" class="layui-btn icon-btn" id="userBtnClear"><i
                                    class="layui-icon"></i>清空
                            </button>
                            <button type="reset" class="layui-btn icon-btn" id="addDetail"><i
                                    class="layui-icon"></i>新建
                            </button>
                            <shiro:hasPermission name="/business/mjfdlvehicle/export">
                                <button type="button" lay-submit="" class="layui-btn layui-btn-warm" lay-filter="uploadImg"
                                        id="exportExcel">导出excel
                                </button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                </div>
            </div>
            <div class="layui-row">
                <table class="layui-hide" id="vehTable" lay-filter="vehicleFilter"></table>
            </div>


            <script type="text/html" id="actionHandle" lay-event="detail">
                <shiro:hasPermission name="/business/mjvehicle/edit">
					<span style="height: 24px; background-color: transparent; cursor: pointer;"
                          lay-event="editDetail">
                        <span style="margin-left: 1px;"></span>
                        <img src="${staticPath}/static/assets/images/edit.png"
                             style="margin-bottom: 5px; cursor: pointer;">
                        <span style="margin: 0px; font-size: 16px; cursor: pointer;">编辑</span>
                    </span>
                </shiro:hasPermission>
                <shiro:hasPermission name="/business/mjvehicle/del">
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
        <p style="text-align: center; margin-top: 10px;">导出进度：<span id="progressText">0%</span></p>
    </div>
</div>
<script type="text/javascript" src="${staticPath}/static/js/colmunFormat.js" charset="utf-8"></script>
<!-- 表格状态列 -->
<script>
    layui.config({
        base: '${staticPath}/static/module/'
    }).use(['form', 'table', 'tree','laydate'], function () {

        var $ = layui.jquery;
        var table = layui.table;
        var form = layui.form;
        var tree = layui.tree;
        var laydate = layui.laydate;
        //日期
        laydate.render({
            elem: '#kssj'
        });
        $.ajax({
            type: "post",
            url: '${path}/admin/syscode/combox',
            data: {"oi_name": '使用性质'},
            dataType: 'json',
            async:false,
            success: function (data) {
                var t;
                var t = "<option value='' selected='selected'>使用性质</option>";
                for (var i = 0; i < data.length; i++) {
                    if (data[i].oi_value != undefined && data[i].oi_code != undefined) {
                        t += '<option value="' + data[i].oi_code + '">' + data[i].oi_value + '</option>';
                    }
                }
                $('#syxz').append(t);
                form.render('select');
            }
        });
        laydate.render({
            elem: '#jssj'
        });
        $('#kssj').val(new Date().Format('yyyy-MM-dd'));
        $('#jssj').val(new Date().Format('yyyy-MM-dd'));
        var renderTable = function () {
            var organ = $('#organ').val();
            table.render({
                elem: '#vehTable'
                , url: '${path}/business/mjvehicle/dataGrid'
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
                    cphm: $('#cphm').val(),
                    cpys: $('#cpys').val(),
                    qybh: $('#organ').val(),
                    pfbz: $('#pfbz').val(),
                    syxz: $('#syxz').val(),
                    sczt: $('#sczt').val(),
                    kssj: $('#kssj').val(),
                    jssj: $('#jssj').val()
                }
                , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                , cols: [[
                    {
                        field: 'qybh', title: '所属机构', sort: true, templet: function (d, row, index) {
                            var value = d.qybh;
                            return getValue("organ", value, "${path }/admin/user/basedata/organ");
                        }
                    },
                    {field: 'cphm', title: '车牌号码', align: 'center', sort: true},
                    {
                        field: 'cpys',
                        title: '车牌颜色',
                        align: 'center',
                        sort: true,
                        templet: function (d, row, index) {
                            var value = d.cpys;
                            switch (value) {
                                case "0":
                                    return "蓝色";
                                    break;
                                case "1":
                                    return "黄色";
                                    break;
                                case "2":
                                    return "白色";
                                    break;
                                case "3":
                                    return "黑色";
                                    break;
                                case "4":
                                    return "绿色";
                                    break;
                                case "5":
                                    return "其他";
                                    break;
                                case "6":
                                    return "黄绿色";
                                    break;
                                default:
                                    return value;
                                    break;
                            }
                        }
                    }, {field: 'ccdjrq', title: '注册时间', align: 'center', sort: true},

                    {
                        field: 'pfbz',
                        title: '排放标准',
                        align: 'center',
                        sort: true,
                        templet: function (d, row, index) {
                            var value = d.pfbz;
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
                    },{
                        field: 'tbdzbhs',
                        title: '同步情况',
                        align: 'center',
                        sort: true
                    },
                    {field: 'cjsj', title: '更新时间', align: 'center', sort: true},
                    {
                        field: 'action', align: 'center', title: '操作', width: 150,
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
                area: ['800px', '600px'],
                content: ['${path}/business/mjvehicle/addPageGL'],
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
        $('#userBtnSearch').click(function () {
            table.reload('vehTable', {
                page: {
                    curr: 1//重新从第一页开始
                }
                , where: {
                    cphm: $('#cphm').val(),
                    cpys: $('#cpys').val(),
                    qybh: $('#organ').val(),
                    pfbz: $('#pfbz').val(),
                    kssj: $('#kssj').val(),
                    jssj: $('#jssj').val()
                }
            });
        });
        $('#exportExcel').click(function () {

            // 2. 显示进度条和遮罩层，禁用导出按钮
            $('#progressContainer').show(); // 显示进度条
            $('#maskLayer').show(); // 显示遮罩层（阻止底层操作）
            $('#progressBar').width('0%');
            $('#progressText').text('0%');
            $(this).prop('disabled', true);

            // 3. 发起导出请求（保持不变）
            var url = '${path}/business/mjvehicle/startExport';
            var params = {
                cphm: $('#cphm').val(),
                cpys: $('#cpys').val(),
                qybh: $('#organ').val(),
                pfbz: $('#pfbz').val(),
                sczt: $('#sczt').val(),
                syxz: $('#syxz').val(),
                kssj: $('#kssj').val(),
                jssj: $('#jssj').val()
            };
            $.ajax({
                type: "post",
                url: url,
                data: params,
                dataType: 'json',
                success: function (data) {
                    if (data.code == 1) {
                        var taskId = data.taskId;
                        // 4. 轮询查询进度（保持不变）
                        var pollInterval = setInterval(function () {
                            $.ajax({
                                type: "post",
                                url: '${path}/business/mjvehicle/exportProgress',
                                data: {taskId: taskId},
                                dataType: 'json',
                                success: function (progressRes) {
                                    var progress = progressRes.progress;
                                    if (progress === -1) {
                                        // 任务不存在
                                        clearInterval(pollInterval);
                                        layer.msg('导出任务不存在', {icon: 2});
                                        resetUI();
                                    } else if (progress === -2) {
                                        // 导出失败
                                        clearInterval(pollInterval);
                                        layer.msg('导出失败，请重试', {icon: 2});
                                        resetUI();
                                    } else if (progress >= 100) {
                                        // 导出完成，触发下载
                                        clearInterval(pollInterval);
                                        $('#progressBar').width('100%');
                                        $('#progressText').text('100%');
                                        setTimeout(function () {
                                            window.location.href = '${path}/business/mjvehicle/downloadExcel?taskId=' + taskId;
                                            resetUI(); // 下载后重置
                                        }, 500);
                                    } else {
                                        // 更新进度
                                        $('#progressBar').width(progress + '%');
                                        $('#progressText').text(progress + '%');
                                    }
                                }

                            });
                        }, 1000);
                    } else {
                        layer.msg(data.msg, {icon: 2});
                        resetUI();
                    }

                }
            });

            // 重置UI状态：同时隐藏进度条和遮罩层
            function resetUI() {
                $('#exportExcel').prop('disabled', false);
                $('#progressContainer').hide(); // 隐藏进度条
                $('#maskLayer').hide(); // 隐藏遮罩层（恢复操作）
            }
        });
        // 清空按钮点击事件
        $('#userBtnClear').click(function () {
            $('#treeclass').html("选择机构");
            $('#cphm').val('');
            $('#cpys').val('');
            $('#pfbz').val('');
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
                    area: ['800px', '700px'],
                    skin: 'my-skin',
                    content: ['${path}/business/mjvehicle/editPageGL?id=' + data.id],
                    end: function () {
                        renderTable();
                    }
                });
            } else if (obj.event === 'deleteDetail') {
                layer.confirm('确定要删除吗',
                    function (index) {
                        // 删除
                        $.ajax({
                            type: "post",
                            url: '${path }/business/mjvehicle/delete',
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