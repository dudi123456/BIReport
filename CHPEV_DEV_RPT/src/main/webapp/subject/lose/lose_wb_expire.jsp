<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import = "java.io.PrintWriter" %>
<%@ page import = "com.ailk.bi.base.util.*" %>
<%@ page import = "com.ailk.bi.common.dbtools.*" %>
<%@ page import = "com.ailk.bi.report.struct.ReportQryStruct" %>
<%@ page import="com.ailk.bi.common.app.AppException"%>  
<%@ page import="com.ailk.bi.base.common.*"%>  
<%@ page import="com.ailk.bi.common.app.DateUtil"%> 

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Content-Language" content="zh-cn">

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/newmain.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/titleStyle.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/Scroll.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dd99.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/chart.js" ></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/net.js"></SCRIPT>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/wait.js"></SCRIPT>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/XmlRPC.js"></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/dojo.js"></SCRIPT>


<title><%=CSysCode.SYS_TITLE%></title>


<%

ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
if(qryStruct == null){
	qryStruct = new ReportQryStruct();
}
%>


<BIBM:SelfRefreshTag pageNames="TableQryForm.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1"/>
<body>

</head>
<%
int screenx = Integer.parseInt(session.getAttribute(WebKeys.Screenx)==null?"1280":(String)session.getAttribute(WebKeys.Screenx));
int chartWidthTmp = (screenx-250)/2;

%>

<FORM name="TableQryForm" action="leaderVwSubject.rptdo">
<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">
<INPUT type=hidden name="tab_fee_flag" >

<div style="display:none;position:absolute;" id=altlayer></div>
<table style="width: 100%; margin-top: 0px;" cellspacing="0" cellpadding="0">
	<tr>
		<td  style="padding-left:3px;border-bottom:1px #b3cddd solid;">
			<a id="menu_1" class="tab_act_short" href="javascript:void(0)" onclick="menuChanger(1,1);changeSrc1('1')">排名分析</a>
		</td>
	</tr>
</table>


<table style="width: 100%;" align="center" cellspacing="0" cellpadding="0">
	<tr class="tools">
		<td id="feeTab"></td><td><a href='javascript:;' onclick="open_metaExplain('SUBJECT')"><img src="<%=request.getContextPath()%>/biimages/home/nav_icon3.gif" width="14" height="14" border="0" hspace="10">指标说明</a></td>
		<td title="点击打开条件选择菜单" align="right" onclick="showcat('menucontent')"; style="cursor:pointer">

		  <font class="tooltxt"  title="点击打开条件选择菜单" ><b>时间</b>：<span id=gather_month><%=qryStruct.gather_month%></span><INPUT id=__gind971_50 type=hidden value="<%=qryStruct.gather_month%>" name="qry__gather_month" monthOnly="true" max="<%=qryStruct.date_e%>" min="200601" size="10"></font>
		</td>
		<td align="center" width="10%"><input type="button" id="button_outputExcel" value="导出EXCEL" onclick="javascript:export_table_content('product_t_001','套餐专题品牌体系')"></td>
	</tr>
</table>

    <div id="menucontent"
         style="position:absolute;width:400px;height:5px;left:<%=screenx-700%>px;top:10px;padding-top:10px;overflow:hidden;">
<table class="popwidow" cellpadding="0" cellspacing="5">
	<tr>
		<td class="poptitle" style="width:110">时间</td>
		<td class="poptitle" style="width:40px">操作</td>
	</tr>
	<tr>
		<td valign="top"><%=PageConditionUtil.getMonthDesc()%></td>
		
		<td valign="top" align="center">
		<table style="width: 100%">
					<tr>
				<td align="center"  height="25"><input id="button_submit" type="button" value="确认" onclick="doQuery();"/></td>
			</tr>
			<tr>
				<td align="center"  height="25"><input id="button_reset" type="button" value="取消" onclick="hiddencat('menucontent')"/></td>
			</tr>

		</table>
	</td>
	</tr>
</table>
</div>

<table style="width: 100%;" id="content_1" cellspacing="0" cellpadding="0">
	<tr  valign="top" id="add_type_1" >
	    <td>

		<table  cellspacing="0" cellpaddingx="0" style="width: 100%;margin-bottom: 15px;" border="0">
			<tr  valign="top">
			   <td class="toolbg" colspan=3>
			 		类型:
			 		<input type="radio" name="type_id" id="type_id" value="1" onClick="typeChange(this.value)" checked="checked" />无线宽带协议
			 		<input type="radio" name="type_id" id="type_id" value="2" onClick="typeChange(this.value)" />ARPU分档
	   		   </td>	
		 	</tr>
			<tr valign="top">
			<td align="left" valign="top" width="50%"><iframe id="chart_lose_wb_expire_01_C_1" 
						scrolling="no" width="100%" height="230" border="0" frameborder="0"
						marginwidth="0" marginheight="0" src=""></iframe></td>
			<td align="left" valign="top" width="50%"><iframe id="chart_lose_wb_expire_01_C_2" 
						scrolling="no" width="100%" height="220" border="0" frameborder="0"
						marginwidth="0" marginheight="0" src=""></iframe></td>	
			</tr>
		</table>
	</td>
	</tr>
	<tr>	
	<td>
		<table  cellspacing="0" cellpaddingx="0" style="width: 100%;">
			<tr valign="top" >
				<td align="left" colspan="2"><iframe id="table_lose_wb_expire_01" width="100%" height="300"
							src=""
					frameborder="0" scrolling="no"></iframe>
				</td>
			</tr>
		</table>
	</td>
	</tr>
	
</table>
<table cellspacing="0" cellpaddingx="0" style="width: 100%;">

		<tr>
		<td><%=CommTool.getEditorHtml(qryStruct.optype,"0")%></td>
	</tr>
</table>

</FORM>
</body>
</html>
<script>

//对象
function BaseXmlSubmit(){
}
//动作
BaseXmlSubmit.prototype.callAction = function f_callAction(url)
{
  var dom = null;
  try{
    var rpc = new XmlRPC(url);
    rpc.send();
    dom = rpc.getText();
  }
  catch(e){
    alert(e.message);
  }
  return dom;
}

//实例
var baseXmlSubmit =new BaseXmlSubmit();

var tab = 1;
var rdoType = 1;
var addTypeLen = 0;
var tableId = "MIFO001";
var title = new Array("总体分析","趋势分析","累计值","累计值同比");
var tabName = new Array("排名分析");



		changeSrc1(rdoType);

function menuChanger(contentID,tabNum){
    for (i=1;i<=tabNum;i++) {
		document.getElementById("menu_"+i).className = "tab_short";	
		document.getElementById("content_"+i).style.display = "none";		
	}

    document.getElementById("menu_"+contentID).className = "tab_act_short";
    document.getElementById("content_"+contentID).style.display = "block";	 
    tab=contentID;
  }

function changeSrc1(rdo){
	var iframe = document.getElementById("table_lose_wb_expire_01");
	tableId = "lose_wb_expire_01";

	iframe.src="SubjectCommTable.rptdo?table_id="+tableId+"&first=Y&table_height=300";

	doChart(rdo);
	var chartIframe = document.getElementById("chart_lose_wb_expire_01_C_1");
	chartIframe.src="SubjectCommChart.screen?chart_id=lose_wb_expire_01_C_1&flag=Y";
	var chartIframe1 = document.getElementById("chart_lose_wb_expire_01_C_2");
	chartIframe1.src="SubjectCommChart.screen?chart_id=lose_wb_expire_01_C_2&flag=Y";
}

function doChart(rdo) {
 
	rdoType = rdo;
	document.getElementById("feeTab").innerHTML="<font class='tooltitle'>客户流失>>无线宽带到期预警>>"+tabName[parseInt(tab)-1]+"</font>";

}

function changeChart(tableId,chartId,value,valuedesc){

	var bar = baseXmlSubmit.callAction("<%=request.getContextPath()%>/leaderview/dayview/qwyfz_ajax.jsp?city_id="+value + "&city_desc=" + valuedesc);
    bar=bar.replace(/^\s+|\n+$/g,'');

	var chartIframe = document.getElementById("table_"+tableId);    
	chartIframe.src="SubjectCommTable.rptdo?table_id="+tableId+"&first=Y&table_height=300";

	checkboxChange('TableQryForm','lose_wb_expire_02_C_1','lose_wb_expire_02_C_1');

}

function typeChange(value) {
	var tableIframe = document.getElementById("table_lose_wb_expire_01");    
	tableIframe.src="SubjectCommTable.rptdo?table_id=lose_wb_expire_0"+value+"&first=Y&table_height=300";
	var chartIframe = document.getElementById("chart_lose_wb_expire_01_C_1");
	chartIframe.src="SubjectCommChart.screen?chart_id=lose_wb_expire_0"+value+"_C_1&flag=Y";	
}
function doQuery() {
	TableQryForm.submit();
}

function export_table_content(){
	var tabT = parseInt(tab)-1;
	window.open("SubjectTableContentExport.screen?table_id="+tableId+"&table_name=无线宽带到期预警-"+tabName[tabT],"数据导出","");
}

function open_metaExplain(adhoc_id)
    {
        var h = "500";
        var w = "750";
        var top = (screen.availHeight - h) / 2;
        var left = (screen.availWidth - w) / 2;
        var optstr = "height=" + h + ",width=" + w + ",left=" + left + ",top=" + top + ",status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no";
        var strUrl = "../adhoc/adhocMetaExplain.rptdo?adhoc_id=" + adhoc_id;
        var newsWin = window.open(strUrl, "editRptHead", optstr);
        if (newsWin != null) {
            newsWin.focus();
        }
    }


</script>