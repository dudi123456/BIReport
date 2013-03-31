<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ailk.bi.base.table.InfoUserGroupTable"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%
String role_id = request.getParameter("role_id");
if(role_id==null){
	role_id = "";
}
String role_name = CommTool.getParameterGB(request,"role_name");
if(role_name==null){
	role_name = "";
}
String role_type = CommTool.getParameterGB(request,"role_type");
InfoUserGroupTable userGroup = (InfoUserGroupTable)request.getAttribute("info");
String[][] groupRole = (String[][])request.getAttribute("groupRole");
String[][] groupRoleList = (String[][])request.getAttribute("groupRoleList");
boolean flag = false;
if(groupRoleList!=null && groupRoleList.length>0) {
	flag = true;
}
//String[][] noInGroupRole = (String[][])request.getAttribute("noInGroupRole");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title></title>
<style type="text/css">
body,td {
	font-family: 宋体;
	font-size: 9pt;
}
</style>
<link rel="StyleSheet" href="common/dtree.css"
	type="text/css" />
<script type="text/javascript" src="common/dtree.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">

<script>
function clickNode(nodeid,objNodeid){
}
</script>
</head>
<body topmargin="0" leftmargin="0">
<form name="groupRoleForm" action="userGroupView.rptdo" method="post">
<input type="hidden" name="group__role_id" >
<input type="hidden" name="group__group_id" value="<%=userGroup.group_id %>">
<input type="hidden" name="group_id" value="<%=userGroup.group_id %>">
<input type="hidden" name="submitType" value="groupRole">
<table width="100%" border=0 cellpadding="2" cellspacing="0" class="portal-navbar">
	<tr>
		<!-- 左侧树型导航 -->
		<td width="10%" valign="top">

							<script type="text/javascript">
							d2 = new dTree('d2');
							d2.add('<%=userGroup.group_id%>',-1,'<%=userGroup.group_name%>');
							<%

								int i = 0;
								if(groupRole!=null && groupRole.length>0){
									for(i=0;i<groupRole.length;i++) {
										String roleId = groupRole[i][0];
										String roleName = groupRole[i][1];
							%>
										d2.add("<%=roleId%>","<%=userGroup.group_id%>","<%=roleName%>");
							<%
									}
								}
							%>
							document.write(d2);
						</script>

		</td>
		<!-- 右侧首页结束 -->
		<td valign="top" width="90%">

					<table>
						<tr>
							<td>
							角色编号：<input type="text" name="role_id" size="8" value="<%=role_id %>">
							角色名称：<input type="text" name="role_name" size="18" value="<%=role_name %>">
							角色类型：<BIBM:TagSelectList listName="role_type" listID="#" focusID="<%=role_type%>" selfSQL="'',全部;1,菜单角色;2,地域角色;3,即席查询角色;"/>
							<input type="submit" name="query" class="button" value="查询">
						<%	if(flag) { %>

					<input type="button" name="save" onclick="doSave()" class="button" value="保存">
					<%} %>
							</td>
						</tr>
					</table>
					<TABLE width="100%" border="0" cellpadding="0" cellspacing="1" class="table-bg">
					<tr align="center">

					<td class="tab-title" >角色编号</td>
					<td class="tab-title" >角色名称</td>
					<td class="tab-title" >角色类型</td>
					<td class="tab-title" >创建组</td>
					<td class="tab-title" ><input type="checkbox" name="ckall" onclick="checkEvent('ck','ckall')" ></td>
					</tr>
					<%

					for(i=0;groupRoleList != null && i<groupRoleList.length;i++) {
						flag = true;
					%>
					<TR class="table-white-bg" align="center">
					<td width="5%"><%=groupRoleList[i][0] %></td>
					<td align="left"  width="60%"><%=groupRoleList[i][1] %></td>
					<td width="15%"><%=groupRoleList[i][2]%></td>
					<td width="20%"><%=groupRoleList[i][3]%></td>
					<td align="center"><input type="checkbox" name="ck" <%=groupRoleList[i][5] %> value="<%=groupRoleList[i][0] %>" rty="<%= groupRoleList[i][4]%>"></td>
					</TR>
					<%}
					%>
					</TABLE>

				</td>
		</tr>

</table>
</form>
</body>
</html>
<script><!--
function checkEvent(name, allCheckId) {
    var allCk = document.getElementById(allCheckId);
    if (allCk.checked == true) checkAll(name);
    else checkAllNo(name);

 }

//全选
function checkAll(name) {
    var names = document.getElementsByName(name);
    var len = names.length;
    if (len > 0) {
        var i = 0;
        for (i = 0; i < len; i++)
         names[i].checked = true;

     }
 }

//全不选
function checkAllNo(name) {
    var names = document.getElementsByName(name);
    var len = names.length;
    if (len > 0) {
        var i = 0;
        for (i = 0; i < len; i++)
         names[i].checked = false;
     }
 }

function doSave() {
    var names = document.getElementsByName('ck');
    var len = names.length;
    var count = 0;
    var value = "";
    var flag = false;
    if (len > 0) {
        var i = 0;
        for (i = 0; i < len; i++) {
          if(names[i].checked) {
			++count;
			value += names[i].value+",";
			if(names[i].rty=='2') {
				flag = true;
			}
          }
        }

     }
/*   	if(!flag) {
		alert("用户组必须有一个地域角色！");
		return false;
   	} */
    groupRoleForm.group__role_id.value = value.substring(0,value.length-1);
    groupRoleForm.submitType.value="saveGroupRole";
    groupRoleForm.submit();
}
--></script>