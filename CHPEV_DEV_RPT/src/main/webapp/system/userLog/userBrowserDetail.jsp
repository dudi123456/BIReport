<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>

<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.common.app.FormatUtil"%>
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


%>

<link href="/BIWeb/css/other/bimain.css" rel="stylesheet" type="text/css" />
<link href="<%=rootPath%>/css/tablecss/main.css" rel="stylesheet" type="text/css" />

<SCRIPT language=javascript src="<%=rootPath%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=javascript src="<%=rootPath%>/js/js.js"></SCRIPT>
<script language=javascript src="<%=rootPath%>/js/date/scw.js"></script>
<script language=javascript src="<%=rootPath%>/js/date/scwM.js"></script>

<script lanaguage="javascript">
	function viewBrowserLogDetail(userid) {
		document.form1.sessionid.value=userid;
		form1.action="userLogStat.rptdo?opType=browserdetail";
		document.form1.submit();
		
	}

 </script>

<body class="main-body">
<form name="form1" action="userLogStat.rptdo?opType=browserdetail" method="post">
<input type="hidden" name="sessionid" value="">
<%=WebPageTool.pageScript("form1","browserdetail.screen")%>
<% 
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, list.length, 30 );
String init = request.getParameter("init");
if("true".equals(init)) {
	pageInfo.iCurPage=0;	
} 
%>

<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>
<table style="width: 99%;" height="100%" align="center">
	<tr>
		<td valign="top" width="50%" style="padding-right:5px;">
		<!--日指标分析开始-->
		<table style="width: 100%">
			<tr>
				<td class="titlebg2" width="151"><font class="title1">登录详情</font></td>
				<td align="right" class="titlebg2_line"><%=WebPageTool.pagePolit(pageInfo,rootPath)%></td>
			</tr></table>
			<table style="width: 100%">
			<tr>
				<td colspan="2">
					<table style="width: 100%"  class="datalist">
						<tr class="celtitle">
							<td class="leftdata" width="15%">登录名</td>
							<td width="15%">用户名</td>
							<td width="20%">登录时间</td>
							<td width="30%">操作介绍</td>
							<td width="20%">登录IP</td>
							
						</tr>
						  <%if(list==null||list.length==0){ %>
  <tr class="celdata" align='center'>
    <td colspan="5"  class="leftdata" >该条件下没有符合要求的数据</td>
  </tr>
  <%}else{ %>

  <%for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
		String[] value = list[i+pageInfo.absRowNoCurPage()];
  %>  

						<tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
							<td class="leftdata"><%=value[0]%></td>
							<td><%=value[1]%></td>
							<td><%=value[2]%></td>
							<td>访问:<%=value[6]%></td>
							<td><%=value[3]%></td>
			
						</tr>
						 <%} %>
  <%} %>

						
					</table>
				</td>
			</tr>
		</table>

			</td>
			</tr>
			</table>
		</form>		

</body>
</html>