<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>BI</title>
<%
String parent_id=request.getParameter("parent_id");
%>
</head>
<frameset cols="170,*" frameborder="no" border="0" framespacing="4" name="bodyFrameset"> 
  <frame src="custom_left.jsp?parent_id=<%=parent_id%>" name="leftFrame" scrolling="no">
  <frame src="" id="bodyFrame" name="bodyFrame" scrolling="auto">
</frameset>
<noframes>
<body>
</body>
</noframes> 
</html>