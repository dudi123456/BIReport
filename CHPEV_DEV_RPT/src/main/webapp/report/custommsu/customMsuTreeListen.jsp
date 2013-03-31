<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="com.ailk.bi.base.exception.CustomMsuException"%>
<%@page import="com.ailk.bi.base.util.TableConsts"%>
<%@page import="com.ailk.bi.report.util.CustomMsuUtil"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%@page import="com.ailk.bi.report.struct.MsuTreeNode"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%
	Log log = LogFactory.getLog(CustomMsuUtil.class);
	//��峰����ㄦ��ID
	String userId = CommonFacade.getLoginId(session);

	String action = CommTool.getParameterGB(request, "action");
	String data = CommTool.getParameterGB(request, "data");
	MsuTreeNode node = CustomMsuUtil.convertJSonToTreeNode(data);
	if (null != node && null != action) {
		String period_type = node.getWidgetId();
		String period = TableConsts.STAT_PERIOD_MONTH;
		if ("MONTH_TYPE".equalsIgnoreCase(period_type)) {
	period = TableConsts.STAT_PERIOD_MONTH;
		} else {
	period = TableConsts.STAT_PERIOD_DAY;
		}
		try {
	String back = CustomMsuUtil
	.convertTreeNodesToJSon(CustomMsuUtil
	.genCustomChildrenNodes(userId, period));
	if (null != back && !"".equals(back)) {
		out.println(back);
	}
		} catch (CustomMsuException cme) {
	log.error(cme);
		}

	}
%>


