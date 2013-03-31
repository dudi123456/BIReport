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

		<title>省分评价信息</title>


		<%
			ReportQryStruct qryStruct = (ReportQryStruct) session
					.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
			if (qryStruct == null) {
				qryStruct = new ReportQryStruct();
			}
			String AreaSqlAll = "SELECT t.province_code,t.province_name FROM ST_DIM_AREA_PROVINCE t";
			String AreaSqlNorth = "SELECT t.province_code,t.province_name FROM ST_DIM_AREA_PROVINCE t where t.geo_flag='1'";
			String AreaSqlSouth = "SELECT t.province_code,t.province_name FROM ST_DIM_AREA_PROVINCE t where t.geo_flag='0'";
			String classifySelectSql="select * from st_eva_all_para u where u.supper_id='A01' ";
		%>


		<BIBM:SelfRefreshTag pageNames="TableQryForm.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1" />

<script>
	
	$(document).ready(function() {
		initframe();		
	});
	function initframe(){			
		//var proTagN = document.getElementById("qry_Area_list_N");
		//var proTagS = document.getElementById("qry_Area_list_S");
		//proTagS.style.display="none";	
		//proTagN.style.display="none";	
		
		var chartIframe = document.getElementById("chart_prov_C_01");
		var tableIframe = document.getElementById("table_prov_T_01");
		var classifySelect='<%=qryStruct.classifySelect%>';

		if(classifySelect==""){//全部
			chartIframe.src="SubjectCommChart.screen?chart_id=CHPEV_DEV_RPT_001_1_C_1";								
			tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_1&first=Y&table_height=250";				
		}else if(classifySelect=="A0101"){//产能
			chartIframe.src="SubjectCommChart.screen?chart_id=CHPEV_DEV_RPT_001_2_C_1";								
			tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_2&first=Y&table_height=250";		
		}else if(classifySelect=="A0102"){//质量
			chartIframe.src="SubjectCommChart.screen?chart_id=CHPEV_DEV_RPT_001_3_C_1";								
			tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_3&first=Y&table_height=250";		
		}else{//效能A0103
			chartIframe.src="SubjectCommChart.screen?chart_id=CHPEV_DEV_RPT_001_4_C_1";								
			tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_4&first=Y&table_height=250";
		}
	}
	
	function areaChange(obj) {
		//var proTagA = document.getElementById("qry_Area_list_A");
		//var proTagN = document.getElementById("qry_Area_list_N");
		//var proTagS = document.getElementById("qry_Area_list_S");

		//if(obj.value==""){//全部
			//proTagA.style.display="inline";
			//proTagN.style.display="none";
			//proTagS.style.display="none";
		//}else if(obj.value=="0"){//南方
			//proTagA.style.display="none";
			//proTagN.style.display="none";
			//proTagS.style.display="inline";
		//}else{//北方
			//proTagA.style.display="none";
			//proTagN.style.display="inline";
			//proTagS.style.display="none";
		//}
				
	}
	function resetCondition(){
		$("#qry__right_attach_region")[0].selectedIndex = 0;
		$("#qry__classifySelect")[0].selectedIndex = 0;
		document.getElementById("qry__gather_month").value = document.getElementById("oriDate").value;		
	}
	
	function classifyChange(obj){	
		var chartIframe = document.getElementById("chart_prov_C_01");	
		var tableIframe = document.getElementById("table_prov_T_01");
		if(obj.value==""){//全部
			chartIframe.src="SubjectCommChart.screen?chart_id=CHPEV_DEV_RPT_001_1_C_1";								
			tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_1&first=Y&table_height=250";
		}else if(obj.value=="A0101"){//产能
			chartIframe.src="SubjectCommChart.screen?chart_id=CHPEV_DEV_RPT_001_2_C_1";								
			tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_2&first=Y&table_height=250";
		}else if(obj.value=="A0102"){//质量
			chartIframe.src="SubjectCommChart.screen?chart_id=CHPEV_DEV_RPT_001_3_C_1";								
			tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_3&first=Y&table_height=250";
		}else{//效能A0103
			chartIframe.src="SubjectCommChart.screen?chart_id=CHPEV_DEV_RPT_001_4_C_1";								
			tableIframe.src="SubjectCommTable.rptdo?table_id=CHPEV_DEV_RPT_001_4&first=Y&table_height=250";
		}
		
	}
	
	function doQuery() {	
		TableQryForm.submit();
		
	}
	
	function subgo(){
		//南北方标识 1:北方；0:南方
		var geo_flag = document.getElementById("qry__right_attach_region").value;
		window.open("Regional.rptdo?optype=mark_right&geo_flag="+geo_flag,"评分指标及权重","");
	}
	
	function export_table_content(){
		var ctl_lvl='<%=qryStruct.ctl_lvl%>';//用户级别
		var province_code='<%=qryStruct.right_metro_id%>';//用户所在省分
		var acct_month=document.getElementById("qry__gather_month").value;
		
		//var tableName="省分指标明细下载";
		//tableName=encodeURI(encodeURI(tableName));
		var tableName="下载指标明细数据";
		if("0"==ctl_lvl){
			tableName= "全国"+tableName;
		}else{
			tableName= "::province_code::区间"+tableName;
		}
		tableName=encodeURI(encodeURI(tableName));
		if(ctl_lvl=="0"){//总部
			window.open("ExportTools.rptdo?exportId=Province_Detail&tableName="+tableName+"&acct_month="+acct_month+"&sqlArgs="+acct_month,"数据导出","");
		}else if(ctl_lvl=="1"){//省分
			window.open("ExportTools.rptdo?exportId=Province_History&tableName="+tableName+"&province_code="+province_code+"&sqlArgs="+province_code,"数据导出","");
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
			<table width="80%" border=0 cellpadding="0" cellspacing="0" align="center">
				<tr>
					<td align="right" class="td-bg">
						考核区域：
					</td>
					<td>
					<BIBM:SelectForCodeList htmlId="qry__right_attach_region" codeType="<%=WebConstKeys.SUBJECT_CHANNEL_AREA_DEF %>" 
						value="<%=qryStruct.right_attach_region %>" htmlName="qry__right_attach_region"
						disabled="false" hasAll="false" allOptionValue="" allOptionDesc="全部"
						htmlAttr="class='style='width: 120px;'"></BIBM:SelectForCodeList>	
					</td>
					<!-- 查询条件区,开始 
                    <td align="center" class="td-bg">省分:</td>
                    <td >
                    <BIBM:TagSelectList  focusID="<%=qryStruct.tactic_type%>" script="class='easyui-combobox'"	listName="qry_Area_list_A" listID="0" allFlag="" selfSQL="<%=AreaSqlAll%>" />
                    <BIBM:TagSelectList  focusID="<%=qryStruct.tactic_type%>" script="class='easyui-combobox'"	listName="qry_Area_list_N" listID="0" allFlag="" selfSQL="<%=AreaSqlNorth%>" />
                    <BIBM:TagSelectList  focusID="<%=qryStruct.tactic_type%>" script="class='easyui-combobox'"	listName="qry_Area_list_S" listID="0" allFlag="" selfSQL="<%=AreaSqlSouth%>" />
                    </td> -->
					<td align="right" class="td-bg">
						月份：
					</td>
					<td>
						<input class="Wdate" style="height: 22px; width: 120px;"
							type="text" id="qry__gather_month" name="qry__gather_month"
							value="<%=qryStruct.gather_month%>"
							onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})" />
					</td>
					<td align="right" class="td-bg">
						考评分类：
					</td>
					<td>
					<BIBM:TagSelectList  focusID="<%=qryStruct.classifySelect%>" script="class='easyui-combobox'"	listName="qry__classifySelect" listID="0" allFlag="" selfSQL="<%=classifySelectSql%>" />
					</td>
					<td>
						<input id="button_submit" type="button" class="btn_search"
							value="查询" onClick="doQuery()" />
					</td>
					<td>
						<input type="button" class="btn3" value="重置" onclick="resetCondition();"/>
					</td>
				</tr>
			</table>
			<!-- 查询条件区,结束 -->
		</div>
	<div class="result_title">
		<span >省分排名</span>		
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
	<!-- 
			<tr valign="top" id="add_type_1">
				<td>
					<table cellspacing="0" cellpaddingx="0" style="width: 100%;">
						<tr valign="top">
							<td align="left">
								省分排名&&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<a href='javascript:;' onclick="subgo()">评分指标及权重</a>&nbsp;&nbsp;
							</td>
						</tr>
					</table>
				</td>
			</tr>
-->
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

	<script type="text/javascript">
        domHover(".btn_search", "btn_search_hover");
        domHover(".btn3", "btn3_hover");
        domHover(".btn_iw", "btn_iw_hover");
       domHover(".btn_excel", "btn_excel_hover");
    </script>
	</form>
	</body>
</html>