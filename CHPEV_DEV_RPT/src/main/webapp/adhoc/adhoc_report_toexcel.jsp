<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.adhoc.util.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<table border="1">
<tr>
<%=AdhocHelper.getSumTableHTML(session) %>
</tr>
</table>

<table width="100%"  border="0" cellspacing="0" cellpadding="0">
      <tr id="list">
        <td >
 		<%=AdhocHelper.getReportStyleTable(session)%>
        </td>
      </tr>
</table>
</body>
</html>