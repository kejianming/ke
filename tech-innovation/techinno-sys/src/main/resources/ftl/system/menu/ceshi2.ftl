<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title></title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <link href="" rel="stylesheet">
    <link rel="stylesheet" href="${re.contextPath}/plugin/layuitree/layui/css/layui.css">
    <link rel="stylesheet" href="${re.contextPath}/plugin/lenos/main.css"/>
    <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
    <script src="${re.contextPath}/plugin/plugins/layui/layui.js"></script>
</head>
<body>
<div id="navBarId" class="layui-side layui-bg-black">
</div>

<div class="layui-body">
    <!-- 内容主体区域 -->
    <div style="padding: 15px;">
        <table class="layui-hide layui-col-md12" id="test"></table>
    </div>
</div>

<div class="layui-footer">
    <!-- 底部固定区域 -->
</div>
</div>

<script src="layui/layui.js" charset="utf-8"></script>
</body>
<script>
    // 导航菜单的间隔像素
    var menuCell = 5;

    layui.use('element', function(){
        var element = layui.element;
        var $ = layui.jquery;

        $.ajax({
            url:"menu/leftmenu",
            type:"get",
            dataType:"json",
            data:{},
            success:function(data){
                console.log("data: "+data);
                var liStr= "";
                // 遍历生成主菜单
                for( var i = 0; i <data.length; i++){
                    //console.log("--> "+JSON.stringify(data[i]));
                    // 判断是否存在子菜单
                    if(data[i].childMenus!=null&&data[i].childMenus.length>0){
                        console.log("--> "+JSON.stringify(data[i].childMenus));
                        liStr+="<li class=\"layui-nav-item\"><a class=\"\" href=\"javascript:;\">"+data[i].name+"</a>\n"+
                                "<dl class=\"layui-nav-child\">\n";
                        // 遍历获取子菜单
                        for( var k = 0; k <data[i].childMenus.length; k++){
                            liStr+=getChildMenu(data[i].childMenus[k],0);
                        }
                        liStr+="</dl></li>";
                    }else{
                        liStr+="<li class=\"layui-nav-item\"><a class=\"\" href=\""+data[i].url+"\">"+data[i].name+"</a></li>";
                    }
                };
                console.log(">>>> "+liStr);
                $("#navBarId").html("<ul class=\"layui-nav layui-nav-tree\"  lay-filter=\"test\">\n" +liStr+"</ul>");
                element.init();
            }
        });

    });

    // 递归生成子菜单
    function getChildMenu(subMenu,num) {
        console.log("num: "+num);
        num++;
        var subStr = "";
        if(subMenu.childMenus!=null&&subMenu.childMenus.length>0){
            subStr+="<dd><ul><li class=\"layui-nav-item\" ><a style=\"margin-Left:"+num*menuCell+"px\" class=\"\" href=\"javascript:;\">"+subMenu.name+"</a>" +
                    "<dl class=\"layui-nav-child\">\n";
            for( var j = 0; j <subMenu.childMenus.length; j++){
                subStr+=getChildMenu(subMenu.childMenus[j],num);
            }
            subStr+="</dl></li></ul></dd>";
        }else{
            subStr+="<dd><a style=\"margin-Left:"+num*menuCell+"px\" href=\""+subMenu.url+"\">"+subMenu.name+"</a></dd>";
        }
        return subStr;
    }
</script>
</body>
</html>

</html>