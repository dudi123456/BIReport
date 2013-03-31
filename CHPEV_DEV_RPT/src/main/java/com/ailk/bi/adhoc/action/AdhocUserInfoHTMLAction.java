package com.ailk.bi.adhoc.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.adhoc.domain.UiAdhocUserBaseInfo;
import com.ailk.bi.adhoc.service.IAdhocUserService;
import com.ailk.bi.adhoc.service.impl.AdhocUserService;
import com.ailk.bi.adhoc.util.AdhocConstant;
import com.ailk.bi.adhoc.util.AdhocSessionUtil;
import com.ailk.bi.base.util.CommTool;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class AdhocUserInfoHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9217646228953517913L;

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
		// 清空以前的session
		AdhocSessionUtil.clearUserInfo(session);
		// 获取用户基本信息
		IAdhocUserService userService = new AdhocUserService();
		UiAdhocUserBaseInfo baseInfo = userService.getUserBaseInfo(userNo,
				gatherMon);
		// 保存到session中
		session.setAttribute(AdhocConstant.ADHOC_USER_BASE_INFO, baseInfo);
		// 转向JSP页面
		setNextScreen(request, "AdhocUserInfo.screen");
	}
}
