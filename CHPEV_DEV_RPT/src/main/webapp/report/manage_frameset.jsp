<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报表管理</title>
</head>
<frameset cols="150,*" frameborder="no" border="0" framespacing="4" name="bodyFrameset">
  <frame src="manage_left.jsp?type=<%=request.getParameter("type")%>" name="leftFrame" scrolling="no">
  <frame src="" id="bodyFrame" name="bodyFrame" scrolling="yes">
</frameset>
<noframes>
<body>
</body>
</noframes> 
</html>