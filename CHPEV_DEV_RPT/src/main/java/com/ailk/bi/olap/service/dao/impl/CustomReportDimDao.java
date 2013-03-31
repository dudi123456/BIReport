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
import com.ailk.bi.base.table.RptOlapUserDimTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.olap.service.dao.ICustomReportDimDao;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CustomReportDimDao implements ICustomReportDimDao {

	public void deleteReportDim(String cusRptId) throws ReportOlapException {
		if (null == cusRptId || "".equals(cusRptId))
			throw new ReportOlapException("删除 用户自定义报表的维度时输入的参数为空");
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = WebDBUtil.getConn();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			String delete = SQLGenator.genSQL("D5571", cusRptId);
			stmt.addBatch(delete);
			stmt.executeBatch();
			stmt.close();
			conn.commit();
			conn.setAutoCommit(true);
		} catch (AppException ae) {
			throw new ReportOlapException("删除自定义报表的维度时发生错误", ae);
		} catch (SQLException se) {
			throw new ReportOlapException("删除自定义报表的维度时发生错误", se);
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

	public void saveReportDim(String cusRptId, List rptDims)
			throws ReportOlapException {
		if (null == cusRptId || "".equals(cusRptId) || null == rptDims
				|| 0 >= rptDims.size())
			throw new ReportOlapException("保存用户自定义报表的维度时输入的参数为空");
		// 使用批量提交功能
		List<RptOlapUserDimTable> domains = new ArrayList<RptOlapUserDimTable>();
		RptOlapUserDimTable domain = null;
		int order = 1;
		Iterator iter = rptDims.iterator();
		while (iter.hasNext()) {
			domain = new RptOlapUserDimTable();
			domain.custom_rptid = cusRptId;
			domain.dim_id = (String) iter.next();
			domain.display_order = order + "";
			domains.add(domain);
			order++;
		}
		try {
			WebDBUtil.insert(domains);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ReportOlapException(e);
		}
	}

	public List getCustomDims(String cusRptId) throws ReportOlapException {
		if (null == cusRptId || "".equals(cusRptId))
			throw new ReportOlapException("获取用户自定义报表的指标时输入的参数为空");
		List rptDims = null;
		try {
			String select = SQLGenator.getOrignalSQL("Q5575");
			Map<String, String> param = new HashMap<String, String>();
			param.put("CUSTOM_RPTID", cusRptId);
			rptDims = WebDBUtil.find(RptOlapUserDimTable.class, select, param);
		} catch (Exception ae) {
			throw new ReportOlapException("获取用户自定义报表的指标时发生错误", ae);
		}
		return rptDims;
	}

	@SuppressWarnings("unused")
	private RptOlapUserDimTable genCusRptDim(String[] svces) {
		RptOlapUserDimTable rptDim = null;
		if (null != svces) {
			rptDim = new RptOlapUserDimTable();
			rptDim.custom_rptid = svces[0];
			rptDim.dim_id = svces[1];
			rptDim.display_order = svces[2];
		}
		return rptDim;
	}

	public Map getCusRptDims(String cusRptId) throws ReportOlapException {
		List results = getCustomDims(cusRptId);
		Map dims = new HashMap();
		RptOlapUserDimTable rptDim = null;
		Iterator iter = results.iterator();
		while (iter.hasNext()) {
			rptDim = (RptOlapUserDimTable) iter.next();
			dims.put(rptDim.dim_id, rptDim);
		}
		return dims;
	}
}
