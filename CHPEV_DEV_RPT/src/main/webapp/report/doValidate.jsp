<%@ page import="java.util.*" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.report.dao.ILReportSelectDataDao"%>
<%@page import="com.ailk.bi.report.dao.impl.LRepoerSelectDataImpl"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.common.app.ReflectUtil"%>
<%@page import="com.ailk.bi.common.app.StringB"%>
<%@page import="com.ailk.bi.pages.PagesInfoStruct"%>
<%@page import="com.ailk.bi.pages.WebPageTool"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@page import="com.ailk.bi.report.domain.RptFilterTable"%>
<%@page import="com.ailk.bi.report.util.ReportConsts"%>
<%@page import="com.ailk.bi.report.util.ReportObjUtil"%>
<%@page import="com.ailk.bi.SysConsts"%>

<%
/**
 * 实现报表中的条件联动效果（可支持多级联动）
 * @author  方慧
 * @version  [版本号, 2012-04-01]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
response.setContentType("text/xml");
response.setHeader("Cache-Control","no-store");
response.setHeader("Pragma","no-cache");
response.setDateHeader("Expires",0);
String fieldValue = request.getParameter("fieldValue");//获得条件值
String fieldName  = request.getParameter("fieldName"); //获得条件名称
String childId    = request.getParameter("childId");   //需要联动的名称
String childIndex = childId.substring(8);
int index =Integer.parseInt(childIndex)-1;
String result=",全部";
RptFilterTable[] rptFilterTables = (RptFilterTable[])session.getAttribute(WebKeys.ATTR_REPORT_FILTERS);
String sql = rptFilterTables[index].filter_sql;
if(!"".equals(fieldValue)){
	sql = rptFilterTables[index].filter_sql+" and " + fieldName + " = '" + fieldValue+"'";
	ILReportSelectDataDao dao  = new LRepoerSelectDataImpl();
	String resultStr = dao.getListItem(sql);
	if(!"".equals(resultStr)){
		result+=";"+resultStr;
	}
}
response.getWriter().write(result);
%>