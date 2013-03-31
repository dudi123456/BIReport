package com.ailk.bi.olap.service.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ailk.bi.domain.olap.UiRptMetaUserOlapDim;
import com.ailk.bi.mapper.olap.IUiRptUserOlapDimMapper;
import com.ailk.bi.olap.service.dao.IUiRptMetaUserOlapDimDAO;

@Repository
public class UiRptMetaUserOlapDimDAOImpl implements IUiRptMetaUserOlapDimDAO {

	private IUiRptUserOlapDimMapper userOlapDimMapper;

	public IUiRptUserOlapDimMapper getUserOlapDimMapper() {
		return userOlapDimMapper;
	}

	@Autowired
	public void setUserOlapDimMapper(IUiRptUserOlapDimMapper userOlapDimMapper) {
		this.userOlapDimMapper = userOlapDimMapper;
	}

	public void saveReportDim(String cusRptId,
			List<UiRptMetaUserOlapDim> rptDims) {
		for (UiRptMetaUserOlapDim userOlapDim : rptDims) {
			userOlapDimMapper.insert(userOlapDim.getCustomRptId(),
					userOlapDim.getDimId(), userOlapDim.getDisplayOrder());
		}
	}

	public List<UiRptMetaUserOlapDim> getCustomDims(String cusRptId) {
		return userOlapDimMapper
				.getAllRptUserDims(Integer.valueOf(cusRptId));
	}

	public Map<String, UiRptMetaUserOlapDim> getCusRptDims(String cusRptId) {
		List<UiRptMetaUserOlapDim> userOlapDims = userOlapDimMapper
				.getAllRptUserDims(Integer.valueOf(cusRptId));
		if (null == userOlapDims || 0 == userOlapDims.size())
			return null;
		Map<String, UiRptMetaUserOlapDim> rptUserOlapDims = new HashMap<String, UiRptMetaUserOlapDim>(
				userOlapDims.size());
		for (UiRptMetaUserOlapDim userOlapDim : userOlapDims) {
			rptUserOlapDims.put(userOlapDim.getDimId() + "", userOlapDim);
		}
		return rptUserOlapDims;
	}

	public void deleteReportDim(String cusRptId) {
		userOlapDimMapper.deleteAllRptUserDims(Integer.valueOf(cusRptId));
	}

}
