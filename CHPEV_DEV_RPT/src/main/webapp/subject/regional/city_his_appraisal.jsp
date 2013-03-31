<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import = "com.ailk.bi.base.util.*" %>
<%@ page import = "com.ailk.bi.report.struct.ReportQryStruct" %>
<%@ include file="/base/commonHtml.jsp"%>

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

<title>地市评分明细</title>


<%
ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
if(qryStruct == null){
	qryStruct = new ReportQryStruct();
}
String editMode="1";
String AreaSqlAll = "SELECT t.city_code,t.city_name FROM dim_pub_city t where t.province_code='"+qryStruct.province_code+"' and t.city_code<>'000' ";
if(qryStruct.ctl_lvl.equals("2")){//地市人员
	AreaSqlAll+=" and t.city_code='"+qryStruct.city_code+"' ";
	editMode="0";
}
%>


<BIBM:SelfRefreshTag pageNames="TableQryForm.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1"/>
		
<script>
	
	$(document).ready(function() {
		initframe();		
	});
	function initframe(){					
		var tableIframe = document.getElementById("table_prov_T_01");
		//var right_attach_region='<%=qryStruct.right_attach_region%>';
		//var classifySelect='<%=qryStruct.classifySelect%>';
		
		tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_11&first=Y&table_height=250";
			
		//if(classifySelect=="" || classifySelect=="0"){
			//tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_11&first=Y&table_height=250";
		//}else if(classifySelect=="1"){
			//tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_17&first=Y&table_height=250";
		//}else if(classifySelect=="2"){
			//tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_19&first=Y&table_height=250";
		//}else{
			//tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_20&first=Y&table_height=250";
		//}
	}
	
	function export_table_content(){
		var tableId="CHPEV_DEV_RPT_001_11";
		//var right_attach_region='<%=qryStruct.right_attach_region%>';
		//var classifySelect='<%=qryStruct.classifySelect%>';
				
		//if(classifySelect=="" || classifySelect=="0"){
			//tableId="CHPEV_DEV_RPT_001_11";
		//}else if(classifySelect=="1"){
			//tableId="CHPEV_DEV_RPT_001_17";
		//}else if(classifySelect=="2"){
			//tableId="CHPEV_DEV_RPT_001_19";
		//}else{
			//tableId="CHPEV_DEV_RPT_001_20";
		//}
	
		var tableName="::province_code::省::city_code::地市评分明细(<%=qryStruct.date_e%>)";
		tableName=encodeURI(encodeURI(tableName));
		window.open("SubjectCommTableExport.rptdo?table_id="+tableId+"&table_name="+tableName+"&province_code=<%=qryStruct.province_code%>&city_code=<%=qryStruct.city_code%>","数据导出","");
	}
	
	function doQuery() {	
		TableQryForm.submit();
	}
</script>
</head>
<body>
		
<form name="TableQryForm" action="Regional.rptdo">
<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">
<INPUT type=hidden name="PROVINCE_CODE" value="<%=qryStruct.province_code%>">
<div style="display:none;position:absolute;" id=altlayer></div>	
<!-- 查询条件区,开始 -->
		<div class="topsearch">
			<table width="80%" border=0 cellpadding="0" cellspacing="0" align="center">
				<tr> 
                    <td align="center" class="td-bg">地市:</td>
                    <td >
                    <BIBM:TagSelectList  focusID="<%=qryStruct.city_code%>" script="class='easyui-combobox'"  listName="city_code" listID="0"  selfSQL="<%=AreaSqlAll%>" />
                    </td> 
					<td align="right" class="td-bg">
						月份：
					</td>
					<td>
						<input class="Wdate" style="height: 22px; width: 120px;"
							type="text" id="ACCT_MONTH" name="ACCT_MONTH"
							value="<%=qryStruct.date_e%>"
							onFocus="WdatePicker({crossFrame:false,isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})" />
					</td>
					<td>
						<input id="button_submit" type="button" class="btn_search" value="查询" onClick="doQuery()" />
					</td>
				</tr>
			</table>
			<!-- 查询条件区,结束 -->
		</div>
		<div class="result_title">
		<span>地市评分明细</span>
		<div class="iw">
	     	<input type="button" id="button_submit"  class="btn_excel"  value="导出EXCEL" onclick="javascript:export_table_content()">
		</div>
	</div>
	<!-- 图表展示区 ,开始-->
	<table style="width: 100%;"  id="content_1" cellspacing="0" cellpaddingx="0">
	<!-- 表格区 -->
	<tr>	
		<td>
		<table  cellspacing="0" cellpaddingx="0" style="width: 100%;">
			<tr valign="top" >
				<td align="left" colspan="2"><iframe id="table_prov_T_01" width="100%" height="600"
							src=""
					frameborder="0" scrolling="yes"></iframe>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<!-- 图表展示区 ,结束-->
	
 </form>
</body>
</html>