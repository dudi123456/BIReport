<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="com.ailk.bi.login.dao.IUserDao"%>
<%@page import="com.ailk.bi.login.dao.impl.UserDaoImpl"%>
<%@ page import="com.ailk.bi.base.table.InfoOperTable"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%
IUserDao userDao = new UserDaoImpl();
InfoOperTable loginUser = CommonFacade.getLoginUser(session);
String[][] systemUser = userDao.getCurrentOnlineUser(loginUser.system_id);

int userCount = 0;
if (systemUser != null){
	userCount = systemUser.length;
}

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>在线用户</title>
<link href="/BIWeb/css/other/bimain.css" rel="stylesheet" type="text/css" />
<link href="/BIWeb/css/tablecss/main.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="/BIWeb/js/prototype.lite.js"></script>
<body class="main-body">
<table style="width: 99%;" height="100%" align="center">
	<tr>
		<td valign="top" width="50%" style="padding-right:5px;">
		<!--日指标分析开始-->
		<table style="width: 100%">
			<tr>
				<td class="titlebg2" width="151"><font class="title1">在线用户</font></td>
				<td align="right" class="titlebg2_line"><table>
<TD><table>
<TD>&nbsp;共<%=userCount%>人在线</TD>
</table>
</TD>
</table>
</td>
			</tr></table>
			<table style="width: 100%">
			<tr>
				<td colspan="2">
					<table style="width: 100%"  class="datalist">
						<tr class="celtitle">
							<td class="leftdata" width="35%">用户ID</td>
							<td>用户名称</td>
						</tr>
						<%for (int i=0;i<userCount;i++){
						%>
						<tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
							<td class="leftdata"><%=systemUser[i][0]%></td>
							<td class="leftdata"><%=systemUser[i][1]%></td>
							
							
						</tr>
						<%}%>
					</table>
				</td>
			</tr>
		</table>

			</td>
			</tr>
			</table>
</body>
</html>