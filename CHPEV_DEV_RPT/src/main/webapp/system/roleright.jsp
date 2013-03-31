<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.system.common.*"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.base.table.*"%>
<%@ page import="com.ailk.bi.system.service.impl.*"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%
    InfoOperTable loginUser = CommonFacade.getLoginUser(session);
	String pageRight="11";
	String role_id = CommTool.getParameterGB(request,"role_id");
	if(role_id == null || "".equals(role_id)){
		role_id = "0";
	}
	//
	//取出角色信息
	InfoRoleTable roleInfo = LSInfoRole.getRoleInfo(role_id);
	if (roleInfo == null) {
		roleInfo = new InfoRoleTable();
	}
	//
	String submit_type = request.getParameter("submit_type");
	if(submit_type == null || "".equals(submit_type)){
		submit_type = "0";
	}
	String value = CommTool.getParameterGB(request,"op_str");
	if(value == null || "".equals(value)){
		value = "";
	}
	
	String region_id=loginUser.region_id;

%>
<html>
<head>
<title></title>
<!-- Modify this file to change the way the tree looks -->
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/other/xnewtree.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/xnewtree.js"></script>
<script src="<%=request.getContextPath()%>/js/webfxcheckboxtreeitem.js"></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<script>
//
var str="";
function selectValue(){

  var objs = document.getElementsByTagName("INPUT");
  if(objs){
	for(var i=0; i<objs.length;i++){
		if(objs[i].type=="checkbox" && objs[i].checked==true){
			str +=","+objs[i].value;
		}
	}
  }
  document.myform.submit_type.value = "2";
  document.myform.op_str.value = str.substr(1);
  document.myform.submit();
}
</script>
<style>
	body { background: white; color: black; }
	input { width: 120px; }
	input.tree-check-box {
 		width:		auto;
 		margin:		0;
 		padding:	0;
 		height:		14px;
 		vertical-align:	middle;
 	}
</style>
</head>
<body class="side-7" onload="">
<form name="myform" action="roleright.jsp" method="post">
<input type="hidden" name="op_str" value="">
<input type="hidden" name="role_id" value="<%=role_id%>">
<input type="hidden" name="submit_type" value="0">
<%
if("2".equals(submit_type)){
		int ErrNo = LSInfoRole.saveMenuInfo(request,value,role_id);
		if(ErrNo< 0){
			out.println("<table width=\"100%\"><tr><td width=\"100%\" align=\"center\"><font color=red>修改失败！</font></td></tr></table>");
		}
		else{
			out.println("<table width=\"100%\"><tr><td width=\"100%\" align=\"center\"><font color=red>修改成功！</font></td></tr></table>");
		}
	}
%>
<table height="95%" width="100%">

<tr>
    <td height="18" ><img src="../images/common/system/arrow7.gif" width="7" height="7"><b><%=roleInfo.role_name%></b>&nbsp;角色信息</td>
</tr>
<tr>
		<td height="1" background="../images/common/system/black-dot.gif"></td>
</tr>

<tr>
<td>
<div style="position: absolute; width: 840px; height: 80%; padding: 5px; overflow: auto;">
<script>
if (document.getElementById) {
	 <%=LSInfoRole.getMenuTree(loginUser.user_id,role_id,loginUser.system_id) %>
}
</script>
</div>
</td>
</tr>

<tr>
<td height="40" align="center" valign="middle">
<%if("11".equals(pageRight)){%>
<TABLE width="" cellpadding="2" cellspacing="0">
					<TR VALIGN="MIDDLE">
						<TD align="CENTER">
						<INPUT TYPE="BUTTON" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON" style="cursor:hand;" VALUE="保存"
							onclick="selectValue();">
							<INPUT TYPE="reset" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON" style="cursor:hand;" VALUE="重置"
							/></TD>
					</TR>
</TABLE>
<%}else{%>
<font color="red">您的归属区域不是该角色的归属区域,只能拥有查看权限.</font>
<%}%>
</td>
</tr>

</table>

</form>
</body>
</html>


