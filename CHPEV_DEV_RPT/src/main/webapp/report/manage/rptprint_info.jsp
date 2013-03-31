<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>

<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
//报表列表
RptResourceTable[] rptTables = (RptResourceTable[])session.getAttribute(WebKeys.ATTR_REPORT_TABLES);
//查询条件
ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT);
if(qryStruct==null){
	qryStruct = new ReportQryStruct();
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/date/scw.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<title>报表打印查询</title>
<BIBM:SelfRefreshTag pageNames="frmEdit.qry" attrNames="<%=WebKeys.ATTR_REPORT_QRYSTRUCT%>" warn="1"/>
</head>
<body class="main-body" onLoad="selfDisp();">
<form NAME="frmEdit" ID="frmEdit" ACTION="rptPrint.rptdo?opType=view" method="post">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="1" background="../biimages/black-dot.gif"></td>
	</tr>
	<tr>
		<td height="2"></td>
	</tr>
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
            <td width="5%" nowrap>打印码：</td>
            <td nowrap><input type="text" size="20" name="qry__print_code" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>

            <td width="5%" nowrap>打印时间：</td>
            <td nowrap>
            <input type="text" size="10" name="qry__date_s" readonly onClick="scwShow(this,this);" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)">
            <img src="../biimages/report/clear.gif" onclick="clearDate();" title="清空打印时间"/>
            </td>
			
			<td width="5%" nowrap>报表名称：</td>
            <td nowrap><input type="text" size="10" name="qry__rpt_name" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
			
			<td width="5%" nowrap>操作用户：</td>
			<td nowrap><input type="text" size="10" name="qry__oper_no" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
            
            <td width="10%">
             <input type="button" name="search" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="查询" onclick="javascript:fnSubmit();"> 
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
  <td class="tab-side2">
<TABLE width="100%" border="0" cellpadding="0" cellspacing="1" class="table-bg">
  <tr align="center">
    <td class="tab-title">打印码</td>
    <td class="tab-title">打印时间</td>
    <td class="tab-title">报表名称</td>
    <td class="tab-title">操作用户</td>
  </tr>
  <%if(rptTables==null||rptTables.length==0){ %>
  <tr class="table-white-bg">
    <td colspan="4" nowrap align="center">该条件下没有符合要求的数据</td>
  </tr>
  <%}else{ %>
  <%for(int i=0;i<rptTables.length;i++){ %>
  <tr class="table-white-bg">
    <td nowrap><%=rptTables[i].print_code%></td>
    <td nowrap><%=rptTables[i].print_time%></td>
    <td nowrap><%=rptTables[i].name%></td>
    <td nowrap><%=rptTables[i].print_username%></td>
  </tr>
  <%} %>
  <%} %>
</TABLE>
    </td>
  </tr>
  <!--报表显示 end-->
</table>
</form>
</body>
<script language=javascript>
function clearDate(){
	document.frmEdit.qry__date_s.value="";
}
function fnSubmit(){
  document.frmEdit.action="rptPrint.rptdo?opType=view";
  document.frmEdit.submit();
}
</script>
</html>