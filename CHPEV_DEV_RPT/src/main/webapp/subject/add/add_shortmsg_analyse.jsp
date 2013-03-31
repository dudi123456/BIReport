<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import = "java.io.PrintWriter" %>
<%@ page import = "com.ailk.bi.base.util.*" %>
<%@ page import = "com.ailk.bi.common.dbtools.*" %>
<%@ page import = "com.ailk.bi.report.struct.ReportQryStruct" %>
<%@ page import="com.ailk.bi.common.app.AppException"%>
<%@ page import="com.ailk.bi.base.common.*"%>

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
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/dojo.js"></SCRIPT>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/XmlRPC.js"></script>

<title><%=CSysCode.SYS_TITLE%></title>


<%

ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
if(qryStruct == null){
	qryStruct = new ReportQryStruct();
}
String tabFlag = (String)session.getAttribute("monitor_addval_tabFee");

%>


<BIBM:SelfRefreshTag pageNames="TableQryForm.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1"/>
<body>
<script>
document.body.onmousemove=quickalt;
document.body.onmouseover=getalt;
document.body.onmouseout=restorealt;
var tempalt='';

</script>

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
var title = new Array("订购率分析","分布分析","饱和度分析","累计值同比");
var tabName = new Array("订购率分析","分布分析","饱和度分析");

<%
if(tabFlag != null && !"".equals(tabFlag)) {
	String[] flag = tabFlag.split(",");
%>

	tab = <%=flag[0]%>;
	rdoType = <%=flag[1]%>;
<%}%>
function window.onload() {
	selfDisp();
	menuChanger(tab,1)
	if(tab == 1) {
		changeSrc1(rdoType);
	}else if(tab == 2) {
		changeSrc2(rdoType);
	}
}
function menuChanger(contentID,tabNum){
    for (i=1;i<=tabNum;i++) {
		document.getElementById("menu_"+i).className = "tab_short";
		document.getElementById("content_"+i).style.display = "none";
	}

    document.getElementById("menu_"+contentID).className = "tab_act_short";
    document.getElementById("content_"+contentID).style.display = "block";
    tab=contentID;
	//alert(tab)

  }

function changeSrc1(rdo){
	//alert(rdo);
	var iframe = document.getElementById("table_add_macro_003_2");
	tableId = "add_macro_003_2";
	iframe.src="SubjectCommTable.rptdo?table_id="+tableId+"&first=Y&table_height=200";
	//document.getElementById("notes").src="notesInfo.jsp?tableId="+tableId;
	doChart(rdo);

	seleType1("","",'1');
}
function changeSrc2(rdo){/*
	var iframe = document.getElementById("table_list_2");

	tableId = "add_macro_003_1";


	iframe.src="SubjectCommTable.rptdo?table_id="+tableId+"&first=Y&table_height=200";
*/
	doChart(rdo);
	//var arr = initTrend();

	seleType2("","",rdoType);
}


function doChart(rdo) {

	rdoType = rdo;
//	var sHtml = "<a href='javascript:void(0)' class='2menu' onclick=\"swapText('1');\">点击查看【"+title[parseInt(tab)-1]+"】指标趋势</a>";
//	var linkDiv = document.getElementById("linkDiv"+tab);

//	linkDiv.innerHTML=sHtml;
	//alert(tab);
	document.getElementById("feeTab").innerHTML="<font class='tooltitle'>专题分析>>增值聚焦专题>>增值业务套餐分析>>短信套餐>>"+tabName[parseInt(tab)-1]+"</font>";
//	document.getElementById("add_type_"+tab).style.display = "none";
//	document.getElementById("trend_chart_"+tab).style.display = "none";

}

function initTrend() {
	var ckb = document.getElementById("addType"+tab+"_1");
	ckb.checked=true;

	var val = ckb.value;
	var arr = val.split(",");
	addTypeLen = arr[2];
	return arr;
}

function swapText(flag) {

	document.getElementById("add_type_"+tab).style.display = "block";
	document.getElementById("trend_chart_"+tab).style.display = "block";
 //  document.getElementById("add_type_"+tab).style.position="absolute";
//   document.getElementById("add_type_"+tab).style.posTop=table_list_1.document.body.scrollHeight;

	var linkDiv = document.getElementById("linkDiv"+tab);
	var rdoT = parseInt(rdoType)-1;
	var opHtml = "<a href='javascript:void(0)' class='2menu' onclick=\"swapText('0');\">点击关闭【"+title[rdoT]+"】指标趋势</a>";
	var clHtml = "<a href='javascript:void(0)' class='2menu' onclick=\"swapText('1');\">点击查看【"+title[rdoT]+"】指标趋势</a>";

	if (flag == 1) {
		linkDiv.innerHTML=opHtml;
	}else {
		linkDiv.innerHTML=clHtml;
		document.getElementById("add_type_"+tab).style.display = "none";
		document.getElementById("trend_chart_"+tab).style.display = "none";
	}

}

function changeAddType(ck) {
	/*
    for(i=1;i<=addTypeLen;i++) {
    	document.getElementById("addType"+tab+"_"+i).checked = false;
    }
    document.getElementById("addType"+tab+"_"+ck).checked = true;
	*/
}

function seleType1(id,name,ck) {
	changeAddType(ck);
	chartIframe = document.getElementById("chart_add_macro_003_2_C_1");
	chartIframe.src="SubjectCommChart.screen?chart_id=add_macro_003_2_C_1&first=Y";


	chartIframe = document.getElementById("chart_add_macro_003_2_C_2");
	chartIframe.src="SubjectCommChart.screen?chart_id=add_macro_003_2_C_2&first=Y";

//	alert(of.outerHTML);
}

function changeChartSeries() {
	var strCheckValue = "";
	for(i=0;i<document.all("myChkBox1").length;i++)  {
		if(document.all("myChkBox1")[i].checked){
			if (strCheckValue.length>0)
			{
				strCheckValue += "," + document.all("myChkBox1")[i].value;
			}else{
				strCheckValue = document.all("myChkBox1")[i].value;
			}

		}
	}
    var info = baseXmlSubmit.callAction("<%=request.getContextPath()%>/subject/add/add_zifee_ajax.jsp?date=" + Date() + "&itemId="+strCheckValue);

	chartIframe = document.getElementById("chart_add_macro_003_2_C_1");
	chartIframe.src="SubjectCommChart.screen?chart_id=add_macro_003_2_C_1&first=Y";
	chartIframe = document.getElementById("chart_add_macro_003_2_C_2");
	chartIframe.src="SubjectCommChart.screen?chart_id=add_macro_003_2_C_2&first=Y";

}

function checkAllItem(opValue){
	var strHref = "";
	if (opValue==0)
	{
		strHref = "<input type='button' onclick='checkAllItem(1);' style='fontSize:12px;font-weight:bold' value='全选'>&nbsp;&nbsp;";
	}else if(opValue==1){

		strHref = "<input type='button' onclick='checkAllItem(0);' style='font-size:12px;font-weight:bold' value='全不选'>&nbsp;&nbsp;";
	}

	checkBoxOption(opValue);
    document.getElementById("choseArea").innerHTML = strHref;
	//changeChartSeries();
}

function checkBoxOption(opValue)
{
	var opBoolean = true;
	if (opValue==0)
	{
		opBoolean = false;
	}else{
		opBoolean = true;
	}

	var code_Values = document.all['myChkBox1'];
	if(code_Values.length){
		for(var i=0;i<code_Values.length;i++)
		{
		code_Values[i].checked = opBoolean;
		}
	}else{
		code_Values.checked = opBoolean;
	}
}

function seleType2(id,name,ck) {
	changeAddType(ck);


	var radio =  TableQryForm["Leve6_1"];
	for(var i=0; i<radio.length; i++) {
		var tmpIndex = i+1;
		if (tmpIndex==rdoType)
		{
			radio[i].checked = true;
		}

	}

	for(var i=0; i<radio.length; i++) {

		if (radio[i].checked)
		{
				var iframe = document.getElementById("table_list_2");

				iframe.src="SubjectCommTable.rptdo?table_id="+radio[i].value+"&first=Y&table_height=200";

				var chartIframe = document.getElementById("chart_add_macro_003_C_1");
				chartIframe.src="SubjectCommChart.screen?chart_id=" + radio[i].value +"_C_1";
		}

	}


}


function changeChart(tableId,chartId,rdoIndex){
	var iframe = document.getElementById("table_list_2");

	iframe.src="SubjectCommTable.rptdo?table_id="+tableId+"&first=Y&table_height=200";

	var chartIframe = document.getElementById("chart_add_macro_003_C_1");
	chartIframe.src="SubjectCommChart.screen?chart_id=" + chartId;

	rdoType = rdoIndex;
}


function doQuery() {
	TableQryForm.tab_fee_flag.value = tab+","+rdoType;
	//alert(TableQryForm.tab_fee_flag.value);
	TableQryForm.submit();
}

function export_table_content(){
	var tabT = parseInt(tab)-1;
	window.open("SubjectTableContentExport.screen?table_id="+tableId+"&table_name=竞争宏观分析-"+tabName[tabT],"数据导出","");
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
</head>
<%
int screenx = Integer.parseInt(session.getAttribute(WebKeys.Screenx)==null?"1280":(String)session.getAttribute(WebKeys.Screenx));
int chartWidthTmp = (screenx-250)/2;

%>

<FORM name="TableQryForm" action="OppSubject.rptdo">
<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">
<INPUT type=hidden name="tab_fee_flag" >

<div style="display:none;position:absolute;" id=altlayer></div>
<table style="width: 100%; margin-top: 0px;" cellspacing="0" cellpadding="0">
	<tr>
		<td  style="padding-left:3px;border-bottom:1px #b3cddd solid;">
			<a id="menu_1" class="tab_short" href="javascript:void(0)" onclick="menuChanger(1,2);changeSrc1('1')" style="fontSize:12px;font-weight:bold">订购率分析</a><a id="menu_2" class="tab_short" href="javascript:void(0)" onclick="menuChanger(2,2);changeSrc2('1')" style="fontSize:12px;font-weight:bold">分布分析</a>
		</td>
	</tr>
</table>


<table style="width: 100%;font-weight:bold" align="center" cellspacing="0" cellpadding="0">
	<tr class="tools">
		<td id="feeTab"></td><td><a href='javascript:;' onclick="open_metaExplain('SUBJECT')"><img src="<%=request.getContextPath()%>/biimages/home/nav_icon3.gif" width="14" height="14" border="0" hspace="10">指标说明</a></td>
		<td title="点击打开条件选择菜单" align="right" onclick="showcat('menucontent')"; style="cursor:pointer">
		<font class="tooltxt"  title="点击打开条件选择菜单" ><b>资费名称</b>：<span id=city_name><%=qryStruct.price_plan_desc%></span><INPUT type=hidden name=qry__price_plan_desc size="10"><INPUT type=hidden name=qry__price_plan_id></font>
		 <font class="tooltxt"  title="点击打开条件选择菜单" ><b>时间</b>：<span id=gather_month><%=qryStruct.gather_month%></span><INPUT id=__gind971_50 type=hidden value="<%=qryStruct.gather_month%>" name="qry__gather_month" monthOnly="true" max="<%=qryStruct.date_e%>" min="200601" size="10"></font>
		</td>
		<td align="center" width="10%"><input type="button" id="button_outputExcel" value="导出EXCEL" onclick="javascript:export_table_content('product_t_001','套餐专题品牌体系')"></td>
	</tr>
</table>

    <div id="menucontent"
         style="position:absolute;width:390px;height:5px;left:<%=screenx-700%>px;top:10px;padding-top:10px;overflow:hidden;">
<table class="popwidow" cellpadding="0" cellspacing="5">
	<tr>
		<td class="poptitle" style="width:450px">资费名称</td>
		<td class="poptitle" style="width:120px">时间</td>
		<td class="poptitle" style="width:40px">操作</td>
	</tr>
	<tr>
		<td valign="top"><%=PageConditionUtil.getPrice_plan_Desc(request,qryStruct.price_plan_id,"price_plan_id","price_plan_desc",2,1)%></td>
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

<table style="width: 100%; display: none;"  id="content_1" cellspacing="0" cellpaddingx="0">
	<tr  valign="top" id="add_type_1" >
	    <td>

		<table  cellspacing="0" cellpaddingx="0" style="width: 100%;margin-bottom: 15px;">
		<tr  valign="top">

		 	</tr>
			<tr valign="top">
						<td align="left" valign="top" ><iframe id="chart_add_macro_003_2_C_1"
						scrolling="no" width="100%" height="220" border="0" frameborder="0"
						marginwidth="0" marginheight="0" src=""></iframe></td>

			<td align="left" valign="top" ><iframe id="chart_add_macro_003_2_C_2"
						scrolling="no" width="100%" height="220" border="0" frameborder="0"
						marginwidth="0" marginheight="0" src=""></iframe></td>


			</tr>

			</tr>

		</table>
	</td>
	</tr>
	<tr  valign="top" id="add_type_1" >

		    <td align=center><table cellspacing="0" cellpaddingx="0">
			<tr><td width="15%" align=right>
			<span id="choseArea">
			<input type='button' onclick="checkAllItem('0');" style="font-size:12px;font-weight:bold" value='全不选'>&nbsp;&nbsp;</span>
			</td><td>
			<%
			String[][] res1 = CommTool.queryZiFeeInfo("1");
			for(int i=0;i<res1.length;i++){
				String chkBox = res1[i][1] + "<INPUT TYPE='checkbox' NAME='myChkBox1' id='myChkBox1' checked value='" + res1[i][0] + "' onclick='changeChartSeries();'>&nbsp;&nbsp;";
				out.print(chkBox);
			}
			%>
	</td>
	</tr>

			</table>
		</td>

	</tr>
	<tr>
	<td>
		<table  cellspacing="0" cellpaddingx="0" style="width: 100%;">
			<tr valign="top" >
				<td align="left" colspan="2"><iframe name="table_add_macro_003_2" id="table_add_macro_003_2" width="100%" height="200"
							src=""
					frameborder="0" scrolling="no"></iframe>
				</td>
			</tr>
		</table>
	</td>
	</tr>

</table>



<table style="width: 100%; display: none;" id="content_2" cellspacing="0" cellpadding="0">
	<tr  valign="top" id="add_type_2" >
	    <td>

		<table  cellspacing="0" cellpaddingx="0" style="width: 100%;margin-bottom: 15px;">
		<tr  valign="top">
			   <td class="toolbg" colspan=2>
			 		<INPUT class="radioStyle" onclick="changeChart('add_macro_003_1','add_macro_003_1_C_1','1');" value="add_macro_003_1" type=radio name=Leve6_1 checked>按ARPU分档
			 		<INPUT class="radioStyle" onclick="changeChart('add_macro_003_1_2','add_macro_003_1_2_C_1','2');" value="add_macro_003_1_2" type=radio name=Leve6_1>按在网时长分档
			 		<INPUT class="radioStyle" onclick="changeChart('add_macro_003_1_3','add_macro_003_1_3_C_1','3');" value="add_macro_003_1_3" type=radio name=Leve6_1>饱和度分档


	   			 </td>
		 	</tr>

			<tr valign="top">


			<td align="left" valign="top" ><iframe id="chart_add_macro_003_C_1"
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
				<td align="left" colspan="2"><iframe id="table_list_2" width="100%" height="200"
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
		<td><%=CommTool.getEditorHtml("add_shortmsg_analyse","0")%></td>
	</tr>
</table>

</FORM>
</body>
</html>
<%session.removeAttribute("monitor_addval_tabFee"); %>
