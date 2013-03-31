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

<script type="text/JavaScript" src="<%=rootPath%>/js/date/scwM.js"></script>
<script type="text/JavaScript" src="<%=rootPath%>/js/date/scw.js"></script>
<script type='text/javascript' src='<%=rootPath%>/js/date/YMDTIME.js'> </script>
<script type="text/javascript" src="<%=rootPath%>/js/prototype.lite.js"></script>

<script lanaguage="javascript">
	function viewBrowserLogDetail(userid) {
		document.form1.sessionid.value=userid;
		form1.action="userLogStat.rptdo?opType=browserdetail";
		document.form1.submit();
		
	}

 </script>

<body class="main-body">
<form name="form1" action="userLogStat.rptdo?opType=logindetail" method="post">
<input type="hidden" name="sessionid" value="">
<%=WebPageTool.pageScript("form1","logindetail.screen")%>
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
							<td width="15%">登录时间</td>
							<td width="15%">离开时间</td>
							<td width="15%">登录IP</td>
							<td width="25%">访问详情</td>
						</tr>
						  <%if(list==null||list.length==0){ %>
  <tr class="celdata" align='center'>
    <td colspan="6"  class="leftdata">该条件下没有符合要求的数据</td>
  </tr>
  <%}else{ %>

  <%for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
		String[] value = list[i+pageInfo.absRowNoCurPage()];
  %>  

						<tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
							<td class="leftdata"><%=value[0]%></td>
							<td><%=value[1]%></td>
							<td><%=value[2]%></td>
							<td><%=value[3]%></td>
							<td><%=value[4]%></td>
							<td class="centerdata"><a href="javascript:;" onclick="javascript:viewBrowserLogDetail('<%=value[5]%>')">该登录访问情况分析</a></td>
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