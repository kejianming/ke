
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>layui</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  
  <link rel="stylesheet" href="${re.contextPath}/plugin/layui/css/layui.css">
  <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/layui2.5/layui.all.js" charset="utf-8"></script>
  <script type="text/javascript" src="${re.contextPath}/plugin/nfdw/js/list.js"></script>
    <script type="text/javascript" src="${re.contextPath}/ftl/workflow/js/show.js"></script>
   
   <style>
   	html,body{
   		height: 100%;
   	}
	table tbody th{
		background-color:rgb(251, 251, 251);
	}
	input[type=radio] {
		display: block;
    margin-top: 5px;
    float: left;
	}
   </style>

</head>

<body>
	<div class="layui-fluid">  
 		<div class="layui-row" style="margin-top: 1%;">
 		 <#if activiti.formPropertyMap??>
                <#assign formPropertyMap=activiti.formPropertyMap>
         </#if>
 		 <div class="layui-col-md9" style="margin-left: 10%">
			<form class="layui-form layui-form-pane" action="">
			 <input type="hidden" name="id" id="ids" value="${((activitiDo.id)?c)!'0'}">
			<input type="hidden" name="status" id="status" value="${activitiDo.status}">
			<input type="hidden" name="type" id="type" >
			<input type="hidden" name="selectId" id="selectId" ><!-- 这步很重要 获取选择流程节点 -->
			<input type="hidden" name="processId" id="processId" value="${(activiti.processId)!''}">
			  <div class="layui-form-item">
			    <label class="layui-form-label"><font color="red">*</font>申请名称</label>
			    <div class="layui-input-block">
			      <input type="text" name="name" value="${activitiDo.name}" lay-verify="required" autocomplete="off" placeholder="请输入名称" class="layui-input">
			    </div>
			  </div>
			  
			 <div class="layui-form-item">
			    <label class="layui-form-label">申请单位</label>
			    <div class="layui-input-block ">
			       <select  name="requireunit" id="region_type" disabled="disabled"> 
			          <option value=""></option>
			        </select>
			    </div>
			  </div>
			  <div class="layui-form-item" id="ydniandu">
			    <label class="layui-form-label"><font color="red">*</font>移动卡数量</label>
			    <div class="layui-input-block">
			      <input type="text" name="cardnum"   placeholder="请输入卡数量" class="layui-input"  value="${activitiDo.cardnum}"/>
			    </div>
			  </div>
			    <div class="layui-form-item" id="ltniandu">
			    <label class="layui-form-label"><font color="red">*</font>联通卡数量</label>
			    <div class="layui-input-block">
			      <input type="text" name="ltcardnum"  placeholder="请输入卡数量" class="layui-input" value="${activitiDo.ltcardnum}"/>
			    </div>
			  </div>
			  
			  <div class="layui-form-item layui-form-text">
			    <label class="layui-form-label">备注</label>
			    <div class="layui-input-block">
			      <textarea placeholder="请输入备注" class="layui-textarea">${activitiDo.remark}</textarea>
			    </div>
			  </div>
			  <div class="layui-form-item" style="text-align: center;">
			      <#if formPropertyMap['save']??>
                      <input type="button" class="layui-btn layui-btn-primary"   lay-submit="" lay-filter="formDemo" value="${formPropertyMap['save'].name}">
                  </#if> 
			       <button class="layui-btn layui-btn-normal" lay-submit="" id="nextss" lay-filter="next" style="display: none;">暂存</button>
			       <input type="button" class="layui-btn layui-btn-normal" lay-submit="" id="nextss" lay-filter="next" style="display: none;" value="暂存">
			       <#if formPropertyMap['next']??>
                      <input type="button" class="layui-btn layui-btn-primary" id="next"  lay-submit="" value="${formPropertyMap['next'].name}">
                  </#if> 
                    <#if formPropertyMap['close']??>
                      <input type="button" class="layui-btn layui-btn-primary" id="close"  lay-submit="" value="${formPropertyMap['close'].name}">
                  </#if>      
                   <#if formPropertyMap['return']??>
                        <input  type="button" class="layui-btn layui-btn-danger" id="return" 
                                lay-submit="" value="${formPropertyMap['return'].name}">
                    </#if>
			     <button class="layui-btn"  onclick="colsedemo();">关闭</button>
			  </div>
			</form>
			  </div>
			</div>
		</div>
		<div id="nextDiv" style="display: none;">
				 
				<script>
				var nextObj = [];
				var nextMap = [];
				var nextparam ={};
				 <#if formPropertyMap['next']??>
			        <#assign nextMap=activiti.nextMap>
			        <#if nextMap??>
			        <#list nextMap?keys as key>
		      	 	 <#assign s=key?string>
			      	 	var returnObjs = {};
			      	 	returnObjs["key"]="${s?substring(0,s?index_of('-'))}";
			      	 	returnObjs["value"]="${s?substring(s?index_of('-')+1,s?length)}";
			      	 	nextObj.push(returnObjs)
		      	 	 <#list nextMap[key] as u>
		      	 	 var returnMaps={}
		      	 		returnMaps["key"]="${s?substring(0,s?index_of('-'))}";
			      	 	returnMaps["name"]="${u.realName}";
			      	 	returnMaps["value"]="${u.id}";
			      	 	nextMap.push(returnMaps);
			      	 	$.extend(nextparam,{key:nextObj});
			            $.extend(nextparam,{value:nextMap});
		      	 	 </#list>
		            </#list>
		           </#if>
				  </#if>				  
				</script>
		</div>
		<div id="returnDiv" style="display: none;">
				 
				<script>
				var returnObj = [];
				var returnMap = [];
				var param ={};	
				 <#if formPropertyMap['return']??>
				    <#assign returnMap=activiti.returnMap>
				    <#if returnMap??>
				      <#list returnMap?keys as key>
		      	 	 <#assign s=key?string>
		      	 	var returnObjs = {};
		      	 	returnObjs["key"]="${s?substring(0,s?index_of('-'))}";
		      	 	returnObjs["value"]="${s?substring(s?index_of('-')+1,s?length)}";
		      	 	returnObj.push(returnObjs)
		      	 	 <#list returnMap[key] as u>
		      	 	 var returnMaps={}
		      	 		returnMaps["key"]="${s?substring(0,s?index_of('-'))}";
			      	 	returnMaps["name"]="${u.realName}";
			      	 	returnMaps["value"]="${u.id}";
			      	 	returnMap.push(returnMaps);
		      	 	 </#list>
		            </#list>
		            $.extend(param,{key:returnObj});
		            $.extend(param,{value:returnMap});
		           </#if>
				  </#if>				  
				</script>
		</div>
	</div>
	<script>
		layui.use('form', function(){
		  var form = layui.form;
		  //监听提交
		  form.on('submit(formDemo)', function(data){
			  data.field.stats="10";
			  data.field.fzrId="";
		 	$.ajax({
		            url:'${re.contextPath}/workflow/addCardWorkInfo',
		            type: "post",  
		            data: data.field,
		            dataType: "JSON",
		            success: function (dateDas) {
						if (dateDas=="sesson") {
							  $("#"+ data.field.type+"Div").html("");//保存完 清除弹出层内容
							colsedemo("idTest");
						}
						
		            },
		            error:function(datas){
		            	layer.msg("申请失败");
					},
		        }); 
		    return false;
		  });
		  form.on('submit(next)', function(data){
			  data.field.fzrId=$("input[type='radio']:checked").val();
			  data.field.returnActiviti=$("#selectId").val();
			  var status=$("#status").val();
			  if (status=='') {
				  data.field.status='2'
			  }
			  if (status=='2') {
				  data.field.status='3'
			  }
			  if (status=='3') {
				  data.field.status='6'
			  }
		 	  $.ajax({
		            url:'${re.contextPath}/workflow/getCardOrderInfo',
		            type: "post",  
		            data: data.field,
		            dataType: "JSON",
		            success: function (dateDas) {
						if (dateDas=="sesson") {
							
							 $("#"+ data.field.type+"Div").html("");//保存完 清除弹出层内容
							 colsedemo("idTest");
						}
						
		            },
		            error:function(datas){
		            	layer.msg("申请失败");
					},
		        }); 
		    return false;
		  });
		  //选择处理人节点
		  getSelectunit('region_type',form,${regionid});
		  $("#next").on('click',function(){
			  
			  showInto("nextDiv",nextparam,form,"next");
		  });
		  $("#return").on('click',function(){
			 showInto("returnDiv",param,form,"return");
		  });
		  $("#close").on('click',function(){
			  test("close");
		  });
		  
		});
		function colsedemo(tableId) {
			var index = parent.layer.getFrameIndex(window.name);  
    		parent.layer.close(index);
    		window.parent.layui.table.reload(tableId); 
		}
		
	</script>
</body>
</html>
