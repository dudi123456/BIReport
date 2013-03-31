<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ailk.bi.base.table.InfoRoleTable"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%
String group_id = CommTool.getParameterGB(request,"group_id");
if (group_id == null || "".equals(group_id)) {
	group_id = "";
}
String group_name = CommTool.getParameterGB(request,"group_name");
if (group_name == null || "".equals(group_name)) {
	group_name = "";
}
String group_type = CommTool.getParameterGB(request,"group_type");
InfoRoleTable roleInfo = (InfoRoleTable)request.getAttribute("info");
String[][] roleGroup = (String[][])request.getAttribute("roleGroup");
String[][] roleGroupList = (String[][])request.getAttribute("roleGroupList");
boolean flag = false;
if(roleGroupList!=null && roleGroupList.length>0) {
	flag = true;
}
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
<form name="roleGroupForm" action="roleview.rptdo" method="post">
<input type="hidden" name="role__group_id" >
<input type="hidden" name="role__role_id" value="<%=roleInfo.role_id %>">
<input type="hidden" name="role_id" value="<%=roleInfo.role_id %>">
<input type="hidden" name="submitType" value="roleGroup">
<table width="100%" border=0 cellpadding="2" cellspacing="0" class="portal-navbar">
	<tr>
		<!-- 左侧树型导航 -->
		<td width="10%" valign="top">

							<script type="text/javascript">
							d2 = new dTree('d2');
							d2.add('<%=roleInfo.role_id%>1',-1,'<%=roleInfo.role_name%>');
							<%

								int i = 0;
								if(roleGroup!=null && roleGroup.length>0){
									for(i=0;i<roleGroup.length;i++) {
										String groupId = roleGroup[i][0];
										String groupName = roleGroup[i][1];
							%>
										d2.add("<%=groupId%>","<%=roleInfo.role_id%>1","<%=groupName%>");
							<%
									}
								}
							%>
							document.write(d2);
						</script>

		</td>
		<!-- 右侧首页结束 -->
		<td valign="top" width="90%">

					<table align="center">
						<tr >
							<td >
							用户组编号：<input type="text" name="group_id" size="8" value="<%=group_id %>">
							用户组名称：<input type="text" name="group_name" size="18" value="<%=group_name %>">
							用户组类型：<BIBM:TagSelectList listName="group_type" listID="#" focusID="<%=group_type%>" selfSQL="'',全部;1,发展;2,楼宇;"/>
							<input type="submit" name="query" class="button" value="查询">
						<%	if(flag) { %>

					<input type="button" name="save" onclick="doSave()" class="button" value="保存">
					<%} %>
							</td>
						</tr>
					</table>
					<TABLE width="100%" border="0" cellpadding="0" cellspacing="1" class="table-bg">
					<tr align="center">

					<td class="tab-title" >用户组编号</td>
					<td class="tab-title" >用户组名称</td>
					<td class="tab-title" >用户组类型</td>
					<td class="tab-title" ><input type="checkbox" name="ckall" onclick="checkEvent('ck','ckall')" ></td>
					</tr>
					<%

					for(i=0;roleGroupList != null && i<roleGroupList.length;i++) {
						flag = true;
					%>
					<TR class="table-white-bg" align="center">
					<td width="5%"><%=roleGroupList[i][0] %></td>
					<td align="left" ><%=roleGroupList[i][1] %></td>
					<td width="15%"><%=roleGroupList[i][2]%></td>
					<td align="center"><input type="checkbox" name="ck" <%=roleGroupList[i][3] %> value="<%=roleGroupList[i][0] %>"></td>
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
<script>
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
    if (len > 0) {
        var i = 0;
        for (i = 0; i < len; i++) {
          if(names[i].checked) {
			++count;
			value += names[i].value+",";
          }
        }
     }

    roleGroupForm.role__group_id.value = value.substring(0,value.length-1);
    roleGroupForm.submitType.value="saveRoleGroup";
    roleGroupForm.submit();
}
</script>