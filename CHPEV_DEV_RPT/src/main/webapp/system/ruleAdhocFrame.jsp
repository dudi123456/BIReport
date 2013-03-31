<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%
String role_id = request.getParameter("role_id");
String role_name = request.getParameter("role_name");
%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统一经营分析系统</title>
</head>

<frameset rows="25,*" cols="*" frameborder="no" border="0" framespacing="0" >
	<frame src="ruleAdhoc_top.jsp?role_id=<%=role_id %>&role_name=<%=role_name%>" name="top" id="top" scrolling="no" noresize>
  	<frameset name="middle" id="middle" rows="*" cols="150,*" framespacing="0" frameborder="no" border="0">
	  <frame src="ruleAdhoc_left.jsp?role_id=<%=role_id %>" name="ruleAdhoc_leftFrame" scrolling="no">
	  <frame src="" name="ruleAdhoc_mainFrame" scrolling="yes">
  	</frameset>
</frameset>
<noframes>

<body>

</body>


</noframes>
</html>
