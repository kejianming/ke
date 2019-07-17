<#-- Created by IntelliJ IDEA.
 User: zxm
 Date: 2017/12/19
 Time: 18:00
 To change this template use File | Settings | File Templates.
 角色管理-->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>设备</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <link rel="stylesheet" type="text/css" href="${re.contextPath}/plugin/layui-formSelects/dist/formSelects-v4.css"/>
    <link rel="stylesheet" href="${re.contextPath}/plugin/layui/css/layui.css">
    <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="${re.contextPath}/plugin/nfdw/js/list.js"></script>
    <script type="text/javascript" src="${re.contextPath}/plugin/layui-formSelects/dist/formSelects-v4.js"></script>
    <link rel="stylesheet" href="${re.contextPath}/plugin/nfdw/css/main.css">
    <script type="text/javascript" src="${re.contextPath}/plugin/tools/tool.js"></script>
    <script type="text/javascript" src="${re.contextPath}/plugin/common/js/echarts.js"></script>
    <script type="text/javascript" src="${re.contextPath}/ftl/section/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
    <style>
        .xm-select-parent .xm-select{
            height: inherit;
        }
    </style>
</head>

<body>

<div class="layui-fluid"  >
    <div class="layui-row" style="margin-bottom: 1%;">
        <div class="layui-form lenos-searchs select" >
            <div class="layui-form-item nfdw-form-item">

                <div class="layui-inline">
                    <label class="layui-form-label nfdw-label">字典值:</label>
                    <div class="layui-input-block nfdw-input-block">
                        <input type="text" name="dvalue"  class="layui-input input-sm" id="dvalue" />
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label nfdw-label">字典组:</label>
                    <div class="layui-input-block nfdw-input-block">
                        <input type="text" name="dgroup"  class="layui-input input-sm" id="dgroup" />
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label nfdw-label">字典编码:</label>
                    <div class="layui-input-block nfdw-input-block">
                        <input type="text" name="code"  class="layui-input input-sm" id="code" />
                    </div>
                </div>

                <div class="layui-inline">
                    <label class="layui-form-label nfdw-label">字典名称:</label>
                    <div class="layui-input-block nfdw-input-block">
                        <input type="text" name="name"  class="layui-input input-sm" id="name"/>
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

                <div class="layui-inline" style="margin-top: -5px;">
                    <button class="layui-btn layui-btn-sm layui-icon-search" data-type="select">
                        <i class="layui-icon">&#xe615;</i>搜索
                    </button>
                </div>
            </div>
        </div>
    </div>
    <table id="systemDictionaryList" class="layui-table" lay-filter="dictionary"></table>
    <script type="text/javascript" src="${re.contextPath}/plugin/layui/layui.all.js" charset="utf-8"></script>

    <script type="text/html" id="operations">
        <div id="bjtest" class="autoWidthDiv" style="width:100%;">
            <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
            <a class="layui-btn layui-btn-xs" lay-event="del">删除</a>
        </div>
    </script>

</div>

<script type="text/html" id="topToolbar">
    <div class="layui-btn-container">
        <button class="layui-btn  layui-btn-sm" data-type="add"><i class="layui-icon">&#xe654;</i>新增</button>
        <button class="layui-btn layui-btn-danger layui-btn-sm" data-type="del"><i class="layui-icon">&#xe640;</i>删除</button>
    </div>
</script>

<script>

    layui.laytpl.toDateString = function(d, format){
        var date = new Date(d || new Date())
                ,ymd = [
            this.digit(date.getFullYear(), 4)
            ,this.digit(date.getMonth() + 1)
            ,this.digit(date.getDate())
        ]
                ,hms = [
            this.digit(date.getHours())
            ,this.digit(date.getMinutes())
            ,this.digit(date.getSeconds())
        ];

        format = format || 'yyyy-MM-dd HH:mm:ss';

        return format.replace(/yyyy/g, ymd[0])
                .replace(/MM/g, ymd[1])
                .replace(/dd/g, ymd[2])
                .replace(/HH/g, hms[0])
                .replace(/mm/g, hms[1])
                .replace(/ss/g, hms[2]);
    };

    //数字前置补零
    layui.laytpl.digit = function(num, length, end){
        var str = '';
        num = String(num);
        length = length || 2;
        for(var i = num.length; i < length; i++){
            str += '0';
        }
        return num < Math.pow(10, length) ? str + (num|0) : num;
    };

    document.onkeydown = function (e) { // 回车提交表单
        var theEvent = window.event || e;
        var code = theEvent.keyCode || theEvent.which;
        if (code == 13) {
            $(".select .select-on").click();
        }
    };
    layui.use('table', function () {
        var table = layui.table;
        //方法级渲染
        table.render({
            id: 'systemDictionaryList',
            elem: '#systemDictionaryList'
            , url: 'showSystemDictionaryList'
            ,toolbar: '#topToolbar'
            ,done: setAutoWidth
            , cols: [[
                {checkbox: true, fixed: true, width: '5%'}
                ,{field: 'dgroup', title: '字典组', width: '15%', sort: true,align:'center'}
                , {field: 'code', title: '字典编码', width: '15%', sort: true,align:'center'}
                , {field: 'dvalue', title: '字典值', width: '15%', sort: true,align:'center'}
                , {field: 'name', title: '字典名称', width: '16%', sort: true,align:'center'}
                , {field: 'isused', title: '是否可用', width: '10%', sort: true,align:'center'}
                , {field: 'sort', title: '排序', width: '10%', sort: true,align:'center'}
                , {fixed: 'right',field: 'text',align:'center', title: '操作', width: '15%', toolbar:'#operations'}

            ]]
            , page: true
            ,  height: 'full-100'
        });

        var $ = layui.$, active = {
            select: function () {
                var dvalue = $('#dvalue').val();
                var dgroup = $('#dgroup').val();
                var code = $("#code").val();
                var name = $("#name").val();
                var isused = $("#isused").val();
                table.reload('systemDictionaryList', {
                    where: {
                        dvalue: dvalue,
                        dgroup: dgroup,
                        code: code,
                        name:name,
                        isused:isused
                    }

                });

            },
            add: function () {
                add('新增', 'showAddDictionary', 700, 450);
            },
            del: function () {
                var checkStatus = table.checkStatus('systemDictionaryList')
                        , data = checkStatus.data;
                if (data.length ==0) {
                    layer.msg('请选择要删除的数据', {icon: 5});
                    return false;
                }
                var ids=[];
                for(item in data){
                    ids.push(data[item].id);
                }
                layer.confirm('确定删除吗？', function(index){
                    del(ids);
                    layer.close(index);
                });
            },
            reload:function(){
                var dvalue = $('#dvalue').val();
                var dgroup = $('#dgroup').val();
                var code = $("#code").val();
                var name = $("#name").val();
                var isused = $("#isused").val();
                table.reload('systemDictionaryList', {
                    where: {
                        dvalue: dvalue,
                        dgroup: dgroup,
                        code: code,
                        name:name,
                        isused:isused
                    }


                });

            },

        };


        //监听工具条
        table.on('tool(dictionary)', function (obj) {
            var data = obj.data;
            if (obj.event === 'del') {
                layer.confirm('确定删除吗？', function(index){
                    del(data.id);
                    layer.close(index);
                });
            }
           else if (obj.event === 'edit') {
                update('编辑信息', 'updateDictionary?id='+data.id, 700, 450);
            }
        });

        $('body').on('click','.layui-btn-container .layui-btn', function () {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });

        $('.select .layui-btn').on('click', function () {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });

    });
    /**批量删除id*/
    function del(ids) {
        $.ajax({
            url: "del",
            type: "post",
            data: {ids: ids},
            success: function (d) {

                if (d.flag) {

                    window.top.layer.msg(d.msg,{icon:6});
                    // layui.table.reload('systemDictionaryList');
                    location.reload();
                } else {
                    window.top.layer.msg(d.msg,{icon:5});
                    location.reload();
                }
            }
        });
    }
    function add(title, url, w, h) {
        if (title == null || title == '') {
            title = false;
        }
        ;
        if (url == null || url == '') {
            url = "/error/404";
        }
        ;
        if (w == null || w == '') {
            w = ($(window).width() * 0.9);
        }
        ;
        if (h == null || h == '') {
            h = ($(window).height() - 50);
        }
        ;
        layer.open({
            id: 'dictionary-add',
            type: 2,
            area: [w + 'px', h + 'px'],
            fix: false,
            maxmin: true,
            shadeClose: false,
            shade: 0.4,
            title: title,
            content: url
        });
    }
    function start(id) {
        $.ajax({
            url: "startUpDetection",
            type: "get",
            data: {id: id},
            success: function (d) {

                if('0000'==d.data){
                    window.top.layer.msg(d.msg,{icon:6});
                }
                else {
                    window.top.layer.msg(d.msg,{icon:5});
                }
                location.reload();
            }
        });
    }

    function update(title, url, w, h) {
        if (title == null || title == '') {
            title = false;
        }
        if (url == null || url == '') {
            url = "/error/404";
        }
        if (w == null || w == '') {
            w = ($(window).width() * 0.9);
        }
        if (h == null || h == '') {
            h = ($(window).height() - 50);
        }
        layer.open({
            id: 'dictionary-update',
            type: 2,
            area: [w + 'px', h + 'px'],
            fix: false,
            maxmin: true,
            shadeClose: false,
            shade: 0.4,
            title: title,
            content: url +'&detail=false'
        });
    }
    function viewReport(title, url, w, h) {
        if (title == null || title == '') {
            title = false;
        }
        ;
        if (url == null || url == '') {
            url = "/error/404";
        }
        ;
        if (w == null || w == '') {
            w = ($(window).width() * 0.9);
        }
        ;
        if (h == null || h == '') {
            h = ($(window).height() - 50);
        }
        ;
        layer.open({
            id: 'dictionary-viewReport',
            type: 2,
            area: [w + 'px', h + 'px'],
            fix: false,
            maxmin: true,
            shadeClose: false,
            shade: 0.4,
            title: title,
            content: url,
            success: function(d){

            }
        });
    }
    function viewLog(title, url, w, h) {
        if (title == null || title == '') {
            title = false;
        }
        ;
        if (url == null || url == '') {
            url = "/error/404";
        }
        ;
        if (w == null || w == '') {
            w = ($(window).width() * 0.9);
        }
        ;
        if (h == null || h == '') {
            h = ($(window).height() - 50);
        }
        ;
        layer.open({
            id: 'dictionary-viewLog',
            type: 2,
            area: [w + 'px', h + 'px'],
            fix: false,
            maxmin: true,
            shadeClose: false,
            shade: 0.4,
            title: title,
            content: url
        });
    }
    function exportExcelFile() {
        $.ajax({
            url: "exportDetectionDemoExcel",
            type: "get",
            traditional: true,
            success: function () {
                //layer.msg(data.msg, {icon: 6});
                layui.table.reload('systemDictionaryList');
                location.reload();
            }
        });
    }
    function daoru(id) {
        shangchuanid=id;
        document.getElementById("file").click();

    }
    function checkDetectionProgress(title, url, w, h) {
        if (title == null || title == '') {
            title = false;
        }
        ;
        if (url == null || url == '') {
            url = "/error/404";
        }
        ;
        if (w == null || w == '') {
            w = ($(window).width() * 0.9);
        }
        ;
        if (h == null || h == '') {
            h = ($(window).height() - 50);
        }
        ;
        layer.open({
            id: 'dictionary-viewReport',
            type: 2,
            area: [w + 'px', h + 'px'],
            fix: false,
            maxmin: true,
            shadeClose: false,
            shade: 0.4,
            title: title,
            content: url
        });
    }
</script>
</body>

</html>
