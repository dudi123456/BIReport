<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.base.util.*" %>
<%@ page import="com.ailk.bi.system.common.*" %>
<%
if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>
<HTML>
<head>
  <title>树形管理</title>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/xtree.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/xmlextras.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/xloadtree.js"></script>
  <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/other/xtree.css" />
  <script type="text/javascript">
	webFXTreeConfig.rootIcon		= "../images/common/xmltree/xp/root.gif";
	webFXTreeConfig.openRootIcon		= "../images/common/xmltree/xp/root.gif";
	webFXTreeConfig.folderIcon		= "../images/common/xmltree/xp/region.gif"
	webFXTreeConfig.openFolderIcon		= "../images/common/xmltree/xp/regionOpen.gif";
	webFXTreeConfig.fileIcon		= "../images/common/xmltree/xp/regiondef.gif";
	webFXTreeConfig.lMinusIcon		= "../images/common/xmltree/xp/Lminus.png";
	webFXTreeConfig.lPlusIcon		= "../images/common/xmltree/xp/Lplus.png";
	webFXTreeConfig.tMinusIcon		= "../images/common/xmltree/xp/Tminus.png";
	webFXTreeConfig.tPlusIcon		= "../images/common/xmltree/xp/Tplus.png";
	webFXTreeConfig.iIcon			= "../images/common/xmltree/xp/I.png";
	webFXTreeConfig.lIcon			= "../images/common/xmltree/xp/L.png";
	webFXTreeConfig.tIcon			= "../images/common/xmltree/xp/T.png";
  </script>
  <%
    String role_code = CommTool.getParameterGB(request,"role_code");//角色ID
    String region_id = CommTool.getParameterGB(request,"region_id");//登陆操作员区域ID
    region_id = "0";
    String tree_name = LSInfoRegion.getRegionName(region_id);
  %>
</head>
<body>
  <TABLE cellpadding="0" WIDTH="90%" >
    <TR valign="top">
      <TD>
        <script>
          var tree = new WebFXLoadTree("<%=tree_name%>","createLNewRoleUserXML.rptdo?parent_id=<%=region_id%>&role_code=<%=role_code%>");
          document.write(tree);
        </script>
      </TD>
    </TR>
  </TABLE>
  <script language=javascript>
    function linkUrl(){
	window.parent.r_newRoleUser.location="r_newRoleUser.jsp?role_code=<%=role_code%>&region_id=<%=region_id%>";
    }
  </script>
</body>
</html>
