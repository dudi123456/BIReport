package com.ailk.bi.report.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.report.util.CustomMsuUtil;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class ReportCustomMsuLoadHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6451531411290668591L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		Log log = LogFactory.getLog(this.getClass());
		HttpSession session = request.getSession();
		if (session == null) {
			// setNextScreen(request, "LoginIn.screen");
		}
		String stat_period = CommTool.getParameterGB(request, "stat_period");
		if (null == stat_period || "".equals(stat_period)) {
			log.error("自定义指标维护－加载自定义指标时没有提供统计周期");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
							+ "由此造成对您的工作不便，深表歉意！");
		}
		// 获取令牌
		session.setAttribute(WebKeys.ATTR_REPORT_CUSTOM_MSU_TOKEN,
				CustomMsuUtil.getToken());
		setNextScreen(request, "rptCustomMsuAdd.screen?stat_period="
				+ stat_period);
	}

}
