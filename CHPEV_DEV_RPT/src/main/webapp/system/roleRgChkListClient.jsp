<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.base.util.*" %>
<%@ page import="com.ailk.bi.system.common.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.ailk.bi.base.struct.UserCtlRegionStruct"%>

<%
//角色ID
String role_id = CommTool.getParameterGB(request,"role_id");
if(role_id == null){
	role_id = "";
}

//页面权限
String pageRight = CommTool.getParameterGB(request,"pageRight");
if(pageRight == null){
	pageRight = "10";
}
String loginRegion = "";
UserCtlRegionStruct region = (UserCtlRegionStruct)session.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
if("1".equals(region.ctl_lvl)) {
	loginRegion = region.ctl_metro_str;
}else {
	loginRegion = region.ctl_city_str_add;
}

%>
<HTML>
<HEAD>
<TITLE>相关数据权限</TITLE>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/other/xnewtree.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/xnewtree.js"></script>
<script src="<%=request.getContextPath()%>/js/rgcheckboxtreeitem.js"></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
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
</HEAD>
<BODY  bgcolor="#ffffff" onload="">

<script>
var tree = new WebFXTree('相关区域',null,null,null,null,null,parent.clickCheck);
tree.setBehavior('classic');
//var r_111 = new WebFXCheckBoxTreeItem('区域1','','111',false);
//tree.add(r_111);
<%
Vector regionVct=LSInfoRole.getRoleRegionVct(loginRegion,role_id);
for(int i=0;i<regionVct.size();i++){
	Vector aRow=(Vector)regionVct.get(i);
	String level=(String)aRow.get(0);
	String region_id=(String)aRow.get(1);
	String parent_id=(String)aRow.get(2);
	String region_name=(String)aRow.get(3);
	String isactive=(String)aRow.get(4);
	String tmp_role_id=(String)aRow.get(5);
	boolean isChecked=role_id.equals(tmp_role_id)?true:false;
	out.println("var r_"+region_id+"=new WebFXCheckBoxTreeItem('"+region_name+"','','"+region_id+"',"+isChecked+");");
	if("1".equals(level)){
		out.println("tree.add(r_"+region_id+");");
	}else{
		out.println("r_"+parent_id+".add(r_"+region_id+");");
	}
}
%>
document.write(tree);
</script>
</BODY>
</HTML>
