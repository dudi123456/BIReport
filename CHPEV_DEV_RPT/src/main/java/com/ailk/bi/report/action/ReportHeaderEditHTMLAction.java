package com.ailk.bi.report.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ailk.bi.base.exception.ReportHeadException;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.report.domain.RptResourceTable;
import com.ailk.bi.report.service.ILReportService;
import com.ailk.bi.report.service.impl.LReportServiceImpl;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.report.util.ReportHeadUtil;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class ReportHeaderEditHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 608934968298009124L;

	public void doTrans(HttpServletRequest req, HttpServletResponse res)
			throws HTMLActionException {
		// 检查登陆信息
		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(req, res))
			return;

		// 定义日志
		Log log = LogFactory.getLog(this.getClass());
		// 获取用户session
		HttpSession session = req.getSession();

		// 定义报表操作对象
		ILReportService rptService = new LReportServiceImpl();
		// 报表基本信息
		RptResourceTable rptTable = (RptResourceTable) session
				.getAttribute(WebKeys.ATTR_REPORT_TABLE);
		if (null == rptTable) {
			log.error("表头定制－报表信息丢失");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
							+ "由此造成对您的工作不便，深表歉意！");
		}

		String report_id = CommTool.getParameterGB(req, "report_id");
		String report_head = CommTool.getParameterGB(req, "report_head");
		String delete = CommTool.getParameterGB(req, "delete");
		if (!report_id.equals(rptTable.rpt_id)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "报表信息有误，请重新确定报表信息！");
		}
		if (null == report_id || "".equals(report_id)) {
			log.error("表头定制－没有提供报表标识，请勿直接访问页面");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
							+ "由此造成对您的工作不便，深表歉意！");
		}
		if (null == report_head) {
			log.error("表头定制－没有提供报表表头，请勿直接访问页面");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
							+ "由此造成对您的工作不便，深表歉意！");
		}
		if ("".equals(report_head) || report_head.indexOf("<tr") < 0
				|| (!"".equals(delete) && "true".equalsIgnoreCase(delete))) {
			try {
				rptService.deleteRptHead(report_id);
			} catch (ReportHeadException rhe) {
				log.error("保存报表报头内容失败，请联系系统管理员，对此我们深表歉意", rhe);
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"保存报表报头内容失败，请联系系统管理员，对此我们深表歉意");
			}
			try {
				rptTable.ishead = ReportConsts.NO;
				rptService.updateReport(rptTable);
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "更新报表信息失败！");
			}
		} else {
			// 这里要设置表格的样式，等
			String trStyle = "";
			String tdStyle = "tab-title";
			// 处理表头并验证
			report_head = ReportHeadUtil.processHead(report_head, trStyle,
					tdStyle);
			// 保存表头
			try {
				rptService.insertRptHead(report_id, report_head);
			} catch (ReportHeadException rhe) {
				log.error("保存报表报头内容失败，请联系系统管理员，对此我们深表歉意", rhe);
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"保存报表报头内容失败，请联系系统管理员，对此我们深表歉意");
			}
			try {
				rptTable.ishead = ReportConsts.YES;
				rptService.updateReport(rptTable);
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "更新报表信息失败！");
			}
		}
		session.setAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE, rptTable);
		throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
				"保存报表报头内容成功！", "loadReportHead.rptdo?report_id=" + report_id);
	}
}
