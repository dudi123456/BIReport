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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/bimain.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/css.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/newmain.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/titleStyle.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/Scroll.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dd99.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/chart.js" ></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/net.js"></SCRIPT>
<title><%=CSysCode.SYS_TITLE%></title>


<%

ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
if(qryStruct == null){
	qryStruct = new ReportQryStruct();
}

%>
<BIBM:SelfRefreshTag pageNames="TableQryForm.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1"/>
<body onLoad="selfDisp()">
<script>
document.body.onmousemove=quickalt;
document.body.onmouseover=getalt;
document.body.onmouseout=restorealt;
var tempalt='';

function export_table_content(tables,names){
	window.open("SubjectTableContentExport.screen?table_id="+tables+"&table_name="+names,"数据导出","");
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
<div style="display:none;position:absolute;" id=altlayer></div>


<table style="width: 100%;" align="center" cellspacing="0" cellpadding="0">
	<tr class="tools">
		<td><font class="tooltitle">专题分析>>增值聚焦专题>>宏观分析>>增值(内容)订购分析</font></td><td><a href='javascript:;' onclick="open_metaExplain('SUBJECT')"><img src="<%=request.getContextPath()%>/biimages/home/nav_icon3.gif" width="14" height="14" border="0" hspace="10">指标说明</a></td>

		<td title="点击打开条件选择菜单" align="right" onclick="showcat('menucontent')"; style="cursor:pointer">
		
		 <font class="tooltxt"  title="点击打开条件选择菜单" ><b>时间</b>：<span id=gather_month><%=qryStruct.gather_month%></span><INPUT id=__gind971_50 type=hidden value="<%=qryStruct.gather_month%>" name="qry__gather_month" monthOnly="true" max="<%=qryStruct.date_e%>" min="200601" size="10"></font>
		</td>
		<td align="center" width="10%"><input type="button" id="button_outputExcel" value="导出EXCEL" onclick="javascript:export_table_content('product_t_001','竞争预警')"></td>
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

<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>	
		<td align="center" width="50%" ><iframe id="chart_product_c_001" width="100%"
			height="250"
			src="SubjectCommChart.screen?chart_id=add_macro_002_C_1&first=Y&gather_month=<%=qryStruct.gather_month%>&width=<%=chartWidthTmp%>"
			frameborder="0" scrolling="no"></iframe></td>
	
			<td align="left"  width="40%" ><iframe id="chart_add_macro_002_C_2" 
						scrolling="no" width="100%" height="250" frameborder="0" src="SubjectCommChart.screen?chart_id=add_macro_002_C_2&first=Y&gather_month=<%=qryStruct.gather_month%>&addval_prod_id=235000000000000000789&chart_name_r=新闻早晚报&category_desc=新闻早晚报&"></iframe>
						</td>
						<td valign="top"><br>
						<input type="radio" name="radioadd_macro_002_C_2" onclick=subjectchartchangenoflag('add_macro_002_C_2','&chart_name_r=移动过网用户数&category_desc=移动过网用户数&category_index=1','add_macro_002_C_2') checked>用户数<br><input type="radio" name="radioadd_macro_002_C_2" onclick=subjectchartchangenoflag('add_macro_002_C_2','&chart_name_r=移动过网用户数&category_desc=移动过网用户数&category_index=2','add_macro_002_C_2')>渗透率</td>
		</tr>
	<tr>	
		<td align="center" colspan="3"><iframe id="table_product_t_001" width="100%"
			height="200"
			src="SubjectCommTable.rptdo?table_id=add_macro_002_1&first=Y&table_height=200"
			frameborder="0" scrolling="no"></iframe></td>
	</tr>
</table>
<table cellspacing="0" cellpaddingx="0" style="width: 100%;">

		<tr>
		<td><%=CommTool.getEditorHtml("add_userbuy_analyse","0")%></td>
	</tr>
</table>

</FORM>
</body>
</html>