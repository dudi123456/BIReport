<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag"%>
<%
String context = request.getContextPath();
String resId = request.getParameter("res_id");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>联通统一经营分析系统</title>

<link rel="stylesheet" href="<%=context%>/css/css.css" type="text/css">
<link rel="stylesheet" href="<%=context%>/css/tab_css.css"
	type="text/css">
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<Tag:Bar defaultValue="<%=resId%>" target="parent"/>
		<!-- 此处要显示导航条 -->
	</tr>
</table>
</body>
</html>
