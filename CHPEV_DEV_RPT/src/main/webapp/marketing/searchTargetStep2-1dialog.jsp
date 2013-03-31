<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.TargetInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
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
	form1.action="targetAction.rptdo?optype=searchTargetStep2-1dialog&doType=search";
	form1.submit();
}


<%
List<TargetInfo> list =(List<TargetInfo>) request.getAttribute("targetList");
//获得类型下拉框数据
String typeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_TARGET_TYPE'";
ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
	 if(null==qryStruct.target_name){
		qryStruct.target_name="";
	  }
%>
</script>
</head>
<body>
<form action="" method="post" name="form1">

<!-- 弹出模式窗口 -->
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
        <tr>
          <td width="15%" class="validatebox-tabTD-left">指标名称：</td>
          <td width="25%" class="validatebox-tabTD-right">
          <input type="hidden" value="2" name="qry__target_state">
          <input id=qry__target_name name=qry__target_name  value="<%=qryStruct.target_name %>" class="txtinput"  style="width:150px"/>
           </td>
           <td width="15%" class="validatebox-tabTD-left">指标类型：</td>
          <td width="25%" class="validatebox-tabTD-right">
          <BIBM:TagSelectList focusID="<%=qryStruct.target_type%>" script="class='easyui-combobox'"	listName="qry__target_type" listID="0" allFlag="" selfSQL="<%=typeSql%>" />
           </td>
          <td width="20%" class="validatebox-tabTD-left" align="center"><input type="button" class="btn4"  value="查找" onclick="doSearch()"></td>
        </tr>
         <tr>
          <td colspan="5" class="validatebox-tabTD-right">
<!-- 查询类表 开始-->
<div class="list_content">
      <table>
        <tr>
          <th width="10%" align="center">选择添加</th>
          <th width="15%" align="center">指标名称</th>
          <th width="10%" align="center">指标类型</th>
          <th width="30%" align="center">指标内容</th>
          <th width="10%" align="center">创建人</th>
          <th width="10%" align="center">审批状态</th>
          <th width="15%" align="center">创建时间</th>
        </tr>
        <% if(null!=list){
        		for(int i = 0 ;i<list.size();i++){
        			if(i%2!=0){
        				%>
        				<tr class="jg">
        				<%
        			}else{
        			%>
        			 <tr><%} %>
          <td align="center"><label>

           <input type="checkbox" id="checkbox_<%=list.get(i).getTargetId() %>" name=kexuan value="<%=list.get(i).getTargetId() %>"></label></td>
          <td align="center"><%=list.get(i).getTargetName() %></td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_TARGET_TYPE",String.valueOf(list.get(i).getTargetType()))  %></td>
           <td align="left"><%=list.get(i).getContent()  %></td>
          <td align="center"> <div align="center"><%=list.get(i).getCreator() %></div></td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_APPROVAL_TYPE",String.valueOf(list.get(i).getState()))  %></td>
          <td align="center"><%=CommonFormate.dateFormate(list.get(i).getCreateDate()) %></td>
        </tr>
        			<%
        		}
        	}%>
      </table>
      <br>
<!-- 查询类表 结束-->
          </td>
        </tr>
</table>
<!-- 模式是窗口结束 -->
</form>
</body>
</html>