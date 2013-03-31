package com.ailk.bi.system.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.struct.LsbiQryStruct;
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.table.UiSysLogTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

@SuppressWarnings({ "rawtypes" })
public class LogViewHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8465332942900249268L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		// 变量申明区
		HttpSession session = request.getSession();
		LsbiQryStruct qryStruct = new LsbiQryStruct();
		UiSysLogTable[] logs = null;
		String sql = "";
		// 程序判断区
		String operType = request.getParameter("oper_type");
		if (operType == null || "".equals(operType)) {
			operType = "0";
		}

		//
		String whereStr = " 1=1 ";
		try {
			AppWebUtil.getHtmlObject(request, "qry", qryStruct);
		} catch (AppException ex) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "用户受理信息有误！");
		}

		if (qryStruct.begin_day == null || "".equals(qryStruct.begin_day)) {
			qryStruct.begin_day = DateUtil.getDiffDay(-30,
					DateUtil.getNowDate());
		}
		if (qryStruct.end_day == null || "".equals(qryStruct.end_day)) {
			qryStruct.end_day = DateUtil.getDiffDay(0, DateUtil.getNowDate());
		}
		//
		if (qryStruct.begin_day != null && !"".equals(qryStruct.begin_day)) {
			whereStr += " and to_char(log_oper_time,'YYYYMMDD') >='"
					+ qryStruct.begin_day + "'";
		}
		if (qryStruct.end_day != null && !"".equals(qryStruct.end_day)) {
			whereStr += " and to_char(log_oper_time,'YYYYMMDD') <='"
					+ qryStruct.end_day + "'";
		}
		if (qryStruct.oper_name != null && !"".equals(qryStruct.oper_name)) {
			whereStr += " and log_oper_name like '%" + qryStruct.oper_name
					+ "%'";
		}
		if (qryStruct.oper_no != null && !"".equals(qryStruct.oper_no)) {
			whereStr += " and log_oper_no = '" + qryStruct.oper_no + "'";
		}
		//
		// 相应条件分支区
		if ("0".equals(operType)) {

			try {
				sql = SQLGenator.genSQL("Q1003", whereStr);
				System.out.println("Q1003=========" + sql);
				Vector v = WebDBUtil.execQryVector(sql, "");
				if (v != null && v.size() > 0) {
					logs = new UiSysLogTable[v.size()];
				}
				for (int i = 0; v != null && i < v.size(); i++) {
					Vector temp = (Vector) v.get(i);
					logs[i] = new UiSysLogTable();
					logs[i].log_oper_no = (String) temp.get(0);
					logs[i].log_oper_name = (String) temp.get(1);
					logs[i].dept_no = (String) temp.get(2);
					logs[i].dept_name = (String) temp.get(3);
					logs[i].region_id = (String) temp.get(4);
					logs[i].region_name = (String) temp.get(5);
					logs[i].login_num = (String) temp.get(6);
				}
			} catch (AppException ex) {
				ex.printStackTrace();
			}
			session.removeAttribute(WebKeys.ATTR_LsbiQryStruct);
			session.setAttribute(WebKeys.ATTR_LsbiQryStruct, qryStruct);
			session.removeAttribute(WebKeys.ATTR_UiSysLogTable);
			session.setAttribute(WebKeys.ATTR_UiSysLogTable, logs);
			setNextScreen(request, "LOG.screen");

		} else if ("1".equals(operType)) {

			String oper_no = request.getParameter("login_oper_no");
			if (oper_no == null || "".equals(oper_no)) {
				oper_no = CommonFacade.getLoginId(session);
			}

			try {
				sql = SQLGenator.genSQL("Q1004", oper_no);
				// System.out.println("Q1004=========" + sql);
				Vector v = WebDBUtil.execQryVector(sql, "");
				if (v != null && v.size() > 0) {
					logs = new UiSysLogTable[v.size()];
				}
				for (int i = 0; v != null && i < v.size(); i++) {
					Vector temp = (Vector) v.get(i);
					logs[i] = new UiSysLogTable();
					logs[i].service_sn = (String) temp.get(0);
					logs[i].log_seq = (String) temp.get(1);
					logs[i].log_type = (String) temp.get(2);
					logs[i].log_oper_no = (String) temp.get(3);
					logs[i].log_oper_name = (String) temp.get(4);
					logs[i].log_ip = (String) temp.get(5);
					logs[i].log_oper_time = (String) temp.get(6);
					logs[i].dept_no = (String) temp.get(7);
					logs[i].dept_name = (String) temp.get(8);
					logs[i].region_id = (String) temp.get(9);
					logs[i].region_name = (String) temp.get(10);
				}
			} catch (AppException ex) {
				ex.printStackTrace();
			}
			InfoOperTable sstUser = new InfoOperTable();
			try {
				sql = SQLGenator.genSQL("Q1005", oper_no);
				// System.out.println("Q1005=========" + sql);
				Vector v = WebDBUtil.execQryVector(sql, "");
				for (int i = 0; v != null && i < v.size(); i++) {
					Vector temp = (Vector) v.get(0);
					sstUser.oper_no = (String) temp.get(0);
					sstUser.oper_name = (String) temp.get(1);
					sstUser.login_num = (String) temp.get(2);
					sstUser.max_log_time = (String) temp.get(3);
					sstUser.min_log_time = (String) temp.get(4);
				}
			} catch (AppException ex) {
				ex.printStackTrace();
			}
			session.removeAttribute(WebKeys.ATTR_InfoOperTable_A);
			session.removeAttribute(WebKeys.ATTR_UiSysLogTable_A);
			session.setAttribute(WebKeys.ATTR_InfoOperTable_A, sstUser);
			session.setAttribute(WebKeys.ATTR_UiSysLogTable_A, logs);
			setNextScreen(request, "LogView.screen");

		} else if ("2".equals(operType)) {

			String log_seq = request.getParameter("log_seq");
			if (log_seq == null || "".equals(log_seq)) {
				log_seq = session.getId();
			}
			String oper_no = request.getParameter("log_oper_no");
			if (oper_no == null) {
				oper_no = "";
			}

			String oper_name = request.getParameter("log_oper_name");
			if (oper_name == null) {
				oper_name = "";
			}
			String login_date = request.getParameter("log_oper_time");
			if (login_date == null) {
				login_date = "";
			}
			String login_ip = request.getParameter("log_ip");
			if (login_ip == null) {
				login_ip = "";
			}
			// 该次登陆基本信息
			InfoOperTable sstUser = new InfoOperTable();
			sstUser.log_ip = login_ip;
			sstUser.oper_no = oper_no;
			sstUser.oper_name = oper_name;
			sstUser.log_oper_time = login_date;
			sstUser.log_seq = log_seq;

			//
			try {
				sql = SQLGenator.genSQL("Q1006", log_seq);
				// System.out.println("Q1006=========" + sql);
				Vector v = WebDBUtil.execQryVector(sql, "");
				if (v != null && v.size() > 0) {
					logs = new UiSysLogTable[v.size()];
				}
				for (int i = 0; v != null && i < v.size(); i++) {
					Vector temp = (Vector) v.get(i);
					logs[i] = new UiSysLogTable();
					logs[i].service_sn = (String) temp.get(0);
					logs[i].log_seq = (String) temp.get(1);
					logs[i].log_type = (String) temp.get(2);
					logs[i].log_oper_no = (String) temp.get(3);
					logs[i].log_oper_name = (String) temp.get(4);
					logs[i].log_ip = (String) temp.get(5);
					logs[i].log_oper_time = (String) temp.get(6);
					logs[i].obj_id = (String) temp.get(7);
					logs[i].obj_name = (String) temp.get(8);

				}
			} catch (AppException ex) {
				ex.printStackTrace();
			}
			session.removeAttribute(WebKeys.ATTR_InfoOperTable_B);
			session.removeAttribute(WebKeys.ATTR_UiSysLogTable_B);
			session.setAttribute(WebKeys.ATTR_UiSysLogTable_B, logs);
			session.setAttribute(WebKeys.ATTR_InfoOperTable_B, sstUser);
			setNextScreen(request, "LogHis.screen");
		}

	}

}
