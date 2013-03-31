<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.ailk.bi.base.struct.UserCtlRegionStruct"%>
<%@ page import="com.ailk.bi.base.util.*" %>
<%
//登陆操作员区域
String loginRegion = "";
UserCtlRegionStruct region = (UserCtlRegionStruct)session.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
if("1".equals(region.ctl_lvl)) {
	loginRegion = region.ctl_metro_str;
}else {
	loginRegion = region.ctl_city_str_add;
}
%>
<HTML>
<head>
<title>菜单管理</title>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META HTTP-EQUIV="Expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
<META HTTP-EQUIV="Expires" CONTENT="0">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/xtree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/xmlextras.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/xloadtree.js"></script>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/other/xtree.css" />
<script type="text/javascript">
	webFXTreeConfig.rootIcon		= "../images/common/xmltree/xp/root.gif";
	webFXTreeConfig.openRootIcon	= "../images/common/xmltree/xp/root.gif";
	webFXTreeConfig.fileIcon		= "../images/common/xmltree/xp/deptOpen.gif";
	webFXTreeConfig.lMinusIcon		= "../images/common/xmltree/xp/Lminus.png";
	webFXTreeConfig.lPlusIcon		= "../images/common/xmltree/xp/Lplus.png";
	webFXTreeConfig.tMinusIcon		= "../images/common/xmltree/xp/Tminus.png";
	webFXTreeConfig.tPlusIcon		= "../images/common/xmltree/xp/Tplus.png";
	webFXTreeConfig.iIcon			= "../images/common/xmltree/xp/I.png";
	webFXTreeConfig.lIcon			= "../images/common/xmltree/xp/L.png";
	webFXTreeConfig.tIcon			= "../images/common/xmltree/xp/T.png";
</script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></head>
<body>
<TABLE cellpadding="0" WIDTH="90%" >
<TR valign="top">
<TD>
<script>
var tree = new WebFXLoadTree("区域树","CreateRegionRoleXML.rptdo?first=<%=loginRegion%>");
document.write(tree);
</script>
</TD>
</TR>
</TABLE>

<script language=javascript>
function linkUrl(){

}
</script>

</body>
</html>

