package com.ailk.bi.olap.service.dao;

import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;

@SuppressWarnings({ "rawtypes" })
public interface ITableHeadDao {

	/**
	 * 生成表格的表头HTML
	 * 
	 * @param tableCols
	 *            列域对象列表
	 * @param olapFun
	 *            当前功能对象
	 * @param statPeriod
	 *            统计周期
	 * @param fixedHead
	 *            是否固定表头
	 * @param trStyle
	 *            行样式
	 * @param tdStyle
	 *            列样式
	 * @return 表头HTML
	 * @throws ReportOlapException
	 */
	public abstract List genHTMLTableHead(PubInfoResourceTable report,
			List tableCols, RptOlapFuncStruct olapFun, String statPeriod,
			boolean fixedHead, String trStyle, String tdStyle)
			throws ReportOlapException;

	/**
	 * 生成折叠展开的表头
	 * 
	 * @param tableCols
	 *            列域对象列表
	 * @param olapFun
	 *            当前功能对象
	 * @param statPeriod
	 *            统计周期
	 * @param fixedHead
	 *            是否固定表头
	 * @param trStyle
	 *            行样式
	 * @param tdStyle
	 *            列样式
	 * @param level
	 *            当前层次
	 * @param singleDim
	 *            是否单个维度
	 * @return 表头的HTML
	 * @throws ReportOlapException
	 */
	public abstract List genExpandHTMLTableHead(List tableCols,
			RptOlapFuncStruct olapFun, String statPeriod, boolean fixedHead,
			String trStyle, String tdStyle, String level, boolean singleDim)
			throws ReportOlapException;

	/**
	 * 获取导出的表头
	 * 
	 * @return
	 */
	public abstract List getExportHead();
}
