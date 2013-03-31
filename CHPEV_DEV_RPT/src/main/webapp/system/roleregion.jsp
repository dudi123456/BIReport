<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.base.table.*"%>
<%@ page import="com.ailk.bi.base.struct.*"%>
<%@ page import="com.ailk.bi.system.common.*"%>

<%
	//角色ID
	String role_id = CommTool.getParameterGB(request, "role_id");
	if (role_id == null) {
		role_id = "";
	}
	//角色名称
	String role_name = CommTool.getParameterGB(request, "name");
	if (role_name == null) {
		role_name = "";
	}

	//角色权限
	String o_Str = CommTool.getParameterGB(request, "o_Str");
	if (o_Str == null) {
		o_Str = "";
	}
	//取出角色信息
	InfoRoleTable roleInfo = LSInfoRole.getRoleInfo(role_id);
	if (roleInfo == null) {
		roleInfo = new InfoRoleTable();
	}

	//判断提交类型
	String submitType = request.getParameter("submitType");
	if (submitType == null) {
		submitType = "0";
	}
%>

<HTML>
<HEAD>
<TITLE>角色控制区域</TITLE>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
<SCRIPT LANGUAGE=javascript src="SelVal.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<SCRIPT LANGUAGE=javascript>
  var o_Str = "";

  function clickCheck(obj){
  　
	pos = o_Str.indexOf("," + obj.value + ";");
	if(pos != -1){
		o_Str = o_Str.substr(0, pos) + o_Str.substr(pos + obj.value.length + 3);
	}
	//alert(obj.checked);
	if(obj.checked){
		o_Str += "," + obj.value + ";1";
	}
	else{
		o_Str += "," + obj.value + ";0"
	}//alert(o_Str);
  }

  function _frmSubmit(){
	//alert(o_Str.length);
	document.hdform.o_Str.value=o_Str;
	document.hdform.submit();
  }

  function _frmReset(){
	document.location="roleregion.jsp?role_id=<%=role_id%>&role_name=<%=role_name%>";
  }
</SCRIPT>
</HEAD>

<BODY class="side-7">
<table width="100%">
	<tr>
		<td height="22"><img src="../images/common/system/arrow7.gif" width="7"
			height="7"> <b><%=roleInfo.role_name%></b>&nbsp;角色信息</td>
		<TD height="22" align="right"><!--<img
			src="../images/SELTREE/region0.gif"><b>[<font
			color="#CC0099">没有下级区域</font>]</b>--></td>
		<td height="22" align="left"><!--<img
			src="../images/SELTREE/region1.gif"><b>[<font
			color="#CC0099">有下级区域</font>]</b>--></TD>
	</tr>
	<tr>
		<td height="1" background="../images/common/system/black-dot.gif" colspan="3"></td>
	</tr>

	<%
		//数据库处理
		int ErrNo = 0;
		if ((o_Str != null) && !"".equals(o_Str)) {
			o_Str = o_Str.substring(1); //去掉第一个","
			String[] o_Arr = o_Str.split(",");
			Vector rgChkArr = new Vector();
			for (int i = 0; i < o_Arr.length; i++) {
				String str = o_Arr[i];
				int pos = str.indexOf(";");
				KeyValueStruct keyvalue = new KeyValueStruct(str.substring(
				0, pos), str.substring(pos + 1));

				rgChkArr.add(keyvalue);
			}
			ErrNo = LSInfoRole.setRoleRegions(request, role_id, rgChkArr);

			if (ErrNo < 0) {
				out
				.println("<table width=\"100%\"><tr><td width=\"100%\" align=\"center\"><font color=red>修改失败！</font></td></tr></table>");
			} else {
				out
				.println("<table width=\"100%\"><tr><td width=\"100%\" align=\"center\"><font color=red>修改成功！</font></td></tr></table>");
			}
		}
	%>
</table>
<TABLE width="100%" height="85%" frameborder="0" marginwidth="0"
	marginheight="0" align='center' valign='top'>
	<TR>
<TD><IFRAME name='RegionFrame'	src="roleRgChkListClient.jsp?role_id=<%=role_id%>&pageRight=<%=11%>"
			width="100%" height="100%" SCROLLING="auto"></IFRAME></TD>
	</TR>
	<%
	if (true) {
	%>
	<TR height=30>
		<TD align=center valign=bottom><INPUT type=button value="保 存"
			class="button" onClick="_frmSubmit();"> <INPUT type=button
			value="重 置" class="button" onClick="_frmReset();"></TD>
	</TR>
	<%
	}
	%>
<form action="roleregion.jsp" name="hdform" method="post">
<input name="role_id" type="hidden" value="<%=role_id%>"/>
<input name="role_name" type="hidden" value="<%=role_name%>"/>
<input name="o_Str" type="hidden" value=""/>
</form>
</TABLE>
</BODY>
</HTML>






