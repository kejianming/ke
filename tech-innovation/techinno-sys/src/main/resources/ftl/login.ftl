
<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <title>后台登录</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />

    <link rel="shortcut icon" href="${re.contextPath}/plugin/x-admin/images/shejiyuan.ico" type="image/x-icon" />
    <link rel="stylesheet" href="${re.contextPath}/plugin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${re.contextPath}/plugin/x-admin/css/font.css">
    <link rel="stylesheet" href="${re.contextPath}/plugin/x-admin/css/xadmin.css">
    <script type="text/javascript" src="${re.contextPath}/plugin/layui/layui.all.js"></script>
    <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="${re.contextPath}/plugin/nfdw/js/base64char.js"></script>

</head>
<body class="login-bg">

<div class="login">
    <div class="message">科技创新管理系统</div>
    <div id="darkbannerwrap"></div>

    <form method="post" action="${re.contextPath}/login" class="layui-form" >
        <input  placeholder="用户名" id="username" autocomplete="off"  type="text" lay-verify="username" class="layui-input" >
        <input name="username" id="usernames" type="hidden" class="layui-input" >
        <hr class="hr15">
        <input  lay-verify="password" id="password" placeholder="密码" autocomplete="off"  type="password" class="layui-input">
          <input name="password" id="passwords" type="hidden" class="layui-input" >
        <hr class="hr15">
        <div  class="layui-inline">
            <label class="layui-form-label" style="width:40px;padding: 9px 0px;">验证码&nbsp;</label>
               <div class="layui-input-inline">
                     <input type="text" name="code" style="width:150px;height:35px;" autocomplete="off" lay-verify="code"   class="layui-input">
              </div>
            <div class="layui-input-inline">
                <img src="" id="code">
            </div>

        </div>
        <#--<div>-->
        <#--<label class="layui-form-label" style="width:40px;padding: 9px 0px;">记住我</label>  由于不好验证 目前去掉-->
            <#--<input type="checkbox"   name="rememberMe" lay-skin="switch" lay-filter="switchTest" lay-text="ON|OFF">-->
        <#--</div>-->
        <hr class="hr15">
        <input value="登录" lay-submit lay-filter="login" style="width:100%;" type="submit">
        <hr class="hr20" >
    </form>
</div>


<script>
  var layer;
  $(function  () {
    layui.use(['form','layer'], function(){
      var form = layui.form;
      form.verify({
        username:function(v){
          var username = encode64($("#username").val());  //对数据加密
          $("#usernames").val(username);
          if(v.trim()==''){
            return "用户名不能为空";
          }
        }
        ,password:function(v){
       	  var password = encode64($("#password").val());
          $("#passwords").val(password);
          if(v.trim()==''){
            return "密码不能为空";
          }
        },code:function(v){
              if(v.trim()==''){
                  return '验证码不能为空';
              }
          }
      });

      form.render();
    });
    layer = layui.layer;
    var msg='${message}';
    if(msg.trim()!=""){
        layer.msg(msg, {icon: 5,anim:6,offset: 't'});
    }
      $("#code").click(function(){
          var url = "${re.contextPath}/getCode?"+new Date().getTime();
          this.src = url;
      }).click().show();
    $('#code').on('mouseover',function(){
        layer.tips('点击刷新验证码', this,{time:1000});
    });
  });

	if (window != top) {
		var url = location.href;
		top.location.href = HTMLEnCode(url);
	}
  	
	/* XSS防御过滤 */
	function HTMLEnCode(str) {
		var s = "";
		if (str.length == 0) {
			 return "";
		}
		str.replace(/&/g, "&gt;");
		s.replace(/</g, "");
		s.replace(/>/g, "");
		s.replace(/ /g, "");
		s.replace(/\"/g, "");
		s.replace(/\'/g, "");
		s.replace(/\n/g, "");
		s.replace(/\//g, "");
		s.replace(/\(/g, "");
		s.replace(/\)/g, "");
		s.replace(/\=/g, "");
	
		return s;
			
	}
  
  
</script>


<!-- 底部结束 -->
</body>
</html>
