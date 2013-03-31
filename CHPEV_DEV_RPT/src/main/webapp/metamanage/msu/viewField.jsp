<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.metamanage.dao.impl.SourceAnalyseDaoImpl" %>
<%
String table_id = request.getParameter("table_id");
String[][] info = SourceAnalyseDaoImpl.getTableField(table_id);
%>
<html xmlns:v="urn:schemas-microsoft-com:vml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache, must-revalidate"/>
    <meta http-equiv="expires" content="0"/>
<TITLE><%=info[0][0] %>(<%=info[0][1] %>)表字段信息</TITLE>
<link href="<%=request.getContextPath()%>/css/other/bimain.css" rel="stylesheet" type="text/css" />
</head>

<BODY>
<table style="width: 100%"  class="datalist">
<tr class="celtitle">
	<td>英文字段名</td>
	<td>中文字段名</td>
</tr>
<%
for(int i=0;info!=null && i<info.length;i++) { %>
<tr class="celdata">
	<td class="leftdata"><%=info[i][2] %></td>
	<td class="leftdata"><%=info[i][3] %></td>
</tr>
<%} %>
</table>
</BODY>
</HTML>
