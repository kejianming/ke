<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>htmltitle</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport"
        content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
  <link rel="stylesheet" href="${r'${re.contextPath}'}/plugin/layui/css/layui.css">
  <link rel="stylesheet" href="${r'${re.contextPath}'}/plugin/lenos/main.css">
  <script type="text/javascript" src="${r'${re.contextPath}'}/plugin/jquery/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="${r'${re.contextPath}'}/plugin/layui/layui.all.js"
          charset="utf-8"></script>
    <script type="text/javascript" src="${r'${re.contextPath}'}/plugin/tools/tool.js"></script>

</head>

<body>

<div class="layui-col-md12" style="height:40px;margin-top:3px;">
  <div class="layui-btn-group">

      <button class="layui-btn layui-btn-normal" data-type="add">
      <i class="layui-icon">&#xe608;</i>新增
       </button>


    <button class="layui-btn layui-btn-normal" data-type="update">
      <i class="layui-icon">&#xe642;</i>编辑
    </button>


    <button class="layui-btn layui-btn-normal" data-type="del">
      <i class="layui-icon">&#xe605;</i>删除
    </button>


  </div>
</div>
<table id="${tableClass.lowerCaseName}List" class="layui-hide" lay-filter="${tableClass.lowerCaseName}List"></table>

<script>
  document.onkeydown = function (e) { // 回车提交表单
    var theEvent = window.event || e;
    var code = theEvent.keyCode || theEvent.which;
    if (code == 13) {
      $(".select .select-on").click();
    }
  }
  layui.use('table', function () {
    var table = layui.table;
    //方法级渲染
    table.render({
      id: '${tableClass.lowerCaseName}List',
      elem: '#${tableClass.lowerCaseName}List'
      , url: '${tableClass.lowerCaseName}List'
      , cols: [[
        {checkbox: true, fixed: true, width: '5%'}
        <#list tableClass.baseFields as item><#if item.remarks?contains("show")>, {field:'${item.fieldName}',title:'${item.remarks ?replace("show","")}'}</#if></#list>
      ]]
      , page: true,
      height: 'full-83'
    });

    var $ = layui.$, active = {

      add: function () {
        add('addModalTitle', 'showAdd${tableClass.shortClassName}', 700, 450);
      },
      update: function () {
        var checkStatus = table.checkStatus('${tableClass.lowerCaseName}List')
            , data = checkStatus.data;
        if (data.length != 1) {
          layer.msg('请选择一行编辑,已选['+data.length+']行', {icon: 5});
          return false;
        }
        update('updateModalTitle', 'update${tableClass.shortClassName}?id=' + data[0].id, 700, 450);
      },
        del:function(){
            var checkStatus = table.checkStatus('${tableClass.lowerCaseName}List')
                    , data = checkStatus.data;
            if (data.length ==0) {
                layer.msg('请选择要删除的数据', {icon: 5});
                return false;
            }
            var ids=[];
            for(item in data){
                ids.push("'"+data[item].id+"'");
            }
            del(ids);
        }


    };

    //监听表格复选框选择
    table.on('checkbox(user)', function (obj) {
      console.log(obj)
    });
    $('.layui-col-md12 .layui-btn').on('click', function () {
      var type = $(this).data('type');
      active[type] ? active[type].call(this) : '';
    });
    $('.select .layui-btn').on('click', function () {
      var type = $(this).data('type');
      active[type] ? active[type].call(this) : '';
    });

  });


  function update(title, url, w, h) {
    if (title == null || title == '') {
      title = false;
    }
    if (url == null || url == '') {
      url = "404.html";
    }
    if (w == null || w == '') {
      w = ($(window).width() * 0.9);
    }
    if (h == null || h == '') {
      h = ($(window).height() - 50);
    }
    layer.open({
      id: '${tableClass.lowerCaseName}-update',
      type: 2,
      area: [w + 'px', h + 'px'],
      fix: false,
      maxmin: true,
      shadeClose: false,
      shade: 0.4,
      title: title,
      content: url + '&detail=false'
    });
  }


  function add(title, url, w, h) {
    if (title == null || title == '') {
      title = false;
    }
    ;
    if (url == null || url == '') {
      url = "404.html";
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
      id: '${tableClass.lowerCaseName}-add',
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

  function del(ids) {
      $.ajax({
          url: "deleteAll",
          type: "post",
          data: {ids: ids},
          dataType: "json", traditional: true,
          success: function (data) {
              layer.msg(data.msg, {icon: 6});
              layui.table.reload('${tableClass.lowerCaseName}List');
          }
      });
  }

</script>
</body>

</html>
