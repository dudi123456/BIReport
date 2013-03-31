package com.ailk.bi.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.system.common.LSInfoResRole;

public class ResRoleHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		@SuppressWarnings("unused")
		HttpSession session = request.getSession();

		// 取得提交参数
		String submitType = request.getParameter("submitType");
		String res_id = request.getParameter("res_id");
		String role_ids = request.getParameter("role_id");
		String res_type = request.getParameter("res_type");
		if (submitType == null || "".equals(submitType)) {
			submitType = "0";
		}

		if ("saveMenuRole".equals(submitType)) { // 菜单列表
			LSInfoResRole.saveRoleInfo(request, res_id, role_ids, res_type);

		}

	}
}
