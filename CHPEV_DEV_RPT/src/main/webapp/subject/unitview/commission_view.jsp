<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.marketing.entity.TacticInfo"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="java.util.*"%>
<%@ include file="/base/commonHtml.jsp"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="Content-Language" content="zh-cn">
		<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/css/other/css1.css">
		<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/css/other/selfcss1.css">

		<title>佣金视图</title>

		<%
		    ReportQryStruct qryStruct = (ReportQryStruct) session
							.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
					if (qryStruct == null) {
						qryStruct = new ReportQryStruct();
					}
					String ChnlTypeSql = "SELECT  distinct a.channel_type_lvl3 as CHNL_ID, substr(a.channel_type_lvl3_name,instr(a.channel_type_lvl3_name,'-') + 1) as CHNL_NAME FROM DIM_CHANNEL_TYPE a where a.channel_type_lvl3 LIKE '1010%'";
		%>



		<script>
	$(document).ready(function() {
		initframe();
	});

	function initframe() {
		loadFrameByLevel()
	}

	function loadFrameByLevel() {

		var chartIframe = document.getElementById("com_income_ratio_C_01");
		var chartIframe2 = document.getElementById("comm_xs_comm_01");
		var chartIframe3 = document.getElementById("comm_type_01");
		var tableIframe = document.getElementById("table_prov_T_09");
		var tableIframe2 = document.getElementById("xs_comm_table_1");
		var tableIframe3 = document.getElementById("comm_type_table_1");
		var isMrg = document.getElementById("isMrg01");
		if (isMrg.value == "true") {
			chartIframe.src = "SubjectCommChart.screen?chart_id=self_comm_ratio_C_2";
			chartIframe2.src = "SubjectCommChart.screen?chart_id=self_xs_comm_02";
			chartIframe3.src = "SubjectCommChart.screen?chart_id=self_comm_type_02";
			tableIframe.src = "SubjectCommTable.rptdo?table_id=self_comm_ratio_T_2&first=Y&table_height=250&with_bar=N&setParentHight=N";
			tableIframe2.src = "SubjectCommTable.rptdo?table_id=self_xs_comm_T_2&first=Y&table_height=250&with_bar=N&setParentHight=N";
			tableIframe3.src = "SubjectCommTable.rptdo?table_id=self_comm_type_T_2&first=Y&table_height=250&with_bar=N&setParentHight=N";
		} else {
			chartIframe.src = "SubjectCommChart.screen?chart_id=self_comm_ratio_C_1";
			chartIframe2.src = "SubjectCommChart.screen?chart_id=self_xs_comm_01";
			chartIframe3.src = "SubjectCommChart.screen?chart_id=self_comm_type_01";
			tableIframe.src = "SubjectCommTable.rptdo?table_id=self_comm_ratio_T_1&first=Y&table_height=250&with_bar=N&setParentHight=N";
			tableIframe2.src = "SubjectCommTable.rptdo?table_id=self_xs_comm_T_1&first=Y&table_height=250&with_bar=N&setParentHight=N";
			tableIframe3.src = "SubjectCommTable.rptdo?table_id=self_comm_type_T_1&first=Y&table_height=250&with_bar=N&setParentHight=N";
		}

	}

	/*
	 **  链接跳转函数.
	 */
	function callOtherURL() {
		alert(111)
		var strUrl = "Weight.rptdo?ioid_id0=B0201";
		var newsWin = window.open(strUrl);
		if (newsWin != null) {
			newsWin.focus();
		}
	}
</script>
	</head>

	<body>

		<FORM name="CommView" action="CommView.rptdo"
			style="background-color: rgb(248, 245, 248)">
			<INPUT type=hidden id="qry__chnl_id" name='qry__chnl_id'
				value="<%=qryStruct.chnl_id%>" />
			<INPUT type=hidden id="isMrg01" value="<%=qryStruct.isMrg%>" />
			<!-- 查询条件区,开始 -->
			<div>


			</div>
			<!-- 查询条件区,结束 -->
			<div class="Template_chaxun"
				style="border-top: 1px solid #d0c7ca; height: 30px;">
				<ul>
					<li>
						渠道编码：
					</li>
					<li><%=qryStruct.chnl_id%></li>
					<li style="margin-left: 50px;">
						渠道名称：
					</li>
					<li><%=qryStruct.chnl_name%></li>
				</ul>
			</div>
			<div class="jieguo">

				<div class="jieguo_tai">
					<ul>
						<li class="ba_shenqlb">
							佣金和收入的占收比
						</li>
					</ul>
				</div>
				<div class="chart">
					<div class="chart_A" style="height: 180px">


						<iframe id="table_prov_T_09" width="100%" style="height: 180px"
							src="" frameborder="0" scrolling="no"></iframe>
					</div>
					<div class="chart_B">
						<iframe id="com_income_ratio_C_01" scrolling="no" width="100%"
							height="220" border="0" frameborder="0" marginwidth="0"
							marginheight="0" src=""></iframe>
					</div>
					<div class="jieguo_tai">
						<ul>
							<li class="ba_shenqlb">
								本期销售佣金
							</li>
						</ul>
					</div>
					<div class="chart_bingz_list" style="height: 190px" width="100">
						<iframe id="xs_comm_table_1" width="250" height="190" src=""
							frameborder="0" scrolling="no"></iframe>
					</div>
					<div class="chart_bingz_tu">
						<iframe id="comm_xs_comm_01" scrolling="no" width="100%"
							height="190" border="0" frameborder="0" marginwidth="0"
							marginheight="0" src=""></iframe>
					</div>
					<div class="jieguo_tai">
						<ul>
							<li class="ba_shenqlb">
								本期佣金类型
							</li>
						</ul>
					</div>
					<div class="chart_bingz_list" style="height: 190px" width="100">
						<iframe id="comm_type_table_1" width="250" height="190" src=""
							frameborder="0" scrolling="no"></iframe>
					</div>
					<div class="chart_bingz_tu">
						<iframe id="comm_type_01" scrolling="no" width="100%" height="190"
							border="0" frameborder="0" marginwidth="0" marginheight="0"
							src=""></iframe>
					</div>
					<!-- 图表展示区 ,开始-->

				</div>
			</div>
		</FORM>
		<!-- 图表展示区 ,结束-->
		<script type="text/javascript">
	domHover(".btn_search", "btn_search_hover");
	domHover(".btn3", "btn3_hover");
	domHover(".btn_iw", "btn_iw_hover");
</script>

	</body>
</html>
