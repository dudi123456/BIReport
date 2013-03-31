<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.base.util.CommTool"%>
<%@ page import="com.ailk.bi.adhoc.util.*"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"%>
<%
String rootPath = request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据预览</title>
<link rel="stylesheet" href="<%=rootPath%>/css/other/css.css"
	type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/css/other/kw.css" type="text/css">	

<link href="<%=rootPath%>/css/tablecss/main.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=rootPath%>/css/other/tab_css.css" type="text/css">

</head>
<script language="JavaScript">
<!--
	
	function MM_reloadPage(init) {  //reloads the window if Nav4 resized
	  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
	    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
	  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
	}
	MM_reloadPage(true);

function cancelClose(){
		
		this.close();
	}
	

</script>

<body>
<center>
<%

String[][] objList = (String[][])session.getAttribute("OBJ_LIST");
if(objList == null){
	objList = new String[0][0];
}
String strRcnt = (String)session.getAttribute("RECORD_CNT");
%>
<form name="tableQryForm" method="post" action="AdhocUserUpLoadCon.rptdo?oper_type=viewdata">

<%=WebPageTool.pageScript("tableQryForm","AdhocUserViewUpData.screen")%>
<% 
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, objList.length, 30 );
String init = request.getParameter("init");
if("true".equals(init)) {
	pageInfo.iCurPage=0;	
} 
%>

<%=WebPageTool.pageHidden(pageInfo)%>

<table width="90%" border="0" cellpadding="0" cellspacing="0">
<tr>
						<td colspan="3" ><table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
<td width="32"></td>
<td width="100%" class="left-menu">数据预览【总记录数:<%=strRcnt%>&nbsp;只提供部分数据浏览】</td>
<td width="5"></td>
</tr>
    
    </table></td>
					
					</tr>

					<tr>
						<td colspan="3" class="search-bg">
						<table width="100%" border="0" cellpadding="0" cellspacing="1"
							class="choice-table-bg">
							<tr class="table-gay-bg2">								
								<td align="left" class="subject-title" width="100%">数据值</td>
							</tr>
							 <%if(objList==null||objList.length==0){ %>
  <tr class="table-white-bg">
    <td colspan="5" nowrap align="center"><font color='red'>没有数据</font></td>
  </tr>
  <%}else{ 
	    for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
		String[] value = objList[i+pageInfo.absRowNoCurPage()];

	  %>
  
								<tr class="table-white-bg">
								
						<td width="100%" align="left"><%=value[0]%></td>

	
								
							 </tr>
								
							<%}}%>
						</table>
						</td>
						</tr>
						

						
						</table>
						
						<br>
						<table width="90%" border="0" cellpadding="0" cellspacing="0">
<tr align="left">
						<td align="left" nowrap><%=WebPageTool.pagePolit(pageInfo,request.getContextPath())%></td>
						</tr>
<tr align="center">
						<td><input type="button" name="close" value="关闭"
							class="button-add" onclick="javascript:cancelClose();"></td>
						</tr>
</table>
</form>		
</center>				
</BODY>

</html>
