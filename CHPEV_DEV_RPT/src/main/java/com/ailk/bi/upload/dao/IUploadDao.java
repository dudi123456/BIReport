package com.ailk.bi.upload.dao;

import java.util.List;

import com.ailk.bi.upload.domain.UiMsuUpReportConfigTable;

@SuppressWarnings({ "rawtypes" })
public interface IUploadDao {
	// 提取报表信息
	UiMsuUpReportConfigTable getReportInfo(String report_id);

	// 取得报表默认第一个记录类型
	String getFirstRecord(String report_id);

	// 根据报表标识获取最大月份
	String getMaxMonthByReportId(String report_id);

	// 根据报表标识和记录类型获取最大月份
	String getMaxMonthByReportIdAndRecord(String report_id, String record);

	// 根据报表标识获取最大日期
	String getMaxDayByReportId(String report_id);

	// 根据报表标识和记录类型获取最大日期
	String getMaxDayByReportIdAndRecord(String report_id, String record);

	// 业务类型控制标志（业务类型权限）
	String getRecordSvcFlag(String report_id, String record_code);

	// 提取报表记录类型的元数据配置
	List getReportMetaInfo(String report_id, String record_code);

	// 提取报表记录类型的元数据配置
	List getReportMetaInfo(String report_id);

}
