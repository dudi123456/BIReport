package com.ailk.bi.olap.service.dao;

import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;

@SuppressWarnings({ "rawtypes" })
public interface IChartTableHeadDao {
	public abstract List genChartHTMLHead(List chartStructs,
			RptOlapFuncStruct olapFun, String trStyle, String tdStyle)
			throws ReportOlapException;
}
