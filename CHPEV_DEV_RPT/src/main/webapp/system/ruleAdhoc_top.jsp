<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%
String role_name = request.getParameter("role_name");
%>
<html>

<head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统一经营分析系统</title>
</head>
<body>
<table width="100%">
	<tr>
		<td><img src="../images/common/system/arrow7.gif" width="7"
			height="7"> <b><%=role_name%></b>&nbsp;角色信息</td>
		<TD align="right"></td>
		<td align="left"></TD>
	</tr>
	<tr>
		<td height="1" background="../images/common/system/black-dot.gif" colspan="3"></td>
	</tr>

</table>
</body>

</html>
