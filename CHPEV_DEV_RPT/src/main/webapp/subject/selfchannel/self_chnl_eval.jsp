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

<!DOCTYPE html>
<html>
<head>
<%@ include file="/base/commonHtml.jsp"%>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<title>自有实体渠道评价</title>
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
				,new Array('taber1','地域总览','javascript:tabIframe.location.href="<%=context%>/subject/SelfChnlEvl.rptdo?optype=eval_by_area"','')
				,new Array('taber2','级别总览','javascript:tabIframe.location.href="<%=context%>/subject/SelfChnlEvl.rptdo?optype=eval_by_level"','')
				,new Array('taber3','渠道明细','javascript:tabIframe.location.href="<%=context%>/subject/SelfChnlEvl.rptdo?optype=chnl_detail"','')
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
			frameborder="0" allowTransparency="true"></iframe>
		 
		 <script language="JavaScript"> 
		 	buildTaberArea('taber',taberArray[1][0],'taberArray','7777','');
		</script>
		
		</td>
	</tr>
</table>
</body>
</html>