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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/Scroll.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dd99.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/chart.js" ></script>
<style type="text/css">
.a1:link{
	color: #D71920;
	text-decoration:none;
}
.a1:visited{
	color: #D71920;
	text-decoration:none;
}
.a1:hover{
	color: #D71920;
	text-decoration:underline;
}
.a1:active{
	color: #D71920;
	text-decoration:none;
}
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>widget</title>
</head>

<body>
<FORM name="TableQryForm" action="">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td align="right">指标：</td>
    	<td><%=PageConditionUtil.getChartFunction(chart_id,"select","2","TableQryForm",10) %></td>
	</tr>
	<tr>
	<td align="center" valign="top" colspan="2"><iframe id="chart_<%=chart_id%>" 
				scrolling="no" width="500" height="170" border="0" frameborder="0"
				marginwidth="0" marginheight="0" src="SubjectCommChart.screen?chart_id=<%=chart_id%>&chart_name_r=网上用户数&first=Y<%=addinfo%>"></iframe></td>
		
	</tr>
	<tr>
    	<td height="20px" colspan="2" align="right"><a class="a1" href="javascript:changeMain();">更多>></a>&nbsp;&nbsp;</td>
		</td>
	</tr>
</table>
</FORM>
</body>
<script type="text/javascript">
function changeMain(){
	var obj = document.getElementById("testOne");
	var selectedOptionValue = obj.options[obj.selectedIndex].value;
	var selectedKpiId = selectedOptionValue.split("_")[0];
	var selectedKpiCycle = selectedOptionValue.split("_")[1];
	var selectedKpiIndex = selectedOptionValue.split("_")[2];
	var url = '/VGOP/portletKpiView.do?action=viewLeaderKpiNew&thisKpiId='+selectedKpiId+'&thisKpiCycle='+selectedKpiCycle+'&selectedOne='+selectedKpiIndex;
	top.UIManager.Tab.open(url,"","领导门户","");
}
</script>
</html>