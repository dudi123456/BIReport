package com.ailk.bi.olap.util;

import javax.servlet.http.HttpSession;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.util.WebKeys;

public class HttpSessionUtil {
	/**
	 * 清除指定报表标识的所有缓存对象
	 *
	 * @param session
	 *            HTTP缓存
	 * @param reportId
	 *            指定的表报标识
	 */
	public static void clearCacheObj(HttpSession session, String reportId)
			throws ReportOlapException {
		if (null == session || null == reportId)
			throw new ReportOlapException("清除分析型报表缓存时输入参数为空");
		session.removeAttribute(WebKeys.ATTR_OLAP_REPORT_ID);
		session.removeAttribute(WebKeys.ATTR_OLAP_DATE_OBJ + "_" + reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_CUR_FUNC_OBJ + "_" + reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_TABLE_DOMAINS_OBJ + "_"
				+ reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_CHART_DOMAINS_OBJ + "_"
				+ reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_REPORT_OBJ + "_" + reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_TABLE_HTML + "_" + reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_FIR_URL + "_" + reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_TABLE_CONTENT + "_"
				+ reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_RPT_DIMS + "_" + reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_RPT_MSUS + "_" + reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_RPT_PRE_MSUS + "_" + reportId);

		session.removeAttribute(WebKeys.ATTR_OLAP_ROW_CONTENT + "_" + reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_ROW_EXP_CONTENT + "_"
				+ reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_TABLE_EXPORT + "_" + reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_USER_REPORTS + "_" + reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_CHART_DIM_JS_OBJECT + "_"
				+ reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_CHART_DIM_VALUES + "_"
				+ reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_CHART_DIM + "_" + reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_TABLE_DOMAINS_PRE_OBJ + "_"
				+ reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_TABLE_PRE + "_" + reportId);
		session.removeAttribute(WebKeys.ATTR_OLAP_TABLE_PRE_BODY + "_"
				+ reportId);
	}
}
