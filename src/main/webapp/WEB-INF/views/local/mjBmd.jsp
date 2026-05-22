<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>白名单管理</title>
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
                            <input name="cphm" id="cphm" class="layui-input"/>
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
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <button id="bmdBtnSearch" class="layui-btn icon-btn"><i
                                    class="layui-icon"></i>查询
                            </button>
                            <button type="reset" class="layui-btn icon-btn" id="bmdBtnClear"><i
                                    class="layui-icon"></i>清空
                            </button>
                            <input type="file" id="excelFile" accept=".xls,.xlsx" style="display: none;">
                            <button type="button" id="importExcel" class="layui-btn layui-btn-warm">导入Excel</button>
                            <button type="button" lay-submit="" class="layui-btn layui-btn-warm" lay-filter="uploadImg"
                                    id="exportExcel">导出excel
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

                <table class="layui-hide" id="bmdTable" lay-filter="vehicleFilter"></table>
            </div>


            <script type="text/html" id="actionHandle" lay-event="detail">
                <shiro:hasPermission name="/local/bmd/edit">
					<span style="height: 24px; background-color: transparent; cursor: pointer;"
                          lay-event="editDetail">
                        <span style="margin-left: 1px;"></span>
                        <img src="${staticPath}/static/assets/images/edit.png"
                             style="margin-bottom: 5px; cursor: pointer;">
                        <span style="margin: 0px; font-size: 16px; cursor: pointer;">编辑</span>
                    </span>
                </shiro:hasPermission>
                <shiro:hasPermission name="/local/bmd/del">
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

        var renderTable = function () {
            var loginname = $('#loginname').val();
            var organ = $('#organ').val();

            table.render({
                elem: '#bmdTable'
                , url: '${path}/local/bmd/dataGrid'
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
                ,where: {
                    loginname: loginname
                    , organ: organ
                }
                , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                , cols: [[
                    {field: 'organ', title: '企业名称', align: 'center', sort: true, display: false},
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
                    },
                    {field: 'czmc', title: '车主名称', align: 'center', sort: true},
                    {field: 'lxdh', title: '联系电话', align: 'center', sort: true},
                    {field: 'sxsj', title: '生效时间', align: 'center', sort: true},
                    {field: 'zzsj', title: '终止时间', align: 'center', sort: true},
                    {field: 'cjsj', title: '更新时间', align: 'center', sort: true},
                    {field: 'crkbh', title: '出入口编号', align: 'center', sort: true},
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
            url: '${path}/local/organization/orgtreelevelForOrgan?organtype=',
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
                area: ['800px', '450px'],
                content: ['${path}/local/bmd/addPage'],
                end: function () {
                    renderTable();
                }
            });
        })
        // 点击"导入Excel"按钮时，触发文件选择框
        $('#importExcel').click(function () {
            $('#excelFile').click(); // 唤起文件选择窗口
        });

// 文件选择框选择文件后触发（补全逻辑）
        $('#excelFile').change(function () {
            var file = this.files[0]; // 获取选择的文件
            if (!file) {
                return; // 未选择文件则退出
            }

            // 1. 校验文件格式和大小
            var fileName = file.name;
            var fileSize = file.size / 1024 / 1024; // 转换为MB
            if (!fileName.endsWith('.xls') && !fileName.endsWith('.xlsx')) {
                layer.msg('请上传Excel文件（.xls或.xlsx格式）', {icon: 2});
                $(this).val(''); // 清空选择，避免重复触发
                return;
            }
            if (fileSize > 10) { // 限制文件大小不超过10MB
                layer.msg('文件大小不能超过10MB', {icon: 2});
                $(this).val('');
                return;
            }

            // 2. 显示进度条和遮罩层，禁用按钮
            $('#progressContainer').show();
            $('#maskLayer').show();
            $('#progressBar').width('0%');
            $('#progressText').text('0%');
            $('#importExcel').prop('disabled', true);

            // 3. 构造FormData，准备上传文件
            var formData = new FormData();
            formData.append('file', file); // 键名需与后端接口的@RequestParam("file")一致

            // 4. 调用后端导入接口，启动导入任务
            $.ajax({
                type: 'post',
                url: '${path}/local/bmd/startImport', // 后端导入接口地址
                data: formData,
                processData: false, // 禁止jQuery序列化FormData（必须）
                contentType: false, // 让浏览器自动设置Content-Type（包含boundary）
                dataType: 'json',
                success: function (data) {
                    if (data.code == 1) {
                        var taskId = data.taskId; // 获取后端返回的任务ID
                        // 5. 轮询查询导入进度
                        var pollInterval = setInterval(function () {
                            checkImportProgress(taskId, pollInterval);
                        }, 1000); // 每秒查询一次
                    } else {
                        // 接口调用失败（如文件上传失败）
                        layer.msg(data.msg || '导入任务启动失败', {icon: 2});
                        resetUI();
                    }
                },
                error: function () {
                    layer.msg('网络错误，导入失败', {icon: 2});
                    resetUI();
                }
            });

            // 重置UI状态（隐藏进度条、启用按钮等）
            function resetUI() {
                $('#progressContainer').hide();
                $('#maskLayer').hide();
                $('#importExcel').prop('disabled', false);
                $('#excelFile').val(''); // 清空文件选择，避免重复上传
            }

            // 检查导入进度的函数
            function checkImportProgress(taskId, interval) {
                $.ajax({
                    type: 'post',
                    url: '${path}/local/bmd/importProgress', // 后端进度查询接口
                    data: {taskId: taskId},
                    dataType: 'json',
                    success: function (res) {
                        var progress = res.progress;
                        if (progress === -1) {
                            // 任务不存在
                            clearInterval(interval);
                            layer.msg('导入任务不存在', {icon: 2});
                            resetUI();
                        } else if (progress === -2) {
                            // 导入失败
                            clearInterval(interval);
                            layer.msg('导入失败：' + (res.errorMsg || '未知错误'), {icon: 2});
                            resetUI();
                        } else if (progress === -3) {
                            // 数据校验失败（有错误报表）
                            clearInterval(interval);
                            layer.confirm(res.errorMsg + '，是否下载错误报表？', {
                                btn: ['下载', '取消']
                            }, function () {
                                // 下载错误报表
                                window.location.href = '${path}/local/bmd/downloadErrorExcel?taskId=' + taskId;
                            });
                            resetUI();
                        } else if (progress >= 100) {
                            // 导入完成
                            clearInterval(interval);
                            $('#progressBar').width('100%');
                            $('#progressText').text('100%');
                            setTimeout(function () {
                                layer.msg(res.msg || '导入成功', {icon: 1});
                                resetUI();
                                // 可选：刷新页面数据
                                // location.reload();
                            }, 500);
                        } else {
                            // 更新进度条
                            $('#progressBar').width(progress + '%');
                            $('#progressText').text(progress + '%');
                        }
                    },
                    error: function () {
                        clearInterval(interval);
                        layer.msg('进度查询失败', {icon: 2});
                        resetUI();
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
        $('#exportExcel').click(function () {
            // 1. 参数验证（保持不变）
            var cphm = $('#cphm').val();
            var organ = $('#organ').val();
            var cpys = $('#cpys').val();
            // 2. 显示进度条和遮罩层，禁用导出按钮
            $('#progressContainer').show(); // 显示进度条
            $('#maskLayer').show(); // 显示遮罩层（阻止底层操作）
            $('#progressBar').width('0%');
            $('#progressText').text('0%');
            $(this).prop('disabled', true);

            // 3. 发起导出请求（保持不变）
            var url = '${path}/local/bmd/startExport';
            var params = {
                cphm: $('#cphm').val(),
                cpys: $('#cpys').val(),
                organ: $('#organ').val()
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
                                url: '${path}/local/bmd/exportProgress',
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
                                            window.location.href = '${path}/business/dztz/downloadExcel?taskId=' + taskId;
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

        // 搜索按钮点击事件
        $('#bmdBtnSearch').click(function () {
            var cphm = $('#cphm').val();
            var organ = $('#organ').val();
            var cpys = $('#cpys').val();
            table.reload('bmdTable', {
                page: {
                    curr: 1//重新从第一页开始
                }
                ,where: {
                    cphm: cphm,
                    cpys:cpys,
                    organ: organ
                }
            });
        });

        // 清空按钮点击事件
        $('#bmdBtnClear').click(function () {
            $('#treeclass').html("选择机构");
            $('#cphm').val('');
            $('#cpys').val('');
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
                    area: ['800px', '450px'],
                    skin: 'my-skin',
                    content: ['${path}/local/bmd/editPage?id=' + data.id],
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
                            url: '${path }/local/bmd/delete',
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