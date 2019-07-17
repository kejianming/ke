//打开 退回或者 下一步的弹出框
function showInto (id,data,form,type) {
	var title="";
	if (type=='next') {
		title="下一步流程";
	}
	if (type=='return') {
		title="回退节点";
	}
	var html= '<form class="layui-form layui-form-pane" action=""> <div class="layui-col-md9" style="margin-top: 1%;margin-left: 10%">';
	html+= ' <div class="layui-form-item">';
	html+= ' <label class="layui-form-label">'+title+'</label>';
	html+= ' <div class="layui-input-block ">';
	html+= ' <select  class="layui-input" name="nextwork" id="reutrnselect" lay-filter="reutrnselect"> ';
	
	html+= ' </select></div></div> </form><div >';
	html+= ' <fieldset class="layui-elem-field">';
	html+= ' <legend>选择处理人</legend>';
	html+= ' <div class="layui-field-box">';
	html+= ' <div class="layui-col-md12" id="reutrnuserId">';
	html+= ' </div></div></fieldset></div><div style="text-align: center;">';
	html+= ' <input type="button" class="layui-btn layui-btn-normal" onclick=test("'+type+'") value="保存">';
	html+= ' <button class="layui-btn layui-layer-close layui-layer-close1" >关闭</button>';
	html+= ' </div>';
	 $("#"+id).html(html);
	 $.each(data.key, function (index, val) {
		 $("#"+id+" #reutrnselect").append('<option value=' +val.key+ '>'+val.value+'</option>');
		 var divhtml='<div class="layui-col-md12" id="'+val.key+'" style="display: none;">';
		 $.each(data.value, function (m, vals) {
			 if (vals.key==val.key) {
				divhtml +='<div   class="layui-btn layui-btn-xs layui-col-md2 layui-btn-primary" style="margin: 5px;padding: 0;height: auto;width: auto;">';
				divhtml +='<label class="layui-col-md12" style="font-size: 20px;padding: 5px;">';
				divhtml +='<input type="radio" style="display: block" name="fzrs" value=' + vals.value + '  flag="1" typeid="undefined">' + vals.name + '</label>';
				divhtml +='</div>';
			 }
		 })
		  divhtml +='</div>';
		  $("#"+id+" #reutrnuserId").append(divhtml);
		  if ($("#"+id+" #reutrnselect").find("option:selected").val()==val.key) {
			  $("#selectId").val(val.key)
				$("#"+val.key).show();
		  }
	  })
      form.on('select(reutrnselect)', function(data){
		  $("#selectId").val(data.value);
		  $("#reutrnuserId").html(111);
			  $("#reutrnselect option").each(function () {
			  var check=$(this).val();
			  var value=data.value;
			  if (value === check) {
				$("#" + value).show();
			  } else {
				$("#" + check).hide();
			  }
            })
  	   }); 
	show(id,form);
}
function show (id,form) {
	 layer.open({
		  type:1,
         title:['查看'] ,
         content: $("#"+id).html(),
         area: ['50%', '50%'],
         success: function(layero, index){
        	 
        	 form.render('select');
         },
         cancel: function(index, layero){ 
        	 
        	 $("#"+id).html("");
        }   

     });
}
function test(type) {
	$("#type").val(type); //设置提交类型
	$("#nextss").trigger("click");
}
