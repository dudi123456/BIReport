<%@page import="org.apache.hadoop.hive.ql.parse.HiveParser.limitClause_return"%>
<%@page import="org.apache.hadoop.hive.metastore.api.ThriftHiveMetastore.list_privileges_args"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.ProjectInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="com.ailk.bi.marketing.entity.NameListInfo"%>
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
<%
//客户类型
String activityclientSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CLIENT_TYPE'";
List<NameListInfo> list = (List<NameListInfo>) session.getAttribute("nameList");
ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
if(null==qryStruct.nameList_Name){
	qryStruct.nameList_Name="";
}
%>
function doSearch()
{
	form1.action="nameListAction.rptdo?doType=search&optype=searchNameListdialog";
	form1.submit();
}
</script>
</head>
<body >
<form action="" method="post" name="form1">
<!-- 弹出模式窗口 -->
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
         <tr>
          <td colspan="5" class="validatebox-tabTD-right">
<!-- 查询类表 开始-->
 <div class="list_content">
      <table >
       <tr>
          <td width="15%" class="validatebox-tabTD-left">客户名单名称：</td>
          <td width="30%" class="validatebox-tabTD-right"><input value="<%=qryStruct.nameList_Name %>" id="nameListName" name="nameListName"></td>
          <td width="15%" class="validatebox-tabTD-left">客户类型：</td>
          <td width="30%" class="validatebox-tabTD-right">
          <BIBM:TagSelectList  focusID="<%=qryStruct.client_type %>" allFlag=""	listName="client_type" listID="0" selfSQL="<%=activityclientSql%>" />
          </td>
          <td width="10%" class="validatebox-tabTD-left"><input type="button" class="btn4"  value="查找" onclick="doSearch()"></td>
        </tr>
        <tr width="100%">
          <th width="10%" align="center"> 选  择 </th>
          <th width="35%" align="center"> 客户名单名称 </th>
          <th width="15%" align="center"> 客户类型 </th>
          <th width="15%" align="center"> 客户名单类型</th>
          <th width="30%" align="center"> 说  明</th>
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
            <input type="checkbox" id="checkbox_<%=list.get(i).getNameListId() %>" name="checkbox" value="<%=list.get(i).getNameListId() %>@@@<%=list.get(i).getNameListName() %>">
          </label></td>
          <td align="center"><%=list.get(i).getNameListName() %></td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_CLIENT_TYPE",String.valueOf(list.get(i).getClientType()))  %></td>
            <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_NAMELIST_TYPE",String.valueOf(list.get(i).getNameListType()))  %></td>
          <td align="center"><%=list.get(i).getRemark()==null?"":list.get(i).getRemark() %></td>

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