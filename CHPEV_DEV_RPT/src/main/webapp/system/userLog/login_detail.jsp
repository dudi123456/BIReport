<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import = "java.io.PrintWriter" %>
<%@ page import = "com.ailk.bi.base.util.*" %>
<%@ page import = "com.ailk.bi.common.dbtools.*" %>
<%@ page import = "com.ailk.bi.report.struct.ReportQryStruct" %>
<%@ page import="com.ailk.bi.common.app.AppException"%>  
<%@ page import="com.ailk.bi.base.common.*"%>  

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Content-Language" content="zh-cn">

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/newmain.css">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<script language=javascript src="<%=request.getContextPath()%>/js/date/scw.js"></script>
<script language=javascript src="<%=request.getContextPath()%>/js/date/scwM.js"></script>
 

<title><%=CSysCode.SYS_TITLE%></title>


<%

ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
if(qryStruct == null){
	qryStruct = new ReportQryStruct();
}
qryStruct.dim2 = request.getParameter("log_seq");
%>


<BIBM:SelfRefreshTag pageNames="TableQryForm.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1"/>
<body>
</head>
<%
int screenx = Integer.parseInt(session.getAttribute(WebKeys.Screenx)==null?"1280":(String)session.getAttribute(WebKeys.Screenx));
int chartWidthTmp = (screenx-250)/2;

%>

<FORM name="TableQryForm" action="leaderVwSubject.rptdo">
<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">

<table style="width: 100%;"  id="content_1" cellspacing="0" cellpaddingx="0">
	<tr>	
	<td >
		<table  cellspacing="0" cellpaddingx="0" style="width: 100%;">			
			<tr valign="top" >
				<td align="left" colspan="2"><iframe name="table_login_rank_03" id="table_login_rank_03" width="100%" height="300"
							src="SubjectCommTable.rptdo?table_id=login_rank_03&first=Y&table_height=300"
					frameborder="0" scrolling="no"></iframe>
				</td>
			</tr>
		</table>
	</td>
	</tr>		
	<tr></tr>

</table>

</FORM>
</body>
</html>
