<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="java.util.*"%>
<%@ page import="com.ailk.bi.base.table.InfoUserGroupTable"%>
 
<%
InfoUserGroupTable userGroup = (InfoUserGroupTable)request.getAttribute("info");
String[][] groupUser = (String[][])request.getAttribute("groupUser");
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
<link rel="StyleSheet" href="/common/dtree.css"
	type="text/css" />
<script type="text/javascript" src="/common/dtree.js"></script>
<script>
function clickNode(nodeid,objNodeid){
}
</script>
</head>
<body topmargin="0" leftmargin="0">
<form name="groupUserForm" action="userGroupView.rptdo?submitType=saveGroupUser" method="post">
<input type="hidden" name="group__user_id" >
<input type="hidden" name="group__group_id" value="<%=userGroup.group_id %>">
<table width="100%" height="800px" border=0 cellpadding="2" cellspacing="0" class="portal-navbar">
	<tr>
		<!-- 左侧树型导航 -->
		<td width="20%" valign="top">
	
							<script type="text/javascript">					
							d2 = new dTree('d2');	
							d2.add('<%=userGroup.group_id%>',-1,'<%=userGroup.group_name%>');						
							<%
							
								int i = 0;
								if(groupUser!=null && groupUser.length>0){
									for(i=0;i<groupUser.length;i++) {
										String userId = groupUser[i][0];
										String userName = groupUser[i][1];
							%>
										d2.add("<%=userId%>","<%=userGroup.group_id%>","<%=userName%>");
							<%	
									}
								}							
							%>
							document.write(d2);
						</script>
		
		</td>
		<!-- 右侧首页结束 -->
		<td valign="top">

				已选用户
					      <select size="20" name="haveRole" style="width:200" >
				 		    <%
				 		    if(groupUser!=null && groupUser.length>0){ 
				 		    	for(i=0;i<groupUser.length;i++) {
				 		    		String userId = groupUser[i][0];
									String userName = groupUser[i][1];
				 		    %>
					        	<option value='<%=userId %>'><%=userName %></option>
					        <%  }
				 		    }
				 		    %>		        
						  </select>
				

		</td>
		<td valign="top">
			<input type="button" name="moveR" value=">>" onclick="moveOut()"> 
			<input type="button" name="moveL" value="<<" onclick="moveIn()"> 
		</td>
		<td valign="top">

				可选用户
					             
						
				

		</td>
	</tr>
	<tr>
		<td valign="top"><input type="button" name="save" value="保存" onclick="doSave()"></td>
	</tr>
</table>
</form>
</body>
</html>
<script>
function moveOut() {
	var haveObj = groupUserForm.haveRole;
	var noHaveObj = groupUserForm.noHaveRole;
	var sourcelen = haveObj.length ;
	for(var i=0;i<sourcelen;i++){
        if (haveObj.options[i].selected){
           addtolist(noHaveObj,haveObj.options[i].text,haveObj.options[i].value);  
           haveObj.options.remove(i);        
        }
     }
}

function moveIn() {
	var haveObj = groupUserForm.haveRole;
	var noHaveObj = groupUserForm.noHaveRole;
	var sourcelen = noHaveObj.length ;
	for(var i=0;i<sourcelen;i++){
        if (noHaveObj.options[i].selected){
           addtolist(haveObj,noHaveObj.options[i].text,noHaveObj.options[i].value);  
           noHaveObj.options.remove(i);        
        }
     }
}

function addtolist(obj,value,label){
    obj.add(new Option(value,label));
}

function doSave() {
	var haveObj = groupUserForm.haveRole;
	var sourcelen = haveObj.length ;
	var value = "";
	for(var i=0;i<sourcelen;i++){
		
       	if(value.length>0) {
           	value += ",";
       	}
		value += haveObj.options[i].value;
	}

	groupUserForm.group__role_id.value = value;
	groupUserForm.submit();
}
</script>