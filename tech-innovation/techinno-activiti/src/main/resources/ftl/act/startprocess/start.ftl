<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>起草申请</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <link rel="stylesheet" href="${re.contextPath}/plugin/layui/css/layui.css">
    <link rel="stylesheet" href="${re.contextPath}/plugin/lenos/main.css"/>
    <script type="text/javascript" src="${re.contextPath}/plugin/jquery/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="${re.contextPath}/plugin/layui/layui.all.js"
            charset="utf-8"></script>
    <script type="text/javascript" src="${re.contextPath}/plugin/tools/tool.js" charset="utf-8"></script>



    <style>
        .layui-icon{font-size: 100px;}
        .site-doc-icon {
            margin-bottom: 50px;
            font-size: 0;
        }
        .site-doc-icon li{
            display: inline-block;
            vertical-align: middle;
            width: 127px;
            line-height: 25px;
            padding: 20px 0;
            margin-right: -1px;
            margin-bottom: -1px;
            border: 1px solid #e2e2e2;
            font-size: 14px;
            text-align: center;
            color: #666;
            transition: all .3s;
            -webkit-transition: all .3s;
        }

        ul li{
            float: right;
            margin-right: 50px;
        }
    </style>
</head>

<body>

<div class="layui-row layui-col-space10">
    <div class="layui-col-md2">

    </div>
    <div class="layui-col-md8">
        <ul style="float: left;margin-top: 50px">
            <li>
                <a href="${re.contextPath}/leave/showLeave">
                    <i class="layui-icon"></i>
                    <div style="padding-left: 20%" class="code">申请请假</div>
                </a>

            </li>

            <li>
                <a href="${re.contextPath}/leave/showLeave">
                    <i class="layui-icon"></i>
                    <div style="padding-left: 20%" class="code">申请采购</div>
                </a>

            </li>

        </ul>




    </div>
    <div class="layui-col-md2">

    </div>
</div>





</body>

</html>
