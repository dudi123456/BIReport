<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="com.ailk.bi.base.exception.CustomMsuException"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.report.util.CustomMsuUtil"%>
<%@page import="com.ailk.bi.report.struct.MsuTreeNode"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.base.util.TableConsts"%>
<%
	Log log = LogFactory.getLog(CustomMsuUtil.class);
	String action = CommTool.getParameterGB(request, "action");
	String data = CommTool.getParameterGB(request, "data");
	Object tmpObj = session
	.getAttribute(WebKeys.ATTR_REPORT_CUSTOM_MSU_STAT_PERIOD);
	String stat_period = null;
	if (null != tmpObj) {
		stat_period = (String) tmpObj;
	}

	if (null != action && !"".equals(action) && null != data
	&& !"".equals(data) && null != stat_period
	&& !"".equals(stat_period)) {
		String period = TableConsts.STAT_PERIOD_MONTH;
		if (null != stat_period && !"".equals(stat_period)) {
	period = stat_period;
		}
		MsuTreeNode node = CustomMsuUtil.convertJSonToTreeNode(data);
		if (null != node) {
	try {
		      String back = CustomMsuUtil
		              .convertTreeNodesToJSon(CustomMsuUtil
		              .getChildrenNodes(node.getWidgetId(),
		                  period));
		      if (null != back && !"".equals(back)) {
	       out.println(back);
		      }
	} catch (CustomMsuException cme) {
		    log.error(cme);
	}
		}
	}
%>


