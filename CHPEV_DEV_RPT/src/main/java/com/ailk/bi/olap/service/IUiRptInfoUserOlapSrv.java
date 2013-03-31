package com.ailk.bi.olap.service;

import java.util.List;
import java.util.Map;

import com.ailk.bi.domain.olap.UiRptInfoUserOlap;
import com.ailk.bi.domain.olap.UiRptMetaUserOlapDim;

public interface IUiRptInfoUserOlapSrv {
	public boolean save(UiRptInfoUserOlap userOlap);

	public boolean remove(UiRptInfoUserOlap userOlap);

	public boolean removeById(Integer id);

	public List<UiRptInfoUserOlap> findAll();

	public UiRptInfoUserOlap findById(Integer id);

	public void saveReportDim(String cusRptId,
			List<UiRptMetaUserOlapDim> rptDims);

	public List<UiRptMetaUserOlapDim> getCustomDims(String cusRptId);

	public Map<String, UiRptMetaUserOlapDim> getCusRptDims(String cusRptId);

	public void deleteReportDim(String cusRptId);

	public void flush();
}
