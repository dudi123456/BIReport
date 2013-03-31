<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import = "com.ailk.bi.base.util.*" %>

<%
String chart_id = request.getParameter("chart_id");
if(chart_id==null||"".equals(chart_id)){
	out.print("<center>");
	out.print("<br><br>操作信息丢失！<br>");
	out.print("</center>");
	return;
}
String addinfo = request.getParameter("addinfo");
if(addinfo==null){
	addinfo = "";
}
%>
<html>
<meta http-equiv="Content-Language" content="zh-cn">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/newmain.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/titleStyle.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/chart/FusionCharts.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/Scroll.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dd99.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/chart.js" ></script>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>图形预览</title>
</head>

<body>
<FORM name="TableQryForm" action="">
<table cellpadding="0" cellspacing="5" width="100%">
	<tr>
    	<td height="20" colspan="2">选择变化请查看源码</td>
	</tr>
	<tr>
		<td align="right">单选改变图形</td>
    	<td><%=PageConditionUtil.getChartFunction(chart_id,"radio","2","TableQryForm",10) %></td>
	</tr>
	<tr>
		<td align="right">多选改变图形</td>
    	<td><%=PageConditionUtil.getChartFunction(chart_id,"checkbox","2","TableQryForm",10) %></td>
	</tr>
	<tr>
		<td align="right">下拉框改变图形</td>
    	<td><%=PageConditionUtil.getChartFunction(chart_id,"select","2","TableQryForm",10) %></td>
	</tr>
	<tr>
	<td align="center" valign="top" colspan="2"><iframe id="chart_<%=chart_id%>"
				scrolling="no" width="1000" height="500" border="0" frameborder="0"
				marginwidth="0" marginheight="0" src="SubjectCommChart.screen?chart_id=<%=chart_id%>&first=Y<%=addinfo%><%=PageConditionUtil.getChartDefaultCheck(chart_id)%>"></iframe></td>

	</tr>
	<tr>
    	<td height="20" colspan="2">
		</td>
	</tr>
</table>
</FORM>
</body>
</html>