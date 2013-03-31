package com.ailk.bi.olap.service.dao;

import java.util.List;
import java.util.Map;

import com.ailk.bi.domain.olap.UiRptMetaUserOlapDim;

public interface IUiRptMetaUserOlapDimDAO {
	public void saveReportDim(String cusRptId,
			List<UiRptMetaUserOlapDim> rptDims);

	public List<UiRptMetaUserOlapDim> getCustomDims(String cusRptId);

	public Map<String, UiRptMetaUserOlapDim> getCusRptDims(String cusRptId);

	public void deleteReportDim(String cusRptId);
}
