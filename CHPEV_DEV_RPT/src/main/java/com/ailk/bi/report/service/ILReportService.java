package com.ailk.bi.report.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.ailk.bi.base.exception.ReportHeadException;
import com.ailk.bi.base.table.DimensionTable;
import com.ailk.bi.base.table.PubInfoConditionTable;
import com.ailk.bi.common.app.AppException;

/**
 * @author Administrator
 *
 */
@SuppressWarnings({ "rawtypes" })
public interface ILReportService {

	/**
	 * 获取自定义报表的ID序列
	 *
	 * @return
	 * @throws AppException
	 */
	public abstract String getSelfReportID() throws AppException;

	/**
	 * 是否存在指定报表ID的报表
	 *
	 * @param rpt_id
	 * @return
	 */
	public abstract boolean existReport(String rpt_id);

	/**
	 * 指定报表在指定周期上是否是审核报表
	 *
	 * @param RptTable
	 * @param rpt_date
	 * @return
	 */
	public abstract boolean processReport(Object RptTable, String rpt_date);

	/**
	 * 获取指定报表ID的报表对象
	 *
	 * @param rpt_id
	 * @return
	 */
	public abstract Object getReport(String rpt_id) throws AppException;

	/**
	 * 获取指定条件的所有报表
	 *
	 * @param whereStr
	 * @return
	 * @throws AppException
	 */
	public abstract List getReports(String whereStr) throws AppException;

	/**
	 * 获取条件定制的所有报表
	 *
	 * @param whereStr
	 * @return
	 * @throws AppException
	 */
	public abstract List getConditionReports(String con_id, String whereStr)
			throws AppException;

	/**
	 * 获取可发布的报表列表，带是否发布的角色信息
	 *
	 * @param whereStr
	 * @param role_id
	 * @return
	 * @throws AppException
	 */
	public abstract List getRoleReports(String whereStr, String role_id)
			throws AppException;

	/**
	 * 添加新报表
	 *
	 * @param RptTable
	 * @return
	 */
	public abstract void insertReport(Object RptTable) throws AppException;

	/**
	 * 更新报表信息
	 *
	 * @param RptTable
	 * @return
	 */
	public abstract void updateReport(Object RptTable) throws AppException;

	/**
	 * 删除报表信息
	 *
	 * @param rpt_id
	 * @throws AppException
	 */
	public abstract void delReport(String rpt_id) throws AppException;

	/**
	 * 获取指定报表ID得所有列定义(展示用)
	 *
	 * @param rpt_id
	 * @param expandcol
	 * @return
	 * @throws AppException
	 */
	public abstract List getReportCol(String rpt_id, String expandcol)
			throws AppException;

	/**
	 * 获取指定报表ID得所有列定义(报表维护用)
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

	/**
	 * 获取指定报表ID得所有条件定义(展示用)
	 *
	 * @param rpt_id
	 * @return
	 * @throws AppException
	 */
	public abstract List getReportFilter(String rpt_id) throws AppException;

	/**
	 * 获取指定报表ID得所有条件定义(报表维护用)
	 *
	 * @param rpt_id
	 * @return
	 * @throws AppException
	 */
	public abstract List getReportFilterDefine(String rpt_id)
			throws AppException;

	/**
	 * 增加报表条件定义
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
	 * @throws AppException
	 */
	public abstract void delReportFilter(String rpt_id) throws AppException;

	/**
	 * 获取报表HTML表头代码(展示用)
	 *
	 * @param rpt_id
	 * @return
	 * @throws AppException
	 */
	public abstract String getReportHead(String rpt_id);

	/**
	 * 获取报表HTML表头代码(报表维护用)
	 *
	 * @param rpt_id
	 * @return
	 * @throws ReportHeadException
	 */
	public abstract String getReportHeadDefine(String rpt_id)
			throws ReportHeadException;

	/**
	 * 添加新的表头信息
	 *
	 * @param rpt_id
	 * @param rptHead
	 * @throws ReportHeadException
	 */
	public abstract void insertRptHead(String rpt_id, String rptHead)
			throws ReportHeadException;

	/**
	 * 删除报表表头
	 *
	 * @param rpt_id
	 * @throws ReportHeadException
	 */
	public abstract void deleteRptHead(String rpt_id)
			throws ReportHeadException;

	/**
	 * 获取报表的数据
	 *
	 * @param RptTable
	 * @param rptCols
	 * @param qryStruct
	 * @return
	 * @throws AppException
	 */
	public abstract String[][] getReportData(Object RptTable, List rptCols,
			Object qryStruct, PubInfoConditionTable[] cdnTables)
			throws AppException;

	/**
	 * 是否存在该流程定义名称
	 *
	 * @param p_name
	 * @return
	 */
	public abstract boolean existProcessName(String p_name);

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
	 * @param RptTable
	 * @param rpt_date
	 * @return
	 * @throws AppException
	 */
	public abstract List getReportProcessStep(Object RptTable, String rpt_date)
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

	/**
	 * 获取报表打印信息
	 *
	 * @param whereSql
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

	/**
	 * 视图树的报表列表
	 *
	 * @param whereStr
	 * @return
	 * @throws AppException
	 */
	public abstract String[][] getViewTreeList(String resId, String resType,
			String rptId, String rptName) throws AppException;

	/**
	 * 视图树的报表列表
	 *
	 * @param whereStr
	 * @return
	 * @throws AppException
	 */
	public abstract String[][] getViewTreeList(HttpSession session,
			String resId, String resType, String rptId, String rptName,
			String tabType) throws AppException;

	public String getBaseVo(String optTypeId) throws AppException;

	/**
	 * 获取报表图形列表
	 *
	 * @param rpt_id
	 * @return
	 * @throws Exception
	 */
	public abstract List getReportCharts(String rpt_id) throws Exception;
}
