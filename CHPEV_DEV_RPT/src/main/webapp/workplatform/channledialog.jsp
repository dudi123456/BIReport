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
<%@page import="com.ailk.bi.system.facade.impl.CommonFacade"%>
<%@page import="com.ailk.bi.base.table.InfoOperTable"%>
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
InfoOperTable loginUser = CommonFacade.getLoginUser(session);
String channleSql = "select t.channle_id,t.channle_name from  MK_PL_CHANNLE_INFO t where state =1";
String clientSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CLIENT_TYPE'";
String	userSql = "SELECT t.user_id,t.user_name  from ui_info_user t where t.user_id <> '"+loginUser.user_id+"'";
%>
</script>
</head>
<body>
<form action="" method="post" name="form1">
<!-- 弹出模式窗口 -->
<input type="hidden" id="qry__activity_state" name="qry__activity_state" value="2">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
        <tr>
          <td width="15%" class="validatebox-tabTD-left">转派名称：</td>
          <td width="30%" class="validatebox-tabTD-right"><input value="" id="txt_transName"></td>
          <td width="15%" class="validatebox-tabTD-left">客户类型：</td>
          <td width="30%" class="validatebox-tabTD-right">
          <BIBM:TagSelectList  focusID="" 	listName="txt_custType" listID="0" selfSQL="<%=clientSql%>" />
          </td>
        </tr>
 <tr>
          <td width="15%" class="validatebox-tabTD-left">转派渠道：</td>
          <td width="30%" class="validatebox-tabTD-right">
 <BIBM:TagSelectList  focusID="" 	listName="txt_channleId" listID="0" selfSQL="<%=channleSql%>" />
</td>
          <td width="15%" class="validatebox-tabTD-left"></td>
          <td width="30%" class="validatebox-tabTD-right">
          </td>
        </tr>
<tr>
          <td width="15%" class="validatebox-tabTD-left">申请理由：</td>
          <td width="75%" colspan="3" class="validatebox-tabTD-right">
<textarea  required="true" style="height:80px;width:400px;t" id="txt_content" name="txt_content"></textarea>
          </td>
        </tr>
</table>
<!-- 模式是窗口结束 -->
</form>
</body>
</html>