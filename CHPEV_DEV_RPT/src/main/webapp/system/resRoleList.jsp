<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.ailk.bi.system.common.*"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.base.table.*"%>
<%
//String[][] resRole = (String[][])request.getAttribute("MenuRoleInfo");
InfoOperTable user = (InfoOperTable)session.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
String menu_id = request.getParameter("menu_id");
String region_id = request.getParameter("region_id");
String res_type = "";
String res_id = "";
String[][] resRole = null;
boolean flag = false;
if(menu_id != null && !"".equals(menu_id)) {
	resRole = LSInfoResRole.getMenuRoleInfo(user.user_id, "1",menu_id,user.group_id);
	res_id = menu_id;
	res_type = "1";
}else if(region_id != null && !"".equals(region_id)){
	resRole = LSInfoResRole.getMenuRoleInfo(user.user_id, "2",region_id,user.group_id);
	res_id = region_id;
	res_type = "2";
}
%>
<HTML>
<head>
<title>菜单赋权</title>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META HTTP-EQUIV="Expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
<META HTTP-EQUIV="Expires" CONTENT="0">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></head>
<body>
<form name="resRolForm" action="resRole.rptdo" METHOD="POST" >
<input type="hidden" name="role_id" >
<input type="hidden" name="res_id" value="<%=res_id %>">
<input type="hidden" name="res_type" value="<%=res_type %>">
<input type="hidden" name="submitType" value="saveMenuRole">

<TABLE width="100%" border="0" cellpadding="0" cellspacing="1" class="table-bg">
<tr align="center">
<td class="tab-title" >角色编号</td>
<td class="tab-title" >角色名称</td>
<td class="tab-title" >角色描述</td>
<td class="tab-title" ><input type="checkbox" name="ckall" onclick="checkEvent('ck','ckall')" ></td>
</tr>
<%

for(int i=0;resRole != null && i<resRole.length;i++) { 
	flag = true;
%>
<TR class="table-white-bg" align="center">
<td><%=resRole[i][0] %></td>
<td><%=resRole[i][1] %></td>
<td><%=resRole[i][2] != null && !"".equals(resRole[i][2]) ? resRole[i][2] : "&nbsp;" %></td>
<td align="center"><input type="checkbox" name="ck" <%=resRole[i][3] %> value="<%=resRole[i][0] %>"/></td>
</TR>
<%} 
if(flag) {
%>

<TR class="table-white-bg" align="center">
<td colspan="4" align="center"><input type="button" name="save"  class="button" onclick="doSave()" value="保存"> </td>
</tr>
<%} %>
</TABLE>
</form>
<script language=javascript>
function linkUrl(){

}
</script>

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
   
    resRolForm.role_id.value = value.substring(0,value.length-1);
    resRolForm.submit();
}
</script>
