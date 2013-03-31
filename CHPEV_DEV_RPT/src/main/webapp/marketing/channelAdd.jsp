<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.ChannleInfo"%>
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
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/date/WdatePicker.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/pubilicButton.css" />
    <style type="text/css">
    <!--
        .STYLE1 {font-size: 12px}
    -->
    </style>
    <script language="javascript">
	function mySave(op) {
		form1.action="channelManaAction.rptdo?optype=channleList&doType="+op;
		form1.submit();
	}
	function mySubmit(op) {
		form1.action="channelManaAction.rptdo?optype=channleList&doType="+op;
		form1.submit();
	}
	</script>
	<%
	SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
	Date date = new Date();
	 String imgUrl =request.getContextPath()+"/images/validatebox_warning.gif";
	ChannleInfo channleInfo = (ChannleInfo) session.getAttribute("channleInfo");
		String channleName = "";
		String channleCode = "";
		String channleCreateDate = "";
		String effectDate = sdf.format(date);
		String invaildDate = sdf.format(date);
		String channleDesc = "";
		String channleTypeId = "";
		if(null!=channleInfo){
			if (!StringTool.checkEmptyString(channleInfo.getChannleName())) {
				channleName=channleInfo.getChannleName();
			}
			if (!StringTool.checkEmptyString(channleInfo.getChannleCode())) {
				channleCode=channleInfo.getChannleCode();
			}
			if (null != channleInfo.getCreateDate()) {
				channleCreateDate=CommonFormate.dateFormate(channleInfo.getCreateDate());
			}
			if (null != channleInfo.getEffectDate()) {
				effectDate=CommonFormate.dateFormate(channleInfo.getEffectDate());
			}
			if (null != channleInfo.getInvaildDate()) {
				invaildDate=CommonFormate.dateFormate(channleInfo.getInvaildDate());
			}
			if (!StringTool.checkEmptyString(channleInfo.getChannle_desc())) {
				channleDesc=channleInfo.getChannle_desc();
			}
			if(null!=channleInfo.getChannleType()){
				if (!StringTool.checkEmptyString(channleInfo.getChannleType().getChannleTypeName()))
				{
					channleTypeId=String.valueOf(channleInfo.getChannleType().getChannleTypeId());
				}
			}
		}
		//获得类型下拉框数据
		String typeSql = "select CHANNLE_TYPE_ID,CHANNLE_TYPE_NAME from MK_PL_CHANNLE_TYPE where state =1";
	%>
</head>
<body style="background-color:#f9f9f9" >
<form name="form1" method="post" action="">
<div id="maincontent">
    <div class="toptag"> 您所在位置：营销策划 >>营销渠道>><em class="red">新增渠道信息</em> <span class="hbt"><a href="javascript:;"
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span><span class="hbt1">

    </span></div>
    <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
<div class="validatebox-tabTD-title">渠道管理信息录入</div>
  <table  width="100%" border="1" cellpadding="4" cellspacing="4" bordercolor="#CCCCCC">
    <tr>
      <td align="left" width="12%" class="validatebox-tabTD-left"><span class="STYLE1"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />渠道类型：</span>      </td>
      <td align="left" width="21%" class="validatebox-tabTD-right"><span style="font-size: 12px">
      <BIBM:TagSelectList focusID="<%=channleTypeId%>" script="class='easyui-combobox'"	listName="channleTypeId" listID="0" selfSQL="<%=typeSql%>" />
      </td>
      <td align="left" width="12%" class="validatebox-tabTD-left"><span class="STYLE1"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />渠道名称：</span></td>
      <td align="left"  class="validatebox-tabTD-right"><input name="channleName" class="txtinput"  value="<%=channleName %>" style="width:130px"/></td>
    </tr>
	 <tr>
      <td align="left"  class="validatebox-tabTD-left"><span class="STYLE1"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />渠道编码：</span>      </td>
      <td align="left" class="validatebox-tabTD-right"><input name="channleCode" class="txtinput"  value="<%=channleCode %>" style="width:130px"/></td>
      <td align="left"  class="validatebox-tabTD-left"><span class="STYLE1"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />创建时间：</span></td>
      <td align="left" class="validatebox-tabTD-right"><input name="channleCreateDate" type="hidden" value="<%=channleCreateDate %>" style="width:130px"/><%=channleCreateDate %></td>
    </tr>
	 <tr>
      <td align="left" class="validatebox-tabTD-left"><span class="STYLE1"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />生效时间：</span>      </td>
      <td align="left"  class="validatebox-tabTD-right"><input name="effectDate"  class="Wdate" value="<%=effectDate %>" style="width:130px" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/></td>
      <td align="left" class="validatebox-tabTD-left"><span class="STYLE1"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />失效时间：</span></td>
      <td align="left"  class="validatebox-tabTD-right"><input name="invaildDate"  class="Wdate" value="<%=invaildDate %>" style="width:130px" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/></td>
    </tr>
    <tr>
      <td width="12%" height="44" align="left" class="validatebox-tabTD-left"><span class="STYLE1"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />详细描述说明</span></td>
      <td colspan="3" align="left" class="validatebox-tabTD-right"><label>
        <textarea name="channleDesc" cols="35" rows="8"><%=channleDesc %></textarea>
      </label></td>
    </tr>

  </table>
   <div class="buttonArea">
      <button class="btn4" type="button" onClick="mySave('save')"> 确 定 </button> &nbsp;
      <button class="btn4" type="button"  onClick="mySubmit('search')"> 取 消 </button>
      </div>
  <p>&nbsp;</p>

	 <p>&nbsp;</p>
</div>
</form>
</body>
</html>
