<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="com.ailk.bi.base.exception.CustomMsuException"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.report.util.CustomMsuUtil"%>
<%
	Log log = LogFactory.getLog(CustomMsuUtil.class);
	String msuId = CommTool.getParameterGB(request, "selectedMsu");
	String srcTabId = CommTool.getParameterGB(request, "srcTabId");

	//返回用户选定指标后，所需维度的 JSON文本
	if (null != msuId && !"".equals(msuId) && null != srcTabId
			&& !"".equals(srcTabId)) {
    //还不能用
		String[] values = srcTabId.split("\\|");
		srcTabId = values[0];
		try {
			String output = CustomMsuUtil
			.genDimValueJSonString(CustomMsuUtil
			.getBaseMsuDimsValue(CustomMsuUtil
					.getBaseMsuDims(msuId, srcTabId)));
			if (null != output && !"".equals(output)) {
		out.println(output);
			}
		} catch (CustomMsuException cme) {
			log.error(cme);
		}
	}
%>
