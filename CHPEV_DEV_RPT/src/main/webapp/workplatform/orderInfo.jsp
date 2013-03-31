<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.workplatform.entity.OrderInfo"%>
<%@page import="com.ailk.bi.marketing.entity.TacticInfo"%>
<%@page import="com.ailk.bi.marketing.entity.ChannleInfo"%>
<%@page import="com.ailk.bi.marketing.entity.ActivityInfo"%>
<%@page import="com.ailk.bi.marketing.entity.NameListInfo"%>
<%@page import="com.ailk.bi.marketing.entity.FileInfo"%>
<%@page import="com.ailk.bi.marketing.entity.ContactPlyInfo"%>
<%@page import="com.ailk.bi.marketing.entity.TargetOpInfo"%>
<%@page import="com.ailk.bi.workplatform.entity.ContactInfo"%>


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
			List<ContactInfo> contactList = (List<ContactInfo>)request.getAttribute("contactList");
			List<OrderInfo> orderlistCust =(List<OrderInfo>) request.getAttribute("orderlistCust");
		    String fileUrl =request.getContextPath()+GetSystemConfig.getBIBMConfig().getUploadFolder();
			OrderInfo orderInfo = (OrderInfo) request.getAttribute("orderInfo");//工单基本信息
	        List<TargetOpInfo> list1  =	(List<TargetOpInfo>)request.getAttribute("TargetOpInfoLis1");//营销指标配置
	        List<ContactPlyInfo> list2 =(List<ContactPlyInfo>) request.getAttribute("ContactPlyInfoList");//接触规则配置
	        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String no="";
			String type="";
			String custName="";
			String custId="";
			String number="";
			String projectName="";
			String activityName="";
			String otherInfo="";
			int projectID = 0;
			int activityID = 0;
			if(null!=orderInfo){
				no=orderInfo.getOrder_no()+"";
				type=orderInfo.getOrder_type()+"";
				if (!StringTool.checkEmptyString(orderInfo.getCust_name())) {
					custName=orderInfo.getCust_name();
					custId = orderInfo.getCust_id();
				}
				if (null!=orderInfo.getServ_number()) {
					number=orderInfo.getServ_number();
				}
				if (null!=orderInfo.getProjectInfo()) {
					projectName=orderInfo.getProjectInfo().getProjectName();
					projectID = orderInfo.getProjectInfo().getProjectId();
				}
				if (null!=orderInfo.getActivityInfo()) {
					activityName=orderInfo.getActivityInfo().getActivityName();
					activityID = orderInfo.getActivityInfo().getActivityId();
				}
				if (null!=orderInfo.getActivityInfo()) {
					otherInfo=orderInfo.getActivityInfo().getActivityName();
				}
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


function openFile(fileUrl){
	window.open(fileUrl);
}
function opProjectWindow(){
	var url = "projectAction.rptdo?optype=projectPass&doType=modify&projectId="+<%=projectID%>;
	window.open(url,'newwindow','height=600,width=1000,top=60,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no, status=no') ;
}
function opActivityWindow(){
	var url = "activityAction.rptdo?optype=activityPass&doType=modify&activityId="+<%=activityID%>;
	window.open(url,'newwindow','height=600,width=1000,top=60,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no, status=no') ;
}
function myFunction(){
	form1.action="contactAction.rptdo?optype=contactAdd&doType=search";
	form1.submit();
}
function myopWindow(id,custId){
	var url = "orderInfoAction.rptdo?optype=orderInfo&doType=searchOne&orderId="+id+"&custId="+custId;
	window.open(url,'newwindow','height=600,width=900,top=60,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no, status=no') ;
}
</script>

</head>
<body style="background-color:#f9f9f9">
<form action="" method="post" name="form1">

 <div id="tt" class="easyui-tabs">
        <div title='工单信息' style="padding: 10px;">
                <!----------------------------------------------------------- 工单信息开始------------------------------------- -->
  <div id="maincontent">
  <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
<div class="validatebox-tabTD-title">策略基本信息</div>

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
        <tr>
          <td width="20%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />工单号：</td>
          <td width="30%" class="validatebox-tabTD-right"><%=no %></td>
          <td width="20%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />工单类型：</td>
          <td class="validatebox-tabTD-right">
          <%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_ORDER_TYPE",type)  %>
          </td>
        </tr>

         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />客户名称：</td>
          <td width="21%" class="validatebox-tabTD-right">    <%=custName %></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />手机号码：</td>
          <td class="validatebox-tabTD-right">    <%=number %></td>
        </tr>

         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />方案信息：</td>
          <td width="21%" class="validatebox-tabTD-right">    <%=projectName %>
         &nbsp;&nbsp;&nbsp;&nbsp;<input  type="button" class="public-btn2" value="查 看 详 细 信 息" onclick="opProjectWindow()" />
          </td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />活动信息：</td>
          <td class="validatebox-tabTD-right">    <%=activityName %>   &nbsp;&nbsp;&nbsp;&nbsp;<input  type="button" class="public-btn2" value="查 看 详 细 信 息" onclick="opActivityWindow()" /></td>
        </tr>
         <tr>
          <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="20 height="16" border="0" />详细描述：</td>
          <td colspan="3" class="validatebox-tabTD-right"><%=otherInfo %></td>
        </tr>
</table>

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
        </div>
         <!-- 工单信息结束-->
        <!-- -------------------------------------------------------------- -->
<!-- 历史接触记录-->
<div class="validatebox-tabTD-title">历史接触记录</div>
<div class="list_content">
 <table >
            <tr width="100%">
              <th width="10%" align="center"> 工单编号</th>
              <th width="20%" align="center"> 接触方式</th>
              <th width="20%" align="center"> 接触时间 </th>
              <th width="20%" align="center"> 接触效果 </th>
              <th width="30%" align="center"> 接触内容 </th>
              </tr>
 <% if(null!=contactList){
        		for(int i = 0 ;i<contactList.size();i++){
        			if(i%2!=0){
        				%>
        				<tr class="jg">
        				<%
        			}else{
        			%>
        			 <tr><%} %>
           <td align="center"><%=contactList.get(i).getOrder_no() %></td>
           <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_CONTAC_MODE",String.valueOf(contactList.get(i).getContactMode())) %></td>
           <td align="center"><%=contactList.get(i).getContact_date() %></td>
           <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_CONTACT_PLEASED_STATE",String.valueOf(contactList.get(i).getPleased_state())) %></td>
           <td align="center"><%=contactList.get(i).getContact_content() %></td>
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
              </tr>
          </table>
 </div>
<!-- 历史接触记录结束 -->
<!--  -->
<div class="validatebox-tabTD-title">该客户未处理的工单</div>
  <div class="list_content">
      <table >
        <tr width="100%">
          <th width="5%" align="center"> 工单编号</th>
          <th width="8%" align="center"> 工单状态 </th>
          <th width="8%" align="center"> 活动名称 </th>
          <th width="8%" align="center"> 客户名称 </th>
          <th width="10%" align="center">服务号码 </th>
          <th width="8%" align="center"> 执行人 </th>
          <th width="8%" align="center"> 服务经理 </th>
          <th width="8%" align="center"> 执行渠道 </th>
          <th width="8%" align="center"> 生成时间</th>
        </tr>
         <%   if(orderlistCust!=null){
      	    for(int i=0;i<orderlistCust.size();i++){
        			if(i%2!=0){
        				%>
        				<tr class="jg">
        				<%
        			}else{
        			%>
        			 <tr><%} %>

          <td  align="center">
           <a href="javascript:myopWindow('<%=orderlistCust.get(i).getOrder_no()%>','<%=orderlistCust.get(i).getCust_id()%>')" ><font color="blue">
          <%=orderlistCust.get(i).getOrder_no() %></font></a></td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_ORDER_STATE",String.valueOf(orderlistCust.get(i).getOrder_state()))  %> </td>
          <td align="center"><%=orderlistCust.get(i).getActivityInfo().getActivityName()  %> </td>
          <td align="center"><%=orderlistCust.get(i).getCust_name()  %> </td>
          <td align="center"><%=orderlistCust.get(i).getServ_number()  %> </td>
          <td align="center"><% if(orderlistCust.get(i).getPerformer_id()!=null) {
        	  %>
        	  <%=orderlistCust.get(i).getPerformer_id().getUserName() %>
        	  <%
          } %> </td>
          <td align="center"><%=orderlistCust.get(i).getCust_svc_mgr_id()  %> </td>
          <td align="center"><%=orderlistCust.get(i).getChannelInfo().getChannleName()  %> </td>
          <td align="center"><%=CommonFormate.dateFormate(orderlistCust.get(i).getCreatedate()) %></td>

        </tr>	<%
        		}
        	}%>
      </table>
    </div>
<!--  -->
       </div>
        <div title='客户回访' style="padding: 10px;">
<iframe id="cc" scrolling="yes" width="100%" height="800"  border="0" frameborder="0"marginwidth="0" marginheight="0"
src="contactAction.rptdo?optype=contactAdd&doType=search&orderId=<%=no%>&custId=<%=custId%>"></iframe>
    </div>
</div>
</form>

</body>
</html>
