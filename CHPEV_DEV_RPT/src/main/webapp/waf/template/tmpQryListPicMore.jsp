<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template" %>
<html>
<%
if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统一经营分析系统</title>
<!-- javascript区域 -->
<template:insert parameter="java_script"/>
</head>
<body class="main-body"	onLoad="selfDisp();pageOnLoad();">

<template:insert parameter="bar"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td> 
        <tr> 
          <td class="blue-side-3"><table width="100%" border="0" cellpadding="0" cellspacing="0">
              
              <tr> 
                <td><table width="100%" height="20" border="0" cellpadding="0" cellspacing="0">
                    <tr> 
                      <td align="center" class="black-title">
					  <template:insert parameter="title"/>
					  </td>
                    </tr>
                    <tr> 
                      <td>
					  <frame>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<form  name="tableQryForm" action=""  method="POST" >
						<input type="hidden" name="next_page">
						<input type="hidden" name="oper_type">
						<input type="hidden" name="page_value">
						<tr>
							<td valign="top" width="100%">
							<%--查询区域--%>
							<template:insert parameter="query_jsp"/>
							</td>
						</tr>
						</form>
						</table>
					  </frame>
					  </td>
                    </tr>
                    <tr> 
                      <td>
                      <!--显示-->
						<frame>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<form  name="tableListForm" action="" method="POST">
						<input type="hidden" name="next_page">
						<input type="hidden" name="oper_type">
						<input type="hidden" name="page_value">
						<tr>
						<td width="100%">
						<%--第一显示区域，通常为用户信息--%>
							<template:insert parameter="disp_list_A"/>
						</td>
						</tr>
						<tr>
						<td width="100%">
						<%--第二显示区域，通常为用户信息--%>
							<template:insert parameter="disp_list_B"/>
						</td>
						</tr>
						<tr>
						<td width="100%">
						<%--第三显示区域，通常为用户信息--%>
							<template:insert parameter="disp_list_C"/>
						</td>
						</tr>
						</form>
						</table>
						</frame>
                </td>
              </tr>
              </table></td>
        </tr>
      </table>
      </td>
  </tr>
</table>
</body>
</html>
