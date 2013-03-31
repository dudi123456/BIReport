<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@ page import="com.ailk.bi.base.util.*" %>
<%@ page import="com.ailk.bi.base.table.*" %>
<%@ page import="com.ailk.bi.system.common.*" %>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%
if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>

<html>
<head>
  <title>角色用户</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
  <script language="javascript">
    function winClose(){
      window.close();
    }
  </script>
  <%
    //roleID
    String role_code = request.getParameter("role_code");
    //role名称
    String role_name =CommTool.getParameterGB(request,"role_name");
    //区域
    String region_id = CommonFacade.getLoginUser(session).region_id;
    //取出角色信息
    InfoRoleTable roleInfo = LSInfoRole.getRoleInfo(role_code);

  %>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
  <table width="800" height="450" border="0" cellpadding="0" cellspacing="1" bgcolor="#999999" class="unnamed1">
    <tr>
      <td height="20" colspan="3"><div align="center">角色 <b><%=roleInfo.role_name%>  设置操作员</b></div></td>
    </tr>
    <tr>
      <td height="12" bgcolor="#999999" align="center"><b>区域</b></td>
      <td height="12" bgcolor="#999999" align="center">&nbsp;</td>
      <td height="12" bgcolor="#999999" align="center"><b>选择操作员</b></td>
    </tr>
    <tr>
      <td width="180" height="100%">
        <iframe name="l_newRoleUser" target="r_newRoleUser" frameborder="0" width="100%" height="100%"
          src="l_newRoleUser.jsp?role_code=<%=role_code%>&region_id=<%=region_id%>"  scrolling="Auto"></iframe>
      </td>
      <td width="10"  bgcolor="#FFFFFF">&nbsp;</td>
      <td width="620">
        <iframe name="r_newRoleUser" frameborder="0" width="100%" height="100%"
          src="r_newRoleUser.jsp?role_code=<%=role_code%>&role_name=<%=role_name%>&region_id=<%=region_id%>" scrolling="Auto"></iframe>
      </td>
    </tr>
    <tr>
      <td height="20" colspan="3" align="center">
    	<input type="button" value="保存" onclick="document.r_newRoleUser._addRoleUser();" style="cursor:hand;" >
    	<input type="button" value="关闭" onclick="window.close();" style="cursor:hand;" >
      </td>
    </tr>
  </table>
</body>
</html>
