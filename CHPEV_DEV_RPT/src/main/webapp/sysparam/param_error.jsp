<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%
//
String info = CommTool.getParameterGB(request , "mess");
if(info == null || "".equals(info)){
	info = "请选择左侧功能点！";
}
//
String closeFlag = CommTool.getParameterGB(request , "closeFlag");
if(closeFlag == null || "".equals(closeFlag)){
	closeFlag = "0";
}
%>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统一经营分析系统</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">



<body <%if("1".equals(closeFlag)) out.print("onload=\"window.close();\""); %>>
<table width="100%" height="80%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td valign="bottom" colspan="2"></td>
</tr>
<tr> 
        <td  valign="bottom" colspan="2" align="center">
        <table width="75%" height="75%" border="0" cellpadding="0" cellspacing="0" class="squareB" >
                <tr> 
                  <td><img src="../images/common/tab/square_corner_1.gif" width="5" height="5"></td>
                  <td background="../images/common/tab/square_line_1.gif"></td>
                  <td><img src="../images/common/tab/square_corner_2.gif" width="5" height="5"></td>
                </tr>  
                <tr> 
                  <td background="../images/common/tab/square_line_2.gif"></td>
                  <td width="100%" height="100%" align="center" valign="middle">
              		<%=info%>
                  </td>                                      
                  <td background="../images/common/tab/square_line_3.gif"></td>
                </tr>                
                <tr> 
                  <td height="6"><img src="../images/common/tab/square_corner_3.gif" width="5" height="5"></td>
                  <td background="../images/common/tab/square_line_4.gif"></td>
                  <td><img src="../images/common/tab/square_corner_4.gif" width="5" height="5"></td>
                </tr>
              </table></td>
          </tr>
</table>
</body>
</html>
