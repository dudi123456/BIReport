<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
	String context = request.getContextPath();
	String location = request.getParameter("location");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>经营分析系统</title>
<SCRIPT language=javascript src="<%=context%>/js/dojo.js"></SCRIPT>
<SCRIPT language=javascript src="<%=context%>/js/wait.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css"
	type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/kw.css" type="text/css">

<script type="text/javascript">
    function load(){
		window.location="<%=location%>";
	}
</script>
<body class="main-body">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>&nbsp;</td>
	</tr>
</table>
<script type="text/javascript">
    ShowWait();
    setTimeout(load,5);
</script>
</body>
</html>

