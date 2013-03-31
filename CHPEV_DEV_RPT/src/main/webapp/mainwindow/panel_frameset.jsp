<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.base.table.*"%>
<%
String menu_id = CommTool.getParameterGB(request , "menuId");

	//scrolling="no"
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<frameset cols="190,*" frameborder="no" border="0" framespacing="0" name="bodyFrameset">
  <frame src="panel_left.jsp?menuId=<%=request.getParameter("menuId")%>" id="leftFrame" name="leftFrame" scrolling="no">
  <frame src="panel_main.jsp" id="bodyFrame" name="bodyFrame" frameborder="no" border="0" >
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>
