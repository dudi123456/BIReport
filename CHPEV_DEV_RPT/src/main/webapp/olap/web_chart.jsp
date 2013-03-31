<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.olap.util.RptOlapConsts"%>
<%@page import="com.ailk.bi.common.chart.CreateChartObj"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="com.ailk.bi.olap.action.ReportOlapChartHTMLAction"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="waf.controller.web.action.HTMLActionException"%>
<%@ include file="/base/commonHtml.jsp"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
	response.setDateHeader("Expires", 1);
%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/chart/FusionCharts.js"></script>
<%
	Log log = LogFactory.getLog(ReportOlapChartHTMLAction.class);
%>
<%
	//没有登陆
	if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,
			response)) {
		response.sendRedirect(context + "/index.jsp");
		return;
	}
%>
<%
	String warnPage = "htmlError.screen";
	//没有表报标识
	String reportId = (String) request.getParameter("report_id");
	if (null == reportId || "".equals(reportId)) {
		HTMLActionException he = new HTMLActionException(session,
		HTMLActionException.WARN_PAGE,
		"报表标示丢失！");
		log.error(he);
		response.sendRedirect(warnPage);
		return;
	}
%>
<%
	Map chartNames = new HashMap();
	chartNames.put(RptOlapConsts.OLAP_FUN_LINE, "趋势图");
	chartNames.put(RptOlapConsts.OLAP_FUN_BAR, "对比图");
	chartNames.put(RptOlapConsts.OLAP_FUN_PIE, "占比图");
	String chartType = (String) request.getParameter("chart_type");
	if (null == chartType || "".equals(chartType)) {
		HTMLActionException he = new HTMLActionException(session,
		HTMLActionException.WARN_PAGE,
		"图形类型未知！");
		log.error(he);
		response.sendRedirect(warnPage);
		return;
	}
	Object tmpObj = null;
	tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_CHART_DOMAINS_OBJ
			+ "_" + reportId);
	if (null == tmpObj) {
		return;
	}
	List cater = new ArrayList();
	List aryIndex = new ArrayList();
	List chartStructs = (List) tmpObj;
	String chartName = "";
	boolean hasGroup = false;
	int index = -1;
	Iterator iter = chartStructs.iterator();
	while (iter.hasNext()) {
		RptOlapChartAttrStruct chartStruct = (RptOlapChartAttrStruct) iter
		.next();
		if (chartStruct.isDim()) {
			if (chartStruct.isDisplay()) {
		if (!chartStruct.isTime()
				&& chartStruct.isUsedForGroup()) {
			hasGroup = true;
			RptOlapDimTable rptDim = (RptOlapDimTable) chartStruct
			.getRptStruct();
			chartName += rptDim.dimInfo.dim_name + "系列下";
		}
			}
			if (chartStruct.isUsedForGroup()
			&& (chartStruct.isDisplay() || (!chartStruct
			.isDisplay() && RptOlapConsts.OLAP_FUN_LINE
			.equals(chartType)))) {
		index++;
		index++;
			}
		} else {
			RptOlapMsuTable rptMsu = (RptOlapMsuTable) chartStruct
			.getRptStruct();
			if (chartStruct.isDisplay()) {
		index++;
		String msuName = (null == rptMsu.col_name || ""
				.equals(rptMsu.col_name)) ? rptMsu.msuInfo.msu_name
				: rptMsu.col_name;
		cater.add(msuName);
		aryIndex.add(index + "");
		chartName += msuName;
			}
		}
	}
	chartName += (String) chartNames.get(chartType);
	String[] names = (String[]) cater.toArray(new String[cater.size()]);
	String[] indexs = (String[]) aryIndex.toArray(new String[aryIndex
			.size()]);
%>
<%
	tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_TABLE_CONTENT + "_"
			+ reportId);
	if (null != tmpObj) {
		String[][] svces = (String[][]) tmpObj;
		String type = "";

		String[] series = null;
		String[] seriesI = null;
		String[] category = null;
		String[][] valuearr = null;
		if (RptOlapConsts.OLAP_FUN_LINE.equals(chartType)) {
			if (hasGroup){
				series = CreateChartObj.getFlashChartObjCategory(svces,3,0,4,"category");
				category = CreateChartObj.getFlashChartObjCategory(svces,3,0,4,"series");
				valuearr = CreateChartObj.getFlashChartObjValue(svces,3,0,4);
			}else {
				series = new String[] { "" };
				category = CreateChartObj.getFlashChartObjCategory(svces,-1, 0, 2,"series");
				valuearr = CreateChartObj.getFlashChartObjValue(svces,-1, 0, 2);
			}
			type = RptOlapConsts.LINE_CHART_FLAT_FLASH;
		} else if (RptOlapConsts.OLAP_FUN_BAR.equals(chartType)) {
			if (hasGroup){
				series = names;
				seriesI = indexs;
				category = CreateChartObj.getFlashChartObjCategory(svces,-1,1,-1,"series");
				valuearr = CreateChartObj.getFlashChartObjValue(svces, series, seriesI, 1);
			}else {
				series = names;
				seriesI = indexs;
				category = new String[] { "" };
				valuearr = CreateChartObj.getFlashChartObjValue(svces, series, seriesI, -1);
			}
			type = RptOlapConsts.BAR_CHART_FLAT_FALSH;
		} else if (RptOlapConsts.OLAP_FUN_PIE.equals(chartType)) {
			if (hasGroup){
			category = CreateChartObj.getFlashChartObjPie(svces,1,2,"series");
			valuearr = CreateChartObj.getFlashChartObjPieValue(svces,1,1,2);
			}else{
			series = names;
			seriesI = indexs;
			valuearr = CreateChartObj.getFlashChartObjPieValue(svces,series,seriesI);
			}
			type = RptOlapConsts.PIE_CHART_FLAT_FLASH;
		}
%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.ailk.bi.olap.domain.RptOlapChartAttrStruct"%>
<%@page import="com.ailk.bi.base.table.RptOlapMsuTable"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.ailk.bi.base.table.RptOlapDimTable"%>
<body style="background-color=transparent">
<div id="chartdiv" style="padding-top:5px"></div>
<BIBM:FlashChart caption="" subcaption="" categories="<%=category%>"
                seriesname="<%=series%>" dataset="<%=valuearr%>" width="520"
                height="360" configId="<%=type%>" jsfunc_name="showChart1"
                render="chartdiv" chartType="<%=type%>" visible="true"
                path="<%=request.getContextPath()%>"/>
</body>
<%
}else{
%>
	<body>
		<div id="maincontent">
		</div>
	</body>
<%}%>
</html>