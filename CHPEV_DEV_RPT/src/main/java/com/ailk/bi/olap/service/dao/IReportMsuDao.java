package com.ailk.bi.olap.service.dao;

import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.RptOlapMsuTable;

@SuppressWarnings({ "rawtypes" })
public interface IReportMsuDao {
	/**
	 * 获取报表的指标列表
	 * 
	 * @param reportId
	 *            报表标识
	 * @return 报表的指标列表
	 * @throws ReportOlapException
	 */
	public abstract List getReportMsu(String reportId)
			throws ReportOlapException;

	/**
	 * 获取报表指标的子指标列表
	 * 
	 * @param rptMsu
	 *            报表指标
	 * @return 子指标列表
	 * @throws ReportOlapException
	 */
	public abstract List getMsuChildren(RptOlapMsuTable rptMsu)
			throws ReportOlapException;
}
