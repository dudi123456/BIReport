<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.base.table.*"%>
<%@ page import="com.ailk.bi.system.common.*"%>


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
<script type="text/javascript">
//	webFXTreeConfig.rootIcon		= "../images/common/xmltree/xp/root.gif";
	webFXTreeConfig.rootIcon		= "../images/wjx1.gif";
	//webFXTreeConfig.openRootIcon		= "../images/common/xmltree/xp/root.gif";
	webFXTreeConfig.openRootIcon		= "../images/wjx1.gif";
	//webFXTreeConfig.folderIcon		= "../images/common/xmltree/xp/region.gif";
	//webFXTreeConfig.fileIcon		= "../images/common/xmltree/xp/regiondef.gif";
	webFXTreeConfig.fileIcon		= "../images/close1.gif";
	webFXTreeConfig.folderIcon		= "../images/file1.gif";
//	webFXTreeConfig.openFolderIcon		= "../images/common/xmltree/xp/regionOpen.gif";
	webFXTreeConfig.openFolderIcon		= "../images/openf1.gif";
	webFXTreeConfig.lMinusIcon		= "../images/common/xmltree/xp/Lminus.png";
	webFXTreeConfig.lPlusIcon		= "../images/common/xmltree/xp/Lplus.png";
	webFXTreeConfig.tMinusIcon		= "../images/common/xmltree/xp/Tminus.png";
	webFXTreeConfig.tPlusIcon		= "../images/common/xmltree/xp/Tplus.png";
	webFXTreeConfig.iIcon			= "../images/common/xmltree/xp/I.png";
	webFXTreeConfig.lIcon			= "../images/common/xmltree/xp/L.png";
	webFXTreeConfig.tIcon			= "../images/common/xmltree/xp/T.png";
	webFXTreeConfig.blankIcon     		= "../images/common/xmltree/xp/blank.png";
</script>
<%

String tree_name="选择文件夹";
%>
</head>
<body>

<TABLE cellpadding="0" WIDTH="100%" HEIGHT="100%">
<TR valign="top">
<TD>
<script>
var tree = new WebFXLoadTree("<%=tree_name%>","createFavorTreeAddXML.rptdo");
document.write(tree);
</script>
</TD>
</TR>
</TABLE>
<script language=javascript>
function linkUrl(){
  window.parent.setFavor('','');
}


</script>

</body>
</html>

