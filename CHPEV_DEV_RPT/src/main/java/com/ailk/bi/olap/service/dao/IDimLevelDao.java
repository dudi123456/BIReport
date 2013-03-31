package com.ailk.bi.olap.service.dao;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.DimLevelTable;
import com.ailk.bi.olap.domain.RptOlapTableColumnStruct;

public interface IDimLevelDao {
	/**
	 * 获取当前维度对象的下一层次
	 * 
	 * @param col
	 *            列域对象
	 * @return 设置后的列域对象
	 * @throws ReportOlapException
	 */
	public abstract RptOlapTableColumnStruct getDimNextLevel(
			RptOlapTableColumnStruct col) throws ReportOlapException;

	/**
	 * 获取当前维度对象的上一次层次
	 * 
	 * @param col
	 *            列域对象
	 * @return 设置后的列域对象
	 * @throws ReportOlapException
	 */
	public abstract RptOlapTableColumnStruct getDimPreLevel(
			RptOlapTableColumnStruct col) throws ReportOlapException;

	/**
	 * 获取维度的当前层次对象，以便使用其字段定义
	 * 
	 * @param col
	 *            列域对象
	 * @return 维度层次对象
	 * @throws ReportOlapException
	 */
	public abstract DimLevelTable getLevelInfo(RptOlapTableColumnStruct col)
			throws ReportOlapException;
}
