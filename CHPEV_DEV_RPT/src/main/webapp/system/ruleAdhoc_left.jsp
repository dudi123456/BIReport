<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.system.common.LSInfoAdhocRole"%>
<%@ page import="com.ailk.bi.system.facade.impl.CommonFacade"%>
<%
String role_id = request.getParameter("role_id");
String user_id = CommonFacade.getLoginId(session);
String[][] info = LSInfoAdhocRole.getAdhocInfo(user_id);
%>
<html>

<head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统一经营分析系统</title>
</head>
<body bgcolor="#02449B" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="99%" border="0" align="center" cellpadding="0" cellspacing="0">
<%for(int i=0;info !=null && i<info.length;i++) { %>
<tr>
<td class="leftbg-tree"><img src="../images/common/system/sign_title.gif" border='0'>&nbsp;<a href="ruleAdhoc_main.jsp?role_id=<%=role_id %>&adhoc_id=<%=info[i][0] %>" target="ruleAdhoc_mainFrame"><span class=".fig-re"><%=info[i][1] %></span></a></td>
</tr>
<%} %>
</table>
<table width="99%" height="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td class="leftbg-tree">&nbsp;</td>
</tr>
</table>
</body>

</html>
