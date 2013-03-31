package com.ailk.bi.olap.service.dao.impl;

import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.RptOlapMsuTable;
import com.ailk.bi.olap.service.dao.IReportMsuDao;
import com.ailk.bi.olap.util.RptOlapMsuUtil;

@SuppressWarnings({ "rawtypes" })
public class ReportMsuDao implements IReportMsuDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.olap.service.dao.IReportMsuDao#getReportMsu(java.lang.String)
	 */
	public List getReportMsu(String reportId) throws ReportOlapException {
		List msus = null;
		if (null == reportId || "".equals(reportId))
			throw new IllegalArgumentException("获取报表的指标定义时报表标识为空");
		msus = RptOlapMsuUtil.getReportMsu(reportId);
		return msus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.olap.service.dao.IReportMsuDao#getMsuChildren(com.asiabi.base
	 * .table.RptOlapMsuTable)
	 */
	public List getMsuChildren(RptOlapMsuTable rptMsu)
			throws ReportOlapException {
		List children = null;
		if (null == rptMsu)
			throw new IllegalArgumentException("获取指标孩子时输入参数为空");
		children = RptOlapMsuUtil.getMsuChildren(rptMsu.msuInfo.msu_id);
		return children;
	}
}
