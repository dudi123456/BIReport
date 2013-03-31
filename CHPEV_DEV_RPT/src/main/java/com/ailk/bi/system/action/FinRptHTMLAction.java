package com.ailk.bi.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.struct.FinRptStruct;
import com.ailk.bi.base.table.BillMonDefTable;
import com.ailk.bi.base.table.GeneralReportDefTable;
import com.ailk.bi.base.util.DBTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.common.CrmTool;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class FinRptHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -702234152917466158L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		// 变量申明区
		HttpSession session = request.getSession();
		// 定义查询统计结构
		FinRptStruct qryStruct = new FinRptStruct();
		// 报表定义表
		GeneralReportDefTable report = null;
		// 账期定义表
		BillMonDefTable billmon = null;

		String sql = "";
		String arr[][] = null;

		// 程序判断区
		String operType = request.getParameter("oper_type");
		if (operType == null || "".equals(operType)) {
			operType = "0";
		}

		// 提取条件
		try {
			AppWebUtil.getHtmlObject(request, "qry", qryStruct);
		} catch (AppException ex) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "系统页面信息有误，请反馈系统管理员！");
		}
		// 判断帐期
		if (qryStruct.gather_mon != null && !"".equals(qryStruct.gather_mon)) {
			billmon = CrmTool.getFixBillMonth(qryStruct.gather_mon);
		} else {
			billmon = CrmTool.getFixBillMonth("temp");
		}
		try {
			System.out.println(billmon.toXml());
		} catch (AppException e1) {

			e1.printStackTrace();
		}
		// 判断报表ID
		if (qryStruct.general_id != null && !"".equals(qryStruct.general_id)) {
			report = CrmTool.getFirstGeneral(qryStruct.general_id);
		} else {
			report = CrmTool.getFirstGeneral("0");
		}

		String whereStrA = "";
		String whereStr = "";
		// 判断帐期
		if (qryStruct.gather_mon != null && !"".equals(qryStruct.gather_mon)) {
			whereStrA += " and a.gather_mon = '" + qryStruct.gather_mon + "'";
			whereStr += " and a.gather_mon = '" + qryStruct.gather_mon + "'";
		} else {
			whereStrA += " and a.gather_mon = '" + billmon.bill_id + "'";
			whereStr += " and a.gather_mon = '" + billmon.bill_id + "'";
		}
		// 判断报表ID
		if (qryStruct.general_id != null && !"".equals(qryStruct.general_id)) {
			whereStrA += " and a.general_ledger_id = '" + qryStruct.general_id
					+ "'";
			whereStr += " and c.general_ledger_id = '" + qryStruct.general_id
					+ "'";
		} else {
			whereStrA += " and a.general_ledger_id = '"
					+ report.general_ledger_id + "'";
			whereStr += " and c.general_ledger_id = '"
					+ report.general_ledger_id + "'";
		}

		// 相应条件分支区
		if ("0".equals(operType)) {

			//
			if ("1".equals(report.general_ledger_type)) {

				sql = " select a.general_ledger_id ,  a.finance_subject_id ,  a.finance_subject_fee,  "
						+ " c.finance_subject_nb , b.name , b.finance_subject_unit"
						+ " from general_ledger a  ,  finance_subject b ,general_rule_subject c"
						+ " where a.finance_subject_id = b.finance_subject_id "
						+ " and  a.general_ledger_id = c.general_ledger_id "
						+ " and  a.finance_subject_id = c.finance_subject_id "
						+ whereStrA + " order by  c.finance_subject_sequence";

			} else {

				sql = " select a.general_ledger_id ,  a.finance_subject_id ,  a.finance_subject_fee,  "
						+ " c.ref_finance_subject_nb , b.name , b.finance_subject_unit"
						+ " from general_ledger a  ,  finance_subject b ,general_ref_rule_subject c"
						+ " where a.finance_subject_id = b.finance_subject_id "
						+ " and  a.general_ledger_id = c.ref_general_ledger_id "
						+ " and  a.finance_subject_id = c.ref_finance_subject_id "
						+ whereStr + " order by  c.finance_subject_sequence";

			}

		} else if ("2".equals(operType)) {

			if ("1".equals(report.general_ledger_type)) {

				sql = " select a.general_ledger_id ,  a.finance_subject_id ,  a.finance_subject_fee,  "
						+ " c.finance_subject_nb , b.name , b.finance_subject_unit"
						+ " from general_ledger a  ,  finance_subject b ,general_rule_subject c"
						+ " where a.finance_subject_id = b.finance_subject_id "
						+ " and  a.general_ledger_id = c.general_ledger_id "
						+ " and  a.finance_subject_id = c.finance_subject_id "
						+ whereStrA + " order by  c.finance_subject_sequence";

			} else {

				sql = " select a.general_ledger_id ,  a.finance_subject_id ,  a.finance_subject_fee,  "
						+ " c.ref_finance_subject_nb , b.name , b.finance_subject_unit"
						+ " from general_ledger a  ,  finance_subject b ,general_ref_rule_subject c"
						+ " where a.finance_subject_id = b.finance_subject_id "
						+ " and  a.general_ledger_id = c.ref_general_ledger_id "
						+ " and  a.finance_subject_id = c.ref_finance_subject_id "
						+ whereStr + " order by  c.sequence";

			}
			//

		}

		System.out.println(sql);

		try {
			arr = WebDBUtil
					.execQryArray(DBTool.getFixedWLSConn("AcctSource",
							"t3://localhost:7100"), sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}

		//
		qryStruct.gather_mon = billmon.bill_id;
		qryStruct.general_id = report.general_ledger_id;
		qryStruct.general_name = report.general_ledger_name + "{"
				+ billmon.sta_time + "--" + billmon.end_time + "}";
		//
		session.removeAttribute("FinRptStruct");
		session.setAttribute("FinRptStruct", qryStruct);

		session.removeAttribute("FINRPTWEBKEYS");
		session.setAttribute("FINRPTWEBKEYS", arr);

		setNextScreen(request, "FINRPT.screen");
	}

}
