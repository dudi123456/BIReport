<html>

<head>

<title>经营分析系统</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page isErrorPage="true"%>
<link rel="stylesheet" href="style.css" type="text/css">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate">
<meta http-equiv="expires" content="Wed, 26 Feb 1997 08:21:57 GMT">
<meta http-equiv="expires" content="0">

<%
	response.addHeader("Cache-Control", "no-cache");
	response.addHeader("Expires", "Thu, 01 Jan 1970 00:00:01 GMT");
%>

</head>



<body text="#000000">

<form action="2.htm">

<p>&nbsp;</p>

<p>&nbsp;</p>

<p>&nbsp;</p>

<table width="95%" border="0" cellspacing="0" cellpadding="6"
  align="center">

  <tr>

    <td>

    <table border="0" cellspacing="10" cellpadding="0" align="center"
      bgcolor="8DAAD8" bordercolor="#000000">
      <tr>
        <td width="60" height="60" bgcolor="#FFFFFF" align="center"><img
          src="../biimages/fail.gif" width="48" height="48" alt=""></td>
        <td width="250" bgcolor="#FFFFFF" align="center"><font
          color="#FF0000">由于你的操作不当，系统出错，请与系统管理员联系<br>
        <%=exception.getMessage()%><br>
        Stack Trace is : <font color="red">
				<%
					java.io.CharArrayWriter cw = new java.io.CharArrayWriter();
					java.io.PrintWriter pw = new java.io.PrintWriter(cw, true);
					exception.printStackTrace(pw);
					out.println(cw.toString());
				%>
 				</font> <br>
        </font></td>
      </tr>
    </table>
    </td>
  </tr>
</table>
<table width="95%" border="0" cellspacing="0" cellpadding="6"
  align="center">

  <tr>

    <td align="center"><img border="0" name="imageField42"
      src="../biimages/goback.gif" width="80" height="22"
      onclick="javascript:history.back();" alt=""></td>

  </tr>

</table>

<p>&nbsp;</p>

</form>

<p>&nbsp;</p>
</body>

</html>

