<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.ProjectInfo"%>
<%@page import="com.ailk.bi.marketing.entity.TacticInfo"%>
<%@page import="com.ailk.bi.marketing.entity.ChannleInfo"%>
<%@page import="com.ailk.bi.marketing.entity.ActivityInfo"%>
<%@page import="com.ailk.bi.marketing.entity.NameListInfo"%>
<%@page import="com.ailk.bi.marketing.entity.FileInfo"%>
<%@page import="com.ailk.bi.marketing.entity.GroupInfo"%>
<%@page import="com.ailk.bi.marketing.entity.SellUserInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base target = "_self">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>北京联通统一经营分析系统</title>
<BIBM:SelfRefreshTag pageNames="frmEdit.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/chart.js" ></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/dojo.js"></SCRIPT>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/XmlRPC.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/date/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/icon.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/icontent.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/pubilicButton.css" />
<%
			GroupInfo groupInfo = (GroupInfo)request.getAttribute("GroupInfo");
			List<SellUserInfo> userlist = (List<SellUserInfo>) request.getAttribute("SellUserInfoList");

			String selectCss = "class='easyui-combobox' style='width:180px;'";
			String groupName="";
			String groupType="";
			String createType="";//来源
			String status="";//提状态
			String brand="";//客户类型
			String count="";
			String remork="";

			if(null!=groupInfo){
				groupName = groupInfo.getGroupName();
				groupType = CodeParamUtil.codeListParamFetcher(request,"MK_PL_GROUP_TYPE",String.valueOf(groupInfo.getGroupTypeId()));
				createType= CodeParamUtil.codeListParamFetcher(request,"MK_PL_GROUP_CREATE_TYPE",String.valueOf(groupInfo.getCreateType()));
				status    = CodeParamUtil.codeListParamFetcher(request,"MK_PL_GROUP_STATUS",groupInfo.getStatus()) ;
				brand 	  = CodeParamUtil.codeListParamFetcher(request,"MK_PL_GROUP_BRAND_OF",String.valueOf(groupInfo.getBrandOf()));
				count     = String.valueOf(groupInfo.getCustomerCount());
				remork    = groupInfo.getUserCharacter();
			}
	    String imgUrl =request.getContextPath()+"/images/validatebox_warning.gif";
	%>
<script language="javascript">
function openFile(obj){
	 var oo = getPath(obj);
	 document.getElementById("fileUrl").value=oo;
}
function getPath(obj)
{
if(obj)
   {

  if (window.navigator.userAgent.indexOf("MSIE")>=1)
    {
       obj.select();

     return document.selection.createRange().text;
    }

 else if(window.navigator.userAgent.indexOf("Firefox")>=1)
    {
     if(obj.files)
       {
       return obj.files.item(0).getAsDataURL();
       }
     return obj.value;
    }
  return obj.value;
  }
}
function isTrueValue(){
		var tt = document.getElementById("txt_phoneNumber").value;
		if(tt==""){
			alert("请输入手机号码！");
			return false;
		}else if(isNaN(tt)){
			alert("请输入正确的手机号码！(有非法字符)");
			return false;
		}else if(tt.length!=11){
			alert("请输入正确的手机号码！(位数不对，必须为11位)");
			return false;
		}
		return true;
}
function mySave() {
		if(isTrueValue()){
			form1.action="sellUserAction.rptdo?optype=sellUserInfo&doType=save";
			form1.submit();
		}
}
function showClose(){
	form1.action="sellUserAction.rptdo?optype=sellUserInfo&doType=search";
	form1.submit();
}
function showMode(op){
	var createType="<%=groupType%>";

	if("普通客户群"==createType){
		alert("该客户群为普通客户群，不能手动维护");
		return ;
	}else{
		var div = document.getElementById("myModeDiv");
		div.style.display=op;
	}
}
function myDelete(){
	var createType="<%=groupType%>";

	if("普通客户群"==createType){
		alert("该客户群为普通客户群，不能手动维护");
		return ;
	}else{
		var arr = document.getElementsByName("checkbox");
		var checlCount = 0;
		for(var i =0;i< arr.length;i++ ){
			if(arr[i].checked){
				checlCount=checlCount+1;
			}
		}
		if(checlCount>0){
			if(confirm("是否确定删除选中的"+checlCount+"条信息？")){
				form1.action="sellUserAction.rptdo?optype=sellUserInfo&doType=delect";
				form1.submit();
			}
		}else{
			alert("请至少选择一条要删除的信息！");
	    }
	}

}

function mydo(){
	form1.action="sellUserAction.rptdo?optype=sellUserInfo&doType=fileRead";
	form1.submit();
}

<%
 String msg = (String)request.getAttribute("fileReadMsg");
if(null==msg){
	%>
	<%}else {
		%>
		alert("有"+<%=msg%>+"条数据导入失败！");
		<%
	}
%>
</script>

</head>
<body style="background-color:#f9f9f9">
<form action="" method="post" name="form1">
<input type="hidden" id="groupId" name="groupId" value="<%=groupInfo.getGroupId()%>" >
  <div id="maincontent">
    <div class="toptag"> 您所在位置：营销策划 >>套餐管理>><em class="red">活动详细信息</em> <span class="hbt"><a href="javascript:;"
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span><span class="hbt1">
    </span></div>

  <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
<div id="myModeDiv" style="display: none">
 <div style="width:90%;" class="validatebox-tabTD-title">
<div style="float:left">
营销案例信息录入
</div>
<div style="float:right">
  <button class="btn4" type="button" onClick="showClose()"> 取消 </button>
</div>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
 <tr>
          <td  width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />手机号码：</td>
          <td colspan="3" class="validatebox-tabTD-right">
		  <input value="" class="easyui-validatebox" required="true" validType="length[1,11]" id="txt_phoneNumber" name="txt_phoneNumber">
          <button class="btn4" type="button" onClick="mySave()"> 保 存 </button>
          </td>
        </tr>

    <tr>
          <td  width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />批量导入：</td>
          <td colspan="3" class="validatebox-tabTD-right">
          <input type="hidden" name="fileUrl" id="fileUrl">
  <input id="fileName" type="file" name="fileName"  onchange="openFile(this)">
  <input type="button" name="button"  value="开始导入" class="btn4"  onclick="mydo()">

          </td>
        </tr>
</table>

</div>


<div style="width:90%;" class="validatebox-tabTD-title">
<div style="float:left">
客户群详细信息列表
</div>
<div style="float:right">
  <button class="btn4" type="button" onClick="showMode('block')"> 添加手机号码 </button>
</div>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
        <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />客户名单名称：</td>
          <td width="21%" class="validatebox-tabTD-right"><%=groupName %></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />客户群类型：</td>
          <td class="validatebox-tabTD-right">
          <%=groupType %>
          </td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />客户群来源类型：</td>
          <td width="21%" class="validatebox-tabTD-right">
            <%=createType%>
         </td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />所属品牌：</td>
          <td class="validatebox-tabTD-right">  <%=brand %></td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />提取数据状态：</td>
          <td width="21%" class="validatebox-tabTD-right">
           <%=status %>
          </td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />包含客户数：</td>
          <td class="validatebox-tabTD-right">
            <%=count %>
          </td>
        </tr>

</table>
<div class="validatebox-tabTD-title">客户信息列表</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />客户名单列表：</td>
          <td colspan="3" class="validatebox-tabTD-right">
<div id="allGruopList" style="display: blok">
      <div class="list_content">
   <table width="50%">
     <tr width="30%">
       <th width="8%" align="center"> 序号 </th>
         <th width="15%" align="center"> 客户手机号码</th>
           <th width="8%" align="center"> 序号 </th>
         <th width="15%" align="center"> 客户手机号码</th>
           <th width="8%" align="center"> 序号 </th>
         <th width="15%" align="center"> 客户手机号码</th>
     </tr>
<%
 	if(null!=userlist){
       		for(int i = 0 ;i<userlist.size();i++){
       			if(i%2!=0){
       				%>
       				<tr class="jg">
       				<%
       			}else{
       			%>
       			 <tr><%} %>
          <td align="center">
            <input type="checkbox"  name="checkbox" value="<%=userlist.get(i).getId() %>">
          </td>
          <td align="center"><%=userlist.get(i).getServNumber() %></td>
          <td align="center"><%if((i+1)<=userlist.size()-1) { %> <input type="checkbox"  name="checkbox" value="<%=userlist.get(i+1).getId() %>"> <%} %></td>
          <td align="center"><%=(i+1)>userlist.size()-1?"":userlist.get(i+1).getServNumber() %></td>
          <td align="center"><%if((i+2)<=userlist.size()-1) {  %> <input type="checkbox"  name="checkbox" value="<%=userlist.get(i+2).getId() %>"><%} %></td>
          <td align="center"><%=(i+2)>userlist.size()-1?"":userlist.get(i+2).getServNumber() %></td>
       </tr>	<%
     		  i=i+2;
       		}
       	}%>
       	<tr class="jg">
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
                <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              </tr>
     </table>

   </div>
     <input type="button" class="public-btn2" value="隐藏手机列表" onclick="javascript:document.getElementById('allGruopList').style.display='none';document.getElementById('allGruopList2').style.display='block'" />
      <input type="button" class="public-btn2" value="删除手机号码" onclick="javascript:myDelete()" />
</div>
<div id="allGruopList2" style="display: none">
 <input type="button"  class="public-btn2" value="显示手机列表" onclick="javascript:document.getElementById('allGruopList').style.display='block';document.getElementById('allGruopList2').style.display='none'" />
  </div>        </td>
        </tr>
</table>

</form>

</body>
</html>
