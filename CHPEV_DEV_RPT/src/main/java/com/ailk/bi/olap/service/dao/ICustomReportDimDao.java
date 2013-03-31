package com.ailk.bi.olap.service.dao;

import java.util.List;
import java.util.Map;

import com.ailk.bi.base.exception.ReportOlapException;

@SuppressWarnings({ "rawtypes" })
public interface ICustomReportDimDao {
	public abstract void saveReportDim(String cusRptId, List rptDims)
			throws ReportOlapException;

	public abstract List getCustomDims(String cusRptId)
			throws ReportOlapException;

	public abstract Map getCusRptDims(String cusRptId)
			throws ReportOlapException;

	public void deleteReportDim(String cusRptId) throws ReportOlapException;
}
