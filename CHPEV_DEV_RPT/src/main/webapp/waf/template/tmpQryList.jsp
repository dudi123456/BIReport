<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template" %>

<html>
<%
if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>
<head>
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
<META HTTP-EQUIV="expires" CONTENT="0">
<!-- javascript区域 -->
<template:insert parameter="java_script"/>
</head>
<body class="main-body" onLoad="selfDisp();pageOnLoad();">

<!-- 标题区域 -->
<center>
<span id="theSpanBodyTitle" class="body-title">
<template:insert parameter="title"/>
</span>
</center>

<!--查询frame-->
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td>

<form  name="tableQryForm" action=""  method="POST" >
<input type="hidden" name="next_page">
<input type="hidden" name="oper_type">
<input type="hidden" name="page_value">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td valign="top" width="100%">
<%--查询区域--%>
<template:insert parameter="query_jsp"/>
</td>
</tr>
</table>
</form>

<!--显示-->
<form  name="tableListForm" action="" method="POST">
<input type="hidden" name="next_page">
<input type="hidden" name="oper_type">
<input type="hidden" name="page_value">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td width="100%">
<%--第一显示区域，通常为用户信息--%>
<template:insert parameter="disp_list"/>
</td>
</tr>
</table>
</form>
</td>
</tr>
</table>


</body>
</html>
