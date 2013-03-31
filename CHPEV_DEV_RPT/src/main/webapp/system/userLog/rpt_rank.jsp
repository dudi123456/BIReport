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
<%@ include file="/base/commonHtml.jsp"%>
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
%>
<BIBM:SelfRefreshTag pageNames="TableQryForm.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1"/>


<body>
<%
int screenx = Integer.parseInt(session.getAttribute(WebKeys.Screenx)==null?"1280":(String)session.getAttribute(WebKeys.Screenx));
int chartWidthTmp = (screenx-250)/2;

%>
<script>
function checkInfo()
{
	var startDate = document.getElementById("qryStartDate").value;
	var endDate = document.getElementById("qryEndDate").value;

	if(startDate!= null && endDate!= null)
		{
			if(startDate > endDate)
				{
					alert("起始日期不能超过结束日期，请重新选择！");
					return false;
				}
		}
}

function resetValue()
{
	var date = new Date();
	var year = date.getFullYear();
	var mon = date.getMonth()+1;
	var day = date.getDate();
	mon = checkDate(mon);
	day = checkDate(day);
	var time = year+""+mon+""+day;

	document.getElementById("qryStartDate").value=time;
	document.getElementById("qryEndDate").value=time;
}

function checkDate(i)
{
	if(i < 10)
		{
			i = "0" + i;
		}
	return i;
}
</script>


<FORM name="TableQryForm" action="leaderVwSubject.rptdo" method="post">
<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">

<table style="width: 100%;" align="center" cellspacing="0" cellpadding="0">
	<tr class="toolbg">
		<td colspan="2">
        <div class="toptag">
            <Tag:Bar />
        </div>
		</td>
	</tr>
	<tr>
		<td class="toolbg">起始日期:
            <input type="text" size="10" id="qryStartDate" value="<%=qryStruct.gather_day%>" name="qry__gather_day" readonly onClick="scwShow(this,this);"
									class="input-text" onFocus="switchClass(this)"
									onBlur="switchClass(this)">结束日期:
            <input type="text" size="10" id="qryEndDate" name="qry__gather_day_e" value="<%=qryStruct.gather_day_e%>" readonly onClick="scwShow(this,this);"
									class="input-text" onFocus="switchClass(this)"
									onBlur="switchClass(this)">
			<input type="submit" id="button_submit" value="查询" onclick="checkInfo()">
			<input type="button" id="button_reset" value="重置" onclick="resetValue()">
		</td>
		<td class="toolbg" align="right">
		</td>
	</tr>
</table>
<table style="width: 100%;"  id="content_1" cellspacing="0" cellpaddingx="0">
	<tr>
	<td >
		<table  cellspacing="0" cellpaddingx="0" style="width: 100%;">
			<tr valign="top" >
				<td align="left" colspan="2"><iframe name="table_rpt_rank_01" id="table_rpt_rank_01" width="100%" height="300"
							src="SubjectCommTable.rptdo?table_id=rpt_rank_01&first=Y&table_height=300"
					frameborder="0" scrolling="no"></iframe>
				</td>
			</tr>
		</table>
	</td>
	</tr>
	<tr>
	<td>
		<table  cellspacing="0" cellpaddingx="0" style="width: 100%;">
			<tr valign="top" >
				<td align="left" colspan="2"><iframe id="chart_rpt_rank_01_C_1"
						scrolling="no" width="100%" height="230" border="0" frameborder="0"
						marginwidth="0" marginheight="0" src="SubjectCommChart.screen?chart_id=rpt_rank_01_C_1&flag=Y"></iframe></td>
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
