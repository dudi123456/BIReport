<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.base.util.CommTool" %>
<%@ page import="com.ailk.bi.base.struct.UserCtlRegionStruct"%>
<%@ page import="com.ailk.bi.base.util.*" %>
<%
if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>


<HTML>
<head>
<title>树形管理</title>
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
<META HTTP-EQUIV="expires" CONTENT="0">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/xtree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/xmlextras.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/xloadtree.js"></script>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/other/xtree.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<script type="text/javascript">
	webFXTreeConfig.rootIcon		= "<%=request.getContextPath()%>/images/common/xmltree/xp/root.gif";
	webFXTreeConfig.openRootIcon	= "<%=request.getContextPath()%>/images/common/xmltree/xp/root.gif";
	webFXTreeConfig.lMinusIcon		= "<%=request.getContextPath()%>/images/common/xmltree/xp/Lminus.png";
	webFXTreeConfig.lPlusIcon		= "<%=request.getContextPath()%>/images/common/xmltree/xp/Lplus.png";
	webFXTreeConfig.tMinusIcon		= "<%=request.getContextPath()%>/images/common/xmltree/xp/Tminus.png";
	webFXTreeConfig.tPlusIcon		= "<%=request.getContextPath()%>/images/common/xmltree/xp/Tplus.png";
	webFXTreeConfig.iIcon			= "<%=request.getContextPath()%>/images/common/xmltree/xp/I.png";
	webFXTreeConfig.lIcon			= "<%=request.getContextPath()%>/images/common/xmltree/xp/L.png";
	webFXTreeConfig.tIcon			= "<%=request.getContextPath()%>/images/common/xmltree/xp/T.png";
	webFXTreeConfig.blankIcon			= "<%=request.getContextPath()%>/images/common/xmltree/blank.png";
<%

  out.println("webFXTreeConfig.folderIcon		= \""+request.getContextPath()+"/images/common/xmltree/xp/dept.gif\";");
  out.println("webFXTreeConfig.openFolderIcon		= \""+request.getContextPath()+"/images/common/xmltree/xp/deptOpen.gif\";");
  out.println("webFXTreeConfig.fileIcon		= \""+request.getContextPath()+"/images/common/xmltree/xp/deptdef.gif\";");

%>
function pageRedirect_id(msuId) {
	var iframe = document.getElementById("msulist_frame");
	iframe.src="msuDet.rptdo?msu_id="+msuId;
}
</script>
</head>
<body bgcolor="#02449B" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="99%" height="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<TR valign="top"  class="leftbg-tree">
<TD>
<script>


var tree = new WebFXLoadTree("指标","createMsuDetTreeXML.rptdo");
document.write(tree);

function linkUrl(){
}

</script>
</TD></TR></TABLE>


</body>
</html>

