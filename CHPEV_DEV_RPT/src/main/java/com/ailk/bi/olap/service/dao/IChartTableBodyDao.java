package com.ailk.bi.olap.service.dao;

import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;

@SuppressWarnings({ "rawtypes" })
public interface IChartTableBodyDao {
	public abstract List getTableBody(String[][] svces, List chartStructs,
			RptOlapFuncStruct olapFun, String statPeriod, String trStyle,
			String tdStyle) throws ReportOlapException;
}
