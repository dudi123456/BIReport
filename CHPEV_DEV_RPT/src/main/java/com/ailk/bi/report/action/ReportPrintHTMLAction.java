package com.ailk.bi.report.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.report.domain.RptResourceTable;
import com.ailk.bi.report.service.ILReportService;
import com.ailk.bi.report.service.impl.LReportServiceImpl;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class ReportPrintHTMLAction extends HTMLActionSupport {

	/**
	 * 报表打印信息的相关操作
	 */
	private static final long serialVersionUID = 5344378535105848540L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 检查登陆信息
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		// 获取用户session
		HttpSession session = request.getSession();
		// 获取操作类型
		String opType = request.getParameter("opType");
		if (StringTool.checkEmptyString(opType)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "未知的操作,请正确访问本系统！");
		}
		ILReportService reportManager = new LReportServiceImpl();

		// 操作处理
		if ("view".equals(opType)) {
			// 浏览报表打印历史操作

			// 获取报表查询条件
			ReportQryStruct qryStruct = new ReportQryStruct();
			try {
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
				if (qryStruct == null) {
					qryStruct = new ReportQryStruct();
				}
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "提取界面查询信息失败，请注意是否登陆超时！");
			}
			try {
				String whereSql = "";
				// 打印码
				if (qryStruct.print_code != null
						&& !"".equals(qryStruct.print_code)) {
					whereSql += " AND A.PRINT_CODE LIKE '%"
							+ qryStruct.print_code + "%'";
				}
				// 打印时间
				if (qryStruct.date_s != null && !"".equals(qryStruct.date_s)) {
					whereSql += " AND TO_CHAR(A.PRINT_TIME,'yyyymmdd')='"
							+ qryStruct.date_s + "'";
				}
				// 报表名称
				if (qryStruct.rpt_name != null
						&& !"".equals(qryStruct.rpt_name)) {
					whereSql += " AND B.NAME LIKE '%" + qryStruct.rpt_name
							+ "%'";
				}
				// 操作用户
				if (qryStruct.oper_no != null && !"".equals(qryStruct.oper_no)) {
					whereSql += " AND A.USER_NAME LIKE '%" + qryStruct.oper_no
							+ "%'";
				}

				List reports = reportManager.getReportPrint(whereSql);
				RptResourceTable[] rptTables = null;
				if (reports != null && reports.size() > 0) {
					rptTables = (RptResourceTable[]) reports
							.toArray(new RptResourceTable[reports.size()]);
				}
				session.setAttribute(WebKeys.ATTR_REPORT_TABLES, rptTables);
				session.setAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT, qryStruct);
				setNextScreen(request, "ReportPrintView.screen");
			} catch (AppException e) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "查询打印历史信息失败！");
			}
		} else if ("print".equals(opType)) {
			// 打印报表操作
			PrintWriter out = null;
			try {
				out = response.getWriter();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			response.setContentType("text/html; charset=GB2312");
			// 报表打印码
			String print_code = request.getParameter("code");
			if (print_code == null || "".equals(print_code)) {
				out.write("false");
			}
			logcommon.debug("print_code=" + print_code);
			String print_time = print_code.substring(0, 4);
			print_time += "-" + print_code.substring(4, 6);
			print_time += "-" + print_code.substring(6, 8);
			print_time += " " + print_code.substring(8, 10);
			print_time += ":" + print_code.substring(10, 12);
			print_time += ":" + print_code.substring(12, 14);
			logcommon.debug("print_time=" + print_time);
			// 报表ID
			String rpt_id = request.getParameter("rpt_id");
			// 用户信息
			String user_id = CommonFacade.getLoginId(session);
			String user_name = CommonFacade.getLoginName(session);

			RptResourceTable rptTable = new RptResourceTable();
			rptTable.print_code = print_code;
			rptTable.print_time = print_time;
			rptTable.rpt_id = rpt_id;
			rptTable.print_userid = user_id;
			rptTable.print_username = user_name;
			try {
				reportManager.insertReportPrint(rptTable);
				out.write("true");
				out.flush();
			} catch (AppException e) {
				out.write("false");
				out.flush();
			}
			setNvlNextScreen(request);
		}
	}

}
