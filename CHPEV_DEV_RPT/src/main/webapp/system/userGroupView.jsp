<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.base.table.*"%>
<%@ page import="com.ailk.bi.system.common.*"%>

<%
	if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,
			response))
		return;
%>
<%
			String pageRight = "11";//CommTool.CheckActionPage(session,CObjects.ROLE);
			//判断提交类型
			String submitType = request.getParameter("submitType");
			//用户组ID
			String group_id = CommTool.getParameterGB(request,"group_id");
			if (group_id == null || "".equals(group_id)) {
				group_id = "0";
			}else if((submitType == null) || "".equals(submitType)){
				submitType = "1";
			}
			//用户组名称
			String group_name = CommTool.getParameterGB(request,"group_name");
			if (group_name == null || "".equals(group_name)) {
				group_name = "";
			}

			//取出用户组信息
			InfoUserGroupTable groupInfo = LSInfoUserGroup.getUserGroupInfo(group_id);
			String parent_name = "";
			if(groupInfo.parent_id != null && !"".equals(groupInfo.parent_id)) {
				parent_name = (LSInfoUserGroup.getUserGroupInfo(groupInfo.parent_id)).group_name;
			}
			if (groupInfo == null) {
				groupInfo = new InfoUserGroupTable();
			}

			if ((submitType == null) || "".equals(submitType)) {
				submitType = "0";
			}

			%>

<HTML>
<HEAD>
<TITLE>用户组信息</TITLE>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
<SCRIPT src="<%=request.getContextPath()%>/js/picker/dprgPicker.js"></SCRIPT>
<style>
	.coolText{behavior:url(../htc/coolText.htc);}
</style>
<script src="<%=request.getContextPath()%>/js/XmlRPC.js" language="javascript"></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">


<script language=javascript>
//-begin rpc
function BaseXmlSubmit(){
}
BaseXmlSubmit.prototype.callAction = function f_callAction(url)
{
  var dom = "";
  try{
    var rpc = new XmlRPC(url);
    rpc.send();
    dom = rpc.getText();
  }
  catch(e){
    alert(e.message);
  }
  return dom;
}
var baseXmlSubmit =new BaseXmlSubmit();
//-end rpc
function SearchGroup(){
	var group_id=document.frmEdit.group__group_id.value;
	if(group_id==null ||group_id==""){
			   	alert("请填写需要查询的用户组代码!");
			   	document.frmEdit.group__group_id.focus();
			   	return;
				}
	var doc=baseXmlSubmit.callAction("../system/regionAction.jsp?oper_type=8&group_id="+group_id);
	doc=doc.replace(/^\s+|\n+$/g,'');
    if(doc=='true'){
		window.location.href="userGroupView.jsp?submitType=1&group_id="+group_id;
    }else if(doc=='false'){
		alert("未查到用户组代码为"+group_id+"的用户组信息。");
		return;
    }else{
		alert("查询失败！");
		return;
    }
}
  function submitClick(submitType){
	var a = document.body.getElementsByTagName("INPUT");//新增
	for(var i=0; i<a.length; i++){
				if(a[i].runtimeStyle.color != a[i].style.color){
					alert("请正确填写信息!");
					return;
	}}
	if(document.all.group__group_id.value==null||document.all.group__group_id.value==""){
		alert("请填写用户组代码!");
	   	document.all.group__group_id.focus();
	   	return;
	}
	if(document.all.group__group_name.value==null||document.all.group__group_name.value==""){
				alert("请填写用户组名称!");
			   	document.all.group__group_name.focus();
			   	return;
	}

//	alert(frmEdit.group__parent_id.value);
	frmEdit.submitType.value = submitType;
	frmEdit.submit();

  }

  function disablegroup(){
  	if(document.all.group__group_name.value==null||document.all.group__group_name.value==""){
				alert("请填写用户组名称!");
			   	document.all.group__group_name.focus();
			   	return;
	}
	if(document.all.group__group_id.value==null||document.all.group__group_id.value==""){
				alert("请填写用户组代码!");
			   	document.all.group__group_id.focus();
			   	return;
	}
  	var statusNewIndex=document.all.group__status.options.selectedIndex;
	if(statusNewIndex==null)statusNewIndex=1;
	var statusNew=document.all.group__status.options[statusNewIndex].value;
	if('<%=groupInfo.status%>'=='0'&&statusNew=='0'){
		alert("用户组为失效状态时不能再次失效该用户组!");
		return;
	}
	document.all.group__status.options[1].selected="true";
	frmEdit.submit();

  }

function delgroup(){//删除
var statusNewIndex=document.all.group__status.options.selectedIndex;
			if(statusNewIndex==null)statusNewIndex=1;
			var statusNew=document.all.group__status.options[statusNewIndex].value;
			if('<%=groupInfo.status%>'=='1'&&statusNew=='1'){
				alert("用户组为有效状态时不能删除用户组信息!");
				return;
			}
			if(confirm("确认要删除该用户组吗？")){
				frmEdit.submitType.value = 2;
				frmEdit.submit();
				return ;
			}
}
  //
  function _frmReset(){
	document.frmEdit.reset();
  }
  //

  function pageOnLoad(){

	if('<%=submitType%>'=='1'||'<%=submitType%>'=='3'){
			parent.groupDefPage.group_id='<%=group_id%>';

       	if(<%=submitType%> == '3'){
					parent.groupList_frame.location.href="groupTreeFrame.jsp";
        	}
	}
  }
</script>
</HEAD>
<BODY onLoad="pageOnLoad();" class="side-7">

<table width="100%">
	<tr>
    <td height="18"><img src="../images/common/system/arrow7.gif" width="7" height="7"><b><%=groupInfo.group_name%></b>&nbsp;用户组信息</td>
	</tr>
	<tr>
		<td height="1" background="../images/common/system/black-dot.gif"></td>
	</tr>
	<tr>
		<td valign="top"><br>
		<TABLE WIDTH="100%" cellpadding="0" CELLSPACING="0">
        <TR>
				<TD>
				<FORM name="frmEdit" action="userGroupView.rptdo" METHOD="POST"
					onsubmit="return false;">
                                        <INPUT TYPE="hidden" id="blankobj"
					name="blankobj" value="" />
					<INPUT TYPE="hidden" id="submitType2"
					name="submitType" value="<%=submitType%>" />

				<TABLE WIDTH="100%" CELLSPACING="0" CELLPADDING="0">
					<TR>
						<TD>
						<TABLE width="100%" CELLPADDING="4" border="0" cellspacing="1">
							<TR>
								<TD align="right" >用户组代码</TD>
								<TD><INPUT type='hidden' name="hidOldGrpId" value="<%=groupInfo.group_id%>">
									<INPUT type='text' name="group__group_id" size="20" maxlength="20" value="<%=groupInfo.group_id%>" class="input-text"  /> *&nbsp;<input type="button" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON" style="cursor:hand;" VALUE=" 查询"
							onclick="SearchGroup();" />
								</TD>
								</TR>
									<TR>
								<TD align="right" >用户组名称</TD>
								<TD>
									<INPUT type='text' name="group__group_name" size="35" maxlength="40" value="<%=groupInfo.group_name%>"
									class="input-text"  /> *

								</TD>
							</TR>
	<!--   						<TR>
								<TD align="right" VALIGN="RIGHT">归属组</TD>
								<TD> <INPUT TYPE="hidden" id="group__parent_id"
									name="group__parent_id" />
								<INPUT
									type="text" name="group__parent_name" size="20" maxlength="20"
									value="<%=parent_name%>"
									onclick="setDept(this, document.all.group__parent_id,document.all.group__parent_id,document.all.group__parent_name,'<%=group_id%>','../system/dpGroupParentTree.jsp');"
									readonly style="cursor:text" />*</TD>
							</TR> -->
							<tr>
								<td align="right">用户组类型</td>
								<td><BIBM:TagSelectList listName="group__group_type" listID="0" focusID="<%=groupInfo.group_type%>" selfSQL="select code_id,code_name from ui_code_list where type_code = 'group_type' order by code_seq"/>*</td>
							</tr>
									<TR>
								<TD align="right" >有效标志</TD>
								<TD>
									<BIBM:TagSelectList listName="group__status" listID="#" focusID="<%=groupInfo.status%>" selfSQL="1,有效;0,无效;"/>*
								</TD>
							</TR>

						</TABLE>
						</TD>
					</TR>
				</TABLE>
				</FORM>
				<%if("11".equals(pageRight)){%>
				<center>
				<TABLE width="" cellpadding="2" cellspacing="0">
					<TR VALIGN="MIDDLE">
						<TD align="CENTER"><INPUT TYPE="BUTTON" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON" style="cursor:hand;" VALUE="保存"
							onclick="submitClick('<%=submitType %>');" /></TD>
						<%if (!"1".equals(group_id) && !"0".equals(group_id)) {

				%>
						<TD align="CENTER"><INPUT TYPE="BUTTON" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON" style="cursor:hand;" VALUE="删除"
							onclick="delgroup();" /></TD>
						<TD align="CENTER"><INPUT TYPE="BUTTON" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON" style="cursor:hand;" VALUE="失效"
							onclick="disablegroup();" /></TD>
						<%}

			%>
						<TD align="CENTER"><INPUT TYPE="BUTTON" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON" style="cursor:hand;" VALUE="重置"
							onclick="_frmReset();" /></TD>
					</TR>
				</TABLE>
				</center>
				<%}else{%>
			       <font color="red">您的归属区域不是该用户组的归属区域,只能拥有查看权限.</font>
				<%}%></TD>
			</TR>
		</TABLE>
		</td>
	</tr>
</table>
</BODY>
</HTML>