package com.ailk.bi.olap.service.dao;

import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.PubInfoResourceTable;

@SuppressWarnings({ "rawtypes" })
public interface IReportDao {
	/**
	 * 获取报表对象
	 * 
	 * @param reportId
	 *            报表标识
	 * @return 报表对象
	 * @throws ReportOlapException
	 */
	public abstract PubInfoResourceTable getReport(String reportId)
			throws ReportOlapException;

	/**
	 * 获取指定报表标识的所有子节点
	 * 
	 * @param reportId
	 *            报表标识
	 * @return 子节点列表
	 * @throws ReportOlapException
	 */
	public abstract List getChildReport(String reportId)
			throws ReportOlapException;
}
