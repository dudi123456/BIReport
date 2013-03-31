<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.workplatform.entity.TransInfo"%>
<%@page import="com.ailk.bi.marketing.entity.TacticInfo"%>
<%@page import="com.ailk.bi.marketing.entity.ChannleInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/date/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/icon.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/icontent.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/pubilicButton.css" />
<script language="javascript">

	function  isTrueValue()
	{
		var nameText = document.getElementById("txt_transName").value;
		var contentText = document.getElementById("txt_content").value;

			if(null==nameText||""==nameText){
				alert("请填写派单名称");
				return false;
			}
			else if(null==contentText||""==contentText){
				alert("请输入申请理由");
				return false;
			}else {
				return true;
			}
		}

	function mySave() {
		if(isTrueValue()){
			form1.action="transAction.rptdo?optype=transList&doType=saveTrans&transState=-1";
			form1.submit();
		}

	}
	function mySubmit(op) {
		window.close();
	}
	 function opWindow()
	 {
		 var url = "htmlAction.rptdo?doType=searchTactic&optype=searchTacticdialog";
		 	tt.form1.action=url;
		 	tt.form1.submit();
		 javascript:$('#d2').dialog('open');
	 }

	 function myLoad()
	 {
	 	javascript:$('#d2').dialog('close');
	 }

	 var tacticIdName;
	 var saveTacticId;
	 var showTacticNam;
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
						tacticIdName=arr[i].value;
					}
				}
				var arr = tacticIdName.split("@@@");
				saveTacticId = arr[0];
				showTacticNam = arr[1];
				form1.document.getElementById("txt_tacticName").value=showTacticNam;
				form1.document.getElementById("txt_tacticId").value=saveTacticId;
				javascript:$('#d2').dialog('close');
			}
	 }

	 function myOpenOrder(){
	     form1.action = "orderInfoAction.rptdo?optype=orderList&doType=search&txt_contact2=0&outChannle=0&setType=5&fromUpdate=true";
		 form1.submit();
	}
	</script>
	<%
			String imgUrl =request.getContextPath()+"/images/validatebox_warning.gif";
			String channleSql = "select t.channle_id,t.channle_name from  MK_PL_CHANNLE_INFO t where state =1";
			String clientSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CLIENT_TYPE'";
			String selectCss = "class='easyui-combobox' style='width:180px;'";
			TransInfo transInfo = (TransInfo)session.getAttribute("transInfo");
			String name="";
			String type="";
			String channle="";
			String content="";
			if(null!=transInfo){
				type = String.valueOf(transInfo.getCustType());
				if(transInfo.getTransName()!=null){
					name = transInfo.getTransName();
				}
				ChannleInfo CInfo = transInfo.getNewChannle() ;
				if (null!=CInfo) {
					channle=String.valueOf(CInfo.getChannleId());
				}
				if(transInfo.getTransUse()!=null){
					content = transInfo.getTransUse();
				}
			}
	%>
</head>
<body style="background-color:#f9f9f9" onload="myLoad()">
<form action="" method="post" name="form1">
<input type="hidden" id ="showType" name ="showType" value="1"/>
<input type="hidden" id="txt_tacticId" name="txt_tacticId" >
  <div id="maincontent">
    <div class="toptag"> 您所在位置：营销策划 >>套餐管理>><em class="red">新增套餐信息</em> <span class="hbt"><a href="javascript:;"
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span><span class="hbt1">

    </span></div>

  <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
<div class="validatebox-tabTD-title">渠道转派基本信息</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
        <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />转派名称：</td>
          <td width="21%" class="validatebox-tabTD-right"><input value="<%=name %>" class="easyui-validatebox" required="true" validType="length[1,20]" id="txt_transName" name="txt_transName"></td>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />客户类型：</td>
          <td class="validatebox-tabTD-right"> <BIBM:TagSelectList focusID="<%=type %>" script="<%=selectCss%>"	listName="txt_custType" listID="0"  selfSQL="<%=clientSql%>" /></td>
        </tr>
         <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />转派渠道：</td>
          <td width="21%" class="validatebox-tabTD-right">
          <BIBM:TagSelectList focusID="<%=channle %>" script="<%=selectCss%>"	listName="txt_channleId" listID="0"  selfSQL="<%=channleSql%>" />
          </td>
          <td width="12%" class="validatebox-tabTD-left"></td>
          <td class="validatebox-tabTD-right">
          </td>
        </tr>

         <tr>
          <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />申请理由：</td>
          <td colspan="3" class="validatebox-tabTD-right"><textarea  class="easyui-validatebox" required="true" style="height:100px;width: 400px" id="txt_content" name="txt_content"><%=content %></textarea>
          </td>
        </tr>
          <td colspan="4" class="validatebox-tabTD-right">
          <div class="validatebox-tabTD-title">包含工单信息</div>
<iframe id="ff" scrolling="0"width="100%" height="200px"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="<%=request.getContextPath()%>/workplatform/orderList2.jsp"></iframe><br>

          </td>
        </tr>
</table>

<div class="buttonArea">
    <button class="btn4" type="button" onClick="myOpenOrder()"> 添加工单 </button>
    <button class="btn4" type="button" onClick="mySave()"> 保 存 </button>
    <button class="btn4" type="button" onClick="mySubmit()"> 取 消 </button>
</div>

</form>
</body>
</html>
