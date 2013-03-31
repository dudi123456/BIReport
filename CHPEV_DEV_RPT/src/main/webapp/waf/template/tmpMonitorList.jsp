<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template" %>
<html>
<head>
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
<META HTTP-EQUIV="expires" CONTENT="0">  
<!-- javascript区域 -->
<template:insert parameter="java_script"/>
</head>

<body class="main-body" onLoad="pageOnLoad();">
<form  name="tableQryForm" action=""  method="POST">
<input type="hidden" name="next_page">
<input type="hidden" name="page_value">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td valign="top" width="100%">
<template:insert parameter="query_jsp"/>
</td>
</tr>
</table>
</form>
</body>
</html>
