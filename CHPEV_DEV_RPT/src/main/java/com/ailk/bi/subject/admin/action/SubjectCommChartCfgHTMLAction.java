package com.ailk.bi.subject.admin.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.common.app.StringB;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "unused" })
public class SubjectCommChartCfgHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2145503103017352780L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		HttpSession session = request.getSession();
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		String strReturn = "";
		String opt_type = StringB.NulltoBlank(request.getParameter("opt_type"));

		if ("list".equals(opt_type)) {// 查询所有ui_subject_common_table_def中数据
			strReturn = "listSubjectCommonChartInfo";

		}
		this.setNextScreen(request, strReturn + ".screen");
		// super.doTrans(request, response);
	}

}
