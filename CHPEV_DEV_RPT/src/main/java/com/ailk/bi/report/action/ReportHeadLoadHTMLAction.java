package com.ailk.bi.report.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ailk.bi.base.exception.ReportHeadException;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.report.service.ILReportService;
import com.ailk.bi.report.service.impl.LReportServiceImpl;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class ReportHeadLoadHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2473916394881283356L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 检查登陆信息
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		// 定义日志
		Log log = LogFactory.getLog(this.getClass());
		// 获取用户session
		HttpSession session = request.getSession();

		// 定义报表操作对象
		ILReportService rptService = new LReportServiceImpl();

		String report_id = CommTool.getParameterGB(request, "report_id");
		if (null == report_id || "".equals(report_id)) {
			log.error("报表表头加载－没有提供报表标识，请勿直接访问页面");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
							+ "由此造成对您的工作不便，深表歉意！");
		}

		try {
			String head = rptService.getReportHeadDefine(report_id);
			// logcommon.debug("head=" + head);
			session.setAttribute(WebKeys.ATTR_REPORT_HEAD_CONTENT, head);
		} catch (ReportHeadException rhe) {
			log.error("报表表头加载－加载表头内容发生错误", rhe);
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"对不起，系统获取报表报头内容发生错误，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
							+ "由此造成对您的工作不便，深表歉意！");
		}
		setNextScreen(request, "rptHeadEditor.screen?report_id=" + report_id);
	}

}
