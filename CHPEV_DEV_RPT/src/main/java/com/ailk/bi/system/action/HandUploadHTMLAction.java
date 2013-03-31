package com.ailk.bi.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.common.UnicomUploadUtil;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

/**
 * <p>
 * Title: LongShine BI System
 * </p>
 * <p>
 * Description: 手动上传action
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: amdocs-longshine
 * </p>
 * 
 * @author wuyan
 * @version 1.0
 */

public class HandUploadHTMLAction extends HTMLActionSupport {
	private static final long serialVersionUID = 2142655251919759557L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		HttpSession session = request.getSession();

		// 处理类型,0:初始页面;1:重传
		String submitType = request.getParameter("submitType");
		if (submitType == null || "".equals(submitType)) {
			submitType = "0"; // 默认页面
		}

		String tag = request.getParameter("tag");
		if (tag == null || "".equals(tag)) {
			tag = "D"; // 默认为日报
		}

		// 查看当期的上传文件列表,上传监控页面
		if ("0".equals(submitType)) {
			// 缺省日期
			String end_date = request.getParameter("end_date");
			if (end_date == null || "".equals(end_date)) {
				end_date = UnicomUploadUtil.getDefaultDate(tag);
			}
			String begin_date = request.getParameter("begin_date");
			if (begin_date == null || "".equals(begin_date)) {
				begin_date = end_date;
			}

			// 报表、上传、下载日期
			String date_type = request.getParameter("date_type");
			if (date_type == null || "".equals(date_type)) {
				date_type = "RPT"; // 默认为报表日期
			}

			// 文件名
			String file_name = request.getParameter("file_name");
			if (file_name == null) {
				file_name = "";
			}

			// 日期字段
			String dateCol = "garther_id";
			if ("UP".equals(date_type)) {
				dateCol = "up_time";
				begin_date = "to_date('" + begin_date
						+ "000000','YYYYMMDDHH24MISS')";
				end_date = "to_date('" + end_date
						+ "235959','YYYYMMDDHH24MISS')";
			} else if ("DOWN".equals(date_type)) {
				dateCol = "down_time";
				begin_date = "to_date('" + begin_date
						+ "000000','YYYYMMDDHH24MISS')";
				end_date = "to_date('" + end_date
						+ "235959','YYYYMMDDHH24MISS')";
			}

			String sql = "select rpt_filename,report_name,up_status,down_status,chk_status,err_desc,report_code,garther_id,"
					+ "to_char(up_time,'YYYY-MM-DD am hh:mi:ss'),to_char(down_time,'YYYY-MM-DD am hh:mi:ss') from RECALLMONTH_RPTLOG"
					+ " where "
					+ dateCol
					+ " >= "
					+ begin_date
					+ " and "
					+ dateCol
					+ " <= "
					+ end_date
					+ " and rpt_type = '"
					+ tag
					+ "'";
			if (!"".equals(file_name.trim())) {
				sql += " and rpt_filename like '%" + file_name.trim() + "%'";
			}
			sql += " order by garther_id desc,rpt_filename,up_time";

			System.out.println("getUploadFileList==============" + sql);
			String[][] list = null;
			try {
				list = WebDBUtil.execQryArray(sql, "");
				session.removeAttribute(WebKeys.ATTR_UploadFileList);
				session.setAttribute(WebKeys.ATTR_UploadFileList, list);
			} catch (AppException ex) {
				ex.printStackTrace();
			}

			setNextScreen(request, "UploadMonitor.screen");
		}

		// 重传文件或重取回执，操作页面
		else if ("1".equals(submitType)) {
			// 操作类型，重传或下载,C:R
			String oper = request.getParameter("oper");
			if (oper == null || "".equals(oper)) {
				oper = "C"; // 默认为重传
			}
			// 操作标志，-1：重传;0：下载
			String chk_status = "-1";
			if ("R".equals(oper)) {
				chk_status = "0";
			}

			// 操作日期，上传或下载
			String date = request.getParameter("date");
			if (date == null || "".equals(date)) { // 缺省日期
				date = UnicomUploadUtil.getDefaultDate(tag);
			}

			String report_code = request.getParameter("report_code");
			if (report_code == null || "".equals(report_code)) {
				report_code = "XXXXXXA"; // 文件代码
			}
			String report_name = CommTool
					.getParameterGB(request, "report_name");
			if (report_name == null || "".equals(report_name)) {
				report_name = "未知"; // 报表中文名
			}
			String file_type = request.getParameter("file_type");
			if (file_type == null || "".equals(file_type)) {
				file_type = "A"; // 文件代码
			}
			String file_name = request.getParameter("file_name");
			if (file_name == null || "".equals(file_name)) {
				file_name = oper + report_code
						+ UnicomUploadUtil.getFileNameDateStr(tag, date)
						+ "001.011"; // 文件代码
			}
			String sql = "insert into RECALLMONTH_CHANGERUN(Garther_id,Rpt_Type,File_Type,Rpt_filename,Report_name,"
					+ "Report_Code,Chk_Status,START_TIME,USER_IP)"
					+ " values("
					+ date
					+ ",'"
					+ tag
					+ "','"
					+ file_type
					+ "','"
					+ file_name
					+ "','"
					+ report_name
					+ "','"
					+ report_code
					+ "',"
					+ chk_status
					+ ",SYSDATE,'"
					+ request.getRemoteAddr()
					+ "')";
			System.out.println("InsertHandUploadList==============" + sql);
			try {
				if (WebDBUtil.execUpdate(sql) != 1) {
					throw new HTMLActionException(session,
							HTMLActionException.ERROR_PAGE, "插入操作记录失败！");
				} else {
					session.setAttribute(WebKeys.ATTR_ANYFLAG, "1");
					throw new HTMLActionException(request.getSession(),
							HTMLActionException.SUCCESS_PAGE,
							"操作指令已提交，请您稍后再访问监控页面，查看操作执行情况。");
				}
			} catch (AppException ex) {
				ex.printStackTrace();
			}
		}

		// doTrans end
	}

	// class end
}
