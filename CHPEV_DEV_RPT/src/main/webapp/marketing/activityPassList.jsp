<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.ActivityInfo"%>
<%@page import="com.ailk.bi.marketing.entity.TacticInfo"%>
<%@page import="com.ailk.bi.marketing.entity.ChannleInfo"%>
<%@page import="com.ailk.bi.marketing.entity.PassInfo"%>
 <%@page import="com.ailk.bi.marketing.entity.FileInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="com.ailk.bi.base.table.InfoOperTable"%>
<%@page import="com.ailk.bi.system.facade.impl.CommonFacade"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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
<script language="javascript">
	function mySubmit() {

		form2.action="passAction.rptdo?step=step1";
		form2.submit();
	}
	function mySave() {

		document.getElementById("state").value= document.getElementById("txt_state").value;
		document.getElementById("advice").value= document.getElementById("txt_advice").value;
		form2.action="passAction.rptdo?step=step2";
		form2.submit();
	}
	function myOpen() {
		 // var re=window.showModalDialog("addPass.jsp",window,"dialogWidth:500px;dialogHeight:300px;scroll:no;status:no;edge:sunken");
		 //window.location.reload();
		javascript:$('#d5').dialog('open');
	}
	function myLoad(){
		javascript:$('#d5').dialog('close');
	}
</script>
	<%
			InfoOperTable loginUser = CommonFacade.getLoginUser(session);
	        List<PassInfo> passlist = ( List<PassInfo>)	session.getAttribute("activityPassList");
			String imgUrl =request.getContextPath()+"/images/validatebox_warning.gif";
			SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String selectCss = "class='easyui-combobox' style='width:180px;'";
			//获得审批人
			String personName = "select t.user_id,t.user_name  from ui_info_user t";
			ActivityInfo acInfo = (ActivityInfo)session.getAttribute("ActivityInfo");

	%>
</head>
<body onload="myLoad()">
<form action="" name="form2" id="form2" method="post">
<input type="hidden" value="<%=acInfo.getActivityId() %>" id="object_id" name="object_id">
<input type="hidden" value="activityPassList" id="optype" name="optype">
<input type="hidden" value="activity" id="doType" name="doType">
<input type="hidden" value="activity" id="state" name="state">
<input type="hidden" value="activity" id="advice" name="advice">
<%
	if(null!=acInfo){
		if(0==acInfo.getState()){
%>
 		&nbsp;此方案还没有提交  &nbsp  请选择审批人： <BIBM:TagSelectList  script="<%=selectCss%>"	listName="warn_name" listID="0"  selfSQL="<%=personName%>" />
        &nbsp;&nbsp; <button class="btn4" type="button" onClick="mySubmit()"> 提交 </button>
<%
		}else {
	%>
				<%
				if(1==acInfo.getState()&&loginUser.user_id.equals(acInfo.getDecider()) ){
						%>
				&nbsp;<font color="red">您是该方案审批流程的下一步审批人,请审批  </font><button class="btn4" type="button" onClick="myOpen()"> 我去审批 </button><br>
						<%
				}
				%>
<%
	}
}

%>
<!-- 弹出窗口 -->
<input type="hidden" value="<%=acInfo.getActivityId() %>" id="object_id" name="object_id">
<input type="hidden" value="activityPassList" id="optype" name="optype">
<input type="hidden" value="activity" id="doType" name="doType">
<input type="hidden" value="step2" id="step" name="step">

<div id="d5" class="easyui-dialog" title="对话窗口" style="width:500px;height:220px;left:250px;top:1px;padding:1px;"
			toolbar="#dlg-toolbar" buttons="#dlg-buttons" resizable="true">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
        <tr>
          <td width="20%" class="validatebox-tabTD-left"><img src="../images/validatebox_warning.gif" width="16" height="16" border="0" />审批选项：</td>
          <td width="80%" class="validatebox-tabTD-right">

          <select id="txt_state" name="txt_state"><option value="2">同意</option>
        		  <option value="-1">驳回</option>
          </select>
        </tr>
         <tr>
          <td class="validatebox-tabTD-left"><img src="../images/validatebox_warning.gif" width="16" height="16" border="0" />审批意见：</td>
          <td colspan="1" class="validatebox-tabTD-right"><textarea id="txt_advice" name="txt_advice" cols="50" rows="20"></textarea></td>
        </tr>

</table>

</div>
	<div id="dlg-buttons">
		<a href="#" class="public-btn1" onClick="mySave()"><span>保存</span></a>
		<a href="#" class="public-btn1" onClick="javascript:$('#d5').dialog('close')"><span>关闭</span></a></div>

<!-- 结束 -->

 <div class="list_content">
   <table >
     <tr width="100%">
        <th width="30%" align="center">审批流程 </th>
       <th width="10%" align="center">提交人</th>
       <th width="15%" align="center">记录时间</th>
       <th width="10%" align="center">审批人 </th>
        <th width="35%" align="center">审批意见</th>
     </tr>
<%
 	if(null!=passlist){
       		for(int i = 0 ;i<passlist.size();i++){
       			if(i%2!=0){
       				%>
       				<tr class="jg">
       				<%
       			}else{
       			%>
       			 <tr><%} %>
          <td align="left">步骤<%=i+1 %>:<%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_PASS_STEP",String.valueOf(passlist.get(i).getStep()))  %></td>
      	  <td align="center"><%=passlist.get(i).getCreator()%></td>
          <td align="center"><%=CommonFormate.dateFormate2(passlist.get(i).getCreateDate())%></td>
          <td align="center"><%=passlist.get(i).getWarnName()%></td>
          <td align="center"><%=passlist.get(i).getAdvice()%></td>
       </tr>	<%
       		}
       	}%>
     </table>
   </div>

</form>
</body>
</html>