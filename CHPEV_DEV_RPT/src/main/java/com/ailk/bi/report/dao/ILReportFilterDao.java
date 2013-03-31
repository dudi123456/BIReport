package com.ailk.bi.report.dao;

import java.util.List;

import com.ailk.bi.common.app.AppException;

@SuppressWarnings({ "rawtypes" })
public interface ILReportFilterDao {

	/**
	 * 获取报表的条件列表(展示用)
	 * 
	 * @param rpt_id
	 *            报表ID
	 * @return
	 * @throws AppException
	 */
	public abstract List getReportFilter(String rpt_id) throws AppException;

	/**
	 * 获取报表的条件列表(报表维护用)
	 * 
	 * @param rpt_id
	 * @return
	 * @throws AppException
	 */
	public abstract List getReportFilterDefine(String rpt_id)
			throws AppException;

	/**
	 * 添加报表条件定义
	 * 
	 * @param rptFilterTable
	 * @return
	 * @throws AppException
	 */
	public abstract void insertReportFilter(List rptFilterTable)
			throws AppException;

	/**
	 * 删除报表条件定义
	 * 
	 * @param rpt_id
	 * @return
	 * @throws AppException
	 */
	public abstract void delReportFilter(String rpt_id) throws AppException;

}
