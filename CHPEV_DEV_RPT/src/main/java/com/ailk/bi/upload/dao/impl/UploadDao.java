package com.ailk.bi.upload.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.adhoc.util.AdhocUtil;
import com.ailk.bi.upload.dao.IUploadDao;
import com.ailk.bi.upload.domain.UiMsuUpReportConfigTable;
import com.ailk.bi.upload.domain.UiMsuUpReportMetaInfoTable;
import com.ailk.bi.upload.util.UploadUtil;

public class UploadDao implements IUploadDao {

	/**
	 * 根据报表标识查询报表配置信息
	 */
	public UiMsuUpReportConfigTable getReportInfo(String report_id) {

		UiMsuUpReportConfigTable reportInfo = new UiMsuUpReportConfigTable();
		String[][] arr = UploadUtil.queryArrayFacade("QU001", report_id);
		if (arr != null && arr.length > 0) {

			reportInfo.setReport_id(arr[0][0].trim());
			reportInfo.setReport_code(arr[0][1].trim());
			reportInfo.setReport_name(arr[0][2].trim());
			reportInfo.setReport_sequence(arr[0][3].trim());
			reportInfo.setReport_group(arr[0][4].trim());
			reportInfo.setReport_type(arr[0][5].trim());
			reportInfo.setFtp_config_id(arr[0][6].trim());
			reportInfo.setIs_valid(arr[0][7].trim());
			reportInfo.setFilename_type(arr[0][8].trim());
		}
		return reportInfo;

	}

	/**
	 * 取得报表默认第一个记录类型
	 */
	public String getFirstRecord(String report_id) {

		String report_code = "02";
		String[][] arr = UploadUtil.queryArrayFacade("QU002", report_id);
		if (arr != null && arr.length > 0) {
			report_code = arr[0][0];
		}
		return report_code;

	}

	/**
	 * 根据报表标识获取最大日期
	 */
	public String getMaxDayByReportId(String report_id) {
		String day = "";
		String[][] arr = UploadUtil.queryArrayFacade("QU004", report_id);
		if (arr != null && arr.length > 0) {
			day = arr[0][0];
		}
		return day;
	}

	/**
	 * 根据报表标识和记录类型获取最大日期
	 */
	public String getMaxDayByReportIdAndRecord(String report_id, String record) {
		String day = "";
		String[][] arr = UploadUtil
				.queryArrayFacade("QU006", report_id, record);
		if (arr != null && arr.length > 0) {
			day = arr[0][0];
		}
		return day;
	}

	/**
	 * 根据报表标识获取最大月份
	 */
	public String getMaxMonthByReportId(String report_id) {
		String month = "";
		String[][] arr = UploadUtil.queryArrayFacade("QU003", report_id);
		if (arr != null && arr.length > 0) {
			month = arr[0][0];
		}
		return month;
	}

	/**
	 * 根据报表标识和记录类型获取最大月份
	 */
	public String getMaxMonthByReportIdAndRecord(String report_id, String record) {
		String month = "";
		String[][] arr = UploadUtil
				.queryArrayFacade("QU005", report_id, record);
		if (arr != null && arr.length > 0) {
			month = arr[0][0];
		}
		return month;
	}

	/**
	 * 业务类型控制标志（业务类型权限）
	 */
	public String getRecordSvcFlag(String report_id, String record_code) {
		String svcFlag = "";
		String[][] arr = UploadUtil.queryArrayFacade("QU007", report_id,
				record_code);
		if (arr != null && arr.length > 0) {
			svcFlag = arr[0][0];
		}
		return svcFlag;
	}

	/**
	 * 查询记录类型元数据配置信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getReportMetaInfo(String report_id, String record_code) {

		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QU008", report_id,
				record_code);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {
				UiMsuUpReportMetaInfoTable metaInfo = new UiMsuUpReportMetaInfoTable();

				metaInfo.setReport_id(arr[i][0].trim());
				metaInfo.setRecord_code(arr[i][1].trim());
				metaInfo.setCol_code(arr[i][2].trim());
				metaInfo.setFiled_type(arr[i][3].trim());
				metaInfo.setNew_title(arr[i][4].trim());
				metaInfo.setTitle_desc(arr[i][5].trim());
				metaInfo.setRef_map_type(arr[i][6].trim());
				metaInfo.setRef_field(arr[i][7].trim());
				metaInfo.setRef_con(arr[i][8].trim());
				metaInfo.setUnit(arr[i][9].trim());
				metaInfo.setDigit(arr[i][10].trim());
				metaInfo.setMsu_id(arr[i][11].trim());
				metaInfo.setCol_sequence(arr[i][12].trim());
				metaInfo.setFlag(arr[i][13].trim());
				list.add(metaInfo);
			}

		}
		return list;

	}

	/**
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getReportMetaInfo(String report_id) {
		ArrayList list = new ArrayList();
		String[][] arr = AdhocUtil.queryArrayFacade("QU009", report_id);
		if (arr != null && arr.length > 0) {

			for (int i = 0; i < arr.length; i++) {
				UiMsuUpReportMetaInfoTable metaInfo = new UiMsuUpReportMetaInfoTable();

				metaInfo.setReport_id(arr[i][0].trim());
				metaInfo.setRecord_code(arr[i][1].trim());
				metaInfo.setCol_code(arr[i][2].trim());
				metaInfo.setFiled_type(arr[i][3].trim());
				metaInfo.setNew_title(arr[i][4].trim());
				metaInfo.setTitle_desc(arr[i][5].trim());
				metaInfo.setRef_map_type(arr[i][6].trim());
				metaInfo.setRef_field(arr[i][7].trim());
				metaInfo.setRef_con(arr[i][8].trim());
				metaInfo.setUnit(arr[i][9].trim());
				metaInfo.setDigit(arr[i][10].trim());
				metaInfo.setMsu_id(arr[i][11].trim());
				metaInfo.setCol_sequence(arr[i][12].trim());
				metaInfo.setFlag(arr[i][13].trim());
				list.add(metaInfo);
			}

		}
		return list;
	}

}
