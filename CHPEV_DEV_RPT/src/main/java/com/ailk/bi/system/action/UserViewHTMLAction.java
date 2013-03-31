package com.ailk.bi.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.system.common.LSInfoUser;

public class UserViewHTMLAction extends HTMLActionSupport {
	/**
	 *
	 */
	private static final long serialVersionUID = -7216625057346179010L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		HttpSession session = request.getSession();

		//
		String submitType = request.getParameter("submitType");
		if (submitType == null || "".equals(submitType)) {
			submitType = "0";
		}
		//
		InfoOperTable sstUser = new InfoOperTable();

		// 取页面用户信息
		try {
			AppWebUtil.getHtmlObject(request, "user", sstUser);
		} catch (AppException ex) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "用户信息有误！");
		}
		System.out.println("sstUser================" + sstUser.region_id);

		if ("0".equals(submitType)) {
			LSInfoUser.addNewOper(request, sstUser);
		} else if ("1".equals(submitType) || "3".equals(submitType)) {
			LSInfoUser.updateOperInfo(request, sstUser);
		} else if ("2".equals(submitType)) {
			LSInfoUser.deleteOperInfo(request, sstUser);
		}
		if ("userRole".equals(submitType)) {
			String user_id = request.getParameter("user_id");
			InfoOperTable user = (InfoOperTable) session
					.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
			String roleTree = LSInfoUser.getRoleTree(user_id);

			String[][] userRole = LSInfoUser.getUserRoleList(user.group_id,
					user_id);
			// String[][] noInUserRole = LSInfoUser.getNoUserRole(user_id);
			request.setAttribute("user_id", user_id);
			request.setAttribute("roleTree", roleTree);
			request.setAttribute("userRole", userRole);
			// request.setAttribute("noInUserRole", noInUserRole);
			this.setNextScreen(request, "userRole.screen");
		} else if ("saveUserRole".equals(submitType)) {
			LSInfoUser.saveUserRole(request, sstUser);
		} else if ("addLoUser".equals(submitType)) {
			LSInfoUser.addLoUser(request, sstUser);
		} else if ("delLoUser".equals(submitType)) {
			LSInfoUser.delLoUser(request, sstUser);
		}
	}

}
