package com.ailk.bi.olap.service.dao;

import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;

@SuppressWarnings({ "rawtypes" })
public interface IReportSortDao {
	/**
	 * 按指定的列或者比率对内容进行排序
	 * 
	 * @param tabCols
	 *            列域对象列表
	 * @param olapFun
	 *            当前功能对象
	 * @param startPeriod
	 *            统计周期
	 * @param svces
	 *            表格内容
	 * @return 排序后的内容
	 * @throws ReportOlapException
	 */
	public abstract String[][] sortTableContent(List tabCols,
			RptOlapFuncStruct olapFun, String statPeriod, String[][] svces)
			throws ReportOlapException;

}
