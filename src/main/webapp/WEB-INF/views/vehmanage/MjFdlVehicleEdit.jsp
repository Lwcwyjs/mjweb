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
    <title>运输非道路信息编辑</title>
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
                <input id="id" name="id" value="${mjFdlVehicle.id}" type="hidden">
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
                            <label class="layui-form-label">环保登记号码</label>
                            <div class="layui-input-inline">
                                <input id="hbdjhm" name="hbdjhm" placeholder="" class="layui-input" lay-verify="required"
                                       value="${mjFdlVehicle.hbdjhm}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">产品识别码</label>
                            <div class="layui-input-inline">
                                <input id="cpsbm" name="cpsbm" placeholder="" class="layui-input"
                                       value="${mjFdlVehicle.cpsbm}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">机械环保代码</label>
                            <div class="layui-input-inline">
                                <input id="jxhbdm" name="jxhbdm" placeholder="" class="layui-input"
                                       value="${mjFdlVehicle.jxhbdm}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">发动机编号</label>
                            <div class="layui-input-inline">
                                <input id="fdjbh" name="fdjbh" placeholder="" class="layui-input" lay-verify="required"
                                       value="${mjFdlVehicle.fdjbh}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">机械生产日期</label>
                            <div class="layui-input-inline">
                                <input type="text" name="scrq" id="scrq" lay-verify="scrq" lay-verify="required"
                                       placeholder=""
                                       autocomplete="off" class="layui-input" value="">
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">机械种类</label>
                            <div class="layui-input-inline">
                                <select id="jxzl" name="jxzl" lay-filter="jclxFilter" lay-verify="required"
                                        lay-search>
                                    <option value="">请选择</option>
                                    <option value="1">装载机</option>
                                    <option value="2">挖掘机</option>
                                    <option value="3">推土机</option>
                                    <option value="4">叉车</option>
                                    <option value="5">非公路用卡车</option>
                                    <option value="6">其他</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">发动机生产厂</label>
                            <div class="layui-input-inline">
                                <input id="fdjscc" name="fdjscc" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjFdlVehicle.fdjscc}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">发动机型号</label>
                            <div class="layui-input-inline">
                                <input id="fdjxh" name="fdjxh" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjFdlVehicle.fdjxh}"/>
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
                                       value="${mjFdlVehicle.syr}"/>
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
                    <div class="layui-form-item">
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
                        <div class="layui-inline">
                            <div class="layui-inline">
                                <label class="layui-form-label">机械型号</label>
                                <div class="layui-input-inline">
                                    <input id="jxxh" name="jxxh" placeholder="" class="layui-input" lay-verify="required"
                                           value="${mjFdlVehicle.jxxh}"/>
                                </div>
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
                                       value="${mjFdlVehicle.cphm}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item" >
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">发动机铭牌照片</label>

                            <div class="layui-input-inline">
                                <button class="layui-btn icon-btn" onclick="fdjmpclick()">照片</button>
                                <input id="fileimgfdjmp" type="file" name="file" class="file" accept="image/*"
                                       multiple="multiple"
                                       value="" style="display: none">
                                <img id="fdjmpzp"  style="width: 200px">
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">整车铭牌照片</label>

                            <div class="layui-input-inline">
                                <button class="layui-btn icon-btn" onclick="zcmpclick()">照片</button>
                                <input id="fileimgzcmp" type="file" name="file" class="file" accept="image/*"
                                       multiple="multiple"
                                       value="" style="display: none">
                                <img id="zcmpzp"  style="width: 200px">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item" >
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">环保标签照片</label>

                            <div class="layui-input-inline">
                                <button class="layui-btn icon-btn" onclick="hbbqclick()">照片</button>
                                <input id="fileimghbbq" type="file" name="file" class="file" accept="image/*"
                                       multiple="multiple"
                                       value="" style="display: none">
                                <img id="hbbqzp"  style="width: 200px">
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
        var imgfdjmpurl="";
        var imgzcmpurl="";
        var imghbbqurl="";
        var imgElementfdjmpzp = document.getElementById('fdjmpzp');
        imgElementfdjmpzp.style.display = "none";
        var imgElementzcmpzp = document.getElementById('zcmpzp');
        imgElementzcmpzp.style.display = "none";
        var imgElementhbbqzp = document.getElementById('hbbqzp');
        imgElementhbbqzp.style.display = "none";
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
        $("input[id='fileimgfdjmp']").change(function (event) {//上传图片
            var file = event.currentTarget.files[0];
            var formFile = new FormData();
            formFile.append("file", file);
            formFile.append("zpzl", "front");
            $.ajax({
                url: '${path}/business/mjfdlvehicle/sendfdjmpzp',
                type: 'POST',
                data: formFile,
                async: true,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    data = $.parseJSON(data);
                    if (data.code != 1) {
                        layer.msg(data.message, {icon: 2});
                    } else {
                        var content = data.message;
                        imgElementfdjmpzp.style.display="block";
                        $("#fdjmpzp").attr("src", content);
                        imgfdjmpurl = content;
                    }
                }
            })

        });
        $("input[id='fileimgzcmp']").change(function (event) {//上传图片
            var file = event.currentTarget.files[0];
            var formFile = new FormData();
            formFile.append("file", file);
            $.ajax({
                url: '${path}/business/mjfdlvehicle/sendzcmpzp',
                type: 'POST',
                data: formFile,
                async: true,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    data = $.parseJSON(data);
                    if (data.code != 1) {
                        layer.msg(data.message, {icon: 2});
                    } else {
                        var content = data.message;
                        imgElementzcmpzp.style.display="block";
                        $("#zcmpzp").attr("src", content);
                        imgzcmpurl = content;
                    }
                }
            })

        });
        $("input[id='fileimghbbq']").change(function (event) {//上传图片
            var file = event.currentTarget.files[0];
            var formFile = new FormData();
            formFile.append("file", file);
            formFile.append("zpzl", "front");
            $.ajax({
                url: '${path}/business/mjfdlvehicle/sendhbbqzp',
                type: 'POST',
                data: formFile,
                async: true,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    data = $.parseJSON(data);
                    if (data.code != 1) {
                        layer.msg(data.message, {icon: 2});
                    } else {
                        var content = data.message;
                        imgElementhbbqzp.style.display = "block";
                        $("#hbbqzp").attr("src", content);
                        imghbbqurl = content;
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
        var zcmpzp = '${mjFdlVehicle.zcmpzp}';
        if (zcmpzp != "") {
            imgElementzcmpzp.style.display = "block";
            $("#zcmpzp").attr("src", zcmpzp);
            imgzcmpurl = zcmpzp;
        }
        var fdjmpzp = '${mjFdlVehicle.fdjmpzp}';
        if (fdjmpzp != "") {
            imgElementfdjmpzp.style.display = "block";
            $("#fdjmpzp").attr("src", fdjmpzp);
            imgfdjmpurl = fdjmpzp;
        }
        var hbbqzp = '${mjFdlVehicle.hbbqzp}';
        if (hbbqzp != "") {
            imgElementhbbqzp.style.display = "block";
            $("#hbbqzp").attr("src", hbbqzp);
            imghbbqurl = hbbqzp;
        }
        $("#organname").html('${organname}');
        var scrq = '${mjFdlVehicle.scrq}';
        if (scrq != "") {
            $('#scrq').val(formatDate(scrq));
        }
        $('#jxzl').val('${mjFdlVehicle.jxzl}');
        $('#qybh').val('${mjFdlVehicle.qybh}');
        $('#pfbz').val('${mjFdlVehicle.pfbz}');
        $('#rlzl').val('${mjFdlVehicle.rlzl}');
        form.render();
        form.on('submit(*)', function (data) {
            var cpsbm = $("#cpsbm").val();
            var jxhbdm = $("#jxhbdm").val();
            var scrq = $("#scrq").val();
            var cphm = $("#cphm").val();
            if ((cpsbm == "")&&(jxhbdm == "")) {
                layer.msg("产品识别码和机械环保代码至少填一项", {icon: 2});
                return;
            }
            if (imgfdjmpurl == "") {
                layer.msg("发动机铭牌照片不能为空", {icon: 2});
                return;
            }
            if (imgzcmpurl == "") {
                layer.msg("整车照片不能为空", {icon: 2});
                return;
            }
            if (cphm == "") {
                layer.msg("车牌号码不能为空", {icon: 2});
                return;
            }
            if(imghbbqurl=="") {
                if (ccdjrq >= "2017-07-01") {
                    if (imgurlscqd == "") {
                        layer.msg("环保标签照片不能为空", {icon: 2});
                        return;
                    }
                }
            }
            $.ajax({
                type: "post",
                url: '${path}/business/mjfdlvehicle/${oper}',
                data: {
                    "id": $("#id").val(),
                    "qybh": $("#qybh").val(),
                    "cphm": $("#cphm").val(),
                    "hbdjhm": $("#hbdjhm").val(),
                    "cpsbm": $("#cpsbm").val(),
                    "jxhbdm": $("#jxhbdm").val(),
                    "jxxh": $("#jxxh").val(),
                    "scrq": $("#scrq").val(),
                    "jxzl": $("#jxzl").val(),
                    "fdjbh": $("#fdjbh").val(),
                    "fdjscc": $("#fdjscc").val(),
                    "fdjxh": $("#fdjxh").val(),
                    "rlzl": $("#rlzl").val(),
                    "syr": $("#syr").val(),
                    "pfbz": $("#pfbz").val(),
                    "fdjmpzp": imgfdjmpurl,
                    "zcmpzp": imgzcmpurl,
                    "hbbqzp": imghbbqurl,
                },
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
    function zcmpclick() {
            $("#fileimgzcmp").click();
    }
    function fdjmpclick() {
            $("#fileimgfdjmp").click();
    }
    function hbbqclick() {
        $("#fileimghbbq").click();
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
</script>
</body>
</html>