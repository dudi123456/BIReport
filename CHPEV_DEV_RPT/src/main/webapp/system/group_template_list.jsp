<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@page import="com.ailk.bi.base.util.SQLGenator"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>

<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
//报表列表
String[][] value = (String[][])session.getAttribute(WebKeys.ATTR_GROUP_TEMPLATE_LIST);
if(value == null){
	value = new String[0][0];
}

//
String group_id = (String)session.getAttribute("group_id");
if(group_id == null){
	group_id = "1";
}

String group_name = "";
if(null==group_name){
	group_name = "";
}
%>
<html>
<head>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<title></title>
</head>
<body class="main-body" >
<form NAME="frmEdit" ID="frmEdit" ACTION="TemplateListView.rptdo" method="post">
<input type="hidden"  name="group_id" value="<%=group_id%>">
<input type="hidden"  name="oper_type" value="add">

<table width="100%" border="0" cellpadding="0" cellspacing="0">

  <tr>
    <td height="5"><font color="blue">当前选中的用户组为：</font><font color="red"><%=group_name%></font></td>
  </tr>
  	
	<tr>
		<td height="1" background="../images/black-dot.gif"></td>
	</tr>
	<tr>
		<td height="5" ></td>
	</tr>
  <!--报表显示 start-->
  <tr>
  <td class="tab-side2">
<TABLE width="100%" border="0" cellpadding="0" cellspacing="1" class="table-bg">
  <tr align="center">
    <td class="tab-title">模板</td>  
    <td class="tab-title">选择
    <input id="SelectAll" type='checkbox' name="SelectAll" value="" onclick="_selectAll();" >全选</td>
  </tr>
  <%if(value==null||value.length==0){ %>
  <tr class="table-white-bg">
    <td colspan="7" nowrap align="center">该条件下没有符合要求的数据</td>
  </tr>
  <%}else{ %>
  <%for(int i=0;i<value.length;i++){ %>
  <tr class="table-white-bg">
    <td nowrap><%=value[i][1]%></td>
    <td align="center"><input type="checkbox" name="group_list" value="<%=value[i][0]%>" <%if(!"".equals(value[i][2])){ out.println("checked=true"); }%> onclick="_init();"></td>
  </tr>
  <%} %>
  <%} %>
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
</form>
</body>
<script language="javascript">
var arr = new Array();
_init();
function _init(){
    var selAll = true;
	var obj = document.frmEdit.elements.tags("input");
	for(var i = 0; i < obj.length; i++){
		if(obj[i].name == "group_list"){
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
	var obj = document.frmEdit.elements.tags("input")
	if (document.all.SelectAll.checked){
		for (i=0; i < obj.length; i++){
			var e = obj[i];
			if(e.name == "group_list" && !e.disabled){
    			e.checked = true;
    		}
		}
	}else{
		for (i=0; i < obj.length; i++){
			var e =obj[i];
			if(e.name == "group_list"){
				e.checked = false;
    		}
		}
	}
}

function _saveSubmit(){
	document.frmEdit.submit();
}
</script>
</html>