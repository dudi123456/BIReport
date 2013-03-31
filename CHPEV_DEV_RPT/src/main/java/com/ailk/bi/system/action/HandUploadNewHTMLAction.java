package com.ailk.bi.system.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

/**
 * <p>
 * Title: LongShine BI System
 * </p>
 * <p>
 * Description: autoFtp2.0监控及重传action
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: amdocs-longshine
 * </p>
 * 
 * @author wuyan
 * @version 2.0
 */

public class HandUploadNewHTMLAction extends HTMLActionSupport {
	private static final long serialVersionUID = 2142655251919759557L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		HttpSession session = request.getSession();

		// 处理类型,0:初始页面;1:重传
		String optype = request.getParameter("optype");
		if (optype == null || "".equals(optype)) {
			optype = "view_log"; // 默认页面
		}

		if ("view_log".equalsIgnoreCase(optype)) {
			// 缺省日期
			String end_date = request.getParameter("end_date");
			if (end_date == null || "".equals(end_date)) {
				end_date = DateUtil.getDiffDay(0, new Date());
			}
			String begin_date = request.getParameter("begin_date");
			if (begin_date == null || "".equals(begin_date)) {
				begin_date = end_date;
			}
			String oper_type = request.getParameter("oper_type");
			if (oper_type == null) {
				oper_type = "";
			}
			String date_type = request.getParameter("date_type");
			if (date_type == null) {
				date_type = "";
			}
			String oper_status = request.getParameter("oper_status");
			if (oper_status == null) {
				oper_status = "";
			}
			String file_name = request.getParameter("file_name");
			if (file_name == null) {
				file_name = "";
			}
			String report_kind = request.getParameter("report_kind");
			if (report_kind == null) {
				report_kind = "";
			}
			file_name = file_name.trim();
			String where = " and a.end_time >= " + (begin_date + "000000")
					+ " and a.end_time <= " + (end_date + "235959");
			if (!"".equals(oper_type)) {
				where += " and a.oper_type = '" + oper_type + "'";
			}
			if (!"".equals(date_type)) {
				where += " and b.report_type = '" + date_type + "'";
			}
			if (!"".equals(oper_status)) {
				where += " and a.oper_status = '" + oper_status + "'";
			}
			if (!"".equals(file_name)) {
				where += " and a.file_name like '%" + file_name + "%'";
			}
			if (!"".equals(report_kind)) {
				where += " and b.report_kind = '" + report_kind + "'";
			}

			String sql = "select a.report_id,a.file_name,a.oper_type,a.oper_status,a.status_desc,a.end_time,b.report_name from UI_MSU_UP_LOG a,ui_msu_up_report_config b WHERE a.report_id=b.report_id"
					+ where + " order by a.end_time desc";
			String[][] list = null;
			try {
				System.out.println("uploadMonitorViewSql======" + sql);
				list = WebDBUtil.execQryArray(sql, "");
			} catch (AppException ex) {
				ex.printStackTrace();
			}
			request.setAttribute("list", list);
			setNextScreen(request, "UploadMonitorViewLog.screen");
		} else {
			String tag = "D";
			if ("up_day".equalsIgnoreCase(optype)) {
				tag = "D";
			} else if ("up_week".equalsIgnoreCase(optype)) {
				tag = "W";
			} else if ("up_month".equalsIgnoreCase(optype)) {
				tag = "M";
			}
			String submit = request.getParameter("submit");
			if (submit == null || "".equals(submit)) {
				String rpt_date = DateUtil.getDiffDay(-1, new Date());
				if ("up_day".equalsIgnoreCase(optype)) {
					String cur_date = DateUtil.getDiffDay(-1, new Date());
					String rebackDateId = request.getParameter("reback");
					if (null != rebackDateId)
						cur_date = rebackDateId;
					// 获取当前系统日期的强制状态
					String cur_date_stat = getForceStat(cur_date);
					if (null == cur_date_stat)
						cur_date_stat = "0";
					// 获取由大到小的前10个日期及其状态
					String[][] dateList = getDateList(10);
					request.setAttribute("cur_date", cur_date);
					request.setAttribute("cur_date_stat", cur_date_stat);
					request.setAttribute("dateList", dateList);
				} else if ("up_month".equalsIgnoreCase(optype)) {
					rpt_date = DateUtil.getDiffMonth(-1, new Date());
				} else if ("up_week".equalsIgnoreCase(optype)) {
					rpt_date = DateUtil.getDiffWeek(new Date(), 0, 1)[0];
				}
				request.setAttribute("rpt_date", rpt_date);
				request.setAttribute("up_switch", getUpSwitch(tag));

				String screen = "UploadMonitorUpDay.screen";
				if ("up_week".equalsIgnoreCase(optype)) {
					screen = "UploadMonitorUpWeek.screen";
				} else if ("up_month".equalsIgnoreCase(optype)) {
					screen = "UploadMonitorUpMonth.screen";
				}
				setNextScreen(request, screen);
			} else if ("1".equals(submit)) { // 修改标志位
				String up_switch = request.getParameter("up_switch");
				String sql = "update UI_MSU_UP_SWITCH set SWITCH = '"
						+ up_switch + "' WHERE DATE_TYPE = '" + tag + "'";
				try {
					if (WebDBUtil.execUpdate(sql) != 1) {
						throw new HTMLActionException(session,
								HTMLActionException.ERROR_PAGE, "修改上传标志失败！");
					} else {
						throw new HTMLActionException(request.getSession(),
								HTMLActionException.SUCCESS_PAGE, "上传标志已修改。");
					}
				} catch (AppException ex) {
					ex.printStackTrace();
				}
			} else if ("2".equals(submit)) { // 选择报表
				String[][] results = null;

				String sql = "select report_id,report_code||report_sequence report_code,report_name from ui_msu_up_report_config";
				String where = " where is_valid = '1' and report_type = '"
						+ tag + "'";

				String todo = request.getParameter("todo");
				if (null != todo && "query".equals(todo)) {
					String code = com.ailk.bi.base.util.CommTool
							.getParameterGB(request, "code").trim();
					if (null == code)
						code = "";
					String name = com.ailk.bi.base.util.CommTool
							.getParameterGB(request, "name").trim();
					if (null == name)
						name = "";
					if (!"".equals(code) && !"".equals(name)) {
						where += " and report_code like '%" + code
								+ "%' and report_name like '%" + name + "%'";
					} else if (!"".equals(code)) {
						where += " and report_code like '%" + code + "%'";
					} else if (!"".equals(name)) {
						where += " and report_name like '%" + name + "%'";
					}
				}

				where += " order by report_code";
				sql += where;

				try {
					results = WebDBUtil.execQryArray(sql, "");
				} catch (AppException ex1) {
					ex1.printStackTrace();
				}
				request.setAttribute("list", results);
				setNextScreen(request, "UploadMonitorSelectRpt.screen");
			} else if ("3".equals(submit)) { // 报表上传
				String rpt_id = request.getParameter("rpt_id");
				String rpt_date = request.getParameter("rpt_date");
				String file_type = request.getParameter("file_type");
				String create_time = request.getParameter("create_time");
				String up_time = request.getParameter("up_time");

				String[] ids = rpt_id.split(",");
				String[] sqls = new String[ids.length];
				try {
					for (int i = 0; i < sqls.length; i++) {
						sqls[i] = SQLGenator.genSQL("I6011", ids[i], rpt_date,
								file_type, create_time, up_time);
						// System.out.println(sqls[i]);
					}
					int res = WebDBUtil.execTransUpdate(sqls);
					if (res != sqls.length || res == 0) {
						throw new HTMLActionException(session,
								HTMLActionException.ERROR_PAGE, "命令插入失败！");
					} else {
						throw new HTMLActionException(request.getSession(),
								HTMLActionException.SUCCESS_PAGE,
								"操作指令已提交，请您稍后再访问监控页面，查看操作执行情况。");
					}
				} catch (AppException e) {
					e.printStackTrace();
				}
			} else if ("5".equals(submit) && "up_day".equalsIgnoreCase(optype)) { // 获取指定日期的强制状态（用于日报上传）
				String date_id = request.getParameter("dateId");
				String dateStat = getForceStat(date_id);
				if (null == dateStat)
					dateStat = "0";
				// 获取输出流
				response.setContentType("text/html;charset=utf-8");
				PrintWriter pw = null;
				try {
					pw = response.getWriter();
				} catch (IOException e) {
					System.out.println("AJAX,获取输出流时出错：\n");
					e.printStackTrace();
				}
				pw.write(dateStat);
				setNvlNextScreen(request);
			} else if ("6".equals(submit) && "up_day".equalsIgnoreCase(optype)) { // 修改指定日期的强制状态（用于日报上传）
				String date_id = request.getParameter("start_date");
				String date_stat = request.getParameter("dateStatSel");
				if (!changeForceStat(date_id, date_stat)) {
					throw new HTMLActionException(session,
							HTMLActionException.ERROR_PAGE, "修改上传强制关闭标志失败！",
							"UploadMonitorNew.do?optype=up_day");
				} else {
					throw new HTMLActionException(request.getSession(),
							HTMLActionException.SUCCESS_PAGE, "上传强制关闭标志已修改。",
							"UploadMonitorNew.do?optype=up_day&reback="
									+ date_id);
				}
			}
		}

		// doTrans end
	}

	private String getUpSwitch(String tag) {
		String up_switch = null;
		String sql = "SELECT SWITCH FROM UI_MSU_UP_SWITCH WHERE DATE_TYPE = '"
				+ tag + "'";
		try {
			String[][] tmp = WebDBUtil.execQryArray(sql, "");
			if (tmp != null && tmp.length > 0) {
				up_switch = tmp[0][0];
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return up_switch;
	}

	private boolean changeForceStat(String date_id, String date_stat) {
		String sql = "SELECT SWITCH FROM UI_MSU_UP_FORCE_SWITCH WHERE DATE_ID = '"
				+ date_id + "'";
		boolean flag = false;
		boolean res = false;
		try {
			String[][] tmp = WebDBUtil.execQryArray(sql, "");
			if (tmp != null && tmp.length > 0) {
				flag = true;
			}
			if (flag) {
				sql = "UPDATE UI_MSU_UP_FORCE_SWITCH SET SWITCH = '"
						+ date_stat + "' WHERE DATE_ID='" + date_id + "'";
			} else {
				sql = "INSERT INTO UI_MSU_UP_FORCE_SWITCH(DATE_ID,SWITCH) VALUES('"
						+ date_id + "','" + date_stat + "')";
			}
			if (WebDBUtil.execUpdate(sql) > 0)
				res = true;
			else
				res = false;
		} catch (AppException ex) {
			ex.printStackTrace();
			return false;
		}
		return res;
	}

	private String getForceStat(String date_id) {
		String up_switch = null;
		String sql = "SELECT SWITCH FROM UI_MSU_UP_FORCE_SWITCH WHERE DATE_ID = '"
				+ date_id + "'";
		try {
			String[][] tmp = WebDBUtil.execQryArray(sql, "");
			if (tmp != null && tmp.length > 0) {
				up_switch = tmp[0][0];
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return up_switch;
	}

	private String[][] getDateList(int i) {
		String sql = "SELECT DATE_ID, SWITCH FROM ("
				+ "SELECT DATE_ID,SWITCH FROM UI_MSU_UP_FORCE_SWITCH ORDER BY DATE_ID DESC"
				+ ") WHERE ROWNUM <= " + i;
		System.out.println("sql====" + sql);
		String[][] res = null;
		try {
			res = WebDBUtil.execQryArray(sql, "");
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return res;
	}
	// class end
}
