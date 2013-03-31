package com.ailk.bi.sigma.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "unused" })
public class SigmaReportHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static String encoding = "UTF-8";
	protected static String ajaxEncoding = "UTF-8";

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		HttpSession session = request.getSession();

		String sigmaId = request.getParameter("rpt_id");
		try {
			request.setCharacterEncoding(ajaxEncoding);
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		response.setContentType("text/html; charset=" + ajaxEncoding);

		request.setAttribute("sigmaId", sigmaId);

		setNextScreen(request, "SigmaReportShow.screen");

	}

}
