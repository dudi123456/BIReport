<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="java.util.*"%>
<%@ page import="com.ailk.bi.base.table.InfoOperTable"%>
 
<%
String roleTree = (String)request.getAttribute("roleTree");
String user_id = (String)request.getAttribute("user_id");
String[][] userRole = (String[][])request.getAttribute("userRole");
boolean flag = false;
//String[][] noInUserRole = (String[][])request.getAttribute("noInUserRole");
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
<form name="userRoleForm" action="userview.rptdo?submitType=saveUserRole" method="post">
<input type="hidden" name="user__role_id" >
<input type="hidden" name="user__user_id" value="<%=user_id %>">
<table width="100%" height="800px" border=0 cellpadding="2" cellspacing="0" class="portal-navbar">
	<tr>
		<!-- 左侧树型导航 -->
		<td width="20%" valign="top">
		<%=roleTree %>
		
		</td>
		<!-- 右侧首页结束 -->
		<td valign="top">

			<TABLE width="100%" border="0" cellpadding="0" cellspacing="1" class="table-bg">
			<tr align="center">
			
			<td class="tab-title" >角色编号</td>
			<td class="tab-title" >角色名称</td>
			<td class="tab-title" >角色类型</td>
			<td class="tab-title" >创建组</td>
			<td class="tab-title" ><input type="checkbox" name="ckall" onclick="checkEvent('ck','ckall')" ></td>
			</tr>
			<%
			
			for(int i=0;userRole != null && i<userRole.length;i++) { 
				flag = true;
			%>
			<TR class="table-white-bg" align="center">
			<td><%=userRole[i][0] %></td>
			<td><%=userRole[i][1] %></td>
			<td><%=userRole[i][2]%></td>
			<td><%=userRole[i][3]%></td>
			<td align="center"><input type="checkbox" name="ck" <%=userRole[i][4] %> value="<%=userRole[i][0] %>"/></td>
			</TR>
			<%} 
			if(flag) {
			%>
			
			<TR class="table-white-bg" align="center">
			<td colspan="5" align="center"><input type="button" name="save" onclick="doSave()" class="button" value="保存"> </td>
			</tr>
			<%} %>
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
   
    userRoleForm.user__role_id.value = value.substring(0,value.length-1);
    userRoleForm.submit();
}
</script>