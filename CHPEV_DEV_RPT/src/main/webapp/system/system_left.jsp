<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%
String rootPath = request.getContextPath();
%>
<html>
<head>
<title>new</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="<%=rootPath%>/css/other/newmain.css">
<script language="JavaScript" src="<%=rootPath%>/js/menuUtil.js"></script>

</head>
<body>
<table style="width:100%; height=100%" align="center">
<tr>
<td valign="top">
<table id="menuTable" style="width:100%; margin-top: 0px" align="center">

<tr> 
    <td id='menuButton' onclick='display(this,6);' style="cursor:hand"> 
       <p class='submenu_l1'> <a href='<%=rootPath%>/system/deptmain.jsp' target='bodyFrame' onclick='setColor(this);'>部门管理</a> 
   </p>
 </td> 
</tr> 
<tr> 
    <td id='menuButton' onclick='display(this,7);' style="cursor:hand"> 
       <p class='submenu_l1'> <a href='<%=rootPath%>/system/regionmain.jsp' target='bodyFrame' onclick='setColor(this);'>区域管理</a> 
   </p>
 </td> 
</tr> 
<tr> 
    <td id='menuButton' onclick='display(this,8);' style="cursor:hand"> 
       <p class='submenu_l1'> <a href='<%=rootPath%>/system/menumain.jsp' target='bodyFrame' onclick='setColor(this);'>菜单管理</a> 
   </p>
 </td> 
</tr> 

<tr> 
    <td id='menuButton' onclick='display(this,8);' style="cursor:hand"> 
       <p class='submenu_l1'> <a href='<%=rootPath%>/system/UploadMonitorTab.screen' target='bodyFrame' onclick='setColor(this);'>上传监控</a> 
   </p>
 </td> 
</tr> 
<tr> 
    <td id='menuButton' onclick='display(this,8);' style="cursor:hand"> 
       <p class='submenu_l1'> <a href='<%=rootPath%>/uploadmsu/uploadmsu_info.jsp?report_id=RPTUM014' target='bodyFrame' onclick='setColor(this);'>中高端用户保有情况分析</a> 
   </p>
 </td> 
</tr> 
<tr> 
    <td id='menuButton' onclick='display(this,8);' style="cursor:hand"> 
       <p class='submenu_l1'> <a href='<%=rootPath%>/report/ReportView.rptdo?rpt_id=RPTU1001' target='bodyFrame' onclick='setColor(this);'>中国指标体系--业务部分</a> 
   </p>
 </td> 
</tr> 
<tr> 
    <td id='menuButton' onclick='display(this,8);' style="cursor:hand"> 
       <p class='submenu_l1'> <a href='<%=rootPath%>/system/PackageView.rptdo' target='bodyFrame' onclick='setColor(this);'>产品套餐配置产品套餐配置</a> 
   </p>
 </td> 
</tr> 

<tr> 
    <td id='menuButton' onclick='display(this,8);' style="cursor:hand"> 
       <p class='submenu_l1'> <a href='<%=rootPath%>/system/bulletinAdmin/bulletinAdmin.rptdo?opt_type=main' target='bodyFrame' onclick='setColor(this);'>公告管理</a> 
   </p>
 </td> 
</tr> 

</table>
</td>
</tr>
</table>
</body>
</html>