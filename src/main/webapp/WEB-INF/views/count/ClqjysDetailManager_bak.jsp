<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>

<html>
<head>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <script type="text/javascript" src="${staticPath}/static/echarts/js/echarts.js" charset="utf-8"></script>
    <link href="${staticPath}/static/layui/formSelects/formSelects-v4.css" rel="stylesheet" />
    <%--    <script src="${staticPath}/static/layui/formSelects/formSelects-v4.js"></script>--%>
    <title>清洁运输细分统计</title>
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
        td {
            width: 100px;
            height: 20px;
            text-align: center;
            table-layout: fixed;
            border-width: 1px;
            border-color: black;
        }
        table td {
            border: 1px solid black; /* 设置1像素的黑色实线边框 */
            padding: 8px; /* 可选：设置内边距，使内容不会紧贴着边框 */
        }
        th {
            height: 50px;
        }

        table {
            position: relative;
            border-width:1px;
            border-color: black;
            width:50%;
            align:center;
            cellspacing:0;
            cellpadding:0;
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
                                <i class="layui-edge"></i>
                            </div>
                            <dl class="layui-anim layui-anim-upbit" id="divselect">
                                <dd>
                                    <ul id="selectOrgan"></ul>
                                </dd>
                            </dl>
                        </div>
                    </div>
                    <label class="layui-form-label">进出类型</label>
                    <div class="layui-input-inline">
                        <select id="jclx" name="jclx" lay-filter="shztFilter">
                            <option value="">请选择</option>
                            <option value="1">进</option>
                            <option value="2">出</option>
                        </select>
                    </div>
                    <label class="layui-form-label">车牌颜色</label>
                    <div class="layui-input-inline">
                        <select id="cpys" name="cpys" lay-filter="shztFilter"  xm-select-height="36px" xm-select="select2">
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
<%--                    <div class="layui-input-inline">--%>
<%--                        <button type="button" lay-submit="" class="layui-btn layui-btn-warm" lay-filter="uploadImg" id="exportExcel">导出excel</button>--%>
<%--                    </div>--%>
                </div>

            </form>

        </div>
    </div>
    <div class="layui-col-xs6 layui-col-sm12 layui-col-md12">
        <div id="cltxptable" style="display: flex;justify-content: center;"></div>
    </div>
</div>
<!-- 表格状态列 -->
<script>
    layui.config({
        base: '${staticPath}/static/layui/'
    }).extend({
        formSelects: 'formSelects-v4'
    });
    layui.use(['element','form', 'table', 'tree', 'laydate', 'formSelects'], function () {
        var $ = layui.jquery;
        var table = layui.table;
        var form = layui.form;
        var tree = layui.tree;
        var laydate = layui.laydate;
        var formSelects = layui.formSelects;
        form.render();
        //日期
        laydate.render({
            elem: '#kssj'
        });

        laydate.render({
            elem: '#jssj'
        });
        $('#kssj').val(new Date().Format('yyyy-MM-dd'));
        $('#jssj').val(new Date().Format('yyyy-MM-dd'));
        var params = {qybh: $('#organ').val(), cpys: $('#cpys').val(), jclx: $('#jclx').val(), kssj: $('#kssj').val(), jssj: $('#jssj').val()};
        // 初始化
        getChartData(params);
        // formSelects.on('select2', function(id, vals, val, isAdd, isDisabled){
        //     console.log(vals);
        //     console.log(val);
        // }, true);

        form.on('submit(searchSubmit)', function () {
            var getName = JSON.stringify(layui.formSelects.value('select2', 'id'));//取值name数组
            console.log(getName);

            var params = {qybh: $('#organ').val(), jclx: $('#jclx').val(),cpys: getName, kssj: $('#kssj').val(), jssj: $('#jssj').val()};
            // 初始化
            getChartData(params);
            return false;
        });
        $('#exportExcel').click(function () {
            var jclx = $('#jclx').val();
            var organ = $('#organ').val();
            var kssj = $('#kssj').val();
            var jssj = $('#jssj').val();
            var cpys = $('#cpys').val();
            window.location.href='${path}/count/clqjysDetail/getExcel?jclx='+jclx+'&cpys='+cpys+'&organ='+organ+'&kssj='+kssj+'&jssj='+jssj;
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
    function getChartData(params) {
        $.ajax({
            type: "post",
            url: '${path}/count/clqjysDetail/clqjyswsj',
            data: params,
            dataType: 'json',
            success: function (cltxpfList) {
                if(cltxpfList!=""){
                    var shtml="";
                    $("#cltxptable").html("");
                    shtml="<table>"
                    shtml = shtml + '<tr><td>货物种类</td><td>运输方式</td><td>运输量（吨）</td><td>合计</td><td>占比%</td></tr>';
                    var lastyshwmc="";
                    for (var i = 0; i < cltxpfList.length; i++) {
                        var currentyshwmc=cltxpfList[i].yshwmc;
                        var isfirst=0;
                        var colspan=0;
                        if (currentyshwmc!=lastyshwmc){
                            isfirst=1;
                            lastyshwmc=currentyshwmc;
                        }
                        for (var m=0;m<cltxpfList.length; m++) {
                            if(cltxpfList[m].yshwmc==currentyshwmc){
                                colspan=colspan+1;
                            }
                        }
                        if(isfirst==1) {
                            if(currentyshwmc=="新能源车合计（吨）"){
                                shtml = shtml + '<tr><td colspan="2">' + cltxpfList[i].yshwmc + '</td><td colspan="3">' + cltxpfList[i].yslhj + '</td></tr>';
                            }
                            else if(currentyshwmc=="国六车合计（吨）"){
                                shtml = shtml + '<tr><td colspan="2">' + cltxpfList[i].yshwmc + '</td><td colspan="3">' + cltxpfList[i].yslhj + '</td></tr>';
                            }
                            else if(currentyshwmc=="国五车合计（吨）"){
                                shtml = shtml + '<tr><td colspan="2">' + cltxpfList[i].yshwmc + '</td><td colspan="3">' + cltxpfList[i].yslhj + '</td></tr>';
                            }
                            else if(currentyshwmc=="货物运输量（吨）"){
                                shtml = shtml + '<tr><td colspan="2">' + cltxpfList[i].yshwmc + '</td><td colspan="3">' + cltxpfList[i].yslhj + '</td></tr>';
                            }
                            else if(currentyshwmc=="新能源车运输比例（%）"){
                                shtml = shtml + '<tr><td colspan="2">' + cltxpfList[i].yshwmc + '</td><td colspan="3">' + cltxpfList[i].yslzb + '</td></tr>';
                            }
                            else {
                                shtml = shtml + '<tr><td rowspan="' + colspan + '">' + cltxpfList[i].yshwmc + '</td><td>' + cltxpfList[i].pfbz + '</td><td>' + cltxpfList[i].ysl + '</td><td rowspan="' + colspan + '">' + cltxpfList[i].yslhj + '</td><td>' + cltxpfList[i].yslzb + '</td></tr>';
                            }
                        }
                        else{
                            shtml = shtml + '<tr><td>' + cltxpfList[i].pfbz + '</td><td>' + cltxpfList[i].ysl + '</td><td>' + cltxpfList[i].yslzb + '</td></tr>';
                        }
                    }
                    shtml=shtml+"</table>";
                    console.log(shtml);
                    $("#cltxptable").html(shtml);
                }
            }
        });
    }

    function cleanFun() {
        $('#jclx').combotree("clear");
        $('#kssj').val('');
        $('#jssj').val('');
        $('#organ').combotree("clear");
    }

    function renderForm() {
        layui.use('form', function () {
            var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功
            form.render();
        });
    }
</script>

</body>
</html>