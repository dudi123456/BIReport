package com.ailk.bi.olap.service.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.base.table.RptUserOlapTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.olap.service.dao.ICustomReportDao;
import com.ailk.bi.olap.util.RptOlapConsts;
import com.ailk.bi.olap.util.RptOlapRptUtil;

public class CustomReportDao implements ICustomReportDao {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.olap.service.dao.ICustomReportDao#saveReport(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	public String saveReport(String userId, String reportName, String reportId,
			String displayMode) throws ReportOlapException {
		if (null == userId || "".equals(userId) || null == displayMode
				|| "".equals(displayMode) || null == reportId
				|| "".equals(reportId))
			throw new ReportOlapException("保存用户自定义的报表时输入的参数为空");
		String cusRptId = null;
		RptUserOlapTable report = new RptUserOlapTable();
		report.user_id = userId;
		report.custom_rptname = reportName;
		report.report_id = reportId;
		report.display_mode = displayMode;
		report.is_valid = RptOlapConsts.YES;
		cusRptId = save(report);
		return cusRptId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.olap.service.dao.ICustomReportDao#saveReport(com.asiabi.base
	 * .table.RptUserOlapTable)
	 */
	public String saveReport(RptUserOlapTable userReport)
			throws ReportOlapException {
		if (null == userReport)
			throw new ReportOlapException("保存用户自定义的报表时输入的参数为空");
		String cusRptId = null;
		cusRptId = save(userReport);
		return cusRptId;
	}

	/**
	 * 保存用户自定义的报表 其实这里应该使用transation的，要不不能保证
	 *
	 * @param userReport
	 * @return
	 * @throws ReportOlapException
	 */
	private String save(RptUserOlapTable userReport) throws ReportOlapException {
		if (null == userReport)
			throw new ReportOlapException("保存用户自定义报表时输入的参数为空");
		String cusRptId = null;
		Connection conn = null;
		Statement stmt = null;
		try {
			cusRptId = finds(userReport.user_id, userReport.report_id);
			if (!StringUtils.isBlank(cusRptId)) {
				List<RptUserOlapTable> list = new ArrayList<RptUserOlapTable>();
				list.add(userReport);
				WebDBUtil.update(list, false);
			} else {
				conn = WebDBUtil.getConn();
				stmt = conn.createStatement();
				// 下面查询出来自增的值，好给后面的使用，这里只有自己来控制了
				String select = SQLGenator.genSQL("Q5560");
				ResultSet rs = stmt.executeQuery(select);
				conn.commit();
				// 提交后
				if (null != rs) {
					if (rs.next()) {
						cusRptId = rs.getString(1);
					}
					rs.close();
				}
				stmt.close();
				conn.setAutoCommit(true);
				userReport.custom_rptid = cusRptId;
				WebDBUtil.insert(userReport);
			}
		} catch (Exception se) {
			throw new ReportOlapException("查询时发生数据库错误", se);
		} finally {
			if (null != stmt) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (null != conn) {
				try {
					conn.close();
				} catch (SQLException se) {
					throw new ReportOlapException("关闭连接时发生错误", se);
				}
			}
		}
		return cusRptId;
	}

	public RptUserOlapTable getCusReport(String cusRptId)
			throws ReportOlapException {
		if (null == cusRptId || "".equals(cusRptId))
			throw new ReportOlapException("获取用户自定义的报表时输入的参数为空");
		RptUserOlapTable cusReport = null;
		try {
			String select = SQLGenator.genSQL("Q5555", cusRptId);
			String[][] svces = WebDBUtil.execQryArray(select, "");
			if (null != svces && 0 < svces.length) {
				cusReport = genFavCusReport(svces[0]);
			}
		} catch (AppException ae) {
			throw new ReportOlapException("查询用户自定义报表时发生数据库", ae);
		}
		return cusReport;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getUserCusReports(String userId) throws ReportOlapException {
		if (null == userId || "".equals(userId))
			throw new ReportOlapException("获取用户自定义的报表时输入的参数为空");
		List cusRpts = null;
		try {
			String select = SQLGenator.genSQL("Q5556", userId);
			String[][] svces = WebDBUtil.execQryArray(select, "");
			if (null != svces && 0 < svces.length) {
				cusRpts = new ArrayList();
				for (int i = 0; i < svces.length; i++) {
					RptUserOlapTable cusReport = genCusReport(svces[i]);
					cusRpts.add(cusReport);
				}
			}
		} catch (AppException ae) {
			throw new ReportOlapException("查询用户自定义报表时发生数据库", ae);
		}
		return cusRpts;
	}

	private RptUserOlapTable genFavCusReport(String[] svces) {
		RptUserOlapTable cusReport = null;
		if (null != svces) {
			cusReport = new RptUserOlapTable();
			PubInfoResourceTable report = RptOlapRptUtil
					.genReportFromArray(svces);
			cusReport.report = report;
			cusReport.custom_rptid = svces[34];
			cusReport.custom_rptname = svces[35];
			cusReport.user_id = svces[36];
			cusReport.report_id = svces[37];
			cusReport.display_mode = svces[38];
		}
		return cusReport;
	}

	public String finds(String userId, String reportId)
			throws ReportOlapException {
		Connection conn = null;
		String cusRptId = null;
		Statement stmt = null;
		try {
			String select = SQLGenator.genSQL("Q55500", "'" + userId + "'", "'"
					+ reportId + "'");
			conn = WebDBUtil.getConn();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(select);
			// 提交后
			if (null != rs) {
				if (rs.next()) {
					cusRptId = rs.getString("CUSTOM_RPTID");
				}
				rs.close();
			}
			stmt.close();
			conn.setAutoCommit(true);
		} catch (SQLException se) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				throw new ReportOlapException("删除用户自定义分析型报表发生异常回退时发生错误", e);
			}
			throw new ReportOlapException("查询时发生数据库错误", se);
		} catch (AppException ae) {
			throw new ReportOlapException("", ae);
		} finally {
			if (null != stmt) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (null != conn) {
				try {
					conn.close();
				} catch (SQLException se) {
					throw new ReportOlapException("关闭连接时发生错误", se);
				}
			}
		}
		return cusRptId;
	}

	public RptUserOlapTable getUserCusReport(String userId, String reportId)
			throws ReportOlapException {
		if (null == userId || "".equals(userId) || null == reportId
				|| "".equals(reportId))
			throw new ReportOlapException("获取用户自定义的报表时输入的参数为空");
		RptUserOlapTable cusReport = null;
		try {
			String select = SQLGenator.genSQL("Q5558", "'" + userId + "'", "'"
					+ reportId + "'");
			String[][] svces = WebDBUtil.execQryArray(select, "");
			if (null != svces && 0 < svces.length) {
				cusReport = genCusReport(svces[0]);
			}
		} catch (AppException ae) {
			throw new ReportOlapException("查询用户自定义报表时发生数据库", ae);
		}
		return cusReport;
	}

	private RptUserOlapTable genCusReport(String[] svces) {
		RptUserOlapTable cusReport = null;
		if (null != svces) {
			cusReport = new RptUserOlapTable();
			cusReport.custom_rptid = svces[0];
			cusReport.custom_rptname = svces[1];
			cusReport.user_id = svces[2];
			cusReport.report_id = svces[3];
			cusReport.display_mode = svces[4];
		}
		return cusReport;
	}

	public void deleteReport(String cusRptId) throws ReportOlapException {
		if (null == cusRptId || "".equals(cusRptId))
			throw new ReportOlapException("删除用户自定义的分析型报表时报表标识为空");
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = WebDBUtil.getConn();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			String SQL = SQLGenator.genSQL("I55502", cusRptId);
			stmt.addBatch(SQL);
			SQL = SQLGenator.genSQL("I55503", cusRptId);
			stmt.addBatch(SQL);
			SQL = SQLGenator.genSQL("I55504", cusRptId);
			stmt.addBatch(SQL);
			stmt.executeBatch();
			conn.commit();
			stmt.close();
			conn.setAutoCommit(true);

		} catch (SQLException se) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				throw new ReportOlapException("删除用户自定义分析型报表发生异常回退时发生错误", e);
			}
			throw new ReportOlapException("删除用户自定义分析型报表发生数据库错误", se);
		} catch (AppException ae) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				throw new ReportOlapException("删除用户自定义分析型报表发生异常回退时发生错误", e);
			}
			throw new ReportOlapException("", ae);
		} finally {
			if (null != stmt) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (null != conn) {
				try {
					conn.close();
				} catch (SQLException se) {
					throw new ReportOlapException("关闭连接时发生错误", se);
				}
			}
		}
	}
}
