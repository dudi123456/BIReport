<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.base.util.*" %>
<%@ page import="com.ailk.bi.system.common.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.ailk.bi.metamanage.dao.impl.DimDefDaoImpl"%>

<%

String dim_id = CommTool.getParameterGB(request,"dim_id");

%>
<HTML>
<HEAD>
<TITLE>维度与数据表对应关系</TITLE>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/other/xnewtree.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/xnewtree.js"></script>
<script src="<%=request.getContextPath()%>/js/rgcheckboxtreeitemauto.js"></script>
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
var tree = new WebFXTree('维度与数据表对应关系',null,null,null,null,null,parent.clickCheck);
tree.setBehavior('classic');

<%
String[][] rs = DimDefDaoImpl.getRuleDimTable(dim_id);
for(int i=0;i<rs.length;i++){
	String level = rs[i][0];
	String id = rs[i][1];
	String name = rs[i][2];
	String parent_id = rs[i][3];
	String chk = rs[i][4];
	String is_table= rs[i][5];
	boolean isChecked = "checked".equals(chk)?true:false;
	String tempId = rs[i][1].replace(".","");
	out.println("var r_"+tempId+"=new WebFXCheckBoxTreeItem('"+name+"','','"+id+"$$"+is_table+"',"+isChecked+");");
	if("1".equals(level)){
		out.println("tree.add(r_"+tempId+");");
	}else{
		out.println("r_"+parent_id+".add(r_"+tempId+");");
	}
}
%>
document.write(tree);
</script>
</BODY>
</HTML>
