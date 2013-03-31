package com.ailk.bi.report.dao;

import java.util.List;

import com.ailk.bi.common.app.AppException;

@SuppressWarnings({ "rawtypes" })
public interface ILReportProcessDao {

	/**
	 * 根据流程ID获取流程定义信息
	 * 
	 * @param p_id
	 * @return
	 * @throws AppException
	 */
	public abstract Object getProcess(String p_id) throws AppException;

	/**
	 * 获取流程基本定义列表
	 * 
	 * @param whereStr
	 * @return
	 * @throws AppException
	 */
	public abstract List getProcesses(String whereStr) throws AppException;

	/**
	 * 添加流程基本定义
	 * 
	 * @param RptProcessTable
	 * @throws AppException
	 */
	public abstract void insertProcess(Object RptProcessTable)
			throws AppException;

	/**
	 * 修改流程基本定义
	 * 
	 * @param RptProcessTable
	 * @throws AppException
	 */
	public abstract void updateProcess(Object RptProcessTable)
			throws AppException;

	/**
	 * 修改流程基本定义
	 * 
	 * @param p_id
	 * @throws AppException
	 */
	public abstract void delProcess(String p_id) throws AppException;

	/**
	 * 获取指定流程ID的流程步骤定义
	 * 
	 * @param p_id
	 * @return
	 * @throws AppException
	 */
	public abstract List getProcessStep(String p_id) throws AppException;

	/**
	 * 添加流程步骤信息
	 * 
	 * @param RptProcessStepTable
	 * @return
	 * @throws AppException
	 */
	public abstract void insertProcessStep(List RptProcessStepTable)
			throws AppException;

	/**
	 * 删除指定流程ID的步骤信息
	 * 
	 * @param p_id
	 * @throws AppException
	 */
	public abstract void delProcessStep(String p_id) throws AppException;

	/**
	 * 获取指定条件下的报表审核流程关系
	 * 
	 * @param whereStr
	 * @return
	 * @throws AppException
	 */
	public abstract List getReportProcess(String whereStr) throws AppException;

	/**
	 * 添加报表流程对应关系
	 * 
	 * @param p_id
	 * @param reportProcess
	 * @param reportCheck
	 * @throws AppException
	 */
	public abstract void insertReportProcess(String p_id, List reportProcess,
			String[] reportCheck) throws AppException;

	/**
	 * 获取指定报表的审核流程ID
	 * 
	 * @param rpt_id
	 * @return
	 * @throws AppException
	 */
	public abstract String getReportProcessID(String rpt_id)
			throws AppException;

	/**
	 * 获取指定报表指定周期的审核流程步骤信息
	 * 
	 * @param rptTable
	 * @param rpt_date
	 * @return
	 * @throws AppException
	 */
	public abstract List getReportProcessStep(Object rptTable, String rpt_date)
			throws AppException;

	/**
	 * 获取指定报表指定周期的审核历史记录
	 * 
	 * @param rpt_id
	 * @param rpt_date
	 * @param region_id
	 * @return
	 * @throws AppException
	 */
	public abstract List getReportProcessHis(String rpt_id, String rpt_date,
			String region_id) throws AppException;

	/**
	 * 添加报表审核信息
	 * 
	 * @param rptHisTable
	 * @throws AppException
	 */
	public abstract void insertReportProcessHis(Object RptHisTable)
			throws AppException;

	/**
	 * 添加报表发布关系
	 * 
	 * @param role_id
	 * @param RptTable
	 * @param reportCheck
	 * @throws AppException
	 */
	public abstract void insertReportDispense(String role_id, Object RptTable,
			String[] reportCheck) throws AppException;
}
