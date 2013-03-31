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
		var nameText = document.getElementById("txt_projectName").value;
		var tacticText = document.getElementById("txt_tacticName").value;
		var date1Text = document.getElementById("txt_date01").value;
		var date2Text = document.getElementById("txt_date02").value;
		var contentText = document.getElementById("txt_projectContent").value;

			if(null==nameText||""==nameText){
				alert("请填写方案名称");
				return false;
			}else if(null==tacticText||""==tacticText){
				alert("请选择对应的营销策略");
				return false;
			}else if(null==date1Text||""==date1Text){
				alert("请选择生效时间");
				return false;
			}else if(null==date2Text||""==date2Text){
				alert("请选择失效时间");
				return false;
			}
			else if(null==contentText||""==contentText){
				alert("请输入方案描述");
				return false;
			}else {
				return true;
			}
		}

	function mySave(op) {
		if(isTrueValue()){
			form1.action="projectAction.rptdo?optype=projectList&doType="+op;
			form1.submit();
		}

	}
	function mySubmit(op) {
		form1.action="projectAction.rptdo?optype=projectList&doType="+op;
		form1.submit();
	}
	 function opWindow()
	 {


		 var url = "htmlAction.rptdo?doType=searchTactic&optype=searchTacticdialog";
		 	tt.form1.action=url;
		 	tt.form1.submit();
		 javascript:$('#d2').dialog('open');
		 //window.showModelessDialog("marketing/searchTacticdialog.jsp","","dialogWidth:800px;dialogHeight:300px;scroll:no;status:no;edge:sunken")
	 }

	 function myLoad()
	 {
	 	javascript:$('#d2').dialog('close');
	 }

	 var tacticIdName;
	 var saveTacticId;
	 var showTacticNam;
	function  myCheck()
	 {
			var arr = tt.document.getElementsByName("checkbox");
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
				arr = tt.document.getElementsByName("checkbox");
				for(var i =0;i< arr.length;i++ ){
					if(arr[i].checked){
						tacticIdName=arr[i].value;
					}
				}
				var arr = tacticIdName.split("@@@");
				saveTacticId = arr[0];
				showTacticNam = arr[1];
				form1.document.getElementById("txt_tacticName").value=showTacticNam;
				form1.document.getElementById("txt_tacticId").value=saveTacticId;
				javascript:$('#d2').dialog('close');
			}
	 }

	function myTimeClick(){
		var date1 = document.getElementById("txt_date01").value;
		var date2 = document.getElementById("txt_date02").value;
		if(date2<date1){
			alert('结束时间不得小于开始时间！！！');
			document.getElementById("txt_date02").value = date1;
		}
	}

	 function toDate(str){
		    var sd=str.split("-");
		    return new Date(sd[0],sd[1],sd[2]);
		}

	 /**--------------------------------------------联动效果----------------------------------------------------*/

	 /**--------------------------------------------联动效果结束----------------------------------------------------*/
	</script>
	<%
			String imgUrl =request.getContextPath()+"/images/validatebox_warning.gif";
			SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String selectCss = "class='easyui-combobox' style='width:180px;'";
			//获得方案类型
			String projectTypeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_PROJECT_TYPE'";
			//获得方案优先级
			String projectPrioritySql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_PROJECT_PRIORITY'";
			//获得方案级别
			String projectLevelSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_PROJECT_LEVEL'";
			//获得渠道
			String channleSql ="select t.channle_id,t.channle_name from  MK_PL_CHANNLE_INFO t where state =1";
			//获得类型下拉框数据
			String tactictypeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_TACTIC_TYPE'";



			ProjectInfo pinfo = (ProjectInfo)session.getAttribute("ProjectInfo");
			String name="";
			String type="";
			String level="";
			String priority="";
			String channle="";
			String tactic="";
			String content="";
			String date01=sdf.format(date);
			String date02=sdf.format(date);
			if(null!=pinfo){
				if (null!=pinfo.getEffectDate()) {
					date01=sdf.format(pinfo.getEffectDate());
				}
				if (null!=pinfo.getInvaildDate()) {
					date02=sdf.format(pinfo.getInvaildDate());
				}
				if (!StringTool.checkEmptyString(pinfo.getProjectContent())) {
					content=pinfo.getProjectContent();
				}
				if (!StringTool.checkEmptyString(pinfo.getProjectName())) {
					name=pinfo.getProjectName();
				}
					type=String.valueOf(pinfo.getProjectType());
					level=String.valueOf(pinfo.getProjectLevel());
					priority=String.valueOf(pinfo.getPriority());
				ChannleInfo CInfo = pinfo.getChannleInfo();
				if (null!=CInfo) {

					channle=String.valueOf(CInfo.getChannleId());
				}
				TacticInfo TInfo = pinfo.getTacticInfo();
				if (null!=TInfo) {
					tactic = TInfo.getTacticName();
				}
			}
	%>
</head>
<body style="background-color:#f9f9f9" onload="myLoad()">
<form action="" method="post" name="form1">
<input type="hidden" id="txt_tacticId" name="txt_tacticId" >
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
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />方案名称：</td>
          <td width="21%" class="validatebox-tabTD-right"><input value="<%=name %>" class="easyui-validatebox" required="true" validType="length[1,20]" id="txt_projectName" name="txt_projectName"></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />方案类型：</td>
          <td class="validatebox-tabTD-right"> <BIBM:TagSelectList focusID="<%=type %>" script="<%=selectCss%>"	listName="txt__project_type" listID="0"  selfSQL="<%=projectTypeSql%>" /></td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />方案级别：</td>
          <td width="21%" class="validatebox-tabTD-right">
          <BIBM:TagSelectList focusID="<%=level %>" script="<%=selectCss%>"	listName="txt__project_level" listID="0"  selfSQL="<%=projectLevelSql%>" />
          </td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />优先级：</td>
          <td class="validatebox-tabTD-right">
           <BIBM:TagSelectList focusID="<%=priority %>"  script="<%=selectCss%>"	listName="txt__project_Priority" listID="0" selfSQL="<%=projectPrioritySql%>" />
          </td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />渠道ID：</td>
          <td width="21%" class="validatebox-tabTD-right">
          <BIBM:TagSelectList focusID="<%=channle %>"  script="<%=selectCss%>"	listName="txt__channle" listID="0"  selfSQL="<%=channleSql%>" />
          </td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />营销策略：</td>
          <td class="validatebox-tabTD-right"><input value="<%=tactic %>"  required="true" validType="url" id="txt_tacticName"name="txt_tacticName"> <input type="button" class="public-btn2" value="选择" onclick="opWindow()" /></td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />开始时间：</td>
          <td width="21%" class="validatebox-tabTD-right"><input value="<%=date01 %>" id="txt_date01" name="txt_date01" type="text" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />结束时间：</td>
          <td class="validatebox-tabTD-right"><input id="txt_date02" value="<%=date02 %>"  name="txt_date02" type="text" class="Wdate" onblur="myTimeClick()"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
        </tr>
         <tr>
          <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />详细描述：</td>
          <td colspan="3" class="validatebox-tabTD-right"><textarea  class="easyui-validatebox" required="true" style="height:100px;width: 400px" id="txt_projectContent" name="txt_projectContent"><%=content %></textarea>
          </td>
        </tr>
         <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />方案附件：</td>
          <td colspan="3" class="validatebox-tabTD-right">
<iframe id="ff" scrolling="0"width="100%" height="200px"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="fileUpload.jsp"></iframe><br>

          </td>
        </tr>
</table>

<div class="buttonArea">
    <button class="btn4" type="button" onClick="mySave('save')"> 保 存 </button>
    <button class="btn4" type="button" onClick="mySubmit('search')"> 取 消 </button>
</div>

<!-- 弹出模式窗口 -->
<div id="d2" class="easyui-dialog"  title="对话窗口" style="width:800px;height:300px;left:0px;top:0px;padding:4px;"
			toolbar="#dlg-toolbar" buttons="#dlg-buttons" resizable="true">
        <iframe id="tt" scrolling="0"width="100%" height="100%"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="searchTacticdialog.jsp"></iframe><br>
</div>
	<div id="dlg-buttons">
		<a href="#" class="public-btn1" onClick="javascript:myCheck()"><span>选择</span></a>
		<a href="#" class="public-btn1" onClick="javascript:$('#d2').dialog('close')"><span>关闭</span></a></div>
<!-- 模式是窗口结束 -->
</form>
</body>
</html>
