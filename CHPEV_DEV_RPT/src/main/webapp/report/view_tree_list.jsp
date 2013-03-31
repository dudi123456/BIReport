<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="java.util.*"%>
<%@page import="com.ailk.bi.base.struct.InfoResStruct"%>
<%@page import="com.ailk.bi.common.app.StringB"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"
%>

<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
//列表数据
String[][] list = (String[][])session.getAttribute("VIEW_TREE_LIST");
String resType = (String)request.getSession().getAttribute("res_type");
String resId = (String)request.getSession().getAttribute("res_id");
String rptId = StringB.nvl((String)request.getSession().getAttribute("rpt_id"));
String rptName = StringB.nvl((String)request.getSession().getAttribute("rpt_name"));			
if(list == null){
	list = new String[0][0];
}
request.getSession().setAttribute("res_type", resType);
request.getSession().setAttribute("res_id", resId);
request.getSession().setAttribute("rpt_id", rptId);
request.getSession().setAttribute("rpt_name", rptName);
%>

<html>
<head>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<title>报表列表</title>
<style > 
.t1 {background-color:#D2E9FF
	height: 20px;
	padding-left: 5px;
	padding-right: 5px;
	padding-top: 3px
}
.t2 {background-color:#ECF5FF;
	height: 20px;
	padding-left: 5px;
	padding-right: 5px;
	padding-top: 3px
}
</style > 
</head>
<body class="main-body">
<form NAME="viewListForm" ACTION="" method="post">
 <!--显示script部分-->
<%=WebPageTool.pageScript("viewListForm","viewTreeList.screen")%>
<% 
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, list.length, 50 );
String init = request.getParameter("init");
if("true".equals(init)) {
	pageInfo.iCurPage=0;	
} 
%>

<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>


<table width="100%" border="0" cellpadding="0" cellspacing="0">
<input type="hidden" name="res_id" value="<%=resId %>">
<input type="hidden" name="res_type" value="<%=resType %>">
    
	<tr>
		<td nowrap class="report-desc" align="right">资源编号：<input type="text" name="rpt_id" value="<%=rptId %>" class="input-text" ></td>
		<td nowrap  class="report-desc" align="center">
			资源名称：<input type="text" name="rpt_name" value="<%=rptName %>" class="input-text" >
			<input type="button" name="query" value="查询" onclick="doQuery()" class="button">
		</td>
	</tr>
	<tr>
		<td height="5" colspan="2"></td>
	</tr>
	<tr>
		<td height="1"  colspan="2" background="../biimages/black-dot.gif"></td>
	</tr>
		<tr>
		<td height="5" colspan="2"></td>
	</tr>
</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	
	
	<tr>
		<td height="5"></td>
	</tr>

  <!--报表显示 start-->
  <tr>
  <td class="tab-side2">
<TABLE id="table1" width="100%" border="1" cellpadding="0" cellspacing="1" style="border-collapse: collapse" bordercolor="#999999">
  <tr align="center">
    <td class="tab-title">报表编码</td>
    <td class="tab-title">报表类型</td>
    <td class="tab-title">报表名称</td>
  </tr>
  <%if(list==null||list.length==0){ %>
  <tr class="table-white-bg">
    <td colspan="3" nowrap align="center">该条件下没有符合要求的数据</td>
  </tr>
  <%}else{ %>

  <%for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
		String[] value = list[i+pageInfo.absRowNoCurPage()];
  %>
  <tr class="table-white-bg" >
    <td nowrap><%=value[0]%></td>
    <td nowrap><%=value[1]%></td>
    <td nowrap><a href=<%=value[3]%>><%=value[2]%></a></td>
  </tr>
  <%} %>
  <%} %>
</TABLE>
    </td>
  </tr>
  <tr>
	<td><%=WebPageTool.pagePolit(pageInfo)%></td>
</tr>
  <!--报表显示 end-->
</table>
</form>
</body>
<script Language="Javascript">
for (i=0;i<table1.rows.length;i++) {
(i%2==0)?(table1.rows(i).className = "t1"):(table1.rows(i).className = "t2");
}

var oPrevRow;
//选中表格行
function rowSelected( oRow ){

	if(oPrevRow){  
		oPrevRow.style.backgroundColor='';
		oPrevRow.runtimeStyle.color='';
	}
	
	oRow.style.backgroundColor='#B8B8DC';
	oPrevRow = oRow;
}

function doQuery() {
	viewListForm.action="viewTreeRpt.rptdo";
	viewListForm.page__iCurPage.value="";
	viewListForm.page__iLinesPerPage.value="";
	viewListForm.page__iLines.value="";
	viewListForm.page__iPages.value="";
	viewListForm.page__checkIDs.value="";
	viewListForm.submit();
}

function goUrl(url) {
	document.location.href=url;
}
</script>

</html>