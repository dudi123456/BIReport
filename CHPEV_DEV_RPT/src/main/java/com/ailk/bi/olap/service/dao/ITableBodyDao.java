package com.ailk.bi.olap.service.dao;

import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;

@SuppressWarnings({ "rawtypes" })
public interface ITableBodyDao {
	/**
	 * 生成表格的表体HTML
	 * 
	 * @param svces
	 *            表体内容
	 * @param tableCols
	 *            列域对象
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
	 * @return 表体的HTML字符串列表
	 * @throws ReportOlapException
	 */
	public abstract List getTableBody(String[][] svces,
			PubInfoResourceTable report, List tableCols,
			RptOlapFuncStruct olapFun, boolean fixedHead, String trStyle,
			String tdStyle) throws ReportOlapException;

	/**
	 * 生成扩展的表体的HTML
	 * 
	 * @param svces
	 *            表体内容
	 * @param tableCols
	 *            列域对象列表
	 * @param olapFun
	 *            当前功能对象
	 * @param statPeriod
	 *            统计周期
	 * @param fixedHead
	 *            是否固定表体
	 * @param trStyle
	 *            行样式
	 * @param tdStyle
	 *            列样式
	 * @param level
	 *            当前层次
	 * @param singleDim
	 *            是否是单维度
	 * @return 表体的HTML
	 * @throws ReportOlapException
	 */
	public abstract List getExpandTableBody(List preBody, String[][] svces,
			PubInfoResourceTable report, List tableCols,
			RptOlapFuncStruct olapFun, boolean fixedHead, String trStyle,
			String tdStyle, String level, boolean singleDim)
			throws ReportOlapException;

	/**
	 * 将行对象列表转换成真正的字符串
	 * 
	 * @param body
	 *            行对象列表
	 * @return
	 * @throws ReportOlapException
	 */
	public abstract List convertRowStructToHtml(List body,
			RptOlapFuncStruct olapFun) throws ReportOlapException;

	/**
	 * 获得导出的表体
	 * 
	 * @return
	 */
	public abstract List getExportBody();

	/**
	 * 获取当前存储导出内容的行结构列表
	 * 
	 * @return
	 */
	public abstract List getPreExpBody();

	/**
	 * 设置当前行结构的列表
	 * 
	 * @param preExpBody
	 */
	public abstract void setPreExpBody(List preExpBody);
}
