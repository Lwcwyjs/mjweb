<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js" charset="utf-8"></script>
<html>
<head>
    <%--<%@ include file="/commons/basejs.jsp"%>--%>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <%--    <meta http-equiv="X-UA-Compatible" content="edge"/>--%>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
        <script type="text/javascript" src="${staticPath}/static/easyui/jquery.min.js" charset="utf-8"></script>
        <script type="text/javascript" src="${staticPath}/static/module/swiper/swiper-4.1.0.min.js"
                charset="utf-8"></script>
        <link rel="stylesheet" href="${staticPath }/static/viewer/viewer.css">
        <script src="${staticPath}/static/viewer/viewer.js"></script>
    <title>运输车辆信息编辑</title>
    <style>

        .layui-tree li i {
            color: white;
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
            color: #313131;
        }

        .layui-form-label {
            width: 100px;
        }

    </style>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-body">
            <div class="layui-form toolbar" id="queryFormItem">
                <input id="id" name="id" value="${mjvehicle.id}" type="hidden">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">组织机构</label>
                        <div class="layui-input-inline">
                            <div class="layui-unselect layui-form-select downpanel" style="width:545px" id="downpl">
                                <div class="layui-select-title">
                                    <span class="layui-input layui-unselect" id="organname"
                                          style="width:545px">选择机构名称</span>
                                    <input type="hidden" name="qybh" id="qybh" value="" lay-filter="qybhFilter">
                                    <i class="layui-edge"></i>
                                </div>
                                <dl class="layui-anim layui-anim-upbit">
                                    <dd>
                                        <ul id="selectOrgan"></ul>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">车牌号码</label>
                            <div class="layui-input-inline">
                                <input id="cphm" name="cphm" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjvehicle.cphm}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">车牌颜色</label>
                            <div class="layui-input-inline">
                                <select id="cpys" name="cpys" lay-filter="jclxFilter" lay-verify="required"
                                        lay-search>
                                    <option value="">请选择</option>
                                    <option value="0">蓝牌</option>
                                    <option value="1">黄牌</option>
                                    <option value="2">白牌</option>
                                    <option value="3">黑牌</option>
                                    <option value="4">绿牌</option>
                                    <option value="6">绿黄牌</option>
                                    <option value="5">其他</option>
                                </select>
                            </div>
                        </div>
                    </div>

                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">车架号</label>
                            <div class="layui-input-inline">
                                <input id="clsbdh" name="clsbdh" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjvehicle.clsbdh}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">发动机号</label>
                            <div class="layui-input-inline">
                                <input id="fdjh" name="fdjh" placeholder="" class="layui-input" lay-verify="required"
                                       value="${mjvehicle.fdjh}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">注册登记日期</label>
                            <div class="layui-input-inline">
                                <input type="text" name="ccdjrq" id="ccdjrq" lay-verify="ccdjrq"
                                       placeholder=""
                                       autocomplete="off" class="layui-input" value="">
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">排放标准</label>
                            <div class="layui-input-inline">
                                <select id="pfbz" name="pfbz" lay-filter="jclxFilter" lay-verify="required"
                                        lay-search>
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
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">车辆类型:</label>
                            <div class="layui-input-inline">
                                <select id="cllx" name="cllx" lay-filter="shztFilter" >
                                    <option value="">请选择</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">车辆品牌型号</label>
                            <div class="layui-input-inline">
                                <input id="clppxh" name="clppxh" placeholder="" class="layui-input" lay-verify="required"
                                       value="${mjvehicle.clppxh}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">所有人</label>
                            <div class="layui-input-inline">
                                <input id="syr" name="syr" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjvehicle.syr}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">核定载质量</label>
                            <div class="layui-input-inline">
                                <input id="hdzzl" name="hdzzl" type="number" placeholder="" class="layui-input"
                                       value="${mjvehicle.hdzzl}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">燃油类型</label>
                            <div class="layui-input-inline">
                                <select id="rlzl" name="rlzl" lay-filter="jclxFilter" lay-verify="required"
                                        lay-search>
                                    <option value="">请选择</option>
                                    <option value="A">汽油</option>
                                    <option value="B">柴油</option>
                                    <option value="C">电</option>
                                    <option value="D">混合油</option>
                                    <option value="E">天然气</option>
                                    <option value="Y">无</option>
                                    <option value="Z">其他</option>
                                    <option value="X">未知</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">车辆种类</label>
                            <div class="layui-input-inline">
                                <select id="clzl" name="clzl" lay-filter="jclxFilter" lay-verify="required"
                                        lay-search>
                                    <option value="">请选择</option>
                                    <option value="0">货运</option>
                                    <option value="1">危固废运输</option>
                                    <option value="2">特种作业</option>
                                    <option value="3">民生保障</option>
                                    <option value="4">客运</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">最新年检日期</label>
                            <div class="layui-input-inline">
                                <input type="text" name="zjnjsj" id="zjnjsj" lay-verify="zjnjsj"
                                       placeholder="" lay-verify="required"
                                       autocomplete="off" class="layui-input" value="">
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">手机号码</label>
                            <div class="layui-input-inline">
                                <input id="lxdh" name="lxdh" placeholder="" class="layui-input"
                                       value="${mjvehicle.lxdh}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">发证日期</label>
                            <div class="layui-input-inline">
                                <input type="text" name="fzrq" id="fzrq" lay-verify="fzrq"
                                       placeholder=""
                                       autocomplete="off" class="layui-input" value="">
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">最大总质量</label>
                            <div class="layui-input-inline">
                                <input id="zdzzl" name="zdzzl" placeholder="" type="number" class="layui-input"
                                       value="${mjvehicle.zdzzl}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">地址</label>
                            <div class="layui-input-inline">
                                <input id="address" name="address" placeholder="" class="layui-input" style="width:545px"
                                       value="${mjvehicle.address}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">联网状态(OBD)</label>
                            <div class="layui-input-inline">
                                <select id="lwzt" name="lwzt" lay-filter="jclxFilter" lay-verify="required"
                                        lay-search>
                                    <option value="">请选择</option>
                                    <option value="0">未联网</option>
                                    <option value="1">已联网</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">使用性质</label>
                            <div class="layui-input-inline">
                                <select id="syxz" name="syxz" lay-filter="jclxFilter" lay-verify="required"
                                        lay-search>
                                    <option value="">请选择</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">排放判定依据</label>
                            <div class="layui-input-inline">
                                <input id="pfpdyj" name="pfpdyj" placeholder="" class="layui-input" style="width:545px"
                                       value="${mjvehicle.pfpdyj}"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">行驶证正页</label>
                            <div class="layui-input-inline">
                                <button class="btrecpture" onclick="xszaclick()">正页*</button>
                                <input id="fileimgxsza" type="file" name="file" class="file" accept="image/*" multiple="multiple" value="" style="display: none">
                                <img id="imgxszazp"  style="width: 200px">
                                <input type="hidden" name="xszazp" id="xszazp" value="${mjvehicle.xszazp}" lay-filter="qybhFilter">
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">行驶证副页</label>
                            <div class="layui-input-inline">
                                <button class="btrecpture" onclick="xszbclick()">副页*</button>
                                <input id="fileimgxszb" type="file" name="file" class="file" accept="image/*"  multiple="multiple" value="" style="display: none">
                                <img id="imgxszbzp"  style="width: 200px">
                                <input type="hidden" name="xszbzp" id="xszbzp" value="${mjvehicle.xszbzp}" lay-filter="qybhFilter">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item" >
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">随车清单照片</label>
                            <div class="layui-input-inline">
                                <button class="btrecpture" onclick="scqdclick()">随车清单</button>
                                <input id="fileimgscqd" type="file" name="file" class="file" accept="image/*"  multiple="multiple" value="" style="display: none">
                                <img id="imgscqdzp"  style="width: 200px">
                                <input type="hidden" name="scqdzp" id="scqdzp" value="${mjvehicle.scqdzp}" lay-filter="qybhFilter">
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">车辆照片</label>
                            <div class="layui-input-inline">
                                <button class="btrecpture" onclick="vehicleclick()">车辆照片*</button>
                                <input id="fileimgvehicle" type="file" name="file" class="file" accept="image/*"  multiple="multiple" value="" style="display: none">
                                <img id="imgvehiclezp"  style="width: 200px">
                                <input type="hidden" name="vehiclezp" id="vehiclezp" value="${mjvehicle.vehiclezp}" lay-filter="qybhFilter">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline" style="float: right; margin: 5px 20px;">
                        <button id="userBtnSearch" lay-submit lay-filter="*" class="layui-btn icon-btn"
                                style="margin-right: 30px;"><i
                                class="layui-icon"></i>确认
                        </button>
                        <button type="reset" class="layui-btn icon-btn" style="margin-right: 10px;" id="userBtnClear"><i
                                class="layui-icon"></i>关闭
                        </button>
                    </div>
                </div>

            </div>

        </div>
    </div>
</div>
<script>
    layui.config({
        base: '${staticPath}/static/module/'
    }).use(['form', 'tree', 'laydate'], function () {
        var laydate = layui.laydate;
        var form = layui.form;
        var tree = layui.tree;
        var xszacphm="";
        var xszbcphm="";
        var $ = layui.jquery;
        var imgElementxszazp = document.getElementById('imgxszazp');
        imgElementxszazp.style.display="none";
        var imgElementxszbzp = document.getElementById('imgxszbzp');
        imgElementxszbzp.style.display="none";
        var imgElementscqdzp = document.getElementById('imgscqdzp');
        imgElementscqdzp.style.display="none";
        var imgElementvehiclezp = document.getElementById('imgvehiclezp');
        imgElementvehiclezp.style.display="none";
        laydate.render({
            elem: '#ccdjrq'
        });
        laydate.render({
            elem: '#zjnjsj'
        });
        laydate.render({
            elem: '#fzrq'
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
                    , id: 'organtree'
                    , isJump: true //是否允许点击节点时弹出新窗口跳转
                    , click: function (obj) {
                        var data = obj.data;  //获取当前点击的节点数据
                        $('#qybh').val(data.id);
                        $('#organname').html(data.title);
                        $('#downpl').toggleClass("layui-form-selected");
                    }
                });
            }
        });
        $.ajax({
            type: "post",
            url: '${path}/admin/syscode/combox',
            data: {"oi_name": '车辆类型'},
            dataType: 'json',
            async:false,
            success: function (data) {
                $('#cllx').empty();
                var t;
                var t = "<option value='' selected='selected'>车辆类型</option>";
                for (var i = 0; i < data.length; i++) {
                    if (data[i].oi_value != undefined && data[i].oi_code != undefined) {
                        t += '<option value="' + data[i].oi_code + '">' + data[i].oi_value + '</option>';
                    }
                }
                $('#cllx').append(t);
                form.render('select');
            }
        });
        $.ajax({
            type: "post",
            url: '${path}/admin/syscode/combox',
            data: {"oi_name": '使用性质'},
            dataType: 'json',
            async:false,
            success: function (data) {
                $('#syxz').empty();
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
        $("input[id='fileimgxsza']").change(function (event) {//上传图片
            var file = event.currentTarget.files[0]
            var formFile = new FormData();
            formFile.append("file", file);
            formFile.append("zpzl", "front");
            var loadingIndex = layer.load(1, {
                shade: [0.5, '#000'] // 0.5透明度的黑色背景
            });
            $.ajax({
                url: '${path}/wechat/sendxszzp',
                type: 'POST',
                data: formFile,
                async: true,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    layer.close(loadingIndex);
                    console.log(data);
                    // 隐藏加载提示
                    data = $.parseJSON(data);
                    if (data.code != 1) {
                        layer.msg(data.message, {icon: 2});
                    } else {
                        var content = data.message;
                        var vehicle=data.vehicle;
                        if(xszbcphm!=""){
                            if(vehicle.cphm!=xszbcphm) {
                                layer.msg("行驶证正页与副页不一致", {icon: 2});
                                return;
                            }
                        }
                        else{
                            xszacphm = vehicle.cphm;
                        }
                        imgElementxszazp.style.display="block";
                        $("#imgxszazp").attr("src", content);
                        imgurlxsza = content;
                        $("#xszazp").val(imgurlxsza);
                        console.log(vehicle.cpys);
                        $("#cpys").val(vehicle.cpys);
                        $("#cphm").val(vehicle.cphm);
                        $("#clsbdh").val(vehicle.clsbdh);
                        $("#fdjh").val(vehicle.fdjh);
                        $("#ccdjrq").val(formatDate(vehicle.ccdjrq));
                        $("#cllx").val(vehicle.cllxid);
                        $("#syxz").val(vehicle.syxzid);
                        $("#clppxh").val(vehicle.clppxh);
                        $("#syr").val(vehicle.syr);
                        $("#address").val(vehicle.address);
                        $("#clzl").val(vehicle.clzl);
                        $("#pfbz").val(vehicle.pfbz);
                        $("#pfpdyj").val(vehicle.pfpdyj);
                        $("#fzrq").val(formatDate(vehicle.fzrq));
                        form.render();
                    }
                }
            })

        });
        $("input[id='fileimgxszb']").change(function (event) {//上传图片
            var file = event.currentTarget.files[0]
            var formFile = new FormData();
            formFile.append("file", file);
            formFile.append("zpzl", "back");
            var loadingIndex = layer.load(1, {
                shade: [0.5, '#000'] // 0.5透明度的黑色背景
            });
            $.ajax({
                url: '${path}/wechat/sendxszzp',
                type: 'POST',
                data: formFile,
                async: true,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    layer.close(loadingIndex);
                    console.log(data);
                    data = $.parseJSON(data);
                    if (data.code != 1) {
                        layer.msg(data.message, {icon: 2});
                    } else {
                        var content = data.message;
                        var vehicle=data.vehicle;
                        imgElementxszbzp.style.display="block";
                        if(xszacphm!=""){
                            if(vehicle.cphm!=xszacphm) {
                                layer.msg("行驶证正页与副页不一致", {icon: 2});
                                return;
                            }
                        }
                        else{
                            xszbcphm = vehicle.cphm;
                        }

                        $("#imgxszbzp").attr("src", content);
                        imgurlxszb = content;
                        $("#xszbzp").val(imgurlxszb);

                        $("#hdzzl").html(vehicle.hdzzl);
                        $("#rlzl").val(vehicle.rlzl);
                        $("#zdzzl").val(vehicle.zdzzl);
                        form.render();
                    }
                }
            })

        });
        $("input[id='fileimgscqd']").change(function (event) {//上传图片
            var file = event.currentTarget.files[0]
            var formFile = new FormData();
            var file = event.currentTarget.files[0]
            var formFile = new FormData();
            if ($("#clsbdh").val() == "") {
                layer.msg("请先拍摄行驶证正页", {icon: 2});
                return;
            }
            formFile.append("file", file);
            formFile.append("zpzl", "scqd");
            formFile.append("fdjh",$("#fdjh").val() );
            formFile.append("clsbdh",$("#clsbdh").val() );
            formFile.append("clppxh",$("#clppxh").val() );
            var loadingIndex = layer.load(1, {
                shade: [0.5, '#000'] // 0.5透明度的黑色背景
            });
            $.ajax({
                url: '${path}/wechat/sendscqdzp',
                type: 'POST',
                data: formFile,
                async: true,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    console.log(data);
                    layer.close(loadingIndex);
                    data = $.parseJSON(data);
                    if (data.code != 1) {
                        layer.msg(data.message, {icon: 2});
                    } else {
                        var url = data.url;
                        var pfbzid=data.pfbzid;
                        imgElementscqdzp.style.display="block";
                        $("#imgscqdzp").attr("src", url);
                        imgurlscqd = url;
                        $("#scqdzp").val(imgurlscqd);
                        if(pfbzid!=""){
                            $("#pfbz").val(pfbzid);
                            pfpdyj=data.pfpdyj;
                            pfpdyjzl=data.pfpdyjzl;
                        }
                        form.render();
                    }
                }
            })

        });
        $("input[id='fileimgvehicle']").change(function (event) {//上传图片
            var file = event.currentTarget.files[0]
            var formFile = new FormData();
            formFile.append("file", file);
            formFile.append("zpzl", "vehicle");
            $.ajax({
                url: '${path}/wechat/sendvehiclezp',
                type: 'POST',
                data: formFile,
                async: true,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    data = $.parseJSON(data);
                    if (data.code != 1) {
                        dialog("错误", data.message);
                    } else {
                        var url = data.url;
                        imgElementvehiclezp.style.display="block";
                        $("#imgvehiclezp").attr("src", url);
                        imgurlvehicle = url;
                        $("#vehiclezp").val(imgurlvehicle);
                    }
                }
            })

        });
        $(".downpanel").on("click", ".layui-select-title", function (e) {
            $(".layui-form-select").not($(this).parents(".layui-form-select")).removeClass("layui-form-selected");
            $(this).parents(".downpanel").toggleClass("layui-form-selected");
            layui.stope(e);
        }).on("click", "dl i", function (e) {
            layui.stope(e);
        });
        var xszazp='${mjvehicle.xszazp}';
        if(xszazp!="") {
            imgElementxszazp.style.display = "block";
            $("#imgxszazp").attr("src", xszazp);
        }
        var xszbzp='${mjvehicle.xszbzp}';
        if(xszbzp!="") {
            imgElementxszbzp.style.display = "block";
            $("#imgxszbzp").attr("src", xszbzp);
        }
        var scqdzp='${mjvehicle.scqdzp}';
        if(scqdzp!="") {
            imgElementscqdzp.style.display = "block";
            $("#imgscqdzp").attr("src", scqdzp);
        }
        var vehiclezp='${mjvehicle.vehiclezp}';
        if(vehiclezp!="") {
            imgElementvehiclezp.style.display = "block";
            $("#imgvehiclezp").attr("src", vehiclezp);
        }
        $("#organname").html('${organname}');
        var ccdjrq='${mjvehicle.ccdjrq}';
        var zjnjsj='${mjvehicle.zjnjsj}';
        if(ccdjrq!="") {
            $('#ccdjrq').val(formatDate(ccdjrq));
        }
        if(zjnjsj!="") {
            $('#zjnjsj').val(formatDate(zjnjsj));
        }
        var fzrq='${mjvehicle.fzrq}';
        if(fzrq!="") {
            $('#fzrq').val(formatDate(fzrq));
        }
        $("#qybh").val('${mjvehicle.qybh}');
        var cllx='${mjvehicle.cllx}';
        if(cllx!="") {
            $('#cllx').val('${mjvehicle.cllx}');
        }
        var syxz='${mjvehicle.syxz}';
        if(syxz!="") {
            $('#syxz').val('${mjvehicle.syxz}');
        }
        var cpys='${mjvehicle.cpys}';
        if(cpys!="") {
            $('#cpys').val('${mjvehicle.cpys}');
        }
        var pfbz='${mjvehicle.pfbz}';
        if(pfbz!="") {
            $('#pfbz').val('${mjvehicle.pfbz}');
        }
        $('#lwzt').val('${mjvehicle.lwzt}');
        $('#clzl').val('${mjvehicle.clzl}');
        $('#rlzl').val('${mjvehicle.rlzl}');
        form.render();
        form.on('submit(*)', function (data) {
            if($('#lxdh').val()!=""){
                if (!checkMobile($('#lxdh').val())) {
                    layer.msg("手机号码不正确", {icon: 2});
                    return;
                }
            }
            //data.field 当前容器的全部表单字段，名值对形式：{name: value}
            $.ajax({
                type: "post",
                url: '${path}/business/mjvehicle/${oper}',
                data: data.field,
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        layer.msg('编辑成功', {icon: 1});
                        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                        setTimeout(function () {
                            parent.layer.close(index); //再执行关闭
                        }, 1000);
                    } else {
                        layer.msg(data.msg, {icon: 2});
                    }
                }
            });
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });
        $('#userBtnClear').click(function () {
            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
            parent.layer.close(index); //再执行关闭
        })
    })
    function formatDate(timestamp) {
        // 创建一个Date对象
        var date = new Date(timestamp);

        // 获取年份（四位数）
        var year = date.getFullYear();

        // 获取月份（0-11），并转换为1-12
        var month = (date.getMonth() + 1).toString();
        month = month.length < 2 ? '0' + month : month;

        // 获取日期（1-31）
        var day = date.getDate().toString();
        day = day.length < 2 ? '0' + day : day;

        // 拼接并返回日期字符串
        return year + '-' + month + '-' + day;
    }
    function checkMobile(str) {
        var re = /^1\d{10}$/
        if (re.test(str)) {
            return true;
        } else {
            return false;
        }
    }
    $(function () {
        // 确保#queryFormItem是一个包含图片的容器
        var $container = $('#queryFormItem');

        // 初始化Viewer实例，仅当需要时
        var viewer = new Viewer($container.get(0), {
            url: 'function(element) { return element.getAttribute("data-src"); }' // 假设图片的真实URL存储在data-src属性中
            // 其他配置...
        });

    });
    function xszaclick() {
        $("#fileimgxsza").click();
    }
    function xszbclick() {
        $("#fileimgxszb").click();
    }
    function scqdclick() {
        $("#fileimgscqd").click();
    }
    function vehicleclick() {
        $("#fileimgvehicle").click();
    }
</script>
</body>
</html>