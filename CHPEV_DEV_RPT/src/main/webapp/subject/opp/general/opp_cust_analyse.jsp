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
var tab = 1;
var rdoType = 1;
var addTypeLen = 0;
var tableId = "MIFO001";
var title = new Array("本期值","本期值环比","累计值","累计值同比");
var tabName = new Array("客户价值","客户质量","在网时长","通话时长分档","通话次数分档");

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
	var iframe = document.getElementById("table_list_1");
/*
    for(i=1;i<=4;i++) {
    	document.getElementById("Leve"+tab+"_"+i).checked = false;
    }
    document.getElementById("Leve"+tab+"_"+rdo).checked = true; 
*/
	tableId = "opp_macro_003_1";

	iframe.src="SubjectCommTable.rptdo?table_id="+tableId+"&first=Y&table_height=300";
	doChart(rdo);
	seleType1("","",'1');
}


function changeSrc2(rdo){

	var iframe = document.getElementById("table_list_2");
	/*
    for(i=1;i<=4;i++) {
    	document.getElementById("Leve"+tab+"_"+i).checked = false;
    }
    document.getElementById("Leve"+tab+"_"+rdo).checked = true; 
	*/
	tableId = "opp_macro_003_2";
	
	iframe.src="SubjectCommTable.rptdo?table_id="+tableId+"&first=Y&table_height=300";
	//document.getElementById("notes").src="./notesInfo.jsp?tableId="+tableId;
	doChart(rdo);
	seleType2("","",'1');

	//var arr = initTrend();
//	seleType2(arr[0],arr[1],'1');
}

function changeSrc3(rdo){

	var iframe = document.getElementById("table_list_3");
	/*
    for(i=1;i<=4;i++) {
    	document.getElementById("Leve"+tab+"_"+i).checked = false;
    }
    document.getElementById("Leve"+tab+"_"+rdo).checked = true; 
	*/
	tableId = "opp_macro_003_3";
	
	iframe.src="SubjectCommTable.rptdo?table_id="+tableId+"&first=Y&table_height=300";
	//document.getElementById("notes").src="./notesInfo.jsp?tableId="+tableId;
	doChart(rdo);
	//seleType2("","",'1');

	//var arr = initTrend();
//	seleType2(arr[0],arr[1],'1');
}

function changeSrc4(rdo){

	var iframe = document.getElementById("table_list_4");
	/*
    for(i=1;i<=4;i++) {
    	document.getElementById("Leve"+tab+"_"+i).checked = false;
    }
    document.getElementById("Leve"+tab+"_"+rdo).checked = true; 
	*/
	tableId = "opp_macro_003_4";
	
	iframe.src="SubjectCommTable.rptdo?table_id="+tableId+"&first=Y&table_height=300";
	//document.getElementById("notes").src="./notesInfo.jsp?tableId="+tableId;
	doChart(rdo);
	//seleType2("","",'1');

	//var arr = initTrend();
//	seleType2(arr[0],arr[1],'1');
}

function changeSrc5(rdo){

	var iframe = document.getElementById("table_list_5");
	/*
    for(i=1;i<=4;i++) {
    	document.getElementById("Leve"+tab+"_"+i).checked = false;
    }
    document.getElementById("Leve"+tab+"_"+rdo).checked = true; 
	*/
	tableId = "opp_macro_003_5";
	
	iframe.src="SubjectCommTable.rptdo?table_id="+tableId+"&first=Y&table_height=300";
	//document.getElementById("notes").src="./notesInfo.jsp?tableId="+tableId;
	doChart(rdo);
	//seleType2("","",'1');

	//var arr = initTrend();
//	seleType2(arr[0],arr[1],'1');
}
function doChart(rdo) {
 
	rdoType = rdo;
	document.getElementById("feeTab").innerHTML="<font class='tooltitle'>专题分析>>竞争对手专题>>竞争宏观分析>>客户分析>>"+tabName[parseInt(tab)-1]+"</font>";
	
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
	var chartIframe = document.getElementById("trend_chart_"+tab);
	chartIframe.src="SubjectCommChart.screen?chart_id=opp_macro_003_1_C_1&whereSql=&data_kind_id="+id+"&chart_name="+name;

	chartIframe = document.getElementById("trend_chart_"+tab+"_2");
	chartIframe.src="SubjectCommChart.screen?chart_id=opp_macro_003_1_C_2&whereSql=&data_kind_id="+id+"&chart_name="+name;

//	alert(of.outerHTML);
}
function seleType2(id,name,ck) {
	changeAddType(ck);
	var chartIframe = document.getElementById("trend_chart_"+tab);
	chartIframe.src="SubjectCommChart.screen?chart_id=opp_macro_003_2_C_1&whereSql=&data_kind_id="+id+"&chart_name="+name;

	chartIframe = document.getElementById("trend_chart_"+tab+"_2");
	chartIframe.src="SubjectCommChart.screen?chart_id=opp_macro_003_2_C_2&whereSql=&data_kind_id="+id+"&chart_name="+name;
}
function doQuery() {
	TableQryForm.tab_fee_flag.value = tab+","+rdoType;
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
			<a id="menu_1" class="tab_short" href="javascript:void(0)" onclick="menuChanger(1,5);changeSrc1('1')">客户价值</a>
			<a id="menu_2" class="tab_short" href="javascript:void(0)" onclick="menuChanger(2,5);changeSrc2('1')">客户质量</a>
			<a id="menu_3" class="tab_short" href="javascript:void(0)" onclick="menuChanger(3,5);changeSrc3('1')">在网时长</a>
			<a id="menu_4" class="tab_short" href="javascript:void(0)" onclick="menuChanger(4,5);changeSrc4('1')">通话时长</a>
			<a id="menu_5" class="tab_short" href="javascript:void(0)" onclick="menuChanger(5,5);changeSrc5('1')">通话次数</a>
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
         style="position:absolute;width:290px;height:5px;left:<%=screenx-500%>px;top:10px;padding-top:10px;overflow:hidden;">
<table class="popwidow" cellpadding="0" cellspacing="5">
	<tr>

		<td class="poptitle" style="width:90px">时间</td>
		<td class="poptitle" style="width:40px">操作</td>
	</tr>
	<tr>
	
		<td valign="top"><%=PageConditionUtil.getMonthDesc()%></td>
		
		<td valign="top" align="center">
		<table style="width: 100%">
					<tr>
				<td align="center"  height="25"><input id="button_submit" type="submit" value="确认"/></td>
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
			<td align="left" valign="top" >
						<iframe id="trend_chart_1" 
						scrolling="no" width="100%" height="300" border="0" frameborder="0"
						marginwidth="0" marginheight="0" src=""></iframe></td>

							<td align="left" valign="top" ><iframe id="trend_chart_1_2" 
						scrolling="no" width="100%" height="300" border="0" frameborder="0"
						marginwidth="0" marginheight="0" src=""></iframe>
						</td>
				
			</tr>
		</table>
	</td>
	</tr>
	<tr>	
	<td>
		<table  cellspacing="0" cellpaddingx="0" style="width: 100%;">
		
			<tr valign="top" >
				<td align="left" colspan="2"><iframe name="table_list_1" id="table_list_1" width="100%" height="350"
							src=""
					frameborder="0" scrolling="no"></iframe>
				</td>
			</tr>
		</table>
	</td>
	</tr>
	
</table>



<table style="width: 100%; display: none;" id="content_2" cellspacing="0" cellpadding="0">
	<tr  valign="top"  id="add_type_2" >
	    <td>

		<table  cellspacing="0" cellpaddingx="0" style="width: 100%;margin-bottom: 15px;">
		
			<tr valign="top">
			<td align="left" valign="top" >
			<iframe id="trend_chart_2" 
						scrolling="no" width="100%" height="300" border="0" frameborder="0"
						marginwidth="0" marginheight="0" src=""></iframe></td>

							<td align="left" valign="top" ><iframe id="trend_chart_2_2" 
						scrolling="no" width="100%" height="300" border="0" frameborder="0"
						marginwidth="0" marginheight="0" src=""></iframe>

		</td>
				
			</tr>
		</table>
	</td>
	</tr>
	<tr>	
	<td>
		<table  cellspacing="0" cellpaddingx="0" style="width: 100%;">			
			<tr valign="top" >
				<td align="left" colspan="2"><iframe id="table_list_2" width="100%" height="300"
							src=""
					frameborder="0" scrolling="no"></iframe>
				</td>
			</tr>
		</table>
	</td>
	</tr>
	
</table>

<table style="width: 100%; display: none;" id="content_3" cellspacing="0" cellpadding="0">
	
	<tr valign="top">			
		<td align="left" width="75%">
			<iframe id="chart_opp_macro_003_3_C_1" 
				scrolling="no" width="100%" height="260" frameborder="0"
				marginwidth="0" marginheight="0" src="SubjectCommChart.screen?chart_id=opp_macro_003_3_C_1&first=Y"></iframe>
		</td>
		<td width="25%">
	    	<table border="0" width="100%">
		    	<tr>
			    	<td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=移动过网用户数&category_desc=移动过网用户数&category_index=2','opp_macro_003_3_C_1') checked>移动过网用户数 </td>
				    <td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=联通过网用户数&category_desc=联通过网用户数&category_index=3','opp_macro_003_3_C_1')>联通过网用户数 </td> 	
				</tr>
				<tr>
					<td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=过网用户数&category_desc=过网用户数&category_index=4','opp_macro_003_3_C_1')>过网用户数</td>				
		    		<td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=移动主叫通话次数&category_desc=移动主叫通话次数&category_index=5','opp_macro_003_3_C_1')>移动主叫通话次数</td>
			    	</tr>
				<tr>
			    	<td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=移动被叫通话次数&category_desc=移动被叫通话次数&category_index=6','opp_macro_003_3_C_1')>移动被叫通话次数</td>		    	
		    		<td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=移动合计通话次数&category_desc=移动合计通话次数&category_index=7','opp_macro_003_3_C_1')>移动合计通话次数
		    		</td>
		    	</tr>
				<tr>
		    		<td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=联通主叫通话次数&category_desc=联通主叫通话次数&category_index=8','opp_macro_003_3_C_1')>联通主叫通话次数</td>
			    	<td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=联通被叫通话次数&category_desc=联通被叫通话次数&category_index=9','opp_macro_003_3_C_1')>联通被叫通话次数</td>
		    	</tr>
		    	<tr>
		    		<td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=联通合计通话次数&category_desc=联通合计通话次数&category_index=10','opp_macro_003_3_C_1')>联通合计通话次数</td>		    	
		    		<td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=主叫通话次数&category_desc=主叫通话次数&category_index=11','opp_macro_003_3_C_1')>主叫通话次数</td>
			    	</tr>
				<tr>
			    	<td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=被叫通话次数&category_desc=被叫通话次数&category_index=12','opp_macro_003_3_C_1')>被叫通话次数</td>
					<td ><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=合计通话次数&category_desc=合计通话次数&category_index=13','opp_macro_003_3_C_1')>合计通话次数</td>
				</tr>
				<tr>
		    		<td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=移动主叫通话时长&category_desc=移动主叫通话时长&category_index=14','opp_macro_003_3_C_1')>移动主叫通话时长</td>
			    	<td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=移动被叫通话时长&category_desc=移动被叫通话时长&category_index=15','opp_macro_003_3_C_1')>移动被叫通话时长</td>
		    	</tr>
		    	<tr>
		    		<td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=移动合计通话时长&category_desc=移动合计通话时长&category_index=16','opp_macro_003_3_C_1')>移动合计通话时长</td>		    	
		    		<td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=联通主叫通话时长&category_desc=联通主叫通话时长&category_index=17','opp_macro_003_3_C_1')>联通主叫通话时长</td>
			    </tr>
				<tr>
			    	<td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=联通被叫通话时长&category_desc=联通被叫通话时长&category_index=18','opp_macro_003_3_C_1')>联通被叫通话时长</td>		    	
		    		<td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=联通合计通话时长&category_desc=联通合计通话时长&category_index=19','opp_macro_003_3_C_1')>联通合计通话时长</td>
		    	</tr>
				<tr>
		    		<td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=主叫通话时长&category_desc=主叫通话时长&category_index=20','opp_macro_003_3_C_1')>主叫通话时长</td>
			    	<td><input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=被叫通话时长&category_desc=被叫通话时长&category_index=21','opp_macro_003_3_C_1')>被叫通话时长</td>
		    	</tr>
		    	<tr>
		    		<td colspan="2">
						<input type="radio" name="radioopp_macro_003_3_C_1" onclick=subjectchartchangenoflag('opp_macro_003_3_C_1','&chart_name_r=合计通话时长&category_desc=合计通话时长&category_index=22','opp_macro_003_3_C_1')>合计通话时长		    		
		    		</td>
		    	</tr>
	    	</table>
		</td>

	</tr>
	
	<tr>
		<td align="left" colspan="2"><iframe id="table_list_3" width="100%" height="300" src=""
			frameborder="0" scrolling="no"></iframe>
		</td>
	</tr>
	
</table>

<table style="width: 100%; display: none;" id="content_4" cellspacing="0" cellpadding="0">
	
	<tr valign="top">			
		<td align="left" width="75%">
			<iframe id="chart_opp_macro_003_4_C_1" 
				scrolling="no" width="100%" height="260" frameborder="0"
				marginwidth="0" marginheight="0" src="SubjectCommChart.screen?chart_id=opp_macro_003_4_C_1&first=Y"></iframe>
		</td>
		<td width="25%">
	    	<table border="0" width="100%">
		    	<tr>
			    	<td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=移动过网用户数&category_desc=移动过网用户数&category_index=2','opp_macro_003_4_C_1') checked>移动过网用户数 </td>
				    <td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=联通过网用户数&category_desc=联通过网用户数&category_index=3','opp_macro_003_4_C_1')>联通过网用户数 </td> 	
				</tr>
				<tr>
					<td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=过网用户数&category_desc=过网用户数&category_index=4','opp_macro_003_4_C_1')>过网用户数</td>				
		    		<td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=移动主叫通话次数&category_desc=移动主叫通话次数&category_index=5','opp_macro_003_4_C_1')>移动主叫通话次数</td>
			    	</tr>
				<tr>
			    	<td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=移动被叫通话次数&category_desc=移动被叫通话次数&category_index=6','opp_macro_003_4_C_1')>移动被叫通话次数</td>		    	
		    		<td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=移动合计通话次数&category_desc=移动合计通话次数&category_index=7','opp_macro_003_4_C_1')>移动合计通话次数
		    		</td>
		    	</tr>
				<tr>
		    		<td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=联通主叫通话次数&category_desc=联通主叫通话次数&category_index=8','opp_macro_003_4_C_1')>联通主叫通话次数</td>
			    	<td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=联通被叫通话次数&category_desc=联通被叫通话次数&category_index=9','opp_macro_003_4_C_1')>联通被叫通话次数</td>
		    	</tr>
		    	<tr>
		    		<td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=联通合计通话次数&category_desc=联通合计通话次数&category_index=10','opp_macro_003_4_C_1')>联通合计通话次数</td>		    	
		    		<td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=主叫通话次数&category_desc=主叫通话次数&category_index=11','opp_macro_003_4_C_1')>主叫通话次数</td>
			    	</tr>
				<tr>
			    	<td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=被叫通话次数&category_desc=被叫通话次数&category_index=12','opp_macro_003_4_C_1')>被叫通话次数</td>
					<td ><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=合计通话次数&category_desc=合计通话次数&category_index=13','opp_macro_003_4_C_1')>合计通话次数</td>
				</tr>
				<tr>
		    		<td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=移动主叫通话时长&category_desc=移动主叫通话时长&category_index=14','opp_macro_003_4_C_1')>移动主叫通话时长</td>
			    	<td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=移动被叫通话时长&category_desc=移动被叫通话时长&category_index=15','opp_macro_003_4_C_1')>移动被叫通话时长</td>
		    	</tr>
		    	<tr>
		    		<td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=移动合计通话时长&category_desc=移动合计通话时长&category_index=16','opp_macro_003_4_C_1')>移动合计通话时长</td>		    	
		    		<td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=联通主叫通话时长&category_desc=联通主叫通话时长&category_index=17','opp_macro_003_4_C_1')>联通主叫通话时长</td>
			    </tr>
				<tr>
			    	<td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=联通被叫通话时长&category_desc=联通被叫通话时长&category_index=18','opp_macro_003_4_C_1')>联通被叫通话时长</td>		    	
		    		<td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=联通合计通话时长&category_desc=联通合计通话时长&category_index=19','opp_macro_003_4_C_1')>联通合计通话时长</td>
		    	</tr>
				<tr>
		    		<td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=主叫通话时长&category_desc=主叫通话时长&category_index=20','opp_macro_003_4_C_1')>主叫通话时长</td>
			    	<td><input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=被叫通话时长&category_desc=被叫通话时长&category_index=21','opp_macro_003_4_C_1')>被叫通话时长</td>
		    	</tr>
		    	<tr>
		    		<td colspan="2">
						<input type="radio" name="radioopp_macro_003_4_C_1" onclick=subjectchartchangenoflag('opp_macro_003_4_C_1','&chart_name_r=合计通话时长&category_desc=合计通话时长&category_index=22','opp_macro_003_4_C_1')>合计通话时长		    		
		    		</td>
		    	</tr>
	    	</table>
		</td>

	</tr>
	
	<tr>
		<td align="left" colspan="2"><iframe id="table_list_4" width="100%" height="300" src=""
			frameborder="0" scrolling="no"></iframe>
		</td>
	</tr>
	
</table>

<table style="width: 100%; display: none;" id="content_5" cellspacing="0" cellpadding="0">

	<tr valign="top">			
		<td align="left" width="75%">
			<iframe id="chart_opp_macro_003_5_C_1" 
				scrolling="no" width="100%" height="260" frameborder="0"
				marginwidth="0" marginheight="0" src="SubjectCommChart.screen?chart_id=opp_macro_003_5_C_1&first=Y"></iframe>
		</td>
		<td width="25%">
	    	<table border="0" width="100%">
		    	<tr>
			    	<td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=移动过网用户数&category_desc=移动过网用户数&category_index=2','opp_macro_003_5_C_1') checked>移动过网用户数 </td>
				    <td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=联通过网用户数&category_desc=联通过网用户数&category_index=3','opp_macro_003_5_C_1')>联通过网用户数 </td> 	
				</tr>
				<tr>
					<td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=过网用户数&category_desc=过网用户数&category_index=4','opp_macro_003_5_C_1')>过网用户数</td>				
		    		<td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=移动主叫通话次数&category_desc=移动主叫通话次数&category_index=5','opp_macro_003_5_C_1')>移动主叫通话次数</td>
			    	</tr>
				<tr>
			    	<td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=移动被叫通话次数&category_desc=移动被叫通话次数&category_index=6','opp_macro_003_5_C_1')>移动被叫通话次数</td>		    	
		    		<td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=移动合计通话次数&category_desc=移动合计通话次数&category_index=7','opp_macro_003_5_C_1')>移动合计通话次数
		    		</td>
		    	</tr>
				<tr>
		    		<td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=联通主叫通话次数&category_desc=联通主叫通话次数&category_index=8','opp_macro_003_5_C_1')>联通主叫通话次数</td>
			    	<td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=联通被叫通话次数&category_desc=联通被叫通话次数&category_index=9','opp_macro_003_5_C_1')>联通被叫通话次数</td>
		    	</tr>
		    	<tr>
		    		<td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=联通合计通话次数&category_desc=联通合计通话次数&category_index=10','opp_macro_003_5_C_1')>联通合计通话次数</td>		    	
		    		<td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=主叫通话次数&category_desc=主叫通话次数&category_index=11','opp_macro_003_5_C_1')>主叫通话次数</td>
			    	</tr>
				<tr>
			    	<td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=被叫通话次数&category_desc=被叫通话次数&category_index=12','opp_macro_003_5_C_1')>被叫通话次数</td>
					<td ><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=合计通话次数&category_desc=合计通话次数&category_index=13','opp_macro_003_5_C_1')>合计通话次数</td>
				</tr>
				<tr>
		    		<td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=移动主叫通话时长&category_desc=移动主叫通话时长&category_index=14','opp_macro_003_5_C_1')>移动主叫通话时长</td>
			    	<td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=移动被叫通话时长&category_desc=移动被叫通话时长&category_index=15','opp_macro_003_5_C_1')>移动被叫通话时长</td>
		    	</tr>
		    	<tr>
		    		<td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=移动合计通话时长&category_desc=移动合计通话时长&category_index=16','opp_macro_003_5_C_1')>移动合计通话时长</td>		    	
		    		<td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=联通主叫通话时长&category_desc=联通主叫通话时长&category_index=17','opp_macro_003_5_C_1')>联通主叫通话时长</td>
			    </tr>
				<tr>
			    	<td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=联通被叫通话时长&category_desc=联通被叫通话时长&category_index=18','opp_macro_003_5_C_1')>联通被叫通话时长</td>		    	
		    		<td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=联通合计通话时长&category_desc=联通合计通话时长&category_index=19','opp_macro_003_5_C_1')>联通合计通话时长</td>
		    	</tr>
				<tr>
		    		<td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=主叫通话时长&category_desc=主叫通话时长&category_index=20','opp_macro_003_5_C_1')>主叫通话时长</td>
			    	<td><input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=被叫通话时长&category_desc=被叫通话时长&category_index=21','opp_macro_003_5_C_1')>被叫通话时长</td>
		    	</tr>
		    	<tr>
		    		<td colspan="2">
						<input type="radio" name="radioopp_macro_003_5_C_1" onclick=subjectchartchangenoflag('opp_macro_003_5_C_1','&chart_name_r=合计通话时长&category_desc=合计通话时长&category_index=22','opp_macro_003_5_C_1')>合计通话时长		    		
		    		</td>
		    	</tr>
	    	</table>
		</td>

	</tr>
	
	<tr>
		<td align="left" colspan="2"><iframe id="table_list_5" width="100%" height="300" src=""
			frameborder="0" scrolling="no"></iframe>
		</td>
	</tr>

</table>
<table cellspacing="0" cellpaddingx="0" style="width: 100%;">

		<tr>
		<td><%=CommTool.getEditorHtml("opp_cust_analyse","0")%></td>
	</tr>
</table>

</FORM>
</body>
</html>
<%session.removeAttribute("monitor_addval_tabFee"); %>
