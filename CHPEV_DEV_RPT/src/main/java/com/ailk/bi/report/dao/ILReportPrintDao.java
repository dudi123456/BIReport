package com.ailk.bi.report.dao;

import java.util.List;

import com.ailk.bi.common.app.AppException;

@SuppressWarnings({ "rawtypes" })
public interface ILReportPrintDao {

	/**
	 * 获取报表打印信息
	 * 
	 * @param whereStr
	 * @return
	 * @throws AppException
	 */
	public abstract List getReportPrint(String whereStr) throws AppException;

	/**
	 * 添加报表打印信息
	 * 
	 * @param rptTable
	 * @throws AppException
	 */
	public abstract void insertReportPrint(Object rptTable) throws AppException;
}
