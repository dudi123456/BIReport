package com.ailk.bi.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ailk.bi.system.common.LSInfoAdhocRole;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class AdhocRoleHTMLAction extends HTMLActionSupport {
	/**
   *
   */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		// HttpSession session = request.getSession();

		// 取得提交参数
		String submitType = request.getParameter("submitType");
		if (submitType == null || "".equals(submitType)) {
			submitType = "0";
		}

		if ("save".equals(submitType)) { // 菜单列表
			LSInfoAdhocRole.saveAdhocRole(request);

		}
	}

}