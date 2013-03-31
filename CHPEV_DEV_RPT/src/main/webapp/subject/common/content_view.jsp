<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG" %>
<%@ page import="com.ailk.bi.base.struct.*"%>
<%@ page import="com.ailk.bi.base.util.*"%>

<%
String content_id = request.getParameter("content_id"); //获取图形ID
if ("".equals(content_id)){
	out.print("<center>");
	out.print("<br><br>未知内容信息！<br>");
	out.print("</center>");
	return;
}
%>

<html>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/icontent.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>
<body topmargin="0" style="background-color:transparent" style="font-size: 12px">
<TAG:ContentTag contentId="<%=content_id%>"/>
</body>
</html>