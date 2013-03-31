package com.ailk.bi.olap.service;

import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.olap.domain.RptOlapDateStruct;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;

@SuppressWarnings({ "rawtypes" })
public interface IChartManager {
	/**
	 * 获取报表的维度对象列表
	 * 
	 * @param reportId
	 *            报表标识
	 * @return 维度对象列表
	 * @throws ReportOlapException
	 */
	public abstract List getRptDims(String reportId) throws ReportOlapException;

	/**
	 * 获取报表的指标对象列表
	 * 
	 * @param reportId
	 *            报表标识
	 * @return 指标对象列表
	 * @throws ReportOlapException
	 */
	public abstract List getRptMsus(String reportId) throws ReportOlapException;

	/**
	 * 
	 * @param reportId
	 * @param rptDims
	 * @param rptMsus
	 * @return
	 * @throws ReportOlapException
	 */
	public abstract List genChartStructs(String reportId)
			throws ReportOlapException;

	/**
	 * 获取图形的数据内容
	 * 
	 * @param structs
	 * @param report
	 * @param olapFun
	 * @param ds
	 * @return
	 * @throws ReportOlapException
	 */
	public abstract String[][] getChartContent(List structs,
			PubInfoResourceTable report, RptOlapFuncStruct olapFun,
			UserCtlRegionStruct userCtl, String svckndRight,
			RptOlapDateStruct ds) throws ReportOlapException;

	/**
	 * @param svces
	 * @param chartStructs
	 * @param olapFun
	 * @param statPeriod
	 * @return
	 * @throws ReportOlapException
	 */
	public abstract String[] getChartTableHTML(String[][] svces,
			List chartStructs, RptOlapFuncStruct olapFun, String statPeriod)
			throws ReportOlapException;
}
