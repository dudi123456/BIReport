<%@page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增报表</title>
<link rel="stylesheet" href=<%=request.getContextPath()%>/css/other/tab_css.csss" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
</head>

<body background="../biimages/xtgl-pic3.jpg" class="welcome-xtgl-bg">
<table width="100%" border="0">
  <tr>
    <td colspan="2" class="welcome-title">新增表报</td>
  </tr>
  <tr>
    <td colspan="2"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="welcome-title-underline">
        <tr>
          <td><img src="../biimages/size.gif" width="1" height="1"></td>
        </tr>
    </table></td>
  </tr>
  <tr>
    <td width="50%" align="center"><a href="editLocalReport.rptdo?opType=step1&opSubmit=addLocalMeta"><img src="../biimages/zb.gif" width="235" height="48" border="0" onMouseOver="switchSrc(this)" onMouseOut="switchSrc(this)" alt="此类报表认证后进入私有报表，自己可以分发给别人浏览"></a></td>
    <td height="89"><a href="editLocalReport.rptdo?opType=step1&opSubmit=addLocal"><img src="../biimages/fzb.gif" width="235" height="48" border="0" onMouseOver="switchSrc(this)" onMouseOut="switchSrc(this)" alt="此类报表认证后进入系统报表，一般由系统管理员进行维护"></a></td>
  </tr>
</table>
</body>
</html>