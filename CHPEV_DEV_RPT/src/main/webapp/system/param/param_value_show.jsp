<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.report.struct.*"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>参数值查看</title>
<%
String rootPath = request.getContextPath();
	//列表数据
String[][] list = (String[][])session.getAttribute("viewdata");
	//查询条件
	if(list == null){
	list = new String[0][0];
}

%>

<link href="<%=rootPath%>/css/other/bimain.css" rel="stylesheet" type="text/css">
</head>

<body>

 <form name="form1" action="codeListAdmin.rptdo?opType=viewdata" method="post">
<%=WebPageTool.pageScript("form1","ParamMemoViewData.screen")%>
<% 
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, list.length, 15 );
String init = request.getParameter("init");
if("true".equals(init)) {
	pageInfo.iCurPage=0;	
} 
%>

<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr> 
    <td height="1"></td>
  </tr>
  <tr> 
    <td><table width="99%"  class="datalist" style="width: 100%">
        <tr class="celtitle FixedTitleRow"> 
          <td width="15%">参数编码</td>
          <td width="15%">参数名称</td>
      
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
        </tr>
		  <%} %>
  <%} %>
       
      </table></td>
  </tr>
  <tr> 
    <td height="20" align="right"><%=WebPageTool.pagePolit(pageInfo)%></td>
  </tr>
 <tr> 
    <td height="20" align="center"><INPUT TYPE="button" VALUE="关闭" ONCLICK="window.close();"></td>
  </tr>
</table>
</form>
</body>
</html>
