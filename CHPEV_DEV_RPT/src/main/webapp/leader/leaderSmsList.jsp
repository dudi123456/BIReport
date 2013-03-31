<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>

<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.common.app.*"%>
<%@ page import="com.ailk.bi.report.struct.*"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%
String rootPath = request.getContextPath();
	//列表数据
String[][] list = (String[][])session.getAttribute("VIEW_TREE_LIST");
	//查询条件
	if(list == null){
	list = new String[0][0];
}

	//查询条件
	ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
	if(qryStruct == null){
		qryStruct = new ReportQryStruct();
	}


%>

<link href="<%=rootPath%>/css/tablecss/main.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=rootPath%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/css/other/tab_css.css" type="text/css">
<SCRIPT language=javascript src="<%=rootPath%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=javascript src="<%=rootPath%>/js/js.js"></SCRIPT>
<script language=javascript src="<%=rootPath%>/js/date/scw.js"></script>
<script language=javascript src="<%=rootPath%>/js/date/scwM.js"></script>

<body class="main-body">
<form name="form1" action="leaderSMSView.rptdo" method="post">
<%=WebPageTool.pageScript("form1","leaderSmsList.screen")%>
<% 
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, list.length,5 );
String init = StringB.NulltoBlank((String)request.getAttribute("init"));

if("true".equals(init)) {
	pageInfo.iCurPage=0;	
} 
%>

<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>

<table style="width: 99%;" align="center" cellspacing="0" cellpadding="0">
	<tr>
		<td class="toolbg">起始日期:
            <input type="text" size="10" name="startDate" id="startDate" value="<%=qryStruct.dim1%>" readonly onClick="scwShow(this,this);" class="input-text" onFocus="switchClass(this)" size="15" 
									onBlur="switchClass(this)">结束日期:
            <input type="text" size="10" name="endDate" id="endDate" value="<%=qryStruct.dim2%>" readonly onClick="scwShow(this,this);"
									class="input-text" onFocus="switchClass(this)"
									onBlur="switchClass(this)"  size="15">
			<input type="submit" id="button_search" value="查询" ><input type="reset" id="button_reset" value="重置" ></td>
		<td class="toolbg" align="right">
		</td>
	</tr>
</table>
<table style="width: 100%" height="325" class="kuangContent" cellspacing="0" cellpadding="0">
					<tr>
						<td valign="top" style="padding: 10px;">
						<table style="width: 100%">
							<tr>
								<td><font class="title1">短信详情</font></td>
							</tr>
							 <%if(list==null||list.length==0){ 
									}else{
										  for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
		String[] value = list[i+pageInfo.absRowNoCurPage()];
  %>  


							<tr>
								<td>
								<hr noshade="noshade" style="height: 1px; color: #caddeb">
								</td>
							</tr>
							<tr>
								<td><font class="newTitle">日期:<%=value[0]%></font></td>
							</tr>
							<tr>
								<td>
								<div style="width: 100%; height: 100px; overflow-y: auto;">
									<%=value[1]%>
								</div>
								</td>
							</tr>		
								 <%} }%>

							
						</table>
						</td>
					</tr>
					<tr>
					<td><%=WebPageTool.pagePolit(pageInfo,rootPath)%>
					</td>
					</tr>
				</table>

		</form>		

</body>
</html>