<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>流程实例列表</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport"
        content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
  <link rel="stylesheet" href="${re.contextPath}/plugin/layui/css/layui.css">
  <link rel="stylesheet" href="${re.contextPath}/plugin/lenos/main.css">
  <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/layui/layui.all.js"
          charset="utf-8"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/tools/tool.js" charset="utf-8"></script>
</head>

<body>
<div class="lenos-search">

</div>

<table id="taskList" class="layui-hide" lay-filter="act"></table>
<script type="text/html" id="toolBar">

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
  }

  layui.use('table', function () {
    var table = layui.table;
    //方法级渲染
    table.render({
      id: 'taskList',
      elem: '#taskList'
      , url: 'showTaskList'
      , cols: [[
          {checkbox: true, fixed: true, width: '5%'}
            , {field: 'id', title: '任务编码', width: '10%', sort: true}
            , {field: 'userName', title: '申请人', width: '10%', sort: true}
            , {field: 'reason', title: '原因', width: '15%', sort: true}
            , {field: 'name', title: '任务名称', width: '10%', sort: true}
            , {field: 'createTime', title: '创建时间', width: '10%', sort: true,templet: '<div>{{ layui.laytpl.toDateString(d.createTime,"yyyy-MM-dd") }}</div>'}
            , {field: 'assignee', title: '委派人', width: '10%', sort: true}
            , {field: 'processInstanceId', title: '流程实例ID', width: '15%', sort: true}
      ]]
      , page: true
      ,  height: 'full-85'
        , where: {processInstanceId:'${processInstanceId}'}
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
  




</script>
</body>

</html>
