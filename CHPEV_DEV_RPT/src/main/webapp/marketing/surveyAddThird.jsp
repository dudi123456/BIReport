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
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
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
	function  isTrueValue()
	{
		return true;

	}
	function mySave(op) {
		if(isTrueValue()){
			form1.action="activityAction.rptdo?optype=activityList&doType="+op;
			form1.submit();
		}

	}
	function mySubmit(op) {
		form1.action="activityAction.rptdo?optype=activityList&doType="+op;
		form1.submit();
	}
	 function opWindow(divName)
	 {
		 if("#d2"==divName){
			tt.form1.action="htmlAction.rptdo?optype=searchTacticdialog&doType=";
			tt.form1.submit();
		 }else if("#d3"==divName){
			pp.form1.action="projectAction.rptdo?optype=searchProjectdialog&doType=search";
			pp.form1.submit();
		 }else if("#d4"==divName){
			var client_type = document.getElementById("txt__client_type").value;
			var nameList_type = document.getElementById("txt__nameListType").value;
			cc.form1.action="nameListAction.rptdo?doType=search&optype=searchNameListdialog&client_type="+client_type+"&nameList_type="+nameList_type;
			cc.form1.submit();
		 }
		 javascript:$(divName).dialog('open');
		 //window.showModelessDialog("marketing/searchTacticdialog.jsp","","dialogWidth:800px;dialogHeight:300px;scroll:no;status:no;edge:sunken")
	 }

	 function myLoad()
	 {
	 	 javascript:$("#d2").dialog('close');
		 javascript:$("#d3").dialog('close');
		 javascript:$("#d4").dialog('close');

	 }

	 var tacticIdName;
	 var saveTacticId;
	 var showTacticNam;
	function  myCheck(oo)
	 {
		var arr  = new Array();
			if("d2"==oo){
				arr = tt.document.getElementsByName("checkbox");
			}else if("d3"==oo){
				arr = pp.document.getElementsByName("checkbox");
			}else if("d4"==oo){
				arr = cc.document.getElementsByName("checkbox");
			}
			var checlCount = 0;
			for(var i =0;i< arr.length;i++ ){
				if(arr[i].checked){
					checlCount=checlCount+1;
				}
			}
			if(checlCount==0){
				alert("你没有选择任何的信息！");
			}else if(checlCount>1)	{
				alert("每次只能选择一条信息！");
			}else{
				for(var i =0;i< arr.length;i++ ){
					if(arr[i].checked){
						tacticIdName=arr[i].value;
					}
				}
				var arr = tacticIdName.split("@@@");
				saveTacticId = arr[0];
				showTacticNam = arr[1];
				if("d2"==oo){
					form1.document.getElementById("txt_tacticName").value=showTacticNam;
					form1.document.getElementById("txt_tacticId").value=saveTacticId;
					javascript:$('#d2').dialog('close');
				}else if("d3"==oo){
					form1.document.getElementById("txt_projectName").value=showTacticNam;
					form1.document.getElementById("txt_projectID").value=saveTacticId;
					javascript:$('#d3').dialog('close');
				}else if("d4"==oo){
					form1.document.getElementById("txt_NameListID").value=showTacticNam;
					form1.document.getElementById("hid_nameListID").value=saveTacticId;
					javascript:$('#d4').dialog('close');
				}


			}
	 }
</script>
	<%
			ActivityInfo info =(ActivityInfo) session.getAttribute("ActivityInfo");
			SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String selectCss = "class='easyui-combobox' style='width:180px;'";

			String name="";
			String type="";
			String code="";
			String channle="";
			String level="";
			String priority="";
			String lcient ="";
			String tactic="";
			String nameList="";
			String nameListType="";
			String project ="";
			String content="";
			String date01=sdf.format(date);
			String date02=sdf.format(date);
			String date03=sdf.format(date);
			String dispatchCyc="";

			int tacticID =0;
			int projectID =0;
			int nameListID = 0;

			if(null!=info){
				if (info.getDisCyc()!=-999) {
					dispatchCyc=String.valueOf(info.getDisCyc());
				}
				if (info.getNameListType()!=-999) {
					nameListType=String.valueOf(info.getNameListType());
				}
				if (null!=info.getStartData()) {
					date01=sdf.format(info.getStartData());
				}
				if (null!=info.getEndDate()) {
					date02=sdf.format(info.getEndDate());
				}
				if (null!=info.getDisDat()) {
					date03=sdf.format(info.getDisDat());
				}
				if (!StringTool.checkEmptyString(info.getContent())) {
					content=info.getContent();
				}
				if (!StringTool.checkEmptyString(info.getActivityName())) {
					name=info.getActivityName();
				}
				if (!StringTool.checkEmptyString(info.getActivityCode())) {
					code=info.getActivityCode();
				}
				if (info.getActivityType()!=-999) {
					type=String.valueOf(info.getActivityType());
				}
				if (info.getActivityLevel()!=-999) {
					level=String.valueOf(info.getActivityLevel());
				}
				if (info.getPriority()!=-999) {
					priority=String.valueOf(info.getPriority());
				}
				ChannleInfo CInfo = info.getChannleInfo();
				if (null!=CInfo) {
					channle=String.valueOf(CInfo.getChannleId());
				}
				TacticInfo TInfo = info.getTacticInfo();
				if (null!=TInfo) {
					tactic = TInfo.getTacticName();
					tacticID = TInfo.getTacticId();
				}
				ProjectInfo PInfo = info.getProjectInfo();
				if (null!=PInfo) {
					project = PInfo.getProjectName();
					projectID = PInfo.getProjectId();
				}
				NameListInfo NInfo = info.getNameListId();
				if (null!=NInfo) {
					nameList = NInfo.getNameListName();
					nameListID = NInfo.getNameListId();
				}
			}


		// 活动类型
		String	activityTypeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_ACTIVITY_TYPE'";
		//客户类型
		String activityclientSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CLIENT_TYPE'";
		//活动级别
		String activityLevelSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_ACTIVITY_LEVEL'";
		//活动优先级
		String activityPrioritySql ="select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_ACTIVITY_PRIORITY'";
		//渠道
		String channleSql = "select t.channle_id,t.channle_name from  MK_PL_CHANNLE_INFO t";
		//客户名单类型
		String nameListTypeSql ="select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_NAMELIST_TYPE'";
		String nameListIDSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_TACTIC_TYPE'";
		String dispatchCycSql= "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_ACTIVITY_DISPATCH_CYC'";
	%>
</head>

<body style="background-color:#f9f9f9" onload="myLoad()">
<form action="" method="post" name="form1">
<input type="hidden" id="txt_tacticId" name="txt_tacticId" value="<%=tacticID%>" >
<input type="hidden" id="txt_projectID" name="txt_projectID"  value="<%=projectID%>">
<input type="hidden" id="hid_nameListID" name="hid_nameListID"  value="<%=nameListID%>">
  <div id="maincontent">
    <div class="toptag"> 您所在位置：营销策划 >>套餐管理>><em class="red">新增套餐信息</em> <span class="hbt"><a href="javascript:;"
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span><span class="hbt1">
    </span></div>

  <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
<div class="validatebox-tabTD-title">营维方案信息录入</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
        <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />活动名称：</td>
          <td width="21%" class="validatebox-tabTD-right"><input value="<%=name %>" class="easyui-validatebox" required="true" validType="length[1,20]" id="txt_activityName" name="txt_activityName"></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />活动类型：</td>
          <td class="validatebox-tabTD-right"> <BIBM:TagSelectList focusID="<%=type %>" script="<%=selectCss%>"	listName="txt_activityType" listID="0"  selfSQL="<%=activityTypeSql%>" /></td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />活动编码：</td>
          <td width="21%" class="validatebox-tabTD-right"><input value="<%=code %>" class="easyui-validatebox" required="true" validType="length[1,20]" id="txt_activityCode" name="txt_activityCode"></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />活动渠道：</td>
          <td class="validatebox-tabTD-right">   <BIBM:TagSelectList focusID="<%=channle %>"   script="<%=selectCss%>"	listName="txt__channle_id" listID="0" selfSQL="<%=channleSql%>" /> </td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />活动级别：</td>
          <td width="21%" class="validatebox-tabTD-right">
          <BIBM:TagSelectList focusID="<%=level %>" script="<%=selectCss%>"	listName="txt_activityLevel" listID="0"  selfSQL="<%=activityLevelSql%>" />
          </td>
          <td width="12%" class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />优先级：</td>
          <td class="validatebox-tabTD-right">
           <BIBM:TagSelectList focusID="<%=priority %>"  script="<%=selectCss%>"	listName="txt__activityPriority" listID="0" selfSQL="<%=activityPrioritySql%>" />
          </td>
        </tr>
          <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />客户类型：</td>
          <td width="21%" class="validatebox-tabTD-right">
<BIBM:TagSelectList focusID="<%=lcient %>"  script="<%=selectCss%>"	listName="txt__client_type" listID="0"  selfSQL="<%=activityclientSql%>" />
          </td>
          <td width="12%" class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />营销策略：</td>
          <td class="validatebox-tabTD-right">
<input value="<%=tactic %>"  required="true" validType="url" id="txt_tacticName"name="txt_tacticName"> <input type="button" class="public-btn2" value="选择" onclick="opWindow('#d2')" />
          </td>
        </tr>
        <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />客户名单类型：</td>
          <td width="21%" class="validatebox-tabTD-right">
          <BIBM:TagSelectList focusID="<%=nameListType %>" script="<%=selectCss%>"	listName="txt__nameListType" listID="0"  selfSQL="<%=nameListTypeSql%>" />
          </td>
          <td width="12%" class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />客户名单列表：</td>
          <td class="validatebox-tabTD-right">
          <input value="<%=nameList %>"  required="true" validType="url" id="txt_NameListID"name="txt_NameListID"> <input type="button" class="public-btn2" value="选择" onclick="opWindow('#d4')" />
          </td>
        </tr>
          <tr>
          <td class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />营销方案：</td>
          <td colspan="3" class="validatebox-tabTD-right">
         <input  style="width: 508px" value="<%=project %>"  required="true" validType="url" id="txt_projectName"name="txt_projectName"> <input  type="button" class="public-btn2" value="选择" onclick="opWindow('#d3')" />
          </td>
        </tr>

         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />开始时间：</td>
          <td width="21%" class="validatebox-tabTD-right"><input value="<%=date01 %>" id="txt_date01" name="txt_date01" type="text" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />结束时间：</td>
          <td class="validatebox-tabTD-right"><input id="txt_date02" value="<%=date02 %>"  name="txt_date02" type="text" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
        </tr>



        	<tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />调度运行周期：</td>
          <td width="21%" class="validatebox-tabTD-right">
               <BIBM:TagSelectList focusID="<%=dispatchCyc %>" script="class='easyui-combobox'"	listName="txt__dispatch_cyc" listID="0"  selfSQL="<%=dispatchCycSql%>" />
          </td>
          <td width="12%" class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />调度运行时间：</td>
          <td class="validatebox-tabTD-right">
          <input id="txt_date03" value="<%=date03 %>"  name="txt_date03" type="text" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})">
          </td>
        </tr>
         <tr>
          <td class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />详细描述：</td>
          <td colspan="3" class="validatebox-tabTD-right"><textarea  required="true" style="height:80px;width:800px;t" id="txt_activityContent" name="txt_activityContent"><%=content %></textarea>
          </td>
        </tr>
         <td class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />方案附件：</td>
          <td colspan="3" class="validatebox-tabTD-right">
<iframe id="ff" scrolling="0"width="100%" height="200px"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="marketing/fileUpload.jsp"></iframe><br>

          </td>
        </tr>
</table>
<div class="buttonArea">
    <button class="btn4" type="button" onClick="mySave('save')"> 保 存 </button>
    <button class="btn4" type="button" onClick="mySubmit('search')"> 取 消 </button>
</div>

<!-- 策略弹出模式窗口 -->
<div id="d2" class="easyui-dialog"  title="营销策略选择窗口" style="width:800px;height:300px;left:0px;top:0px;padding:4px;"
			toolbar="#dlg-toolbar" buttons="#dlg-buttons" resizable="true">
<iframe id="tt" scrolling="0"width="100%" height="100%"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="marketing/searchTacticdialog.jsp"></iframe><br>
</div>
	<div id="dlg-buttons">
		<a href="#" class="public-btn1" onClick="javascript:myCheck('d2')"><span>选择</span></a>
		<a href="#" class="public-btn1" onClick="javascript:$('#d2').dialog('close')"><span>关闭</span></a></div>
<!-- 策略模式是窗口结束 -->

<!-- 方案弹出模式窗口 -->
<div id="d3" class="easyui-dialog"  title="营销方案选择窗口" style="width:800px;height:300px;left:0px;top:0px;padding:4px;"
			toolbar="#dlg-toolbar" buttons="#dlg-buttons" resizable="true">
<iframe id="pp" scrolling="0"width="100%" height="100%"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="marketing/searchProjectdialog.jsp"></iframe><br>
</div>
	<div id="dlg-buttons">
		<a href="#" class="public-btn1" onClick="javascript:myCheck('d3')"><span>选择</span></a>
		<a href="#" class="public-btn1" onClick="javascript:$('#d3').dialog('close')"><span>关闭</span></a></div>
<!-- 方案模式是窗口结束 -->

<!-- 客户名单弹出模式窗口 -->
<div id="d4" class="easyui-dialog"  title="客户名单选择窗口" style="width:800px;height:300px;left:0px;top:0px;padding:4px;"
			toolbar="#dlg-toolbar" buttons="#dlg-buttons" resizable="true">
<iframe id="cc" scrolling="0"width="100%" height="100%"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="marketing/searchNameListdialog.jsp"></iframe><br>
</div>
	<div id="dlg-buttons">
		<a href="#" class="public-btn1" onClick="javascript:myCheck('d4')"><span>选择</span></a>
		<a href="#" class="public-btn1" onClick="javascript:$('#d4').dialog('close')"><span>关闭</span></a></div>
<!-- 客户名单模式是窗口结束 -->
</form>
</body>
</html>
