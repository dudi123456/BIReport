package com.ailk.bi.report.dao;

import com.ailk.bi.common.app.AppException;

public interface ILReportSelectDataDao {

	public String getListItem(String strSql) throws AppException;
}
