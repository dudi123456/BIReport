<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.bulletin.util.*"%>
<%
String rootPath = request.getContextPath();
%>

<html>
<head>
<title></title>
<!-- Modify this file to change the way the tree looks -->
<link type="text/css" rel="stylesheet" href="<%=rootPath%>/css/other/xnewtree.css">
<link rel="stylesheet" href="<%=rootPath%>/css/other/syscss.css" type="text/css">
<script src="<%=rootPath%>/js/xnewtree.js"></script>
<script src="<%=rootPath%>/js/checkBoxBulletin.js"></script>
<SCRIPT language=javascript src="<%=rootPath%>/js/js.js"></SCRIPT>
<script>
//
function selectValue(){
  var str="";

  var objs = document.getElementsByTagName("INPUT");
  if(objs){
	for(var i=0; i<objs.length;i++){

		if(objs[i].type=="checkbox" && objs[i].checked==true){
			if (str.length==0)
			{
				str = objs[i].value;
				
			}else{
				str +=","+objs[i].value;
			}
			
		}
	}
  }
  window.returnValue=str;
  this.close();

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

<table height="95%" width="100%">

<tr>
    <td height="18" ><img src="<%=rootPath%>/biimages/arrow7.gif" width="7" height="7"><b></b>&nbsp;角色信息</td>
</tr>
<tr>
		<td height="1" background="<%=rootPath%>/biimages/black-dot.gif"></td>
</tr>

<tr>
<td>
<div style="position: absolute; width:  90%; height: 90%; padding: 5px; overflow: auto;">

<script>
if (document.getElementById) {

	 <%=BulletinRoleGrp.buildMenuTree()%>
}
</script>
</div>
</td>
</tr>
<tr>
<td height="40" align="center" valign="middle">
<TABLE width="" cellpadding="2" cellspacing="0">
					<TR VALIGN="MIDDLE">
						<TD align="CENTER">
						<INPUT TYPE="BUTTON" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON" style="cursor:hand;" VALUE="确定"
							onclick="selectValue();">
							<INPUT TYPE="BUTTON" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON" style="cursor:hand;" VALUE="关闭" onclick="window.close();" 
							/></TD>
					</TR>
</TABLE>
</td>
</tr>


</table>

</form>
</body>
</html>


