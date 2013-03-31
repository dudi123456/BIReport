package com.ailk.bi.olap.service.dao;

import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.RptUserOlapTable;

public interface ICustomReportDao {
	public abstract String saveReport(String userId, String reportName,
			String reportId, String displayMode) throws ReportOlapException;

	public abstract String saveReport(RptUserOlapTable userReport)
			throws ReportOlapException;

	public abstract RptUserOlapTable getCusReport(String cusRptId)
			throws ReportOlapException;

	public abstract RptUserOlapTable getUserCusReport(String userId,
			String reportId) throws ReportOlapException;

	@SuppressWarnings("rawtypes")
	public abstract List getUserCusReports(String userId)
			throws ReportOlapException;

	public abstract void deleteReport(String cusRptId)
			throws ReportOlapException;
}
