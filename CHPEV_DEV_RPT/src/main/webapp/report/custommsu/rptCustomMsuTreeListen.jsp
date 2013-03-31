<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.ailk.bi.common.app.StringB"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="com.ailk.bi.base.exception.CustomMsuException"%>
<%@page import="com.ailk.bi.report.util.CustomMsuUtil"%>
<%@page import="com.ailk.bi.report.struct.MsuTreeNode"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.system.facade.impl.*"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%
	Log log = LogFactory.getLog(CustomMsuUtil.class);
	//获取用户ID
	String userId = CommonFacade.getLoginId(session);

	String action = CommTool.getParameterGB(request, "action");
	String data = CommTool.getParameterGB(request, "data");
	MsuTreeNode node = CustomMsuUtil.convertJSonToTreeNode(data);
	if (null != node && null != action) {
		String period = node.getObjectId();
		String[] values = period.split("\\|");
		period = values[0];
		String baseMsuId = node.getWidgetId();
		baseMsuId = StringB.replaceFirst(baseMsuId,"C_","");
		try {
	String back = CustomMsuUtil
	.convertTreeNodesToJSon(CustomMsuUtil
	.getCustomChildrenNodes(userId, baseMsuId,
			period));
	if (null != back && !"".equals(back)) {
		out.println(back);
	}
		} catch (CustomMsuException cme) {
	log.error(cme);
		}

	}
%>
