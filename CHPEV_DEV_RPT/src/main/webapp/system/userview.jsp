<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.base.util.CommTool"%>
<%@ page import="com.ailk.bi.base.table.InfoOperTable"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%@ page import="com.ailk.bi.system.common.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%
	if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
//LSInfoUser.getpwd();

%>
<%

	String channleSql = "select  channle_id,channle_name from MK_PL_CHANNLE_INFO";
	//登陆操作员
	InfoOperTable loginUser = CommonFacade.getLoginUser(session);
	//操作员ID
	String user_id = request.getParameter("user_id");
	if (user_id == null || "".equals(user_id)) { //user_id，默认user_id为0，新建一个操作员
		user_id = "0";
	}

	System.out.println("user_id================="+user_id);
	//取操作员信息
	InfoOperTable userInfo = LSInfoUser.getUserInfo(user_id);
	if (userInfo == null) {
		userInfo = new InfoOperTable();
	}
	//区域ID
	String region_id = request.getParameter("region_id");
	if (region_id == null || "".equals(region_id)) {
		region_id = userInfo.region_id;
	}
	//区域名称
	String region_name = request.getParameter("region_name");
	System.out.println("region_name="+region_name);
	if (region_name == null || "".equals(region_name)) {
		region_name = LSInfoRegion.getRegionName(region_id);
	}
	//用户组名称
	String group_id = request.getParameter("group_id");
	if (group_id == null || "".equals(group_id)) {
		group_id = userInfo.group_id;
	}
	//用户组名称
	String group_name = request.getParameter("group_name");
	if (group_name == null || "".equals(group_name)) {
		group_name = LSInfoRegion.getGroupName(group_id);
	}

	//联系电话
	String mobile_phone = request.getParameter("mobile_phone");
	if (mobile_phone == null || "".equals(mobile_phone)) {
		mobile_phone = LSInfoRegion.getGroupName(mobile_phone);
	}
	//部门ID
	String dept_id = request.getParameter("dept_id");
	if (dept_id == null || "".equals(dept_id)) {
		dept_id = userInfo.dept_id;//当前选中操作员部门编号
	}
	if ("".equals(dept_id)) {//当前操作员部门为空，表示新增操作员，则赋予登陆操作员部门编号
		dept_id = loginUser.dept_id;//登陆操作员部门编号
	}
	//部门名称
	String dept_name = LSInfoDept.getDeptName(dept_id);

	//提交类型
	String submitType = request.getParameter("submitType");
	if (submitType == null || "".equals(submitType)) { //判断提交类型
		submitType = "0";
	}
%>


<HTML>
<HEAD>
<TITLE>操作员信息</TITLE>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
<SCRIPT src="<%=request.getContextPath()%>/js/picker/dprgPicker.js"></SCRIPT>
<script src="<%=request.getContextPath()%>/js/XmlRPC.js" language="javascript"></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/date/scw.js"></SCRIPT>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
#passwordStrength
{
	height:10px;
	display:block;
	float:left;
}

.strength0
{
	width:150px;
	background:#cccccc;
}

.strength1
{
	width:50px;
	background:#ff0000;
}

.strength2
{
	width:80px;
	background:#ff5f5f;
}

.strength3
{
	width:100px;
	background:#56e500;
}

.strength4
{
	background:#4dcd00;
	width:120px;
}

.strength5
{
	background:#399800;
	width:150px;
}
</style>
<script language="javascript">

//对象
function BaseXmlSubmit(){}

//实例
var baseXmlSubmit =new BaseXmlSubmit();
//动作
BaseXmlSubmit.prototype.callAction = function f_callAction(url)
{
  var dom = null;
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

//生成工具条
function AJAXPWD(pwd){
    if(pwd == null || pwd == ""){
    	alert("没有 输入密码!");
    	return;
    }
    if(document.frmEdit.user__user_id.value==null ||document.frmEdit.user__user_id.value==""){
		   alert("请填写用户编号!");
		   document.frmEdit.user__user_id.focus();
		   return;
	}

    var bar = baseXmlSubmit.callAction("../system/SystemAjax.jsp?user_id="+document.frmEdit.user__user_id.value+"&pwd="+pwd);
    bar=bar.replace(/^\s+|\n+$/g,'');
    //
    if(bar!=null&&bar!=""){
        alert(bar);
        document.frmEdit.user__pwd.focus();
        return;
    }
}
function passwordStrength(password)
{
	var desc = new Array();
	desc[0] = "非常弱";
	desc[1] = "弱";
	desc[2] = "一般";
	desc[3] = "中等";
	desc[4] = "强";
	desc[5] = "非常强";

	var score   = 0;

	//if password bigger than 6 give 1 point
	if (password.length >=6) score++;

	//if password has both lower and uppercase characters give 1 point
	if ( ( password.match(/[a-z]/) ) && ( password.match(/[A-Z]/) ) ) score++;

	//if password has at least one number give 1 point
	if (password.match(/\d+/)) score++;

	//if password has at least one special caracther give 1 point
	if ( password.match(/.[!,@,#,$,%,^,&,*,?,_,~,-,(,)]/) )	score++;

	//if password bigger than 12 give another 1 point
	if (password.length >= 12) score++;

	 document.getElementById("passwordDescription").innerHTML = desc[score];
	 document.getElementById("passwordStrength").className = "strength" + score;
}

function pageOnLoad(){
	    //刷新界面
    	if(<%=(submitType.equals("3")||submitType.equals("2"))%>){
			parent.userList_frame.location.href="userListFrame.jsp";
		if(<%=submitType.equals("2")%>){
			parent._newUser();
		}else{
			parent.userDefPage.user_id="<%=user_id%>";
		   	parent.userDefPage.user_name="<%=userInfo.user_name%>";
		   	parent.userDefPage.region_id="<%=region_id%>";
		   	parent.userDefPage.region_name="<%=region_name%>";
		}
	 }else if(<%=(submitType.equals("1"))%>){
	 	    parent.userDefPage.user_id="<%=user_id%>";
		   	parent.userDefPage.user_name="<%=userInfo.user_name%>";
		   	parent.userDefPage.region_id="<%=region_id%>";
		   	parent.userDefPage.region_name="<%=region_name%>";
	 }


}
//
</script>
<SCRIPT language="javascript">
function submitClick(){
			var a = document.body.getElementsByTagName("INPUT");

			//检查用户编号
			if(document.frmEdit.user__user_id.value==null ||document.frmEdit.user__user_id.value==""){
				   alert("请填写用户编号!");
				   document.frmEdit.user__user_id.focus();
				   return;
			}
			//用户组
			if(document.frmEdit.user__group_id.value==null ||document.frmEdit.user__group_id.value==""){
				   alert("请选择用户组!");
				   document.frmEdit.user__group_name.focus();
				   return;
			}
			//校验操作员名称
			if(document.frmEdit.user__user_name.value==null ||document.frmEdit.user__user_name.value==""){
			   alert("请填写真实姓名!");
			   document.frmEdit.user__user_name.focus();
			   return;
			}

			//区域检测 user__region_id
			if(document.frmEdit.user__region_id.value == null || document.frmEdit.user__region_id.value == "")
				{
					alert("请选择区域！");
					document.frmEdit.user__region_name.focus();
					return;
				}

			//校验操作员问题
			if(document.frmEdit.user__question.value==null ||document.frmEdit.user__question.value==""){
			   alert("请填写操作员问题!");
			   document.frmEdit.user__question.focus();
			   return;
			}
			//校验操作员答案
			if(document.frmEdit.user__answer.value==null ||document.frmEdit.user__answer.value==""){
			   alert("请填写操作员答案!");
			   document.frmEdit.user__answer.focus();
			   return;
			}
			//联系电话 user__mobile_phone
			if(document.frmEdit.user__mobile_phone.value==null ||document.frmEdit.user__mobile_phone.value==""){
				   alert("请填写操作员联系电话!");
				   document.frmEdit.user__mobile_phone.focus();
				   return;
				}

			//校验操作员密码
			if(document.frmEdit.user__pwd.value==null ||document.frmEdit.user__pwd.value==""){
			   alert("操作员密码为空!");
			   document.frmEdit.user__pwd.focus();
			   return;
			}
			else
			{

				 //密码强度
				 var pwdLevel = document.frmEdit.user__pwd.value;//document.getElementById("passwordStrength").value;//className;
			   	 //pwdLevel = pwdLevel.substring(8);//substring(start,end)start为必须，end可选。
			   	 if(pwdLevel.length<8){
			    			alert("密码不符合强度要求，密码长度不低于8位。");
			    			document.frmEdit.user__pwd.focus();
			    			return;
			      }
			   	 else{
					 var sc = 0;
					 if ((pwdLevel.match(/[a-z]/)!=null) || ( pwdLevel.match(/[A-Z]/)!=null ) ) sc++;
					 if (pwdLevel.match(/.[!,@,#,$,%,^,&,*,?,_,~,-,(,)]/) != null)	sc++;
					 if (pwdLevel.match(/.[0-9]/) != null)	sc++;
					 if(sc < 2)
						 {
						 	alert("密码不符合强度要求，需要由字母、数字及下划线、@等特殊字符至少两种组成。");
			    			document.frmEdit.user__pwd.focus();
			    			return;
						 }
		     	 }

	 		}
			frmEdit.submit();

}
function deleMit(){
	document.frmEdit.submitType.value="2";
	if(confirm("确认要删除该操作员吗？")){
				frmEdit.submit();
				return ;
			}
}
function _frmReset(){
	document.frmEdit.reset();
}

function GetAutoNewOperNo(){
	var ret=false;
	var doc=baseXmlSubmit.callAction("../system/regionAction.jsp?oper_type=3");
	doc=doc.replace(/^\s+|\n+$/g,'');
        if(doc==''){
		alert("获取工号失败.");
		return;
        }
	document.all.user__user_id.value=doc;
}

function SearchOper(){
	var user_id = document.frmEdit.user__user_id.value;
	if(user_id==null ||user_id==""){
			   	alert("请填写需要查询的操作员编号!");
			   	document.frmEdit.user__user_id.focus();
			   	return;
	}
	var doc=baseXmlSubmit.callAction("../system/regionAction.jsp?oper_type=41&user_id="+user_id);
	doc=doc.replace(/^\s+|\n+$/g,'');
    if(doc=='true'){
			window.location.href="userview.jsp?submitType=1&user_id="+user_id;
    }else if(doc=='false'){
			alert("未查到工号为"+user_id+"的操作员信息。");
			return;
    }else{
			alert("查询失败!");
			return;
    }
}

function unLock(){
	var user_id=document.frmEdit.user__user_id.value;
	var doc=baseXmlSubmit.callAction("../system/unlockUserAction.jsp?oper_type=41&user_id="+user_id);
	doc=doc.replace(/^\s+|\n+$/g,'');
	//alert(doc);
	if(doc==1){
		alert("解锁成功！");
		window.location.href="userview.jsp?submitType=1&user_id="+user_id;
	}else if(doc==0){
		alert("该用户"+user_id+"不是锁闭状态，不需要解锁！");
		return;
	}else{
		alert("解锁错误！");
		return;
	}
	return;
}
//
</SCRIPT>
<style>
	.coolText{behavior:url(../htc/coolText.htc);}
</style>
</HEAD>
<BODY onLoad="pageOnLoad();" class="side-7">
<FORM name="frmEdit" action="userview.rptdo" METHOD="POST"	onsubmit="return false;">
<INPUT TYPE="hidden" id="submitType" name="submitType" value="<%=submitType%>" />

<table width="100%">
	<tr>
		<td height="22"><img src="../images/common/system/arrow7.gif" width="7"
			height="7"> <b><%=userInfo.user_name%> </b>&nbsp;操作员信息</td>
	</tr>
	<tr>
		<td height="1" background="../images/common/system/black-dot.gif"></td>
	</tr>
	<TR>
		<td valign="top"><br>
		<TABLE WIDTH="100%" cellpadding="0" CELLSPACING="0">
			<TR>
				<TD>

				<TABLE bgColor="#999999" WIDTH="100%" CELLSPACING="0"
					CELLPADDING="0">
					<TR>
						<TD>
						<TABLE width="100%" CELLPADDING="4" border="0" cellspacing="1">
							<TR bgColor="#ffffff">
								<TD>用户编号<INPUT TYPE="hidden" id="hid_old_user_id" name="hid_old_user_id" value="<%=userInfo.user_id%>"></TD>
								<TD><INPUT type='text' name="user__user_id" maxlength="15"	value="<%=userInfo.user_id%>" class="coolText" texttype="" />*
									<input	type="button" class="button2" onMouseOver="switchClass(this)"
									onMouseOut="switchClass(this)" NAME="BUTTON"
									style="cursor:hand;" VALUE=" 自动生成"
									onclick="GetAutoNewOperNo();" />
									<input type="button" class="button"
								onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
								NAME="BUTTON" style="cursor:hand;" VALUE=" 查询"
								onclick="SearchOper();" />
							 </span>
									</TD>
									<TD>用户组</TD>
								<TD><INPUT TYPE="hidden" id="user__group_id"
									name="user__group_id" value="<%=group_id%>" /> <INPUT
									type="text" name="user__group_name" size="20" maxlength="20"
									value="<%=group_name%>"
									onclick="setDept(this, document.all.user__group_id,document.all.user__group_id,document.all.user__group_name,'<%=group_id%>','../system/dpGroupTree.jsp');"
									readonly style="cursor:text" />*</TD>

							</TR>

							<TR bgColor="#ffffff">
								<TD>密码</TD>
								<TD><INPUT type='password' name="user__pwd"
									maxlength="32"
									value="<%="".equals(userInfo.pwd)?"111111@a":userInfo.pwd%>"
									class="coolText" texttype="" onkeyup="passwordStrength(this.value)"
									onBlur="AJAXPWD(this.value)"/>*
									<div id="passwordDescription"></div>
									<div id="passwordStrength" class="strength0">
									</div>

								</TD>
								<TD>真实姓名</TD>
								<TD><INPUT type='text' name="user__user_name"
									maxlength="32" value="<%=userInfo.user_name%>" class="coolText"
									texttype="" />*</TD>

							</TR>

							<TR bgColor="#ffffff">
								<TD>所在部门</TD>
								<TD><INPUT TYPE="hidden" id="user__dept_id"
									name="user__dept_id" value="<%=dept_id%>" /> <INPUT
									type="text" name="user__dept_name" size="20" maxlength="20"
									value="<%=dept_name%>"
									onclick="setDept(this, document.all.user__dept_id,document.all.user__region_id,document.all.user__region_name,'<%=loginUser.dept_id%>','../system/dprgTree.jsp');"
									readonly style="cursor:text" />*</TD>
								<TD>性别</TD>
								<TD><SELECT id="user__sex" name="user__sex">
									<OPTION VALUE="1"
										<%if ("1".equals(userInfo.sex))  out.print("SELECTED");%>>男</OPTION>
									<OPTION VALUE="2"
										<%if ("2".equals(userInfo.sex))  out.print("SELECTED");%>>女</OPTION>
								</SELECT>*</TD>


							</TR>

							<TR bgColor="#ffffff">

								<TD>区域</TD>
								<TD>
									<INPUT TYPE="hidden" id="user__region_id"
										name="user__region_id" value="<%=region_id%>" />
									<INPUT
										type="text" name="user__region_name" size="20" maxlength="20"
										onclick="setDept(this, document.all.user__region_id,document.all.user__region_id,document.all.user__region_name,'<%=loginUser.region_id%>','../system/dpregionTree.jsp');"
										value="<%=region_name%>" readonly style="cursor:text" />*</TD>

								<TD>职务</TD>
								<TD><BIBM:TagSelectList listName="user__duty_id"
									focusID="<%=userInfo.duty_id%>" listID="#"
									selfSQL="1,普通职员;2,渠道经理;3,部门经理;" />*</TD>
							</TR>

							<TR bgColor="#ffffff">
								<TD>问题</TD>
								<TD><INPUT type='text' name="user__question"
									maxlength="32" value="<%=userInfo.question%>" class="coolText"
									texttype="" />*</TD>

								<TD>回答</TD>
								<TD><INPUT type='text' name="user__answer" maxlength="32"
									value="<%=userInfo.answer%>" class="coolText" texttype="" />*</TD>
							</TR>


							<TR bgColor="#ffffff">
								<TD>联系电话</TD>
								<TD><INPUT type='text' name="user__mobile_phone" value="<%=userInfo.mobile_phone%>"
									maxlength="32"
									texttype="" />*</TD>
								<TD>有效标志</TD>
								<TD><BIBM:TagSelectList listName="user__status"
									focusID="<%=userInfo.status%>" listID="#" selfSQL="0,有效;1,密码错误1次;2,密码错误2次;99,锁定" />*
								</TD>
							</TR>

							<TR bgColor="#ffffff">
								<TD>所属渠道</TD>
								<TD><BIBM:TagSelectList listName="user__channleId" script="style='width:150px'"
									focusID="<%=userInfo.channleId%>" listID="0" selfSQL="<%=channleSql %>" />*</TD>
								<TD></TD>
								<TD></TD>
							</TR>

						</TABLE>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</td>
	</TR>
</table>

<%
if (true) {
%>
<center>
<TABLE width="" cellpadding="2" cellspacing="0">
	<TR VALIGN="MIDDLE">
		<TD align="CENTER"><INPUT TYPE="BUTTON" class="button"
			onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
			NAME="BUTTON" style="cursor:hand;" VALUE="保存"
			onclick="submitClick();" /></TD>
		<%
				if (!"0".equals(user_id) && !"1".equals(user_id)
				&& !user_id.equals(CommonFacade.getLoginId(session))) {
		%>
		<TD align="CENTER"><INPUT TYPE="BUTTON" class="button"
			onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
			NAME="BUTTON" style="cursor:hand;" VALUE="删除" onclick="deleMit();" />
		<input type="button" class="button" value="解锁" onclick="unLock();"/>
		</TD>
		<%
		}
		%>
		<TD align="CENTER"><INPUT TYPE="BUTTON" class="button"
			onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
			NAME="BUTTON" style="cursor:hand;" VALUE="重置" onclick="_frmReset();" /></TD>
	</TR>
</TABLE>
</center>
<%
}
%>
</FORM>
</BODY>
</HTML>






