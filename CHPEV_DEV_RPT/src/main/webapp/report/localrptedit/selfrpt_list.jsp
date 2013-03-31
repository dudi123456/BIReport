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

<html>
<head>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<title>报表列表</title>
<BIBM:SelfRefreshTag pageNames="frmEdit.qry" attrNames="<%=WebKeys.ATTR_REPORT_QRYSTRUCT%>" warn="1"/>
</head>
<body class="main-body" onLoad="selfDisp();">
<form NAME="frmEdit" ID="frmEdit" ACTION="ReportListView.rptdo?opType=listSelfRpt" method="post">
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
            <td width="5%" nowrap>统计周期：</td>
            <td nowrap><BIBM:TagSelectList listID="S3001" allFlag="" listName="qry__rpt_cycle" /></td>
			<td width="5%" nowrap>报表状态：</td>
            <td nowrap><BIBM:TagSelectList listID="S3002" allFlag="" listName="qry__rpt_status" /></td>
			<td width="5%" nowrap>报表编码：</td>
			<td nowrap><input type="text" size="10" name="qry__rpt_id"></td>
			<!-- <td width="5%" nowrap>本地应用编码：</td>
			<td nowrap><input type="text" size="10" name="qry__rpt_local_res_code"></td> -->
			<td width="5%" nowrap>报表名称：</td>
			<td nowrap><input type="text" size="15" name="qry__rpt_name"></td>
            
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
    <td class="tab-title">报表类型</td>
    <td class="tab-title">统计周期</td>
    <td class="tab-title">报表编码</td>
    <td class="tab-title">报表名称</td>
    <td class="tab-title">创建人</td>
    <td class="tab-title">报表状态</td>
    <td class="tab-title">操作</td>
  </tr>
  <%if(rptTables==null||rptTables.length==0){ %>
  <tr class="table-white-bg">
    <td colspan="7" nowrap align="center">该条件下没有符合要求的数据</td>
  </tr>
  <%}else{ %>
  <%for(int i=0;i<rptTables.length;i++){ %>
  <tr class="table-white-bg">
    <td nowrap><%=rptTables[i].parent_name%></td>
    <td nowrap><%=rptTables[i].cycle_desc%></td>
    <td nowrap><%=rptTables[i].res_id%></td>
	<td><%=rptTables[i].name%></td>
	<td><%=rptTables[i].d_user_id%></td>
    <td nowrap>
    <%if("Y".equals(rptTables[i].status)){ %>
     <font color="green">认证通过</font>
    <%}else if("W".equals(rptTables[i].status)){%>
     <font color="red">待认证</font>
    <%}else{ %>
     不可用
    <%} %>
    </td>
    <td nowrap align="center"><a href="editLocalReport.rptdo?opType=step1&rpt_id=<%=rptTables[i].rpt_id%>">修改</a></td>
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
function fnSubmit(){
  document.frmEdit.submit();
}
</script>
</html>