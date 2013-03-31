<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.system.common.LSInfoUser" %>
<%@ page import="com.ailk.bi.base.table.InfoOperTable"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%
    response.setHeader("Cache-Control", "no-stored");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    String user_id = request.getParameter("user_id");
    InfoOperTable loginUser = CommonFacade.getLoginUser(session);
    String biSys = LSInfoUser.getBiSys();
    String[][] info = (String[][])LSInfoUser.getLoUser(user_id,biSys,loginUser.system_id);
    String bi_user = "";
    String lo_user = "";
	if(biSys.equals(loginUser.system_id)) {
		bi_user = "value='"+user_id+"' readOnly";
	}else {
		lo_user = "value='"+user_id+"' readOnly";
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache, must-revalidate"/>
    <meta http-equiv="expires" content="0"/>
    <title>子系统用户关系</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
    <script type="text/javascript">
         function add() {
            if (form.bi_user.value == '') {
                alert('请输入用户账号！');
                form.bi_user.focus();
                return;
            }
            if (form.lo_user.value == '') {
                alert('请输入用户账号！');
                form.lo_user.focus();
                return;
            }
            this.form.submit();
        }       

        function del() { 
            form.method.value = "delete";
            this.form.submit();
        }

    </script>
</head>
<body >
<form name="form" method="post" action="userview.rptdo">
<input type="hidden" name="submitType" id="submitType"  value="addLoUser"/>
<input type="hidden" name="sys_user" value="<%=user_id %>">
<table width="100%">
	<tr>
		<td height="22"><img src="../biimages/arrow7.gif" width="7"
			height="7">&nbsp;<b>子系统用户关系</b></td>
	</tr>
	<tr>
		<td height="1" background="../biimages/black-dot.gif"></td>
	</tr>
	
</table>
<TABLE bgColor="#999999" WIDTH="60%" CELLPADDING="4" border="0" cellspacing="1">
<TR bgColor="#ffffff">
	<td>bi用户</td>
	<td>对应用户</td>
	<td>操作</td>
</tr>
<%for(int i=0;info !=null &&i<info.length;i++) { %>
<TR bgColor="#ffffff">
	<td><%=info[i][0] %></td>
	<td><%=info[i][1] %></td>
	<td><a href="userview.rptdo?submitType=delLoUser&sys_user=<%=user_id %>&bi_user=<%=info[i][0] %>&lo_user=<%=info[i][1] %>">删除</a></td>
</tr>
<%} %>
</table>
<br>
<br>
 <TABLE bgColor="#999999" WIDTH="60%" CELLPADDING="4" border="0" cellspacing="1">
    <TR bgColor="#ffffff">
        <td align="left">bi用户<input type="text" id="bi_user" name="bi_user" class="ormalField2"  <%=bi_user %> /></td>
		<td align="left">对应用户<input type="text" id="lo_user" name="lo_user" class="ormalField2"  <%=lo_user %>   /></td>
	</tr>  
<tr bgColor="#ffffff" align="center">
	<td colspan="2">
     	<input type="button" class="button" value="新增" id="addBtn" onclick="add()"/>
	</td>
</tr> 
</table>

</form>
</body>
</html>
