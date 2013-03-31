<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.ActivityInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="com.ailk.bi.marketing.entity.ActivityInfo"%>
<%@page import="java.util.List"%>
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
function doSearch()
{
 	form1.action="activityAction.rptdo?optype=searchActivitydialog&doType=search";
 	form1.submit();
}
<%
List<ActivityInfo> list = (List<ActivityInfo>) request.getAttribute("activityList");
String	activityTypeSql = "SELECT T.ACTIVITY_TYPE_ID,T.ACTIVITY_TYPE_NAME FROM  MK_PL_ACTIVITY_TYPE T";
ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
if(null==qryStruct.activity_name){
	qryStruct.activity_name="";
}
%>
</script>
</head>
<body>
<form action="" method="post" name="form1">
<!-- 弹出模式窗口 -->
<input type="hidden" id="qry__activity_state" name="qry__activity_state" value="2">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
        <tr>
          <td width="15%" class="validatebox-tabTD-left">活动名称：</td>
          <td width="30%" class="validatebox-tabTD-right"><input value="<%=qryStruct.activity_name%>" id="qry__activity_name" name="qry__activity_name"></td>
          <td width="15%" class="validatebox-tabTD-left">活动类型：</td>
          <td width="30%" class="validatebox-tabTD-right">
          <BIBM:TagSelectList  focusID="<%=qryStruct.activity_type%>" allFlag=""	listName="qry__activity_type" listID="0" selfSQL="<%=activityTypeSql%>" />
          </td>
          <td width="10%" class="validatebox-tabTD-left"><input type="button" class="btn4"  value="查找" onclick="doSearch()"></td>
        </tr>
         <tr>
          <td colspan="5" class="validatebox-tabTD-right">
<!-- 查询类表 开始-->
 <div class="list_content">
      <table >
        <tr width="100%">
          <th width="8%" align="center"> 选  择 </th>
          <th width="12%" align="center"> 活动名称 </th>
          <th width="10%" align="center"> 活动类型 </th>
          <th width="10%" align="center"> 创建人</th>
          <th width="10%" align="center"> 创建时间</th>
          <th width="10%" align="center"> 生效时间</th>
          <th width="10%" align="center"> 失效时间</th>
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
          <td align="center"><label>
            <input type="checkbox" id="checkbox_<%=list.get(i).getActivityId() %>" name="checkbox" value="<%=list.get(i).getActivityId() %>@@@<%=list.get(i).getActivityName() %>">
          </label></td>
          <td align="center"><%=list.get(i).getActivityName() %></td>
          <td align="center"><%=list.get(i).getActivityType().getActivityTypeName()  %></td>
          <td align="center"><%=list.get(i).getCreator()%></td>
          <td align="center"><%=CommonFormate.dateFormate(list.get(i).getCreateDate()) %> </td>
          <td align="center"><%=CommonFormate.dateFormate(list.get(i).getStartData()) %></td>
          <td align="center" class="last"><%=CommonFormate.dateFormate(list.get(i).getEndDate()) %></td>
        </tr>	<%
        		}
        	}%>
      </table>
    </div>
<!-- 查询类表 结束-->
          </td>
        </tr>

</table>
<!-- 模式是窗口结束 -->
</form>
</body>
</html>