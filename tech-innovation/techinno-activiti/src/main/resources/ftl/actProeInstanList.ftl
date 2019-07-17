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
  <div class="select">
    实例id：
    <div class="layui-inline">
      <input class="layui-input" height="20px" id="processInstanceId" autocomplete="off">
    </div>
    流程名称：
    <div class="layui-inline">
      <input class="layui-input" height="20px" id="name" autocomplete="off">
    </div>
    <button class="select-on layui-btn layui-btn-sm" data-type="select"><i class="layui-icon"></i>
    </button>
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;"
            data-type="reload">
      <i class="layui-icon">ဂ</i>
    </button>
  </div>
</div>

<table id="processInstanceList" class="layui-hide" lay-filter="act"></table>
<script type="text/html" id="toolBar">
 <#-- <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"><i class="layui-icon">&#xe640;</i>删除</a>-->
  <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="showtask"><i class="layui-icon">&#xe640;</i>任务列表</a>
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
      id: 'processInstanceList',
      elem: '#processInstanceList'
      , url: 'showProcessInstanceList'
      , cols: [[
          {checkbox: true, fixed: true, width: '5%'}
        , {field: 'processDefinitionId', title: '流程定义编号', width: '20%', sort: true}
        , {field: 'processDefinitionName', title: '流程定义名称', width: '20%', sort: true}
        , {field: 'deploymentId', title: '流程部署id', width: '20%', sort: true}
        , {field: 'name', title: '流程实例名称', width: '20%', sort: true}
        , {field: 'text', title: '操作', width: '10%', toolbar:'#toolBar'}
      ]]
      , page: true
      ,  height: 'full-85'
        , where: {deploymentId:'${deploymentId}'}
    });

    var $ = layui.$, active = {
      select: function () {
        var deploymentId = $('#deploymentId').val();
        var name = $('#name').val();
        table.reload('processInstanceList', {
          where: {
            deploymentId: deploymentId,
            name: name
          }
        });
      }
      ,assignee:function(){
        var checkStatus = table.checkStatus('processInstanceList')
            , data = checkStatus.data;
        if (data.length !=1) {
          layer.msg('请选择一个流程', {icon: 5});
          return false;
        }
        assignee(data[0].id,data[0].deploymentId);
      }
      ,reload:function(){
        $('#deploymentId').val('');
        $('#name').val('');
        table.reload('processInstanceList', {
          where: {
            deploymentId: null,
            name: null
          }
        });
      }
    };
    //监听工具条
      table.on('tool(act)', function (obj) {
      var data = obj.data;
      if (obj.event === 'del') {
        layer.confirm('将会删除正在执行的流程,确定删除？', {
          btn: ['确定','取消'] //按钮
        }, function(){
          del(data.deploymentId);
        }, function(){
        });
      }

      if(obj.event==='showtask'){
          showtask(data.processInstanceId);
      }



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
  
  function showtask(id) {
      layer.open({
          type: 2,
          content: '${re.contextPath}/act/goTaskList?processInstanceId='+id,
          area: ['1080px', '500px']
      });
  }

  
  function del(id) {
    /*$.ajax({
      url: "delDeploy",
      type: "post",
      data: {id: id},
      dataType: "json", traditional: true,
      success: function (d) {
        if(d.flag){
          layer.msg(d.msg, {icon: 6});
          layui.table.reload('actList');
        }else{
          layer.msg(d.msg, {icon: 5});
        }
      }
    });*/
  }
  /**
   * 流程绑定节点
   * @param id
   */
  function assignee(id,deploymentId){
   /* var index =
        layer.open({
          id: 'assignee',
          type: 2,
          area: ['600px',  '350px'],
          fix: false,
          maxmin: true,
          shadeClose: false,
          shade: 0.4,
          title: '设置流程节点',
          content: 'goAssignee/'+deploymentId
        });
    layer.full(index);*/
  }


</script>
</body>

</html>
