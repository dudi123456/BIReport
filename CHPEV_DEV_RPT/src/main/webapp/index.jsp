<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="java.io.File"%>
<%
//登录等待页面
String fileName = "/wait.htm";
File waitHtm = new File(session.getServletContext().getRealPath(fileName));
if(waitHtm.exists() && waitHtm.isFile()){
	String path = request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>联通经营分析系统</title>
<link href="<%=path%>/css/css.css" rel="stylesheet" type="text/css">
</head>
<body class="login-bg">
<table width="100%" height="100%" border="0">
  <tr>
    <td height="56">&nbsp;</td>
  </tr>
  <tr>
    <td height="407">
      <table width="765" height="405" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
          <td>
			<table width="100%" height="100%" border="0">
              <tr>
                <td width="55%" align="center"><img src="<%=path%>/images/login_logo.gif" width="250" height="144"></td>
                <td width="4%"><img src="<%=path%>/images/login_2.gif" width="15" height="293"></td>
                <td> <table width="100%" border="0">
                    <tr><td>
                    <jsp:include page="<%=fileName%>" flush="true"/>
                    </td></tr>
                  </table></td>
              </tr>
              <tr align="center">
                <td colspan="3" align="right"><img src="<%=path%>/images/login_1.gif" width="725" height="54"></td>
              </tr>
            </table></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td height="25" align="center" class="login-text" >经营分析系统 </td>
  </tr>
</table>
</body>
</html>
<%
} else if(true){
	response.sendRedirect("login/login.jsp");
} else{
	String js = "<script languag='javascript'>\n";
	js += "var optstr = \"status=no,toolbar=no,menubar=no,directions=no,resizable=yes,fullscreen=no,scrollbars=no,location=no\"\n";
	js += "var win = window.open(\"main/main.jsp\",\"unicom\",optstr);\n";
    	js += "if(win != null) win.focus();\n";
    	js += "</script>\n";
    	out.println(js);
}

%>


