<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.ActivityTypeInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="com.ailk.bi.marketing.entity.NameListInfo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html>
<html>
<head>
<title>北京联通统一经营分析系统</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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

    <style type="text/css">
    <!--
    .STYLE1 {font-size: 12px}
    -->
    </style>
    <script language="javascript">
    function checkNum(obj){
    	if(obj.value==""){
    		obj.value="0";
    	}
    	if(isNaN(obj.value)){
    		alert("您输入的不是数字类型");
    		obj.value="0";
    	}
    }
	function mySave(op) {
		var activityTypeName = form1.activityTypeName.value;
		var setTime = form1.setTime.value;
		if(null==activityTypeName||""==activityTypeName){
			alert("请填写活动类型名称");
			return false;
		}
		if(null==setTime||""==setTime){
			alert("请设置时间");
			return false;
		}
		form1.action="activityTypeAction.rptdo?optype=activityTyleList&doType="+op;
		form1.submit();
	}
	function myCancle(op) {
		form1.action="activityTypeAction.rptdo?optype=activityTyleList&doType="+op;
		form1.submit();
	}
	</script>
	<%
	 String imgUrl =request.getContextPath()+"/images/validatebox_warning.gif";
	    ActivityTypeInfo activityTypeInfo = (ActivityTypeInfo) session.getAttribute("activityTypeInfo");
		Date date = new Date();
		SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
		String activityTypeName = "";
		String contactCount = "";
		String setTime =sdf.format(date);
		String typeDesc = "";
		if(null != activityTypeInfo){
			if (!StringTool.checkEmptyString(activityTypeInfo.getActivityTypeName())) {
				activityTypeName = activityTypeInfo.getActivityTypeName();
			}
			if (!StringTool.checkEmptyString(Integer.toString(activityTypeInfo.getContactCount()))) {
				contactCount = Integer.toString(activityTypeInfo.getContactCount());
			}
			if (null != activityTypeInfo.getSetTime()) {
				setTime = CommonFormate.dateFormate(activityTypeInfo.getSetTime());
			}
			if(null!=activityTypeInfo.getTypeDesc()){
				typeDesc = activityTypeInfo.getTypeDesc();
			}
		}
	%>
</head>
<body style="background-color:#f9f9f9" >
<form name="form1" method="post" action="">
<div id="maincontent">
    <div class="toptag"> 您所在位置：营销策划 >>营销渠道>><em class="red">新增活动类型信息</em> <span class="hbt"><a href="javascript:;"
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span><span class="hbt1">

    </span></div>
    <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
<div class="validatebox-tabTD-title">活动类型信息录入</div>
  <table  width="100%" border="1" cellpadding="4" cellspacing="4" bordercolor="#CCCCCC">
    <tr>
      <td align="left" width="12%" class="validatebox-tabTD-left"><span class="STYLE1"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />活动类型名称：</span>      </td>
      <td align="left" width="88%" class="validatebox-tabTD-right"> <input name="activityTypeName" class="txtinput "  style="width:130px" value="<%=activityTypeName %>"/></td>
    </tr>
	 <tr>
      <td align="left"  class="validatebox-tabTD-left"><span class="STYLE1"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />最大接触次数：</span>      </td>
      <td align="left"class="validatebox-tabTD-right"><input  onblur="checkNum(this)" name="contactCount" class="txtinput "  style="width:130px" value="<%=contactCount %>"/></td>
    </tr>
	 <tr>
      <td align="left"class="validatebox-tabTD-left"><span class="STYLE1"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />接触时间设置：</span></td>
      <td align="left"  class="validatebox-tabTD-right"><input name="setTime" class="Wdate"  style="width:130px" value="<%=setTime %>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/></td>
    </tr>
    <tr>
      <td  height="44" align="left" class="validatebox-tabTD-left"><span class="STYLE1"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />详细描述说明：</span></td>
      <td align="left" class="validatebox-tabTD-right"><label>
        <textarea name="typeDesc" cols="35" rows="8" ><%=typeDesc %></textarea>
      </label></td>
    </tr>
    <tr>
      <td colspan="2" align="left"><div align="center">

      </div></td>
    </tr>
  </table>
<div class="buttonArea">
    <button type="button" class="btn4" onClick="mySave('save')"> 确 定 </button>  
      <button type="button" class="btn4" onClick="myCancle('search')"> 取 消 </button>
</div>
  </div>
</form>
</body>
</html>
