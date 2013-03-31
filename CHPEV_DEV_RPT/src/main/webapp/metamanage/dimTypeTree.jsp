<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<HTML>
<head>
    <title>维度类型</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache, must-revalidate">
    <meta http-equiv="expires" content="0">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/xtree.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/xmlextras.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/xloadtree.js"></script>
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/other/xtree.css"/>
    <script type="text/javascript">
        webFXTreeConfig.rootIcon = "../images/common/xmltree/xp/root.gif";
        webFXTreeConfig.openRootIcon = "../images/common/xmltree/xp/root.gif";
        webFXTreeConfig.fileIcon = "../images/common/xmltree/xp/deptOpen.gif";
        webFXTreeConfig.lMinusIcon = "../images/common/xmltree/xp/Lminus.png";
        webFXTreeConfig.lPlusIcon = "../images/common/xmltree/xp/Lplus.png";
        webFXTreeConfig.tMinusIcon = "../images/common/xmltree/xp/Tminus.png";
        webFXTreeConfig.tPlusIcon = "../images/common/xmltree/xp/Tplus.png";
        webFXTreeConfig.iIcon = "../images/common/xmltree/xp/I.png";
        webFXTreeConfig.lIcon = "../images/common/xmltree/xp/L.png";
        webFXTreeConfig.tIcon = "../images/common/xmltree/xp/T.png";
    </script>
</head>
<body>
<table cellpadding="0" width="90%">
    <tr valign="top">
        <td>
            <script type="text/javascript">
                var tree = new WebFXLoadTree("维度类型", "<c:out value="${pageContext.request.contextPath}"/>/metamanage/createDimTypeTreeXml.jsp?dim_type_id=-1");
                document.write(tree);
            </script>
        </td>
    </tr>
</table>
</body>
</html>

