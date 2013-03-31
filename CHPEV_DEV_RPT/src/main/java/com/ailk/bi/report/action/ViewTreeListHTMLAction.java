package com.ailk.bi.report.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.report.service.ILReportService;
import com.ailk.bi.report.service.impl.LReportServiceImpl;

public class ViewTreeListHTMLAction extends HTMLActionSupport {

	/**
	 * 获取报表列表
	 */
	private static final long serialVersionUID = -5643321881510988393L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 检查登陆信息
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		// 变量申明区
		HttpSession session = request.getSession();

		String resType = request.getParameter("res_type");
		String resId = request.getParameter("res_id");
		String rptId = request.getParameter("rpt_id");
		String rptName = request.getParameter("rpt_name");

		String hotType = request.getParameter("tabb_type");
		try {

			ILReportService reportManager = new LReportServiceImpl();
			// String[][] reports =
			// reportManager.getViewTreeList(resId,resType,rptId,rptName);
			String[][] reports = reportManager.getViewTreeList(session, resId,
					resType, rptId, rptName, hotType);

			request.getSession().setAttribute("res_type", resType);
			request.getSession().setAttribute("res_id", resId);
			request.getSession().setAttribute("rpt_id", rptId);
			request.getSession().setAttribute("rpt_name", rptName);
			session.setAttribute("VIEW_TREE_LIST", reports);
			setNextScreen(request, "viewTreeList.screen");
		} catch (AppException ex) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "获取报表列表内容失败！");
		}
	}
}
