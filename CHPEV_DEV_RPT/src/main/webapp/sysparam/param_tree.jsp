<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<HTML  XMLNS:IE>
<head>
<title>树形管理</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/xtree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/xmlextras.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/xloadtree.js"></script>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/other/xtree.css" />
<script type="text/javascript">
	webFXTreeConfig.rootIcon		= "../images/common/xmltree/xp/root.gif";
	webFXTreeConfig.openRootIcon	= "../images/common/xmltree/xp/root.gif";
	webFXTreeConfig.fileIcon		= "../images/common/xmltree/xp/role.gif";
	webFXTreeConfig.lMinusIcon		= "../images/common/xmltree/xp/Lminus.png";
	webFXTreeConfig.lPlusIcon		= "../images/common/xmltree/xp/Lplus.png";
	webFXTreeConfig.tMinusIcon		= "../images/common/xmltree/xp/Tminus.png";
	webFXTreeConfig.tPlusIcon		= "../images/common/xmltree/xp/Tplus.png";
	webFXTreeConfig.iIcon			= "../images/common/xmltree/xp/I.png";
	webFXTreeConfig.lIcon			= "../images/common/xmltree/xp/L.png";
	webFXTreeConfig.tIcon			= "../images/common/xmltree/xp/T.png";
</script>
</head>
<body>

<TABLE cellpadding="0" WIDTH="90%" >
<TR valign="top">
<TD>
<script>
var tree = new WebFXLoadTree("参数列表","CreateParamTree.rptdo");
document.write(tree);
</script>
</TD>
</TR>

</TABLE>
<script language=javascript>
function linkUrl(){
	return;
}
</script>

</body>
</html>

