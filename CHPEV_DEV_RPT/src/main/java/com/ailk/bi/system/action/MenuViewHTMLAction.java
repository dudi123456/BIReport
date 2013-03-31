package com.ailk.bi.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.table.InfoMenuTable;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.system.common.LSInfoMenu;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class MenuViewHTMLAction extends HTMLActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = -1786125939253480988L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		HttpSession session = request.getSession();

		// 取得提交参数
		String submitType = request.getParameter("submitType");
		if (submitType == null || "".equals(submitType)) {
			submitType = "1";
		}

		InfoMenuTable menu = new InfoMenuTable();
		if ("1".equals(submitType)) {
			// 取页面角色信息
			try {
				AppWebUtil.getHtmlObject(request, "menu", menu);
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "菜单受理信息有误！");
			}
			menu.menu_name = menu.menu_name.trim();
			menu.menu_name = menu.menu_name.replaceAll("\\s", "");
			menu.menu_url = menu.menu_url.trim();
			menu.menu_url = menu.menu_url.replaceAll("\\s", "");
			if ("0".equals(menu.menu_id)) {// 新增
				LSInfoMenu.addNewMenu(request, menu);
			} else {// 更改
				LSInfoMenu.updateMenuInfo(request, menu);
			}

		}
		if ("2".equals(submitType)) {// 删除
			// 取页面角色信息
			try {
				AppWebUtil.getHtmlObject(request, "menu", menu);
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "角色受理信息有误！");
			}
			// 调试
			menu.menu_name = menu.menu_name.trim();
			menu.menu_name = menu.menu_name.replaceAll("\\s", "");
			menu.menu_url = menu.menu_url.trim();
			menu.menu_url = menu.menu_url.replaceAll("\\s", "");

			LSInfoMenu.deleteMenuInfo(request, menu);

		}

	}

}
