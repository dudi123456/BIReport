<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.report.domain.RptColDictTable"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@page import="com.ailk.bi.report.util.ReportConsts"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>

<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
//报表信息对象
RptResourceTable rptTable = (RptResourceTable)session.getAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE);
if(rptTable==null){
	out.print("<center>");
	out.print("<br><br>操作信息丢失！<br>");
	out.print("</center>");
	return;
}
//报表ID
String rpt_id = rptTable.rpt_id;
//操作步骤
String rpt_step = (String)session.getAttribute(WebKeys.ATTR_REPORT_STEP);
if (rpt_step == null || "".equals(rpt_step)){
	rpt_step = "1";
}
//报表列信息对象
RptColDictTable[] reportCols = (RptColDictTable[])session.getAttribute(WebKeys.ATTR_REPORT_COLS_DEFINE);
int colNum = 0;
if(reportCols!=null&&reportCols.length>0){
	colNum = reportCols.length;
}
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/base/commonHtml.jsp"%>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<title>定制报表页面</title>
</head>
<body class="main-body">
<table style="width: 100%;" align="center" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="2">
        <div class="toptag">
            <Tag:Bar />
        </div>
		</td>
	</tr>
</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="1" background="../biimages/black-dot.gif"></td>
	</tr>
	<%if(reportCols!=null&&reportCols.length>0){ %>
	<tr>
		<%if(!StringTool.checkEmptyString(rptTable.rpt_id)&&ReportConsts.NO.equals(rptTable.status)){ %>
		<td align="center" height="25"><font color="red">该报表处于初始化阶段，生成报表后改报表可以进行认证。</font>
		  <input name="create" type="button" class="button2" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" 
            value="生成报表" onclick="window.location.href='editLocalReport.rptdo?opType=change_status&status=<%=ReportConsts.RPT_STATUS_WAIT%>'"> 
        </td>
		<%}else if(ReportConsts.RPT_STATUS_WAIT.equals(rptTable.res_id)&&ReportConsts.RPT_STATUS_WAIT.equals(rptTable.status)){ %>
		<td align="center" height="25"><font color="red">该报表待认证！认证通过后可以正常使用。</font>
		  <input name="accecpt" type="button" class="button2" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" 
            value="认证通过" onclick="window.location.href='editLocalReport.rptdo?opType=change_status&status=<%=ReportConsts.YES%>'"> 
        </td>
        <%}else if(ReportConsts.RPT_STATUS_WAIT.equals(rptTable.status)){ %>
		<td align="center" height="25"><font color="red">该报表待认证！在认证通过前你可以继续修改。</font></td>
		<%} %>
	</tr>
	<%} %>
	<tr>
		<td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="13%" class="body-tabarea"><!--fieldHeadTab标签切换区开始-->
				<span id="taber"></span>
			<script language="JavaScript">
			//定义fieldHeadTab标签数组
			var taberArray=
			new Array(''
				,new Array('taber1','基本属性','javascript:tabIframe.location.href="localRptStep1.screen?rpt_id=<%=rpt_id%>"','')
				,new Array('taber2','选择模版','javascript:tabIframe.location.href="localRptStep2.screen?rpt_id=<%=rpt_id%>"','')
				<%if(ReportConsts.YES.equals(rptTable.metaflag)&&!ReportConsts.YES.equals(rptTable.status)){%>
				,new Array('taber3','选择指标','javascript:tabIframe.location.href="customRptChooseMeasure.screen?rpt_id=<%=rpt_id%>"','')
				,new Array('taber4','选择维度','javascript:tabIframe.location.href="customRptChooseDim.screen?rpt_id=<%=rpt_id%>"','')
				<%}%>
				,new Array('taber5','展示定制','javascript:tabIframe.location.href="localRptStep3.screen?rpt_id=<%=rpt_id%>"','')
				,new Array('taber6','条件定制','javascript:tabIframe.location.href="localRptStep4.screen?rpt_id=<%=rpt_id%>"','')
				,new Array('taber7','预览报表','javascript:tabIframe.location.href="ReportView.rptdo?rpt_id=<%=rpt_id%>&preview=Y"','')
			);
			</script> <!--/fieldHeadTab标签切换区开始--></td>
				<td width="85%" class="body-tabarea">&nbsp;</td>
				<td width="5"><img src="../biimages/tab/field_head_right.gif"
					width="5" height="19"></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="550" valign="top"><iframe src="" id="tabIframe"
			name="tabframe" width="100%" height="100%" scrolling="auto"
			frameborder="0" allowTransparency="true"></iframe> <script
			language="JavaScript">
	buildTaberArea('taber',taberArray[<%=rpt_step%>][0],'taberArray','<%=rpt_id%>','<%=colNum%>');
	</script></td>
	</tr>
</table>
</body>
</html>