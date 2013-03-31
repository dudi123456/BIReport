<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.base.table.*"%>
<%@ page import="com.ailk.bi.system.common.*"%>
<%@ page import="com.ailk.bi.filemgr.common.ListFile"%>

<%@ page import="com.ailk.bi.common.dbtools.DAOFactory"%>


<%
	if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,
			response))
		return;
%>
<%
			InfoOperTable user = (InfoOperTable)session.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
			String pageRight = "11";//CommTool.CheckActionPage(session,CObjects.ROLE);
			//判断提交类型
			String submitType = request.getParameter("submitType");
			//目录ID
			String favor_id = CommTool.getParameterGB(request,"favor_id");
			String res_id = CommTool.getParameterGB(request,"res_id");
			if (favor_id == null || "".equals(favor_id)) {

			}else if((submitType == null) || "".equals(submitType)){
				submitType = "1";
			}
			//目录名称
			String favor_name = CommTool.getParameterGB(request,"favor_name");
			if (favor_name == null || "".equals(favor_name)) {
				favor_name = "";
			}
			InfoFavoriteTable favorInfo = new InfoFavoriteTable();
			//取出目录信息
			if(!"".equals(favor_id) || !"".equals(res_id)) {
				favorInfo = ListFile.getDirInfo(favor_id);
			}
			String parent_name = "";
			if(favorInfo.parent_id != null && !"".equals(favorInfo.parent_id)) {
				parent_name = ListFile.getDirName(favorInfo.parent_id);
			}
			if (favorInfo == null) {
				favorInfo = new InfoFavoriteTable();
			}

			if ((submitType == null) || "".equals(submitType)) {
				submitType = "0";
			}

String grp_id = DAOFactory.getCommonFac().getLoginUser(session).group_id;

			%>

<HTML>
<HEAD>
<TITLE>目录信息</TITLE>
<%@ include file="/base/commonHtml.jsp" %>
<script src="<%=request.getContextPath()%>/js/common.js" language="javascript"></script>
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

  function submitClick(submitType){
	var a = document.body.getElementsByTagName("INPUT");//新增
	for(var i=0; i<a.length; i++){
				if(a[i].runtimeStyle.color != a[i].style.color){
					alert("请正确填写信息!");
					return;
	}}
	if(frmEdit.favor_name.value==null||trim(frmEdit.favor_name.value)==""){
				alert("请填写目录名称!");
			   	document.all.favor_name.focus();
			   	return;
	}

	if(!trim(frmEdit.parent_name.value)=="" && frmEdit.favor_id.value == frmEdit.parent_id.value){
		alert("该目录的父节点不能是自己!");
	   	document.all.parent_name.focus();
	   	return;
	}
	frmEdit.favor_name.value=trim(frmEdit.favor_name.value);
//	alert(frmEdit.favor__parent_id.value);
	frmEdit.submit();

  }

function addFavor() {
	frmEdit.favor_id.value="";
	frmEdit.favor_name.value="";
	frmEdit.res_id.value="";
	frmEdit.parent_id.value="";
	frmEdit.parent_name.value="";
	frmEdit.submitType.value="0";
	frmEdit.action = "dirView.jsp";
	frmEdit.submit();
}

function delFavor(){//删除
	if(document.frmEdit.favor_name.value ==null || document.frmEdit.favor_name.value =="")
		{
			alert("请选择要删除的目录！");
			return;
		}
	else if(confirm("确认要删除该目录吗？"))
	{
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
			parent.favorDefPage.favor_id='<%=favor_id%>';

        	if(<%=submitType%> == '3'){
					parent.favorList_frame.location.href="dirTree.jsp";
        	}
	}
  }
</script>
</HEAD>
<BODY onLoad="pageOnLoad();" >
<FORM name="frmEdit" action="CreateFileDirXML.rptdo?opType=addDir" METHOD="POST" onsubmit="return false;">
	<INPUT TYPE="hidden" id=user_id name="user_id" value="<%=user.user_id %>" />
    <INPUT TYPE="hidden" id=favor_id name="favor_id" value="<%=favor_id %>" />
    <INPUT TYPE="hidden" id=res_id name="res_id" value="<%=res_id %>" />
	<INPUT TYPE="hidden" id="submitType2" name="submitType" value="<%=submitType%>" />
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

	<tr>
    <td height="100%" valign="top" class="favorites-bg"> <br><br>
      <table border="0" cellspacing="0" cellpadding="0" class="tableSty">
        <TR>
			<TD >目录名称:</TD>
			<TD>
				<INPUT type='text' name="favor_name" size="35" maxlength="40" value="<%=favorInfo.favorite_name%>"
				class="input-text"  /> *
			</TD>
		</TR>
		<TR>
			<TD>创建到:</TD>
			<TD> <INPUT TYPE="hidden" id="parent_id"
				name="parent_id" />
			<INPUT
				type="text" name="parent_name" size="20" maxlength="20"
				value="<%=parent_name%>"
				onclick="setDept(this, document.all.parent_id,document.all.parent_id,document.all.parent_name,'<%=favor_id%>','../system/dpDirTree.jsp');"
				 style="cursor:text" />如果为空，则为根节点</TD>
		</TR>
		</table>
		<br><br><br><br>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td align="center">
            <input name="imageField2" type="button" value="新增" onclick="submitClick(<%=submitType%>);" border="0">
          	<%if(grp_id.equals("1"))
          	{%>
          	<input name="imageField3" type="button" value="删除" onclick="delFavor();" border="0">
          	<%}%>
          	<input name="imageField" type="button" value="清空" onclick="addFavor();" border="0">
          	<!--  <input type="button" name="dc" class="btn3" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" value="导 出" onclick="javascript:_exportSubmit();">-->
          </td>
        </tr>
        </table>


		</td>
	</tr>
</table>
</FORM>
</BODY>
    <script type="text/javascript">
        domHover(".btn3", "btn3_hover");

        function switchClass2(theObj)
		{
			if(theObj.className.indexOf("_hover")<0)
		{
			theObj.className=theObj.className+"_hover";
		}
		else
		{
			theObj.className=theObj.className.replace("_hover","");
		}
		}

    </script>
</HTML>