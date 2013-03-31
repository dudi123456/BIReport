package com.ailk.bi.content.service;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.report.struct.ReportQryStruct;

public interface ICustContentHTML {

	/**
	 * 获取指定ID的内容信息
	 *
	 * @param contentID
	 * @param whereSql
	 * @return
	 * @throws AppException
	 */
	public abstract String getContentDesc(String contentID, String whereSql,
			String whereSqlnoDate, ReportQryStruct qryStruct)
			throws AppException;
}
