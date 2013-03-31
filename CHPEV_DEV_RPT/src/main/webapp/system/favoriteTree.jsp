<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.base.table.*"%>
<%@ page import="com.ailk.bi.system.common.LSInfoRegion"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>

<%
	if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,
			response))
		return;
%>
<%
String treeType = request.getParameter("treeType");
if(treeType == null || "".equals(treeType)){
	treeType = "Type_Level";
}
//登陆操作员信息
InfoOperTable sstUser = CommonFacade.getLoginUser(session);

%>
<HTML>
<head>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/icontent.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>

  <title>收藏夹管理</title>
  <META HTTP-EQUIV="pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
  <META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/xtree.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/xmlextras.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/xloadtree.js"></script>
  <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/other/xtree.css" />
  <script type="text/javascript">
	webFXTreeConfig.rootIcon		= "../images/common/xmltree/xp/root.gif";
	webFXTreeConfig.openRootIcon		= "../images/common/xmltree/xp/root.gif";
	webFXTreeConfig.fileIcon		= "../images/common/xmltree/xp/roledef.gif";
	webFXTreeConfig.lMinusIcon		= "../images/common/xmltree/xp/Lminus.png";
	webFXTreeConfig.lPlusIcon		= "../images/common/xmltree/xp/Lplus.png";
	webFXTreeConfig.tMinusIcon		= "../images/common/xmltree/xp/Tminus.png";
	webFXTreeConfig.tPlusIcon		= "../images/common/xmltree/xp/Tplus.png";
	webFXTreeConfig.iIcon			= "../images/common/xmltree/xp/I.png";
	webFXTreeConfig.lIcon			= "../images/common/xmltree/xp/L.png";
	webFXTreeConfig.tIcon			= "../images/common/xmltree/xp/T.png";
  </script>

</head>
<body bgcolor="white"> 

<TABLE cellpadding="0" WIDTH="95%" ><TR valign="top"><TD>
<script>
var tree = new WebFXLoadTree("收藏夹列表","createFavorTreeXML.rptdo");
document.write(tree);
</script>
</TD></TR></TABLE>
<script language=javascript>
function linkUrl(){
	
}
</script>

</body>
</html>
