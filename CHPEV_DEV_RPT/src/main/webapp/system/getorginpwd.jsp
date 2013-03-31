<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.base.table.InfoOperTable"%>
<%@ include file="/base/commonMeta.jsp"%>
<%@ include file="/base/commonStyle.jsp"%>
<html>
<head>
    <link rel="stylesheet" href="<%=context%>/css/icontent.css" type="text/css">
<script language="javascript">
	function copyPwd(pwd) {

		window.clipboardData.setData("Text", pwd);
		alert("复制成功!");
	}
</script>

<TITLE>获取的密码</TITLE>
<%
	InfoOperTable userInfo = (InfoOperTable) session
			.getAttribute("VIEW_TREE_LIST");
	if (userInfo == null) {
		userInfo = new InfoOperTable();
	}
%>
</head>
<BODY>
	<div id="maincontent">
		<div class="widget_content1">
			<div class="widget_tb_content_big1">
				<table align="center" border="0" width="60%">
					<tr>
						<td height="26" colspan="2" class="right">【获取密码】<font
							color="red"></font></td>
					</tr>

					<TR>
						<td height="30" align="right">账号：</td>
						<td align="left"><%=userInfo.user_id%>
						</td>
					</tr>
					<TR>
						<td height="30" align="right">姓名：</td>
						<td align="left"><%=userInfo.user_name%>
						</td>
					</tr>
					<TR>
						<td height="30" align="right">密码：</td>
						<td align="left"><font color="red"><%=userInfo.oper_pwd%></font>
						</td>
					</tr>

					<tr height="20px;">
						<td></td><td></td>
					<tr>
					<tr class="add_bottom">
						<td align="center" class="right" colspan="2"><input
							type="button" name="an11"
							onClick="javascript:copyPwd('<%=userInfo.oper_pwd%>');"
							value="复制密码" class="btn3"> &nbsp; <input type="button"
							class="btn3" value="关闭" name="btnclose" class="an11"
							onclick="javascript:window.close();">
						</td>
					</tr>
				</table>
			</div>
		</div>
		<script type="text/javascript">
			$(document).ready(function() {
				domHover(".btn3", "btn3_hover");
			});
		</script>
	</div>
</BODY>

</html>

