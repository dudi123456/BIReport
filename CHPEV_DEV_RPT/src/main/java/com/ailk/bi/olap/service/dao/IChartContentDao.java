package com.ailk.bi.olap.service.dao;

import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.olap.domain.RptOlapDateStruct;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;

@SuppressWarnings({ "rawtypes" })
public interface IChartContentDao {
	/**
	 * 获取图形分析所需要的内容
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
}
