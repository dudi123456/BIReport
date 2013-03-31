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
<%@page import="com.ailk.bi.marketing.entity.ActivityModeInfo"%>
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
			ActivityModeInfo modeInfo = (ActivityModeInfo)session.getAttribute("modeInfo");
			String modeContent="";
			if(null!=modeInfo){
				if(null==modeInfo.getContent()){
					modeContent="";
				}else{
					modeContent=modeInfo.getContent();
				}
			}
			List<GroupInfo> groupList = (List<GroupInfo>) session.getAttribute("groups");
	        List<FileInfo> list = (List<FileInfo>) session.getAttribute("files");
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
			String client ="";
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
			int activityID=0;

			if(null!=info){
				wave=String.valueOf(info.getWave());
					dispatchCyc=String.valueOf(info.getDisCyc());
					client=String.valueOf(info.getClientType());
					nameListType=String.valueOf(info.getNameListType());
				if (null!=info.getStartData()) {
					date01=sdf.format(info.getStartData());
				}
				activityID = info.getActivityId();
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
					type=String.valueOf(info.getActivityType().getActivityTypeName());
				}

					level=String.valueOf(info.getActivityLevel());
					priority=String.valueOf(info.getPriority());
				ChannleInfo CInfo = info.getChannleInfo();
				if (null!=CInfo) {
					channle=CInfo.getChannleName();
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
			}
	    String imgUrl =request.getContextPath()+"/images/validatebox_warning.gif";
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
	var myDate2="<%=CommonFormate.dateFormate(info.getEndDate())%>";
	var state="<%=info.getState()%>";
	var myDate = new Date();
	var yy = myDate.getFullYear();    //获取完整的年份(4位,1970-????)
	var mm =  myDate.getMonth()+1;       //获取当前月份(0-11,0代表1月)
	var dd = myDate.getDate();
	if(mm<10){
		mm="0"+mm;
	}
	if(dd<10){
		dd="0"+dd;
	}
	myDate1=yy+"-"+mm+"-"+dd;

	if(2!=state){
		alert("该活动还没有通过审批，不能转为营销案例");
		return ;
	}else if(myDate1>=myDate2){
		alert("该活动还没有结束，不能转为营销案例");
		return ;
	}else{
		var div = document.getElementById("myModeDiv");
		div.style.display=op;
	}
}
function showClose(){
	form1.action="activityAction.rptdo?optype=activityPass&doType=modify&activityId="+<%=activityID%>;
	form1.submit();
}
function openFile(fileUrl){
	window.open(fileUrl);
}

function opProjectWindow(){
	var url = "projectAction.rptdo?optype=projectPass&doType=modify&projectId="+<%=projectID%>;
	window.open(url,'newwindow','height=600,width=1000,top=60,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no, status=no') ;
	//window.open(url, 'newwindow ', 'height=100, width=400, top=0, left=0, toolbar=no, menubar=no, scrollbars=no');
	//window.open(url,   'height=100,   width=400,   top=0,   left=0,   toolbar=no,   menubar=no,   scrollbars=no,   resizable=no,location=no,   status=no ');
}

function opTactictWindow(){
	var url = "tacticAction.rptdo?optype=tacticPass&doType=modify&tactictId="+<%=tacticID%>;
	window.open(url,'newwindow','height=600,width=1000,top=60,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no, status=no') ;

}
</script>

</head>
<body style="background-color:#f9f9f9">
<form action="" method="post" name="form1">
<input type="hidden" id="txt_tacticId" name="txt_tacticId" value="<%=tacticID%>" >
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
          <td  width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />详细描述：</td>
          <td colspan="3" class="validatebox-tabTD-right">

          <input type="hidden" id="txt_activityId" name="txt_activityId" value="<%=info.getActivityId() %>">
          <textarea  required="true" style="height:80px;width:800px;t" id="txt_modeContent" name="txt_modeContent"><%=modeContent %></textarea>
          </td>
        </tr>
        <tr>
         <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />活动附件：</td>
          <td colspan="3" class="validatebox-tabTD-right">
<iframe id="ff" scrolling="0"width="100%" height="100px"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="<%=request.getContextPath()%>/marketing/modeFileUpload.jsp"></iframe><br>

          </td>
        </tr>
</table>
		<div class="buttonArea">
			 <button class="btn4" type="button" onClick="mySave()"> 保 存 </button>
			 <button class="btn4" type="button" onClick="showClose()"> 取 消 </button>
		 </div>
</div>


<div style="width:90%;" class="validatebox-tabTD-title">
<div style="float:left">
营销活动基本信息录入
</div>
<div style="float:right">
  <button class="btn4" type="button" onClick="showMode('block')"> 转为营销案例 </button>
</div>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
        <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />活动名称：</td>
          <td width="21%" class="validatebox-tabTD-right"><%=name %></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />活动类型：</td>
          <td class="validatebox-tabTD-right">
          <%=type  %>
          </td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />活动编码：</td>
          <td width="21%" class="validatebox-tabTD-right">
            <%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_ACTIVITY_TYPE",code)  %>
         </td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />活动波次：</td>
          <td class="validatebox-tabTD-right">  <%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_ACTIVITY_WAVE",wave)  %></td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />活动级别：</td>
          <td width="21%" class="validatebox-tabTD-right">
           <%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_ACTIVITY_LEVEL",level)  %>
          </td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />优先级：</td>
          <td class="validatebox-tabTD-right">
            <%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_ACTIVITY_PRIORITY",priority)  %>
          </td>
        </tr>
        	<tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />调度运行周期：</td>
          <td width="21%" class="validatebox-tabTD-right">
          <%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_ACTIVITY_DISPATCH_CYC",dispatchCyc)  %>
          </td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />调度运行时间：</td>
          <td class="validatebox-tabTD-right">
          <%=date03 %>
          </td>
        </tr>

         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />开始时间：</td>
          <td width="21%" class="validatebox-tabTD-right">    <%=date01 %></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />结束时间：</td>
          <td class="validatebox-tabTD-right">    <%=date02 %></td>
        </tr>
</table>

<div class="validatebox-tabTD-title">活动对应策略/方案</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
  <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />营销策略：</td>
          <td  class="validatebox-tabTD-right">
          <%=tactic %>&nbsp;&nbsp;&nbsp;&nbsp;<input  type="button" class="public-btn2" value="查 看 详 细 信 息" onclick="opTactictWindow()" />
          </td>
          <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />营销方案：</td>
          <td class="validatebox-tabTD-right">
          <%=project %>&nbsp;&nbsp;&nbsp;&nbsp;<input  type="button" class="public-btn2" value="查 看 详 细 信 息" onclick="opProjectWindow()" />
          </td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />客户类型：</td>
          <td width="21%" class="validatebox-tabTD-right">
            <%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_CLIENT_TYPE",client)  %>
      </td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />活动渠道：</td>

          <td class="validatebox-tabTD-right"> <%=channle %></td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />客户名单列表：</td>
          <td colspan="3" class="validatebox-tabTD-right">
<div id="allGruopList" style="display: block">
      <div class="list_content">
   <table >
     <tr width="100%">
       <th width="8%" align="center"> 序号 </th>
         <th width="20%" align="center"> 客户群名称 </th>
          <th width="10%" align="center"> 客户群类型 </th>
           <th width="10%" align="center"> 客户群来源类型 </th>
            <th width="10%" align="center"> 所属品牌 </th>
             <th width="10%" align="center"> 提取数据状态 </th>
              <th width="10%" align="center"> 包含客户数 </th>
               <th width="10%" align="center"> 提取客户数 </th>
     </tr>
<%
 	if(null!=groupList){
       		for(int i = 0 ;i<groupList.size();i++){
       			if(i%2!=0){
       				%>
       				<tr class="jg">
       				<%
       			}else{
       			%>
       			 <tr><%} %>
          <td align="center">客户群<%=i+1 %></td>
            <td align="center"><%=groupList.get(i).getGroupName() %></td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_GROUP_TYPE",String.valueOf(groupList.get(i).getGroupTypeId()))  %> </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_GROUP_CREATE_TYPE",String.valueOf(groupList.get(i).getCreateType()))  %> </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_GROUP_BRAND_OF",String.valueOf(groupList.get(i).getBrandOf()))  %> </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_GROUP_STATUS",groupList.get(i).getStatus())  %> </td>
          <td align="center"><%=String.valueOf(groupList.get(i).getCustomerCount())  %> </td>
          <td align="center"> <%=String.valueOf(groupList.get(i).numCount)%></td>
       </tr>	<%
       		}
       	}%>

       	 <tr class="jg">
              <td align="center"><label></label></td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
               <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              </tr>
     </table>
   </div>
     <input type="button" class="public-btn2" value="隐藏客户群列表" onclick="javascript:document.getElementById('allGruopList').style.display='none';document.getElementById('allGruopList2').style.display='block'" />
</div>
<div id="allGruopList2" style="display: none">
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
          <td colspan="3" class="validatebox-tabTD-right" height="50px"><%=content %>
          </td>
        </tr>
        <tr>
         <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />活动附件：</td>
          <td colspan="3" class="validatebox-tabTD-right">
          <div id="allfileList" style="display: block">
<div class="list_content">
   <table >
     <tr width="100%">
       <th width="8%" align="center"> 序  号 </th>
       <th width="12%" align="center">文件编码</th>
       <th width="12%" align="center">文件名称</th>
       <th width="10%" align="center">文件大小 </th>
     </tr>
<%
 	if(null!=list){
       		for(int i = 0 ;i<list.size();i++){
       			if(i%2!=0){
       				%>
       				<tr class="jg">
       				<%
       			}else{
       			%>
       			 <tr><%} %>
          <td align="center">附件<%=i+1 %></td>
          <td align="center">
          <a style="color: blue" href="javascript:openFile('<%=fileUrl+list.get(i).getFileUrl()%>')">
          <%=list.get(i).getFileCode()%></a></td>
      	  <td align="center"><%=list.get(i).getFileName()%></td>
          <td align="center"><%=list.get(i).getFileSize()%>KB</td>
       </tr>	<%
       		}
       	}%>
       		 <tr class="jg">
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              </tr>
     </table>
   </div>
 <input type="button" class="public-btn2" value="隐藏附件列表" onclick="javascript:document.getElementById('allfileList').style.display='none';document.getElementById('allfileList2').style.display='block'" />
</div>
<div id="allfileList2" style="display: none">
 <input type="button"  class="public-btn2" value="显示附件列表" onclick="javascript:document.getElementById('allfileList').style.display='block';document.getElementById('allfileList2').style.display='none'" />
  </div>

          </td>
        </tr>
</table>
 <div class="validatebox-tabTD-title">活动审批信息</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
 <tr>
        </tr>
          <tr>
          <td  width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />审批日志：</td>
          <td colspan="3" class="validatebox-tabTD-right" height="50px">
          <div id="allssList" style="display: block">
           <iframe id="ff" scrolling="0"width="100%" height="250px"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="<%=request.getContextPath()%>/marketing/activityPassList.jsp"></iframe><br>
           <input type="button" class="public-btn2" value="隐藏审批日志列表" onclick="javascript:document.getElementById('allssList').style.display='none';document.getElementById('allssList2').style.display='block'" />
</div>
<div id="allssList2" style="display: none">
 <input type="button"  class="public-btn2" value="显示审批日志列表" onclick="javascript:document.getElementById('allssList').style.display='block';document.getElementById('allssList2').style.display='none'" />
  </div>
          </td>
        </tr></table>
</form>

</body>
</html>
