package com.ailk.bi.olap.service.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.RptOlapUserMsuTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.olap.service.dao.ICustomReportMsuDao;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CustomReportMsuDao implements ICustomReportMsuDao {

	public void deleteReportMsu(String cusRptId) throws ReportOlapException {
		if (null == cusRptId || "".equals(cusRptId))
			throw new ReportOlapException("删除 用户自定义报表的指标时输入的参数为空");
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = WebDBUtil.getConn();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			String delete = SQLGenator.genSQL("D5581", cusRptId);
			stmt.addBatch(delete);
			stmt.executeBatch();
			stmt.close();
			conn.commit();
			conn.setAutoCommit(true);
		} catch (AppException ae) {
			throw new ReportOlapException("插入自定义报表的维度时发生错误", ae);
		} catch (SQLException se) {
			throw new ReportOlapException("插入自定义报表的维度时发生错误", se);
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
					throw new ReportOlapException("关闭数据库连接时发生错误", se);
				}
			}
		}
	}

	public void saveReportMsu(String cusRptId, List msus)
			throws ReportOlapException {
		if (null == cusRptId || "".equals(cusRptId) || null == msus
				|| 0 >= msus.size())
			throw new ReportOlapException("保存用户自定义报表的维度时输入的参数为空");
		// 使用批量提交功能
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = WebDBUtil.getConn();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			int order = 1;
			Iterator iter = msus.iterator();
			while (iter.hasNext()) {
				String userMsu = (String) iter.next();
				String select = SQLGenator.genSQL("I5580", cusRptId, "'"
						+ userMsu + "'", order + "");
				stmt.addBatch(select);
				order++;
			}
			stmt.executeBatch();
			stmt.close();
			conn.commit();
			conn.setAutoCommit(true);
		} catch (AppException ae) {
			throw new ReportOlapException("插入自定义报表的维度时发生错误", ae);
		} catch (SQLException se) {
			throw new ReportOlapException("插入自定义报表的维度时发生错误", se);
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
					throw new ReportOlapException("关闭数据库连接时发生错误", se);
				}
			}
		}

	}

	public List getCustomMsus(String cusRptId) throws ReportOlapException {
		if (null == cusRptId || "".equals(cusRptId))
			throw new ReportOlapException("获取用户自定义报表的指标时输入的参数为空");
		List rptMsus = null;
		try {
			String select = SQLGenator.genSQL("Q5585", cusRptId);
			String[][] svces = WebDBUtil.execQryArray(select, select);
			if (null != svces) {
				rptMsus = new ArrayList();
				for (int i = 0; i < svces.length; i++) {
					RptOlapUserMsuTable rptMsu = genCusRptMsu(svces[i]);
					rptMsus.add(rptMsu);
				}
			}
		} catch (AppException ae) {
			throw new ReportOlapException("获取用户自定义报表的指标时发生错误", ae);
		}
		return rptMsus;
	}

	private RptOlapUserMsuTable genCusRptMsu(String[] svces) {
		RptOlapUserMsuTable rptMsu = null;
		if (null != svces) {
			rptMsu = new RptOlapUserMsuTable();
			rptMsu.custom_rptid = svces[0];
			rptMsu.msu_id = svces[1];
			rptMsu.display_order = svces[2];
		}
		return rptMsu;
	}

	public Map getCusRptMsus(String cusRptId) throws ReportOlapException {
		List result = getCustomMsus(cusRptId);
		Map msus = new HashMap();
		RptOlapUserMsuTable rptMsu = null;
		Iterator iter = result.iterator();
		while (iter.hasNext()) {
			rptMsu = (RptOlapUserMsuTable) iter.next();
			msus.put(rptMsu.msu_id, rptMsu);
		}
		return msus;
	}
}
