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
    <title>白名单信息編輯</title>
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
            <div class="layui-form toolbar" id="queryFormItem" lay-filter="mjbmdForm">
                <input id="id" name="id"  value="${mjbmd.id}" type="hidden">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">组织机构</label>
                        <div class="layui-input-inline">
                            <div class="layui-unselect layui-form-select downpanel"style="width:300px" id="downpl">
                                <div class="layui-select-title">
                                    <span class="layui-input layui-unselect" id="organname" style="width:300px">选择机构名称</span>
                                    <input type="hidden" name="organ" id="organ" value="">
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
                                <input id="cphm" name="cphm" placeholder="" class="layui-input" lay-verify="required"
                                       value="${mjbmd.cphm}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label">车牌颜色</label>
                        <div class="layui-input-inline">
                            <select id="cpys" name="cpys" lay-filter="jclxFilter" lay-verify="required" readonly="true"
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
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">车主名称</label>
                            <div class="layui-input-inline">
                                <input id="czmc" name="czmc" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjbmd.czmc}"/>
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">电话</label>
                            <div class="layui-input-inline">
                                <input id="lxdh" name="lxdh" placeholder="" class="layui-input"
                                       lay-verify="required"
                                       value="${mjbmd.lxdh}" onkeyup='this.value=this.value.replace(/\D/gi,"")'/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">生效时间</label>
                            <div class="layui-input-inline">
                                <input type="text" name="sxsj" id="sxsj" lay-verify="sxsj"
                                       placeholder=""
                                       autocomplete="off" class="layui-input" value="${sxsj}">
                            </div>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">终止时间</label>
                            <div class="layui-input-inline">
                                <input type="text" name="zzsj" id="zzsj" lay-verify="zzsj"
                                       placeholder=""
                                       autocomplete="off" class="layui-input" value="${zzsj}">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">备注</label>
                            <div class="layui-input-inline">
                                <input type="text" name="bz" id="bz"
                                       placeholder=""
                                       autocomplete="off" class="layui-input" style="width: 500px" value="${mjbmd.bz}">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-inline">
                            <label class="layui-form-label">可出入口</label>
                            <div class="layui-input-inline" style="width: 400px">
                                <input type="checkbox" name="crkbh1" value="1" title="1号门" lay-skin="primary">
                                <input type="checkbox" name="crkbh2" value="2" title="2号门" lay-skin="primary">
                                <input type="checkbox" name="crkbh3" value="3" title="3号门" lay-skin="primary">
                                <input type="checkbox" name="crkbh4" value="4" title="4号门" lay-skin="primary">
                                <input type="checkbox" name="crkbh5" value="5" title="5号门" lay-skin="primary">
                                <input type="checkbox" name="crkbh6" value="6" title="6号门" lay-skin="primary">
                                <input type="checkbox" name="crkbh7" value="7" title="7号门" lay-skin="primary">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline" style="float: right; margin: 5px 20px;">
                        <button id="userBtnSearch" lay-submit lay-filter="submitForm" class="layui-btn icon-btn"
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
        var laydate = layui.laydate;
        //日期
        laydate.render({
            elem: '#sxsj'
        });

        laydate.render({
            elem: '#zzsj'
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
                        $('#organ').val(data.id);
                        $('#organname').html(data.title);
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
        $("#organname").html('${organname}');
        $('#organ').val('${mjbmd.organ}');
        $('#cpys').val('${mjbmd.cpys}');
        var crkbhs='${mjbmd.crkbh}';
        if(crkbhs.indexOf("1")>=0){
            form.val("mjbmdForm", {crkbh1: "1"});
        }
        if(crkbhs.indexOf("2")>=0){
            form.val("mjbmdForm", {crkbh2: "2"});
        }
        if(crkbhs.indexOf("3")>=0){
            form.val("mjbmdForm", {crkbh3: "3"});
        }
        if(crkbhs.indexOf("4")>=0){
            form.val("mjbmdForm", {crkbh4: "4"});
        }
        if(crkbhs.indexOf("5")>=0){
            form.val("mjbmdForm", {crkbh5: "5"});
        }
        if(crkbhs.indexOf("6")>=0){
            form.val("mjbmdForm", {crkbh6: "6"});
        }
        if(crkbhs.indexOf("7")>=0){
            form.val("mjbmdForm", {crkbh7: "7"});
        }
        form.render();
        form.on('submit(submitForm)', function (data) {
            // 关键修复：先判断表单数据是否存在，再处理 crkbh
            var formData = data.field;
            var crkbh="";
            crkbh= add(crkbh,formData.crkbh1);
            crkbh= add(crkbh,formData.crkbh2);
            crkbh= add(crkbh,formData.crkbh3);
            crkbh= add(crkbh,formData.crkbh4);
            crkbh= add(crkbh,formData.crkbh5);
            crkbh= add(crkbh,formData.crkbh6);
            crkbh= add(crkbh,formData.crkbh7);
            var params = {id: formData.id, cphm: formData.cphm,cpys: formData.cpys, czmc: formData.czmc, lxdh: formData.lxdh,
                organ:formData.organ,sxsj:formData.sxsj,zzsj:formData.zzsj,bz:formData.bz,crkbh:crkbh};
            console.log(params);
            //data.field 当前容器的全部表单字段，名值对形式：{name: value}
            $.ajax({
                type: "post",
                url: '${path}/local/bmd/${oper}',
                data: params,
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
        // 4. （可选）监听 Checkbox 变化，实时获取值
        form.on('checkbox(crkbh)', function(){
            var formData = form.val('mjbmdForm');
            var crkbhValues = formData.crkbh ? (Array.isArray(formData.crkbh) ? formData.crkbh : [formData.crkbh]) : [];
            console.log("实时选中值：", crkbhValues);
        });

    })
    function add(crkbh,xh) {
        if(xh!=undefined){
            if (crkbh==""){
                crkbh=xh;
            }
            else {
                crkbh=crkbh+","+xh;
            }
        }
        return crkbh;
    }
</script>
</body>
</html>