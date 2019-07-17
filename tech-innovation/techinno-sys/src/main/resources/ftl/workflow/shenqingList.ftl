
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>layui</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  
  <link rel="stylesheet" href="${re.contextPath}/plugin/layui2.5/css/layui.css">
  <link rel="stylesheet" href="${re.contextPath}/plugin/nfdw/css/main.css">
  <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/ftl/card/js/card.js" charset="utf-8"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/layui2.5/layui.js" charset="utf-8"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/common/js/toStringTime.js" charset="utf-8"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/nfdw/js/list.js"></script>
   
   <style>
   	html,body{
   		height: 100%;
   	}
   </style>

</head>

<body >
   <div class="layui-fluid"  >
      <div class="layui-row" style="margin-bottom: 1%;">
		<div class="layui-form lenos-searchs" > 
		  <div class="layui-form-item nfdw-form-item">
			<div class="layui-inline"> 
			  <label class="layui-form-label nfdw-label">工单号:</label>  
			  <div class="layui-input-block nfdw-input-block"> 
			    <input type="text" name="code" class="layui-input input-sm" id="code" placeholder="请输入工单号"/> 
			  </div> 
			</div>
			
			<div class="layui-inline" > 
		      <label class="layui-form-label nfdw-label" style="width: 80px">工单状态:</label>  
		      <div class="layui-input-block nfdw-input-block" style="width: 180px;"> 
		       <select name="status" id="lc_type" lay-verify="menuType" lay-filter="menuType"> 
		          <option value=""></option>
		        </select>
		      </div> 
		   </div> 
			<div class="layui-inline" style="margin-top: -5px;"> 
				<button class="layui-btn layui-btn-sm" data-type="select" id="selectno">
					<i class="layui-icon layui-icon-search"></i>搜索
				</button>
		    </div>
		    
		  </div>
		</div>
	</div>
<script type="text/html" id="toolbarDemo">
  <div class="layui-btn-container">

    <@shiro.hasPermission name="control:del">
    	<button class="layui-btn layui-btn-sm" data-type="selectCardFlow">申请</button>
	</@shiro.hasPermission>
  </div>
</script>
    
    <div class="layui-row"  style="height: 48%;overflow: auto; background-color: #ffffff">
       <div class="layui-col-md12" >
    <table class="layui-table" lay-data="{url:'${re.contextPath}/workflow/findCardOrderList',done: setAutoWidth,toolbar: '#toolbarDemo',page: { 
      layout: ['limit', 'count', 'prev', 'page', 'next', 'skip']
    ,limits:[5,10, 20, 30, 40, 50]
    ,limit:10
    ,groups: 1   
    }, id:'idTest'}" lay-filter="demo" >
  <thead >
   <tr>
      <th lay-data="{type:'checkbox' }"></th>
      <th lay-data="{field:'code' ,sort: true,align:'center'}">工单号</th>
      <th lay-data="{field:'processId' ,sort: true,align:'center'}">流程实例ID</th>
      <th lay-data="{field:'name', sort: true, align:'center'}">工单名称</th>
      <th lay-data="{field:'requireman', align:'center'}">申请人</th>
      <th lay-data="{field:'statuss', align:'center'}">工单状态</th>
      <th lay-data="{field:'requiredate', sort: true, align:'center',templet:'#titleTpl'}">申请时间</th>
      <th lay-data="{width:'15%' , align:'center', toolbar: '#barDemo'}">操作</th>
    </tr>
  </thead>
</table>
<script type="text/html" id="titleTpl">

 <div>{{ layui.laytpl.toDateString(d.requiredate,"yyyy-MM-dd")}}</div>
</script>
<script type="text/html" id="barDemo">
 <div id="bjtest" class="" style="width:100%;">
  <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="showFlow">审批</a>
<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="showTu">查看流程图</a>
</div>
</script>

      </div>
  </div>
 
</div>

 <script>
 var table;

 var iis;
layui.use('table', function(){
	
  //监听表格复选框选择
   table = layui.table;
  //查看
  table.on('tool(demo)', function(obj){
   	 var data = obj.data;
	 if(obj.event === 'chakan'){ 	//===========》查看
          var status=obj.data.status;
          var flowType=obj.data.status;

          var type='ck';
          var id = obj.data.id;
          layer.open({
              type: 2,
              title:['查看'] ,
              content: '${re.contextPath}/workBench/findCardOderEdit?lcstatus='+status+'&type='+type+'&id='+id,
              area: ['100%', '95%'],

          });

      }
      if(obj.event === 'showFlow'){  //流程图
          var status=obj.data.status;
          var id=obj.data.id;
		  var processId=obj.data.processId
          var type='ck';
          var id = obj.data.id;
      	var index = layer.open({
            type: 2,
            title:['卡申请'] 
            ,shade: 0
            ,maxmin: true
            ,content: '${re.contextPath}/workflow/showCardSq?id='+id,
            area: ['100%', '100%'],
        }); 
  	   layer.full(index);
    } 
    if(obj.event === 'showTu'){  //流程图
	    var processId=obj.data.processId
        var id = obj.data.id;
	    layer.open({
	          id: 'leave-image',
	          type: 2,
	          area: [ '780px', '400px'],
	          fix: false,
	          maxmin: true,
	          shadeClose: false,
	          shade: 0.4,
	          title: '流程图',
	          content: '${re.contextPath}/leave/shinePics/' + processId
	    });
  	}
      
      if(obj.event === 'edit'){ 	//===========》编辑
		 var type='sq';
		   layer.closeAll();
	    	layer.open({
	            type: 2,
	            title:['编辑'] ,
	            content: '${re.contextPath}/workBench/findCardOderEdit?lcstatus=1&type='+type+'&id='+data.id,
	            area: ['75%', '95%'],
	            
	        });
	    	
	 }
	 if(obj.event === 'editstauts'){ 	//===========》作废
		 
		 var id = obj.data.id;
		 layer.confirm('确定要作废吗！', function(index){
			  $.ajax({
		            url:'${re.contextPath}/workBench/updateDeleStautsById',
		            type: "post",  
		            data: {delestatus:'1',id:id},
		            dataType: "JSON",
		            success: function (dateDas) {
		           	 layer.close(index);
					 	table.reload('idTest',{
				            where:{}
					        ,page: {
					            curr: 1
					        }
				        });
		            },
		            error:function(datas){
		            	layer.msg("异常");
					},
		        });
		 });
	 }
    
  });
  
  
  var $ = layui.$, active = {
    //搜索框条件查询
    selectno:function () {
        var code=$("#code").val();
        var lc_type=$("#lc_type").val();
        table.reload('idTest',{
            where:{code:code,status:lc_type}
	        ,page: {
	            curr: 1
	        }
        });
    }
     //卡申请
    ,selectCardFlow:function (){
    	layer.closeAll();
  	var index = layer.open({
            type: 2,
            title:['卡申请'] 
            ,shade: 0
            ,maxmin: true
            ,content: '${re.contextPath}/workflow/showCardSq',
            area: ['100%', '100%'],
        }); 
  	   layer.full(index);
    }
    //导出
    ,selectExport:function (){
    	layer.msg("导出");
    }
    
  };
  
  $("body").on('click','.layui-btn-container .layui-btn', function () {
  	  var type = $(this).data('type');
	      active[type] ? active[type].call(this) : '';
    })
  //点击搜事件
  $("#selectno").on('click',function(){
	  active.selectno();
  });
 //点击清空按钮
  $("#remove").on('click',function(){
	　//document.getElementById("layui-input").reset();
	　$('.layui-form select').val("");
	　$('.layui-form input').val("");
  });
 
  //卡申请
    $("#selectCardFlow").on('click',function(){
	  active.selectCardFlow();
  });
    layui.use('form', function(){
    	 var form = layui.form;
    	  
    	  getSelect('lc_type',form);
    });
  
});
</script>

</body>
</html>
