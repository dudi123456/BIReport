<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="com.ailk.bi.system.action.FavoriteHTMLAction"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>收藏夹</title>
</head>
<frameset cols="180,*" frameborder="no" border="0" framespacing="4" name="bodyFrameset">
  <frame src="favorite_left.jsp" name="leftFrame" scrolling="no">
  <frame src="<%=FavoriteHTMLAction.getDefaultUrl(session)%>" id="bodyFrame" name="bodyFrame" scrolling="auto">
</frameset>
<noframes>
<body>
</body>
</noframes> 
</html>