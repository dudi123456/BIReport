<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>

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

	//查询条件
	ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
	if(qryStruct == null){
		qryStruct = new ReportQryStruct();
	}


%>
<link href="/BIWeb/css/other/bimain.css" rel="stylesheet" type="text/css" />
<link href="<%=rootPath%>/css/tablecss/main.css" rel="stylesheet" type="text/css" />

<SCRIPT language=javascript src="<%=rootPath%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=javascript src="<%=rootPath%>/js/js.js"></SCRIPT>
<script language=javascript src="<%=rootPath%>/js/date/scw.js"></script>
<script language=javascript src="<%=rootPath%>/js/date/scwM.js"></script>

<body class="main-body">
<form name="form1" action="userLogStat.rptdo?opType=qryftpinfo" method="post">
<%=WebPageTool.pageScript("form1","ftpdownload_dtl.screen")%>
<% 
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, list.length,30 );
String init = (String)request.getAttribute("init");
if("true".equals(init)) {
	pageInfo.iCurPage=0;	
} 
%>

<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>
<table style="width: 99%;" align="center" cellspacing="0" cellpadding="0">
 <tr>
            <td>
            <Tag:Bar />
            </td>
        </tr>

	<tr>
		<td class="toolbg">起始日期:
            <input type="text" size="10" name="qryStartDate" id="qryStartDate" value="<%=qryStruct.dim1%>" readonly onClick="scwShow(this,this);"
									class="input-text" onFocus="switchClass(this)"
									onBlur="switchClass(this)">结束日期:
            <input type="text" size="10" name="qryEndDate" id="qryEndDate" value="<%=qryStruct.dim2%>" readonly onClick="scwShow(this,this);"
									class="input-text" onFocus="switchClass(this)"
									onBlur="switchClass(this)">
			<input type="submit" id="button_search" value="查询" ><input type="reset" id="button_reset" value="重置" ></td>
		<td class="toolbg" align="right">
		</td>
	</tr>
</table>
<table style="width: 99%;" height="100%" align="center">
	<tr>
		<td valign="top" width="50%" style="padding-right:5px;">
		<!--日指标分析开始-->
		<table style="width: 100%">
			<tr>
				<td class="titlebg2" width="151"><font class="title1">爱乐FTP下载查询</font></td>
				<td align="right" class="titlebg2_line"><%=WebPageTool.pagePolit(pageInfo,rootPath)%></td>
			</tr></table>
			<table style="width: 100%">
			<tr>
				<td colspan="2">
					<table style="width: 100%"  class="datalist">
						<tr class="celtitle">
							<td class="leftdata" width="15%">文件名</td>
							<td width="15%">文件属性</td>
							<td width="15%">下载日期</td>
							<td width="15%">下载状态</td>
							
						</tr>
						  <%if(list==null||list.length==0){ %>
  <tr class="table-white-bg">
    <td colspan="4" nowrap  class="leftdata">该条件下没有符合要求的数据</td>
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