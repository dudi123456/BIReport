<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.base.table.*"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%@ page import="com.ailk.bi.system.common.*"%>
<%
if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>
<%//访问页权限
			String pageRight = "11";//CommTool.CheckActionPage(session,CObjects.ROLE);
			//角色ID
			String role_code = request.getParameter("role_code");
			//角色名称
			String role_name = CommTool.getParameterGB(request, "role_name");
			//登陆操作员信息
			InfoOperTable loginUser = CommonFacade.getLoginUser(session);
			String dept_id = loginUser.dept_id;//登陆操作员部门
			String region_id=loginUser.region_id;//登陆操作员区域
			//取出角色信息
			InfoRoleTable roleInfo = LSInfoRole.getRoleInfo(role_code);
			if (roleInfo == null) {
				roleInfo = new InfoRoleTable();
			}
			//不是操作员登陆区域的角色,只有查看
			//if (("0".equals(roleInfo.belong_type)&&!roleInfo.region_id.equals(region_id))) {
			//	pageRight = "10";
			//}
			//判断提交类型
			String submitType = request.getParameter("submitType");
			if (submitType == null) {
				submitType = "0";
			}

			int ErrNo = -1;
			if ("1".equals(submitType)) {
				String[] delArr = request.getParameterValues("delSelect");

				if (delArr != null && delArr.length > 0) {
					String delStr = "";
					for (int i = 0; i < delArr.length; i++) {
						if (delStr.length() > 0) {
							delStr += " , '" + delArr[i]+"'";
						} else {
							delStr += "'"+delArr[i]+"'";
						}
					}
					//
					ErrNo = 0;//LSInfoRole.delUserRole(request,delStr,role_code);

					if (ErrNo < 0) {
						out.println("<table width=\"100%\"><tr><td width=\"100%\" align=\"center\"><font color=red>修改失败！</font></td></tr></table>");
					} else {
						out.println("<table width=\"100%\"><tr><td width=\"100%\" align=\"center\"><font color=red>修改成功！</font></td></tr></table>");
					}
				}
			}

			//取角色的所有操作员列表
			InfoOperTable sstUsers[] = null;//LSInfoRole.getRoleOpers(role_code);//角色ID和登陆操作员区域
			//System.out.println(sstUsers.length);


			%>

<HTML>
<HEAD>
<TITLE>角色用户</TITLE>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script language="javascript">
//关闭窗口
function closeOpenWindow(operWin){
eval("if ("+operWin+" && "+operWin+".open && (!"+operWin+".closed))"+operWin+".close();");
}
  //选择操作员
  function _newRoleUser(){
	var optstr = "status=yes,toolbar=no,menubar=no,scrollbars=auto,location=no,height=450,width=800"
	<%if("0".equals(roleInfo.belong_type)){%>
	var win = window.open("newRoleUser.jsp?role_code=<%=role_code%>&role_name=<%=role_name%>&region_id=<%=region_id%>","OpenWinRoleUser",optstr);
    	<%}else{%>
	var win = window.open("newRoleUser_byDept.jsp?role_code=<%=role_code%>&role_name=<%=role_name%>&dept_id=<%=dept_id%>","OpenWinRoleUser",optstr);
        <%}%>
	if(win != null)
		win.focus();
  }

  function _frmReset(){
	document.frmEdit.reset();
  }

  function _delRoleUser(){
	var selectFlag = false;
	var del = document.frmEdit.elements.tags("input");
	for(i = 0; i < del.length; i++){
		if(del[i].type == "checkbox" && del[i].checked == true){
			selectFlag = true;
			break;
		}
	}
	if(!selectFlag){
		alert("您没有选中要删除的操作员！");
		return;
	}
	else{
		if(!confirm("请您确定要删除角色下的操作员！"))
			return;
	}

	window.frmEdit.submitType.value = 1;
	window.frmEdit.submit();
  }

  function _selectAll(){
	var obj = document.frmEdit.elements.tags("input")

	if (document.all.SelectAll.checked){
		for (i=0; i < obj.length; i++){
			var e = obj[i];
			if(e.type == "checkbox" && !e.disabled){
    				e.checked = true;
    			}
		}
	}
	else{
		for (i=0; i < obj.length; i++){
			var e =obj[i];
			if(e.type == "checkbox"){
    				e.checked = false;
    			}
		}
	}
	return false;
  }
  function _isSelectAll(){
	var obj = document.frmEdit.elements.tags("input")

	for (i=0; i < obj.length; i++){
		var e = obj[i];
		if(e.name == "delSelect" && !e.disabled && !e.checked){
			document.all.SelectAll.checked = false;
			return;
		}
	}

	document.all.SelectAll.checked = true;
  }
</script>
</HEAD>
<BODY class="side-7">
<table width="100%">
	<tr>
		<td height="22"><img src="../biimages/arrow7.gif" width="7" height="7">
		<b><%=roleInfo.role_id%>&nbsp;<%=roleInfo.role_name%></b>&nbsp;相关操作员</td>
	</tr>
	<tr>
		<td height="1" background="../biimages/black-dot.gif"></td>
	</tr>
</table>
<TABLE WIDTH="100%" align="center">
	<TR>
		<TD>
		<FORM name="frmEdit" action="userlist.jsp" METHOD="POST"	onsubmit="return false;">
		<INPUT TYPE="hidden" id="submitType"	name="submitType" value="0" />
		<INPUT TYPE="hidden" id="role_code" name="role_code"	value="<%=role_code%>" />
		<INPUT TYPE="hidden" id="role_name" name="role_name"	value="<%=role_name%>" />
		<INPUT TYPE="hidden" id="region_id" name="region_id"	value="<%=region_id%>" />
		<INPUT TYPE="hidden" id="pageRight"	name="pageRight" value="<%=pageRight%>" />

		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="table">
			<tr class="table-th">
				<td align="center" class="table-item" width="14%">姓名</td>
				<td align="center" class="table-item" width="17%">登陆工号</td>
				<td class="table-item" align="center" width="7%">性别</td>
				<td class="table-item" align="center" width="15%">区域</td>
				<td class="table-item" align="center" width="15%">部门</td>
				<td class="table-item2" align="center" width="8%">删除</td>
			</tr>
			<%if (sstUsers == null) {

			%>
			<TR  class="table-tr" onMouseOver="switchClass(this)"
				onMouseOut="switchClass(this)">
				<TD class="table-item2" colspan="6" ALIGN="CENTER"><font color="red">此角色下还没有设置操作员</font></TD>
			</TR>
			<%} else {
				for (int i = 0; i < sstUsers.length; i++) {
					if ((i + 1) % 2 == 0) {

					%>
			<tr class="table-trb" onMouseOver="switchClass(this)"
				onMouseOut="switchClass(this)">
				<%} else {

					%>
			<tr class="table-tr" onMouseOver="switchClass(this)"
				onMouseOut="switchClass(this)">
				<%}%>
				<TD align="center" class="table-td" width="14%"><%=sstUsers[i].oper_name%>&nbsp;</TD>
				<TD class="table-td" width="17%"><%=sstUsers[i].user_id%>&nbsp;</TD>
				<TD align="center" class="table-td" width="7%">
				    <%if (sstUsers[i].gender.equals("1"))
						out.println("男&nbsp;");
					else if (sstUsers[i].gender.equals("2"))
						out.println("女&nbsp;");
					else
						out.println("未知&nbsp;");

					%></TD>
				<TD align="center" class="table-td" width="27%"><%=sstUsers[i].region_name%>&nbsp;</TD>
				<TD align="center" class="table-td" width="27%"><%=sstUsers[i].dept_name%>&nbsp;</TD>
				<TD align="center" class="table-td2" width="8%"><INPUT
					id="delSelect" type='checkbox' name="delSelect"
					value="<%=sstUsers[i].user_id%>" onclick='_isSelectAll();' /></TD>
			</TR>
			<%}
			}

			%>
		</TABLE>
		</FORM>
		<table width="100%">
			<tr>
				<td align="right">全选</td>
				<td width="40"><INPUT id="SelectAll" type='checkbox'
					name="SelectAll" value="" onclick="_selectAll();" /></td>
			</tr>
		</table>
		<br>
		<center>
		<%if("11".equals(pageRight)){%>
		<TABLE width="" cellpadding="2" cellspacing="0">
			<TR VALIGN="MIDDLE">
				<TD align="CENTER"><INPUT TYPE="BUTTON" class="button"
					onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
					NAME="BUTTON" style="cursor:hand;" VALUE="新增"
					onclick="_newRoleUser();" /></TD>
				<TD align="CENTER"><INPUT TYPE="BUTTON" class="button"
					onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
					NAME="BUTTON" style="cursor:hand;" VALUE="重置"
					onclick="_frmReset();" /></TD>
				<TD align="CENTER"><INPUT TYPE="BUTTON" class="button3"
					onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
					NAME="BUTTON" style="cursor:hand;" VALUE="从角色下删除"
					onclick="_delRoleUser();" /></TD>
			</TR>
		</TABLE>
		<%}%>
		</center>
		</TD>
	</TR>
</TABLE>
</BODY>
</HTML>


