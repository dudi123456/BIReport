<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@ include file="/base/commonHtml.jsp"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="Content-Language" content="zh-cn">

	<link rel="stylesheet" type="text/css"
		href="<%=request.getContextPath()%>/css/other/newmain.css">
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/titleStyle.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/Scroll.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/dd99.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/chart.js"></script>
	<SCRIPT language=javascript
		src="<%=request.getContextPath()%>/js/net.js"></SCRIPT>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/wait.js"></SCRIPT>

	<title>地市评价信息</title>


	<%
		ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		if (qryStruct == null) {
			qryStruct = new ReportQryStruct();
		}
		String classifySelectSql="select * from st_eva_all_para u where u.supper_id='A01' ";
	%>


	<BIBM:SelfRefreshTag pageNames="TableQryForm.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1" />

<script>
	
	$(document).ready(function() {
		initframe();		
	});
	
	/*
	**得到默认账期
	*/
	function getOriDate(){
		var oriDate = document.getElementById("qry__rpt_date");
		return oriDate.value;
 	}
	
	function resetCon(){
		var oSerMonth = document.getElementById("qry__gather_month");
		oSerMonth.value=getOriDate();
		$("#qry__classifySelect")[0].selectedIndex = 0;
	}
	function initframe(){				
		var chartIframe = document.getElementById("chart_prov_C_01");
		var tableIframe = document.getElementById("table_prov_T_01");
		var classifySelect='<%=qryStruct.classifySelect%>';
		
		if(classifySelect==""){//全部
			chartIframe.src="SubjectCommChart.screen?chart_id=CHPEV_DEV_RPT_001_7_C_1";								
			tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_7&first=Y&table_height=250";				
		}else if(classifySelect=="A0101"){//产能
			chartIframe.src="SubjectCommChart.screen?chart_id=CHPEV_DEV_RPT_001_8_C_1";								
			tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_8&first=Y&table_height=250";		
		}else if(classifySelect=="A0102"){//质量
			chartIframe.src="SubjectCommChart.screen?chart_id=CHPEV_DEV_RPT_001_9_C_1";								
			tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_9&first=Y&table_height=250";		
		}else{//效能A0103
			chartIframe.src="SubjectCommChart.screen?chart_id=CHPEV_DEV_RPT_001_10_C_1";								
			tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_10&first=Y&table_height=250";
		}
	}
	
	function classifyChange(obj){	
		var chartIframe = document.getElementById("chart_prov_C_01");	
		var tableIframe = document.getElementById("table_prov_T_01");
		if(obj.value==""){//全部
			chartIframe.src="SubjectCommChart.screen?chart_id=CHPEV_DEV_RPT_001_7_C_1";								
			tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_7&first=Y&table_height=250";
		}else if(obj.value=="A0101"){//产能
			chartIframe.src="SubjectCommChart.screen?chart_id=CHPEV_DEV_RPT_001_8_C_1";								
			tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_8&first=Y&table_height=250";
		}else if(obj.value=="A0102"){//质量
			chartIframe.src="SubjectCommChart.screen?chart_id=CHPEV_DEV_RPT_001_9_C_1";								
			tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_9&first=Y&table_height=250";
		}else{//效能A0103
			chartIframe.src="SubjectCommChart.screen?chart_id=CHPEV_DEV_RPT_001_10_C_1";								
			tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_10&first=Y&table_height=250";
		}
		
	}
	
	function doQuery() {	
		TableQryForm.submit();
	}
	
	function subgo(){
		window.open("Regional.rptdo?optype=mark_right","评分指标及权重","");
	}
	
	function export_table_content(){
		var ctl_lvl='<%=qryStruct.ctl_lvl%>';//用户级别
		var province_code='<%=qryStruct.province_code%>';//用户所在省分
		var acct_month=document.getElementById("qry__gather_month").value;
		var city_code='<%=qryStruct.right_city_id%>';//用户所在地市
		
		var tableName="指标明细下载";
		if("1"==ctl_lvl){
			tableName= "::province_code::"+tableName;
		}else{
			tableName= "::province_code::::city_code::区间"+tableName;
		}
		tableName=encodeURI(encodeURI(tableName));
		if(ctl_lvl=="1"){//省分
			window.open("ExportTools.rptdo?exportId=City_Detail&province_code="+province_code+"&tableName="+tableName+"&acct_month="+acct_month+"&sqlArgs="+province_code+","+acct_month,"数据导出","");
		}else if(ctl_lvl=="2"){//地市
			window.open("ExportTools.rptdo?exportId=City_History&province_code="+province_code+"&city_code="+city_code+"&tableName="+tableName+"&sqlArgs="+city_code,"数据导出","");
		}
	}
</script>
</head>
<body>
<form name="TableQryForm" action="Regional.rptdo">
<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">
<INPUT type=hidden id="oriDate" name="qry__rpt_date"  value="<%=qryStruct.rpt_date%>">
<div style="display: none; position: absolute;" id=altlayer></div>
	
	<!-- 查询条件区,开始 -->
	<div class="topsearch">
	<table width="60%" border=0 cellpadding="0" cellspacing="0" align="center">
		<tr>
			<td align="right" class="td-bg">月份：</td>
			<td>
				<input class="Wdate" style="height: 22px;" type="text"
					id="qry__gather_month" name="qry__gather_month"
					value="<%=qryStruct.gather_month%>" size="15"
					onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})" />
			</td>
			<td align="right" class="td-bg">
				考评分类:
			</td>
			<td>
				<BIBM:TagSelectList  focusID="<%=qryStruct.classifySelect%>" script="class='easyui-combobox'"	listName="qry__classifySelect" listID="0" allFlag="" selfSQL="<%=classifySelectSql%>" />
				</select>
			</td>
			<td>
				<input id="button_submit" type="button" class="btn_search" value="查询"
					onclick="doQuery()" />
			</td>
			<td>
				<input type="button" class="btn3" value="重置" onclick="resetCon();"/>
			</td>
		</tr>
	</table>
	</div>
	<!-- 查询条件区,结束 -->
	<div class="result_title">
		<span >地市排名</span>		
	    <div class="iw">
	       <input type="button" id="button_submit"  class="btn_excel"  value="下载指标明细" onclick="javascript:export_table_content()">
		   <input  id="button_submit"  class="btn_iw" type="button" value="评分指标及权重" onclick="subgo()"/>
		</div>
	</div>
	<!-- 图表展示区 ,开始-->
	<table style="width: 100%;" id="content_1" cellspacing="0"
		cellpaddingx="0">
		<!-- 图形区 -->
		<tr valign="top" id="add_type_1">
			<td>
				<table cellspacing="0" cellpaddingx="0"
					style="width: 100%; margin-bottom: 15px;">
					<tr valign="top">
					</tr>
					<tr valign="top">
						<td align="left" valign="top">
							<iframe id="chart_prov_C_01" scrolling="no" width="100%"
								height="220" border="0" frameborder="0" marginwidth="0"
								marginheight="0" src=""></iframe>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<!-- 表格区 -->
		<tr>
			<td>
				<table cellspacing="0" cellpaddingx="0" style="width: 100%;">
					<tr valign="top">
						<td align="left" colspan="2">
							<iframe id="table_prov_T_01" width="100%" height="250" src=""
								frameborder="0" scrolling="no"></iframe>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<!-- 图表展示区 ,结束-->	
</form>
	<script type="text/javascript">
        domHover(".btn_search", "btn_search_hover");
        domHover(".btn3", "btn3_hover");
        domHover(".btn_iw", "btn_iw_hover");
       domHover(".btn_excel", "btn_excel_hover");
    </script>
</body>
</html>