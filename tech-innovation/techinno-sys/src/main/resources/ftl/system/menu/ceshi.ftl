<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>科技创新管理系统</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <link href="" rel="stylesheet">
    <link rel="shortcut icon" href="${re.contextPath}/plugin/x-admin/images/bizhou.ico" type="image/x-icon" />
    <link rel="stylesheet" href="${re.contextPath}/plugin/plugins/layui/css/layui.css" media="all" />
    <link rel="stylesheet" href="${re.contextPath}/plugin/plugins/font-awesome/css/font-awesome.min.css" media="all" />
    <link rel="stylesheet" href="${re.contextPath}/plugin/build/css/app.css" media="all" />
    <link rel="stylesheet" href="${re.contextPath}/plugin/build/css/themes/default.css" media="all" id="skin" kit-skin />
    <link rel="stylesheet" href="${re.contextPath}/plugin/layuitree/layui/css/layui.css">
    <link rel="stylesheet" href="${re.contextPath}/plugin/lenos/main.css"/>
    <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
    <style>

        <#--前端无聊美化ing-->
        .layui-footer{background-color: #2F4056;}
        .layui-side-scroll{border-right: 3px solid #009688;}
    </style>
</head>

<body class="kit-theme" >
<div class="layui-layout layui-layout-admin kit-layout-admin">
<#macro tree data start end>
    <#if (start=="start")>
    <div class="layui-side layui-nav-tree layui-bg-black kit-side">
    <div class="layui-side-scroll">
    <div class="kit-side-fold"><i class="fa fa-navicon" aria-hidden="true"></i></div>
    <ul class="layui-nav layui-nav-tree" lay-filter="kitNavbar" kit-navbar>
    </#if>
    <#list data as child>
        <#if child.children?size gt 0>
    <li class="layui-nav-item ">
        <a class="" href="javascript:;"><i aria-hidden="true" class="layui-icon">${child.icon}</i><span> ${child.name}</span></a>
        <dl class="layui-nav-child">
    <@tree data=child.children start="" end=""/>
        </dl>
    </li>
        <#else>
    <dd>
        <a href="javascript:;" kit-target data-options="{url:'${child.url}',icon:'${child.icon}',title:'${child.name}',id:'${child.num?c}'}">
            <i class="layui-icon">${child.icon}</i><span> ${child.name}</span></a>
    </dd>
        </#if>
    </#list>
    <#if (end=="end")>
    </ul>
    </div>
    </div>
    </#if>
</#macro>
<@tree data=menu start="start" end="end"/>
    <div class="layui-body" <#--style="border:1px solid red;padding-bottom:0;"--> id="container">
        <!-- 内容主体区域 -->

        <div style="padding: 15px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop">&#xe63e;</i>
            请稍等...
        </div>
    </div>

    <div class="layui-footer">
        <!-- 底部固定区域 -->
        2018 &copy;
        <a target="_blank" href="http://www.gzbizhong.icoc.cc/">比众官网</a> 由Iven Lee根据开源项目打造的一款快速开发脚手架
    </div>
    <script type="text/javascript">
        function open4()

        {
            var diag = new Dialog();

            diag.Width = 300;

            diag.Height = 100;


            diag.Title = "内容页为html代码的窗口";

            diag.InnerHtml='<div style="text-align:center;color:red;font-size:14px;">直接输出html，使用 <b>InnerHtml</b> 属性。</div>'

            diag.OKEvent = function(){diag.close();};//点击确定后调用的方法

            diag.show();

        }

    </script>

    <script src="${re.contextPath}/plugin/plugins/layui/layui.js"></script>
    <script src="${re.contextPath}/plugin/tools/main.js"></script>
</body>

</html>
