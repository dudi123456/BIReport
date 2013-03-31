package com.ailk.bi.olap.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.olap.util.HttpSessionUtil;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class ReportOlapCacheHTMLAction extends HTMLActionSupport {

	private static final long serialVersionUID = 3440395927098385437L;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * waf.controller.web.action.HTMLActionSupport#doTrans(javax.servlet.http
	 * .HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (null == request || null == response)
			throw new IllegalArgumentException("获取分析性报表内容时参数为空");
		HttpSession session = request.getSession();
		String reportId = request.getParameter("report_id");
		if (null == reportId || "".equals(reportId))
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"无法获取报表标识！");
		// 清除缓存
		HttpSessionUtil.clearCacheObj(session, reportId);
		System.out.println("the olap session cache cleared");
		setNvlNextScreen(request);
	}

}
