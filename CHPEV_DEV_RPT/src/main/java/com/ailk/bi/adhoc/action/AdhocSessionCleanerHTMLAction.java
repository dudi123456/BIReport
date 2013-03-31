package com.ailk.bi.adhoc.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.adhoc.util.AdhocConstant;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class AdhocSessionCleanerHTMLAction extends HTMLActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		HttpSession session = request.getSession();

		session.removeAttribute(AdhocConstant.ADHOC_CONDITION_WEBKEYS);

		session.removeAttribute(AdhocConstant.ADHOC_DIM_WEBKEYS);

		session.removeAttribute(AdhocConstant.ADHOC_MSU_WEBKEYS);

		session.removeAttribute(AdhocConstant.ADHOC_FAVORITE_DEF_TABLE_WEBKEYS);

		//
		session.removeAttribute(AdhocConstant.ADHOC_ROOT_WEBKEYS);

		session.removeAttribute(AdhocConstant.ADHOC_TABLE_WEBKEYS);

		session.removeAttribute(AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS);

		String url = "";
		String adhoc_root = request.getParameter(AdhocConstant.ADHOC_ROOT);
		if (adhoc_root == null || "".equals(adhoc_root)) {
			url = "AdhocParam.screen?reloadtree=true";
		} else {
			url = "AdhocParam.screen?reloadtree=true&adhoc_root=" + adhoc_root;
		}

		this.setNextScreen(request, url);

	}

}
