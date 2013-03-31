<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.ActivityInfo"%>
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
<%
		String decider = (String )request.getAttribute("decider");
		ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		if(null==qryStruct.activity_name)
		{
			qryStruct.activity_name="";
		}
		//获得活动类型
		//String activityTypeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_ACTIVITY_TYPE'";
		String	activityTypeSql = "SELECT T.ACTIVITY_TYPE_ID,T.ACTIVITY_TYPE_NAME FROM  MK_PL_ACTIVITY_TYPE T";
		//获得审批意见类型
		String activityStateSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_APPROVAL_TYPE'";
		//获得客户类型
		String clientSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CLIENT_TYPE'";
		//获得方案名称
		String projectSql = "SELECT T.PROJECT_ID,T.PROJECT_NAME FROM MK_PL_PROJECT_INFO T";
		List<ActivityInfo> list =(List<ActivityInfo>) request.getAttribute("activityList");
	%>
<script language="javascript">
	function mySubmit(op) {
		form1.action="activityAction.rptdo?optype=activityList&doType="+op+"&decider=<%=decider%>";
		form1.submit();
	}
	function myAdd(op) {
		form1.action="activityAction.rptdo?optype=activityAdd&doType="+op;
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
					form1.action="activityAction.rptdo?optype=activityAdd&doType="+op+"&activityId="+id;
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
		if(checlCount>0){
			if(confirm("是否确定删除选中的"+checlCount+"条信息？")){
				form1.action="activityAction.rptdo?optype=activityList&doType="+op;
				form1.submit();
			}
		}else{
			alert("请至少选择一条要删除的信息！");
	    }
	}
	</script>

</head>
<body style="background-color:#f9f9f9" onLoad="selfDisp();">
<form ID="form1" name="form1" method="post" action="">
<%=WebPageTool.pageScript("form1","activityAction.rptdo?optype=activityList&doType=search")%>
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
    <div class="toptag" > 您所在位置：营销策划 >> 营销策略 >> <em class="red">营销活动管理</em> <span class="hbt"><a href="javascript:;"
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span> </div>
    <div class="topsearch">
      <table width="100%">
        <tr>
          <td align="right" width="15%">活动名称： </td>
          <td width="18%"><input id=qry__activity_name name=qry__activity_name value="<%=qryStruct.activity_name %>" class="txtinput"  />
          </td>
          <td align="right" width="15%"> 活动类型： </td>
          <td width="18%">
           <BIBM:TagSelectList focusID="<%=qryStruct.activity_type%>" script="class='easyui-combobox'"	listName="qry__activity_type" listID="0" allFlag="" selfSQL="<%=activityTypeSql%>" />
          </td>
          <td align="right" width="15%">审批状态： </td>
          <td width="18%">
          <BIBM:TagSelectList focusID="<%=qryStruct.activity_state%>" script="class='easyui-combobox'"	listName="qry__activity_state" listID="0" allFlag="" selfSQL="<%=activityStateSql %>" />
          </td>
        </tr>
        <tr>
             <td align="right" width="15%"> 客户类型： </td>
          <td width="18%">
           <BIBM:TagSelectList focusID="<%=qryStruct.activity_client%>" script="class='easyui-combobox'"	listName="qry__activity_client" listID="0" allFlag="" selfSQL="<%=clientSql%>" />
          </td>
            <td align="right" width="15%"> 对应方案： </td>
          <td width="18%">
           <BIBM:TagSelectList focusID="<%=qryStruct.activity_project%>" script="class='easyui-combobox'"	listName="qry__activity_project" listID="0" allFlag="" selfSQL="<%=projectSql%>" />
          </td>
          <td align="right" width="15%"></td>
          <td>
                <button class="btn3" type="button" onClick="mySubmit('search')"> 查 询 </button>
       	  </td>
        </tr>
      </table>
    </div>
    <div class="topsearch_btn"> <span>
    <%if (null==decider) {
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
          <th width="5%" align="center"> 选  择 </th>
          <th width="12%" align="center"> 活动名称 </th>
          <th width="12%" align="center"> 对应方案名称 </th>
          <th width="8%" align="center"> 活动类型 </th>
          <th width="10%" align="center"> 客户类型 </th>
          <th width="8%" align="center"> 活动级别 </th>
          <th width="5%" align="center"> 优先级 </th>
          <th width="8%" align="center"> 创建人</th>
          <th width="8%" align="center"> 审批状态</th>
          <th width="8%" align="center"> 创建时间</th>
          <th width="8%" align="center"> 生效时间</th>
          <th width="8%" align="center"> 失效时间</th>
        </tr>
         <%   if(perPageCount>0){
      	    for(int i=0;i<pageInfo.iLinesPerPage && (1+i+pageInfo.absRowNoCurPage())<=pageInfo.iLines;i++){
        			if(i%2!=0){
        				%>
        				<tr class="jg">
        				<%
        			}else{
        			%>
        			 <tr><%} %>
          <td align="center"><label>
            <input type="checkbox" id="checkbox_<%=list.get(i+pageInfo.absRowNoCurPage()).getActivityId()%>" name="checkbox" value="<%=list.get(i+pageInfo.absRowNoCurPage()).getActivityId() %>">
          </label></td>
          <td align="center">
          <input type="hidden" name="hiddenState" value="<%=list.get(i+pageInfo.absRowNoCurPage()).getState()%>">
          <a href="activityAction.rptdo?optype=activityPass&doType=modify&activityId=<%=list.get(i).getActivityId() %>" ><font color="blue">
          <%=list.get(i+pageInfo.absRowNoCurPage()).getActivityName() %></font></a></td>
          <td align="center"><%=list.get(i+pageInfo.absRowNoCurPage()).getProjectInfo().getProjectName() %></td>
          <td align="center"><%=list.get(i+pageInfo.absRowNoCurPage()).getActivityType().getActivityTypeName()  %> </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_CLIENT_TYPE",String.valueOf(list.get(i+pageInfo.absRowNoCurPage()).getClientType()))  %> </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_ACTIVITY_LEVEL",String.valueOf(list.get(i+pageInfo.absRowNoCurPage()).getActivityLevel()))  %> </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_ACTIVITY_PRIORITY",String.valueOf(list.get(i+pageInfo.absRowNoCurPage()).getPriority()))  %> </td>
          <td align="center"><%=list.get(i+pageInfo.absRowNoCurPage()).getCreator() %></td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_APPROVAL_TYPE",String.valueOf(list.get(i+pageInfo.absRowNoCurPage()).getState()))  %> </td>
          <td align="center"><%=CommonFormate.dateFormate(list.get(i+pageInfo.absRowNoCurPage()).getCreateDate()) %> </td>
          <td align="center"><%=CommonFormate.dateFormate(list.get(i+pageInfo.absRowNoCurPage()).getStartData()) %></td>
          <td align="center" class="last"><%=CommonFormate.dateFormate(list.get(i+pageInfo.absRowNoCurPage()).getEndDate()) %></td>

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
