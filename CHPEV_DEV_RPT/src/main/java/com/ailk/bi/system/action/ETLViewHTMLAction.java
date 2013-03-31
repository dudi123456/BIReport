package com.ailk.bi.system.action;

import java.util.Vector;
import waf.controller.web.action.*;
import javax.servlet.http.*;
import com.ailk.bi.base.struct.*;
import com.ailk.bi.base.table.*;
import com.ailk.bi.base.util.*;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;

public class ETLViewHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1507146640046122751L;

	@SuppressWarnings({ "rawtypes" })
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		// 变量申明区
		HttpSession session = request.getSession();
		LsbiQryStruct qryStruct = new LsbiQryStruct();
		TEtlLogTable[] logs = null;
		String sql = "";
		// 程序判断区
		String operType = request.getParameter("oper_type");
		if (operType == null || "".equals(operType)) {
			operType = "0";
		}

		// 相应条件分支区
		if ("0".equals(operType)) {
			// 取页面角色信息
			try {
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "系统监控页面信息有误，请反馈系统管理员！");
			}
			// 调试
			if (qryStruct.oper_freq == null || "".equals(qryStruct.oper_freq)) {
				qryStruct.oper_freq = "0";// 默认为日频次
			}
			// 频次日期处理
			if ("0".equals(qryStruct.oper_freq)) {
				if (qryStruct.end_day == null || "".equals(qryStruct.end_day)) {
					qryStruct.end_day = DateUtil.getDiffDay(-1,
							DateUtil.getNowDate());
				}
				if (qryStruct.begin_day == null
						|| "".equals(qryStruct.begin_day)) {
					qryStruct.begin_day = DateUtil.getDiffDay(-1,
							DateUtil.getNowDate());
				}

			} else if ("1".equals(qryStruct.oper_freq)) {
				if (qryStruct.end_day == null || "".equals(qryStruct.end_day)) {
					qryStruct.end_day = DateUtil.getDiffMonth(-1,
							DateUtil.getNowDate())
							+ "01";
				}
				if (qryStruct.begin_day == null
						|| "".equals(qryStruct.begin_day)) {
					qryStruct.begin_day = DateUtil.getDiffMonth(-6,
							DateUtil.getNowDate() + "01");
				}
			}
			// //操作结果处理
			// if(qryStruct.result==null||"".equals(qryStruct.result)){
			// qryStruct.result = "99";
			// }
			// 操作层次
			if (qryStruct.sess_layer == null || "".equals(qryStruct.sess_layer)) {
				qryStruct.sess_layer = "ODS";
			}
			session.removeAttribute(WebKeys.ATTR_LsbiQryStruct);
			session.setAttribute(WebKeys.ATTR_LsbiQryStruct, qryStruct);
			// 查询条件
			String whereStr = "";

			// 频次和日期-日
			if (qryStruct.oper_freq != null && !"".equals(qryStruct.oper_freq)
					&& "0".equals(qryStruct.oper_freq)) {
				whereStr += " and  length(A.gather_time)=8";
				if (qryStruct.begin_day != null
						&& !"".equals(qryStruct.begin_day)) {
					whereStr += " and  A.gather_time>=" + qryStruct.begin_day;
				}
				if (qryStruct.end_day != null && !"".equals(qryStruct.end_day)) {
					whereStr += " and  A.gather_time<=" + qryStruct.end_day;
				}
			}
			// 频次和日期-月
			if (qryStruct.oper_freq != null && !"".equals(qryStruct.oper_freq)
					&& "1".equals(qryStruct.oper_freq)) {
				whereStr += " and  length(A.gather_time)=6";
				if (qryStruct.begin_day != null
						&& !"".equals(qryStruct.begin_day)) {
					whereStr += " and  A.gather_time>="
							+ qryStruct.begin_day.substring(0, 6);
				}
				if (qryStruct.end_day != null && !"".equals(qryStruct.end_day)) {
					whereStr += " and  A.gather_time<="
							+ qryStruct.end_day.substring(0, 6);
				}
			}
			// 操作结果
			if (qryStruct.result != null && !"".equals(qryStruct.result)) {
				if ("99".equals(qryStruct.result)) {
					whereStr += " and  A.result not in(0,1)";
				} else {
					whereStr += " and  A.result =" + qryStruct.result;
				}
			}
			// 操作层次
			if (qryStruct.sess_layer != null
					&& !"".equals(qryStruct.sess_layer)) {
				whereStr += " and  B.sess_layer ='" + qryStruct.sess_layer
						+ "'";
			}

			try {
				sql = SQLGenator.genSQL("Q1002", whereStr);
				// System.out.println("Q1002====0====="+sql);
				Vector v = WebDBUtil.execQryVector(sql, "");
				if (v != null && v.size() > 0) {
					logs = new TEtlLogTable[v.size()];
				}
				for (int i = 0; v != null && i < v.size(); i++) {
					Vector temp = (Vector) v.get(i);
					logs[i] = new TEtlLogTable();
					logs[i].sess_id = (String) temp.get(0);
					logs[i].gather_time = (String) temp.get(1);
					logs[i].start_time = (String) temp.get(2);
					logs[i].end_time = (String) temp.get(3);
					logs[i].src_rows = (String) temp.get(4);
					logs[i].trg_succ_rows = (String) temp.get(5);
					logs[i].trg_fail_rows = (String) temp.get(6);
					logs[i].result = (String) temp.get(7);
					logs[i].result_info = (String) temp.get(8);
					logs[i].redo_flag = (String) temp.get(9);
					logs[i].curr_flag = (String) temp.get(10);
					logs[i].chkpt_done_flag = (String) temp.get(11);
					logs[i].sess_layer = (String) temp.get(12);
					logs[i].sess_knd = (String) temp.get(13);
					logs[i].sess_name = (String) temp.get(14);
					logs[i].sess_desc = (String) temp.get(15);

					logs[i].src_tab_list = (String) temp.get(16);
					logs[i].trg_tab_list = (String) temp.get(17);
					logs[i].src_obj_name = getName(logs[i].src_tab_list);
					logs[i].trg_obj_name = getName(logs[i].trg_tab_list);

					logs[i].func_list = (String) temp.get(18);
					logs[i].valid_flag = (String) temp.get(19);
					logs[i].sess_cycle_type = (String) temp.get(20);

				}
			} catch (AppException ex) {
				ex.printStackTrace();
			}

			session.setAttribute(WebKeys.ATTR_TSysEtlLogTable, logs);
			setNextScreen(request, "ETL.screen");

		} else if ("1".equals(operType)) {
			String whereStr = "";
			String sess_id = request.getParameter("sess_id");
			whereStr += "and A.sess_id = " + sess_id;
			try {
				sql = SQLGenator.genSQL("Q1002", whereStr);
				// System.out.println("Q1002====1====="+sql);
				Vector v = WebDBUtil.execQryVector(sql, "");
				if (v != null && v.size() > 0) {
					logs = new TEtlLogTable[v.size()];
				}
				for (int i = 0; v != null && i < v.size(); i++) {
					Vector temp = (Vector) v.get(i);
					logs[i] = new TEtlLogTable();
					logs[i].sess_id = (String) temp.get(0);
					logs[i].gather_time = (String) temp.get(1);
					logs[i].start_time = (String) temp.get(2);
					logs[i].end_time = (String) temp.get(3);
					logs[i].src_rows = (String) temp.get(4);
					logs[i].trg_succ_rows = (String) temp.get(5);
					logs[i].trg_fail_rows = (String) temp.get(6);
					logs[i].result = (String) temp.get(7);
					logs[i].result_info = (String) temp.get(8);
					logs[i].redo_flag = (String) temp.get(9);
					logs[i].curr_flag = (String) temp.get(10);
					logs[i].chkpt_done_flag = (String) temp.get(11);
					logs[i].sess_layer = (String) temp.get(12);
					logs[i].sess_knd = (String) temp.get(13);
					logs[i].sess_name = (String) temp.get(14);
					logs[i].sess_desc = (String) temp.get(15);

					logs[i].src_tab_list = (String) temp.get(16);
					logs[i].trg_tab_list = (String) temp.get(17);
					logs[i].src_obj_name = getName(logs[i].src_tab_list);
					logs[i].trg_obj_name = getName(logs[i].trg_tab_list);

					logs[i].func_list = (String) temp.get(18);
					logs[i].valid_flag = (String) temp.get(19);
					logs[i].sess_cycle_type = (String) temp.get(20);
				}
			} catch (AppException ex) {
				ex.printStackTrace();
			}

			session.setAttribute(WebKeys.ATTR_TSysEtlLogTable_A, logs);
			setNextScreen(request, "ETLView.screen");

		}
	}

	/**
	 * 处理源，目标名称
	 * 
	 * @param list_name
	 * @return
	 */
	public static String getName(String list_name) {
		String name = "";
		String arr[] = list_name.split("[|]");
		for (int i = 0; arr != null && i < arr.length; i++) {
			name += "<font color=\"red\">[</font>" + arr[i].trim()
					+ "<font color=\"red\">]</font><br>";
		}

		return name;
	}

}
