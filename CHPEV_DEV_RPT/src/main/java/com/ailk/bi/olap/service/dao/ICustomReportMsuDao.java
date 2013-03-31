package com.ailk.bi.olap.service.dao;

import java.util.List;
import java.util.Map;

import com.ailk.bi.base.exception.ReportOlapException;

@SuppressWarnings({ "rawtypes" })
public interface ICustomReportMsuDao {
	public abstract void saveReportMsu(String cusRptId, List msus)
			throws ReportOlapException;

	public abstract List getCustomMsus(String cusRptId)
			throws ReportOlapException;

	public abstract Map getCusRptMsus(String cusRptId)
			throws ReportOlapException;

	public void deleteReportMsu(String cusRptId) throws ReportOlapException;
}
