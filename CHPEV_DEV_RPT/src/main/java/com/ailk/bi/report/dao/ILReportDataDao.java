package com.ailk.bi.report.dao;

import java.util.List;

import com.ailk.bi.base.table.PubInfoConditionTable;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "rawtypes" })
public interface ILReportDataDao {

	/**
	 * 获取报表数据
	 *
	 * @param rptTable
	 * @param rptCols
	 * @param qryStruct
	 * @return
	 * @throws AppException
	 */
	public String[][] getReportData(Object rptTable, List rptCols,
			Object qryStruct, PubInfoConditionTable[] cdnTables)
			throws AppException;

	/**
	 * 获取报表数据
	 *
	 * @param rptTable
	 * @param rptCols
	 * @param qryStruct
	 * @param db
	 * @return
	 * @throws AppException
	 */
	public String[][] getReportData(Object rptTable, List rptCols,
			Object qryStruct, WebDBUtil db, PubInfoConditionTable[] cdnTables)
			throws AppException;
}
