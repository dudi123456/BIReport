<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.ProjectInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="com.ailk.bi.pages.PagesInfoStruct"%>
<%@page import="java.util.*"%>
<%@page import="com.ailk.bi.pages.WebPageTool"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.common.app.ReflectUtil"%>
<%@page import="com.ailk.bi.common.app.StringB"%>

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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/icon.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/icontent.css">
<script language="javascript">
<%
String warnName = (String)request.getAttribute("warnName");
ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
if(null==qryStruct.project_name)
{
	qryStruct.project_name="";
}
//获得方案类型
String projectTypeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_PROJECT_TYPE'";
//获得审批意见类型
String projectStateSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_APPROVAL_TYPE'";
//获得方案优先级
String projectPrioritySql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_PROJECT_PRIORITY'";
//获得方案级别
String projectLevelSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_PROJECT_LEVEL'";

List<ProjectInfo> list =(List<ProjectInfo>) request.getAttribute("projectList");
%>

//: 判断网页是否加载完成
document.onreadystatechange = function () {
  if(document.readyState=="complete") {
	   <%
	   String msg = (String)request.getAttribute("delectMsg");
	   if(null!=msg){
	   	%>
	   	alert('<%=msg%>');
	   	<%
	   }
	   %>
  }
}
	function newActivity(projectId){
		//alert(projectId);
		form1.action="activityAction.rptdo?optype=activityAdd&doType=newActivity&projectId="+projectId;
		form1.submit();
	}
	function mySubmit(op) {
		form1.action="projectAction.rptdo?optype=projectList&doType="+op+"&warnName=<%=warnName%>";
		form1.submit();
	}
	function myAdd(op) {
		form1.action="projectAction.rptdo?optype=projectAdd&doType="+op;
		form1.submit();
	}
	function myModify(op) {
		var id = 0;
		var state = 0;
		var arr = document.getElementsByName("checkbox");
		var arrState = document.getElementsByName("hiddenState");
		var checlCount = 0;
		for(var i =0;i< arr.length;i++ ){
			if(arr[i].checked){
				checlCount=checlCount+1;
			}
		}
		if(checlCount==0){
			alert("你没有选择任何的信息！");
		}else if(checlCount>1)	{
			alert("每次只能编辑一条信息！");
		}else{
			arr = document.getElementsByName("checkbox");
			for(var i =0;i< arr.length;i++ ){
				if(arr[i].checked){
					id=arr[i].value;
					state = arrState[i].value;
				}
			}
			if(state!=0){
				alert("该信息已经进入审批环节，不能再修改！！");
			}else{
				form1.action="projectAction.rptdo?optype=projectAdd&doType="+op+"&projectId="+id;
				form1.submit();
			}
		}
	}
	function myDelect(op) {
		var arr = document.getElementsByName("checkbox");
		var checlCount = 0;
		for(var i =0;i< arr.length;i++ ){
			if(arr[i].checked){
				checlCount=checlCount+1;
			}
		}

		if(checlCount==0){
			alert("你没有选择任何的信息！");
		}else if(checlCount>1)	{
			alert("每次只能删除一条信息！");
		}else{
			if(confirm("是否确定删除选中的"+checlCount+"条信息？")){
				form1.action="projectAction.rptdo?optype=projectList&doType="+op;
				form1.submit();
			}
		}
	}
	</script>

</head>
<body style="background-color:#f9f9f9" onLoad="selfDisp();">
<form ID="form1" name="form1" method="post" action="">
<%=WebPageTool.pageScript("form1","projectAction.rptdo?optype=projectList&doType=search")%>
<%
//报表翻页页数
int perPageCount =10;
int recordCount = list.size();
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, recordCount, perPageCount );
if (pageInfo != null) {
    out.print(WebPageTool.pageHidden(pageInfo));
}
%>
  <div id="maincontent">
    <div class="toptag" > 您所在位置：营销策划 >> 营销策略 >> <em class="red">营销方案管理</em> <span class="hbt"><a href="javascript:;"
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span> </div>
    <div class="topsearch">
      <table width="100%">
        <tr>
          <td align="right" width="15%">方案名称： </td>
          <td width="18%"><input id=qry__project_name name=qry__project_name value="<%=qryStruct.project_name %>" class="txtinput"  />
          </td>
          <td align="right" width="15%"> 方案类型： </td>
          <td width="18%">
           <BIBM:TagSelectList focusID="<%=qryStruct.project_type%>" script="class='easyui-combobox'"	listName="qry__project_type" listID="0" allFlag="" selfSQL="<%=projectTypeSql%>" />
          </td>
          <td align="right" width="15%">审批状态： </td>
          <td width="18%">
          <BIBM:TagSelectList focusID="<%=qryStruct.project_state%>" script="class='easyui-combobox'"	listName="qry__project_state" listID="0" allFlag="" selfSQL="<%=projectStateSql %>" />
          </td>
        </tr>
        <tr>
             <td align="right" width="15%"> 方案級別： </td>
          <td width="18%">
           <BIBM:TagSelectList focusID="<%=qryStruct.project_level%>" script="class='easyui-combobox'"	listName="qry__project_level" listID="0" allFlag="" selfSQL="<%=projectLevelSql%>" />
          </td>
            <td align="right" width="15%"> 优先级： </td>
          <td width="18%">
           <BIBM:TagSelectList focusID="<%=qryStruct.project_Priority%>" script="class='easyui-combobox'"	listName="qry__project_Priority" listID="0" allFlag="" selfSQL="<%=projectPrioritySql%>" />
          </td>
          <td align="right" width="15%"></td>
          <td>
                <button class="btn3" type="button" onClick="mySubmit('search')"> 查 询 </button>
       	  </td>
        </tr>
      </table>
    </div>

    	<div class="topsearch_btn"> <span>
    <% if(null==warnName) {
    	%>
      <button class="btn3" type="button" onClick="myAdd('add')"> 新 建 </button>
      <button class="btn3" type="button" onClick="myModify('modify')"> 修 改 </button>
      <button class="btn3" type="button" onClick="myDelect('delect')">删 除 </button>
    	<%
    }%>
   </span> </div>
    <div class="list_content">
      <table >
        <tr width="100%">
          <th width="5%"  align="center"> 选  择 </th>
          <th width="15%" align="center"> 方案名称 </th>
          <th width="15%" align="center"> 营销策略名称 </th>
          <th width="10%" align="center"> 渠道ID </th>
          <th width="10%" align="center"> 方案类型 </th>
          <th width="8%"  align="center"> 方案级别 </th>
          <th width="5%"  align="center"> 优先级 </th>
          <th width="8%"  align="center"> 创建人</th>
          <th width="8%"  align="center"> 审批状态</th>
          <th width="8%"  align="center"> 创建时间</th>
          <th width="8%"  align="center"> 创建活动</th>
        </tr>
         <%if(perPageCount>0){
     	    for(int i=0;i<pageInfo.iLinesPerPage && (1+i+pageInfo.absRowNoCurPage())<=pageInfo.iLines;i++){
        			if(i%2!=0){
        				%>
        				<tr class="jg">
        				<%
        			}else{
        			%>
        			 <tr><%} %>
          <td align="center"><label>
            <input type="checkbox" id="checkbox_<%=list.get(i).getProjectId() %>" name="checkbox" value="<%=list.get(i).getProjectId() %>">
          </label></td>
          <td align="center"><a href="projectAction.rptdo?optype=projectPass&doType=modify&projectId=<%=list.get(i).getProjectId() %>" ><font color="blue"><%=list.get(i+pageInfo.absRowNoCurPage()).getProjectName() %></font></a></td>
          <td align="center"><%=list.get(i+pageInfo.absRowNoCurPage()).getTacticInfo().getTacticName() %> </td>
          <td align="center"><%=list.get(i+pageInfo.absRowNoCurPage()).getChannleInfo().getChannleName() %> </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_PROJECT_TYPE",String.valueOf(list.get(i+pageInfo.absRowNoCurPage()).getProjectType()))  %> </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_PROJECT_LEVEL",String.valueOf(list.get(i+pageInfo.absRowNoCurPage()).getProjectLevel()))  %> </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_PROJECT_PRIORITY",String.valueOf(list.get(i+pageInfo.absRowNoCurPage()).getPriority()))  %> </td>
          <td align="center"><%=list.get(i+pageInfo.absRowNoCurPage()).getCreator() %></td>
          <td align="center"><input type="hidden" name="hiddenState" value="<%=list.get(i+pageInfo.absRowNoCurPage()).getState()%>"> <%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_APPROVAL_TYPE",String.valueOf(list.get(i+pageInfo.absRowNoCurPage()).getState()))  %> </td>
          <td align="center"><%=CommonFormate.dateFormate(list.get(i+pageInfo.absRowNoCurPage()).getCreateDate()) %> </td>
          <td align="center" class="last">
			<% if(list.get(i+pageInfo.absRowNoCurPage()).getState()==2){
				%>
				<input type="button" value="创建活动" onclick="newActivity('<%=list.get(i+pageInfo.absRowNoCurPage()).getProjectId()%>')">
				<%
			} %>
		  </td>
        </tr>	<%
        		}
        	}%>
      </table>
    </div>
  </div>
  <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
    <div id="div2"  style="border:0px solid green; width:100%; height:40px; position:absolute;  bottom:0px;"><%=WebPageTool.pagePolit(pageInfo)%></div>
</form>
</body>
</html>
