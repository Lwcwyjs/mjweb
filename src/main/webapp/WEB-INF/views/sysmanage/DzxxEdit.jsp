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
    <title>道闸信息編輯</title>
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
                <input id="id" name="id" value="${mjdzxx.id}" type="hidden">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">组织机构</label>
                        <div class="layui-input-inline">
                            <div class="layui-unselect layui-form-select downpanel" style="width:545px" id="downpl">
                                <div class="layui-select-title">
                                    <span class="layui-input layui-unselect" id="organname"
                                          style="width:545px">选择机构名称</span>
                                    <input type="hidden" name="qybh" id="qybh" value="">
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
                            <label class="layui-form-label">出入口编号</label>
                            <div class="layui-input-inline">
                                <select id="crkbh" name="crkbh" lay-filter="crkbhFilter" lay-verify="required"
                                        lay-search>
                                    <option value="">请选择</option>
                                    <option value="A">A</option>
                                    <option value="B">B</option>
                                    <option value="C">C</option>
                                    <option value="D">D</option>
                                    <option value="E">E</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">道闸编号</label>
                            <div class="layui-input-inline">
                                <input id="dzbh" name="dzbh" placeholder="" class="layui-input" lay-verify="required"
                                       value="${mjdzxx.dzbh}"/>
                            </div>
                        </div>
                    </div>

                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">道闸名称</label>
                            <div class="layui-input-inline">
                                <input id="dzmc" name="dzmc" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjdzxx.dzmc}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">道闸地址</label>
                            <div class="layui-input-inline">
                                <input id="dzdz" name="dzdz" placeholder="" class="layui-input" lay-verify="required"
                                       value="${mjdzxx.dzdz}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">经度</label>
                            <div class="layui-input-inline">
                                <input id="lng" name="lng" type="number" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjdzxx.lng}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">纬度</label>
                            <div class="layui-input-inline">
                                <input id="lat" name="lat" type="number" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjdzxx.lat}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">道闸负责人</label>
                            <div class="layui-input-inline">
                                <input id="dzfzr" name="dzfzr" placeholder="" class="layui-input" lay-verify="required"
                                       value="${mjdzxx.dzfzr}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">道闸负责人联系电话</label>
                            <div class="layui-input-inline">
                                <input id="dzfzrlxdh" name="dzfzrlxdh" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjdzxx.dzfzrlxdh}" onkeyup='this.value=this.value.replace(/\D/gi,"")'/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">道闸运维单位名称</label>
                            <div class="layui-input-inline">
                                <input id="dzywdwmc" name="dzywdwmc" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjdzxx.dzywdwmc}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">道闸运维单位联系电话</label>
                            <div class="layui-input-inline">
                                <input id="dzywdwlxdh" name="dzywdwlxdh" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjdzxx.dzywdwlxdh}" onkeyup='this.value=this.value.replace(/\D/gi,"")'/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">道闸运维单位联系人</label>
                            <div class="layui-input-inline">
                                <input id="dzywdwlxr" name="dzywdwlxr" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjdzxx.dzywdwlxr}"/>
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

        var form = layui.form;
        var tree = layui.tree;
        var $ = layui.jquery;
        form.on('select(crkbhFilter)',function (data) {
            var value = data.value
            $("#dzbh").val(value);
        })
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
                        $.ajax({
                            type: "post",
                            url: '${path}/admin/organization/getorgan',
                            data: {"qybh": data.id},
                            dataType: 'json',
                            async: false,
                            success: function (organ) {
                                console.log(organ);
                                if (organ != null) {
                                    $('#lng').val(organ.lng);
                                    $('#lat').val(organ.lat);
                                }
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
        $("#organname").html('${organname}');
        $('#qybh').val('${mjdzxx.qybh}');
        $('#crkbh').val('${mjdzxx.crkbh}');
        form.render();
        form.on('submit(*)', function (data) {
            if (!checkMobile($('#dzywdwlxdh').val())) {
                layer.msg("道闸运维单位联系电话不正确", {icon: 2});
                return;
            }
            if (!checkMobile($('#dzfzrlxdh').val())) {
                layer.msg("道闸负责人联系电话不正确", {icon: 2});
                return;
            }
            var dzbh=$("#dzbh").val();
            var crkbh=$("#crkbh").val();
            if(dzbh.length != 3){
                layer.msg("道闸编号必须为3位", {icon: 2});
                return;
            }
            else if (dzbh.indexOf(crkbh)!=0){
                layer.msg("道闸编号必须以出入口编号开头", {icon: 2});
                return;
            }
            //data.field 当前容器的全部表单字段，名值对形式：{name: value}
            $.ajax({
                type: "post",
                url: '${path}/admin/dzxx/${oper}',
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
    function checkMobile(str) {
        var re = /^1\d{10}$/
        if (re.test(str)) {
            return true;
        } else {
            return false;
        }
    }
</script>
</body>
</html>