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
<script> djConfig = { parseWidgets: false, searchIds: ["dojoPopup"] }; </script>
<link rel="stylesheet" href="<%=context%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=context%>/css/other/tab_css.css"
	type="text/css">
<style type="text/css">
  body { font-family : sans-serif; }
  .dojoDialog {
    background : #eee;
    border : 1px solid #999;
    -moz-border-radius : 5px;
    padding : 4px;
  }

  form {
    margin-bottom : 0;
  }

  /* group multiple buttons in a row */
  .box {
    display: block;
    text-align: center;
  }
  .box .dojoButton {
    float: left;
    margin-right: 10px;
  }
  .dojoButton .dojoButtonContents {
    font-size: medium;
  }

</style>
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

