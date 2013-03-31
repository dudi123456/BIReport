package com.ailk.bi.olap.service.dao;

import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;

@SuppressWarnings({ "rawtypes" })
public interface IReportDimDao {
	/**
	 * 生成报表的维度列表
	 * 
	 * @param reportId
	 *            报表标识
	 * @return 报表的维度列表
	 * @throws ReportOlapException
	 */
	public abstract List getReportDim(String reportId)
			throws ReportOlapException;
}
