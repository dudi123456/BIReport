<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>

<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
//报表ID
String rpt_id = request.getParameter("rpt_id");
if(rpt_id==null||"".equals(rpt_id)){
	out.print("获取信息失败");
	return;
}
//用户信息
//String oper_no = CommTool.getLoginId(session);
//查询语句
//String strSql1 = "SELECT USER_NAME,PHONE_NUM,DUTY FROM UI_RPT_SMS_USERCUSTOM_DEF WHERE SM_ID='"+rpt_id+"' AND USER_NO='0'";
//String strSql2 = "SELECT USER_NAME,PHONE_NUM,DUTY FROM UI_RPT_SMS_USERCUSTOM_DEF WHERE SM_ID='"+rpt_id+"' AND USER_NO='"+oper_no+"'";
//全部人员列表
String[][] result1 = null;
//自己的人员列表
String[][] result2 = null;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<title>人员信息配置</title>
</head>
<body class="main-body">
<form NAME="mainFrm" ID="mainFrm" ACTION="" method="post">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
              <Tag:Bar defaultValue="SMS100101" favTag="false"/>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="1" background="../biimages/black-dot.gif"></td>
	</tr>
	<tr>
		<td height="2"></td>
	</tr>
  <tr>
    <td height="5"></td>
  </tr>
  <!--报表显示 start-->
  <tr>
    <td class="tab-side2">
<TABLE width="100%" border="0" cellpadding="0" cellspacing="1" class="table-bg">
  <tr align="center">
    <td class="tab-title">人员名称</td>
    <td class="tab-title">手机号码</td>
    <td class="tab-title">职务</td>
    <td class="tab-title">选中
    <input id="SelectAll" type='checkbox' name="SelectAll" value="" onclick="_selectAll();" ></td>
  </tr>
  <%if(result1==null||result1.length==0){ %>
  <tr class="table-white-bg">
    <td colspan="4" nowrap align="center">没有用户信息数据</td>
  </tr>
  <%}else{ %>
  <%for(int i=0;i<result1.length;i++){ %>
  <tr class="table-white-bg">
    <td nowrap><%=result1[i][0]%></td>
    <td nowrap><%=result1[i][1]%></td>
    <td nowrap><%=result1[i][2]%></td>
    <td nowrap>
    <%
      boolean isCheck = false;
      for(int m=0;result2!=null&&m<result2.length;m++){
    	  if(result2[m][1].equals(result1[i][1])){
    		  isCheck = true;
    	  }
      }
    %>
    <input type="checkbox" id="telnum" name="telnum" value="<%=result1[i][0]%>:<%=result1[i][1]%>" <%if(isCheck) out.println("checked=true");%>  onclick='_tabToggle(this);'/>
    </td>
  </tr>
  <%} %>
  <%} %>
</TABLE>
    </td>
  </tr>
  <!--报表显示 end-->
  <tr>
    <td>
<table width="100%">
  <tr>
	<td align="center">
	  <input type="button" value="确 定" name="Ok" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)">
	  <input type="button" value="取 消" name="Cancel" onclick="window.close();" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)">
	</td>
  </tr>
</table>
    </td>
  </tr>
</table>
<INPUT TYPE="hidden" name="HaveSelectedName" id="HaveSelectedName" value="">
<INPUT TYPE="hidden" name="HaveSelectedID" id="HaveSelectedID" value="">
</form>
</body>
<SCRIPT LANGUAGE=JavaScript FOR=Ok EVENT=onclick>
 window.opener.document.all.user_name.value=document.mainFrm.HaveSelectedName.value;
 window.opener.document.all.deviceNumber.value=document.mainFrm.HaveSelectedID.value;
 window.close();
 //document.mainFrm.action = "sendSms.rptdo";
 //document.mainFrm.submit();
</SCRIPT>
<SCRIPT>
function _tabToggle(obj){
	var selStrName = document.mainFrm.HaveSelectedName.value;
	var selStr = document.mainFrm.HaveSelectedID.value;
	var curValue = obj.value.split(":");
	var curName = curValue[0];
	var curID = curValue[1];
	if(obj.checked){
	    if( (selStrName+";").indexOf(curName+";") ){
			if(selStrName!="")
				selStrName += ";";
			selStrName += curName;
		}
		if( (selStr+";").indexOf(curID+";") ){
			if(selStr!="")
				selStr += ";";
			selStr += curID;
		}
	}else{
	    var pos = (selStrName+";").indexOf(curName+";");
		if(pos!=-1){
			selStrName = (selStrName +";").substr(0,pos) + (selStrName +";").substr(pos+curName.length+1);
			selStrName = selStrName.substr(0,selStrName.length-1);
		}
		var pos = (selStr+";").indexOf(curID+";");
		if(pos!=-1){
			selStr = (selStr +";").substr(0,pos) + (selStr +";").substr(pos+curID.length+1);
			selStr = selStr.substr(0,selStr.length-1);
		}
	}
	document.mainFrm.HaveSelectedName.value = selStrName;
	document.mainFrm.HaveSelectedID.value = selStr;
}
function _selectAll(){
	var obj = document.mainFrm.elements.tags("input")
	if (document.all.SelectAll.checked){
		for (i=0; i < obj.length; i++){
			var e = obj[i];
			if(e.name == "telnum" && !e.disabled){
    			e.checked = true;
    			_tabToggle(e);
    		}
		}
	}else{
		for (i=0; i < obj.length; i++){
			var e =obj[i];
			if(e.name == "telnum"){
				e.checked = false;
				_tabToggle(e);
    		}
		}
	}
	return false;
}
</SCRIPT>
</html>