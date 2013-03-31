<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.TargetInfo"%>
<%@page import="com.ailk.bi.marketing.entity.ContactPlyInfo"%>
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
	List<ContactPlyInfo> list2 =(List<ContactPlyInfo>) session.getAttribute("ContactPlyInfoList");
    String opModeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CONTAC_MODE'";
    String opTimeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CONTAC_TIME'";
	String imgUrl =request.getContextPath()+"/images/validatebox_warning.gif";
%>
function cancel() {
	form1.action="tacticAction.rptdo?optype=tacticList&doType=search";
	form1.submit();
}
function add(){
	form1.action="tacticAction.rptdo?optype=tacticAddStep3&doType=add&step=step3&addOne=one";
	form1.submit();
}
function myUp(){
	form1.action="tacticAddStep2.jsp?";
	form1.submit();
}
function myNext(){
	form1.action="tacticAddStep4.jsp?";
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
		form1.action="tacticAction.rptdo?optype=tacticAddStep3&doType=add&step=step3Delete";
		form1.submit();
	}else{
		alert("请在已选指标栏中选择要删除的指标");
	}
}
var scriptID;
var hiddenID;
var opWindowID;
var ModeID;
var ModeName;
function mySelect(SearchType){
	scriptID="";
	hiddenID="";
	ModeID="";
	ModeName="";
		var arr = document.getElementsByName("yixuan");
		var checlCount = 0;
		for(var i =0;i< arr.length;i++ ){
			if(arr[i].checked){
				scriptID=i;
				hiddenID=i;
				ModeID=i;
				ModeName=i;
				checlCount=checlCount+1;
			}
		}
		if(checlCount>1){
			alert("每次只能选择一条接触规则");
		}else if(checlCount<1){
			alert("请选择一条接触规则");
		}else {
			scriptID = "scriptId" + scriptID;
			hiddenID = "hiddenId" + hiddenID;
			ModeID="modeId"+ModeID;
			ModeName="modeName"+ModeName;
			javascript:$('#d2').dialog('open');
		}

}

function  myCheck(oo)
{

	var arr  = new Array();
		if("d2"==oo){
			arr = tt.document.getElementsByName("checkbox");
		}else if("d3"==oo){
			arr = pp.document.getElementsByName("checkbox");
		}
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
			for(var i =0;i< arr.length;i++ ){
				if(arr[i].checked){
					tacticIdName=arr[i].value;
				}
			}
			var arr = tacticIdName.split("@@@");
			saveTacticId = arr[0];
			showTacticNam = arr[1];
			if("d2"==oo){
				form1.document.getElementById(scriptID).innerHTML=saveTacticId;
				form1.document.getElementById(hiddenID).value=saveTacticId;
				var modeID = tt.document.getElementById("ModeID").value;
				var modeArr=modeID.split("@@@");
				form1.document.getElementById(ModeName).innerHTML=modeArr[1];
				form1.document.getElementById(ModeID).value=modeArr[0];
				//alert(showTacticNam+";"+saveTacticId);
				javascript:$('#d2').dialog('close');
				form1.action="tacticAction.rptdo?optype=tacticAddStep3&doType=add&step=step3";
				form1.submit();
			}

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
<div class="validatebox-tabTD-title">营销策略信息维护:当前是第三步(接触规则配置)/共四步</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
         <tr>
          <td colspan="3" class="validatebox-tabTD-right">
<!-- 营销规则 -->
<div class="list_content">
          <table >
            <tr width="100%">
              <th width="10%" align="center"> 选         择</th>
              <th width="15%" align="center"> 步         骤</th>
              <th width="15%" align="center"> 建议接触时间 </th>
              <th width="20%" align="center"> 接触天数限制(天) </th>
              <th width="20%" align="center"> 运营脚本ID </th>
                <th width="20%" align="center"> 接触方式 </th>
              </tr>
 <% if(null!=list2){
        		for(int i = 0 ;i<list2.size();i++){
        			if(i%2!=0){
        				%>
        				<tr class="jg">
        				<%
        			}else{
        			%>
        			 <tr><%} %>
           <td align="center">
           <label> <input type="checkbox" id="yixuan" name="yixuan" value="<%=i %>"></label></td>
           <td align="center">第<%=i+1 %>步</td>
           <td align="center"><BIBM:TagSelectList  focusID="<%=String.valueOf(list2.get(i).getContactTime()) %>"	listName="txt_opDate" listID="0"  selfSQL="<%=opTimeSql%>" /></td>
           <td align="center"><input value="<%=String.valueOf(list2.get(i).getContactDay()) %>"   class="easyui-validatebox"  required="true"  id="txt_opDay" name="txt_opDay"></td>
           <td align="center"> <input type="hidden" name="hiddenId" id="hiddenId<%=i%>" value="<%=list2.get(i).getScriptId() %>">ID:<div style="display:inline;" id="scriptId<%=i %>"><%=list2.get(i).getScriptId() %></div></td>
    	   <td align="center">
    	   <input type="hidden" name="modeId" id="modeId<%=i%>" value="<%=list2.get(i).getMode()%>">
    	   <div style="display:inline;" id="modeName<%=i %>"> <%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_CONTAC_MODE",String.valueOf(list2.get(i).getMode()))  %></div>
    	  </td>
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
          <input type="button" class="public-btn2" onclick="add()" value="添加接触规则"/>
            <input type="button" class="public-btn2" onclick="myDelete()" value="删除接触规则" />
             <input type="button" class="public-btn2" onclick="mySelect()" value="选择运营脚本"/>
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

<iframe id="tt" scrolling="0"width="100%" height="100%"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="msgAction.rptdo?optype=searchContactStep3-1dialog&doType=search"></iframe><br>
</div>
	<div id="dlg-buttons">
		<a href="#" class="public-btn1" onClick="javascript:myCheck('d2')"><span>选择</span></a>
		<a href="#" class="public-btn1" onClick="javascript:$('#d2').dialog('close')"><span>关闭</span></a></div>
<!-- 策略模式是窗口结束 -->
</body>
</html>
