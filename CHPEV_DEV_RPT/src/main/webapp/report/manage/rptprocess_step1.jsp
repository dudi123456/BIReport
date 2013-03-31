<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.report.domain.RptProcessTable"%>

<HTML>
<HEAD>
<TITLE>流程基本信息</TITLE>
<LINK REL="stylesheet" TYPE="text/css" HREF="<%=request.getContextPath()%>/css/other/css.css">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/picker/deptPicker.js"></SCRIPT>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
</HEAD>
<%
//审核流程信息
RptProcessTable pTable = (RptProcessTable) session.getAttribute(WebKeys.ATTR_REPORT_PROCESS);
if(pTable==null){
	pTable = new RptProcessTable();
	pTable.status = "Y";
}
//提交类型
String submit = "";
if("".equals(pTable.p_id)){
	submit="insert";
}else{
	submit="update";
}
%>
<BODY>
<FORM name="editForm" action="rptProcess.rptdo" method="POST">
<table width="100%" height="100%" border="0" cellpadding="0"
	cellspacing="0">
	<tr>
		<td valign="top" class="squareB">
		<table width="100%" height="100%" border="0" cellpadding="0"
			cellspacing="0">
			<tr>
				<td width="5" height="7" background="../biimages/square_line_2.gif"></td>
				<td width="100%" colspan="2"></td>
				<td><img src="../biimages/tab/field_upline_right.gif" width="5"
					height="7"></td>
			</tr>
			<tr>
				<td height="7" background="../biimages/square_line_2.gif">&nbsp;</td>
				<td height="100%" colspan="2" valign="top">
				<table width="95%" align="center" cellpadding="5">
					<tr>
						<td width="9%" nowrap>流程名称：</td>
						<td colspan="3"><input name="p_flag_name" type="text"
							class="input-text" value="<%=pTable.p_flag_name%>"
							onFocus="switchClass(this)" onBlur="switchClass(this)" size="52">
						</td>
					</tr>
					<tr>
						<td width="9%" nowrap>流程状态：</td>
						<td colspan="3"><BIBM:TagRadio radioName="status" radioID="S3003" focusID="<%=pTable.status%>" />
						</td>
					</tr>
					<tr>
						<td valign="top" nowrap>说　　明：</td>
						<td colspan="3"><textarea name="p_note" cols="50" rows="5"
							class="input-text" onFocus="switchClass(this)"
							onBlur="switchClass(this)"><%=pTable.p_note%></textarea></td>
					</tr>
				</table>
				</td>
				<td background="../biimages/square_line_3.gif">&nbsp;</td>
			</tr>
			<tr>
				<td height="5"><img src="../biimages/square_corner_3.gif"
					width="5" height="5"></td>
				<td colspan="2" background="../biimages/square_line_4.gif"></td>
				<td><img src="../biimages/square_corner_4.gif" width="5"
					height="5"></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
      <td height="35" align="center" valign="bottom">
        <input name="bc" type="button" class="button" onMouseOver="switchClass(this)"	onMouseOut="switchClass(this)" 
		  value="保存" onclick="setSubmitFlag('step1','<%=submit%>','current')">
	    <input name="sc" type="button" class="button"	onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
		  value="删除" onclick="setSubmitFlag('step1','delete','current')" <%if("".equals(pTable.p_id)){out.print("disabled=true");}%>>
        <input name="next" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
		  value="下一步" onclick="setSubmitFlag('step1','<%=submit%>','next')">
	  </td>
	</tr>
</table>
<INPUT TYPE="hidden" id="opType" name="opType" value="" />
<INPUT TYPE="hidden" id="opSubmit" name="opSubmit" value="" />
<INPUT TYPE="hidden" id="opDirection" name="opDirection" value="" />
</FORM>
</BODY>
<script language="javascript">
	function setSubmitFlag(type,submit,direction){
	  document.editForm.opType.value = type;
	  document.editForm.opSubmit.value = submit;
	  document.editForm.opDirection.value = direction;
	  if(submit == 'delete'){
	    if(confirm("您确定要删除该审核流程吗？此操作不可恢复!")){
          document.editForm.submit();
        }
	  }else{
	    if (editForm.p_flag_name.value==null || editForm.p_flag_name.value==''){
		  alert('流程名称不能为空！！！');
		  return;
		}
        if(submit == 'insert'||submit == 'update'){
		  document.editForm.submit();
        }
	  }
	}
</script>
</HTML>
