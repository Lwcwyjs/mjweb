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
                <input id="id" name="id" value="${mjspxx.id}" type="hidden">
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
                            <label class="layui-form-label">道闸编号</label>
                            <div class="layui-input-inline">
                                <select id="dzbh" name="dzbh" lay-filter="crkbhFilter" lay-verify="required"
                                        lay-search>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">进出类型</label>
                            <div class="layui-input-inline">
                                <select id="jclx" name="jclx" lay-filter="jclxFilter" lay-verify="required"
                                        lay-search>
                                    <option value="">请选择</option>
                                    <option value="1">进</option>
                                    <option value="2">出</option>
                                </select>
                            </div>
                        </div>
                    </div>

                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">IP</label>
                            <div class="layui-input-inline">
                                <input id="ip" name="ip" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjspxx.ip}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">用户名</label>
                            <div class="layui-input-inline">
                                <input id="username" name="username" placeholder="" class="layui-input" lay-verify="required"
                                       value="${mjspxx.username}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">密码</label>
                            <div class="layui-input-inline">
                                <input id="pwd" name="pwd" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjspxx.pwd}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">厂商名称</label>
                            <div class="layui-input-inline">
                                <input id="csmc" name="csmc"  placeholder="" class="layui-input"
                                       value="${mjspxx.csmc}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">型号</label>
                            <div class="layui-input-inline">
                                <input id="xh" name="xh" placeholder="" class="layui-input"
                                       value="${mjspxx.xh}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">门口类型</label>
                            <div class="layui-input-inline">
                                <select id="mklx" name="mklx" lay-filter="crkbhFilter"
                                        lay-search>
                                    <option value="">请选择</option>
                                    <option value="1">物料门</option>
                                    <option value="2">行政门</option>
                                    <option value="3">应急门</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">摄像头编号</label>
                            <div class="layui-input-inline">
                                <input id="sxtbh" name="sxtbh" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjspxx.sxtbh}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">摄像头名称</label>
                            <div class="layui-input-inline">
                                <input id="sxtmc" name="sxtmc" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjspxx.sxtmc}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">实时播放路径</label>
                            <div class="layui-input-inline">
                                <input id="realurl" name="realurl" placeholder="" class="layui-input"
                                       lay-verify="required" style="width:545px"
                                       value="${mjspxx.realurl}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">回放播放路径</label>
                            <div class="layui-input-inline">
                                <input id="replayurl" name="replayurl" placeholder="" class="layui-input"
                                       lay-verify="required" style="width:545px"
                                       value="${mjspxx.replayurl}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">国标流编号</label>
                            <div class="layui-input-inline">
                                <input id="gblbh" name="gblbh" placeholder="" class="layui-input"
                                       value="${mjspxx.gblbh}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">通道号</label>
                            <div class="layui-input-inline">
                                <input id="tdh" name="tdh" placeholder="" class="layui-input"
                                       value="${mjspxx.tdh}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">设备序列号</label>
                            <div class="layui-input-inline">
                                <input id="sbxlh" name="sbxlh" placeholder="" class="layui-input"
                                       lay-verify="required" style="width:545px"
                                       value="${mjspxx.sbxlh}"/>
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
                            url: '${path}/admin/syscode/comboxdzbh',
                            data: {"qybh":$('#qybh').val()},
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
        form.on('select(jclxFilter)',function (data) {
            var value = data.value
            var sxtbh=$("#qybh").val()+'0'+$("#jclx").val()+$("#dzbh").val();
            $("#sxtbh").val(sxtbh);
        })
        $("#organname").html('${organname}');
        $('#qybh').val('${mjspxx.qybh}');
        $.ajax({
            type: "post",
            url: '${path}/admin/syscode/comboxdzbh',
            data: {"qybh":$('#qybh').val()},
            dataType: 'json',
            async:false,
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
        $('#jclx').val('${mjspxx.jclx}');
        $('#mklx').val('${mjspxx.mklx}');
        $('#dzbh').val('${mjspxx.dzbh}');
        form.render();
        form.on('submit(*)', function (data) {
            //data.field 当前容器的全部表单字段，名值对形式：{name: value}
            $.ajax({
                type: "post",
                url: '${path}/admin/spxx/${oper}',
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
</script>
</body>
</html>