<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.base.table.*"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.system.common.*"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%
	if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>
<%
	InfoOperTable loginUser = CommonFacade.getLoginUser(session);
	//模块ID
	String dept_id = request.getParameter("dept_id");
	if (dept_id == null || "".equals(dept_id)) {
		dept_id = "";
	}
	//部门信息
	InfoDeptTable infoDept = LSInfoDept.getDeptInfo(dept_id);
	if (infoDept == null) {
		infoDept = new InfoDeptTable();
		infoDept.parent_dept_id="0";
	}
	//区域ID
	String region_id = CommTool.getParameterGB(request, "region_id");
	if (region_id == null || "".equals(region_id)) {
		region_id = infoDept.region_id;
	}
	if("".equals(region_id)){
		region_id = loginUser.region_id;
	}
	//区域名称
	String region_name = CommTool.getParameterGB(request, "region_name");
	if (region_name == null || "".equals(region_name)) {
		region_name = LSInfoRegion.getRegionName(region_id);
	}


	//提交类型
	String submitType = request.getParameter("submitType");
	if (submitType == null || "".equals(submitType)) { //判断提交类型
		submitType = "0";
	}
	//提交类型
	String oper_type = request.getParameter("oper_type");
	if (oper_type == null || "".equals(oper_type)) { //判断提交类型
		oper_type = "0";
	}
	//
%>


<HTML>
<HEAD>
<TITLE>操作员信息</TITLE>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
<SCRIPT src="<%=request.getContextPath()%>/js/picker/deptPicker.js"></SCRIPT>
<SCRIPT src="<%=request.getContextPath()%>/js/picker/areaPicker.js"></SCRIPT>
<script src="<%=request.getContextPath()%>/js/XmlRPC.js" language="javascript"></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript">
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
//
function CheckExsit(value,oper_type){
	if(value==null||value==""){
		alert("请输入部门编码！");
		return;
	}
	var doc=baseXmlSubmit.callAction("../system/regionAction.jsp?oper_type=7&dept_id="+value);
	doc=doc.replace(/^\s+|\n+$/g,'');
	if(doc == 'true')
	{
		window.location.href = "deptview.jsp?submitType=1&dept_id="+value;
	}
	else if(doc == 'false')
	{
		alert("没有查到部门编码为" + value + "的部门信息，可以使用！");
		return;
	}
	else
	{
		alert("查询失败！");
		return;
	}
}
function pageOnLoad()
{
	//刷新界面
    	if(<%=(submitType.equals("1")||submitType.equals("2")||submitType.equals("3"))%>){
			parent.DeptList.location.href="depttree.jsp";
			if(<%=submitType.equals("2")%>){
				parent.DeptView.location = "deptview.jsp";
			}
	    }
}

function InputDeptNo(){
	var re=window.showModalDialog("../system/inputDeptNo.jsp?addee",re,"dialogWidth:300px; dialogHeight:100px; dialogLeft:200px; dialogTop:200px; status:no; directories:yes;scrollbars:no;Resizable=no;help:no");;
	if(re!=null&&re!=""&&re.length>5){
	var infoArr = re.split(";");
        if(infoArr!=null&&infoArr.length==4){
		document.all.dept__parent_dept_id.value=infoArr[0];
		document.all.dept__parent_dept_name.value=infoArr[1];
		document.all.dept__area_id.value=infoArr[2];
		document.all.dept__area_name.value=infoArr[3];
		return;
	}
	}
	alert("没有获取到相关部门信息!");
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
			if(document.frmEdit.dept__dept_id.value == null ||
					document.frmEdit.dept__dept_id.value == "")
				{
					alert("部门编码不能为空" + "\n"+"请重新输入！");
					document.frmEdit.dept__dept_id.focus();
					return ;
				}
			if(document.frmEdit.dept__dept_name.value==null ||
					document.frmEdit.dept__dept_name.value=="")
			{
			   alert("部门名称不能为空!");
			   document.frmEdit.dept__dept_name.focus();
			   break;
			}


			if(document.frmEdit.dept__dept_id.value==document.frmEdit.dept__parent_dept_id.value){
				alert("上级部门不能为自身！");
				break;
			}

			if(document.getElementsByName("dept__region_name")[0].value == null ||
					document.getElementsByName("dept__region_name")[0].value == "")
			{
				alert("归属区域不能为空，请重新输入!");
				document.getElementsByName("dept__region_name")[0].focus();
				return;
			}
//			if(document.frmEdit.dept__parent_dept_id.value==""){
//				alert("上级部门不能为空！");
//				break;
//			}
			frmEdit.submit();
			break;

		case 2:
			if(document.frmEdit.dept__dept_id.value == "" || document.frmEdit.dept__dept_id.value == null)
       		{
       			alert("请选择要删除的部门！");
       			return ;
       		}
        	else
       	    {
        		if(confirm("确认要删除该部门吗？"))
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
			height="7"><%=infoDept.dept_name%>&nbsp;部门信息</td>
	</tr>
	<tr>
		<td height="1" background="../images/common/system/black-dot.gif"></td>
	</tr>
	<TR>
		<td valign="top" align="center"><br>
		<TABLE WIDTH="500" cellpadding="0" CELLSPACING="0">
			<TR>
				<TD>
				<FORM name="frmEdit" action="deptview.rptdo"
					METHOD="POST"		onsubmit=" returnfalse;">
					<INPUT	TYPE="hidden" id="submitType" name="submitType" value="<%=submitType%>" />
					<INPUT	TYPE="hidden" id="oper_type" name="oper_type" value="<%=oper_type%>" />


				    <TABLE bgColor="#999999" WIDTH="100%" CELLSPACING="0"
					CELLPADDING="0">
					<TR>
						<TD>
						<TABLE width="100%" CELLPADDING="4" border="0" cellspacing="1">
							<TR bgColor="#ffffff">
								<TD ALIGN="left">部门编码</TD>
								<TD ALIGN="left"><INPUT type='text' name="dept__dept_id"
									maxlength="6" value="<%=infoDept.dept_id%>"
									 /><label style="color: red">*</label><input type="button" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON" style="cursor:hand;" VALUE="查 询"
							onclick="CheckExsit(document.frmEdit.dept__dept_id.value,7);" /></TD>
							</TR>

							<TR bgColor="#ffffff">
								<TD ALIGN="left">部门名称</TD>
								<TD ALIGN="left"><INPUT type='text' name="dept__dept_name"
									maxlength="32" value="<%=infoDept.dept_name%>"
									class="coolText" texttype="" /><label style="color: red">*</label></TD>
							</TR>

							<TR bgColor="#ffffff">
								<TD ALIGN="left">上级部门</TD>
								<TD ALIGN="left">
								<INPUT TYPE="hidden" id="dept__parent_dept_id" name="dept__parent_dept_id" value="<%=infoDept.parent_dept_id%>" />
								<INPUT type="text"  name="dept__parent_dept_name" size="20" maxlength="20" value="<%=infoDept.parent_dept_name%>" onclick="setDept(this, document.all.dept__parent_dept_id,document.all.dept__region_id,document.all.dept__region_name,document.all.const_region_id,'<%=loginUser.dept_id%>','../system/dpTree.jsp');"  readonly style="cursor:text"/>
								<!--
								<input type="button" class="button2"
									onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
									NAME="BUTTON" style="cursor:hand;" VALUE=" 输入编码"
									onclick="InputDeptNo();" />-->
								</TD>
							</TR>

							<TR bgColor="#ffffff">
								<TD ALIGN="left">归属区域</TD>
								<TD ALIGN="left">
								<INPUT TYPE="hidden" id="const_region_id" name="const_region_id" value="<%=region_id%>" />
								<INPUT TYPE="hidden" id="dept__region_id" name="dept__region_id" value="<%=infoDept.region_id%>" />
								<INPUT type="text"  name="dept__region_name" size="20" maxlength="20" value="<%=infoDept.region_name%>" onclick="setArea(this, document.all.dept__region_id,document.all.const_region_id,'../system/areaTree.jsp');"  readonly style="cursor:text"/>
								<label style="color: red">*</label></TD>
							</TR>

							<TR bgColor="#ffffff">
								<TD ALIGN="left">部门类型</TD>
								<TD ALIGN="left"><BIBM:TagSelectList listName="dept__dept_type"
									listID="0" focusID="<%=infoDept.dept_type%>"
									selfSQL="select code_id ,code_name from ui_code_list where type_code ='dept_type'" /><label style="color: red">*</label></TD>

							</TR>

							<TR bgColor="#ffffff">
								<TD ALIGN="left">有效标志</TD>
								<TD ALIGN="left"><BIBM:TagSelectList listName="dept__flag"
									listID="#" focusID="<%=infoDept.flag%>"
									selfSQL="0,有效;1,无效;" /><label style="color: red">*</label>
								</TD>
							</TR>

							<TR bgColor="#ffffff">
								<TD ALIGN="left">电话</TD>
								<TD ALIGN="left"><INPUT type='text' name="dept__phone"
									maxlength="32" value="<%=infoDept.phone%>"
									class="coolText" texttype="EM" /></TD>
							</TR>
							<TR bgColor="#ffffff">
								<TD ALIGN="left">传真</TD>
								<TD ALIGN="left"><INPUT type='text' name="dept__fax"
									maxlength="32" value="<%=infoDept.fax%>"
									class="coolText" texttype="EM" /></TD>
							</TR>
							<TR bgColor="#ffffff">
								<TD ALIGN="left">地址</TD>
								<TD ALIGN="left"><INPUT type='text' name="dept__address"
									maxlength="32" value="<%=infoDept.address%>"
									class="coolText" texttype="EM" /></TD>
							</TR>
							<TR bgColor="#ffffff">
								<TD ALIGN="left">描述</TD>
								<TD ALIGN="left"><INPUT type='text' name="dept__comments"
									maxlength="32" value="<%=infoDept.comments%>"
									class="coolText" texttype="EM" /></TD>
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
						<%
								if (!"0".equals(dept_id)&&!dept_id.equals(loginUser.dept_id)) {
						%>
						<TD align="CENTER"><INPUT TYPE="BUTTON" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON" style="cursor:hand;" VALUE="删除"
							onclick="submitClick(2);" /></TD>
						<%}%>
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








