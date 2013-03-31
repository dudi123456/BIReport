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
			InfoOperTable user = (InfoOperTable)session.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
			String pageRight = "11";//CommTool.CheckActionPage(session,CObjects.ROLE);
			//判断提交类型
			String submitType = request.getParameter("submitType");
			//收藏夹ID
			String favor_id = CommTool.getParameterGB(request,"favor_id");
			String res_id = CommTool.getParameterGB(request,"res_id");
			if (favor_id == null || "".equals(favor_id)) {

			}else if((submitType == null) || "".equals(submitType)){
				submitType = "1";
			}

			//收藏夹名称
			String favor_name = CommTool.getParameterGB(request,"favor_name");
			if (favor_name == null || "".equals(favor_name)) {
				favor_name = "";
			}
			InfoFavoriteTable favorInfo = new InfoFavoriteTable();
			//取出收藏夹信息
			if(!"".equals(favor_id) || !"".equals(res_id)) {
				favorInfo = LSInfoFavorite.getFavorInfo(user.user_id,favor_id,res_id);
			}
			String parent_name = "";
			if(favorInfo.parent_id != null && !"".equals(favorInfo.parent_id)) {
				parent_name = LSInfoFavorite.getFavorName(user.user_id,favorInfo.parent_id);
			}
			if (favorInfo == null) {
				favorInfo = new InfoFavoriteTable();
			}

			if ((submitType == null) || "".equals(submitType)) {
				submitType = "0";
			}

			%>

<HTML>
<HEAD>


<script language=javascript>

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

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/icontent.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>

<TITLE>收藏夹信息</TITLE>
<LINK REL="stylesheet" TYPE="text/css" HREF="<%=request.getContextPath()%>/css/other/tab.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
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

				alert("请填写收藏夹名称!");
			   	document.all.favor_name.focus();
			   	return;
	}

	if(trim(frmEdit.parent_name.value) !="" &&
			frmEdit.favor_id.value == frmEdit.parent_id.value)
	{
		{
			alert("该收藏夹的父节点不能是自己!");
			document.all.parent_name.value = "";
		   	document.all.parent_name.focus();
		   	return;
		}
	}

	frmEdit.favor_name.value=trim(frmEdit.favor_name.value);
	//alert(frmEdit.favor__parent_id.value);
	frmEdit.submit();

  }

function addFavor() {
	frmEdit.favor_id.value="";
	frmEdit.favor_name.value="";
	frmEdit.res_id.value="";
	frmEdit.parent_id.value="";
	frmEdit.parent_name.value="";
	frmEdit.submitType.value="0";
	frmEdit.action = "favoriteView.jsp";
	frmEdit.submit();
}

function delFavor(){//删除
	var obj = document.frmEdit.favor_name.value;
	if(obj == null || obj =="")
	{
		alert("请选择要删除的收藏夹！");
		return ;
	}
	else
	{
		if(confirm("确认要删除该收藏夹吗？"))
		{
			frmEdit.submitType.value = 2;
			frmEdit.submit();
			return ;
		}
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
					parent.favorList_frame.location.href="favoriteTree.jsp";
        	}
	}
  }
</script>
</HEAD>
<BODY onLoad="pageOnLoad();" >
<div id="maincontent">
<FORM name="frmEdit" action="favorite.rptdo" METHOD="POST" onsubmit="return false;">
	<INPUT TYPE="hidden" id=user_id name="user_id" value="<%=user.user_id %>" />
    <INPUT TYPE="hidden" id=favor_id name="favor_id" value="<%=favor_id %>" />
    <INPUT TYPE="hidden" id=res_id name="res_id" value="<%=res_id %>" />
	<INPUT TYPE="hidden" id="submitType2" name="submitType" value="<%=submitType%>" />
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" >

	<tr>
    <td height="100%" valign="top" class="favorites-bg-big_old"> <br><br>
    <div class="topsearch">
 	<table>
        <TR>
			<TD >收藏夹名称:</TD>
			<TD>
				<INPUT type='text' name="favor_name"  value="<%=favorInfo.favorite_name%>"
				class="txtinput"  /> *
			</TD>
		</TR>
			<TR>
			<TD>创建到:</TD>
			<TD> <INPUT TYPE="hidden" id="parent_id" name="parent_id" />
			<INPUT	type="text" name="parent_name"	id="parent_name" value="<%=parent_name%>"
				onclick="setDept(this, document.all.parent_id,document.all.parent_id,document.all.parent_name,'<%=favor_id%>','../system/dpFavorTree.jsp');"
				class="txtinput" readonly style="cursor:text" />如果为空，则为根节点</TD>
		</TR>
		</table>
    </div>

    <!--
      <table border="0" cellspacing="0" cellpadding="0" class="tableSty">
        <TR>
			<TD >收藏夹名称:</TD>
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
				onclick="setDept(this, document.all.parent_id,document.all.parent_id,document.all.parent_name,'<%=favor_id%>','../system/dpFavorTree.jsp');"
				 style="cursor:text" />如果为空，则为根节点</TD>
		</TR>
		</table>
		 -->

		<br><br><br><br>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td align="center">
            <!-- <input name="imageField2" type="image" src="../biimages/save.gif" onclick="submitClick(<%=submitType%>);" border="0">
            <input name="imageField3" type="image" src="../biimages/system/delete.gif" onclick="delFavor();" border="0">
          	<input name="imageField" type="image" src="../biimages/new_folder.gif" onclick="addFavor();" border="0">
          	 -->
          	 <div class="co_btn" id="co_btn" >
          	<input name="imageField2" class="btn3" type="button"  onclick="submitClick(<%=submitType%>);"  value="保 存" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" />
            <input name="imageField3" class="btn3" type="button"  onclick="delFavor()" value="删 除" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"/>
          	<input name="imageField" class="btn4" type="button"  onclick="addFavor();" value="新建文件夹" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"/>
			</div>
          </td>
        </tr>
        </table>


		</td>
	</tr>
</table>
</FORM>
</div>
</BODY>
</HTML>