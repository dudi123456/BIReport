<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="waf.controller.web.action.HTMLActionException"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="com.ailk.bi.olap.action.ReportOlapTableHTMLAction"%>
<%@page contentType="application/vnd.ms-excel;charset=UTF-8"%>
<%
  response.setHeader("Content-Disposition", "attachment;filename=olapExport.xls");
  response.setHeader("Pragma", "No-cache");
  response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
  response.setDateHeader("Expires", 1);
%>
<%
	Log log = LogFactory.getLog(ReportOlapTableHTMLAction.class);
	String warnPage = "htmlError.screen";
	String reportId = request.getParameter("report_id");
	if (null == reportId || "".equals(reportId)) {
		HTMLActionException he = new HTMLActionException(session,
		HTMLActionException.WARN_PAGE,
		"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
				+ "由此造成对您的工作不变，深表歉意！");
		log.error(he);
		response.sendRedirect(warnPage);
		return;
	}
	Object tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_TABLE_EXPORT
			+ "_" + reportId);
	if (null == tmpObj) {
		HTMLActionException he = new HTMLActionException(session,
		HTMLActionException.WARN_PAGE,
		"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
				+ "由此造成对您的工作不变，深表歉意！");
		log.error(he);
		response.sendRedirect(warnPage);
		return;
	}
	String[] HTML = (String[]) tmpObj;
	if (null != HTML) {
		for (int i = 0; i < HTML.length; i++)
			out.println(HTML[i]);
	}
%>
