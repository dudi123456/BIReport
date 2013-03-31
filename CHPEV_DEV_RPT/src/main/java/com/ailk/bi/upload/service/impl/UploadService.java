package com.ailk.bi.upload.service.impl;

import java.util.List;

import com.ailk.bi.upload.dao.IUploadDao;
import com.ailk.bi.upload.domain.UiMsuUpReportConfigTable;
import com.ailk.bi.upload.service.IUploadService;

@SuppressWarnings({ "rawtypes" })
public class UploadService implements IUploadService {

	private IUploadDao dao = null;

	public IUploadDao getDao() {
		return dao;
	}

	public void setDao(IUploadDao dao) {
		this.dao = dao;
	}

	public UiMsuUpReportConfigTable getReportInfo(String report_id) {
		return dao.getReportInfo(report_id);
	}

	public String getFirstRecord(String report_id) {
		return dao.getFirstRecord(report_id);
	}

	public String getMaxDayByReportId(String report_id) {
		return dao.getMaxDayByReportId(report_id);
	}

	public String getMaxDayByReportIdAndRecord(String report_id, String record) {
		return dao.getMaxDayByReportIdAndRecord(report_id, record);
	}

	public String getMaxMonthByReportId(String report_id) {
		return dao.getMaxMonthByReportId(report_id);
	}

	public String getMaxMonthByReportIdAndRecord(String report_id, String record) {
		return dao.getMaxMonthByReportIdAndRecord(report_id, record);
	}

	public String getRecordSvcFlag(String report_id, String record_code) {
		return dao.getRecordSvcFlag(report_id, record_code);
	}

	public List getReportMetaInfo(String report_id, String record_code) {
		return dao.getReportMetaInfo(report_id, record_code);
	}

	public List getReportMetaInfo(String report_id) {
		return dao.getReportMetaInfo(report_id);
	}

}
