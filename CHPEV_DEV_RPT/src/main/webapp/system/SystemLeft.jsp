<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<title>new</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/other/newmain.css">
<script language="JavaScript" src="<%=request.getContextPath()%>/js/menuUtil.js"></script>

</head>
<body>
<table style="width:100%; height=100%" align="center">
<tr>
<td valign="top">
<table id="menuTable" style="width:100%; margin-top: 0px" align="center">

<tr> 
    <td id='menuButton' onclick='display(this,1);' style="cursor:hand"> 
       <p class='submenu_menutitle'>系统管理</p>
    </td> 
</tr> 

<tr style='display:none'> 
	<td nowrap id='sub_title1'> 

		<p class='submenu_l2'><a href='../sysadmin/noSectionAdmin/numSectionAdmin.rptdo?opt_type=nummain' target='analysis_contents' onclick='setChildColor(this);'>号段管理</a></p> 
	</td> 
</tr> 
<tr style='display:none'> 
	<td nowrap id='sub_title1'> 

		<p class='submenu_l2'><a href='/localapp/busiView_Mon.jsp' target='analysis_contents' onclick='setChildColor(this);'>日志管理</a></p> 
	</td> 
</tr> 
<tr style='display:none'> 
	<td nowrap id='sub_title1'> 

		<p class='submenu_l2'><a href='../sysadmin/blackName/blackNameAdmin.rptdo?opt_type=main' target='analysis_contents' onclick='setChildColor(this);'>黑名单管理</a></p> 
	</td> 
</tr> 
<tr style='display:none'> 
	<td nowrap id='sub_title1'> 

		<p class='submenu_l2'><a href='../sysadmin/bulletinAdmin/bulletinAdmin.rptdo?opt_type=main' target='analysis_contents' onclick='setChildColor(this);'>公告管理</a></p> 
	</td> 
</tr> 

<tr style='display:none'> 
	<td nowrap id='sub_title1'> 

		<p class='submenu_l2'><a href='../system/usermain.jsp' target='analysis_contents' onclick='setChildColor(this);'>用户管理</a></p> 
	</td> 
</tr> 
<tr style='display:none'> 
	<td nowrap id='sub_title1'>
		<p class='submenu_l2'><a href='../system/rolemain.jsp' target='analysis_contents' onclick='setChildColor(this);'>角色管理</a></p> 
	</td> 
</tr>
<tr style='display:none'> 
	<td nowrap id='sub_title1'>
		<p class='submenu_l2'><a href='../system/deptmain.jsp' target='analysis_contents' onclick='setChildColor(this);'>部门管理</a></p> 
	</td> 
</tr>
<tr style='display:none'> 
	<td nowrap id='sub_title1'>
		<p class='submenu_l2'><a href='../system/regionmain.jsp' target='analysis_contents' onclick='setChildColor(this);'>区域管理</a></p> 
	</td> 
</tr>
<tr style='display:none'> 
	<td nowrap id='sub_title1'>
		<p class='submenu_l2'><a href='../system/menumain.jsp' target='analysis_contents' onclick='setChildColor(this);'>菜单管理</a></p> 
	</td> 
</tr>

</table>
</td>
</tr>
</table>
</body>
</html>