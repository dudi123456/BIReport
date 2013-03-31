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

		<title>门店汇总</title>


		<%
			ReportQryStruct qryStruct = (ReportQryStruct) session
					.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
			if (qryStruct == null) {
				qryStruct = new ReportQryStruct();
			}
			String ctl_lv1_str=qryStruct.ctl_lvl;
			String prov_code=qryStruct.province_code;
			String AreaSqlAll = "SELECT t.province_code,t.province_name FROM ST_DIM_AREA_PROVINCE t";
			String AreaSqlCity = "select b.city_code,b.city_name from ST_DIM_AREA_PROVINCE a,dim_pub_city b where a.province_code=b.province_code and b.province_code='"+prov_code+"' ";
		%>


		<BIBM:SelfRefreshTag pageNames="TableQryForm.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1" />

<script>
	
	$(document).ready(function() {
		initframe();		
	});
	function initframe(){					
		var tableIframe = document.getElementById("table_prov_T_01");
		//var ctl_lvl='<%=qryStruct.ctl_lvl%>';//用户级别
		
		//if(ctl_lvl=="0"){//总部	
			//tableIframe.src="SubjectCommTable.rptdo?table_id=SOC_CHANNL_GEN_P_T&first=Y&table_height=250";	
		//}else if(ctl_lvl=="1"){//省分	
			//tableIframe.src="SubjectCommTable.rptdo?table_id=SOC_CHANNL_PROV_P_T&first=Y&table_height=250";
		//}else if(ctl_lvl=="2"){//地市
			tableIframe.src="SubjectCommTable.rptdo?table_id=SOC_CHANNL_CITY_P_T&first=Y&table_height=550";
		//}		
	}

	
	
	function doQuery() {	
		TableQryForm.submit();
	}
	
	/*
	**得到默认账期
	*/
	function getOriDate(){
		var oriDate = document.getElementById("qry__rpt_date");
		return oriDate.value;
 	}
 	
	function resetCondition(){
		var oSerMonth = document.getElementById("qry__gather_month");
		oSerMonth.value=getOriDate();
		$("#qry__chain_level")[0].selectedIndex = 0;
	}
	
</script>
	</head>
	<body>
	<form name="TableQryForm" action="SocChannlPD.rptdo" method="post">
	<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">
	<INPUT type=hidden id="oriDate" name="qry__rpt_date"  value="<%=qryStruct.rpt_date%>">
		<div style="display: none; position: absolute;" id=altlayer></div>
		
		<!-- 查询条件区,开始 -->
		<div class="topsearch">
			<table width="60%" border=0 cellpadding="0" cellspacing="0" align="center">
				<tr>
					<td align="right" class="td-bg">
						月份：
					</td>
					<td>
						<input class="Wdate" style="height: 22px; width: 120px;"
							type="text" id="qry__gather_month" name="qry__gather_month"
							value="<%=qryStruct.gather_month%>"
							onFocus="WdatePicker({crossFrame:false,isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})" />
					</td>
					<!-- 
					<%if(ctl_lv1_str.equals("0")){ %>					
                    <td align="center" class="td-bg">省分:</td>
                    <td>
                    <BIBM:TagSelectList  focusID="<%=qryStruct.province_code%>" script="class='easyui-combobox'"  listName="qry__province_code" listID="0" allFlag="" selfSQL="<%=AreaSqlAll%>" />
                    </td>
                    <%}else if(ctl_lv1_str.equals("1")){ %>
                    <td align="center" class="td-bg">地市:</td>
                    <td>
                    <BIBM:TagSelectList  focusID="<%=qryStruct.city_code%>" script="class='easyui-combobox'"  listName="qry__city_code" listID="0" allFlag="" selfSQL="<%=AreaSqlCity%>" />
                    </td>
                    <%} %> -->
                    <td align="right" class="td-bg">
						连锁级别：
					</td>
					<td>
					<BIBM:SelectForCodeList htmlId="qry__chain_level" codeType="<%=WebConstKeys.SOC_CHANNEL_CHAIN_LEVEL %>" 
						value="<%=qryStruct.chain_level %>" htmlName="qry__chain_level"
						disabled="false" hasAll="true" allOptionValue="" allOptionDesc="全部"
						htmlAttr="class='style='width: 120px;'"></BIBM:SelectForCodeList>
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
		<span >门店汇总</span>		
	</div>
		<!-- 图表展示区 ,开始-->
		<table style="width: 100%;" id="content_1" cellspacing="0"
			cellpaddingx="0">
			<!-- 表格区 -->
			<tr>
				<td>
					<table cellspacing="0" cellpaddingx="0" style="width: 100%;">
						<tr valign="top">
							<td align="left" colspan="2">
								<iframe id="table_prov_T_01" width="100%" height="380" src=""
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