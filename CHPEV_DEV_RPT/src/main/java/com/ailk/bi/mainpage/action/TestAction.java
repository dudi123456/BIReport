package com.ailk.bi.mainpage.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.report.struct.ReportQryStruct;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class TestAction extends HTMLActionSupport {
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

		// 通用查询结构（只服务于条件）
		ReportQryStruct qryStruct = new ReportQryStruct();
		try {
			AppWebUtil.getHtmlObject(request, "qry", qryStruct);
		} catch (AppException ex) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"总部高层决策日运营总览提取查询条件信息错误，请检查系统程序！");
		}
		if (qryStruct.gather_month == null || "".equals(qryStruct.gather_month)) {
			qryStruct.gather_month = DateUtil.getDiffMonth(-1,
					DateUtil.getNowDate());
		}
		if (qryStruct.dim1 == null || "".equals(qryStruct.dim1)) {
			qryStruct.dim1 = DateUtil.getDiffMonth(0, DateUtil.getNowDate());
		}
		if (qryStruct.gather_day == null || "".equals(qryStruct.gather_day)) {
			qryStruct.gather_day = DateUtil.getDiffDay(-1,
					DateUtil.getNowDate());
		}
		if (qryStruct.date_e == null || "".equals(qryStruct.date_e)) {
			qryStruct.date_e = DateUtil.getDiffDay(0, DateUtil.getNowDate());
		}
		System.out.println(qryStruct.gather_day);
		System.out.println(qryStruct.gather_month);
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);

		this.setNextScreen(request, "testScreen.screen");
	}

}
