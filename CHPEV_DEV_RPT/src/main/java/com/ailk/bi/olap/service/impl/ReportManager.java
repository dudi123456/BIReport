package com.ailk.bi.olap.service.impl;

import java.util.Map;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.base.table.RptUserOlapTable;
import com.ailk.bi.olap.service.IReportManager;
import com.ailk.bi.olap.service.dao.ICustomReportDao;
import com.ailk.bi.olap.service.dao.ICustomReportDimDao;
import com.ailk.bi.olap.service.dao.ICustomReportMsuDao;
import com.ailk.bi.olap.service.dao.IReportDao;
import com.ailk.bi.olap.service.dao.impl.CustomReportDao;
import com.ailk.bi.olap.service.dao.impl.CustomReportDimDao;
import com.ailk.bi.olap.service.dao.impl.CustomReportMsuDao;
import com.ailk.bi.olap.service.dao.impl.ReportDao;

@SuppressWarnings({ "rawtypes" })
public class ReportManager implements IReportManager {
	/**
	 * 新建一个报表DAO对象
	 */
	private IReportDao reportDao = new ReportDao();

	private ICustomReportDao cusReportDao = new CustomReportDao();

	private ICustomReportDimDao cusReportDimDao = new CustomReportDimDao();

	private ICustomReportMsuDao cusReportMsuDao = new CustomReportMsuDao();

	/*
	 * (non-Javadoc)
	 *
	 * @see com.asiabi.olap.service.IReportManager#getReport(java.lang.String)
	 */
	public PubInfoResourceTable getReport(String reportId)
			throws ReportOlapException {
		if (null == reportId || "".equals(reportId))
			throw new IllegalArgumentException("获取分析型报表基本信息时报表标识为空");
		PubInfoResourceTable report = null;
		report = reportDao.getReport(reportId);
		return report;
	}

	public RptUserOlapTable getCusReport(String cusRptId)
			throws ReportOlapException {
		if (null == cusRptId || "".equals(cusRptId))
			throw new ReportOlapException("获取自定义报表对象时输入的参数为空");
		return cusReportDao.getCusReport(cusRptId);
	}

	public Map getCusRptDims(String cusRptId) throws ReportOlapException {
		if (null == cusRptId || "".equals(cusRptId))
			throw new ReportOlapException("获取自定义报表的维度列表时输入的参数为空");
		return cusReportDimDao.getCusRptDims(cusRptId);
	}

	public Map getCusRptMsus(String cusRptId) throws ReportOlapException {
		if (null == cusRptId || "".equals(cusRptId))
			throw new ReportOlapException("获取自定义报表的指标列表时输入的参数为空");
		return cusReportMsuDao.getCusRptMsus(cusRptId);
	}

}
