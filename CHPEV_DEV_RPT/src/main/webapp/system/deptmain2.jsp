<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<HTML>
<%
if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>
<HEAD>
<TITLE>部门维护</TITLE>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
<SCRIPT src="<%=request.getContextPath()%>/js/common.js"></SCRIPT>
<SCRIPT src="<%=request.getContextPath()%>/js/tabpane.js"></SCRIPT>
<script language="JavaScript" src="<%=request.getContextPath()%>/js/js.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<STYLE>
@media all
{
	cool\:bar{
	   	behavior: url("../htc/coolbar_js.htc");
	        padding:1px
        }

	cool\:button{
		behavior: url("../htc/coolbutton_js.htc");
	        text-align: left
	}
}
</STYLE>
<link id="luna-tab-style-sheet" type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/other/tab.css" />
<head>
<body  style="margin:0">
<form action="" method="post" name="form1">
<table cellspacing="0" cellpadding="0" width="100%" height="100%" border="0">

	<tr height="22">
    <td height="20"  colspan="2">

    </td>
	</tr>

	<tr>
		<td width="25%" height="100%" valign="top">
		<iframe id="DeptList" name="DeptList" width="100%" height="100%" border="1"
			frameborder="1" marginwidth="0" marginheight="0" scrolling="auto"
			src="depttree2.jsp"></iframe>
	   </td>
       <td  width="75%" height="100%">
       <iframe id="DeptView" name="DeptView" width="100%" height="100%" frameborder="0"
			marginwidth="0" marginheight="0" scrolling="auto"
			src="deptview2.jsp?oper_type=1"> </iframe>
	 </td>
	</tr>

 </table>
</form>
</body>
</html>