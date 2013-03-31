package com.ailk.bi.report.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.ailk.bi.common.app.AppException;

@SuppressWarnings({ "rawtypes" })
public interface ILReportDao {

	/**
	 * 获取自定义报表的ID序列
	 *
	 * @return
	 * @throws AppException
	 */
	public abstract String getSelfReportID() throws AppException;

	/**
	 * 获取指定报表ID的报表结构
	 *
	 * @param reportId
	 * @return
	 * @throws AppException
	 */
	public abstract Object getReport(String rpt_id) throws AppException;

	/**
	 * 获取指定条件下的所有报表
	 *
	 * @param whereStr
	 * @return
	 * @throws AppException
	 */
	public abstract List getReports(String whereStr) throws AppException;

	/**
	 * 获取可发布的报表列表，带是否发布的角色信息
	 *
	 * @param whereStr
	 * @param role_id
	 * @return
	 * @throws AppException
	 */
	public abstract List getRoleReports(String whereStr, String role_id) throws AppException;

	/**
	 * 添加新报表
	 *
	 * @param rptTable
	 * @return
	 */
	public abstract void insertReport(Object rptTable) throws AppException;

	/**
	 * 更新报表信息
	 *
	 * @param rptTable
	 * @return
	 */
	public abstract void updateReport(Object rptTable) throws AppException;

	/**
	 * 删除报表信息
	 *
	 * @param rpt_id
	 * @throws AppException
	 */
	public abstract void delReport(String rpt_id) throws AppException;

	/**
	 * 获取条件定制的所有报表
	 *
	 * @param whereStr
	 * @return
	 * @throws AppException
	 */
	public abstract List getConditionReports(String con_id, String whereStr) throws AppException;

	/**
	 * 视图树的报表列表
	 *
	 * @param whereStr
	 * @return
	 * @throws AppException
	 */
	public abstract String[][] getViewTreeList(String resId, String resType, String rptId,
			String rptName) throws AppException;

	/**
	 * 视图树的报表列表
	 *
	 * @param whereStr
	 * @return
	 * @throws AppException
	 */
	public abstract String[][] getViewTreeList(HttpSession session, String resId, String resType,
			String rptId, String rptName, String tabType) throws AppException;

	/**
	 * 获取报表的图形信息
	 *
	 * @param rpt_id
	 * @return
	 * @throws AppException
	 */
	public abstract List getReportCharts(String rpt_id) throws Exception;

}
