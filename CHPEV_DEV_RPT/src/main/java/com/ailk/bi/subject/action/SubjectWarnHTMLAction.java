package com.ailk.bi.subject.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "unused" })
public class SubjectWarnHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8746283088213452891L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		HttpSession session = request.getSession();

		// 获取页面screen标示
		String optype = request.getParameter("optype");
		String retnScreen = "";
		if (optype.equalsIgnoreCase("busi")) {
			// 业务预警
			retnScreen = "opp_warn_busi";
		} else if (optype.equalsIgnoreCase("cust")) {
			// 客服预警
			retnScreen = "opp_warn_cust_server";
		}

		setNextScreen(request, retnScreen + ".screen");

	}

}
