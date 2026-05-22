<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <%--<%@ include file="/commons/basejs.jsp"%>--%>
    <%@ include file="/commons/baseloginjs.jsp" %>
    <%--    <meta http-equiv="X-UA-Compatible" content="edge"/>--%>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>机构信息编辑</title>
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
            color: white;
        }

        input[type=number]::-webkit-inner-spin-button,
        input[type=number]::-webkit-outer-spin-button {
            -webkit-appearance: none;
            margin: 0;
        }
    </style>
</head>
<body>
<div class="layui-fluid">
    <input id="oldorgan" value="${organ.organ}" type="hidden">
    <div class="layui-card">
        <div class="layui-card-body">
            <div class="layui-form toolbar" id="queryFormItem">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">机构编号</label>
                            <div class="layui-input-inline">
                                <input id="organ" name="organ" placeholder="请输入机构编号" class="layui-input"
                                       lay-verify="required"
                                       value="${organ.organ}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">机构简称</label>
                            <div class="layui-input-inline">
                                <input id="jc" name="jc" placeholder="" class="layui-input" lay-verify="required"
                                       value="${organ.jc}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">机构名称</label>
                            <div class="layui-input-inline">
                                <input id="name" name="name" placeholder="请输入机构名称" class="layui-input"
                                       lay-verify="required"
                                       value="${organ.name}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">地址</label>
                            <div class="layui-input-inline">
                                <input id="address" name="address" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${organ.address}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">机构类型</label>
                            <div class="layui-input-inline">
                                <select id="organtype" name="organtype" lay-filter="oiNameFilter" lay-search
                                        lay-verify="required">
                                    <option value="">请选择</option>
                                    <option value="0">省环保厅</option>
                                    <option value="1">市环保局</option>
                                    <option value="2">县区环保局</option>
                                    <option value="3">企业</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">排序</label>
                        <div class="layui-input-inline">
                            <input id="seq" name="seq" placeholder="" class="layui-input" lay-verify="required"
                                   value="${organ.seq}" onkeyup='this.value=this.value.replace(/\D/gi,"")'/>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">上级机构</label>
                            <div class="layui-input-inline">
                                <div class="layui-unselect layui-form-select downpanel" id="downpl">
                                    <div class="layui-select-title">
                                        <span class="layui-input layui-unselect" id="porganname">选择机构名称</span>
                                        <input type="hidden" name="porgan" id="porgan" value="">
                                        <i class="layui-edge"></i>
                                    </div>
                                    <dl class="layui-anim layui-anim-upbit" id="divselect">
                                        <dd>
                                            <ul id="selectOrgan"></ul>
                                        </dd>
                                    </dl>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">机构编号(新)</label>
                            <div class="layui-input-inline">
                                <input id="organnew" name="organnew" placeholder="请输入新机构编号" class="layui-input"
                                       lay-verify="required"
                                       value="${organ.organnew}"/>
                            </div>
                        </div>
                    </div>

                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">统一社会信用编码</label>
                            <div class="layui-input-inline">
                                <input id="tyshxybm" name="tyshxybm" placeholder="" class="layui-input"
                                       lay-verify=""
                                       value="${organ.tyshxybm}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">法人代表</label>
                            <div class="layui-input-inline">
                                <input id="frdb" name="frdb" placeholder="" class="layui-input" lay-verify=""
                                       value="${organ.frdb}"/>
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
                                       value="${organ.lng}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">纬度</label>
                            <div class="layui-input-inline">
                                <input id="lat" name="lat" type="number" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${organ.lat}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">行业类型</label>
                            <div class="layui-input-inline">
                                <input id="hylx" name="hylx" placeholder="" class="layui-input" lay-verify=""
                                       value="${organ.hylx}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">行政区划</label>
                            <div class="layui-input-inline">
                                <input id="ssxq" name="ssxq" placeholder="" class="layui-input" lay-verify=""
                                       value="${organ.ssxq}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">行业分支</label>
                            <div class="layui-input-inline">
                                <input id="hyfz" name="hyfz" placeholder="" class="layui-input"
                                       lay-verify=""
                                       value="${organ.hyfz}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">绩效分级管控类型</label>
                            <div class="layui-input-inline">
                                <select id="jxfjgklx" name="jxfjgklx" lay-filter="oiNameFilter" lay-search
                                        lay-verify="required">
                                    <option value="">请选择</option>
                                    <option value="A">A级企业</option>
                                    <option value="B">B级企业</option>
                                    <option value="C">C级企业</option>
                                    <option value="D">D级企业</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">联系人</label>
                            <div class="layui-input-inline">
                                <input id="lxr" name="lxr" placeholder="" class="layui-input"
                                       lay-verify=""
                                       value="${organ.lxr}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">联系人电话</label>
                            <div class="layui-input-inline">
                                <input id="lxrdh" name="lxrdh" type="number" placeholder="" class="layui-input"
                                       lay-verify=""
                                       value="${organ.lxrdh}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">载货出入口数量</label>
                            <div class="layui-input-inline">
                                <input id="zhcrksl" name="zhcrksl" type="number" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${organ.zhcrksl}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">道闸数量</label>
                            <div class="layui-input-inline">
                                <input id="dzsl" name="dzsl" type="number" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${organ.dzsl}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">运输车辆数量</label>
                            <div class="layui-input-inline">
                                <input id="ysclsl" name="ysclsl" type="number" placeholder="" class="layui-input"
                                       lay-verify=""
                                       value="${organ.ysclsl}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">厂内运输车辆数量</label>
                            <div class="layui-input-inline">
                                <input id="cnysclsl" name="cnysclsl" type="number" placeholder="" class="layui-input"
                                       lay-verify=""
                                       value="${organ.cnysclsl}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">非道路移动机械数量</label>
                            <div class="layui-input-inline">
                                <input id="fdlydjxsl" name="fdlydjxsl" type="number" placeholder="" class="layui-input"
                                       lay-verify=""
                                       value="${organ.fdlydjxsl}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">状态</label>
                            <div class="layui-input-inline">
                                <select id="status" name="status" lay-filter="oiNameFilter" lay-search
                                        lay-verify="required">
                                    <option value="">请选择</option>
                                    <option value="0">停用</option>
                                    <option value="1">启用</option>
                                </select>
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
    var oldorgan = $("#oldorgan").val();
    layui.config({
        base: '${staticPath}/static/module/'
    }).use(['form', 'tree'], function () {
        var form = layui.form;
        var tree = layui.tree;
        var $ = layui.jquery;
        $.ajax({
            type: "post",
            url: '${path}/admin/organization/orgtreelevelForOrgan?organtype=0,1,2',
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
                        $('#porgan').val(data.id);
                        $('#porganname').html(data.title);
                        $('#downpl').toggleClass("layui-form-selected");
                    }
                });
            }
        });
        // 车辆类型数据
        $.ajax({
            type: "post",
            url: '${path}/admin/syscode/combox',
            data: {"oi_name": '辖区'},
            dataType: 'json',
            success: function (data) {
                $('#ssxq').empty();
                var t;
                var t = "<option value='' selected='selected'>请选择</option>";
                for (var i = 0; i < data.length; i++) {
                    if (data[i].oi_value != undefined && data[i].oi_code != undefined) {
                        t += '<option value="' + data[i].oi_code + '">' + data[i].oi_value + '</option>';
                    }
                }
                $('#ssxq').append(t);
                form.render('select');
                $('#ssxq').val('${organ.ssxq}');
                form.render();
            }
        });

        $(".downpanel").on("click", ".layui-select-title", function (e) {
            $(".layui-form-select").not($(this).parents(".layui-form-select")).removeClass("layui-form-selected");
            $(this).parents(".downpanel").toggleClass("layui-form-selected");
            layui.stope(e);
        }).on("click", "dl i", function (e) {
            layui.stope(e);
        });
        $("#organtype").val('${organ.organtype}');
        var $select = $($(this)[0].elem).parents(".layui-form-select");

        document.getElementById("porganname").innerText = '${organ.porganname}';
        $("#porgan").val('${organ.porgan}');
        $("#status").val('${organ.status}');
        $("#jxfjgklx").val('${organ.jxfjgklx}');
        form.render();

        form.on('submit(*)', function (data) {
            console.log(data.field);
            //data.field 当前容器的全部表单字段，名值对形式：{name: value}
            $.ajax({
                type: "post",
                url: '${path}/admin/organization/${oper}?oldorgan=' + oldorgan,
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