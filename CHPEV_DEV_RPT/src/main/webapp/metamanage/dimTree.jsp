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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/xloadtree.js"></script>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/other/xtree.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<script type="text/javascript">
	webFXTreeConfig.rootIcon		= "../images/common/xmltree/xp/root.gif";
	webFXTreeConfig.openRootIcon	= "../images/common/xmltree/xp/root.gif";
	webFXTreeConfig.lMinusIcon		= "../images/common/xmltree/xp/Lminus.png";
	webFXTreeConfig.lPlusIcon		= "../images/common/xmltree/xp/Lplus.png";
	webFXTreeConfig.tMinusIcon		= "../images/common/xmltree/xp/Tminus.png";
	webFXTreeConfig.tPlusIcon		= "../images/common/xmltree/xp/Tplus.png";
	webFXTreeConfig.iIcon			= "../images/common/xmltree/xp/I.png";
	webFXTreeConfig.lIcon			= "../images/common/xmltree/xp/L.png";
	webFXTreeConfig.tIcon			= "../images/common/xmltree/xp/T.png";

<%

  out.println("webFXTreeConfig.folderIcon		= \"../images/common/xmltree/xp/dept.gif\";");
  out.println("webFXTreeConfig.openFolderIcon		= \"../images/common/xmltree/xp/deptOpen.gif\";");
  out.println("webFXTreeConfig.fileIcon		= \"../images/common/xmltree/xp/deptdef.gif\";");

%>
function pageRedirect_id(dimId) {
	var iframe = document.getElementById("dimlist_frame");
	iframe.src="dimDef.rptdo?dim_id="+dimId;
}
</script>
</head>
<body bgcolor="#02449B" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table width="99%" height="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<TR valign="top"  class="leftbg-tree">
<TD>
<script>


var tree = new WebFXLoadTree("维度","createDimTreeXML.rptdo");
document.write(tree);

function linkUrl(){
}

</script>
</TD></TR></TABLE>


</body>
</html>

