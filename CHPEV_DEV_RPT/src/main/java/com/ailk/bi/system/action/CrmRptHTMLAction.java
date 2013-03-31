package com.ailk.bi.system.action;

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.struct.CrmRptStruct;
import com.ailk.bi.base.table.FraudStatReportTable;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.common.CrmTool;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "rawtypes" })
public class CrmRptHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 612578238223296801L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		// 变量申明区
		HttpSession session = request.getSession();

		// 欺诈类型
		HashMap mapCode = CrmTool.getCrmMap("code");

		// 用户类型
		HashMap mapUserType = CrmTool.getCrmMap("user");

		// 业务类型
		HashMap mapTeleType = CrmTool.getCrmMap("tele");

		// 欺诈级别
		HashMap mapLevel = CrmTool.getCrmMap("level");

		// 话单类型
		HashMap mapCharge = CrmTool.getCrmMap("record");

		// 置入session
		session.setAttribute("mapCode", mapCode);
		session.setAttribute("mapTeleType", mapTeleType);
		session.setAttribute("mapUserType", mapUserType);
		session.setAttribute("mapLevel", mapLevel);
		session.setAttribute("mapCharge", mapCharge);

		// 定义查询统计结构
		CrmRptStruct qryStruct = new CrmRptStruct();
		FraudStatReportTable[] crms = null;
		FraudStatReportTable[] crmlist = null;
		FraudStatReportTable codeList[] = null;
		FraudStatReportTable userList[] = null;
		FraudStatReportTable teleList[] = null;
		FraudStatReportTable levelList[] = null;

		String sql = "";
		String sqla = "";
		// 程序判断区
		String operType = request.getParameter("oper_type");
		if (operType == null || "".equals(operType)) {
			operType = "0";
		}

		// 相应条件分支区
		if ("0".equals(operType)) {

			//
			try {
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "系统监控页面信息有误，请反馈系统管理员！");
			}
			//
			if (qryStruct.end_day == null || "".equals(qryStruct.end_day)) {
				qryStruct.end_day = DateUtil.getDiffDay(-1,
						DateUtil.getNowDate());
			}
			if (qryStruct.begin_day == null || "".equals(qryStruct.begin_day)) {
				qryStruct.begin_day = DateUtil.getDiffDay(-7,
						DateUtil.getNowDate());
			}
			//
			String whereStr = " 1=1 ";
			if (qryStruct.end_day != null && !"".equals(qryStruct.end_day)) {
				whereStr += " and  fraud_evt_call_date <='" + qryStruct.end_day
						+ "'";
			}
			if (qryStruct.begin_day != null && !"".equals(qryStruct.begin_day)) {
				whereStr += " and fraud_evt_call_date >='"
						+ qryStruct.begin_day + "'";
			}
			//
			try {
				sql = "select '1' as catid ,FRAUD_ITEM_CODE as type, count(*) from Fraud_stat_report where "
						+ whereStr
						+ " group by FRAUD_ITEM_CODE "
						+ " union"
						+ " select '2' as catid ,FRAUD_EVT_TELE_TYPE as type, count(*) from Fraud_stat_report where "
						+ whereStr
						+ " group by FRAUD_EVT_TELE_TYPE"
						+ " union "
						+ " select '3' as catid ,FRAUD_USER_TYPE as type, count(*) from Fraud_stat_report where "
						+ whereStr
						+ " group by FRAUD_USER_TYPE "
						+ " union"
						+ " select '4' as catid ,FRAUD_EMERG_LEVEL as type, count(*) from Fraud_stat_report where "
						+ whereStr + " group by FRAUD_EMERG_LEVEL ";

				System.out.println(sql);
				Vector v = WebDBUtil.execQryVector(sql, "");
				if (v != null && v.size() > 0) {
					crms = new FraudStatReportTable[v.size()];
				}
				int countcode = 0;
				int countuser = 0;
				int counttele = 0;
				int countlevel = 0;

				for (int i = 0; v != null && i < v.size(); i++) {
					Vector temp = (Vector) v.get(i);
					crms[i] = new FraudStatReportTable();
					crms[i].cat_id = (String) temp.get(0);
					crms[i].cat_type = (String) temp.get(1);
					crms[i].cat_num = (String) temp.get(2);
					if ("1".equals(crms[i].cat_id)) {
						countcode++;
					} else if ("2".equals(crms[i].cat_id)) {
						countuser++;
					} else if ("3".equals(crms[i].cat_id)) {
						counttele++;
					} else if ("4".equals(crms[i].cat_id)) {
						countlevel++;
					}
				}
				//
				if (countcode > 0) {
					codeList = new FraudStatReportTable[countcode];
					for (int i = 0, j = 0; i < crms.length; i++) {
						if (crms[i].cat_id.equals("1")) {
							codeList[j] = new FraudStatReportTable();
							codeList[j] = crms[i];
							j++;
						}

					}
				}
				//
				if (countuser > 0) {
					userList = new FraudStatReportTable[countuser];
					for (int i = 0, j = 0; i < crms.length; i++) {
						if (crms[i].cat_id.equals("3")) {
							userList[j] = new FraudStatReportTable();
							userList[j] = crms[i];
							j++;
						}

					}
				}
				//
				if (counttele > 0) {
					teleList = new FraudStatReportTable[counttele];
					for (int i = 0, j = 0; i < crms.length; i++) {
						if (crms[i].cat_id.equals("2")) {
							teleList[j] = new FraudStatReportTable();
							teleList[j] = crms[i];
							j++;
						}

					}
				}
				//
				if (countlevel > 0) {
					levelList = new FraudStatReportTable[countlevel];
					for (int i = 0, j = 0; i < crms.length; i++) {
						if (crms[i].cat_id.equals("4")) {
							levelList[j] = new FraudStatReportTable();
							levelList[j] = crms[i];
							j++;
						}

					}
				}

				//
				sqla = " select FRAUD_WARN_SN,FRAUD_ITEM_CODE,FRAUD_EVT_TELE_TYPE, FRAUD_USER_TYPE,"
						+ "FRAUD_EMERG_LEVEL,FRAUD_LOCAL_NET,FRAUD_EVT_CALL_DATE,FRAUD_EVT_ANSWER_DATE,"
						+ "FRAUD_EVT_CALL_DURATION,FRAUD_EVT_CALLOUT_DEVICE,FRAUD_EVT_CALLIN_DEVICE,"
						+ "FRAUD_EVT_CALL_COUNTRY,FRAUD_EVT_RECORD_TYPE,FRAUD_CHARGE_PREPAY,"
						+ "FRAUD_CHARGE,FRAUD_COMMENTS from Fraud_stat_report where "
						+ whereStr;

				//
				Vector va = WebDBUtil.execQryVector(sqla, "");
				if (va != null && va.size() > 0) {
					crmlist = new FraudStatReportTable[va.size()];
				}

				for (int i = 0; va != null && i < va.size(); i++) {
					Vector temp = (Vector) va.get(i);
					crmlist[i] = new FraudStatReportTable();
					crmlist[i].fraud_warn_sn = (String) temp.get(0);
					crmlist[i].fraud_item_code = (String) temp.get(1);
					crmlist[i].fraud_evt_tele_type = (String) temp.get(2);
					crmlist[i].fraud_user_type = (String) temp.get(3);
					crmlist[i].fraud_emerg_level = (String) temp.get(4);
					crmlist[i].fraud_local_net = (String) temp.get(5);
					crmlist[i].fraud_evt_call_date = (String) temp.get(6);
					crmlist[i].fraud_evt_answer_date = (String) temp.get(7);
					crmlist[i].fraud_evt_call_duration = (String) temp.get(8);
					crmlist[i].fraud_evt_callout_device = (String) temp.get(9);
					crmlist[i].fraud_evt_callin_device = (String) temp.get(10);
					crmlist[i].fraud_evt_call_country = (String) temp.get(11);
					crmlist[i].fraud_evt_record_type = (String) temp.get(12);
					crmlist[i].fraud_charge_prepay = (String) temp.get(13);
					crmlist[i].fraud_charge = (String) temp.get(14);
					crmlist[i].fraud_comments = (String) temp.get(15);
				}

			} catch (AppException ex) {
				ex.printStackTrace();
			}

			//

			session.removeAttribute("CRMRPTWEBKEYS_A");
			session.removeAttribute("CRMRPTWEBKEYS_B");
			session.removeAttribute("CRMRPTWEBKEYS_C");
			session.removeAttribute("CRMRPTWEBKEYS_D");
			//

			session.setAttribute("CRMRPTWEBKEYS_A", codeList);
			session.setAttribute("CRMRPTWEBKEYS_B", userList);
			session.setAttribute("CRMRPTWEBKEYS_C", teleList);
			session.setAttribute("CRMRPTWEBKEYS_D", levelList);

			session.removeAttribute("CrmRptStruct");
			session.setAttribute("CrmRptStruct", qryStruct);

			session.removeAttribute("CRMRPTWEBKEYS");
			session.setAttribute("CRMRPTWEBKEYS", crmlist);

			setNextScreen(request, "CRMRPT.screen");

		} else if ("2".equals(operType)) {

			//
			//
			try {
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "系统监控页面信息有误，请反馈系统管理员！");
			}
			//
			if (qryStruct.end_day == null || "".equals(qryStruct.end_day)) {
				qryStruct.end_day = DateUtil.getDiffDay(-1,
						DateUtil.getNowDate());
			}
			if (qryStruct.begin_day == null || "".equals(qryStruct.begin_day)) {
				qryStruct.begin_day = DateUtil.getDiffDay(-7,
						DateUtil.getNowDate());
			}
			//
			String whereStr = " 1=1 ";
			if (qryStruct.end_day != null && !"".equals(qryStruct.end_day)) {
				whereStr += " and  fraud_evt_call_date <='" + qryStruct.end_day
						+ "'";
			}
			if (qryStruct.begin_day != null && !"".equals(qryStruct.begin_day)) {
				whereStr += " and fraud_evt_call_date >='"
						+ qryStruct.begin_day + "'";
			}
			if (qryStruct.item_code != null && !"".equals(qryStruct.item_code)) {
				whereStr += " and FRAUD_ITEM_CODE ='" + qryStruct.item_code
						+ "'";
			}
			if (qryStruct.tele_type != null && !"".equals(qryStruct.tele_type)) {
				whereStr += " and  FRAUD_EVT_TELE_TYPE ='"
						+ qryStruct.tele_type + "'";
			}
			if (qryStruct.user_type != null && !"".equals(qryStruct.user_type)) {
				whereStr += " and FRAUD_USER_TYPE ='" + qryStruct.user_type
						+ "'";
			}
			if (qryStruct.level != null && !"".equals(qryStruct.level)) {
				whereStr += " and FRAUD_EMERG_LEVEL ='" + qryStruct.level + "'";
			}
			//

			sqla = " select FRAUD_WARN_SN,FRAUD_ITEM_CODE,FRAUD_EVT_TELE_TYPE, FRAUD_USER_TYPE,"
					+ "FRAUD_EMERG_LEVEL,FRAUD_LOCAL_NET,FRAUD_EVT_CALL_DATE,FRAUD_EVT_ANSWER_DATE,"
					+ "FRAUD_EVT_CALL_DURATION,FRAUD_EVT_CALLOUT_DEVICE,FRAUD_EVT_CALLIN_DEVICE,"
					+ "FRAUD_EVT_CALL_COUNTRY,FRAUD_EVT_RECORD_TYPE,FRAUD_CHARGE_PREPAY,"
					+ "FRAUD_CHARGE,FRAUD_COMMENTS from Fraud_stat_report where "
					+ whereStr;

			System.out.println(sqla);
			Vector va = new Vector();
			try {
				va = WebDBUtil.execQryVector(sqla, "");
			} catch (AppException e) {

				e.printStackTrace();
			}
			if (va != null && va.size() > 0) {
				crmlist = new FraudStatReportTable[va.size()];
			}

			for (int i = 0; va != null && i < va.size(); i++) {
				Vector temp = (Vector) va.get(i);
				crmlist[i] = new FraudStatReportTable();
				crmlist[i].fraud_warn_sn = (String) temp.get(0);
				crmlist[i].fraud_item_code = (String) temp.get(1);
				crmlist[i].fraud_evt_tele_type = (String) temp.get(2);
				crmlist[i].fraud_user_type = (String) temp.get(3);
				crmlist[i].fraud_emerg_level = (String) temp.get(4);
				crmlist[i].fraud_local_net = (String) temp.get(5);
				crmlist[i].fraud_evt_call_date = (String) temp.get(6);
				crmlist[i].fraud_evt_answer_date = (String) temp.get(7);
				crmlist[i].fraud_evt_call_duration = (String) temp.get(8);
				crmlist[i].fraud_evt_callout_device = (String) temp.get(9);
				crmlist[i].fraud_evt_callin_device = (String) temp.get(10);
				crmlist[i].fraud_evt_call_country = (String) temp.get(11);
				crmlist[i].fraud_evt_record_type = (String) temp.get(12);
				crmlist[i].fraud_charge_prepay = (String) temp.get(13);
				crmlist[i].fraud_charge = (String) temp.get(14);
				crmlist[i].fraud_comments = (String) temp.get(15);
			}

			//
			session.removeAttribute("CrmRptStruct_A");
			session.setAttribute("CrmRptStruct_A", qryStruct);

			session.removeAttribute("CRMRPTWEBKEYS_LIST");
			session.setAttribute("CRMRPTWEBKEYS_LIST", crmlist);
			//
			setNextScreen(request, "CRMRPT_DETAIL.screen");

		}
	}
}
