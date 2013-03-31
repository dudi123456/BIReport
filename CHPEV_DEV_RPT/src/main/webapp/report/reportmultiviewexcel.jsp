<%@ page contentType="application/vnd.ms-excel;charset=UTF-8"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.common.app.ReflectUtil"%>
<%@page import="com.ailk.bi.common.app.StringB"%>
<%@page import="com.ailk.bi.pages.PagesInfoStruct"%>
<%@page import="com.ailk.bi.pages.WebPageTool"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@page import="com.ailk.bi.report.domain.RptFilterTable"%>
<%@page import="com.ailk.bi.report.util.ReportConsts"%>
<%@page import="com.ailk.bi.report.util.ReportObjUtil"%>
<%@page import="com.ailk.bi.base.struct.LsbiQryStruct"%>
<%@page import="com.ailk.bi.report.struct.MultiColDataStruct"%>

<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
//查询结构
  LsbiQryStruct lsbiQry = (LsbiQryStruct)session.getAttribute(WebKeys.ATTR_LsbiQryStruct);
  if(lsbiQry == null){
	  lsbiQry = new LsbiQryStruct();
  }

MultiColDataStruct[] data = (MultiColDataStruct[])session.getAttribute("MultiColDataStruct_DATA");
if(data == null){
	data = new MultiColDataStruct[0];
}


%>

<HTML>
<HEAD>
<TITLE>指标类多列报表显示</TITLE>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/date/scw.js"></script>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/date/scwM.js"></script>
</HEAD>

<body class="main-body" >
<FORM NAME="frmEdit" ID="frmEdit" method="POST">
<table width="100%" border="0" cellpadding="0" cellspacing="0">  
  
 
  <!--报表显示 start-->
  <tr>
    <td>
    <table id="AutoNumber1" width="100%" border="1" cellpadding="0" cellspacing="0">
      <TR><TD colspan="2" align="center"><%=lsbiQry.obj_name%></TD></TR>
<TR><TD colspan="2">
<TABLE width="100%" ><TR>
<TD nowrap width="5%" align="left"><span class="tab-font">地区：<%=lsbiQry.region_name%></span></TD>
<TD nowrap align="left"></TD>
<TD nowrap align="right"><span class="tab-font">帐期：<%=lsbiQry.gather_mon %></span></TD>
<TD nowrap width="5%" align="left"></TD>
</TR>
</TABLE></td></TR>

    
	  <tr>
		<td colspan="2" class="tab-side2">
		<table width="100%" border="1" cellpadding="0" cellspacing="0" >
	    <TR align="center">
			<TD >行次</TD>
			<TD >统计项目说明</TD>
			<TD >统计值</TD>
			<TD >行次</TD>
			<TD >统计项目说明</TD>
			<TD >统计值</TD>
		</TR>
		<% for(int i=0;data!=null&&i<data.length;i++){ %>
<TR class="table-white-bg">
<TD><%=data[i].getRow_no()%></TD>
<TD><%=data[i].getItem_desc()%></TD>
<TD align="right"><%=data[i].getItem_value()%></TD>
<TD><%=data[i].getRow_no()%></TD>
<TD><%=data[i].getItem_2_desc()%></TD>
<TD align="right"><%=data[i].getItem_2_value()%></TD>
</TR>

<%} %>




        </table>
        </td>
      </tr>
    
      
    </table>
    </td>
  </tr>
 
  
</table>
</FORM>
</body>
</html>