package com.ailk.bi.upload.facade;

import java.util.ArrayList;

import com.ailk.bi.base.facade.IFacade;
import com.ailk.bi.upload.dao.IUploadDao;
import com.ailk.bi.upload.domain.UiMsuUpReportConfigTable;
import com.ailk.bi.upload.domain.UiMsuUpReportMetaInfoTable;
import com.ailk.bi.upload.service.IUploadService;
import com.ailk.bi.upload.service.impl.UploadService;

public class UploadFacade implements IFacade {

	private IUploadService uploadService = null;

	public void setHocService(IUploadService uploadService) {
		this.uploadService = uploadService;
	}

	public UploadFacade(IUploadDao dao) {
		UploadService service = new UploadService();
		service.setDao(dao);
		this.uploadService = service;
	}

	public UiMsuUpReportConfigTable getReportInfo(String report_id) {
		return uploadService.getReportInfo(report_id);
	}

	public String getFirstRecord(String report_id) {
		return uploadService.getFirstRecord(report_id);
	}

	public String getMaxDayByReportId(String report_id) {

		return uploadService.getMaxDayByReportId(report_id);
	}

	public String getMaxDayByReportIdAndRecord(String report_id, String record) {
		//
		return uploadService.getMaxDayByReportIdAndRecord(report_id, record);
	}

	public String getMaxMonthByReportId(String report_id) {
		//
		return uploadService.getMaxMonthByReportId(report_id);
	}

	public String getMaxMonthByReportIdAndRecord(String report_id, String record) {
		//
		return uploadService.getMaxMonthByReportIdAndRecord(report_id, record);
	}

	public String getRecordSvcFlag(String report_id, String record_code) {
		//
		return uploadService.getRecordSvcFlag(report_id, record_code);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public UiMsuUpReportMetaInfoTable[] getReportMetaInfo(String report_id,
			String record_code) {
		UiMsuUpReportMetaInfoTable[] info = null;
		//
		ArrayList list = (ArrayList) uploadService.getReportMetaInfo(report_id,
				record_code);
		if (list != null && !list.isEmpty()) {
			info = (UiMsuUpReportMetaInfoTable[]) list
					.toArray(new UiMsuUpReportMetaInfoTable[list.size()]);
		}
		return info;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public UiMsuUpReportMetaInfoTable[] getReportMetaInfo(String report_id) {
		UiMsuUpReportMetaInfoTable[] info = null;
		//
		ArrayList list = (ArrayList) uploadService.getReportMetaInfo(report_id);
		if (list != null && !list.isEmpty()) {
			info = (UiMsuUpReportMetaInfoTable[]) list
					.toArray(new UiMsuUpReportMetaInfoTable[list.size()]);
		}
		return info;
	}

}
