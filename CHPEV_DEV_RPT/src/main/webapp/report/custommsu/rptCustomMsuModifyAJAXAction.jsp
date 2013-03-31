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

	String msuName = null;
	if ("normal".equalsIgnoreCase(action)) {
		msuName = CommTool.getParameterGB(request, "customMsuName");
	} else {
		msuName = CommTool.getParameterUTF(request, "customMsuName");
	}

	if (null == msuName || "".equals(msuName)) {
		log.error("指标定制维护－修改指标时没有提供衍生指标名称");
		out.println(new AjaxResultInfo("修改自定义指标失败",
		"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
				+ "由此造成对您的工作不便，深表歉意！"));
		return;
	}
	String msuId = CommTool.getParameterGB(request, "selectedMsu");
	if (null == msuId || "".equals(msuId)) {
		log.error("指标定制维护－修改指标时没有提供衍生指标标识");
		out.println(new AjaxResultInfo("修改自定义指标失败",
		"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
				+ "由此造成对您的工作不便，深表歉意！"));
		return;
	}
	boolean used = CustomMsuUtil.customMsuUsedForReport(msuId);
	if (used) {
		log.error("指标定制维护－修改指标时发现指标已经被报表使用");
		out.println(new AjaxResultInfo("修改自定义指标失败",
		"对不起，该自定义指标已经被报表使用，因此不允许进行修改！"));
		return;
	}
	boolean hasSameCustomMsu = CustomMsuUtil.hasSameNamedCustomMsu(
			msuId, msuName);
	if (hasSameCustomMsu) {
		out.println(new AjaxResultInfo("修改自定义指标失败", "对不起，您已经定义过名称为 '"
		+ msuName + "' 的指标，请返回修改指标名称，或者使用已经定义的指标!"));
		return;
	} else {
		try {
			CustomMsuUtil.modifyCustomMsu(msuId, msuName, request);
		} catch (CustomMsuException cme) {
			log.error("指标定制维护－更新自定义指标失败", cme);
			out.println(new AjaxResultInfo("修改自定义指标失败",
			"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。"
			+ "如果此问题依然存在，请联系系统管理员，"
			+ "由此造成对您的工作不便，深表歉意！"));
			return;
		}
	}
	AjaxResultInfo info = new AjaxResultInfo("修改自定义指标成功",
			"您已经成功修改了自定义指标！", true);
	out.println(info.toString());
%>
