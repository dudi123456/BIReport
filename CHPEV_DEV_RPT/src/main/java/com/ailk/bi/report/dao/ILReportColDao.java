package com.ailk.bi.report.dao;

import java.util.List;

import com.ailk.bi.base.table.DimensionTable;
import com.ailk.bi.common.app.AppException;

@SuppressWarnings({ "rawtypes" })
public interface ILReportColDao {

	/**
	 * 获取报表列定义列表(展示用)
	 * 
	 * @param rpt_id
	 * @param expandcol
	 * @return
	 * @throws AppException
	 */
	public abstract List getReportCol(String rpt_id, String expandcol)
			throws AppException;

	/**
	 * 获取报表列定义列表(报表维护用)
	 * 
	 * @param rpt_id
	 * @return
	 * @throws AppException
	 */
	public abstract List getReportColDefine(String rpt_id) throws AppException;

	/**
	 * 获取自定义报表可选纬度信息
	 * 
	 * @param rpt_id
	 * @return
	 * @throws AppException
	 */
	public abstract DimensionTable[] getCustomRptDimTable(String rpt_id)
			throws AppException;

	/**
	 * 增加报表列定义
	 * 
	 * @param rptColTable
	 * @return
	 * @throws AppException
	 */
	public abstract void insertReportCol(List rptColTable) throws AppException;

	/**
	 * 删除指定报表id的列定义信息
	 * 
	 * @param rpt_id
	 * @throws AppException
	 */
	public abstract void delReportCol(String rpt_id) throws AppException;

	/**
	 * 删除指定报表id的数值列定义信息
	 * 
	 * @param rpt_id
	 * @throws AppException
	 */
	public abstract void delReportNumberCol(String rpt_id) throws AppException;

	/**
	 * 删除指定报表id的描述列定义信息
	 * 
	 * @param rpt_id
	 * @throws AppException
	 */
	public abstract void delReportCharCol(String rpt_id) throws AppException;
}
