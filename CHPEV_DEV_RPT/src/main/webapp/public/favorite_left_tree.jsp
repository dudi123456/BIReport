<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%
	if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,
			response))
		return;
%>
<HTML>
<head>
  <title>报表发布角色</title>
  <META HTTP-EQUIV="pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
  <META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/xtree.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/xmlextras.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/xloadtree.js"></script>
  <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/other/xtree.css" />
  <script type="text/javascript">
	webFXTreeConfig.rootIcon		= "../biimages/xmltree/biimages/xp/root.gif";
	webFXTreeConfig.openRootIcon	= "../biimages/xmltree/biimages/xp/root.gif";
	webFXTreeConfig.fileIcon		= "../biimages/xmltree/biimages/favorite/file.gif";
	webFXTreeConfig.lMinusIcon		= "../biimages/xmltree/biimages/xp/Lminus.png";
	webFXTreeConfig.lPlusIcon		= "../biimages/xmltree/biimages/xp/Lplus.png";
	webFXTreeConfig.tMinusIcon		= "../biimages/xmltree/biimages/xp/Tminus.png";
	webFXTreeConfig.tPlusIcon		= "../biimages/xmltree/biimages/xp/Tplus.png";
	webFXTreeConfig.iIcon			= "../biimages/xmltree/biimages/xp/I.png";
	webFXTreeConfig.lIcon			= "../biimages/xmltree/biimages/xp/L.png";
	webFXTreeConfig.tIcon			= "../biimages/xmltree/biimages/xp/T.png";
	webFXTreeConfig.blankIcon       = "../biimages/xmltree/biimages/blank.png";
  </script>

</head>
<body>
  <TABLE cellpadding="0" WIDTH="90%" >
    <TR valign="top">
      <TD>
        <script>
          var tree = new WebFXLoadTree("收藏夹","createFavoriteTreeXML.rptdo");
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
