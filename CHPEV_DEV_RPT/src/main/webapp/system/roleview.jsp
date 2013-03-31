<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.base.util.CommTool"%>
<%@ page import="com.ailk.bi.base.table.*"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
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
			//角色ID
			String role_id = CommTool.getParameterGB(request,"role_id");
			if (role_id == null || "".equals(role_id)) {
				role_id = "0";
			}

			//角色名称
			String role_name = request.getParameter("role_name");
			if (role_name == null || "".equals(role_name)) {
				role_name = "";
			}
			//角色描述
			String comments = CommTool.getParameterGB(request,"comments");
			if (comments == null || "".equals(comments)) {
				comments = "";
			}
			//登陆操作员信息
			InfoOperTable sstUser = CommonFacade.getLoginUser(session);
			String region_id = sstUser.region_id;
			//取出角色信息
			InfoRoleTable roleInfo = LSInfoRole.getRoleInfo(role_id);
			if (roleInfo == null) {
				roleInfo = new InfoRoleTable();
			}
			//角色ID
			if ("0".equals(role_id)) {
				roleInfo.region_id = region_id;
			}
			if ((submitType == null) || "".equals(submitType)) {
				submitType = "0";
			}
			%>

<HTML>
<HEAD>
<TITLE>角色信息</TITLE>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
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
function SearchRole(){
	var role_id=document.frmEdit.role__role_id.value;
	if(role_id==null ||role_id==""){
			   	alert("请填写需要查询的角色代码!");
			   	document.frmEdit.role__role_id.focus();
			   	return;
				}
	var doc=baseXmlSubmit.callAction("../system/regionAction.jsp?oper_type=5&role_id="+role_id);
	doc = doc.replace(/^\s+|\n+$/g,'');
        if(doc=='true'){
			window.location.href="roleview.jsp?submitType=1&role_id="+role_id;
        }else if(doc=='false'){
			alert("未查到角色代码为"+role_id+"的角色信息，请继续。");
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
	if(document.all.role__role_id.value==null||document.all.role__role_id.value==""){
		alert("请填写角色代码!");
	   	document.all.role__role_id.focus();
	   	return;
	}
	if(document.all.role__role_name.value==null||document.all.role__role_name.value==""){
				alert("请填写角色名称!");
			   	document.all.role__role_name.focus();
			   	return;
	}
	frmEdit.submitType.value = submitType;
	frmEdit.submit();

  }

  function disablerole(){
  	if(document.all.role__role_name.value==null||document.all.role__role_name.value==""){
				alert("请填写角色名称!");
			   	document.all.role__role_name.focus();
			   	return;
	}
	if(document.all.role__role_id.value==null||document.all.role__role_id.value==""){
				alert("请填写角色代码!");
			   	document.all.role__role_id.focus();
			   	return;
	}
  	var statusNewIndex=document.all.role__status.options.selectedIndex;
	if(statusNewIndex==null)statusNewIndex=1;
	var statusNew=document.all.role__status.options[statusNewIndex].value;
	if('<%=roleInfo.status%>'=='0'&&statusNew=='0'){
		alert("角色为失效状态时不能再次失效该角色!");
		return;
	}
	document.all.role__status.options[1].selected="true";
	frmEdit.submit();

  }

function delrole(){//删除
var statusNewIndex=document.all.role__status.options.selectedIndex;
			if(statusNewIndex==null)statusNewIndex=1;
			var statusNew=document.all.role__status.options[statusNewIndex].value;
			if('<%=roleInfo.status%>'=='1'&&statusNew=='1'){
				alert("角色为有效状态时不能删除角色信息!");
				return;
			}
			if(confirm("确认要删除该角色吗？")){
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
	//
	if('<%=submitType%>'=='1'||'<%=submitType%>'=='3'){
			parent.roleDefPage.role_id='<%=role_id%>';
        	if(<%=submitType%> == '3'){
					parent.roleList_frame.location.href="roleTreeFrame.jsp";
        	}
	}
  }
</script>
</HEAD>
<BODY onLoad="pageOnLoad();" class="side-7">

<table width="100%">
	<tr>
    <td height="18"><img src="../images/common/system/arrow7.gif" width="7" height="7"><b><%=roleInfo.role_name%></b>&nbsp;角色信息</td>
	</tr>
	<tr>
		<td height="1" background="../images/common/system/black-dot.gif"></td>
	</tr>
	<tr>
		<td valign="top"><br>
		<TABLE WIDTH="100%" cellpadding="0" CELLSPACING="0">
        <TR>
				<TD>
				<FORM name="frmEdit" action="roleview.rptdo" METHOD="POST"
					onsubmit="return false;">
                    <INPUT TYPE="hidden" id="blankobj"
					name="blankobj" value="" />
					<INPUT TYPE="hidden" id="submitType2"
					name="submitType" value="<%=submitType%>" />
					<INPUT TYPE="hidden"
					id="role__region_id" name="role__region_id" value="<%=region_id%>" />
				<TABLE WIDTH="100%" CELLSPACING="0" CELLPADDING="0">
					<TR>
						<TD>
						<TABLE width="100%" CELLPADDING="4" border="0" cellspacing="1">
							<TR>
								<TD align="right">角色代码</TD>
								<TD><INPUT type='hidden' name="hidRoleId" value="<%=roleInfo.role_id%>">
									<INPUT type='text' name="role__role_id" size="20" maxlength="20" value="<%=roleInfo.role_id%>" class="input-text"  /> *&nbsp;<input type="button" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON" style="cursor:hand;" VALUE=" 查询"
							onclick="SearchRole();" />
								</TD>
								</TR>
									<TR>
								<TD align="right">角色名称</TD>
								<TD>
									<INPUT type='text' name="role__role_name" size="35" maxlength="40" value="<%=roleInfo.role_name%>"
									class="input-text"  /> *

								</TD>
							</TR>
							<TR>
								<TD align="right">角色类型</TD>
								<TD>
									<BIBM:TagSelectList listName="role__role_type" listID="0" focusID="<%=roleInfo.role_type%>" selfSQL="select code_id,code_name from ui_code_list where type_code = 'role_type' order by code_seq"/>*
								</TD>
							</TR>
									<TR>
								<TD align="right">有效标志</TD>
								<TD>
									<BIBM:TagSelectList listName="role__status" listID="#" focusID="<%=roleInfo.status%>" selfSQL="1,有效;0,无效;"/>*
								</TD>
							</TR>
							<TR>
								<TD align="right" VALIGN="top">描述</TD>
								<TD colspan="3"><TEXTAREA name="role__comments" rows="6"
									style="width:100%;overflow:auto" class="query-input-text"><%=roleInfo.comments%></TEXTAREA>
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
							onclick="submitClick(<%=submitType%>);" ></TD>
						<%if (!"1".equals(role_id) && !"0".equals(role_id)) {

				%>
						<TD align="CENTER"><INPUT TYPE="BUTTON" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON" style="cursor:hand;" VALUE="删除"
							onclick="delrole();" ></TD>
						<TD align="CENTER"><INPUT TYPE="BUTTON" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON" style="cursor:hand;" VALUE="失效"
							onclick="disablerole();" ></TD>
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
			       <font color="red">您的归属区域不是该角色的归属区域,只能拥有查看权限.</font>
				<%}%></TD>
			</TR>
		</TABLE>
		</td>
	</tr>
</table>
</BODY>
</HTML>