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
<%@page import="com.ailk.bi.marketing.entity.MessageInfo"%>
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
String typeSql ="select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_MSG_TYPE'";
String addResult = (String)request.getAttribute("addResult");
MessageInfo minfo =(MessageInfo) request.getAttribute("msgInfo");
String type="";
String content="";
if(null!=minfo)
{
	if (!StringTool.checkEmptyString(minfo.getContent())) {
		content=minfo.getContent();
	}
	type =String.valueOf(minfo.getMsgType()) ;
}
if("Refresh".equals(addResult))
{
	%>
		parent.form1.action="msgAction.rptdo?optype=msgList&doType=search";
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
          <td class="validatebox-tabTD-left">短信类型：</td>
          <td class="validatebox-tabTD-right"><label>
          <BIBM:TagSelectList focusID="<%=type %>" script="class='easyui-combobox'"	listName="txt__msg_type" listID="0"  selfSQL="<%=typeSql%>" /></label></td>

        </tr>
         <tr>
          <td class="validatebox-tabTD-left"><img src="../images/validatebox_warning.gif" width="16" height="16" border="0" />详细描述：</td>
          <td  class="validatebox-tabTD-right"><textarea id="txt__msg_content" name="txt__msg_content" cols="45" rows="" required="true"><%=content %></textarea></td>
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