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
	//区域ID
	String region_id = CommTool.getParameterGB(request, "region_id");
	if (region_id == null || "".equals(region_id)) {
		region_id = "-1";
	}

	//模块信息
	InfoRegionTable infoRegion = LSInfoRegion.getRegionInfo(region_id);
	if (infoRegion == null) {
		infoRegion = new InfoRegionTable();
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
<TITLE>区域信息维护</TITLE>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
<SCRIPT src="<%=request.getContextPath()%>/js/picker/rgPicker.js"></SCRIPT>
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
	if(value =="" || value=="")
		{
			alert("请填写区域编码！");
			document.frmEdit.region__region_id.focus();
			return;
		}
	var ret=false;
	var doc=baseXmlSubmit.callAction("../system/regionAction.jsp?region_id="+value+"&oper_type="+oper_type);
	doc=doc.replace(/^\s+|\n+$/g,'');
     if(doc == "0"){//成功
		alert("不存在相同的区域编码，您可以继续操作！");
     }else if(doc == "1"){//不成功
		alert("存在相同的区域编码，请您尝试别的区域编码！");
		document.frmEdit.region__region_id.value="";
		document.frmEdit.region__region_id.focus();
		return;
     }

}
//
function pageOnLoad()
{
	//刷新界面
    	if(<%=(submitType.equals("1")||submitType.equals("2"))%>){
			parent.RegionList.location.href="regiontree.jsp";
			if(<%=submitType.equals("2")%>){
				parent.RegionView.location = "regionview.jsp";
			}
	    }
}
//
</script>
<SCRIPT language="javascript">
function submitClick(submitType)
{
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

			if(document.getElementsByName("region__region_id")[0].value == null ||
					document.getElementsByName("region__region_id")[0].value == "")
				{
					alert("区域编码不能为空，请重新输入!");
					document.getElementsByName("region__region_id")[0].focus();
					return;
				}
			if(document.getElementsByName("region__region_name")[0].value == null ||
					document.getElementsByName("region__region_name")[0].value == "")
				{
					alert("区域名称不能为空，请重新输入!");
					document.getElementsByName("region__region_name")[0].focus();
					return;
				}
			if(document.frmEdit.region__region_id.value==document.frmEdit.region__parent_id.value)
			{
				alert("上级区域不能为自身！");
				var v = document.getElementsByName("region__parent_name")[0];
				v.value = "";
				v.focus();
				break;
			}
			else if( document.frmEdit.region__parent_name.value == null ||
					document.frmEdit.region__parent_name.value == "")
			{
				alert("上级区域不能为空，请重新输入！");
				document.getElementsByName("region__parent_name")[0].focus();
				break;
			}

			frmEdit.submit();
			break;

		case 2:
			if(document.frmEdit.region__region_id.value == "" || document.frmEdit.region__region_id.value == null)
       		{
       			alert("请选择要删除的区域！");
       			return ;
       		}
        	else
       	    {
        		if(confirm("确认要删除该区域吗？"))
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
			height="7"><b><%=infoRegion.region_name%></b>&nbsp;区域信息</td>
	</tr>
	<tr>
		<td height="1" background="../images/common/system/black-dot.gif"></td>
	</tr>
	<TR>
		<td valign="top" align="center"><br>
		<TABLE WIDTH="500" cellpadding="0" CELLSPACING="0">
			<TR>
				<TD>
				<FORM name="frmEdit" action="regionview.rptdo"
					METHOD="POST"		onsubmit=" returnfalse;">
					<INPUT	TYPE="hidden" id="oper_type" name="oper_type" value="<%=oper_type%>" />
                    <INPUT	TYPE="hidden" id="submitType" name="submitType" value="<%=submitType%>" />

			<TABLE bgColor="#999999" WIDTH="100%" CELLSPACING="0"
					CELLPADDING="0">
					<TR>
						<TD>
						<TABLE width="100%" CELLPADDING="4" border="0" cellspacing="1">

							<TR bgColor="#ffffff">
								<TD ALIGN="left">区域编码</TD>
								<TD ALIGN="left"><INPUT type='text' name="region__region_id"
									maxlength="32" value="<%=infoRegion.region_id%>"
									 /><label style="color: red">*</label><input type="button" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON" style="cursor:hand;" VALUE="检 测"
							onclick="CheckExsit(document.frmEdit.region__region_id.value,1);" /></TD>
							</TR>
							<TR bgColor="#ffffff">
								<TD ALIGN="left">区域名称</TD>
								<TD ALIGN="left" ><INPUT type='text' name="region__region_name"
									maxlength="32" value="<%=infoRegion.region_name%>"
									 /><label style="color: red">*</label></TD>
							</TR>

							<TR bgColor="#ffffff">
								<TD ALIGN="left">上级区域</TD>
								<TD ALIGN="left">
								<INPUT TYPE="hidden" id="region__parent_id" name="region__parent_id" value="<%=infoRegion.parent_id%>" />
								<INPUT type="text"  name="region__parent_name" size="20" maxlength="20" value="<%=infoRegion.parent_name%>" onclick="setRegion(this, document.all.region__parent_id,'<%=loginUser.region_id%>','../system/areaTree.jsp');"  readonly style="cursor:text"/><label style="color: red">*</label>
                                                    		</TD>
							</TR>
							<TR bgColor="#ffffff">
								<TD ALIGN="left">有效标志</TD>
								<TD ALIGN="left"><BIBM:TagSelectList listName="region__status"
									listID="#" focusID="<%=infoRegion.status%>"
									selfSQL="1,有效;0,无效;" /><label style="color: red">*</label></TD>
							</TR>
							<TR bgColor="#ffffff">
								<TD ALIGN="left">备注</TD>
								<TD ALIGN="left">
								<textarea rows="4" name="region__comments" style="width:98%;overflow:auto" class="query-input-text"><%=infoRegion.comments%></textarea></TD>
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
							NAME="BUTTON1" style="cursor:hand;" VALUE="保存"
							onclick="submitClick(1);" /></TD>
						<TD align="CENTER"><INPUT TYPE="BUTTON" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON2" style="cursor:hand;" VALUE="删除"
							onclick="submitClick(2);" /></TD>
						<TD align="CENTER"><INPUT TYPE="BUTTON" class="button"
							onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
							NAME="BUTTON3" style="cursor:hand;" VALUE="重置"
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








