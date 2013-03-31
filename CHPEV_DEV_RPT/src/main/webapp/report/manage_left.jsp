<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>菜单</title>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=JavaScript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);
//-->
</script>
</head>

<body>
<div id="Layer1" style="position:absolute; width:100%; height:100%; z-index:1;"> 
  <table height="100%"  border="0" align="right" cellpadding="0" cellspacing="0">
    <tr> 
      <td background="../biimages/flex.gif" onMouseOver="switchSrc(leftFrameSwitcher)" onMouseOut="switchSrc(leftFrameSwitcher)" onClick="switchLeftFrame(150)" style="cursor:hand"><img src="../biimages/toleft.gif" width="6" height="50" id="leftFrameSwitcher"></td>
    </tr>
  </table>
</div>
<table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td height="100%" valign="top"> <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0">
        <tr> 
          <td>
		    <table cellpadding=0 cellspacing=0 width="100%">
            </table>
		  </td>
        </tr>
        <tr> 
          <td height="100%" valign="top">
		  <iframe frameborder="0" name="treeIframe" src="manage_left_tree.jsp?type=<%=request.getParameter("type")%>" width="100%" height="100%" scrolling="no"></iframe></td>
       </tr>
      </table>
    </td>
    <td><img src="../biimages/size.gif" width="6" height="1"></td>
  </tr>
</table>
</body>
</html>
