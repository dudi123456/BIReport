<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.ailk.bi.base.util.WebConstKeys"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统一经营分析系统</title>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
</head>

<body class="main-body" background="<%=request.getContextPath()%>/biimages/dot4.gif">
<br>
<br>
<br>
<br>
<br>
<br>
<table width="450" height="140" border="0" align="center"
	cellpadding="5" cellspacing="5" bgcolor="DCDCDC" class="wait">
	<tr>
		<td bgcolor="F9F9F9">
		<table width="100%" border="0">
			<tr>
				<td width="20%" align="right"><img src="<%=request.getContextPath()%>/biimages/error.gif"
					width="70" height="70"></td>
				<td align="center" valign="middle">操作员没有正常登录或者已经过期 !请您重新登入。<br>
				<br>
				<!-- <input type="reset" name="Submit2" class="button"
					onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
					value="返回"
					onClick="javascript:top.location='<%=WebConstKeys.BSS_URL%>'">-->
				</td>
				<td width="10%" valign="middle">&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html>

