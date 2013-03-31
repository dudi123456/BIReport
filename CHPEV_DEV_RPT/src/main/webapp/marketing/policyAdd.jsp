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
<%@page import="com.ailk.bi.marketing.entity.PolicyInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="com.ailk.bi.marketing.entity.NameListInfo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/date/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/icon.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/icontent.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/pubilicButton.css" />
<script type="text/javascript">
<%
//获得类型下拉框数据
String typeSql ="select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_POLICY_TYPE'";
String addResult = (String)request.getAttribute("addResult");
PolicyInfo pinfo =(PolicyInfo) request.getAttribute("policy");
String type="";
String name="";
String content="";
Date dd = new Date();
String d1 = CommonFormate.dateFormate(dd);
String d2 = CommonFormate.dateFormate(dd);

if(null!=pinfo)
{
	if (!StringTool.checkEmptyString(pinfo.getPolicyName())) {
		name=pinfo.getPolicyName();
	}
	if (!StringTool.checkEmptyString(pinfo.getContent())) {
		content=pinfo.getContent();
	}
	type =String.valueOf(pinfo.getPolicyType()) ;
	d1 = CommonFormate.dateFormate(pinfo.getEffectDate());
	d2 = CommonFormate.dateFormate(pinfo.getInvaildDate());
}
if("Refresh".equals(addResult))
{
	%>
		parent.form1.action="policyAction.rptdo?optype=policyList&doType=search";
		parent.form1.submit();
<%}
%>

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
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
 <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="../images/validatebox_warning.gif" width="16" height="16" border="0" />政策名称：</td>
          <td width="38%" class="validatebox-tabTD-right"><input value="<%=name %>" id="txt__policy_name" name="txt__policy_name" class="easyui-validatebox" required="true" validType="length[1,3]"></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="../images/validatebox_warning.gif" width="16" height="16" border="0" />政策类型：</td>
          <td class="validatebox-tabTD-right">         	 <BIBM:TagSelectList focusID="<%=type %>" script="class='easyui-combobox'"	listName="txt__policy_type" listID="0"  selfSQL="<%=typeSql%>" /></td>
        </tr>

          <td width="12%" class="validatebox-tabTD-left"><img src="../images/validatebox_warning.gif" width="16" height="16" border="0" />生效时间：</td>
          <td width="38%" class="validatebox-tabTD-right"><input value="<%=d1 %>" id="txt_date01" name="txt_date01" type="text" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="../images/validatebox_warning.gif" width="16" height="16" border="0" />失效时间：</td>
          <td class="validatebox-tabTD-right"><input value="<%=d2 %>" id="txt_date02"  name="txt_date02" type="text" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
        </tr>
         <tr>
          <td class="validatebox-tabTD-left"><img src="../images/validatebox_warning.gif" width="16" height="16" border="0" />详细描述：</td>
          <td colspan="3" class="validatebox-tabTD-right"><textarea id="txt__policy_content" name="txt__policy_content" cols="45" rows="" required="true"><%=content %></textarea></td>
        </tr>

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