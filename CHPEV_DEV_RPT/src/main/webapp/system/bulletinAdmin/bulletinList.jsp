<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.report.struct.*"%>
<%@ page import="com.ailk.bi.common.app.StringB"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ailk.bi.bulletin.entity.*"%>
<%
String rootPath = request.getContextPath();
	//列表数据
List list = (ArrayList)session.getAttribute("BULLETIN_BOARD_LIST");
	//查询条件
	System.out.println("sdfds:" + list.size());
	if(list == null){
	list = new ArrayList();
}

	//查询条件
	ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
	if(qryStruct == null){
		qryStruct = new ReportQryStruct();
	}
%>
<!DOCTYPE html>
<html>
<head>
<title>经营分析</title>
<%@ include file="/base/commonHtml.jsp"%>
<link href="<%=rootPath%>/css/other/bimain.css" rel="stylesheet" type="text/css" />
<link href="<%=rootPath%>/css/tablecss/main.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=rootPath%>/js/date/WdatePicker.js"></script>
<SCRIPT language=javascript src="<%=rootPath%>/js/dojo.js"></SCRIPT>
<script type="text/javascript" src="<%=rootPath%>/js/XmlRPC.js"></script>

<script lanaguage="javascript">
var rootTmpPath = '<%=rootPath%>';
function BaseXmlSubmit(){
}
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

var baseXmlSubmit =new BaseXmlSubmit();

function _delete(id){
 if(confirm("您确定要删除吗？此操作不可恢复!")){

 	var strUrl = "bulletinAdmin.rptdo?opt_type=delBulletin&id=" + id;
	//alert(strUrl);
 	document.form1.action=strUrl;
	document.form1.submit();
}
}

function isEmpty(s){
  return ((s == null) || (s.length == 0))
}
function isSpace(content2){
  for(i=0;i<content2.length;i++){
    var c=content2.charAt(i);
    if(c!=" ") return false;
  }
  return true;
}
function getStringLength(contact){
  var num=0;
  if (contact!=""){
    var i;
    var s;
    for(i=0;i<contact.length;i++){
      s=contact.charCodeAt(i);
      if(s-128<0) num=num+1;
      else num=num+2;
    }
  }
  return num;
}
function getValue(){

 if(isEmpty(document.form1.starttime.value)){
     alert("请输入生效时间，不能为空！");
     document.form1.starttime.focus();
     return false;
  }
    if(isSpace(document.form1.starttime.value)){
      alert("请输入生效时间，不能全部为空格！");
      document.form1.starttime.focus();
      return false;
    }
 if(isEmpty(document.form1.endtime.value)){
     alert("请输入失效时间，不能为空！");
     document.form1.endtime.focus();
     return false;
  }
    if(isSpace(document.form1.endtime.value)){
      alert("请输入失效时间，不能全部为空格！");
      document.form1.endtime.focus();
      return false;
    }

 if(isEmpty(document.form1.title.value)){
     alert("请输入公告标题，不能为空！");
     document.form1.title.focus();
     return false;
  }
    if(isSpace(document.form1.title.value)){
      alert("请输入公告标题，不能全部为空格！");
      document.form1.title.focus();
      return false;
    }
	if(getStringLength(document.form1.title.value)>200)
       {
         alert("你输入的公告标题长度过长！");
         document.form1.title.focus();
         return false;
       }
  if(isEmpty(document.form1.content.value)){
     alert("请输入公告内容，不能为空！");
     document.form1.content.focus();
     return false;
  }
    if(isSpace(document.form1.content.value)){
      alert("请输入公告内容，不能全部为空格！");
      document.form1.content.focus();
      return false;
    }
	if(getStringLength(document.form1.content.value)>2000)
       {
         alert("你输入的公告内容长度过长！");
         document.form1.content.focus();
         return false;
       }

	form1.action = 'bulletinAdmin.rptdo?opt_type=doadd';
	form1.submit();
  }


  function doQuery() {
	form1.action="bulletinAdmin.rptdo?opt_type=bAdmin";
	form1.page__iCurPage.value="";
	form1.page__iLinesPerPage.value="";
	form1.page__iLines.value="";
	form1.page__iPages.value="";
	form1.page__checkIDs.value="";
	form1.submit();
}

function doSele(id,city,type_id,starttime,endtime,title,count,system_id) {

	form1.id.value=id;
	form1.city.value=city;
	form1.type_id.value=type_id;
	form1.starttime.value=starttime;
	form1.endtime.value=endtime;
	form1.title.value=title;
	form1.system_id.value=system_id;

	form1.content.value=eval("form1.cont"+count+".value");
	form1.hidTxtBulRight.value=eval("form1.bulRight"+count+".value");
	form1.txtBulRight.value = "";
	if (form1.hidTxtBulRight.value.length>0)
	{
		var bar_desc = baseXmlSubmit.callAction(rootTmpPath + "/system/bulRight_multiCheck_ajax.jsp?idStr="+form1.hidTxtBulRight.value);
	//	alert("zxj:" + form1.hidTxtBulRight.value + ":");
	//	form1.txtBulRight.value
		form1.txtBulRight.value = bar_desc;
	}else{
		form1.txtBulRight.value = "";
	}
//	alert(form1.hidTxtBulRight.value);
	//	var bar_desc = baseXmlSubmit.callAction(rtPath + "/system/bulRight_multiCheck_ajax.jsp?idStr="+form1.hidTxtBulRight.value);
	//	bar_desc=bar_desc.replace(/^\s+|\n+$/g,'');

	//		form1.txtBulRight.innerText = bar_desc;


}

function checkEvent(name, allCheckId) {
    var allCk = document.getElementById(allCheckId);
    if (allCk.checked == true) checkAll(name);
    else checkAllNo(name);

 }

//全选
function checkAll(name) {
    var names = document.getElementsByName(name);
    var len = names.length;
    if (len > 0) {
        var i = 0;
        for (i = 0; i < len; i++)
         names[i].checked = true;

     }
 }

//全不选
function checkAllNo(name) {
    var names = document.getElementsByName(name);
    var len = names.length;
    if (len > 0) {
        var i = 0;
        for (i = 0; i < len; i++)
         names[i].checked = false;
     }
 }

function doAdd(){
	form1.reset();
  }

function openBulletinRight(rtPath){
		returnValue=window.showModalDialog(rtPath + "/system/bulletinRight.jsp?time=" + Date(),window,"dialogWidth:600px; dialogHeight:450px; dialogLeft:320px; dialogTop:250px; status:no; directories:yes;scrollbars:no;Resizable=no;help:no");
		if(returnValue == null || returnValue == "" || returnValue == "undefined"){
			form1.txtBulRight.innerText = "";
			form1.hidTxtBulRight.value="";
			return;
	    }else{
			form1.hidTxtBulRight.value=returnValue;

		var bar_desc = baseXmlSubmit.callAction(rtPath + "/system/bulRight_multiCheck_ajax.jsp?idStr="+returnValue);
		bar_desc=bar_desc.replace(/^\s+|\n+$/g,'');

			form1.txtBulRight.innerText = bar_desc;

		}
	//window.open(rtPath + "/system/bulletinRight.jsp");
  }

function doDelete() {
    if(form1.id.value=="") {
    	alert("请选择要删除的信息！");
		return false;
    }

    form1.action = 'bulletinAdmin.rptdo?opt_type=delBulletin&id='+form1.id.value;
    form1.submit();
}
 </script>


<body>
<table style="width: 99%;" align="center" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="2">
        <div class="toptag">
            <Tag:Bar />
        </div>
		</td>
	</tr>
</table>
<form name="form1" action="bulletinAdmin.rptdo?opt_type=bAdmin" method="post">
<!--显示script部分-->
<%=WebPageTool.pageScript("form1","bulletinList.screen")%>
<%
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, list.size(), 5 );
String init = request.getParameter("init");
if("true".equals(init)) {
	pageInfo.iCurPage=0;
}
String systemId = StringB.NulltoBlank((String)session.getAttribute("system_id"));
		if (systemId.length()==0){
			systemId = "1";
		}
%>

<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>
<input type="hidden" name="id" >
<table width="100%" border="0" cellpadding="5" cellspacing="1" >
  <tr>
    <td class="td-info">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>

            <td >生效时间：</td>
            <td ><input name="qry_type_id" type="hidden"  value="1" ><input name="qry_city" type="hidden"  value="" ><INPUT TYPE="text" NAME="qry_starttime" id="qry_starttime" onfocus="WdatePicker({isShowClear:false,readOnly:true})"  >
			</td>

            <td >公告标题：</td>
            <td ><input name="qry_title" type="text"  value="<%=qryStruct.dim2%>" ></td>

            <td >
            <input type="button" id="button_search" value="查询" onclick="return doQuery()">
            </td>
          </tr>
        </table>
  </td>
  </tr>
  </table>
<table width="100%" border="0" cellpadding="0" cellspacing="1" >
<tr>
<td>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="datalist">
  <tr class="celtitle">
    <td width="4%" align="center" class="leftdata"> </td>
    <td height="22" width="25%" align="center" >公告标题</td>
    <td height="22" width="55%" align="center" >公告内容</td>
    <td height="22" width="8%" align="center" >生效时间</td>
    <td height="22" width="8%" align="center" >失效时间</td>
  </tr>
                          <%if(list==null||list.size()==0){ %>
  <tr class="celdata">
    <td colspan="5" nowrap align="center">该条件下没有符合要求的数据</td>
  </tr>
  <%}else{ %>

  <%for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
		//String[] value = list[i+pageInfo.absRowNoCurPage()];
		MartInfoBulletin objInfo = (MartInfoBulletin)list.get(i+pageInfo.absRowNoCurPage());
  %>  <input type="hidden" name="cont<%=i %>" value="<%=objInfo.getNewsMsg() %>"><input type="hidden" name="bulRight<%=i %>" value="<%=objInfo.getGroupId() %>">
            <tr class="celdata">
              <td align="center" bgcolor="#FFFFFF"><input type="radio" name="ck" onclick="doSele(<%=objInfo.getId()%>,'<%=objInfo.getCityId()%>','<%=objInfo.getTypeId() %>','<%=objInfo.getValidBeginDate() %>','<%=objInfo.getValidEndDate() %>','<%=objInfo.getTitle() %>','<%=i %>','<%=objInfo.getSystemId() %>')"></td>
              <td height="20"  class="leftdata"><%=objInfo.getTitle()%></td>
              <td height="20"  class="leftdata"><%=objInfo.getNewsMsg()%></td>
              <td height="20"  class="leftdata"><%=objInfo.getValidBeginDate()%></td>
              <td height="20"  class="leftdata"><%=objInfo.getValidEndDate()%></td>
            </tr>

               <%} %>
  <%} %>
</table>
</td>
</tr>

<tr>
  <td><%=WebPageTool.pagePolit(pageInfo)%></td>
</tr>

</table>

<table width="100%" border="0" cellpadding="0" cellspacing="1" class="info-table">


     <tr>
         <td>公告标题:</td>
         <td colspan="3"><textarea name="title" rows="1" cols="70"></textarea>&nbsp;&nbsp;<font color="red">*</font></td>
     </tr>
     <tr>
          <td>公告内容:</td>
          <td colspan="3"><textarea name="content" rows="4" cols="70"></textarea>&nbsp;&nbsp;<font color="red">*</font></td>
    </tr>
	<tr>
         <td>生效时间：</td>
         <td colspan="3"><input name="type_id" type="hidden"  value="1" ><input name="system_id" id="system_id" type="hidden"  value="<%=systemId%>" ><INPUT TYPE="text" NAME="starttime" id="starttime" onfocus="WdatePicker({isShowClear:false,readOnly:true})"  >
             &nbsp;&nbsp;<font color="red">*(yyyyMMdd)</font>&nbsp;&nbsp&nbsp;&nbsp
                失效时间：
         <INPUT TYPE="text" NAME="endtime" id="endtime" onfocus="WdatePicker({isShowClear:false,readOnly:true})"  >
             &nbsp;&nbsp;<font color="red">*(yyyyMMdd)</font></td>

    </tr>

    <tr>
         <td>公告范围：</td>
         <td colspan="3"><input name="hidTxtBulRight" type="hidden"  value="" ><input name="city" type="hidden"  value="" ><textarea name="txtBulRight" readonly rows="4" cols="50"></textarea><img src="../images/common/system/icon-hit.gif" width="16" height="16" style="cursor:hand" alt="请点击设置" onclick="javascript:openBulletinRight('<%=rootPath%>')">
		 </td>
        </tr>

</table>
<br>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
	  	<td nowrap align="center">
		  <input type="button" id="button_add" value="新建" onclick="return doAdd()">
		  <input type="button" id="button_save" value="保存" onclick="return getValue()">
		  <input type="button" id="button_del" value="删除" onclick="return doDelete()">
	   </td>
      </tr>
</table>
</form>
</body>
</html>
