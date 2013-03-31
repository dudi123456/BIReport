<%@page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>

<html>
<head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">

</head>
<body>
<form method="post" action="tableDef.rptdo" >
<table width="100%" height="100%" >
	<tr height="20">
		<td colspan="2">
		<table cellspacing="0" cellpadding="0" width="100%"
			border="0" >
        <tr>
          <td valign="bottom" class="pageTitle" nowrap>子系统管理</td>
          <td width="2%"><span class="handin"></span></td>

         <td width="85%" valign="bottom"  border=0>
		<img src="../images/common/system/arrow7.gif" width="7" height="7">
		<span class="bulefont">你所在位置： </span>
		<a href="javascript:;">系统管理</a>
		&gt;&gt; <a href="javascript:;">权限管理</a>
        &gt;&gt; <a href="javascript:;">子系统管理</a>
          </td>
          <td width="10%" valign="bottom" nowrap>&nbsp;</td>
        </tr>
        <tr>
          <td height="1" colspan="4" >
          <td colspan="2"  background="../images/common/system/black-dot.gif"></td>
        </tr>
        </table>
		</td>
	</tr>
	<tr>
		<td width="20%" height="100%" valign="top"><iframe id="system_frame"
		name="system_frame" width="100%" height="100%"
		frameborder="0" marginwidth="0" marginheight="0" scrolling="no"
		src="subSysTree.jsp"> </iframe></td>
		<td width="80%" height="100%" valign="top"><iframe id="systemlist_frame"
		name="systemlist_frame" width="100%" height="100%"
		frameborder="0" marginwidth="0" marginheight="0" scrolling="no"
		src="system.rptdo"> </iframe></td>
	</tr>
</table>
</form>
</body>
</html>