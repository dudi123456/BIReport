<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.ailk.bi.report.domain.RptProcessTable"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<HTML>
<%
if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>
<%
RptProcessTable[] pTables = (RptProcessTable[])session.getAttribute(WebKeys.ATTR_REPORT_PROCESSES);
%>
<HEAD>
<TITLE>流程发布</TITLE>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<script language="JavaScript" src="<%=request.getContextPath()%>/js/js.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
<body>
<table width="100%">
  <tr>
    <td class="query-input-label"><img src="../biimages/process_icon.gif" width="16" height="18"></td>
    <td class="blackbold"><a href="javascript:;" target="info_frame">审核流程</a></td>
  </tr>
  <%for(int i=0;pTables!=null&&i<pTables.length;i++){ %>
  <tr>
    <td>&nbsp;</td>
    <td nowrap><img src="../biimages/process_icon2.gif" width="13" height="11"> <a href="rptProcess.rptdo?opType=listRptProcess&p_id=<%=pTables[i].p_id%>&p_flag_name=<%=pTables[i].p_flag_name%>" target="custview"><%=pTables[i].p_flag_name%></a></td>
  </tr>
  <%} %>
</table>
</body>
</html>