<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%
if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统一经营分析系统</title>
</head>

<frameset cols="150,*" frameborder="no" border="0" framespacing="4" name="bodyFrameset"> 
  <frame src="param_left.jsp" name="paramtree" scrolling="no">
  <frame src="param_info.jsp" name="paramview" scrolling="yes">
</frameset>
<noframes>
<body>
</body>
</noframes> 
</html>


