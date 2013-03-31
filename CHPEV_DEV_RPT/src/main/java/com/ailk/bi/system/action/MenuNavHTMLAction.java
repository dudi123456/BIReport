package com.ailk.bi.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.system.facade.impl.CommonFacade;

public class MenuNavHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		HttpSession session = request.getSession();
		@SuppressWarnings("unused")
		InfoOperTable loginUser = CommonFacade.getLoginUser(session);
		String menu_code = request.getParameter("menu_code");
		session.setAttribute("CURRENT_MENU_CODE", menu_code);

		setNextScreen(request, "MenuNavHTMLMain.screen");
	}

}
