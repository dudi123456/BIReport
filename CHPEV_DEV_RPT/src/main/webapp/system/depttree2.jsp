<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.base.table.*" %>
<%@ page import="com.ailk.bi.base.util.*" %>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%
if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>

<%
//登陆操作员
InfoOperTable loginUser = CommonFacade.getLoginUser(session);
//登陆操作员区域
String dept_id = loginUser.dept_id;
//区域名称
String tree_name = "选择部门";
%>
<HTML>
<head>
<title>部门管理</title>
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
<META HTTP-EQUIV="expires" CONTENT="0">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/xtree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/xmlextras.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/xloadtree.js"></script>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/other/xtree.css" />
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
	webFXTreeConfig.folderIcon		= "../images/common/xmltree/xp/dept.gif";
	webFXTreeConfig.openFolderIcon		= "../images/common/xmltree/xp/deptOpen.gif";
	webFXTreeConfig.fileIcon		= "../images/common/xmltree/xp/deptdef.gif";
</script>
</head>
<body>

<TABLE cellpadding="0" WIDTH="90%" ><TR valign="top"><TD>
<script>
var tree = new WebFXLoadTree("<%=tree_name%>","createSysDeptTreeXML2.rptdo?user_id=<%=loginUser.user_id%>");
document.write(tree);
</script>
</TD></TR></TABLE>
<script language=javascript>
function linkUrl(){

}
</script>

</body>
</html>

