
function getSelect(key, form, selected){
	var keyId = "#" + key;
	
	//获取路径
	var pathName=window.document.location.pathname;
	//截取，得到项目名称
	var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	$.post(projectName+'/dictionary/selectDictionary',{dgroup:key},function(info){
		if (null == info || '' == info) {
			return;
		}
		info = JSON.parse(info)
	
	/*	if (key == 'region_type') {
			var aaa=$(keyId).parent("div").parent("div");
			aaa.attr("id",key+"s");
			if (info.length <= 1) {
				$("#"+key+"s").hide();
			}
		}*/
		$(keyId+" option:gt(0)").remove();
		
		for(var i = 0; i < info.length; i++){
			if(info[i].code==selected){
				$(keyId).append("<option selected='selected' value='"+info[i].code+"' >"+info[i].dvalue+"</option>");
			}else{
				$(keyId).append("<option value='"+info[i].code+"'>"+info[i].dvalue+"</option>");
			}	
		}
		
		// 重新渲染表单下拉框
		form.render('select');
	})
}
function getSelectunit(key, form,selected){
	var keyId = "#"+key;
	var pathName=window.document.location.pathname;
	//截取，得到项目名称
	var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	$.post(projectName+'/dictionary/selectDictionary',{dgroup:key},function(info){
		info = JSON.parse(info)
		$(keyId+" option:gt(0)").remove();
		for(var i = 0; i < info.length; i++){
			if(info[i].code==selected){
				$(keyId).append("<option selected='selected' value='"+info[i].code+"' >"+info[i].dvalue+"局</option>");
			}else{
				$(keyId).append("<option value='"+info[i].code+"'>"+info[i].dvalue+"局</option>");
			}	
		}
		// 重新渲染表单下拉框
		form.render('select');
	})
}
function setAutoWidth() {
	var classid= $('.layui-table th:last').find("div").attr("class");
	var array=classid.split(" ");
//	var tablelenth=$(".setAutoWidth th").length-1;
	$("."+array[1]).css({'width':$(".autoWidthDiv").width()+25+'px','padding': '0px'})
}
