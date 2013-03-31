<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import="com.ailk.bi.base.struct.*"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<script language="JavaScript" src="<%=request.getContextPath()%>/js/chart/FusionCharts.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/js/chart/globe.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/js/chart/DataGrid.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/js/chart/Common.css" type="text/css">

<%
String chart_id = request.getParameter("chart_id"); //获取图形ID
if ("".equals(chart_id)){
	out.print("<center>");
	out.print("<br><br>未知图形信息！<br>");
	out.print("</center>");
	return;
}
//查询结构
ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
if (qryStruct == null) {
  qryStruct = new ReportQryStruct();
}
//数据session名称
String chart_dataset_name = request.getParameter("dataset");
String[][] dataset = null;
if(chart_dataset_name!=null&&!"".equals(chart_dataset_name.trim())){
	dataset = (String[][])session.getAttribute(chart_dataset_name);
}
//图形名称
String chart_name = request.getParameter("chart_name");
//图形子标题
String chart_subName = request.getParameter("chart_subName");
//图形类型
String chartType = request.getParameter("chartType");
//图形category_index索引
String category_index = request.getParameter("category_index");
//图形category_desc索引
String category_desc = request.getParameter("category_desc");
//System.out.println("category_desc="+category_desc);
if(category_desc!=null&&!"".equals(category_desc)){
	//category_desc = new String(request.getParameter("category_desc").getBytes("8859_1"), "GB2312");
}
//图形value索引
String value_index = request.getParameter("value_index");
//附加的数据条件
String whereSQL = "";
//图形宽度
String width = request.getParameter("width");
//图形高度
String height = request.getParameter("height");
//后台配置信息ID
String configId = request.getParameter("configId");
//生成Javascript函数名称
String jsfunc_name = request.getParameter("jsfunc_name");
//图形可视
String visible = request.getParameter("visible");
if(visible==null||"".equals(visible)){
	visible = "true";
}
//图形显示的层div id
String render = request.getParameter("render");
//系统路径
String path = request.getParameter("path");
//替换SQL内容
String replace = request.getParameter("replace");
//图形radio名称
String chart_name_r = request.getParameter("chart_name_r");
//是否启用session标记
String session_flag = (String)session.getAttribute("session_flag"+chart_id);
//表格传入条件flag标记
String flag = request.getParameter("flag");
if(flag!=null&&"Y".equals(flag)){
	session.setAttribute("chart_name",request.getParameter("chart_name"+chart_id));
	session.setAttribute("tmpcondition"+chart_id,com.ailk.bi.common.app.StringB.toGB(request.getQueryString()));
	//System.out.println("tmpcondition="+com.ailk.bi.common.app.StringB.toGB(request.getQueryString()));
}
//第一次进入
String first = request.getParameter("first");
if(first!=null&&"Y".equals(first)){
	session.removeAttribute("tmpcondition"+chart_id);
}
//获取解析条件
if(chart_name_r!=null && !"".equals(chart_name_r)){
	String tmpcondition = (String)session.getAttribute("tmpcondition"+chart_id);
	whereSQL = CommConditionUtil.getStringWhere(chart_id,"1",tmpcondition);
	//System.out.println("whereSQL1="+whereSQL);
	String tmpChartType = StringTool.findStrValue(tmpcondition, "&", "chartType");
	if(tmpChartType!=null&&!"".equals(tmpChartType)){
		chartType = tmpChartType;
	}
}

if(chart_name==null||"".equals(chart_name)){
	chart_name = "";
}
if(chart_name_r!=null && !"".equals(chart_name_r)){
	chart_name += " " + chart_name_r;
}
%>
<html>
<body topmargin="0">
<center>
<Tag:Chart chartId="<%=chart_id%>" chartName="<%=chart_name%>" chartSubName="<%=chart_subName%>"
 qryStruct="<%=qryStruct%>" categoryIndex="<%=category_index%>" categoryDesc="<%=category_desc%>"
 whereSQL="<%=whereSQL%>"
 chartType="<%=chartType%>"
 valueIndex="<%=value_index%>"
 dataset="<%=dataset%>"
 width="<%=width%>"
 height="<%=height%>"
 configId="<%=configId%>"
 jsfunc_name="<%=jsfunc_name%>"
 visible="<%=visible%>"
 render="<%=render%>"
 path="<%=path%>"
 replace="<%=replace%>"
/>
</center>
</body>
</html>