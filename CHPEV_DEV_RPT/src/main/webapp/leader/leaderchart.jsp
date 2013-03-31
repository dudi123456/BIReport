<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@ page import="com.ailk.bi.base.common.CSysCode,com.ailk.bi.base.struct.LeaderQryStruct,com.ailk.bi.leader.struct.LeaderKpiInfoStruct,com.ailk.bi.base.util.*,com.ailk.bi.base.util.CommTool,com.ailk.bi.common.app.StringB,com.ailk.bi.common.app.FormatUtil,com.ailk.bi.base.struct.*,com.ailk.bi.leader.util.*" %>
<%
	String path = request.getContextPath();
    int screenx = Integer.parseInt(session.getAttribute(WebKeys.Screenx) == null ? "1024" : (String) session.getAttribute(WebKeys.Screenx));
    String width = screenx * 0.45 + "";

    //查询结果对象
    LeaderQryStruct qryStruct = (LeaderQryStruct) session.getAttribute(WebKeys.ATTR_ailk_QRY_STRUCT);
    if (qryStruct == null) {
        qryStruct = new LeaderQryStruct();
    }

    //当前分析指标
    LeaderKpiInfoStruct kpiStruct = null;
    String msuId = CommTool.getParameterGB(request, "msu_id");

    if (msuId == null || "".equals(msuId)) {
        kpiStruct = (LeaderKpiInfoStruct) session.getAttribute(WebKeys.ATTR_LEADER_KPI_INFO_STRUCT_FIRST);
    } else {
        kpiStruct = LeaderKpiUtil.getKpiInfoStruct(msuId);
    }
    if (kpiStruct == null) {
        kpiStruct = new LeaderKpiInfoStruct();
    }
	//当前数据区域
    String cityId = request.getParameter("city_id");
    if (cityId != null && !"".equals(cityId)) {
        qryStruct.city_id = cityId;
    }else{
    	qryStruct.city_id = "";
    }

	//查询结构
    ReportQryStruct chartStruct = new ReportQryStruct();
    chartStruct.gather_day = qryStruct.gather_day;
    chartStruct.gather_month = qryStruct.gather_mon;
    chartStruct.city_id = qryStruct.city_id;
    chartStruct.msu_id = kpiStruct.msu_id;
    session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, chartStruct);

    //放大显示
    String ajax_flag = CommTool.getParameterGB(request, "ajax_flag");
    if (ajax_flag == null || "".equals(ajax_flag)) {
        ajax_flag = "false";
    }
    if ("true".equalsIgnoreCase(ajax_flag)) {
    	width = screenx * 0.75 + "";
    }

    //根据指标周期判断图形
    String chart_id = "";
    String chartUrl = "";
    if (SysConsts.STAT_PERIOD_DAY.equals(kpiStruct.msu_cycle)) {
    	chart_id = "leader_chart_day";
    	chartUrl = "SubjectCommChart.screen?chart_id="+chart_id+"&width="+width+"&first=Y&gather_day="+chartStruct.gather_day+"&msu_id="+chartStruct.msu_id+"&city_id="+chartStruct.city_id;
    }
    if (SysConsts.STAT_PERIOD_MONTH.equals(kpiStruct.msu_cycle)) {
    	chart_id = "leader_chart_month";
    	chartUrl = "SubjectCommChart.screen?chart_id="+chart_id+"&width="+width+"&first=Y&gather_month="+chartStruct.gather_month+"&msu_id="+chartStruct.msu_id+"&city_id="+chartStruct.city_id;
    }
	if(chart_id==null||"".equals(chart_id)){
		out.print("<center>");
		out.print("<br><br>操作信息丢失，请联系系统管理员！<br>");
		out.print("</center>");
		return;
	}

%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/bimain.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/chart/Common.css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/chart/FusionCharts.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/chart/globe.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/chart/DataGrid.js"></script>
    <title><%=CSysCode.SYS_TITLE%></title>
</head>
<body>
<table style="width:100%">
    <%if ("false".equalsIgnoreCase(ajax_flag)) { %>
    <tr>
        <td height="13" valign="top" width="50%">
            <table style="width:100%">
                <tr>
                    <td align="right">
                        <input type="image" src="<%=request.getContextPath()%>/images/common/leader/magnifier.jpg" name="bigg" value="放大"
                               onclick="parent.ZoomOut('<%=kpiStruct.msu_id%>');return false;"/></td>
                </tr>
            </table>
        </td>
    </tr>
	<%} if ("true".equalsIgnoreCase(ajax_flag)) {%>
    <tr>
        <td height="13" valign="bottom" align="right">
            <a href="javascript:dlgChart.hide();" class="chart-zoom" style="cursor:hand"><strong>还原</strong></a>
        </td>
    </tr>
    <%} %>
    <tr>
        <td align="center">
        <iframe id="chart_<%=chart_id%>"
				scrolling="no" width="<%=width%>" height="195" border="0" frameborder="0"
				marginwidth="0" marginheight="0" src="<%=chartUrl%>"></iframe>
        </td>
    </tr>
</table>
</body>
</html>