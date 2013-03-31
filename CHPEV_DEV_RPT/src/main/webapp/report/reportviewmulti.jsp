<%@page contentType="text/html; charset=UTF-8" %>
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
<%@page import="com.ailk.bi.base.util.*"%>
<%@page import="com.ailk.bi.report.util.ReportObjUtil"%>
<%@page import="com.ailk.bi.base.struct.LsbiQryStruct"%>
<%@page import="com.ailk.bi.base.struct.UserCtlRegionStruct"%>
<%@page import="com.ailk.bi.report.struct.MultiColDataStruct"%>


<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
//
UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
if(ctlStruct == null){
	ctlStruct = new UserCtlRegionStruct();
}
String sql="select distinct city_id, city_desc from bi_mid.d_city";
if(!"".equals(ctlStruct.ctl_city_str_add)){
	 sql += " where city_id = "+ctlStruct.ctl_city_str_add;
}
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
<TITLE>经营分析系统</TITLE>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/date/scw.js"></script>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/date/scwM.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<script language="JavaScript" type="text/JavaScript">
//页面登陆
function pageOnLoad(){

}
//
function _fnSubmit(){
  document.frmEdit.action="ReportViewMul.rptdo?rpt_id=<%=lsbiQry.obj_id%>";
  document.frmEdit.submit();
}
//

function _exportSubmit(){
  window.open("../report/reportmultiviewexcel.jsp");
}

</script>
<%--
设置回显参数
--%>
<%
String attNames=WebKeys.ATTR_LsbiQryStruct;
%>
<BIBM:SelfRefreshTag pageNames = "frmEdit.qry"
 attrNames = "<%=attNames%>"
 warn = "1"
/>

</HEAD>

<body class="main-body" onLoad="pageOnLoad()">
<FORM NAME="frmEdit" ID="frmEdit"  method="POST">
<table width="100%" border="0" cellpadding="0" cellspacing="0">  
  <!--条件区展示 start-->
  <tr>
    <td>
    <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" class="squareB" >
      <tr> 
        <td><img src="../biimages/square_corner_1.gif" width="5" height="5"></td>
        <td background="../biimages/square_line_1.gif"></td>
        <td><img src="../biimages/square_corner_2.gif" width="5" height="5"></td>
      </tr>
      <tr>
        <td background="../biimages/square_line_2.gif"></td>
        <td width="100%" height="100%" valign="top">
        <table width="100%" border="0">
          <tr>
          <td align="right" width="10%">帐期月份</td>
		  <td nowrap>
		  	<input type="text" size="10" name="qry__gather_mon" readonly onClick="scwShowM(this,this);" class="input-text" value="<%=lsbiQry.gather_mon%>"  onFocus="switchClass(this)" onBlur="switchClass(this)">
		  </td>

            <td width="5%" nowrap>地域：</td>
            <% if("".equals(ctlStruct.ctl_city_str_add)){ %>
            <td><BIBM:TagSelectList listName="qry__city_id" allFlag="999" focusID="<%=lsbiQry.city_id%>"
							listID="0"
							selfSQL="<%=sql%>" /></td>
			<%}else{ 			
			%>
			 <td><BIBM:TagSelectList listName="qry__city_id"  focusID="<%=lsbiQry.city_id%>"
							listID="0"
							selfSQL="<%=sql%>" /></td>
			<%} %>
            
            <td colspan="2" width="30%" align="center" nowrap>
             <input type="button" name="search" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="查询" onclick="javascript:_fnSubmit();"> 
             <input type="button" name="dc" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="导出" onclick="javascript:_exportSubmit();"> 
            </td>
          </tr>
        </table>
        </td>
        <td background="../biimages/square_line_3.gif"></td>
      </tr>
      <tr> 
        <td height="6"><img src="../biimages/square_corner_3.gif" width="5" height="5"></td>
        <td background="../biimages/square_line_4.gif"></td>
        <td><img src="../biimages/square_corner_4.gif" width="5" height="5"></td>
      </tr>
    </table>
    </td>
  </tr>
  <!--条件区展示 end-->
  <tr>
    <td height="5"></td>
  </tr>
  <!--报表显示 start-->
  <tr>
    <td>
    <table id="AutoNumber1" width="100%" border="0" cellpadding="0" cellspacing="0">
      <TR><TD colspan="2" class="tab-boldtitle"><%=lsbiQry.obj_name%></TD></TR>
<TR><TD colspan="2">
<TABLE width="100%"><TR>
<TD nowrap width="5%" align="left"><span class="tab-font">地区：<%=lsbiQry.region_name%></span></TD>
<TD nowrap align="left"></TD>
<TD nowrap align="right"><span class="tab-font">帐期：<%=lsbiQry.gather_mon %></span></TD>
<TD nowrap width="5%" align="left"></TD>
</TR>
</TABLE></td></TR>

    
	  <tr>
		<td colspan="2" class="tab-side2">
		<table width="100%" border="1" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#999999">
	    <TR align="center">
			<TD class="tab-title">行次</TD>
			<TD class="tab-title">统计项目说明</TD>
			<TD class="tab-title">统计值</TD>
			<TD class="tab-title">行次</TD>
			<TD class="tab-title">统计项目说明</TD>
			<TD class="tab-title">统计值</TD>
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
  <!--报表显示 end-->
  <tr>
    <td height="5"></td>
  </tr>
   <tr>
    <td>
    <%=CommTool.getEditorHtml(lsbiQry.obj_id,"0")%>
    </td>
  </tr>
</table>
<INPUT TYPE="hidden" id="qry__first_view" name="qry__first_view" value="">
</FORM>
</body>
</html>