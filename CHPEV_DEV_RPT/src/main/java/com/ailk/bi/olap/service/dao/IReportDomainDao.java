package com.ailk.bi.olap.service.dao;

import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;

@SuppressWarnings({ "rawtypes" })
public interface IReportDomainDao {
	/**
	 * 组装表格的列域对象
	 * 
	 * @param rptDims
	 *            报表维度对象列表
	 * @param rptMsus
	 *            报表指标对象列表
	 * @return 列对象列表
	 * @throws ReportOlapException
	 */
	public abstract List genTableColStruct(List rptDims, List rptMsus)
			throws ReportOlapException;
}
