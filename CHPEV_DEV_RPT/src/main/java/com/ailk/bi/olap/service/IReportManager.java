package com.ailk.bi.olap.service;

import java.util.Map;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.base.table.RptUserOlapTable;

@SuppressWarnings({ "rawtypes" })
public interface IReportManager {
	/**
	 * 获取指定标识的表报对象
	 * 
	 * @param reportId
	 *            表报标识
	 * @return 报表对象
	 * @throws ReportOlapException
	 */
	public abstract PubInfoResourceTable getReport(String reportId)
			throws ReportOlapException;

	public abstract RptUserOlapTable getCusReport(String cusRptId)
			throws ReportOlapException;

	public abstract Map getCusRptDims(String cusRptId)
			throws ReportOlapException;

	public abstract Map getCusRptMsus(String cusRptId)
			throws ReportOlapException;
}
