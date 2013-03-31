<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.subject.admin.SubjectCommonConst"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.ailk.bi.subject.admin.entity.UiPubInfoCondition"%>

<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
//报表列表
List listInfo = (ArrayList)request.getAttribute(SubjectCommonConst.CONST_SUBJECT_TABLE_DEF);
String table_id = (String)request.getAttribute("table_id");
String table_name = (String)request.getAttribute("table_name");

%>

<html>
<script language=javascript>

function _drillTableDefSet(rpt_id){
  var optstr = "height=600,width=800,left=0,top=0,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no";
  var strUrl = "SubjectCommonTblDef.rptdo?opt_type=listDrillset&table_id=" + rpt_id;
  newsWin = window.open(strUrl,"editRptHead",optstr);
  if(newsWin!=null){
    newsWin.focus();
  }
}

function _editRptTableDefDrill(rpt_id,rowid){
  var optstr = "height=400,width=800,left=0,top=0,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no";
  var strUrl = "SubjectCommonTblDef.rptdo?opt_type=edtTblCondition&table_id=" + rpt_id + "&rowid=" + rowid;
  newsWin = window.open(strUrl,"editRptHead",optstr);
  if(newsWin!=null){
    newsWin.focus();
  }
}

function addNewInfo(rpt_id){
  var optstr = "height=400,width=800,left=0,top=0,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no";
  var strUrl = "SubjectCommonTblDef.rptdo?opt_type=addTblCondition&table_id=" + rpt_id;
  
 newsWin = window.open(strUrl,"editRptHead",optstr);
 //newsWin = showModalDialog(strUrl,"editRptHead",optstr);
  if(newsWin!=null){
    newsWin.focus();
  }
}

function fnSubmit(){
  document.frmEdit.submit();
}

function _delete(rpt_id,rowid){
 if(confirm("您确定要删除吗？此操作不可恢复!")){
 	
 	var strUrl = "SubjectCommonTblDef.rptdo?opt_type=doDelCommTblCondition&table_id=" + rpt_id + "&rowid=" + rowid;
 	document.frmEdit.action=strUrl;
	document.frmEdit.submit();
}
}
</script>

<head>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<title>报表列表</title>
<BIBM:SelfRefreshTag pageNames="frmEdit.qry" attrNames="<%=WebKeys.ATTR_REPORT_QRYSTRUCT%>" warn="1"/>
</head>
<body class="main-body" onLoad="selfDisp();">
<form NAME="frmEdit" ID="frmEdit" ACTION="SubjectCommonTblDef.rptdo?opt_type=list" method="post">
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
		<td height="1" background="../images/black-dot.gif"></td>
	</tr>
	<tr>
		<td height="2"></td>
	</tr>
	<!--条件区展示 start-->
  <tr>
    <td>
    <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" class="squareB" >
      <tr> 
        <td><img src="../images/square_corner_1.gif" width="5" height="5"></td>
        <td background="../images/square_line_1.gif"></td>
        <td><img src="../images/square_corner_2.gif" width="5" height="5"></td>
      </tr>
      <tr> 
        <td background="../images/square_line_2.gif"></td>
        <td width="100%" height="100%" valign="top">
        <table width="100%" border="0">
          <tr>
            <td>
             <input type="button" name="addnew" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="新增" onclick="javascript:addNewInfo('<%=table_id%>');"> 
             <input type="button" name="addnew" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="返回" onclick="javascript:history.go(-1);"> 
             &nbsp;&nbsp;&nbsp;&nbsp;为<font color='red'><%=table_id%>:<%=table_name%></font>&nbsp;&nbsp;定义列显示规则
            </td>
          </tr>
				  
        </table>
        </td>
        <td background="../images/square_line_3.gif"></td>
      </tr>
      <tr> 
        <td height="6"><img src="../images/square_corner_3.gif" width="5" height="5"></td>
        <td background="../images/square_line_4.gif"></td>
        <td><img src="../images/square_corner_4.gif" width="5" height="5"></td>
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
    <td class="tab-title" width="10%">查询代码</td>
    <td class="tab-title"  width="10%">条件代码</td>
	<td class="tab-title" width="10%">数据类型</td>
    <td class="tab-title"  width="10%">显示顺序</td>
    <td class="tab-title" width="10%">状态</td>
    <td class="tab-title">操作</td>
  </tr>
  <%if(listInfo==null||listInfo.size()==0){ %>
  <tr class="table-white-bg">
    <td colspan="7" nowrap align="center">该条件下没有符合要求的数据</td>
  </tr>
  <%}else{ 
  for(int i=0;i<listInfo.size();i++){ 
  	UiPubInfoCondition obj = (UiPubInfoCondition)listInfo.get(i);
	  String strDataType = "数值型";
	  if (obj.getDataType().equals("2")){
		strDataType = "字符型";
	  }
	  %>
  <tr class="table-white-bg">
  <td nowrap><%=obj.getQryCode()%></td>
	<td nowrap><%=obj.getConCode()%></td>
  <td nowrap><%=strDataType%></td>
	<td nowrap><%=obj.getSequence()%></td>

	<td nowrap><%=obj.getStatus()%></td>

  <td nowrap><a href="javascript:;" onclick="javascript:_editRptTableDefDrill('<%=table_id%>','<%=obj.getRowId()%>');">修改</a>&nbsp;&nbsp;<a href="javascript:;" onclick="javascript:_delete('<%=table_id%>','<%=obj.getRowId()%>');">删除</a></td>
  
</tr>
  <%} %>
  <%} %>
</TABLE>
    </td>
  </tr>
  <!--报表显示 end-->
</table>
</form>
<span style="display:none" id="report_msu_memo"></span>
</body>
</html>