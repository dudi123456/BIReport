<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.report.domain.RptProcessTable"%>
<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<title>流程维护页面</title>
</head>
<%
//报表信息对象
RptProcessTable pTable = (RptProcessTable)session.getAttribute(WebKeys.ATTR_REPORT_PROCESS);
if (pTable == null){
	pTable = new RptProcessTable();
	pTable.p_id = "0";
}
//流程ID
String p_id = pTable.p_id;
//操作步骤
String rpt_step = (String)session.getAttribute(WebKeys.ATTR_REPORT_STEP);
if (rpt_step == null || "".equals(rpt_step))
	rpt_step = "1";
%>
<body class="main-body">
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
	<tr>
		<td height="5"></td>
	</tr>
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
				,new Array('taber1','基本信息','javascript:tabIframe.location.href="rptProcessStep1.screen?p_id=<%=p_id%>"','')
				,new Array('taber2','审核步骤','javascript:tabIframe.location.href="rptProcessStep2.screen?p_id=<%=p_id%>"','')
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
		<td height="445" valign="top"><iframe src="" id="tabIframe"
			name="tabframe" width="100%" height="100%" scrolling="auto"
			frameborder="0" allowTransparency="true"></iframe> <script
			language="JavaScript">
	buildTaberArea('taber',taberArray[<%=rpt_step%>][0],'taberArray','<%=p_id%>');
	</script></td>
	</tr>
</table>
</body>
</html>
