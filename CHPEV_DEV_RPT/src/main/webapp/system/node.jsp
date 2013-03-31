<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<HTML  XMLNS:IE>
<head>
  <title>树形管理1</title>
  <META HTTP-EQUIV="pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
  <META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/xtree.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/xmlextras.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/xloadtree.js"></script>
  <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/other/xtree.css" />
  <script type="text/javascript">
	webFXTreeConfig.rootIcon		= "../images/xmltree/images/xp/root.gif";
	webFXTreeConfig.openRootIcon		= "../images/xmltree/images/xp/root.gif";
	webFXTreeConfig.fileIcon		= "../images/xmltree/images/xp/roledef.gif";
	webFXTreeConfig.lMinusIcon		= "../images/xmltree/images/xp/Lminus.png";
	webFXTreeConfig.lPlusIcon		= "../images/xmltree/images/xp/Lplus.png";
	webFXTreeConfig.tMinusIcon		= "../images/xmltree/images/xp/Tminus.png";
	webFXTreeConfig.tPlusIcon		= "../images/xmltree/images/xp/Tplus.png";
	webFXTreeConfig.iIcon			= "../images/xmltree/images/xp/I.png";
	webFXTreeConfig.lIcon			= "../images/xmltree/images/xp/L.png";
	webFXTreeConfig.tIcon			= "../images/xmltree/images/xp/T.png";
  </script>
  
</head>
<body>
  <TABLE cellpadding="0" WIDTH="90%" >
    <TR valign="top">
      <TD>
        <script>          
          var tree = new WebFXLoadTree("节点列表","createNodeXML.rptdo");
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
