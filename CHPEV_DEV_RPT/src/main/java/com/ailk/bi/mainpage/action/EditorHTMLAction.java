package com.ailk.bi.mainpage.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class EditorHTMLAction extends HTMLActionSupport {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		// 变量申明区
		HttpSession session = request.getSession();

		String sid = request.getParameter("id");
		String sdate = request.getParameter("date");
		String memo = request.getParameter("memo");
		if (sdate == null || "".equals(sdate))
			sdate = "0";
		if (memo == null)
			memo = "";
		if (sid == null) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"没有指定要分析标识，请勿直接访问！");
		}
		try {
			String sql = SQLGenator.genSQL("TQ3002", sid, sdate);
			String[][] tmp = WebDBUtil.execQryArray(sql, "");
			if (tmp == null || tmp.length == 0)
				sql = SQLGenator.genSQL("TQ3004", sid, sdate, memo);
			else
				sql = SQLGenator.genSQL("TQ3005", memo, sid, sdate);
			// System.out.println("sql=" + sql);
			int icount = WebDBUtil.execUpdate(sql);
			System.out.println("icount=" + icount);
			if (icount != 1) {
				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE,
						"保存信息失败！");
			} else {
				session.setAttribute(WebKeys.ATTR_ANYFLAG, "1");
				throw new HTMLActionException(session,
						HTMLActionException.SUCCESS_PAGE,
						"保存信息成功！");
			}
		} catch (AppException ex) {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE,
					"保存信息失败！");
		}
	}
}
