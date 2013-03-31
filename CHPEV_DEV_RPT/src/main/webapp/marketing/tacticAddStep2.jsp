<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.TargetInfo"%>
<%@page import="com.ailk.bi.marketing.entity.TargetOpInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="java.util.List"%>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/date/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/icon.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/icontent.css">
<script language="javascript">
<%
	ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
	 if(null==qryStruct.target_name){
		qryStruct.target_name="";
	  }
	List<TargetOpInfo> list1  =	(List<TargetOpInfo>)session.getAttribute("TargetOpInfoLis1");
    String typeSql = "select TARGET_TYPE_ID   ,  TARGET_TYPE_NAME from MK_PL_TARGET_TYPE";
    String	opTypeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_OP_TYPE'";
	String imgUrl =request.getContextPath()+"/images/validatebox_warning.gif";
%>
function myOpen(){
		tt.form1.action="targetAction.rptdo?optype=searchTargetStep2-1dialog&doType=search";
		tt.form1.submit();
		javascript:$('#d2').dialog('open');
}
function add(){
	var ids="";
	var arr  = new Array();
	arr = tt.document.getElementsByName("kexuan");
	for(var i =0;i< arr.length;i++ ){
		if(arr[i].checked){
			ids+=arr[i].value+",";
		}
	}
	if(','==ids.charAt(ids.length-1)){
		ids = ids.substring(0, ids.length-1)
	}
	form1.action="tacticAction.rptdo?optype=tacticAddStep2&doType=add&step=step2"+"&ids="+ids;
	form1.submit();
}

function myNext(){
	form1.action="tacticAction.rptdo?optype=tacticAddStep3&doType=add&step=step2";
	form1.submit();
}
function myUp(){
	form1.action="tacticAction.rptdo?optype=tacticAddStep1&doType=add&step=step2";
	form1.submit();
}
function myDelete(){
	var arr = document.getElementsByName("yixuan");
	var checlCount = 0;
	for(var i =0;i< arr.length;i++ ){
		if(arr[i].checked){
			checlCount=checlCount+1;
		}
	}
	if(checlCount>0){
		form1.action="tacticAction.rptdo?optype=tacticAddStep2&doType=add&step=step2Delete";
		form1.submit();
	}else{
		alert("请在已选指标栏中选择要删除的指标");
	}
}
function cancel() {
	form1.action="tacticAction.rptdo?optype=tacticList&doType=search";
	form1.submit();
}
function checkNum(obj){
	if(isNaN(obj.value)){
		alert("您输入的不是数字类型");
		obj.value="0";
	}
}
</script>
</head>
<body style="background-color:#f9f9f9" onload="javascript:$('#d2').dialog('close')">
<jsp:include page="processbar.jsp"></jsp:include>
<form name="form1" method="post" action="" >
  <div id="maincontent">
    <div class="toptag"> 您所在位置：营销策划 >>套餐管理>><em class="red">新增套餐信息</em> <span class="hbt"><a href="javascript:;"
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span><span class="hbt1">

    </span></div>

  <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
<div class="validatebox-tabTD-title">营销策略信息维护:当前是第二步(营销指标配置)/共四步</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
         <tr>
          <td colspan="3" class="validatebox-tabTD-right">
<!-- 营销规则 -->
<div class="list_content">
          <table >
            <tr width="100%">
              <th width="10%" align="center"> 选择删除</th>
              <th width="20%" align="center"> 指标类型</th>
              <th width="20%" align="center"> 指标名称 </th>
              <th width="20%" align="center"> 操作符 </th>
              <th width="20%" align="center"> 指标值 </th>
              <th width="10%" align="center"> 单位 </th>
              </tr>
               <% if(null!=list1){
        		for(int i = 0 ;i<list1.size();i++){
        			if(i%2!=0){
        				%>
        				<tr class="jg">
        				<%
        			}else{
        			%>
        			 <tr><%} %>
          <td align="center">
          <label> <input type="checkbox" id="yixuan" name="yixuan" value="<%=i %>"></label></td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_TARGET_TYPE",String.valueOf(list1.get(i).getTargetInfo().getTargetType()))  %></td>
          <td align="center"><%=list1.get(i).getTargetInfo().getTargetName() %></td>
          <td align="center"><BIBM:TagSelectList focusID="<%=String.valueOf(list1.get(i).getOpType()) %>"	listName="txt_opType" listID="0"  selfSQL="<%=opTypeSql%>" /></td>
          <td align="center"><input onblur="checkNum(this)" value="<%=list1.get(i).getOpValue() %>"  class="easyui-validatebox" required="true"  id="txt_opValue" name="txt_opValue"></td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_UNIT_TYPE",String.valueOf(list1.get(i).getTargetInfo().getUnit()))  %></td>

        </tr>
        			<%
        		}
        	}%>
            <tr class="jg">
              <td align="center"><label></label></td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              </tr>
          </table>
        </div>
 <br>
  <input type="button" class="public-btn2" onclick="myOpen()" value="添加营销指标"/>
  <input type="button" class="public-btn2" onclick="myDelete()" value="删除营销指标" />
<!-- 营销规则结束 -->
          </td>
        </tr>
</table>

<div class="buttonArea">
	<button class="btn4" type="button" onClick="myUp()"> 上 一 步  </button>
    <button class="btn4" type="button" onClick="myNext()"> 下 一 步  </button>
    <button class="btn4" type="button" onClick="cancel()"> 取 消 </button>
    <br>
</div>
</form>
<!-- 策略弹出模式窗口 -->
<div id="d2" class="easyui-dialog"  title="运营脚本选择--短信模板选择窗口" style="width:800px;height:500px;left:0px;top:0px;padding:4px;"
			toolbar="#dlg-toolbar" buttons="#dlg-buttons" resizable="true">
<iframe id="tt" scrolling="0"width="100%" height="100%"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="targetAction.rptdo?optype=searchTargetStep2-1dialog&doType=search"></iframe><br>
</div>
	<div id="dlg-buttons">
		<a href="#" class="public-btn1" onClick="add()"><span>选择</span></a>
		<a href="#" class="public-btn1" onClick="javascript:$('#d2').dialog('close')"><span>关闭</span></a></div>
<!-- 策略模式是窗口结束 -->
</body>
</html>
