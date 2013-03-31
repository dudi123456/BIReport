<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="com.ailk.bi.base.exception.CustomMsuException"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.report.util.CustomMsuUtil"%>
<%@page import="org.apache.commons.logging.LogFactory"%>

<%
  Log log = LogFactory.getLog(CustomMsuUtil.class);
	String msuId = CommTool.getParameterGB(request, "selectedMsu");

	//返回用户选定指标后，所需维度的 JSON文本
	if (null != msuId && !"".equals(msuId)) {
		try {
			String output = CustomMsuUtil
			.genDimValueJSonString(CustomMsuUtil
			.getCustomMsuDimsValue(CustomMsuUtil
					.getCustomMsuDims(msuId)));
			out.println(output);
		} catch (CustomMsuException cme) {
			log.error(cme);
		}
	}
%>
