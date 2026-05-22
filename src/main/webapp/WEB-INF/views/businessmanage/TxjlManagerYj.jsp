<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>

<html>
<head>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>通行记录数据查询</title>
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
                    <label class="layui-form-label">企业名称</label>
                    <div class="layui-input-inline">
                        <div class="layui-unselect layui-form-select downpanel" id="downpl">
                            <div class="layui-select-title">
                                <span class="layui-input layui-unselect" id="treeclass">请选择企业</span>
                                <input type="hidden" name="organ" id="organ" value="">
                                <input type="hidden" id="isExporting" name="isExporting" value=""/>
                                <i class="layui-edge"></i>
                            </div>
                            <dl class="layui-anim layui-anim-upbit" id="divselect">
                                <dd>
                                    <ul id="selectOrgan"></ul>
                                </dd>
                            </dl>
                        </div>
                    </div>
                    <label class="layui-form-label">道闸编号</label>
                    <div class="layui-input-inline">
                        <select id="dzbh" name="dzbh" lay-filter="dzbhFilter">
                        </select>
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


                </div>
                <div class="layui-form-item" lay-filter="searchForm">
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
                    <label class="layui-form-label">时间</label>
                    <div class="layui-input-inline">
                        <input name="kssj" id="kssj" class="layui-input"/>
                    </div>
                    <div class="layui-input-inline">
                        <input name="jssj" id="jssj" class="layui-input"/>
                    </div>

                    <div class="layui-input-inline">
                        <button class="layui-btn" id="serach" lay-submit="" lay-filter="searchSubmit">查询</button>
                        <button type="reset" class="layui-btn">重置</button>
                    </div>
                    <div class="layui-input-inline">
                        <button type="button" lay-submit="" class="layui-btn layui-btn-warm" lay-filter="uploadImg"
                                id="exportExcel">导出excel
                        </button>
                    </div>
                </div>

            </form>

        </div>
    </div>
    <div class="layui-card">
        <div class="layui-card-body">
            <script type="text/html" id="actionHandle">
                <span style="height: 24px; background-color: transparent; color: white; cursor: pointer;"
                      lay-event="detail">
                        <span style="margin: 10px; cursor: pointer;color:#27d9ff">详细</span>
                </span>
                <shiro:hasPermission name="/business/txjlYj/yshw">
					<span style="height: 24px; background-color: transparent; cursor: pointer;"
                          lay-event="edityshw">
                        <span style="margin-left: 1px;"></span>
                        <img src="${staticPath}/static/assets/images/edit.png"
                             style="margin-bottom: 5px; cursor: pointer;">
                        <span style="margin: 0px; font-size: 16px; cursor: pointer;">登记运输货物</span>
                    </span>
                </shiro:hasPermission>
            </script>

            <table class="layui-hide" id="vehicleTable" lay-filter="view"></table>
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
                , url: '${path}/business/txjlYj/DataGrid?nowTime=' + new Date().getTime()
                , method: 'GET'
                , request: {
                    pageName: 'page' //页码的参数名称，默认：page
                    , limitName: 'rows' //每页数据量的参数名，默认：limit
                }
                , where: {
                    cphm: $('#cphm').val(),
                    cpys: $('#cpys').val(),
                    organ: $('#organ').val(),
                    dzbh: $('#dzbh').val(),
                    pfbz: $('#pfbz').val(),
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
                    {field: 'dzbh', title: '道闸编号', align: 'center', sort: true},
                    {
                        field: 'bgzt',
                        title: '摆杆状态',
                        align: 'center',
                        sort: true,
                        templet: function (d, row, index) {
                            var value = d.bgzt;
                            switch (value) {
                                case "0":
                                    return "未摆杆";
                                    break;
                                case "1":
                                    return "手动抬杆";
                                    break;
                                case "2":
                                    return "自动摆杆";
                                    break;
                                default:
                                    return value;
                                    break;
                            }
                        }
                    },
                    {field: 'gkjg', title: '管控结果', align: 'center', sort: true},
                    {
                        field: 'jclx',
                        title: '进出类型',
                        align: 'center',
                        sort: true,
                        templet: function (d, row, index) {
                            var value = d.jclx;
                            switch (value) {
                                case "1":
                                    return "进";
                                    break;
                                case "2":
                                    return "出";
                                    break;
                                default:
                                    return value;
                                    break;
                            }
                        }
                    },
                    {field: 'yshwmc', title: '运输货物名称', align: 'center', sort: true},
                    {field: 'tgkssj', title: '通过时间', align: 'center', sort: true},
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
                    },
                    {field: 'opreat', title: '操作', align: 'center', templet: '#actionHandle'}
                ]]
                , page: true
                , limit: 15
                , limits: [5, 10, 15, 20, 25, 30]
                , done: function (res, curr, count) {
                }
            });
        };
        renderTable();
        table.on('tool(view)', function (obj) {
            //获取当前行数据
            var data = obj.data;
            if (obj.event === 'detail') {
                parent.layer.closeAll();
                parent.layer.open({
                    type: 2,
                    title: '通行详情',
                    shadeClose: true,
                    shade: false,
                    maxmin: true, //开启最大化最小化按钮
                    area: ['98%', '98%'],
                    content: ['${path}/business/txjlYj/showPage?id=' + data.id + '&type=0']
                });
            }
            if (obj.event === 'edityshw') {
                parent.layer.closeAll();
                parent.layer.open({
                    type: 2,
                    title: '编辑',
                    shadeClose: true,
                    shade: false,
                    maxmin: false, //开启最大化最小化按钮
                    // btn: ['确认', '关闭'],
                    area: ['500px', '320px'],
                    skin: 'my-skin',
                    content: ['${path}/business/txjlYj/editPage?id=' + data.id],
                    end: function () {
                        renderTable();
                    }
                });
            }
        });

        form.on('submit(searchSubmit)', function () {
            table.reload('vehicleTable', {
                url: '${path}/business/txjlYj/DataGrid?nowTime=' + new Date().getTime(),
                page: {
                    curr: 1
                },
                where: {
                    cphm: $('#cphm').val(),
                    cpys: $('#cpys').val(),
                    organ: $('#organ').val(),
                    dzbh: $('#dzbh').val(),
                    pfbz: $('#pfbz').val(),
                    kssj: $('#kssj').val(),
                    jssj: $('#jssj').val()
                }
            });
            return false;
        });
        $('#exportExcel').click(function () {
            // 1. 参数验证（保持不变）
            var cphm = $('#cphm').val();
            var cpys = $('#cpys').val();
            var organ = $('#organ').val();
            var pfbz = $('#pfbz').val();
            var kssj = $('#kssj').val();
            var jssj = $('#jssj').val();
            var dzbh=$('#dzbh').val();
            var jclx = $('#jclx').val() || ''; // 处理未定义的情况

            if (!kssj || !jssj) {
                layer.msg('时间不能为空', {icon: 2});
                return;
            }
            var days = daysBetweenDates(kssj, jssj);
            if (days > 31) {
                layer.msg('请选择31天内数据', {icon: 2});
                return;
            }

            // 2. 显示进度条和遮罩层，禁用导出按钮
            $('#progressContainer').show(); // 显示进度条
            $('#maskLayer').show(); // 显示遮罩层（阻止底层操作）
            $('#progressBar').width('0%');
            $('#progressText').text('0%');
            $(this).prop('disabled', true);

            // 3. 发起导出请求（保持不变）
            var url = '${path}/business/txjlYj/startExport';
            var params = {
                cphm: cphm,
                cpys: cpys,
                organ: organ,
                pfbz: pfbz,
                kssj: kssj,
                jssj: jssj,
                jclx: jclx,
                dzbh: dzbh
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
                                url: '${path}/business/txjlYj/exportProgress',
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
                                            window.location.href = '${path}/business/txjlYj/downloadExcel?taskId=' + taskId;
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
        laydate.render({
            elem: '#kssj'
        });
        laydate.render({
            elem: '#jssj'
        });
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
                        $.ajax({
                            type: "post",
                            url: '${path}/admin/syscode/getdzbh',
                            data: {"organ": $('#organ').val()},
                            dataType: 'json',
                            success: function (data) {
                                $('#dzbh').empty();
                                var t;
                                var t = "<option value='' selected='selected'></option>";
                                for (var i = 0; i < data.length; i++) {
                                    if (data[i].dzbh != undefined) {
                                        t += '<option value="' + data[i].dzbh + '">' + data[i].dzbh + '</option>';
                                    }
                                }
                                $('#dzbh').append(t);
                                form.render('select');
                            }
                        });
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
    });

    function cleanFun() {
        $('#cphm').val('');
        $('#cpys').val('');
        $('#kssj').val('');
        $('#jssj').val('');
        $('#pfbz').combobox('clear');
        $('#dzbh').combobox('clear');
        $('#organ').combotree("clear");
    }

    function renderForm() {
        layui.use('form', function () {
            var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
            form.render();
        });
    }

    function daysBetweenDates(dateString1, dateString2) {
        // 创建两个 Date 对象
        const date1 = new Date(dateString1);
        const date2 = new Date(dateString2);

        // 检查日期字符串是否有效
        if (isNaN(date1.getTime()) || isNaN(date2.getTime())) {
            throw new Error('Invalid date string');
        }

        // 计算时间差（毫秒）
        const diffInMilliseconds = Math.abs(date1 - date2);

        // 将时间差转换为天数
        const diffInDays = Math.ceil(diffInMilliseconds / (1000 * 60 * 60 * 24));

        return diffInDays;
    }
</script>

</body>
</html>