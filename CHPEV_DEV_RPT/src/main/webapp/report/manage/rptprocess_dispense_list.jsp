<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@page import="java.util.List"%>
<%@page import="com.ailk.bi.base.util.SQLGenator"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.report.domain.RptProcessTable"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>

<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
//当前审核流程信息
RptProcessTable pTable = (RptProcessTable)session.getAttribute(WebKeys.ATTR_REPORT_PROCESS);
//报表审核流程关系
List listRptProcess = (List)session.getAttribute(WebKeys.ATTR_REPORT_PROCESS_RELT);
//查询条件
ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT);
if(qryStruct==null){
	qryStruct = new ReportQryStruct();
}
//报表类型下拉列表值
String strSql = SQLGenator.genSQL("Q3172");
%>

<html>
<head>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<title>审核流程发布</title>
<BIBM:SelfRefreshTag pageNames="myform.qry" attrNames="<%=WebKeys.ATTR_REPORT_QRYSTRUCT%>" warn="1"/>
</head>
<body class="main-body" onLoad="selfDisp();">
<form NAME="myform" ID="myform" ACTION="rptProcess.rptdo?opType=listRptProcess&p_id=<%=pTable.p_id%>&p_flag_name=<%=pTable.p_flag_name%>" method="post">
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
            <td width="5%" nowrap>报表类型：</td>
            <td nowrap>
            <BIBM:TagSelectList listID="0" allFlag="" listName="qry__rpt_kind" selfSQL="<%=strSql%>" /></td>

            <td width="5%" nowrap>统计周期：</td>
            <td nowrap>
            <BIBM:TagSelectList listID="S3001" allFlag="" listName="qry__rpt_cycle" /></td>			
			
			<td width="5%" nowrap>报表名称：</td>
			<td nowrap><input type="text" size="20" name="qry__rpt_name" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
            
            <td width="5%">
             <input type="button" name="search" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="查询" onclick="_selSubmit();"> 
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
  <tr>
    <td height="5">当前选中的审核流程为：<%=pTable.p_flag_name%></td>
  </tr>
  <!--报表显示 start-->
  <tr>
  <td class="tab-side2">
<TABLE width="100%" border="0" cellpadding="0" cellspacing="1" class="table-bg">
  <tr align="center">
  	<td class="tab-title" nowrap>报表类型</td>
  	<td class="tab-title" nowrap>报表周期</td>
    <td class="tab-title" nowrap>报表名称</td>
    <td class="tab-title" nowrap>流程名称</td>
    <td class="tab-title" nowrap>
    <input id="SelectAll" type='checkbox' name="SelectAll" value="" onclick="_selectAll();" >全选</td>
  </tr>
  <%
  for(int i=0;listRptProcess!=null&&i<listRptProcess.size();i++){
	  RptProcessTable rptProcess = (RptProcessTable)listRptProcess.get(i);
  %>
  <tr class="table-white-bg"> 
    <td><%=rptProcess.parent_name%></td>
    <td><%=rptProcess.cycle_desc%></td>
    <td><%=rptProcess.rpt_name%></td>          
    <td><%=rptProcess.p_flag_name%></td>  
    <td align="center"><input type="checkbox" name="rptSel" value="<%=rptProcess.rpt_id%>" <%if(rptProcess.p_id.equals(pTable.p_id)){ out.println("checked=true"); }%> onclick="_init();"></td>
  </tr>
  <%}%>
</TABLE>
    </td>
  </tr>
  <!--报表显示 end-->
</table>
<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" class="table-bg">
  <tr class="table-white-bg" align="center">
    <td align="center" colspan="4" height="30"> 
      <input type="button" name="Submit3"  class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" onclick="_saveSubmit();" value="保存"> 
      <input type="reset" name="Submit22"  class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="取消">
    </td>
  </tr>
</TABLE>
<INPUT TYPE="hidden" id="opSubmit" name="opSubmit" value="" />
</form>
</body>
<script language="javascript">
var arr = new Array();
_init();
function _init(){
    var selAll = true;
	var obj = document.myform.elements.tags("input");
	for(var i = 0; i < obj.length; i++){
		if(obj[i].name == "rptSel"){
			arr[obj[i].value/1] = obj[i].checked;
			if(selAll){
				if(!obj[i].checked){
					selAll = false;
				}
			}
		}
	}
	document.all.SelectAll.checked = selAll;
}
function _selectAll(){
	var obj = document.myform.elements.tags("input")
	if (document.all.SelectAll.checked){
		for (i=0; i < obj.length; i++){
			var e = obj[i];
			if(e.name == "rptSel" && !e.disabled){
    			e.checked = true;
    		}
		}
	}else{
		for (i=0; i < obj.length; i++){
			var e =obj[i];
			if(e.name == "rptSel"){
				e.checked = false;
    		}
		}
	}
}

function _selSubmit(){
	document.myform.submit();
}
function _saveSubmit(){
	document.myform.opSubmit.value = "save";
	document.myform.submit();
}
</script>
</html>