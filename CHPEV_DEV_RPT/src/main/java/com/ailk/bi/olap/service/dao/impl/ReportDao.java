package com.ailk.bi.olap.service.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.olap.service.dao.IReportDao;
import com.ailk.bi.olap.util.RptOlapRptUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReportDao implements IReportDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asiabi.olap.service.dao.IReportDao#getReport(java.lang.String)
	 */
	public PubInfoResourceTable getReport(String reportId)
			throws ReportOlapException {
		PubInfoResourceTable report = null;
		if (null == reportId || "".equals(reportId))
			throw new IllegalArgumentException("报表标识为空");
		try {
			String sql = SQLGenator.genSQL("Q5500", "'" + reportId + "'");
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (null != svces) {
				report = RptOlapRptUtil.genReportFromArray(svces[0]);
			}
		} catch (AppException ae) {
			throw new ReportOlapException("获取分析型报表基本信息出错", ae);
		}
		return report;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.olap.service.dao.IReportDao#getChildReport(java.lang.String)
	 */
	public List getChildReport(String reportId) throws ReportOlapException {
		List children = null;
		if (null == reportId || "".equals(reportId))
			throw new IllegalArgumentException("报表标识为空");
		try {
			String sql = SQLGenator.genSQL("Q5510", reportId);
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (null != svces) {
				children = new ArrayList();
				for (int i = 0; i < svces.length; i++) {
					PubInfoResourceTable report = RptOlapRptUtil
							.genReportFromArray(svces[0]);
					children.add(report);
				}
			}
		} catch (AppException ae) {
			throw new ReportOlapException("获取分析型报表孩子节点失败", ae);
		}
		return children;
	}

}
