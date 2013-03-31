<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.ailk.bi.report.struct.AjaxResultInfo"%>
<%@ page import="com.ailk.bi.base.exception.CustomMsuException"%>
<%@ page import="com.ailk.bi.base.util.CommTool"%>
<%@ page import="com.ailk.bi.report.util.CustomMsuUtil"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%
	Log log = LogFactory.getLog(CustomMsuUtil.class);
	String action = CommTool.getParameterGB(request, "action");
	if (null == action || "".equals(action)) {
		action = "normal";
	}

	String msu_id = CommTool.getParameterGB(request, "selectedMsu");

	if (null == msu_id || "".equals(msu_id)) {
		log.error("指标定制维护－删除指标时没有提供衍生指标标识");
		out.println(new AjaxResultInfo("删除自定义指标失败",
		"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
				+ "由此造成对您的工作不便，深表歉意！"));
		return;
	}
	boolean used = CustomMsuUtil.customMsuUsedForReport(msu_id);
	if (used) {
		log.error("指标定制维护－删除指标时发现指标已经被报表使用");
		out.println(new AjaxResultInfo("删除自定义指标失败",
		"对不起，该自定义指标已经被报表使用，因此不允许进行删除！"));
		return;
	}
	try {
		CustomMsuUtil.deleteCustomMsu(msu_id);
	} catch (CustomMsuException cme) {
		log.error("指标定制维护－删除自定义指标时发生错误", cme);
		out.println(new AjaxResultInfo("删除自定义指标失败",
		"对不起，删除自定义指标时发生错误！请联系系统管理员，" + "由此造成对您的工作不便，深表歉意！"));
	}
	AjaxResultInfo info = new AjaxResultInfo("删除自定义指标成功",
			"您已经成功删除了自定义指标！",true);
	out.println(info.toString());
%>
