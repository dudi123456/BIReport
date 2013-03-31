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
<%@page import="java.util.*"%>
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

	function mySave(op) {
		form1.action="targetAction.rptdo?optype=targetList&doType="+op;
		form1.submit();
	}
	function mySubmit(op) {
		form1.action="targetAction.rptdo?optype=targetList&doType="+op;
		form1.submit();
	}
	function myOpen(url){
	  parent.frames["bodyFrame"].window.location.href="add_Question.htm";
	}
	</script>
	<%
		TargetInfo targetInfo = (TargetInfo) session.getAttribute("targetInfo");
		String name="";
		String unit="";
		String type="";
		String creator="";
		String content="";
		if(null!=targetInfo){
			if (!StringTool.checkEmptyString(targetInfo.getTargetName())) {
				name=targetInfo.getTargetName();
			}
			if (!StringTool.checkEmptyString(targetInfo.getCreator())) {
				creator=targetInfo.getCreator();
			}
			if (!StringTool.checkEmptyString(targetInfo.getContent())) {
				content=targetInfo.getContent();
			}
			type=String.valueOf(targetInfo.getTargetType());
			unit=String.valueOf(targetInfo.getUnit());
		}
		//获得类型下拉框数据
		String typeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_TARGET_TYPE'";
		String unitSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_UNIT_TYPE'";
	%>
</head>
<body style="background-color:#f9f9f9">
<form name="form1" method="post" action="">
  <div id="maincontent">
    <div class="toptag"> 您所在位置：营销策划 >>套餐管理>><em class="red">新增套餐信息</em> <span class="hbt"><a href="javascript:;"
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span><span class="hbt1">

    </span></div>

  <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
<div class="validatebox-tabTD-title">营维指标信息录入</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
        <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />指标名称：</td>
          <td class="validatebox-tabTD-right"><input name="txt_targetName" value="<%=name %>"  class="easyui-validatebox" required="true" validType="length[1,3]" ></td>
        </tr>

        <tr>
          <td class="validatebox-tabTD-left">指标类型：</td>
          <td class="validatebox-tabTD-right"><label>
           <BIBM:TagSelectList  focusID="<%=type %>" script="class='easyui-combobox'"	listName="target_type" listID="0" selfSQL="<%=typeSql%>" />
           </label></td>
        </tr>

         <tr>
          <td class="validatebox-tabTD-left">单位：</td>
          <td class="validatebox-tabTD-right"><label>
           <BIBM:TagSelectList  focusID="<%=unit %>" script="class='easyui-combobox'"	listName="txt_nuit" listID="0" selfSQL="<%=unitSql%>" />
           </label></td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left">创建人:</td>
          <td class="validatebox-tabTD-right"><input name="txt_creator" readonly="readonly"   value="<%=creator %>" class="easyui-validatebox" required="true" validType="length[1,20]"></td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left">创建时间：</td>
          <td class="validatebox-tabTD-right"><input name="txt_create_date"  readonly="readonly"  value="<%=CommonFormate.dateFormate(targetInfo.getCreateDate()) %>" class="easyui-validatebox" required="true" validType="length[1,20]"></td>
        </tr>
         <tr>
          <td class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />详细描述：</td>
          <td class="validatebox-tabTD-right"><textarea name="txt_content" class="easyui-validatebox" required="true" style="height:100px;"><%=content %></textarea></td>
        </tr>

</table>
<div class="buttonArea">
    <button class="btn3" type="button" onClick="mySave('save')"> 保 存 </button>
    <button class="btn4" type="button" onClick="mySubmit('search')"> 取 消 </button>
</div>
</form>
</body>
</html>
