<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.report.struct.*"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"%>

<%
String rootPath = request.getContextPath();
	//列表数据
String[][] list = (String[][])session.getAttribute("BULLETIN_BOARD_LIST");
	//查询条件
	if(list == null){
	list = new String[0][0];
}

	//查询条件
	ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
	if(qryStruct == null){
		qryStruct = new ReportQryStruct();
	}
String info = (String)request.getAttribute("info");

%>
<!DOCTYPE html>
<html>
<head>
<title>参数管理</title>
<%@ include file="/base/commonHtml.jsp"%>
<link href="<%=rootPath%>/css/other/bimain.css" rel="stylesheet" type="text/css">
<link href="<%=rootPath%>/css/tablecss/main.css" rel="stylesheet" type="text/css" />
<link href="<%=rootPath%>/css/other/css.css" rel="stylesheet"  type="text/css">
<SCRIPT language=javascript src="../sysparam/bi_zz.js"></SCRIPT>
</head>

<body>
<div id="maincontent">
<form name="form1" action="codeListAdmin.rptdo?opType=qryinfo" method="post">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="2">
        <div class="toptag">
            <Tag:Bar />
        </div>
		</td>
	</tr>
<%if (info!=null){%>
   <tr>
    <td>
   <font color='red'><%=info%></font>
    </td>
  </tr>
<%}%>
</table>

<%=WebPageTool.pageScript("form1","ParamMemo.screen")%>
<%
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, list.length, 15 );
String init = request.getParameter("init");
if("true".equals(init)) {
	pageInfo.iCurPage=0;
}
%>

<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>
<table width="100%" border="0" cellpadding="5" cellspacing="1" >
  <tr>
    <td class="td-info">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td >参数代码：</td>
            <td ><input name="qryCode" id = "qryCode" type="text"  value="<%=qryStruct.dim1%>" ></td>

            <td >参数名称：</td>
            <td ><input name="qryName" id = "qryName" type="text"  value="<%=qryStruct.dim2%>" ></td>



            <td >
            <input type="button" id="button_search" value="查询" onclick="return doQuery()">
            </td>
          </tr>
        </table>
  </td>
  </tr>
    <tr>
    <td height="1"></td>
  </tr>

  </table>
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	  <tr>
	    <td height="1"></td>
	  </tr>
	  <tr>
	    <td><table width="99%"  class="datalist" style="width: 100%">
	        <tr class="celtitle">
	          <td width="15%">参数编码</td>
	          <td width="15%">参数名称</td>
	          <td >参数类型</td>
	          <td width="15%">操作</td>
	        </tr>
	  <%if(list==null||list.length==0){ %>
	  <tr class="celdata">
	    <td colspan="4" nowrap align="center">该条件下没有符合要求的数据</td>
	  </tr>
	  <%}else{ %>

	  <%for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
			String[] value = list[i+pageInfo.absRowNoCurPage()];
	  %>

	        <tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
	          <td class="leftdata"><%=value[0]%></td>
	          <td class="leftdata"><%=value[1]%></td>
	          <td class="leftdata"><% if(value[2].equals("1")) out.print("select " + value[5] + " from " + value[6] + " where " + value[7] + " " + value[10]); else out.print(value[3] + ":" + value[4]); %></td>
	          <td class="leftdata"><a href="javascript:;" onclick="viewdata('<%=value[0]%>','<%=value[2]%>');">查看值</a>&nbsp;&nbsp;<a href="javascript:;" onclick="refreshmem('<%=value[0]%>','<%=value[2]%>');">刷新内存</a></td>
	        </tr>
			  <%} %>
	  <%} %>

	      </table></td>
	  </tr>
	  <tr>
	    <td height="20" align="right"><%=WebPageTool.pagePolit(pageInfo)%></td>
	  </tr>
	</table>
</form>
</div>
</body>
</html>
<script>
function doQuery() {
	form1.action="codeListAdmin.rptdo?opType=qryinfo";
	form1.page__iCurPage.value="";
	form1.page__iLinesPerPage.value="";
	form1.page__iLines.value="";
	form1.page__iPages.value="";
	form1.page__checkIDs.value="";
	form1.submit();
}
function viewdata(id,type) {
	 var h = "400";
        var w = "690";
        var top = (screen.availHeight - h) / 2;
        var left = (screen.availWidth - w) / 2;
        var optstr = "height=" + h + ",width=" + w + ",left=" + left + ",top=" + top + ",status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no";
        var strUrl = "codeListAdmin.rptdo?opType=viewdata&typeCode=" + id + "&type=" + type;
        newsWin = window.open(strUrl, "bulletList", optstr);
        if (newsWin != null) {
            newsWin.focus();
        }
}

function refreshmem(id,type) {
	form1.action="codeListAdmin.rptdo?opType=refreshmem&typeCode=" + id + "&type=" + type;
	form1.submit();
}
</script>