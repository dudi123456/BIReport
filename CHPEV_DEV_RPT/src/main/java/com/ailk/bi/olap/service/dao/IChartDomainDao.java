package com.ailk.bi.olap.service.dao;

import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;

@SuppressWarnings({ "rawtypes" })
public interface IChartDomainDao {
	/**
	 * 获取分析型报表的默认图形显示
	 * 
	 * @param reportId
	 *            报表标识
	 * @return 以各个关键字为键值的LIST列表
	 * @throws ReportOlapException
	 */
	public abstract List genDefaultSetting(String reportId, List rptDims,
			List rptMsus) throws ReportOlapException;
}
