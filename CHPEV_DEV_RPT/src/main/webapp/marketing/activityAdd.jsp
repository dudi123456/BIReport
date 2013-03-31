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
<script language="javascript">
function myTimeClick(){
	var date1 = document.getElementById("txt_date01").value;
	var date2 = document.getElementById("txt_date02").value;
	if(date2<date1){
		alert('结束时间不得小于开始时间！！！');
		document.getElementById("txt_date02").value = date1;
	}
}
	function  isTrueValue()
	{

		var msg="";
		var name = document.getElementById("txt_activityName").value;
		var code = document.getElementById("txt_activityCode").value;
		var content = document.getElementById("txt_activityContent").value;
		var tactic = document.getElementById("txt_tacticName").value;
		//var nameList = document.getElementById("txt_NameListID").value;
		var projct = document.getElementById("txt_projectName").value;
		if(name==""){
			msg="请输入活动名称;\n";
		}
		if(code==""){
			msg=msg+"请输入活动编码;\n";
		}
		if(tactic==""){
			msg=msg+"请选择活动策略;\n";
		}
		if(code==""){
			msg=msg+"请输入活动编码;\n";
		}
		if(content==""){
			msg=msg+"请输入详细信息;\n";
		}
		if(projct==""){
			msg=msg+"请选择活动方案;\n";
		}
		if(""==msg){
			return true;
		}else {
			alert(msg);
			return false;
		}


	}
	function mySave(op) {
		//先去保存客户名单的配置值
		gg.form1.action="groupAction.rptdo?optype=groupListForActivity&doType=addForActivity";
		gg.form1.submit();
		//然后提交表单
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
			tt.form1.action="htmlAction.rptdo?optype=searchTacticdialog&doType=search";
			tt.form1.submit();
		 }else if("#d3"==divName){
			pp.form1.action="projectAction.rptdo?optype=searchProjectdialog&doType=search&qry__project_state=2";
			pp.form1.submit();
		 }else if("#d4"==divName){
			//cc.form1.action="nameListAction.rptdo?doType=search&optype=searchNameListdialog";
			//cc.form1.submit();
			cc.form1.action="groupAction.rptdo?optype=searchGroupListdialog&doType=search";
			cc.form1.submit();
		 }else if("#d5"==divName){
		    var myLevel = document.getElementById("txt_activityLevel").value;
		    //alert('活动级别：' + myLevel);
			nn.form1.action="activityAction.rptdo?optype=searchActivitydialog&doType=search&qry__activity_state=2&isDialog="+myLevel;
		    nn.form1.submit();
		 }
		 javascript:$(divName).dialog('open');
	 }

	 function myLoad()
	 {
	 	 javascript:$("#d2").dialog('close');
		 javascript:$("#d3").dialog('close');
		 javascript:$("#d4").dialog('close');
	     javascript:$("#d5").dialog('close');
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
	function  myCheckGroupName()
	 {
	 	var arr  = new Array();
	    arr = cc.document.getElementsByName("checkbox");
		var checlCount = 0;
		for(var i =0;i< arr.length;i++ ){
			if(arr[i].checked){
				checlCount=checlCount+1;
			}
		}
		var ids="";
		for(var i =0;i< arr.length;i++ ){
			if(arr[i].checked){
				ids+=arr[i].value+",";
			}
		}
		//截掉最后一个逗号
		if(ids!=""){
			if(ids.charAt(ids.length-1)==','){
				ids=ids.substring(0, ids.length-1);
			}
		}
		gg.form1.action="groupAction.rptdo?optype=groupListForActivity&doType=addForActivity&addids="+ids;
		gg.form1.submit();

		javascript:$('#d4').dialog('close');
	 }
  var  activityIds="";
  var  activityNames="";
	function myCheckActivityName(){
		var arr  = new Array();
	    arr = nn.document.getElementsByName("checkbox");
		var checlCount = 0;
		for(var i =0;i< arr.length;i++ ){
			if(arr[i].checked){
				checlCount=checlCount+1;
			}
		}
		activityIds="";
		activityNames="";
		for(var i =0;i< arr.length;i++ ){
			if(arr[i].checked){
				var newarr = arr[i].value.split("@@@");
				activityIds+=newarr[0]+",";
				activityNames+=newarr[1]+",";
			}
		}
		//截掉最后一个逗号
		if(activityIds!=""){
			if(activityIds.charAt(activityIds.length-1)==','){
				activityIds=activityIds.substring(0, activityIds.length-1);
			}
		}
		if(activityNames!=""){
			if(activityNames.charAt(activityNames.length-1)==','){
				activityNames=activityNames.substring(0, activityNames.length-1);
			}
		}
		form1.document.getElementById("txt_conActivity").value=activityNames;
		form1.document.getElementById("txt_conActivityIds").value=activityIds;
		javascript:$('#d5').dialog('close');
	}
function myChange(){
	alert("选择级别后请重新选择符合条件的互斥活动！");
	form1.document.getElementById("txt_conActivity").value="";
	form1.document.getElementById("txt_conActivityIds").value="";

}
</script>
	<%

			ActivityInfo info =(ActivityInfo) session.getAttribute("ActivityInfo");
			SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String selectCss = "class='easyui-combobox' style='width:180px;'";
			String wave="";
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
			String conActivityIds = "";
			String conActivityNames = "";

			if(null!=info){
					wave=String.valueOf(info.getWave());
					dispatchCyc=String.valueOf(info.getDisCyc());
					nameListType=String.valueOf(info.getNameListType());
					if (null!=info.cont_activityIds) {
						conActivityIds=info.cont_activityIds;
					}
					if (null!=info.cont_activityNames) {
						conActivityNames=info.cont_activityNames;
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

				if(null!=info.getActivityType()){
					type=String.valueOf(info.getActivityType().getActivityTypeId());
				}

					level=String.valueOf(info.getActivityLevel());
					priority=String.valueOf(info.getPriority());
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

	    String imgUrl =request.getContextPath()+"/images/validatebox_warning.gif";
	    //活动波次
		String	waveSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_ACTIVITY_WAVE'";
		// 活动类型
		//String	activityTypeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_ACTIVITY_TYPE'";
		String	activityTypeSql = "SELECT T.ACTIVITY_TYPE_ID,T.ACTIVITY_TYPE_NAME FROM  MK_PL_ACTIVITY_TYPE T";
		//客户类型
		String activityclientSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CLIENT_TYPE'";
		//活动级别
		String activityLevelSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_ACTIVITY_LEVEL'";
		//活动优先级
		String activityPrioritySql ="select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_ACTIVITY_PRIORITY'";
		//渠道
		String channleSql = "select t.channle_id,t.channle_name from  MK_PL_CHANNLE_INFO t where state =1";
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
<input type="hidden" id="txt_conActivityIds" name="txt_conActivityIds" value="<%=conActivityIds%>">


  <div id="maincontent">
    <div class="toptag"> 您所在位置：营销策划 >>套餐管理>><em class="red">新增套餐信息</em> <span class="hbt"><a
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span><span class="hbt1">
    </span></div>
</div>
  <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
<div class="validatebox-tabTD-title">营维活动基本信息录入</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
        <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />活动名称：</td>
          <td width="21%" class="validatebox-tabTD-right"><input value="<%=name %>" class="easyui-validatebox" required="true" validType="length[1,20]" id="txt_activityName" name="txt_activityName"></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />活动类型：</td>
          <td class="validatebox-tabTD-right"> <BIBM:TagSelectList focusID="<%=type %>" script="<%=selectCss%>"	listName="txt_activityType" listID="0"  selfSQL="<%=activityTypeSql%>" /></td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />活动编码：</td>
          <td width="21%" class="validatebox-tabTD-right"><input value="<%=code %>" class="easyui-validatebox" required="true" validType="length[1,20]" id="txt_activityCode" name="txt_activityCode"></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />活动波次：</td>
          <td class="validatebox-tabTD-right">   <BIBM:TagSelectList focusID="<%=wave %>"   script="<%=selectCss%>"	listName="txt__wave" listID="0" selfSQL="<%=waveSql%>" /> </td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />活动级别：</td>
          <td width="21%" class="validatebox-tabTD-right">
          <BIBM:TagSelectList focusID="<%=level %>" script="onchange='myChange()'"	listName="txt_activityLevel" listID="0"  selfSQL="<%=activityLevelSql%>" />
          </td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />优先级：</td>
          <td class="validatebox-tabTD-right">
           <BIBM:TagSelectList focusID="<%=priority %>"  script="<%=selectCss%>"	listName="txt__activityPriority" listID="0" selfSQL="<%=activityPrioritySql%>" />
          </td>
        </tr>

        <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />互斥活动：</td>
          <td colspan="3" width="21%" class="validatebox-tabTD-right">
          <input style="width:500px" value="<%=conActivityNames%>" readonly="readonly" required="true" validType="url" id="txt_conActivity"name="txt_conActivity">
          <input type="button" class="public-btn2" value="选择" onclick="opWindow('#d5')" />
          </td>

        </tr>

        	<tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />调度运行周期：</td>
          <td width="21%" class="validatebox-tabTD-right">
               <BIBM:TagSelectList focusID="<%=dispatchCyc %>" script="<%=selectCss%>"	listName="txt__dispatch_cyc" listID="0"  selfSQL="<%=dispatchCycSql%>" />
          </td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />调度运行时间：</td>
          <td class="validatebox-tabTD-right">
          <input id="txt_date03" value="<%=date03 %>"  name="txt_date03" type="text" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})">
          </td>
        </tr>

         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />开始时间：</td>
          <td width="21%" class="validatebox-tabTD-right"><input value="<%=date01 %>" id="txt_date01" name="txt_date01" type="text" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />结束时间：</td>
          <td class="validatebox-tabTD-right"><input id="txt_date02" value="<%=date02 %>"  name="txt_date02" onblur="myTimeClick()" type="text" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
        </tr>




</table>

<div class="validatebox-tabTD-title">活动对应策略/方案</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
  <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />营销策略：</td>
          <td  class="validatebox-tabTD-right">
<input value="<%=tactic %>" readonly="readonly" required="true" validType="url" id="txt_tacticName"name="txt_tacticName"> <input type="button" class="public-btn2" value="选择" onclick="opWindow('#d2')" />
          </td>
          <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />营销方案：</td>
          <td class="validatebox-tabTD-right">
         <input  value="<%=project %>"  required="true" validType="url" id="txt_projectName"name="txt_projectName" readonly="readonly"> <input  type="button" class="public-btn2" value="选择" onclick="opWindow('#d3')" />
          </td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />客户类型：</td>
          <td width="21%" class="validatebox-tabTD-right">
        <BIBM:TagSelectList focusID="<%=lcient %>"  script="<%=selectCss%>"	listName="txt__client_type" listID="0"  selfSQL="<%=activityclientSql%>" /></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />活动渠道：</td>
          <td class="validatebox-tabTD-right">   <BIBM:TagSelectList focusID="<%=channle %>"   script="<%=selectCss%>"	listName="txt__channle_id" listID="0" selfSQL="<%=channleSql%>" /> </td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />客户名单列表：</td>
          <td colspan="3" class="validatebox-tabTD-right">
<div id="allGruopList" style="display: none">
          <iframe id="gg" scrolling="0"width="100%" height="100px"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="groupListForActivity.jsp"></iframe><br>
    <input type="button" class="public-btn2" value="选择客户群(可以选择多个客户群)" onclick="opWindow('#d4')" />
     <input type="button" class="public-btn2" value="隐藏客户群列表" onclick="javascript:document.getElementById('allGruopList').style.display='none';document.getElementById('allGruopList2').style.display='block'" />
</div>
<div id="allGruopList2" style="display: block">
 <input type="button"  class="public-btn2" value="显示客户群列表" onclick="javascript:document.getElementById('allGruopList').style.display='block';document.getElementById('allGruopList2').style.display='none'" />
  </div>        </td>
        </tr>
</table>
<div class="validatebox-tabTD-title">活动描述及附件信息</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
 <tr>
        </tr>
          <tr>
          <td  width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />详细描述：</td>
          <td colspan="3" class="validatebox-tabTD-right"><textarea  required="true" style="height:80px;width:800px;t" id="txt_activityContent" name="txt_activityContent"><%=content %></textarea>
          </td>
        </tr>
        <tr>
         <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />活动附件：</td>
          <td colspan="3" class="validatebox-tabTD-right">
<iframe id="ff" scrolling="0"width="100%" height="200px"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="fileUpload.jsp"></iframe><br>

          </td>
        </tr>
</table>
<div class="buttonArea">
    <button class="btn4" type="button" onClick="mySave('save')"> 保 存 </button>
    <button class="btn4" type="button" onClick="mySubmit('search')"> 取 消 </button>
</div>

<!-- 策略弹出模式窗口 -->
<div id="d2" class="easyui-dialog"  title="营销策略选择窗口" style="width:800px;height:400px;left:0px;top:0px;padding:4px;"
			toolbar="#dlg-toolbar" buttons="#dlg-buttons" resizable="true">
<iframe id="tt" scrolling="0"width="100%" height="100%"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="searchTacticdialog.jsp"></iframe><br>
</div>
	<div id="dlg-buttons">
		<a href="#" class="public-btn1" onClick="javascript:myCheck('d2')"><span>选择</span></a>
		<a href="#" class="public-btn1" onClick="javascript:$('#d2').dialog('close')"><span>关闭</span></a></div>
<!-- 策略模式是窗口结束 -->

<!-- 方案弹出模式窗口 -->
<div id="d3" class="easyui-dialog"  title="营销方案选择窗口" style="width:800px;height:400px;left:0px;top:0px;padding:4px;"
			toolbar="#dlg-toolbar" buttons="#dlg-buttons" resizable="true">
<iframe id="pp" scrolling="0"width="100%" height="100%"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="searchProjectdialog.jsp"></iframe><br>
</div>
	<div id="dlg-buttons">
		<a href="#" class="public-btn1" onClick="javascript:myCheck('d3')"><span>选择</span></a>
		<a href="#" class="public-btn1" onClick="javascript:$('#d3').dialog('close')"><span>关闭</span></a></div>
<!-- 方案模式是窗口结束 -->

<!-- 客户名单弹出模式窗口 -->
<div id="d4" class="easyui-dialog"  title="客户名单选择窗口" style="width:800px;height:400px;left:0px;top:0px;padding:4px;"
			toolbar="#dlg-toolbar" buttons="#dlg-buttons" resizable="true">
<iframe id="cc" scrolling="0"width="100%" height="100%"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="searchNameListdialog.jsp"></iframe><br>
</div>
	<div id="dlg-buttons">
		<a href="#" class="public-btn1" onClick="javascript:myCheckGroupName()"><span>选择</span></a>
		<a href="#" class="public-btn1" onClick="javascript:$('#d4').dialog('close')"><span>关闭</span></a></div>
<!-- 客户名单模式是窗口结束 -->
<!-- 互斥活动模式窗口 -->
<div id="d5" class="easyui-dialog"  title="互斥活动选择窗口" style="width:800px;height:400px;left:0px;top:0px;padding:4px;"
			toolbar="#dlg-toolbar" buttons="#dlg-buttons" resizable="true">
<iframe id="nn" scrolling="0"width="100%" height="100%"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="searchActivitydialog.jsp"></iframe><br>
</div>
	<div id="dlg-buttons">
		<a href="#" class="public-btn1" onClick="javascript:myCheckActivityName()"><span>选择</span></a>
		<a href="#" class="public-btn1" onClick="javascript:$('#d5').dialog('close')"><span>关闭</span></a></div>
<!-- 客户名单模式是窗口结束 -->
</form>
</body>
</html>
