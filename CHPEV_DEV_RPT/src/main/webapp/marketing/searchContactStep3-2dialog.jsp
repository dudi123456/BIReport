<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.MessageInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="com.ailk.bi.marketing.entity.SurveyInfo"%>
<%@page import="java.util.List"%>
<%@page import="com.ailk.bi.pages.PagesInfoStruct"%>
<%@page import="com.ailk.bi.pages.WebPageTool"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/pubilicButton.css" />
<script type="text/javascript">
function doSearch() {
	form1.action="surveyAction.rptdo?optype=searchContactStep3-2dialog&doType=search";
	form1.submit();
}
function mychange(){
	form1.action="msgAction.rptdo?optype=searchContactStep3-1dialog&doType=search";
	form1.submit();
}
<%
ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
String typeSql ="select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_SURVEY_TYPE'";

String opModeSql = "select t.code_id||'@@@'||t.code_name,t.code_name from ui_code_list t  where t.type_code='MK_PL_CONTAC_MODE' order by  t.code_id asc ";
List<SurveyInfo> list =(List<SurveyInfo>) request.getAttribute("surveyList");
%>
</script>
</head>
<body>
<form action="" method="post" name="form1">
<!-- 弹出模式窗口 -->
<div id="div1" >
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
        <tr>
         <td width="15%" class="validatebox-tabTD-left">接触方式：</td>
          <td width="25%" class="validatebox-tabTD-right">
           <BIBM:TagSelectList  script="onchange='mychange()'" 	listName="ModeID" listID="0" selfSQL="<%=opModeSql%>" />
           </td>
          <td width="15%" class="validatebox-tabTD-left">短信类型：</td>
          <td width="25%" class="validatebox-tabTD-right">
		   <input type="hidden" value="2" id="qry__survey_state" name="qry__survey_state">
           <BIBM:TagSelectList focusID="<%=qryStruct.msg_type%>" script="class='easyui-combobox'"	listName="qry__survey_type" listID="0" allFlag="" selfSQL="<%=typeSql%>" />
           </td>
          <td width="20%" class="validatebox-tabTD-left" align="center"><input type="button" class="btn4"  value="查找" onclick="doSearch()"></td>
        </tr>
         <tr>
          <td colspan="5" class="validatebox-tabTD-right">
<!-- 查询类表 开始-->
 <div class="list_content">





       <table>
        <tr>
          <th width="10%" align="center"> 选  择 </th>
          <th width="15%" align="center">问卷名称</th>
          <th width="10%" align="center">问卷类型</th>
          <th width="10%" align="center">创建人</th>
          <th width="10%" align="center">审批状态</th>
          <th width="15%" align="center">创建时间</th>
        </tr>
        <% if(null!=list)
        	{
        		for(int i = 0 ;i<list.size();i++){
        			if(i%2!=0){
        				%>
        				<tr class="jg">
        				<%
        			}else{
        			%>
        			 <tr><%} %>
         <td align="center"><label> <input type="checkbox" id="checkbox_<%=list.get(i).getSurveyId() %>" name="checkbox" value="<%=list.get(i).getSurveyId() %>@@@<%=list.get(i).getSurveyName() %>"></label></td>
          <td align="center"><%=list.get(i).getSurveyName() %></td>
           <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_SURVEY_TYPE",String.valueOf(list.get(i).getSurveyType()))  %></td>
           <td align="left"><%=list.get(i).getCreator()  %></td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_APPROVAL_TYPE",String.valueOf(list.get(i).getState()))  %></td>
          <td align="center"><%=CommonFormate.dateFormate(list.get(i).getCreateDate()) %></td>
        </tr>
        			<%
        		}
        	}%>
      </table>

    </div>
<!-- 查询类表 结束-->
          </td>
        </tr>
</table>
</div>
<!-- 模式是窗口结束 -->

</form>
</body>
</html>