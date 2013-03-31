<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.base.table.*"%>
<%@ page import="com.ailk.bi.system.common.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%
	if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>
<%
	//菜单
	String menu_id = request.getParameter("menu_id");
	if (menu_id == null || "".equals(menu_id)) {
		menu_id = "0";
	}

	//模块信息
	InfoMenuTable infoMenu = LSInfoMenu.getMenuInfo(menu_id);

	if (infoMenu == null) {
		infoMenu = new InfoMenuTable();
	}
	//提交类型
	String submitType = request.getParameter("submitType");
	if (submitType == null || "".equals(submitType)) { //判断提交类型
		submitType = "0";
	}
	//
%>


<HTML>
<HEAD>
<TITLE>菜单信息</TITLE>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
<SCRIPT src="<%=request.getContextPath()%>/js/picker/menuPickerModule.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript">
function pageOnLoad()
{
	//刷新界面
    	if(<%=(submitType.equals("1")||submitType.equals("2"))%>){
			parent.MenuList.location.href="menutree.jsp";
			if(<%=submitType.equals("2")%>){
				parent.work_frame.location = "menuview.jsp";
			}
	    }
}
//
</script>
<SCRIPT language="javascript">
function submitClick(submitType){
	frmEdit.submitType.value = submitType;
	switch(submitType){
		case 1:
			var a = document.body.getElementsByTagName("INPUT");
			for(var i=0; i<a.length; i++){
				if(a[i].runtimeStyle.color != a[i].style.color){
					alert("请正确填写信息!");
					return;
				}
			}
			if(document.frmEdit.menu__menu_name.value==null ||document.frmEdit.menu__menu_name.value==""){
			   alert("菜单名称不能为空!");
			   document.frmEdit.menu__menu_name.focus();
			   break;
			}
			if(document.frmEdit.menu__sequence.value != null || document.frmEdit.menu__sequence.value != "" )
			{
				var arr1 = "0123456789";
				var arr2 = new Array();
				var menuStr = document.frmEdit.menu__sequence.value;
				for(var i=0;i<menuStr.length;i++)
					{
						arr2[i] = menuStr.substr(i,1);
						if(arr1.match(arr2[i]) == null)
							{
								alert("菜单顺序输入不正确！"+ "\n" +"请输入阿拉伯数字！");
								document.frmEdit.menu__sequence.value = "";
								document.frmEdit.menu__sequence.focus();
								return;
							}
					}
			}
			if(document.frmEdit.menu__menu_id.value==document.frmEdit.menu__parent_id.value){
			   alert("上级菜单不能为自身！");
			   break;
			}

			frmEdit.submit();
			break;

		case 2:
			if(document.frmEdit.menu__menu_name.value == "" || document.frmEdit.menu__menu_name.value == null)
       		{
       			alert("请选择要删除的菜单！");
       			return ;
       		}
        	else
       	    {
        		if(confirm("确认要删除该菜单吗？"))
       			{
        			frmEdit.submit();
    				return ;
       			}
        		else
       			{
       				return;
       			}
       	    }

			break;
	}

}
function _frmReset(){
	document.frmEdit.reset();
}

</SCRIPT>
<style>
	.coolText{behavior:url(../htc/coolText.htc);}
</style>
</HEAD>
<BODY onLoad="pageOnLoad();" class="side-7">
<table width="100%">
	<tr>
		<td height="22"><img src="../images/common/system/arrow7.gif" width="7"
			height="7"><b><%=infoMenu.menu_name%></b>&nbsp;菜单信息</td>
	</tr>
	<tr>
		<td height="1" background="../images/common/system/black-dot.gif"></td>
	</tr>
	<TR>
		<td valign="top" align="center"><br>
		<TABLE WIDTH="500" cellpadding="0" CELLSPACING="0">
			<TR>
				<TD>
				<FORM name="frmEdit" action="menuview.rptdo"
					METHOD="POST"		onsubmit=" returnfalse;">
					<INPUT	TYPE="hidden" id="submitType" name="submitType" value="0" />
					<INPUT	TYPE="hidden" id="menu__menu_id" name="menu__menu_id" value="<%=menu_id%>" />

				    <TABLE bgColor="#999999" WIDTH="100%" CELLSPACING="0" CELLPADDING="0">
					<TR>
						<TD>
						<TABLE width="100%" CELLPADDING="4" border="0" cellspacing="1">

							<TR bgColor="#ffffff">
								<TD align="left" width="20%">菜单名称</TD>
								<TD align="left"><INPUT type='text' name="menu__menu_name"
									maxlength="32" value="<%=infoMenu.menu_name%>"
									class="coolText" texttype="" />*</TD>
							</TR>
							<TR bgColor="#ffffff">
								<TD align="left" width="20%">菜单顺序</TD>
								<TD align="left"><INPUT type='text' name="menu__sequence"
									maxlength="32" value="<%=infoMenu.sequence%>"
									class="coolText" texttype="" />*<span style="color:red">(请输入阿拉伯数字!)</span></TD>

							</TR>
							<TR bgColor="#ffffff">
								<TD  align="left" width="20%">上级菜单</TD>

								<TD align="left">

								<INPUT TYPE="hidden" id="menu__parent_id" name="menu__parent_id" value="<%=infoMenu.parent_id%>" />
								<INPUT type="text"  name="menu__parent_name" size="20" maxlength="20" value="<%=infoMenu.parent_name%>" onclick="setMenu(this, document.all.menu__parent_id,'../system/menuPickerTree.jsp');"  readonly style="cursor:text"/>

								</TD>
							</TR>


							<TR bgColor="#ffffff">
								<TD align="left" width="20%">菜单状态</TD>
								<TD align="left"><SELECT id="menu__status" name="menu__status">
									<OPTION VALUE="1"
										<%if ("1".equals(infoMenu.status))  out.print("SELECTED");%>>有效</OPTION>
									<OPTION VALUE="0"
										<%if ("0".equals(infoMenu.status))  out.print("SELECTED");%>>无效</OPTION>
								</SELECT>*</TD>
							</TR>

							<TR bgColor="#ffffff">
								<TD align="left" width="20%">是否显示</TD>
								<TD align="left">
								<SELECT id="menu__isshow" name="menu__isshow">
									<OPTION VALUE="1"
										<%if ("1".equals(infoMenu.isshow))  out.print("SELECTED");%>>显示</OPTION>
									<OPTION VALUE="0"
										<%if ("0".equals(infoMenu.isshow))  out.print("SELECTED");%>>不显示</OPTION>
								</SELECT>*
								</TD>
							</TR>

							<TR bgColor="#ffffff">
								<TD align="left" width="20%">菜单访问路径</TD>
								<TD align="left" width="80%">
								<TEXTAREA name="menu__menu_url" rows="2"
									style="width:350px;overflow:auto" class="query-input-text"><%=infoMenu.menu_url%></TEXTAREA>
								</TD>
							</TR>
							<TR bgColor="#ffffff">
								<TD align="left" width="20%">描述</TD>
								<TD align="left" width="80%"><TEXTAREA name="menu__menu_desc" rows="2"
									style="width:350px;overflow:auto" class="query-input-text"><%=infoMenu.menu_desc%></TEXTAREA>
								</TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
				</TABLE>
				</FORM>
				<%
				if (true) {
				%>
				<center>
				<TABLE width="" cellpadding="2" cellspacing="0">
					<TR VALIGN="MIDDLE">
						<TD align="CENTER"><INPUT TYPE="BUTTON" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON" style="cursor:hand;" VALUE="保存"
							onclick="submitClick(1);" /></TD>

						<TD align="CENTER"><INPUT TYPE="BUTTON" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON" style="cursor:hand;" VALUE="删除"
							onclick="submitClick(2);" /></TD>

						<TD align="CENTER"><INPUT TYPE="BUTTON" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON" style="cursor:hand;" VALUE="重置"
							onclick="_frmReset();" /></TD>
					</TR>
				</TABLE>
				</center>
				<%
				}
				%>
				</TD>
			</TR>
		</TABLE>
		</td>
		</TR>
		</table>
</BODY>
</HTML>








