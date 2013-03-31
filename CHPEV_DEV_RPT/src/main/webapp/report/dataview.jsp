<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.ailk.bi.common.app.DateUtil"%>
<%@page import="com.ailk.bi.common.dbtools.*"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@ page import="com.ailk.bi.common.dbtools.WebDBUtil"%>
<%@ page import="com.ailk.bi.common.app.AppException"%>

<%@page import="java.sql.*"%>
<html>
<head>
	<title>预览报表数据</title>
</head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<body>
<%
String rpt_id=request.getParameter("rpt_id");
String op_time=request.getParameter("op_time");
String rp_engine=request.getParameter("rp_engine");
if (op_time.length()==6) {
 	op_time=op_time.substring(0,4)+"-"+op_time.substring(4,6);
}else if (op_time.length()==8) {
	op_time=op_time.substring(0,4)+"-"+op_time.substring(4,6)+"-"+op_time.substring(6,8);
}
String[] sql = new String[1];
sql[0] = "insert into UI_RPT_INFO_DATA_QLOG(RPT_ID,OP_TIME,CREATE_DATE,MODIFY_DATE,STATE,SQL_ENGINE_NAME)"
	   + " values ('"+rpt_id+"','"+op_time+"',sysdate,null,'A','rpt_sql_engine_ring')";
System.out.println(sql[0]);

int n = 0;
try {

WebDBUtil.execTransUpdate(sql);
System.out.println(111);

} catch (AppException e) {
	e.printStackTrace();
}
%>
预览任务已经提交,请稍后查看
<hr>

</body>

</html>