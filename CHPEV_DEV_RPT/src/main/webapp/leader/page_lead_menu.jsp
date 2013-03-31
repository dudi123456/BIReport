<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.ailk.bi.base.util.MenuTreeUtil" %>
<html>
<head>
    <title>new</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/other/newmain.css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/menuUtil.js"></script>
</head>
<body>
<table style="width:100%;height:100%" align="center">
    <tr>
        <td valign="top">
            <table style="width:100%;" align="center">
                <tr>
                    <td valign="top" class="submenutitle">领导关注</td>
                </tr>
            </table>
            <table id="menuTable" style="width:100%; margin-top: 0px" align="center">
                <%=MenuTreeUtil.getTreeHtml("2") %>
            </table>
        </td>
    </tr>
</table>
</body>
</html>