<%@page contentType="text/html; charset=UTF-8" %>

<html>

<%
String context = request.getContextPath();
%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>操作提示</title>
<SCRIPT language=javascript src="<%=context%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=javascript src="<%=context%>/js/js.js"></SCRIPT>
<link rel="stylesheet" href="<%=context%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=context%>/css/ilayout.css"
	type="text/css">
<script src="<%=context%>/js/jquery.min.js"></script>
<script src="<%=context%>/js/jquery.bi.js"></script>
</head>
<%
  String dealInfo = (String)request.getAttribute("CONST_SUBJECT_DEAL_INFO");

%>
<body class="main-body" background="<%=context%>/biimages/dot4.gif">
<table width="100%" height="100%" border="0">
	<tr>
		<td valign="middle">
		<table width="100%" height="160" border="0" align="center"
			cellpadding="5" cellspacing="5" bgcolor="DCDCDC" class="wait">
			<tr>
				<td bgcolor="F9F9F9">
				<table width="100%" border="0">
					<tr>
						<td width="25%" align="right">
						<img src="<%=context%>/images/popedom.gif" width="73" height="70">

						</td>
						<td width="62%" align="center" valign="middle">
						<font size=2><b><%=dealInfo%></b></font>
 <br>
						<br>

						</td>
						<td width="13%" valign="middle">&nbsp;</td>
					</tr>
					<tr><td width="25%" align="center" colspan="3">
						<input type="button" name="Submit4" value="关闭" class="btn" onClick="javascript:top.closeBlock()"></td></tr>
				</table>
				</td>
			</tr>
		</table>

		</td>
	</tr>
</table>
	<script type="text/javascript">
		$(document).ready(function() {
			domHover(".btn", "btn_hover");
		});
	</script>
</body>
<script>
<!--

function keySpace(){
  if (event.keyCode==32)
    document.img_btn.onclick();
}
document.onkeypress=keySpace
//-->
</script>
</html>

