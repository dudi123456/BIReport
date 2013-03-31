<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统一经营分析系统</title>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=JavaScript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
</head>

<body>
<div id="Layer1" style="position:absolute; width:100%; height:100%; z-index:1">
  <table height="100%"  border="0" align="right" cellpadding="0" cellspacing="0">
    <tr> 
      <td background="../images/common/system/flex.gif" onMouseOver="switchSrc(leftFrameSwitcher)" onMouseOut="switchSrc(leftFrameSwitcher)" onClick="switchLeftFrame(150)" style="cursor:hand">
        <img src="../images/common/system/toleft.gif" width="6" height="50" id="leftFrameSwitcher">
      </td>
    </tr>
  </table>
</div>
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td width="100%" valign="top"> 
      <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0">
          <td height="100%" valign="top">
		  <iframe frameborder="0" name="treeIframe" src="param_tree.jsp" width="100%" height="100%" scrolling="auto"></iframe></td>
        </tr>
      </table>
    </td>
    <td><img src="../images/common/system/size.gif" width="0" height="1"></td>
  </tr>
</table>
</body>
</html>
