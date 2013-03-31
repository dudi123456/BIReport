package com.ailk.bi.adhoc.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.adhoc.domain.UiAdhocUserScore;
import com.ailk.bi.adhoc.service.IAdhocUserService;
import com.ailk.bi.adhoc.service.impl.AdhocUserService;
import com.ailk.bi.adhoc.util.AdhocConstant;
import com.ailk.bi.base.util.CommTool;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class AdhocUserScoreInfoHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8228879777838474959L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		HttpSession session = request.getSession();
		// 获取用户编号
		String userNo = CommTool.getParameterGB(request, "user_no");
		// 获取查询月份
		String gatherMon = CommTool.getParameterGB(request, "gather_mon");
		// 如果两个都没有，抛出异常
		if (null == userNo || "".equals(userNo) || null == gatherMon
				|| "".equals(gatherMon)) {
			throw new HTMLActionException(request.getSession(),
					HTMLActionException.WARN_PAGE, "请提供用户标识和查询月份");
		}
		IAdhocUserService userService = new AdhocUserService();
		UiAdhocUserScore scoreInfo = userService
				.getUsreScore(userNo, gatherMon);
		session.setAttribute(AdhocConstant.ADHOC_USER_SCORE_INFO, scoreInfo);
		// 转向JSP页面
		setNextScreen(request, "AdhocUserScore.screen");
	}
}
