package com.ailk.bi.olap.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ailk.bi.domain.olap.UiRptInfoUserOlap;
import com.ailk.bi.domain.olap.UiRptMetaUserOlapDim;
import com.ailk.bi.olap.service.IUiRptInfoUserOlapSrv;
import com.ailk.bi.olap.service.dao.IUiRptInfoUserOlapDAO;
import com.ailk.bi.olap.service.dao.IUiRptMetaUserOlapDimDAO;

@Service("userOlapSrv")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class UiRptInfoUserOlapSrvImpl implements IUiRptInfoUserOlapSrv {
	@Resource
	private IUiRptInfoUserOlapDAO uiRptInfoUserOlapDAO;
	@Resource
	private IUiRptMetaUserOlapDimDAO uiRptMetaUserOlapDimDAO;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(UiRptInfoUserOlap userOlap) {
		return uiRptInfoUserOlapDAO.save(userOlap);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean remove(UiRptInfoUserOlap userOlap) {

		return uiRptInfoUserOlapDAO.remove(userOlap);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean removeById(Integer id) {
		deleteReportDim(id + "");
		return uiRptInfoUserOlapDAO.removeById(id);
	}

	public List<UiRptInfoUserOlap> findAll() {
		return uiRptInfoUserOlapDAO.findAll();
	}

	public UiRptInfoUserOlap findById(Integer id) {
		return uiRptInfoUserOlapDAO.find(id);
	}

	public void flush() {
		uiRptInfoUserOlapDAO.flush();
	}

	public void saveReportDim(String cusRptId,
			List<UiRptMetaUserOlapDim> rptDims) {
		uiRptMetaUserOlapDimDAO.saveReportDim(cusRptId, rptDims);
	}

	public List<UiRptMetaUserOlapDim> getCustomDims(String cusRptId) {
		if (StringUtils.isBlank(cusRptId))
			return null;
		return uiRptMetaUserOlapDimDAO.getCustomDims(cusRptId);
	}

	public Map<String, UiRptMetaUserOlapDim> getCusRptDims(String cusRptId) {
		if (StringUtils.isBlank(cusRptId))
			return null;
		return uiRptMetaUserOlapDimDAO.getCusRptDims(cusRptId);
	}

	public void deleteReportDim(String cusRptId) {
		if (StringUtils.isBlank(cusRptId))
			return;
		uiRptMetaUserOlapDimDAO.deleteReportDim(cusRptId);
	}

}
