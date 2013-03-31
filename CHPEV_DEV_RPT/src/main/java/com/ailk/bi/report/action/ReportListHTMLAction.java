package com.ailk.bi.report.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.report.domain.RptResourceTable;
import com.ailk.bi.report.service.ILReportService;
import com.ailk.bi.report.service.impl.LReportServiceImpl;
import com.ailk.bi.report.struct.ReportQryStruct;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class ReportListHTMLAction extends HTMLActionSupport {

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
		// 操作用户信息
		String oper_no = "1";// CommTool.getLoginId(session);
		String user_role = "1";// CommTool.getRoleStringByOper(oper_no);
		user_role = "," + user_role + ",";
		// 获取操作类型
		String opType = request.getParameter("opType");
		if (StringTool.checkEmptyString(opType)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "未知的报表操作！");
		}
		// 选中角色CODE
		String role_code = request.getParameter("role_code");
		// 选中角色名称
		String role_name = request.getParameter("role_name");
		// 提交操作的类型
		String opSubmit = request.getParameter("opSubmit");
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
		// 报表状态
		if ("listAttstationRpt".equals(opType)) {
			qryStruct.rpt_status = "W";
		}

		try {
			ILReportService reportManager = new LReportServiceImpl();

			// 查询条件
			String whereSql = "";
			// 报表编码
			if (!StringTool.checkEmptyString(qryStruct.rpt_id)) {
				whereSql += " AND A.RES_ID LIKE '%" + qryStruct.rpt_id + "%'";
			}
			// 报表本地应用编码
			if (!StringTool.checkEmptyString(qryStruct.rpt_local_res_code)) {
				whereSql += " AND B.LOCAL_RES_CODE LIKE '%"
						+ qryStruct.rpt_local_res_code + "%'";
			}
			// 报表名称
			if (!StringTool.checkEmptyString(qryStruct.rpt_name)) {
				whereSql += " AND B.NAME LIKE '%" + qryStruct.rpt_name + "%'";
			}
			// 报表周期
			if (!StringTool.checkEmptyString(qryStruct.rpt_cycle)) {
				whereSql += " AND B.CYCLE='" + qryStruct.rpt_cycle + "'";
			}
			// 报表类型
			if (!StringTool.checkEmptyString(qryStruct.rpt_kind)) {
				whereSql += " AND A.PARENT_ID='" + qryStruct.rpt_kind + "'";
			}
			// 报表状态
			if (!StringTool.checkEmptyString(qryStruct.rpt_status)) {
				whereSql += " AND B.STATUS='" + qryStruct.rpt_status + "'";
			}

			// 定制报表非管理员只能查看自己定义的报表
			if ("listLocalRpt".equals(opType)) {
				whereSql += " AND (B.RPT_TYPE LIKE '2%' or B.RPT_TYPE LIKE '3%')";
			} else if ("listSelfRpt".equals(opType)) {
				whereSql += " AND B.D_USER_ID='" + oper_no + "'";
				whereSql += " AND B.PRIVATEFLAG='Y'";
			} else if ("listPrivateRpt".equals(opType)) {
				if ("save".equals(opSubmit)) {
					String[] reportCheck = request.getParameterValues("rptSel");
					RptResourceTable[] rptTables = (RptResourceTable[]) session
							.getAttribute(WebKeys.ATTR_REPORT_TABLES);
					try {
						reportManager.insertReportDispense(role_code,
								rptTables, reportCheck);
					} catch (AppException e) {
						throw new HTMLActionException(session,
								HTMLActionException.WARN_PAGE, "更改报表发布关系失败！");
					}
				}
				whereSql += " AND B.D_USER_ID='" + oper_no + "'";
				whereSql += " AND B.PRIVATEFLAG='Y'";
				whereSql += " AND B.STATUS='Y'";
			}

			List reports = null;
			String city = null;
			// 查看报表加以限制，地市的只能看该地市和下级区县报表，
			UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
					.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
			if (ctlStruct == null) {
				ctlStruct = new UserCtlRegionStruct();
			}

			if (!StringTool.checkEmptyString(ctlStruct.ctl_city_str)) {// 地市
				city = ctlStruct.ctl_city_str;
				if (!StringTool.checkEmptyString(ctlStruct.ctl_county_str)
						&& ctlStruct.ctl_county_str.length() == 4) {// 区县
					city = ctlStruct.ctl_county_str;
				}
			} else {
				city = "999";
			}
			if (city != null && !"999".equals(city)) {
				if (city.length() == 2) { // 地市
					whereSql += " and B.filldept in ('" + city + "',"
							+ ctlStruct.ctl_county_str_add + ")";
				} else {
					whereSql += " and B.filldept='" + city + "'"; // 区县
				}
			}

			reports = reportManager.getReports(whereSql);
			/*
			 * if ("listPrivateRpt".equals(opType)) { reports =
			 * reportManager.getRoleReports(whereSql, role_code); } else {
			 * reports = reportManager.getReports(whereSql); }
			 */
			RptResourceTable[] rptTables = null;
			if (reports != null && reports.size() > 0) {
				rptTables = (RptResourceTable[]) reports
						.toArray(new RptResourceTable[reports.size()]);
			}
			session.setAttribute(WebKeys.ATTR_REPORT_DISPENSE_ROLE_ID,
					role_code);
			session.setAttribute(WebKeys.ATTR_REPORT_DISPENSE_ROLE_NAME,
					role_name);
			session.setAttribute(WebKeys.ATTR_REPORT_TABLES, rptTables);
			session.setAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT, qryStruct);

			setNextScreen(request, opType + ".screen");
		} catch (AppException ex) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "获取报表列表内容失败！");
		}
	}
}
