<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报表定制页面</title>
<%
String parent_id=request.getParameter("parent_id");
String isCustom = request.getParameter("isCustom");
if(isCustom==null||"".equals(isCustom)){
	isCustom = "N";
}
%>
</head>
<frameset cols="160,*" frameborder="no" border="0" framespacing="4" name="bodyFrameset"> 
  <frame src="custom_left.jsp?parent_id=<%=parent_id%>&isCustom=<%=isCustom%>" name="leftFrame" scrolling="no">
  <frame src="" id="bodyFrame" name="bodyFrame" scrolling="no">
</frameset>
<noframes>
<body>
</body>
</noframes> 
</html>