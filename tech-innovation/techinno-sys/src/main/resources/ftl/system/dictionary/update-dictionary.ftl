<#--Created by IntelliJ IDEA.
User: Administrator
Date: 2017/12/27
Time: 12:40
To change this template use File | Settings | File Templates.-->

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>菜单管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
    <link rel="stylesheet" href="${re.contextPath}/plugin/layui/css/layui.css">
    <link rel="stylesheet" href="${re.contextPath}/plugin/nfdw/css/main.css">
    <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="${re.contextPath}/plugin/layui/layui.all.js" charset="utf-8"></script>
    <script type="text/javascript" src="${re.contextPath}/plugin/nfdw/js/list.js"></script>
</head>

<body>
<div class="x-body">


    <form class="layui-form layui-form-pane" style="margin-left: 20px;">
        <div style="width:100%;height:500px;overflow: auto;">
            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">基础信息</legend>
                </fieldset>
            </div>
            <div class="layui-form lenos-searchs select" >
                <div class="layui-form-item">
                    <input value="${systemDictionaryList.id}" type="hidden" name="id">
                    <div class="layui-inline">
                        <label class="layui-form-label nfdw-label">字典组:</label>
                        <div class="layui-input-block nfdw-input-block">
                            <input type="text" name="dgroup" value="${systemDictionaryList.dgroup}" class="layui-input input-sm" id="snList" placeholder="请输入sn号(多个sn号则以逗号隔开)"/>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label nfdw-label">字典值:</label>
                        <div class="layui-input-block nfdw-input-block">
                            <input type="text" name="dvalue" value="${systemDictionaryList.dvalue}" class="layui-input input-sm" id="snList" placeholder="请输入sn号(多个sn号则以逗号隔开)"/>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label nfdw-label">字典名称:</label>
                        <div class="layui-input-block nfdw-input-block">
                            <input type="text" name="name" value="${systemDictionaryList.name}" class="layui-input input-sm" id="snList" placeholder="请输入sn号(多个sn号则以逗号隔开)"/>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label nfdw-label">排序编码:</label>
                        <div class="layui-input-block nfdw-input-block">
                            <input type="text" name="sort" value="${systemDictionaryList.sort}" class="layui-input input-sm" id="snList" placeholder="请输入sn号(多个sn号则以逗号隔开)"/>
                        </div>
                    </div>
                    <div class="layui-inline">
                        <label class="layui-form-label nfdw-label">  是否启用：</label>
                        <div class="layui-input-block nfdw-input-block">
                            <select name="isused" id="isused"  lay-filter="isused">
                                <option value=""></option>
                                <option value="0">否</option>
                                <option value="1">是</option>
                            </select>
                        </div>
                    </div>

                </div>
                <div class="layui-form-item" style=" float: right;margin-right: 30px;margin-top: 8px">
                    <button  class="layui-btn layui-btn-normal" lay-filter="add" lay-submit="">
                        修改
                    </button>
                    <button  class="layui-btn layui-btn-primary" id="close">
                        取消
                    </button>
                </div>
            </div>
        </div>

    </form>
</div>
<script>

    document.onkeydown = function (e) { // 回车提交表单
        var theEvent = window.event || e;
        var code = theEvent.keyCode || theEvent.which;
        if (code == 13) {
            $(".select .select-on").click();
        }
    };

    $(function(){
        getSelect('factory_type',layui.form);
    });

    layui.use(['layer','form'],function () {
        var form=layui.form;
        var layer=layui.layer;
        form.on('submit(add)',function (data) {
            $.ajax({
                url: "saveOrUpdateDictionary",
                type: "post",
                data: data.field,
                success: function (d) {
                    if (d.flag) {
                        window.top.layer.msg(d.msg,{icon:6});
                        var index = parent.layer.getFrameIndex(window.name);
                        parent.layer.close(index);//关闭当前页
                        window.parent.location.reload();

                    } else {
                        window.top.layer.msg(d.msg,{icon:5});
                        location.reload();
                    }
                }
            });
        })
    })
</script>
</body>

</html>
