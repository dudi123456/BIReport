<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="com.ailk.bi.syspar.action.ParamRadioHTMLAction"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import = "com.ailk.bi.base.util.*" %>
<%@ page import = "com.ailk.bi.report.struct.ReportQryStruct" %>
<%
ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
if(qryStruct == null){
	qryStruct = new ReportQryStruct();
}
String node_id = request.getParameter("node_id");
String en_name = request.getParameter("en_name");
String qry_radio_id = request.getParameter("qry_radio_id");
String qry_radio_name = ParamRadioHTMLAction.qryItemName(node_id,en_name,qry_radio_id);
%>
<%=qry_radio_name%>