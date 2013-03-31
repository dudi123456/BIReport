<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.PackageInfo"%>
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
    <script language="javascript">
    function  isTrueValue()
	{
		var packageName = document.getElementById("packageName").value;
		var parentPackageName = document.getElementById("parentPackageName").value;
		var freeProject = document.getElementById("freeProject").value;
		var effectDate = document.getElementById("effectDate").value;
		var invalidDate = document.getElementById("invalidDate").value;
		if(null==packageName||""==packageName){
			alert("请填写套餐名称");
			return false;
		}else if(null==parentPackageName||""==parentPackageName){
			alert("请选择上级套餐名称");
			return false;
		}else if(null==freeProject||""==freeProject){
			alert("请填写资费方案");
			return false;
		}else if(null==effectDate||""==effectDate){
			alert("请选择政策生效时间");
			return false;
		}
		else if(null==invalidDate||""==invalidDate){
			alert("请选择套餐失效期限");
			return false;
		}else {
			return true;
		}

	}
	function mySave(op) {
		//if(isTrueValue()){
		    form1.action="packageManaAction.rptdo?optype=packageList&doType="+op;
		    form1.submit();
		//}
	}
	function mySubmit(op) {
		form1.action="packageManaAction.rptdo?optype=packageList&doType="+op;
		form1.submit();
	}
	function opWindow()
	 {
         tt.form1.action = "packageManaAction.rptdo?doType=search&optype=searchPackagedialog";
         tt.form1.submit();
		 javascript:$('#d2').dialog('open');
		 //window.showModelessDialog("marketing/searchTacticdialog.jsp","","dialogWidth:800px;dialogHeight:300px;scroll:no;status:no;edge:sunken")
	 }

	 function myLoad()
	 {
	 	javascript:$('#d2').dialog('close');
	 }

	 var packageIdName;
	 var savePackageId;
	 var showPackageNam;
	 function  myCheck()
	 {
			var arr = tt.document.getElementsByName("checkbox");
			var checlCount = 0;
			for(var i =0;i< arr.length;i++ ){
				if(arr[i].checked){
					checlCount=checlCount+1;
				}
			}
			if(checlCount==0){
				alert("你没有选择任何的信息！");
			}else if(checlCount>1)	{
				alert("每次只能选择一条信息！");
			}else{
				arr = tt.document.getElementsByName("checkbox");
				for(var i =0;i< arr.length;i++ ){
					if(arr[i].checked){
						packageIdName=arr[i].value;
						break;
					}
				}
				var arr = packageIdName.split("@@@");
				savePackageId = arr[0];
				showPackageNam = arr[1];
				form1.document.getElementById("parentPackageName").value=showPackageNam;
				form1.document.getElementById("parentPackageId").value=savePackageId;
				javascript:$('#d2').dialog('close');

			}
	    }
	</script>
	<%
	   String imgUrl =request.getContextPath()+"/images/validatebox_warning.gif";
	SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
	Date date = new Date();
	PackageInfo packageInfo = (PackageInfo) session.getAttribute("packageInfo");
	PackageInfo parentPackageInfo = (PackageInfo) session.getAttribute("parentPackageInfo");
		String packageName = "";
		String parentPackageName = "";
		String packageType = "";
		String parentPackageId = "";
		String productType = "";
		String makePriceMode = "";
		String languageType = "";
		String clientGroup = "";

		String freeProject = "";
		String effectDate = sdf.format(date);
		String invalidDate = sdf.format(date);
		String packgeDesc = "";
		String channelId = "";
		if(null!=packageInfo){
		if (!StringTool.checkEmptyString(packageInfo.getPackageName())) {
			packageName=packageInfo.getPackageName();
		}
		//PackageInfo parentPackage = packageInfo.getParentPackage();
		//if(null != parentPackage){
		parentPackageId=String.valueOf(packageInfo.getParentPackageId());
		if(parentPackageInfo == null){
			parentPackageName = "";
		}else{
			parentPackageName = parentPackageInfo.getPackageName();
		}

		//}

		packageType = String.valueOf(packageInfo.getPackageType());
		productType = String.valueOf(packageInfo.getProductType());
		makePriceMode = String.valueOf(packageInfo.getPriceMode());
		languageType = String.valueOf(packageInfo.getVoiceType());
		clientGroup = String.valueOf(packageInfo.getCustGroup());
		if (null != packageInfo.getFreeProject()) {
			freeProject = String.valueOf(packageInfo.getFreeProject());
		}

		ChannleInfo channleInfo = packageInfo.getChannleInfo();
		if (null != packageInfo.getEffectDate()) {
			effectDate=CommonFormate.dateFormate(packageInfo.getEffectDate());
		}
		if (null != packageInfo.getInvaildDate()) {
			invalidDate=CommonFormate.dateFormate(packageInfo.getInvaildDate());
		}
		if (!StringTool.checkEmptyString(packageInfo.getPackageContent())) {
			packgeDesc=packageInfo.getPackageContent();
		}
		if (null!=channleInfo) {

			channelId=String.valueOf(channleInfo.getChannleId());
		}

	}
		//获得套餐类型数据
		String packageTypeSql ="select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_PACKAGE_TYPE'";
		//获得产品类别数据
		String productTypeSql ="select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_PRODUCT_TYPE'";
		//获定价模式数据
		String priceModeSql ="select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_MAKEPRICE_MODE'";
		//获得语言类别数据
		String languageTypeSql ="select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_LANGUAGE_TYPE'";
		//获得客户群数据
		String custGroupSql ="select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CLIENT_GROUP'";
		//获得渠道
		String channleSql ="select T.CHANNLE_ID,T.CHANNLE_NAME from MK_PL_CHANNLE_INFO T";
	%>
</head>
<body style="background-color:#f9f9f9" onload="myLoad()">
<form name="form1" method="post" action="">
<input type="hidden" id="parentPackageId" name="parentPackageId" value="<%=parentPackageId%>">
  <div id="maincontent">
    <div class="toptag"> 您所在位置：营销策划 >>套餐管理>><em class="red">新增套餐信息</em> <span class="hbt"><a href="javascript:;"
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span><span class="hbt1">

    </span></div>
    <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
<div class="validatebox-tabTD-title">套餐管理信息录入</div>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
      <tr>
        <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />套餐名称： </td>
        <td width="21%" class="validatebox-tabTD-right"><input name="packageName" id="packageName" class="txtinput"  style="width:200px"  value="<%=packageName %>"/>        </td>
        <td width="12%" class="validatebox-tabTD-left"> <img src="<%=imgUrl %>"  width="16" height="16" border="0" />套餐类型： </td>
        <td class="validatebox-tabTD-right"><BIBM:TagSelectList focusID="<%=packageType%>" script="class='easyui-combobox'"	listName="packageType" listID="0" selfSQL="<%=packageTypeSql%>" /></td>
      </tr>
      <tr>
        <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />上级套餐名称： </td>
        <td width="21%" class="validatebox-tabTD-right"><input value="<%=parentPackageName %>"  required="true" validType="url" id="parentPackageName" name="parentPackageName">
        <input type="button" class="public-btn2" value="选择" onclick="opWindow()" /></td>
        <td width="12%" class="validatebox-tabTD-left"> <img src="<%=imgUrl %>"  width="16" height="16" border="0" />产品类别： </td>
        <td class="validatebox-tabTD-right"><BIBM:TagSelectList focusID="<%=productType%>" script="class='easyui-combobox'"	listName="productType" listID="0" selfSQL="<%=productTypeSql%>" /></td>
      </tr>
      <tr>
        <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />定价模板：</td>
        <td width="21%" class="validatebox-tabTD-right"><BIBM:TagSelectList focusID="<%=makePriceMode%>" script="class='easyui-combobox'"	listName="makePriceMode" listID="0" selfSQL="<%=priceModeSql%>" /></td>
        <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />语言类型：</td>
        <td class="validatebox-tabTD-right"><BIBM:TagSelectList focusID="<%=languageType%>" script="class='easyui-combobox'"	listName="languageType" listID="0" selfSQL="<%=languageTypeSql%>" /></td>
      </tr>
	    <tr>
        <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />客户群：</td>
        <td width="21%" class="validatebox-tabTD-right"><BIBM:TagSelectList focusID="<%=clientGroup%>" script="class='easyui-combobox'"	listName="clientGroup" listID="0" selfSQL="<%=custGroupSql%>" /></td>
        <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />渠道：</td>
        <td class="validatebox-tabTD-right"><BIBM:TagSelectList focusID="<%=channelId %>" script="class='easyui-combobox'"		listName="channelId" listID="0"  selfSQL="<%=channleSql%>" />
        </td>
      </tr>
      <tr>
        <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />资费方案：</td>
        <td colspan="3" class="validatebox-tabTD-right">
        <input name="freeProject" id="freeProject" class="txtinput"  style="width:560px" value="<%=freeProject %>"/>
        </td>
      </tr>
	   <tr>
        <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />政策生效时间：</td>
        <td width="21%" class="validatebox-tabTD-right"><input name="effectDate" id="effectDate" value="<%=effectDate %>" type="text"  class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/></td>
        <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />套餐失效期限：</td>
        <td class="validatebox-tabTD-right"><input name="invalidDate" id="invalidDate" value="<%=invalidDate %>" type="text"  class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/></td>
      </tr>

      <tr>
        <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>"  width="16" height="16" border="0" />套餐描述：</td>
        <td class="validatebox-tabTD-right" colspan="3"><label>
          <textarea name="packgeDesc" id="packgeDesc" cols="79" rows="5"><%=packgeDesc %></textarea>
          </label></td>
      </tr>
    </table>
   <div class="buttonArea">
  <button class="btn4"  onClick="mySave('save')">添 加</button>   
        <button class="btn4" onClick="mySubmit('search')">取 消</button></div>
  </div>

  <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
  </script>
<!-- 弹出模式窗口 -->
<div id="d2" class="easyui-dialog"  title="对话窗口" style="width:800px;height:300px;left:0px;top:0px;padding:4px;"
			toolbar="#dlg-toolbar" buttons="#dlg-buttons" resizable="true">
        <iframe id="tt" scrolling="0"width="100%" height="100%"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="searchParentPackageDialog.jsp"></iframe><br>
</div>
	<div id="dlg-buttons">
		<a href="#" class="public-btn1" onClick="javascript:myCheck()"><span>选择</span></a>
		<a href="#" class="public-btn1" onClick="javascript:$('#d2').dialog('close')"><span>关闭</span></a></div>
<!-- 模式是窗口结束 -->

</form>
</body>
</html>
