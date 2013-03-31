<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
  String context=request.getContextPath();
%>  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加自定义指标成功</title>
<link rel="stylesheet" href="<%=context%>/css/other/css.css" type="text/css">
<SCRIPT language=javascript src="<%=context%>/js/tab_js.js"></SCRIPT>
<script type="text/javascript">
location.replace="rptMsuCustomSuccess.screen";
</script>
</head>
<body background="<%=context%>/images/dot4.gif" onUnload="opener.location.reload()">
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p><p>&nbsp;</p><table width="500" height="160" border="0" align="center" cellpadding="5" cellspacing="5" bgcolor="DCDCDC" class="wait">
  <tr> 
    <td bgcolor="#FFFFFF">
<table width="100%" border="0">
        <tr> 
          <td width="47%" rowspan="2"><img src="<%=context%>/images/success_icon.gif"></td>
          <td width="53%"class="redbold"> 添加自定义指标成功!</td>
        </tr>
        <tr> 
          <td valign="middle"> <input type="image" src="<%=context%>/images/manage_zb.gif" width="118" height="22" border="0" name="Close" onclick="window.location='<%=context%>/report/rptCustomMsuMaintain.screen';">
            <br>
            <br>
<!-- <input type="button" name="Close2" value="关闭" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" onclick="window.close();">  -->
          </td>
        </tr>
      </table></td>
  </tr>
</table>
</body>
</html>
