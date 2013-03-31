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
<%@page import="com.ailk.bi.marketing.entity.ContactPlyInfo"%>
<%@page import="com.ailk.bi.marketing.entity.TargetOpInfo"%>


<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="com.ailk.bi.common.sysconfig.GetSystemConfig"%>
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
		    String fileUrl =request.getContextPath()+GetSystemConfig.getBIBMConfig().getUploadFolder();
	        TacticInfo tacticInfo = (TacticInfo) session.getAttribute("TacticInfo");//策略基本信息
	        List<TargetOpInfo> list1  =	(List<TargetOpInfo>)session.getAttribute("TargetOpInfoLis1");//营销指标配置
	        List<ContactPlyInfo> list2 =(List<ContactPlyInfo>) session.getAttribute("ContactPlyInfoList");//接触规则配置
	        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
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

	    String imgUrl =request.getContextPath()+"/images/validatebox_warning.gif";
	    String selectCss = "class='easyui-combobox' style='width:180px;'";
	%>
<script language="javascript">
	function isTrueValue(){
		var tt = document.getElementById("txt_modeContent").value;
		if(tt==""){
			alert("请输入详细信息！");
			return false;
		}return true;
	}
	function mySave() {
		if(isTrueValue()){
			form1.action="activityModeAction.rptdo?optype=activityList&doType=save";
			form1.submit();
		}
	}

function showMode(op){
}
function showClose(){
	//form1.action="activityAction.rptdo?optype=activityPass&doType=modify&activityId=";
	//form1.submit();
}
function openFile(fileUrl){
	window.open(fileUrl);
}

function opProjectWindow(){
	//var url = "projectAction.rptdo?optype=projectPass&doType=modify&projectId=";
	//window.open(url,'newwindow','height=600,width=1000,top=60,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no, status=no') ;
	//window.open(url, 'newwindow ', 'height=100, width=400, top=0, left=0, toolbar=no, menubar=no, scrollbars=no');
	//window.open(url,   'height=100,   width=400,   top=0,   left=0,   toolbar=no,   menubar=no,   scrollbars=no,   resizable=no,location=no,   status=no ');
}
</script>

</head>
<body style="background-color:#f9f9f9">
<form action="" method="post" name="form1">
<input type="hidden" id="txt_tacticId" name="txt_tacticId" value="" >


  <div id="maincontent">
    <div class="toptag"> 您所在位置：营销策划 >>套餐管理>><em class="red">活动详细信息</em> <span class="hbt"><a href="javascript:;"
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span><span class="hbt1">
    </span></div>

  <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
<div class="validatebox-tabTD-title">策略基本信息</div>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
        <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />策略名称：</td>
          <td width="21%" class="validatebox-tabTD-right"><%=name %></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />策略类型：</td>
          <td class="validatebox-tabTD-right">
          <%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_TACTIC_TYPE",type)  %>
          </td>
        </tr>

         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />开始时间：</td>
          <td width="21%" class="validatebox-tabTD-right">    <%=date01 %></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />结束时间：</td>
          <td class="validatebox-tabTD-right">    <%=date02 %></td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />接触周期：</td>
          <td colspan="3" class="validatebox-tabTD-right">
          <%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_TACTIC_TIME",leashCyc) %>
          </td>
        </tr>
         <tr>
          <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="20 height="16" border="0" />详细描述：</td>
          <td colspan="3" class="validatebox-tabTD-right"><%=content %></td>
        </tr>
</table>
<!-- 营销指标 -->
<div class="validatebox-tabTD-title">营销指标列表</div>
<div class="list_content">
<table >
            <tr width="100%">
              <th width="10%" align="center"> 指标编号</th>
              <th width="20%" align="center"> 指标类型</th>
              <th width="20%" align="center"> 指标名称 </th>
              <th width="20%" align="center"> 操作符 </th>
              <th width="20%" align="center"> 指标值 </th>
              <th width="10%" align="center"> 单位 </th>
              </tr>
               <% if(null!=list1){
        		for(int i = 0 ;i<list1.size();i++){
        			if(i%2!=0){
        				%>
        				<tr class="jg">
        				<%
        			}else{
        			%>
        			 <tr><%} %>
          <td align="center">指标<%=i+1 %>:</td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_TARGET_TYPE",String.valueOf(list1.get(i).getTargetInfo().getTargetType()))  %></td>
          <td align="center"><%=list1.get(i).getTargetInfo().getTargetName() %></td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_OP_TYPE",String.valueOf(list1.get(i).getOpType()))  %></td>
          <td align="center"><%=list1.get(i).getOpValue() %></td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_UNIT_TYPE",String.valueOf(list1.get(i).getTargetInfo().getUnit()))  %></td>
        </tr>
        			<%
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
<!-- 营销指标结束 -->
<!-- 接触规则开始-->
<div class="validatebox-tabTD-title">接触规则列表</div>
<div class="list_content">
 <table >
            <tr width="100%">
              <th width="10%" align="center"> 编         号</th>
              <th width="20%" align="center"> 接触步骤</th>
              <th width="20%" align="center"> 建议接触时间 </th>
              <th width="20%" align="center"> 接触天数限制(天) </th>
              <th width="20%" align="center"> 运营脚本ID </th>
              <th width="10%" align="center"> 接触方式 </th>
              </tr>
 <% if(null!=list2){
        		for(int i = 0 ;i<list2.size();i++){
        			if(i%2!=0){
        				%>
        				<tr class="jg">
        				<%
        			}else{
        			%>
        			 <tr><%} %>
           <td align="center">接触规则<%=i+1 %>:</td>
           <td align="center">第<%=i+1 %>步</td>
           <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_CONTAC_TIME",String.valueOf(list2.get(i).getContactTime()))  %></td>
           <td align="center"><%=String.valueOf(list2.get(i).getContactDay()) %></td>
           <td align="center"> <input type="hidden" name="hiddenId" id="hiddenId<%=i%>" value="<%=list2.get(i).getScriptId() %>">ID:<div style="display:inline;" id="scriptId<%=i %>"><%=list2.get(i).getScriptId() %></div></td>
    	   <td align="center">
    	   <input type="hidden" name="modeId" id="modeId<%=i%>" value="<%=list2.get(i).getMode()%>">
    	   <div style="display:inline;" id="modeName<%=i %>"> <%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_CONTAC_MODE",String.valueOf(list2.get(i).getMode()))  %></div>
    	  </td>
        </tr>
        			<%
        		}
        	}%>
            <tr class="jg">
              <td align="center"><label></label></td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              </tr>
          </table>
 </div>
<!-- 接触规则结束 -->

 <div class="validatebox-tabTD-title">活动审批信息</div>

           <iframe id="ff" scrolling="0"width="100%" height="250px"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="<%=request.getContextPath()%>/marketing/tacticPassList.jsp"></iframe><br>
</form>

</body>
</html>
