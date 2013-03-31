<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<HTML>
<%
if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>
<HEAD>
<TITLE>用户组视图权限控制</TITLE>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<script language="JavaScript" src="<%=request.getContextPath()%>/js/js.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript">
function pageRedirect(code,name){
  window.list.location="TemplateListView.rptdo?group_id=" +code + "&group_name=" + name;
}
</script>
<head>
<body>
<table cellspacing="0" cellpadding="0" width="100%" height="100%" border="0">
	<tr>
		<td colspan="2">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="1" background="../images/black-dot.gif" colspan="2"></td>
	</tr>
	<tr>
		<td height="2" colspan="2"></td>
	</tr>
	<tr>
		<td width="20%" height="100%" valign="top">
		<iframe id="tree" name="tree" width="100%" height="100%" border="1"
			frameborder="1" marginwidth="0" marginheight="0" scrolling="auto"
			src="GroupTemplateTree.screen"></iframe>
		</td>
		<td width="80%" height=100%>
		<iframe id="list" name="list" width="100%" height="100%" frameborder="0"
			marginwidth="0" marginheight="0" scrolling="auto"
			src="GroupTemplateWelCome.screen"></iframe>
		</td>
	</tr>
</table>
</body>
</html>