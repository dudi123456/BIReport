<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.TacticInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
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
<script language="javascript" src="<%=request.getContextPath()%>/js/patch.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/date/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/icon.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/icontent.css">
<script language="javascript">
	function myTimeClick(){
		var date1 = document.getElementById("date01").value;
		var date2 = document.getElementById("date02").value;
		if(date2<date1){
			alert('结束时间不得小于开始时间！！！');
			document.getElementById("date02").value = date1;
		}
	}
	function myNext(op) {
		var name = document.getElementById("txt_tacticName").value;
		var content = document.getElementById("txt_content").value;
		if(null==name||""==name){
			alert('请输入策略名称...');
			return false;
		}else if(null==content||""==content){
			alert('请输入详细描述...');
			return false;
		}else {
			form1.action="tacticAction.rptdo?optype=tacticAddStep2&doType=add&step=step1";
			form1.submit();
		}


	}
	function mySubmit(op) {
		form1.action="targetAction.rptdo?optype=targetList&doType="+op;
		form1.submit();
	}
	function cancel() {
		form1.action="tacticAction.rptdo?optype=tacticList&doType=search";
		form1.submit();
	}
</script>
	<%
	String typeSql =  "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_TACTIC_TYPE'";
	String timeSql =  "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_TACTIC_TIME'";
	SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
	Date date = new Date();
	String imgUrl =request.getContextPath()+"/images/validatebox_warning.gif";
	TacticInfo tacticInfo = (TacticInfo) session.getAttribute("TacticInfo");
		String name="";
		String type="";
		String date01=sdf.format(date);
		String date02=sdf.format(date);
		String leashCyc="";
		String content="";
		if(null!=tacticInfo){
			if (!StringTool.checkEmptyString(tacticInfo.getTacticName())) {
				name=tacticInfo.getTacticName();
			}
			if (!StringTool.checkEmptyString(tacticInfo.getContent())) {
				content=tacticInfo.getContent();
			}
			if (!StringTool.checkEmptyString(tacticInfo.getContent())) {
				content=tacticInfo.getContent();
			}
			if (null!=tacticInfo.getStartDate()) {
				date01=sdf.format(tacticInfo.getStartDate());
			}
			if (null!=tacticInfo.getEndDate()) {
				date02=sdf.format(tacticInfo.getEndDate());
			}
		 	type=String.valueOf(tacticInfo.getTacticType());
		 	leashCyc=String.valueOf(tacticInfo.getLeashCyc());
		}
		//获得类型下拉框数据

	%>
</head>
<body style="background-color:#f9f9f9">
<form name="form1" method="post" action="" >
  <div id="maincontent">
    <div class="toptag"> 您所在位置：营销策划 >>套餐管理>><em class="red">新增套餐信息</em> <span class="hbt"><a href="javascript:;"
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span><span class="hbt1">

    </span></div>

  <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
<div class="validatebox-tabTD-title">营销策略信息维护:当前是第一步(基本信息)/共四步</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
        <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />策略名称：</td>
          <td width="21%" class="validatebox-tabTD-right"><input value="<%=name %>" class="easyui-validatebox" required="true"  id="txt_tacticName" name="txt_tacticName"></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />策略类型：</td>
          <td class="validatebox-tabTD-right"> <BIBM:TagSelectList focusID="<%=type %>"  script="class='easyui-combobox'"	listName="txt_tactic_type" listID="0" selfSQL="<%=typeSql%>" /></td>
        </tr>

         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />开始时间：</td>
          <td width="21%" class="validatebox-tabTD-right"><input  value="<%=date01 %>" id="date01" name="date01" type="text" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />结束时间：</td>
          <td class="validatebox-tabTD-right"><input  value="<%=date01 %>" onblur="myTimeClick()" id="date02" name="date02" type="text" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
        </tr>
        <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />接触周期：</td>
          <td colspan="3" class="validatebox-tabTD-right"> <BIBM:TagSelectList   script="class='easyui-combobox'"	listName="txt_tactic_dispatch" listID="0" selfSQL="<%=timeSql%>" /></td>
        </tr>
         <tr>
          <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="20 height="16" border="0" />详细描述：</td>
          <td colspan="3" class="validatebox-tabTD-right"><textarea name="txt_content" id="txt_content" class="easyui-validatebox" required="true" style="height:80px;"><%=content %></textarea></td>
        </tr>
</table>
<div class="buttonArea">
    <button class="btn4" type="button" onClick="myNext()"> 下一步 </button>
    <button class="btn4" type="button" onClick="cancel()"> 取 消 </button>
    <br>
</div>
</form>
</body>
</html>
