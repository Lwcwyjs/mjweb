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
    <link rel="stylesheet" href="${staticPath }/static/viewer/viewer.min.css">
    <script src="${staticPath}/static/viewer/viewer-jquery.min.js"></script>
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

        .btrecpture {
            margin-left: 20px;
        }

    </style>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-body">
            <div class="layui-form toolbar" id="queryFormItem">
                <input id="id" name="id" value="${mjInsideVehicle.id}" type="hidden">
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
                                       value="${mjInsideVehicle.cphm}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">环保登记号码</label>
                            <div class="layui-input-inline">
                                <input id="hbdjhm" name="hbdjhm" placeholder="" class="layui-input"
                                       value="${mjInsideVehicle.hbdjhm}"/>
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
                                       value="${mjInsideVehicle.clsbdh}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">发动机号</label>
                            <div class="layui-input-inline">
                                <input id="fdjh" name="fdjh" placeholder="" class="layui-input"
                                       value="${mjInsideVehicle.fdjh}"/>
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
                            <label class="layui-form-label">生产日期</label>
                            <div class="layui-input-inline">
                                <input type="text" name="scrq" id="scrq" lay-verify="scrq"
                                       placeholder=""
                                       autocomplete="off" class="layui-input" value="">
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">车辆品牌型号</label>
                            <div class="layui-input-inline">
                                <input id="clppxh" name="clppxh" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjInsideVehicle.clppxh}"/>
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
                                       value="${mjInsideVehicle.syr}"/>
                            </div>
                        </div>
                    </div>
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
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">行驶证正页</label>
                            <div class="layui-input-inline">
                                <button class="btrecpture" onclick="xszclick()">正页*</button>
                                <input id="fileimgxsz" type="file" name="file" class="file" accept="image/*" multiple="multiple" value="" style="display: none">
                                <img id="imgxszzp"  style="width: 200px">
                                <input type="hidden" name="xszzp" id="xszzp" value="${mjInsideVehicle.xszzp}" lay-filter="qybhFilter">
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">随车清单照片</label>
                            <div class="layui-input-inline">
                                <button class="btrecpture" onclick="scqdclick()">随车清单</button>
                                <input id="fileimgscqd" type="file" name="file" class="file" accept="image/*"  multiple="multiple" value="" style="display: none">
                                <img id="imgscqdzp"  style="width: 200px">
                                <input type="hidden" name="scqdzp" id="scqdzp" value="${mjInsideVehicle.scqdzp}" lay-filter="qybhFilter">
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
        var $ = layui.jquery;
        var imgElementxszzp = document.getElementById('imgxszzp');
        imgElementxszzp.style.display="none";
        var imgElementscqdzp = document.getElementById('imgscqdzp');
        imgElementscqdzp.style.display="none";
        laydate.render({
            elem: '#ccdjrq'
        });
        laydate.render({
            elem: '#scrq'
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
        $("input[id='fileimgxsz']").change(function (event) {//上传图片
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
                        imgElementxszzp.style.display="block";
                        $("#imgxszzp").attr("src", content);
                        imgurlxsza = content;
                        $("#xszzp").val(imgurlxsza);
                        $("#cphm").val(vehicle.cphm);
                        $("#clsbdh").val(vehicle.clsbdh);
                        $("#fdjh").val(vehicle.fdjh);
                        $("#ccdjrq").val(formatDate(vehicle.ccdjrq));
                        $("#clppxh").val(vehicle.clppxh);
                        $("#syr").val(vehicle.syr);
                        $("#pfbz").val(vehicle.pfbz);
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
                        }
                        form.render();
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
        var xszzp='${mjInsideVehicle.xszzp}';
        if(xszzp!="") {
            imgElementxszzp.style.display = "block";
            $("#imgxszzp").attr("src", xszzp);
        }
        var scqdzp='${mjInsideVehicle.scqdzp}';
        if(scqdzp!="") {
            imgElementscqdzp.style.display = "block";
            $("#imgscqdzp").attr("src", scqdzp);
        }
        $("#organname").html('${organname}');
        var ccdjrq = '${mjInsideVehicle.ccdjrq}';
        if (ccdjrq != "") {
            $('#ccdjrq').val(formatDate(ccdjrq));
        }
        var scrq = '${mjInsideVehicle.scrq}';
        if (scrq != "") {
            $('#scrq').val(formatDate(scrq));
        }
        $('#qybh').val('${mjInsideVehicle.qybh}');
        $('#pfbz').val('${mjInsideVehicle.pfbz}');
        $('#rlzl').val('${mjInsideVehicle.rlzl}');
        form.render();
        form.on('submit(*)', function (data) {
            //data.field 当前容器的全部表单字段，名值对形式：{name: value}
            if ($('#cphm').val() == "" && $('#hbdjhm').val() == "") {
                layer.msg("车牌号和环保登记号码有一个必填", {icon: 2});
                return;
            }
            $.ajax({
                type: "post",
                url: '${path}/business/mjinsidevehicle/${oper}',
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

    function showpic(id) {
        $("#PicList").viewer({imgsrc: "data-original"});
    }
    function scqdclick() {
            $("#fileimgscqd").click();
    }
    function xszclick() {
        $("#fileimgxsz").click();
    }
</script>
</body>
</html>