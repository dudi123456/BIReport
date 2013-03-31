package com.ailk.bi.report.dao;

import com.ailk.bi.common.app.AppException;

public interface ILReportBaseDateDao {

	/**
	 * 获取基础数据
	 * 
	 * @param rpt_id
	 * @param expandcol
	 * @return
	 * @throws AppException
	 */
	public abstract String getBaseDate(String optTypeId) throws AppException;
}